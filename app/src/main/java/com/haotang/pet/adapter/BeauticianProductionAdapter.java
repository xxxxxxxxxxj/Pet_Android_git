package com.haotang.pet.adapter;

import java.util.ArrayList;

import com.haotang.pet.BeauticianProductionDetailActivity;
import com.haotang.pet.BeauticianProductuonActivity;
import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.Production;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BeauticianProductionAdapter extends BaseAdapter {
	private Activity context;
	private ArrayList<Production> list;
	private LayoutInflater mInflater;
	private int imageWidth;
	
	public BeauticianProductionAdapter(Activity context,int imageWidth,
			ArrayList<Production> list) {
		super();
		this.context = context;
		this.list = list;
		this.imageWidth = imageWidth;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = list.size();
		return count%2==0?count/2:count/2+1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if(convertView == null){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.beauticianproduction_item, null);
			holder.srivItem1 = (SelectableRoundedImageView) convertView.findViewById(R.id.sriv_beauticianproduction_item_1);
			holder.srivItem2 = (SelectableRoundedImageView) convertView.findViewById(R.id.sriv_beauticianproduction_item_2);
			holder.tvItem1 = (TextView) convertView.findViewById(R.id.tv_beauticianproduction_item_1);
			holder.tvItem2 = (TextView) convertView.findViewById(R.id.tv_beauticianproduction_item_2);
			holder.rlItem1 = (RelativeLayout) convertView.findViewById(R.id.rl_beauticianproduction_item_1);
			holder.rlItem2 = (RelativeLayout) convertView.findViewById(R.id.rl_beauticianproduction_item_2);
			setProductionHeight(holder.srivItem1,imageWidth);
			setProductionHeight(holder.srivItem2,imageWidth);
			
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		if(list.size()<2*(position+1)){
			//奇数个
			holder.rlItem2.setVisibility(View.INVISIBLE);
			holder.rlItem1.setVisibility(View.VISIBLE);
			holder.tvItem1.setText(list.get(position*2).title);
			
			holder.srivItem1.setTag(list.get(2*position).smallimage);
			holder.srivItem1.setImageResource(R.drawable.icon_production_default);
			
			if(list.get(2*position).smallimage!=null&&!"".equals(list.get(2*position).smallimage.trim())){
				ImageLoaderUtil.loadImg(list.get(2*position).smallimage, holder.srivItem1,0, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View view) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(String path, View view, Bitmap bitmap) {
						// TODO Auto-generated method stub
						if(view!=null&&bitmap!=null){
							ImageView iv = (ImageView) view;
							String imagetag = (String) iv.getTag();
							if(path!=null&&path.equals(imagetag)){
								iv.setImageBitmap(bitmap);
							}
						}
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}else{
			//偶数个
			holder.rlItem2.setVisibility(View.VISIBLE);
			holder.rlItem1.setVisibility(View.VISIBLE);
			holder.tvItem1.setText(list.get(position*2).title);
			
			
			holder.srivItem1.setTag(list.get(2*position).smallimage);
			holder.srivItem1.setImageResource(R.drawable.icon_production_default);
			
			if(list.get(2*position).smallimage!=null&&!"".equals(list.get(2*position).smallimage.trim())){
				ImageLoaderUtil.loadImg(list.get(2*position).smallimage, holder.srivItem1,0, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View view) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(String path, View view, Bitmap bitmap) {
						// TODO Auto-generated method stub
						if(view!=null&&bitmap!=null){
							ImageView iv = (ImageView) view;
							String imagetag = (String) iv.getTag();
							if(path!=null&&path.equals(imagetag)){
								iv.setImageBitmap(bitmap);
							}
						}
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}
			
			holder.srivItem2.setTag(list.get(2*position+1).smallimage);
			holder.srivItem2.setImageResource(R.drawable.icon_production_default);
			
			if(list.get(2*position+1).smallimage!=null&&!"".equals(list.get(2*position+1).smallimage.trim())){
				ImageLoaderUtil.loadImg(list.get(2*position+1).smallimage, holder.srivItem2,0, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View view) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(String path, View view, Bitmap bitmap) {
						// TODO Auto-generated method stub
						if(view!=null&&bitmap!=null){
							ImageView iv = (ImageView) view;
							String imagetag = (String) iv.getTag();
							if(path!=null&&path.equals(imagetag)){
								iv.setImageBitmap(bitmap);
							}
						}
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			}
			
			
			holder.tvItem2.setText(list.get(position*2+1).title);
		}
		holder.srivItem1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(2*position);
			}
		});
		holder.srivItem2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(2*position+1);
			}
		});
		
		return convertView;
	}
	
	private void goNext(int index){
		Intent intent = new Intent(context, BeauticianProductionDetailActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		context.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("index", index);
		intent.putExtra("previous", Global.PRE_BEAUTICIANPRODUCTIONLIST_TO_PRODUCTIONDETAIL);
		
		context.startActivity(intent);
	}
	
	private void setProductionHeight(ImageView iv, int height){
		LayoutParams lParams = iv.getLayoutParams();
		lParams.width = height;
		lParams.height = height;
		iv.setLayoutParams(lParams);
	}
	
	private class Holder{
		SelectableRoundedImageView srivItem1;
		SelectableRoundedImageView srivItem2;
		TextView tvItem1;
		TextView tvItem2;
		RelativeLayout rlItem1;
		RelativeLayout rlItem2;
	}

}
