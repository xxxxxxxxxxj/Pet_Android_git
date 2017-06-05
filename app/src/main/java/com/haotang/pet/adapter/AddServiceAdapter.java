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

public class AddServiceAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<AddServiceItem> list;
	private LayoutInflater mInflater;
	
	public AddServiceAdapter(Context context, ArrayList<AddServiceItem> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(convertView==null){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.addserviceitem, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_addserviceitem);
			holder.tv = (TextView) convertView.findViewById(R.id.tv_addserviceitem);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		if(list.get(position).isChecked){
			holder.iv.setBackgroundResource(R.drawable.complaint_reason);
		}else{
			holder.iv.setBackgroundResource(R.drawable.bg_whitecircle_grayborder);
		}
		holder.tv.setText(list.get(position).name+"¥"+Utils.formatDouble(list.get(position).price));
		return convertView;
	}
	
	class Holder{
		ImageView iv;
		TextView tv;
	}

}
