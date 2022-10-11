package si.module.letsencryptv3.certificate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.shredzone.acme4j.Certificate;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.Status;
import org.shredzone.acme4j.toolbox.AcmeUtils;
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

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class RequestCertificate implements IBaseExecutable 
{
	//##########################################################################################	

	@InputVar(label="Domain", description="",type=VariableType.STRING)
	public String Domain ="";
	
	@InputVar(label="Country", description="",type=VariableType.STRING)
	public String Country ="";
	
	@InputVar(label="State", description="",type=VariableType.STRING)
	public String State ="";
	
	@InputVar(label="Organization", description="",type=VariableType.STRING)
	public String Organization ="";
	
	@InputVar(label="OrganizationUnit", description="",type=VariableType.STRING)
	public String OrganizationUnit ="";
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
				
		File PrivateKey = Standards.CertPK();
			
		log.info("Trying to Request a Certificate");
							
		KeyPair KP = null;
		
		if(Standards.CertPK().exists())
		{
			log.debug("Loading Private Key");
			KP = KeyPairUtils.readKeyPair(new FileReader(Standards.CertPK()));
		}
		else
		{
			log.debug("Creating new Private Key");
			KP = KeyPairUtils.createKeyPair(4096);
			KeyPairUtils.writeKeyPair(KP, new FileWriter(Standards.CertPK()));
		}
		
			
		if(!PrivateKey.exists())
		{
			log.debug("Private Key is missing!");
			return;
		}
				
		log.debug("Loading existing KeyPair from: " + PrivateKey.getAbsolutePath());
		try
		{
			FileReader FR = new FileReader(PrivateKey);
			FR.close();
			log.debug("KeyPair Successfully loaded.");
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
			log.debug("KeyPair Loading failed.");
			Success = false;
			return;
		}
				
		try
		{
									
			byte[] CSR = null;
						
			log.debug("Creating CSR:");
			log.debug("Domain:" + Domain);
			log.debug("Country:" +Country);
			log.debug("Organization: " + Organization);
			log.debug("OrganizationUnit:" + OrganizationUnit);
			log.debug("State:" + State);
			
			ProcessBuilder PB = new ProcessBuilder();
						
			List<String> Command = new ArrayList<String>();
			Command.add("java");
			Command.add("-jar");
			Command.add("/var/starface/module/modules/repo/580396ad-77d9-4d7e-94d0-4c0f72c39993/CSRBuilder.jar");
			Command.add(Domain);
			Command.add(Country);
			Command.add(Organization);
			Command.add(OrganizationUnit);
			Command.add(State);
			
			PB.command(Command);
	
			log.debug(Command);
			
			Process P = PB.start();
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(P.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				log.debug(line);
			}	
			
			File CSRFile = Standards.CertCSR();
			CSR = Files.readAllBytes(CSRFile.toPath());		
			
			log.debug("Trying to Request Certificate");
						
			Order O = Storage.O;
			O.execute(CSR);
			
			while(O.getStatus().equals(Status.PENDING) || O.getStatus().equals(Status.PROCESSING))
			{
				Thread.sleep(5000);
				O.update();
			}
			
			if(O.getStatus().equals(Status.INVALID))
			{
				log.debug("Order returned Status INVALID!");
				Success = false;
			}
			else if(O.getStatus().equals(Status.VALID))
			{						
				log.info("Request Successful! Writing Certificate to HDD");
				
				Certificate C = O.getCertificate();
							
				X509Certificate Cert = C.getCertificate();								
				X509Certificate CACert = C.getCertificateChain().get(0);			
				X509Certificate CACerts[] = new X509Certificate[1];
				CACerts[0] = CACert;
				
				X509Certificate CAChain[] = new X509Certificate[2];
				
				CAChain[0] = Cert;
				CAChain[1] = CACert;
				
				log.debug("Saving Certificate to HDD");
				{
					if(Standards.Certificate().exists())
					{
						Standards.Certificate().delete();
					}				
					
					Standards.Certificate().createNewFile();
					FileWriter FW = new FileWriter(Standards.Certificate());
					AcmeUtils.writeToPem(Cert.getEncoded(), AcmeUtils.PemLabel.CERTIFICATE, FW);
					FW.flush();
					FW.close();
				}
				
				{
					if(Standards.CACertificate().exists())
					{
						Standards.CACertificate().delete();
					}
					Standards.CACertificate().createNewFile();
					FileWriter FW = new FileWriter(Standards.CACertificate());
					for(X509Certificate Certs : C.getCertificateChain())
					{
						AcmeUtils.writeToPem(Certs.getEncoded(), AcmeUtils.PemLabel.CERTIFICATE, FW);
					}
					FW.flush();
					FW.close();
				}
				
				log.debug("Setting Certificate for Webserver");
				
				if(Standards.TargetKeyStore().exists())
				{
					log.debug("Backing up Current Keystore");
					FileUtils.copyFile(Standards.TargetKeyStore(), Standards.KeyStoreBackup());
					log.debug("Deleting KeyStore.");
					Standards.TargetKeyStore().delete();
				}
				
				log.debug("Creating new Keystore");
				
				KeyStore KS = KeyStore.getInstance("jks");
				
				KS.load(null, "changeit".toCharArray());
				
				log.debug("Storing Keychain in KeyStore");
				
				KS.setKeyEntry("root", (Key)KP.getPrivate(), "changeit".toCharArray(), CAChain);
				KS.setKeyEntry("tomcat", (Key)KP.getPrivate(), "changeit".toCharArray(), CAChain);
				
				log.debug("Writing KeyChain to HDD");
				
				FileOutputStream FOS = new FileOutputStream(Standards.TargetKeyStore());
				
				KS.store(FOS, "changeit".toCharArray());
				
				log.info("Done");
					
				Success = true;
			}
		}
		catch(Exception e)
		{
			Success=false;
			LogHelper.EtoStringLog(log, e);
			log.info("Trying to restore old Keystore...");
			if(Standards.KeyStoreBackup().exists())
			{
				FileUtils.copyFile(Standards.KeyStoreBackup(), Standards.TargetKeyStore());
				log.info("Old Keystore sucessfully restored!");
			}

			
		}
		
	}//END OF EXECUTION

	
}
