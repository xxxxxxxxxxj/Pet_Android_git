package com.haotang.pet.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;

@SuppressLint("NewApi")
public class ServiceSearchAddressAdapter extends BaseAdapter {
	private Context context;
	private List<ArrayMap<String, String>> list = new ArrayList<ArrayMap<String, String>>();
	private boolean isHavePet;

	public ServiceSearchAddressAdapter(Context context) {
		this.context = context;
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
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder mHolder = null;
		if (view == null) {
			mHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.service_search_address, null);
			mHolder.tv_service_serch_name = (TextView) view
					.findViewById(R.id.tv_service_serch_name);
			mHolder.tv_service_serch_address = (TextView) view
					.findViewById(R.id.tv_service_serch_address);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		ArrayMap<String, String> arrayMap = list.get(arg0);
		if (arrayMap != null && arrayMap.size() > 0) {
			String name = arrayMap.get("name");
			String address = arrayMap.get("address");
			Utils.setStringText(mHolder.tv_service_serch_name, name);
			if (isHavePet) {
				mHolder.tv_service_serch_address.setVisibility(View.GONE);
			} else {
				mHolder.tv_service_serch_address.setVisibility(View.VISIBLE);
				Utils.setStringText(mHolder.tv_service_serch_address, address);
			}
		}
		return view;
	}

	public class ViewHolder {
		public TextView tv_service_serch_address;
		public TextView tv_service_serch_name;
	}

	public void setData(List<ArrayMap<String, String>> searchressList,
			boolean isHavePet) {
		this.isHavePet = isHavePet;
		this.list.clear();
		this.list.addAll(searchressList);
		notifyDataSetChanged();
	}
}
