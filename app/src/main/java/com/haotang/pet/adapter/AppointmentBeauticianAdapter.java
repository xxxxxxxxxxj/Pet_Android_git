package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CircleImageView;

public class AppointmentBeauticianAdapter extends BaseAdapter {
	private Activity context;
	private ArrayList<Beautician> list;
	private LayoutInflater mInflater;
	private int itemPosition;

	public AppointmentBeauticianAdapter(Activity context,
			ArrayList<Beautician> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	public void setList(ArrayList<Beautician> list) {
		this.list = list;
		notifyDataSetChanged();
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
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(
					R.layout.appointment_beauticianlist_item, null);
			holder.civ_appointment_beauticianlist_item_img = (CircleImageView) convertView
					.findViewById(R.id.civ_appointment_beauticianlist_item_img);
			holder.iv_appointment_beauticianlist_item = (ImageView) convertView
					.findViewById(R.id.iv_appointment_beauticianlist_item);
			holder.tv_appointment_beauticianlist_item_name = (TextView) convertView
					.findViewById(R.id.tv_appointment_beauticianlist_item_name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Beautician beautician = list.get(position);
		if (beautician != null) {
			ImageLoaderUtil.loadImg(beautician.image,
					holder.civ_appointment_beauticianlist_item_img,
					R.drawable.icon_production_default, null);
			Utils.setText(holder.tv_appointment_beauticianlist_item_name,
					beautician.name, "", View.VISIBLE, View.INVISIBLE);
		}
		if (position == 0) {
			holder.tv_appointment_beauticianlist_item_name.setTextColor(context
					.getResources().getColor(R.color.aE33A4A));
			holder.iv_appointment_beauticianlist_item
					.setImageResource(R.drawable.beautician_select);
			holder.civ_appointment_beauticianlist_item_img.mBorderColor = Color
					.parseColor("#E33A4A");
			holder.civ_appointment_beauticianlist_item_img.invalidate();
		} else {
			holder.iv_appointment_beauticianlist_item
					.setImageResource(R.drawable.beautician_disable);
			holder.tv_appointment_beauticianlist_item_name.setTextColor(context
					.getResources().getColor(R.color.a333333));
			holder.civ_appointment_beauticianlist_item_img.mBorderColor = Color
					.parseColor("#bbbbbb");
			holder.civ_appointment_beauticianlist_item_img.invalidate();
		}
		return convertView;
	}

	private class Holder {
		CircleImageView civ_appointment_beauticianlist_item_img;
		ImageView iv_appointment_beauticianlist_item;
		TextView tv_appointment_beauticianlist_item_name;
	}
}
