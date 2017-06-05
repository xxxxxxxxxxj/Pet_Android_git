package com.haotang.pet.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.HomeStep;
import com.haotang.pet.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class GoHomeStepAdapter extends BaseAdapter{

	private Context context;
	
	private int wh[];
	private HomeStep homeStep;
	public GoHomeStepAdapter(Context context,int wh[],HomeStep homeStep){
		this.context=context;
		this.wh=wh;
		this.homeStep=homeStep;
	}
	public GoHomeStepAdapter(Context context,int wh[]){
		this.context=context;
		this.wh=wh;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return homeStep.list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return homeStep.list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (convertView==null) {
			mHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_go_home_detail,null);
			mHolder.textView_item_step_show = (TextView) convertView.findViewById(R.id.textView_item_step_show);
			mHolder.imageView_item_step_show =(ImageView) convertView.findViewById(R.id.imageView_item_step_show);
			convertView.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.textView_item_step_show.setText(homeStep.list.get(position).txt);
		if (homeStep.list.size()>0) {
			ImageLoaderUtil.loadImg(homeStep.list.get(position).img, mHolder.imageView_item_step_show,0, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
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
		convertView.setLayoutParams(new GridView.LayoutParams(wh[0] / 2, wh[0] / 2+50));
		return convertView;
	}

	class ViewHolder{
		ImageView imageView_item_step_show;
		TextView textView_item_step_show;
	}
}
