package si.module.letsencryptv3.utility;

import org.shredzone.acme4j.Account;
import org.shredzone.acme4j.Authorization;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.challenge.Dns01Challenge;
import org.shredzone.acme4j.challenge.Http01Challenge;

public class Storage 
{
	public static Session S = null;
	public static Account AC = null;
	public static Authorization A = null;
	public static Dns01Challenge DNS = null;
	public static Http01Challenge HTTP = null;
	public static Order O = null;
}
