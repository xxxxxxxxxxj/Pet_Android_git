package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.OtherGoods;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class OtherGoodS<T> extends CommonAdapter<T>{

	public OtherGoodS(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OtherGoods goods = (OtherGoods) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_other_goods, position);
		viewHolder.setText(R.id.other_textview_name, goods.name);
		viewHolder.setText(R.id.other_textview_goods_rmb, "¥"+goods.money);
		viewHolder.setText(R.id.other_textview_goods_num, " x"+goods.nums);
		return viewHolder.getConvertView();
	}

}
