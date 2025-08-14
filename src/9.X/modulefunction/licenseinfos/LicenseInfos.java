package si.module.modulefunction;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.starface.license.manager.LicenseComponent;
import de.starface.license.manager.ws.beans.license.License;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="Returns License Infos")
public class LicenseInfos implements IBaseExecutable 
{
	//##########################################################################################
	
	@OutputVar(label="ServerLicense", description="Server License Key",type=VariableType.STRING)
	public String ServerLicense="";
		
	@OutputVar(label="OtherLicenses", description="Map<Licensekey, Licensetype>",type=VariableType.STRING)
	public Map<String,String> OtherLicenses=new HashMap<String, String>();
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		log.debug("Getting Server license key");
		
		LicenseComponent LC = (LicenseComponent)StarfaceComponentProvider.getInstance().fetch(LicenseComponent.class);		
		License L = LC.getServerLicense();

		
		if(L == null)
		{
			log.debug("Fetching License failed!");
			ServerLicense = "FREEPBX";
			return;
		}
		ServerLicense = L.getLicenseKey();
		log.debug("License Key: " + ServerLicense);
		
		for(License Entry : L.getChildren())
		{
			log.debug(Entry.getLicenseKey() +" " + Entry.getProductName());
			OtherLicenses.put(Entry.getLicenseKey(), Entry.getProductName());
		}
		
		
	}//END OF EXECUTION

	
}
