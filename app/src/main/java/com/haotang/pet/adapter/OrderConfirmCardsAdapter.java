package com.haotang.pet.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.entity.CardItems;
import com.haotang.pet.entity.CardsConfirmOrder;
import com.haotang.pet.entity.packageDetails;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.ViewHolder;

public class OrderConfirmCardsAdapter<T> extends CommonAdapter<T>{
	
	private int pos= -1;
	private ArrayList<CardsConfirmOrder> OldCardsConfirmOrders;
	private ArrayList<Integer> listIds;
	public OrderConfirmCardsAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}
	public void setOldData(ArrayList<CardsConfirmOrder> OldCardsConfirmOrders/*,ArrayList<Integer> listIds*/){
		this.OldCardsConfirmOrders = OldCardsConfirmOrders;
//		this.listIds = listIds;
	}
	public void setChooseState(int pos){
		this.pos = pos;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CardsConfirmOrder cardsConfirmOrder = (CardsConfirmOrder) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_order_confirm_choose_card, position);
		viewHolder.setText(R.id.item_choose_cards_name, cardsConfirmOrder.cardName);
		viewHolder.setText(R.id.item_choose_cards_detail, cardsConfirmOrder.tip);
		viewHolder.setImageResource(R.id.imageview_show_back_img, R.drawable.mycard_back);
		if (!TextUtils.isEmpty(cardsConfirmOrder.cardBgImg)) {
			viewHolder.setBackgroundNormal(R.id.imageview_show_back_img,cardsConfirmOrder.cardBgImg, 0);
		}
//		if (cardsConfirmOrder.bathCountOld>0) {
//			viewHolder.setText(R.id.textview_choose_top_show_cards, cardsConfirmOrder.bathTitleTag);
//			viewHolder.setText(R.id.textview_choose_top_show_cards_num, "剩余"+cardsConfirmOrder.bathCountOld+"次");
//		}else {
//			viewHolder.getView(R.id.layout_top).setVisibility(View.GONE);
//		}
//		if (cardsConfirmOrder.beautyCountOld>0) {
//			viewHolder.setText(R.id.textview_choose_bottom_show_cards, cardsConfirmOrder.beautyTitleTag);
//			viewHolder.setText(R.id.textview_choose_bottom_show_cards_num, "剩余"+cardsConfirmOrder.beautyCountOld+"次");
//		}else {
//			viewHolder.getView(R.id.layout_bottom).setVisibility(View.GONE);
//		}
		MListview listview_cards_nums = viewHolder.getView(R.id.listview_cards_nums);
//		listview_cards_nums.setEnabled(false);
		if (cardsConfirmOrder.arrayList.size()>0) {
			listview_cards_nums.setVisibility(View.VISIBLE);
			OrderCardnumsAdapter<CardItems> packageAdapter = new OrderCardnumsAdapter<CardItems>(mContext, cardsConfirmOrder.arrayList);
			listview_cards_nums.setAdapter(packageAdapter);
		}else {
			listview_cards_nums.setVisibility(View.GONE);
		}
		ImageView item_image_choose_card = viewHolder.getView(R.id.item_image_choose_card);
		item_image_choose_card.setImageResource(R.drawable.card_not_choose_show);
		Utils.mLogError("==-->cardsConfirmOrder.isChoose:= "+cardsConfirmOrder.isChoose);
		if (cardsConfirmOrder.isChoose==0) {
			item_image_choose_card.setImageResource(R.drawable.card_not_choose_show);
		}else if (cardsConfirmOrder.isChoose==1) {
			item_image_choose_card.setImageResource(R.drawable.choose_card_ok);
			
		}
		
		if (pos==position) {
			if (cardsConfirmOrder.isChoose==0) {
				item_image_choose_card.setImageResource(R.drawable.card_not_choose_show);
			}else if (cardsConfirmOrder.isChoose==1) {
				item_image_choose_card.setImageResource(R.drawable.choose_card_ok);
			}
		}
		
		return viewHolder.getConvertView();
	}

}
