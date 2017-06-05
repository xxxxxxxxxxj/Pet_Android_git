package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.UpdateEntity;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class UpdateAdapter<T> extends CommonAdapter<T>{
	private int index =-1;
	public UpdateAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}
	public void setSource(int index){
		this.index=index;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UpdateEntity updateEntity = (UpdateEntity) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_update_show, position);
		try {
			viewHolder.setText(R.id.textView_left_name, updateEntity.title);
			viewHolder.setText(R.id.textView_right_price, updateEntity.name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (index==-1) {
			viewHolder.getView(R.id.img_left_icon).setVisibility(View.GONE);
		}else if(index==1){
			viewHolder.getView(R.id.img_left_icon).setVisibility(View.VISIBLE);
		}
		return viewHolder.getConvertView();
	}

}
