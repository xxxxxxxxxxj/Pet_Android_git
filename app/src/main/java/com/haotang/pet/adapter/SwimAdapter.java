package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.SwimIcon;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class SwimAdapter<T> extends CommonAdapter<T>{

	public SwimAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_swim_show_icon, position);
		SwimIcon swimIcon = (SwimIcon) mDatas.get(position);
//		if (swimIcon.outOrInside==2) {
		if (position==0) {
			viewHolder.setBackgroundNormal(R.id.imageView_item_swim_icon, swimIcon.picPath.replace(".png", "2.png"), R.drawable.wifi);
		}else if (position==1) {
			viewHolder.setBackgroundNormal(R.id.imageView_item_swim_icon, swimIcon.picPath.replace(".png", "2.png"), R.drawable.air_cool);
		}else if (position==2) {
			viewHolder.setBackgroundNormal(R.id.imageView_item_swim_icon, swimIcon.picPath.replace(".png", "2.png"), R.drawable.aired_cool);
		}else if (position==3) {
			viewHolder.setBackgroundNormal(R.id.imageView_item_swim_icon, swimIcon.picPath.replace(".png", "2.png"), R.drawable.park);
		}else if (position==4) {
			viewHolder.setBackgroundNormal(R.id.imageView_item_swim_icon, swimIcon.picPath.replace(".png", "2.png"), R.drawable.sleeping);
		}
//		}
//		else if (swimIcon.outOrInside==1) {
//			if (position==0) {
//				viewHolder.setBackgroundNormal(R.id.imageView_item_swim_icon, swimIcon.picPath.replace(".png", "1.png"), R.drawable.wifi);
//				Utils.mLogError("==-->swimIcon 1 "+swimIcon.picPath.replace(".png", "1.png"));
//			}else if (position==1) {
//				viewHolder.setBackgroundNormal(R.id.imageView_item_swim_icon, swimIcon.picPath.replace(".png", "1.png"), R.drawable.air_cool);
//			}else if (position==2) {
//				viewHolder.setBackgroundNormal(R.id.imageView_item_swim_icon, swimIcon.picPath.replace(".png", "1.png"), R.drawable.aired_cool);
//			}else if (position==3) {
//				viewHolder.setBackgroundNormal(R.id.imageView_item_swim_icon, swimIcon.picPath.replace(".png", "1.png"), R.drawable.park);
//			}else if (position==4) {
//				viewHolder.setBackgroundNormal(R.id.imageView_item_swim_icon, swimIcon.picPath.replace(".png", "1.png"), R.drawable.sleeping);
//			}
//		}
		viewHolder.setText(R.id.textView_item_swim_detail, swimIcon.name);
		return viewHolder.getConvertView();
	}

}
