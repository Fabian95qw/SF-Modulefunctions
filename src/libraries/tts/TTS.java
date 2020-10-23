package nucom.module.tts;

import java.io.File;
import java.io.FileOutputStream;
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
import nucom.module.tts.utility.EnumHelper.AudioFormat;
import nucom.module.tts.utility.EnumHelper.Language;
import nucom.module.tts.utility.EnumHelper.Voice;
import nucom.module.tts.utility.LogHelper;

@Function(visibility=Visibility.Public, rookieFunction=false, description="")
public class TTS implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="APIKEY", description="http://www.voicerss.org API-KEY",type=VariableType.STRING)
	public String APIKEY="";
	
	@InputVar(label="Text", description="Text to convert",type=VariableType.STRING)
	public String Text="";
		    	
	@InputVar(label="Language", description="Language", valueByReferenceAllowed=true)
	public Language language = Language.German_Germany;
	
	@InputVar(label="Voice", description="Voice", valueByReferenceAllowed=true)
	public Voice voice = Voice.German_Germany_Hanna;
	
	@InputVar(label="Rate", description="Rate between -10 and 10 (0 == default)",type=VariableType.NUMBER)
	public Integer Rate=0;
	
	@InputVar(label="AudioCodec", description="Output format", valueByReferenceAllowed=true)
	public AudioCodec audiocodec = AudioCodec.WAV;
	
	@InputVar(label="AudioFormat", description="Audio format", valueByReferenceAllowed=true)
	public AudioFormat audioformat = AudioFormat.AF_48khz_16bit_stereo;
	
	@InputVar(label="Targetpath", description="",type=VariableType.STRING)
	public String Targetpath="/tmp/";
	
	@OutputVar(label="Success", description="Tells if TTS was a success",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
	@OutputVar(label="Resultfile", description="Returns the Full path to the generated file",type=VariableType.STRING)
	public String Resultfile="";
		
    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();

		if(!Targetpath.endsWith("/")) {Targetpath=Targetpath+"/";};
				
		VoiceProvider VP  = new VoiceProvider(APIKEY);
		VoiceParameters Params = new VoiceParameters(Text, language.toString());
		Params.setVoice(voice.toString());		
		Params.setFormat(audioformat.toString());
		Params.setCodec(audiocodec.toString());
		Params.setBase64(false);
		Params.setSSML(false);
		if(Rate < -10 || Rate > 10)
		{
			Rate = 0;
		}
		Params.setRate(0);
		
		
		try
		{
			log.debug("Doing TTS...");
			byte[] Audio =  VP.speech(Params);
			File F= new File(Targetpath+UUID.randomUUID().toString()+audiocodec.toString().toLowerCase());
			log.debug("Writing to:" + F.getAbsolutePath());
			
			FileOutputStream FOS = new FileOutputStream(F);
			FOS.write(Audio, 0, Audio.length);
			FOS.flush();
			FOS.close();
			Resultfile = F.getAbsolutePath();
			Success=true;
		}
		catch(Exception e)
		{
			LogHelper.EtoStringLog(log, e);
		}
	}//END OF EXECUTION

	
}
