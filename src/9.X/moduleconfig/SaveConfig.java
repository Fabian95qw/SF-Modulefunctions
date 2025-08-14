
package si.module.moduleconfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.Variable;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.model.resource.FileResource;
import de.vertico.starface.module.core.model.resource.ListResource;
import de.vertico.starface.module.core.model.resource.MapResource;
import de.vertico.starface.module.core.model.resource.TimerSettingsResource;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import si.module.moduleconfig.serializers.FileSerializable;
import si.module.moduleconfig.serializers.TimerSettingsSerializable;
import si.module.moduleconfig.serializers.VariableSerializable;

@Function(visibility=Visibility.Private, description="")
public class SaveConfig implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="UUID", description="TargetFile",type=VariableType.STRING)
	public String UUID="";
	
	@InputVar(label="TargetFile", description="TargetFile",type=VariableType.STRING)
	public String TargetFile="";
		    	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		ModuleRegistry ModReg = context.provider().fetch(ModuleRegistry.class);
		
		List<Variable> VarList = ModReg.getInstance(UUID).getInputVars();
				
		List<VariableSerializable> Variables = new ArrayList<VariableSerializable>();
		
		for(Variable V : VarList)
		{
			VariableSerializable VS = new VariableSerializable();
			//log.debug("Writing: " + V.getName() + " ==> " + V.getType().toString() + " ==> " + V.getValue());
			
			VS.setName(V.getName());
			VS.setType(V.getType().toString());
			
			try
			{
			switch(V.getType())
			{
			case BOOLEAN:
			case NUMBER:
			case STARFACE_GROUP:
			case STARFACE_USER:
			case STRING:
				VS.setContent(V.getValue());
				break;
			case ASTERISK_SOUND_FILE:
			case SNOM_SOUND_FILE:
			case TIPTEL_SOUND_FILE:
			case FILE_RESOURCE:

				if(!V.getValue().isEmpty())
				{	
					FileResource FR  = null;
					try
					{
						FR = context.getInvocationInfo().getModuleInstance().getFileResource(V.getValue());

						ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
						ObjectOutputStream OOS = new ObjectOutputStream(BAOS);
						
						FileSerializable FS = new FileSerializable(FR);
						
						OOS.writeObject(FS);
						OOS.flush();
						OOS.close();
						VS.setContent(Base64.getEncoder().encodeToString((BAOS.toByteArray())));	
					}
					catch(Exception e)
					{
						EtoStringLog(log, e);
						log.debug("Failed to Save File: " + FR.getRealFile().getAbsolutePath());
					}
				}
				break;
			case LIST:
				if(!V.getValue().isEmpty())
				{
					ListResource LR = context.getInvocationInfo().getModuleInstance().getListResource(V.getValue());
					
					if(LR != null && LR.getValues() != null)
					{
						List<String> List = new ArrayList<String>();
						List = LR.getValues();
						ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
						ObjectOutputStream OOS = new ObjectOutputStream(BAOS);
						OOS.writeObject(List);
						OOS.flush();
						OOS.close();
						VS.setContent(Base64.getEncoder().encodeToString((BAOS.toByteArray())));
					}
				}
				break;
			case MAP:
				if(!V.getValue().isEmpty())
				{
					MapResource MR = context.getInvocationInfo().getModuleInstance().getMapResource(V.getValue());
					if(MR != null && MR.getMap() != null)
					{
						Map<String, String> Map = new HashMap<String, String>();
						Map = MR.getMap();
						ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
						ObjectOutputStream OOS = new ObjectOutputStream(BAOS);
						OOS.writeObject(Map);
						OOS.flush();
						OOS.close();
						VS.setContent(Base64.getEncoder().encodeToString((BAOS.toByteArray())));
						break;
					}
				}
				break;

			case TIMER_SETTINGS:
				if(!V.getValue().isEmpty())
				{
					TimerSettingsResource TSR = context.getInvocationInfo().getModuleInstance().getTimerSettings(V.getValue());
					
					ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
					ObjectOutputStream OOS = new ObjectOutputStream(BAOS);
					
					TimerSettingsSerializable TSer = new TimerSettingsSerializable(TSR);
					
					OOS.writeObject(TSer);
					OOS.flush();
					OOS.close();
					VS.setContent(Base64.getEncoder().encodeToString((BAOS.toByteArray())));				
				}
				break;
			default:
				log.debug("Skipped: " + V.getName() +" " + V.getType());
				break;
			}
			log.debug(VS.toString());
			Variables.add(VS);
			}
			catch(Exception e)
			{
				log.error("Error while Exporting: "+V.getName());
				EtoStringLog(log, e);
			}
		}

		
		File F= new File(TargetFile);
		if(F.exists())
		{
			F.delete();
		}
		
		log.debug("Exporting to:" + TargetFile);
		FileOutputStream FOS = new FileOutputStream(F);
		ObjectOutputStream OOS = new ObjectOutputStream(FOS);
		OOS.writeObject(Variables);
		OOS.flush();
		OOS.close();
		log.debug("Export Completed");
		
	}//END OF EXECUTION

	public static void EtoStringLog(Logger log, Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}
	
}
