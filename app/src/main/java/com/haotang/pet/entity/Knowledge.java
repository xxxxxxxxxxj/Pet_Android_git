package com.haotang.pet.entity;

import org.json.JSONObject;

public class Knowledge {
	public String tag;
	public String title;
	public String content;
	public String path;
	
	public static Knowledge json2Entity(JSONObject json){
		Knowledge knowledge = new Knowledge();
		try{
			if(json.has("head")&&!json.isNull("head")){
				knowledge.content = json.getString("head");
			}
			if(json.has("tag")&&!json.isNull("tag")){
				knowledge.tag = json.getString("tag");
			}
			if(json.has("title")&&!json.isNull("title")){
				knowledge.title = json.getString("title");
			}
			if(json.has("url")&&!json.isNull("url")){
				knowledge.path = json.getString("url");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return knowledge;
	}
}
