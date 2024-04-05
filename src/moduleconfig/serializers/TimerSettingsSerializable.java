package si.module.moduleconfig.serializers;

import java.io.Serializable;

import de.vertico.starface.module.core.model.resource.TimerSettingsResource;

public class TimerSettingsSerializable implements Serializable
{
	private static final long serialVersionUID = -4461637528063368530L;

	private String Id="";
	private Integer Multiplikator=0;
	private String Name="";
	private String Periodname="";
	private String Periodresourcekey="";
	private Long Starttime=0L;
	
	public TimerSettingsSerializable(TimerSettingsResource TSR) 
	{
		this.Id=TSR.getId();
		this.Multiplikator = TSR.getMultiplikator();
		this.Name=TSR.getName();
		this.Periodname=TSR.getPeriod().getName();
		this.Periodresourcekey=TSR.getPeriod().getResourceKey();
		this.Starttime =TSR.getStartTime();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return Id;
	}

	public Integer getMultiplikator() {
		return Multiplikator;
	}

	public String getName() {
		return Name;
	}

	public String getPeriodname() {
		return Periodname;
	}

	public String getPeriodresourcekey() {
		return Periodresourcekey;
	}

	public Long getStarttime() {
		return Starttime;
	}

	
	
}
