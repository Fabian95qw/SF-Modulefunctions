package nucom.module.msgraphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.IUserCollectionPage;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import nucom.module.msgraphs.utility.LogHelper;

@Function(visibility=Visibility.Public, rookieFunction=false, description="Returns List<User>")
public class ConvertPagetoUsers implements IBaseExecutable 
{
	//##########################################################################################

	@InputVar(label="UserPage", description="UserPage Object",type=VariableType.OBJECT)
	public Object IUserPage = null;
	
	@OutputVar(label="Users", description="Returns a List<Map<String,Object>>",type=VariableType.LIST)
	public List<Map<String, Object>> Users = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		
		IUserCollectionPage UserPage = null;
		
		log.debug("Converting Page into Users");
		try
		{
			UserPage = (IUserCollectionPage)IUserPage;
			Users = new ArrayList<Map<String, Object>>();
			for(User User : UserPage.getCurrentPage())
			{
				Users.add(toMap(User.getRawObject()));
			}
		}
		catch(Exception e)
		{
			log.debug("Converting Users failed");
			LogHelper.EtoStringLog(log, e);
			return;
		}
		

	}//END OF EXECUTION

	
    public Map<String, Object> toMap(JsonObject jsonObject)  throws JSONException 
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<String> keys = jsonObject.keySet();
        for(String key : keys) 
        {
            Object value = jsonObject.get(key);
            if (value instanceof JsonArray) 
            {
                value = toList((JsonArray) value);
            }
            else if (value instanceof JsonObject)
            {
                value = toMap((JsonObject) value);
            }   
            map.put(key, value);
        }   return map;
    }
    
    public List<Object> toList(JsonArray array) throws JSONException 
    {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.size(); i++) 
        {
            Object value = array.get(i);
            if (value instanceof JSONArray) 
            {
                value = toList((JsonArray) value);
            }
            else if (value instanceof JsonObject) 
            {
                value = toMap((JsonObject) value);
            }
            list.add(value);
        }  
        return list;
    }
	
	
}
