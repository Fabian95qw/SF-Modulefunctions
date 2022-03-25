package si.module.letsencryptv3.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class CreateTAR implements IBaseExecutable 
{
	//##########################################################################################
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		
		ProcessBuilder PB = new ProcessBuilder();
		
		List<String> Command = new ArrayList<String>();
		Command.add("tar");
		Command.add("-cvf");
		Command.add("/home/starface/letsencrypt.tar");
		Command.add("/home/starface/letsencrypt/");
		
		PB.command(Command);
		log.debug(Command);
		
		Process P = PB.start();
		
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(P.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			log.debug(line);
		}	
		
	}//END OF EXECUTION

	
}
