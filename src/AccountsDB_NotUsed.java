import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountsDB_NotUsed {

/*	private static Connection connect;
	private static boolean hasData = false;
	
	public AccountsDB() throws ClassNotFoundException, SQLException
	{
		if(connect == null)
		{
			getConnection();
		}
	}
	
	public ResultSet getUsers() throws ClassNotFoundException, SQLException
	{
		/*
		 * if(connect == null) { getConnection(); }
		 */
		
/*		Statement state = connect.createStatement();
		
		ResultSet rs = state.executeQuery("SELECT * FROM Accounts");
		
		return rs;
	}
	
	private void getConnection() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
		Class.forName("org.sqlite.JDBC");
		
		connect = DriverManager.getConnection("jdbc:sqlite:SQLiteAccounts.db");
		
		initialise();
	}

	private void initialise() throws SQLException {
		// TODO Auto-generated method stub
		
		//
		if(!hasData)
		{
			hasData = true;
			
			Statement state = connect.createStatement();
			
			try {
				ResultSet res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' and name='Accounts'");
				
				if(!res.next())
				{
					System.out.println("Building table");
					
					Statement createTable  = connect.createStatement();
					
					createTable.execute("CREATE TABLE Accounts("
							+ "accountID integer primary key autoincrement, "
							+ "accountName varchar(50),"
							+ "accountUserName varchar(50),"
							+ "accountpass varchar(100),"
							+ "accountURL varchar(90));");
					
					/*
					 * PreparedStatement prep =
					 * connect.prepareStatement("INSERT INTO accounts values(?,?,?,?,?);");
					 * 
					 * prep.setString(2, "testName"); prep.setString(3, "testUserName");
					 * prep.setString(4, "pass"); prep.setString(5, "URLLINK"); prep.execute();
					 */
					
/*				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
}
