package si.module.examples.documentation;

import java.text.SimpleDateFormat;

import de.starface.bo.license.UserLicenseType;
import de.starface.license.manager.LicenseComponent;
import de.starface.license.manager.LicenseComponent.Features;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.PersonAndAccountHandler;
import de.vertico.starface.persistence.databean.core.Permission;
import java.util.Date;

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
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		
		LicenseComponent LC = (LicenseComponent)context.springApplicationContext().getBean(LicenseComponent.class);
		
		
		PersonAndAccountHandler PAH = (PersonAndAccountHandler)context.springApplicationContext().getBean(PersonAndAccountHandler.class);
		
		Integer AvailableUsers = PAH.getAvailableLicensesCount(UserLicenseType.USER);
		Integer AvailableUsersLight = PAH.getAvailableLicensesCount(UserLicenseType.USERLIGHT);
		Integer AvailableUCC = PAH.countSettedPermission(Permission.UCI_AUTOPROVISIONING);
		
		LicensedUsers = LC.getMaxUsers()-AvailableUsers+"/"+LC.getMaxUsers();
		LicensedUsersLight = LC.getMaxLightUsers()-AvailableUsersLight+"/"+LC.getMaxLightUsers();
		LicensedUsersPremium = LC.getLicensedUsersOfFeature(LicenseComponent.Features.UCI_AUTOPROVISIONING)-AvailableUCC+"/"+LC.getLicensedUsersOfFeature(LicenseComponent.Features.UCI_AUTOPROVISIONING);
					
		Integer TotalTerminalserverLicenses = LC.getLicensedUsersOfFeature(Features.WINCLIENT_TERMINAL_SERVER);//Bei der Lizenzkomponente Anfragen, wie viele Lizenzen vom Typ "WINCLIENT_TERMINAL_SERVER", also TS Lizenzen dass es gibt.
		Integer UsedTerminalserverLicenses= PAH.countSettedPermission(Permission.WINCLIENT_TERMINAL_SERVER);

		LicensedUsersTS = UsedTerminalserverLicenses +"/"+TotalTerminalserverLicenses; //Ergebnis zusammenbauen
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");
		if(LC.getServerLicense() != null && LC.getServerLicense().getUpdateOption() != null && LC.getServerLicense().getUpdateOption().getExpireDate() != 0L)
		//if(LC.getServerLicense().getUpdateOption().getExpireDate() != null && LIF.getUpdateOptionExpireDate().getTime() != 0L)
		{
			Date D = new Date(LC.getServerLicense().getUpdateOption().getExpireDate());
			Updateexpiredate = SDF.format(D);
		}
		else
		{
			Updateexpiredate = "-";
		}
		
		if(LC.getServerLicense() != null && LC.getServerLicense().getUpdateOption() != null)
		{
				UserUpdateOption = LC.getServerLicense().getUpdateOption().getUsers();
		}
				
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
