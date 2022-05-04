package si.module.pdftoolbox.creation;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.pdftoolbox.pdfobject.PDFObject;
import si.module.pdftoolbox.utility.ColorConverter;

@Function(name="[PDF] Add Textfield to Page",visibility=Visibility.Public, rookieFunction=false, description="")
public class AddTextField implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="PDF", description="",type=VariableType.OBJECT)
	public Object PDFO = null;
	
	@InputVar(label="Page", description="",type=VariableType.OBJECT)
	public Object PO = null;
	
	@InputVar(label="Fieldname", description="The Fieldname for this Field.",type=VariableType.STRING)
	public String Fieldname="";
		
	@InputVar(label="Text", description="Text to write into the Field",type=VariableType.STRING)
	public String Text="";

	@InputVar(label="Width", description="",type=VariableType.NUMBER)
	public Float Width = 0f;
	
	@InputVar(label="Height", description="",type=VariableType.NUMBER)
	public Float Height = 0f;
	
	@InputVar(label="Offset X", description="",type=VariableType.NUMBER)
	public Float OffsetX = 0f;
	
	@InputVar(label="Offset Y", description="",type=VariableType.NUMBER)
	public Float OffsetY = 0f;
	
	@InputVar(label="Bordercolor", description="The Color RGB[0-255] Example: 255,255,255",type=VariableType.STRING)
	public String Bordercolor="255,255,255";
	
	@InputVar(label="Backgroundcolor", description="The Color RGB[0-255] Example: 255,255,255",type=VariableType.STRING)
	public String Backgroundcolor="128,128,128";
		  	
	@InputVar(label="Allow Editing of Textfield", description="",type=VariableType.BOOLEAN)
	public Boolean isWritable=true;
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();

		if (!(PDFO instanceof PDFObject))
		{
			log.error("PDFObject is invalid!");
			return;
		}
		
		if (!(PO instanceof PDPage))
		{
			log.error("Page is invalid!");
			return;
		}

		PDFObject PDF = (PDFObject) PDFO;
		PDPage Page = (PDPage) PO;
		
		log.debug("Trying to Create Textfield");

		PDTextField TextField= new PDTextField(PDF.getForm());
		TextField.setPartialName(Fieldname);
		TextField.setValue(Text);
		TextField.setReadOnly(!isWritable);
		PDRectangle Rectangle = new PDRectangle(OffsetX, OffsetY, Width, Height);
		
		log.debug("Converting Colors...");
        PDAppearanceCharacteristicsDictionary Appearance = new PDAppearanceCharacteristicsDictionary(new COSDictionary()); //TODO Implement
        Appearance.setBorderColour(ColorConverter.ConverttoPDColor(Bordercolor, log));
        Appearance.setBackground(ColorConverter.ConverttoPDColor(Backgroundcolor, log));

		PDF.getForm().getFields().add(TextField);
        PDAnnotationWidget Widget = new PDAnnotationWidget();
        Widget.setRectangle(Rectangle);
        Widget.setPage(Page);
        Widget.setParent(TextField);
		Widget.setPrinted(true);
		Widget.setAppearanceCharacteristics(Appearance);
		TextField.setWidgets(List.of(Widget));
        Page.getAnnotations().add(Widget);
        
		log.debug("Textfield Sucessfully created!");
		
		
		Success=true;
	}//END OF EXECUTION


	
	
}
