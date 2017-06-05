package com.haotang.pet.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;


public class MyCouponCanUse implements Serializable{

	public int amount;
	public String description;
	public String endTime;
	public int CouponId;
	public String orderId;
	public String startTime;
	public int  status;
	public int  typeId;
	public String used;
	public int  userId;
	public String name;
	public int pageSize;
	public int isGive;
	public int isCanGive;
	
	public static MyCouponCanUse json2Entity(JSONObject json){
		MyCouponCanUse couponCanUse = new MyCouponCanUse();
		try {
			if (json.has("amount") && !json.isNull("amount")) {
				couponCanUse.amount=json.getInt("amount");
			}
			if (json.has("description")&& !json.isNull("description")) {
				couponCanUse.description=json.getString("description");
			}
			if (json.has("endTime")&& !json.isNull("endTime")) {
				couponCanUse.endTime=json.getString("endTime");
			}
			if (json.has("CouponId")&& !json.isNull("CouponId")) {
				couponCanUse.CouponId=json.getInt("CouponId");
			}
			if (json.has("orderId")&& !json.isNull("orderId")) {
				couponCanUse.orderId=json.getString("orderId");
			}
			if (json.has("startTime")&& !json.isNull("startTime")) {
				couponCanUse.startTime=json.getString("startTime");
			}
			if (json.has("status")&& !json.isNull("status")) {
				couponCanUse.status=json.getInt("status");
			}
			if (json.has("typeId")&& !json.isNull("typeId")) {
				couponCanUse.typeId=json.getInt("typeId");
			}
			if (json.has("used")&& !json.isNull("used")) {
				couponCanUse.used=json.getString("used");
			}
			if (json.has("userId")&& !json.isNull("userId")) {
				couponCanUse.userId=json.getInt("userId");
			}
			if (json.has("name")&& !json.isNull("name")) {
				couponCanUse.name=json.getString("name");
			}
			if (json.has("pageSize")&&!json.isNull("pageSize")) {
				couponCanUse.pageSize = json.getInt("pageSize");
			}
			if (json.has("isGive")&&!json.isNull("isGive")) {
				couponCanUse.isGive = json.getInt("isGive");
			}
			if (json.has("isCanGive")&&!json.isNull("isCanGive")) {
				couponCanUse.isCanGive = json.getInt("isCanGive");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return couponCanUse;
		
	}
}
