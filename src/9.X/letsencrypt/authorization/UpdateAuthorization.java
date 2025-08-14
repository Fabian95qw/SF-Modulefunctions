package si.module.letsencryptv3.authorization;

import org.apache.logging.log4j.Logger;
import org.shredzone.acme4j.Authorization;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.Status;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.letsencryptv3.utility.Storage;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Authorizes a Domain, and Returns the Acme Challenge")
public class UpdateAuthorization implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="Domain", description="",type=VariableType.STRING)
	public String Domain ="";

	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		if(Domain.isEmpty())
		{
			log.error("Supplied Domain: "+  Domain +" is invalid!");
			return;
		}
		
		Order O = Storage.AC.newOrder().domains(Domain).create();		
		Storage.O=O;
		log.debug("Loading Authorizations...");
		
		O.update();
		for(Authorization Auth : O.getAuthorizations())
		{
			log.debug("----------------------------------------");
			log.debug("Identifiert:"+Auth.getIdentifier());
			log.debug("Status:"+Auth.getStatus().toString());
			log.debug("Expires:"+Auth.getExpires().toString());
			
			if(Auth.getStatus().equals(Status.VALID))
			{
				Auth.update();
				
				log.debug("Expires new: "+ Auth.getExpires());
			}			
			
		}
		Success = true;
	}//END OF EXECUTION

	
}
