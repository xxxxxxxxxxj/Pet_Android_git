package com.haotang.pet.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class CommAddr {

	public String address;
	public int Customer_AddressId;
	public double lat;
	public double lng;
	public int userId;
	public boolean isSelected = false;//是否选中
	
	public static CommAddr json2Entity(JSONObject json){
		CommAddr addr = new CommAddr();
		try {
			if (json.has("address")&&!json.isNull("address")) {
				addr.address = json.getString("address");
			}
			if (json.has("Customer_AddressId")&&!json.isNull("Customer_AddressId")) {
				addr.Customer_AddressId = json.getInt("Customer_AddressId");
			}
			if (json.has("lat")&&!json.isNull("lat")) {
				addr.lat = json.getDouble("lat");
			}
			if (json.has("lng")&&!json.isNull("lng")) {
				addr.lng = json.getDouble("lng");
			}
			if (json.has("userId")&&!json.isNull("userId")) {
				addr.userId = json.getInt("userId");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addr;
		
	}
}
