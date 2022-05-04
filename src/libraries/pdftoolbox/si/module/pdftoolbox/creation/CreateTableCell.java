package si.module.pdftoolbox.creation;

import org.apache.commons.logging.Log;
import org.vandeseer.easytable.settings.BorderStyle;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.cell.TextCell;
import org.vandeseer.easytable.structure.cell.TextCell.TextCellBuilder;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import si.module.pdftoolbox.utility.ColorConverter;
import si.module.pdftoolbox.utility.EnumHelper.Fonts;

@Function(name="[Table] Create Cell for insertion into a Row" ,visibility=Visibility.Public, rookieFunction=false, description="")
public class CreateTableCell implements IBaseExecutable 
{
	//##########################################################################################
		
	@InputVar(label="Content", description="", type=VariableType.STRING)
	public String Content="";
	
	@InputVar(label="Columnspan", description="Allows Spanning over Multiple Columns. To Span use values 2 or greater", type=VariableType.NUMBER)
	public Integer Columnspan=0;
	
	@InputVar(label="Rowspan", description="Allows Spanning over Multiple Rows. To Span use values 2 or greater", type=VariableType.NUMBER)
	public Integer Rowspan=0;
	
	@InputVar(label="Overridesettings", description="Override Table Settings. If set to False, only Content and Spanning is required", type=VariableType.BOOLEAN)
	public boolean Overridesettings=false;
	
	@InputVar(label="Font", description="", type=VariableType.STRING)
	public Fonts F = Fonts.HELVETICA;
	
	@InputVar(label="FontSize", description="", type=VariableType.NUMBER)
	public Integer FontSize=12;
	
	@InputVar(label="FontColor", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String FontColor="128,128,128";
	
	@InputVar(label="DoWordbreak", description="Break Text/Create Multiple Lines if it doesn't fit", type=VariableType.BOOLEAN)
	public boolean DoWordBreak=true;
	
	@InputVar(label="VerticalAlignment", description="", valueByReferenceAllowed = true)
	public VerticalAlignment VerticalAlignmentStyle=VerticalAlignment.MIDDLE;
	
	@InputVar(label="HorizontalAlignment", description="", valueByReferenceAllowed = true)
	public HorizontalAlignment HorizontalAlignmentStyle=HorizontalAlignment.CENTER;
	
	@InputVar(label="BackGroundColor", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String Backgroundcolor="128,128,128";
	
	@InputVar(label="BorderColorTop", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String BorderColorTop="128,128,128";
	
	@InputVar(label="BorderColorBottom", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String BorderColorBottom="128,128,128";
	
	@InputVar(label="BorderColorLeft", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String BorderColorLeft="128,128,128";
	
	@InputVar(label="BorderColorRight", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String BorderColorRight="128,128,128";
		
	@InputVar(label="BorderStyleTop", description="", valueByReferenceAllowed = true)
	public BorderStyle BorderStyleTop=BorderStyle.SOLID;
	
	@InputVar(label="BorderStyleBottom", description="", valueByReferenceAllowed = true)
	public BorderStyle BorderStyleBottom=BorderStyle.SOLID;
	
	@InputVar(label="BorderStyleLeft", description="", valueByReferenceAllowed = true)
	public BorderStyle BorderStyleLeft=BorderStyle.SOLID;
	
	@InputVar(label="BorderStyleRight", description="", valueByReferenceAllowed = true)
	public BorderStyle BorderStyleRight=BorderStyle.SOLID;
	
	@InputVar(label="BorderWidthTop", description="", type=VariableType.NUMBER)
	public Float BorderWidthTop=1f;
	
	@InputVar(label="BorderWidthBottom", description="", type=VariableType.NUMBER)
	public Float BorderWidthBottom=1f;
	
	@InputVar(label="BorderWidthLeft", description="", type=VariableType.NUMBER)
	public Float BorderWidthLeft=1f;
	
	@InputVar(label="BorderWidthRight", description="", type=VariableType.NUMBER)
	public Float BorderWidthRight=1f;
		
	@OutputVar(label="Cell", description="",type=VariableType.OBJECT)
	public Object Cell = null;
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();

		@SuppressWarnings("rawtypes")
		TextCellBuilder TCB = TextCell.builder();
		
		log.debug("Constructing Table Cell");
		
		TCB.text(Content);
		if(Columnspan > 0)
		{
			TCB.colSpan(Columnspan);
		}

		if(Rowspan > 0)
		{
			TCB.rowSpan(Rowspan);
		}
		
		if(Overridesettings)
		{
			TCB.backgroundColor(ColorConverter.ConverttoAWTColor(Backgroundcolor, log));
			TCB.borderColorTop(ColorConverter.ConverttoAWTColor(BorderColorTop, log));
			TCB.borderColorBottom(ColorConverter.ConverttoAWTColor(BorderColorBottom, log));
			TCB.borderColorLeft(ColorConverter.ConverttoAWTColor(BorderColorLeft, log));
			TCB.borderColorRight(ColorConverter.ConverttoAWTColor(BorderColorRight, log));
			TCB.borderStyleTop(BorderStyleTop);
			TCB.borderStyleBottom(BorderStyleBottom);
			TCB.borderStyleLeft(BorderStyleLeft);
			TCB.borderStyleRight(BorderStyleRight);
			TCB.borderWidthTop(BorderWidthTop);
			TCB.borderWidthLeft(BorderWidthLeft);
			TCB.borderWidthLeft(BorderWidthLeft);
			TCB.borderWidthRight(BorderWidthRight);
			TCB.fontSize(FontSize);
			TCB.font(F.Font);
			TCB.textColor(ColorConverter.ConverttoAWTColor(FontColor, log));
			TCB.wordBreak(DoWordBreak);
			TCB.horizontalAlignment(HorizontalAlignmentStyle);
			TCB.verticalAlignment(VerticalAlignmentStyle);
		}		
		
		Cell = TCB.build();
		
		log.debug("Cell Created!");
		
		Success=true;
	}//END OF EXECUTION


	
	
}
