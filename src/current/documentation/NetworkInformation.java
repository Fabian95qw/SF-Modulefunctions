package si.module.examples.documentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import de.vertico.starface.config.server.forms.NetworkDataForm;
import de.vertico.starface.config.server.forms.NetworkInterfaceForm;
import de.vertico.starface.helpers.network.NetworkConfigurationComponent;
import de.vertico.starface.helpers.network.ProxyConfigurationBean;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.module.core.runtime.functions.system.Execute4;
import io.jsonwebtoken.lang.Objects;

@Function(visibility=Visibility.Private, description="")
public class NetworkInformation implements IBaseExecutable 
{
	//##########################################################################################

	@OutputVar(label="Networkdata", description="",type=VariableType.MAP)
	public Map<String, Object> Networkdata = new HashMap<String, Object>();
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		NetworkConfigurationComponent NCC = (NetworkConfigurationComponent)context.springApplicationContext().getBean(NetworkConfigurationComponent.class);
		NetworkDataForm NDF = NCC.loadNetworkDataFromDB();
		
		Execute4 Command = new Execute4();
		Command.command = "dig TXT +short o-o.myaddr.l.google.com @ns1.google.com";
		
		Command.execute(context);
		
		String IPext = Command.output;
		IPext = SafeChar(IPext);
		
		Networkdata.put("natactive", Objects.nullSafeToString(NDF.isNatActive()));
		Networkdata.put("externalip",Objects.nullSafeToString(NDF.getExtIp()));
		Networkdata.put("detectedexternalip", IPext);
		Networkdata.put("hostname",Objects.nullSafeToString(NDF.getHostname()));
		Networkdata.put("gateway",Objects.nullSafeToString(NDF.getGateway()));
		Networkdata.put("dns1",Objects.nullSafeToString(NDF.getDnsServer(0)));
		Networkdata.put("dns2",Objects.nullSafeToString(NDF.getDnsServer(1)));
		ProxyConfigurationBean ProxyConfig = NDF.getProxyConfiguration();
		Networkdata.put("httpproxyenabled",Objects.nullSafeToString(ProxyConfig.isHttpProxyEnabled()));
		Networkdata.put("httpsproxyenabled",Objects.nullSafeToString(ProxyConfig.isHttpsProxyEnabled()));
		Networkdata.put("proxyaddress",Objects.nullSafeToString(ProxyConfig.getAddress()));
		Networkdata.put("proxyport", Objects.nullSafeToString(ProxyConfig.getPort()));
		Networkdata.put("proxyrequiresauthentication", Objects.nullSafeToString(ProxyConfig.isAuthEnabled()));
		Networkdata.put("proxyusername", Objects.nullSafeToString(ProxyConfig.getUsername()));
		Networkdata.put("proxypassword", Objects.nullSafeToString(ProxyConfig.getPassword()));

		List<Map<String, Object>> Networkadapters = new ArrayList<Map<String, Object>>();
		
		for(NetworkInterfaceForm NIF : NDF.getInterfaces())
		{
			Map<String, Object> Networkadapter = new HashMap<String, Object>();
			Networkadapter.put("enabled",  Objects.nullSafeToString(NIF.getEnabled()));
			Networkadapter.put("name", Objects.nullSafeToString(NIF.getName()));
			Networkadapter.put("usedhcp", Objects.nullSafeToString(NIF.getDhcp()));
			Networkadapter.put("ip",  Objects.nullSafeToString(NIF.getIp()));
			Networkadapter.put("mask",  Objects.nullSafeToString(NIF.getMask()));
			Networkadapters.add(Networkadapter);
		}
		
		Networkdata.put("networkadapters", Networkadapters);
		
		
	}//END OF EXECUTION

	public String SafeChar(String StringtoFilter)
	{
		String safeChar="0123456789.";
	    String TextResult="";
		char[] allowed = safeChar.toString().toCharArray();
	    char[] charArray = StringtoFilter.toString().toCharArray();
	    StringBuilder Result = new StringBuilder();
	    for (char c : charArray)
	    {
	        for (char a : allowed)
	        {
	            if(c==a) Result.append(a);
	        }
	    }
	    TextResult = Result.toString();
	    return TextResult;
	}
	

	
}
