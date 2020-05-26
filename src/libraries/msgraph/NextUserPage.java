package nucom.module.msgraphs;

import org.apache.commons.logging.Log;

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

@Function(visibility=Visibility.Public, rookieFunction=false, description="Returns next Page of Users")
public class NextUserPage implements IBaseExecutable 
{
	//##########################################################################################

	@InputVar(label="PreviousUserPage", description="Previus UserPage Object",type=VariableType.OBJECT)
	public Object PreviousPage = null;
	
	@OutputVar(label="NextUserPage", description="Next Userpage",type=VariableType.OBJECT)
	public Object NextUserPage = null;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		
		IUserCollectionPage UserPage = null;
		
		try
		{
			UserPage = (IUserCollectionPage) PreviousPage;
		}
		catch(Exception e)
		{
			log.debug("Previous Page is not valid!");
			LogHelper.EtoStringLog(log, e);
			return;
		}
		
		log.debug("Finding next Page..");
		try
		{
			UserPage = (IUserCollectionPage) PreviousPage;
			NextUserPage = UserPage.getNextPage().buildRequest().get();
		}
		catch(Exception e)
		{
			log.debug("Acessing Users failed!");
			LogHelper.EtoStringLog(log, e);
			return;
		}		
		
	}//END OF EXECUTION

	
}
