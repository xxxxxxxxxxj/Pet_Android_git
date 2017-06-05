package com.haotang.pet.entity;

import java.util.List;

public class ComplaintHistory {
	private int code;
	private DataBean data;
	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static class DataBean {
		private java.util.List<FeedbacksBean> feedbacks;

		public List<FeedbacksBean> getFeedbacks() {
			return feedbacks;
		}

		public void setFeedbacks(List<FeedbacksBean> feedbacks) {
			this.feedbacks = feedbacks;
		}

		public static class FeedbacksBean {
			private List<String> reason;
			private int pay_price;
			private String typeName;
			private String appointment;
			private String avatar;
			private int type;
			private String managerTime;
			private String content;
			private int pick_up;
			private String managerStatus;
			private String service;
			private String serviceLocName;
			private int serviceLoc;
			private int id;
			private String orderNum;
			private String managerInfo;
			private String managerStatusName;

			public String getManagerStatusName() {
				return managerStatusName;
			}

			public void setManagerStatusName(String managerStatusName) {
				this.managerStatusName = managerStatusName;
			}

			public String getManagerInfo() {
				return managerInfo;
			}

			public void setManagerInfo(String managerInfo) {
				this.managerInfo = managerInfo;
			}

			public String getOrderNum() {
				return orderNum;
			}

			public void setOrderNum(String orderNum) {
				this.orderNum = orderNum;
			}

			public List<String> getReason() {
				return reason;
			}

			public void setReason(List<String> reason) {
				this.reason = reason;
			}

			public int getPay_price() {
				return pay_price;
			}

			public void setPay_price(int pay_price) {
				this.pay_price = pay_price;
			}

			public String getTypeName() {
				return typeName;
			}

			public void setTypeName(String typeName) {
				this.typeName = typeName;
			}

			public String getAppointment() {
				return appointment;
			}

			public void setAppointment(String appointment) {
				this.appointment = appointment;
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

			public String getManagerTime() {
				return managerTime;
			}

			public void setManagerTime(String managerTime) {
				this.managerTime = managerTime;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}

			public int getPick_up() {
				return pick_up;
			}

			public void setPick_up(int pick_up) {
				this.pick_up = pick_up;
			}

			public String getManagerStatus() {
				return managerStatus;
			}

			public void setManagerStatus(String managerStatus) {
				this.managerStatus = managerStatus;
			}

			public String getService() {
				return service;
			}

			public void setService(String service) {
				this.service = service;
			}

			public String getServiceLocName() {
				return serviceLocName;
			}

			public void setServiceLocName(String serviceLocName) {
				this.serviceLocName = serviceLocName;
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
		}
	}
}
