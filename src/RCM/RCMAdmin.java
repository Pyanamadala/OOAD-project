package RCM;

import java.util.*;

public class RCMAdmin implements Observer {
	int rcmID;
	String username;
	RCMAdminHelper rAdminHelper;
	RCMStatusHelper rStatusHelper;
	boolean rcmIsFull,rcmNoAmt;
	/**
	 * @return the rcmID
	 */
	public int getRcmID() {
		return rcmID;
	}
	/**
	 * @param rcmID the rcmID to set
	 */
	public void setRcmID(int rcmID) {
		this.rcmID = rcmID;
	}
	/**
	 * @param rcmID
	 * @param username
	 * @param password
	 */
	public RCMAdmin(int rcmID,RCMStatusHelper rStatusHelper) {
		super();
		this.rcmID = rcmID;
		rAdminHelper=new RCMAdminHelper(rcmID);
		rStatusHelper.addObserver(this);
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	boolean validateAdmin(String password){
		return rAdminHelper.validateAdmin(this.username, password);
	}
	@Override
	public void update(Observable ob, Object updEntity) {
		// TODO Auto-generated method stub
		String updEntity1=updEntity.toString().toUpperCase();
//		if(updEntity1.equals("WEIGHT")){
//			rcmIsFull=true;
//			rAdminHelper.deactivateRCM("FULL");
//		}
//		if(updEntity1.equals("AMOUNT")){
//			rcmNoAmt=true;
//			rAdminHelper.deactivateRCM("AMOUNT");
//		}
		rAdminHelper.deactivateRCM(updEntity1);
	}
}