package si.module.moduleupdater;


import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.StarfaceReleaseInfo;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(name="Moduleupdater.Update", visibility=Visibility.Private, rookieFunction=false, description="HTTP(S) GET a Metafile and Updates for any module")
public class Update implements IBaseExecutable 
{
	//##########################################################################################
	@InputVar(label="http(s) URL", description="Target URL",type=VariableType.STRING)
	public String URL ="";

	@InputVar(label="Get-Arguments", description="Attaches those values to URL as get arguments [URL]?<Key>=<Value>&<Key>=<Value>...",type=VariableType.MAP)
	public Map<String, String> GetArgs = null;
		
	@InputVar(label="AllowRedirect", description="Allow URL-Redirection (302)",type=VariableType.BOOLEAN)
	public boolean AllowRedirect=false;
	
	@InputVar(label="Module ID", description="Type in your module identifiers from your meta.xml",type=VariableType.STRING)
	public String Module_ID="";
	
	@InputVar(label="Module ID Auto-detect", description="Uses this module UUID as ID",type=VariableType.BOOLEAN)
	public Boolean ModuleIDAutoDetect = false;
	
	@InputVar(label="Module Version", description="The Module version of this module",type=VariableType.STRING)
	public String Module_Version="";
	
	@InputVar(label="Module Version Auto-Detect", description="Use the Starface module version",type=VariableType.BOOLEAN)
	public Boolean ModuleVersionAutoDetect = false;
	
	@InputVar(label="Manual Starface Version", description="Starface version",type=VariableType.STRING)
	public String Starface_Version="";
	
	@InputVar(label="Starface Version Auto-detect", description="Auto-detects the Starface version",type=VariableType.BOOLEAN)
	public Boolean SFVersionAutoDetect=false;
		
	@InputVar(label="Starface Version Fuzzy Search", description="Does Fuzzy Search, if the exact version was not found. Example: 6.1.2.3 = Not found looking for => 6.1.2 = Not found looking for => 6.1 = Not found looking for => 6",type=VariableType.BOOLEAN)
	public Boolean DoFuzzySearch=false;
	
	@InputVar(label="DoUpdate", description="Wherever to actually do the Update, otherwise the info is just returned",type=VariableType.BOOLEAN)
	public Boolean DoUpdate=false;
	
	@OutputVar(label="hasUpdate", description="Returns True when an Update would be available",type=VariableType.BOOLEAN)
	public Boolean hasUpdate=false;
	
	@OutputVar(label="ReleaseNote", description="Content from the \"Release.txt\" from the Current Version",type=VariableType.STRING)
	public String ReleaseNote = "";
	
	@OutputVar(label="NewVersion", description="Version of the Module's Update",type=VariableType.STRING)
	public String NewVersion = "";
	
	@OutputVar(label="VersionMap", description="A Map of all available Versions of this Module on the Server. Map<String(SF-Version), String(Module-Version)>",type=VariableType.MAP)
	public Map<String,String> VersionMap = null;
	
	@OutputVar(label="hasError", description="Return True when an Error Occured, while Downloading or Updating an Module. Not finding a module on an updateserver is not an error",type=VariableType.BOOLEAN)
	public Boolean hasError=false;
	
	@OutputVar(label="Errormessage", description="Returns the Error Message.",type=VariableType.STRING)
	public String Errormessage ="";
		
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
    Logger log = null;
     
    Boolean HTTPS = false;    
    String UpdateFolder = "/tmp/mupdate";
    
    
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		log = context.getLog();
		ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class);
						
		if(!URL.endsWith("/"))
		{
			URL = URL+"/";
		}
		
		if(ModuleIDAutoDetect)
		{
			Module_ID = context.getInvocationInfo().getModule().getId();
		}
		
		if(ModuleVersionAutoDetect)
		{
			Module_Version = ""+MR.getModule(Module_ID).getVersion();
		}
		
		if(SFVersionAutoDetect)
		{
			Starface_Version = StarfaceReleaseInfo.getVersion();
		}
		
		log.debug("Module ID: " + Module_ID);
		log.debug("Module Version: " + Module_Version);
		log.debug("Starface Version: " + Starface_Version);
		log.debug("Url:" + URL);
		
		HTTPS = URL.startsWith("https");
		
		Updater U = new Updater(URL, GetArgs, HTTPS,AllowRedirect, Module_ID, Module_Version, Starface_Version, DoFuzzySearch, context);
		if(U.LoadMeta())
		{
			U.LoadModuleInfo();
			hasUpdate = U.hasUpdate();
			VersionMap = U.getVersionMap();
			if(hasUpdate)
			{
				ReleaseNote = U.LoadReleaseNote();
				NewVersion = U.getNewVersion();
				
				
				if(DoUpdate)
				{
					U.LoadUpdate();
					hasError= U.hasError();
					Errormessage = U.getErrormessage();
				}
			}
		}
		else
		{
			Errormessage=U.getErrormessage();
			hasError=true;
		}
	}
}
