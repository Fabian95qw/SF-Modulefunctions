package si.module.pdftoolbox.creation;

import org.apache.commons.logging.Log;

import org.vandeseer.easytable.settings.BorderStyle;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.settings.HorizontalAlignment;
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

@Function(name="[Table] Create Table",visibility=Visibility.Public, rookieFunction=false, description="")
public class CreateTable implements IBaseExecutable 
{
	//##########################################################################################
	

	@InputVar(label="TableWidth", description="", type=VariableType.NUMBER)
	public Float TableWidth=600f;
	
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
	
	@InputVar(label="BorderColor", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String BorderColor="128,128,128";
		
	@InputVar(label="BorderStyle", description="", valueByReferenceAllowed = true)
	public BorderStyle TableBorderStyle=BorderStyle.SOLID;
	
	@InputVar(label="BorderWidth", description="", type=VariableType.NUMBER)
	public Float BorderWidth=1f;
	
	@InputVar(label="Padding", description="", type=VariableType.NUMBER)
	public Float Padding=1f;

	@OutputVar(label="Table", description="Represents an empty Table. Add Columns using [Table] Add Column to Table", type=VariableType.OBJECT)
	public Object Table = null;
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();

		log.debug("Creating Table...");
		TableBuilder TB = org.vandeseer.easytable.structure.Table.builder();
		
		TB.backgroundColor(ColorConverter.ConverttoAWTColor(Backgroundcolor, log));
		TB.borderColor(ColorConverter.ConverttoAWTColor(BorderColor, log));
		TB.borderStyle(TableBorderStyle);
		TB.borderWidth(BorderWidth);
		TB.width(TableWidth);
		TB.fontSize(FontSize);
		TB.font(F.Font);
		TB.textColor(ColorConverter.ConverttoAWTColor(FontColor, log));
		TB.wordBreak(DoWordBreak);
		TB.horizontalAlignment(HorizontalAlignmentStyle);
		TB.verticalAlignment(VerticalAlignmentStyle);
		TB.padding(Padding);

		log.debug("Empty Table Created!");
		
		Table = TB;
		
		Success=true;
	}//END OF EXECUTION


	
	
}
