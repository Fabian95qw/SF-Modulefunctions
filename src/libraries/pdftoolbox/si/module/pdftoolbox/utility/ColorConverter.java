package si.module.pdftoolbox.utility;

import org.apache.commons.logging.Log;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import java.awt.Color;

public class ColorConverter 
{
	public static PDColor ConverttoPDColor(String ColorCode, Log log)
	{
		String[] Pieces = ColorCode.split(",");
		if(Pieces.length < 3)
		{
			log.error("ColorCode: "+ ColorCode +" is invalid!");
			//        Appearance.setBorderColour(new PDColor(new float[]{0.5f,1,0.5f}, PDDeviceRGB.INSTANCE));
			return new PDColor(new float[]{0.5f,0.5f, 0.5f}, PDDeviceRGB.INSTANCE);
		}
		
		Float R=0.5f;
		Float G=0.5f;
		Float B=0.5f;
		try
		{
			Integer I = Integer.valueOf(Pieces[0]);
			R = I / 255f;
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		try
		{
			Integer I = Integer.valueOf(Pieces[1]);
			G = I / 255f;
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		try
		{
			Integer I = Integer.valueOf(Pieces[2]);
			B = I / 255f;
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return new PDColor(new float[]{R,G,B}, PDDeviceRGB.INSTANCE);
	}
	
	public static Color ConverttoAWTColor(String ColorCode, Log log)
	{
		String[] Pieces = ColorCode.split(",");
		if(Pieces.length < 3)
		{
			log.error("ColorCode: "+ ColorCode +" is invalid!");
			//        Appearance.setBorderColour(new PDColor(new float[]{0.5f,1,0.5f}, PDDeviceRGB.INSTANCE));
			return Color.GRAY;
		}
		
		Integer R=128;
		Integer G=128;
		Integer B=128;
		try
		{
			R = Integer.valueOf(Pieces[0]);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		try
		{
			G = Integer.valueOf(Pieces[1]);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		try
		{
			B = Integer.valueOf(Pieces[2]);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return new Color(R,G,B);
	}
}
