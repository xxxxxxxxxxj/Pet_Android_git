package com.haotang.pet.entity;

import java.util.List;

/**
 * 启动页数据实体类
 * <p>
 * Title:StartPageBean
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-17 下午3:29:25
 */
public class StartPageBean {
	private List<StartPage> spcList;

	public static class StartPage {
		private String img_url;
		private String jump_url;
		private String version;

		public String getImg_url() {
			return img_url;
		}

		public void setImg_url(String img_url) {
			this.img_url = img_url;
		}

		public String getJump_url() {
			return jump_url;
		}

		public void setJump_url(String jump_url) {
			this.jump_url = jump_url;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}
	}

	public List<StartPage> getSpcList() {
		return spcList;
	}

	public void setSpcList(List<StartPage> spcList) {
		this.spcList = spcList;
	}
}
