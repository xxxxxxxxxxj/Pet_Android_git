package com.haotang.pet.entity;

import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;

public class MainHotHospital {
	public int id;
	public String name;
	public String addr;
	public String imgurl;
	public static MainHotHospital json2Entity(JSONObject jobj){
		MainHotHospital mhh = new MainHotHospital();
		try {
			if(jobj.has("HospitalId")&&!jobj.isNull("HospitalId")){
				mhh.id = jobj.getInt("HospitalId");
			}
			if(jobj.has("name")&&!jobj.isNull("name")){
				mhh.name = jobj.getString("name");
			}
			if(jobj.has("addr")&&!jobj.isNull("addr")){
				mhh.addr = jobj.getString("addr");
			}
			if(jobj.has("avatar")&&!jobj.isNull("avatar")){
				mhh.imgurl = CommUtil.getServiceNobacklash()+jobj.getString("avatar");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mhh;
	}
}
