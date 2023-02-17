package si.module.modulefunction;

import java.util.List;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.Module;
import de.vertico.starface.module.core.model.ModuleInstance;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, rookieFunction=false, description="")
public class GetInstanceStatebyName implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="InstanceName", description="",type=VariableType.STRING)
	public String InstanceName="";
	
	@OutputVar(label="Disabled", description="",type=VariableType.BOOLEAN)
	public Boolean Disabled = false;

    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	  public void execute(IRuntimeEnvironment context) throws Exception
	  {
	    Logger log = context.getLog();

	    if(InstanceName.isEmpty()) //If no instancename supplied
	    {
	      log.debug("No Instance Name Supplied");
	      return;
	    }

	    ModuleRegistry MR = (ModuleRegistry)context.provider().fetch(ModuleRegistry.class); //Fetch Moduleregistry

	    List<Module> Modules = MR.getModules(); //Fetch all currently installed Modules

	    List<ModuleInstance> ModuleInstances = null;
	    log.debug("Looking for: " + InstanceName);
	    for(Module M: Modules) //For each Module
	    {
	      ModuleInstances  = MR.getInstances4Module(M.getId()); //Fetch all Instances
	      for(ModuleInstance MI: ModuleInstances)  //For each Instance
	      {
	       // log.debug(MI.getName() + " <==> " +InstanceName);
	        if(MI.getName().equals(InstanceName)) //Check if name Machtes
	        {
	          log.debug(MI.getName() + "==> "+ MI.getId() + " <==> " + InstanceName);
	          Disabled = MI.getDisabled();
	          break; //Break, because Instancenames are Unique
	        }
	      }
	    }
	  }//END OF EXECUTION
}
