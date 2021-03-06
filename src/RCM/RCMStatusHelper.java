package RCM;

import java.sql.*;
import java.util.*;

public class RCMStatusHelper {
	int rcmID;

	private ObservableDelegate delegate;
	/**
	 * @param rcmID
	 */
	public RCMStatusHelper(int rcmID) {
		super();
		this.rcmID = rcmID;
		delegate = new ObservableDelegate();
	}

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/ecore";
	static final String USERNAME = "root";
	static final String PASSWORD = "";
	private static final int RCMCurWt = 0;
	private Connection con;
	private Statement stmt;
	String sql;
	ResultSet rs;

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

	boolean checkRCMNotFull(int rcmid, double itemWeight) {
		double rcmWtLimit = 0, rcmCurWt = 0;
		this.getConnection();
		try {
			sql = "select rcmWtLimit, rcmCurWt from rcmdetails where rcmid="
					+ rcmid;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				rcmWtLimit = rs.getDouble("rcmWtLimit");
				rcmCurWt = rs.getDouble("rcmCurWt");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (rcmWtLimit >= (rcmCurWt + itemWeight))
			return true;
		else
			return false;
	}

	String checkAmountNotEmpty(int rcmID, double itemAmount) {
		double rcmCurAmt = 0;
		this.getConnection();
		try {
			sql = "select rcmCurAmt from rcmdetails where rcmid=" + rcmID;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				rcmCurAmt = rs.getDouble("rcmCurAmt");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (0 < (rcmCurAmt - itemAmount))
			return "CASH";
		else
			return "COUPON";
	}
	String checkCouponNotEmpty(int rcmID, double itemAmount) {
		double rcmCurCoupon = 0;
		this.getConnection();
		try {
			sql = "select rcmCurCoupon from rcmdetails where rcmid=" + rcmID;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				rcmCurCoupon = rs.getDouble("rcmCurCoupon");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (0 < (rcmCurCoupon - itemAmount))
			return "COUPON";
		else
			return String.valueOf(rcmCurCoupon);
	}
	void updateRCMStatus(String amountMode, Map<String, List<Double>> status,
			double cumAmt, double cumWt) {
		String key;
		List<Double> sItems;

		this.getConnection();
		for (Map.Entry<String, List<Double>> entry : status.entrySet()) {
			key = entry.getKey();
			sItems = entry.getValue();
			System.out.println(key);
			System.out.println(sItems.get(0));
			System.out.println(sItems.get(1));
			updRcmItems(key, sItems);
		}
		System.out.println("cumm amount:" + cumAmt);
		System.out.println("cum weight:" + cumWt);
		updCumDet(amountMode, cumAmt, cumWt);
	}

	void updCumDet(String amountMode, double cumAmount, double cumWt) {
		double rcmCurAmt = 0, rcmCurWt = 0,rcmWtLimit=0,rcmCurCoupon=0;
		try {
			this.getConnection();
			sql = "update rcmdetails set rcmCurWt=rcmCurWt+" + cumWt
					+ " where rcmid=" + rcmID;
			stmt.executeUpdate(sql);
			if (amountMode.equals("CASH")) {
				sql = "update rcmdetails set rcmCurAmt=rcmCurAmt-" + cumAmount
						+ " where rcmid=" + rcmID;
			}
			else if(amountMode.equals("COUPON")){
				sql = "update rcmdetails set rcmCurCoupon=rcmCurCoupon-" + cumAmount
						+ " where rcmid=" + rcmID;
			}
					
			stmt.executeUpdate(sql);
			sql = "select rcmCurAmt,rcmCurWt,rcmWtLimit,rcmCurCoupon from rcmdetails where rcmid="
					+ rcmID;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				rcmCurAmt = rs.getDouble("rcmCurAmt");
				rcmCurWt = rs.getDouble("rcmCurWt");
				rcmCurCoupon=rs.getDouble("rcmCurCoupon");
				rcmWtLimit=rs.getDouble("rcmWtLimit");
			}
			if(amountMode.equals("CASH")){
				if (rcmCurAmt < 5) {
					delegate.setChanged();
					delegate.notifyObservers(new String("AMOUNT"));
				}
			}else  
				if (rcmCurCoupon < 5) {
					delegate.setChanged();
					delegate.notifyObservers(new String("COUPON"));
				}
			if((rcmCurAmt < 5)&&(rcmCurCoupon < 5)){
				delegate.setChanged();
				delegate.notifyObservers(new String("DEACTIVATE"));
			}
			if (rcmWtLimit<rcmCurWt) {
				delegate.setChanged();
				delegate.notifyObservers(new String("WEIGHT"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void updRcmItems(String key, List<Double> sItems) {
		try {
			this.getConnection();
			sql = "insert into rcmitemdetails(rcmid,itemtype,itemweight,transactiontime,totamtbyitem)values ("
					+ this.rcmID
					+ ",'"
					+ key
					+ "',"
					+ sItems.get(0)
					+ ",now()," + sItems.get(1) + ")";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Observable getObservable() {
		return delegate;
	}

	public void addObserver(Observer ob) {
		delegate.addObserver(ob);
	}

	public boolean checkActivated(int rcmid) {
		// TODO Auto-generated method stub
		int status = 0;
		try {
			this.getConnection();
			sql = "select Activation from rcmdetails where rcmid=" + rcmid;
			rs = stmt.executeQuery(sql);
			if(rs.next())
				status=rs.getInt("Activation");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(status==0)
			return false;
		else
			return true;
	}
}

class ObservableDelegate extends java.util.Observable {

	/*
	 * Open up the access permissions of these methods Defined as protected
	 * methods in super class
	 */

	public void clearChanged() {
		super.clearChanged();
	}

	public void setChanged() {
		super.setChanged();
	}

}