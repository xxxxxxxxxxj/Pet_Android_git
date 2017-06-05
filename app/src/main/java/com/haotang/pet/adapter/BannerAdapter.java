package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.ADActivity;
import com.haotang.pet.entity.Banner;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.rollviewpager.adapter.DynamicPagerAdapter;

public class BannerAdapter extends DynamicPagerAdapter {
	private ArrayList<Banner> list;
	private int type;// 点击后跳转的类型
	protected String[] imgUrls;

	public BannerAdapter(int type, ArrayList<Banner> list) {
		super();
		this.list = list;
		this.type = type;
	}

	@Override
	public View getView(ViewGroup container, final int position) {
		imgUrls = new String[list.size()];
		if (this.list != null && this.list.size() > 0) {
			for (int i = 0; i < this.list.size(); i++) {
				Banner banner = this.list.get(i);
				if (banner != null) {
					if (banner.imgurl != null && !TextUtils.isEmpty(banner.imgurl)) {
						imgUrls[i] = banner.imgurl;
					}
				}
			}
		}
		final ImageView view = new ImageView(container.getContext());
		view.setScaleType(ImageView.ScaleType.FIT_XY);
		view.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		ImageLoaderUtil.loadImg(list.get(position).imgurl, view, 0, null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (list.get(position).url != null
						&& !TextUtils.isEmpty(list.get(position).url)) {
					if (type == 2) {
						goAd(view.getContext(), list.get(position).url,
								ADActivity.class, Global.MAINBANNER_TO_AD);
					} else {
						goAd(view.getContext(), list.get(position).url,
								ADActivity.class, 0);
					}
				} else {
					Utils.imageBrower(view.getContext(), position, imgUrls);
				}
			}
		});
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	private void goMember(Context context) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		intent.putExtra("previous",
				Global.PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT);
		context.sendBroadcast(intent);
	}

	private void goAd(Context context, String url, Class cls, int pre) {
		Intent intent = new Intent(context, cls);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		((Activity) context).getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("url", url);
		intent.putExtra("previous", pre);
		context.startActivity(intent);
	}

}
