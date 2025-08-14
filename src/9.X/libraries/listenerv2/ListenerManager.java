package si.module.listenerinterface.listenerv2;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponent;
import de.starface.core.component.events.StarfaceEventService;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import si.module.listenerinterface.listenerv2.eventlistener.ListenerImpl;
import si.module.listenerinterface.listenerv2.eventlistener.OnAnyEventListener;
import si.module.listenerinterface.listenerv2.eventlistener.OnCallStateChangedEventListener;
import si.module.listenerinterface.listenerv2.eventlistener.OnDNDSettingChangedEventListener;
import si.module.listenerinterface.listenerv2.eventlistener.OnLineStateChangedEventListener;
import si.module.listenerinterface.listenerv2.eventlistener.OnModuleInstanceStateChangedEventListener;
import si.module.listenerinterface.listenerv2.eventlistener.OnPresenceChangedEventListener;
import si.module.listenerinterface.listenerv2.eventlistener.OnTelephonyStateChangedEventListener;
import si.module.listenerinterface.listenerv2.eventlistener.OnUserAssignmentSettingsChangedEventListener;
import si.module.listenerinterface.listenerv2.utility.EnumHelper.ListenerType;

public class ListenerManager extends StarfaceComponent
{		
	private static boolean isRunning=false;	
	private static Map<String, ListenerImpl> RegisteredListener = null;
	
	public ListenerManager(){}

	public boolean isRunning()
	{
		return isRunning;
	}

	@Override
	public void shutdownComponent() throws Throwable 
	{
		isRunning = false;
	}

	@Override
	public void startComponent() throws Throwable 
	{		
		RegisteredListener= new HashMap<String, ListenerImpl>();
		isRunning = true;
	}

	public boolean registerListener(String Listener_UUID, String InstanceID, String EntrypointID, ListenerType LT, IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		if(RegisteredListener == null) 
		{
			log.debug("Listener Service seems to be down");
			return false;
		}
		
		if(RegisteredListener.get(Listener_UUID) == null)
		{
			ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);	
			log.debug("Creating Listener: "+ Listener_UUID +" with Type: " + LT);
			ListenerImpl Listener = null;
			switch(LT)
			{
			case NONE:
				log.debug("[NONE] is not a valid Listener");
				return false;
			case onDoNotDistrubSettingChangedEvent:
				Listener = new OnDNDSettingChangedEventListener(Listener_UUID, InstanceID, EntrypointID, LT, MR, context.getLog());
				break;
			case onNewCallStateEvent:
				Listener = new OnCallStateChangedEventListener(Listener_UUID, InstanceID, EntrypointID, LT, MR, context.getLog());
				break;
			case onPresenceChangedEvent:
				Listener = new OnPresenceChangedEventListener(Listener_UUID, InstanceID, EntrypointID, LT, MR, context.getLog());
				break;
			case onTelephonyStateChangedEvent:
				Listener = new OnTelephonyStateChangedEventListener(Listener_UUID, InstanceID, EntrypointID, LT, MR, context.getLog());
				break;
			case onModuleInstanceStateChangedEvent:
				Listener = new OnModuleInstanceStateChangedEventListener(Listener_UUID, InstanceID, EntrypointID, LT, MR, context.getLog());
				break;
			case onLineStateChangedEvent:
				Listener = new OnLineStateChangedEventListener(Listener_UUID, InstanceID, EntrypointID, LT, MR, context.getLog());	
				break;
			case onAnyEvent:
				Listener = new OnAnyEventListener(Listener_UUID, InstanceID, EntrypointID, LT, MR, context.getLog());	
				break;
			case onUserAssignmendSettingsChangedEvent:
				Listener = new OnUserAssignmentSettingsChangedEventListener(Listener_UUID, InstanceID, EntrypointID, LT, MR, context.getLog());	
				break;
			default:
				break;		
			}
			
			if(Listener != null)
			{
				log.debug("Subscribing Listener...");

					StarfaceEventService SES = context.provider().fetch(StarfaceEventService.class);
					
					SES.subscribe(Listener);
				RegisteredListener.put(Listener_UUID, Listener);
				return true;
			}
			return false;
		}		
		log.debug("Listener already exists");
		return false;
	}

	public ListenerImpl getInstance(String Listener_UUID) 
	{
		if(RegisteredListener == null)
		{
			return null;
		}
		return RegisteredListener.get(Listener_UUID);
	}

	@Override
	protected boolean startupCondition() 
	{
		return true;
	}

	public boolean removeListener(String Listener_UUID, IRuntimeEnvironment context) 
	{
		if(RegisteredListener == null) {return false;}
		ListenerImpl Listener = getInstance(Listener_UUID);
		log.debug("Unsubscribing Listener: "+ Listener_UUID+" from: " + Listener.getLT().toString());
		StarfaceEventService SES = context.provider().fetch(StarfaceEventService.class);
		SES.unsubscribe(Listener);
		Listener.unregistered();		
		
		RegisteredListener.remove(Listener_UUID);
		
		return true;
	}
}
