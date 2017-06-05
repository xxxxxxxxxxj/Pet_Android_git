package com.haotang.pet.entity;

import org.json.JSONObject;

public class City {
	public int id;
	public String name;
	public static City json2City(JSONObject json){
		City city = new City();
		try{
			if(json.has("id")&&!json.isNull("id")){
				city.id = json.getInt("id");
			}
			if(json.has("name")&&!json.isNull("name")){
				city.name = json.getString("name");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return city;
	}
}
