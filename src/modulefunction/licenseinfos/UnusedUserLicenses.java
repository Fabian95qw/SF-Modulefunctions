package nucom.module.prtg.sensors.licenses;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.config.server.forms.LicenseInfoForm;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Returns the amount of currently unused licenses")
public class UnusedUserLicenses implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Unused Licenses", description="",type=VariableType.NUMBER)
	public  Integer UnusedLicenses=0;
	
	@OutputVar(label="Unused UCC Licenses", description="",type=VariableType.NUMBER)
	public  Integer UnusedUCCLicenses=0;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		
		LicenseInfoForm LIF = new LicenseInfoForm(StarfaceComponentProvider.getInstance());
				
		UnusedLicenses = LIF.getAvailableUsers();
		UnusedUCCLicenses  = LIF.getAvailableUcc();
		
	
	}//END OF EXECUTION

	
}
