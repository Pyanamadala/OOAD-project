package RCM;
import java.util.HashMap;

class RCM {
	private static int rcmID;
	UserSession usess;
	ItemsManager imanage;
	RCMStatusHelper rsh;

	RCMAdmin rcmAdmin;
	public int getRcmID() {
		return rcmID;
	}

	public RCM(int rcmID) {
		super();
		RCM.rcmID = rcmID;
		imanage=new ItemsManager();
		rsh=new RCMStatusHelper(rcmID);
		rcmAdmin=new RCMAdmin(1,rsh);
	}
	boolean checkRCMActivated(){
		return rsh.checkActivated(RCM.rcmID);
	}
	double getCumulativeAmount(){
		return usess.getCumulativeAmount();
	}
	void recycle(){
		usess=new UserSession(rcmID);
	}
	HashMap<String,Double> displayItems(){
		return imanage.getRecyclableItems();
	}
	double convertToMetric(double itemWeight){
		return (itemWeight*0.453592);
	}
	double getCumulativeWeight(){
		return usess.getTotalWtForItems();
	}
	
	String isItemAccepted(String itemType,double itemWeight){
		double itemAmount;
		String status,itemstatus = null;
		itemAmount=usess.calculateItemAmount(itemType, itemWeight);
		if(itemAmount!=0.0)
			if(rsh.checkRCMNotFull(RCM.rcmID, usess.getCumulativeWeight(itemWeight))){
				status=rsh.checkAmountNotEmpty(rcmID,usess.getCumulativeAmount(itemAmount));
					if(status.equals("CASH")){
						//addItem(itemType,itemWeight,itemAmount);
						itemstatus= String.valueOf(itemAmount);
					}						
					else if (status.equals("COUPON")){
						status=rsh.checkCouponNotEmpty(rcmID,usess.getCumulativeAmount(itemAmount));
						if(status.equals("COUPON"))
							itemstatus= "NO_CASH";
						else
							itemstatus=status;
					}
			}
			else
				itemstatus="FULL";
		else
			itemstatus="INVALID";	
		return itemstatus;		
	}
	double calculateAmount(String itemType,double itemWeight){
		return usess.calculateItemAmount(itemType, itemWeight);
	}
	double addItem(String itemType, double itemWeight,double itemAmount){
		return usess.addItemToSession(itemType, itemWeight, itemAmount);
	}
	void endSession(String amountMode){
		rsh.updateRCMStatus(amountMode,usess.getSessionItems(),usess.getTotalAmountForItems(),usess.getTotalWtForItems());
		System.out.println(usess.getTotalAmountForItems());
		usess.endSession();
		this.usess=null;
	}
}