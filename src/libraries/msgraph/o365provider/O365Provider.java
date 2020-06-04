package nucom.module.msgraphs.o365provider;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IContactCollectionPage;
import com.microsoft.graph.requests.extensions.IContactFolderCollectionPage;
import com.microsoft.graph.requests.extensions.IUserCollectionPage;

import nucom.module.msgraphs.utility.LogHelper;

public class O365Provider 
{
	ClientCredentialProvider authProvider = null;
	IGraphServiceClient GC = null;
	Log log =null;
	
	public O365Provider(String TenantID, String ClientID, String ClientSecret, List<String> Scopes, Log log)
	{
		this.log=log;
		authProvider = new ClientCredentialProvider(ClientID, Scopes, ClientSecret, TenantID, NationalCloud.Global);
		GC = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
		GC.validate();
		
	}
	
	public IUserCollectionPage getUsers()
	{
		try
		{
			return GC.users().buildRequest().get();
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
			return null;
		}
	}
	
	public IContactFolderCollectionPage getContactFoldersforUser(String IDorUPN)
	{
		try
		{
			return GC.users(IDorUPN).contactFolders().buildRequest().get();
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
			return null;
		}
	}
	
	public IContactCollectionPage getContactsforUser(String IDorUPN, String FolderID)
	{
		try
		{
			if(FolderID.isEmpty())
			{
				return GC.users(IDorUPN).contacts().buildRequest().get();
			}
			else
			{
				return GC.users(IDorUPN).contactFolders(FolderID).contacts().buildRequest().get();
			}
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
			return null;
		}
	}
	
	public JSONObject genericRequest(String SubURL, Map<String, String>Body) throws ClientException, ParseException
	{	
		JSONParser JP = new JSONParser();
		
		return (JSONObject) JP.parse(GC.customRequest(SubURL).buildRequest().get().toString());
	}

	public String Token() 
	{
		return authProvider.getAcccessToken();
	}

	public void tobeta() 
	{
		GC.setServiceRoot("https://graph.microsoft.com/beta");
	}
}
