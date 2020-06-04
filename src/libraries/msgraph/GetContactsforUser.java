package nucom.module.msgraphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.microsoft.graph.models.extensions.Contact;
import com.microsoft.graph.requests.extensions.IContactCollectionPage;
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

@Function(visibility=Visibility.Public, rookieFunction=false, description="Returns the Contact for the Provided User with the FolderID(Optional)")
public class GetContactsforUser implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="O365Provider", description="Office365 Provider",type=VariableType.OBJECT)
	public Object O = null;
	
	@InputVar(label="UserID/UPN", description="User ID or UserPrincipalname",type=VariableType.STRING)
	public String IDorUPN="";
	
	@InputVar(label="FolderID (Optional)", description="FolderID to use, if not supplied the default folder will be used.",type=VariableType.STRING)
	public String FolderID="";
	
	@OutputVar(label="ContactFolders", description="Returns a List<Map<String,Object>>",type=VariableType.LIST)
	public List<Map<String, Object>> Contacts = null;
	
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
		
		log.debug("Accessing Contacts for:"+IDorUPN +" in Folder: " + FolderID);

		IContactCollectionPage ContactPage = null;
		
		try
		{
			ContactPage = Provider.getContactsforUser(IDorUPN, FolderID);
			Contacts = new ArrayList<Map<String, Object>>();
			for(Contact C : ContactPage.getCurrentPage())
			{
				JSONParser JP = new JSONParser();
				JSONObject JSO = (JSONObject) JP.parse(C.getRawObject().toString());
				Contacts.add(JSONConverter.toMap(JSO));
			}
		}
		catch(Exception e)
		{
			log.debug("Converting Contacts failed");
			LogHelper.EtoStringLog(log, e);
			return;
		}
		
		log.debug("Contacts loaded!");
		
	}//END OF EXECUTION

}
