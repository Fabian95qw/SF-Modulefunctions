package si.module.examples.redirection;

import java.util.List;

import de.starface.bo.RedirectBusinessObject;

import de.starface.integration.uci.java.v30.types.RedirectSetting;
import de.starface.integration.uci.java.v30.values.OrderDirection;
import de.starface.integration.uci.java.v30.values.RedirectSettingProperties;
import de.starface.integration.uci.java.v30.values.RedirectSettingType;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(name="GetRedirections", visibility=Visibility.Private, description="")
public class GetRedirections implements IBaseExecutable 
{
	//##########################################################################################		
	
	@InputVar(label="TargetUser/TargetGroup", description="Zielbenutzer/Gruppe. dessen Umleitung ausgelesen werden soll.", type=VariableType.STARFACE_ACCOUNT)
	public Integer STARFACE_ACCOUNT = -1;
	
	@InputVar(label="RedirectSettingType", description="Welcher Umleitungstyp (ALWAYS == IMMER || BUSY == BESETZT || TIMEOUT == Zeitüberschr.", valueByReferenceAllowed=true)
	public RedirectSettingType RST  = RedirectSettingType.ALWAYS;
	
	@OutputVar(label="RedirectSettings", description="Die Liste der Umleitungseinstellungen.", type=VariableType.OBJECT)
	public Object RedirectSettings = null;
	
	 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{	
		RedirectBusinessObject RBO = (RedirectBusinessObject)context.springApplicationContext().getBean(RedirectBusinessObject.class);

		List<RedirectSetting> Settings = RBO.getRedirectSettings(STARFACE_ACCOUNT, RST, null, RedirectSettingProperties.calledNumber, OrderDirection.ASCENDING);
		RedirectSettings = Settings;
		
	}//END OF EXECUTION
		
}
