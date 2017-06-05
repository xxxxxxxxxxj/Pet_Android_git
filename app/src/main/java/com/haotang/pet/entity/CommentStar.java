package com.haotang.pet.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class CommentStar {
	public int grade;
	public String tag;
	public int CommentTagId;
	public boolean ifChoose = false;
	
	public static CommentStar json2Entity(JSONObject json){
		CommentStar star = new CommentStar();
		try {
			if (json.has("grade")&&!json.isNull("grade")) {
				star.grade = json.getInt("grade");
			}
			if (json.has("tag")&&!json.isNull("tag")) {
				star.tag = json.getString("tag");
			}
			if (json.has("CommentTagId")&&!json.isNull("CommentTagId")) {
				star.CommentTagId = json.getInt("CommentTagId");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return star;
		
	}
}
