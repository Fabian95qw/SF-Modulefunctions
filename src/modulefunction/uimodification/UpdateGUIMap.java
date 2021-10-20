package si.module.modulefunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.Variable;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.model.resource.MapResource;
import de.vertico.starface.module.core.model.resource.Resource;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class UpdateGUIMap implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="GUI_NAME", description="Name of the GUI_Element example: GUI_MAP" ,type=VariableType.STRING)
	public String GUI_NAME = "";
	
	@InputVar(label="GuiMap", description="The Map, which should be set in the GUI Element" ,type=VariableType.MAP)
	public Map<String, String> GuiMap = null;
	
	@InputVar(label="MERGE", description="Merge with current Map in this Element " ,type=VariableType.BOOLEAN)
	public Boolean MERGE = false;
	
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################

	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
				
		log.debug("Updating: " + GUI_NAME+ " with: " + GuiMap.toString());
		
		List<Variable> VARList = context.getInvocationInfo().getModuleInstance().getInputVars();
		
		Variable Target = null;
		
		for (Variable Var : VARList)
		{
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
		MapResource ResourceMap = context.getInvocationInfo().getModuleInstance().getMapResource(Target.getValue());
		
		if(ResourceMap == null)
		{
			log.debug("Resource Map is null?" );
			ResourceMap = new MapResource();
			ResourceMap.setId(Target.getValue());
		}
		
		Map<String, String> MergedMap = null;
		if(MERGE)
		{
			MergedMap = ResourceMap.getMap();
		}
		else
		{
			MergedMap = new HashMap<String,String>();
		}
		
		for(Entry<String, String> Entry: GuiMap.entrySet())
		{
			if(!MergedMap.containsKey(Entry.getKey()))
			{
				MergedMap.put(Entry.getKey(), Entry.getValue());
			}
		}
		
		ResourceMap.setMap(MergedMap);
		
		Set<Resource> Resources = context.getInvocationInfo().getModuleInstance().getResources();
		Resource ROld = null;
			
		for(Resource R : Resources)
		{
			//log.debug(R.getId() + " <==> " + Target.getValue());
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
			//log.debug(ResourceMap.getMap().toString());
			log.debug("New Resource Size: " + ResourceMap.getMap().size());
			Resources.remove(ROld);
			ResourceMap.setId(ROld.getId());
			Resources.add(ResourceMap);
		}
		else
		{
			log.debug("Adding New Map");
			Resources.add(ResourceMap);
		}
		
		
	}//END OF EXECUTION

	
	
}
