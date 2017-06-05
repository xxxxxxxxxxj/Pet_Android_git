package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class BeauToAppointmentAdapter<T> extends CommonAdapter<T>{

	public BeauToAppointmentAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Beautician beautician = (Beautician) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,R.layout.item_beau_to_appointment, position);  
		viewHolder.setText(R.id.textViewbeau_service_content, beautician.BeauDetailName);
		viewHolder.setBackgroundNormal(R.id.imageView_beau_service_icon, beautician.BeauDetailIcon, R.drawable.beau_to_apponiment);
		return viewHolder.getConvertView();
	}

}
