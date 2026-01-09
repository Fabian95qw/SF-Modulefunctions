package si.module.examples.ai;


import org.apache.logging.log4j.Logger;

import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import io.github.sashirestela.openai.SimpleOpenAI;
import de.starface.ai.openai.chat.OpenAiChatComponent;

@Function(visibility = Visibility.Private, description = "")
public class OpenAIChatCustomBaseURL implements IBaseExecutable
{
	@InputVar(label = "Text", description = "", type = VariableType.STRING)
	public String Text;
	
	@InputVar(label = "BaseURL", description = "", type = VariableType.STRING)
	public String BaseURL = "https://custom.openai.endpoint";
	
	@InputVar(label = "APIKey", description = "", type = VariableType.STRING)
	public String Key = "";
	
	@InputVar(label = "AssistantID", description = "", type = VariableType.STRING)
	public String AID = "";
	
	@OutputVar(label = "Response", description = "", type = VariableType.STRING)
	public String Response;
	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception
	{
		Logger log = context.getLog();
		
		OpenAiChatComponent OAIC = (OpenAiChatComponent) context.springApplicationContext()
				.getBean(OpenAiChatComponent.class);
		log.debug("Building SimpleOpenAI Client");
		SimpleOpenAI AI = SimpleOpenAI.builder().apiKey(Key).baseUrl(BaseURL).build();
		log.debug("Sending Request...");
		Response = OAIC.chat(AI, Text, AID);
		log.debug("Response: " + Response);
	}
}
