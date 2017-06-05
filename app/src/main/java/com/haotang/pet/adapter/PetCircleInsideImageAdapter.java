package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class PetCircleInsideImageAdapter<T> extends CommonAdapter<T>{

	public PetCircleInsideImageAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_pet_circle_inside_image, position);
		try {
			viewHolder.setBackgroundNormal(R.id.imageView_petcircle, mDatas.get(position).toString(), R.drawable.icon_production_default);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewHolder.getConvertView();
	}

}
