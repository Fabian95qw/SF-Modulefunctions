package si.module.letsencryptv3.utility;

import java.io.File;

public class Standards 
{
	
	public static String Workdir()
	{
		return "/home/starface/letsencrypt/";
	}
	
	public static File SessionPK()
	{
		return new File("/home/starface/letsencrypt/SessionPK.pem");
	}
	
	public static File CertPK()
	{
		return new File("/home/starface/letsencrypt/CertPK.pem");
	}
	
	public static File CertPKSource()
	{
		return new File("/opt/tomcat/ssl/tomcatkey.pem");
	}
	
	
	public static File RegistrationURI()
	{
		return new File("/home/starface/letsencrypt/RegistrationURI.txt");
	}

	public static File CertCSR()
	{
		return new File("/home/starface/letsencrypt/CertCSR.csr");
	}
	
	public static File Certificate()
	{
		return new File("/home/starface/letsencrypt/Certificate.cer");
	}
		
	public static File CACertificate()
	{
		return new File("/home/starface/letsencrypt/CACertificate.cer");
	}
	
	public static File KeyStoreBackup()
	{
		return new File("/home/starface/letsencrypt/KeyStoreBackup.jks");
	}
	
	public static File TargetKeyStore()
	{
		return new File("/opt/tomcat/ssl/tomcat.keystore");
	}
	
}
