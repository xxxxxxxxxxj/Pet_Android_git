package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class ExpressAdapter<T> extends CommonAdapter<T>{

	public ExpressAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_express_order, position);
		MulPetService petService = (MulPetService) mDatas.get(position);
		Utils.mLogError("==-->aaaa"+CommUtil.getServiceNobacklash()+petService.petimage);
		viewHolder.setBackgroundNormal(R.id.dog_item_icon,petService.petimage, 0);
		if (petService.petCustomerId>0) {
			viewHolder.setText(R.id.textView_dog_service_name, petService.petCustomerName+petService.serviceName);
		}else {
			viewHolder.setText(R.id.textView_dog_service_name, petService.petName+petService.serviceName);
		}
		viewHolder.setText(R.id.textView_dog_service_price, "¥"+Utils.formatDouble(petService.fee)+"");
		if (position==mDatas.size()-1) {
			viewHolder.getView(R.id.view_botton_line).setVisibility(View.GONE);
		}else {
			viewHolder.getView(R.id.view_botton_line).setVisibility(View.VISIBLE);
		}
		return viewHolder.getConvertView();
	}

}
