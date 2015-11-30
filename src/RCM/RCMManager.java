package RCM;

import java.util.HashMap;

public class RCMManager {
RMOS_RCMHelper helper=new RMOS_RCMHelper();

	public RCMManager() {
		
	}
	public void addRCM(String location){
		helper.addRCM(location);
	}
	public int[] getAllRCMs(){
		return helper.getAllRCMs();
		
	}
	public HashMap<String,String> getRcmDetails(int rcmid){
		return helper.getRcmDetails(rcmid);
	}
	public void removeRCM(int rcmid){
		helper.removeRCM(rcmid);
	}
	
}