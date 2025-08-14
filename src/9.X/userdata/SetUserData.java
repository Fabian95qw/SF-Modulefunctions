package si.module.examples.userdata;

import de.starface.bo.UserBusinessObject;
import de.starface.core.component.StarfaceComponentProvider;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.databean.core.ExtendedUserData;

@Function(visibility = Visibility.Private, rookieFunction = false, description = "")
public class SetUserData implements IBaseExecutable
{
	// ##########################################################################################

	@InputVar(label = "STARFACE_USER", description = "User to edit", type = VariableType.STARFACE_USER)
	public Integer STARFACE_USER = 0;

	@InputVar(label = "UserFields", description = "User to edit", valueByReferenceAllowed = true) // Use Enum selection
	public UserFields UF = UserFields.NONE;

	@InputVar(label = "Value", description = "", type = VariableType.OBJECT)
	public Object Value = null;

	@OutputVar(label = "Success", description = "", type = VariableType.BOOLEAN)
	public Boolean Success = false;

	@OutputVar(label = "Errormessage", description = "", type = VariableType.STRING)
	public String Errormessage = "";

	StarfaceComponentProvider componentProvider = StarfaceComponentProvider.getInstance();
	// ##########################################################################################

	// ################### Code Execution ############################
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception
	{
		UserBusinessObject UBO = context.provider().fetch(UserBusinessObject.class); // Fetch BO for Users

		ExtendedUserData EUD = UBO.getUserByAccountId(STARFACE_USER); // Fetch UserData for provided AccountID;

		if (EUD == null)
		{
			Errormessage = "No User found for AccountID: " + STARFACE_USER;
			return;
		}

		try
		{

			switch (UF)
			{
			case CallWaitingIndication:
				UBO.setCallWaitingIndication(STARFACE_USER, (Boolean) Value); // This is a Special case, returning
																				// afterwards because there is nothing
																				// to save.
				Success = true;
				return;
			case EMailfaxjournal:
				EUD.setEmailfaxjournal((Boolean) Value);
				break;
			case Email:
				EUD.setEmail((String) Value);
				break;
			case FamilyNme:
				EUD.setFamilyName((String) Value);
				break;
			case FaxCallerId:
				EUD.setFaxCallerId((String) Value);
				break;
			case FaxHeader:
				EUD.setFaxHeader((String) Value);
				break;
			case Faxcoverpage:
				EUD.setFaxcoverpage((Boolean) Value);
				break;
			case FirstName:
				EUD.setFirstName((String) Value);
				break;
			case Language:
				EUD.setLanguage((String) Value);
				break;
			case Loginnumber:
				EUD.setLoginNumber((String) Value);
				break;
			case MissedCallReport:
				EUD.setMissedCallReport((Boolean) Value);
				break;
			case NONE:
				break;
			default:
				break;

			}
			UBO.updateUser(EUD, false, false, false);
			Success = true;
		}
		catch (ClassCastException e)
		{
			Errormessage = "Provided Value is not of the right Type!" + e.getMessage();

		}
		catch (Exception e)
		{
			Errormessage = e.getMessage();
		}

	}// END OF EXECUTION

	private enum UserFields
	{
		NONE,
		// password
		Email, Language, Loginnumber, FaxCallerId, FaxHeader, Faxcoverpage, EMailfaxjournal, MissedCallReport,
		CallWaitingIndication, FirstName, FamilyNme
	}

}
