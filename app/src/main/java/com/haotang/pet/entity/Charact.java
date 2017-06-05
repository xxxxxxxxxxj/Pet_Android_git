package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;

public class Charact implements Serializable{
	public String name;//服务名
	public String pic;//图片地址
	public String minprice;//价格区间
	public String minpricesuffix;//价格后的“起”
	public ArrayList<CharctItem> serviceitems ;
	
	public static Charact json2Entity(JSONObject json){
		Charact charact = new Charact();
		try {
			if (json.has("name")&&!json.isNull("name")) {
				charact.name=json.getString("name");
			}
			if (json.has("pic")&&!json.isNull("pic")) {
				charact.pic = CommUtil.getServiceNobacklash()+json.getString("pic");
			}
			if (json.has("txtPrice")&&!json.isNull("txtPrice")) {
				charact.minpricesuffix = json.getString("txtPrice");
			}
			if (json.has("minPrice")&&!json.isNull("minPrice")) {
				charact.minprice = json.getString("minPrice");
			}
			
			if (json.has("listService")&&!json.isNull("listService")) {
				JSONArray jarr = json.getJSONArray("listService");
				int length = jarr.length();
				charact.serviceitems = new ArrayList<CharctItem>();
				for(int i=0;i<length;i++){
					charact.serviceitems.add(CharctItem.json2Entity(jarr.getJSONObject(i)));
				}
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return charact;
	}
}
