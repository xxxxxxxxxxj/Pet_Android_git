package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.ADActivity;
import com.haotang.pet.BeauticianCommonPicActivity;

public class MPagerAdapter extends PagerAdapter {
	private Context context;
	private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
	private String[] urls = new String[0];
	private int flag;

	public MPagerAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return Integer.MAX_VALUE;
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
		container.removeView(imageViews.get(position % imageViews.size()));
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		container.addView(imageViews.get(position % imageViews.size()));

		final int result = (position) % imageViews.size();

		ImageView iv = imageViews.get(result);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					if (urls != null && urls.length > position) {
						if (flag == 0)
							goAd(result);
						else
							goNext(position, urls);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		return imageViews.get(position % imageViews.size());
	}

	private void goAd(int index) {
		Intent intent = new Intent(context, ADActivity.class);
		intent.putExtra("url", urls[index]);
		context.startActivity(intent);
	}

	private void goNext(int index, String[] pics) {
		Intent intent = new Intent(context, BeauticianCommonPicActivity.class);
		intent.putExtra("index", index);
		intent.putExtra("pics", pics);
		context.startActivity(intent);
	}

	public void setData(ArrayList<ImageView> imageViews, String[] urls, int flag) {
		this.imageViews.clear();
		this.imageViews = imageViews;
		this.urls = urls;
		this.flag = flag;
		notifyDataSetChanged();
	}
}
