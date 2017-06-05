package com.haotang.pet.entity;

import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;

/**
 * 美容师作品
 * @author Administrator
 *
 */
public class Production {
	public int id;
	public int workerid;
	public String image;
	public String smallimage;
	public String title;
	public String des;
	public String time;
	public static Production json2Entity(JSONObject json){
		Production pr = new Production();
		try{
			if(json.has("WorkId")&&json.has("WorkId")){
				pr.id = json.getInt("WorkId");
			}
			if(json.has("workerId")&&json.has("workerId")){
				pr.workerid = json.getInt("workerId");
			}
			if(json.has("imgUrl")&&json.has("imgUrl")&&!"".equals(json.getString("imgUrl").trim())){
				pr.image = CommUtil.getServiceNobacklash() + json.getString("imgUrl");
			}
			if(json.has("smallImgUrl")&&json.has("smallImgUrl")&&!"".equals(json.getString("smallImgUrl").trim())){
				pr.smallimage = CommUtil.getServiceNobacklash() + json.getString("smallImgUrl");
			}
			if(json.has("title")&&json.has("title")){
				pr.title = json.getString("title");
			}
			if(json.has("description")&&json.has("description")){
				pr.des = json.getString("description");
			}
			if(json.has("created")&&json.has("created")){
				pr.time = json.getString("created");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return pr;
	}
}
