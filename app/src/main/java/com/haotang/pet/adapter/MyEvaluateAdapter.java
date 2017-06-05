package com.haotang.pet.adapter;

import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.haotang.pet.R;
import com.haotang.pet.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class MyEvaluateAdapter extends BaseAdapter {
	private ArrayList<Bitmap>arrayList;
	private Context context;
	private int screenW;
	private int screenH;
	private List<String> list  = null;
	DisplayMetrics display;
	
	//图片缓存类
	private LruCache mLruCache;
	
	public MyEvaluateAdapter(Context context,ArrayList<Bitmap>bmpList,List<String> list){
		this.context=context;
		this.arrayList=bmpList;
		this.list = list;
		screenW = context.getResources().getDisplayMetrics().widthPixels/4;
		screenH = context.getResources().getDisplayMetrics().heightPixels/4;
		
		
		
		//应用程序最大可用内存
				int maxMemory = (int) Runtime.getRuntime().maxMemory();
				//设置图片缓存大小为maxMemory的1/3
				int cacheSize = maxMemory/3;
				
				mLruCache = new LruCache(cacheSize) {
					protected int sizeOf(String key, Bitmap bitmap) {
						return bitmap.getRowBytes() * bitmap.getHeight();
					}
				};
	}
	@Override
	public int getCount() {
		return arrayList.size();
	}
	//设置设配器数据
	public void setDate(ArrayList<Bitmap>bmpList){
		this.arrayList=bmpList;
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.pingjia_item, null);
			viewHolder=new ViewHolder();
			viewHolder.imageView=(ImageView) convertView.findViewById(R.id.iv);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
			try {
				viewHolder.imageView.setScaleType(ScaleType.FIT_XY);
				viewHolder.imageView.getWidth();
				viewHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(90, 90));
				//绑定图片原始尺寸，方便以后应用
			int [] parameter={arrayList.get(position).getWidth(),arrayList.get(position).getHeight()};
			viewHolder.imageView.setTag(parameter);
			viewHolder.imageView.setImageBitmap(arrayList.get(position));
//				DisplayImageOptions   options = new DisplayImageOptions.Builder()    
//				.showStubImage(R.drawable.add_dog_phone)          // 设置图片下载期间显示的图片    
//				.showImageForEmptyUri(R.drawable.add_dog_phone)  // 设置图片Uri为空或是错误的时候显示的图片    
//				.showImageOnFail(R.drawable.add_dog_phone)       // 设置图片加载或解码过程中发生错误显示的图片        
//				.cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中    
//				.cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中    
//				.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片    
//				.build();                                   // 创建配置过得DisplayImageOption对象   
//				ImageLoaderUtil.getInstance(context).dispaly(list.get(position), viewHolder.imageView);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
	}
	class ViewHolder{
		ImageView imageView;
	}
	
	
	
	
	/**
	 * 为ImageView设置图片(Image)
	 * 1 从缓存中获取图片
	 * 2 若图片不在缓存中则为其设置默认图片
	 */
	private void setImageForImageView(String imagePath, ImageView imageView) {
		Bitmap bitmap = getBitmapFromLruCache(imagePath);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.dog_icon);
		}
	}
	
	
	/**
	 * 将图片存储到LruCache
	 */
	public void addBitmapToLruCache(String key, Bitmap bitmap) {
		if (getBitmapFromLruCache(key) == null) {
			mLruCache.put(key, bitmap);
		}
	}
	
	/**
	 * 从LruCache缓存获取图片
	 */
	public Bitmap getBitmapFromLruCache(String key) {
		return (Bitmap) mLruCache.get(key);
	}
}
