package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.CardMakeDog;
import com.haotang.pet.entity.CardsBuy;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class MyCardsEveryItemAdapter<T> extends CommonAdapter<T>{

	private CardMakeDog CardMakeDog=null;;
	public MyCardsEveryItemAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	public void setCutDownPrice(CardMakeDog CardMakeDog){
		this.CardMakeDog = CardMakeDog;
		notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CardsBuy cardsBuy  = (CardsBuy) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_cards_every_item, position);
		viewHolder.setText(R.id.textView_card_service_name, cardsBuy.title);
		StringBuilder builder = new StringBuilder();
		if (cardsBuy.listStr.size()>0) {
			for (int i = 0; i < cardsBuy.listStr.size(); i++) {
				if (i==cardsBuy.listStr.size()-1) {
					builder.append(cardsBuy.listStr.get(i));
				}else {
					builder.append(cardsBuy.listStr.get(i)+"\n\n");
				}
			}
//			builder.append(cardsBuy.listStr)
		}
//		if (TextUtils.isEmpty(builder)) {
//			
//		}else {
//		viewHolder.setText(R.id.textView_card_service_detail, cardsBuy.content);//这个返回新的
			
//		}
		viewHolder.setText(R.id.textView_card_service_detail,builder.toString());//这个返回新的
		viewHolder.setText(R.id.textView_cards_price_show, ""+cardsBuy.price);
		viewHolder.setText(R.id.textview_cutdown_num,cardsBuy.discount+"折");
		if (CardMakeDog!=null) {
			if (CardMakeDog.isChoose) {
				viewHolder.setText(R.id.textview_cutdown_num,cardsBuy.petCardDiscount+"折");
				viewHolder.setText(R.id.textView_cards_price_show,""+cardsBuy.price1);
			}else {
				viewHolder.setText(R.id.textview_cutdown_num,cardsBuy.discount+"折");
				viewHolder.setText(R.id.textView_cards_price_show,""+cardsBuy.price);
			}
		}else {
			viewHolder.setText(R.id.textview_cutdown_num,cardsBuy.discount+"折");
			viewHolder.setText(R.id.textView_cards_price_show,""+cardsBuy.price);
			
			viewHolder.setText(R.id.textView_cards_detail_cutdown,"¥"+cardsBuy.listPrice);
		}
		TextView textView_cards_detail_cutdown = viewHolder.getView(R.id.textView_cards_detail_cutdown);
		textView_cards_detail_cutdown.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		textView_cards_detail_cutdown.getPaint().setAntiAlias(true);
		return viewHolder.getConvertView();
	}

}
