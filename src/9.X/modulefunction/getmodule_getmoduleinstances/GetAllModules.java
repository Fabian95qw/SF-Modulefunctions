package si.module.modulefunction;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.Module;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class GetAllModules implements IBaseExecutable 
{
	//##########################################################################################
	
	@OutputVar(label="Moduledata <UUID, Name>", description="Returns all Installed Modules (NOT INSTANCES, with their UUID and Display Name)",type=VariableType.MAP)
	public Map<String, String> Modules  = new HashMap<String, String>();

    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);
		
		for(Module M : MR.getModules())
		{
			log.debug(M.getId() +" ==> " + M.getName());
			Modules.put(M.getId(), M.getName());
		}
		
	}//END OF EXECUTION

	
}
