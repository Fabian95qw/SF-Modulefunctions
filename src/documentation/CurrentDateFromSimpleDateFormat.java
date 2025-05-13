package si.module.documentation.collection;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(visibility=Visibility.Private, description="")
public class CurrentDateFromSimpleDateFormat implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="Format", description="",type=VariableType.STRING)
	public String Format="";
		    	
	@OutputVar(label="DateFormatted", description="",type=VariableType.STRING)
	public String DateFormatted="";
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		SimpleDateFormat SDF = new SimpleDateFormat(Format);
		DateFormatted = SDF.format(new Date());
		
	}//END OF EXECUTION


	
	
}
