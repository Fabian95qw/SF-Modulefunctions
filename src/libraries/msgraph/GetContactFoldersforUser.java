package nucom.module.msgraphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.microsoft.graph.models.extensions.ContactFolder;
import com.microsoft.graph.requests.extensions.IContactFolderCollectionPage;
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

@Function(visibility=Visibility.Public, rookieFunction=false, description="Returns the Contact Folders for the Provided User")
public class GetContactFoldersforUser implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="O365Provider", description="Office365 Provider",type=VariableType.OBJECT)
	public Object O = null;
	
	@InputVar(label="UserID/UPN", description="User ID or UserPrincipalname",type=VariableType.STRING)
	public String IDorUPN="";
	
	@OutputVar(label="ContactFolders", description="Returns a List<Map<String,Object>>",type=VariableType.LIST)
	public List<Map<String, Object>> ContactFolders = null;
	
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
		
		log.debug("Accessing Contactfolders for:"+IDorUPN);

		IContactFolderCollectionPage ContactFoldersPage = null;
		
		try
		{
			ContactFoldersPage = Provider.getContactFoldersforUser(IDorUPN);
			ContactFolders = new ArrayList<Map<String, Object>>();
			for(ContactFolder CF : ContactFoldersPage.getCurrentPage())
			{
				JSONParser JP = new JSONParser();
				JSONObject JSO = (JSONObject) JP.parse(CF.getRawObject().toString());
				ContactFolders.add(JSONConverter.toMap(JSO));
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
