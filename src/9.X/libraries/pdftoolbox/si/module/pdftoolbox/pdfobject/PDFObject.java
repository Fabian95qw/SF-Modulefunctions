package si.module.pdftoolbox.pdfobject;

import java.io.IOException;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;

public class PDFObject 
{
	private PDDocument Document = null;
	private PDAcroForm Form = null;
	private PDResources Resources = null;
	
	public PDFObject(String DefaultAppearanceSetting, COSName DefaultFontIdentifier, PDType1Font DefaultFont)
	{
		Document = new PDDocument();
		Form = new PDAcroForm(Document);
		Resources = new PDResources();
		Resources.put(DefaultFontIdentifier, DefaultFont); 
		Document.getDocumentCatalog().setAcroForm(Form);
		Form.setDefaultResources(Resources);

		Form.setDefaultAppearance(DefaultAppearanceSetting);
	}
	
	public PDFObject(PDDocument Document, String DefaultAppearanceSetting, COSName DefaultFontIdentifier, PDType1Font DefaultFont) 
	{
		this.Document = Document;
		if(Document.getDocumentCatalog() != null && Document.getDocumentCatalog().getAcroForm() != null)
		{
			this.Form = Document.getDocumentCatalog().getAcroForm();
		}
		else
		{
			Form = new PDAcroForm(Document);
			Document.getDocumentCatalog().setAcroForm(Form);
		}
		
		if(Form.getDefaultResources() == null)
		{
			Resources = new PDResources();
			Resources.put(DefaultFontIdentifier, DefaultFont); 
			Form.setDefaultResources(Resources);
		}
		
		if(Form.getDefaultAppearance() == null)
		{
			Form.setDefaultAppearance(DefaultAppearanceSetting);
		}
	}

	public PDDocument getDocument()
	{
		return Document;
	}
	
	public PDResources getResources() {
		return Resources;
	}

	public PDAcroForm getForm() {
		return Form;
	}
	
	public void addPage(PDPage Page) 
	{
		Document.addPage(Page);
	}

	public void addFont(COSName Identifier, PDType1Font Font) 
	{
		Resources.put(Identifier, Font);
	}
	
	public void save(String Targetpath) throws IOException 
	{
		Document.save(Targetpath);
	}
	
}
