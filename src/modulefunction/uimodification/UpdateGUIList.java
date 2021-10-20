package si.module.modulefunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.Variable;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.model.resource.ListResource;
import de.vertico.starface.module.core.model.resource.Resource;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class UpdateGUIList implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="GUI_NAME", description="Name of the GUI_Element example: GUI_LIST" ,type=VariableType.STRING)
	public String GUI_NAME = "GUI_LIST";
	
	@InputVar(label="GuiList", description="The List, which should be set in the GUI Element" ,type=VariableType.LIST)
	public Object GuiListRAW = null;
	
	@InputVar(label="MERGE", description="Merge with current List in this Element " ,type=VariableType.BOOLEAN)
	public Boolean MERGE = false;
	
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################

	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
				
		@SuppressWarnings("unchecked")
		List<String> GuiList = (List<String>) GuiListRAW;
		
		log.debug("Updating: " + GUI_NAME+ " with: " + GuiList.toString());
		
		List<Variable> VARList = context.getInvocationInfo().getModuleInstance().getInputVars();
		
		Variable Target = null;
		
		for (Variable Var : VARList)
		{
			log.debug(Var.getName()+ " <==> " + GUI_NAME);
			if (Var.getName().equals(GUI_NAME))
			{
				log.debug(GUI_NAME+ " found!");
				Target = Var;
				break;
			}
		}	
		
		if(Target == null)
		{
			log.debug("GUI Element not Found.");
			return;
		}
		ListResource ResourceList = context.getInvocationInfo().getModuleInstance().getListResource(Target.getValue());
		
		if(ResourceList == null)
		{
			log.debug("Resource List is null?" );
			ResourceList = new ListResource();
		}
		
		List<String> MergedList = null;
		if(MERGE)
		{
			MergedList = ResourceList.getValues();
		}
		else
		{
			MergedList = new ArrayList<String>();
		}
		
		
		for(String Entry: GuiList)
		{
			if(!MergedList.contains(Entry))
			{
				MergedList.add(Entry);
			}
		}
				
		ResourceList.setValues(MergedList);
		
		Set<Resource> Resources = context.getInvocationInfo().getModuleInstance().getResources();
		Resource ROld = null;
			
		for(Resource R : Resources)
		{
			log.debug(R.getId() + " <==> " + Target.getValue());
			if(R.getId().equals(Target.getValue()))
			{
				log.debug("Found Resource.");
				ROld = R;
				break;
			}
		}
		
		if(ROld != null)
		{
			log.debug("Replacing Resource");
			log.debug("New Resource Size: " + ResourceList.getValues().size());
			Resources.remove(ROld);
			ResourceList.setId(ROld.getId());
			Resources.add(ResourceList);
		}
				
	}//END OF EXECUTION

	
	
}
