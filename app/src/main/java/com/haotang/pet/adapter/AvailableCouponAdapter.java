package com.haotang.pet.adapter;

import java.util.ArrayList;

import com.haotang.pet.R;
import com.haotang.pet.entity.Coupon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AvailableCouponAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Coupon> list;
	private LayoutInflater mInflater;
	private int couponId;
	
	public AvailableCouponAdapter(Context context, ArrayList<Coupon> list) {
		super();
		this.context = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}

	public void setCouponId(int couponId){
		this.couponId=couponId;
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
		Holder holder = null;
		if(convertView == null){
			holder = new Holder();
//			convertView = mInflater.inflate(R.layout.coupon_item, null);
			convertView = mInflater.inflate(R.layout.coupon_adapter_new, null);
//			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_couponitem_content);
//			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_couponitem_time);
//			holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_couponitem_money);
			holder.textView_coupon_why_give = (TextView) convertView.findViewById(R.id.tv_couponitem_content);
			holder.textView_coupon_time = (TextView) convertView.findViewById(R.id.tv_couponitem_time);
			holder.textView_coupon_price = (TextView) convertView.findViewById(R.id.tv_couponitem_money);
			holder.textview_isgohomeorgoshop = (TextView) convertView.findViewById(R.id.textview_isgohomeorgoshop);
			holder.layout_mycoupon = (RelativeLayout) convertView.findViewById(R.id.rl_couponitem);
			holder.imageView_coupon_overtime = (ImageView) convertView.findViewById(R.id.imageView_coupon_overtime);
			holder.imageview_coupon_isstatus = (ImageView) convertView.findViewById(R.id.imageview_coupon_isstatus);
			holder.imageview_tag = (ImageView) convertView.findViewById(R.id.imageview_tag);
			holder.img_select = (ImageView) convertView.findViewById(R.id.img_select);

			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
//		holder.tvContent.setText(list.get(position).name);
//		holder.tvTime.setText(list.get(position).starttime+"-"+list.get(position).endtime);
//		holder.tvMoney.setText(list.get(position).amount);
		holder.textView_coupon_why_give.setText(list.get(position).name);
		holder.textView_coupon_time.setText(list.get(position).starttime.substring(0, list.get(position).starttime.indexOf(" ")).replace("-", ".")+
				"至"+list.get(position).endtime.substring(0, list.get(position).endtime.indexOf(" ")).replace("-", "."));
		holder.textView_coupon_price.setText(list.get(position).amount+"");
		holder.textview_isgohomeorgoshop.setVisibility(View.GONE);
		holder.imageview_tag.setVisibility(View.GONE);
		if (list.get(position).isGive == 1) {
			holder.imageview_tag.setVisibility(View.VISIBLE);
			holder.imageview_tag.setImageResource(R.drawable.good_friend);
		}else {
			holder.imageview_tag.setVisibility(View.GONE);
		}

		if (couponId==list.get(position).id) {
			if (!list.get(position).isChoose) {
				list.get(position).isChoose = !list.get(position).isChoose;
				holder.img_select.setVisibility(View.VISIBLE);
			}else {
				holder.img_select.setVisibility(View.VISIBLE);
			}
		}else {
			if (list.get(position).isChoose) {
				list.get(position).isChoose = !list.get(position).isChoose;
			}
			holder.img_select.setVisibility(View.GONE);
		}
		notifyDataSetChanged();
		return convertView;
	}
	
	private class Holder{
//		TextView tvContent;
//		TextView tvTime;
//		TextView tvMoney;
		
		RelativeLayout layout_mycoupon ;
		TextView textView_coupon_time;
		TextView textView_coupon_price;
		TextView textView_coupon_why_give;
		TextView textview_isgohomeorgoshop;
		ImageView imageView_coupon_overtime;
		ImageView imageview_coupon_isstatus;
		ImageView imageview_tag;
		ImageView img_select;
	}

}
