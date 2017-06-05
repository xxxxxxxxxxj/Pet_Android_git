package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.haotang.pet.R;
import com.haotang.pet.entity.CommentStar;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class CommentStarAdapter<T> extends CommonAdapter<T>{

	public CommentStarAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CommentStar commentStar = (CommentStar) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_commentstart, position);
		viewHolder.setText(R.id.textView_every_show, commentStar.tag);
		viewHolder.getView(R.id.textView_every_show).setBackgroundResource(R.drawable.eva_star_un_choose);
		viewHolder.setTextColor(R.id.textView_every_show,"#CCCCCC");
		if (commentStar.ifChoose) {
			viewHolder.getView(R.id.textView_every_show).setBackgroundResource(R.drawable.eva_star_choose);
			viewHolder.setTextColor(R.id.textView_every_show,"#BB996C");
		}
		return viewHolder.getConvertView();
	}

}
