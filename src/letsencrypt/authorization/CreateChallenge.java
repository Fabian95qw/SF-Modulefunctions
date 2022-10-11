package si.module.letsencryptv3.authorization;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.logging.log4j.Logger;
import org.shredzone.acme4j.Authorization;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.Status;
import org.shredzone.acme4j.challenge.Dns01Challenge;
import org.shredzone.acme4j.challenge.Http01Challenge;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.letsencryptv3.utility.LogHelper;
import si.module.letsencryptv3.utility.Storage;
import si.module.letsencryptv3.utility.EnumHelper.Challenge;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Authorizes a Domain, and Returns the Acme Challenge")
public class CreateChallenge implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="Domain", description="",type=VariableType.STRING)
	public String Domain ="";
	
	@InputVar(label="ChallengeType", description="", valueByReferenceAllowed=true)
	public Challenge C  = Challenge.NONE;
	
	@OutputVar(label="Acme_Entry", description="",type=VariableType.STRING)
	public String AcmeEntry ="";
	
	@OutputVar(label="Acme_Digest", description="",type=VariableType.STRING)
	public String AcmeDigest ="";
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		log.info("Trying to Authorize Domain: "+ Domain);
		
		if(Domain.isEmpty())
		{
			log.debug("The Domain Field is Empty");
			Success = false;
			
			return;
		}
				
		Authorization A = null;
		

		Order O = Storage.AC.newOrder().domains(Domain).create();		
		Storage.O=O;
		log.debug("Loading Authorizations...");
		
		for(Authorization Auth : O.getAuthorizations())
		{
			log.debug("----------------------------------------");
			log.debug("Identifiert:"+Auth.getIdentifier());
			log.debug("Status:"+Auth.getStatus().toString());
			log.debug("Expires:"+Auth.getExpires().toString());
			
			if(Auth.getStatus().equals(Status.VALID))
			{
				A = Auth;
			}			
			
		}
		
		if(A != null)
		{
			log.debug("No new Challenge required. Skipping...");
			Storage.A = O.getAuthorizations().get(0);
			Success=true;
		}
		
		try
		{
			switch(C)
			{
			case DNS:
				 A = O.getAuthorizations().get(0);
				if(Storage.DNS == null || Storage.DNS.getStatus() == Status.INVALID)
				{
					Dns01Challenge DNS = A.findChallenge(Dns01Challenge.TYPE);
					AcmeEntry ="DNS-Entry-TYPE:TXT ==> _acme-challenge."+Domain;
					AcmeDigest = "DNS-Entry-CONTENT: ==> " + DNS.getDigest();
					Storage.DNS = DNS;
				}
				else
				{
					AcmeEntry ="DNS-Entry-TYPE:TXT ==> _acme-challenge."+Domain;
					AcmeDigest = "DNS-Entry-CONTENT: ==> " +Storage.DNS.getDigest();
				}
				break;
			case HTTP:
				A=O.getAuthorizations().get(0);					
				File WellKnownFolder = new File("/opt/tomcat/webapps/localhost/starface/.well-known/acme-challenge");
				WellKnownFolder.mkdirs();
				
				log.debug("Deleting old Authorizations...");
				for(File F: WellKnownFolder.listFiles())
				{
					log.debug("Deleting: "+  F.getAbsolutePath());
					F.delete();
				}
				
				if(Storage.HTTP == null || Storage.HTTP.getStatus() == Status.INVALID)
				{
					Http01Challenge HTTP = A.findChallenge(Http01Challenge.TYPE);
					AcmeEntry ="TARGET-URL: http://"+Domain+"/.well-known/acme-challenge/"+HTTP.getToken();
					AcmeDigest="FILE-CONTENT: " +HTTP.getAuthorization();
					Storage.HTTP = HTTP;
				}
				else
				{
					AcmeEntry = "TARGET-URL: http://"+Domain+"/.well-known/acme-challenge/"+Storage.HTTP.getToken();
					AcmeDigest="FILE-CONTENT: " +Storage.HTTP.getAuthorization();
				}
				
				log.debug("Creating Token...");
				File Token = new File("/opt/tomcat/webapps/localhost/starface/.well-known/acme-challenge/"+Storage.HTTP.getToken());
				if(!Token.exists())
				{
					log.debug("Writing Token..."+  Token.getAbsoluteFile());
					Token.createNewFile();
				}
				try
				{
					FileOutputStream FOS = new FileOutputStream(Token, false);
					FOS.write(Storage.HTTP.getAuthorization().getBytes());
					FOS.flush();
					FOS.close();
				}
				catch(Exception e)
				{
					LogHelper.EtoStringLog(log, e);
				}
				
				break;
			case NONE:
				break;		
			}
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
			Success= false;
			return;
		}
		
		Storage.A = A;
				
	}//END OF EXECUTION

	
}
