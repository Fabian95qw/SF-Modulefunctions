package si.module.moduleconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.Module;
import de.vertico.starface.module.core.model.ModuleInstance;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

import java.util.List;

import java.util.ArrayList;

@Function(visibility=Visibility.Private, description="")
public class GetModules implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar (label="ModuleFilter", description="",type=VariableType.LIST)
	public List<String> ModuleFilter = new ArrayList<String>();
	
	@InputVar (label="InstanceFilter", description="",type=VariableType.LIST)
	public List<String> InstanceFilter = new ArrayList<String>();
	
	@OutputVar(label="Modules", description="",type=VariableType.MAP)
	public Map<String, Object> Modules = new HashMap<String, Object>();
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		log.debug("Extracting Modules");
		
		ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);		
    	
		for(Module M : MR.getModules())
		{
			if(!ModuleFilter.isEmpty() && !ModuleFilter.contains(M.getName()))
			{
				continue;
			}
			
			Map<String, String> Instances =new HashMap<String, String>();
			for(ModuleInstance MIS: MR.getInstances4Module(M.getId()))
			{
				if(!InstanceFilter.isEmpty() && !InstanceFilter.contains(MIS.getName()))
				{
					continue;
				}
				
				Instances.put(MIS.getId(), MIS.getName());
			};
			Modules.put(M.getId(), Instances);
		}
		
		log.debug("Total Modules: "+ Modules.size());
		
	}//END OF EXECUTION

}
