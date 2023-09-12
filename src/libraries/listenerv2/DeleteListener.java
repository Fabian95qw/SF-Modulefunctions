package si.module.listenerinterface.listenerv2;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.listenerinterface.listenerv2.utility.LogHelper;

@Function(visibility=Visibility.Public, rookieFunction=false, description="")
public class DeleteListener implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="Listener_UUID", description="Listener UUID to delete",type=VariableType.STRING)
	public String Listener_UUID="";
	
	@OutputVar(label="Success", description="Returns True if Listener was deleted. Returns false if listener did not exist",type=VariableType.BOOLEAN)
	public boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		
		ListenerManager LM = (ListenerManager)context.provider().fetch(ListenerManager.class);	

		if(!LM.isRunning())
		{
			try 
			{
				LM.startComponent();
			} 
			catch (Throwable e) 
			{
				LogHelper.EtoStringLog(log, e);
			}
		}
		
		if(LM.getInstance(Listener_UUID) == null)
		{
			Success = false;
		}
		else
		{
			Success = LM.removeListener(Listener_UUID, context);
		}
	}//END OF EXECUTION

	
}
