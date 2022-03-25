package si.module.letsencryptv3.Session;

import java.net.URI;

import org.apache.commons.logging.Log;
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

@Function(visibility=Visibility.Private, rookieFunction=false, description="Registers a KeyPair, and Creates a Session.")
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
		Log log = context.getLog();
		Session S = null;
		/*
		File PrivateKey = Standards.SessionPK();
		KeyPair SessionKP = null;
		if(PrivateKey.exists())
		{
			log.debug("Loading existing KeyPair from: " + PrivateKey.getAbsolutePath());
			try
			{
				FileReader FR = new FileReader(PrivateKey);
				SessionKP = KeyPairUtils.readKeyPair(FR);
				log.debug("KeyPair Successfully loaded.");
			}
			catch(Exception e)
			{
				LogHelper.EtoStringLog(log, e);
				log.debug("KeyPair Loading failed.");
				S = null;
				Success = false;
				return;
			}
		}
		else
		{
			log.debug("Generating new KeyPair");
			
			SessionKP = KeyPairUtils.createKeyPair(4096);

			if(!PrivateKey.getParentFile().exists())
			{
				if(!PrivateKey.getParentFile().mkdirs())
				{
					log.debug("Creating Folder: " + PrivateKey.getParentFile().getAbsolutePath() +" failed!");
				}
			}
			
			try
			{
				log.debug("Saving Session PrivateKey to: " + PrivateKey.getAbsolutePath());
				FileWriter FW = new FileWriter(PrivateKey);
				KeyPairUtils.writeKeyPair(SessionKP, FW);
				FW.close();
			}
			catch(Exception e)
			{
				LogHelper.EtoStringLog(log, e);
				log.debug("KeyPair Creation failed.");
				S = null;
				Success = false;
				return;
			}
		}
		
		*/
		
		log.debug("Creating Session:" + Service);
		URI URL = URI.create(Service);
		S = new Session(URL);
		Storage.S=S;
		Success = true;
	}//END OF EXECUTION

	
}
