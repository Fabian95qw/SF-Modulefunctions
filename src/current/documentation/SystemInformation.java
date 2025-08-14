package si.module.examples.documentation;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.starface.pbx.starfaceconfig.PbxConfigurationService;

import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;


@Function(visibility=Visibility.Private, description="")
public class SystemInformation implements IBaseExecutable 
{
	//##########################################################################################
		 
	@OutputVar(label="SFVersion", description="",type=VariableType.STRING)
	public String SFVersion="";
	
	@OutputVar(label="Builddate", description="",type=VariableType.STRING)
	public String Builddate="";
	
	@OutputVar(label="Lastupdate", description="",type=VariableType.STRING)
	public String Lastupdate="";
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		PbxConfigurationService PCS = context.springApplicationContext().getBean(PbxConfigurationService.class);
		
		SFVersion = context.springApplicationContext().getBean("pbxVersion", String.class);
		SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");
		Instant I =  context.springApplicationContext().getBean("buildDate", Instant.class);
		Builddate = SDF.format(Date.from(I));
		//Lastupdate = SDF.format(Version.getLatestUpdateTime());
		Long L = PCS.update().latestTime().getValueOrDefault();
		if(L!= null && L != 0L)
		{
			Lastupdate = SDF.format(new Date(L));
		};
		
		
	}//END OF EXECUTION


	
}
