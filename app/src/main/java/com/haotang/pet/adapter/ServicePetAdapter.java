package com.haotang.pet.adapter;

import java.util.ArrayList;

import com.haotang.base.SuperActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.util.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ServicePetAdapter extends BaseAdapter {
	private SuperActivity context;
	private LayoutInflater mInflater;
	private ArrayList<MulPetService> list;
	private boolean isFull;//五只宠物后不再显示最后一条线
	
	public ServicePetAdapter(SuperActivity context,
			ArrayList<MulPetService> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	public void setIsFull(boolean isFull){
		this.isFull = isFull;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if(convertView==null){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.servicepet, null);
			holder.rlLeft = (RelativeLayout) convertView.findViewById(R.id.rl_servicepet_left);
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_servicepet_icon);
			holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_servicepet_delete);
			holder.tvInfo = (TextView) convertView.findViewById(R.id.tv_servicepet_info);
			holder.tvFee = (TextView) convertView.findViewById(R.id.tv_servicepet_fee);
			holder.vBottomLine = convertView.findViewById(R.id.view_servicepet_bottom);
			
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		if(list.get(position).serviceType==1)
			holder.ivIcon.setBackgroundResource(R.drawable.icon_bath);
		else
			holder.ivIcon.setBackgroundResource(R.drawable.icon_beauty);
		if(isFull&&position==list.size()-1)
			holder.vBottomLine.setVisibility(View.GONE);
		else
			holder.vBottomLine.setVisibility(View.VISIBLE);
		holder.tvFee.setText("￥"+Utils.formatDouble(list.get(position).fee, 2));
		if(list.get(position).addServiceIds!=null&&!"".equals(list.get(position).addServiceIds)){
			if(list.get(position).petCustomerName!=null&&!"".equals(list.get(position).petCustomerName))
				holder.tvInfo.setText(list.get(position).petCustomerName+"[有单项]");
			else
				holder.tvInfo.setText(list.get(position).petName+"[有单项]");
		}else{
			if(list.get(position).petCustomerName!=null&&!"".equals(list.get(position).petCustomerName))
				holder.tvInfo.setText(list.get(position).petCustomerName);
			else
				holder.tvInfo.setText(list.get(position).petName);
		}
		
		
		return convertView;
	}
	
	class Holder{
		RelativeLayout rlLeft;
		ImageView ivIcon;
		ImageView ivDelete;
		TextView tvInfo;
		TextView tvFee;
		View vBottomLine;
	}

}
