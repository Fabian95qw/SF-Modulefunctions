package si.module.guichanges;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Checks if the current Instance Changes is triggered by SaveChanges")
public class IsEntryPointTriggeredbySaveChanges implements IBaseExecutable 
{
	//##########################################################################################
	
	@OutputVar(label="IsTriggeredbySaveChanges", description="Returns true if this Instance Changed was triggered by SaveChanges",type=VariableType.BOOLEAN)
	public Boolean IsTriggeredbySaveChanges=false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		IsTriggeredbySaveChanges = SaveChangesHelper.IsTriggeredinTime(context.getInvocationInfo().getModuleInstance().getId(), log);
		
	}//END OF EXECUTION

	
}
