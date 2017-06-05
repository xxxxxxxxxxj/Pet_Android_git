package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;

public class Pet implements Serializable{
	public int id=-1;
	public int kindid;
	public String name="";
	public String image;
	public String nickName="";
	public int customerpetid=0;
	public int sa;//是否支持服务
	public int bathnum;//洗澡次数
	public int beautynum;//美容次数
	public int fosternum;//寄养次数
	public int charmnum;//魅力值
	public int cleannum;//清洁值
	public int sex;//性别 1：男 0：女
	public int mood;//心情
	public String visibleid;//可见ID
	public String cleanhint;//提示
	public String availServiceType;
	public long [] serviceHome=null; 
	public long [] serviceShop=null; 
	public double youyongPrice=0;
	public double listPrice=0;
	public int serviceloc;
	public int serviceid;
	public String certiOrderStatus;
	public CertiOrder certiOrder;
	public int sumSpecial;
	public int certiStatus;
	public String certiNo;
	public String certiUrl;
	public int certiDogSize;
	public int orderFee;
	public int orderid;
	public int sumSwim;
	public String month;
	public int isCerti;
	public static Pet json2Entity(JSONObject json){
		Pet pet = new Pet();
		
		try{
			if(json.has("month")&&!json.isNull("month")){
				pet.month = json.getString("month");
			}
			if(json.has("isCerti")&&!json.isNull("isCerti")){
				pet.isCerti = json.getInt("isCerti");
			}
			if(json.has("PetId")&&!json.isNull("PetId")){
				pet.id = json.getInt("PetId");
			}
			if(json.has("CustomerPetId")&&!json.isNull("CustomerPetId")){
				pet.customerpetid = json.getInt("CustomerPetId");
			}
			if(json.has("petKind")&&!json.isNull("petKind")){
				pet.kindid = json.getInt("petKind");
			}
			if(json.has("petName")&&!json.isNull("petName")){
				pet.name = json.getString("petName");
			}
			if(json.has("nickName")&&!json.isNull("nickName")){
				pet.nickName = json.getString("nickName").trim();
			}
			if(json.has("avatarPath")&&!json.isNull("avatarPath")){
				String string = json.getString("avatarPath");
				if(!string.startsWith("http://") && !string.startsWith("https://")){
					pet.image = CommUtil.getServiceNobacklash()+json.getString("avatarPath");
				}else{
					pet.image = string;
				}
			}
			if (json.has("sa")&&!json.isNull("sa")) {
				pet.sa = json.getInt("sa");
			}
			
			if(json.has("sumBath")&&!json.isNull("sumBath")){
				pet.bathnum = json.getInt("sumBath");
			}
			if(json.has("sumCos")&&!json.isNull("sumCos")){
				pet.beautynum = json.getInt("sumCos");
			}
			if(json.has("sumFos")&&!json.isNull("sumFos")){
				pet.fosternum = json.getInt("sumFos");
			}
			if(json.has("sumSpecial")&&!json.isNull("sumSpecial")){
				pet.sumSpecial = json.getInt("sumSpecial");
			}
			if(json.has("cleanGrade")&&!json.isNull("cleanGrade")){
				pet.cleannum = json.getInt("cleanGrade");
			}
			if(json.has("beautyGrade")&&!json.isNull("beautyGrade")){
				pet.charmnum = json.getInt("beautyGrade");
			}
			if(json.has("sex")&&!json.isNull("sex")){
				pet.sex = json.getInt("sex");
			}
			if(json.has("emotion")&&!json.isNull("emotion")){
				pet.mood = json.getInt("emotion");
			}
			if(json.has("petCode")&&!json.isNull("petCode")){
				pet.visibleid = json.getString("petCode");
			}
			if(json.has("certiStatus")&&!json.isNull("certiStatus")){
				pet.certiStatus = json.getInt("certiStatus");
			}
			if(json.has("gradeTxt")&&!json.isNull("gradeTxt")){
				pet.cleanhint = json.getString("gradeTxt");
			}
			if(json.has("availServiceTypes")&&!json.isNull("availServiceTypes")){
				JSONArray arr = json.getJSONArray("availServiceTypes");
				pet.availServiceType = arr.toString();
			}
			if (json.has("serviceHome")&&!json.isNull("serviceHome")) {
				JSONArray arrayService = json.getJSONArray("service");
				long ServiceHome [] = new long[arrayService.length()];
				for (int j = 0; j < arrayService.length(); j++) {
					ServiceHome[j] = arrayService.getLong(j);
				}
				pet.serviceHome= ServiceHome;
			}
			if (json.has("serviceShop")&&!json.isNull("serviceShop")) {
				JSONArray arrayService = json.getJSONArray("serviceShop");
				long serviceShop [] = new long[arrayService.length()];
				for (int j = 0; j < arrayService.length(); j++) {
					serviceShop[j] = arrayService.getLong(j);
				}
				pet.serviceShop=serviceShop;
			}
			if (json.has("certiOrderStatus")&&!json.isNull("certiOrderStatus")) {
				pet.certiOrderStatus = json.getString("certiOrderStatus");
			}
			if (json.has("certiOrder")&&!json.isNull("certiOrder")) {
				JSONObject jsonObject = json.getJSONObject("certiOrder");
				pet.certiOrder = CertiOrder.json2Entity(jsonObject);
			}
			if(json.has("certiNo")&&!json.isNull("certiNo")){
				pet.certiNo = json.getString("certiNo");
			}
			if(json.has("certiUrl")&&!json.isNull("certiUrl")){
				pet.certiUrl = json.getString("certiUrl");
			}
			if(json.has("certiDogSize")&&!json.isNull("certiDogSize")){
				pet.certiDogSize = json.getInt("certiDogSize");
			}
			if(json.has("sumSwim")&&!json.isNull("sumSwim")){
				pet.sumSwim = json.getInt("sumSwim");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return pet;
	}
}
