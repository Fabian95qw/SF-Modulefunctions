package si.module.modulefunction;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.ModuleInstance;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class GetModuleInstances implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="UUID", description="",type=VariableType.STRING)
	public String UUID="";
	
	@OutputVar(label="Moduledata <UUID, Name>", description="Returns all Moduleinstances for the Module UUID",type=VariableType.MAP)
	public Map<String, String> Instances  = new HashMap<String, String>();

    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();

		ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);
						
		for(ModuleInstance MIS: MR.getInstances4Module(UUID))
		{
			log.debug(MIS.getId() +" ==> " + MIS.getName());
			Instances.put(MIS.getId(), MIS.getName());
		}

		
	}//END OF EXECUTION

	
}
