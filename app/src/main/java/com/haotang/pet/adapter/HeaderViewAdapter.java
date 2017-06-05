package com.haotang.pet.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.view.CircleImageView;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class HeaderViewAdapter extends BaseAdapter{
	private List<ArrayMap<String,Object>> list = null;
	private Context context;
	private int kind;
	public HeaderViewAdapter(Context context,int kind,List<ArrayMap<String,Object>> list){
		this.context=context;
		this.list=list;
		this.kind = kind;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder =null;
		if (view==null) {
			mHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_headview, null);
			mHolder.headerview_imageView_animal_icon = (SelectableRoundedImageView) view.findViewById(R.id.headerview_imageView_animal_icon);
			mHolder.headerview_title=(TextView) view.findViewById(R.id.headerview_title);
			view.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) view.getTag();
		}
		mHolder.headerview_title.setText(list.get(arg0).get("petName").toString());
		mHolder.headerview_imageView_animal_icon.setTag(CommUtil.getServiceNobacklash()+list.get(arg0).get("avatarPath"));
		if(kind == 1){
			mHolder.headerview_imageView_animal_icon.setImageResource(R.drawable.dog_icon_unnew);
		}else{
			mHolder.headerview_imageView_animal_icon.setImageResource(R.drawable.cat_icon_unnet);
		}
		
		if(list.get(arg0).get("avatarPath")!=null&&!"".equals(list.get(arg0).get("avatarPath"))){
			ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+(String)list.get(arg0).get("avatarPath"),mHolder.headerview_imageView_animal_icon,0, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View view) {
					// TODO Auto-generated method stub
//					((ImageView)view).setImageResource(R.drawable.dog_icon_unnew);
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
		
		return view;
	}
	  class ViewHolder {
		SelectableRoundedImageView headerview_imageView_animal_icon;
		TextView headerview_title;
	}

}
