package si.module.modulefunction;

import org.apache.commons.logging.Log;

import de.starface.bo.UserStateBusinessObject;
import de.starface.bo.events.NewUserStateEvent;
import de.starface.core.component.StarfaceComponentProvider;
import de.starface.core.component.events.StarfaceEventService;
import de.starface.integration.uci.java.v30.types.UserState;
import de.starface.integration.uci.java.v30.values.ChatPresence;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class SetChatPresence implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="STARFACE_USER", description="STARFACE User to set presence of",type=VariableType.STARFACE_USER)
	public Integer STARFACE_USER=-1;
	
	@InputVar(label="ChatPresence", description="Presence to set", valueByReferenceAllowed=true)
	public ChatPresence CP  = ChatPresence.AVAILABLE;
		    	
	@OutputVar(label="Success", description="Success",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		
		log.debug("Getting User with AccountID: "+ STARFACE_USER);
		
		UserStateBusinessObject UBO = (UserStateBusinessObject)context.provider().fetch(UserStateBusinessObject.class);
		StarfaceEventService ES = (StarfaceEventService)context.provider().fetch(StarfaceEventService.class);

		UserState userState = UBO.getUserState(STARFACE_USER); //Userstatus für AccountID Abrufen

		log.debug("Setting Chat Presence to : " +CP.toString());
		userState.setChatPresence(CP); //Neuen Status setzen
		
		log.debug("Publishing new UserState...");
		NewUserStateEvent Update = new NewUserStateEvent(STARFACE_USER, userState); //Neuen Status in ein Event Verwandeln
		ES.publish(Update, context.getLog()); //Statusänderungsevent Publishen
		log.debug("Done");
		
		Success = true;
		
	}//END OF EXECUTION

	
}
