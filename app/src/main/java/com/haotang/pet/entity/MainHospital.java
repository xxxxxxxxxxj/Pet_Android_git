package com.haotang.pet.entity;

import org.json.JSONObject;

public class MainHospital {
	public int HospitalId;
	public String addr;
	public String avatar;
	public String name;

	public static MainHospital json2Entity(JSONObject json) {
		MainHospital addr = new MainHospital();
		try {
			if (json.has("HospitalId") && !json.isNull("HospitalId")) {
				addr.HospitalId = json.getInt("HospitalId");
			}
			if (json.has("addr") && !json.isNull("addr")) {
				addr.addr = json.getString("addr");
			}
			if (json.has("avatar") && !json.isNull("avatar")) {
				addr.avatar = json.getString("avatar");
			}
			if (json.has("name") && !json.isNull("name")) {
				addr.name = json.getString("name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addr;
	}
}
