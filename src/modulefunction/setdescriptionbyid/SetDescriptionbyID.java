package si.module.modulefunction;

import java.util.List;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.model.gui.InputGUIObject;
import de.vertico.starface.module.core.model.gui.InputGUITab;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class SetDescriptionbyID implements IBaseExecutable 
{
	//##########################################################################################
		
	@InputVar(label="GUI_ID", description="ID des GUI Elements",type=VariableType.STRING)
	public String GUI_ID="";
	
	@InputVar(label="Wert", description="Der Wert, welcher in das Feld geschrieben werden soll",type=VariableType.STRING)
	public String Wert;

    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception {

		//Logger log = context.getLog();
		List<InputGUITab> VARList = context.getInvocationInfo().getModule().getGUITabs();
				
		//log.trace("Looking for GUI Object with ID: " + GUI_ID);
		
		for (InputGUITab Var : VARList)
		{
			for(InputGUIObject IGO : Var.getChildren())
			{
				//log.trace(IGO.getGUIString()+" "+ IGO.getId());
				if(IGO.getId().equals(GUI_ID))
				{
					//log.debug("Object found. setting value...");
					IGO.setDescription(Wert);
					//log.debug("Done");
					return;
				}
			}
		}	
		
	}//END OF EXECUTION

	
}
