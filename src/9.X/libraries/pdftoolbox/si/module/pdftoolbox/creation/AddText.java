package si.module.pdftoolbox.creation;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;

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
import si.module.pdftoolbox.utility.EnumHelper.Fonts;

@Function(name="[PDF] Add Text to Page",visibility=Visibility.Public, rookieFunction=false, description="")
public class AddText implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="PDF", description="",type=VariableType.OBJECT)
	public Object PDFO = null;
	
	@InputVar(label="Page", description="",type=VariableType.OBJECT)
	public Object PO = null;
	
	@InputVar(label="Text", description="",type=VariableType.STRING)
	public String Text="";
	
	@InputVar(label="Font", description="", valueByReferenceAllowed = true)
	public Fonts F = Fonts.HELVETICA;
	
	@InputVar(label="Font Size", description="",type=VariableType.NUMBER)
	public Integer Size = 12;
	
	@InputVar(label="Font Color", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String FontColor="0,0,0";
	
	@InputVar(label="Offset X", description="",type=VariableType.NUMBER)
	public Float X = 0f;
	
	@InputVar(label="Offset Y", description="",type=VariableType.NUMBER)
	public Float Y = 0f;
		  	
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
		
		log.debug("Trying to Append Text");
		PDPageContentStream ContentStream = new PDPageContentStream(PDF.getDocument(), Page, AppendMode.APPEND, false);
		ContentStream.beginText();
		ContentStream.newLineAtOffset(X, Y);
		ContentStream.setFont(F.Font, Size);
		ContentStream.setNonStrokingColor(ColorConverter.ConverttoAWTColor(FontColor, log));
		ContentStream.setStrokingColor(ColorConverter.ConverttoAWTColor(FontColor, log));
		ContentStream.showText(Text);
		ContentStream.endText();
		ContentStream.close();
		log.debug("Text Sucessfully appended!");
		
		
		Success=true;
	}//END OF EXECUTION


	
	
}
