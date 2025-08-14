package si.module.pdftoolbox.utility;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDMMType1Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class EnumHelper 
{
	public enum Fonts
	{
		DEFAULT(null),
		HELVETICA(PDMMType1Font.HELVETICA),
		HELVETICA_BOLD(PDMMType1Font.HELVETICA_BOLD),
		HELVETICA_BOLD_OBLIQUE(PDMMType1Font.HELVETICA_BOLD_OBLIQUE),
		HELVETICA_OBLIQUE(PDMMType1Font.HELVETICA_OBLIQUE),
		COURIER(PDMMType1Font.COURIER),
		COURIER_BOLD(PDMMType1Font.COURIER_BOLD),
		COURIER_BOLD_OBLIQUE(PDMMType1Font.COURIER_BOLD_OBLIQUE),
		COURIER_OBLIQUE(PDMMType1Font.COURIER_OBLIQUE),
		SYMBOL(PDMMType1Font.SYMBOL),
		TIMES_BOLD(PDMMType1Font.TIMES_BOLD),
		TIMES_BOLD_ITALIC(PDMMType1Font.TIMES_BOLD_ITALIC),
		TIMES_ITALIC(PDMMType1Font.TIMES_ITALIC),
		TIMES_ROMAN(PDMMType1Font.TIMES_ROMAN),
		ZAPF_DINGBATS(PDMMType1Font.ZAPF_DINGBATS),
		;
		
		public final PDType1Font Font;
		Fonts(PDType1Font Font) 
		{
			this.Font=Font;
		}
	}
	
	public enum PageSize
	{
		LEGAL(PDRectangle.LEGAL),
		LETTER(PDRectangle.LETTER),
		A0(PDRectangle.A0),
		A1(PDRectangle.A1),
		A2(PDRectangle.A2),
		A3(PDRectangle.A3),
		A4(PDRectangle.A4),
		A5(PDRectangle.A5),
		A6(PDRectangle.A6),
		
		;
		
		public final PDRectangle Size;
		PageSize(PDRectangle Size) 
		{
			this.Size=Size;
		}
		
	}
	
}
