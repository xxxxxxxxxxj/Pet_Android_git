package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Utils;

public class PetService implements Serializable{
	public int addrtype = 0;//上门还是门店服务2：上门，1:门店
	public int isDefaultWorker = 0;//是否为系统推荐的美容师0：是 1：否
	public boolean ispickup;//是否接送 true:接送 ，false：不接送
	public double pickupprice;//接送费用
	public String pickupopt=""; //是否显示接送，“1”为显示，其他不显示
	public boolean ispickuphot; //是否显示接送HOT标签
	public String servicetime;//客户端显示的时间
	public String servicedate;//传给后台的时间（格式YYYY-MM-dd HH:mm:ss)
	public int serviceType;
	
	public int beautician_id;
	public int beautician_sex;
	public String beautician_name;
	public int beautician_storeid;
	public int beautician_sort;//美容师分级
	public String beautician_levelname;//美容师分级名称
	public String beautician_image;
	public String beautician_sign;
	public String beautician_titlelevel;
	public int beautician_ordernum;
	public int beautician_isavail = 3;//美容师是否时间可用
	public int beautician_levels;//等级
	public int beautician_stars;//等级下的星级
	public String upgradeTip;//美容师是否即将升级
	
	public int addr_id=0;
	public double addr_lat;
	public double addr_lng;
	public String addr_detail;
	
	public String homeServiceDesc;//上门地址
	public int areaid;//上门服务范围id
	public String petstoreName;//店名
	public String petstoreAddr;//店铺地址
	public String petstorePhone;//店铺电话
	public String petstoreImg;//店铺图片
	public int petstoreid;//店铺id
	public int vieEnabled;
	public String pickupTip;// 接送提示文案
	public String restAmount;// 剩余数量
	
	
}
