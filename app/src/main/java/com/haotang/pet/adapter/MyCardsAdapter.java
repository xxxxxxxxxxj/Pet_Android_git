package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Cards;
import com.haotang.pet.entity.packageDetails;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.ViewHolder;

public class MyCardsAdapter<T> extends CommonAdapter<T>{

	private boolean isHistory = false;
	private LayoutParams params;
	private LinearLayout.LayoutParams params2;
	public MyCardsAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
		params = new LayoutParams(LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics((Activity)mContext)[0]*255/716);
		params2 = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics((Activity)mContext)[0]*255/716);
		params.leftMargin=20;
		params.rightMargin=20;
		params.topMargin=20;
	}

	public void setIsHistory(boolean isHistory){
		this.isHistory = isHistory;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Cards cards = (Cards) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_my_cards, position);
		
		viewHolder.setText(R.id.textView_top_servicename, cards.cardName);
		try {
			viewHolder.setText(R.id.textview_cards_detail, cards.explainText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		if (TextUtils.isEmpty(cards.bath_level_text)) {
//			viewHolder.getView(R.id.top_cards_name_and_nums).setVisibility(View.GONE);
//			viewHolder.getView(R.id.layout_top).setVisibility(View.GONE);
//		}else {
//			viewHolder.getView(R.id.layout_top).setVisibility(View.VISIBLE);
//			viewHolder.getView(R.id.top_cards_name_and_nums).setVisibility(View.VISIBLE);
//			viewHolder.setText(R.id.top_cards_name_and_nums, cards.bath_level_text);
//		}
//		if (TextUtils.isEmpty(cards.bath_count_text)) {
//			viewHolder.getView(R.id.top_cards_nums).setVisibility(View.GONE);
//		}else {
//			viewHolder.getView(R.id.top_cards_nums).setVisibility(View.VISIBLE);
//			viewHolder.setText(R.id.top_cards_nums, cards.bath_count_text);
//		}
//		
//		if (TextUtils.isEmpty(cards.beauty_level_text)) {
//			viewHolder.getView(R.id.layout_bottom).setVisibility(View.GONE);
//			viewHolder.getView(R.id.bottom_cards_name_and_nums).setVisibility(View.GONE);
//		}else {
//			viewHolder.getView(R.id.layout_bottom).setVisibility(View.VISIBLE);
//			viewHolder.getView(R.id.bottom_cards_name_and_nums).setVisibility(View.VISIBLE);
//			viewHolder.setText(R.id.bottom_cards_name_and_nums, cards.beauty_level_text);
//		}
//		if (TextUtils.isEmpty(cards.beauty_count_text)) {
//			viewHolder.getView(R.id.bottom_cards_nums).setVisibility(View.GONE);
//		}else {
//			viewHolder.getView(R.id.bottom_cards_nums).setVisibility(View.VISIBLE);
//			viewHolder.setText(R.id.bottom_cards_nums, cards.beauty_count_text);
//		}
		
		
		final MListview listview_show_cards = viewHolder.getView(R.id.listview_show_cards);
		
		
		viewHolder.getView(R.id.textview_used_history_one).setVisibility(View.GONE);
		if (!TextUtils.isEmpty(cards.timeAndNums)) {
			viewHolder.getView(R.id.textview_used_history_one).setVisibility(View.VISIBLE);
			viewHolder.setText(R.id.textview_used_history_one, cards.timeAndNums);
		}else {
			viewHolder.getView(R.id.textview_used_history_one).setVisibility(View.VISIBLE);
			viewHolder.setText(R.id.textview_used_history_one,"暂无使用记录");
		}
		RelativeLayout layout_show_top = viewHolder.getView(R.id.layout_show_top);
//		layout_show_top.setLayoutParams(params2);
		if (!TextUtils.isEmpty(cards.cardBgImg)) {
			ImageView my_cards_image_back = viewHolder.getView(R.id.my_cards_image_back);
//			my_cards_image_back.setLayoutParams(params);
			try {
				viewHolder.setBackgroundNormal(R.id.my_cards_image_back, cards.cardBgImg,0/* R.drawable.mycard_back*/);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		LinearLayout layout_top_cards_detail = viewHolder.getView(R.id.layout_top_cards_detail);
//		layout_top_cards_detail.setLayoutParams(params);
		TextView cards_to_apponit = viewHolder.getView(R.id.cards_to_apponit);
		final LinearLayout layout_bottom_appoint = viewHolder.getView(R.id.layout_bottom_appoint);
		final ImageView imageView_open_close = viewHolder.getView(R.id.imageView_open_close);
		if (cards.isOpen) {
			layout_bottom_appoint.setVisibility(View.VISIBLE);
			imageView_open_close.setImageResource(R.drawable.cards_is_open);
		}else {
			layout_bottom_appoint.setVisibility(View.GONE);
			imageView_open_close.setImageResource(R.drawable.card_not_open);
		}
		
		viewHolder.getView(R.id.imageView_open_close).setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(cards.petCard)) {
//			if (cards.petCard.equals("1")) {
				//这里是犬证卡 一会说展开一会不展开。坑
				viewHolder.getView(R.id.imageView_open_close).setVisibility(View.GONE);
//			}
		}else {
			layout_top_cards_detail.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (layout_bottom_appoint.getVisibility()==View.VISIBLE) {
						Cards cardsClick = (Cards) mDatas.get(position);
						cardsClick.isOpen = false;
						layout_bottom_appoint.setVisibility(View.GONE);
						imageView_open_close.setImageResource(R.drawable.card_not_open);
						mDatas.set(position, (T) cardsClick);
					}else {
						for (int i = 0; i < mDatas.size(); i++) {
							Cards cardsEvery = (Cards) mDatas.get(i);
							cardsEvery.isOpen = false;
							mDatas.set(i, (T) cardsEvery);
						}
						
						
						//notifyDataSetChanged();
						Cards cardsClick = (Cards) mDatas.get(position);
						if (cardsClick.isOpen) {
							layout_bottom_appoint.setVisibility(View.VISIBLE);
							imageView_open_close.setImageResource(R.drawable.cards_is_open);
						}else {
							layout_bottom_appoint.setVisibility(View.GONE);
							imageView_open_close.setImageResource(R.drawable.card_not_open);
						}
						if (cardsClick.pkdes.size()>0) {
							PackageAdapter<packageDetails> packageAdapter = new PackageAdapter<packageDetails>(mContext, cardsClick.pkdes);
							listview_show_cards.setAdapter(packageAdapter);
						}
						cardsClick.isOpen = !cardsClick.isOpen;
						mDatas.set(position, (T) cardsClick);
					}
					notifyDataSetChanged();
				}
			});
		}
		if (isHistory) {
			cards_to_apponit.setVisibility(View.INVISIBLE);
			ImageView my_cards_image_back = viewHolder.getView(R.id.my_cards_image_back);
//			my_cards_image_back.setLayoutParams(params);
			try {
				viewHolder.setBackgroundNormal(R.id.my_cards_image_back,"",R.drawable.card_out_data);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			cards_to_apponit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					sendToMian();
					mContext.finish();
				}
			});
		}
		return viewHolder.getConvertView();
	}
	private void sendToMian(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		intent.putExtra("previous", Global.CARDS_TO_MAINACTIVITY);
		mContext.sendBroadcast(intent);
		Utils.mLogError("开始发送广播");
	}
}
