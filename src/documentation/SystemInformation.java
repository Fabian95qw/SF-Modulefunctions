package si.module.documentation.collection;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.StarfaceReleaseInfo;
import de.vertico.starface.Version;
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
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		SFVersion = StarfaceReleaseInfo.getVersion();
		SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");
		Builddate = SDF.format(Date.from(StarfaceReleaseInfo.getBuildDate()));
		Lastupdate = SDF.format(Version.getLatestUpdateTime());
		
		
	}//END OF EXECUTION


	
}
