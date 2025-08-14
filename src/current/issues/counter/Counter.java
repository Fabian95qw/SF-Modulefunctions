package si.module.examples.issues.counter;

import org.apache.logging.log4j.Logger;

public class Counter implements Runnable 
{
	private Logger log = null;
	private Integer Count = 0;
	
	public Counter(Logger log2)
	{
		this.log=log2;
		log2.debug("Hello I'm a counter");
	}
	
	@Override
	public void run() 
	{
		while(true)
		{
			log.debug("Count: "+ Count);
			Count = Count++;
			try
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) 
			{
				break;
			}
		}		
	}

}
