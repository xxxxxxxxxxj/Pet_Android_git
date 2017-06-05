package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Charact;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;

public class MainHotServiceAdapter extends BaseAdapter {
	private Activity context;
	private LayoutInflater mInflater;
	private ArrayList<Charact> listHotService = new ArrayList<Charact>();
	private int screenWidth;

	public MainHotServiceAdapter(Activity context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		screenWidth = Utils.getDisplayMetrics(context)[0];
	}

	@Override
	public int getCount() {
		return listHotService.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listHotService.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.mainhotserviceitem, null);
			holder.iv_mainhotserviceitem = (ImageView) convertView
					.findViewById(R.id.iv_mainhotserviceitem);
			holder.tv_mainhotserviceitem_name = (TextView) convertView
					.findViewById(R.id.tv_mainhotserviceitem_name);
			holder.tv_mainhotserviceitem_price = (TextView) convertView
					.findViewById(R.id.tv_mainhotserviceitem_price);
			holder.tv_mainhotserviceitem_unit = (TextView) convertView
					.findViewById(R.id.tv_mainhotserviceitem_unit);
			holder.rl_mainhotserviceitem = (RelativeLayout) convertView
					.findViewById(R.id.rl_mainhotserviceitem);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		setViewLayout(holder.iv_mainhotserviceitem, screenWidth / 2, screenWidth * 2 / 5);
		Charact charact = listHotService.get(position);
		String pic = charact.pic;
		String name = charact.name;
		String minpricesuffix = charact.minpricesuffix;
		String minprice = charact.minprice;
		holder.iv_mainhotserviceitem.setTag(pic);// 打上标签
		ImageLoaderUtil.setImageWithTag(holder.iv_mainhotserviceitem,pic, R.drawable.icon_production_default);
		Utils.setStringText(holder.tv_mainhotserviceitem_name, name);
		Utils.setStringText(holder.tv_mainhotserviceitem_price, "¥"
				+ minprice);
		Utils.setStringText(holder.tv_mainhotserviceitem_unit, minpricesuffix);
		if(name!=null && !TextUtils.isEmpty(name)){
			if(name.equals("more")){
				holder.rl_mainhotserviceitem.setVisibility(View.GONE);
			}else{
				holder.rl_mainhotserviceitem.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}
	
	private void setViewLayout(View iv, int width, int height) {
		android.view.ViewGroup.LayoutParams lParams = iv.getLayoutParams();
		lParams.width = width;
		lParams.height = height;
		iv.setLayoutParams(lParams);
	}

	class Holder {
		public ImageView iv_mainhotserviceitem;
		public TextView tv_mainhotserviceitem_name;
		public TextView tv_mainhotserviceitem_price;
		public TextView tv_mainhotserviceitem_unit;
		private RelativeLayout rl_mainhotserviceitem;
	}

	public void setData(ArrayList<Charact> listHotService) {
		this.listHotService.clear();
		this.listHotService.addAll(listHotService);
		notifyDataSetChanged();
	}

}
