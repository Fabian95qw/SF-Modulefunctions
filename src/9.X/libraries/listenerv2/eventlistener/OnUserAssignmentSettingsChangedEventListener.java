package si.module.listenerinterface.listenerv2.eventlistener;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.annotation.EventSubscriber;

import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.persistence.connector.events.UserAssignmentsSettingsChangedEvent;
import si.module.listenerinterface.listenerv2.utility.LogHelper;
import si.module.listenerinterface.listenerv2.utility.EnumHelper.ListenerType;

public class OnUserAssignmentSettingsChangedEventListener extends ListenerImpl
{
	public OnUserAssignmentSettingsChangedEventListener(String UUID, String InstanceID, String EntryPointID, ListenerType LT, ModuleRegistry MR, Logger logger)
	{
		super(UUID, InstanceID, EntryPointID, LT, MR, logger);
	}

	@EventSubscriber()
	  public void onUserAssignmentsSettingsChangedEvent(UserAssignmentsSettingsChangedEvent Event)
	  {
		try
		{
		  Map<String, Object> EventMap = new HashMap<String, Object>();
		  EventMap.put("STARFACE_GROUP", Event.getAccountId());
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
