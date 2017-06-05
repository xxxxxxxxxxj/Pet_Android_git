package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;

public class MainBeauticianAdapter extends BaseAdapter {
	private Activity context;
	private ArrayList<Beautician> list;
	private LayoutInflater mInflater;
	private int mwidth;
	public MainBeauticianAdapter(Activity context, ArrayList<Beautician> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
		this.mwidth = Utils.getDisplayMetrics(context)[0];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int size = 0;
		if(list.size()>2&&list.size()%2>0){
			size = list.size()-1;
		}else{
			size = list.size();
		}
		return size;
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
			convertView = mInflater.inflate(R.layout.mainbeauticianitem, null);
			holder.tvLevel = (TextView) convertView.findViewById(R.id.tv_mainbeauticianitem_sign);
			holder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_mainbeauticianitem_photo);
			holder.rlMain = (RelativeLayout) convertView.findViewById(R.id.rl_mainbeauticianitem);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_mainbeauticianitem_name);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		if(position==0||position==3){
			holder.rlMain.setBackgroundColor(context.getResources().getColor(R.color.aFCE7D6));
		}else if(position==1||position==2){
			holder.rlMain.setBackgroundColor(context.getResources().getColor(R.color.f8f8ff8));
		}
		android.view.ViewGroup.LayoutParams lp1 = holder.ivPhoto.getLayoutParams();
		lp1.width = mwidth*17/60;
		lp1.height = mwidth*17/60;
		holder.ivPhoto.setLayoutParams(lp1);
		holder.ivPhoto.setTag(list.get(position).image);
		ImageLoaderUtil.setImageWithTag(holder.ivPhoto, list.get(position).image, 0);
		android.view.ViewGroup.LayoutParams lp = holder.tvName.getLayoutParams();
		lp.width = mwidth*23/60;
		holder.tvName.setLayoutParams(lp);
		if(list.get(position).name!=null&&!"".equals(list.get(position).name))
			holder.tvName.setText(list.get(position).name);
		if(list.get(position).sort==10){
			holder.tvLevel.setText("中级美容师");
		}else if(list.get(position).sort==20){
			holder.tvLevel.setText("高级美容师");
		}else if(list.get(position).sort==30){
			holder.tvLevel.setText("首席美容师");
		}
		return convertView;
	}
	
	class Holder{
		ImageView ivPhoto;
		TextView tvLevel;
		TextView tvName;
		RelativeLayout rlMain;
	}

}
