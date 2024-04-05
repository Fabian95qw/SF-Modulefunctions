package si.module.moduleconfig.serializers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;

import org.apache.commons.io.IOUtils;

import de.vertico.starface.module.core.model.resource.FileResource;

public class FileSerializable implements Serializable 
{
	private static final long serialVersionUID = 7314663765851819068L;
	private String Id="";
	private String Name="";
	private String Locale="";
	private String RealFilename="";
	private String RealFileExtension="";
	private String Type="";
	private String ContentB64="";
	
	public FileSerializable(FileResource FR) throws IOException 
	{
		this.Id=FR.getId();
		this.Name=FR.getName();
		this.Locale=FR.getLocale();
		this.RealFilename=FR.getRealFileName();
		this.RealFileExtension=FR.getRealFileExtension();
		this.Type=FR.getType().toString();
		FileInputStream FIS = new FileInputStream(FR.getRealFile());

		ContentB64 = Base64.getEncoder().encodeToString(IOUtils.toByteArray(FIS));
		FIS.close();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return Id;
	}

	public String getName() {
		return Name;
	}

	public String getLocale() {
		return Locale;
	}

	public String getRealFilename() {
		return RealFilename;
	}

	public String getRealFileExtension() {
		return RealFileExtension;
	}

	public String getType() {
		return Type;
	}

	public String getContentB64() {
		return ContentB64;
	}

	
	
}
