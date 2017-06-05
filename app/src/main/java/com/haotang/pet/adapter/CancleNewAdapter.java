package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.CancleReson;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class CancleNewAdapter<T> extends CommonAdapter<T>{

	public int pos =-1;
	public CancleNewAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}
	
	public void setPostition(int pos){
		this.pos=pos;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CancleReson reson = (CancleReson) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.cancle_adapter_choose, position);
		viewHolder.setText(R.id.textView_item_cancle_title,reson.txt);
		viewHolder.setTextColor(R.id.textView_item_cancle_title,"#666666");
		viewHolder.getView(R.id.imageView_item_cancle_state).setVisibility(View.GONE);
		if (position==pos) {
			viewHolder.getView(R.id.imageView_item_cancle_state).setVisibility(View.VISIBLE);
			viewHolder.setImageResource(R.id.imageView_item_cancle_state, R.drawable.icon_pay_selected);
		}
		return viewHolder.getConvertView();
	}

}
