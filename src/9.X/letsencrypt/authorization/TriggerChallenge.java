package si.module.letsencryptv3.authorization;

import org.apache.logging.log4j.Logger;
import org.shredzone.acme4j.Status;
import org.shredzone.acme4j.challenge.Dns01Challenge;
import org.shredzone.acme4j.challenge.Http01Challenge;
import org.shredzone.acme4j.exception.AcmeServerException;

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

@Function(visibility=Visibility.Private, rookieFunction=false, description="Authorizes a Domain")
public class TriggerChallenge implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="ChallengeType", description="", valueByReferenceAllowed=true)
	public Challenge C  = Challenge.NONE;
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
	@OutputVar(label="Retry", description="",type=VariableType.BOOLEAN)
	public Boolean Retry = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		if(Storage.A != null && Storage.A.getStatus().equals(Status.VALID))
		{
			log.debug("No Challenge triggered...");
			Success=true;
			return;
		}
		
		log.info("Triggering Challenge");
		try
		{
			switch(C)
			{
			case NONE:
				break;	
			case DNS:
				Dns01Challenge DNS = Storage.DNS;
				DNS.trigger();
				
				while(DNS.getStatus() != Status.VALID && DNS.getStatus() != Status.INVALID)
				{
					log.debug("Challenge State: " + DNS.getStatus().toString());
					Thread.sleep(5000);
					DNS.update();
				}
				
				if(DNS.getStatus() == Status.VALID)
				{	
					log.debug("Challenge Succeeded!");
					Success = true;
					return;
				}
				log.debug("Challenge Failed. Returned: " + DNS.getStatus());
				Storage.DNS=null;
				
				break;
			case HTTP:
				Http01Challenge HTTP = Storage.HTTP;
				HTTP.trigger();
				
				while(HTTP.getStatus() != Status.VALID && HTTP.getStatus() != Status.INVALID)
				{
					log.debug("Challenge State: " + HTTP.getStatus().toString());
					Thread.sleep(5000);
					HTTP.update();
				}
				
				if(HTTP.getStatus() == Status.VALID)
				{
					log.debug("Challenge Succeeded!");
					Success = true;
					return;
				}
				log.debug("Challenge Failed. Returned: " + HTTP.getStatus());
				Storage.HTTP = null;
				break;
			}
		}
		catch(AcmeServerException e)
		{
			LogHelper.EtoStringLog(log, e);
			Retry = true;
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
		Success = false;
	}//END OF EXECUTION

	
}
