package si.module.examples.userstate;

import org.apache.logging.log4j.Logger;

import de.starface.ch.processing.bo.api.events.TelephonyStateChangedEvent;
import de.starface.core.component.StarfaceComponentProvider;
import de.starface.core.component.events.StarfaceEventService;
import de.starface.integration.uci.java.v30.values.TelephonyState;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class SetUserTelephonyState implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="STARFACE_USER", description="",type=VariableType.STARFACE_USER)
	public Integer STARFACE_USER=-1;
	
	@InputVar(label="Telephonystate", description="", valueByReferenceAllowed=true)
	public TelephonyState TS  = TelephonyState.ACTIVE;
			
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		StarfaceEventService ES = (StarfaceEventService)context.provider().fetch(StarfaceEventService.class);
		TelephonyStateChangedEvent TCE = new TelephonyStateChangedEvent(STARFACE_USER, TS);
		log.debug("Publishing Telephony Changed Event");
		ES.publish(TCE, context.getLog());

	}//END OF EXECUTION	
}
