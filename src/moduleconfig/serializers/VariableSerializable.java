package si.module.moduleconfig.serializers;

import java.io.Serializable;

public class VariableSerializable implements Serializable
{
	private static final long serialVersionUID = -4256139433880769025L;
	private String Type="";
	private String Name="";
	private String Content="";	
	private boolean hasContent=false;
	
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
		hasContent=true;
	}
	
	public boolean hasContent() {
		return hasContent;
	}
	@Override
	public String toString()
	{
		return Name+" | " + Type;
	}
	
}
