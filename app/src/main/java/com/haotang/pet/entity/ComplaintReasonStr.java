package com.haotang.pet.entity;


public class ComplaintReasonStr {
	private String str;
    private boolean isCheck;
    
	public ComplaintReasonStr(String str, boolean isCheck) {
		super();
		this.str = str;
		this.isCheck = isCheck;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
    
}
