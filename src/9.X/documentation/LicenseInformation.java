package si.module.documentation.collection;

import java.text.SimpleDateFormat;

import de.starface.core.component.StarfaceComponentProvider;
import de.starface.license.manager.LicenseComponent;
import de.starface.license.manager.LicenseComponent.Features;
import de.vertico.starface.config.server.forms.LicenseInfoForm;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.PersonAndAccountHandler;
import de.vertico.starface.persistence.databean.core.Permission;

@Function(visibility=Visibility.Private, description="")
public class LicenseInformation implements IBaseExecutable 
{
	//##########################################################################################
		 
	@OutputVar(label="LicensedUsers", description="",type=VariableType.STRING)
	public String LicensedUsers="";
	
	@OutputVar(label="LicensedUsersLight", description="",type=VariableType.STRING)
	public String LicensedUsersLight="";
	
	@OutputVar(label="LicensedUsersPremium", description="",type=VariableType.STRING)
	public String LicensedUsersPremium="";
	
	@OutputVar(label="LicensedUserTS", description="",type=VariableType.STRING)
	public String LicensedUsersTS="";
	
	@OutputVar(label="UserUpdateOption", description="",type=VariableType.NUMBER)
	public Number UserUpdateOption=-1;
	
	@OutputVar(label="Updateexpiredate", description="",type=VariableType.STRING)
	public String Updateexpiredate="";
	
	@OutputVar(label="Serverlicensekey", description="",type=VariableType.STRING)
	public String Serverlicensekey="";
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		
		LicenseComponent LC = (LicenseComponent)StarfaceComponentProvider.getInstance().fetch(LicenseComponent.class);
		LicenseInfoForm LIF = new LicenseInfoForm(StarfaceComponentProvider.getInstance());
		
		PersonAndAccountHandler PAH = (PersonAndAccountHandler)StarfaceComponentProvider.getInstance().fetch(PersonAndAccountHandler.class);
		
		
		
		LicensedUsers = LIF.getLicensedUsers()-LIF.getAvailableUsers()+"/"+LIF.getLicensedUsers();
		LicensedUsersLight = LIF.getLicensedLightUsers()-LIF.getAvailableLightUsers()+"/"+LIF.getLicensedLightUsers();
		LicensedUsersPremium = LIF.getLicensedUcc()-LIF.getAvailableUcc()+"/"+LIF.getLicensedUcc();
					
		Integer TotalTerminalserverLicenses = LC.getLicensedUsersOfFeature(Features.WINCLIENT_TERMINAL_SERVER);//Bei der Lizenzkomponente Anfragen, wie viele Lizenzen vom Typ "WINCLIENT_TERMINAL_SERVER", also TS Lizenzen dass es gibt.
		Integer UsedTerminalserverLicenses= PAH.getAccountsWithPermission(Permission.WINCLIENT_TERMINAL_SERVER.getPermissionid()).size();

		LicensedUsersTS = UsedTerminalserverLicenses +"/"+TotalTerminalserverLicenses; //Ergebnis zusammenbauen
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");
		if(LIF.getUpdateOptionExpireDate() != null && LIF.getUpdateOptionExpireDate().getTime() != 0L)
		{
			Updateexpiredate = SDF.format(LIF.getUpdateOptionExpireDate());
		}
		else
		{
			Updateexpiredate = "-";
		}
		
		UserUpdateOption = LIF.getUpdateOptionUsers();
		
		if(LC.getServerLicense() != null)
		{
			Serverlicensekey = LC.getServerLicense().getLicenseKey();
			//https://support.starface.de/forum/thread/10649-gratismodul-starface-dokumentation-erzeugen/
			Serverlicensekey = Serverlicensekey.replaceAll("^(.{5})(.{5})(.{5})(.{5})$", "$1-$2-$3-$4").toUpperCase();
		}	
		else
		{
			Serverlicensekey = "FREEPBX";
		}
		
	}//END OF EXECUTION


	
}
