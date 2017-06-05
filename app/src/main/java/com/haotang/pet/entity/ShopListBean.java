package com.haotang.pet.entity;

import java.util.List;

public class ShopListBean {
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
		private java.util.List<RegionsBean> regions;

		public List<RegionsBean> getRegions() {
			return regions;
		}

		public void setRegions(List<RegionsBean> regions) {
			this.regions = regions;
		}

		public static class RegionsBean {
			private int areacode;
			private String region;
			private java.util.List<ShopsBean> shops;

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
				private String address;
				private int areaId;
				private String dist;
				private String img;
				private int ShopId;
				private String shopName;
				private String shopPrice;
				private int status;
				private String phone;
				private List<String> hotelImg;

				public int getShopId() {
					return ShopId;
				}

				public void setShopId(int shopId) {
					ShopId = shopId;
				}

				public String getPhone() {
					return phone;
				}

				public void setPhone(String phone) {
					this.phone = phone;
				}

				public List<String> getHotelImg() {
					return hotelImg;
				}

				public void setHotelImg(List<String> hotelImg) {
					this.hotelImg = hotelImg;
				}

				public String getAddress() {
					return address;
				}

				public void setAddress(String address) {
					this.address = address;
				}

				public int getAreaId() {
					return areaId;
				}

				public void setAreaId(int areaId) {
					this.areaId = areaId;
				}

				public String getDist() {
					return dist;
				}

				public void setDist(String dist) {
					this.dist = dist;
				}

				public String getImg() {
					return img;
				}

				public void setImg(String img) {
					this.img = img;
				}

				public String getShopName() {
					return shopName;
				}

				public void setShopName(String shopName) {
					this.shopName = shopName;
				}

				public String getShopPrice() {
					return shopPrice;
				}

				public void setShopPrice(String shopPrice) {
					this.shopPrice = shopPrice;
				}

				public int getStatus() {
					return status;
				}

				public void setStatus(int status) {
					this.status = status;
				}
			}
		}
	}
}
