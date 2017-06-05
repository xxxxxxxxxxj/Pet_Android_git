package com.haotang.pet.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MainHotHospital;
import com.haotang.pet.util.ImageLoaderUtil;

public class MainHotHospitalAdapter extends BaseAdapter {
	private Context context;
	private List<MainHotHospital> list;
	private LayoutInflater mInflater;
	
	public MainHotHospitalAdapter(Context context, List<MainHotHospital> list) {
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
			convertView = mInflater.inflate(R.layout.mainhothospitalitem, null);
			holder.srivImg = (ImageView) convertView.findViewById(R.id.sriv_mainhothospitalitem_img);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_mainhothospitalitem_name);
			holder.tvAddr = (TextView) convertView.findViewById(R.id.tv_mainhothospitalitem_addr);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		holder.srivImg.setTag(list.get(position).imgurl);
		ImageLoaderUtil.setImageWithTag(holder.srivImg, list.get(position).imgurl, 0);
		holder.tvAddr.setText("地址："+list.get(position).addr);
		holder.tvName.setText(list.get(position).name);
		return convertView;
	}
	
	class Holder{
		ImageView srivImg;
		TextView tvName;
		TextView tvAddr;
	}

}
