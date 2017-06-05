package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.BeauticianCommonPicActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.util.ImageLoaderUtil;

public class BeauticianCommentAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Comment> list;
	private LayoutInflater mInflater;
	
	public BeauticianCommentAdapter(Context context, ArrayList<Comment> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(convertView == null){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.beauticiancommentslist_item_new, null);
			holder.srivBeautician = (ImageView) convertView.findViewById(R.id.sriv_beauticiancommentslist_item_beautician);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_beauticiancommentslist_item_name);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_beauticiancommentslist_item_content);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_beauticiancommentslist_item_time);
			holder.ivImage1 = (ImageView) convertView.findViewById(R.id.iv_beauticiancommentslist_item_image1);
			holder.ivImage2 = (ImageView) convertView.findViewById(R.id.iv_beauticiancommentslist_item_image2);
			holder.ivImage3 = (ImageView) convertView.findViewById(R.id.iv_beauticiancommentslist_item_image3);
			holder.TextView_waiter_Reply = (TextView) convertView.findViewById(R.id.TextView_waiter_Reply);
			holder.textview_waiter_reply_time = (TextView) convertView.findViewById(R.id.textview_waiter_reply_time);
			holder.item_layout_reply = (LinearLayout) convertView.findViewById(R.id.item_layout_reply);
			holder.ll_beauticiancommetslist_item_image = (LinearLayout) convertView.findViewById(R.id.ll_beauticiancommetslist_item_image);
			holder.imageview_tag = (ImageView) convertView.findViewById(R.id.imageview_tag);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(list.get(position).memberIcon)) {
			holder.imageview_tag.setVisibility(View.VISIBLE);
			ImageLoaderUtil.setImageWithTag(holder.imageview_tag, list.get(position).memberIcon, 0);
		}else {
			holder.imageview_tag.setVisibility(View.GONE);
		}
		holder.srivBeautician.setTag(list.get(position).image);
		ImageLoaderUtil.setImageWithTag(holder.srivBeautician, list.get(position).image, R.drawable.user_icon_unnet);
		
		holder.tvTime.setText(list.get(position).time);
		holder.tvContent.setText(list.get(position).content);
		holder.tvName.setText(list.get(position).name);
		try {
			if (list.get(position).replyContent!=null&&!"".equals(list.get(position).replyContent)) {
				holder.item_layout_reply.setVisibility(View.VISIBLE);
				holder.TextView_waiter_Reply.setText(list.get(position).replyMan+":"+list.get(position).replyContent);
				holder.textview_waiter_reply_time.setText(list.get(position).replyTime+"");
			}else {
				holder.item_layout_reply.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.ivImage1.setVisibility(View.GONE);
		holder.ivImage2.setVisibility(View.GONE);
		holder.ivImage3.setVisibility(View.GONE);
		holder.ll_beauticiancommetslist_item_image.setVisibility(View.GONE);
		if(list.get(position).images!=null){
			holder.ll_beauticiancommetslist_item_image.setVisibility(View.VISIBLE);
			for(int i=0; i<list.get(position).images.length;i++){
				if(i == 0){
					holder.ivImage1.setVisibility(View.VISIBLE);
					holder.ivImage1.setTag(list.get(position).images[i]);
					ImageLoaderUtil.setImageWithTag(holder.ivImage1, list.get(position).images[i], R.drawable.icon_production_default);
				}else if(i == 1){
					holder.ivImage2.setVisibility(View.VISIBLE);
					holder.ivImage2.setTag(list.get(position).images[i]);
					ImageLoaderUtil.setImageWithTag(holder.ivImage2, list.get(position).images[i], R.drawable.icon_production_default);
				}else if(i == 2){
					holder.ivImage3.setVisibility(View.VISIBLE);
					holder.ivImage3.setTag(list.get(position).images[i]);
					ImageLoaderUtil.setImageWithTag(holder.ivImage3, list.get(position).images[i], R.drawable.icon_production_default);
				}
			}
		}
		holder.ivImage1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(0,list.get(position).images);
			}
		});
		holder.ivImage2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(1,list.get(position).images);
			}
		});
		holder.ivImage3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(2,list.get(position).images);
			}
		});
		
		return convertView;
	}
	
	private void goNext(int index,String[] pics){
		Intent intent = new Intent(context, BeauticianCommonPicActivity.class);
		intent.putExtra("index", index);
		intent.putExtra("pics", pics);
		
		context.startActivity(intent);
	}
	
	private class Holder{
		ImageView srivBeautician;
		TextView tvName;
		TextView tvContent;
		TextView tvTime;
		ImageView ivImage1;
		ImageView ivImage2;
		ImageView ivImage3;
		TextView TextView_waiter_Reply;
		TextView textview_waiter_reply_time;
		LinearLayout item_layout_reply;
		LinearLayout ll_beauticiancommetslist_item_image;
		ImageView imageview_tag;
	}

}
