package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.view.rollviewpager.RollPagerView;
import com.haotang.pet.view.rollviewpager.adapter.LoopPagerAdapter;
import com.jc.videoplayer.lib.JCVideoPlayer;
/**
 * 顶部视频+图片轮播 暂时留下后版本估计会调整
 * @author Administrator
 *
 */
public class BannerVideoLoopAdapter extends LoopPagerAdapter{
	private ArrayList<String> list;
	public BannerVideoLoopAdapter(RollPagerView viewPager,ArrayList<String> list) {
		super(viewPager);
		this.list = list;
	}

	@Override
	public View getView(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_looper_banner_video,null);
		ImageView imageView_view = (ImageView) view.findViewById(R.id.imageView_view);
		JCVideoPlayer videoplayer_train = (JCVideoPlayer) view.findViewById(R.id.videoplayer_train);
		ImageLoaderUtil.setImage(imageView_view, "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640", 0);
		if (list.get(position).contains(".jpg")) {
			videoplayer_train.setVisibility(View.GONE);
			imageView_view.setVisibility(View.VISIBLE);
		}else {
			videoplayer_train.setVisibility(View.VISIBLE);
			imageView_view.setVisibility(View.GONE);
			videoplayer_train.setUp(list.get(position), "123");
		}
		return view;
	}

	@Override
	protected int getRealCount() {
		// TODO Auto-generated method stub
		if (list.size()>0) {
			return list.size();
		}else {
			return 0;
		}
	}

}
