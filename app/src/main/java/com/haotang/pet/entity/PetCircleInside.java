package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PetCircleInside implements Serializable{
	public String userName;
	public String avatar;
	public int userId;
	public int groupId;
	public int PostInfoId;
	public String created;//帖子创建时间
	public String content;//
	public int duty;//1 店长 2 区域经理
	public List<String> list = new ArrayList<String>();//帖子列表里边的图片
	public List<String> smallImgsList = new ArrayList<String>();//帖子列表里边的小图
	public String shareurl;
	public int commentAmount;//评价数；
	public String title;//来自宠物家单子评价
	public String memberIcon;//用户是会员的话 设置会员等级img url;
}
