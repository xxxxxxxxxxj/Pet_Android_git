package com.haotang.pet.entity;

import org.json.JSONObject;

public class MainCharacteristicService {
	public int PetServiceTypeForListId;
	public String content;
	public int isDel;
	public int isHome;
	public String minPrice;
	public String name;
	public String pic;
	public int sort;
	public int totalSoldCount;
	public String txtPrice;
	public int hotOrNew;

	public static MainCharacteristicService json2Entity(JSONObject json) {
		MainCharacteristicService addr = new MainCharacteristicService();
		try {
			if (json.has("hotOrNew") && !json.isNull("hotOrNew")) {
				addr.hotOrNew = json.getInt("hotOrNew");
			}
			if (json.has("PetServiceTypeForListId")
					&& !json.isNull("PetServiceTypeForListId")) {
				addr.PetServiceTypeForListId = json
						.getInt("PetServiceTypeForListId");
			}
			if (json.has("content") && !json.isNull("content")) {
				addr.content = json.getString("content");
			}
			if (json.has("isDel") && !json.isNull("isDel")) {
				addr.isDel = json.getInt("isDel");
			}
			if (json.has("isHome") && !json.isNull("isHome")) {
				addr.isHome = json.getInt("isHome");
			}
			if (json.has("minPrice") && !json.isNull("minPrice")) {
				addr.minPrice = json.getString("minPrice");
			}
			if (json.has("name") && !json.isNull("name")) {
				addr.name = json.getString("name");
			}
			if (json.has("pic") && !json.isNull("pic")) {
				addr.pic = json.getString("pic");
			}
			if (json.has("sort") && !json.isNull("sort")) {
				addr.sort = json.getInt("sort");
			}
			if (json.has("totalSoldCount") && !json.isNull("totalSoldCount")) {
				addr.totalSoldCount = json.getInt("totalSoldCount");
			}
			if (json.has("txtPrice") && !json.isNull("txtPrice")) {
				addr.txtPrice = json.getString("txtPrice");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addr;
	}
}
