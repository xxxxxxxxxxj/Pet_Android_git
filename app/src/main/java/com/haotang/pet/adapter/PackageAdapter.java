package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.packageDetails;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class PackageAdapter<T> extends CommonAdapter<T>{

	public PackageAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		packageDetails details = (packageDetails) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_package_detail, position);
		viewHolder.setText(R.id.textview_left, details.title);
		viewHolder.setText(R.id.textview_right, details.countTag);
		return viewHolder.getConvertView();
	}

}
