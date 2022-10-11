package si.module.letsencryptv3.utility;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class CleanUp implements IBaseExecutable 
{
	//##########################################################################################

    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		log.debug("Cleaning");
		
		if(Standards.SessionPK().exists())
		{
			Standards.SessionPK().delete();
			if(Standards.SessionPK().exists())
			{
				log.debug("Deletion of: " + Standards.SessionPK().getAbsolutePath() + " failed");
			}
				
		}
		
		if(Standards.CertPK().exists())
		{
			Standards.CertPK().delete();
			if(Standards.CertPK().exists())
			{
				log.debug("Deletion of: " + Standards.CertPK().getAbsolutePath() + " failed");
			}
		}
		
		if(Standards.CertCSR().exists())
		{
			Standards.CertCSR().delete();
			if(Standards.CertCSR().exists())
			{
				log.debug("Deletion of: " + Standards.CertCSR().getAbsolutePath() + " failed");
			}
		}
		
		if(Standards.Certificate().exists())
		{
			Standards.Certificate().delete();
			if(Standards.Certificate().exists())
			{
				log.debug("Deletion of: " + Standards.Certificate().getAbsolutePath() + " failed");
			}
		}
		
		if(Standards.CACertificate().exists())
		{
			Standards.CACertificate().delete();
			if(Standards.CACertificate().exists())
			{
				log.debug("Deletion of: " + Standards.CACertificate().getAbsolutePath() + " failed");
			}
		}

		if(Standards.RegistrationURI().exists())
		{
			Standards.RegistrationURI().delete();
			if(Standards.RegistrationURI().exists())
			{
				log.debug("Deletion of: " + Standards.RegistrationURI().getAbsolutePath() + " failed");
			}
		}
		
		if(Standards.KeyStoreBackup().exists())
		{
			Standards.KeyStoreBackup().delete();
			if(Standards.KeyStoreBackup().exists())
			{
				log.debug("Deletion of: " + Standards.KeyStoreBackup().getAbsolutePath() + " failed");
			}
		}
		
		Storage.A=null;
		Storage.DNS=null;
		Storage.HTTP=null;
		Storage.AC=null;
		Storage.S=null;
		
		log.debug("Cleanup Completed");
		
	}//END OF EXECUTION

	
}
