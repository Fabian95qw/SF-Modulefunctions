package nucom.module.msgraphs;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import nucom.module.msgraphs.o365provider.O365Provider;
import nucom.module.msgraphs.utility.LogHelper;

@Function(visibility=Visibility.Public, rookieFunction=false, description="Returns first UserPage (Resolve to Access Users)")
public class GetUserPage implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="O365Provider", description="Office365 Provider",type=VariableType.OBJECT)
	public Object O = null;
	
	@OutputVar(label="UserPage", description="",type=VariableType.OBJECT)
	public Object UserPage = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		
		log.debug("Loading Provider...");
		O365Provider Provider = null;
		try
		{
			Provider = (O365Provider)O;
		}
		catch(Exception e)
		{
			log.debug("Provider is not valid!");
			LogHelper.EtoStringLog(log, e);
			return;
		}
		
		log.debug("Accessing Users...");
		try
		{
			UserPage = Provider.getUsers();
		}
		catch(Exception e)
		{
			log.debug("Acessing Users failed!");
			LogHelper.EtoStringLog(log, e);
			return;
		}
		
		if(UserPage == null)
		{
			log.debug("Loading Users failed!");
		}
		
		log.debug("Userpage loaded!");
		
	}//END OF EXECUTION

	
}
