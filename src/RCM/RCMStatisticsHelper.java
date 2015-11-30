package RCM;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class RCMStatisticsHelper {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/ecore";
	static final String USERNAME = "root";
	static final String PASSWORD = "";
	private Connection con;
	private Statement stmt;
	HashMap<Integer,Integer> noOfTimesLastEmptied;
	HashMap<Integer,Double> totalWtRecycled;
	HashMap<Integer,Double>totalAmtVended;
	HashMap<String,Double>wtByType;
	ItemsManager items;
	String sql;
	ResultSet rs;
	
	
public RCMStatisticsHelper(){
	 noOfTimesLastEmptied= new HashMap<Integer,Integer>();
	 totalWtRecycled= new HashMap<Integer,Double>();
	 totalAmtVended=new HashMap<Integer,Double>();
	 wtByType= new HashMap<String,Double>();
	 items= new ItemsManager();
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
	public int noOfRcms(){
		this.getConnection();
		
		int num=0;
		try {
			sql="select count(distinct rcmid) from rmositems";
			rs=stmt.executeQuery(sql);
			rs.next();
			num= rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	public HashMap<Integer,Integer> getNoOfTimesLastEmptied(String fromDate,String toDate){
		this.getConnection();
		int noOfTimesEmptied=0;
		for (int i=0;i<noOfRcms();i++){
		sql="select count(distinct timelastemptied) from rmositems where rcmid="+(i+1)+" and timelastemptied >='"+fromDate+"00:00:00'and timelastemptied <'"+toDate+"23:59:59'"; 
		try {
			rs=stmt.executeQuery(sql);
			rs.next();
			noOfTimesEmptied=rs.getInt(1);
			noOfTimesLastEmptied.put(i+1,noOfTimesEmptied);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}
		return  noOfTimesLastEmptied;
	}
	public HashMap <Integer,Double>getRcmTotalWt(String fromDate,String toDate){
		this.getConnection();
		double totWt;
		for(int i=0;i<noOfRcms();i++){
			sql="select sum(itemweight) from rmositems where rcmid="+(i+1)+" and timelastemptied >='"+fromDate+"00:00:00'and timelastemptied <'"+toDate+"23:59:59'";
			try {
				rs=stmt.executeQuery(sql);
				rs.next();
				totWt=rs.getDouble(1);
				totalWtRecycled.put(i+1,totWt);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalWtRecycled;
	}
	public HashMap<Integer,Double> getAmtVended(String fromDate,String toDate){
		this.getConnection();
		double totAmt;
		for(int i=0;i<noOfRcms();i++){
			sql="select sum(itemamount) from rmositems where rcmid="+(i+1)+" and timelastemptied >='"+fromDate+"00:00:00'and timelastemptied <'"+toDate+"23:59:59'";
			try {
				rs=stmt.executeQuery(sql);
				rs.next();
				totAmt=rs.getDouble(1);
				totalAmtVended.put(i+1,totAmt);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalAmtVended;
	}
	public HashMap<String,Double> getWtByItemType(int rcmid,String fromDate,String toDate){
		this.getConnection();
		double itemWt;
		for(String key:items.getRecyclableItems().keySet()){ 
			System.out.println(items.getRecyclableItems());
			sql="select sum(itemweight) from rmositems where rcmid="+rcmid+" and itemtype='"+key.toUpperCase()+"' and timelastemptied >='"+fromDate+"00:00:00'and timelastemptied <'"+toDate+"23:59:59' ";
			try {
				rs=stmt.executeQuery(sql);
				rs.next();
				itemWt=rs.getDouble(1);
				wtByType.put(key, itemWt);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return wtByType;
	}
	public static void main(String[] args){
		RCMStatisticsHelper hp= new  RCMStatisticsHelper();
	System.out.println(hp.getWtByItemType(1,"2015-11-23","2015-11-26"));
	}
/**insert into rmositems (rcmid,itemtype,itemweight,itemamount,timelastemptied) values (4,'ALUMINIUM',21.97,21.97,now());
insert into rmositems (rcmid,itemtype,itemweight,itemamount,timelastemptied) values (1,'PAPER',30.15,15.07,'2015-11-26 02:15:25');
insert into rmositems (rcmid,itemtype,itemweight,itemamount,timelastemptied) values (1,'PLASTIC',25.81,19.375,'2015-11-26 02:15:25');
insert into rmositems (rcmid,itemtype,itemweight,itemamount,timelastemptied) values (1,'GLASS',20.33,57.63,'2015-11-26 02:15:25');*/
}
