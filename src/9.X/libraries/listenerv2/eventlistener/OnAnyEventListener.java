package si.module.listenerinterface.listenerv2.eventlistener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.annotation.EventSubscriber;

import de.vertico.starface.module.core.ModuleRegistry;

import de.starface.core.component.events.Event;
import si.module.listenerinterface.listenerv2.utility.LogHelper;
import si.module.listenerinterface.listenerv2.utility.EnumHelper.ListenerType;

public class OnAnyEventListener extends ListenerImpl
{
	public OnAnyEventListener(String UUID, String InstanceID, String EntryPointID, ListenerType LT, ModuleRegistry MR, Logger logger)
	{
		super(UUID, InstanceID, EntryPointID, LT, MR, logger);
	}
	
	@EventSubscriber()
	  public void onAnyEvent(Event AnyEvent)
	  {
		try
		{
		  Map<String, Object> EventMap = new HashMap<String, Object>();
		  EventMap.put("Type", AnyEvent.getClass().getCanonicalName());  
		  
		  Class<?> C = AnyEvent.getClass();
		   
		  Method[] AllMethods = C.getDeclaredMethods();
		  
		  for(Method M : AllMethods)
		  {
			  M.setAccessible(true);
			  //log.debug(M.getName());
			  if(M.getName().startsWith("get") || M.getName().startsWith("is"))
			  {
				  String S = M.getName();
				  if(M.getName().startsWith("get"))
				  {
					 S=M.getName().replaceFirst("get", "");
				  }
				  if(M.getName().startsWith("is"))
				  {
					  S=M.getName().replaceFirst("is", "");
				  }
				  
				  Object O = null;
				  try
				  {
					  O = M.invoke(AnyEvent);
					  if(O!= null)
					  {
						  EventMap.put(S, O.toString());
					  }
					  else
					  {
						  EventMap.put(S, "");
					  }
	
				  }
				  catch(Exception e)
				  {
					 EtoStringLog(log, e);
				  }
			  }
		  }
		  pushEvent(EventMap);
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
	  }

	@Override
	public void unregistered() {}

	private static void EtoStringLog(Logger log, Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}
	
}

