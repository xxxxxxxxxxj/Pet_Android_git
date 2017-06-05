package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;


public class Comment {
	public int id;
	public String image="";
	public String name;
	public String content;
	public String time;
	public String replyUser="";
	public String replyContent="";
	public String replyTime="";
	public String[] images;//作品图片
	public String replyMan;
	public String memberIcon=null;//会员头像
	
	public static Comment json2Entity(JSONObject json){
		Comment comment = new Comment();
		try{
			if(json.has("CommentId")&&!json.isNull("CommentId")){
				comment.id = json.getInt("CommentId");
			}
			if(json.has("avatar")&&!json.isNull("avatar")&&!"".equals(json.getString("avatar").trim())){
				comment.image = CommUtil.getServiceNobacklash()+json.getString("avatar");
			}
			if(json.has("realName")&&!json.isNull("realName")){
				comment.name = json.getString("realName");
			}
			if(json.has("content")&&!json.isNull("content")){
				comment.content = json.getString("content");
			}
			if(json.has("created")&&!json.isNull("created")){
				String time = json.getString("created");
				comment.time = formatTime(time);
				
			}
			if (json.has("replyUser")&&!json.isNull("replyUser")) {
				comment.replyUser = json.getString("replyUser");
			}
			if (json.has("replyContent")&&!json.isNull("replyContent")) {
				comment.replyContent = json.getString("replyContent");
			}
			if (json.has("replyTime")&&!json.isNull("replyTime")) {
				comment.replyTime = json.getString("replyTime");
			}
			if(json.has("imgList")&&!json.isNull("imgList")){
				JSONArray arr = json.getJSONArray("imgList");
				int length = arr.length()>3?3:arr.length();
				if(length>0){
					comment.images= new String[length];
					for(int i=0;i<length;i++){
						comment.images[i] = CommUtil.getServiceNobacklash()+arr.getString(i);
					}
				}	
			}
			if (json.has("user")&&!json.isNull("user")) {
				JSONObject objectUser = json.getJSONObject("user");
				if (objectUser.has("userMemberLevel")&&!objectUser.isNull("userMemberLevel")) {
					JSONObject objectUserLevel = objectUser.getJSONObject("userMemberLevel");
					if (objectUserLevel.has("memberIcon")&&!objectUserLevel.isNull("memberIcon")) {
						comment.memberIcon = objectUserLevel.getString("memberIcon");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return comment;
	}
	
	private static String formatTime(String time){
		time = time.replace("年", "-");
		time = time.replace("月", "-");
		time = time.replace("日", " ");
		time = time.replace("时", ":");
		time = time.replace("分", "");
		return time;
	}
}
