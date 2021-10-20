package si.module.modulefunction;

import java.util.List;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.Variable;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;


@Function(visibility=Visibility.Private, rookieFunction=false, description="Modifiziere GUI Element.")
public class SetBoolbyGUIName implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="GUI_NAME", description="Name des GUI Elements",type=VariableType.STRING)
	public String GUI_NAME="";
	
	@InputVar(label="Wert", description="Der Wert, welcher in das Feld geschrieben werden soll",type=VariableType.BOOLEAN)
	public String Wert;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception {

		List<Variable> VARList = context.getInvocationInfo().getModuleInstance().getInputVars();
		
		for (Variable Var : VARList)
		{
			if (Var.getName().contains(GUI_NAME))
					{
						Var.setValue(Wert);
						return;
					}
		}	
		
	}//END OF EXECUTION

	
}
