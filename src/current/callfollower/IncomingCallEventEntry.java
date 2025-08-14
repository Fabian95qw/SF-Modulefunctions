package si.module.examples.callfollower;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.Logger;


import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IAGIJavaExecutable;
import de.vertico.starface.module.core.runtime.IAGIRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;

@Function(name="IncomingCallEventEntry",visibility=Visibility.Private, description="")
public class IncomingCallEventEntry implements IAGIJavaExecutable
{
	//##########################################################################################
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IAGIRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		log.debug("Incoming Call...");
		
		try
		{
			CallFollower CF = new CallFollower(context);
			Thread T = new Thread(CF);
			T.start();
		}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.debug(sw.toString()); //
		}
		
		Thread.sleep(100);

	}//END OF EXECUTION

	
}
