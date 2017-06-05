package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.pet.util.Utils;

public class Blance implements Serializable {
//	public String amount;//金额 
//	public int TradeHistoryId;//
//	public String item;//title
//	public String status;//状态描述
//	public String tradeDate;//时间
//	public String refundDate;//时间
//	public String tradeNo;//单号
//	public String payWay;//支付方式
//	public int tradeType;//时间
//	public String refundAmount;//退款金额
	
	
	public String item;//迷你雪纳瑞洗护套餐
	public String amount;//金额
	public String tradeNo;//单号
	public String tradeDate;//时间
	public String state;//状态
	public String desc;//支付方式
	public String icon;//图标
	public boolean isOpen = false;
	public List<OtherGoods> list = new ArrayList<OtherGoods>();
	public boolean isRedOn = false;
	public static Blance json2Entity(JSONObject json){
		Blance blance = new Blance();
		try {
			if (json.has("amount")&&!json.isNull("amount")) {
				blance.amount=json.getString("amount");
			}
			if (json.has("item")&&!json.isNull("item")) {
				blance.item=json.getString("item");
			}
			if (json.has("state")&&!json.isNull("state")) {
				blance.state=json.getString("state");
			}
			if (json.has("desc")&&!json.isNull("desc")) {
				blance.desc=json.getString("desc");
			}
			if (json.has("icon")&&!json.isNull("icon")) {
				blance.icon=json.getString("icon");
			}
			if (json.has("tradeDate")&&!json.isNull("tradeDate")) {
				blance.tradeDate=json.getString("tradeDate");
			}
			if (json.has("tradeNo")&&!json.isNull("tradeNo")) {
				blance.tradeNo=json.getString("tradeNo");
			}
			if (json.has("isRedOn")&&!json.isNull("isRedOn")) {
				blance.isRedOn = json.getBoolean("isRedOn");
			}else {
				blance.isRedOn = false;
			}
			if (json.has("detail")&&!json.isNull("detail")) {
				JSONObject objectDetail = json.getJSONObject("detail");
				String shopName ="";
				if (objectDetail.has("shop")&&!objectDetail.isNull("shop")) {
					shopName = objectDetail.getString("shop");
				}
				if (objectDetail.has("items")&&!objectDetail.isNull("items")) {
					JSONArray array = objectDetail.getJSONArray("items");
					if (array.length()>0) {
						for (int i = 0; i < array.length(); i++) {
							OtherGoods goods = new OtherGoods();
							JSONObject objectEvery = array.getJSONObject(i);
							goods.shopName=shopName;
							if (objectEvery.has("name")&&!objectEvery.isNull("name")) {
								goods.name = objectEvery.getString("name");
							}
							if (objectEvery.has("price")&&!objectEvery.isNull("price")) {
								goods.money = objectEvery.getString("price");
							}
							if (objectEvery.has("amount")&&!objectEvery.isNull("amount")) {
								goods.nums = objectEvery.getInt("amount");
							}
							blance.list.add(goods);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return blance;
		
	}
}
