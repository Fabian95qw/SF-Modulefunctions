package nucom.module.modulefunction;

import java.util.List;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.Module;
import de.vertico.starface.module.core.model.ModuleInstance;
import de.vertico.starface.module.core.model.ModuleInstanceProject;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Default")
public class ToggleModuleInstancebyUUID implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="InstanceUUID", description="",type=VariableType.STRING)
	public String InstanceUUID="";

	@OutputVar(label="Disabled", description="Outputs if the Module is now disabled",type=VariableType.BOOLEAN)
	public boolean Disabled = false;

    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
			
		if(InstanceUUID.isEmpty())
		{
			log.debug("Empty UUID Supplied");
			return;
		}
		
		ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);
				
		List<Module> Modules = MR.getModules();
		
		List<ModuleInstance> ModuleInstances = null;
		
		for(Module M: Modules)
		{
			ModuleInstances  = MR.getInstances4Module(M.getId());
			//log.debug("Working on: " + M.getName() +" ==> " + M.getId());
			for(ModuleInstance MI: ModuleInstances)
			{
				//log.debug(MI.getId() + " <==> " + InstanceUUID);
				
				if(MI.getId().equals(InstanceUUID) || MI.getModuleId().equals(InstanceUUID))
				{
					//log.debug(MI.getName() + "==> "+ MI.getId() + " <==> " + InstanceUUID);
					ModuleInstanceProject MIP = MR.getInstance4Edit(MI.getId());
					Disabled = !MIP.getObject().getDisabled();
					MR.activateModuleInstance(MIP, !Disabled);
					MR.updateModuleInstance(MIP);
				}
			}
		}
		
	}//END OF EXECUTION

	
}
