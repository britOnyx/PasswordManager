
/*import java.nio.charset.Charset;
import java.util.Random;*/
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;


public class PassHolderMain {

	private static Scanner scan;
	private static Singleton singleton;
	private static String key;
	private static Account acc;
	private static String[] listofKeys = new String[10];
	private static boolean done = false;
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		scan = new Scanner(System.in);
		singleton = Singleton.getInstance();
		String key;

		System.out.println(singleton.getList().size());
		
		if(singleton.getList().size() <= 0)
		{
			System.out.println("Enter a new Master Key (This is the Key to view all your passwords saved on the system)");
			System.out.println("NOTE:");
			System.out.println("Please Store the Master Password in a safe space!");
			System.out.println("To generate a new key, remove all accounts stored");
			key = scan.nextLine();
		}
		else
		{
			System.out.println("Enter your Master Key:");
			key = scan.nextLine();
			checkMastKey(key);
		}
		
		singleton.setMasterKey(key);
		
		menu();
					
	}
	
	private static void menu() throws ClassNotFoundException, SQLException
	{
		
		//1. Add
		//2. View
		//3. Delete
		//4. Edit
		//5. Pass Gen
		
		System.out.println("Password Holder");
		System.out.println("------------------------");
		System.out.println("Please select from 1-5");
		System.out.println("1. Add");
		System.out.println("2. View");
		System.out.println("3. Delete");
		System.out.println("4. Edit");
		
		String holder = scan.nextLine();
	
		System.out.println(holder);
	
		switch(holder)
		{
			case "1":
				addAccount();
				
				System.out.println("--------------");
				menu();
				break;
				
			case "2":
				viewAccounts();
				System.out.println("--------------");
				menu();
				break;
			case "3":
				deleteAccount();
				System.out.println("--------------");
				menu();
				break;
			case "4":
				editAccount();
				System.out.println("--------------");
				menu();
				break;
				
			default:
				System.out.println("Must enter a number between 1 - 5");
				menu();
				break;
		}
		
	}
	
	
	private static Boolean checkMastKey(String key) throws SQLException {
		// TODO Auto-generated method stub
		
		//first time opening app
		if(singleton.getOpened() == 0)
		{
			return true;
		}
		
		//get a stored password from DB
		Account account = singleton.getFirstAccountFromDB();
		
	
		//decrypt password using key provided
		try
		{
			//if it decrypts correctly
			singleton.setMasterKey(key);
			singleton.PasswordDecryption(account.getAccountPass());
			return true; //return true
			
		}catch( EncryptionOperationNotPossibleException e)
		{
			//if doesn't work, return false
			System.out.println("Incorrect Key");
			System.exit(0);
			return false;
		}
		
	}

	//add an account
	private static void addAccount() throws SQLException {
		
		int listPosn = singleton.getList().size()+1;
		int flag = 0;
	
		
		System.out.println("Add Account"); 
		Scanner usr_inp = new Scanner(System.in);

		System.out.println("Enter Web Name:");
		String accHolder = usr_inp.nextLine();

		System.out.println("Enter User Name:");
		String accUserName = usr_inp.nextLine();
		
		System.out.println("Enter Password:");
		String pass = usr_inp.nextLine();
		singleton.PasswordEncryption(pass);
		
		for(int i =0; i < singleton.getList().size(); i++)
		{
			if(singleton.getAccountFromList(i).getAccountName().equals(accHolder) && singleton.getAccountFromList(i).getAccountUserName().equals(accUserName))
			{
				System.out.println("Account Already Stored");
				return;
			}
		}
	
		if(!accHolder.equals("") && !accUserName.equals("") && !pass.equals(""))
		{
			Account acc = new Account(listPosn, accHolder,accUserName, singleton.getEncrypPass() , "null");
			singleton.addAccountToList(acc);		
		}
		else
		{
			if(accHolder.equals("") && accUserName.equals("") && pass.equals(""))
			{
				System.out.println("No information entered, did not add account!");
			}
			else if(accHolder.equals(""))
			{
				System.out.println("'Web Name' information was empty, did not add account!");
			}
			else if(accUserName.equals(""))
			{
				System.out.println("'User Name' information was empty, did not add account!");
			}else if(pass.equals(""))
			{
				System.out.println("'Password' information was empty, did not add account!");
			}
		}
				
	}
	
	//used to view accounts entered by the user
	private static void viewAccounts() throws NumberFormatException, SQLException, ClassNotFoundException {
								
		Scanner scan = new Scanner(System.in);
		
		checkAccountsListAvilability();
		
		System.out.println("Please Enter a number to view ");
		
		viewIndividualAccounts();
				
		int scan_inp = scan.nextInt();
		
				
		Account temp_acc = singleton.getAccountFromList(scan_inp-1);
		
		System.out.println("Account Name: " + temp_acc.getAccountName());
		System.out.println("Account User Name: " + temp_acc.getAccountUserName());
		System.out.println("Account Password: " + singleton.PasswordDecryption(temp_acc.getAccountPass()));
		
		waitUntilKey();
		
	}
	
	
	//used to wait for the user to press 'Enter' Key
	private static void waitUntilKey() {
		System.out.println("Press 'Enter' to continue...");
		try{
			System.in.read();
		} catch(Exception e){	e.printStackTrace();}
	}

	
	
	//delete account details
	private static void deleteAccount() throws NumberFormatException, SQLException, ClassNotFoundException {
	
		
		int duplicate=0;
		System.out.println("Delete Account");
		
		checkAccountsListAvilability();
		
		System.out.println("Select an Account to Delete..");
		
		viewIndividualAccounts();
		
		String scan_inp = scan.nextLine().toLowerCase();
		
		String accountName = singleton.getAccountFromList(Integer.parseInt(scan_inp)-1).getAccountName();
		String accountUsrName = singleton.getAccountFromList(Integer.parseInt(scan_inp)-1).getAccountUserName();
		System.out.println("Are you sure you want to delete the account " + accountName + "?");
		
		String scanYN = scan.nextLine().toLowerCase();
		
		if(scanYN.equals("y") || scanYN.equals("yes"))
		{
			singleton.deleteAccountFromList(Integer.parseInt(scan_inp)-1);
			singleton.deleteAccountFromDB(accountName,accountUsrName);
			System.out.println("Account Deleted!");			
		}
		else
		{
			System.out.println("Unsuccessfully Deleted Account!");
		}
		
		waitUntilKey();
				

	}
	
	//edit account details
	private static void editAccount() throws SQLException, ClassNotFoundException {
		
		System.out.println("--------------");
		
		System.out.println("Edit Account");
		
		checkAccountsListAvilability();
		
		System.out.println("Select an Account to Edit..");
				
		viewIndividualAccounts();
		
		String scan_inp = scan.nextLine().toLowerCase();
		
		Account acc = singleton.getAccountFromList(Integer.valueOf(scan_inp)-1);

		System.out.println("Enter Web Name:");
		System.out.println("Previous Name: " + acc.getAccountName());
		String accHolder = scan.nextLine();

		System.out.println("Enter User Name:");
		System.out.println("Previous User Name: " + acc.getAccountUserName());
		String accUserName = scan.nextLine();
		
		System.out.println("Enter Password:");
		String pass = scan.nextLine();
		singleton.PasswordEncryption(pass);
		
		singleton.editDataAndSaveToDB(accHolder, accUserName, singleton.getEncrypPass() , "null", acc.getAccountUserName(), acc.getAccountName());
		singleton.editAccountInList(Integer.valueOf(scan_inp)-1, accHolder, accUserName, singleton.getEncrypPass(), acc.getAccountURL());
		
		waitUntilKey();
	}

	private static void viewIndividualAccounts()
	{
		for(int i =0; i < singleton.getList().size(); i++)
		{
			System.out.println(i+1 +". " + singleton.getList().get(i).getAccountName());
		}
	}
	
	private static void checkAccountsListAvilability() throws ClassNotFoundException, SQLException
	{
		if(!singleton.getList().isEmpty())
		{
			System.out.println(singleton.getList().size() + " Accounts Stored...");
			
		}
		else
		{
			System.out.println("No Accounts Stored...");
			menu(); //return if no accounts have been found
		}
	}
	
	private static void test()
	{
		HashMap<String, Integer> duplicateAccounts = new HashMap<String, Integer>();
		ArrayList<String> temp = new ArrayList<>();
		
		for(int i =0, j =0; i < singleton.getList().size(); i++)
		{
			
			if(!singleton.getAccountFromList(i).getAccountName().equals(temp.get(j)))
			{
				temp.add(singleton.getAccountFromList(i).getAccountName());
				j++;
			}
			else
			{
				j=0;
			}
		}
	}

}

/*ArrayList<String> name = new ArrayList<String>();

String[][] duplicateArrays = new String[name.size()][2];


for(int i =0; i < singleton.getList().size(); i++)
{
	String temp_name = singleton.getList().get(i).getAccountName();
	
	for(int j =i +1; j < singleton.getList().size(); j++)
	{
		String strAcc = singleton.getList().get(j).getAccountName();
		if(!temp_name.equals(strAcc))
		{
			name.add(singleton.getList().get(i).getAccountName());
		}
	}
}


for(int i =0; i< name.size(); i++)
{
	System.out.println(i+1 +". " + name.get(i));
}

/*for(int i=0; i < singleton.getList().size(); i++)
{
	duplicateArrays[i][0] = singleton.getList().get(i).getAccountName();
	
	for(int j = i + 1; j < singleton.getList().size(); j++)
	{
		 if(singleton.getList().get(i).getAccountName().equals(name.get(j))){
			 
			 duplicate++;
			 duplicateArrays[i][1] = String.valueOf(duplicate);
		 }
		 
		 
	}
	duplicateArrays[i][1] = String.valueOf(duplicate);
	duplicate = 0;
}*/


/*for(int i =0; i < duplicateArrays.length; i++)
{
	System.out.println(i+1 +". " + duplicateArrays[i][0].toString());
}*/