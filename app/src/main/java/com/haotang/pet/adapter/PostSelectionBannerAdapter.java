package com.haotang.pet.adapter;

import java.util.ArrayList;
import java.util.List;

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
import com.haotang.pet.entity.PostSelectionBean.BannersBean;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.rollviewpager.adapter.DynamicPagerAdapter;

/**
 * <p>
 * Title:PostSelectionBannerAdapter
 * </p>
 * <p>
 * Description:精选列表顶部banner适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-13 下午4:41:52
 */
public class PostSelectionBannerAdapter extends DynamicPagerAdapter {
	private List<BannersBean> list = new ArrayList<BannersBean>();
	private String[] imgUrls = new String[1];

	public PostSelectionBannerAdapter() {
		super();
	}

	@Override
	public View getView(ViewGroup container, final int position) {
		final ImageView view = new ImageView(container.getContext());
		view.setScaleType(ImageView.ScaleType.FIT_XY);
		view.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		BannersBean bannersBean = list.get(position);
		if (bannersBean != null) {
			final String imgUrl = bannersBean.getImgUrl();
			final String imgLink = bannersBean.getImgLink();
			ImageLoaderUtil.loadImg(imgUrl, view,
					R.drawable.icon_production_default, null);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (imgLink != null && !TextUtils.isEmpty(imgLink)) {
						goAd(view.getContext(), imgLink, ADActivity.class, 0);
					} else {
						imgUrls[0] = imgUrl;
						Utils.imageBrower(view.getContext(), position, imgUrls);
					}
				}
			});
		}
		return view;
	}

	@Override
	public int getCount() {
		return list.size();
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

	public void setData(List<BannersBean> list) {
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}
}
