package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class CardsBuy implements Serializable{
	public int CardPackageId;
	public int price;
	public String discount;
	public String title;
	public String content;
	public int listPrice;
	public int price1;
	public double petCardDiscount;
	public ArrayList<String> listStr = new ArrayList<String>();
}
