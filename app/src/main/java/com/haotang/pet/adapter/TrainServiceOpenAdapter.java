package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.haotang.pet.FostercareOrderConfirmActivity;
import com.haotang.pet.R;
import com.haotang.pet.TrainAppointMentActivity;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.TrainServiceEvery;
import com.haotang.pet.entity.TrainTransfer;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class TrainServiceOpenAdapter<T> extends CommonAdapter<T>{

	private String confirmtip;
	private String petName;
	private String time;
	private TrainTransfer trainTransfer=null;
	private int shopId;
	public TrainServiceOpenAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TrainServiceEvery trainServiceEvery = (TrainServiceEvery) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_train_service_open, position);
		viewHolder.setText(R.id.service_open_name, trainServiceEvery.name);
		viewHolder.setText(R.id.service_open_detail,trainServiceEvery.info);
		viewHolder.setText(R.id.service_open_train_price,trainServiceEvery.price+"");
		if (!TextUtils.isEmpty(TrainAppointMentActivity.noTrainersMsg)) {
			viewHolder.setText(R.id.button_go_apponit_train, "约满");
			viewHolder.getView(R.id.button_go_apponit_train).setBackgroundResource(R.drawable.bg_button_train_appoinment_can_not);
		}else {
			viewHolder.setText(R.id.button_go_apponit_train, "预约");
			viewHolder.getView(R.id.button_go_apponit_train).setBackgroundResource(R.drawable.bg_button_service_apponit_ok);
		}
		if (position==mDatas.size()) {
			viewHolder.getView(R.id.train_video_more_line).setVisibility(View.GONE);
		}
		viewHolder.getView(R.id.layout_go_next).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext(position);
			}
		});
		viewHolder.getView(R.id.button_go_apponit_train).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNext(position);
			}
		});
		return viewHolder.getConvertView();
	}
	private void goNext(final int position) {
		if (TextUtils.isEmpty(TrainAppointMentActivity.petname)) {
			ToastUtil.showToastShortCenter(mContext, "请选择宠物");
			return;
		}else if (TextUtils.isEmpty(TrainAppointMentActivity.apptime)) {
			ToastUtil.showToastShortCenter(mContext, "请选择预约时间");
			return;
		}else if (TrainAppointMentActivity.transferLast==null) {
			ToastUtil.showToastShortCenter(mContext, "请选择中转站");
			return;
		}else if (!TextUtils.isEmpty(TrainAppointMentActivity.noTrainersMsg)) {
			ToastUtil.showToastShortCenter(mContext,TrainAppointMentActivity.noTrainersMsg);
			return;
		}
		TrainServiceEvery trainServiceEveryClick =  (TrainServiceEvery) mDatas.get(position);
		Intent intent = new Intent(mContext, FostercareOrderConfirmActivity.class);
		intent.putExtra("pre", Global.TRAIN_TO_ORDER_CONFIRM);
		intent.putExtra("confirmtip", TrainAppointMentActivity.confirm_tip);
		intent.putExtra("TrainServiceEvery", trainServiceEveryClick);
		intent.putExtra("trainTransfer", TrainAppointMentActivity.transferLast);
		intent.putExtra("customerpetname", TrainAppointMentActivity.customerpetname);
		intent.putExtra("customerpetid", TrainAppointMentActivity.customerpetid);
		intent.putExtra("petname", TrainAppointMentActivity.petname);
		intent.putExtra("time", TrainAppointMentActivity.apptime);
		intent.putExtra("shopId", TrainAppointMentActivity.shopId);
		intent.putExtra("petid", TrainAppointMentActivity.petid);
		intent.putExtra("agreementPage",TrainAppointMentActivity.agreementPage);
		mContext.startActivity(intent);
	}
}
