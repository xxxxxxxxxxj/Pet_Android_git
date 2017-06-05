package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class JoinWorkAdapter<T> extends CommonAdapter<T>{
	private int screenWidth;
//	private int imageWidth;
	private int imageHeight;
	private LayoutParams lp1;
	public JoinWorkAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
		screenWidth = Utils.getDisplayMetrics((Activity)mContext)[0];
//		imageWidth = screenWidth * 15 / 35;
		imageHeight = screenWidth * 48 / 75;
//		lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, imageWidth);
		lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, imageHeight);
//		lp1.bottomMargin=Utils.dip2px(mContext, 10);
//		lp1.leftMargin=Utils.dip2px(mContext, 10);
//		lp1.rightMargin=Utils.dip2px(mContext, 10);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_join_work_adapter, position);
		ImageView img = viewHolder.getView(R.id.item_join_work_imageView);
		img.setLayoutParams(lp1);
		viewHolder.setBackgroundNormal(R.id.item_join_work_imageView,mDatas.get(position).toString(), R.drawable.icon_production_default);
		return viewHolder.getConvertView();
	}
	
}
