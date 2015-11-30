package RCM;

import java.util.*;

public class UserSession {
	int rcmid;
	private double totalAmountForItems = 0, totalWtForItems = 0;
	private int itemCount = 0;
	private ItemsManager imanage;
	private final String rejectItemType;
	Map<String, List<Double>> sessItems;

//	class SessionItems {
//		String type;
//		String amountMode;
//		double weight;
//		double cumWt;
//		double itemAmount;
//		double cumAmount;
//
//		SessionItems(String type, String amountMode, double weight,double cumWt,
//				double itemAmount, double cumAmount) {
//			this.type = type;
//			this.weight = weight;
//			this.itemAmount = itemAmount;
//			this.cumAmount = cumAmount;
//			this.cumWt = cumWt;
//			this.amountMode = amountMode;
//		}
//	}

	public UserSession() {
		rejectItemType = null;

	}

	/**
	 * @param rcmID
	 */
	public UserSession(int rcmID) {
		super();
		this.rcmid = rcmID;
		imanage = new ItemsManager();
		rejectItemType = randomizeRejectItemType();
		sessItems = new HashMap<String, List<Double>>();
	}

	private String randomizeRejectItemType() {
		Random r = new Random();
		List<String> itemKeys = new ArrayList<String>(imanage
				.getRecyclableItems().keySet());
		String randomItem = itemKeys.get(r.nextInt(itemKeys.size()));
		return randomItem;

	}

	private boolean isAccepted(String itemType) {
		if (rejectItemType.equals(null))
			randomizeRejectItemType();
		if (itemType.equals(rejectItemType))
			return false;
		else
			return true;
	}

	double calculateItemAmount(String itemType, double itemWeight) {
		double itemAmount;
		if (isAccepted(itemType)) {
			itemAmount = itemWeight * (imanage.getPrice(itemType));
			return itemAmount;
		} else
			return 0.0;
	}

	/**
	 * @return the totalAmountForItems
	 */
	public double getTotalAmountForItems() {
		return totalAmountForItems;
	}

	/**
	 * @return the totalWtForItems
	 */
	public double getTotalWtForItems() {
		return totalWtForItems;
	}
	double getCumulativeAmount() {
		return totalAmountForItems;
	}

	double getCumulativeAmount(double itemAmount) {
		return totalAmountForItems + itemAmount;
	}

	double getCumulativeWeight(double itemWeight) {
		return totalWtForItems + itemWeight;
	}

	public void endSession() {
		this.rcmid =0;
		imanage = null;
		sessItems =null;
		this.itemCount=0;
		totalAmountForItems = 0;
		totalWtForItems = 0;
	}

	public double addItemToSession(String itemType, String amountMode,
			double itemWeight, double itemAmount) {
		// TODO Auto-generated method stub
		List<Double> item;
		itemCount++;
		if (amountMode.equals("CASH"))
			totalAmountForItems += itemAmount;
		totalWtForItems += itemWeight;
		if(sessItems.containsKey(itemType)){
			item=sessItems.get(itemType);
			item.set(0, item.get(0)+itemWeight);
			item.set(1, item.get(1)+itemAmount);
			sessItems.put(itemType,item);
		}
		else{
			item=new ArrayList<Double>();	
			item.add(itemWeight);
			item.add(itemAmount);			
			sessItems.put(itemType,item);
		}
		// populate it
//		sessItems.put(itemCount, new SessionItems(itemType, amountMode,
//				itemWeight, totalWtForItems, itemAmount, totalAmountForItems));
		return totalAmountForItems;
	}

	public Map<String,List<Double>> getSessionItems() {
		return sessItems;
	}

}
