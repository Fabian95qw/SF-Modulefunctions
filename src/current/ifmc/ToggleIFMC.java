package si.module.examples.ifmc;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;


import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.persistence.connector.SipAndPhonesHandler;
import de.vertico.starface.persistence.databean.config.phone.FMCBean;

@Function(visibility=Visibility.Private, description="")
public class ToggleIFMC implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="STARFACE_ACCOUNT", description="",type=VariableType.STARFACE_ACCOUNT)
	public Integer STARFACE_ACCOUNT=-1;
		
	@InputVar(label="Enable", description="",type=VariableType.BOOLEAN)
	public Boolean Enable=false;
	
	@InputVar(label="FilterList", description="", type=VariableType.LIST)
	public Object FilterListRaw= null; //This is to Circumvent a bug where Lists turn into a List<List<String>> instead of a List<String>
	
	@InputVar(label="isBlacklist", description="", type=VariableType.BOOLEAN)
	public boolean isBlacklist=true;

     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();

		SipAndPhonesHandler SIPH = (SipAndPhonesHandler)context.springApplicationContext().getBean((SipAndPhonesHandler.class));
		List<String> FilterList = null; 
		try
		{
			FilterList = (List<String>) FilterListRaw; //Cast the List
		}
		catch(Exception e)
		{
			List<List<String>> TempList = (List<List<String>>) FilterListRaw; //If the Casting Fails, try the Bugfix cast
			FilterList = TempList.get(0);
		}
		
		FilterList=GlobListtoRegexList(FilterList);
		
		if(STARFACE_ACCOUNT == -1) 
		{
			log.debug("Invalid Starface Account Provided: -1");
			return;
		}
		
		List<FMCBean> IFMCBeans = SIPH.getFMCBeansForAccount(STARFACE_ACCOUNT); //Fetch all IFMC Configurations for a User
		
		List<FMCBean> UpdatedBeans = new ArrayList<FMCBean>();
		
		
		Outerloop : for(FMCBean FMCB : IFMCBeans)
		{
			
			boolean Found=false;			
			Innerloop: for(String Filter : FilterList) //Check if this IFMC Bean matches one of the provided from the List
			{				
				String Number = FMCB.getExternnumber().replace(" ", "");
				boolean Matches = Number.matches(Filter);
				log.debug(Filter+" <==> " + Number +" <==> " + Matches);
				if(Matches && isBlacklist)  //If it Matches, but the mode is set to blacklist
				{
					log.debug("Filter Matched, Skipping: " + FMCB.getExternnumber());
					continue Outerloop;
				}
				if(Matches && !isBlacklist) //If it Matches, and the Mode is set to whitelist
				{
					log.debug("Filter Matched, Proceeding...");
					Found = true;
					break Innerloop;
				}
			}
			if(!isBlacklist && !Found)
			{
				log.debug(FMCB.getExternnumber() +" is not on Whitelist. Skipping...");
				continue;
			}
			
			FMCB.setActive(Enable); //Toggle Based on Input
			UpdatedBeans.add(FMCB); //Add to the List of Beans to update
		}
		
		SIPH.updateFMCBeansForAccount(STARFACE_ACCOUNT, UpdatedBeans, true); //Update the Beans
		
		
	}//END OF EXECUTION

	private List<String> GlobListtoRegexList(List<String> FilterRAW)
	{
		List<String> Filter = new ArrayList<String>();
	
		for(String S : FilterRAW)
		{
			S=GlobtoRegex(S);
			Filter.add(S);
		}
		
		
		return Filter;
	}
	
	//Thanks to https://stackoverflow.com/questions/45321050/java-string-matching-with-wildcards
	private String GlobtoRegex(String S) 
	{
	    StringBuilder SB = new StringBuilder("^");
	    for(int Counter = 0; Counter < S.length(); ++Counter) 
	    {
	        final char C = S.charAt(Counter);
	        switch(C) 
	        {
	            case '*': 
	            	SB.append(".*"); 
	            	break;
	            case '?': 
	            	SB.append('.'); 
	            	break;
	            case '+':
	            	SB.append("\\+");
	            	break;
	            default: 
	            	SB.append(C);
	            	break;
	        }
	    }
	    SB.append('$');
	    return SB.toString();
	}
	
}
