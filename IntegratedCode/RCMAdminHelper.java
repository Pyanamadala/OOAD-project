package RCM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RCMAdminHelper {
	int rcmID;
	String username;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/ecore";
	static final String USERNAME = "root";
	static final String PASSWORD = "";
	private Connection con;
	private Statement stmt;
	String sql;
	ResultSet rs;
	/**
	 * @param username
	 */
	public RCMAdminHelper(int rcmID) {
		super();
		this.rcmID = rcmID;
	}
	boolean validateAdmin(String username,String password){
		return true;
	}
	private void getConnection() {
		try {
			Class.forName(JDBC_DRIVER);
			this.con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			stmt = this.con.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void deactivateRCM(String status){
		try {
			this.getConnection();
			if(status.equals("WEIGHT")){
				sql="update rcmdetails set activation=0,isRCMFull=1 where rcmid="+this.rcmID;
				stmt.executeUpdate(sql);
			}
			if(status.equals("AMOUNT")){
				sql="update rcmdetails set activation=0,isAmtEmpty=1 where rcmid="+this.rcmID;
				stmt.executeUpdate(sql);
			}
			if(status.equals("ISDOWN")){
				sql="update rcmdetails set activation=0,isDown=1 where rcmid="+this.rcmID;
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
