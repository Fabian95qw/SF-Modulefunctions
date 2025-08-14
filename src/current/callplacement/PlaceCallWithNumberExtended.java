package si.module.examples.callplacement;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import org.apache.logging.log4j.Logger;

import de.starface.ch.processing.bo.impl.CallServiceImpl;

import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, description="")
public class PlaceCallWithNumberExtended implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="STARFACE_USER_SOURCE", description="The Accountid of the Source (-1 for no Source Account)",type=VariableType.STARFACE_USER)
	public Integer STARFACE_USER_SOURCE=-1;
	
	@InputVar(label="STARFACE_USER_DESTINATION", description="The accountid of the Destination (-1 for ne Destination Account)",type=VariableType.STARFACE_USER)
	public Integer STARFACE_USER_DESTINATION=-1;
	
	@InputVar(label="DestinationNumberIsUserInput", description="",type=VariableType.BOOLEAN)
	public Boolean DestinationNumberIsUserInput=false;
	
	@InputVar(label="DestinationNumber", description="",type=VariableType.STRING)
	public String DestinationNumber="";
	
	@InputVar(label="OriginatingNumberIsUserInput", description="",type=VariableType.BOOLEAN)
	public Boolean OriginatingNumberIsUserInput=false;
	
	@InputVar(label="OriginatingNumber", description="",type=VariableType.STRING)
	public String OriginatingNumber="";
	
	@InputVar(label="CallerID", description="CallerID to set",type=VariableType.STRING)
	public String CallerID="";
	
	@InputVar(label="CallID", description="UUID to set for the call. if none is set a random one is generated",type=VariableType.STRING)
	public String CallID="";
	
	@InputVar(label="HoldActiveCall", description="Puts Active Call on Hold, if user already has one",type=VariableType.BOOLEAN)
	public Boolean HoldActiveCall=false;
		
	@InputVar(label="UsePSTN", description="Use PSTN Line for Call",type=VariableType.BOOLEAN)
	public Boolean UsePSTN=false;
	
	@InputVar(label="Outgoing", description="Define if its an Outgoing call",type=VariableType.BOOLEAN)
	public Boolean Outgoing = false;
	
	@InputVar(label="PhoneName", description="Which phone to use, if none is defined, uses users default phone",type=VariableType.STRING)
	public String PhoneName="";
		    	
	@OutputVar(label="GeneratedCallID", description="The generated CallID if none was set",type=VariableType.STRING)
	public String GeneratedCallID="";
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		CallServiceImpl CIMP = context.springApplicationContext().getBean(CallServiceImpl.class);
		
		try
		{	
			if(CallID == null || CallID.isEmpty())
			{
				GeneratedCallID = UUID.randomUUID().toString();
			}
			else
			{
				GeneratedCallID = CallID;
			}
			
			CIMP.placeCallWithNumber(STARFACE_USER_SOURCE, null, STARFACE_USER_DESTINATION, UUID.fromString(GeneratedCallID), OriginatingNumber, CallerID, DestinationNumber, OriginatingNumberIsUserInput, DestinationNumberIsUserInput, UsePSTN, HoldActiveCall, Outgoing, PhoneName);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
			EtoStringLog(log, e);
		}
		
	}//END OF EXECUTION

		public static void EtoStringLog(Logger log, Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.debug(sw.toString()); //
		}
		
	
	
}
