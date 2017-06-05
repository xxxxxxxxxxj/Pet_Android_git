package com.haotang.pet.adapter;

import java.util.List;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class TimeAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;
	private LayoutInflater mInflater;
	private int width;
	public TimeAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}
	public void setWidth(int width){
		this.width = width;
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
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = mInflater.inflate(R.layout.timeitem, null);
		TextView tv = (TextView) v.findViewById(R.id.tv_timeitem_time);
		android.view.ViewGroup.LayoutParams lp = tv.getLayoutParams();
		lp.width = width/5;
		lp.height = width/5;
		tv.setLayoutParams(lp);
		tv.setText(list.get(position));
		
		return v;
	}

}
