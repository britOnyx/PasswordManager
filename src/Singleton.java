import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Singleton {

    private static Singleton instance = null;
    private String encryptpassword = null;
    private StandardPBEStringEncryptor encryptor = null;
    private StandardPBEStringEncryptor decryptor = null;
    private Account acc;
    private static String key;
    
	private static Connection connect;
	private static DatabaseMetaData dbm;
	
	private int opened = 0; //this checks if the program has been ran already
    
    private ArrayList<Account> listofAccounts;
    
    private Statement statement;
    
    private Singleton() throws ClassNotFoundException, SQLException {
    	
    	if(encryptor == null)
    	{
    		encryptor = new StandardPBEStringEncryptor();
    	}
    	
    	if(decryptor == null)
    	{
    		decryptor = new StandardPBEStringEncryptor();
    	}
    	
    	
    	if(listofAccounts == null)
    	{
    		listofAccounts = new ArrayList<Account>();
    	}
    	
		if (connect == null) {
			getConnect();
		}
    	
    }

    
    public static Singleton getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
    
    private void getConnect() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		Class.forName("org.sqlite.JDBC");

		connect = DriverManager.getConnection("jdbc:sqlite:acc.db");

		initialise();
	}
    

	private void initialise() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		statement = connect.createStatement();
		dbm = connect.getMetaData();
		
		try {
			
			/*ResultSet res = createTable
					.executeQuery("SELECT * FROM accounts");*/
			
			ResultSet table = dbm.getTables(null, null, "accounts", null);

			// table doesnt exists
			if (!table.next()) {

				System.out.println("Building table");

				

				statement.execute("CREATE TABLE accounts(" + "accountID integer primary key, " // 
						+ "accountName varchar(50) NOT NULL," + "accountUserName varchar(50) NOT NULL," + "accountpass varchar(100) NOT NULL,"
						+ "accountURL varchar(90) NOT NULL);");

				/*
				 * PreparedStatement prep =
				 * connect.prepareStatement("INSERT INTO accounts values(?,?,?,?,?);");
				 * 
				 * prep.setInt(1, 1); prep.setString(2, "testName"); prep.setString(3,
				 * "testUserName"); prep.setString(4, "pass"); prep.setString(5, "URLLINK");
				 * prep.execute();
				 */

			} else {
				ResultSet res = statement
						.executeQuery("SELECT * FROM accounts");
								
				int size = 0;
				
				//if theres already data stored
				if(res.next())
				{
					size++; //check how much data is stored
				}
				
				if(size > 0)
				{
					setOpened(1); // the application has now been classed as open
					saveToList();
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void saveToList() throws SQLException {
		// TODO Auto-generated method stub

		//createTable = connect.createStatement();

		ResultSet rs = statement.executeQuery("SELECT * FROM accounts");

		Account account;

		while (rs.next()) {
			int id = rs.getInt("accountID");
			String accName = rs.getString("accountName");
			String accountUserName = rs.getString("accountUserName");
			String accountPass = rs.getString("accountpass");
			String url = rs.getString("accountURL");

			account = new Account(id, accName, accountUserName, accountPass, url);

			listofAccounts.add(account);

			
		}
		
		System.out.println("Accounts Imported!");
	}
	
    public void PasswordEncryption(String password)
    {
    	if(!encryptor.isInitialized())
    	{
    		encryptor.setPassword(key);
    	}
    	
        String encryptedPassword = encryptor.encrypt(password);
        encryptpassword = encryptedPassword;
        
    }
    
    public String PasswordDecryption(String encPass)
    {
    	if(!decryptor.isInitialized())
    	{
		 decryptor.setPassword(key);
    	}
    	    	
		return ("Decryption Password: " + decryptor.decrypt(encPass)); 
    }
    
    
    public String getMasterKey()
    {
    	return key;
    }
    
    
    public String getEncrypPass() {
    	return encryptpassword;
    }
    
    public void setEncrypPass(String val) {
    	encryptpassword = val;
    }
    
    
    public void setMasterKey(String masterKey)
    {
    	key = masterKey;
    }
    
    //generate keys to reset password
   /* private void setKey()
    {

    		int leftLimit = 97; // letter 'a'
    	    int rightLimit = 122; // letter 'z'
    	    int targetStringLength = 10; //key length
    	    Random random = new Random();
    	 
    	    String generatedString = random.ints(leftLimit, rightLimit + 1)
    	      .limit(targetStringLength)
    	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append) 
    	      .toString(); //append the characters into a string
    	 
    	    key = generatedString;
    	    

    }*/
    
    public Account getAccount()
    {
    	return acc;
    }
    
    public void setAccount(Account account)
    {
    	acc = account;
    }
    
    public ArrayList<Account> getList()
    {
    	return listofAccounts;
    }
    
    public void setList(ArrayList<Account> tempList)
    {
    	listofAccounts = tempList;
    }
    
    public int getOpened()
    {
    	return opened;
    }
    
    public void setOpened(int val)
    {
    	opened = val;
    }
        
    
    public Account getAccountFromList(int id)
    {
    	return listofAccounts.get(id);
    }
    
    public Account getFirstAccountFromDB() throws SQLException
    {

    	ResultSet rs = statement.executeQuery("SELECT * FROM accounts;");
    	Account acc = null;
    	
    	acc = new Account(rs.getInt("accountID"), rs.getString("accountName"), rs.getString("accountUserName"), rs.getString("accountpass"), rs.getString("accountURL"));
    	
    	return acc;
    }
    
    public void addAccountToList(Account acc) throws SQLException
    {

    	listofAccounts.add(acc);
    	
    	
    	saveDataToDB(acc.getID(), acc.getAccountName(), acc.getAccountUserName(), acc.getAccountPass(), acc.getAccountURL());
    	
    }
    
    public void deleteAccountFromList(int id) throws SQLException
    {

    	listofAccounts.remove(id);
    	
    }
    
    public void deleteAccountFromDB(String name, String userName) throws SQLException
    {
    	//PreparedStatement prep = connect.prepareStatement("DELETE FROM accounts WHERE accountID = ?;");

    	PreparedStatement prep = connect.prepareStatement("DELETE FROM accounts WHERE accountName = '" + name +"' AND accountUserName = '" + userName + "';");
    	
		prep.execute();
		
		System.out.println("Account Deleted Successfully");
    	
    	
    	
    	/*int size = 0;
    	
    	ArrayList<Account> acc= new ArrayList<>();
    	
    	
    	while(rs.next())
    	{
    		size++;
    		acc.add(new Account(rs.getInt("accountID"), rs.getString("accountName"), rs.getString("accountUserName"), rs.getString("accountpass"), rs.getString("accountURL")));
    	}
    	
    	System.out.println(size);
    	
    	if(size == 1 )
    	{
    		
    		System.out.println(acc.get(0).getID() + ", " + acc.get(0).getAccountName() + ", " + acc.get(0).getAccountUserName());
    		PreparedStatement prep = connect.prepareStatement("DELETE FROM accounts WHERE accountName = '" + name + "';");  		

    		prep.execute();
    		
    		System.out.println("Account Deleted Successfully");
    	}*/

    }
    
	public void saveDataToDB(int id, String accountName, String accountUserName, String accountPass, String url)
			throws SQLException {
		PreparedStatement prep = connect.prepareStatement("INSERT INTO accounts values(?,?,?,?,?);");
		
		/*prep.setInt(1, id);*/
		prep.setString(2, accountName);
		prep.setString(3, accountUserName);
		prep.setString(4, accountPass);
		prep.setString(5, url);

		prep.execute();
		
		System.out.println("Account Added Successfully");
	}
	
	//clear decryptor
	public void clearDecryptor()
	{
		decryptor = null;
	}
	
	
	public void editDataAndSaveToDB(String accountName, String accountUserName, String accountPass, String url, String oldUserName, String oldaccountName)
			throws SQLException {
		
		PreparedStatement prep = connect.prepareStatement("UPDATE accounts SET accountName='" + accountName+ "', accountUserName='" + accountUserName + "', accountPass='" + accountPass + "', accountURL='"+ url + "' WHERE accountName ='" + oldaccountName + "' AND accountUserName='" +oldUserName + "';");
		
		prep.execute();
		
		System.out.println("Account Edited Successfully");
	}
	
	public void editAccountInList(int tid, String accountName, String accountUserName, String accountPass, String url)
	{
		//edit information
		listofAccounts.get(tid).setAccountName(accountName);
		listofAccounts.get(tid).setAccountUserName(accountUserName);
		listofAccounts.get(tid).setAccountPass(accountPass);
		listofAccounts.get(tid).setAccountURL(url);
	}
	

}
