package si.module.pdftoolbox.creation;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.pdmodel.PDPage;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.pdftoolbox.pdfobject.PDFObject;

@Function(name="[PDF] Get Page of an existing PDF",visibility=Visibility.Public, rookieFunction=false, description="")
public class GetPage implements IBaseExecutable 
{
	//##########################################################################################
		
	@InputVar(label="PDF", description="",type=VariableType.OBJECT)
	public Object PDFO = null;
	
	@InputVar(label="PageNumber", description="", type=VariableType.NUMBER)
	public Integer PageNumber=1;
		  	
	@OutputVar(label="Page", description="Represents a new Page. This needs to be added to a PDFObject with [PDF] Add Page to PDF",type=VariableType.OBJECT)
	public Object Page = null;
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		log.debug("Trying to get Page: " + PageNumber +" for PDF...");
		
		if (!(PDFO instanceof PDFObject))
		{
			log.error("PDFObject is invalid!");
			return;
		}
		
		PDFObject PDF = (PDFObject) PDFO;
		PDPage SelectedPage = PDF.getDocument().getPage(PageNumber);
		
		if(SelectedPage == null)
		{
			log.error("Page with Number: "+ PageNumber + " does not exist");
			return;
		}
		
		Page = SelectedPage;
		
		Success=true;
	}//END OF EXECUTION


	
	
}
