package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class CharctItem implements Serializable{
	public int serviceid;
	public int servicetype;
	public String name;
	public String pricttxt;
//	public static ArrayList<String> items ;
	public static CharctItem json2Entity(JSONObject jobj){
		CharctItem ci = new CharctItem();
		try {
			if(jobj.has("PetServicePojoId")&&!jobj.isNull("PetServicePojoId")){
				ci.serviceid = jobj.getInt("PetServicePojoId");
			}
			if(jobj.has("serviceType")&&!jobj.isNull("serviceType")){
				ci.servicetype = jobj.getInt("serviceType");
			}
			if(jobj.has("tag")&&!jobj.isNull("tag")){
				ci.name = jobj.getString("tag");
			}
			if(jobj.has("minPrice")&&!jobj.isNull("minPrice")){
				ci.pricttxt = jobj.getString("minPrice");
			}
//			if(jobj.has("serviceItem")&&!jobj.isNull("serviceItem")){
//				JSONArray jarr = jobj.getJSONArray("serviceItem");
//				int length = jarr.length();
//				ci.items = new ArrayList<String>();
//				for(int i=0;i<length;i++){
//					ci.items.add(jarr.getString(i));
//				}
//			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ci;
	}
}
