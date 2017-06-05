package com.haotang.pet.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;

public class CustomerOrderComment {
	public int petService;
	public int petId;
	public int orderId;
	public String commentGradeCopy;
	public String created;
	public int grade;
	public int commentId;
	public String content;
	public String petServiceName;
	public String replyUser="";
	public String replyContent;
	public String replyTime;
	public ArrayList<String> imgLists = new ArrayList<String>();
	
	public static CustomerOrderComment json2Entity(JSONObject json){
		CustomerOrderComment comment = new CustomerOrderComment();
		try {
			if (json.has("replyContent")&&!json.isNull("replyContent")) {
				comment.replyContent = json.getString("replyContent");
			}
			if (json.has("replyTime")&&!json.isNull("replyTime")) {
				comment.replyTime = json.getString("replyTime");
			}
			if (json.has("petServiceName")&&!json.isNull("petServiceName")) {
				comment.petServiceName = json.getString("petServiceName");
			}
			if (json.has("grade")&&!json.isNull("grade")) {
				comment.grade = json.getInt("grade");
			}
			if (json.has("commentGradeCopy")&&!json.isNull("commentGradeCopy")) {
				comment.commentGradeCopy = json.getString("commentGradeCopy");
			}
			if (json.has("content")&&!json.isNull("content")) {
				comment.content = json.getString("content");
			}
			if (json.has("created")&&!json.isNull("created")) {
				comment.created = json.getString("created");
			}
			if (json.has("replyUser")&&!json.isNull("replyUser")) {
				comment.replyUser = json.getString("replyUser");
			}
			if (json.has("imgList")&&!json.isNull("imgList")) {
				JSONArray arrayImg = json.getJSONArray("imgList");
				if (arrayImg.length()>0) {
					for (int i = 0; i < arrayImg.length(); i++) {
						if (arrayImg.getString(i).contains("http")) {
							comment.imgLists.add(arrayImg.getString(i));
						}else {
							comment.imgLists.add(CommUtil.getServiceNobacklash()+arrayImg.getString(i));
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return comment;
		
	}
}
