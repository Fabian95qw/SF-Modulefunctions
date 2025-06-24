package si.module.moduleconfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.ModuleException;
import de.vertico.starface.module.core.model.ModuleInstanceProject;
import de.vertico.starface.module.core.model.TimerInterval;
import de.vertico.starface.module.core.model.Variable;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.model.resource.FileResource;
import de.vertico.starface.module.core.model.resource.ListResource;
import de.vertico.starface.module.core.model.resource.MapResource;
import de.vertico.starface.module.core.model.resource.Resource;
import de.vertico.starface.module.core.model.resource.TimerSettingsResource;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.model.resource.FileType;
import si.module.guichanges.SaveChangesHelper;
import si.module.moduleconfig.serializers.FileSerializable;
import si.module.moduleconfig.serializers.TimerSettingsSerializable;
import si.module.moduleconfig.serializers.VariableSerializable;

@Function(visibility=Visibility.Private, description="")
public class ReadConfig implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="SourceFile", description="SourceFile",type=VariableType.STRING)
	public String SourceFile="";
	
	@InputVar(label="IgnoreVariables", description="Ignore Variables",type=VariableType.LIST)
	public List<String> IgnoreVariables = new ArrayList<String>();
		    	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();		
		
		List<VariableSerializable> Variables = new ArrayList<VariableSerializable>();
		log.debug("Trying to Read File:"+ SourceFile);
		
		try
		{
			File F = new File(SourceFile);
			FileInputStream FIS = new FileInputStream(F);
			ObjectInputStream IN = new ObjectInputStream(FIS);
			Object O =IN.readObject();
			IN.close();
			Variables = (List<VariableSerializable>) O;
		}
		catch(Exception e)
		{
			EtoStringLog(log, e);
		}
		
		log.debug("File Loaded!");
		
		log.debug("Processing: "+ Variables.size() +" Variables...");
		

		ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);
		ModuleInstanceProject MIP = MR.getInstance4Edit(context.getInvocationInfo().getModuleInstance().getId());
		
		for(VariableSerializable VS : Variables)
		{
			log.debug(VS.toString());
			if(IgnoreVariables.contains(VS.getName()))
			{
				log.debug("Skipped: " + VS.getName());
				continue;
			}
			for(Variable Var : MIP.getObject().getInputVars())
			{
				if(VS.getName().equals(Var.getName()))
				{
					try
					{
						log.debug("Mapping: " + VS.toString());
						if(VS.hasContent())
						{
							SetValue(VS, Var,MIP, log, context);
						}
					}
					catch(Exception e)
					{
						EtoStringLog(log, e);
					}
				}
					
			}
		}
		
		log.debug("Updating Instance " + MIP.getObject().getModuleName() +" " + MIP.getObject().getId());
		
		try
		{
			SaveChangesHelper.AddChange(MIP.getObject().getId(), context);
		}
		catch(NoSuchMethodError e)
		{
			
		}
		MR.updateModuleInstance(MIP);
		
	}//END OF EXECUTION

	public static void EtoStringLog(Logger log, Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}
	
	public void SetValue(VariableSerializable VS, Variable Var, ModuleInstanceProject MIP, Logger log, IRuntimeEnvironment context) throws IOException, ModuleException, ClassNotFoundException
	{
		String Value=null;
		switch(Var.getType())
		{
		case BOOLEAN:
			Value = CAST_BOOLEAN(VS.getContent(), log);
			//log.debug(Value +" <==> " + Var.getValue());
			if(!Var.getValue().equals(Value))
			{
				Var.setValue(Value);
			}
			break;
		case STRING:
			Value= CAST_STRING(VS.getContent(), log);
			//log.debug(Value +" <==> " + Var.getValue());
			if(!Var.getValue().equals(Value))
			{
				Var.setValue(Value);
			}
			break;
		case NUMBER:
			Value= CAST_NUMBER(VS.getContent(), log);
			//log.debug(Value +" <==> " + Var.getValue());
			if(!Var.getValue().equals(Value))
			{
				Var.setValue(Value);
			}
			break;
		case STARFACE_ACCOUNT:
		case STARFACE_GROUP:
		case STARFACE_USER:
			Value= CAST_STARFACE_ENTITY(VS.getContent(), log);
			//log.debug(Value +" <==> " + Var.getValue());
			if(!Var.getValue().equals(Value))
			{
				Var.setValue(Value);
			}
			break;
		case MAP:
			{
				ByteArrayInputStream BIS = new ByteArrayInputStream(Base64.getDecoder().decode(VS.getContent()));
				ObjectInputStream IN = new ObjectInputStream(BIS);
				Object O =IN.readObject();
				IN.close();			
				
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
				}				
			}
			break;
		case LIST:
		{
			ByteArrayInputStream BIS = new ByteArrayInputStream(Base64.getDecoder().decode(VS.getContent()));
			ObjectInputStream IN = new ObjectInputStream(BIS);
			Object O =IN.readObject();
			IN.close();		
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
				//log.debug(Values.toString() +"<==>"+LR.getValues().toString());
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
			}
			break;
		}
		case FILE_RESOURCE:
		case ASTERISK_SOUND_FILE:
		case SNOM_SOUND_FILE:
		case TIPTEL_SOUND_FILE:
		{
			if(VS.getContent().isEmpty())
			{
				break;
			}
			
			ByteArrayInputStream BIS = new ByteArrayInputStream(Base64.getDecoder().decode(VS.getContent()));
			ObjectInputStream IN = new ObjectInputStream(BIS);
			Object O =IN.readObject();
			IN.close();		
			
			FileSerializable FS = (FileSerializable) O;
			byte[] Content = Base64.getDecoder().decode(FS.getContentB64());
						
			File ResourcePath = new File("/var/starface/module/instances/repo/"+MIP.getObject().getId()+"/res/");
			ResourcePath.mkdirs();
			

			FileResource FR = new FileResource(FS.getName(), FileType.valueOf(FS.getType()), FS.getRealFileExtension());
			FR.setId(FS.getId());
			FR.setName(FS.getName());			
			
			File Realfile = new File(ResourcePath.getAbsolutePath()+"/"+FR.getId()+"."+FR.getRealFileExtension());
			
			FileUtils.writeByteArrayToFile(Realfile, Content);
			FR.setRealFile(Realfile);
			
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
			}
			
			Resources.add(FR);
			Var.setValue(FR.getId());
			MIP.getObject().setResources(Resources);
			
			break;
		}
		case TIMER_SETTINGS:
			
			ByteArrayInputStream BIS = new ByteArrayInputStream(Base64.getDecoder().decode(VS.getContent()));
			ObjectInputStream IN = new ObjectInputStream(BIS);
			Object O =IN.readObject();
			IN.close();		
			
			TimerSettingsSerializable TSS = (TimerSettingsSerializable) O;
			
			TimerSettingsResource TSR = MIP.getObject().getTimerSettings(TSS.getId());
			TSR.setMultiplikator(TSS.getMultiplikator());
			TSR.setStartTime(TSS.getStarttime());
			TSR.setPeriod(TimerInterval.valueOf(TSS.getPeriodname()));
			
			
			break;
		default:
			break;
		}
	}
		
	private String CAST_STARFACE_ENTITY(Object O, Logger log)
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
	
	private String CAST_NUMBER(Object O, Logger log)
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
	
	private String CAST_STRING(Object O, Logger log)
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
			
	private String CAST_BOOLEAN(Object O, Logger log)
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
