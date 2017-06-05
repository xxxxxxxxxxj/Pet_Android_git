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
import com.haotang.pet.entity.Charact;
import com.haotang.pet.util.ImageLoaderUtil;

public class CharacterAdapter extends BaseAdapter{

	private Context context;
	private List<Charact> list ;
	public CharacterAdapter(Context context,List<Charact> list){
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder=null;
		if (convertView==null) {
			mHolder = new ViewHolder();
			convertView  = LayoutInflater.from(context).inflate(R.layout.item_character, null);
			mHolder.imageView_item_show_img = (ImageView) convertView.findViewById(R.id.imageView_item_show_img);
			mHolder.textview_item_title = (TextView) convertView.findViewById(R.id.textview_item_title);
			mHolder.textview_item_price = (TextView) convertView.findViewById(R.id.textview_item_price);
			mHolder.textview_item_price_unit = (TextView) convertView.findViewById(R.id.textview_item_price_unit);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.imageView_item_show_img.setTag(list.get(position).pic);//打上标签
		ImageLoaderUtil.setImageWithTag(mHolder.imageView_item_show_img, list.get(position).pic, R.drawable.icon_image_default);
		
		mHolder.textview_item_title.setText(list.get(position).name);
		mHolder.textview_item_price.setText("¥"+list.get(position).minprice);
		if(list.get(position).minpricesuffix!=null&&!"".equals(list.get(position).minpricesuffix.trim())){
			mHolder.textview_item_price_unit.setText(list.get(position).minpricesuffix);
			mHolder.textview_item_price_unit.setVisibility(View.VISIBLE);
		}else{
			mHolder.textview_item_price_unit.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder{
		ImageView imageView_item_show_img;
		TextView textview_item_title;
		TextView textview_item_price;
		TextView textview_item_price_unit;
	}
	
}
