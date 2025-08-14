package si.module.examples.voicemails;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.Logger;

import de.starface.bo.VoicemailListBusinessObject;

import de.starface.integration.uci.java.v30.types.VoicemailList;
import de.starface.integration.uci.java.v30.types.VoicemailListEntry;
import de.starface.integration.uci.java.v30.values.OrderDirection;
import de.starface.integration.uci.java.v30.values.VoicemailListEntryFolder;
import de.starface.integration.uci.java.v30.values.VoicemailListEntryProperties;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.VoiceboxHandler;
import de.vertico.starface.persistence.databean.voicemail.Account2VoicemailuserBean;
import de.vertico.starface.persistence.databean.voicemail.VoicemailUserList;

@Function(visibility=Visibility.Private, description="")
public class GetVoiceMails implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="*9[Nr]", description="",type=VariableType.STRING)
	public String Voicemailno="";
	
	@InputVar(label="STARFACE_ACCOUNT", description="",type=VariableType.STARFACE_ACCOUNT)
	public Integer STARFACE_ACCOUNT=-1;
	
	@InputVar(label="Dateformat", description="",type=VariableType.STRING)
	public String DateFormat="";
	
	@OutputVar(label="Voicemails", description="",type=VariableType.LIST)
	List<Map<String,Object>> Voicemails = new ArrayList<Map<String, Object>>();
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		SimpleDateFormat SDF=null;
		try
		{
			SDF = new SimpleDateFormat(DateFormat);
		}
		catch(Exception e)
		{
			log.error("Date/Time Format error!" + e.getMessage());
			return;
		}
		
		VoiceboxHandler VOB = (VoiceboxHandler)context.springApplicationContext().getBean(VoiceboxHandler.class); //Fetch the Voiceboxhandler
		log.debug("Using ID: "+STARFACE_ACCOUNT + " with target: "+ Voicemailno);
		
		VoicemailUserList VBs = VOB.getVoicemailBoxesForAccountId(STARFACE_ACCOUNT); //List all Voicemailboxes this User/Group is allowed to See

		String Voicemailid = "";
		
		for(Account2VoicemailuserBean Entry: VBs.getList()) //Check if this Voicemailbox is the one we're Looking for.
		{
			log.trace(Entry.getMailbox() +" <==> " + Voicemailno + " " + Entry.getMailbox().equals(Voicemailno));
			if(Entry.getMailbox().equals(Voicemailno))
			{
				log.debug("Found the correct Voicemailbox!");
				Voicemailid = ""+Entry.getId();
				break;
			}
		}
		
		if(Voicemailid.isEmpty())
		{
			log.error("The Voicemailbox: "+ Voicemailno +" does not exist for User: " + STARFACE_ACCOUNT);
			return;
		}
		
	
		VoicemailListBusinessObject VBO = (VoicemailListBusinessObject)context.springApplicationContext().getBean(VoicemailListBusinessObject.class);
		Date D = new Date();
		
		
		VoicemailList VMList = null;
		
		try
		{
			//Once the Correct Voicemailbox is found list all the Voicemails of the Last 365 Days from the Inbox of this Voicemailbox
			VMList= VBO.getVoicemailList(STARFACE_ACCOUNT, D , DateUtils.addDays(D, -365), "",  VoicemailListEntryFolder.INBOX , VoicemailListEntryProperties.callDescription, OrderDirection.ASCENDING, 0, Integer.MAX_VALUE);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
			return;
		}
		
		
		log.debug("Searching trough Voicemails. Total of:" + VMList.getEntries().size());
		for(VoicemailListEntry Entry : VMList.getEntries())
		{
			//log.debug(Entry.getMailboxId() +" <==> "+ Voicemailid+ " " + Entry.getMailboxId().equals(Voicemailid));
			//Turn Each Entry into a Map
			if(Entry.getMailboxId().equals(Voicemailid))
			{
				Map<String, Object> M = new HashMap<String, Object>();
				log.trace("#########################");
				log.trace("Id:"+Entry.getId());
				log.trace("Description:"+Entry.getCallDescription());
				log.trace("CallerNumber:"+Entry.getCallerNumber());
				log.trace("CalledNumber:"+Entry.getCalledNumber());
				log.trace("Duration:"+Entry.getDuration());
				log.trace("Folder:"+Entry.getFolder());
				log.trace("MailboxId:"+Entry.getMailboxId());
				log.trace("MailboxName:"+Entry.getMailboxName());
				log.trace("Starttime:"+Entry.getStartTime().toString());
				
				File F = VBO.getVoicemailFile(STARFACE_ACCOUNT, Entry.getId());
				log.trace("File: " + F.getAbsolutePath());
				
				M.put("id", Entry.getId());
				M.put("callednumber", Entry.getCalledNumber());
				M.put("callernumber", Entry.getCallerNumber());
				M.put("duration", Entry.getDuration());
				M.put("starttime", SDF.format(Entry.getStartTime()));
				M.put("mailboxid", Entry.getMailboxId());
				M.put("mailboxname", Entry.getMailboxName());
				M.put("file", F.getAbsolutePath());
				Voicemails.add(M);
			}
		}
		log.debug("Amount of Voicemails for Alert: "+ Voicemails.size());
		
	}//END OF EXECUTION


	
	
}
