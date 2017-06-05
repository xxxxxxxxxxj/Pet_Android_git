package com.haotang.pet.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haotang.base.BaseFragment;
import com.haotang.pet.ClipPictureActivity;
import com.haotang.pet.LoginActivity;
import com.haotang.pet.MediaRecorderActivity;
import com.haotang.pet.PostSelectionDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.SendSelectPostActivity;
import com.haotang.pet.adapter.PostSelectionBannerAdapter;
import com.haotang.pet.adapter.PostSelectionfragmentAdapter;
import com.haotang.pet.adapter.PostSelectionfragmentAdapter.OnGoToLoginListener;
import com.haotang.pet.crop.Crop;
import com.haotang.pet.entity.PostSelectionBean;
import com.haotang.pet.entity.PostSelectionBean.BannersBean;
import com.haotang.pet.entity.PostSelectionBean.PostsBean;
import com.haotang.pet.guideview.Component;
import com.haotang.pet.guideview.Guide;
import com.haotang.pet.guideview.GuideBuilder;
import com.haotang.pet.guideview.MutiComponent;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.UnicodeToEmoji;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SurfaceVideoView;
import com.haotang.pet.view.UserNameAlertDialog;
import com.haotang.pet.view.rollviewpager.RollPagerView;
import com.haotang.pet.view.rollviewpager.hintview.ColorPointHintView;
import com.melink.bqmmsdk.sdk.BQMM;
import com.melink.bqmmsdk.ui.keyboard.IBQMMUnicodeEmojiProvider;
import com.yixia.weibo.sdk.util.DeviceUtils;

/**
 * <p>
 * Title:PostSelectionFragment
 * </p>
 * <p>
 * Description:精选列表界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-9-27 下午3:52:37
 */
@SuppressLint("NewApi")
public class PostSelectionFragment extends BaseFragment implements
		SurfaceVideoView.OnPlayStateListener, OnErrorListener,
		OnPreparedListener, OnClickListener, OnCompletionListener,
		OnInfoListener {
	private Activity context;
	private ImageView iv_postselectionfragment_post;
	private PullToRefreshListView prl_postselectionfragment;
	private View header;
	private RollPagerView rpv_header_postselection;
	private LinearLayout ll_header_postselection_sx;
	private Button btn_header_postselection_all;
	private Button btn_header_postselection_gz;
	private Button btn_header_postselection_video;
	private List<PostsBean> postList = new ArrayList<PostsBean>();
	private PostSelectionfragmentAdapter<PostsBean> postSelectionfragmentAdapter;
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;
	private int page = 1;// 页码
	private int isFollowed;// 只显示自己关注的人的帖子
	private int isExistsVideo;// 只显示存在视频的帖子
	private int isExistsImg;// 只显示存在图片的帖子
	private PostSelectionBannerAdapter postSelectionBannerAdapter;
	private List<BannersBean> bannerList = new ArrayList<PostSelectionBean.BannersBean>();
	private int screenWidth;
	private int topBannerWidth;
	private LayoutParams lp;
	private PopupWindow pWin;
	/**
	 * 标识从相机的返回
	 */
	private static final int REQUEST_CODE_CAPTURE_CAMEIA = 3;
	private static final int PET_CROP = 1024;
	protected static final int CACHE_SUCCESS = 111;
	private String shareUrl;
	private int positionnum;
	private int adapterFlag;
	/** 播放控件 */
	private SurfaceVideoView mVideoView;
	/** 暂停按钮 */
	private ImageView mPlayerStatus;
	private ImageView mLoading;
	protected ImageView iv_item_postselectionfragment_img;
	public int firstVisible = 0, visibleCount = 0, totalCount = 0;
	private BQMM bqmmsdk;
	private Guide guide;
	private String localVideoPath;
	private ImageView iv_postuser_default;
	private TextView tv_postuser_default;
	protected boolean localVoice;
	protected long readSize;
	protected int mediaLength;
	private int PostSelectionfragmentAdapter_position;
	private File PetTemp;
	private File PetCrop;
	private List<PostsBean> tempList = new ArrayList<PostSelectionBean.PostsBean>();
	private int index;
	private long timestamp;
	private int postId;
	private String localUrl;
	private AudioManager mAudioManager;
	private CheckBox cb_item_postselectionfragment_voice;
	private int localPage = 1;
	private int pageSize;
	private int position = -1;
	private int tempPosition = -1;
	private int positionFlag;

	public PostSelectionFragment() {
		super();
	}

	public PostSelectionFragment(Activity context) {
		this.context = context;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
		spUtil = SharedPreferenceUtil.getInstance(context);
		pDialog = new MProgressDialog(context);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CACHE_SUCCESS:
				if (tempPosition != -1 && position != -1) {
					if (tempPosition == position) {
						PostsBean postsBean = postList.get(position);
						if (postsBean != null) {
							List<String> videos = postsBean.getVideos();
							if (videos != null && videos.size() > 0) {
								String videoPath = videos.get(0);
								if (videoPath != null
										&& !TextUtils.isEmpty(videoPath)) {
									String substring = videoPath
											.substring(videoPath
													.lastIndexOf("/") + 1);
									localUrl = Environment
											.getExternalStorageDirectory()
											.getAbsolutePath()
											+ "/PetCircleVideoCache/"
											+ substring;
									if (new File(localUrl).exists()) {// 文件存在，读取缓存中的文件播放
										localVideoPath = localUrl;
										initPlayer(1);// 播放
									} else {// 文件不存在，缓存文件
									}
								}
							}
						}
					} else {
						initPlayer(0);// 不播放
					}
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onPrepared(MediaPlayer mp) {
		if (mVideoView != null) {
			int vol = (int) (cb_item_postselectionfragment_voice.isChecked() ? mAudioManager
					.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) : 0);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0); // 设置音量
			mVideoView.start();
		}
		mLoading.setVisibility(View.GONE);
	}

	@Override
	public void onStateChanged(boolean isPlaying) {
		mPlayerStatus.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// 播放失败重试机制
		mHandler.removeMessages(CACHE_SUCCESS);
		mHandler.sendEmptyMessage(CACHE_SUCCESS);
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mPlayerStatus.setVisibility(View.VISIBLE);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
			// 音频和视频数据不正确
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (!context.isFinishing())
				mVideoView.pause();
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			if (!context.isFinishing())
				mVideoView.start();
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
			if (DeviceUtils.hasJellyBean()) {
				mVideoView.setBackground(null);
			} else {
				mVideoView.setBackgroundDrawable(null);
			}
			break;
		}
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mVideoView != null) {
			mVideoView.release();
			mVideoView = null;
		}
		try {
			PetCircleOrSelectFragment.myAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initGuide() {
		boolean TAG_MUTICOMPONENT = spUtil.getBoolean("TAG_MUTICOMPONENT",
				false);
		if (!TAG_MUTICOMPONENT) {
			getActivity()
					.getWindow()
					.getDecorView()
					.getViewTreeObserver()
					.addOnGlobalLayoutListener(
							new ViewTreeObserver.OnGlobalLayoutListener() {
								@Override
								public void onGlobalLayout() {
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
										getActivity()
												.getWindow()
												.getDecorView()
												.getViewTreeObserver()
												.removeOnGlobalLayoutListener(
														this);
									} else {
										getActivity()
												.getWindow()
												.getDecorView()
												.getViewTreeObserver()
												.removeGlobalOnLayoutListener(
														this);
									}
									showGuideView();
								}
							});
		}
	}

	public void showGuideView() {
		GuideBuilder builder = new GuideBuilder();
		builder.setTargetView(iv_postselectionfragment_post).setAlpha(150)
				.setHighTargetGraphStyle(Component.CIRCLE)
				.setOverlayTarget(false).setOutsideTouchable(false)
				.setAutoDismiss(false)
				.setExitAnimationId(android.R.anim.fade_out);
		builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
			@Override
			public void onShown() {
			}

			@Override
			public void onDismiss() {
				spUtil.saveBoolean("TAG_MUTICOMPONENT", true);
			}
		});
		builder.addComponent(new MutiComponent(clickListener));
		guide = builder.createGuide();
		guide.setShouldCheckLocInWindow(true);
		guide.show(getActivity());
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView1:
				guide.dismiss();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		positionFlag = 1;
		try {
			PetCircleOrSelectFragment.myAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * BQMM集成 加载SDK
		 */
		bqmmsdk = BQMM.getInstance();
		UnicodeToEmoji.initPhotos(context);
		bqmmsdk.setUnicodeEmojiProvider(new IBQMMUnicodeEmojiProvider() {
			@Override
			public Drawable getDrawableFromCodePoint(int i) {
				return UnicodeToEmoji.EmojiImageSpan.getEmojiDrawable(i);
			}
		});
		mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		spUtil = SharedPreferenceUtil.getInstance(context);
		pDialog = new MProgressDialog(context);
		View view = inflater.inflate(R.layout.postselectionfragment_layout,
				container, false);
		iv_postselectionfragment_post = (ImageView) view
				.findViewById(R.id.iv_postselectionfragment_post);
		prl_postselectionfragment = (PullToRefreshListView) view
				.findViewById(R.id.prl_postselectionfragment);
		header = LayoutInflater.from(context).inflate(
				R.layout.header_postselection_fragment, null);
		rpv_header_postselection = (RollPagerView) header
				.findViewById(R.id.rpv_header_postselection);
		iv_postuser_default = (ImageView) header
				.findViewById(R.id.iv_postuser_default);
		tv_postuser_default = (TextView) header
				.findViewById(R.id.tv_postuser_default);
		screenWidth = Utils.getDisplayMetrics(context)[0];
		topBannerWidth = screenWidth * 2 / 5;
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, topBannerWidth);
		rpv_header_postselection.setLayoutParams(lp);
		ll_header_postselection_sx = (LinearLayout) header
				.findViewById(R.id.ll_header_postselection_sx);
		btn_header_postselection_all = (Button) header
				.findViewById(R.id.btn_header_postselection_all);
		btn_header_postselection_gz = (Button) header
				.findViewById(R.id.btn_header_postselection_gz);
		btn_header_postselection_video = (Button) header
				.findViewById(R.id.btn_header_postselection_video);
		rpv_header_postselection
				.setLayoutParams(new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT, DeviceUtils
								.getScreenWidth(context) * 30 / 75));
		prl_postselectionfragment.getRefreshableView().addHeaderView(header);
		prl_postselectionfragment.setMode(Mode.BOTH);
		prl_postselectionfragment
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
					@Override
					public void onRefresh(PullToRefreshBase refreshView) {
						PullToRefreshBase.Mode mode = refreshView
								.getCurrentMode();
						if (mode == Mode.PULL_FROM_START) {// 下拉刷新
							page = 1;
							getData();
						} else {// 上拉加载更多
							getData();
						}
					}
				});
		postSelectionBannerAdapter = new PostSelectionBannerAdapter();
		rpv_header_postselection.setAdapter(postSelectionBannerAdapter);
		postSelectionfragmentAdapter = new PostSelectionfragmentAdapter<PostsBean>(
				context, postList,
				prl_postselectionfragment.getRefreshableView(), mAudioManager);
		postSelectionfragmentAdapter
				.setOnGoToLoginListener(new OnGoToLoginListener() {
					@Override
					public void OnGoToLogin(int flag, int position,
							SurfaceVideoView view1, ImageView view2,
							ImageView view3, ImageView view4, CheckBox view5) {
						adapterFlag = flag;
						positionnum = position;
						if (flag == postSelectionfragmentAdapter.NORMAL) {
							startActivity(new Intent(context,
									LoginActivity.class).putExtra("previous",
									Global.LOGIN_TO_POSTSELECTIONFRAGMENT));
						} else if (flag == postSelectionfragmentAdapter.PLAYVIDEO) {
							initVideoPlayer(view1, view2, view3, view4, view5,
									position, 2);
						} else if (flag == postSelectionfragmentAdapter.REFRESH) {
							page = localPage;
							getData();
						} else if (flag == postSelectionfragmentAdapter.REFRESHDELETEPOST) {
							if (postList != null && postList.size() > 0) {
								if (postList.size() > position) {
									tempList.remove(position);
									postList.remove(position);
									if (postList.size() <= 0) {
										page = 1;
										getData();
									} else {
										if ((postList.size() % pageSize) != 0) {
											page = localPage;
										}
										postSelectionfragmentAdapter
												.setData(postList);
									}
								}
							}
						}
					}
				});
		prl_postselectionfragment.setAdapter(postSelectionfragmentAdapter);
		prl_postselectionfragment.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 空闲状态
					if (Utils.getNetworkType(context) == 1) {
						autoPlayVideo();
					}
					break;
				case OnScrollListener.SCROLL_STATE_FLING:// 滚动状态
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸后滚动
					break;
				}
			}

			/**
			 * 正在滚动 firstVisibleItem第一个Item的位置 visibleItemCount 可见的Item的数量
			 * totalItemCount item的总数
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// firstVisibleItem 当前第一个可见的item
				// visibleItemCount 当前可见的item个数
				if (firstVisible == firstVisibleItem) {
					return;
				}
				firstVisible = firstVisibleItem;
				visibleCount = visibleItemCount;
				totalCount = totalItemCount;
			}
		});
		ll_header_postselection_sx.setOnClickListener(this);
		btn_header_postselection_all.setOnClickListener(this);
		btn_header_postselection_gz.setOnClickListener(this);
		btn_header_postselection_video.setOnClickListener(this);
		iv_postselectionfragment_post.setOnClickListener(this);
		iv_postselectionfragment_post.bringToFront();
		isFollowed = 0;
		isExistsVideo = 0;
		isExistsImg = 0;
		page = 1;
		getData();
		PetTemp = new File(Environment.getExternalStorageDirectory(), "PetTemp");
		if (!PetTemp.exists()) {
			PetTemp.mkdirs();
		}
		PetCrop = new File(Environment.getExternalStorageDirectory(), "PetCrop");
		if (!PetCrop.exists()) {
			PetCrop.mkdirs();
		}
		return view;
	}

	private void initVideoPlayer(SurfaceVideoView view1, ImageView view2,
			ImageView view3, ImageView view4, CheckBox view5, int position,
			int videoflag) {
		PostsBean postsBean = postList.get(position);
		if (postsBean != null) {
			boolean voice = postsBean.isVoice();
			postId = postsBean.getId();
			localVoice = voice;
			List<String> videos = postsBean.getVideos();
			if (videos != null && videos.size() > 0) {
				String videoPath = videos.get(0);
				if (videoPath != null && !TextUtils.isEmpty(videoPath)) {
					String substring = videoPath.substring(videoPath
							.lastIndexOf("/") + 1);
					localUrl = Environment.getExternalStorageDirectory()
							.getAbsolutePath()
							+ "/PetCircleVideoCache/"
							+ substring;
					int localflag = 0;
					if (new File(localUrl).exists()) {// 文件存在，读取缓存中的文件播放
						localflag = 1;
						localVideoPath = localUrl;
					} else {// 文件不存在，缓存文件
						localflag = 2;
						localVideoPath = videoPath;
						if (Utils.getNetworkType(context) == 1
								|| videoflag == 2) {// WIFI网络或者用户点击
							cacheFile();
						}
					}
					if (mVideoView != null) {
						if (mVideoView.isPlaying()) {
							mVideoView.pause();
						}
					}
					if (mVideoView != null) {
						mVideoView.release();
					}
					mVideoView = view1;
					mPlayerStatus = view2;
					mLoading = view3;
					iv_item_postselectionfragment_img = view4;
					cb_item_postselectionfragment_voice = view5;
					mVideoView
							.setOnPreparedListener(PostSelectionFragment.this);
					mVideoView
							.setOnPlayStateListener(PostSelectionFragment.this);
					mVideoView.setOnErrorListener(PostSelectionFragment.this);
					mVideoView.setOnClickListener(PostSelectionFragment.this);
					mVideoView.setOnInfoListener(PostSelectionFragment.this);
					mVideoView
							.setOnCompletionListener(PostSelectionFragment.this);
					if (localflag == 1) {
						initPlayer(1);// 播放
					} else if (localflag == 2) {
						if (Utils.getNetworkType(context) == 1
								|| videoflag == 2) {// WIFI网络或者用户点击
							initPlayer(2);// 加载
						}
					}
				}
			}
		}
	}

	protected void cacheFile() {
		if (localVideoPath != null && !TextUtils.isEmpty(localVideoPath)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					FileOutputStream out = null;
					InputStream is = null;
					try {
						tempPosition = position;
						URL url = new URL(localVideoPath);
						HttpURLConnection httpConnection = (HttpURLConnection) url
								.openConnection();
						String substring = localVideoPath
								.substring(localVideoPath.lastIndexOf("/") + 1);
						String localUrl = Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/PetCircleVideoCache/" + substring;
						File cacheFile = new File(localUrl);
						if (!cacheFile.exists()) {
							cacheFile.getParentFile().mkdirs();
							cacheFile.createNewFile();
						}
						readSize = cacheFile.length();
						out = new FileOutputStream(cacheFile, true);
						httpConnection.setRequestProperty("User-Agent",
								"NetFox");
						httpConnection.setRequestProperty("RANGE", "bytes="
								+ readSize + "-");
						is = httpConnection.getInputStream();
						mediaLength = httpConnection.getContentLength();
						if (mediaLength == -1) {
							return;
						}
						mediaLength += readSize;
						byte buf[] = new byte[4 * 1024];
						int size = 0;
						while ((size = is.read(buf)) != -1) {
							try {
								out.write(buf, 0, size);
								readSize += size;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						localVideoPath = localUrl;
						mHandler.removeMessages(CACHE_SUCCESS);
						mHandler.sendEmptyMessage(CACHE_SUCCESS);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (out != null) {
							try {
								out.close();
							} catch (IOException e) {
							}
						}
						if (is != null) {
							try {
								is.close();
							} catch (IOException e) {
							}
						}
					}
				}
			}).start();
		}
	}

	private void autoPlayVideo() {
		ListView refreshableView = prl_postselectionfragment
				.getRefreshableView();
		for (int i = 0; i < visibleCount; i++) {
			if (refreshableView != null) {
				View childAt = refreshableView.getChildAt(i);
				if (childAt != null) {
					SurfaceVideoView view1 = (SurfaceVideoView) childAt
							.findViewById(R.id.sfvv_item_postselectionfragment);
					ImageView view2 = (ImageView) childAt
							.findViewById(R.id.iv_item_postselectionfragment_play_status);
					ImageView view3 = (ImageView) childAt
							.findViewById(R.id.iv_item_postselectionfragment_videoloading);
					ImageView view4 = (ImageView) childAt
							.findViewById(R.id.iv_item_postselectionfragment_img);
					CheckBox view5 = (CheckBox) childAt
							.findViewById(R.id.cb_item_postselectionfragment_voice);
					if (view1 != null && view2 != null && view3 != null
							&& view4 != null && view5 != null
							&& view2.getVisibility() == View.VISIBLE
							&& view5.getVisibility() == View.VISIBLE) {
						Rect rect = new Rect();
						view1.getLocalVisibleRect(rect);
						int videoheight3 = view1.getHeight();
						if (rect.top == 0 && rect.bottom == videoheight3) {// 播放
							if (postList != null && postList.size() > 0) {
								position = (Integer) view1.getTag();
								if (mVideoView != null) {
									if (mVideoView.isPlaying()) {
										mVideoView.pause();
									}
								}
								if (mVideoView != null) {
									mVideoView.release();
								}
								localVideoPath = "";
								initVideoPlayer(view1, view2, view3, view4,
										view5, position, 1);
							}
							return;
						}
					}
				}
			}
		}
	}

	private void initPlayer(int type) {
		if (type == 1) {// 播放
			iv_item_postselectionfragment_img.setVisibility(View.GONE);
			mVideoView.setVisibility(View.VISIBLE);
			mPlayerStatus.setVisibility(View.GONE);
			mLoading.setVisibility(View.GONE);
			mVideoView.setVideoPath(localVideoPath);
			mVideoView.start();
			int vol = (int) (cb_item_postselectionfragment_voice.isChecked() ? mAudioManager
					.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) : 0);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0); // 设置音量
		} else if (type == 0) {// 不播放
			mVideoView.setVisibility(View.GONE);
			iv_item_postselectionfragment_img.setVisibility(View.VISIBLE);
			mLoading.setVisibility(View.GONE);
			mPlayerStatus.setVisibility(View.VISIBLE);
			mPlayerStatus.bringToFront();
		}
		if (type == 2) {// 加载
			mVideoView.setVisibility(View.GONE);
			iv_item_postselectionfragment_img.setVisibility(View.VISIBLE);
			mLoading.setVisibility(View.VISIBLE);
			mPlayerStatus.setVisibility(View.GONE);
			mLoading.setBackgroundResource(R.drawable.spinner_small);
			AnimationDrawable ad = (AnimationDrawable) mLoading.getBackground();
			ad.start();
			mLoading.bringToFront();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (rpv_header_postselection != null)
			rpv_header_postselection.resume();
		String string = spUtil.getString("LOGIN_TO_POSTSELECTIONFRAGMENT_FLAG",
				"");
		String string1 = spUtil.getString(
				"POSTSUCCESS_TO_POSTSELECTIONFRAGMENT_FLAG", "");
		if (string != null && !TextUtils.isEmpty(string)) {
			if (string.equals("LOGIN_TO_POSTSELECTIONFRAGMENT")) {
				page = localPage;
				getData();
			}
		}
		if (string1 != null && !TextUtils.isEmpty(string1)) {
			if (string1.equals("POSTSUCCESS_TO_POSTSELECTIONFRAGMENT")) {
				initAll();
			}
		}
		int PostSelectionfragmentAdapter_isFollow = spUtil.getInt(
				"PostSelectionfragmentAdapter_isFollow", -1);
		PostSelectionfragmentAdapter_position = spUtil.getInt(
				"PostSelectionfragmentAdapter_position", -1);
		boolean PostSelectionfragmentAdapter_flower = spUtil.getBoolean(
				"PostSelectionfragmentAdapter_flower", false);
		boolean PostSelectionfragmentAdapter_shit = spUtil.getBoolean(
				"PostSelectionfragmentAdapter_shit", false);
		boolean PostSelectionfragmentAdapter_comments = spUtil.getBoolean(
				"PostSelectionfragmentAdapter_comments", false);
		int PostSelectionfragmentAdapter_postid = spUtil.getInt(
				"PostSelectionfragmentAdapter_postid", -1);
		if (PostSelectionfragmentAdapter_position != -1) {
			if (postSelectionfragmentAdapter != null) {
				if (PostSelectionfragmentAdapter_isFollow != -1) {
					postSelectionfragmentAdapter.refreshGZ(
							PostSelectionfragmentAdapter_position,
							PostSelectionfragmentAdapter_isFollow);
					spUtil.removeData("PostSelectionfragmentAdapter_isFollow");
					spUtil.removeData("PostSelectionfragmentAdapter_position");
				}
				if (PostSelectionfragmentAdapter_flower == true) {
					postSelectionfragmentAdapter
							.refreshFlower(PostSelectionfragmentAdapter_position);
					spUtil.removeData("PostSelectionfragmentAdapter_flower");
					spUtil.removeData("PostSelectionfragmentAdapter_position");
				}
				if (PostSelectionfragmentAdapter_shit == true) {
					postSelectionfragmentAdapter
							.refreshShit(PostSelectionfragmentAdapter_position);
					spUtil.removeData("PostSelectionfragmentAdapter_shit");
					spUtil.removeData("PostSelectionfragmentAdapter_position");
				}
				if (PostSelectionfragmentAdapter_comments == true) {
					page = localPage;
					getData();
				}
				if (PostSelectionfragmentAdapter_postid != -1) {
					if (postList != null && postList.size() > 0) {
						if (postList.size() > PostSelectionfragmentAdapter_position) {
							tempList.remove(PostSelectionfragmentAdapter_position);
							postList.remove(PostSelectionfragmentAdapter_position);
							if (postList.size() <= 0) {
								page = 1;
								getData();
							} else {
								if ((postList.size() % pageSize) != 0) {
									page = localPage;
								}
								postSelectionfragmentAdapter.setData(postList);
							}
						}
					}
					spUtil.removeData("PostSelectionfragmentAdapter_postid");
					spUtil.removeData("PostSelectionfragmentAdapter_position");
				}
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mVideoView != null) {
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
			}
		}
		if (rpv_header_postselection != null)
			rpv_header_postselection.pause();
	}

	private void getData() {
		if (adapterFlag != postSelectionfragmentAdapter.REFRESH) {
			pDialog.showDialog();
		}
		localPage = page;
		CommUtil.queryGoodPosts(timestamp, spUtil.getString("cellphone", ""),
				context, page, isFollowed, isExistsVideo, isExistsImg,
				queryGoodPostsHandler);
	}

	private AsyncHttpResponseHandler queryGoodPostsHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			prl_postselectionfragment.onRefreshComplete();
			pDialog.closeDialog();
			processData(new String(responseBody));
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			prl_postselectionfragment.onRefreshComplete();
			pDialog.closeDialog();
		}
	};

	// 解析json数据
	private void processData(String result) {
		try {
			JSONObject jobj = new JSONObject(result);
			if (jobj.has("code") && !jobj.isNull("code")) {
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						Gson gson = new Gson();
						PostSelectionBean data = gson.fromJson(
								jdata.toString(), PostSelectionBean.class);
						if (data != null) {
							List<BannersBean> banners = data.getBanners();
							List<PostsBean> posts = data.getPosts();
							shareUrl = data.getShareUrl();
							pageSize = data.getPageSize();
							timestamp = data.getTimestamp();
							postSelectionfragmentAdapter.setShareUrl(shareUrl);
							// 顶部banner
							if (banners != null && banners.size() > 0) {
								rpv_header_postselection
										.setHintView(new ColorPointHintView(
												context, Color
														.parseColor("#FE8A3F"),
												Color.parseColor("#FFE2D0")));
								bannerList.clear();
								bannerList.addAll(banners);
								postSelectionBannerAdapter.setData(bannerList);
							} else {
								bannerList.clear();
								postSelectionBannerAdapter.setData(bannerList);
							}
							// 帖子列表
							if (posts != null && posts.size() > 0) {
								postList.clear();
								if (page == 1) {
									iv_postuser_default
											.setVisibility(View.GONE);
									tv_postuser_default
											.setVisibility(View.GONE);
									tempList.clear();
									tempList.addAll(posts);
									postList.addAll(tempList);
								} else {
									tempList.addAll(posts);
									if (index == page) {
										List<PostsBean> subList = tempList
												.subList(0, (page * pageSize)
														- pageSize);// 前面几条正确的数据
										subList.addAll(posts);
										postList.addAll(subList);
										tempList.clear();
										tempList.addAll(postList);
									} else {
										postList.addAll(tempList);
									}
								}
								index = page;
								String string = spUtil
										.getString(
												"POSTSUCCESS_TO_POSTSELECTIONFRAGMENT_FLAG",
												"");
								if (positionFlag == 1
										|| (string != null && !TextUtils
												.isEmpty(string))) {
									positionFlag = 0;
									spUtil.removeData("POSTSUCCESS_TO_POSTSELECTIONFRAGMENT_FLAG");
									prl_postselectionfragment
											.setAdapter(postSelectionfragmentAdapter);
									postSelectionfragmentAdapter
											.setData(postList);
								} else {
									postSelectionfragmentAdapter
											.setData(postList);
								}
								if (adapterFlag == postSelectionfragmentAdapter.REFRESH
										|| adapterFlag == postSelectionfragmentAdapter.NORMAL) {
									prl_postselectionfragment
											.getRefreshableView().setSelection(
													positionnum);
									postSelectionfragmentAdapter
											.notifyDataSetChanged();
								}
								String string1 = spUtil.getString(
										"LOGIN_TO_POSTSELECTIONFRAGMENT_FLAG",
										"");
								if (string1 != null
										&& !TextUtils.isEmpty(string1)) {
									if (string1
											.equals("LOGIN_TO_POSTSELECTIONFRAGMENT")) {
										spUtil.removeData("LOGIN_TO_POSTSELECTIONFRAGMENT_FLAG");
									}
								}
								boolean PostSelectionfragmentAdapter_comments = spUtil
										.getBoolean(
												"PostSelectionfragmentAdapter_comments",
												false);
								if (PostSelectionfragmentAdapter_comments == true) {
									prl_postselectionfragment
											.getRefreshableView()
											.setSelection(
													PostSelectionfragmentAdapter_position);
									postSelectionfragmentAdapter
											.notifyDataSetChanged();
									spUtil.removeData("PostSelectionfragmentAdapter_position");
									spUtil.removeData("PostSelectionfragmentAdapter_comments");
								}
								int PostSelectionfragmentAdapter_postid = spUtil
										.getInt("PostSelectionfragmentAdapter_postid",
												-1);
								if (PostSelectionfragmentAdapter_postid != -1) {
									prl_postselectionfragment
											.getRefreshableView()
											.setSelection(
													PostSelectionfragmentAdapter_position);
									postSelectionfragmentAdapter
											.notifyDataSetChanged();
									spUtil.removeData("PostSelectionfragmentAdapter_position");
									spUtil.removeData("PostSelectionfragmentAdapter_postid");
								}
								if (posts.size() >= pageSize) {
									page = ++page;
								}
							} else {
								if (page == 1) {
									postList.clear();
									iv_postuser_default
											.setVisibility(View.VISIBLE);
									tv_postuser_default
											.setVisibility(View.VISIBLE);
								} else {
									ToastUtil.showToastShortBottom(context,
											"没有更多帖子了");
								}
								postSelectionfragmentAdapter.setData(postList);
								if (adapterFlag == postSelectionfragmentAdapter.REFRESH
										|| adapterFlag == postSelectionfragmentAdapter.NORMAL) {
									prl_postselectionfragment
											.getRefreshableView().setSelection(
													positionnum);
									postSelectionfragmentAdapter
											.notifyDataSetChanged();
								}
							}
						}
					}
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg")) {
						String msg = jobj.getString("msg");
						if (msg != null && !TextUtils.isEmpty(msg)) {
							ToastUtil.showToastShortBottom(context, msg);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sfvv_item_postselectionfragment:
			if (mVideoView != null) {
				if (mVideoView.isPlaying()) {
					Intent intent = new Intent(context,
							PostSelectionDetailActivity.class);
					intent.putExtra("postId", postId);
					intent.putExtra("flag", "click");
					context.startActivity(intent);
					mVideoView.pause();
				} else {
					mVideoView.start();
				}
			}
			break;
		case R.id.iv_postselectionfragment_post:// 发帖
			if (spUtil.getString("cellphone", "") != null
					&& !TextUtils.isEmpty(spUtil.getString("cellphone", ""))) {
				String username = spUtil.getString("username", "");
				if (username != null && !TextUtils.isEmpty(username)) {
					showPopPhoto();
				} else {
					tqDialog();
				}
			} else {
				startActivity(new Intent(context, LoginActivity.class)
						.putExtra("previous",
								Global.LOGIN_TO_POSTSELECTIONFRAGMENT));
			}
			break;
		case R.id.btn_header_postselection_all:// 全部
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Select_Whole);
			initAll();
			break;
		case R.id.btn_header_postselection_gz:// 关注
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Select_Selected);
			if (mVideoView != null) {
				if (mVideoView.isPlaying()) {
					mVideoView.pause();
				}
			}
			if (mVideoView != null) {
				mVideoView.release();
				mVideoView = null;
			}
			btn_header_postselection_gz.setTextColor(context.getResources()
					.getColor(R.color.aD1494F));
			btn_header_postselection_gz.setBackground(context.getResources()
					.getDrawable(R.drawable.btn_header_postselection));
			btn_header_postselection_all.setTextColor(context.getResources()
					.getColor(R.color.a333333));
			btn_header_postselection_all.setBackgroundColor(Color.TRANSPARENT);
			btn_header_postselection_video.setTextColor(context.getResources()
					.getColor(R.color.a333333));
			btn_header_postselection_video
					.setBackgroundColor(Color.TRANSPARENT);
			isFollowed = 1;
			isExistsVideo = 0;
			isExistsImg = 0;
			page = 1;
			getData();
			break;
		case R.id.btn_header_postselection_video:// 视频
			UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Select_Video);
			if (mVideoView != null) {
				if (mVideoView.isPlaying()) {
					mVideoView.pause();
				}
			}
			if (mVideoView != null) {
				mVideoView.release();
				mVideoView = null;
			}
			btn_header_postselection_video.setTextColor(context.getResources()
					.getColor(R.color.aD1494F));
			btn_header_postselection_video.setBackground(context.getResources()
					.getDrawable(R.drawable.btn_header_postselection));
			btn_header_postselection_all.setTextColor(context.getResources()
					.getColor(R.color.a333333));
			btn_header_postselection_all.setBackgroundColor(Color.TRANSPARENT);
			btn_header_postselection_gz.setTextColor(context.getResources()
					.getColor(R.color.a333333));
			btn_header_postselection_gz.setBackgroundColor(Color.TRANSPARENT);
			isFollowed = 0;
			isExistsVideo = 1;
			isExistsImg = 0;
			page = 1;
			getData();
			break;
		default:
			break;
		}
	}

	private void initAll() {
		if (mVideoView != null) {
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
			}
		}
		if (mVideoView != null) {
			mVideoView.release();
			mVideoView = null;
		}
		btn_header_postselection_all.setTextColor(context.getResources()
				.getColor(R.color.aD1494F));
		btn_header_postselection_all.setBackground(context.getResources()
				.getDrawable(R.drawable.btn_header_postselection));
		btn_header_postselection_gz.setTextColor(context.getResources()
				.getColor(R.color.a333333));
		btn_header_postselection_gz.setBackgroundColor(Color.TRANSPARENT);
		btn_header_postselection_video.setTextColor(context.getResources()
				.getColor(R.color.a333333));
		btn_header_postselection_video.setBackgroundColor(Color.TRANSPARENT);
		isFollowed = 0;
		isExistsVideo = 0;
		isExistsImg = 0;
		page = 1;
		getData();
	}

	private void tqDialog() {
		new UserNameAlertDialog(context).builder().setTitle("没昵称我  \"蓝瘦\"")
				.setTextViewHint("请填写昵称").setCloseButton(new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).setComplaintsButton("保	 存", new OnClickListener() {
					@Override
					public void onClick(View v) {
						CommUtil.updateUser(spUtil.getString("cellphone", "0"),
								Global.getIMEI(context), context,
								spUtil.getInt("userid", 0),
								UserNameAlertDialog.getUserName(), null,
								updateUser);
					}
				}).show();
	}

	private AsyncHttpResponseHandler updateUser = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				Utils.mLogError("==-->updateUser" + new String(responseBody));
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show();
					spUtil.saveString("username",
							UserNameAlertDialog.getUserName());
					getData();
				} else {
					String msg = jsonObject.getString("msg");
					Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
	};
	private Uri fromFile;
	protected int flag;

	private void showPopPhoto() {
		if (pWin == null) {
			final View view = View.inflate(context, R.layout.activity_media,
					null);
			RelativeLayout rlParent = (RelativeLayout) view
					.findViewById(R.id.rl_pop_selectphoto_parent);
			LinearLayout ll_pop_selectphoto_camera = (LinearLayout) view
					.findViewById(R.id.ll_pop_selectphoto_camera);
			LinearLayout ll_pop_selectphoto_photo = (LinearLayout) view
					.findViewById(R.id.ll_pop_selectphoto_photo);
			LinearLayout ll_pop_selectphoto_video = (LinearLayout) view
					.findViewById(R.id.ll_pop_selectphoto_video);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
			pWin.setFocusable(true);
			pWin.setWidth(Utils.getDisplayMetrics(context)[0]);
			pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
			rlParent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					pWin.dismiss();
					pWin = null;
				}
			});
			ll_pop_selectphoto_camera.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// 拍照
					flag = 2;
					getImageFromCamera();
					pWin.dismiss();
					pWin = null;
				}
			});
			ll_pop_selectphoto_photo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// 相册
					flag = 1;
					Crop.pickImage(context, PostSelectionFragment.this);
					pWin.dismiss();
					pWin = null;
				}
			});
			ll_pop_selectphoto_video.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// 小视频
					startActivity(new Intent(context,
							MediaRecorderActivity.class));
					pWin.dismiss();
					pWin = null;
				}
			});
		}
	}

	/**
	 * 从相机中获取拍照图片
	 */
	private void getImageFromCamera() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String fileName = "Pet_Temp_camera"
					+ String.valueOf(System.currentTimeMillis() + ".png");
			File cropFile = new File(PetTemp, fileName);
			fromFile = Uri.fromFile(cropFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fromFile);
			startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
		} else {
			Toast.makeText(context, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Crop.REQUEST_PICK) {
				beginCrop(data.getData());
			} else if (requestCode == PET_CROP) {
				handleCrop(resultCode, data);
			} else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
				beginCrop(fromFile);
			}
		}
	}

	private void handleCrop(int resultCode, Intent result) {
		if (resultCode == Activity.RESULT_OK) {
			Uri output = Crop.getOutput(result);
			Intent intent = new Intent(context, SendSelectPostActivity.class);
			intent.putExtra("flag", "pic");
			intent.putExtra("picOutput", output);
			context.startActivity(intent);
		} else if (resultCode == Crop.RESULT_ERROR) {
		}
	}

	private void beginCrop(Uri source) {
		/*
		 * String fileName = "Pet_Crop_Img" +
		 * String.valueOf(System.currentTimeMillis() + ".png"); File cropFile =
		 * new File(PetCrop, fileName); Uri destination =
		 * Uri.fromFile(cropFile); Crop.of(source, destination).withAspect(75,
		 * 69) .start(context, this, PET_CROP);
		 */
		startActivity(new Intent(context, ClipPictureActivity.class).putExtra(
				"source", source).putExtra("flag", flag));
	}

}
