package si.module.guichanges;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.ModuleInstanceProject;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;


@Function(visibility=Visibility.Private, rookieFunction=false, description="Save Changes to the ModuleInstanceProject")
public class SaveChanges implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="ModuleInstanceProject", description="The Object returned by GetModuleInstance4Edit",type=VariableType.OBJECT)
	public Object MIPObject=null;
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public boolean Success=false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		if(MIPObject == null)
		{
			log.debug("ModuleInstanceProject is null!");
			return;
		}
		
		ModuleInstanceProject MIP = (ModuleInstanceProject) MIPObject;
		ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);
		
		try 
		{
			log.debug("Updating Instance " + MIP.getObject().getModuleName() +" " + MIP.getObject().getId());
			SaveChangesHelper.AddChange(MIP.getObject().getId(), log);
			MR.updateModuleInstance(MIP);
			Success=true;
		}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.debug(sw.toString()); //
		}
		
	}//END OF EXECUTION

	
}
