package nucom.module.msgraphs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import nucom.module.msgraphs.o365provider.O365Provider;

@Function(visibility=Visibility.Public, rookieFunction=false, description="")
public class CreateO365Provider implements IBaseExecutable 
{
	//##########################################################################################
			    		
	@InputVar(label="TenantID", description="Microsoft Azure Tenant ID",type=VariableType.STRING)
	public String TenantID="";
	
	@InputVar(label="ClientID", description="Microsoft Azure APP ID",type=VariableType.STRING)
	public String ClientID="";
	
	@InputVar(label="ClientSecret", description="Microsoft Azure APP Secret",type=VariableType.STRING)
	public String ClientSecret="";
	
	@InputVar(label="Scopes", description="List of Ms Graph Scopes",type=VariableType.LIST)
	public List<String> Scopes= new ArrayList<String>();
	
	@OutputVar(label="O365Provider", description="Return an Office365 Provider",type=VariableType.OBJECT)
	public Object O365Provider = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		log.debug("Creating Provider...");
		
		O365Provider Provider = new O365Provider(TenantID, ClientID, ClientSecret, Scopes, context.getLog());
		O365Provider = Provider;
		log.debug("Provider Sucessfully created!");
		
	}//END OF EXECUTION

	
}
