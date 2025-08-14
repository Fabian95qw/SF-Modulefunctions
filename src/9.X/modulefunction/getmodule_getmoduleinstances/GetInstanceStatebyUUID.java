package si.module.modulefunction;

import org.apache.logging.log4j.Logger;

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

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class GetInstanceStatebyUUID implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="UUID", description="",type=VariableType.STRING)
	public String UUID="";
	
	@OutputVar(label="Disabled", description="Returns if the Module is Disabled. True == Disabled, False == Enabled",type=VariableType.BOOLEAN)
	public boolean Disabled = false;

    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);
						
		ModuleInstanceProject MIP = MR.getInstance4Edit(UUID);
		Disabled = MIP.getObject().getDisabled();
		
		log.debug(UUID + " ==> " + Disabled);

		
	}//END OF EXECUTION

	
}
