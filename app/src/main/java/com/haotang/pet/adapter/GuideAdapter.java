package com.haotang.pet.adapter;

import java.util.ArrayList;

import com.haotang.pet.ADActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuideAdapter extends PagerAdapter {
	private Context context;
	private ArrayList<ImageView> imageViews;
	
	public GuideAdapter(Context context, ArrayList<ImageView> imageViews) {
		super();
		this.context = context;
		this.imageViews = imageViews;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		return Integer.MAX_VALUE;
		return imageViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(imageViews.get(position%imageViews.size()));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(imageViews.get(position%imageViews.size()));
		
		return imageViews.get(position%imageViews.size());
	}
	
	

}
