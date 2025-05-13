package si.module.documentation.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.bo.UserBusinessObject;
import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.module.core.runtime.functions.entities.GetUsersOfGroup2;
import de.vertico.starface.persistence.connector.CATConnectorPGSQL;
import de.vertico.starface.persistence.connector.GroupHandler;
import de.vertico.starface.persistence.connector.CATConnectorPGSQL.TelephoneNumberType;
import de.vertico.starface.persistence.databean.core.ExtendedUserData;
import de.vertico.starface.persistence.databean.core.Group;
import de.vertico.starface.persistence.databean.core.PhoneNumberBean;
import io.jsonwebtoken.lang.Objects;

@Function(visibility=Visibility.Private, description="")
public class GetAllGroups implements IBaseExecutable 
{
	//##########################################################################################
			    	
	@InputVar(label="OnlyActiveUsers", description="", type=VariableType.BOOLEAN)
	public boolean OnlyActiveUsers=true;
	
	@OutputVar(label="Groups", description="",type=VariableType.LIST)
	public List<Map<String, Object>> Groups = new ArrayList<Map<String, Object>>();
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		log.debug("Extracting Groupdata");
		
		GroupHandler GH = context.provider().fetch(GroupHandler.class);
		//GroupBusinessObject GBO = context.provider().fetch(GroupBusinessObject.class);
		CATConnectorPGSQL CAT = context.provider().fetch(CATConnectorPGSQL.class);
				
		for(Group G : GH.getGroups())
		{
			Map<String, Object> Map = new HashMap<String, Object>();
			Map.put("starface_account", Objects.nullSafeToString(G.getAccountId()));

			//ExtendedGroup EG = GBO.getGroupByAccountId(G.getAccountId());
						
			Map.put("name", Objects.nullSafeToString(G.getDescription()));
			
			String Type = GH.getShortServiceName(G.getAccountId());
			
			if(Type.startsWith("PluginSelectorService"))
			{
				Map.put("type", "Modul");
			}
			else
			{
				Type = Type.replace("Service", "");
				Map.put("type", Type);
			}

			
			StringBuilder SB = new StringBuilder();
			Boolean First = true;
			for(PhoneNumberBean PNB: CAT.getNumberList4AccountId(G.getAccountId(), TelephoneNumberType.EXTERNAL, true, false))
			{
				//log.debug(PNB);
				if(First)
				{
					SB.append(CastPhoneNumberBean(PNB));
					First =false;
				}
				else
				{
					SB.append(System.lineSeparator());
					SB.append(CastPhoneNumberBean(PNB));
				}
			}
			
			Map.put("extnumbers", SB.toString());
			SB = new StringBuilder();
			First = true;
			for(PhoneNumberBean PNB: CAT.getNumberList4AccountId(G.getAccountId(), TelephoneNumberType.INTERNAL, true, false))
			{
				//log.debug(PNB);
				if(First)
				{
					SB.append(CastPhoneNumberBean(PNB));
					First =false;
				}
				else
				{
					SB.append(System.lineSeparator());
					SB.append(CastPhoneNumberBean(PNB));
				}
			}
			Map.put("intnumbers", SB.toString());
			
			//log.debug(EG.toString());
			
			GetUsersOfGroup2 GOG = new GetUsersOfGroup2();
			GOG.groupId = G.getAccountId();

			Integer Active = 0;
			Integer All = 0;
			GOG.activeOnly=true;
			GOG.excludeDND=false;
			GOG.execute(context);
			
			Active = GOG.usersOfGroup.size();
			List<Integer> ActiveUsers = GOG.usersOfGroup;
			
			GOG.activeOnly=false;
			GOG.execute(context);
			All = GOG.usersOfGroup.size();
			List<Integer> AllUsers = GOG.usersOfGroup;
				
			Map.put("activeusers", ""+Active);
			Map.put("totalusers", ""+All);
			
			
			List<Map<String, String>>Users = ExtractUsers(AllUsers, ActiveUsers, context);
			Map.put("users", Users);
			//log.debug(Users);
					
			Groups.add(Map);
		}
		
		log.debug("Total Groups: "+ Groups.size());
		
	}//END OF EXECUTION
	
	private List<Map<String, String>> ExtractUsers(List<Integer> AllUsers, List<Integer> ActiveUsers, IRuntimeEnvironment context) 
	{
		UserBusinessObject UBO = context.provider().fetch(UserBusinessObject.class);
		//Logger log =context.getLog();
		List<Map<String, String>>Users = new ArrayList<Map<String, String>>();
		for(Integer I : AllUsers)
		{
			//log.debug("Working on ID:" + I); 
			ExtendedUserData EUD = UBO.getUserByAccountId(I);
			Map<String, String> Map = new HashMap<String, String>();
			Map.put("firstname", Objects.nullSafeToString(EUD.getFirstName()));
			Map.put("lastname", Objects.nullSafeToString(EUD.getFamilyName()));
			Map.put("loginid", Objects.nullSafeToString(EUD.getLoginNumber()));
			if(ActiveUsers.contains(I))
			{
				Map.put("active", "true");
			}
			else
			{
				if(OnlyActiveUsers)
				{
					continue;
				}
				Map.put("active", "false");
			}
			Users.add(Map);
		}
		
		return Users;
	}

	private String CastPhoneNumberBean(PhoneNumberBean PNB)
	{
		if(PNB == null) {return "";}
		
		return Objects.nullSafeToString(PNB.getPhoneNumberWithNationalPrefix());
	}


	
}
