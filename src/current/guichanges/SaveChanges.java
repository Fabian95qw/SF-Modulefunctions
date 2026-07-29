package si.module.guichanges;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.State;

import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.ModuleInstanceProject;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;


@Function(visibility=Visibility.Private, description="Save Changes to the ModuleInstanceProject")
public class SaveChanges implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="ModuleInstanceProject", description="The Object returned by GetModuleInstance4Edit",type=VariableType.OBJECT)
	public Object MIPObject=null;
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public boolean Success=false;
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		//Logger log = context.getLog();
		if(MIPObject == null)
		{
			//log.debug("ModuleInstanceProject is null!");
			return;
		}
		
		ModuleInstanceProject MIP = (ModuleInstanceProject) MIPObject;
		ModuleRegistry MR = (ModuleRegistry)context.springApplicationContext().getBean(ModuleRegistry.class);
		
		try 
		{
			//log.debug("Updating Instance " + MIP.getObject().getModuleName() +" " + MIP.getObject().getId());
			
			Thread T = Thread.currentThread();
			
			new Thread(new Runnable(){

				@Override
				public void run()
				{
					try
					{
						Integer MaxTimeout = 10;
						Integer Count =0;
						while(!T.getState().equals(State.TERMINATED) && Count < MaxTimeout)
						{
							context.getLog().trace(T.getName()+" "+T.getState().toString() +" "+Count+"/"+MaxTimeout);
							Count=Count+1;
							Thread.sleep(1000);
						}
						SaveChangesHelper.AddChange(MIP.getObject().getId(), context);
						MR.updateModuleInstance(MIP);
					}
					catch (Exception e)
					{
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						context.getLog().debug(sw.toString()); //
					}
					catch(Error e)
					{
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						context.getLog().debug(sw.toString()); //
					}

				}}).start();;
			
			Success=true;
		}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			context.getLog().debug(sw.toString()); //
		}
		
	}//END OF EXECUTION

	
}
