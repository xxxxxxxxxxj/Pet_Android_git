package com.haotang.pet.entity;

import java.util.List;

/**
 * <p>
 * Title:LiveServicesItemBean
 * </p>
 * <p>
 * Description:直播详情页底部服务菜单信息实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-9-15 下午3:24:01
 */
public class LiveServicesItemBean {
	private List<DataBean> data;

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean {
		private String icon;
		private String name;
		private String type;
		/**
		 * serviceIds : [1,3] serviceType : 1
		 */

		private InfoBean info;
		private int MenuItemId;

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public InfoBean getInfo() {
			return info;
		}

		public void setInfo(InfoBean info) {
			this.info = info;
		}

		public int getMenuItemId() {
			return MenuItemId;
		}

		public void setMenuItemId(int MenuItemId) {
			this.MenuItemId = MenuItemId;
		}

		public static class InfoBean {
			private int type;
			private int serviceType;
			private List<Integer> serviceIds;

			public int getServiceType() {
				return serviceType;
			}

			public int getType() {
				return type;
			}

			public void setType(int type) {
				this.type = type;
			}

			public void setServiceType(int serviceType) {
				this.serviceType = serviceType;
			}

			public List<Integer> getServiceIds() {
				return serviceIds;
			}

			public void setServiceIds(List<Integer> serviceIds) {
				this.serviceIds = serviceIds;
			}
		}
	}
}
