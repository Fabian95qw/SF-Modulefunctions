package si.module.examples.documentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;


import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.model.Module;
import de.vertico.starface.module.core.model.ModuleInstance;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.CATConnectorPGSQL;
import de.vertico.starface.persistence.databean.core.PhoneNumberInfoBean;

@Function(visibility=Visibility.Private, description="")
public class GetAllModules implements IBaseExecutable 
{
	//##########################################################################################
	
	@OutputVar(label="Modules", description="",type=VariableType.LIST)
	public List<Map<String, Object>> Modules = new ArrayList<Map<String, Object>>();
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		log.debug("Extracting Modules");
		
		ModuleRegistry MR = (ModuleRegistry)context.springApplicationContext().getBean(ModuleRegistry.class);		
    	CATConnectorPGSQL CAT = (CATConnectorPGSQL)context.springApplicationContext().getBean(CATConnectorPGSQL.class);
    	
		for(Module M : MR.getModules())
		{
			Map<String, Object> Map = new HashMap<String, Object>();
			
			if(M.getVersion() == 0L && M.getVendor().isEmpty())
			{
				continue;
			}
			
			Map.put("id", M.getId());
			Map.put("name", M.getName());
			Map.put("version", M.getVersion());
			Map.put("vendor", M.getVendor());

			List<Map<String, String>> Instances = new ArrayList<Map<String, String>>();
			for(ModuleInstance MIS: MR.getInstances4Module(M.getId()))
			{
				Map<String, String> Instance = new HashMap<String, String>();
				Instance.put("id", MIS.getId());
				Instance.put("name", MIS.getName());
				Instance.put("disabled", ""+MIS.getDisabled());
				
				Instance.put("phonenumbers", ExtracPhonenumbers(MIS.getId(), CAT.getAssignedModuleNumbers(0, "", MR)));
				
				Instances.add(Instance);
			}
			Map.put("instances", Instances);
			
			Modules.add(Map);
		}
		
		log.debug("Total Modules: "+ Modules.size());
		
	}//END OF EXECUTION


	private String ExtracPhonenumbers(String Instanceid, List<PhoneNumberInfoBean> Phonebeans)
	{
		StringBuilder SB = new StringBuilder();
		Boolean First = true;
		for(PhoneNumberInfoBean PNIB : Phonebeans)
		{
			if(PNIB.getIndividualID().equals(Instanceid))
			{		
				if(First)
				{
					First = false;
				}
				else
				{
					SB.append(System.lineSeparator());
				}
				SB.append(formatNumber(PNIB.getPhoneNumberWithNationalPrefix()));
			}
		}
		return SB.toString();
	}
	
	private String formatNumber(String Number)
	{
		//Remove all Spaces, and replace the + with a 00
		Number = Number.replaceAll("\\+", "00");
		Number = Number.replace(" " ,"");
		
		return Number;
	}
	
}
