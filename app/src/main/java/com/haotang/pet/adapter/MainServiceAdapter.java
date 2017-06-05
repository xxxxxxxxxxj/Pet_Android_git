package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MainService;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;

public class MainServiceAdapter extends BaseAdapter {
	private ArrayList<MainService> list;
	private LayoutInflater mInflater;
	private SharedPreferenceUtil spUtil;

	public MainServiceAdapter(Context context, ArrayList<MainService> list) {
		super();
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
		spUtil = SharedPreferenceUtil.getInstance(context);
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
	public View getView(int position, View contentView, ViewGroup parent) {
		Holder holder = null;
		if (contentView == null) {
			holder = new Holder();
			contentView = mInflater.inflate(R.layout.mainserviceitem, null);
			holder.srivImg = (ImageView) contentView
					.findViewById(R.id.sriv_mainserviceitem_img);
			holder.ivTag = (ImageView) contentView
					.findViewById(R.id.iv_mainserviceitem_hot);
			holder.tvName = (TextView) contentView
					.findViewById(R.id.tv_mainserviceitem_name);
			contentView.setTag(holder);
		} else {
			holder = (Holder) contentView.getTag();
		}
		MainService mainService = list.get(position);
		if (mainService != null) {
			ImageLoaderUtil.loadImg(mainService.strimg, holder.srivImg,
					R.drawable.icon_production_default, null);
			Utils.setText(holder.tvName, mainService.name, "", View.VISIBLE,
					View.VISIBLE);
			if (mainService.newOrHot == 0) {// 非热门最新
				holder.ivTag.setVisibility(View.GONE);
			} else if (mainService.newOrHot == 1 || mainService.newOrHot == 2) {
				if(mainService.newOrHot == 1){//热门
					int hotPoint = spUtil.getInt("hotPoint"+mainService.point, 0);
					if (hotPoint >= 3) {
						holder.ivTag.setVisibility(View.GONE);
					} else {
						holder.ivTag.setVisibility(View.VISIBLE);
						holder.ivTag
						.setBackgroundResource(R.drawable.icon_hot_mainservice);
					}
				}else if(mainService.newOrHot == 2){//最新
					int newPoint = spUtil.getInt("newPoint"+mainService.point, 0);
					if (newPoint >= 3) {
						holder.ivTag.setVisibility(View.GONE);
					} else {
						holder.ivTag.setVisibility(View.VISIBLE);
						holder.ivTag
						.setBackgroundResource(R.drawable.icon_new_mainservice);
					}
				}
			}
		}
		return contentView;
	}
	
	class Holder {
		ImageView srivImg;
		ImageView ivTag;
		TextView tvName;
	}

}
