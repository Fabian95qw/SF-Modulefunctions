package si.module.examples.moduleinstances;

import java.util.List;

import org.apache.logging.log4j.Logger;


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

@Function(visibility = Visibility.Private,  description = "")
public class EnableDisableModuleInstancebyName implements IBaseExecutable
{
//##########################################################################################

	@InputVar(label = "InstanceName", description = "", type = VariableType.STRING)
	public String InstanceName = "";

	@InputVar(label = "Disable", description = "If Instance should be Disabled", type = VariableType.BOOLEAN)
	public Boolean Disable = false;

	
	// ##########################################################################################

//###################      Code Execution      ############################
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception
	{
		Logger log = context.getLog();

		if (InstanceName.isEmpty()) // If no instancename supplied
		{
			log.debug("No Instance Name Supplied");
			return;
		}

		ModuleRegistry MR = (ModuleRegistry) context.springApplicationContext().getBean(ModuleRegistry.class); // Fetch Moduleregistry

		List<Module> Modules = MR.getModules(); // Fetch all currently installed Modules

		List<ModuleInstance> ModuleInstances = null;

		for (Module M : Modules) // For each Module
		{
			ModuleInstances = MR.getInstances4Module(M.getId()); // Fetch all Instances
			for (ModuleInstance MI : ModuleInstances) // For each Instance
			{
				log.debug(MI.getName() + " <==> " + InstanceName);
				if (MI.getName().equals(InstanceName)) // Check if name Machtes
				{
					log.debug(MI.getName() + "==> " + MI.getId() + " <==> " + InstanceName);
					ModuleInstanceProject MIP = MR.getInstance4Edit(MI.getId()); // Get Instance as editable instance
					MR.activateModuleInstance(MIP, !Disable); // Set Enabled Boolean
					MR.updateModuleInstance(MIP); // Save Changes to instance
					break; // Break, because Instancenames are Unique
				}
			}
		}
	}// END OF EXECUTION
}