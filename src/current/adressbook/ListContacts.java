package si.module.examples.adressbook;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;


import de.vertico.starface.frontend.addressbook.form.AddressbookContact;
import de.vertico.starface.frontend.addressbook.form.DataEntry;
import de.vertico.starface.frontend.addressbook.form.SearchResultWithEstimate;
import de.vertico.starface.manager.beans.SearchBean;
import de.vertico.starface.module.core.model.VariableType;
import de.vertico.starface.module.core.model.Visibility;
import de.vertico.starface.module.core.runtime.IBaseExecutable;
import de.vertico.starface.module.core.runtime.IRuntimeEnvironment;
import de.vertico.starface.module.core.runtime.annotations.Function;
import de.vertico.starface.module.core.runtime.annotations.InputVar;
import de.vertico.starface.module.core.runtime.annotations.OutputVar;
import de.vertico.starface.persistence.connector.addressbook.AddressBookHandler;
import de.vertico.starface.persistence.connector.addressbook.AddressBookInterface;

@Function(visibility=Visibility.Private, description="")
public class ListContacts implements IBaseExecutable 
{
	//##########################################################################################
	
	@InputVar(label="STARFACE_USER", description="STARFACE_USER",type=VariableType.STARFACE_USER) //To Search the Adressbook a User is required
	public Integer STARFACE_USER=-1;
		  
	@InputVar(label="Foldername", description="Foldername",type=VariableType.STRING) //The folder to search. Eg "All/private/0/1/2..."
	public String Foldername="";
	
	@InputVar(label="isPrivate", description="",type=VariableType.BOOLEAN) //Define if only the users private contacts should be listed
	public boolean isPrivate=false;
	
	@InputVar(label="Contacts", description="Contacts",type=VariableType.MAP) //A List of Maps, each representing a Contact
	List<Map<String, Object>> Contacts = new ArrayList<Map<String, Object>>();
	
	@OutputVar(label="Success", description="",type=VariableType.BOOLEAN)
	public Boolean Success = false;
	
     
    //##########################################################################################
	
	//###################			Code Execution			############################	
	@Override
	public void execute(IRuntimeEnvironment context) throws Exception 
	{
		Logger log = context.getLog();
		
		AddressBookHandler ABH = (AddressBookHandler)context.springApplicationContext().getBean(AddressBookHandler.class); //Fetch the Adressbookhandler
		
		AddressBookInterface ABI = ABH.getAddressBookInterface(); //Fetch the Interface, this Example Only works if the Returned Interface is the Local Adressbook. If its LDAP this example will not work
		
		
		if(STARFACE_USER == -1) //If no Account is supplied 
		{
			log.debug("No Account supplied.");
			return;
		}
		
		SearchBean SB = new SearchBean(); //Create a SearchBean for the Request
		SB.setAccountId(STARFACE_USER); //Set the User
		SB.setPageSize(Integer.MAX_VALUE); //Set the Max Amount of allowed returns to the Max Allowed Coontacts of 2 147 483 647
		SB.setSearchBase(Foldername);  //Restrict the Folders for the search
	
		log.debug("Executing Search: " + STARFACE_USER+"-"+Foldername);
		SearchResultWithEstimate SR = ABI.search(SB, "", true); //Search the Folder with no Searchterm defined, so it will return all Users
				
		List<AddressbookContact> srfiltered = new ArrayList<AddressbookContact>(); // Setup new List for Filter
	    if(isPrivate) //If the Search is private, there is a separate list of ACL's which lists, which contact can only be seen by which user
	    {
			//Even if the Folder "private" is searched, it will return all Private Contacts of all users, unless filtered
			List<List<Object>> ACLList = GetACLforPerson(STARFACE_USER, log);
			
			String PersonID=null;
			
		    for (AddressbookContact AC : SR.getContacts()) //Check for each Contact, if the ACL List contains it's UUID
		    {		 
		    	//TODO Replace with Contains logic
		      	for(List<Object> SubList : ACLList)
		      	{
		      		PersonID = SubList.get(0).toString();		      		
		      		if(AC.getId().equals(PersonID))
		      		{
		      			srfiltered.add(AC);
		      			break;
		      		}    		
		      	}  
		    }
	    }
	    else
	    {
	    	srfiltered = SR.getContacts();
	    }
	    
		if(srfiltered.size() == 0)
		{
			log.debug("Search returned 0 Contacts.");
			return;
		}
		
		log.debug("Found: " + srfiltered.size() + " Contacts");

		for(AddressbookContact AC: srfiltered) //Turning the Contact into a Map
		{
			//This is just a basic Example with some basic attributes.
			Map<String, Object> Contact = new HashMap<String, Object>();
			Contact.put("firstname", AC.getFirstname());
			Contact.put("familyname", AC.getFamilyname());
			
			for(Entry<String, DataEntry> Property : AC.getContactProperties().entrySet()) // The non Basic Fields are Stored as Properties, or Property Add ons
			{
				Contact.put(Property.getValue().getKey(), Property.getValue().getValue());
			}
			
			for(Entry<String, DataEntry> PropertyAdd : AC.getPropertyAddons().entrySet()) // The non Basic Fields are Stored as Properties, or Property Add ons
			{
				Contact.put(PropertyAdd.getValue().getKey(), PropertyAdd.getValue().getValue());
			}			
		}
		Success = true;
		
	}//END OF EXECUTION

		
	private List<List<Object>> GetACLforPerson(int STARFACE_ACCOUNT, Logger log) //Get all Contact UUID's that are owned by this STARFACE ACCOUNT from the DB
	{
		Connection DB_CONNECTION = GetSFConnection(log);
		
		//TODO Replace with PreparedStatement
		String Statement = "SELECT personid FROM personacl WHERE acaccountid="+STARFACE_ACCOUNT; //Create the Selection Statement
		Statement Query;
		try
		{
			Query= DB_CONNECTION.createStatement();
			Query.execute(Statement);
		
			List<List<Object>> QueryResult = new ArrayList<List<Object>>();
			ResultSet QuerySet = Query.getResultSet();
			ResultSetMetaData QueryMeta = QuerySet.getMetaData();
			
			while(QuerySet.next())
			{
		        List<Object> row = new ArrayList<Object>(QueryMeta.getColumnCount());
		        for (int i = 1; i <= QueryMeta.getColumnCount(); i++) {
		          row.add(QuerySet.getObject(i));
		        }
		        QueryResult.add(row);
			}
			
			return QueryResult;
			
		}
		catch(Exception e)
		{
			log.debug("Fehler beim Verarbeiten der ACLList!" + e.getMessage());
		}
		return null;
	}
	
	public Connection GetSFConnection(Logger log) //Return a Default Connection for the local Database
	{
		try 
		{
			Class.forName("org.postgresql.Driver");
			String DBURL = "jdbc:postgresql://127.0.0.1/asterisk";
			return DriverManager.getConnection(DBURL, "asterisk", "asterisk");
		} 
		catch (ClassNotFoundException e) 
		{
			log.debug("Fehler beim Abrufen der Starface Datenbankverbindung!");
			log.debug(e.getMessage());
		} 
		catch (SQLException e) 
		{
			log.debug("Fehler beim Abrufen der Starface Datenbankverbindung!");
			log.debug(e.getMessage());
		}
		return null;
	}
	
}
