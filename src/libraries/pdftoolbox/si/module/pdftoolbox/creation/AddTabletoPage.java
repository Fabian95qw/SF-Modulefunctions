package si.module.pdftoolbox.creation;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Table.TableBuilder;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.pdftoolbox.pdfobject.PDFObject;


@Function(name="[PDF] Add finished Table to Page",visibility=Visibility.Public, rookieFunction=false, description="")
public class AddTabletoPage implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="PDF", description="The PDF to add the Table to",type=VariableType.OBJECT)
	public Object PDFO = null;
	
	@InputVar(label="Page", description="The specific Page to add the Table to",type=VariableType.OBJECT)
	public Object PO = null;
	
	@InputVar(label="Table", description="The finished Table after inserting Columns and Rows", type=VariableType.OBJECT)
	public Object TO = null;

	@InputVar(label="Offset X", description="",type=VariableType.NUMBER)
	public Float OffsetX = 0f;
	
	@InputVar(label="Offset Y", description="",type=VariableType.NUMBER)
	public Float OffsetY = 0f;
		
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
		
		if (!(TO instanceof TableBuilder))
		{
			log.error("Table is invalid!");
			return;
		}
		
		PDFObject PDF = (PDFObject) PDFO;
		PDPage Page = (PDPage) PO;
		TableBuilder TB = (TableBuilder) TO;
		
		PDPageContentStream ContentStream = new PDPageContentStream(PDF.getDocument(), Page,AppendMode.APPEND, false);
		
		TableDrawer TD = TableDrawer.builder().contentStream(ContentStream)
				.startX(OffsetX)
				.startY(OffsetY)
				.table(TB.build())
				.build();
		TD.draw();
		
		ContentStream.close();

		
		Success=true;
	}//END OF EXECUTION


	
	
}
