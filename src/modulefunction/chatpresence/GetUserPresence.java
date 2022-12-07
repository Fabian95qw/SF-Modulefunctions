package si.module.examples.chatpresence;

import org.apache.logging.log4j.Logger;

import de.starface.bo.BusinessObjects;
import de.starface.core.component.StarfaceComponentProvider;
import de.starface.integration.uci.java.v30.types.UserState;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Get the User's Chatpresence")
public class GetUserPresence implements IBaseExecutable
{
//##########################################################################################

  @InputVar(label="AccountID", description="The STARFACE_USER to do this action for",type=VariableType.STARFACE_USER)
  public int AccountID=0;

  @OutputVar(label="ChatPresence", description="The currently set chatpresence",type=VariableType.STRING)
  public String ChatPresence="";

  @OutputVar(label="ChatPresenceMessage", description="The currently set presencemessage possibleValues={AVAILABLE, AWAY, DO_NOT_DISTURB ,EXTENDED_AWAY, FREE_FOR_CHAT, UNAVAILABLE}",type=VariableType.STRING)
  public String ChatPresenceMessage="";

  @OutputVar(label="Success", description="If setting the status was sucessful",type=VariableType.BOOLEAN)
  public boolean Success=false;

    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance();
    //##########################################################################################


  //###################      Code Execution      ############################
  @Override
  public void execute(IRuntimeEnvironment context) throws Exception
  {
    Logger log  = context.getLog();
    //Fetch the Required Components
    BusinessObjects BO = (BusinessObjects)context.provider().fetch(BusinessObjects.class);

    UserState userState = BO.getUserStateBO().getUserState(AccountID); //Fetch the current UserState for the accountid
    if(userState == null) //If AccountID is invalid/user does not exist
    {
      log.error("User with AccountID: "+ AccountID+ " does not exist!");
      Success = false;
      return;
    }

    ChatPresence = userState.getChatPresence().toString(); //Read out ChatPresence to String
    ChatPresenceMessage = userState.getChatPresenceMessage(); //Red out ChatMessage

  }//END OF EXECUTION
}