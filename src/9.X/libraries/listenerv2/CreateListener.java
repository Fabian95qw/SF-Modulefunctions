package si.module.listenerinterface.listenerv2;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.ModuleInstanceProject;
import de.vertico.starface.module.core.model.ModuleProject;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.model.entryPoint.RpcEntryPoint;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.listenerinterface.listenerv2.utility.LogHelper;
import si.module.listenerinterface.listenerv2.utility.EnumHelper.ListenerType;

@Function(visibility=Visibility.Public, rookieFunction=false, description="")
public class CreateListener implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="ListenerUUID", description="If Listener with this UUID already exists, no new one will be created",type=VariableType.STRING)
	public String ListenerUUID="";
	
	@InputVar(label="InstanceUUID", description="InstanceUUID to Invoke",type=VariableType.STRING)
	public String InstanceUUID="";
	
	@InputVar(label="XML-RPC-Entrypointname", description="XML-RPC Entrypoint to Invoke",type=VariableType.STRING)
	public String Entrypointname="";
		
	@InputVar(label="ListenerType", description="", valueByReferenceAllowed=true)
	public ListenerType LT  = ListenerType.NONE;
	
	@OutputVar(label="Success", description="Return true, if Listene was sucessfully created. Returns false if listener already exists.",type=VariableType.BOOLEAN)
	public boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
				
		ListenerManager LM = (ListenerManager)context.provider().fetch(ListenerManager.class);	

		if(!LM.isRunning())
		{
			try 
			{
				LM.startComponent();
			} 
			catch (Throwable e) 
			{
				LogHelper.EtoStringLog(log, e);
			}
		}
		
		if(LM.getInstance(ListenerUUID) == null)
		{
			
			ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);
			ModuleInstanceProject MIP = MR.getInstance4Edit(InstanceUUID);
			ModuleProject MP = MR.getModule4Edit(MIP.getObject().getModuleId());
			
			for(RpcEntryPoint RPC : MP.getObject().getRpcEntryPoints())
			{
				log.debug(RPC.getName() +" <==> " + Entrypointname);
				if(RPC.getName().equals(Entrypointname))
				{
					log.debug("Registering:" + ListenerUUID);
					try
					{
						Success = LM.registerListener(ListenerUUID, InstanceUUID, RPC.getId(), LT, context);
					}
					catch(Exception e)
					{
						LogHelper.EtoStringLog(log, e);
						Success=false;
					}
					return;
				}
			}
			log.error("Entrypoint:" + Entrypointname +" does not exist!");

		}
		else
		{
			Success = false;
		}
	}//END OF EXECUTION

	
}
