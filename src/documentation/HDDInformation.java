package si.module.documentation.collection;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.module.core.runtime.functions.lang.string.StripEnd;
import de.vertico.starface.module.core.runtime.functions.system.Execute4;

@Function(visibility=Visibility.Private, description="")
public class HDDInformation implements IBaseExecutable 
{
	//##########################################################################################
		 
	@OutputVar(label="Total HDD[MB]", description="",type=VariableType.NUMBER)
	public Integer HDDTotalMB=0;
	
	@OutputVar(label="HDD Usage[MB]", description="",type=VariableType.NUMBER)
	public Integer HDDUsageMB=0;
	
	@OutputVar(label="HDD Free[MB]", description="",type=VariableType.NUMBER)
	public Integer HDDFreeMB=0;
	
	@OutputVar(label="HDDChart", description="",type=VariableType.STRING)
	public String HDDChart="";
			
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		HDDTotalMB = ExecuteHDDCommand("df -BKB | grep /dev/mapper/starface-root | awk ' NR == 1 { print $2}'", context);
		HDDUsageMB = ExecuteHDDCommand("df -BKB | grep /dev/mapper/starface-root | awk ' NR == 1 { print $3}'", context);
		HDDFreeMB =  ExecuteHDDCommand("df -BKB | grep /dev/mapper/starface-root | awk ' NR == 1 { print $4}'", context);
						
		HDDChart = CreateHDDChart(HDDTotalMB, HDDUsageMB, HDDFreeMB, log);
		
	}//END OF EXECUTION

	private String CreateHDDChart(Integer HDDTotalMB, Integer HDDUsageMB, Integer HDDFreeMB, Logger log) 
	{
		DefaultPieDataset DS = new DefaultPieDataset();
		DS.setValue("Genutzter Speicherplatz", HDDUsageMB.doubleValue());
		DS.setValue("Freier Speicherplatz", HDDFreeMB.doubleValue());
		
		JFreeChart PieChart = ChartFactory.createPieChart("Speicherplatzverbrauch", DS, false, true, false);				
				
		String ChartFile = "/tmp/hddchart.png";
		File F = new File(ChartFile);
		
		if(F.exists())
		{
			F.delete();
		}
		
		BufferedImage BI = PieChart.createBufferedImage(1920, 1080);
		try 
		{
			ImageIO.write(BI, "png", F);
		}
		catch (IOException e) 
		{
			EtoStringLog(log, e);
		}
		
		return ChartFile;
	}

	public Integer ExecuteHDDCommand(String Command, IRuntimeEnvironment context) throws Exception
	{
		Execute4 E = new Execute4();
		E.command=Command;
		E.execute(context);
		StripEnd SE = new StripEnd();
		SE.text = E.output;
		SE.stripChars="kB";
		
		try
		{
			return Integer.valueOf(SE.text);
		}
		catch(Exception e)
		{
			EtoStringLog(context.getLog(), e);
		}
		return -1;
	}
	
	public static void EtoStringLog(Logger log, Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.debug(sw.toString()); //
	}
	
}
