package si.module.letsencryptv3.registration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.security.KeyPair;

import org.apache.logging.log4j.Logger;
import org.shredzone.acme4j.Account;
import org.shredzone.acme4j.AccountBuilder;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.util.KeyPairUtils;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.letsencryptv3.utility.LogHelper;
import si.module.letsencryptv3.utility.Standards;
import si.module.letsencryptv3.utility.Storage;


@Function(visibility=Visibility.Private, rookieFunction=false, description="Returns the Registration Location for an Account")
public class RegisterACMEAccount implements IBaseExecutable 
{
	//#########################################################################################
	
	@InputVar(label="E-Mail Address", description="",type=VariableType.STRING)
	public String EMail="";
		
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public boolean Success =false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		log.info("Trying to Register: "+ EMail);
		
		if(EMail.isEmpty())
		{
			log.error("No E-Mail provided. Aborting..");
			return;
		}
		
		Account AC = null;
		
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
				AC = null;
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
				AC = null;
				Success = false;
				return;
			}
		}
				
		Session S = Storage.S;
		URI RegistrationLocation = null;
				
		if(Standards.RegistrationURI().exists())
		{
			log.debug("Loading Registration file");
			BufferedReader BR = new BufferedReader(new FileReader(Standards.RegistrationURI()));
			String SUri = BR.readLine();
			BR.close();
			RegistrationLocation = URI.create(SUri);
		}
		
		if(RegistrationLocation == null)
		{
			AccountBuilder AB = new AccountBuilder();
			AB.addContact("mailto:" +EMail);
			
			//try
			//{
				log.info("Accepting Agreement");
				AB.agreeToTermsOfService();
				log.debug("Accepted Agreement");
				AB.useKeyPair(SessionKP);
				AC = AB.create(S);
				log.debug("Registration Successful.");
				RegistrationLocation = AC.getLocation().toURI();
			//}
			/*
			catch(CertException e)
			{
				A = A.bind(S, e.getLocation());
				RegistrationLocation = R.getLocation().toURI();
				log.debug("E-Mail is already registered. Getting existing Registration");
			}
			*/
			
			log.debug("Saving RegistrationURI");
			FileWriter FW = new FileWriter(Standards.RegistrationURI());
			FW.write(RegistrationLocation.toString());	
			FW.close();
		}
		else
		{
			log.debug("Binding existing Registration to Session");
			AccountBuilder AB = new AccountBuilder();
			AB.onlyExisting();
			AB.useKeyPair(SessionKP);
			AC = AB.create(S);
		}
		Storage.AC=AC;
		Success = true;

	}//END OF EXECUTION

	
}
