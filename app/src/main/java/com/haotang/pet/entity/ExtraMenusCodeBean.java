package com.haotang.pet.entity;

import java.util.List;

/**
 * <p>
 * Title:ExtraMenusCodeBean
 * </p>
 * <p>
 * Description:个人中心附加菜单数据实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-7-22 上午11:59:09
 */
public class ExtraMenusCodeBean {
	/**
	 * code : 0 data :
	 * [{"text":"办理狗证","url":"http://m.cwjia.cn/web/petcerti/register"}] msg :
	 * 操作成功
	 */

	private int code;
	private String msg;
	/**
	 * text : 办理狗证 url : http://m.cwjia.cn/web/petcerti/register
	 */

	private List<DataBean> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean {
		private String text;
		private String url;
		private String tag;

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
}
