package si.module.tts.voicerss.utility;

public class EnumHelper 
{
	
	public enum Language
	{
		Catalan("ca-es"),
		Chinese_China("zh-cn"),
		Chinese_HongKong("zh-hk"),
		Chinese_Taiwan("zh-tw"),
		Danish("da-dk"),
		Dutch_Belgium("nl-be"),
		Dutch_Netherlands("nl-nl"),
		English_Australia("en-au"),
		English_Canada("en-ca"),
		English_GreatBritain("en-gb"),
		English_India("en-in"),
		English_Ireland("en-ie"),
		English_UnitedStates("en-us"),
		Finnish("fi-fi"),
		French_Canada("fr-ca"),
		French_France("fr-fr"),
		French_Switzerland("fr-ch"),
		German_Austria("de-at"),
		German_Germany("de-de"),
		German_Switzerland("de-ch"),
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
		Swedish("sv-se"),
		Romanian("ro-ro"),
		Tamil("ta-in"),
		Thai("th-th"),
		Turkish("tr_TR"),
		Vietnamese("vi-vn"),
		Arabic_SaudiArabia("ar-sa"),
		Arabic_Egypt("ar-eg"),
		Bulgarian("bg-bg"),
		Croatian("hr-hr"),
		Czech("cs-cz"),
		Greek("el-gr"),
		Hebrew("he-il"),
		Hindi("hi-in"),
		Hungarian("hu-hu"),
		Indonesian("id-id"),
		Malay("ms-my"),
		Slovak("sk-sk"),
		Slovenian("sl-si"),
		;
	    private String S;
		 
	    Language(String S) {
	        this.S = S;
	    }
	 
	    public String toString() {
	        return S;
	    }
	}
	

	public enum Voice
	{
		Arabic_Egypt_Oda("Oda"),
		Arabic_SaudiArabia_Salim("Salim"),
		Bulgarian_Dimo("Dimo"),
		Catalan_Rut("Rut"),
		Chinese_China_Chow("Chow"),
		Chinese_China_Luli("Luli"),
		Chinese_China_Shu("Shu"),
		Chinese_China_Wang("Wang"),
		Chinese_HongKong_Chen("Chen"),
		Chinese_HongKong_Jia("Jia"),
		Chinese_HongKong_Xia("Xia"),
		Chinese_Taiwan_Akemi("Akemi"),
		Chinese_Taiwan_Lee("Lee"),
		Chinese_Taiwan_Lin("Lin"),
		Croatian_Nikola("Nikola"),
		Czech_Josef("Josef"),
		Danish_Freja("Freja"),
		Dutch_Belgium_Daan("Daan"),
		Dutch_Netherlands_Bram("Bram"),
		Dutch_Netherlands_Lotte("Lotte"),
		English_Australia_Evie("Evie"),
		English_Australia_Isla("Isla"),
		English_Australia_Jack("Jack"),
		English_Australia_Zoe("Zoe"),
		English_Canada_Clara("Clara"),
		English_Canada_Emma("Emma"),
		English_Canada_Mason("mason"),
		English_Canada_Rose("Rose"),
		English_GreatBritain_Alice("Alice"),
		English_GreatBritain_Harry("Harry"),
		English_GreatBritain_Lily("Lily"),
		English_GreatBritain_Nancy("Nancy"),
		English_India_Ajit("Ajit"),
		English_India_Eka("Eka"),
		English_India_Jai("Jai"),
		English_Ireland_Oran("Oran"),
		English_UnitedStates_Amy("Amy"),
		English_UnitedStates_John("John"),
		English_UnitedStates_Linda("Linda"),
		English_UnitedStates_Mary("Mary"),
		English_UnitedStates_Mike("Mike"),
		Finnish_Aada("Aada"),
		French_Canada_Emile("Emile"),
		French_Canada_Felix("Felix"),
		French_Canada_Logan("Logan"),
		French_Canada_Olivia("Olivia"),
		French_France_Axel("Axel"),
		French_France_Bette("Bette"),
		French_France_Iva("Iva"),
		French_France_Zola("Zola"),
		French_Switzerland_Theo("Theo"),
		German_Austria_Lukas("Lukas"),
		German_Germany_Hanna("Hanna"),
		German_Germany_Jonas("Jonas"),
		German_Germany_Lina("Lina"),
		German_Switzerland_Tim("Tim"),
		Greek_Neo("Neo"),
		Hebrew_Rami("Rami"),
		Hindi_Puja("Puja"),
		Hindi_Puja_Kabir("Kabir"),
		Hungarian_Mate("Mate"),
		Indonesian_Intan("Intan"),
		Italian_Bria("Bria"),
		Italian_Mia("Mia"),
		Italian_Pietro("Pietro"),
		Japanese_Airi("Airi"),
		Japanese_Akira("Akira"),
		Japanese_Fumi("Fumi"),
		Japanese_Hina("Hina"),
		Korean_Nari("Nari"),
		Malay_Aqil("Aqil"),
		Norwegian_Erik("Erik"),
		Norwegian_Marte("Marte"),
		Polish_Jan("Jan"),
		Polish_Julia("Julia"),
		Portuguese_Brazil_Dinis("Dinis"),
		Portuguese_Brazil_Ligia("Ligia"),
		Portuguese_Brazil_Marcia("Marcia"),
		Portuguese_Brazil_Yara("Yara"),
		Portuguese_Portugal_Leonor("Leonor"),
		Romanian_Doru("Doru"),
		Russian_Marina("Marina"),
		Russian_Olga("Olga"),
		Russian_Peter("Peter"),
		Slovak_Beda("Beda"),
		Slovenian_Vid("Vid"),
		Spanish_Mexico_Jose("Jose"),
		Spanish_Mexico_Juana("Juana"),
		Spanish_Mexico_Silvia("Silvia"),
		Spanish_Mexico_Teresa("Teresa"),
		Spanish_Spain_Camila("Camila"),
		Spanish_Spain_Diego("Diego"),
		Spanish_Spain_Luna("Luna"),
		Swedish_Hugo("Hugo"),
		Swedish_Molly("Molly"),
		Tamil_Sai("Sai"),
		Thai_Ukrit("Ukrit"),
		Turkish_Omer("Omer"),
		Vietnamese_Chi("Chi"),
		;
		
	    private String S;
		 
		Voice(String S) {
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
