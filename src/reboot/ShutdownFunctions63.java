package si.module.reboot;

import org.apache.commons.logging.Log;

import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.helpers.SystemUtils;
import de.vertico.starface.manager.SessionManager;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.servlets.LogoutServlet;


@Function(visibility=Visibility.Private, rookieFunction=false, description="Reboot")
public class ShutdownFunctions63 implements IBaseExecutable 
{
	//##########################################################################################
	
	  public static enum Optionen
	  {
	    Herunterfahren, Neustarten, Webserver_Neustart, XMPP_Neustart, Asterisk_Neustart, Alle_Dienste_Neustarten;
	    private Optionen() {}
	  }
	
	@InputVar(label="Funktion", description="Auswahl, Anlage Herunterfahren oder Neustarten",type=VariableType.STRING, valueByReferenceAllowed=false)
	public Optionen optionen = Optionen.Neustarten;

    StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance(); 
    //##########################################################################################
	
	//###################			Code Execution			############################	

	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Log log = context.getLog();
		
		log.debug("Triggering Reboot with Option: " +  optionen.toString());
	    		
		Thread T = new Thread(new Reboot());
		T.start();
		
	}//END OF EXECUTION
	

	public class Reboot implements Runnable
	{
	
		@Override
		public void run() 
		{
			try 
			{
				Thread.sleep(10000);
			} 
			catch (InterruptedException e) {}
			switch (optionen)
			{
			case Herunterfahren:
				(componentProvider.fetch(SessionManager.class)).logoutAll(LogoutServlet.LogoutType.SERVER_SHUTDOWN);
				(componentProvider.fetch(SessionManager.class)).shutdown();
				SystemUtils.shutdownServer();
				break;
			case Neustarten:
				(componentProvider.fetch(SessionManager.class)).logoutAll(LogoutServlet.LogoutType.SERVER_RESTART);
				(componentProvider.fetch(SessionManager.class)).restart();
				SystemUtils.restartServer();
				break;
			case Webserver_Neustart:
				//(componentProvider.fetch(SessionManager.class)).logoutAll(LogoutServlet.LogoutType.WEBSERVER_RESTART);
				// Verursacht Probleme, da kein AutoLogin m�glich ist
				SystemUtils.restartTomcat();
				break;
			case XMPP_Neustart:
				//(componentProvider.fetch(SessionManager.class)).logoutAll(LogoutServlet.LogoutType.UNKNOW);
			    SystemUtils.restartXMPP();
			    try {Thread.sleep(60000);} catch (InterruptedException e){}
				break;
			case Asterisk_Neustart:
				//(componentProvider.fetch(SessionManager.class)).logoutAll(LogoutServlet.LogoutType.UNKNOW);
			    SystemUtils.restartAsterisk(true);
			    try {Thread.sleep(60000);} catch (InterruptedException e){}
				break;
			case Alle_Dienste_Neustarten:
				//(componentProvider.fetch(SessionManager.class)).logoutAll(LogoutServlet.LogoutType.SERVICE_RESTART);
			    SystemUtils.restartServices();
			    try {Thread.sleep(60000);} catch (InterruptedException e){}
				break;
			}			
		}
	}
	

}

