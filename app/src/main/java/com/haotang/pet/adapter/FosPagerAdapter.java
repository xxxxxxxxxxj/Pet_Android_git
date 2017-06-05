package com.haotang.pet.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.view.rollviewpager.adapter.DynamicPagerAdapter;
import com.loveplusplus.demo.image.ImagePagerActivity;

/**
 * <p>
 * Title:FosPagerAdapter
 * </p>
 * <p>
 * Description:寄养详情页顶部banner适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-11-15 上午10:34:04
 */
public class FosPagerAdapter extends DynamicPagerAdapter {
	private List<String> list;
	private Context context;

	public FosPagerAdapter(Context context, List<String> list) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public View getView(ViewGroup container, final int position) {
		final ImageView view = new ImageView(container.getContext());
		view.setScaleType(ImageView.ScaleType.FIT_XY);
		view.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		String imgUrl = list.get(position);
		if (imgUrl != null && !TextUtils.isEmpty(imgUrl)) {
			ImageLoaderUtil.loadImg(imgUrl, view,
					R.drawable.icon_production_default, null);
		}
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imageBrower(position, list.toArray(new String[list.size()]));
			}
		});
		return view;
	}

	private void imageBrower(int position, String[] urls) {
		for (int i = 0; i < urls.length; i++) {
			if (!urls[i].startsWith("http://")
					&& !urls[i].startsWith("https://")) {
				urls[i] = CommUtil.getServiceNobacklash() + urls[i];
			}
		}
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		context.startActivity(intent);
	}

	@Override
	public int getCount() {
		return list.size();
	}
}
