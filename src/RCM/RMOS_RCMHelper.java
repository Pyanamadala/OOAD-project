package RCM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class RMOS_RCMHelper {
	protected static final double rcmWtLimit = 100.00;
	protected static final double rcmAmtLimit = 100.00;
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/ecore";
	static final String USERNAME = "root";
	static final String PASSWORD = "";
	private Connection con;
	private Statement stmt;
	int[] rcms = new int[20];
	HashMap<String, String> rcmDetails;
	String sql;
	ResultSet rs;

	public RMOS_RCMHelper() {

	}

	private void getConnection() {
		try {
			Class.forName(JDBC_DRIVER);
			this.con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			stmt = this.con.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public int[] getAllRCMs() {
		this.getConnection();
		try {
			sql = "select rcmid from rcmdetails";
			rs = stmt.executeQuery(sql);
			int i = 0;
			while (rs.next()) {
				rcms[i] = (rs.getInt("rcmid"));
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rcms;

	}

	public HashMap<String, String> getRcmDetails(int rcmid) {
		rcmDetails = new HashMap<String, String>();
		this.getConnection();
		sql = "select rcmCurWt,rcmCurAmt,rcmCurCoupon,timeLastEmpt from rcmdetails where rcmid =" + rcmid;
		try {
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				rcmDetails.put("Current Available Weight", rs.getString("rcmCurWt") + "lbs");
				rcmDetails.put("Current available Cash", "$" + rs.getString("rcmCurAmt"));
				rcmDetails.put("Current Available Coupons","$"+rs.getString("rcmCurCoupon"));
				rcmDetails.put("Time Last Emptied", rs.getString("timeLastEmpt"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rcmDetails;
	}

	public void addRCM(String location) {
		this.getConnection();
		sql = "insert into rcmdetails (rcmlocation,isDown,rcmWtLimit,rcmCurWt,rcmAmtLimit,rcmCurAmt,timeLastEmpt,isRCMFull,isAmtEmpty,Activation,rcmCouponLimit, rcmCurCoupon,isCouponEmpty) values ('"+location+"',0, 100,0,100, 100,now(), 0, 0, 0, 200, 200, 0)";
		try {
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void removeRCM(int rcmid) {
		this.getConnection();
		double curCapacity = 0, curBalance = 0;
		sql = "select rcmCurWt,rcmCurAmt from rcmdetails where rcmid=" + rcmid;
		try {
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				curCapacity = rs.getDouble("rcmCurWt");
				curBalance = rs.getDouble("rcmCurAmt");
			}
			rs.close();
			if (curCapacity == rcmWtLimit && curBalance == 0) {
				sql = "delete from rcmdetails where rcmid=" + rcmid;
				stmt.executeQuery(sql);
			} else {
				sql = "update rcmdetails set activation=0";
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
