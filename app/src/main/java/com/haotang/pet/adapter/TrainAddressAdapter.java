package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.TrainTransfer;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class TrainAddressAdapter<T> extends CommonAdapter<T>{

	public TrainAddressAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TrainTransfer trainTransfer = (TrainTransfer) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_train_address, position);
		viewHolder.setText(R.id.address_item, trainTransfer.name);
		viewHolder.setText(R.id.textView_address_detail, trainTransfer.address);
		TextView textView = viewHolder.getView(R.id.textview_tel);
		textView.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线;
		textView.getPaint().setAntiAlias(true);//抗锯齿
		viewHolder.setText(R.id.textview_tel, trainTransfer.tel);
		viewHolder.getView(R.id.textview_tel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MDialog mDialog = new MDialog.Builder(mContext)
				.setTitle("").setType(MDialog.DIALOGTYPE_CONFIRM)
				.setMessage("您确定要拨打电话吗？").setCancelStr("否").setOKStr("是")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						TrainTransfer trainTransfer = (TrainTransfer) mDatas.get(position);
						Utils.telePhoneBroadcast(mContext,trainTransfer.tel);
					}
				}).build();
				mDialog.show();
				
			}
		});
		return viewHolder.getConvertView();
	}

}
