package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.haotang.pet.R;
import com.haotang.pet.entity.TranVideo;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;
import com.jc.videoplayer.lib.JCVideoPlayer;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class TrainVideoAdaper<T> extends CommonAdapter<T>{

	private int type=0;
	private LinearLayout.LayoutParams lp1;
	public TrainVideoAdaper(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics(mContext)[0]*9/16);
		
	}

	public void setSourse(int type,int margin){
		this.type = type;
		lp1.leftMargin=Utils.dip2px(mContext, margin);
		lp1.rightMargin=Utils.dip2px(mContext, margin);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TranVideo tranVideo = (TranVideo) mDatas.get(position);
		ViewHolder  viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_list, position);
		LinearLayout layout = viewHolder.getView(R.id.layout_video);
		JCVideoPlayer jcVideoPlayer = viewHolder.getView(R.id.videoplayer);
		layout.setLayoutParams(lp1);
		if (type==1) {
			viewHolder.setText(R.id.video_title, tranVideo.title);
			viewHolder.getView(R.id.view_show_white).setVisibility(View.VISIBLE);
		}else {
			viewHolder.getView(R.id.video_title).setVisibility(View.GONE);
			viewHolder.getView(R.id.view_show_white).setVisibility(View.GONE);
		}
		jcVideoPlayer.setUp(tranVideo.videoUrl,tranVideo.title,false,0);
		jcVideoPlayer.ivThumb.setScaleType(ScaleType.FIT_XY);
		ImageLoaderUtil.setImage(jcVideoPlayer.ivThumb,tranVideo.thumbnail,R.drawable.icon_production_default_video);
		return viewHolder.getConvertView();
	}

}
