package nucom.module.msgraphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.IUserCollectionPage;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import nucom.module.msgraphs.o365provider.O365Provider;
import nucom.module.msgraphs.utility.JSONConverter;
import nucom.module.msgraphs.utility.LogHelper;

@Function(visibility=Visibility.Public, rookieFunction=false, description="Returns a List of Users")
public class GetUsers implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="O365Provider", description="Office365 Provider",type=VariableType.OBJECT)
	public Object O = null;
	
	@OutputVar(label="Users", description="Returns a List<Map<String,Object>>",type=VariableType.LIST)
	public List<Map<String, Object>> Users = null;
	
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

		IUserCollectionPage UserPage = null;
		
		try
		{
			UserPage = Provider.getUsers();
			Users = new ArrayList<Map<String, Object>>();
			for(User User : UserPage.getCurrentPage())
			{
				//log.debug(User.getRawObject().toString());
				//Users.add(toMap(User.getRawObject()));
				JSONParser JP = new JSONParser();
				JSONObject JSO = (JSONObject) JP.parse(User.getRawObject().toString());
				Users.add(JSONConverter.toMap(JSO));
			}
		}
		catch(Exception e)
		{
			log.debug("Converting Users failed");
			LogHelper.EtoStringLog(log, e);
			return;
		}
		
		log.debug("Users loaded!");
		
	}//END OF EXECUTION

}
