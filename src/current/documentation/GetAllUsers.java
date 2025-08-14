package si.module.examples.documentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.bo.UserBusinessObject;

import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.CATConnectorPGSQL;
import de.vertico.starface.persistence.connector.CATConnectorPGSQL.DataDefault;
import de.vertico.starface.persistence.connector.CATConnectorPGSQL.TelephoneNumberType;
import de.vertico.starface.persistence.connector.PersonAndAccountHandler;
import de.vertico.starface.persistence.databean.config.phone.PhoneBeanLite;
import de.vertico.starface.persistence.databean.core.ExtendedUserData;
import de.vertico.starface.persistence.databean.core.Permission;
import de.vertico.starface.persistence.databean.core.PhoneNumberBean;
import de.vertico.starface.persistence.databean.core.PhoneNumberBeanLite;
import io.jsonwebtoken.lang.Objects;

@Function(visibility=Visibility.Private, description="")
public class GetAllUsers implements IBaseExecutable 
{
	//##########################################################################################
			    	
	@InputVar(label="ShowGroupNumbers", description="", type=VariableType.BOOLEAN)
	public boolean ShowGroupNumbers=true;
	
	@OutputVar(label="Users", description="",type=VariableType.LIST)
	public List<Map<String, String>> Users = new ArrayList<Map<String, String>>();
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		log.debug("Extracting Userdata");
		
		PersonAndAccountHandler PAH = context.springApplicationContext().getBean(PersonAndAccountHandler.class);
		UserBusinessObject UBO = context.springApplicationContext().getBean(UserBusinessObject.class);
		CATConnectorPGSQL CAT = context.springApplicationContext().getBean(CATConnectorPGSQL.class);
		
		List<Integer> UCIPremium = PAH.getAccountsWithPermission(Permission.UCI_AUTOPROVISIONING.getPermissionId());
		List<Integer> TS = PAH.getAccountsWithPermission(Permission.WINCLIENT_TERMINAL_SERVER.getPermissionId());
		
		for(ExtendedUserData EUD : UBO.getUsers())
		{
			Map<String, String> Map = new HashMap<String, String>();
			Map.put("starface_account", Objects.nullSafeToString(EUD.getAccountId()));
			Map.put("isadmin", Objects.nullSafeToString(EUD.isAdmin()));
			Map.put("isdnd", Objects.nullSafeToString(EUD.isDnd()));
			Map.put("firstname", Objects.nullSafeToString(EUD.getFirstName()));
			Map.put("lastname", Objects.nullSafeToString(EUD.getFamilyName()));
			Map.put("loginid", Objects.nullSafeToString(EUD.getLoginNumber()));
			Map.put("licensetype", Objects.nullSafeToString(EUD.getLicenseType()));
			Map.put("email", Objects.nullSafeToString(EUD.getEmail()));
			
			
			
			if(UCIPremium.contains(EUD.getAccountId()))
			{
				Map.put("uccpremiumlicense", "true");
			}
			else
			{
				Map.put("uccpremiumlicense", "false");
			}
				
			if(TS.contains(EUD.getAccountId()))
			{
				Map.put("tslicense", "true");
			}
			else
			{
				Map.put("tslicense", "false");
			}
			
			
			String callwaitingindication = PAH.getPersonData(EUD.getAccountId(), DataDefault.CALL_WAITING_INDICATION, 0);
			if(callwaitingindication == null)
			{
				Map.put("callwaitingindication", "false");

			}
			else
			{
				Map.put("callwaitingindication", Objects.nullSafeToString(callwaitingindication.equals("1")));

			}

			String MainExtPhoneNumber = CastPhoneNumberBeanLite(EUD.getExtPhoneNumber());
			String MainIntPhoneNumber = CastPhoneNumberBeanLite(EUD.getIntPhoneNumber());	
			
			Map.put("mainintphonenumber", MainIntPhoneNumber);
			Map.put("mainextphonenumber", MainExtPhoneNumber);
			
			Integer PrimaryPhoneId = CAT.getPrimaryTelephoneId4accountid(EUD.getAccountId());
			
			StringBuilder SB = new StringBuilder();
			Boolean First = true;
			for(PhoneBeanLite PBL: CAT.getPhoneList4AccountId(EUD.getAccountId()))
			{
				if(PBL.getId() == PrimaryPhoneId)
				{
					SB.append("*");
				}
				//log.debug(PNB);
				if(First)
				{
					SB.append(PBL.getName());
					First =false;
				}
				else
				{
					SB.append(System.lineSeparator());
					SB.append(PBL.getName());
				}
			}
			Map.put("assignedphones", SB.toString());

			//#########################################
			SB = new StringBuilder();
			if(!MainExtPhoneNumber.isEmpty())
			{
				SB.append("*"+MainExtPhoneNumber);
			}

			for(PhoneNumberBean PNB: CAT.getNumberList4AccountId(EUD.getAccountId(), TelephoneNumberType.EXTERNAL, ShowGroupNumbers, false))
			{
				String S = CastPhoneNumberBeanLite(PNB);
				if(PNB.getGroupAccountId() != -1)
				{
					S="[G]"+S;
				}
				if(S.equals(MainExtPhoneNumber)) {continue;};
					SB.append(System.lineSeparator());
					SB.append(S);
			}
			
			Map.put("extnumbers", SB.toString());
			
			//#########################################
			SB = new StringBuilder();
			if(!MainIntPhoneNumber.isEmpty())
			{
				SB.append("*"+MainIntPhoneNumber+" ");
			}
			
			for(PhoneNumberBean PNB: CAT.getNumberList4AccountId(EUD.getAccountId(), TelephoneNumberType.INTERNAL, ShowGroupNumbers, false))
			{
				String S = CastPhoneNumberBeanLite(PNB);
				if(PNB.getGroupAccountId() != -1)
				{
					S="[G]"+S;
				}

				if(S.equals(MainIntPhoneNumber)) {continue;};
				SB.append(System.lineSeparator());
				SB.append(S);
			}
			Map.put("intnumbers", SB.toString());
					
			Map.put("primaryphone", CastPhoneBeanLite(EUD.getPhone()));
			Users.add(Map);
		}
		
		log.debug("Total Users: "+ Users.size());
		
	}//END OF EXECUTION

	

	
	private String CastPhoneNumberBeanLite(PhoneNumberBeanLite PNBL)
	{
		if(PNBL == null) {return "";}
		
		return Objects.nullSafeToString(PNBL.getPhoneNumberWithNationalPrefix());
	}

	private String CastPhoneBeanLite(PhoneBeanLite PBL)
	{
		if(PBL == null) {return "";}
		return Objects.nullSafeToString(PBL.getName());
	}

	
	
}
