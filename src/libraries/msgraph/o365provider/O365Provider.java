package nucom.module.msgraphs.o365provider;

import java.util.List;

import org.apache.commons.logging.Log;

import com.microsoft.graph.auth.confidentialClient.ClientCredentialProvider;
import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IContactCollectionPage;
import com.microsoft.graph.requests.extensions.IUserCollectionPage;

import nucom.module.msgraphs.utility.LogHelper;

public class O365Provider 
{
	private ClientCredentialProvider authProvider = null;
	private IGraphServiceClient GC = null;
	private Log log = null;
	
	public O365Provider(String TenantID, String ClientID, String ClientSecret, List<String> Scopes, Log log)
	{
		this.log=log;
		authProvider = new ClientCredentialProvider(ClientID, Scopes, ClientSecret, TenantID, NationalCloud.Global);
		GC = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
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
	
	public IContactCollectionPage getContactsforUser(User U)
	{
		try
		{
			return GC.users(U.id).contacts().buildRequest().get();
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
			return null;
		}
	}
	
}
