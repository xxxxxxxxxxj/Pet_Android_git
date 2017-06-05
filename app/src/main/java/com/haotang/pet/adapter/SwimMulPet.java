package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class SwimMulPet<T> extends CommonAdapter<T>{

	private int index;
	public SwimMulPet(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	public void setData(int index){
		this.index = index;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_swim_detail_pet, position);
		Pet pet = (Pet) mDatas.get(position);
		viewHolder.setBackgroundCircle(R.id.mul_petimg, pet.image, R.drawable.dog_icon_unnew);
		if (pet.nickName==null||pet.nickName.equals("")) {
			viewHolder.setText(R.id.textView_swim_title, pet.name.trim()+"游泳");
		}else {
			viewHolder.setText(R.id.textView_swim_title, pet.nickName.trim()+"游泳");
		}
		viewHolder.setText(R.id.textview_swim_price, Utils.formatDouble(pet.youyongPrice)+"");
		viewHolder.setText(R.id.textview_swim_price_two, Utils.formatDouble(pet.youyongPrice)+"");
		if (index==1) {
			viewHolder.getView(R.id.layout_price).setVisibility(View.GONE);
			viewHolder.getView(R.id.layout_price_two).setVisibility(View.VISIBLE);
			viewHolder.getView(R.id.imageView_arrow).setVisibility(View.GONE);
			
			TextView textView = viewHolder.getView(R.id.textView_swim_title);
			RelativeLayout.LayoutParams tv = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			tv.leftMargin=Utils.dip2px(mContext, 10);
			tv.addRule(RelativeLayout.LEFT_OF,R.id.layout_price_two);
			tv.addRule(RelativeLayout.RIGHT_OF,R.id.mul_petimg);
			tv.addRule(RelativeLayout.CENTER_VERTICAL);
			textView.setLayoutParams(tv);
		}
		return viewHolder.getConvertView();
	}

}
