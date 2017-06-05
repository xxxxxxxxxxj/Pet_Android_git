package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.HostPitalAndOther;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class MainToListAdapter<T> extends CommonAdapter<HostPitalAndOther>{

	public MainToListAdapter(Activity mContext, List<HostPitalAndOther> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.main_to_list_adapter, position);
		HostPitalAndOther andOther = mDatas.get(position);
		viewHolder.setBackgroundCircle(R.id.item_maintolist_adapter_imageview, andOther.pic, R.drawable.icon_production_default);
		viewHolder.setText(R.id.item_maintolist_adapter_name, andOther.name);
		viewHolder.setText(R.id.item_maintolist_adapter_address, "地址："+andOther.addr);
		return viewHolder.getConvertView();
	}

}
