package si.module.modulefunction;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;

import de.starface.ch.routing.tracking.CallRoutingContext;
import de.starface.ch.routing.tracking.CallRoutingContextStore;
import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IAGIJavaExecutable;
import de.vertico.starface.module.core.runtime.IAGIRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class RepairCallPhoneNumber implements IAGIJavaExecutable
{
	//##########################################################################################
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################

	@Override
	public void execute(IAGIRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		log.debug("Checking if CallRoutingContext needs to be repaired");
		
		CallRoutingContextStore CRCS = context.provider().fetch(CallRoutingContextStore .class);
		CallRoutingContext CRC = null;
		try
		{
			CRC = CRCS.retrieveContext();
			if(CRC != null)
			{
				log.debug("CallRoutingContext seems ok: "+ CRC.toString());
			}
		}
		catch(Exception e)
		{
			log.debug("CallRoutingContext is lost. Generating new One...");
			  try
			  {
				 CRC = CRCS.setupContext();
				  if(CRC != null)
				  {
					  log.debug("New CallRoutingContext:" + CRC.toString());
				  }
			  }
			  catch(Exception e2)
			  {
				  log.debug("Setup of Context failed! Module will fail to Call!");
				  EtoStringLog(log, e2);
			  }
			  
		}
		
	}
	
	private static void EtoStringLog(Log log, Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}
	

	
}
