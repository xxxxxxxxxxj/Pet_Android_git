package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haotang.pet.R;
import com.haotang.pet.entity.CardMakeDog;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class CardMakeDogAdapter<T> extends CommonAdapter<T>{

	private int pos=-1;
	public CardMakeDogAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	public void setChooseState(int pos){
		this.pos = pos;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CardMakeDog cardMakeDog = (CardMakeDog) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,R.layout.item_card_make_dog, position);
		viewHolder.setText(R.id.textview_first_make_dog, cardMakeDog.name);
		viewHolder.setText(R.id.textview_first_make_dog_price, "¥"+cardMakeDog.price);
		LinearLayout layout_first_make_dog_card = viewHolder.getView(R.id.layout_first_make_dog_card);
		ImageView imageview_first_make_dog_card = viewHolder.getView(R.id.imageview_first_make_dog_card);
		imageview_first_make_dog_card.setImageResource(R.drawable.complaint_reason_disable);
		layout_first_make_dog_card.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		if (cardMakeDog.isCerti==0) {
			layout_first_make_dog_card.setBackgroundColor(mContext.getResources().getColor(R.color.af8f8f8));
			viewHolder.setTextColor(R.id.textview_first_make_dog,"#999999");
			viewHolder.setTextColor(R.id.textview_first_make_dog_price,"#999999");
			imageview_first_make_dog_card.setImageResource(R.drawable.cards_not_choose_pet);
		}else if(cardMakeDog.isCerti==1){
			layout_first_make_dog_card.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			viewHolder.setTextColor(R.id.textview_first_make_dog,"#333333");
			viewHolder.setTextColor(R.id.textview_first_make_dog_price,"#333333");
		}
		if (pos == position) {
			if (cardMakeDog.isChoose) {
				imageview_first_make_dog_card.setImageResource(R.drawable.complaint_reason);
			}else {
				imageview_first_make_dog_card.setImageResource(R.drawable.complaint_reason_disable);
			}
		}
		return viewHolder.getConvertView();
	}

}
