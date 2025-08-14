package si.module.modulefunction;

import java.util.List;
import org.apache.logging.log4j.Logger;

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

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class UpdateGUIDropdown implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="Displayname", description="Requires the DISPLAYNAME not the GUI_NAME of an Element!" ,type=VariableType.STRING)
	public String Name = "TextChoice";
	
	@InputVar(label="GuiList", description="The List, which should be set in the GUI Element" ,type=VariableType.LIST)
	public Object GuiListRAW = null;

    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################

	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
				
		@SuppressWarnings("unchecked")
		List<String> GuiList = (List<String>) GuiListRAW;
		
		log.debug("Updating: " + Name+ " with: " + GuiList.toString());
		
		List<InputGUITab> GUITabs = context.getInvocationInfo().getModule().getGUITabs();
		List<InputGUIObject> Childs= null;
		TextChoice TC = null;
				
		for(InputGUITab Tab : GUITabs)
		{
			Childs = Tab.getChildren();
			for(InputGUIObject Child : Childs)
			{
				log.debug(Child.getName() +" <==> " + Name);
				if(Child.getName().equals(Name))
				{
					try
					{
						TC = (TextChoice)Child;
						TC.setChoices(GuiList);
						log.debug("Update Successful!");
					}
					catch(ClassCastException e)
					{
						
					}
					continue;
				}
			}
		}
		
		
	}//END OF EXECUTION

	
	
}
