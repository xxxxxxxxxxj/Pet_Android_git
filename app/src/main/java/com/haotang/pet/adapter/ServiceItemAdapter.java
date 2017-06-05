package com.haotang.pet.adapter;

import java.util.ArrayList;

import com.haotang.pet.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceItemAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> list;
	private LayoutInflater mInflater;
	
	public ServiceItemAdapter(Context context, ArrayList<String> list) {
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
			convertView = mInflater.inflate(R.layout.serviceitem, null);
			holder.iv = (TextView) convertView.findViewById(R.id.iv_servicedetail_serviceitems);
			holder.tv = (TextView) convertView.findViewById(R.id.tv_servicedetail_serviceitems);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.tv.setText(list.get(position));
		if (position%4==0) {
			holder.iv.setVisibility(View.GONE);
		}else {
			holder.iv.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	class Holder{
		TextView iv;
		TextView tv;
	}

}
