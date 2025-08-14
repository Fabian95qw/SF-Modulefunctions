package si.module.listenerinterface.listenerv2;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.ModuleInstanceProject;
import de.vertico.starface.module.core.model.ModuleProject;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.model.entryPoint.RpcEntryPoint;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.ModuleRegistry;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class InvokeFunction implements IBaseExecutable 
{
	//##########################################################################################
		
	@InputVar(label="InstanceID", description="The name of the Targetfunction.",type=VariableType.STRING)
	public String InstanceID="";
	
	@InputVar(label="XML-RPC-Entrypointname", description="The name of the XML-Entrypoint",type=VariableType.STRING)
	public String Entrypointname="";
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);
		
		ModuleInstanceProject MIP = MR.getInstance4Edit(InstanceID);
		
		ModuleProject MP = MR.getModule4Edit(MIP.getObject().getModuleId());
	
		Map<String, Object> Variables = new HashMap<String, Object>();
		
		Map<String, String> Data = new HashMap<String, String>();
		Data.put("Test", "Test");
		Variables.put("Data", Data);
		
		for(RpcEntryPoint RPC : MP.getObject().getRpcEntryPoints())
		{
			log.debug(RPC.getName() +" <==> " + Entrypointname);
			if(RPC.getName().equals(Entrypointname))
			{
				MR.callEntryPoint(InstanceID, RPC.getId(), Variables, false, null, null, null);
			}
		}
		


	}//END OF EXECUTION
	
	
}
