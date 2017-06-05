package com.haotang.pet.entity;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * <p>
 * Title:CertiOrder
 * </p>
 * <p>
 * Description:宠物信息-狗证信息实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-7-19 下午8:05:37
 */
public class CertiOrder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 11243124L;
	public int CertiOrderId;
	public int status;
	public String[] statusName;
	public String remark;
public int certiId;
	public static CertiOrder json2Entity(JSONObject json) {
		CertiOrder certiOrder = new CertiOrder();
		try {
			if (json.has("CertiOrderId") && !json.isNull("CertiOrderId")) {
				certiOrder.CertiOrderId = json.getInt("CertiOrderId");
			}
			if (json.has("status") && !json.isNull("status")) {
				certiOrder.status = json.getInt("status");
			}
			if (json.has("remark") && !json.isNull("remark")) {
				certiOrder.remark = json.getString("remark");
			}
			if (json.has("certiId") && !json.isNull("certiId")) {
				certiOrder.certiId = json.getInt("certiId");
			}
			if (json.has("statusName") && !json.isNull("statusName")) {
				JSONArray jsonArray = json.getJSONArray("statusName");
				String[] str = new String[jsonArray.length()];
				for (int i = 0; i < jsonArray.length(); i++) {
					str[i] = jsonArray.getString(i);
				}
				certiOrder.statusName = str;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return certiOrder;
	}
}
