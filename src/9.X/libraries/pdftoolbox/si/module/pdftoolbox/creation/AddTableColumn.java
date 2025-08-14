package si.module.pdftoolbox.creation;

import org.apache.commons.logging.Log;
import org.vandeseer.easytable.settings.BorderStyle;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Column;
import org.vandeseer.easytable.structure.Column.ColumnBuilder;
import org.vandeseer.easytable.structure.Table.TableBuilder;

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

@Function(name="[Table] Add Column to Table" ,visibility=Visibility.Public, rookieFunction=false, description="")
public class AddTableColumn implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="Table", description="", type=VariableType.OBJECT)
	public Object TO = null;
	
	@InputVar(label="ColumnWidth", description="", type=VariableType.NUMBER)
	public Float ColumnWidth=50f;
	
	@InputVar(label="Overridesettings", description="Override Table Settings. If set to False, only ColumnWidth is required", type=VariableType.BOOLEAN)
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
	
	@InputVar(label="BorderColorLeft", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String BorderColorLeft="128,128,128";
	
	@InputVar(label="BorderColorRight", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String BorderColorRight="128,128,128";
		
	@InputVar(label="BorderStyleLeft", description="", valueByReferenceAllowed = true)
	public BorderStyle BorderStyleLeft=BorderStyle.SOLID;
	
	@InputVar(label="BorderStyleRight", description="", valueByReferenceAllowed = true)
	public BorderStyle BorderStyleRight=BorderStyle.SOLID;
	
	@InputVar(label="BorderWidthLeft", description="", type=VariableType.NUMBER)
	public Float BorderWidthLeft=1f;
	
	@InputVar(label="BorderWidthRight", description="", type=VariableType.NUMBER)
	public Float BorderWidthRight=1f;
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();

		if (!(TO instanceof TableBuilder))
		{
			log.error("TableObject is invalid!");
			return;
		}
		TableBuilder TB = (TableBuilder) TO;
		
		log.debug("Creating Column...");
		ColumnBuilder CB = Column.builder();
		
		CB.width(ColumnWidth);
		if(Overridesettings)
		{
			CB.backgroundColor(ColorConverter.ConverttoAWTColor(Backgroundcolor, log));
			CB.borderColorLeft(ColorConverter.ConverttoAWTColor(BorderColorLeft, log));
			CB.borderColorRight(ColorConverter.ConverttoAWTColor(BorderColorRight, log));
			CB.borderStyleLeft(BorderStyleLeft);
			CB.borderStyleRight(BorderStyleRight);
			CB.borderWidthLeft(BorderWidthLeft);
			CB.borderWidthRight(BorderWidthRight);
			CB.fontSize(FontSize);
			CB.font(F.Font);
			CB.textColor(ColorConverter.ConverttoAWTColor(FontColor, log));
			CB.wordBreak(DoWordBreak);
			CB.horizontalAlignment(HorizontalAlignmentStyle);
			CB.verticalAlignment(VerticalAlignmentStyle);
		}		
		TB.addColumn(CB.build());
		log.debug("Column Created!");
		
		Success=true;
	}//END OF EXECUTION


	
	
}
