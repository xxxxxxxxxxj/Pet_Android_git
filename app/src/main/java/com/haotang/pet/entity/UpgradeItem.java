package com.haotang.pet.entity;

import org.json.JSONObject;

import com.haotang.pet.util.Utils;

public class UpgradeItem {
	public int id;
	public String name;
	public double price;
	public double listprice;
	public int needtime;
	public static UpgradeItem json2Entity(JSONObject json){
		UpgradeItem item = new UpgradeItem();
		try {
			if(json.has("id")&&!json.isNull("id")){
				item.id = json.getInt("id");
			}
			if(json.has("duration")&&!json.isNull("duration")){
				item.needtime = json.getInt("duration");
			}
			if(json.has("name")&&!json.isNull("name")){
				item.name = json.getString("name");
			}
			if(json.has("price")&&!json.isNull("price")){
				item.price = Utils.formatDouble(json.getDouble("price"), 2);
			}
			if(json.has("listPrice")&&!json.isNull("listPrice")){
				item.listprice = Utils.formatDouble(json.getDouble("listPrice"), 2);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return item;
	}
}
