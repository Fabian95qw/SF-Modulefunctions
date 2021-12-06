package si.module.guichanges;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.ModuleException;
import de.vertico.starface.module.core.model.ModuleInstanceProject;
import de.vertico.starface.module.core.model.Variable;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.model.resource.ListResource;
import de.vertico.starface.module.core.model.resource.MapResource;
import de.vertico.starface.module.core.model.resource.Resource;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, name="SetFieldbyGUIName4Instance" , description="Set Value of GUI_ELEMENT")
public class SetFieldbyGUIName4Instance implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="ModuleInstanceProject", description="The Object returned by GetModuleInstance4Edit",type=VariableType.OBJECT)
	public Object MIPObject=null;
	
	@InputVar(label="GUI_NAME", description="Name of the GUI_Element",type=VariableType.STRING,  valueByReferenceAllowed = false)
	public String GUI_NAME="";
	
	@InputVar(label="Value", description="The new Value to set. Supported Types: BOOLEAN,STRING,NUMBER,STARFACE_ACCOUNT,STARFACE_GROUP,STARFACE_USER,MAP,LIST",type=VariableType.OBJECT)
	public Object Value=null;
		
	@OutputVar(label="VariableChanged", description="true ==> New and old value were different, false ==> New and old value were the same",type=VariableType.BOOLEAN)
	public boolean VariableChanged=false;
	
	@OutputVar(label="Success", description="true if variable was found, false if no variable with GUI_NAME was found",type=VariableType.BOOLEAN)
	public boolean Success=false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception {

		Log log = context.getLog();
		if(MIPObject == null)
		{
			log.debug("ModuleInstanceProject is null!");
			return;
		}

		ModuleInstanceProject MIP = (ModuleInstanceProject) MIPObject;
		List<Variable> VARList = MIP.getObject().getInputVars();
				
		for (Variable Var : VARList)
		{
			if (Var.getName().contains(GUI_NAME))
			{
				VariableChanged = SetValue(Value, Var,MIP, log);
				Success=true;
				break;
			}
		}	
		
	}//END OF EXECUTION
		
	public boolean SetValue(Object O, Variable Var, ModuleInstanceProject MIP, Log log) throws IOException, ModuleException
	{
		String Value=null;
		switch(Var.getType())
		{
		case BOOLEAN:
			Value = CAST_BOOLEAN(O, log);
			log.debug(Value +" <==> " + Var.getValue());
			if(!Var.getValue().equals(Value))
			{
				Var.setValue(Value);
				return true;
			}
			return false;
		case STRING:
			Value= CAST_STRING(O, log);
			log.debug(Value +" <==> " + Var.getValue());
			if(!Var.getValue().equals(Value))
			{
				Var.setValue(Value);
				return true;
			}
			return false;
		case NUMBER:
			Value= CAST_NUMBER(O, log);
			log.debug(Value +" <==> " + Var.getValue());
			if(!Var.getValue().equals(Value))
			{
				Var.setValue(Value);
				return true;
			}
			return false;
		case STARFACE_ACCOUNT:
		case STARFACE_GROUP:
		case STARFACE_USER:
			Value= CAST_STARFACE_ENTITY(O, log);
			log.debug(Value +" <==> " + Var.getValue());
			if(!Var.getValue().equals(Value))
			{
				Var.setValue(Value);
				return true;
			}
			return false;
		case MAP:
			{
				@SuppressWarnings("unchecked")
				Map<String,String> Values = (Map<String, String>)O;
				MapResource MR = new MapResource();
				if(Var.getValue() != null)
				{
					//log.debug(Var.getValue());
					MR = MIP.getObject().getMapResource(Var.getValue()); 
					if(MR == null)	
					{
						//log.debug("MR is null!");
						MR = new MapResource();
					}
				}

				log.debug(Values.toString() +" <==> " + MR.getMap().toString());
				if(!MR.getMap().equals(Values))
				{
					MR.setMap(Values);
					Set<Resource> Resources = MIP.getObject().getResources();
					Resource ROld = null;
						
					for(Resource R : Resources)
					{
						if(R.getId().equals(Var.getValue()))
						{
							ROld = R;
							break;
						}
					}
					if(ROld != null)
					{
						Resources.remove(ROld);
						MR.setId(ROld.getId());
						Resources.add(MR);
					}
					else
					{
						MR.setId(Var.getValue());
						Resources.add(MR);
					}
					MIP.getObject().setResources(Resources);
					return true;
				}				
			}
			return false;
		case LIST:
		{
			@SuppressWarnings("unchecked")
			List<String> Values = (List<String>)O;
			
			ListResource LR = new ListResource();
			if(Var.getValue() != null)
			{
				log.debug(Var.getValue());
				LR = MIP.getObject().getListResource(Var.getValue()); 
				if(LR == null)
				{
					//log.debug("LR is null!");
					LR = new ListResource(); 
				}
			}

			if(!LR.getValues().equals(Values))
			{
				log.debug(Values.toString() +"<==>"+LR.getValues().toString());
				LR.setValues(Values);
				Set<Resource> Resources = MIP.getObject().getResources();
				Resource ROld = null;
	
				for(Resource R : Resources)
				{
					if(R.getId().equals(Var.getValue()))
					{
						ROld = R;
						break;
					}
				}
				
				if(ROld != null)
				{
					//log.debug("Removing: "+ ROld.toString());
					Resources.remove(ROld);
					LR.setId(ROld.getId());
					//log.debug("Adding:" + LR.toString());
					Resources.add(LR);
				}
				else
				{
					//log.debug("Adding:" + LR.toString());
					LR.setId(Var.getValue());
					Resources.add(LR);
				}
				MIP.getObject().setResources(Resources);
			return true;
			}
		}
		return false;
		default:
			break;
		}
		return false;
	}
		
	private String CAST_STARFACE_ENTITY(Object O, Log log)
	{
		if (O instanceof String)
		{
			String S = (String)O;
			return S.replaceAll("[^0-9]", "");
		}
		else if (O instanceof Integer)
		{
			Integer I = (Integer)O;
			return String.valueOf(I);
		}
		else if (O instanceof Double)
		{
			Double D = (Double)O;
			return String.valueOf(D.intValue());
		}
		else if (O instanceof Long)
		{
			Long L = (Long)O;
			return String.valueOf(L.intValue());
		}
		log.debug("Unable to Cast: "+ O.getClass().toString());
		return "-1";
	}
	
	private String CAST_NUMBER(Object O, Log log)
	{
		if (O instanceof String)
		{
			String S = (String)O;
			return S.replaceAll("[^0-9]", "");
		}
		else if(O instanceof Boolean)
		{
			Boolean B = (Boolean) O;
			if(B)
			{
				return "1";	
			}
			else
			{
				return "0";
			}
		}
		else if (O instanceof Integer)
		{
			Integer I = (Integer)O;
			return String.valueOf(I);
		}
		else if (O instanceof Double)
		{
			Double D = (Double)O;
			return String.valueOf(D);
		}
		else if (O instanceof Long)
		{
			Long L = (Long)O;
			return String.valueOf(L);
		}
		log.debug("Unable to Cast: "+ O.getClass().toString());
		return "-999999999";
	}
	
	private String CAST_STRING(Object O, Log log)
	{
		if (O instanceof String)
		{
			String S = (String)O;
			return S;
		}
		else if(O instanceof Boolean)
		{
			Boolean B = (Boolean) O;
			return B.toString();
		}
		else if (O instanceof Integer)
		{
			Integer I = (Integer)O;
			return String.valueOf(I);
		}
		else if (O instanceof Double)
		{
			Double D = (Double)O;
			return String.valueOf(D);
		}
		else if (O instanceof Long)
		{
			Long L = (Long)O;
			return String.valueOf(L);
		}
		else if (O instanceof List)
		{
			List<?> L = (List<?>)O;
			return L.toString();
		}
		else if (O instanceof Map)
		{
			Map<?, ?> M = (Map<?, ?>)O;
			return M.toString();
		}
		log.debug("Unable to Cast: "+ O.getClass().toString());
		return "CAST_ERROR";
	}
			
	private String CAST_BOOLEAN(Object O, Log log)
	{
		if(O instanceof Boolean)
		{
			Boolean B = (Boolean) O;
			return B.toString();
		}
		else if (O instanceof Integer)
		{
			Integer I = (Integer)O;
			if(I == 1)
			{
				return "true";
			}
			else
			{
				return "false";
			}
		}
		else if (O instanceof Double)
		{
			Double D = (Double)O;
			if(D == 1.0)
			{
				return "true";
			}
			else
			{
				return "false";
			}
		}
		else if (O instanceof Long)
		{
			Long L = (Long)O;
			if(L == 1L)
			{
				return "true";
			}
			else
			{
				return "false";
			}
		}
		else if (O instanceof String)
		{
			String S = (String)O;
			return S;
		}
		log.debug("Unable to Cast: "+ O.getClass().toString());
		return "CAST_ERROR";
	}
	
	
}
