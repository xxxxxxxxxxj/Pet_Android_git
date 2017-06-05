package com.haotang.pet.entity;

import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Utils;

public class AddServiceItem {
	public int id;
	public String name;
	public double price;
	public boolean isChecked;
	
	public static AddServiceItem json2Entity(JSONObject json){
		AddServiceItem item = new AddServiceItem();
		try{
			if(json.has("ExtraServiceItemId")&&!json.isNull("ExtraServiceItemId")){
				item.id = json.getInt("ExtraServiceItemId");
			}
			
			if(json.has("name")&&!json.isNull("name")){
				item.name = json.getString("name");
			}
			
			if(json.has("price")&&!json.isNull("price")){
				item.price = Utils.formatDouble(json.getDouble("price"), 2);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return item;
	}
	
}
