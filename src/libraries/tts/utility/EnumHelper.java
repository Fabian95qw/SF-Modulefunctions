package nucom.module.tts.utility;

public class EnumHelper 
{
	
	public enum Language
	{
		Catalan("ca-es"),
		Chinese_China("zh-cn"),
		Chinese_HongKong("zh-hk"),
		Chinese_Taiwan("zh-tw"),
		Danish("da-dk"),
		Dutch("nl-nl"),
		English_Australia("en-au"),
		English_Canada("en-ca"),
		English_GreatBritain("en-gb"),
		English_India("en-in"),
		English_UnitedStates("en-us"),
		Finnish("fi-fi"),
		French_Canada("fr-ca"),
		French_France("fr-fr"),
		German("de-de"),
		Italian("it-it"),
		Japanese("ja-jp"),
		Korean("ko-kr"),
		Norwegian("nb-no"),
		Polish("pl-pl"),
		Portuguese_Brazil("pt-br"),
		Portuguese_Portugal("pt-pt"),
		Russian("ru-ru"),
		Spanish_Mexico("es-mx"),
		Spanish_Spain("es-es"),
		Swedish("sv-se");
		
	    private String S;
		 
	    Language(String S) {
	        this.S = S;
	    }
	 
	    public String toString() {
	        return S;
	    }
	}
	
	public enum AudioCodec
	{
		MP3,
		WAV,
		AAC,
		OGG,
		CAF
	}
	
	public enum AudioFormat
	{
		AF_8khz_8bit_mono("8khz_8bit_mono"),
		AF_8khz_8bit_stereo("8khz_8bit_stereo"),
		AF_8khz_16bit_mono("8khz_16bit_mono"),
		AF_8khz_16bit_stereo("8khz_16bit_stereo"),
		AF_11khz_8bit_mono("11khz_8bit_mono"),
		AF_11khz_8bit_stereo("11khz_8bit_stereo"),
		AF_11khz_16bit_mono("11khz_16bit_mono"),
		AF_11khz_16bit_stereo("11khz_16bit_stereo"),
		AF_12khz_8bit_mono("12khz_8bit_mono"),
		AF_12khz_8bit_stereo("12khz_8bit_stereo"),
		AF_12khz_16bit_mono("12khz_16bit_mono"),
		AF_12khz_16bit_stereo("12khz_16bit_stereo"),
		AF_16khz_8bit_mono("16khz_8bit_mono"),
		AF_16khz_8bit_stereo("16khz_8bit_stereo"),
		AF_16khz_16bit_mono("16khz_16bit_mono"),
		AF_16khz_16bit_stereo("16khz_16bit_stereo"),
		AF_22khz_8bit_mono("22khz_8bit_mono"),
		AF_22khz_8bit_stereo("22khz_8bit_stereo"),
		AF_22khz_16bit_mono("22khz_16bit_mono"),
		AF_22khz_16bit_stereo("22khz_16bit_stereo"),
		AF_24khz_8bit_mono("24khz_8bit_mono"),
		AF_24khz_8bit_stereo("24khz_8bit_stereo"),
		AF_24khz_16bit_mono("24khz_16bit_mono"),
		AF_24khz_16bit_stereo("24khz_16bit_stereo"),
		AF_32khz_8bit_mono("32khz_8bit_mono"),
		AF_32khz_8bit_stereo("32khz_8bit_stereo"),
		AF_32khz_16bit_mono("32khz_16bit_mono"),
		AF_32khz_16bit_stereo("32khz_16bit_stereo"),
		AF_44khz_8bit_mono("44khz_8bit_mono"),
		AF_44khz_8bit_stereo("44khz_8bit_stereo"),
		AF_44khz_16bit_mono("44khz_16bit_mono"),
		AF_44khz_16bit_stereo("44khz_16bit_stereo"),
		AF_48khz_8bit_mono("48khz_8bit_mono"),
		AF_48khz_8bit_stereo("48khz_8bit_stereo"),
		AF_48khz_16bit_mono("48khz_16bit_mono"),
		AF_48khz_16bit_stereo("48khz_16bit_stereo"),
		AF_alaw_8khz_mono("alaw_8khz_mono"),
		AF_alaw_8khz_stereo("alaw_8khz_stereo"),
		AF_alaw_11khz_mono("alaw_11khz_mono"),
		AF_alaw_11khz_stereo("alaw_11khz_stereo"),
		AF_alaw_22khz_mono("alaw_22khz_mono"),
		AF_alaw_22khz_stereo("alaw_22khz_stereo"),
		AF_alaw_44khz_mono("alaw_44khz_mono"),
		AF_alaw_44khz_stereo("alaw_44khz_stereo"),
		AF_ulaw_8khz_mono("ulaw_8khz_mono"),
		AF_ulaw_8khz_stereo("ulaw_8khz_stereo"),
		AF_ulaw_11khz_mono("ulaw_11khz_mono"),
		AF_ulaw_11khz_stereo("ulaw_11khz_stereo"),
		AF_ulaw_22khz_mono("ulaw_22khz_mono"),
		AF_ulaw_22khz_stereo("ulaw_22khz_stereo"),
		AF_ulaw_44khz_mono("ulaw_44khz_mono"),
		AF_ulaw_44khz_stereo("ulaw_44khz_stereo");
		
	    private String S;
		 
	    AudioFormat(String S) {
	        this.S = S;
	    }
	 
	    public String toString() {
	        return S;
	    }
	}
}
