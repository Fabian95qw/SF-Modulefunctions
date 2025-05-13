package si.module.documentation.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import de.starface.bo.RedirectBusinessObject;
import de.starface.bo.UserBusinessObject;
import de.starface.core.component.StarfaceComponentProvider;
import de.starface.integration.uci.java.v30.types.Mailbox;
import de.starface.integration.uci.java.v30.types.RedirectSetting;
import de.starface.integration.uci.java.v30.values.OrderDirection;
import de.starface.integration.uci.java.v30.values.RedirectSettingProperties;
import de.starface.integration.uci.java.v30.values.RedirectSettingType;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.databean.core.ExtendedUserData;
import io.jsonwebtoken.lang.Objects;

@Function(visibility = Visibility.Private,  description = "")
public class GetAllUserRedirections implements IBaseExecutable
{
	// ##########################################################################################

	@OutputVar(label = "Redirections", description = "", type = VariableType.LIST)
	public List<Map<String, Object>> Redirections = new ArrayList<Map<String, Object>>();

	StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance();
	// ##########################################################################################

	// ################### Code Execution ############################
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception
	{
		Logger log = context.getLog();

		log.debug("Extracting Redirectiondata");

		RedirectBusinessObject RBO = (RedirectBusinessObject) context.provider().fetch(RedirectBusinessObject.class);
		UserBusinessObject UBO = context.provider().fetch(UserBusinessObject.class);

		for (ExtendedUserData EUD : UBO.getUsers())
		{
			Map<String, Object> Map = new HashMap<String, Object>();
			Map.put("firstname", Objects.nullSafeToString(EUD.getFirstName()));
			Map.put("lastname", Objects.nullSafeToString(EUD.getFamilyName()));
			Map.put("loginid", Objects.nullSafeToString(EUD.getLoginNumber()));

			List<Map<String, String>> RedirectionList = new ArrayList<Map<String, String>>();

			List<RedirectSetting> Always = RBO.getRedirectSettings(EUD.getAccountId(), RedirectSettingType.ALWAYS, "",
					RedirectSettingProperties.calledNumber, OrderDirection.ASCENDING);
			List<RedirectSetting> Busy = RBO.getRedirectSettings(EUD.getAccountId(), RedirectSettingType.BUSY, "",
					RedirectSettingProperties.calledNumber, OrderDirection.ASCENDING);
			List<RedirectSetting> Timeout = RBO.getRedirectSettings(EUD.getAccountId(), RedirectSettingType.TIMEOUT, "",
					RedirectSettingProperties.calledNumber, OrderDirection.ASCENDING);

			for (RedirectSetting RSAlways : Always)
			{
				if(RSAlways.isGroupRedirect()){continue;}
				Map<String, String> M = new HashMap<String, String>();
				M.put("number", RSAlways.getCalledNumber());
				RedirectSetting RSBusy = FindSetting(RSAlways.getCalledNumber(), Busy);
				RedirectSetting RSTimeout = FindSetting(RSAlways.getCalledNumber(), Timeout);

				M.put("always_enabled", Objects.nullSafeToString(RSAlways.isEnabled()));

				switch (RSAlways.getDestinationType())
				{
				case PHONENUMBER:
					M.put("always_target", Objects.nullSafeToString(RSAlways.getLastDestinationNumber()));
					break;
				case VOICEMAIL:		
					M.put("always_target", Objects.nullSafeToString("[?]"+ RSAlways.getLastDestinationMailbox()));
					if(RSAlways.getLastDestinationMailbox() != null)
					{
						for(Mailbox MB : RSAlways.getMailboxes())
						{
							if(MB.getId().equals(RSAlways.getLastDestinationMailbox()))
							{
								M.put("always_target", Objects.nullSafeToString(MB.getName()));
								break;
							}
						}
					}
					break;
				}

				M.put("busy_enabled", Objects.nullSafeToString(RSBusy.isEnabled()));

				switch (RSBusy.getDestinationType())
				{
				case PHONENUMBER:
					M.put("busy_target", Objects.nullSafeToString(RSBusy.getLastDestinationNumber()));
					break;
				case VOICEMAIL:
					M.put("busy_target", Objects.nullSafeToString("[?]"+ RSBusy.getLastDestinationMailbox()));
					if(RSBusy.getLastDestinationMailbox() != null)
					{
						for(Mailbox MB : RSBusy.getMailboxes())
						{
							if(MB.getId().equals(RSBusy.getLastDestinationMailbox()))
							{
								M.put("busy_target", Objects.nullSafeToString(MB.getName()));
								break;
							}
						}
					}
					break;
				}

				M.put("timeout_enabled", Objects.nullSafeToString(RSTimeout.isEnabled()));
				M.put("timeout_timeout", Objects.nullSafeToString(RSTimeout.getTimeout()));

				switch (RSTimeout.getDestinationType())
				{
				case PHONENUMBER:
					M.put("timeout_target", Objects.nullSafeToString(RSTimeout.getLastDestinationNumber()));
					break;
				case VOICEMAIL:
					M.put("timeout_target", Objects.nullSafeToString("[?]"+ RSTimeout.getLastDestinationMailbox()));
					if(RSTimeout.getLastDestinationMailbox() != null)
					{
						for(Mailbox MB : RSTimeout.getMailboxes())
						{
							if(MB.getId().equals(RSTimeout.getLastDestinationMailbox()))
							{
								M.put("timeout_target", Objects.nullSafeToString(MB.getName()));
								break;
							}
						}
					}
					break;
				}
				RedirectionList.add(M);
			}
			Map.put("redirections", RedirectionList);

			Redirections.add(Map);
		}

	}// END OF EXECUTION

	private RedirectSetting FindSetting(String Number, List<RedirectSetting> RedirectSettings)
	{
		for (RedirectSetting RS : RedirectSettings)
		{
			if (RS.getCalledNumber().equals(Number))
			{
				return RS;
			}
		}
		return null;
	}

}
