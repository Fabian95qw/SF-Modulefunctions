package si.module.moduleupdater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.vertico.starface.module.core.ModuleRegistry;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;

public class Updater 
{
	private String URL="";
	private Map<String, String> GetArgs=null;
	private boolean AllowRedirect = false;
	private String ModuleID="";
	private String ModuleVersion="";
	private String StarfaceVersion="";
 	private boolean DoFuzzySearch=false;
	private boolean HTTPS=false;
 	private IRuntimeEnvironment context = null;
	private Logger log = null;
 	private String Errormessage="";
	private boolean hasError=false;
	private Document D  =null;
	
	private Element Module = null;
	private List<Element> TargetVersions = null;
	private Element TargetVersion = null; 
	private Map<String, String> VersionMap = null;
	
	private String UpdateFolder = "/tmp/mupdate";
	
	public Updater(String URL, Map<String, String> GetArgs, boolean HTTPS, boolean AllowRedirect, String ModuleID, String ModuleVersion, String StarfaceVersion, boolean DoFuzzySearch, IRuntimeEnvironment context)
	{
		this.URL=URL;
		this.HTTPS=HTTPS;
		this.GetArgs=GetArgs;
		this.AllowRedirect=AllowRedirect;
		this.ModuleID=ModuleID;
		this.ModuleVersion=ModuleVersion;
		this.StarfaceVersion=StarfaceVersion;
		this.DoFuzzySearch=DoFuzzySearch;
		this.context=context;
		VersionMap = new HashMap<String, String>();
		this.log=context.getLog();
	}
	
	public boolean LoadMeta()
	{
		DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
		DocumentBuilder DB = null;
		Document D = null;
		InputStream IS = null;
		try 
		{
				if(HTTPS)
				{
					try 
					{
						IS =  HTTPsGet(URL+"meta.xml"+AttachArgs(), AllowRedirect);
					} 
					catch (Exception e) 
					{
						Errormessage ="Connection Error: "+ URL+"meta.xml"+AttachArgs()+ " " + e.getMessage();
						EtoStringLog(e);
					}
				}
				else
				{
					try 
					{
						IS = HTTPGet(URL+"meta.xml"+AttachArgs(), AllowRedirect);
					} 
					catch (Exception e) 
					{
						Errormessage ="Connection Error: "+ URL+"meta.xml"+AttachArgs()+ " " + e.getMessage();
						EtoStringLog(e);
					}
				}
	
				log.debug("Trying to Parse File to XML");
				
				DB = DBF.newDocumentBuilder();
				D = DB.parse(IS);
				
				log.debug("Parsing successful");
				this.D=D;
				return true;
			}
			catch(Exception e)
			{
				EtoStringLog(e);
				return false;
			}
		}
	
	
	
		public void LoadModuleInfo()
		{
			Module = GetModule();
			if(Module == null)
			{
				Errormessage="MODULE_NOT_FOUND";
				hasError=true;
				return;
			}
			GetVersions();
		
			
		}
		
		public boolean hasUpdate()
		{
			log.debug("Finding out if Update is Required");
			Integer NewestVersion = -1;
			TargetVersion = null;
			Integer ThisVersion = -1;	
			
			for(Element TV: TargetVersions)
			{
				ThisVersion = Integer.parseInt(TV.getAttribute(Attr.version.toString()));
				
				if(ThisVersion > NewestVersion)
				{
					TargetVersion = TV;
					NewestVersion = ThisVersion;
				}					
			}
			
			log.debug("Newest Version is: " + NewestVersion);
			
			String Version = ""+NewestVersion;
			
			log.debug(Version + " == " + ModuleVersion);
			if(Version.equals(ModuleVersion))
			{
				log.debug("No Update Required");
				return false;
			}
			else
			{
				log.debug("Newer Version Available");
				return true;
			}
		}
		
		public String LoadReleaseNote() 
		{
			String ReleaseURL = URL+TargetVersion.getAttribute(Attr.file.toString());
			ReleaseURL = ReleaseURL.substring(0, ReleaseURL.lastIndexOf("/"));
			ReleaseURL = ReleaseURL+"/release.txt";
			
			log.debug("Downloading: " + ReleaseURL);
			
			InputStream IS = null;
			try
			{
				if(HTTPS)
				{
					IS = HTTPsGet(ReleaseURL+AttachArgs(), AllowRedirect);
				}
				else
				{
					IS = HTTPGet(ReleaseURL+AttachArgs(), AllowRedirect);
				}
				return IOUtils.toString(IS, "UTF-8");
			}
			catch(Exception e)
			{
				EtoStringLog(e);
				return "Fehler beim Laden der Releasenotes";
			}
		}
		
		public void LoadUpdate()
		{
			try
			{
				log.debug("Update Required");
			
			
				log.debug("Downloading: " + URL+TargetVersion.getAttribute(Attr.file.toString()));
				
				
				File UpdateF = new File(UpdateFolder);
				if(!UpdateF.exists())
				{
					UpdateF.mkdirs();
				}
										
				File F = new File(UpdateFolder+"/"+Module.getAttribute(Attr.id.toString())+"_ModuleUpdate.sfm");
				
				log.debug("Downloading to: " + F.getAbsolutePath());
				
				F.createNewFile();
				FileOutputStream FOS = new FileOutputStream(F);
				InputStream FIS = null;
				
				if(HTTPS)
				{
					try 
					{
						FIS = HTTPsGet(URL+TargetVersion.getAttribute(Attr.file.toString())+AttachArgs(), AllowRedirect);
					} 
					catch (Exception e) 
					{
						hasError = true;
						Errormessage ="Downloading Update failed: " + URL+TargetVersion.getAttribute(Attr.file.toString())+AttachArgs();
						EtoStringLog(e);
						FOS.close();
						return;
					}
				}
				else
				{
					try 
					{
						FIS = HTTPGet(URL+TargetVersion.getAttribute(Attr.file.toString())+AttachArgs(), AllowRedirect);
					} 
					catch (Exception e) 
					{
						hasError = true;
						Errormessage ="Downloading Update failed: " + URL+TargetVersion.getAttribute(Attr.file.toString())+AttachArgs();
						EtoStringLog(e);
						FOS.close();
						return;
					}
				}
				
				ModuleRegistry MR = (ModuleRegistry)context.springApplicationContext().getBean(ModuleRegistry.class);
			
				
				if(FIS != null)
				{
					byte[] Buffer = new byte[1024*16];
					int r = 0;
					
					while( (r = FIS.read(Buffer)) >0)
					{
						FOS.write(Buffer, 0, r);
					}
					
					FOS.close();
					
					log.debug("Download Completed");
	
					String Hash = TargetVersion.getAttribute(Attr.hash.toString());					
					
					String FileHash = DigestUtils.md5Hex(new FileInputStream(F));
					
					log.debug(Hash +"<==>"+FileHash);
					if(Hash.equals(FileHash))
					{
						log.debug("Trying to Push Update...");
						MR.importModule(F.getAbsolutePath(), true);
						log.debug("Update Pushed!");
					}
					else
					{
						hasError = true;
						log.debug("Hash Mismatch!");
						Errormessage ="Hash Mismatch "+ Hash +"<==>"+FileHash;
					}
				}
				else
				{
					log.debug("Update Failed!");
					hasError = true;
					Errormessage="Could not Open File Stream for download";
				}	
			}
			catch(IOException e)
			{
				hasError = true;
				
			}
			catch(Exception e)
			{
				hasError = true;
				Errormessage ="Unknown Exception 1313";
				EtoStringLog(e);
			}
		}
		
		private void GetVersions()
		{
			TargetVersions = new ArrayList<Element>();
			{
				Integer Fuzzy = StarfaceVersion.split("\\.").length-1;
				String SearchVersion = "";
				String[] Split = null;
				NodeList Versions = Module.getElementsByTagName(Meta.version.toString());
				Element Version = null;
				while (TargetVersions.size() == 0 && Fuzzy >= 0)
				{
					
					SearchVersion ="";
					if(DoFuzzySearch)
					{
						Boolean First = true;
						Split = StarfaceVersion.split("\\.");
						for(int Count = 0 ; Count <= Fuzzy; Count++)
						{
							if(First)
							{
								SearchVersion = Split[Count];
								First = false;
							}
							else
							{
								SearchVersion = SearchVersion+"."+Split[Count];
							}
						}
					}
					else
					{
						SearchVersion = StarfaceVersion;
						Fuzzy = -1;
					}
					log.debug("Searching for Version: " + SearchVersion);
					
					
					for (int Count = 0; Count < Versions.getLength(); ++Count)
					{	
						Version = (Element) Versions.item(Count);
						
						VersionMap.put(Version.getAttribute(Attr.sfversion.toString()), Version.getAttribute(Attr.version.toString()));
						
						log.debug(Version.getAttribute(Attr.version.toString()));
						log.debug(Version.getAttribute(Attr.sfversion.toString()));
						log.debug(Version.getAttribute(Attr.file.toString()));
						log.debug(Version.getAttribute(Attr.hash.toString()));
						
						if(Version.getAttribute(Attr.sfversion.toString()).equals(SearchVersion))
						{
							//log.debug("Found Version for this SF-Version");
							TargetVersions.add(Version);
						}
					}
					Fuzzy--;
				}
			}
		}
			
		private Element GetModule()
		{
			Element TargetModule = null;
			{
				NodeList Modules = D.getElementsByTagName(Meta.module.toString());
				Element Module = null;
				
				for (int Count = 0; Count < Modules.getLength(); ++Count)
				{
					Module = (Element) Modules.item(Count);
					log.debug(Module.getAttribute(Attr.id.toString()) + " ==> " + Module.getAttribute(Attr.name.toString()));
					
					if(Module.getAttribute(Attr.id.toString()) != null && Module.getAttribute(Attr.id.toString()).equals(ModuleID))
					{
						//log.debug("Module found in Meta. Loading Versions");
						TargetModule = Module;
						break;
					}
				}
			}
			return TargetModule;
		}
		
		public String AttachArgs()
		{
			StringBuilder SB = new StringBuilder();
			SB.append("?");
			
			if(GetArgs == null || GetArgs.size() == 0)
			{
				return "";
			}
			else
			{
				for(Entry<String, String> Entry: GetArgs.entrySet())
				{
					SB.append(Entry.getKey()+"="+Entry.getValue()+"&");
				}
			}
			String Args =SB.toString();
			Args = Args.substring(0, Args.length()-1);
			return Args;
		}
		
		private InputStream HTTPsGet(String RequestURL, boolean AllowRedirect) throws IOException, NoSuchAlgorithmException, KeyManagementException
		{
			URL TargetURL=null;
			
			InputStream IS = null;
					
			int TimeOut=10;
			int Counter=0;
			HttpsURLConnection HTTPSConnection = null;	
			
			log.debug("Using HTTPS");
			log.debug("Creating TrustManager");
			
			  class EasyTrustManager
			    implements X509TrustManager
			  {
			    public void checkClientTrusted(X509Certificate[] chain, String authType)
			      throws CertificateException
			    {}
			    
			    public void checkServerTrusted(X509Certificate[] chain, String authType)
			      throws CertificateException
			    {}
			    
			    public X509Certificate[] getAcceptedIssuers()
			    {
			      return new X509Certificate[0];
			    }
			  }
			
			SSLContext sc = SSLContext.getInstance("SSL");
		    sc.init(null, new TrustManager[] { new EasyTrustManager() }, null);
		    
		    //HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			
			TargetURL = new URL(RequestURL);
			log.debug("Trying to Connect to: " + TargetURL);
			HTTPSConnection = (HttpsURLConnection) TargetURL.openConnection();
			HTTPSConnection.setSSLSocketFactory(sc.getSocketFactory());
			
			log.debug("Setting AllowRedirect 302 to " + AllowRedirect);
			HTTPSConnection.setInstanceFollowRedirects(AllowRedirect);
			//HTTPSConnection.setRequestMethod("GET");
			log.debug("Setting Connection Type to GET");
			HTTPSConnection.setDoOutput(true);
			log.debug("Timeout is:" +(TimeOut*1000));
			HTTPSConnection.setConnectTimeout(TimeOut*1000);
			
			log.debug("Getting Data");
			IS = HTTPSConnection.getInputStream(); 
			
			while(true)
			{
				log.debug(HTTPSConnection.getResponseCode());
				log.debug("Server Answer:" + HTTPSConnection.getResponseCode());
				if(HTTPSConnection.getResponseCode()==200)
				{
					log.debug("Response is 200");
					break;
				}
				else if(HTTPSConnection.getResponseCode()==302 && AllowRedirect)
				{
					//Success=true;
					//ResponseOutput ="302 OK!";
					log.debug("Response is 302");
					RequestURL=HTTPSConnection.getHeaderField("Location");
					if(RequestURL.contains("http://"))
					{
						log.debug("302 Redirection from https to http is not allowed");
						hasError = true;
						return null;
					}
					else
					{
						log.debug("302 Redirection is active, following url");
						TargetURL = new URL(RequestURL);
						HTTPSConnection = (HttpsURLConnection) TargetURL.openConnection();
						HTTPSConnection.setInstanceFollowRedirects(AllowRedirect);
						HTTPSConnection.setDoOutput(true);
						HTTPSConnection.setConnectTimeout(TimeOut*1000);
					}
				}
				else if(HTTPSConnection.getResponseCode()==302 && !AllowRedirect)
				{
					log.debug("302 was Returned. but 302 is not accepted.");
					hasError = true;
					return null;
				}
				else if(Counter == TimeOut)
				{
					log.debug("Timeout reached!");
					return null;
				}
				Counter++;
			}
			return IS;
		}
			
		private InputStream HTTPGet(String RequestURL, boolean AllowRedirect) throws IOException, InterruptedException
		{
			URL TargetURL=null;
			HttpURLConnection Connection = null;
			
			InputStream IS = null;
					
			int TimeOut=10;
			int Counter=0;

			log.debug("Using HTTP");
			log.debug("Target URL is:" + RequestURL);
			TargetURL = new URL(RequestURL);
			log.debug("Connecting...");
			Connection = (HttpURLConnection) TargetURL.openConnection();
			log.debug("Setting AllowRedirect 302 to:" + AllowRedirect);
			Connection.setInstanceFollowRedirects(AllowRedirect);
			log.debug("Setting Connection Type to GET");
			Connection.setDoOutput(true);
			log.debug("Setting Timeout to: "+TimeOut*1000);
			Connection.setConnectTimeout(TimeOut*1000);
							
			log.debug("Getting Stream");
			IS = Connection.getInputStream(); 
			
			while(true)
			{
				log.debug("Answer from Server:" + Connection.getResponseCode());
				log.debug(Connection.getResponseCode());
				if(Connection.getResponseCode()==200)
				{
					log.debug("Server Returned 200");
					break;
				}
				else if(Connection.getResponseCode()==302 && AllowRedirect)
				{
					//Success=true;
					//ResponseOutput ="302 OK!";
					log.debug("Server Returned 302");
					RequestURL=Connection.getHeaderField("Location");
					if(RequestURL.contains("https://"))
					{
						log.debug("302 Redirection from http to https is not allowed");
						hasError = true;
						return null;
					}
					log.debug("302 Redireaction is active");
					TargetURL = new URL(RequestURL);
					log.debug("New Target is" + RequestURL);
					Connection = (HttpURLConnection) TargetURL.openConnection();
					Connection.setInstanceFollowRedirects(AllowRedirect);
					Connection.setDoOutput(true);
					Connection.setConnectTimeout(TimeOut*1000);
					//break;
				}
				else if(Connection.getResponseCode()==302 && !AllowRedirect)
				{
					log.debug("302 was Returned. but Redirection is not allwed");
					return null;
				}
				else if(Counter == TimeOut)
				{
					log.debug("Timeout reached!");
					return null;
				}
				Counter++;
				Thread.sleep(1000);
				log.debug("Timeout: " + Counter);
			}
			
			return IS;
		}
		
		private enum Meta
		{
			module,
			version
		}
		
		private enum Attr
		{
			id,
			name,
			version,
			sfversion,
			file,
			hash
		}
		
		
		public void EtoStringLog(Exception e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.debug(sw.toString()); //
		}
		
		public void EtoStringLog(Throwable e)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.debug(sw.toString()); //
		}

		public String getErrormessage() {
			return Errormessage;
		}

		public boolean hasError() {
			return hasError;
		}

		public String getNewVersion() 
		{
			if(TargetVersion == null)
			{
				return "-1";
			}
			return TargetVersion.getAttribute(Attr.version.toString());
		}

		public Map<String, String> getVersionMap() 
		{
			return VersionMap;
		}


	
		
}
