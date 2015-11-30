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
	double getCumulativeAmount(){
		return usess.getCumulativeAmount();
	}
	
	HashMap<String,Double> displayItems(){
		usess=new UserSession(rcmID);
		return imanage.getRecyclableItems();
	}
	double convertToMetric(double itemWeight){
		return (itemWeight*0.453592);
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
					else if (status.equals("COUPONS"))
						itemstatus= "NO_CASH";
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
	double addItem(String amountMode, String itemType, double itemWeight,double itemAmount){
		return usess.addItemToSession(itemType,amountMode, itemWeight, itemAmount);
	}
	void endSession(String amountMode){
		rsh.updateRCMStatus(amountMode,usess.getSessionItems(),usess.getTotalAmountForItems(),usess.getTotalWtForItems());
		System.out.println(usess.getTotalAmountForItems());
		usess.endSession();
	}
}