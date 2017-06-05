package com.haotang.pet.entity;

import java.util.List;

/**
 * <p>
 * Title:PetCardInfoCodeBean
 * </p>
 * <p>
 * Description:订单信息实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-7-19 上午10:47:09
 */
public class PetCardInfoCodeBean {
	private String tradeNo;
	private double totalPrice;
	private String orderNum;
	private int payWay;
	private String remark;
	private String updateTime;
	private int debitAmount;
	private int userId;
	private int userPetId;
	private String createTime;
	private int isUnitedPay;
	private int isDel;
	private int certiId;
	private int status;
	private int CertiOrderId;
	private String certiCouponDe;
	private List<CertiCouponsBean> certiCoupons;

	public String getCertiCouponDe() {
		return certiCouponDe;
	}

	public void setCertiCouponDe(String certiCouponDe) {
		this.certiCouponDe = certiCouponDe;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public int getPayWay() {
		return payWay;
	}

	public void setPayWay(int payWay) {
		this.payWay = payWay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(int debitAmount) {
		this.debitAmount = debitAmount;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserPetId() {
		return userPetId;
	}

	public void setUserPetId(int userPetId) {
		this.userPetId = userPetId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getIsUnitedPay() {
		return isUnitedPay;
	}

	public void setIsUnitedPay(int isUnitedPay) {
		this.isUnitedPay = isUnitedPay;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public int getCertiId() {
		return certiId;
	}

	public void setCertiId(int certiId) {
		this.certiId = certiId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCertiOrderId() {
		return CertiOrderId;
	}

	public void setCertiOrderId(int CertiOrderId) {
		this.CertiOrderId = CertiOrderId;
	}

	public List<CertiCouponsBean> getCertiCoupons() {
		return certiCoupons;
	}

	public void setCertiCoupons(List<CertiCouponsBean> certiCoupons) {
		this.certiCoupons = certiCoupons;
	}

	public static class CertiCouponsBean {
		private String couponInfo;
		private double price;
		private String description;
		private int CertiCouponId;
		private int fee;
		private double reduce;
		private String caption;

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public int getFee() {
			return fee;
		}

		public void setFee(int fee) {
			this.fee = fee;
		}

		public double getReduce() {
			return reduce;
		}

		public void setReduce(double reduce) {
			this.reduce = reduce;
		}

		public String getCouponInfo() {
			return couponInfo;
		}

		public void setCouponInfo(String couponInfo) {
			this.couponInfo = couponInfo;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public int getCertiCouponId() {
			return CertiCouponId;
		}

		public void setCertiCouponId(int CertiCouponId) {
			this.CertiCouponId = CertiCouponId;
		}
	}
}
