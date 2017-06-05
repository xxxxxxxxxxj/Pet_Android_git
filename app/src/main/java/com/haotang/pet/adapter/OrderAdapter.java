package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Order;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class OrderAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Order> list;
	private LayoutInflater mInflater;
	
	public OrderAdapter(Context context, ArrayList<Order> list) {
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
//			convertView = mInflater.inflate(R.layout.orderitem, null);
			convertView = mInflater.inflate(R.layout.item_order_new, null);
//			holder.ivType = (ImageView) convertView.findViewById(R.id.iv_orderitem_type);
			holder.tvAddrType = (TextView) convertView.findViewById(R.id.tv_orderitem_addrtype);
			holder.tvServiceFee = (TextView) convertView.findViewById(R.id.tv_orderitem_servicefee);
			holder.tvPickup = (TextView) convertView.findViewById(R.id.tv_orderitem_pickup);
			holder.tvServiceName = (TextView) convertView.findViewById(R.id.tv_orderitem_servicename);
			holder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_orderitem_starttime);
//			holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_orderitem_status);
			holder.textView_right_status = (TextView) convertView.findViewById(R.id.textView_right_status);
			holder.tvType = (TextView) convertView.findViewById(R.id.tv_orderitem_type);
//			holder.tvType_up = (TextView) convertView.findViewById(R.id.iv_orderitem_type_up);
			holder.sriv = (SelectableRoundedImageView) convertView.findViewById(R.id.sriv_orderitem_pet);
			holder.title_YearandMonth = (TextView) convertView.findViewById(R.id.title_YearandMonth);
			holder.layout_title_YearAndMonth = (LinearLayout) convertView.findViewById(R.id.layout_title_YearAndMonth);
			holder.line = (View) convertView.findViewById(R.id.line);
			
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		Order order = list.get(position);
		holder.tvPickup.setVisibility(View.GONE);
		if(order.type==2){
//			holder.ivType.setBackgroundResource(R.drawable.foster);
			holder.tvType.setText("寄养");
			Drawable drawable = Utils.getDW("E5728D");
			holder.tvType.setBackgroundDrawable(drawable);
//			holder.tvType_up.setText("寄养");
			if(order.starttime!=null&&!"".equals(order.starttime))
				holder.tvStartTime.setText("入住时间："+order.starttime);
			if(order.endtime!=null&&!"".equals(order.endtime))
				holder.tvAddrType.setText("离店时间："+order.endtime);
		}else if (order.type==3) {
			Drawable drawable = Utils.getDW("5BB0EC");
			holder.tvType.setBackgroundDrawable(drawable);
			holder.tvType.setText("游泳");
//			holder.tvType_up.setText("游泳");
		}else if (order.type==4) {
			Drawable drawable = Utils.getDW("6C6CC2");
			holder.tvType.setBackgroundDrawable(drawable);
			holder.tvType.setText("训练");
		}else{
			if(order.petkind==1&&order.serviceid==1||order.petkind==2&&order.serviceid==3){
//				holder.ivType.setBackgroundResource(R.drawable.wash);
				holder.tvType.setText("洗护");
				Drawable drawable = Utils.getDW("FAA04A");
				holder.tvType.setBackgroundDrawable(drawable);
//				holder.tvType_up.setText("洗澡");
			}else if(order.petkind==1&&order.serviceid==2||order.petkind==2&&order.serviceid==4){
				holder.tvType.setText("美容");
				Drawable drawable = Utils.getDW("80C26C");
				holder.tvType.setBackgroundDrawable(drawable);
			}else{
				holder.tvType.setText("特色服务");
				Drawable drawable = Utils.getDW("FF9999");
				holder.tvType.setBackgroundDrawable(drawable);
			}
			if (!TextUtils.isEmpty(order.orderSource)) {
				if (order.orderSource.equals("vie")) {
					holder.tvType.setText("即时预约");
					Drawable drawable = Utils.getDW("F9605C");
					holder.tvType.setBackgroundDrawable(drawable);
				}
			}
			if(order.starttime!=null&&!"".equals(order.starttime))
				holder.tvStartTime.setText("预约时间："+order.starttime);
			if(order.addrtype==1){
				holder.tvAddrType.setText("服务方式：门店服务");
				if(order.pickup==1){
					holder.tvPickup.setVisibility(View.VISIBLE);
					holder.tvPickup.setText("(需要接送)");
				}else/* if(order.pickup==0)*/{
					holder.tvPickup.setVisibility(View.VISIBLE);
					holder.tvPickup.setText("(不接送)");
				}
			}
			else if(order.addrtype==2){
				holder.tvAddrType.setText("服务方式：上门服务");
			}
			
			
				
		}
		
//		if(order.statusstr!=null&&!"".equals(order.statusstr))
//			holder.tvStatus.setText(order.statusstr);
		if(order.statusstr!=null&&!"".equals(order.statusstr)){
//			||order.status==4||order.status==2||order.status==21||order.status==22||order.status==23
			if (order.status==1) {//待付款
				holder.textView_right_status.setTextColor(Color.parseColor("#E33A4A"));
			}else if (order.status==6) {//已取消
				holder.textView_right_status.setTextColor(Color.parseColor("#CCCCCC"));
			}else {
				holder.textView_right_status.setTextColor(Color.parseColor("#BB996C"));
			}
			holder.textView_right_status.setText(order.statusstr);
		}
		holder.line.setVisibility(View.VISIBLE);
		if (order.isShow) {
			holder.layout_title_YearAndMonth.setVisibility(View.VISIBLE);
			String [] timeLast = order.EverytYearAndMonth.split("-");
			if (timeLast[1].equals("12")) {
				holder.title_YearandMonth.setText(order.EverytYearAndMonth+"月");
			}else {
				holder.title_YearandMonth.setText(timeLast[1]+"月");
			}
		}else {
			holder.layout_title_YearAndMonth.setVisibility(View.GONE);
		}
		try {
			Order order2 = list.get(position+1);
			if (order2.isShow) {
				holder.line.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(order.petname!=null&&!"".equals(order.petname))
			holder.tvServiceName.setText(order.petname);
		if(order.customerpetname!=null&&!"".equals(order.customerpetname))
			holder.tvServiceName.setText(order.customerpetname);
		if(order.servicename!=null&&!"".equals(order.servicename))
			holder.tvServiceName.setText(holder.tvServiceName.getText().toString().trim()+order.servicename);
		if (order.type==3) {
			holder.tvServiceName.setText(order.petname.trim()+"游泳");
			holder.tvAddrType.setText("服务方式：门店服务");
			String time [] = order.starttime.split(" ");
			String AmOrPm="";
			if (time[1].contains("10")) {
				AmOrPm="上午";
			}else if (time[1].contains("14")) {
				AmOrPm="下午";
			}
			holder.tvStartTime.setText("预约时间："+time[0]+" "+AmOrPm);
		}
		if (order.type==4) {
			holder.tvStartTime.setText("预约时间："+order.starttime.replace("00", "").replace(":", "").trim());
			holder.tvAddrType.setText("服务方式：门店服务");
		}
		String lastPrice = "¥"+Utils.formatDouble(order.fee);
//		holder.tvServiceFee.setText("¥"+Utils.formatDouble(order.fee));
		holder.tvServiceFee.setText(getTextPrice(lastPrice));
		holder.sriv.setTag(order.petimg);
		if(order.petimg!=null&&!"".equals(order.petimg))
			ImageLoaderUtil.loadImg(order.petimg, holder.sriv, 0,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onLoadingComplete(String path, View view, Bitmap bitmap) {
					// TODO Auto-generated method stub
					if(view!=null&&bitmap!=null){
						SelectableRoundedImageView iv = (SelectableRoundedImageView) view;
						String imagetag = (String) iv.getTag();
						if(path!=null&&path.equals(imagetag)){
							iv.setImageBitmap(bitmap);
						}
					}
					
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
				}
			});
		
		return convertView;
	}
	
	class Holder{
//		ImageView ivType;
		TextView tvType;
//		TextView tvStatus;
		TextView tvServiceName;
		TextView tvServiceFee;
		TextView tvStartTime;
		TextView tvAddrType;
		TextView tvPickup;
		SelectableRoundedImageView sriv;
		TextView textView_right_status;
//		TextView tvType_up;
		TextView title_YearandMonth;
		LinearLayout layout_title_YearAndMonth;
//		TextView title_month_day;
		View line;
	}

	private SpannableString getTextPrice(String lastOnePrice) {
		SpannableString styledText = new SpannableString(lastOnePrice);
		styledText.setSpan(new TextAppearanceSpan(context, R.style.styleorder), 0, 1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		styledText.setSpan(new TextAppearanceSpan(context, R.style.styleorderprice), 1,lastOnePrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return styledText;
	}

}
