package si.module.listenerinterface.listenerv2.eventlistener;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.annotation.EventSubscriber;

import de.vertico.starface.module.core.ModuleRegistry;
import si.module.listenerinterface.listenerv2.utility.LogHelper;
import si.module.listenerinterface.listenerv2.utility.EnumHelper.ListenerType;
import de.starface.ch.processing.bo.api.events.LineStateChangedEvent;

public class OnLineStateChangedEventListener extends ListenerImpl
{
	public OnLineStateChangedEventListener(String UUID, String InstanceID, String EntryPointID, ListenerType LT, ModuleRegistry MR, Logger logger)
	{
		super(UUID, InstanceID, EntryPointID, LT, MR, logger);
	}

	@EventSubscriber(eventServiceName = "CallProcessingEventService")
	  public void onLineStateChangedEvent(LineStateChangedEvent Event)
	  {
		try
		{
		  Map<String, Object> EventMap = new HashMap<String, Object>();
		  EventMap.put("LineconfigId", Event.getLineconfigId());
		  EventMap.put("LineName", Event.getLineName());
		  EventMap.put("IsOnline", Event.isOnline());
		  pushEvent(EventMap);
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
	  }

	@Override
	public void unregistered() {}

	
}
