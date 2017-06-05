package com.haotang.pet.entity;

import java.util.List;

public class TimeListCodeBean {
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
		private String vieEnableTip;
		private String desc;
		private java.util.List<SelectionBean> selection;
		private String isFullTip;

		public String getIsFullTip() {
			return isFullTip;
		}

		public void setIsFullTip(String isFullTip) {
			this.isFullTip = isFullTip;
		}

		public String getVieEnableTip() {
			return vieEnableTip;
		}

		public void setVieEnableTip(String vieEnableTip) {
			this.vieEnableTip = vieEnableTip;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public List<SelectionBean> getSelection() {
			return selection;
		}

		public void setSelection(List<SelectionBean> selection) {
			this.selection = selection;
		}

		public static class SelectionBean {
			private String date;
			private int year;
			private int isFull;
			private String desc;
			private java.util.List<TimesBean> times;

			public String getDate() {
				return date;
			}

			public void setDate(String date) {
				this.date = date;
			}

			public int getYear() {
				return year;
			}

			public void setYear(int year) {
				this.year = year;
			}

			public int getIsFull() {
				return isFull;
			}

			public void setIsFull(int isFull) {
				this.isFull = isFull;
			}

			public String getDesc() {
				return desc;
			}

			public void setDesc(String desc) {
				this.desc = desc;
			}

			public List<TimesBean> getTimes() {
				return times;
			}

			public void setTimes(List<TimesBean> times) {
				this.times = times;
			}

			public static class TimesBean {
				private String time;
				private java.util.List<Integer> workers;

				public java.util.List<Integer> getWorkers() {
					return workers;
				}

				public void setWorkers(java.util.List<Integer> workers) {
					this.workers = workers;
				}

				public String getTime() {
					return time;
				}

				public void setTime(String time) {
					this.time = time;
				}
			}
		}
	}
}
