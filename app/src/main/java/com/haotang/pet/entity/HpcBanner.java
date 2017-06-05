package com.haotang.pet.entity;

import org.json.JSONObject;

public class HpcBanner {
	public int areaId;
	public String imgUrl;
	public String imgUrlSmall;
	public int isBigShow;

	@Override
	public String toString() {
		return "HpcBanner [areaId=" + areaId + ", imgUrl=" + imgUrl
				+ ", imgUrlSmall=" + imgUrlSmall + ", isBigShow=" + isBigShow
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	public static HpcBanner json2Entity(JSONObject jobj) {
		HpcBanner b = new HpcBanner();
		try {
			if (jobj.has("areaId") && !jobj.isNull("areaId")) {
				b.areaId = jobj.getInt("areaId");
			}
			if (jobj.has("isBigShow") && !jobj.isNull("isBigShow")) {
				b.isBigShow = jobj.getInt("isBigShow");
			}
			if (jobj.has("imgUrl") && !jobj.isNull("imgUrl")) {
				b.imgUrl = jobj.getString("imgUrl");
			}
			if (jobj.has("imgUrlSmall") && !jobj.isNull("imgUrlSmall")) {
				b.imgUrlSmall = jobj.getString("imgUrlSmall");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
}
