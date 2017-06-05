package com.haotang.pet.entity;

import org.json.JSONObject;

public class Addr {
	public int id=0;
	public double lat;
	public double lng;
	public String detail;
	public static Addr json2Entity(JSONObject json){
		Addr addr = new Addr();
		try{
			if(json.has("Customer_AddressId")&&!json.isNull("Customer_AddressId")){
				addr.id = json.getInt("Customer_AddressId");
			}
			if(json.has("lat")&&!json.isNull("lat")){
				addr.lat = json.getDouble("lat");
			}
			if(json.has("lng")&&!json.isNull("lng")){
				addr.lng = json.getDouble("lng");
			}
			if(json.has("address")&&!json.isNull("address")){
				addr.detail = json.getString("address");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return addr;
	}
}
