package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.CardItems;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class OrderCardnumsAdapter<T> extends CommonAdapter<T>{

	public OrderCardnumsAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CardItems cardItems = (CardItems) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_package_detail, position);
		viewHolder.setText(R.id.textview_left, cardItems.title);
		viewHolder.setText(R.id.textview_right, cardItems.countTag);
		return viewHolder.getConvertView();
	}

}
