package si.module.documentation.collection;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.bo.UserBusinessObject;
import de.starface.bo.VoicemailListBusinessObject;
import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.VoiceboxHandler;

@Function(visibility=Visibility.Private, description="")
public class GetAllVoicemails implements IBaseExecutable 
{
	//##########################################################################################
			    	
	@OutputVar(label="Voicemails", description="",type=VariableType.LIST)
	public List<Map<String, String>> Voicemails = new ArrayList<Map<String, String>>();
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@SuppressWarnings("unused")
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		log.debug("Extracting Voicemaildata");
		VoiceboxHandler VBH = (VoiceboxHandler)context.provider().fetch(VoiceboxHandler.class);
		VoicemailListBusinessObject VBO = (VoicemailListBusinessObject)context.provider().fetch(VoicemailListBusinessObject.class);
		UserBusinessObject UBO = (UserBusinessObject)context.provider().fetch(UserBusinessObject.class);

		
			
		
		
	}//END OF EXECUTION

	

	

	
}
