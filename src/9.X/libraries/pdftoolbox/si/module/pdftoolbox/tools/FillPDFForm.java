package si.module.pdftoolbox.tools;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;

@Function(name="[PDF] Fill out a existing PDF Form", visibility=Visibility.Public, rookieFunction=false, description="Fill out a PDF Form, and save it as a new file")
public class FillPDFForm implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="Template", description="The Sourcefile containing a PDF Form",type=VariableType.STRING)
	public String Templatefile="";
	
	@InputVar(label="Targetfile", description="The export location of the filled out pdf",type=VariableType.STRING)
	public String Targetfile="/tmp/export.pdf";
	
	@InputVar(label="Mapping", description="Map<Fieldname, Content> Sets the content of a Field Based on the Fieldname. For example: {'Textbox1', '7.2.0.1'} sets the value of Textbox1 in the PDF to 7.2.0.1",type=VariableType.MAP)
	public Map<String, String> Mapping = new HashMap<String, String>();
	
	@InputVar(label="Replacement", description="Map<Searchstring, Replacementstring>. Replaces the content of Fields Based on the Searchstring. For example: {'%SFVersion%'. '7.2.0.1'} checks every field in the pdf for %SFVersion% and replaces every instance of it with 7.2.0.1",type=VariableType.MAP)
	public Map<String, String> Replacement = new HashMap<String, String>();
	
	@InputVar(label="SetReadOnly", description="Sets edited Fields to readonly, so they can't be edited by hand later on",type=VariableType.BOOLEAN)
	public Boolean SetReadOnly = true;
			    	
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
		
		log.debug("Loading Source:" + Templatefile);
		
		File Template = new File(Templatefile);
		if(!Template.exists())
		{
			Errormessage = "Source file "+Templatefile+" does not exist!";
			log.error(Errormessage);
			return;
		}
		
		PDDocument Document = null;
		
		try
		{
			Document = PDDocument.load(Template);
		}
		catch (IOException e) 
		{
			Errormessage ="Error while loading PDF: " + e.getMessage();
			log.error(Errormessage);
			return;
		}
		
		if(Document.getDocumentCatalog() == null || Document.getDocumentCatalog().getAcroForm() == null)
		{
			Errormessage ="This PDF does not contain a valid form!";
			log.error(Errormessage);
			return;
		}
		
		log.debug("Loading Document finished. Filling Fields..");
		
		for(PDField Field : Document.getDocumentCatalog().getAcroForm().getFields())
		{
			log.debug(Field.getPartialName() +" " + Field.getValueAsString());
			if(Mapping.get(Field.getPartialName()) != null)
			{
				String Value = Mapping.get(Field.getPartialName());
				log.debug("Updating Field: "+ Field.getPartialName() +" with new value: ");
				try
				{
					Field.setValue(Value);
				}
				catch(IOException e)
				{
					Errormessage ="Error while setting value for field: " + Field.getPartialName()+" | " + e.getMessage();
					log.error(e);
				}
				if(SetReadOnly)
				{
					Field.setReadOnly(true);
				}
			}
			
			log.debug("Scanning for Replacement Strings...");
			
			for(Entry<String, String> Entry : Replacement.entrySet())
			{
				log.debug(Field.getValueAsString() +" <==> " + Entry.getKey());
				if(Field.getValueAsString().contains(Entry.getKey()))
				{
					log.debug("Matched! Replacing: "+ Entry.getKey() +" with: " + Entry.getValue());
					Field.setValue(Field.getValueAsString().replace(Entry.getKey(), Entry.getValue()));
					if(SetReadOnly)
					{
						Field.setReadOnly(true);
					}
				}
			}
		}
		log.debug("Filling Fields completed. Trying to save PDF to: "+ Targetfile);
		
		File Target = new File(Targetfile);
		try
		{
			Document.save(Target);
		}
		catch(IOException e)
		{
			Errormessage = "Error while saving PDF:" + e.getMessage();
			log.error(Errormessage);
			return;
		}
		
		Success = true;
		
	}//END OF EXECUTION


	
	
}
