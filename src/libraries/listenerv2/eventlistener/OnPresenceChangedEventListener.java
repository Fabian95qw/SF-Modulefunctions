package si.module.listenerinterface.listenerv2.eventlistener;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.annotation.EventSubscriber;

import de.starface.integration.openfire.PresenceChangedEvent;
import de.vertico.starface.module.core.ModuleRegistry;
import si.module.listenerinterface.listenerv2.utility.LogHelper;
import si.module.listenerinterface.listenerv2.utility.EnumHelper.ListenerType;

public class OnPresenceChangedEventListener extends ListenerImpl
{
	public OnPresenceChangedEventListener(String UUID, String InstanceID, String EntryPointID, ListenerType LT, ModuleRegistry MR, Logger logger)
	{
		super(UUID, InstanceID, EntryPointID, LT, MR, logger);
	}

	  @EventSubscriber 
	  public void onPresenceChangedEvent(PresenceChangedEvent Event)
	  {
		  try
		  {
		  Map<String, Object> EventMap = new HashMap<String, Object>();
		  EventMap.put("STARFACE_ACCOUNT", Event.getAccountId()+"");
		  EventMap.put("Presence", Event.getPresence().toString());
		  EventMap.put("PresenceMessage", Event.getPresenceMessage());
		  EventMap.put("AvatarSha1Hash", Event.getAvatarSha1Hash());
		  EventMap.put("From", Event.getFrom().toString());
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
