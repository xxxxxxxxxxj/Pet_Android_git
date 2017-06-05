package com.haotang.pet.entity;

import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Utils;

public class Room {
	public int id;
	public String kindname;
	public String des;
	public String img;
	public String img2;
	public String imggray;
	public double price;
	public double listprice;
	public String flag;
	public String scopedes;//适用范围
	public boolean valid;//是否可用
	public String sizeDesc;
	public String other_desc;
	
	public static Room json2entity(JSONObject json){
		Room r = new Room();
		try{
			if(json.has("id")&&!json.isNull("id")){
				r.id = json.getInt("id");
			}
			if(json.has("name")&&!json.isNull("name")){
				r.kindname = json.getString("name");
			}
			if(json.has("desc1")&&!json.isNull("desc1")){
				r.des = json.getString("desc1");
			}
			if(json.has("desc2")&&!json.isNull("desc2")){
				r.flag = json.getString("desc2");
			}
			if(json.has("picDesc")&&!json.isNull("picDesc")){
				r.scopedes = json.getString("picDesc");
			}
			if(json.has("img1")&&!json.isNull("img1")){
				r.img = CommUtil.getServiceNobacklash()+json.getString("img1");
			}
			if(json.has("img2")&&!json.isNull("img2")){
				r.img2 = CommUtil.getServiceNobacklash()+json.getString("img2");
			}
			if(json.has("img1_na")&&!json.isNull("img1_na")){
				r.imggray = CommUtil.getServiceNobacklash()+json.getString("img1_na");
			}
			if(json.has("price")&&!json.isNull("price")){
				r.price = json.getDouble("price");
			}
			if(json.has("listPrice")&&!json.isNull("listPrice")){
				r.listprice = json.getDouble("listPrice");
			}
			if(json.has("isFull")&&!json.isNull("isFull")){
				r.valid = json.getInt("isFull")==0?true:false;
			}
			if(json.has("sizeDesc")&&!json.isNull("sizeDesc")){
				r.sizeDesc = json.getString("sizeDesc");
			}
			if(json.has("otherDesc")&&!json.isNull("otherDesc")){
				r.other_desc = json.getString("otherDesc");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return r;
	}
}
