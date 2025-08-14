package si.module.listenerinterface.listenerv2.eventlistener;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.annotation.EventSubscriber;
import de.starface.core.component.events.Event;
import de.starface.integration.uci.bo.events.NewCallStateEvent;
import de.vertico.starface.module.core.ModuleRegistry;
import si.module.listenerinterface.listenerv2.utility.EnumHelper.ListenerType;
import si.module.listenerinterface.listenerv2.utility.LogHelper;

public class OnCallStateChangedEventListener extends ListenerImpl
{
	public OnCallStateChangedEventListener(String UUID, String InstanceID, String EntryPointID, ListenerType LT, ModuleRegistry MR, Logger logger)
	{
		super(UUID, InstanceID, EntryPointID, LT, MR, logger);
	}
	
	@EventSubscriber()
	  public void onAnyEvent(Event AnyEvent)
	  {
			if(AnyEvent instanceof de.starface.integration.uci.bo.events.NewCallStateEvent)
			{
				try
				{
					onCallStateChangeEvent((NewCallStateEvent) AnyEvent);
				}
				catch(Exception e)
				{
					LogHelper.EtoStringLog(log, e);
				}
			}
	  }
	
	
	//@EventSubscriber(eventServiceName = "CallProcessingEventService")
	public void onCallStateChangeEvent(de.starface.integration.uci.bo.events.NewCallStateEvent Event)
	  {
		  Map<String, Object> EventMap = new HashMap<String, Object>();

		  EventMap.put("STARFACE_ACCOUNT", Event.getAccountId()+"");
		  EventMap.put("AvatarHash", Event.getCall().getAvatarHash());
		  EventMap.put("CalledName", Event.getCall().getCalledName());
		  EventMap.put("CallerName", Event.getCall().getCallerName());
		  EventMap.put("CalledNumber", Event.getCall().getCalledNumber());
		  EventMap.put("CallerNumber", Event.getCall().getCallerNumber());
		  EventMap.put("CallState", Event.getCall().getState().toString());
		  EventMap.put("CallChannels", Event.getCall().getChannelNames());
		  EventMap.put("ConferenceRoomId", Event.getCall().getConferenceRoomId());
		  
		  if(Event.getCall().getConnectedTimestamp() != null)
		  {
			  EventMap.put("ConnectedTimeStamp", Event.getCall().getConnectedTimestamp().getTime());
		  }
		  else
		  {
			  EventMap.put("ConnectedTimeStamp", "0");
		  }
		  		  
		  EventMap.put("DoorlineCamUrl", Event.getCall().getDoorlineCamUrl());
		  EventMap.put("DoorlineDTMFCode", Event.getCall().getDoorlineDtmfCode());
		  EventMap.put("DoorlineImageProviderId", Event.getCall().getDoorlineImageProviderId());
		  EventMap.put("Duration", Event.getCall().getDuration());
		  EventMap.put("ForwardCallerIdName", Event.getCall().getForwarderCallerIdName());
		  EventMap.put("ForwardCallerIdNumber", Event.getCall().getForwarderCallerIdNumber());
		  EventMap.put("ForwardType", Event.getCall().getForwardType().toString());
		  EventMap.put("GroupId", Event.getCall().getGroupId());
		  EventMap.put("CallUUID", Event.getCall().getId());
		  EventMap.put("JabberId", Event.getCall().getJabberId());
		  EventMap.put("Peernames", Event.getCall().getPeerNames());
		  EventMap.put("ReferenceOfConsultation", Event.getCall().getReferenceOfConsultation());
		  EventMap.put("SipCallIds", Event.getCall().getSipCallIds());
		  
		  if(Event.getCall().getTimestamp() != null)
		  {
			  EventMap.put("Timestamp", Event.getCall().getTimestamp().getTime());
		  }
		  else
		  {
			  EventMap.put("Timestamp", "0");
		  }

		  pushEvent(EventMap);
	  }

	@Override
	public void unregistered() {}

	
}
