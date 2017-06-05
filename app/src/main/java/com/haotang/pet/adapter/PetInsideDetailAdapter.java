package com.haotang.pet.adapter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.LoginActivity;
import com.haotang.pet.PostUserInfoActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.PetInsideDetail;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;
import com.melink.bqmmsdk.sdk.BQMMMessageHelper;
import com.melink.bqmmsdk.widget.BQMMMessageText;

public class PetInsideDetailAdapter<T> extends CommonAdapter<T>{

	public PetInsideDetailAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder =ViewHolder.get(mContext, convertView, parent, R.layout.item_pet_circle_inside_detail, position);
		final PetInsideDetail insideDetail = (PetInsideDetail) mDatas.get(position);
		if (insideDetail.userId==-1) {
			viewHolder.getView(R.id.layout_is_show).setVisibility(View.GONE);
		}else {
			viewHolder.getView(R.id.layout_is_show).setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(insideDetail.userName)) {
			viewHolder.setText(R.id.textview_inside_detail_name,"");
		}else {
			viewHolder.setText(R.id.textview_inside_detail_name, insideDetail.userName+":");
		}
		if (insideDetail.duty==1||insideDetail.duty==2) {
			viewHolder.getView(R.id.imageview_petcircle_tag).setVisibility(View.VISIBLE);
		}else {
			viewHolder.getView(R.id.imageview_petcircle_tag).setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(insideDetail.memberIcon)) {
			viewHolder.getView(R.id.imageview_petcircle_tag).setVisibility(View.VISIBLE);
			if (insideDetail.duty==1||insideDetail.duty==2) {
				ImageView imageView = viewHolder.getView(R.id.imageview_petcircle_tag);
				imageView.setImageResource(R.drawable.dz_jl_icon);
			}else {
				viewHolder.getView(R.id.imageview_petcircle_tag).setVisibility(View.VISIBLE);
				ImageLoaderUtil.loadImg(insideDetail.memberIcon, (ImageView)viewHolder.getView(R.id.imageview_petcircle_tag),0, null);
			}
		}else {
			viewHolder.getView(R.id.imageview_petcircle_tag).setVisibility(View.GONE);
		}
		viewHolder.setText(R.id.textview_inside_detail_time, insideDetail.created);
		String content = insideDetail.content;
		int type = insideDetail.contentType;
		int id = insideDetail.PostCommentId;
		viewHolder.setBQMMMessageText(R.id.textView_eva_content, content, id, type);
		if (TextUtils.isEmpty(insideDetail.avatar)) {
			ImageView imageView_eva_self_icon = viewHolder.getView(R.id.imageView_eva_self_icon);
			imageView_eva_self_icon.setImageResource(R.drawable.icon_self);
		}else {
			viewHolder.setBackgroundCircle(R.id.imageView_eva_self_icon, insideDetail.avatar, R.drawable.icon_self);
		}
		viewHolder.getView(R.id.imageView_eva_self_icon).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Utils.checkLogin(mContext)) {
					goToNext(PostUserInfoActivity.class,insideDetail.userId,0, "");
				}else {
					goToNext(LoginActivity.class,0,0, "");
				}
			}
		});
		return viewHolder.getConvertView();
	}
	private void goToNext(Class clazz, int userId,int previous, String flag) {
		Intent intent = new Intent(mContext, clazz);
		intent.putExtra("userId", userId);
		intent.putExtra("flag", flag);
		intent.putExtra("previous", previous);
		mContext.startActivity(intent);
	}
}
