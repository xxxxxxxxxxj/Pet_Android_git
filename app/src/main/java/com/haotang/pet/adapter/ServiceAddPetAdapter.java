package com.haotang.pet.adapter;

import java.util.ArrayList;

import com.haotang.pet.R;
import com.haotang.pet.entity.AddServiceItem;
import com.haotang.pet.util.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceAddPetAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<AddServiceItem> list;
	private LayoutInflater mInflater;
	
	public ServiceAddPetAdapter(Context context, ArrayList<AddServiceItem> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if(convertView==null){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.serviceaddpetitem, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_serviceaddpetitem_name);
			holder.tvFee = (TextView) convertView.findViewById(R.id.tv_serviceaddpetitem_fee);
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_serviceaddpetitem);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		if(list.get(position).isChecked)
			holder.iv.setBackgroundResource(R.drawable.icon_pay_selected);
		else
			holder.iv.setBackgroundResource(R.drawable.icon_pay_normal);
		holder.tvName.setText(list.get(position).name);
		holder.tvFee.setText("¥"+Utils.formatDouble(list.get(position).price));
		return convertView;
	}
	
	class Holder{
		TextView tvName;
		TextView tvFee;
		ImageView iv;
	}

}
