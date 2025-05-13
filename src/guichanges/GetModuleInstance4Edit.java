package si.module.guichanges;

import java.io.PrintWriter;
import java.io.StringWriter;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.ModuleInstanceProject;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, description="Get the Module Instance to Edit")
public class GetModuleInstance4Edit implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="InstanceID", description="The InstanceID to Edit",type=VariableType.STRING)
	public String InstanceID = "";
		    	
	@OutputVar(label="ModuleInstanceProject", description="The Object that is used with the other building blocks",type=VariableType.OBJECT)
	public Object MIPObject=null;
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public boolean Success=false;

	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{		
		ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);
		try
		{
			ModuleInstanceProject MIP = MR.getInstance4Edit(InstanceID);
			if(MIP != null)
			{
				MIPObject = MIP;
				Success=true;
			}
		}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			context.getLog().debug(sw.toString()); //
		}
		
	
		
	}//END OF EXECUTION

	
}
