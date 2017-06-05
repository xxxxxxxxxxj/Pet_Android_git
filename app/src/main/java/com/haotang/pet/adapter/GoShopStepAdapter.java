package com.haotang.pet.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.ShopEntity;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.view.CircleImageView;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class GoShopStepAdapter extends BaseAdapter{

	private Context context;
//	private List<String> list;
	private ShopEntity shopEntity;
	public GoShopStepAdapter(Context context,ShopEntity shopEntity){
		this.context=context;
		this.shopEntity=shopEntity;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shopEntity.list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return shopEntity.list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder=null;
		if (view==null) {
			mHolder = new ViewHolder();
			view=LayoutInflater.from(context).inflate(R.layout.item_go_shop_detail, null);
			mHolder.imageView_item_go_shop_icon=(SelectableRoundedImageView) view.findViewById(R.id.imageView_item_go_shop_icon);
			mHolder.textView_item_go_shop_title=(TextView) view.findViewById(R.id.textView_item_go_shop_title);
			view.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) view.getTag();
		}
		ImageLoaderUtil.loadImg(shopEntity.list.get(arg0).img, mHolder.imageView_item_go_shop_icon,0, new ImageLoadingListener() {
			
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
					ImageView iv = (ImageView)view;
					iv.setImageBitmap(bitmap);
				}
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		mHolder.textView_item_go_shop_title.setText(shopEntity.list.get(arg0).name);
		return view;
	}

	class ViewHolder{
		SelectableRoundedImageView imageView_item_go_shop_icon;
		TextView textView_item_go_shop_title;
	}
}
