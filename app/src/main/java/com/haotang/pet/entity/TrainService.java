package com.haotang.pet.entity;

import java.util.ArrayList;
import java.util.List;

public class TrainService{//训练那些个预约服务条目

	public String type;//服务类型
	public String icon;//图片
	public boolean isOpen = false;
	public List<TrainServiceEvery> everyTrainServiceList = new ArrayList<TrainServiceEvery>();
}
