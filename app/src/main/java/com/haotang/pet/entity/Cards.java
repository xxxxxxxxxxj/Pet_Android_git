package com.haotang.pet.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Cards {
	public String cardName;
	public String explainText;
	public String cardTip;
	public String bath_level_text;
	public String beauty_level_text;
	public String bath_count_text;
	public String beauty_count_text;
	public String timeAndNums = "";
	public String cardImg;
	public String cardBgImg;
	public String petCard=null;
	public boolean isOpen = false;
	public ArrayList<packageDetails> pkdes = new ArrayList<packageDetails>();
	
	
	
	public static Cards json2Entity(JSONObject json){
		Cards cards = new Cards();
		try {
			JSONObject objectCards = json;
			if (objectCards.has("petCard")&&!objectCards.isNull("petCard")) {
				cards.petCard = objectCards.getString("petCard");
			}
			if (objectCards.has("cardName")&&!objectCards.isNull("cardName")) {
				cards.cardName = objectCards.getString("cardName");
			}
			if (objectCards.has("explainText")&&!objectCards.isNull("explainText")) {
				cards.explainText = objectCards.getString("explainText");
			}
			if (json.has("cardTip")&&!objectCards.isNull("cardTip")) {
				cards.cardTip = objectCards.getString("cardTip");
			}
			if (json.has("cardImg")&&!json.isNull("cardImg")) {
				cards.cardImg = objectCards.getString("cardImg");
			}
			if (json.has("cardBgImg")&&!json.isNull("cardBgImg")) {
				cards.cardBgImg = objectCards.getString("cardBgImg");
			}
			if (objectCards.has("packageDetails")&&!json.isNull("packageDetails")) {
				JSONArray array = objectCards.getJSONArray("packageDetails");
				if (array.length()>0) {
					for (int i = 0; i < array.length(); i++) {
						packageDetails details = new packageDetails();
						JSONObject objectEva = array.getJSONObject(i);
						if (objectEva.has("id")&&!objectEva.isNull("id")) {
							details.id = objectEva.getInt("id");
						}
						if (objectEva.has("count")&&!objectEva.isNull("count")) {
							details.count = objectEva.getInt("count");
						}
						if (objectEva.has("level")&&!objectEva.isNull("level")) {
							details.level = objectEva.getInt("level");
						}
						if (objectEva.has("title")&&!objectEva.isNull("title")) {
							details.title = objectEva.getString("title");
						}
						if (objectEva.has("countTag")&&!objectEva.isNull("countTag")) {
							details.countTag = objectEva.getString("countTag");
						}
						cards.pkdes.add(details);
					}
				}
//				JSONObject objectPackageDetail = objectCards.getJSONObject("packageDetails");
//				if (objectPackageDetail.has("serviceDetails")&&!objectPackageDetail.isNull("serviceDetails")) {
//					JSONObject objectServiceDetail = objectPackageDetail.getJSONObject("serviceDetails");
//					if (objectServiceDetail.has("bath_count_text")&&!objectServiceDetail.isNull("bath_count_text")) {
//						cards.bath_count_text = objectServiceDetail.getString("bath_count_text");
//					}
//					if (objectServiceDetail.has("beauty_count_text")&&!objectServiceDetail.isNull("beauty_count_text")) {
//						cards.beauty_count_text = objectServiceDetail.getString("beauty_count_text");
//					}
//					if (objectServiceDetail.has("beauty_level_text")&&!objectServiceDetail.isNull("beauty_level_text")) {
//						cards.beauty_level_text = objectServiceDetail.getString("beauty_level_text");
//					}
//					if (objectServiceDetail.has("bath_level_text")&&!objectServiceDetail.isNull("bath_level_text")) {
//						cards.bath_level_text = objectServiceDetail.getString("bath_level_text");
//					}
//				}
				
//				if (objectPackageDetail.has("usedDetails")&&!objectPackageDetail.isNull("usedDetails")) {
//					JSONArray arrayUserDetail = objectPackageDetail.getJSONArray("usedDetails");
//					if (arrayUserDetail.length()>0) {
//						StringBuilder builder = new StringBuilder();
//						for (int j = 0; j < arrayUserDetail.length(); j++) {
//							JSONObject objectUserDetail = arrayUserDetail.getJSONObject(j);
//							if (objectUserDetail.has("appointment")&&!objectUserDetail.isNull("appointment")) {
//								if (objectUserDetail.has("usedText")&&!objectUserDetail.isNull("usedText")) {
//									if (j==arrayUserDetail.length()-1) {
//										builder.append(objectUserDetail.getString("appointment")+"		"+objectUserDetail.getString("usedText"));
//									}else {
//										builder.append(objectUserDetail.getString("appointment")+"		"+objectUserDetail.getString("usedText")+"\n\n");
//									}
//								}
//							}
//						}
//						cards.timeAndNums = builder.toString();
//					}
//				}
			}
			if (objectCards.has("usedDetails")&& !objectCards.isNull("usedDetails")) {
				JSONArray arrayUserDetail = objectCards.getJSONArray("usedDetails");
				if (arrayUserDetail.length() > 0) {
					StringBuilder builder = new StringBuilder();
					for (int j = 0; j < arrayUserDetail.length(); j++) {
						JSONObject objectUserDetail = arrayUserDetail.getJSONObject(j);
						if (objectUserDetail.has("appointment")&& !objectUserDetail.isNull("appointment")) {
							if (objectUserDetail.has("usedText")&& !objectUserDetail.isNull("usedText")) {
								if (j == arrayUserDetail.length() - 1) {
									builder.append(objectUserDetail.getString("appointment")+ "		"+ objectUserDetail.getString("usedText"));
								} else {
									builder.append(objectUserDetail.getString("appointment")+ "		"+ objectUserDetail.getString("usedText")+ "\n\n");
								}
							}
						}
					}
					cards.timeAndNums = builder.toString();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cards;
		
	}
}
