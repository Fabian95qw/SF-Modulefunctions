package si.module.examples.issues.listener;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.events.StarfaceEventService;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;

@Function(visibility=Visibility.Private, description="")
public class RegisterListener implements IBaseExecutable 
{
	private static ExampleListener Example = null;
	
	@InputVar(label="RegisterListener", description="If true, registers listener, if false unregisters listener",type=VariableType.BOOLEAN)
	public boolean RegisterListener=false;
		    	
	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		if(Example == null && RegisterListener)
		{
			log.debug("Registering new Listener!");
			Example = new ExampleListener(log);
			StarfaceEventService SES = context.springApplicationContext().getBean(StarfaceEventService.class);
			SES.subscribe(Example);
		}
		else if (Example != null && !RegisterListener)
		{
			log.debug("Unregistering Listener!");
			StarfaceEventService SES = context.springApplicationContext().getBean(StarfaceEventService.class);
			SES.unsubscribe(Example);
			Example = null;
		}
	}
}
