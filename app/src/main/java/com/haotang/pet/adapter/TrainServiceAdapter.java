package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.TrainAppointMentActivity;
import com.haotang.pet.entity.TrainService;
import com.haotang.pet.entity.TrainServiceEvery;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.ViewHolder;

/*public class TrainServiceAdapter<T> extends CommonAdapter<T>{
	private TrainServiceOpenAdapter openAdapter;
	public TrainServiceAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TrainService trainService = (TrainService) mDatas.get(position);
		ViewHolder viewHolder  = ViewHolder.get(mContext, convertView, parent, R.layout.item_train_service_adaper, position);
		viewHolder.setText(R.id.textView_service_name, trainService.type);
		viewHolder.setBackgroundCircle(R.id.imageView_service_icon, trainService.icon, R.drawable.user_icon_unnet);
		try {
			viewHolder.setText(R.id.textview_layout_service_low_price, trainService.everyTrainServiceList.get(0).price+"");
		} catch (Exception e) {
			e.printStackTrace();
			Utils.mLogError("==-->没取到价格崩了");
		}
		
		final MListview train_pets_service_choose_open = viewHolder.getView(R.id.train_pets_service_choose_open);
		viewHolder.getView(R.id.layout_service_all_train).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TrainService trainServiceList = (TrainService) mDatas.get(position);
				if (!trainServiceList.isOpen) {
					openAdapter = new TrainServiceOpenAdapter<TrainServiceEvery>(mContext, trainServiceList.everyTrainServiceList);
					train_pets_service_choose_open.setAdapter(openAdapter);
					train_pets_service_choose_open.setVisibility(View.VISIBLE);
				}else {
					train_pets_service_choose_open.setVisibility(View.GONE);
				}
				trainServiceList.isOpen=!trainServiceList.isOpen;
				mDatas.set(position, (T) trainServiceList);
				Utils.setListHeight(train_pets_service_choose_open);
				Utils.setListHeight(TrainAppointMentActivity.train_pets_service_choose);
			}
		});
		return viewHolder.getConvertView();
	}

}*/
