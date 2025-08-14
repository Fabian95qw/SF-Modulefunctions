package si.module.examples.redirection;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.logging.log4j.Logger;

import de.starface.bo.RedirectBusinessObject;

import de.starface.integration.uci.java.v30.types.RedirectSetting;
import de.starface.integration.uci.java.v30.values.RedirectSettingType;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;

@Function(name="SetRedirections", visibility=Visibility.Private, description="")
public class SetRedirections implements IBaseExecutable 
{
	//##########################################################################################		
	
	@InputVar(label="TargetUser/TargetGroup", description="Zielbenutzer/Gruppe. dessen Umleitung gesetzt werden soll.", type=VariableType.STARFACE_ACCOUNT)
	public Integer STARFACE_ACCOUNT = -1;
		
	@InputVar(label="Impersonate_User", description="Der User, in dessem Name der Vorgang ausgeführt wird", type=VariableType.STARFACE_ACCOUNT)
	public Integer IMPERSONATE_ACCOUNT=-1;
	
	@InputVar(label="RedirectSettingType", description="Welcher Umleitungstyp (ALWAYS == IMMER || BUSY == BESETZT || TIMEOUT == Zeitüberschr.", valueByReferenceAllowed=true)
	public RedirectSettingType RST  = RedirectSettingType.ALWAYS;
	
	@InputVar(label="RedirectSettings", description="Die Liste der Umleitungseinstellungen.", type=VariableType.OBJECT)
	public Object RedirectSettings = null;
	
	 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{	
		RedirectBusinessObject RBO = (RedirectBusinessObject)context.springApplicationContext().getBean(RedirectBusinessObject.class);

		try
		{
			List<RedirectSetting> Settings = (List<RedirectSetting>) RedirectSettings;
			RBO.setRedirectSettings(IMPERSONATE_ACCOUNT, STARFACE_ACCOUNT, Settings);
			
		}
		catch(Exception e)
		{
			EtoStringLog(context.getLog(), e);
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
