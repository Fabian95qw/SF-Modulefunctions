WIP
# ModuleBusinessObject

Das ModulebusinessObject, sowie die ModuleRegisry enthalten einen grossteil aller Funktionen, die bereits als Baustein zur Verüfgung stellen, haben jedoch noch mehr nutzen.

Liste aller Funktionen:
Alle mit * Markierten Funktionen haben ein Beispiel weiter unten


| Funktion | Zweck | Inputvariablen | Returns | Returnwert Zweck | Beispiel vorhanden? |
|---|---|---|---|---|---|
| answer | Nimmt den entsprechend Anruf vom angegeben channel an. Entspricht dem Bausten "answer" | String Channelname, int Delay | void | - | Nein |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |




 - void answer(String Channelname, int delay) 
 - void callNumbertoConference(String phoneNumber, String conferencName, int accountid)
 - void conference(String channelName, String conferenceRoomId, ConferenceConfig config) *
 - void createConfe



# Beispiele
## void Conference

    // DER BAUSTEIN BENÖTIGT EINEN AKTIVEN CALLCHANNEL, 
    import de.starface.bo.callhandling.actions.ModuleBusinessObject;
    import de.starface.ch.routing.agi.api.ConferenceConfig;
    import de.starface.core.component.StarfaceComponentProvider;
    import de.vertico.starface.module.core.model.VariableType;
    import de.vertico.starface.module.core.model.Visibility;
    import de.vertico.starface.module.core.runtime.IAGIRuntimeEnvironment;
    import de.vertico.starface.module.core.runtime.annotations.Function;
    import de.vertico.starface.module.core.runtime.annotations.InputVar;
    import de.vertico.starface.module.core.runtime.functions.callHandling.CallHandlingFunction;
    
    @Function(visibility=Visibility.Private, rookieFunction=false, description="")
    public class CreateConference extends CallHandlingFunction 
    {
    	//##########################################################################################
    	
    	@InputVar(label="Room", description="",type=VariableType.STRING) 
    	public String Room="";  
    	
    	@InputVar(label="AnnounceOnlyUser", description="",type=VariableType.BOOLEAN)
    	public Boolean AnnounceOnlyUser=false; 
    	
    	@InputVar(label="AnnounceUserCount", description="",type=VariableType.BOOLEAN)
    	public Boolean AnnounceUserCount=false;
    	
    	@InputVar(label="AnnounceUserCountAll", description="",type=VariableType.BOOLEAN)
    	public Boolean AnnounceUserCountAll=false;
    	
    	@InputVar(label="AnnounceJoinLeave", description="",type=VariableType.BOOLEAN)
    	public Boolean AnnounceJoinLeave=false;
    	
    	@InputVar(label="MusicOnHoldWhenEmpty", description="",type=VariableType.BOOLEAN)
    	public Boolean MusicOnHoldWhenEmpty=false;
    		    		
        StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
        //##########################################################################################
    	
    	//###################			Code Execution			############################	
    	@Override
    	public void executeImpl(IAGIRuntimeEnvironment context) throws Exception 
    	{
    	      ModuleBusinessObject MBO = (ModuleBusinessObject)context.provider().fetch(ModuleBusinessObject.class);
    	      
    	     ConferenceConfig CC = new ConferenceConfig.Builder()
    	      .setAnnounceOnlyUser(AnnounceOnlyUser) //Text "sie sind alleine im Konferenzraum" abspielen
    	      .setAnnounceUserCount(AnnounceUserCount) //Text "[n] Benutzer sind im Koferenzraum" für neu Beigetreten Benutzer abspielen
    	      .setAnnounceUserCountAll(AnnounceUserCountAll) //Text "[n] Benutzer sind im Konferenzraum" für alle Abspielen
    	      .setAnnounceJoinLeave(AnnounceJoinLeave) //Text "Jemand hat den Konferenzraum betreten/verlassen" abspielen
    	      .setMusicOnHoldWhenEmpty(MusicOnHoldWhenEmpty).build(); //Wartemusik abspielen, wenn nur ein Teilnehmer im Raum ist
    	     
    	     MBO.conference(context.getCallerChannelName(), Room, CC); //Den aktuellen Anrufer (der gerade das Modul Anruft) mit der Conferenceconfig in den Raum setzen. Wenn dies der erste Teilnehmer ist, wird der Konferenzraum automatisch erstellt
    
    	}//END OF EXECUTION
    }

