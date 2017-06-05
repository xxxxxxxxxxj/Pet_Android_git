package com.haotang.pet.entity;

public class MDate {
	public String date;
	public String day;
	public String holiday;
	public int week=-1;
	public int selectedindex=-1;//1为入住日期  2为离店日期
	public boolean valid;//是否可用
	public boolean isselected;//是否选中
	public boolean isselectedmiddle;//是否选中日期
}
