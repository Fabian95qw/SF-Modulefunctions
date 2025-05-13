package si.module.documentation.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;

@Function(visibility=Visibility.Private, description="")
public class SortLogic implements IBaseExecutable 
{
	//##########################################################################################
			    	
	@InputVar(label="SortColumnName", description="", type=VariableType.STRING)
	public String SortColumnName="";
	
	@InputVar(label="Ascending", description="", type=VariableType.BOOLEAN)
	public Boolean Ascending = true;
	
	@InputVar(label="DataRaw", description="",type=VariableType.OBJECT)
	public Object DataRaw = null;
	
	@InputVar(label="DataSorted", description="",type=VariableType.LIST)
	public List<Map<String, Object>> DataSorted = new ArrayList<Map<String, Object>>();
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> Data = (List<Map<String, Object>>) DataRaw;
		
		log.debug("Sorting Data by: " + SortColumnName);
		
		Collections.sort(Data, mapComparator);
		
		if(!Ascending)
		{
			log.debug("Inverting Collection...");
			Collections.reverse(Data);
		}

		log.debug("Sorting Completed");
		
		DataSorted = Data;

	}//END OF EXECUTION

	
	public Comparator<Map<String, Object>> mapComparator = new Comparator<Map<String, Object>>() 
	{
	    public int compare(Map<String, Object> m1, Map<String, Object> m2) 
	    {
	    	Object O1 =m1.get(SortColumnName);
	    	Object O2 =m2.get(SortColumnName);
	    	
	    	if(O1 instanceof String && O2 instanceof String)
			{
				String S1 = (String) O1;
				String S2 = (String) O2;
				
	    		try
	    		{
	    			Integer I1 = Integer.parseInt(S1);
	    			Integer I2 = Integer.parseInt(S2);
	    			return I1.compareTo(I2);
	    		}catch(Exception e)	{}
	    		
	    		try
	    		{
	    			Boolean B1 = Boolean.parseBoolean(S1);
	    			Boolean B2 = Boolean.parseBoolean(S2);
	    			return B1.compareTo(B2);
	    		}catch(Exception e)	{}
	    		
	    		
				return S1.compareTo(S2);
			}
	    	else if(O1 instanceof Integer && O2 instanceof Integer)
	    	{
				Integer I1 = (Integer) O1;
				Integer I2 = (Integer) O2;
				return I1.compareTo(I2);
	    	}
	    	else if(O1 instanceof Long && O2 instanceof Long)
	    	{
				Long L1 = (Long) O1;
				Long L2 = (Long) O2;
				return L1.compareTo(L2);
	    	}
	    	else if(O1 instanceof Double && O2 instanceof Double)
	    	{
				Double D1 = (Double) O1;
				Double D2 = (Double) O2;
				return D1.compareTo(D2);
	    	}
	    	else if(O1 instanceof Boolean && O2 instanceof Boolean)
	    	{
	    		Boolean B1 = (Boolean) O1;
	    		Boolean B2 = (Boolean) O2;
				return B1.compareTo(B2);
	    	}
	    	return -1;
	    }
	};


	
}
