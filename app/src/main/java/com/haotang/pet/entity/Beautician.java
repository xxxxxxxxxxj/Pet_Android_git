package com.haotang.pet.entity;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;

public class Beautician implements Serializable{
	public int id;
	public String name;
	public int storeid;

	public int sort;//美容师认证级别
	public String levelname;//美容师认证等级名称
	public double fee;//总价
	public String image;
	public String sign;
	public String titlelevel;
	public int ordernum;
	public int levels;//等级
	public int stars;//等级下的星级
	public int gender;//男女
	public int isAvail=3;//当前时间该美容师是否可用 1：可用 0：不可用 3:没选时间进入的列表
	public double price=0;//每个美容师当前等级价格
	public String expertises="";
	public String desc;
	public double realGrade1;
	public double realGrade2;
	public double realGrade3;
	public double goodRate;
	public double grade;
	
//	start------------美容师详情页面预约数据
	
	public String BeauDetailIcon;
	public String BeauDetailName;
	public String BeauDetailType;
	public int BeauDetailServiceType;
	public int BeauDetailMenuItemId;
	public String areaName;
	public String BeauDetailTypeId;
//	end ------------美容师详情页面预约数据
	
//	美容师列表页新增字段
	public int homeServiceSchedule;
	public int shopServiceSchedule;
	
	public boolean isChoose = false;
	public String appointment = "";
	public int colorId;
	public String upgradeTip;
	public int areaId;
	@Override
	public String toString() {
		return "Beautician [id=" + id + ", name=" + name + ", storeid="
				+ storeid + ", sort=" + sort + ", levelname=" + levelname
				+ ", fee=" + fee + ", image=" + image + ", sign=" + sign
				+ ", titlelevel=" + titlelevel + ", ordernum=" + ordernum
				+ ", levels=" + levels + ", stars=" + stars + ", gender="
				+ gender + ", isAvail=" + isAvail + ", price=" + price
				+ ", expertises=" + expertises + ", desc=" + desc
				+ ", realGrade1=" + realGrade1 + ", realGrade2=" + realGrade2
				+ ", realGrade3=" + realGrade3 + ", BeauDetailIcon="
				+ BeauDetailIcon + ", BeauDetailName=" + BeauDetailName
				+ ", BeauDetailType=" + BeauDetailType
				+ ", BeauDetailServiceType=" + BeauDetailServiceType
				+ ", BeauDetailMenuItemId=" + BeauDetailMenuItemId
				+ ", areaName=" + areaName + ", homeServiceSchedule="
				+ homeServiceSchedule + ", shopServiceSchedule="
				+ shopServiceSchedule + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}



	public static Beautician json2Entity(JSONObject json){
		Beautician beautician = new Beautician();
		try{
			if(json.has("areaId")&&!json.isNull("areaId")){
				beautician.areaId = json.getInt("areaId");
			}
			if (json.has("upgradeTip")&&!json.isNull("upgradeTip")) {
				beautician.upgradeTip = json.getString("upgradeTip");
			}
			if (json.has("goodRate")&&!json.isNull("goodRate")) {
				beautician.goodRate = json.getDouble("goodRate");
			}
			if (json.has("grade")&&!json.isNull("grade")) {
				beautician.grade = json.getDouble("grade");
			}
			if(json.has("WorkerId")&&!json.isNull("WorkerId")){
				beautician.id = json.getInt("WorkerId");
			}
			if(json.has("shopId")&&!json.isNull("shopId")){
				beautician.storeid = json.getInt("shopId");
			}
			if(json.has("level")&&!json.isNull("level")){
				JSONObject jl = json.getJSONObject("level");
				if(jl.has("sort")&&!jl.isNull("sort")){
					beautician.sort = jl.getInt("sort");
				}
				if(jl.has("name")&&!jl.isNull("name")){
					beautician.levelname = jl.getString("name");
				}
			}
			if(json.has("isAvail")&&!json.isNull("isAvail")){
				beautician.isAvail = json.getInt("isAvail");
			}
		
			if(json.has("totalOrderAmount")&&!json.isNull("totalOrderAmount")){
				beautician.ordernum = json.getInt("totalOrderAmount");
			}
			if(json.has("realName")&&!json.isNull("realName")){
				beautician.name = json.getString("realName");
			}
			
			if(json.has("avatar")&&!json.isNull("avatar")){
				beautician.image = CommUtil.getServiceNobacklash()+json.getString("avatar");
			}
			if(json.has("sign")&&!json.isNull("sign")){
				beautician.sign = json.getString("sign");
			}
			if(json.has("desc")&&!json.isNull("desc")){
				beautician.desc = json.getString("desc");
			}
			if(json.has("expertises")&&!json.isNull("expertises")){
				JSONArray array = json.getJSONArray("expertises");
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					if (object.has("itemName")&&!object.isNull("itemName")) {
						builder.append(object.getString("itemName")+"、");
					}
				}
				beautician.expertises =builder.substring(0, builder.length()-1);
			}
			if(json.has("title")&&!json.isNull("title")){
				beautician.titlelevel = json.getString("title");
			}
			if(json.has("workerGrade")&&!json.isNull("workerGrade")){
				int grade = json.getInt("workerGrade");
				beautician.levels = grade/10;
				beautician.stars = grade%10;
			}
			if(json.has("gender")&&!json.isNull("gender")){
				beautician.gender = json.getInt("gender");
			}
			if (json.has("realGrade1")&&!json.isNull("realGrade1")) {
				beautician.realGrade1 = json.getDouble("realGrade1");
			}
			if (json.has("realGrade2")&&!json.isNull("realGrade2")) {
				beautician.realGrade2 = json.getDouble("realGrade2");
			}
			if (json.has("realGrade3")&&!json.isNull("realGrade3")) {
				beautician.realGrade3 = json.getDouble("realGrade3");
			}
			if (json.has("area")&&!json.isNull("area")) {
				JSONObject object = json.getJSONObject("area");
				if (object.has("name")&&!object.isNull("name")) {
					beautician.areaName=object.getString("name");
				}
			}
//			---------以下美容师详情页 点预约弹出信息
			
			if (json.has("icon")&&!json.isNull("icon")) {
				beautician.BeauDetailIcon = CommUtil.getServiceNobacklash()+json.getString("icon");
			}
			if (json.has("name")&&!json.isNull("name")) {
				beautician.BeauDetailName = json.getString("name");
			}
			if (json.has("type")&&!json.isNull("type")) {
				beautician.BeauDetailType = json.getString("type");
			}
			if (json.has("info")&&!json.isNull("info")) {
				JSONObject object = json.getJSONObject("info");
				if (object.has("serviceType")&&!object.isNull("serviceType")) {
					beautician.BeauDetailServiceType = object.getInt("serviceType");
				}
				if (object.has("typeId")&&!object.isNull("typeId")) {
					beautician.BeauDetailTypeId = object.getString("typeId");
				}
			}
			if (json.has("MenuItemId")&&!json.isNull("MenuItemId")) {
				beautician.BeauDetailMenuItemId = json.getInt("MenuItemId");
			}
//			-------------美容师是否支持上门、到店
			if (json.has("homeServiceSchedule")&&!json.isNull("homeServiceSchedule")) {
				beautician.homeServiceSchedule = json.getInt("homeServiceSchedule");
			}
			if (json.has("shopServiceSchedule")&&!json.isNull("shopServiceSchedule")) {
				beautician.shopServiceSchedule = json.getInt("shopServiceSchedule");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return beautician;
	}
}
