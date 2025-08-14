package si.module.iqueueposteditingtime;


import de.starface.ch.processing.model.data.participants.Account;
import de.starface.ch.processing.model.data.participants.User;

import de.starface.core.component.StarfaceComponentProvider;

import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.starface.ch.processing.model.CallModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.Logger;

import de.starface.callhandling.busylampfieldstates.UpdateType;
import de.starface.callhandling.busylampfieldstates.UserStateObserver;
import de.starface.callhandling.gate.StaticEventGate;
import de.starface.ch.processing.model.data.participants.Account.AgentPostEditingEvent;

@Function(visibility = Visibility.Private, rookieFunction = false, description = "")
public class SetPostEditingTime implements IBaseExecutable
{
	@InputVar(label = "STARFACE_GROUP", description = "", type = VariableType.STARFACE_GROUP)
	public Integer STARFACE_GROUP = Integer.valueOf(-1);
	
	@InputVar(label = "STARFACE_USER", description = "", type = VariableType.STARFACE_USER)
	public Integer STARFACE_USER = Integer.valueOf(-1);
	
	@InputVar(label = "Seconds", description = "", type = VariableType.NUMBER)
	public Integer Seconds = 0;
	
	@InputVar(label = "MaxSeconds", description = "", type = VariableType.NUMBER)
	public Integer MaxSeconds = 0;
	
	@InputVar(label = "Extend", description = "", type = VariableType.BOOLEAN)
	public boolean Extend=false;
	
	@OutputVar(label = "Timestampend", description = "", type = VariableType.NUMBER)
	public Long Timestamp = 0L;
	
	@OutputVar(label = "Enddate", description = "", type = VariableType.STRING)
	public String Enddate="";
		
	StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance();

	public void execute(IRuntimeEnvironment context) throws Exception
	{
		Logger log = context.getLog();
		
		CallModel CM = (CallModel)context.provider().fetch(CallModel.class);
		UserStateObserver USO = (UserStateObserver)context.provider().fetch(UserStateObserver.class);
		
		Account A = CM.getAccountMapper().getAccountById(STARFACE_USER);
		
		if(Extend)
		{
			Timestamp = A.getTimestampAgentPostEditingEnd();
			if(Timestamp == 0L)
			{
				Timestamp = System.currentTimeMillis();
			}
			
			Long TimestampMax = Timestamp+(MaxSeconds*1000);
			
			Timestamp = Timestamp + (Seconds*1000);
			if(Timestamp > TimestampMax)
			{
				Timestamp = TimestampMax;
			}
		}
		else
		{
			Timestamp = System.currentTimeMillis();
			Timestamp = Timestamp + (Seconds*1000);
		}
		
		
		
		log.debug("New Timestamp: " + Timestamp +" for AccountID: " + STARFACE_USER + "in Group:  " +STARFACE_GROUP);
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date D = new Date(Timestamp);
		Enddate = SDF.format(D);		
		
		A.setTimestampAgentPostEditingEnd(Timestamp);
		USO.update((User)A, UpdateType.AGENT);
		
		AgentPostEditingEvent APEE = new AgentPostEditingEvent(STARFACE_USER, STARFACE_GROUP, Timestamp);
		
		StaticEventGate.publishInternal(APEE, log);
		
	}
}
