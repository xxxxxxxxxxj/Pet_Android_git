package com.haotang.pet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.pet.adapter.HlvItemPostselectionDetailAdapter;
import com.haotang.pet.adapter.MlvItemPostselectionDetailAdapter;
import com.haotang.pet.entity.PostSelectionDetailBean;
import com.haotang.pet.entity.PostSelectionDetailBean.CommentsBean;
import com.haotang.pet.entity.PostSelectionDetailBean.CommentsBean.UserBean;
import com.haotang.pet.entity.PostSelectionDetailBean.FlowerUsersBean;
import com.haotang.pet.entity.PostSelectionDetailBean.PostUserBean;
import com.haotang.pet.entity.PostSelectionDetailBean.PostUserBean.UserMemberLevel;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengShareUtils;
import com.haotang.pet.util.UnicodeToEmoji;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.wxpay.Util_WX;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.HorizontalListView;
import com.haotang.pet.view.PeriscopeLayout;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.SurfaceVideoView;
import com.loveplusplus.demo.image.ImagePagerActivity;
import com.melink.baseframe.utils.DensityUtils;
import com.melink.bqmmsdk.bean.Emoji;
import com.melink.bqmmsdk.sdk.BQMM;
import com.melink.bqmmsdk.sdk.BQMMMessageHelper;
import com.melink.bqmmsdk.sdk.IBqmmSendMessageListener;
import com.melink.bqmmsdk.ui.keyboard.BQMMKeyboard;
import com.melink.bqmmsdk.ui.keyboard.IBQMMUnicodeEmojiProvider;
import com.melink.bqmmsdk.widget.BQMMEditView;
import com.melink.bqmmsdk.widget.BQMMMessageText;
import com.melink.bqmmsdk.widget.BQMMSendButton;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.OauthHelper;
import com.yixia.weibo.sdk.util.DeviceUtils;

/**
 * <p>
 * Title:PostSelectionDetailActivity
 * </p>
 * <p>
 * Description:精选帖子详情页
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-13 上午11:28:18
 */
@SuppressLint("NewApi")
public class PostSelectionDetailActivity extends FragmentActivity implements
		SurfaceVideoView.OnPlayStateListener, OnErrorListener,
		OnPreparedListener, OnClickListener, OnCompletionListener,
		OnInfoListener {
	protected static final int CACHE_SUCCESS = 111;
	private PullToRefreshListView prl_postselection_detail;
	private View header;
	private TextView tv_item_postselectionfragment_username;
	private TextView tv_item_postselectionfragment_fbtime;
	private RelativeLayout rl_item_postselectionfragment_userinfo;
	private SelectableRoundedImageView sriv_item_postselectionfragment_userimg;
	private ImageView iv_item_postselectionfragment_level;
	private Button btn_item_postselectionfragment_gz;
	private ImageView iv_item_postselectionfragment_img;
	private HorizontalListView hlv_item_postselectionfragment;
	private Button btn_item_postselectionfragment_num;
	private ImageButton ib_item_postselectionfragment_sh;
	private ImageButton ib_item_postselectionfragment_bb;
	private ImageButton ib_item_postselectionfragment_pl;
	private ImageButton ib_item_postselectionfragment_fx;
	private TextView tv_item_postselectionfragment_qbpl;
	/*
	 * BQMM集成 相关变量
	 */
	private BQMMKeyboard bqmmKeyboard;
	private View inputbox;
	private BQMMSendButton bqmmSend;
	private CheckBox bqmmKeyboardOpen;
	private BQMMEditView bqmmEditView;
	private BQMM bqmmsdk;
	/**
	 * BQMM集成 键盘切换相关
	 */
	private Rect tmp = new Rect();
	private int mScreenHeight;
	private View mMainContainer;
	private final int DISTANCE_SLOP = 180;
	private final String LAST_KEYBOARD_HEIGHT = "last_keyboard_height";
	private boolean mPendingShowPlaceHolder;
	private RelativeLayout rl_item_postselectionfragment_picorvideo;
	private int postId;
	private BQMMMessageText bqmt_item_postselectionfragment_title;
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;
	private PopupWindow pWin;
	protected String sharetitle = "宠物家";
	protected String sharetxt = "宠物家";
	protected String shareurl = "";
	private IWXAPI api;
	private UmengShareUtils umengShareUtils;
	private String imgUrl;
	protected Bitmap bmp;
	private int page = 1;
	private List<FlowerUsersBean> flowerUsersList = new ArrayList<FlowerUsersBean>();
	private List<CommentsBean> commentsList = new ArrayList<CommentsBean>();
	private List<CommentsBean> tempList = new ArrayList<CommentsBean>();
	private HlvItemPostselectionDetailAdapter<FlowerUsersBean> hlvItemPostselectionDetailAdapter;
	private MlvItemPostselectionDetailAdapter<CommentsBean> mlvItemPostselectionDetailAdapter;
	private SurfaceVideoView sfvv_item_postselectionfragment;
	/** 暂停按钮 */
	private ImageView iv_item_postselectionfragment_play_status;
	private ImageView iv_item_postselectionfragment_videoloading;
	private int FLAG;
	private int FOLLOW = 1;
	private int FLOWER = 2;
	private int SHIT = 3;
	private int COMMENT = 4;
	private int index;
	private int DELETEPOST = 5;
	private int userId;
	private int flowerCount;
	private PeriscopeLayout circle_image_flower_other;
	private PeriscopeLayout circle_image_feces_other;
	private RelativeLayout rl_item_postselectionfragment_num;
	private int picOrVideo;
	private int pic = 1;
	private int video = 2;
	private String[] imgUrls = new String[1];
	private Button bt_titlebar_other;
	private TextView tv_titlebar_title;
	private ImageButton ib_titlebar_back;
	private int networkType;
	private String coverImg;
	private RelativeLayout include1;
	protected String content;
	protected int contentType;
	private int commentsCount;
	private int refFlag;
	private List<Object> emojis = new ArrayList<Object>();
	private CheckBox cb_item_postselectionfragment_voice;
	private ImageView iv_item_postselectionfragment_default;
	private TextView tv_item_postselectionfragment_default;
	protected long readSize;
	protected int mediaLength;
	private int position;
	private String videoPath;
	private boolean isToLogin = false;
	private boolean isTouch = false;
	private String localVideoPath;
	private AudioManager mAudioManager;
	private TextView tv_item_postselectionfragment_delete;
	private int localPage = 1;
	private UserMemberLevel userMemberLevel;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CACHE_SUCCESS:
				initPlayer(1);
				break;
			}
			super.handleMessage(msg);
		}
	};
	private String flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initView();
		initLinster();
		initBQMM();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!Utils.checkLogin(this)) {
			isToLogin = false;
		}
		String userName = spUtil.getString("username", "");
		if (userName.equals("") || TextUtils.isEmpty(userName)) {
			isTouch = false;
		}
		page = localPage;
		getData();
	}

	private void init() {
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		api = WXAPIFactory.createWXAPI(this, Global.APP_ID);
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		postId = intent.getIntExtra("postId", 0);
		flag = intent.getStringExtra("flag");
	}

	private void getData() {
		refFlag = 0;
		pDialog.showDialog();
		localPage = page;
		CommUtil.queryPostInfo(spUtil.getString("cellphone", ""), this, postId,
				page, queryPostInfo);
	}

	private AsyncHttpResponseHandler queryPostInfo = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			prl_postselection_detail.onRefreshComplete();
			pDialog.closeDialog();
			processData(new String(responseBody));
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			prl_postselection_detail.onRefreshComplete();
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
						JSONObject jData = jobj.getJSONObject("data");
						Gson gson = new Gson();
						PostSelectionDetailBean data = gson
								.fromJson(jData.toString(),
										PostSelectionDetailBean.class);
						if (data != null) {
							int userId2 = data.getUserId();
							int isBianBian = data.getIsBianBian();
							int isFlower = data.getIsFlower();
							shareurl = data.getShareUrl();
							List<CommentsBean> comments = data.getComments();
							List<FlowerUsersBean> flowerUsers = data
									.getFlowerUsers();
							int duty = data.getDuty();
							String content = data.getContent();
							int pageSize = data.getPageSize();
							if (content != null && !TextUtils.isEmpty(content)) {
								sharetxt = content;
							} else {
								sharetxt = "宠物家";
							}
							PostUserBean postUser = data.getPostUser();
							List<String> imgs = data.getImgs();
							List<String> videos = data.getVideos();
							List<String> coverImgs = data.getCoverImgs();
							int isFollow = data.getIsFollow();
							String created = data.getCreated();
							Utils.setStringText(
									tv_item_postselectionfragment_fbtime,
									created);
							if (userId2 == spUtil.getInt("userid", 0)) {
								btn_item_postselectionfragment_gz
										.setVisibility(View.GONE);
								tv_item_postselectionfragment_delete
										.setVisibility(View.VISIBLE);
							} else {
								btn_item_postselectionfragment_gz
										.setVisibility(View.VISIBLE);
								tv_item_postselectionfragment_delete
										.setVisibility(View.GONE);
							}
							if (coverImgs != null && coverImgs.size() > 0) {
								imgUrl = coverImgs.get(0);
								coverImg = coverImgs.get(0);
							}
							if (isFollow == 2) {// 未关注
								btn_item_postselectionfragment_gz
										.setBackground(getResources()
												.getDrawable(
														R.drawable.btn_item_postselectionfragment_notgz));
							} else if (isFollow == 1) {// 已关注
								btn_item_postselectionfragment_gz
										.setBackground(getResources()
												.getDrawable(
														R.drawable.btn_item_postselectionfragment_gz));
							}
							if (isBianBian == 1) {
								ib_item_postselectionfragment_bb
										.setEnabled(false);
								ib_item_postselectionfragment_bb
										.setBackground(getResources()
												.getDrawable(
														R.drawable.ib_item_postselectionfragment_bb_select));
							} else {
								ib_item_postselectionfragment_bb
										.setEnabled(true);
								ib_item_postselectionfragment_bb
										.setBackground(getResources()
												.getDrawable(
														R.drawable.ib_item_postselectionfragment_bb));
							}
							if (isFlower == 1) {
								ib_item_postselectionfragment_sh
										.setEnabled(false);
								ib_item_postselectionfragment_sh
										.setBackground(getResources()
												.getDrawable(
														R.drawable.ib_item_postselectionfragment_sh_select));
							} else {
								ib_item_postselectionfragment_sh
										.setEnabled(true);
								ib_item_postselectionfragment_sh
										.setBackground(getResources()
												.getDrawable(
														R.drawable.ib_item_postselectionfragment_sh));
							}
							if (videos != null && videos.size() > 0) {
								cb_item_postselectionfragment_voice
										.setVisibility(View.VISIBLE);
								videoPath = videos.get(0);
								if (videoPath != null
										&& !TextUtils.isEmpty(videoPath)) {
									picOrVideo = video;
									prepareVideo(1);
								}
							}
							if (imgs != null && imgs.size() > 0) {
								cb_item_postselectionfragment_voice
										.setVisibility(View.GONE);
								picOrVideo = pic;
								imgUrl = imgs.get(0);
								ImageLoaderUtil.loadImg(imgUrl,
										iv_item_postselectionfragment_img,
										R.drawable.icon_production_default,
										null);
								sfvv_item_postselectionfragment
										.setVisibility(View.GONE);
								iv_item_postselectionfragment_play_status
										.setVisibility(View.GONE);
								iv_item_postselectionfragment_img
										.setVisibility(View.VISIBLE);
							}
							if (content != null && !TextUtils.isEmpty(content)) {
								JSONArray jsonArray = null;
								if (content.contains("［")
										&& content.contains("］")
										&& content.contains("＂")) {
									content = content.replaceAll("［", "[")
											.replaceAll("］", "]")
											.replaceAll("＂", "\"");
									try {
										jsonArray = new JSONArray(content);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								} else if (content.contains("[")
										&& content.contains("]")
										&& content.contains("\"")) {
									try {
										jsonArray = new JSONArray(content);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								} else {
									emojis.clear();
									emojis.add(content);
									jsonArray = BQMMMessageHelper
											.getMixedMessageData(emojis);
								}
								bqmt_item_postselectionfragment_title
										.setVisibility(View.VISIBLE);
								// 小表情或文字或图文混排
								bqmt_item_postselectionfragment_title
										.showMessage(
												postId + "",
												BQMMMessageHelper
														.getMsgCodeString(jsonArray),
												BQMMMessageText.EMOJITYPE,
												jsonArray);
								bqmt_item_postselectionfragment_title
										.getBackground().setAlpha(255);
							} else {
								bqmt_item_postselectionfragment_title
										.setVisibility(View.GONE);
							}
							// 用户信息相关
							if (postUser != null) {
								String userName = postUser.getUserName();
								if (userName != null
										&& !TextUtils.isEmpty(userName)) {
									sharetitle = userName;
								} else {
									sharetitle = "宠物家";
								}
								String avatar = postUser.getAvatar();
								userId = postUser.getUserId();
								userMemberLevel = postUser.getUserMemberLevel();
								Utils.setStringText(
										tv_item_postselectionfragment_username,
										userName);
								ImageLoaderUtil
										.loadImg(
												avatar,
												sriv_item_postselectionfragment_userimg,
												R.drawable.icon_self, null);
							}
							if (duty == 1 || duty == 2) {
								iv_item_postselectionfragment_level
										.setBackgroundResource(R.drawable.iv_item_postselectionfragment_level);
								iv_item_postselectionfragment_level
										.setVisibility(View.VISIBLE);
							} else {
								if (userMemberLevel != null) {
									String memberIcon = userMemberLevel
											.getMemberIcon();
									if (memberIcon != null
											&& !TextUtils.isEmpty(memberIcon)) {
										ImageLoaderUtil
												.loadImg(
														memberIcon,
														iv_item_postselectionfragment_level,
														R.drawable.icon_self,
														null);
										iv_item_postselectionfragment_level
												.setVisibility(View.VISIBLE);
									} else {
										iv_item_postselectionfragment_level
												.setVisibility(View.GONE);
									}
								} else {
									iv_item_postselectionfragment_level
											.setVisibility(View.GONE);
								}
							}
							flowerCount = data.getFlowerCount();
							if (flowerCount > 99) {
								btn_item_postselectionfragment_num
										.setVisibility(View.VISIBLE);
								btn_item_postselectionfragment_num
										.setText("99+");
							} else {
								if (flowerCount > 10) {
									btn_item_postselectionfragment_num
											.setVisibility(View.VISIBLE);
									btn_item_postselectionfragment_num
											.setText(flowerCount + "");
								} else {
									btn_item_postselectionfragment_num
											.setVisibility(View.GONE);
								}
							}
							if (flowerUsers != null && flowerUsers.size() > 0) {
								flowerUsersList.clear();
								flowerUsersList.addAll(flowerUsers);
								hlvItemPostselectionDetailAdapter
										.setData(flowerUsersList);
							} else {
								rl_item_postselectionfragment_num
										.setVisibility(View.GONE);
							}
							commentsCount = data.getCommentsCount();
							if (commentsCount <= 0) {
								tv_item_postselectionfragment_qbpl
										.setVisibility(View.GONE);
							} else {
								tv_item_postselectionfragment_qbpl
										.setVisibility(View.VISIBLE);
								Utils.setStringText(
										tv_item_postselectionfragment_qbpl,
										"全部评论(" + commentsCount + ")");
							}
							// 评论列表
							if (comments != null && comments.size() > 0) {
								commentsList.clear();
								iv_item_postselectionfragment_default
										.setVisibility(View.GONE);
								tv_item_postselectionfragment_default
										.setVisibility(View.GONE);
								prl_postselection_detail.getRefreshableView()
										.setDividerHeight(2);
								if (page == 1) {
									tempList.clear();
									tempList.addAll(comments);
									commentsList.addAll(tempList);
								} else {
									tempList.addAll(comments);
									if (index == page) {
										List<CommentsBean> subList = tempList
												.subList(0, (page * pageSize)
														- pageSize);// 前面几条正确的数据
										subList.addAll(comments);
										commentsList.addAll(subList);
										tempList.clear();
										tempList.addAll(commentsList);
									} else {
										commentsList.addAll(tempList);
									}
								}
								index = page;
								mlvItemPostselectionDetailAdapter
										.setData(commentsList);
								mlvItemPostselectionDetailAdapter.setFlag(0);
								if (flag != null) {
									if (flag.equals("bottom")) {
										prl_postselection_detail
												.getRefreshableView()
												.setSelection(
														prl_postselection_detail
																.getRefreshableView()
																.getBottom());
									}
								}
								if (comments.size() >= pageSize) {
									page = ++page;
								}
							} else {
								initDefault();
							}
						} else {
							initDefault();
						}
					} else {
						initDefault();
					}
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg")) {
						String msg = jobj.getString("msg");
						if (msg != null && !TextUtils.isEmpty(msg)) {
							ToastUtil.showToastShortBottom(this, msg);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initDefault() {
		if (page == 1) {
			iv_item_postselectionfragment_default.setVisibility(View.VISIBLE);
			tv_item_postselectionfragment_default.setVisibility(View.VISIBLE);
		} else {
			ToastUtil.showToastShortBottom(this, "没有更多评论了");
		}
		prl_postselection_detail.getRefreshableView().setDividerHeight(0);
		if (page == 1) {
			refFlag = 1;
			commentsList.clear();
			commentsList.add(new CommentsBean("", "", 1024, null, 1024));
			mlvItemPostselectionDetailAdapter.setData(commentsList);
			mlvItemPostselectionDetailAdapter.setFlag(1);
		}
	}

	private void prepareVideo(int videoflag) {
		networkType = Utils.getNetworkType(this);
		String substring = videoPath.substring(videoPath.lastIndexOf("/") + 1);
		String localUrl = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/PetCircleVideoCache/" + substring;
		int localflag = 0;
		if (new File(localUrl).exists()) {// 文件存在，读取缓存中的文件播放
			localVideoPath = localUrl;
			localflag = 1;
		} else {// 文件不存在，缓存文件
			localflag = 2;
			localVideoPath = videoPath;
			if (networkType == 1 || videoflag == 2) {// WIFI网络或者用户点击
				cacheFile();
			}
		}
		if (localflag == 1) {
			initPlayer(1);// 播放
		} else if (localflag == 2) {
			if (networkType == 1 || videoflag == 2) {// WIFI网络或者用户点击
				initPlayer(2);// 加载
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

	private void initPlayer(int type) {
		if (type == 1) {// 播放
			iv_item_postselectionfragment_img.setVisibility(View.GONE);
			iv_item_postselectionfragment_play_status.setVisibility(View.GONE);
			iv_item_postselectionfragment_videoloading.setVisibility(View.GONE);
			if (sfvv_item_postselectionfragment != null) {
				sfvv_item_postselectionfragment.setVisibility(View.VISIBLE);
				sfvv_item_postselectionfragment.setVideoPath(localVideoPath);
				sfvv_item_postselectionfragment.start();
			}
			int vol = (int) (cb_item_postselectionfragment_voice.isChecked() ? mAudioManager
					.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) : 0);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0); // 设置音量
		} else if (type == 0) {// 不播放
			ImageLoaderUtil.loadImg(coverImg,
					iv_item_postselectionfragment_img,
					R.drawable.icon_production_default, null);
			sfvv_item_postselectionfragment.setVisibility(View.GONE);
			iv_item_postselectionfragment_img.setVisibility(View.VISIBLE);
			iv_item_postselectionfragment_videoloading.setVisibility(View.GONE);
			iv_item_postselectionfragment_play_status
					.setVisibility(View.VISIBLE);
			iv_item_postselectionfragment_play_status.bringToFront();
		}
		if (type == 2) {// 加载
			ImageLoaderUtil.loadImg(coverImg,
					iv_item_postselectionfragment_img,
					R.drawable.icon_production_default, null);
			sfvv_item_postselectionfragment.setVisibility(View.GONE);
			iv_item_postselectionfragment_img.setVisibility(View.VISIBLE);
			iv_item_postselectionfragment_videoloading
					.setVisibility(View.VISIBLE);
			iv_item_postselectionfragment_play_status.setVisibility(View.GONE);
			iv_item_postselectionfragment_videoloading
					.setBackgroundResource(R.drawable.spinner_small);
			AnimationDrawable ad = (AnimationDrawable) iv_item_postselectionfragment_videoloading
					.getBackground();
			ad.start();
			iv_item_postselectionfragment_videoloading.bringToFront();
		}
	}

	private void initBQMM() {
		/**
		 * BQMM集成 加载SDK
		 */
		bqmmsdk = BQMM.getInstance();
		// 初始化表情MM键盘，需要传入关联的EditView,SendBtn
		bqmmsdk.setEditView(bqmmEditView);
		bqmmsdk.setKeyboard(bqmmKeyboard);
		bqmmsdk.setSendButton(bqmmSend);
		UnicodeToEmoji.initPhotos(this);
		bqmmsdk.setUnicodeEmojiProvider(new IBQMMUnicodeEmojiProvider() {
			@Override
			public Drawable getDrawableFromCodePoint(int i) {
				return UnicodeToEmoji.EmojiImageSpan.getEmojiDrawable(i);
			}
		});
		bqmmsdk.load();
		/*
		 * 默认方式打开软键盘时切换表情符号的状态
		 */
		bqmmEditView.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				bqmmKeyboardOpen.setChecked(false);
				Utils.mLogError("==-->111");// 为什么会执行两次
				String userName = spUtil.getString("username", "");
				if (!Utils.checkLogin(PostSelectionDetailActivity.this)) {
					if (!isToLogin) {
						isToLogin = true;
						Intent intent = new Intent(
								PostSelectionDetailActivity.this,
								LoginActivity.class);
						startActivity(intent);
					}
				} else {
					if (userName.equals("") || TextUtils.isEmpty(userName)) {
						if (!isTouch) {
							isTouch = true;
							tqDialog();
						}
					}
				}
				return false;
			}
		});
		/**
		 * BQMM集成 实现输入联想
		 */
		bqmmEditView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				BQMM.getInstance().startShortcutPopupWindowByoffset(
						PostSelectionDetailActivity.this, s.toString(),
						bqmmSend, 0, DensityUtils.dip2px(4));
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.length() <= 0) {
					bqmmSend.setEnabled(false);
					bqmmSend.setBackgroundResource(R.drawable.img_bqmm_sendbutton_bg);
				} else {
					bqmmSend.setEnabled(true);
					bqmmSend.setBackgroundResource(R.drawable.img_bqmm_sendbutton_bgfb);
				}
			}
		});
		/**
		 * BQMM集成 设置发送消息的回调
		 */
		bqmmsdk.setBqmmSendMsgListener(new IBqmmSendMessageListener() {
			/**
			 * 单个大表情消息
			 */
			@Override
			public void onSendFace(final Emoji face) {
				bqmmKeyboardOpen.setChecked(false);
				closebroad();
				FLAG = COMMENT;
				JSONArray faceMessageData = BQMMMessageHelper
						.getFaceMessageData(face);
				content = faceMessageData.toString();
				contentType = 2;
				CommUtil.commentPost(contentType,
						spUtil.getString("cellphone", ""),
						PostSelectionDetailActivity.this, postId, content,
						followUserHandler);
			}

			/**
			 * 图文混排消息
			 */
			@Override
			public void onSendMixedMessage(List<Object> emojis,
					boolean isMixedMessage) {
				bqmmKeyboardOpen.setChecked(false);
				closebroad();
				JSONArray mixedMessageData = BQMMMessageHelper
						.getMixedMessageData(emojis);
				FLAG = COMMENT;
				content = mixedMessageData.toString();
				contentType = 1;
				CommUtil.commentPost(contentType,
						spUtil.getString("cellphone", ""),
						PostSelectionDetailActivity.this, postId, content,
						followUserHandler);
			}
		});
		/**
		 * 表情键盘切换监听
		 */
		bqmmEditView.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						// Keyboard -> BQMM
						if (mPendingShowPlaceHolder) {
							// 在设置mPendingShowPlaceHolder时已经调用了隐藏Keyboard的方法，直到Keyboard隐藏前都取消重绘
							if (isKeyboardVisible()) {
								ViewGroup.LayoutParams params = bqmmKeyboard
										.getLayoutParams();
								int distance = getDistanceFromInputToBottom();
								// 调整PlaceHolder高度
								if (distance > DISTANCE_SLOP
										&& distance != params.height) {
									params.height = distance;
									bqmmKeyboard.setLayoutParams(params);
									getPreferences(MODE_PRIVATE)
											.edit()
											.putInt(LAST_KEYBOARD_HEIGHT,
													distance).apply();
								}
								return false;
							} else {
								showBqmmKeyboard();
								mPendingShowPlaceHolder = false;
								return false;
							}
						} else {// BQMM -> Keyboard
							if (isBqmmKeyboardVisible() && isKeyboardVisible()) {
								hideBqmmKeyboard();
								return false;
							}
						}
						return true;
					}
				});
		// 切换开关
		bqmmKeyboardOpen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (spUtil.getString("cellphone", "") != null
						&& !TextUtils.isEmpty(spUtil.getString("cellphone", ""))) {
					String username = spUtil.getString("username", "");
					if (username != null && !TextUtils.isEmpty(username)) {
						// 除非软键盘和PlaceHolder全隐藏时直接显示PlaceHolder，其他情况此处处理软键盘，onPreDrawListener处理PlaceHolder
						if (isBqmmKeyboardVisible()) { // PlaceHolder ->
														// Keyboard
							showSoftInput(bqmmEditView);
						} else if (isKeyboardVisible()) { // Keyboard ->
															// PlaceHolder
							mPendingShowPlaceHolder = true;
							hideSoftInput(bqmmEditView);
						} else { // Just show PlaceHolder
							showBqmmKeyboard();
						}
					} else {
						tqDialog();
					}
				} else {
					goToNext(LoginActivity.class, 0, "");
				}
			}
		});
	}

	private void initLinster() {
		btn_item_postselectionfragment_num.setOnClickListener(this);
		tv_item_postselectionfragment_delete.setOnClickListener(this);
		bqmmEditView.setOnClickListener(this);
		rl_item_postselectionfragment_userinfo.setOnClickListener(this);
		btn_item_postselectionfragment_gz.setOnClickListener(this);
		rl_item_postselectionfragment_picorvideo.setOnClickListener(this);
		ib_item_postselectionfragment_sh.setOnClickListener(this);
		ib_item_postselectionfragment_bb.setOnClickListener(this);
		ib_item_postselectionfragment_fx.setOnClickListener(this);
		cb_item_postselectionfragment_voice.setOnClickListener(this);
		include1.setOnClickListener(this);
		prl_postselection_detail.setOnTouchListener(getOnTouchListener());
		ib_titlebar_back.setOnClickListener(this);
		prl_postselection_detail.getRefreshableView().addHeaderView(header);
		prl_postselection_detail.setMode(Mode.BOTH);
		prl_postselection_detail
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
		sfvv_item_postselectionfragment.setOnPreparedListener(this);
		sfvv_item_postselectionfragment.setOnPlayStateListener(this);
		sfvv_item_postselectionfragment.setOnErrorListener(this);
		sfvv_item_postselectionfragment.setOnClickListener(this);
		sfvv_item_postselectionfragment.setOnInfoListener(this);
		sfvv_item_postselectionfragment.setOnCompletionListener(this);
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		int vol = (int) (cb_item_postselectionfragment_voice.isChecked() ? mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) : 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0); // 设置音量
		sfvv_item_postselectionfragment.start();
		iv_item_postselectionfragment_videoloading.setVisibility(View.GONE);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {// 跟随系统音量走
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
			sfvv_item_postselectionfragment.dispatchKeyEvent(this, event);
			break;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onStateChanged(boolean isPlaying) {
		iv_item_postselectionfragment_play_status
				.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
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
		iv_item_postselectionfragment_play_status.setVisibility(View.VISIBLE);
		sfvv_item_postselectionfragment.pause();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
			// 音频和视频数据不正确
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (!isFinishing())
				sfvv_item_postselectionfragment.pause();
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			if (!isFinishing())
				sfvv_item_postselectionfragment.start();
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
			if (DeviceUtils.hasJellyBean()) {
				sfvv_item_postselectionfragment.setBackground(null);
			} else {
				sfvv_item_postselectionfragment.setBackgroundDrawable(null);
			}
			break;
		}
		return false;
	}

	private void initView() {
		setContentView(R.layout.activity_post_selection_detail);
		prl_postselection_detail = (PullToRefreshListView) findViewById(R.id.prl_postselection_detail);
		header = LayoutInflater.from(this).inflate(
				R.layout.header_item_postselectionfragment, null);
		tv_item_postselectionfragment_username = (TextView) header
				.findViewById(R.id.tv_item_postselectionfragment_username);
		tv_item_postselectionfragment_fbtime = (TextView) header
				.findViewById(R.id.tv_item_postselectionfragment_fbtime);
		rl_item_postselectionfragment_userinfo = (RelativeLayout) header
				.findViewById(R.id.rl_item_postselectionfragment_userinfo);
		sriv_item_postselectionfragment_userimg = (SelectableRoundedImageView) header
				.findViewById(R.id.sriv_item_postselectionfragment_userimg);
		iv_item_postselectionfragment_level = (ImageView) header
				.findViewById(R.id.iv_item_postselectionfragment_level);
		btn_item_postselectionfragment_gz = (Button) header
				.findViewById(R.id.btn_item_postselectionfragment_gz);
		iv_item_postselectionfragment_img = (ImageView) header
				.findViewById(R.id.iv_item_postselectionfragment_img);
		hlv_item_postselectionfragment = (HorizontalListView) header
				.findViewById(R.id.hlv_item_postselectionfragment);
		btn_item_postselectionfragment_num = (Button) header
				.findViewById(R.id.btn_item_postselectionfragment_num);
		ib_item_postselectionfragment_sh = (ImageButton) header
				.findViewById(R.id.ib_item_postselectionfragment_sh);
		ib_item_postselectionfragment_bb = (ImageButton) header
				.findViewById(R.id.ib_item_postselectionfragment_bb);
		ib_item_postselectionfragment_pl = (ImageButton) header
				.findViewById(R.id.ib_item_postselectionfragment_pl);
		ib_item_postselectionfragment_pl.setVisibility(View.GONE);
		ib_item_postselectionfragment_fx = (ImageButton) header
				.findViewById(R.id.ib_item_postselectionfragment_fx);
		rl_item_postselectionfragment_picorvideo = (RelativeLayout) header
				.findViewById(R.id.rl_item_postselectionfragment_picorvideo);
		bqmt_item_postselectionfragment_title = (BQMMMessageText) header
				.findViewById(R.id.bqmt_item_postselectionfragment_title);
		tv_item_postselectionfragment_qbpl = (TextView) header
				.findViewById(R.id.tv_item_postselectionfragment_qbpl);
		include1 = (RelativeLayout) findViewById(R.id.include1);
		inputbox = findViewById(R.id.messageToolBox);
		mMainContainer = findViewById(R.id.main_container);
		bqmmKeyboard = (BQMMKeyboard) findViewById(R.id.chat_msg_input_box);
		bqmmSend = (BQMMSendButton) findViewById(R.id.chatbox_send);
		bqmmKeyboardOpen = (CheckBox) findViewById(R.id.chatbox_open);
		bqmmEditView = (BQMMEditView) findViewById(R.id.chatbox_message);
		if (flag != null) {
			if (flag.equals("comment")) {
				bqmmEditView.requestFocus();
			}
		}
		sfvv_item_postselectionfragment = (SurfaceVideoView) header
				.findViewById(R.id.sfvv_item_postselectionfragment);
		iv_item_postselectionfragment_play_status = (ImageView) header
				.findViewById(R.id.iv_item_postselectionfragment_play_status);
		iv_item_postselectionfragment_play_status.bringToFront();
		circle_image_flower_other = (PeriscopeLayout) header
				.findViewById(R.id.circle_image_flower_other);
		circle_image_feces_other = (PeriscopeLayout) header
				.findViewById(R.id.circle_image_feces_other);
		rl_item_postselectionfragment_num = (RelativeLayout) header
				.findViewById(R.id.rl_item_postselectionfragment_num);
		iv_item_postselectionfragment_videoloading = (ImageView) header
				.findViewById(R.id.iv_item_postselectionfragment_videoloading);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		cb_item_postselectionfragment_voice = (CheckBox) header
				.findViewById(R.id.cb_item_postselectionfragment_voice);
		iv_item_postselectionfragment_default = (ImageView) header
				.findViewById(R.id.iv_item_postselectionfragment_default);
		tv_item_postselectionfragment_default = (TextView) header
				.findViewById(R.id.tv_item_postselectionfragment_default);
		tv_item_postselectionfragment_delete = (TextView) header
				.findViewById(R.id.tv_item_postselectionfragment_delete);
		tv_titlebar_title.setText("详情");
		bt_titlebar_other.setVisibility(View.GONE);
		RelativeLayout.LayoutParams mPreviewParams = (RelativeLayout.LayoutParams) rl_item_postselectionfragment_picorvideo
				.getLayoutParams();
		mPreviewParams.width = DeviceUtils.getScreenWidth(this);
		mPreviewParams.height = DeviceUtils.getScreenWidth(this) * 69 / 75;
		hlvItemPostselectionDetailAdapter = new HlvItemPostselectionDetailAdapter<FlowerUsersBean>(
				this, flowerUsersList);
		hlv_item_postselectionfragment
				.setAdapter(hlvItemPostselectionDetailAdapter);
		mlvItemPostselectionDetailAdapter = new MlvItemPostselectionDetailAdapter<CommentsBean>(
				this, commentsList);
		prl_postselection_detail.setAdapter(mlvItemPostselectionDetailAdapter);
		hlv_item_postselectionfragment
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						FlowerUsersBean flowerUsersBean = flowerUsersList
								.get(position);
						if (flowerUsersBean != null) {
							int userId2 = flowerUsersBean.getUserId();
							startActivity(new Intent(
									PostSelectionDetailActivity.this,
									PostUserInfoActivity.class).putExtra(
									"userId", userId2));
						}
					}
				});
	}

	@Override
	public void onPause() {
		super.onPause();
		if (picOrVideo == video) {
			if (sfvv_item_postselectionfragment != null) {
				if (sfvv_item_postselectionfragment.isPlaying()) {
					sfvv_item_postselectionfragment.pause();
				}
			}
		}
	}

	private void setDialog() {
		new AlertDialogNavAndPost(PostSelectionDetailActivity.this).builder()
				.setTitle("").setMsg("确定删除此条动态吗")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						FLAG = DELETEPOST;
						CommUtil.deletePost(spUtil.getString("cellphone", ""),
								PostSelectionDetailActivity.this, postId,
								followUserHandler);
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cb_item_postselectionfragment_voice:
			int vol = (int) (cb_item_postselectionfragment_voice.isChecked() ? mAudioManager
					.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) : 0);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0); // 设置音量
			break;
		case R.id.btn_item_postselectionfragment_num:
			goToNext(UserListActivity.class, 0, "flower");
			break;
		case R.id.tv_item_postselectionfragment_delete:
			if (spUtil.getString("cellphone", "") != null
					&& !TextUtils.isEmpty(spUtil.getString("cellphone", ""))) {
				setDialog();
			} else {
				goToNext(LoginActivity.class, 0, "");
			}
			break;
		case R.id.include1:
			// 关闭键盘
			bqmmKeyboardOpen.setChecked(false);
			closebroad();
			break;
		case R.id.ib_titlebar_back:// 返回
			finish();
			break;
		case R.id.rl_item_postselectionfragment_userinfo:// 查看用户信息
			goToNext(PostUserInfoActivity.class, 0, "");
			break;
		case R.id.btn_item_postselectionfragment_gz:// 关注
			if (spUtil.getString("cellphone", "") != null
					&& !TextUtils.isEmpty(spUtil.getString("cellphone", ""))) {
				String username = spUtil.getString("username", "");
				if (username != null && !TextUtils.isEmpty(username)) {
					FLAG = FOLLOW;
					CommUtil.followUser(spUtil.getString("cellphone", ""),
							this, userId, followUserHandler);
				} else {
					tqDialog();
				}
			} else {
				goToNext(LoginActivity.class, 0, "");
			}
			break;
		case R.id.rl_item_postselectionfragment_picorvideo:// 查看图片
			if (picOrVideo == pic) {
				imgUrls[0] = imgUrl;
				imageBrower(0, imgUrls);
			} else if (picOrVideo == video) {
				if (videoPath != null && !TextUtils.isEmpty(videoPath)) {
					prepareVideo(2);
				}
			}
			break;
		case R.id.sfvv_item_postselectionfragment:// 播放视频
			if (sfvv_item_postselectionfragment != null) {
				if (sfvv_item_postselectionfragment.isPlaying()) {
				} else {
					sfvv_item_postselectionfragment.start();
				}
			}
			break;
		case R.id.ib_item_postselectionfragment_sh:// 送花
			if (spUtil.getString("cellphone", "") != null
					&& !TextUtils.isEmpty(spUtil.getString("cellphone", ""))) {
				String username = spUtil.getString("username", "");
				if (username != null && !TextUtils.isEmpty(username)) {
					circle_image_flower_other.addHeart(0);
					circle_image_flower_other.addHeart(0);
					circle_image_flower_other.addHeart(0);
					FLAG = FLOWER;
					CommUtil.giftPost(spUtil.getString("cellphone", ""), this,
							postId, 1, followUserHandler);
				} else {
					tqDialog();
				}
			} else {
				goToNext(LoginActivity.class, 0, "");
			}
			break;
		case R.id.ib_item_postselectionfragment_bb:// 粑粑
			if (spUtil.getString("cellphone", "") != null
					&& !TextUtils.isEmpty(spUtil.getString("cellphone", ""))) {
				String username = spUtil.getString("username", "");
				if (username != null && !TextUtils.isEmpty(username)) {
					circle_image_feces_other.addHeart(1);
					circle_image_feces_other.addHeart(1);
					circle_image_feces_other.addHeart(1);
					FLAG = SHIT;
					CommUtil.giftPost(spUtil.getString("cellphone", ""), this,
							postId, 2, followUserHandler);
				} else {
					tqDialog();
				}
			} else {
				goToNext(LoginActivity.class, 0, "");
			}
			break;
		case R.id.ib_item_postselectionfragment_fx:// 分享
			new Thread(networkTask).start();
			break;
		default:
			break;
		}
	}

	private void tqDialog() {
		Intent intent = new Intent(this, LanShowActivity.class);
		startActivityForResult(intent,
				Global.PETCIRCLEINSIDEDETAIL_TO_DIALOG_SHOW);
	}

	private void goToNext(Class clazz, int previous, String flag) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra("postId", postId);
		intent.putExtra("userId", userId);
		intent.putExtra("previous", previous);
		intent.putExtra("flag", flag);
		startActivity(intent);
	}

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	/**
	 * 网络操作相关的子线程
	 */
	Runnable networkTask = new Runnable() {
		@Override
		public void run() {
			// TODO
			// 在这里进行 http request.网络请求相关操作
			Bitmap returnBitmap = Utils.GetLocalOrNetBitmap(imgUrl);
			Message msg = new Message();
			msg.obj = returnBitmap;
			handler.sendMessage(msg);
		}
	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			bmp = (Bitmap) msg.obj;
			showShare();
		}
	};

	private void showShare() {
		pWin = null;
		if (pWin == null) {
			final View view = View.inflate(this, R.layout.sharedialog, null);
			RelativeLayout rlParent = (RelativeLayout) view
					.findViewById(R.id.rl_sharedialog_parent);
			LinearLayout ll_sharedialog_wxfriend = (LinearLayout) view
					.findViewById(R.id.ll_sharedialog_wxfriend);
			LinearLayout ll_sharedialog_wxpyq = (LinearLayout) view
					.findViewById(R.id.ll_sharedialog_wxpyq);
			LinearLayout ll_sharedialog_qqfriend = (LinearLayout) view
					.findViewById(R.id.ll_sharedialog_qqfriend);
			LinearLayout ll_sharedialog_sina = (LinearLayout) view
					.findViewById(R.id.ll_sharedialog_sina);
			ll_sharedialog_sina.setVisibility(View.VISIBLE);
			Button btn_sharedialog_cancel = (Button) view
					.findViewById(R.id.btn_sharedialog_cancel);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
			pWin.setFocusable(true);
			pWin.setWidth(DeviceUtils.getScreenWidth(this));
			pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
			rlParent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					pWin.dismiss();
					pWin = null;
				}
			});
			ll_sharedialog_wxfriend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// 微信好友
					pWin.dismiss();
					pWin = null;
					setWXShareContent(1);
				}
			});
			ll_sharedialog_wxpyq.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// 微信朋友圈
					pWin.dismiss();
					pWin = null;
					setWXShareContent(2);
				}
			});
			ll_sharedialog_qqfriend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// QQ好友
					pWin.dismiss();
					pWin = null;
					setWXShareContent(3);
				}
			});
			ll_sharedialog_sina.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// 新浪微博
					pWin.dismiss();
					pWin = null;
					setWXShareContent(4);
				}
			});
			btn_sharedialog_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {// 取消
					pWin.dismiss();
					pWin = null;
				}
			});
		}
	}

	private void setWXShareContent(int type) {
		boolean weixinAvilible = Utils.isWeixinAvilible(this);
		if (shareurl.contains("?")) {
			shareurl = shareurl + "&postId=" + postId;
		} else {
			shareurl = shareurl + "?postId=" + postId;
		}
		if (bmp != null && shareurl != null && !TextUtils.isEmpty(shareurl)) {
			if (type == 1 || type == 2) {// 微信
				if (weixinAvilible) {
					WXWebpageObject wxwebpage = new WXWebpageObject();
					wxwebpage.webpageUrl = shareurl;
					WXMediaMessage wxmedia = new WXMediaMessage(wxwebpage);
					wxmedia.title = sharetitle;
					wxmedia.description = sharetxt;
					Bitmap createBitmapThumbnail = Utils
							.createBitmapThumbnail(bmp);
					wxmedia.setThumbImage(createBitmapThumbnail);
					wxmedia.thumbData = Util_WX.bmpToByteArray(
							createBitmapThumbnail, true);
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("webpage");
					req.message = wxmedia;
					if (type == 1) {
						req.scene = SendMessageToWX.Req.WXSceneSession;
					} else {
						req.scene = SendMessageToWX.Req.WXSceneTimeline;
					}
					api.sendReq(req);
				} else {
					ToastUtil.showToastShortBottom(this, "微信不可用");
				}
			} else if (type == 3) {// qq
				umengShareUtils = new UmengShareUtils(this, sharetxt, shareurl,
						sharetitle, imgUrl);
				umengShareUtils.mController.getConfig().closeToast();
				umengShareUtils.mController.postShare(this, SHARE_MEDIA.QQ,
						new SnsPostListener() {
							@Override
							public void onStart() {
								/*
								 * ToastUtil.showToastShortCenter(ADActivity.this
								 * , "开始分享.");
								 */
							}

							@Override
							public void onComplete(SHARE_MEDIA arg0, int eCode,
									SocializeEntity arg2) {
								if (eCode == 200) {
									/*
									 * ToastUtil.showToastShortCenter(
									 * ADActivity.this, "分享成功.");
									 */
								} else {
									String eMsg = "";
									if (eCode == -101) {
										eMsg = "没有授权";
									}
									/*
									 * ToastUtil.showToastShortCenter(
									 * ADActivity.this, "分享失败[" + eCode + "] " +
									 * eMsg);
									 */
								}
							}
						});
			} else if (type == 4) {// 新浪微博
				umengShareUtils = new UmengShareUtils(this, sharetxt, shareurl,
						sharetitle, imgUrl);
				umengShareUtils.mController.getConfig().closeToast();
				boolean isSina = OauthHelper.isAuthenticated(this,
						SHARE_MEDIA.SINA);
				// 如果未授权则授权
				if (!isSina) {
					umengShareUtils.mController.doOauthVerify(this,
							SHARE_MEDIA.SINA, new UMAuthListener() {
								@Override
								public void onStart(SHARE_MEDIA arg0) {

								}

								@Override
								public void onError(SocializeException arg0,
										SHARE_MEDIA arg1) {

								}

								@Override
								public void onComplete(Bundle value,
										SHARE_MEDIA platform) {
									umengShareUtils.mController.postShare(
											PostSelectionDetailActivity.this,
											SHARE_MEDIA.SINA,
											new SnsPostListener() {
												@Override
												public void onStart() {
													/*
													 * ToastUtil.
													 * showToastShortCenter
													 * (ADActivity.this ,
													 * "开始分享.");
													 */
												}

												@Override
												public void onComplete(
														SHARE_MEDIA arg0,
														int eCode,
														SocializeEntity arg2) {
													if (eCode == 200) {
														/*
														 * ToastUtil.
														 * showToastShortCenter(
														 * ADActivity.this,
														 * "分享成功.");
														 */
													} else {
														String eMsg = "";
														if (eCode == -101) {
															eMsg = "没有授权";
														}
														/*
														 * ToastUtil.
														 * showToastShortCenter(
														 * ADActivity.this,
														 * "分享失败[" + eCode +
														 * "] " + eMsg);
														 */
													}
												}
											});
								}

								@Override
								public void onCancel(SHARE_MEDIA arg0) {
								}
							});
				} else {
					umengShareUtils.mController.postShare(
							PostSelectionDetailActivity.this, SHARE_MEDIA.SINA,
							new SnsPostListener() {
								@Override
								public void onStart() {
									/*
									 * ToastUtil.showToastShortCenter(ADActivity.
									 * this , "开始分享.");
									 */
								}

								@Override
								public void onComplete(SHARE_MEDIA arg0,
										int eCode, SocializeEntity arg2) {
									if (eCode == 200) {
										/*
										 * ToastUtil.showToastShortCenter(
										 * ADActivity.this, "分享成功.");
										 */
									} else {
										String eMsg = "";
										if (eCode == -101) {
											eMsg = "没有授权";
										}
										/*
										 * ToastUtil.showToastShortCenter(
										 * ADActivity.this, "分享失败[" + eCode +
										 * "] " + eMsg);
										 */
									}
								}
							});
				}
			}
		} else {
			ToastUtil.showToastShortCenter(
					PostSelectionDetailActivity.this,
					PostSelectionDetailActivity.this.getResources().getString(
							R.string.no_bitmap));
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	private AsyncHttpResponseHandler followUserHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("关注：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (FLAG == FOLLOW) {
						FLAG = 0;
						if (jobj.has("data") && !jobj.isNull("data")) {
							JSONObject jData = jobj.getJSONObject("data");
							if (jData.has("isFollow")
									&& !jData.isNull("isFollow")) {
								int isFollow = jData.getInt("isFollow");
								if (isFollow == 2) {// 未关注
									ToastUtil.showToastShortBottom(
											PostSelectionDetailActivity.this,
											"取消关注");
									btn_item_postselectionfragment_gz
											.setBackground(getResources()
													.getDrawable(
															R.drawable.btn_item_postselectionfragment_notgz));
								} else if (isFollow == 1) {// 已关注
									ToastUtil.showToastShortBottom(
											PostSelectionDetailActivity.this,
											"关注成功");
									btn_item_postselectionfragment_gz
											.setBackground(getResources()
													.getDrawable(
															R.drawable.btn_item_postselectionfragment_gz));
								}
								spUtil.saveInt(
										"PostSelectionfragmentAdapter_isFollow",
										isFollow);
							}
						}
					} else if (FLAG == FLOWER) {
						ToastUtil.showToastShortBottom(
								PostSelectionDetailActivity.this, "小花+1");
						FLAG = 0;
						if (rl_item_postselectionfragment_num.getVisibility() == View.GONE) {
							rl_item_postselectionfragment_num
									.setVisibility(View.VISIBLE);
						}
						ib_item_postselectionfragment_sh.setEnabled(false);
						ib_item_postselectionfragment_sh
								.setBackground(getResources()
										.getDrawable(
												R.drawable.ib_item_postselectionfragment_sh_select));
						flowerCount++;
						if (flowerCount > 99) {
							btn_item_postselectionfragment_num.setText("99+");
						} else {
							btn_item_postselectionfragment_num
									.setText(flowerCount + "");
						}
						flowerUsersList.add(0,
								new FlowerUsersBean(spUtil.getInt("userid", 0),
										spUtil.getString("userimage", "")));
						hlvItemPostselectionDetailAdapter
								.setData(flowerUsersList);
						spUtil.saveBoolean(
								"PostSelectionfragmentAdapter_flower", true);
					} else if (FLAG == SHIT) {
						ToastUtil.showToastShortBottom(
								PostSelectionDetailActivity.this, "biu~");
						FLAG = 0;
						ib_item_postselectionfragment_bb.setEnabled(false);
						ib_item_postselectionfragment_bb
								.setBackground(getResources()
										.getDrawable(
												R.drawable.ib_item_postselectionfragment_bb_select));
						spUtil.saveBoolean("PostSelectionfragmentAdapter_shit",
								true);
					} else if (FLAG == COMMENT) {
						iv_item_postselectionfragment_default
								.setVisibility(View.GONE);
						tv_item_postselectionfragment_default
								.setVisibility(View.GONE);
						ToastUtil.showToastShortBottom(
								PostSelectionDetailActivity.this, "发送成功");
						if (refFlag == 1) {
							refFlag = 0;
							commentsList.clear();
						}
						prl_postselection_detail.getRefreshableView()
								.setDividerHeight(2);
						FLAG = 0;
						SimpleDateFormat sDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd    hh:mm");
						String date = sDateFormat.format(new java.util.Date());
						String cellphone = spUtil.getString("cellphone", "");
						String username = spUtil.getString("username", "");
						String nc = "";
						if (username != null && !TextUtils.isEmpty(username)) {
							nc = username;
						} else if (cellphone != null
								&& !TextUtils.isEmpty(cellphone)) {
							nc = cellphone;
						}
						commentsList.add(new CommentsBean(date, content,
								new Random().nextInt(10000), new UserBean(nc,
										spUtil.getString("userimage", "")),
								contentType));
						mlvItemPostselectionDetailAdapter.setData(commentsList);
						mlvItemPostselectionDetailAdapter.setFlag(0);
						prl_postselection_detail.getRefreshableView()
								.setSelection(
										prl_postselection_detail
												.getRefreshableView()
												.getBottom());
						commentsCount = ++commentsCount;
						tv_item_postselectionfragment_qbpl
								.setVisibility(View.VISIBLE);
						Utils.setStringText(tv_item_postselectionfragment_qbpl,
								"全部评论(" + commentsCount + ")");
						spUtil.saveBoolean(
								"PostSelectionfragmentAdapter_comments", true);
					} else if (FLAG == DELETEPOST) {
						spUtil.saveInt("PostSelectionfragmentAdapter_postid",
								postId);
						ToastUtil.showToastShortBottom(
								PostSelectionDetailActivity.this, "删除成功");
						finish();
					}
					spUtil.saveInt("PostSelectionfragmentAdapter_position",
							position);
				}
			} catch (JSONException e) {

			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			ToastUtil.showToastShort(PostSelectionDetailActivity.this, "请求失败");
		}
	};

	/**************************
	 * 表情键盘软键盘切换相关 start
	 **************************************/
	private void closebroad() {
		if (isBqmmKeyboardVisible()) {
			hideBqmmKeyboard();
		} else if (isKeyboardVisible()) {
			hideSoftInput(bqmmEditView);
		}
	}

	private boolean isKeyboardVisible() {
		return (getDistanceFromInputToBottom() > DISTANCE_SLOP && !isBqmmKeyboardVisible())
				|| (getDistanceFromInputToBottom() > (bqmmKeyboard.getHeight() + DISTANCE_SLOP) && isBqmmKeyboardVisible());
	}

	private void showSoftInput(View view) {
		view.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, 0);
	}

	private void hideSoftInput(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * Activity在此方法中测量根布局的高度
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && mScreenHeight <= 0) {
			mMainContainer.getGlobalVisibleRect(tmp);
			mScreenHeight = tmp.bottom;
		}
	}

	private void showBqmmKeyboard() {
		bqmmKeyboard.showKeyboard();
	}

	private void hideBqmmKeyboard() {
		bqmmKeyboard.hideKeyboard();
	}

	private boolean isBqmmKeyboardVisible() {
		return bqmmKeyboard.isKeyboardVisible();
	}

	/**
	 * 输入框的下边距离屏幕的距离
	 */
	private int getDistanceFromInputToBottom() {
		return mScreenHeight - getInputBottom();
	}

	/**
	 * 输入框下边的位置
	 */
	private int getInputBottom() {
		inputbox.getGlobalVisibleRect(tmp);
		return tmp.bottom;
	}

	/**************************
	 * 表情键盘软键盘切换相关 end
	 **************************************/

	@Override
	protected void onDestroy() {
		// 关闭SDK
		bqmmsdk.destory();
		if (picOrVideo == video) {
			if (sfvv_item_postselectionfragment != null) {
				sfvv_item_postselectionfragment.release();
				sfvv_item_postselectionfragment = null;
			}
		}
		super.onDestroy();
	}

	/**
	 * 软键盘或者表情键盘打开时，按返回则关闭
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& (isBqmmKeyboardVisible() || isKeyboardVisible())) {
			closebroad();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 若软键盘或表情键盘弹起，点击上端空白处应该隐藏输入法键盘
	 */
	private View.OnTouchListener getOnTouchListener() {
		return new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 关闭键盘
				bqmmKeyboardOpen.setChecked(false);
				closebroad();
				return false;
			}
		};
	}
}
