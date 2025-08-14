package si.module.letsencryptv3.Session;

import java.net.URI;

import org.apache.logging.log4j.Logger;
import org.shredzone.acme4j.Session;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.letsencryptv3.utility.Storage;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Creates a Session.")
public class RegisterSession implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="Service", description="",type=VariableType.STRING)
	public String Service ="";;
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		Session S = null;

		log.info("Creating Session:" + Service);
		URI URL = URI.create(Service);
		S = new Session(URL);
		Storage.S=S;
		Success = true;
	}//END OF EXECUTION

	
}
