package com.haotang.pet.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.view.CircleImageView;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

@SuppressLint("ResourceAsColor")
public class ChangeOrderBeau<T> extends CommonAdapter<T>{
	
	public ChangeOrderBeau(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Beautician beautician = (Beautician) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_change_beau, position);
		CircleImageView circleImageView = viewHolder.getView(R.id.change_img_beau);
		if (!TextUtils.isEmpty(beautician.image)) {
			try {
				if (beautician.image.startsWith("http")) {
					viewHolder.setBackgroundCircleImage(R.id.change_img_beau, beautician.image, 0);
				}else {
					viewHolder.setBackgroundCircleImage(R.id.change_img_beau, CommUtil.getServiceNobacklash()+beautician.image, 0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				viewHolder.setBackgroundCircleImage(R.id.change_img_beau, CommUtil.getServiceNobacklash()+beautician.image, 0);
			}
		}
		if (beautician.isChoose) {
			viewHolder.getView(R.id.change_img_choose).setVisibility(View.VISIBLE);
			ImageView imageView = viewHolder.getView(R.id.change_img_choose);
			imageView.setBackgroundResource(R.drawable.beautician_select);
			circleImageView.setBorderColor("#D1494F");
			TextView textView = viewHolder.getView(R.id.change_img_beau_name);
			textView.setTextColor(mContext.getResources().getColor(R.color.aD1494F));
		}else {
			circleImageView.setBorderColor("#CCCCCC");
			TextView textView = viewHolder.getView(R.id.change_img_beau_name);
			textView.setTextColor(mContext.getResources().getColor(R.color.a333333));
			viewHolder.getView(R.id.change_img_choose).setVisibility(View.VISIBLE);
			ImageView imageView = viewHolder.getView(R.id.change_img_choose);
			imageView.setBackgroundResource(R.drawable.beautician_disable);
		}
		
		viewHolder.setText(R.id.change_img_beau_name, beautician.name);
		
		return viewHolder.getConvertView();
	}
}
