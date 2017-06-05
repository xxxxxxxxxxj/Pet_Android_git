package com.haotang.pet.entity;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * <p>
 * id:OrderPayScanInfo
 * </p>
 * <p>
 * Description:扫码支付商品信息数据实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-11-24 下午3:46:31
 */
public class OrderPayScanInfo implements Serializable{
	public int amount;
	public int id;
	public String item;
	public double price;

	public static OrderPayScanInfo json2Entity(JSONObject jobj) {
		OrderPayScanInfo orderPayScanInfo = new OrderPayScanInfo();
		try {
			if (jobj.has("amount") && !jobj.isNull("amount")) {
				orderPayScanInfo.amount = jobj.getInt("amount");
			}
			if (jobj.has("id") && !jobj.isNull("id")) {
				orderPayScanInfo.id = jobj.getInt("id");
			}
			if (jobj.has("item") && !jobj.isNull("item")) {
				orderPayScanInfo.item = jobj.getString("item");
			}
			if (jobj.has("price") && !jobj.isNull("price")) {
				orderPayScanInfo.price = jobj.getDouble("price");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderPayScanInfo;
	}
}
