package RCM;

import java.io.*;
import java.util.*;


class ItemsManager {
	RecyclableItemsHelper riHelper;
	HashMap<String,Double> rItems;
	
	public ItemsManager() {
		super();
		riHelper=new RecyclableItemsHelper();
	}
	double getPrice(String itemType){
		return (riHelper.getrecycledItems().get(itemType));
	}
	
	HashMap<String,Double> getRecyclableItems(){
		rItems=riHelper.getrecycledItems();
		return rItems;		
	}
	
}
class RecyclableItemsHelper {
	/**
	 * 
	 */
	HashMap<String,Double> recycledItems;
	/**
	 * @return 
	 * 
	 */
	public  RecyclableItemsHelper() {
		recycledItems=new HashMap<String,Double>();
	}


	/**
	 * @return the recyclableItems
	 */
	@SuppressWarnings("unchecked") 
	public HashMap<String, Double> getrecycledItems() {
		HashMap<String, Double>rItems=null;
		ObjectInputStream os = null;
			try {
				os=new ObjectInputStream(new FileInputStream("RecylableItems.ser"));
				rItems=(HashMap<String, Double>)os.readObject();
				os.close();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return rItems;
	}


	void addItems(String itemType, Double price){
		recycledItems=this.getrecycledItems();
		recycledItems.put(itemType, price);
		saveItems(recycledItems);
	}
	void saveItems(HashMap<String, Double> recyclableItems){
		ObjectOutputStream os = null;
		
		try {
			os=new ObjectOutputStream(new FileOutputStream("RecylableItems.ser"));
			os.writeObject(recycledItems);
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}