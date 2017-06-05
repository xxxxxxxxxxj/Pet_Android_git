package com.haotang.pet.entity;

import java.util.List;

/**
 * <p>
 * Title:SingelAreaBean
 * </p>
 * <p>
 * Description:获取距离用户最近的商圈接口实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-17 下午4:58:00
 */
public class SingelAreaBean {
	private List<AreaBean> area;

	public List<AreaBean> getArea() {
		return area;
	}


	public void setArea(List<AreaBean> area) {
		this.area = area;
	}


	public static class AreaBean {
		private int areaId;
		private String points;
		private String region;
		private int shopId;
		private String shopName;
		private int areacode;
		private int toBeOpen;
		private String toBeOpenTxt;

		public int getToBeOpen() {
			return toBeOpen;
		}

		public void setToBeOpen(int toBeOpen) {
			this.toBeOpen = toBeOpen;
		}

		public String getToBeOpenTxt() {
			return toBeOpenTxt;
		}

		public void setToBeOpenTxt(String toBeOpenTxt) {
			this.toBeOpenTxt = toBeOpenTxt;
		}

		public int getAreacode() {
			return areacode;
		}

		public void setAreacode(int areacode) {
			this.areacode = areacode;
		}

		public int getAreaId() {
			return areaId;
		}

		public void setAreaId(int areaId) {
			this.areaId = areaId;
		}

		public String getPoints() {
			return points;
		}

		public void setPoints(String points) {
			this.points = points;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public int getShopId() {
			return shopId;
		}

		public void setShopId(int shopId) {
			this.shopId = shopId;
		}

		public String getShopName() {
			return shopName;
		}

		public void setShopName(String shopName) {
			this.shopName = shopName;
		}
	}
}
