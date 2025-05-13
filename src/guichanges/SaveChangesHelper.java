package si.module.guichanges;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;

public class SaveChangesHelper 
{
	private static Map<String, Date> LastChanges = new HashMap<String, Date>();
//	private static SimpleDateFormat SDF = new SimpleDateFormat("YYYY.MM.dd-HH:ss");
	
	public static void AddChange(String id, IRuntimeEnvironment context)
	{
		Date D = new Date();
		//context.getLog().debug(id+ " " + SDF.format(D));
		LastChanges.put(id, D);
	}
	
	public static boolean IsTriggeredinTime(String id, IRuntimeEnvironment context)
	{	
		//context.getLog().debug(id);
		Date D = LastChanges.get(id);
		if (D == null) {return false;}
		
		
		Date Threshhold = DateUtils.addSeconds(new Date(), -10);
		//log.debug(id +" " + SDF.format(Threshhold) +" before " + SDF.format(D));
		return Threshhold.before(D);
	}
}
