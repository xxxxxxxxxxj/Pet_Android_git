package com.haotang.pet.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;

public class HostPitalAndOther {
	public int id;
	public String name;
	public String addr;
	public String pic;
	
	public static HostPitalAndOther json2Entity(JSONObject json){
		HostPitalAndOther pitalAndOther = new HostPitalAndOther();
		try {
			if(json.has("id")&&!json.isNull("id")){
				pitalAndOther.id = json.getInt("id");
			}
			if(json.has("name")&&!json.isNull("name")){
				pitalAndOther.name = json.getString("name");
			}
			if(json.has("addr")&&!json.isNull("addr")){
				pitalAndOther.addr = json.getString("addr");
			}
			if(json.has("pic")&&!json.isNull("pic")){
				pitalAndOther.pic = CommUtil.getServiceBaseUrl()+json.getString("pic");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pitalAndOther;
		
	}
}
