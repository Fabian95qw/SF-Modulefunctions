package nucom.module.tts.utility;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.model.gui.InputGUIObject;
import de.vertico.starface.module.core.model.gui.InputGUITab;
import de.vertico.starface.module.core.model.gui.text.TextChoice;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import nucom.module.tts.utility.EnumHelper.AudioFormat;
import nucom.module.tts.utility.EnumHelper.Language;
import nucom.module.tts.utility.EnumHelper.Voice;

@Function(visibility=Visibility.Public, rookieFunction=false, description="Fills the TextChoices with all the Language Options for the Module")
public class TTS_FILL_TEXTCHOICES implements IBaseExecutable 
{
	//##########################################################################################
	
	  @InputVar(label = "GUI_NAME_TEXTCHOICE_AUDIOFORMAT", description = "The Textchoice which should be filled with audio choices", type = VariableType.STRING)
	  public String GUI_NAME_TEXTCHOICE_AUDIOFORMAT = "";
		    
	  @InputVar(label = "GUI_NAME_TEXTCHOICE_LANGUAGE", description = "The Textchoice which should be filled with language choices", type = VariableType.STRING)
	  public String GUI_NAME_TEXTCHOICE_LANGUAGE = "";
	  
	  @InputVar(label = "GUI_NAME_TEXTCHOICE_VOICE", description = "The Textchoice which should be filled with voice choices", type = VariableType.STRING)
	  public String GUI_NAME_TEXTCHOICE_VOICE = "";
	  	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{		
	    Log log = context.getLog();
		
		List<InputGUITab> GUITabs = context.getInvocationInfo().getModule().getGUITabs();
	    List<InputGUIObject> Children = null;
	    TextChoice TC = null;
	    
	    
	    
	    for (InputGUITab Tab : GUITabs) 
	    {
	        Children = Tab.getChildren();
	        for (InputGUIObject Child : Children) 
	        {
	        log.debug(Child.getName());
	          if (Child.getName().equals(GUI_NAME_TEXTCHOICE_AUDIOFORMAT)) 
	          {	            
	            try 
	            {
	            List<String> Choices = new ArrayList<String>();
	            	
	            for(AudioFormat AF : AudioFormat.values())
	            {
	            	Choices.add(AF.toString());
	            }
	            
	              TC = (TextChoice)Child;
	              TC.setChoices(Choices);
	            }
	            catch (ClassCastException classCastException) {}
	          }
	          else if (Child.getName().equals(GUI_NAME_TEXTCHOICE_LANGUAGE)) 
	          {
		            try 
		            {
		            List<String> Choices = new ArrayList<String>();
		            	
		            for(Language L : Language.values())
		            {
		            	Choices.add(L.toString());
		            }
		            
		              TC = (TextChoice)Child;
		              TC.setChoices(Choices);
		            }
		            catch (ClassCastException classCastException) {}  
	          }
	          else if (Child.getName().equals(GUI_NAME_TEXTCHOICE_VOICE)) 
	          {
		            try 
		            {
		            List<String> Choices = new ArrayList<String>();
		            	
		            for(Voice V : Voice.values())
		            {
		            	Choices.add(V.toString());
		            }
		            
		              TC = (TextChoice)Child;
		              TC.setChoices(Choices);
		            }
		            catch (ClassCastException classCastException) {}
	          }
	        } 
	      } 
	    
		
	}//END OF EXECUTION

	
}
