package com.haotang.pet.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Utils;

public class ServicesFeature {
	public int serviceType;
	public int petKind;
	public String pic;
	public int sort;
	public String priceInterval;
	public String minPrice;
	public String name;
	public String detailPic;
	public int PetServicePojoId;
	public String spic;
	public String description;
	public String title10;
	public String title20;
	public String title30;
	public String desc10;
	public String desc20;
	public String desc30;
	public ServicePrice ServicePrice = null;
	public String btnTxtShop10;
	public String btnTxtShop20;
	public String btnTxtShop30;
	public String noticeShow;
	
	public static ServicesFeature json2Entity(JSONObject json){
		ServicesFeature feature = new ServicesFeature();
		try {
			if (json.has("serviceType")&&!json.isNull("serviceType")) {
				feature.serviceType = json.getInt("serviceType");
			}
			if (json.has("petKind")&&!json.isNull("petKind")) {
				feature.petKind = json.getInt("petKind");
			}
			if (json.has("sort")&&!json.isNull("sort")) {
				feature.sort = json.getInt("sort");
			}
			if (json.has("pic")&&!json.isNull("pic")) {
				feature.pic=CommUtil.getServiceNobacklash()+json.getString("pic");
			}
			if (json.has("priceInterval")&&!json.isNull("priceInterval")) {
				feature.priceInterval=json.getString("priceInterval");
			}
			if (json.has("minPrice")&&!json.isNull("minPrice")) {
				feature.minPrice=json.getString("minPrice");
			}
			if (json.has("name")&&!json.isNull("name")) {
				feature.name=json.getString("name");
			}
			if (json.has("detailPic")&&!json.isNull("detailPic")) {
				feature.detailPic=CommUtil.getServiceNobacklash()+json.getString("detailPic");
			}
			if (json.has("PetServicePojoId")&&!json.isNull("PetServicePojoId")) {
				feature.PetServicePojoId=json.getInt("PetServicePojoId");
			}
			if (json.has("spic")&&!json.isNull("spic")) {
				feature.spic=CommUtil.getServiceNobacklash()+json.getString("spic");
			}
			if (json.has("description")&&!json.isNull("description")) {
				feature.description=json.getString("description");
			}
			if (json.has("servicePrice")&&!json.isNull("servicePrice")) {
				ServicePrice getService = new ServicePrice();
				JSONObject object = json.getJSONObject("servicePrice");
				if (object.has("shopPrice10")&&!object.isNull("shopPrice10")) {
					getService.shopPrice10 = Utils.formatDouble(object.getDouble("shopPrice10"));
				}
				if (object.has("shopPrice20")&&!object.isNull("shopPrice20")) {
					getService.shopPrice20 = Utils.formatDouble(object.getDouble("shopPrice20"));
				}
				if (object.has("shopPrice30")&&!object.isNull("shopPrice30")) {
					getService.shopPrice30 = Utils.formatDouble(object.getDouble("shopPrice30"));
				}
				if (object.has("shopListPrice")&&!object.isNull("shopListPrice")) {
					getService.shopListPrice = object.getString("shopListPrice");
				}
				if (object.has("shopListPrice20")&&!object.isNull("shopListPrice20")) {
					getService.shopListPrice20 = object.getString("shopListPrice20");
				}
				if (object.has("shopListPrice30")&&!object.isNull("shopListPrice30")) {
					getService.shopListPrice30 = object.getString("shopListPrice30");
				}
				feature.ServicePrice = getService;
			}
			if (json.has("targetPets")&&!json.isNull("targetPets")) {
				JSONObject targetPet = json.getJSONObject("targetPets");
				if (targetPet.has("10")&&!targetPet.isNull("10")) {
					JSONObject object10 = targetPet.getJSONObject("10");
					if (object10.has("title")&&!object10.isNull("title")) {
						feature.title10=object10.getString("title");
					}
					if (object10.has("desc")&&!object10.isNull("desc")) {
						feature.desc10=object10.getString("desc");
					}
					if (object10.has("btnTxt")&&!object10.isNull("btnTxt")) {
						feature.btnTxtShop10=object10.getString("btnTxt");
					}
				}
				if (targetPet.has("20")&&!targetPet.isNull("20")) {
					JSONObject object20 = targetPet.getJSONObject("20");
					if (object20.has("title")&&!object20.isNull("title")) {
						feature.title20=object20.getString("title");
					}
					if (object20.has("desc")&&!object20.isNull("desc")) {
						feature.desc20=object20.getString("desc");
					}
					if (object20.has("btnTxt")&&!object20.isNull("btnTxt")) {
						feature.btnTxtShop20=object20.getString("btnTxt");
					}
				}
				if (targetPet.has("30")&&!targetPet.isNull("30")) {
					JSONObject object30 = targetPet.getJSONObject("30");
					if (object30.has("title")&&!object30.isNull("title")) {
						feature.title30=object30.getString("title");
					}
					if (object30.has("desc")&&!object30.isNull("desc")) {
						feature.desc30=object30.getString("desc");
					}
					if (object30.has("btnTxt")&&!object30.isNull("btnTxt")) {
						feature.btnTxtShop30=object30.getString("btnTxt");
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return feature;

		
	}
}
