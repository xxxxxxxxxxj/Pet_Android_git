package com.haotang.pet.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Coupon {
	public int id;
	public String content;
	public String starttime;
	public String endtime;
	public String amount;
	public String name;
	public int isGive;
	public boolean isChoose = false;
	
	public static Coupon jsonToEntity(JSONObject json){
		Coupon coupon = new Coupon();
		try {
			if(json.has("isGive")&&!json.isNull("isGive")){
				coupon.isGive = json.getInt("isGive");
			}
			if(json.has("CouponId")&&!json.isNull("CouponId")){
				coupon.id = json.getInt("CouponId");
			}
			if(json.has("description")&&!json.isNull("description")){
				coupon.content = json.getString("description");
			}
			if(json.has("startTime")&&!json.isNull("startTime")){
				coupon.starttime = json.getString("startTime");
			}
			if(json.has("endTime")&&!json.isNull("endTime")){
				coupon.endtime = json.getString("endTime");
			}
			if(json.has("amount")&&!json.isNull("amount")){
				coupon.amount = json.getString("amount");
			}
			if (json.has("name")&&!json.isNull("name")) {
				coupon.name = json.getString("name");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return coupon;
	}
}
