package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ServiceAreaCodeBean.AreasBean.ShopsBean;
import com.haotang.pet.util.Utils;

public class AllServiceAreaAdapter extends BaseAdapter {
	private Activity context;
	private List<ShopsBean> list;
	private int checkItemPosition = -1;

	public AllServiceAreaAdapter(Activity context, List<ShopsBean> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ShopsBean shopsBean = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_service_area,
					null);
			holder.tv_servicetext = (TextView) convertView
					.findViewById(R.id.tv_servicetext);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (shopsBean != null) {
			Utils.setText(holder.tv_servicetext, shopsBean.getShopName(), "",
					View.VISIBLE, View.INVISIBLE);
			if (checkItemPosition == position) {
				holder.tv_servicetext.setTextColor(context.getResources()
						.getColor(R.color.aD1494F));
			} else {
				holder.tv_servicetext.setTextColor(context.getResources()
						.getColor(R.color.black));
			}
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView tv_servicetext;
	}

	public void setCheckItem(int position) {
		checkItemPosition = position;
		notifyDataSetChanged();
	}
}
