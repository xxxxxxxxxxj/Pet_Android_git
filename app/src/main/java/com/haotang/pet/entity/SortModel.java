package com.haotang.pet.entity;

public class SortModel {

	private String name;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
	private String avatarPath;//设置显示的头像地址
//	private String created;//创建时间
	private String description;//描述
	private Integer PetId;//宠物id
	private boolean isHot;//是否热门
	private int petKind;//种类
	private String pyhead;//字母缩写
	private long [] serviceHome=null; 
	private long [] serviceShop=null; 
	private int isCerti;
	
	public String getAvatarPath() {
		return avatarPath;
	}
	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
//	public String getCreated() {
//		return created;
//	}
//	public void setCreated(String created) {
//		this.created = created;
//	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPetId() {
		return PetId;
	}
	public void setPetId(Integer petId) {
		PetId = petId;
	}
	public boolean isHot() {
		return isHot;
	}
	public void setHot(boolean isHot) {
		this.isHot = isHot;
	}
	public int getPetKind() {
		return petKind;
	}
	public void setPetKind(int petKind) {
		this.petKind = petKind;
	}
	public String getPyhead() {
		return pyhead;
	}
	public void setPyhead(String pyhead) {
		this.pyhead = pyhead;
	}
	public long[] getServiceHome() {
		return serviceHome;
	}
	public void setServiceHome(long[] serviceHome) {
		this.serviceHome = serviceHome;
	}
	public long[] getServiceShop() {
		return serviceShop;
	}
	public void setServiceShop(long[] serviceShop) {
		this.serviceShop = serviceShop;
	}
	public int getIsCerti() {
		return isCerti;
	}
	public void setIsCerti(int isCerti) {
		this.isCerti = isCerti;
	}
	
	
	
}
