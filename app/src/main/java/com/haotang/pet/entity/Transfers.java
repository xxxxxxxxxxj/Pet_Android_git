package com.haotang.pet.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * <p>
 * Title:Transfers
 * </p>
 * <p>
 * Description:寄养选择房型界面中转站信息实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-9-1 下午3:33:34
 */
public class Transfers {
	public String address;
	public double dist;
	public int id;
	public String name;
	public String tel;
	public double lat;
	public double lng;
	public List<String> hotelImg = new ArrayList<String>();

	public static Transfers json2entity(JSONObject json) {
		Transfers Transfers = new Transfers();
		try {
			if (json.has("address") && !json.isNull("address")) {
				Transfers.address = json.getString("address");
			}
			if (json.has("dist") && !json.isNull("dist")) {
				Transfers.dist = json.getDouble("dist");
			}
			if (json.has("id") && !json.isNull("id")) {
				Transfers.id = json.getInt("id");
			}
			if (json.has("name") && !json.isNull("name")) {
				Transfers.name = json.getString("name");
			}
			if (json.has("tel") && !json.isNull("tel")) {
				Transfers.tel = json.getString("tel");
			}
			if (json.has("lat") && !json.isNull("lat")) {
				Transfers.lat = json.getDouble("lat");
			}
			if (json.has("lng") && !json.isNull("lng")) {
				Transfers.lng = json.getDouble("lng");
			}
			if (json.has("hotelImg") && !json.isNull("hotelImg")) {
				JSONArray bannersArray = json.getJSONArray("hotelImg");
				if (bannersArray.length() > 0) {
					Transfers.hotelImg.clear();
					for (int i = 0; i < bannersArray.length(); i++) {
						Transfers.hotelImg.add(bannersArray.getString(i));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Transfers;
	}
}
