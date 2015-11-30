package RCM;

import java.util.HashMap;

public class RCMUsageStatistics {
	RCMStatisticsHelper statHelper;
	public RCMUsageStatistics(){
		statHelper= new RCMStatisticsHelper(); 
	}
public HashMap<Integer,Integer> getNoOfTimesLastEmptied(String fromDate,String toDate){
	return statHelper.getNoOfTimesLastEmptied(fromDate,toDate);
}

public HashMap<Integer,Double> getAmtVended(String fromDate,String toDate){
	return statHelper.getAmtVended(fromDate, toDate);
}
public HashMap <Integer,Double>getRcmTotalWt(String fromDate,String toDate){
	return statHelper.getRcmTotalWt(fromDate, toDate);
}
public HashMap<String,Double> getWtByItemType(int rcmid,String fromDate,String toDate){
	return statHelper.getWtByItemType(rcmid,fromDate,toDate);
}

}
