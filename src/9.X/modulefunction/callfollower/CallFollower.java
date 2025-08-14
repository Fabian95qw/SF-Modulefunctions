package si.module.examples.callfollower;

import de.starface.bo.callhandling.actions.ModuleBusinessObject;
import de.starface.callhandling.callmodel.enums.CallLegState;
import de.starface.ch.processing.bo.api.pojo.data.PojoCall;
import de.starface.ch.processing.bo.api.pojo.data.PojoCallLeg;
import de.starface.integration.uci.java.v30.types.Call;
import de.starface.integration.uci.java.v30.values.CallState;
import de.vertico.starface.module.core.runtime.IAGIRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.functions.callHandling.call.GetCalledNumber;
import de.vertico.starface.module.core.runtime.functions.callHandling.call.GetCaller2;
import de.vertico.starface.module.core.runtime.functions.callHandling.call.IsInternalCall;

import de.vertico.starface.module.core.runtime.functions.entities.ResolveUserData;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.Logger;
import java.util.HashMap;

public class CallFollower implements Runnable
{
	private String CallerChannelNameOrig = "";
	private IAGIRuntimeEnvironment context = null;
	private Logger log = null;
	private String Callednumber = "";
	private String Callernumber = "";
	private String Callername = "";
	private UUID CID = null;

	private Map<String, Object> CallInformation = null;

	private boolean isInternalCall = false;
	private static List<String> RunningUUIDs = new ArrayList<String>();

	private static boolean inUse = false;

	// private ParticipantType LastCallerType = null;
	// private ParticipantType LastCalledType2 = null;
	private boolean hasError = false;
	private Integer ErrorCount = 0;

	public CallFollower(IAGIRuntimeEnvironment context)
	{
		CallInformation = new HashMap<String, Object>();
		this.context = context;
		log = context.getLog();

		CallerChannelNameOrig = context.getCallerChannelName();

		ModuleBusinessObject MBO = (ModuleBusinessObject) context.provider().fetch(ModuleBusinessObject.class);
		PojoCall PC = MBO.getPojoCallByChannelName(CallerChannelNameOrig);
		CID = PC.getCallId();

		CallInformation.put("Timestamp_Callstart", new Date());

		GetCalledNumber GCN = new GetCalledNumber();
		try
		{
			GCN.execute(context);
			Callednumber = GCN.calledNumber;

		}
		catch (Exception e)
		{
			EtoStringLog(this.log, e);
		}

		CallInformation.put("Callednumber", this.Callednumber);

		try
		{
			IsInternalCall ISC = new IsInternalCall();
			ISC.execute(context);
			CallInformation.put("InternalCall", ISC.internCall);
			isInternalCall = ISC.internCall.booleanValue();
			log.debug("[CF]" + CID + " Is Internal Call: " + ISC.internCall);
		}
		catch (Exception e)
		{
			EtoStringLog(this.log, e);
		}

		GetCaller2 GC = new GetCaller2();

		try
		{
			GC.execute(context);
			Callernumber = GC.callerExtNumber;
			if (this.isInternalCall)
			{

				Callernumber = GC.callerIntNumber;
			}
			else if (GC.callerExtNumber.isEmpty() || GC.callerExtNumber.equals("0"))
			{

				Callernumber = GC.callerIntNumber;
			}
			Callername = GC.callerName;
		}
		catch (Exception e)
		{

			Callernumber = "Anonymous";
			Callername = "Anonymous";
			EtoStringLog(this.log, e);
		}
		log.debug("[CF]" + CID + " Callername: " + this.Callername);
		log.debug("[CF]" + CID + " Callernumber: " + this.Callernumber);
		log.debug("[CF]" + CID + " Callednumber: " + this.Callednumber);


		CallInformation.put("Callername", this.Callername);
		CallInformation.put("Callernumber", this.Callernumber);
	}

	public void run()
	{
		ModuleBusinessObject MBO = (ModuleBusinessObject) context.provider().fetch(ModuleBusinessObject.class);

		log.debug("[CF]" + CID + " Follwing Call!");

		PojoCall PC = MBO.getPojoCallByChannelName(CallerChannelNameOrig);

		CallInformation.put("UUID", CID.toString());

		Call CallStateInfo = MBO.getCallState(PC.getCaller().getChannelName(), 0);

		log.debug("[CF] " + CID + " " + CallStateInfo.getState().toString());

		if (UUIDhaswatchdog(CID.toString(), this.log))
		{
			log.debug("[CF] The UUID: " + CID.toString() + " is already being watched.");
			return;
		}

		Boolean Pickup = false;

		CallState LastState = CallState.INCOMING;

		while (MBO.getCallState(CallerChannelNameOrig, 0) != null)
		{
			try
			{
				Thread.sleep(1000L);

				CallStateInfo = MBO.getCallState(CallerChannelNameOrig, 0);

				if (CallStateInfo == null || CallStateInfo.getState() == null)
				{

					log.debug("[CF]" + CID.toString() + " is null Probably hangup!");
					break;
				}

				if (!CallStateInfo.getState().equals(LastState))
				{
					log.debug("[CF]" + CID + " Callstate: " + LastState.toString() + " ==> "
							+ CallStateInfo.getState().toString());
				}

				if (CallStateInfo.getState().equals(CallState.PARKED))
				{
					log.debug("[CF]" + CID + " Call is Parked");
					LastState = CallStateInfo.getState();
					continue;
				}

				PC = MBO.getPojoCallByChannelName(CallerChannelNameOrig);
				if (PC == null)
				{
					log.debug("[CF]" + CID + " Call is null!");
					break;
				}

				List<PojoCallLeg> PJCalled = PC.getCalled();

				if (CallStateInfo.getState().equals(CallState.CONNECTED) && !LastState.equals(CallState.CONNECTED))
				{
					PojoCallLeg PJCalledTarget = null;

					for (PojoCallLeg PCL : PJCalled)
					{
						if (PCL.getCallLegState().equals(CallLegState.LINKED))
						{
							PJCalledTarget = PCL;
						}
					}

					if (PJCalledTarget != null)
					{
						this.log.debug("[CF] " + CID + " Call is linked! Finding out to who");
						this.log.debug("[CF] " + CID + " TargetAccountID: " + PJCalledTarget.getParticipantId());
						CallInformation.put("Pickup", true);
						if (PJCalledTarget.getParticipantId().intValue() == -1
								|| PJCalledTarget.getParticipantId().intValue() == 0)
						{
							this.log.debug("[CF]" + CID + " Call was not Picked up by an User! Not Resolving....");
						}
						else
						{
							this.log.debug("[CF]" + CID + " Call was Picked up by an User! Resolving...");
							ResolveUserData RUD = new ResolveUserData();
							RUD.accountId = PJCalledTarget.getParticipantId().intValue();

							RUD.execute(this.context);
							log.debug("[CF]" + CID + " TargetUser: " + RUD.firstName + " " + RUD.familyName);
							log.debug("[CF]" + CID + "LoginID: " + RUD.loginId);

							CallInformation.put("Pickup_Firstname", RUD.firstName);
							CallInformation.put("Pickup_Lastname", RUD.familyName);
							CallInformation.put("Pickup_LoginID", RUD.loginId);
							CallInformation.put("Pickup_AccountID", Integer.valueOf(RUD.accountId));
							CallInformation.put("Timestamp_Pickup", new Date());
							CallInformation.put("Call_Finished", false);
						}
					}
				}
				else if (LastState.equals(CallState.CONNECTED) && !CallStateInfo.getState().equals(CallState.CONNECTED))
				{
					log.debug("[CF]" + CID + " Call is no Longer Linked! Following the Call..");
				}
				LastState = CallStateInfo.getState();
			}
			catch (Exception e)
			{
				EtoStringLog(log, e);
				hasError = true;
				ErrorCount = ErrorCount + 1;
				if (ErrorCount > 10)
				{
					log.error("The Call with ID: " + CID + " encountered too many errors!");
					break;
				}
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e1)
				{

				}
			}
		}

		log.debug("[CF]" + CID + " Waiting for Hangup...");
		while (MBO.getCallState(CallerChannelNameOrig, 0) != null)
		{
			try
			{
				Thread.sleep(1000L);
			}
			catch (InterruptedException e)
			{
				EtoStringLog(this.log, e);
			}
		}

		this.log.debug("[CF]" + CID + " Hangup Occured!");

		if (hasError)
		{
			this.CallInformation.put("Timestamp_Hangup", new Date(0));
		}
		else
		{
			this.CallInformation.put("Timestamp_Hangup", new Date());
		}

		this.CallInformation.put("Call_Finished", Boolean.valueOf(true));
		this.CallInformation.put("Pickup", Pickup);

		RemoveWatchdog(CID.toString(), this.log);

		//TODO: Do something with the Information Map;
		
	}

	private static void RemoveWatchdog(String UUID, Logger log)
	{
		while (inUse)
		{

			try
			{
				Thread.sleep(100L);
			}
			catch (InterruptedException e)
			{

				EtoStringLog(log, e);
			}
		}
		inUse = true;

		try
		{
			log.debug("[WD] Removing Watchdog: " + UUID);
			RunningUUIDs.remove(UUID);
		}
		catch (Exception e)
		{

			EtoStringLog(log, e);
		}
		inUse = false;
	}

	private static boolean UUIDhaswatchdog(String UUID, Logger log)
	{
		if (RunningUUIDs.contains(UUID))
		{

			log.debug("[WD] Watchdog refused. Call: " + UUID + " already has a Watchdog");
			return true;
		}

		while (inUse)
		{

			try
			{
				Thread.sleep(100L);
			}
			catch (InterruptedException e)
			{

				EtoStringLog(log, e);
			}
		}
		inUse = true;

		try
		{
			log.debug("[WD] Adding Watchdog: " + UUID);
			RunningUUIDs.add(UUID);
		}
		catch (Exception e)
		{

			EtoStringLog(log, e);
		}
		inUse = false;
		return false;
	}


	private static void EtoStringLog(Logger log, Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}
	
	
}
