package si.module.examples.callplacement;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.Logger;

import de.starface.bo.callhandling.actions.CallBusinessObject;
import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class PlaceCallWithNumber implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="STARFACE_USER_SOURCE", description="The Accountid of the Source (-1 for no Source Account)",type=VariableType.STARFACE_USER)
	public Integer STARFACE_USER_SOURCE=-1;
	
	@InputVar(label="STARFACE_USER_DESTINATION", description="The accountid of the Destination (-1 for ne Destination Account)",type=VariableType.STARFACE_USER)
	public Integer STARFACE_USER_DESTINATION=-1;
	
	@InputVar(label="DestinationNumber", description="",type=VariableType.STRING)
	public String DestinationNumber="";
	
	@InputVar(label="OriginatingNumber", description="",type=VariableType.STRING)
	public String OriginatingNumber="";
	
	@InputVar(label="CallID", description="UUID to set for the call. if none is set a random one is generated",type=VariableType.STRING)
	public String CallID="";
	
	@InputVar(label="UsePSTN", description="Use PSTN Line for Call",type=VariableType.BOOLEAN)
	public Boolean UsePSTN=false;
	
	@InputVar(label="Outgoing", description="Define if its an Outgoing call",type=VariableType.BOOLEAN)
	public Boolean Outgoing = false;
		    	
	@OutputVar(label="GeneratedCallID", description="The generated CallID if none was set",type=VariableType.STRING)
	public String GeneratedCallID="";
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		CallBusinessObject CBO = context.provider().fetch(CallBusinessObject.class);
		
		try
		{
			GeneratedCallID = CBO.placeCallWithNumber(STARFACE_USER_SOURCE, STARFACE_USER_DESTINATION, DestinationNumber, OriginatingNumber, CallID, UsePSTN, Outgoing);
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
