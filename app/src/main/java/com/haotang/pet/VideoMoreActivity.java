package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.TrainVideoAdaper;
import com.haotang.pet.entity.TranVideo;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.jc.videoplayer.lib.JCVideoPlayer;

public class VideoMoreActivity extends SuperActivity{
	
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private PullToRefreshListView listView_video;
	private TrainVideoAdaper trainVideoAdaper;
	private List<TranVideo> videoList = new ArrayList<TranVideo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_more);
		initView();
		setView();
	}
	private void setView() {
		videoList.clear();
		
		
		listView_video.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
					// 下拉刷新
					getData();
				} else {
					getData();
				}
			}
		});
		
		
		
		getData();
	}
	private void getData() {
		mPDialog.showDialog();
		videoList.clear();
		try {
			JCVideoPlayer.releaseAllVideos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommUtil.getTrainVideos(mContext,spUtil.getString("cellphone", ""), handler);
	}
	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		tv_titlebar_title.setText("视频介绍");
		listView_video = (PullToRefreshListView) findViewById(R.id.listView_video);
		
		
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			JCVideoPlayer.releaseAllVideos();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			Utils.mLogError("==-->训练视频列表：="+new String(responseBody));
			mPDialog.closeDialog();
			try {
				listView_video.onRefreshComplete();
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONArray array = object.getJSONArray("data");
						if (array.length()>0) {
							for (int i = 0; i < array.length(); i++) {
								TranVideo tranVideo = new TranVideo();
								JSONObject objectEvery = array.getJSONObject(i);
								if (objectEvery.has("thumbnail")&&!objectEvery.isNull("thumbnail")) {
									tranVideo.thumbnail = objectEvery.getString("thumbnail");
								}
								if (objectEvery.has("title")&&!objectEvery.isNull("title")) {
									tranVideo.title = objectEvery.getString("title");
								}
								if (objectEvery.has("videoUrl")&&!objectEvery.isNull("videoUrl")) {
									tranVideo.videoUrl = objectEvery.getString("videoUrl");
								}
								videoList.add(tranVideo);
							}
						}
						trainVideoAdaper = new TrainVideoAdaper<TranVideo>(mContext, videoList);
						listView_video.setAdapter(trainVideoAdaper);
						if (videoList.size()>0) {
							try {
								trainVideoAdaper.setSourse(1, 10);
								trainVideoAdaper.notifyDataSetChanged();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					mPDialog.closeDialog();
					listView_video.onRefreshComplete();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			try {
				mPDialog.closeDialog();
				listView_video.onRefreshComplete();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	};
}
