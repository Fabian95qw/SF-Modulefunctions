package nucom.module.tts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.apache.commons.logging.Log;

import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import nucom.module.tts.utility.EnumHelper.AudioCodec;
import nucom.module.tts.utility.LogHelper;

@Function(visibility=Visibility.Public, rookieFunction=false, description="")
public class TTS_ASTERISK_SOUND_FILE_STRING_INPUT implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="APIKEY", description="http://www.voicerss.org API-KEY",type=VariableType.STRING)
	public String APIKEY="";
	
	@InputVar(label="Text", description="Text to convert",type=VariableType.STRING)
	public String Text="";
		    	
	@InputVar(label="Language", description="Language", valueByReferenceAllowed=true)
	public String Language="";
		
	@InputVar(label="Voice", description="Voice", valueByReferenceAllowed=true)
	public String Voice="";
	
	@InputVar(label="Rate", description="Rate between -10 and 10 (0 == default)",type=VariableType.NUMBER)
	public Integer Rate=0;
	
	@InputVar(label="AudioFormat", description="Audio format", valueByReferenceAllowed=true)
	public String AudioFormat="";
	
	@InputVar(label="Targetpath", description="IMPORTANT! PATH HAS TO BE ACCESSIBLE BY ASTERISK, OR IT WONT WORK IN PLAYBACKRESOURCEFILE",type=VariableType.STRING)
	public String Targetpath="/tmp/";
	
	@OutputVar(label="Success", description="Tells if TTS was a success",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
	@OutputVar(label="ASTERISK_SOUND_FILE", description="Returns the ASTERISK_SOUNDFILE.",type=VariableType.ASTERISK_SOUND_FILE)
	public String ASTERISK_SOUND_FILE="";
	
	
	
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();

		if(!Targetpath.endsWith("/")) {Targetpath=Targetpath+"/";};
		
		VoiceProvider VP  = new VoiceProvider(APIKEY);
		VoiceParameters Params = new VoiceParameters(Text, Language);
		Params.setVoice(Voice);
		Params.setFormat(AudioFormat);
		Params.setCodec(AudioCodec.WAV.toString());
		
		Params.setBase64(false);
		Params.setSSML(false);
		
		if(Rate < -10 || Rate > 10)
		{
			Rate = 0;
		}
		Params.setRate(Rate);
		
		try
		{
			log.debug("Doing TTS...");
			byte[] Audio =  VP.speech(Params);
			File SourceFile= new File("/tmp/"+UUID.randomUUID().toString()+AudioCodec.WAV.toString().toLowerCase());
			log.debug("Writing to:" + SourceFile.getAbsolutePath());
			
			FileOutputStream FOS = new FileOutputStream(SourceFile);
			FOS.write(Audio, 0, Audio.length);
			FOS.flush();
			FOS.close();
			
			log.debug("Converting to SLN16");
			
			File TargetFile = new File(Targetpath+UUID.randomUUID().toString()+".sln16");
			
			Convert(SourceFile, TargetFile, log);
			
			log.debug("Done Converting");
			
			ASTERISK_SOUND_FILE = TargetFile.getAbsolutePath();
			Success=true;
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
	}//END OF EXECUTION

	public static void Convert (File SourceFile, File Targetfile, Log log) throws IOException, InterruptedException
	{
		String ConvertedCommand = "/usr/bin/sox -V1 " + SourceFile.getAbsolutePath() + " -t raw -r 16000 -c 1 -s -b 16 " + Targetfile.getAbsolutePath();
		
		Process Terminal = null;
		InputStream ReadStream = null;
		try 
		{
			Terminal = Runtime.getRuntime().exec(ConvertedCommand);
			Terminal.waitFor();
			ReadStream =  Terminal.getInputStream();
		} 
		catch (IOException e) 
		{
			log.debug("[Converter]Converting Failed! Couldn't execute Command");
			throw(e);
		} 
		catch (InterruptedException e) 
		{
			log.debug("[Converter]Waiting got Interrupted!");
			throw(e);
		}
		
		if(Terminal.exitValue()!=0)
		{
			log.debug("[Converter]Converter Failed!");
			log.debug("[Converter]Command:" + ConvertedCommand + " failed!");
			BufferedReader BR = new BufferedReader (new InputStreamReader(ReadStream));
			
			String line = "";
			
			try
			{
				while((line = BR.readLine()) != null)
				{
						log.debug("[Converter]"+line);
				}
			}
			catch(IOException e)
			{
				LogHelper.EtoStringLog(log, e);
			}
			
			try{BR.close();}catch(Exception e){}
			return;
		}
		
		if(Terminal.exitValue()==0)
		{
			BufferedReader BR = new BufferedReader (new InputStreamReader(ReadStream));
			
			String line = "";
			
			try
			{
				while((line = BR.readLine()) != null)
				{
						log.debug("[Converter]"+line);
				}
			}
			catch(IOException e)
			{
				throw(e);
			}
			
			try{BR.close();}catch(Exception e){}
		}
	}
	
	
	
}
