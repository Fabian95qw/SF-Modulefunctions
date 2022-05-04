package si.module.pdftoolbox.creation;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDMMType1Font;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.pdftoolbox.pdfobject.PDFObject;

@Function(name="[PDF] Load existing PDF",visibility=Visibility.Public, rookieFunction=false, description="")
public class LoadPDFObject implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="Sourcefile", description="The PDF Source",type=VariableType.STRING)
	public String Sourcefile="";
	
	@OutputVar(label="PDF", description="Represents a PDF. Is used with other Buildingsblocks",type=VariableType.OBJECT)
	public Object PDF = null;
		  	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		log.debug("Loading PDFObject from. " + Sourcefile);
		
		File Source = new File(Sourcefile);
		if(!Source.exists())
		{
			String Errormessage = "Source file "+Sourcefile+" does not exist!";
			log.error(Errormessage);
			return;
		}
		
		PDDocument Document = null;
		
		try
		{
			Document = PDDocument.load(Source);
		}
		catch (IOException e) 
		{
			String Errormessage ="Error while loading PDF: " + e.getMessage();
			log.error(Errormessage);
			return;
		}
		
		
		PDFObject PDFO = new PDFObject(Document, "/Helv 0 Tf 0 g", COSName.HELV, PDMMType1Font.HELVETICA);
		PDF = PDFO;
		log.debug("PDFObject Created!");
		
		Success=true;
	}//END OF EXECUTION


	
	
}
