package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.Blance;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.ViewHolder;


public class BlanceRecordAdapter<T> extends CommonAdapter<T> {
	OtherGoodS goodS = null;
	public BlanceRecordAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_balancerecord, position);
		Blance blance = (Blance) mDatas.get(position);
		viewHolder.setText(R.id.textView_balance_service_name, blance.item);
		viewHolder.setText(R.id.amount, blance.amount+"");
		viewHolder.setText(R.id.textView_balance_service_time, blance.tradeDate);
//		if (blance.amount.contains("+")) {
//			viewHolder.setTextColor(R.id.amount, "#FF9531");
//			viewHolder.setTextColor(R.id.textView_balance_service_detail_message, "#FF9531");
//		}else if(blance.amount.contains("-")) {
//			viewHolder.setTextColor(R.id.amount, "#464646");
//			viewHolder.setTextColor(R.id.textView_balance_service_detail_message, "#464646");
//		}else {
//			viewHolder.setTextColor(R.id.textView_balance_service_detail_message, "#464646");
//		}
		viewHolder.setText(R.id.textView_balance_service_detail_message, blance.state);
		viewHolder.setText(R.id.textview_blance_ordernum, "交易流水号："+blance.tradeNo);
		if (TextUtils.isEmpty(blance.desc)||blance.desc.equals("")||blance.desc==null) {
			viewHolder.setText(R.id.textview_blance_payway,"");
		}else {
			viewHolder.setText(R.id.textview_blance_payway, blance.desc);
		}
		viewHolder.setBackgroundNormal(R.id.blance_icon, blance.icon, 0);
		final MListview mListview = viewHolder.getView(R.id.other_things);
		mListview.setVisibility(View.GONE);
		viewHolder.getView(R.id.textview_dianming).setVisibility(View.GONE);
		viewHolder.getView(R.id.view_line).setVisibility(View.GONE);
		if (blance.isOpen) {
			goodS = new OtherGoodS(mContext,blance.list);	
			mListview.setAdapter(goodS);
			mListview.setVisibility(View.VISIBLE);
			try {
				viewHolder.setText(R.id.textview_dianming, blance.list.get(0).shopName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			viewHolder.getView(R.id.textview_dianming).setVisibility(View.VISIBLE);
			viewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
		}else {
			mListview.setVisibility(View.GONE);
			viewHolder.getView(R.id.textview_dianming).setVisibility(View.GONE);
			viewHolder.getView(R.id.view_line).setVisibility(View.GONE);
		}
		if (blance.isRedOn) {
			viewHolder.getView(R.id.imageview_has_tag).setVisibility(View.VISIBLE);
			viewHolder.getView(R.id.layout_item).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Blance blanceEvery = (Blance) mDatas.get(position);
					if (!blanceEvery.isOpen) {
						goodS = new OtherGoodS(mContext,blanceEvery.list);	
						mListview.setAdapter(goodS);
						mListview.setVisibility(View.VISIBLE);
						try {
							viewHolder.setText(R.id.textview_dianming, blanceEvery.list.get(0).shopName);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						viewHolder.getView(R.id.textview_dianming).setVisibility(View.VISIBLE);
						viewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
					}else {
						mListview.setVisibility(View.GONE);
						viewHolder.getView(R.id.textview_dianming).setVisibility(View.GONE);
						viewHolder.getView(R.id.view_line).setVisibility(View.GONE);
					}
					blanceEvery.isOpen=!blanceEvery.isOpen;
					mDatas.set(position, (T) blanceEvery);
				}
			});
		}else {
			viewHolder.getView(R.id.imageview_has_tag).setVisibility(View.GONE);
		}
		
		return viewHolder.getConvertView();
	}

/*	private Context context;
	private List<Blance> list;
	public BlanceRecordAdapter(Context context,List<Blance> list){
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_balancerecord, null);
			mHolder.textView_balance_service_name = (TextView) convertView.findViewById(R.id.textView_balance_service_name);
			mHolder.textView_balance_service_price = (TextView) convertView.findViewById(R.id.textView_balance_service_price);
			mHolder.textView_balance_service_time = (TextView) convertView.findViewById(R.id.textView_balance_service_time);
			mHolder.textView_balance_service_detail_message = (TextView) convertView.findViewById(R.id.textView_balance_service_detail_message);
			convertView.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.textView_balance_service_name.setText(list.get(position).item);
		mHolder.textView_balance_service_price.setText(list.get(position).amount+"");
		mHolder.textView_balance_service_time.setText(Utils.ChangeTime(list.get(position).tradeDate));
		mHolder.textView_balance_service_detail_message.setText(list.get(position).status);
		if (list.get(position).amount.contains("+")) {
			mHolder.textView_balance_service_price.setTextColor(Color.parseColor("#FF9531"));
		}else if (list.get(position).amount.contains("-")) {
			mHolder.textView_balance_service_price.setTextColor(Color.parseColor("#464646"));
		}
		return convertView;
	}

	class ViewHolder{
		TextView textView_balance_service_name;
		TextView textView_balance_service_price;
		TextView textView_balance_service_time;
		TextView textView_balance_service_detail_message;
	}*/
}
