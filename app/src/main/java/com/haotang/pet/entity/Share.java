package com.haotang.pet.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Share {

	public String txt;
	public String topic;
	public String title;
	public String url;
	public String img;
	
	public static Share json2Entity(JSONObject json){
		Share share = new Share();
		try {
			if (json.has("txt")&&!json.isNull("txt")) {
				share.txt=json.getString("txt");
			}
			if (json.has("topic")&&!json.isNull("topic")) {
				share.topic=json.getString("topic");
			}
			if (json.has("title")&&!json.isNull("title")) {
				share.title=json.getString("title");
			}
			if (json.has("url")&&!json.isNull("url")) {
				share.url=json.getString("url");
			}
			if (json.has("img")&&!json.isNull("img")) {
				share.img=json.getString("img");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return share;
		
	}
}
