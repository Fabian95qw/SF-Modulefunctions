package si.module.modulefunction;

import org.apache.logging.log4j.Logger;
import de.starface.ch.routing.bo.impl.CallRoutingServiceImpl;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;

@Function(visibility=Visibility.Private, description="Sets Music on Hold for current caller")
public class SetMOH implements IBaseExecutable 
{
	//##########################################################################################
	
	
	@InputVar(label="Channelname", description="",type=VariableType.STRING)
	public String Channelname="";
	
	@InputVar(label="MusicOnHold", description="",type=VariableType.STRING)
	public String MusicOnHold="";
		    	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception
	{
		Logger log = context.getLog();
		
		log.debug("Trying to set Music on hold for:" + Channelname +" to: " + MusicOnHold);
	     CallRoutingServiceImpl CRSI = (CallRoutingServiceImpl)context.springApplicationContext().getBean(CallRoutingServiceImpl.class);	     
	     CRSI.setMusicOnHold(Channelname, MusicOnHold);
		
	}

	
}
