package si.module.reboot;

import java.io.File;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class isAfterReboot implements IBaseExecutable 
{
	//##########################################################################################	    
	@OutputVar(label="isAfterReboot", description="",type=VariableType.BOOLEAN)
	public boolean isAfterReboot = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception
	{
		Log log = context.getLog();

		File F = new File("/dev/reboot");
		File FBlock = new File("/home/starface/reboot");
		
		if(F.exists() && FBlock.exists())
		{
			log.debug("Reboot is in Progres...");
			return;
		}
		
		if(!F.exists() && FBlock.exists())
		{
			log.debug("It's after Reboot");
			FBlock.delete();
			isAfterReboot=true;
			return;
		}
		else
		{
			
		}
				
	}//END OF EXECUTION

	
}
