package si.module.examples.callprocessing;

import org.apache.logging.log4j.Logger;

import de.starface.bo.callhandling.actions.ModuleBusinessObject;
import de.starface.callhandling.enums.HangupCause;

import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IAGIJavaExecutable;
import de.vertico.starface.module.core.runtime.IAGIRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.module.core.runtime.functions.callHandling.call.CallPhonenumber2;
import de.vertico.starface.module.core.runtime.functions.callHandling.call.GetCaller2;

@Function(visibility=Visibility.Private, description="")
public class CallProcessingExample implements IAGIJavaExecutable //IAGIJavaExecutable for call processing!
{
	//##########################################################################################
	
	@InputVar(label="Redirect_to", description="Redirect to this Number",type=VariableType.STRING)
	public String Redirect_to="";
		    	
	@InputVar(label="Timeout", description="Timeout for Redirect",type=VariableType.NUMBER)
	public Integer Timeout=30;
		    	
	@OutputVar(label="Success", description="If the call was sucessfully redirected",type=VariableType.BOOLEAN)
	public boolean Success=false;
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	

	@Override
	public void execute(IAGIRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		ModuleBusinessObject MBO = (ModuleBusinessObject)context.springApplicationContext().getBean(ModuleBusinessObject.class);
	
		if(context.getCallerChannelName() == null || context.getCallerChannelName().isEmpty()) //Check if Module has active channel
		{
			log.debug("There is no active call Channel!");
		}
		String CallerChannel = context.getCallerChannelName(); //Get the active Channel name
		
		MBO.answer(CallerChannel, 0); //Answer Channel
		
		if(!MBO.isChannelUp(CallerChannel)) {	return;	} //Stop execution if Caller is gone
		
		log.debug("Extracting Caller Information...");
		GetCaller2 GC = new GetCaller2(); //Resolve Caller Information using different Integrated ModuleBlock
		GC.execute(context);
		
		String CallerName = GC.callerName; //Extract Caller Information
		String CallerSignallingNumber = GC.callerSignallingNumber;
		
		
		log.debug("Parking Call..");
		
		if(!MBO.isChannelUp(CallerChannel)) {	return;	} //Stop execution if Caller is gone
		MBO.parkCall(CallerChannel, 3600); //Park Channel 
		
		
		if(!MBO.isChannelUp(CallerChannel)) {	return;	} //Stop execution if Caller is gone
		CallPhonenumber2 CPN = new CallPhonenumber2(); //Call Phonenumber using a building block
		CPN.callerName="Module -> " + CallerName; //Set the Callername using the resolved caller
		CPN.callerNumber=CallerSignallingNumber; //Set the Resolved Number to show in this call
		CPN.timeout = Timeout; //The Timeout deliviered from the buildingblock
		CPN.cancelCallLeg = CallerChannel; //Cancel the Call if the Caller hangs up.
		
		CPN.execute(context); // Try to call the Phonenumber;
		
		
		if(CPN.success) //If called number picked up
		{
			String CalledChannel = context.getCallerChannelName(); //Get the new Channelname für the called user
			if(!MBO.isChannelUp(CallerChannel))  //If the Caller just Hung up, after the called answered
			{	
				MBO.hangup(CalledChannel, HangupCause.NORMAL_CLEARING); //Hang up the Called Channel
				return;	
			}
			MBO.unparkCall(CallerChannel); //Unpark the Caller, which automatically connects to the called channel
			//Module can no longer interact with the call, because two endpoints are connected
		}
				
	}//END OF EXECUTION


	
	
}
