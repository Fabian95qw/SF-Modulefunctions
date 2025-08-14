package si.module.examples.documentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;


import de.vertico.starface.config.phone.forms.PhoneListBean;
import de.vertico.starface.config.phone.forms.SnomUserListBean;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.SipAndPhonesHandler;
import de.vertico.starface.persistence.connector.SipAndPhonesHandler.DeviceFunctionType;
import io.jsonwebtoken.lang.Objects;

@Function(visibility=Visibility.Private, description="")
public class GetAllPhones implements IBaseExecutable 
{
	//##########################################################################################
			    	
	@OutputVar(label="Phones", description="",type=VariableType.LIST)
	public List<Map<String, String>> Phones = new ArrayList<Map<String, String>>();
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		log.debug("Extracting Phonedata");
		
		SipAndPhonesHandler SIPH = context.springApplicationContext().getBean(SipAndPhonesHandler.class);
		
		for(PhoneListBean PLB : SIPH.getPhoneListWithAssignedUser("", ""))
		{
			Map<String, String> M = new HashMap<String, String>(); //Store all the Phonedata in this map.
			M.put("id", Objects.nullSafeToString(PLB.getId()));
			M.put("ip", Objects.nullSafeToString(PLB.getIp()));
			M.put("name", Objects.nullSafeToString(PLB.getSipname()));
			
			if(PLB.getIpui() != null)
			{
				M.put("ipui", Objects.nullSafeToString(PLB.getIpui()));
			}
			else
			{
				M.put("ipui", "");
			}
			
			M.put("mac", Objects.nullSafeToString(PLB.getMac()));		
			M.put("typename", Objects.nullSafeToString(PLB.getName()));
			M.put("isonline", Objects.nullSafeToString(PLB.getOnline()));
			//M.put("pin", Objects.nullSafeToString(PLB.getPin()));	 //Unknown Purpose
			//M.put(Phone.isprivateip.toString(), Objects.nullSafeToString(PLB.getPrivateIpAddress())); //Unknown Purpose
			M.put("serveraddress", Objects.nullSafeToString(PLB.getServerAddress()));
			//M.put(Phone.sipname.toString(), Objects.nullSafeToString(PLB.getSipname())); //Unknown Purpose
			M.put("assignedusers", ExtractUsers(PLB.getUserList())); //Extract the Users into a List
				
			DeviceFunctionType DFT = SIPH.getFunctionType4Telephone("SIP/"+PLB.getSipname()); //Get Defined Functiontype
			
			if(DFT != null)
			{
				M.put("devicefunctiontype", DFT.toString());
			}
			else
			{
				M.put("devicefunctiontype", "Unknown");
			}
			
			Phones.add(M); //Add this to the Phones list
		}
		
		log.debug("Total Phones: "+ Phones.size());
		
	}//END OF EXECUTION

	private String ExtractUsers(List<SnomUserListBean> UserList)
	{
		StringBuilder SB = new StringBuilder();
		Boolean First = true;
		for(SnomUserListBean SLB : UserList)
		{
			String Firstname = Objects.nullSafeToString(SLB.getFirstName());
			String Familyname = Objects.nullSafeToString(SLB.getFamilyName());
			if(First)
			{
				First = false;
			}
			else
			{
				SB.append(System.lineSeparator());
			}
			SB.append(Firstname +" "+Familyname);
			
		}
		return SB.toString();
	}
	
}
