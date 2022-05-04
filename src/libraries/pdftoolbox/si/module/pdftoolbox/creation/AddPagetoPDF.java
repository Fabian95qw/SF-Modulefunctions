package si.module.pdftoolbox.creation;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.pdmodel.PDPage;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.pdftoolbox.pdfobject.PDFObject;

@Function(name="[PDF] Add Page to PDF",visibility=Visibility.Public, rookieFunction=false, description="")
public class AddPagetoPDF implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="PDF", description="",type=VariableType.OBJECT)
	public Object PDFO = null;
	
	@InputVar(label="Page", description="Created Page from: [PDF] Create new Page",type=VariableType.OBJECT)
	public Object PO = null;
			
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();

		if (!(PDFO instanceof PDFObject))
		{
			log.error("PDFObject is invalid!");
			return;
		}
		
		if (!(PO instanceof PDPage))
		{
			log.error("Page is invalid!");
			return;
		}

		PDFObject PDF = (PDFObject) PDFO;
		PDPage Page = (PDPage) PO;
		
		PDF.addPage(Page);


		Success=true;
	}//END OF EXECUTION


	
	
}
