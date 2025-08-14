package si.module.examples.issues.counter;

import org.apache.logging.log4j.Logger;

import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;

@Function(visibility=Visibility.Private, description="")
public class StartCounter implements IBaseExecutable 
{
	private static Thread CounterThread = null; //Static Thread Storage

	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		if(CounterThread == null)
		{
			log.debug("Creating new Counter!");
			Counter C = new Counter(log);
			CounterThread = new Thread(C);
			CounterThread.start();
		}
	}
}
