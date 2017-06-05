package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.pet.util.Utils;

public class Rechar implements Serializable{

	public int activityNum;//活动剩余数量
	public String endDate; //活动结束时间
	public String created;//创建时间
	public String title;//无活动文案
	public int activityBonus;//活动赠送金额
	public String activityTitle;//活动文案
	public String recharge;//充值金额
	public int instantBonus;//无活动赠送金额
	public String cardDesc;//有效期
	public int isHot;//是否热门：1是，0否
	public String startDate;//活动开始时间 
	public int tempType;
	public int RechargeTemplateId;//id--貌似关联模板需要
	public String surplus;////剩余多少”天/个”
	public String caption;//详情用的活动名
//	public String desc;//描述
	public List<String> listDesc = new ArrayList<String>();
	public List<String> listIntro = new ArrayList<String>();
	public String memberGrowthValue;//赠送的成长值
	public static Rechar json2Entity(JSONObject json){
		Rechar rechar = new Rechar();
		try {
			if (json.has("activityNum")&&!json.isNull("activityNum")) {
				rechar.activityNum=json.getInt("activityNum");
			}
			if (json.has("endDate")&&!json.isNull("endDate")) {
				rechar.endDate=json.getString("endDate");
			}
			if (json.has("startDate")&&!json.isNull("startDate")) {
				rechar.startDate=json.getString("startDate");
			}
			if (json.has("created")&&!json.isNull("created")) {
				rechar.created=json.getString("created");
			}
			if (json.has("title")&&!json.isNull("title")) {
				rechar.title=json.getString("title");
			}
			if (json.has("activityBonus")&&!json.isNull("activityBonus")) {
				rechar.activityBonus=json.getInt("activityBonus");
			}
			if (json.has("activityTitle")&&!json.isNull("activityTitle")) {
				rechar.activityTitle=json.getString("activityTitle");
			}
			if (json.has("recharge")&&!json.isNull("recharge")) {
				rechar.recharge=json.getString("recharge");
			}
			if (json.has("instantBonus")&&!json.isNull("instantBonus")) {
				rechar.instantBonus=json.getInt("instantBonus");
			}
			if (json.has("cardDesc")&&!json.isNull("cardDesc")) {
				rechar.cardDesc=json.getString("cardDesc");
			}
			if (json.has("isHot")&&!json.isNull("isHot")) {
				rechar.isHot=json.getInt("isHot");
			}
			if (json.has("tempType")&&!json.isNull("tempType")) {
				rechar.tempType=json.getInt("tempType");
			}
			if (json.has("RechargeTemplateId")&&!json.isNull("RechargeTemplateId")) {
				rechar.RechargeTemplateId=json.getInt("RechargeTemplateId");
			}
			if (json.has("surplus")&&!json.isNull("surplus")) {
				rechar.surplus=json.getString("surplus");
			}
			if (json.has("caption")&&!json.isNull("caption")) {
				rechar.caption=json.getString("caption");
			}
			if (json.has("memberGrowthValue")&&!json.isNull("memberGrowthValue")) {
				rechar.memberGrowthValue=json.getString("memberGrowthValue");
			}
			if (json.has("desc")&&!json.isNull("desc")) {
				JSONArray array = json.getJSONArray("desc");
				for (int i = 0; i < array.length(); i++) {
					rechar.listDesc.add(array.getString(i));
				}
			}
			if (json.has("intro")&&!json.isNull("intro")) {
				JSONArray array = json.getJSONArray("intro");
				for (int i = 0; i < array.length(); i++) {
					rechar.listIntro.add(array.getString(i));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rechar;
	}
}
