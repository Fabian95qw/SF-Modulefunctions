package nucom.module.msgraphs.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONConverter 
{
    public static Map<String, Object> toMap(JSONObject JSO)
    {
    	Map<String, Object> Map = new HashMap<String, Object>();
    	for(Object Key : JSO.keySet())
    	{
    		String SKey = (String) Key;
    		Object Value = JSO.get(Key);
    		
    		if(Value instanceof JSONArray)
    		{
    			Map.put(SKey, toList((JSONArray) Value));
    		}
    		else if (Value instanceof JSONObject)
    		{
    			Map.put(SKey, toMap((JSONObject) Value));
    		}
    		else
    		{
    			Map.put(SKey, Value);
    		}
    	}
    	return Map;
    }
    
    public static List<Object> toList(JSONArray JSO)
    {
    	List<Object> List = new ArrayList<Object>();
    	
    	for(Object Value : JSO)
    	{
    		if(Value instanceof JSONArray)
    		{
    			List.add(toList((JSONArray) Value));
    		}
    		else if (Value instanceof JSONObject)
    		{
    			List.add(toMap((JSONObject) Value));
    		}
    		else
    		{
    			List.add(Value);
    		}
    	}
    	
    	return List;
    }
}
