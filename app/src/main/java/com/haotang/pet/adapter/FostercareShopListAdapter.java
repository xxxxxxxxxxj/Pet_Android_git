package com.haotang.pet.adapter;

import java.util.ArrayList;

import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.adapter.RoomAdapter.Holder;
import com.haotang.pet.entity.ShopsWithPrice;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ResourceAsColor") public class FostercareShopListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private ArrayList<ShopsWithPrice> list;
	
	public FostercareShopListAdapter(Context context,
			ArrayList<ShopsWithPrice> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if(convertView==null){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.fostercareshoplist_item, null);
			holder.srivImage = (SelectableRoundedImageView) convertView.findViewById(R.id.sriv_fostercareshoplist_item_image);
			holder.tvShopName = (TextView) convertView.findViewById(R.id.tv_fostercareshoplist_item_shopname);
			holder.tvShopPrice = (TextView) convertView.findViewById(R.id.tv_fostercareshoplist_item_shopprice);
			holder.tvShopPriceSuffix = (TextView) convertView.findViewById(R.id.tv_fostercareshoplist_item_shopprice_suffix);
			holder.tvShopDistance = (TextView) convertView.findViewById(R.id.tv_fostercareshoplist_item_shopdistance);
			holder.tvShopAddr = (TextView) convertView.findViewById(R.id.tv_fostercareshoplist_item_shopaddr);
			holder.tvSubmit = (TextView) convertView.findViewById(R.id.tv_fostercareshoplist_item_submit);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		ShopsWithPrice shop = list.get(position);
		holder.tvShopName.setText(shop.shopName);
		if (shop.status==2) {
			holder.tvShopPrice.setText(shop.lowestPrice);
		}else {
			holder.tvShopPrice.setText("¥"+shop.lowestPrice);
		}
		holder.tvShopDistance.setText("距宠物地址   "+shop.dist);
		holder.tvShopAddr.setText("地址："+shop.address);
		if(shop.isMulPrice){
			holder.tvShopPriceSuffix.setVisibility(View.VISIBLE);
		}else{
			holder.tvShopPriceSuffix.setVisibility(View.GONE);
		}
		if(shop.isFull&&shop.status!=2){
			holder.tvSubmit.setBackgroundResource(R.drawable.bg_button_c1_normal);
			holder.tvSubmit.setText("订满");
			holder.tvSubmit.setTextColor(context.getResources().getColor(R.color.white));
		}else if(shop.status==2){
			holder.tvSubmit.setText("即将\n开业");
			holder.tvSubmit.setBackgroundResource(R.drawable.bg_search_orangeborder);
			holder.tvSubmit.setTextColor(context.getResources().getColor(R.color.orange));
		}else {
			holder.tvSubmit.setBackgroundResource(R.drawable.bg_button_orager_oval);
			holder.tvSubmit.setText("预约");
			holder.tvSubmit.setTextColor(context.getResources().getColor(R.color.white));
		}
		
		holder.srivImage.setTag(shop.img);
		if(shop.img!=null&&!"".equals(shop.img.trim())){
			ImageLoaderUtil.loadImg(shop.img, holder.srivImage,0, 
					new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View view) {
					// TODO Auto-generated method stub
//					SelectableRoundedImageView iv = (SelectableRoundedImageView) view;
//					iv.setImageResource(R.drawable.icon_production_default);
				}
				
				@Override
				public void onLoadingFailed(String arg0, View view, FailReason arg2) {
					// TODO Auto-generated method stub
					SelectableRoundedImageView iv = (SelectableRoundedImageView) view;
					iv.setImageResource(R.drawable.icon_shop);
				}
				
				@Override
				public void onLoadingComplete(String path, View view, Bitmap bitmap) {
					// TODO Auto-generated method stub
					if(view!=null&&bitmap!=null){
						SelectableRoundedImageView iv = (SelectableRoundedImageView) view;
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
		
		
		return convertView;
	}
	
	class Holder{
		SelectableRoundedImageView srivImage;
		TextView tvShopName;
		TextView tvShopPrice;
		TextView tvShopPriceSuffix;
		TextView tvShopDistance;
		TextView tvShopAddr;
		TextView tvSubmit;
	}

}
