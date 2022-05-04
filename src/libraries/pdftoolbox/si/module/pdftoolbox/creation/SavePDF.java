package si.module.pdftoolbox.creation;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.pdftoolbox.pdfobject.PDFObject;

@Function(name="[PDF] Save PDF to File",visibility=Visibility.Public, rookieFunction=false, description="")
public class SavePDF implements IBaseExecutable 
{
	//##########################################################################################
		
	@InputVar(label="TargetFile", description="" ,type=VariableType.STRING)
	public String TargetFile="/tmp/export.pdf";
		  	
	@InputVar(label="PDF", description="",type=VariableType.OBJECT)
	public Object PDFO = null;
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		
		log.debug("Saving PDF File...");
		
		if (!(PDFO instanceof PDFObject))
		{
			log.error("PDFObject is invalid!");
			return;
		}
		
		
		log.debug("PDF Saved!");
		PDFObject PDF = (PDFObject) PDFO;
		PDF.save(TargetFile);
		
		Success=true;
	}//END OF EXECUTION


	
	
}
