package com.haotang.pet.entity;

import java.io.Serializable;

public class Fostercare implements Serializable{
	public int petid;
	public int petkind;
	public int customerpetid;
	public String petname;
	public String customerpetname;
	public String image;
	
	public int addrid;
	public String addr;
	public double lat;
	public double lng;
	
	public int shopid;
	public String shopname;
	
	public String startdate;
	public String enddate;
	public int daynum;
	
	public int roomid;
	public String roomname;
	public double roomprice;
	public double roomlistprice;
	public String roomdes;
	public String roomimg;
	public boolean roomvalid;
	public int transferId;
	public String roomSize;
}
