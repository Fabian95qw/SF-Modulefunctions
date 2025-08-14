package si.module.letsencryptv3.utility;

public class EnumHelper 
{
	public enum Challenge
	{
		NONE,
		HTTP,
		DNS
	}
	
	public enum ACME
	{
		SESSION,
		REGISTER,
		AUTH,
		CHALLENGE,
		CERTIFICATE
	}
	
}
