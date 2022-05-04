package si.module.pdftoolbox.creation;

import java.util.List;

import org.apache.commons.logging.Log;
import org.vandeseer.easytable.settings.BorderStyle;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Row.RowBuilder;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

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

@Function(name="[Table] Insert TableRow into Table" ,visibility=Visibility.Public, rookieFunction=false, description="")
public class AddTableRow implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="Table", description="The Table. Needs to be filled with Columns from: [Table] Add Column to Table", type=VariableType.OBJECT)
	public Object TO = null;
	
	@InputVar(label="Rowdata", description="Requires List<Cell>, Cells can be created by using [Table] Create Cell.", type=VariableType.OBJECT)
	public Object Rowdata = null;
		
	@InputVar(label="Overridesettings", description="Override Table Settings. If set to False, only RowData and TableObject is required", type=VariableType.BOOLEAN)
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
	
	@InputVar(label="Backgroundcolor", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String Backgroundcolor="128,128,128";
	
	@InputVar(label="BorderColor", description="The Color RGB[0-255] Example: 255,255,255", type=VariableType.STRING)
	public String Bordercolor="128,128,128";
	
	@InputVar(label="BorderStye", description="", valueByReferenceAllowed = true)
	public BorderStyle RowBorderStye=BorderStyle.SOLID;

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
		
		log.debug("Creating Row...");
		
		RowBuilder RB = Row.builder();
				
		if(Rowdata instanceof List)
		{
			@SuppressWarnings("unchecked")
			List<Object> OList = (List<Object>) Rowdata;
			for(Object O : OList)
			{
				if(!(O instanceof TextCell))
				{
						log.error("RowData has invalid Objects in list!");
						log.debug(O.getClass().toGenericString());
						return;
				}
				TextCell Cell = (TextCell) O;
				RB.add(Cell);
			}
		}
		else
		{
			log.error("RowData has invalid Objects in list!");
			return;
		}
		
		
		if(Overridesettings)
		{
			RB.backgroundColor(ColorConverter.ConverttoAWTColor(Backgroundcolor, log));
			RB.borderColor(ColorConverter.ConverttoAWTColor(Bordercolor, log));
			RB.borderStyle(RowBorderStye);
			RB.fontSize(FontSize);
			RB.font(F.Font);
			RB.textColor(ColorConverter.ConverttoAWTColor(FontColor, log));
			RB.wordBreak(DoWordBreak);
			RB.horizontalAlignment(HorizontalAlignmentStyle);
			RB.verticalAlignment(VerticalAlignmentStyle);
		}		
		
		TB.addRow(RB.build());
		
		log.debug("Row Added!");
		
		Success=true;
	}//END OF EXECUTION


	
	
}
