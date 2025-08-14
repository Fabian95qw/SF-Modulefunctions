package si.module.examples.callplacement;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.bo.callhandling.actions.CallBusinessObject;

import de.starface.integration.uci.java.v30.types.Call;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, description="")
public class GetCallInformationbyUUID implements IBaseExecutable 
{
	//##########################################################################################
	
	
	@InputVar(label="STARFACE_USER_SOURCE", description="The Accountid of the Source (-1 for no Source Account)",type=VariableType.STARFACE_USER)
	public Integer STARFACE_USER_SOURCE=-1;
	
	@InputVar(label="STARFACE_USER_DESTINATION", description="The accountid of the Destination (-1 for ne Destination Account)",type=VariableType.STARFACE_USER)
	public Integer STARFACE_USER_DESTINATION=-1;
	
	@InputVar(label="CallID", description="UUID of the Call",type=VariableType.STRING)
	public String CallID="";
	
	@OutputVar(label="CallInformation", description="CallInformation as a Map<String, String>",type=VariableType.MAP)
	public  Map<String, Object> EventMap = new HashMap<String, Object>();

     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		CallBusinessObject CBO = context.springApplicationContext().getBean(CallBusinessObject.class);

		try
		{			
			List<String> SourceCalls = CBO.getCallIds(STARFACE_USER_SOURCE);
			List<String> DestinationCalls = CBO.getCallIds(STARFACE_USER_DESTINATION);
			
			Call C = null;
			
			if(SourceCalls.contains(CallID))
			{
				C = CBO.getCallState(CallID, STARFACE_USER_SOURCE);
			}
			else if (DestinationCalls.contains(CallID))
			{
				C = CBO.getCallState(CallID, STARFACE_USER_DESTINATION);
			}
			
			if(C == null)
			{
				return;
			}
			  EventMap.put("STARFACE_ACCOUNT", C.getAccountId()+"");
			  EventMap.put("AvatarHash", C.getAvatarHash());
			  EventMap.put("CalledName", C.getCalledName());
			  EventMap.put("CallerName", C.getCallerName());
			  EventMap.put("CalledNumber", C.getCalledNumber());
			  EventMap.put("CallerNumber", C.getCallerNumber());
			  EventMap.put("CallState", C.getState().toString());
			  EventMap.put("CallChannels", C.getChannelNames());
			  EventMap.put("ConferenceRoomId", C.getConferenceRoomId());
			  
			  if(C.getConnectedTimestamp() != null)
			  {
				  EventMap.put("ConnectedTimeStamp", ""+C.getConnectedTimestamp().getTime());
			  }
			  else
			  {
				  EventMap.put("ConnectedTimeStamp", "0");
			  }
			  		  
			  EventMap.put("DoorlineCamUrl", C.getDoorlineCamUrl());
			  EventMap.put("DoorlineDTMFCode", C.getDoorlineDtmfCode());
			  EventMap.put("DoorlineImageProviderId", C.getDoorlineImageProviderId());
			  EventMap.put("Duration", C.getDuration());
			  EventMap.put("ForwardCallerIdName", C.getForwarderCallerIdName());
			  EventMap.put("ForwardCallerIdNumber", C.getForwarderCallerIdNumber());
			  EventMap.put("ForwardType", C.getForwardType().toString());
			  EventMap.put("GroupId", C.getGroupId());
			  EventMap.put("CallUUID", C.getId());
			  EventMap.put("JabberId", C.getJabberId());
			  EventMap.put("Peernames", C.getPeerNames());
			  EventMap.put("ReferenceOfConsultation", C.getReferenceOfConsultation());
			  EventMap.put("SipCallIds", C.getSipCallIds());
			  
			  if(C.getTimestamp() != null)
			  {
				  EventMap.put("Timestamp", ""+C.getTimestamp().getTime());
			  }
			  else
			  {
				  EventMap.put("Timestamp", "0");
			  }
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
			EtoStringLog(log, e);
		}
		
	}//END OF EXECUTION

		public static void EtoStringLog(Logger log, Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.debug(sw.toString()); //
		}
}
