package si.module.pdftoolbox.creation;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDMMType1Font;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.pdftoolbox.pdfobject.PDFObject;

@Function(name="[PDF] Create PDF",visibility=Visibility.Public, rookieFunction=false, description="")
public class CreatePDFObject implements IBaseExecutable 
{
	//##########################################################################################
	
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
		log.debug("Creating PDFObject..");
		PDFObject PDFO = new PDFObject("/Helv 0 Tf 0 g", COSName.HELV, PDMMType1Font.HELVETICA);
		PDF = PDFO;
		log.debug("PDFObject Created!");
		
		Success=true;
	}//END OF EXECUTION


	
	
}
