import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * This stores Each Account information as an object 
 */
public class Account {

	private String accountName;
	private String accountUserName;
	private String accountPass;
	private String url;
	private int id;

	public Account(int id, String accountName, String accountUserName, String accountPass, String url) {

		this.id = id;

		this.accountName = accountName;

		this.accountUserName = accountUserName;

		this.accountPass = accountPass;

		this.url = url;

	}

	public Account() {

	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountUserName() {
		return accountUserName;
	}

	public void setAccountUserName(String accountUserName) {
		this.accountUserName = accountUserName;
	}

	public String getAccountPass() {
		return accountPass;
	}

	public void setAccountPass(String accountPass) {
		this.accountPass = accountPass;
	}

	public String getAccountURL() {
		return url;
	}

	public void setAccountURL(String url) {
		this.url = url;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

}
