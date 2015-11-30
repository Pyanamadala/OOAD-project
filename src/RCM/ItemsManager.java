package RCM;
import java.io.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


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
				e.printStackTrace();
			}
		
		return rItems;
	}

	void addItems(String itemType, Double price){
		recycledItems=this.getrecycledItems();
		recycledItems.put(itemType, price);
		saveItems(recycledItems);
	}
	void deleteItems(String itemType){
		recycledItems=this.getrecycledItems();
		recycledItems.remove(itemType);
		saveItems(recycledItems);
	}
	void changePrice(String itemType,Double price){
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
			e.printStackTrace();
		}
		
	}
	public static void main (String[] args){
		RecyclableItemsHelper rih = new RecyclableItemsHelper();
		System.out.println(rih.getrecycledItems());
		rih.addItems("cardboard",1.45);
		System.out.println(rih.getrecycledItems());
		rih.deleteItems("cardboard");
		System.out.println(rih.getrecycledItems());
	}
	
	
	
}
