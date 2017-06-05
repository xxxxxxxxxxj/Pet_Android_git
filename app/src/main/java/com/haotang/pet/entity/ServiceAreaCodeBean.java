package com.haotang.pet.entity;

import java.util.List;

public class ServiceAreaCodeBean {
	private List<AreasBean> areas;
	private String introOtherArea;

	public List<AreasBean> getAreas() {
		return areas;
	}

	public void setAreas(List<AreasBean> areas) {
		this.areas = areas;
	}

	public String getIntroOtherArea() {
		return introOtherArea;
	}

	public void setIntroOtherArea(String introOtherArea) {
		this.introOtherArea = introOtherArea;
	}

	public static class AreasBean {
		private int areacode;
		private String region;
		private List<ShopsBean> shops;

		public int getAreacode() {
			return areacode;
		}

		public void setAreacode(int areacode) {
			this.areacode = areacode;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public List<ShopsBean> getShops() {
			return shops;
		}

		public void setShops(List<ShopsBean> shops) {
			this.shops = shops;
		}

		public static class ShopsBean {
			private int areaId;
			private String points;
			private int shopId;
			private String shopName;
			private int toBeOpen;
			private String toBeOpenTxt;
			private String desc1;
			private double lng;
			private double lat;

			public double getLng() {
				return lng;
			}

			public void setLng(double lng) {
				this.lng = lng;
			}

			public double getLat() {
				return lat;
			}

			public void setLat(double lat) {
				this.lat = lat;
			}

			public String getDesc1() {
				return desc1;
			}

			public void setDesc1(String desc1) {
				this.desc1 = desc1;
			}

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
}
