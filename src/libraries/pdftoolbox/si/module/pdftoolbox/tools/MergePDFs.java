package si.module.pdftoolbox.tools;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(name="[PDF] Merge multiple PDF Files into one",visibility=Visibility.Public, rookieFunction=false, description="")
public class MergePDFs implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="PDF-Files", description="Map<Order, Path/to/PDF/File.pdf> Merges the PDF's in the Set order For example: [{1, /tmp/page1.pdf}{2, /tmp/page2.pdf}] ",type=VariableType.STRING)
	public Map<Integer, String> Pages = new HashMap<Integer, String>();

	@InputVar(label="Targetfile", description="The export location of the merged pdf",type=VariableType.STRING)
	public String Targetfile="/tmp/merged.pdf";
	
	@OutputVar(label="Success", description="True if Targetfile was sucessfully written.",type=VariableType.BOOLEAN)
	public boolean Success = false;
	
	@OutputVar(label="Errormessage", description="The Errormessage, if Success is false",type=VariableType.STRING)
	public String Errormessage="";
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		
		PDFMergerUtility Merger = new PDFMergerUtility();
		Merger.setDestinationFileName(Targetfile);
		
		Integer Counter = 1;
		Integer Size = Pages.size();
		
		while(Counter <= Size)
		{
			String SourceFile = Pages.get(Counter);
			if(SourceFile == null)
			{
				Errormessage="No Page found for Number: " + Counter;
				log.error(Errormessage);
				return;
			}
			
			File Source = new File(SourceFile);
			if(!Source.exists())
			{
				Errormessage = "Source file "+Targetfile+" does not exist!";
				log.error(Errormessage);
				return;
			}
			Merger.addSource(Source);
			Counter++;
		}
		
		log.debug("Trying to merge all PDFs...");
		try
		{
			Merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
		}
		catch(Exception e)
		{
			Errormessage = "Error while saving PDF:" + e.getMessage();
			log.error(Errormessage);
			return;
		}
		
		
		
	}//END OF EXECUTION


	
	
}
