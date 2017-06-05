package com.haotang.pet.entity;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * <p>
 * Title:OrdersBean
 * </p>
 * <p>
 * Description:最近订单
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-15 下午6:03:48
 */
public class OrdersBean implements Serializable {
	private int pick_up;
	private int pay_price;
	private String service;
	private String appointment;
	private int serviceLoc;
	private int id;
	private String avatar;
	private int type;
	private int managerStatus;
	private String managerStatusName;
	private String serviceLocName;
	private String typeName;
	private String orderNum;
	private boolean isComplaint = true;

	public int getPick_up() {
		return pick_up;
	}

	public void setPick_up(int pick_up) {
		this.pick_up = pick_up;
	}

	public int getPay_price() {
		return pay_price;
	}

	public void setPay_price(int pay_price) {
		this.pay_price = pay_price;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getAppointment() {
		return appointment;
	}

	public void setAppointment(String appointment) {
		this.appointment = appointment;
	}

	public int getServiceLoc() {
		return serviceLoc;
	}

	public void setServiceLoc(int serviceLoc) {
		this.serviceLoc = serviceLoc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getManagerStatus() {
		return managerStatus;
	}

	public void setManagerStatus(int managerStatus) {
		this.managerStatus = managerStatus;
	}

	public String getManagerStatusName() {
		return managerStatusName;
	}

	public void setManagerStatusName(String managerStatusName) {
		this.managerStatusName = managerStatusName;
	}

	public String getServiceLocName() {
		return serviceLocName;
	}

	public void setServiceLocName(String serviceLocName) {
		this.serviceLocName = serviceLocName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public boolean isComplaint() {
		return isComplaint;
	}

	public void setComplaint(boolean isComplaint) {
		this.isComplaint = isComplaint;
	}

	public static OrdersBean json2Entity(JSONObject json) {
		OrdersBean ordersBean = new OrdersBean();
		try {
			if (json.has("pick_up") && !json.isNull("pick_up")) {
				ordersBean.pick_up = json.getInt("pick_up");
			}
			if (json.has("pay_price") && !json.isNull("pay_price")) {
				ordersBean.pay_price = json.getInt("pay_price");
			}
			if (json.has("service") && !json.isNull("service")) {
				ordersBean.service = json.getString("service");
			}
			if (json.has("appointment") && !json.isNull("appointment")) {
				ordersBean.appointment = json.getString("appointment");
			}
			if (json.has("serviceLoc") && !json.isNull("serviceLoc")) {
				ordersBean.serviceLoc = json.getInt("serviceLoc");
			}
			if (json.has("id") && !json.isNull("id")) {
				ordersBean.id = json.getInt("id");
			}
			if (json.has("avatar") && !json.isNull("avatar")) {
				ordersBean.avatar = json.getString("avatar");
			}
			if (json.has("type") && !json.isNull("type")) {
				ordersBean.type = json.getInt("type");
			}
			if (json.has("managerStatus") && !json.isNull("managerStatus")) {
				ordersBean.managerStatus = json.getInt("managerStatus");
				ordersBean.isComplaint = false;
			} else {
				ordersBean.isComplaint = true;
			}
			if (json.has("managerStatusName")
					&& !json.isNull("managerStatusName")) {
				ordersBean.managerStatusName = json
						.getString("managerStatusName");
			}
			if (json.has("serviceLocName") && !json.isNull("serviceLocName")) {
				ordersBean.serviceLocName = json.getString("serviceLocName");
			}
			if (json.has("typeName") && !json.isNull("typeName")) {
				ordersBean.typeName = json.getString("typeName");
			}
			if (json.has("orderNum") && !json.isNull("orderNum")) {
				ordersBean.orderNum = json.getString("orderNum");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ordersBean;
	}
}
