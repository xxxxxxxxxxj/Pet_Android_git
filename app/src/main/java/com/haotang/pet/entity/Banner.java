package com.haotang.pet.entity;

import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;

public class Banner {
	public String url;
	public String imgurl;
	
	public static Banner json2Entity(JSONObject jobj){
		Banner b = new Banner();
		try {
			if(jobj.has("url")&&!jobj.isNull("url")){
				b.url=jobj.getString("url");
			}
			if(jobj.has("pic")&&!jobj.isNull("pic")){
				b.imgurl=CommUtil.getServiceNobacklash()+jobj.getString("pic");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}
}
