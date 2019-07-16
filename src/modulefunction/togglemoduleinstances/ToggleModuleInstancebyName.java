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
public class ToggleModuleInstancebyName implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="InstanceName", description="",type=VariableType.STRING)
	public String InstanceName="";

	@OutputVar(label="Disabled", description="Outputs if the Module is now disabled",type=VariableType.BOOLEAN)
	public boolean Disabled = false;

    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
			
		if(InstanceName.isEmpty())
		{
			log.debug("Empty InstanceName Supplied");
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
				//log.debug(MI.getName() + "==> "+ MI.getId() + " <==> " + InstanceUUID);
				if(MI.getName().equals(InstanceName))
				{
					//log.debug(MI.getName() + " <==> " + InstanceName);
					ModuleInstanceProject MIP = MR.getInstance4Edit(MI.getId());
					Disabled = !MIP.getObject().getDisabled();
					MR.activateModuleInstance(MIP, !Disabled);
					MR.updateModuleInstance(MIP);
				}
			}
		}
		
	}//END OF EXECUTION

	
}
