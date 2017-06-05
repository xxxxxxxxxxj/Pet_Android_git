package com.haotang.pet.entity;

import org.json.JSONObject;

public class MDay {
	public String year;
	public String day;
	public String daydes;
	
	public static MDay json2Entity(JSONObject json){
		MDay day = new MDay();
		try{
			if(json.has("date")&&!json.isNull("date")){
				day.day = json.getString("date");
			}
			if(json.has("year")&&!json.isNull("year")){
				day.year = json.getString("year");
			}
			if(json.has("desc")&&!json.isNull("desc")){
				day.daydes = json.getString("desc");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return day;
	}
}
