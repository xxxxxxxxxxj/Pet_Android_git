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
import com.haotang.pet.R;
import com.haotang.pet.entity.Banner;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.rollviewpager.RollPagerView;
import com.haotang.pet.view.rollviewpager.adapter.LoopPagerAdapter;

public class BannerLoopAdapter extends LoopPagerAdapter {
	private ArrayList<Banner> list;
	private String[] imgUrls = new String[1];

	public BannerLoopAdapter(RollPagerView viewPager, ArrayList<Banner> list) {
		super(viewPager);
		this.list = list;
	}

	@Override
	public View getView(ViewGroup container, final int position) {
		final ImageView view = new ImageView(container.getContext());
		view.setScaleType(ImageView.ScaleType.FIT_XY);
		view.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		ImageLoaderUtil.loadImg(list.get(position).imgurl, view,
				R.drawable.icon_production_default, null);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (list.get(position).url != null
						&& !TextUtils.isEmpty(list.get(position).url)) {
					goAd(view.getContext(), list.get(position).url,
							ADActivity.class);
				} else {
					imgUrls[0] = list.get(position).imgurl;
					Utils.imageBrower(view.getContext(), position, imgUrls);
				}
			}
		});
		return view;
	}

	private void goAd(Context context, String url, Class cls) {
		Intent intent = new Intent(context, cls);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		((Activity) context).getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("url", url);
		context.startActivity(intent);
	}

	@Override
	protected int getRealCount() {
		return list.size();
	}

}
