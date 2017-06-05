package com.haotang.pet.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;

public class ShopsWithPrice {

	public String img;
	public String shopName;
	public String shopPrice;
	public String dist=null;
	public String address;
	public String tel;
	public String lowestPrice;//最低价提示
	public int shopId;
	public boolean isFull;//是否订满
	public boolean isMulPrice;//是否有多个价格,true:显示“起” false:不显示“起”
	
	public int status=-1; // 状态为2 表示未开业 没有此字段表示正常营业
	public String hotelImg;
	public static ShopsWithPrice json2Entity(JSONObject json){
		ShopsWithPrice shopsWithPrice = new ShopsWithPrice();
		try {
			if (json.has("img")&&!json.isNull("img")) {
				shopsWithPrice.img = CommUtil.getServiceNobacklash()+json.getString("img");
			}
			if (json.has("shopName")&&!json.isNull("shopName")) {
				shopsWithPrice.shopName = json.getString("shopName");
			}
			if (json.has("shopPrice")&&!json.isNull("shopPrice")) {
				shopsWithPrice.shopPrice = json.getString("shopPrice");
			}
			if (json.has("lowestPrice")&&!json.isNull("lowestPrice")) {
				shopsWithPrice.lowestPrice = json.getString("lowestPrice");
			}
			if (json.has("lowestPrice2")&&!json.isNull("lowestPrice2")) {
				shopsWithPrice.isMulPrice=true;
			}else{
				shopsWithPrice.isMulPrice=false;
			}
			if (json.has("dist")&&!json.isNull("dist")) {
				shopsWithPrice.dist = json.getString("dist");
			}
			if (json.has("address")&&!json.isNull("address")) {
				shopsWithPrice.address = json.getString("address");
			}
			if (json.has("phone")&&!json.isNull("phone")) {
				shopsWithPrice.tel = json.getString("phone");
			}
			if (json.has("ShopId")&&!json.isNull("ShopId")) {
				shopsWithPrice.shopId = json.getInt("ShopId");
			}
			if (json.has("isFull")&&!json.isNull("isFull")) {
				shopsWithPrice.isFull = (json.getInt("isFull")==1?true:false);
			}
			if (json.has("status")&&!json.isNull("status")) {
				shopsWithPrice.status = json.getInt("status");
			}
			if (json.has("hotelImg")&&!json.isNull("hotelImg")) {
				JSONArray array = json.getJSONArray("hotelImg");
				shopsWithPrice.hotelImg = CommUtil.getServiceNobacklash()+array.getString(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return shopsWithPrice;
		
	}
}
