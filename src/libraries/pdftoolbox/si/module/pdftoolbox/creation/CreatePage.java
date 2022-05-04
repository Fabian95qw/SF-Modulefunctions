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
import si.module.pdftoolbox.utility.EnumHelper.PageSize;

@Function(name="[PDF] Create new Page",visibility=Visibility.Public, rookieFunction=false, description="")
public class CreatePage implements IBaseExecutable 
{
	//##########################################################################################
		
	@InputVar(label="PageSize", description="", valueByReferenceAllowed = true)
	public PageSize PS = PageSize.A4;
		  	
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
		log.debug("Creating Page with Size:" + PS.Size);
		PDPage P = new PDPage(PS.Size);
		Page = P;
		log.debug("Page Created!");
		
		Success=true;
	}//END OF EXECUTION


	
	
}
