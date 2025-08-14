package si.module.listenerinterface.listenerv2.eventlistener;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.annotation.EventSubscriber;

import de.starface.ch.processing.bo.api.events.TelephonyStateChangedEvent;
import de.vertico.starface.module.core.ModuleRegistry;
import si.module.listenerinterface.listenerv2.utility.LogHelper;
import si.module.listenerinterface.listenerv2.utility.EnumHelper.ListenerType;

public class OnTelephonyStateChangedEventListener extends ListenerImpl
{
	public OnTelephonyStateChangedEventListener(String UUID, String InstanceID, String EntryPointID, ListenerType LT, ModuleRegistry MR, Logger logger)
	{
		super(UUID, InstanceID, EntryPointID, LT, MR, logger);
	}

	  @EventSubscriber 
	  public void onTelephonyStateChangedEvent(TelephonyStateChangedEvent Event)
	  {
		  try
		  {
		  Map<String, Object> EventMap = new HashMap<String, Object>();
		  EventMap.put("STARFACE_ACCOUNT", Event.getAccountId()+"");
		  EventMap.put("Telephonystate", Event.getTelephonyState().toString());
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
