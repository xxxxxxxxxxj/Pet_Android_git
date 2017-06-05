package com.haotang.pet.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.UpgradeItem;
import com.haotang.pet.util.Utils;

public class UpgradeServiceAdapter extends BaseAdapter {
	private Context context;
	private List<UpgradeItem> list;
	private LayoutInflater mInflater;

	public UpgradeServiceAdapter(Context context, List<UpgradeItem> list) {
		super();
		this.context = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.upgradeservice_item, null);
			holder.tvItem1 = (TextView) convertView
					.findViewById(R.id.tv_upgradeserviceitem_item1);
			holder.tvItem2 = (TextView) convertView
					.findViewById(R.id.tv_upgradeserviceitem_item2);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		UpgradeItem upgradeItem = list.get(position);
		if (upgradeItem != null) {
			Utils.setText(holder.tvItem1, upgradeItem.name, "", View.VISIBLE,
					View.VISIBLE);
			Utils.setText(holder.tvItem2, "¥" + upgradeItem.price, "",
					View.VISIBLE, View.VISIBLE);
		}
		return convertView;
	}

	class Holder {
		TextView tvItem1;
		TextView tvItem2;
	}

}
