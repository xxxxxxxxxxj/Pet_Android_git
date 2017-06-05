package com.haotang.pet.entity;

import org.json.JSONObject;

public class MainPetEncyclopedia {
	public int PetKnowledgeId;
	public String content;
	public String createTime;
	public String img;
	public int isDel;
	public int isSend;
	public String title;
	public String updateTime;
	public String url;

	public static MainPetEncyclopedia json2Entity(JSONObject json) {
		MainPetEncyclopedia addr = new MainPetEncyclopedia();
		try {
			if (json.has("PetKnowledgeId") && !json.isNull("PetKnowledgeId")) {
				addr.PetKnowledgeId = json.getInt("PetKnowledgeId");
			}
			if (json.has("content") && !json.isNull("content")) {
				addr.content = json.getString("content");
			}
			if (json.has("createTime") && !json.isNull("createTime")) {
				addr.createTime = json.getString("createTime");
			}
			if (json.has("img") && !json.isNull("img")) {
				addr.img = json.getString("img");
			}
			if (json.has("isDel") && !json.isNull("isDel")) {
				addr.isDel = json.getInt("isDel");
			}
			if (json.has("isSend") && !json.isNull("isSend")) {
				addr.isSend = json.getInt("isSend");
			}
			if (json.has("title") && !json.isNull("title")) {
				addr.title = json.getString("title");
			}
			if (json.has("updateTime") && !json.isNull("updateTime")) {
				addr.updateTime = json.getString("updateTime");
			}
			if (json.has("url") && !json.isNull("url")) {
				addr.url = json.getString("url");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addr;
	}
}
