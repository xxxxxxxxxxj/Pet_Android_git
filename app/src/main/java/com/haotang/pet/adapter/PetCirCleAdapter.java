package com.haotang.pet.adapter;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.haotang.pet.LoginActivity;
import com.haotang.pet.PetCircleInsideActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.PetCircle;
import com.haotang.pet.fragment.PetCircleFragment;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class PetCirCleAdapter<T> extends CommonAdapter<T>{
	public PetCirCleAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_pet_circle,position);
		final PetCircle circle = (PetCircle) mDatas.get(position);
		viewHolder.setBackgroundCircle(R.id.imageView_pet_circle_icon, circle.pic, R.drawable.user_icon_unnet);
		viewHolder.setText(R.id.item_petcircle_name, circle.groupName);
		viewHolder.setText(R.id.item_petcircle_detail, circle.description);
		Button item_petcircle_addin = viewHolder.getView(R.id.item_petcircle_addin);
		ImageView item_oetcircle_alreadyin = viewHolder.getView(R.id.item_oetcircle_alreadyin);
		if (circle.isFollowed==0) {
			item_petcircle_addin.setVisibility(View.VISIBLE);
			item_oetcircle_alreadyin.setVisibility(View.GONE);
		}else if (circle.isFollowed==1) {
			item_petcircle_addin.setVisibility(View.GONE);
			item_oetcircle_alreadyin.setVisibility(View.VISIBLE);
		}
		viewHolder.getView(R.id.item_layout_show).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,PetCircleInsideActivity.class);
				intent.putExtra("petCircle", circle);
				mContext.startActivity(intent);
			}
		});
		
		item_petcircle_addin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Utils.checkLogin(mContext)) {
					CommUtil.followGroup(SharedPreferenceUtil.getInstance(mContext).getString("cellphone", ""), mContext, circle.PostGroupId, followGroup);
				}else {
					Intent intent = new Intent(mContext, LoginActivity.class);
					mContext.startActivity(intent);
				}
			}
		});
		return viewHolder.getConvertView();
	}
	private AsyncHttpResponseHandler followGroup = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->加入圈子:"+new String(responseBody));
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0 ) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("isFollow")&&!objectData.isNull("isFollow")) {
							int isFollow = objectData.getInt("isFollow");
							if (isFollow==1) { // 1 已关注 2 已取消
								ToastUtil.showToastShortCenter(mContext, mContext.getResources().getString(R.string.pet_circle_join_success));
								PetCircleFragment.petCircleFragment.getData();
							}else{
//								ToastUtil.showToastShortCenter(mContext, "加入圈子失败");
							}
						}
					}
				}else {
					ToastUtil.showToastShortCenter(mContext, "加入圈子失败");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				try {
//					ToastUtil.showToastShortCenter(mContext, mContext.getResources().getString(R.string.errornet));
				} catch (NotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
		
	};
}
