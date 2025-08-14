package si.module.pdftoolbox.creation;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.pdftoolbox.pdfobject.PDFObject;

@Function(name="[PDF] Add Image to Page",visibility=Visibility.Public, rookieFunction=false, description="")
public class AddImage implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="PDF", description="The PDFObject to edit",type=VariableType.OBJECT)
	public Object PDFO = null;
	
	@InputVar(label="Page", description="The Page to add the Image to",type=VariableType.OBJECT)
	public Object PO = null;
	
	@InputVar(label="Path to Image", description="Full path to the Image",type=VariableType.STRING)
	public String ImagePath="";
			
	@InputVar(label="Width", description="Set Width. 0 == Original",type=VariableType.NUMBER)
	public Integer Width = 0;
	
	@InputVar(label="Height", description="Set Height 0 == Original",type=VariableType.NUMBER)
	public Integer Height = 0;
	
	@InputVar(label="Offset X", description="",type=VariableType.NUMBER)
	public Float OffsetX = 0f;
	
	@InputVar(label="Offset Y", description="",type=VariableType.NUMBER)
	public Float OffsetY = 0f;
		
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
		


		log.debug("Loading Image from: "+ ImagePath);
		PDImageXObject Image = PDImageXObject.createFromFile(ImagePath, PDF.getDocument());
		if(Width > 0)
		{
			Image.setWidth(Width);
		}
		if(Height > 0 )
		{
			Image.setHeight(Height);
		}
		log.debug("Trying to Add Image");
		PDPageContentStream ContentStream = new PDPageContentStream(PDF.getDocument(), Page, AppendMode.APPEND, false);
		ContentStream.drawImage(Image, OffsetX, OffsetY);
		ContentStream.close();
		log.debug("Image was added!");
		Success=true;
	}//END OF EXECUTION


	
	
}
