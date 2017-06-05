package com.haotang.pet.entity;

public class MemberLevelDes {
	private String no;
	private String title;
	private String des;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public MemberLevelDes(String no, String title, String des) {
		super();
		this.no = no;
		this.title = title;
		this.des = des;
	}
}
