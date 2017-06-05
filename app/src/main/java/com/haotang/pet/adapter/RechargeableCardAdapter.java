package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Rechar;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class RechargeableCardAdapter<T> extends CommonAdapter<T>{

	public RechargeableCardAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_mylastmoney_new, position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_mylastmoney_new_two, position);
		Rechar rechar = (Rechar) mDatas.get(position);
		if(rechar != null){
			int tempType = rechar.tempType;
			viewHolder.getView(R.id.imageView_is_hot).setVisibility(View.GONE);
			viewHolder.getView(R.id.textView_chongzhika_lijizengsong).setVisibility(View.VISIBLE);
			viewHolder.getView(R.id.textView_chongzhika_zengsong).setVisibility(View.VISIBLE);
			viewHolder.getView(R.id.textView_chongzhika_yuan).setVisibility(View.VISIBLE);
			viewHolder.getView(R.id.chongzhika_all_money_yuan).setVisibility(View.VISIBLE);
			viewHolder.getView(R.id.textview_chongzhika_last_days).setVisibility(View.VISIBLE);
			
			if (tempType ==1) {//无活动
				viewHolder.getView(R.id.imageView_is_hot).setVisibility(View.GONE);
				viewHolder.setBackgroundResource(R.id.layout_left_back, R.drawable.wuhuodong_back);
				viewHolder.setText(R.id.textView_item_money_price, rechar.recharge);
				viewHolder.setTextColor(R.id.textView_item_money_price, "#FAA04A");
				viewHolder.setTextColor(R.id.chongzhika_all_money_yuan, "#FAA04A");
				viewHolder.setTextColor(R.id.textview_chongzhika, "#FAA04A");
				viewHolder.setTextColor(R.id.textView_chongzhika_lijizengsong, "#FAA04A");
				viewHolder.setTextColor(R.id.textView_chongzhika_zengsong, "#FAA04A");
				viewHolder.setTextColor(R.id.textView_chongzhika_yuan, "#FAA04A");
				viewHolder.setTextColor(R.id.textview_item_time, "#333333");
				viewHolder.setText(R.id.textview_item_time, rechar.cardDesc);
				viewHolder.getView(R.id.textview_chongzhika_last_days).setVisibility(View.GONE);
				viewHolder.setText(R.id.textView_chongzhika_yuan, "元");
				TextView textView_chongzhika_yuan = viewHolder.getView(R.id.textView_chongzhika_yuan);
				textView_chongzhika_yuan.setTextSize(10);
				viewHolder.setTextColor(R.id.textView_chongzhika_yuan, "#FAA04A");
				if (rechar.instantBonus<=0) {
					viewHolder.getView(R.id.textView_chongzhika_lijizengsong).setVisibility(View.GONE);
					viewHolder.getView(R.id.textView_chongzhika_zengsong).setVisibility(View.GONE);
					viewHolder.getView(R.id.textView_chongzhika_yuan).setVisibility(View.GONE);
				}else {
					viewHolder.setText(R.id.textView_chongzhika_zengsong, rechar.instantBonus+"");
				}
				if (rechar.isHot==1) {
					viewHolder.getView(R.id.imageView_is_hot).setVisibility(View.VISIBLE);
				}else {
					viewHolder.getView(R.id.imageView_is_hot).setVisibility(View.GONE);
				}
			}else if (tempType ==3) {//时间段活动 
				if (rechar.isHot==1) {
					viewHolder.getView(R.id.imageView_is_hot).setVisibility(View.VISIBLE);
				}else {
					viewHolder.getView(R.id.imageView_is_hot).setVisibility(View.GONE);
				}
				viewHolder.setBackgroundResource(R.id.layout_left_back, R.drawable.chongzhikahuodong_hot);
				viewHolder.setText(R.id.textView_item_money_price, rechar.recharge);
				viewHolder.setTextColor(R.id.textView_item_money_price, "#FFFFFF");
				viewHolder.setTextColor(R.id.chongzhika_all_money_yuan, "#FFFFFF");
				viewHolder.setTextColor(R.id.textview_chongzhika, "#FFFFFF");
				if (TextUtils.isEmpty(rechar.surplus)) {
					viewHolder.getView(R.id.textview_chongzhika_last_days).setVisibility(View.GONE);
				}else {
					viewHolder.setText(R.id.textview_chongzhika_last_days, "剩余:"+rechar.surplus);
				}
				viewHolder.setTextColor(R.id.textview_chongzhika_last_days, "#FFFFFF");
				viewHolder.setTextColor(R.id.textView_chongzhika_lijizengsong, "#FB4460");
				viewHolder.setTextColor(R.id.textView_chongzhika_zengsong, "#FB4460");
				viewHolder.setTextColor(R.id.textView_chongzhika_yuan, "#FB4460");
				viewHolder.setTextColor(R.id.textview_item_time, "#333333");
				viewHolder.setText(R.id.textview_item_time, rechar.cardDesc);
				viewHolder.setText(R.id.textView_chongzhika_yuan, "元");
				TextView textView_chongzhika_yuan = viewHolder.getView(R.id.textView_chongzhika_yuan);
				textView_chongzhika_yuan.setTextSize(10);
				viewHolder.setTextColor(R.id.textView_chongzhika_yuan, "#FC4465");
				if (rechar.activityBonus<=0) {
					viewHolder.getView(R.id.textView_chongzhika_lijizengsong).setVisibility(View.GONE);
					viewHolder.getView(R.id.textView_chongzhika_zengsong).setVisibility(View.GONE);
					viewHolder.getView(R.id.textView_chongzhika_yuan).setVisibility(View.GONE);
				}else {
					viewHolder.setText(R.id.textView_chongzhika_zengsong, rechar.activityBonus+"");
				}
			}else if (tempType == 4) {// 个数活动
				if (rechar.isHot==1) {
					viewHolder.getView(R.id.imageView_is_hot).setVisibility(View.VISIBLE);
				}else {
					viewHolder.getView(R.id.imageView_is_hot).setVisibility(View.GONE);
				}
				viewHolder.setBackgroundResource(R.id.layout_left_back, R.drawable.chongzhikahuodong_hot);
				viewHolder.setText(R.id.textView_item_money_price, rechar.recharge);
				viewHolder.setTextColor(R.id.textView_item_money_price, "#FFFFFF");
				viewHolder.setTextColor(R.id.chongzhika_all_money_yuan, "#FFFFFF");
				viewHolder.setTextColor(R.id.textview_chongzhika, "#FFFFFF");
				if (rechar.activityNum>0) {
					viewHolder.setText(R.id.textview_chongzhika_last_days, "剩余:"+rechar.surplus);
				}else {
					viewHolder.getView(R.id.textview_chongzhika_last_days).setVisibility(View.GONE);
				}
				viewHolder.setTextColor(R.id.textview_chongzhika_last_days, "#FFFFFF");
				viewHolder.setTextColor(R.id.textView_chongzhika_lijizengsong, "#FB4460");
				viewHolder.setTextColor(R.id.textView_chongzhika_zengsong, "#FB4460");
				viewHolder.setTextColor(R.id.textView_chongzhika_yuan, "#FB4460");
				viewHolder.setTextColor(R.id.textview_item_time, "#333333");
				viewHolder.setText(R.id.textview_item_time, rechar.cardDesc);
				viewHolder.setText(R.id.textView_chongzhika_yuan, "元");
				TextView textView_chongzhika_yuan = viewHolder.getView(R.id.textView_chongzhika_yuan);
				textView_chongzhika_yuan.setTextSize(10);
				viewHolder.setTextColor(R.id.textView_chongzhika_yuan, "#FC4465");
				if (rechar.activityBonus<=0) {
					viewHolder.getView(R.id.textView_chongzhika_lijizengsong).setVisibility(View.GONE);
					viewHolder.getView(R.id.textView_chongzhika_zengsong).setVisibility(View.GONE);
					viewHolder.getView(R.id.textView_chongzhika_yuan).setVisibility(View.GONE);
				}else {
					viewHolder.setText(R.id.textView_chongzhika_zengsong, rechar.activityBonus+"");
				}
			}else if (tempType ==2) {
				viewHolder.getView(R.id.imageView_is_hot).setVisibility(View.GONE);
				if (rechar.isHot==1) {
					viewHolder.getView(R.id.imageView_is_hot).setVisibility(View.VISIBLE);
				}else {
					viewHolder.getView(R.id.imageView_is_hot).setVisibility(View.GONE);
				}
				viewHolder.setBackgroundResource(R.id.layout_left_back, R.drawable.lipinka_back);
				String showPrice="";
				if (rechar.recharge.contains("礼品")) {
					showPrice = "礼品";
				}else {
					showPrice = rechar.recharge;
				}
				viewHolder.setText(R.id.textView_item_money_price, showPrice);
				viewHolder.setTextColor(R.id.textView_item_money_price, "#D0D99F");
				viewHolder.setTextColor(R.id.textview_chongzhika, "#D0D99F");
				viewHolder.setText(R.id.textView_chongzhika_yuan, "输入兑换码即可充值");
				viewHolder.setTextColor(R.id.textView_chongzhika_yuan, "#D0D99F");
				viewHolder.setTextColor(R.id.textview_item_time, "#333333");
				viewHolder.setText(R.id.textview_item_time, rechar.cardDesc);
				viewHolder.getView(R.id.textView_chongzhika_lijizengsong).setVisibility(View.GONE);
				viewHolder.getView(R.id.textView_chongzhika_zengsong).setVisibility(View.GONE);
				viewHolder.getView(R.id.textview_chongzhika_last_days).setVisibility(View.GONE);
				viewHolder.getView(R.id.chongzhika_all_money_yuan).setVisibility(View.GONE);
				TextView textView_chongzhika_yuan = viewHolder.getView(R.id.textView_chongzhika_yuan);
				textView_chongzhika_yuan.setTextSize(12);
			}	
//			viewHolder.getView(R.id.imageview_tag_show_youhui).setVisibility(View.GONE);
//			viewHolder.getView(R.id.textview_time_down).setVisibility(View.GONE);
//			viewHolder.setTextColor(R.id.textview_item_time, "#666666");
//			if (position==0) {
//				viewHolder.setBackgroundResource(R.id.rechar_left_show, R.drawable.rechar_left_show_high);
//				viewHolder.setBackgroundResource(R.id.rechar_right_show, R.drawable.rechar_right_show);
//				viewHolder.setTextColor(R.id.textview_go_to_pay, "#51415D");
//				viewHolder.setTextColor(R.id.textView_item_money_price, "#51415D");
//				viewHolder.setTextColor(R.id.textview_item_detail, "#51415D");
//				viewHolder.setTextColor(R.id.textView_unit_price, "#51415D");
//				viewHolder.setViewVisible(R.id.textView_unit_price, View.VISIBLE);
//			}
//			else if (rechar.isHot==1 && tempType!=2) {//这块可以隐藏了
//				viewHolder.getView(R.id.imageview_tag_show_youhui).setVisibility(View.VISIBLE);
//				viewHolder.getView(R.id.textview_time_down).setVisibility(View.VISIBLE);
//				viewHolder.setText(R.id.textview_time_down, rechar.surplus);
//				
//				viewHolder.setBackgroundResource(R.id.rechar_left_show, R.drawable.rechar_left_show_high);
//				viewHolder.setBackgroundResource(R.id.rechar_right_show, R.drawable.rechar_right_show);
//				viewHolder.setTextColor(R.id.textview_go_to_pay, "#51415D");
//				viewHolder.setTextColor(R.id.textView_item_money_price, "#51415D");
//				viewHolder.setTextColor(R.id.textview_item_detail, "#51415D");
//				viewHolder.setTextColor(R.id.textView_unit_price, "#51415D");
//				viewHolder.setTextColor(R.id.textview_item_time, "#51415D");
//				viewHolder.setViewVisible(R.id.textView_unit_price, View.VISIBLE);
//			}
//			else if (rechar.isHot==1 && tempType ==1) {//无活动
//				
//			}else if (rechar.isHot==1 && tempType ==3) {//时间段活动
//				
//			}else if (rechar.isHot==1 && tempType ==4) {//个数活动
//				
//			}else if(tempType == 2){//礼品卡
//				viewHolder.setBackgroundResource(R.id.rechar_left_show, R.drawable.rechar_left_green_show_high);
//				viewHolder.setBackgroundResource(R.id.rechar_right_show, R.drawable.rechar_right_green_show);
//				viewHolder.setTextColor(R.id.textView_item_money_price, "#D0D99F");
//				viewHolder.setTextColor(R.id.textview_item_detail, "#D0D99F");
//				viewHolder.setTextColor(R.id.textview_go_to_pay, "#D0D99F");
//				viewHolder.setViewVisible(R.id.textView_unit_price, View.GONE);
//			}else{
//				viewHolder.setBackgroundResource(R.id.rechar_left_show, R.drawable.rechar_left_orange_show_high);
//				viewHolder.setBackgroundResource(R.id.rechar_right_show, R.drawable.rechar_right_orange_show);
//				viewHolder.setTextColor(R.id.textView_item_money_price, "#FAA04A");
//				viewHolder.setTextColor(R.id.textview_item_detail, "#FAA04A");
//				viewHolder.setTextColor(R.id.textview_go_to_pay, "#FAA04A");
//				viewHolder.setTextColor(R.id.textView_unit_price, "#FAA04A");
//				viewHolder.setViewVisible(R.id.textView_unit_price, View.VISIBLE);
//			}
//			viewHolder.setText(R.id.textView_item_money_price, rechar.recharge+"");
//			viewHolder.setText(R.id.textview_item_detail, rechar.title);
//			viewHolder.setText(R.id.textview_item_time,rechar.cardDesc);
		}
		return viewHolder.getConvertView();
	}

/*	private Context context;
	private List<Rechar> list ;
	private int num;
	public RechargeableCardAdapter(Context context,List<Rechar> list){
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder =null;
		if (convertView == null) {
			mHolder = new ViewHolder();
//			convertView = LayoutInflater.from(context).inflate(R.layout.item_mylastmoney, null);
			convertView = LayoutInflater.from(context).inflate(R.layout.item_mylastmoney_new, null);
			mHolder.textView_item_money_price = (TextView) convertView.findViewById(R.id.textView_item_money_price);
			mHolder.textview_item_detail = (TextView) convertView.findViewById(R.id.textview_item_detail);
			mHolder.textview_item_time = (TextView) convertView.findViewById(R.id.textview_item_time);
//			mHolder.layout_item_to_recharge = (RelativeLayout) convertView.findViewById(R.id.layout_item_to_recharge);
			mHolder.rech_layout = (LinearLayout) convertView.findViewById(R.id.rech_layout);
			mHolder.imageview_logo_rech = (ImageView) convertView.findViewById(R.id.imageview_logo_rech);
			convertView.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.textView_item_money_price.setText((int)list.get(position).recharge+"");
		mHolder.textview_item_detail.setText(list.get(position).title);
		mHolder.textview_item_time.setText("有效期：永久"list.get(position).validity);//马总要求要改的。
		if (position%4==0) {
//			mHolder.layout_item_to_recharge.setBackgroundResource(R.drawable.item_money_back_1);
			mHolder.rech_layout.setBackgroundResource(R.drawable.item_money_back_1_new);
			mHolder.imageview_logo_rech.setBackgroundResource(R.drawable.item_money_back_logo_1);
			mHolder.textview_item_detail.setTextColor(context.getResources().getColor(R.color.aFFE3A4));
		}else if (position%4==1) {
//			mHolder.layout_item_to_recharge.setBackgroundResource(R.drawable.item_money_back_2);
			mHolder.rech_layout.setBackgroundResource(R.drawable.item_money_back_2_new);
			mHolder.imageview_logo_rech.setBackgroundResource(R.drawable.item_money_back_logo_2);
			mHolder.textview_item_detail.setTextColor(context.getResources().getColor(R.color.a8AF7EE));
		}else if (position%4==2) {
//			mHolder.layout_item_to_recharge.setBackgroundResource(R.drawable.item_money_back_3);
			mHolder.rech_layout.setBackgroundResource(R.drawable.item_money_back_3_new);
			mHolder.imageview_logo_rech.setBackgroundResource(R.drawable.item_money_back_logo_3);
			mHolder.textview_item_detail.setTextColor(context.getResources().getColor(R.color.aCAF2B9));
		}else if (position%4==3) {
//			mHolder.layout_item_to_recharge.setBackgroundResource(R.drawable.item_money_back_4);
			mHolder.rech_layout.setBackgroundResource(R.drawable.item_money_back_4_new);
			mHolder.imageview_logo_rech.setBackgroundResource(R.drawable.item_money_back_logo_4);
			mHolder.textview_item_detail.setTextColor(context.getResources().getColor(R.color.affe3e4));
		}else {
//			mHolder.layout_item_to_recharge.setBackgroundResource(R.drawable.item_money_back_1);
			mHolder.rech_layout.setBackgroundResource(R.drawable.item_money_back_1_new);
			mHolder.imageview_logo_rech.setBackgroundResource(R.drawable.item_money_back_logo_1);
			mHolder.textview_item_detail.setTextColor(context.getResources().getColor(R.color.aFFE3A4));
		}
//		mHolder.layout_item_to_recharge.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				JumpToNext(RechargePage.class,list.get(position));
//				UmengStatistics.UmengEventStatistics(context,Global.UmengEventID.click_ChargeNow);
//			}
//		});
		return convertView;
	}

	class ViewHolder{
		TextView textView_item_money_price;
		TextView textview_item_detail;
		TextView textview_item_time;
//		RelativeLayout layout_item_to_recharge;
		LinearLayout rech_layout;
		ImageView imageview_logo_rech;
	}
	private void JumpToNext(Class clazz,Rechar rechar) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		Bundle bundle = new Bundle();
		bundle.putSerializable("rechar", rechar);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}*/

}
