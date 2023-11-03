package si.module.examples.redirection;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.logging.log4j.Logger;

import de.starface.bo.RedirectBusinessObject;
import de.starface.core.component.StarfaceComponentProvider;
import de.starface.integration.uci.java.v30.types.RedirectSetting;
import de.starface.integration.uci.java.v30.values.OrderDirection;
import de.starface.integration.uci.java.v30.values.RedirectSettingDestinationType;
import de.starface.integration.uci.java.v30.values.RedirectSettingProperties;
import de.starface.integration.uci.java.v30.values.RedirectSettingType;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.functions.entities.ResolveGroupData;
import de.vertico.starface.module.core.runtime.functions.entities.ResolveUserData;

@Function(name="RedirectionFK", visibility=Visibility.Private, rookieFunction=false, description="")
public class RedirectionFK implements IBaseExecutable 
{
	//##########################################################################################		
	
	@InputVar(label="TargetUser/TargetGroup", description="Zielbenutzer/Gruppe, f�r die die Umleitungen umgesetzt werden.", type=VariableType.STARFACE_ACCOUNT)
	public Integer STARFACE_ACCOUNT = -1;
	
	@InputVar(label="Impersonate_User", description="Der User, in dessem Name der Vorgang ausgeführt wird", type=VariableType.STARFACE_ACCOUNT)
	public Integer IMPERSONATE_ACCOUNT=-1;
	
	@InputVar(label="Numlenght", description="Nummernl�nge der internen Rufnummern, dies wird benötigt, um zwischen internen/externen Umleitungen zu unterscheiden. Z.b. interne Rufnummern von 200-999 == 3", type=VariableType.NUMBER)
	public Integer Numlenght = 0;
	
	@InputVar(label="Enable", description="Enabled == TRUE ==> Umleitungen Aktiviert", type=VariableType.BOOLEAN)
	public boolean Enable = false;
	
	@InputVar(label="EnableGroup", description="Muss f�r Gruppenumleitungen auf True Gesetzt werden.", type=VariableType.BOOLEAN)
	public boolean EnableGroup = false;
	
	@InputVar(label="RedirectSettingType", description="Welcher Umleitungstyp soll ge�ndert werden. (ALWAYS == IMMER || BUSY == BESETZT || TIMEOUT == Zeitüberschr.", valueByReferenceAllowed=true)
	public RedirectSettingType RST  = RedirectSettingType.ALWAYS;
	
	@InputVar(label="InternalRedirectionType", description="Wohin die internen Nummern umgeleitet werden sollen. NONE == IGNORIERT.", valueByReferenceAllowed=true)
	public RedirectionType IRT  = RedirectionType.NONE;
		
	@InputVar(label="InternalTarget", description="Umleitungsziel der internen Nummern.", type=VariableType.OBJECT)
	public Object ITarget = null;
	
	@InputVar(label="TimeoutInternal", description="Zeit, bis zur Zeit�berschreitung (interne Rufnummer)(Wird nur bei Zeitüb. Umleitung benötigt)", type=VariableType.NUMBER)
	public Integer TimeoutInternal = 0;
	
	@InputVar(label="ExternalRedirectionType", description="Wohin die externen Nummern umgeleitet werden sollen. NONE == IGNORIERT.", valueByReferenceAllowed=true)
	public RedirectionType ERT  = RedirectionType.NONE;
	
	@InputVar(label="ExternalTarget", description="Umleitungsziel der externen Nummern.", type=VariableType.OBJECT)
	public Object ETarget = null;
	
	@InputVar(label="TimeoutExternal", description="Zeit, bis zur Zeitüberschreitung (externe Rufnummer)(Wird nur bei Zeitüb. Umleitung benötigt)", type=VariableType.NUMBER)
	public Integer TimeoutExternal = 0;
	
	StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{	
		
		Logger log = context.getLog();

		RedirectBusinessObject RBO = (RedirectBusinessObject)context.provider().fetch(RedirectBusinessObject.class);
				
		String TargetInternal = "";
		String TargetExternal = "";
		
		try
		{
			switch(IRT)
			{
			case NONE:
				break;
			case VOICEMAILBOX:
				break;	
			case USER:
				ResolveUserData RU = new ResolveUserData();
				RU.accountId = ((Double) ITarget).intValue();
				RU.execute(context);
				TargetInternal = RU.primaryCallNumber;
				break;
			case GROUP:
				ResolveGroupData RG = new ResolveGroupData();
				RG.accountId = ((Double) ITarget).intValue();
				RG.execute(context);
				TargetInternal = RG.primaryCallNumber;
				break;

			case NUMBER:
				TargetInternal = (String)ITarget;
				break;
			}
		}
		catch(Exception e)
		{
			EtoStringLog(log, e);
		}
		
		try
		{
			switch(ERT)
			{
			case NONE:
				break;
			case VOICEMAILBOX:
				break;	
			case USER:
				ResolveUserData RU = new ResolveUserData();
				RU.accountId = ((Double) ETarget).intValue();
				RU.execute(context);
				TargetExternal = RU.primaryCallNumber;
				break;
			case GROUP:
				ResolveGroupData RG = new ResolveGroupData();
				RG.accountId = ((Double) ETarget).intValue();
				RG.execute(context);
				TargetExternal = RG.primaryCallNumber;
				break;

			case NUMBER:
				TargetExternal = (String)ETarget;
				break;
			}
		}
		catch(Exception e)
		{
			EtoStringLog(log, e);
		}
		
		List<RedirectSetting> Settings = RBO.getRedirectSettings(STARFACE_ACCOUNT, RST, null, RedirectSettingProperties.calledNumber, OrderDirection.ASCENDING);
				
	
		
		for(RedirectSetting RS: Settings)
		{
			if(RS.isGroupRedirect() && !EnableGroup)
			{
				log.debug("Skipping: " + RS.getCalledNumber() + " because it's a Group Number");
				continue;
			}
						
			if(Enable)
			{
				RS.getCalledNumber().equals("500");
				//Interne Nummern
				if(!(RS.getCalledNumber().length() > Numlenght) && !IRT.equals(RedirectionType.NONE))
				{			
					RS.setEnabled(Enable);
					if(RST.equals(RedirectSettingType.TIMEOUT)){RS.setTimeout(TimeoutInternal);}
					log.debug("Updating: " + RS.getCalledNumber() + " with :" + IRT.toString() + " " + TargetInternal);
					switch(IRT)
					{
					case NONE:
						break;
					case VOICEMAILBOX:
						RS.setDestinationType(RedirectSettingDestinationType.VOICEMAIL);
						RS.setDestination(RS.getMailboxes().get(0).getId());
						break;	
					case USER:
						RS.setDestinationType(RedirectSettingDestinationType.PHONENUMBER);
						RS.setDestination(TargetInternal);
						break;
					case GROUP:
						RS.setDestinationType(RedirectSettingDestinationType.PHONENUMBER);
						RS.setDestination(TargetInternal);
						break;
					case NUMBER:
						RS.setDestinationType(RedirectSettingDestinationType.PHONENUMBER);
						RS.setDestination(TargetInternal);
						break;
					}
				}
				
				if((RS.getCalledNumber().length() > Numlenght) && !ERT.equals(RedirectionType.NONE))
				{
					RS.setEnabled(Enable);
					if(RST.equals(RedirectSettingType.TIMEOUT)){RS.setTimeout(TimeoutExternal);}
					log.debug("Updating: " + RS.getCalledNumber() + " with :" + ERT.toString() + " " + TargetExternal);
					switch(ERT)
					{
					case NONE:
						break;
					case VOICEMAILBOX:
						RS.setDestinationType(RedirectSettingDestinationType.VOICEMAIL);
						RS.setDestination(RS.getMailboxes().get(0).getId());
						break;	
					case USER:
						RS.setDestinationType(RedirectSettingDestinationType.PHONENUMBER);
						RS.setDestination(TargetExternal);
						break;
					case GROUP:
						RS.setDestinationType(RedirectSettingDestinationType.PHONENUMBER);
						RS.setDestination(TargetExternal);
						break;
					case NUMBER:
						RS.setDestinationType(RedirectSettingDestinationType.PHONENUMBER);
						RS.setDestination(TargetExternal);
						break;
					}
				}
			}
			else
			{
				if(!(RS.getCalledNumber().length() > Numlenght) && !IRT.equals(RedirectionType.NONE))
				{	
					RS.setEnabled(false);
				}
				
				if((RS.getCalledNumber().length() > Numlenght) && !ERT.equals(RedirectionType.NONE))
				{
					RS.setEnabled(false);
				}
				
			}
			try
			{			
				if(RS.isEnabled())
				{
					if(RS.getDestination().isEmpty())
					{
						log.debug("Target: " + RS.toString()+" is empty! Skipping...");
						continue;
					}
				}
				RBO.setRedirectSetting(IMPERSONATE_ACCOUNT,STARFACE_ACCOUNT, RS);
			}
			catch(Exception e)
			{
				log.debug(STARFACE_ACCOUNT + " ==> " +RS.toString());
				EtoStringLog(log, e);
			}
		}
		
		
	}//END OF EXECUTION

	
	public enum RedirectionType
	{
		NONE,
		USER,
		GROUP,
		VOICEMAILBOX,
		NUMBER
	}
	
	public static void EtoStringLog(Logger log, Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}
	
}
