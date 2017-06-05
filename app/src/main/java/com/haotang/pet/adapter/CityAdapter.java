package com.haotang.pet.adapter;

import java.util.ArrayList;

import com.haotang.pet.R;
import com.haotang.pet.entity.City;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CityAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<City> list;
	private String lastcity;
	private LayoutInflater mInflater;
	
	public CityAdapter(Context context, ArrayList<City> list,String lastcity) {
		super();
		this.context = context;
		this.list = list;
		this.lastcity = lastcity;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(convertView == null){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.city_item, null);
			holder.tvCity = (TextView) convertView.findViewById(R.id.tv_cityitem_city);
			holder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_cityitem_selected);
			holder.vline = convertView.findViewById(R.id.view_cityitem_line);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.tvCity.setText(list.get(position).name);
		if(lastcity.contains(list.get(position).name)||
				list.get(position).name.contains(lastcity)){
			holder.ivSelected.setBackgroundResource(R.drawable.icon_city_selected);
		}else{
			holder.ivSelected.setBackgroundResource(R.drawable.bk_empty);
		}
		if(position == list.size() - 1){
			holder.vline.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
	
	private class Holder{
		TextView tvCity;
		ImageView ivSelected;
		View vline;
	}

}
