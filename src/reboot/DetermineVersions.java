package si.module.reboot;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Modifiziere GUI Element.")
public class DetermineVersions implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="VersionString", description="VersionString",type=VariableType.STRING)
	public String VersionString="";
	    
	@OutputVar(label="", description="",type=VariableType.NUMBER)
	public Integer SubVersionNr = 0;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception
	{
		Log log = context.getLog();
		log.debug("VersionString:" + VersionString);
		String[] Split = null;
		Split = VersionString.split("\\.");
		SubVersionNr = Integer.valueOf(Split[1]);		
	}//END OF EXECUTION

	
}
