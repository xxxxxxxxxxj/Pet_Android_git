package com.haotang.pet.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haotang.pet.R;

public class DropMenuAdapter extends BaseAdapter {
	private Context context;
	private List<String> asList;

	public DropMenuAdapter(Context context, List<String> asList) {
		this.context = context;
		this.asList = asList;
	}

	@Override
	public int getCount() {
		return asList.size();
	}

	@Override
	public Object getItem(int position) {
		return asList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_drapmenu, null);
			holder.tv_item_drapmenu = (TextView) convertView
					.findViewById(R.id.tv_item_drapmenu);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String name = asList.get(position);
		if (name != null && !TextUtils.isEmpty(name)) {
			holder.tv_item_drapmenu.setText(name);
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView tv_item_drapmenu;
	}

}
