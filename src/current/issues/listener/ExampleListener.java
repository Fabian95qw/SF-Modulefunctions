package si.module.examples.issues.listener;

import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.annotation.EventSubscriber;

import de.vertico.starface.persistence.connector.events.DoNotDisturbSettingChangedEvent;

public class ExampleListener 
{
	private Logger log = null;
	public ExampleListener(Logger log2)
	{
		this.log=log2;
		log2.debug("Hello I'm a Example Listener");
	}

	  @EventSubscriber 
	  public void onDoNotDistrubSettingChangedEvent(DoNotDisturbSettingChangedEvent Event)
	  {
		  log.debug("DND State for:" + Event.getAccountId() +" is " + Event.isDoNotDisturbSetting());
	  }
	
}
