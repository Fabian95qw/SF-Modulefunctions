package si.module.listenerinterface.listenerv2.eventlistener;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.vertico.starface.module.core.ModuleRegistry;
import si.module.listenerinterface.listenerv2.utility.EnumHelper.ListenerType;

public abstract class ListenerImpl 
{
	private String UUID = "";
	private String InstanceID="";
	private String EntryPointID="";
	private ListenerType LT = ListenerType.NONE;
	private ModuleRegistry MR = null;
	protected Logger log = null;
	
	
	
	public ListenerImpl(String UUID, String InstanceID, String EntryPointID, ListenerType LT, ModuleRegistry MR, Logger logger)
	{
		this.UUID=UUID;
		this.InstanceID=InstanceID;
		this.EntryPointID=EntryPointID;
		this.LT=LT;
		this.log=logger;
		this.MR=MR;
	}
	
	public void pushEvent(Map<String, Object> EventMap)
	{
		Map<String, Object> Params = new HashMap<String, Object>();
		Params.put("Data", EventMap);
		log.debug("Pushing Eventmap:" + EventMap);
		MR.callEntryPoint(InstanceID, EntryPointID, Params, false, null, null, null);
	}
	
	public String getUUID() {
		return UUID;
	}

	public ListenerType getLT() {
		return LT;
	}
	
	public abstract void unregistered();
}
