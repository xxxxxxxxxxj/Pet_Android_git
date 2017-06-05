package com.haotang.pet.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MyCouponCanUse;

public class CouponAdapter extends BaseAdapter{

	private List<MyCouponCanUse> list;
//	private List<String> list;
	private Context mContext;
	
	public CouponAdapter(Context mContext,List<MyCouponCanUse> list){
		this.mContext=mContext;
		this.list=list;
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
	public void setNotif(){
		notifyDataSetChanged();
	}
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder=null;
		if (view == null) {
			mHolder = new ViewHolder();
//			view = LayoutInflater.from(mContext).inflate(R.layout.coupon_adapter, null);
			view = LayoutInflater.from(mContext).inflate(R.layout.coupon_adapter_my_new, null);
			mHolder.textView_coupon_why_give = (TextView) view.findViewById(R.id.tv_couponitem_content);
			mHolder.textView_coupon_time = (TextView) view.findViewById(R.id.tv_couponitem_time);
			mHolder.textView_coupon_price = (TextView) view.findViewById(R.id.tv_couponitem_money);
			mHolder.textview_isgohomeorgoshop = (TextView) view.findViewById(R.id.textview_isgohomeorgoshop);
			mHolder.layout_mycoupon = (RelativeLayout) view.findViewById(R.id.rl_couponitem);
			mHolder.coupon_new_layout_left = (RelativeLayout) view.findViewById(R.id.coupon_new_layout_left);
			mHolder.imageView_coupon_overtime = (ImageView) view.findViewById(R.id.imageView_coupon_overtime);
			mHolder.imageview_coupon_isstatus = (ImageView) view.findViewById(R.id.imageview_coupon_isstatus);
			mHolder.layout_give_other = (LinearLayout) view.findViewById(R.id.layout_give_other);
			mHolder.textview_give_people = (TextView) view.findViewById(R.id.textview_give_people);
			mHolder.imageview_tag = (ImageView) view.findViewById(R.id.imageview_tag);
			view.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) view.getTag();
		}
		mHolder.textView_coupon_why_give.setText(list.get(arg0).name.trim());
		mHolder.textView_coupon_time.setText(list.get(arg0).startTime.substring(0, list.get(arg0).startTime.indexOf(" ")).replace("-", ".")+
				" - "+list.get(arg0).endTime.substring(0, list.get(arg0).endTime.indexOf(" ")).replace("-", "."));
		mHolder.textView_coupon_price.setText((int)Math.floor((list.get(arg0).amount))+"");
		if (list.get(arg0).description==null) {
			mHolder.textview_isgohomeorgoshop.setText("");
		}else {
			mHolder.textview_isgohomeorgoshop.setText(list.get(arg0).description+"");
		}
		mHolder.layout_give_other.setVisibility(View.GONE);
		mHolder.imageview_tag.setVisibility(View.GONE);
		if (list.get(arg0).status==0) {
			mHolder.coupon_new_layout_left.setBackgroundResource(R.drawable.coupon_left_show_icon);
			mHolder.layout_give_other.setVisibility(View.VISIBLE);
			mHolder.textview_give_people.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			if (list.get(arg0).isCanGive == 0) {
				mHolder.layout_give_other.setVisibility(View.VISIBLE);
			}else if (list.get(arg0).isCanGive == 1) {
				mHolder.layout_give_other.setVisibility(View.GONE);
			}
			if (list.get(arg0).isGive == 1) {//1 被别人赠送 0 自己的优惠券
				mHolder.imageview_tag.setVisibility(View.VISIBLE);
				mHolder.imageview_tag.setImageResource(R.drawable.good_friend);
				mHolder.layout_give_other.setVisibility(View.GONE);
			}else {
				mHolder.imageview_tag.setVisibility(View.GONE);
			}
		}else if (list.get(arg0).status==3) {
			mHolder.coupon_new_layout_left.setBackgroundResource(R.drawable.coupon_left_cannot_icon);
			mHolder.layout_give_other.setVisibility(View.GONE);
			if (list.get(arg0).description==null) {
				mHolder.textview_isgohomeorgoshop.setText("已过期");
			}else {
				mHolder.textview_isgohomeorgoshop.setText(list.get(arg0).description+"已过期");
			}
//			if (list.get(arg0).isGive == 1) {
//				mHolder.imageview_tag.setVisibility(View.VISIBLE);
//				mHolder.imageview_tag.setImageResource(R.drawable.good_friend_time_out);
//			}else {
//				mHolder.imageview_tag.setVisibility(View.GONE);
//			}
			mHolder.imageview_tag.setVisibility(View.GONE);
		}else if (list.get(arg0).status==2) {
			mHolder.coupon_new_layout_left.setBackgroundResource(R.drawable.coupon_left_cannot_icon);
			mHolder.layout_give_other.setVisibility(View.GONE);
			if (list.get(arg0).description==null) {
				mHolder.textview_isgohomeorgoshop.setText("已过期");
			}else {
				mHolder.textview_isgohomeorgoshop.setText(list.get(arg0).description+"已过期");
			}
//			if (list.get(arg0).isGive == 1) {
//				mHolder.imageview_tag.setVisibility(View.VISIBLE);
//				mHolder.imageview_tag.setImageResource(R.drawable.good_friend_time_out);
//			}else {
//				mHolder.imageview_tag.setVisibility(View.GONE);
//			}
			mHolder.imageview_tag.setVisibility(View.GONE);
		}
		
//		if (list.get(arg0).isCanGive==0) {
//			mHolder.textview_give_people.setVisibility(View.VISIBLE);
//		}else if (list.get(arg0).isCanGive==1) {
//			mHolder.textview_give_people.setVisibility(View.GONE);
//		}
		return view;
	}
	private class ViewHolder{
		RelativeLayout coupon_new_layout_left ;
		RelativeLayout layout_mycoupon ;
		TextView textView_coupon_time;
		TextView textView_coupon_price;
		TextView textView_coupon_why_give;
		TextView textview_isgohomeorgoshop;
		ImageView imageView_coupon_overtime;
		ImageView imageview_coupon_isstatus;
		LinearLayout layout_give_other;
		TextView textview_give_people;
		ImageView imageview_tag;
	}
}
