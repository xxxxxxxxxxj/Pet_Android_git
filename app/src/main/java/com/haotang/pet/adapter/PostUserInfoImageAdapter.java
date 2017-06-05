package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class PostUserInfoImageAdapter<T> extends CommonAdapter<T> {
	public PostUserInfoImageAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_post_userinfoimage, position);
		try {
			viewHolder.setBackgroundNormal(R.id.iv_item_postuserinfoimage,
					mDatas.get(position).toString(),
					R.drawable.icon_production_default);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewHolder.getConvertView();
	}
}
