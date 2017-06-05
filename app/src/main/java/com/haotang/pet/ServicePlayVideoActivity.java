package com.haotang.pet;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.jc.videoplayer.lib.JCVideoPlayer;

public class ServicePlayVideoActivity extends SuperActivity{

	private JCVideoPlayer videoplayer_service_video;
	private String videourl;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private String videos [];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_service_play_video);
		
		initView();
		getData();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void getData() {
		videourl = getIntent().getStringExtra("videourl");
		if (videourl.contains("video")) {
			videos = videourl.split("\\|\\|");
		}
	}

	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		videoplayer_service_video = (JCVideoPlayer) findViewById(R.id.videoplayer_service_video);
		
		
		tv_titlebar_title.setVisibility(View.GONE);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		videoplayer_service_video.setUp(videos[2], "");
		ImageLoaderUtil.setImage(videoplayer_service_video.ivThumb, videos[1],0);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			JCVideoPlayer.releaseAllVideos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
