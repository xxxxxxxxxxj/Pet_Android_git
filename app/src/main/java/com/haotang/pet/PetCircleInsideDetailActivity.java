package com.haotang.pet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.adapter.PetInsideDetailAdapter;
import com.haotang.pet.entity.PetCircleInside;
import com.haotang.pet.entity.PetInsideDetail;
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
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.UnicodeToEmoji;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.wxpay.Util_WX;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.UserNameAlertDialog;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
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

@SuppressLint({ "RtlHardcoded", "NewApi" })
public class PetCircleInsideDetailActivity extends FragmentActivity implements OnClickListener{

	private Activity mContext;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private TextView tv_titlebar_other;
	private PullToRefreshListView listView_petcircle_inside_detail;
	private View header;
	private Button button_pet_inside_or;
	private ImageView imageview_show_ower_icon;
	private TextView textView_show_post_ower;
	private BQMMMessageText pet_circle_inside_content;
//	private TextView textview_show_post_num;
	private TextView show_post_time;
	private TextView textview_petinside_count;
	private LinearLayout layout_show_img;
	private PetInsideDetailAdapter detailAdapter;

	private ArrayList<PetInsideDetail> list = new ArrayList<PetInsideDetail>();
	private ArrayList<String> Imagelist = new ArrayList<String>();
	private ArrayList<Bitmap> imgBitMap = new ArrayList<Bitmap>();
	private ArrayList<ImageView> images = new ArrayList<ImageView>();
	private int postId;
	private int page=1;
	private boolean isJoinCircle = false;
//	private PetCircleInside inside;
	private ImageView imageview_tag;
	private boolean isFirst = true;
	private int userId;
	private BQMMEditView bqmmEditView;
	private ImageView image_one;
	private ImageView image_two;
	private ImageView image_thr;
	private ImageView image_four;
	private ImageView image_five;
	private ImageView image_six;
	private ImageView image_sev;
	private ImageView image_egh;
	private ImageView image_nin;
	
	private UmengShareUtils umengShareUtils;
	private PopupWindow pWin;
	protected String sharetitle;
	protected String sharetxt;
	protected String shareurl;
	private IWXAPI api;
	private String imgUrl;
	protected Bitmap bmp;
	private int id;
	private MProgressDialog mPDialog;
	private SharedPreferenceUtil spUtil;

	/**
	 * BQMM集成 键盘切换相关
	 */
	private boolean mPendingShowPlaceHolder;
	private View inputbox;
	private BQMM bqmmsdk;
	private BQMMKeyboard bqmmKeyboard;
	private BQMMSendButton bqmmSend;
	private CheckBox bqmmKeyboardOpen;
	private Rect tmp = new Rect();
	private int mScreenHeight;
	private View mMainContainer;
	private final int DISTANCE_SLOP = 180;
	private final String LAST_KEYBOARD_HEIGHT = "last_keyboard_height";
	private ArrayList<PetInsideDetail> listLast = new ArrayList<PetInsideDetail>();
	private boolean isEva = false;
	private LinearLayout show_no_data;
	private LinearLayout layout_delte;
	private TextView textview_delete;
	private TextView textview_from_eva;
	private String bigMessage="";
	private String smallMessage="";
	private boolean isToLogin = false;
	private boolean isTouch = false;
	private int index = 0;
	private double pageSize = 10;
	private int removePosition =-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petcircleinsidedetail);
		MApplication.listAppoint.add(this);
		mContext = this;
		mPDialog = new MProgressDialog(mContext);
		spUtil  =SharedPreferenceUtil.getInstance(mContext);
		api = WXAPIFactory.createWXAPI(mContext, Global.APP_ID);
		getIntentData();
		initView();
		initBQMM();
		setView();
		sharetitle = "宠物家";
		sharetxt = "宠物家";
		shareurl = "http://www.haotang365.com.cn/";
		imgUrl = "http://www.haotang365.com.cn/images/logo1.png";
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Global.RESULT_OK) {
			if (requestCode == Global.PETCIRCLEINSIDEDETAIL_TO_DIALOG_SHOW) {
				index = 1;
			}
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_PetCircle_Details);
		if (!Utils.checkLogin(mContext)) {
			isToLogin = false;
		}
		String userName = spUtil.getString("username", "");
		if (userName.equals("")||TextUtils.isEmpty(userName)) {
			isTouch = false;
		}
		if (index!=1) {
			page = 1;
			list.clear();
			listLast.clear();
			detailAdapter.notifyDataSetChanged();
			getData();
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
				Utils.mLogError("==-->111");//为什么会执行两次 
				String userName = spUtil.getString("username", "");
				if (!Utils.checkLogin(mContext)) {
					if (!isToLogin) {
						isToLogin = true;
						Intent intent = new Intent(mContext, LoginActivity.class);
						startActivity(intent);
					}
				}else {
					if (userName.equals("")||TextUtils.isEmpty(userName)) {
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
						mContext,s.toString(),
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
//				FLAG = COMMENT;
				JSONArray faceMessageData = BQMMMessageHelper
						.getFaceMessageData(face);
				bigMessage = faceMessageData.toString();
				if (Utils.checkLogin(mContext)) {
					mPDialog.showDialog();
					CommUtil.commentPost(2, spUtil.getString("cellphone", ""),
							mContext, postId,
							faceMessageData.toString(), commentPostHandler);
				}else {
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.putExtra("previous", Global.PRE_RECHARGEPAGE_ZF);
					startActivityForResult(intent, Global.PETCIRCLEINSIDEDETAIL_TO_LOGIN_BACK);
				}
				
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
				smallMessage = mixedMessageData.toString();
				if (Utils.checkLogin(mContext)) {
					mPDialog.showDialog();
					CommUtil.commentPost(1, spUtil.getString("cellphone", ""),
							mContext, postId,
							mixedMessageData.toString(), commentPostHandler);
				}else {
					Intent intent = new Intent(mContext, LoginActivity.class);
					intent.putExtra("previous", Global.PRE_RECHARGEPAGE_ZF);
					startActivityForResult(intent, Global.PETCIRCLEINSIDEDETAIL_TO_LOGIN_BACK);

				}
				
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
				// 除非软键盘和PlaceHolder全隐藏时直接显示PlaceHolder，其他情况此处处理软键盘，onPreDrawListener处理PlaceHolder
				if (!Utils.checkLogin(mContext)) {
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);
					return;
				}else {
					String userName = spUtil.getString("username", "");
					if (userName.equals("")||TextUtils.isEmpty(userName)) {
						tqDialog();
						return;
					}
				}
				if (isBqmmKeyboardVisible()) { // PlaceHolder -> Keyboard
					showSoftInput(bqmmEditView);
				} else if (isKeyboardVisible()) { // Keyboard -> PlaceHolder
					mPendingShowPlaceHolder = true;
					hideSoftInput(bqmmEditView);
				} else { // Just show PlaceHolder
					showBqmmKeyboard();
				}
			}
		});
	}
	private void getIntentData() {
		postId = getIntent().getIntExtra("postId", 0);
		id = postId;
		removePosition = getIntent().getIntExtra("ClickPosition", -1);
	}
	private void getData() {
		// TODO Auto-generated method stub
		mPDialog.showDialog();
		CommUtil.queryPostInfo(spUtil.getString("cellphone", ""), mContext, postId, page, queryPostInfo);
	}
	private AsyncHttpResponseHandler queryPostInfo = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->查询帖子详情："+new String(responseBody));
			mPDialog.closeDialog();
			listView_petcircle_inside_detail.onRefreshComplete();
			try {
				JSONObject objectTop = new JSONObject(new String(responseBody));
				int code = objectTop.getInt("code");
				if (code==0) {
					if (objectTop.has("data")&&!objectTop.isNull("data")) {
						JSONObject object = objectTop.getJSONObject("data");
						if (isFirst) {
							if (object.has("imgs") && !object.isNull("imgs")) {
								setImgs(object);
							}
						}
						if (object.has("created")&&!object.isNull("created")) {
							show_post_time.setText(object.getString("created"));
						}
						if (object.has("content")&&!object.isNull("content")) {
							try {
								sharetxt = object.getString("content");
								if (sharetxt.length()>140) {
									sharetxt = sharetxt.substring(0, 140);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							pet_circle_inside_content.setText(object.getString("content"));
						}
						layout_delte.setVisibility(View.GONE);
						if (object.has("userId")&&!object.isNull("userId")) {
							userId = object.getInt("userId");
						}
						boolean isViDelete = false;
						if (Utils.checkLogin(mContext)) {
							if (spUtil.getInt("userid", -1)==userId) {
								button_pet_inside_or.setVisibility(View.GONE);
//								layout_delte.setVisibility(View.VISIBLE);
								textview_delete.setVisibility(View.VISIBLE);
								isViDelete = true;
							}else {
//								layout_delte.setVisibility(View.GONE);
								textview_delete.setVisibility(View.GONE);
								isViDelete = false;
							}
						}
						if (object.has("title")&&!object.isNull("title")) {
							layout_delte.setVisibility(View.VISIBLE);
							String fromOrdereva = object.getString("title");
							textview_from_eva.setVisibility(View.VISIBLE);
							textview_from_eva.setText(fromOrdereva);
						}else {
							textview_from_eva.setVisibility(View.GONE);
							if (isViDelete) {
								layout_delte.setVisibility(View.VISIBLE);
							}else {
								layout_delte.setVisibility(View.GONE);
							}
						}
						int duty = -1;
						if (object.has("duty")&&!object.isNull("duty")) {
							duty = object.getInt("duty");
							if (duty==1||duty==2) {
								imageview_tag.setVisibility(View.VISIBLE);
							}else {
								imageview_tag.setVisibility(View.GONE);
							}
						}
						if (object.has("commentsCount")&&!object.isNull("commentsCount")) {
							textview_petinside_count.setText("全部评论("+object.getString("commentsCount")+")");
						}
						if (object.has("isMySelf")&&!object.isNull("isMySelf")) {
							int isMySelf = object.getInt("isMySelf");
							if (isMySelf==1) {
								button_pet_inside_or.setVisibility(View.GONE);
							}
						}
						if (object.has("isFollow")&&!object.isNull("isFollow")) {
							int isFollow = object.getInt("isFollow");
							if (isFollow==1) {
								isJoinCircle = true;
								//如果没加入执行点击然后已加入
								button_pet_inside_or.setText("已关注");
								button_pet_inside_or.setBackgroundResource(R.drawable.bg_button_huise_back);
							}
						}else {
							isJoinCircle = false;
							button_pet_inside_or.setText("+ 关注");
							button_pet_inside_or.setBackgroundResource(R.drawable.bg_button_appoiment_ok);
						}
						String memberIcon = null;
						if (object.has("postUser")&&!object.isNull("postUser")) {
							JSONObject objectPost = object.getJSONObject("postUser");
							if (objectPost.has("userName")&&!objectPost.isNull("userName")) {
								sharetitle = objectPost.getString("userName");
								textView_show_post_ower.setText(objectPost.getString("userName"));
							}
							if (objectPost.has("userMemberLevel")&&!objectPost.isNull("userMemberLevel")) {
								JSONObject userMemberLevel = objectPost.getJSONObject("userMemberLevel");
								if (userMemberLevel.has("memberIcon")&&!userMemberLevel.isNull("memberIcon")) {
									memberIcon  = userMemberLevel.getString("memberIcon");
									
								}
							}
							if (objectPost.has("avatar")&&!objectPost.isNull("avatar")) {
								String avatar = objectPost.getString("avatar");
								ImageLoaderUtil.setImage(imageview_show_ower_icon, avatar, R.drawable.icon_self);
							}
						}
						if (duty==1||duty==2) {
							imageview_tag.setVisibility(View.VISIBLE);
							imageview_tag.setImageResource(R.drawable.dz_jl_icon);
						}else {
							if (!TextUtils.isEmpty(memberIcon)) {
								imageview_tag.setVisibility(View.VISIBLE);
								if (memberIcon.startsWith("http")) {
									ImageLoaderUtil.setImage(imageview_tag, memberIcon, 0);
								}else {
									ImageLoaderUtil.setImage(imageview_tag, CommUtil.getServiceNobacklash()+memberIcon, 0);
								}
							}else {
								imageview_tag.setVisibility(View.GONE);
							}
						}
						if (object.has("shareUrl")&&!object.isNull("shareUrl")) {
							shareurl = object.getString("shareUrl");
						}
						if (object.has("pageSize")&&!object.isNull("pageSize")) {
							pageSize = object.getInt("pageSize");
						}
						if (object.has("comments")&&!object.isNull("comments")) {
							JSONArray arrayComm = object.getJSONArray("comments");
							Utils.mLogError("==-->arrayComm page 1 输出页数 "+page);
							if (arrayComm.length()>0) {
								listLast.clear();
								page++;
								Utils.mLogError("==-->arrayComm page 2 当前请求size > 0 页数增大 "+page);
								for (int i = 0; i < arrayComm.length(); i++) {
									PetInsideDetail insideDetail = new PetInsideDetail();
									JSONObject objectEveryComm = arrayComm.getJSONObject(i);
									if (objectEveryComm.has("user")&&!objectEveryComm.isNull("user")) {
										JSONObject objectUser = objectEveryComm.getJSONObject("user");
										if (objectUser.has("userName")&&!objectUser.isNull("userName")) {
											insideDetail.userName = objectUser.getString("userName");
										}
										if (objectUser.has("avatar")&&!objectUser.isNull("avatar")) {
											insideDetail.avatar = objectUser.getString("avatar");
										}
										if (objectUser.has("userMemberLevel")&&!objectUser.isNull("userMemberLevel")) {
											JSONObject userMemberLevel = objectUser.getJSONObject("userMemberLevel");
											if (userMemberLevel.has("memberIcon")&&!userMemberLevel.isNull("memberIcon")) {
												insideDetail.memberIcon = userMemberLevel.getString("memberIcon");
											}
										}
									}
									if (objectEveryComm.has("duty")&&!objectEveryComm.isNull("duty")) {
										insideDetail.duty = objectEveryComm.getInt("duty");
									}
									if (objectEveryComm.has("PostCommentId")&&!objectEveryComm.isNull("PostCommentId")) {
										insideDetail.PostCommentId = objectEveryComm.getInt("PostCommentId");
									}
									if (objectEveryComm.has("contentType")&&!objectEveryComm.isNull("contentType")) {
										insideDetail.contentType = objectEveryComm.getInt("contentType");
									}
									if (objectEveryComm.has("created")&&!objectEveryComm.isNull("created")) {
										insideDetail.created = objectEveryComm.getString("created");
									}
									if (objectEveryComm.has("content")&&!objectEveryComm.isNull("content")) {
										insideDetail.content = objectEveryComm.getString("content");
									}
									if (objectEveryComm.has("userId")&&!objectEveryComm.isNull("userId")) {
										insideDetail.userId = objectEveryComm.getInt("userId");
									}
									listLast.add(insideDetail);
									list.add(insideDetail);
								}
							}
						}
						if (list.size()<=0) {
							show_no_data.setVisibility(View.VISIBLE);
							textview_petinside_count.setVisibility(View.GONE);
							DataNull();
						}else {
							textview_petinside_count.setVisibility(View.VISIBLE);
							show_no_data.setVisibility(View.GONE);
							int j = -1;
							for (int i = 0; i < list.size(); i++) {
								if (list.get(i).userId==-1) {
									j = i;
								}
							}
							if (j!=-1) {
								list.remove(j);
							}
						}
						detailAdapter.notifyDataSetChanged();
						if (isEva) {
							try {
								listView_petcircle_inside_detail.getRefreshableView().setSelection(list.size()+1);
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
					listView_petcircle_inside_detail.onRefreshComplete();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		private void setImgs(JSONObject object) throws JSONException {
			isFirst = false;
			JSONArray arrayImgs = object.getJSONArray("imgs");
			if (arrayImgs.length()>0) {
				for (int j = 0; j < arrayImgs.length(); j++) {
					ImageView imageView = new ImageView(mContext);
					imageView.setTag(j);
					images.add(imageView);
					Imagelist.add(arrayImgs.getString(j));
				}
				switch (arrayImgs.length()) {
				case 1:
					image_one.setVisibility(View.VISIBLE);
					image_two.setVisibility(View.GONE);
					image_thr.setVisibility(View.GONE);
					image_four.setVisibility(View.GONE);
					image_five.setVisibility(View.GONE);
					image_six.setVisibility(View.GONE);
					image_sev.setVisibility(View.GONE);
					image_egh.setVisibility(View.GONE);
					image_nin.setVisibility(View.GONE);
					break;
				case 2:
					image_one.setVisibility(View.VISIBLE);
					image_two.setVisibility(View.VISIBLE);
					image_thr.setVisibility(View.GONE);
					image_four.setVisibility(View.GONE);
					image_five.setVisibility(View.GONE);
					image_six.setVisibility(View.GONE);
					image_sev.setVisibility(View.GONE);
					image_egh.setVisibility(View.GONE);
					image_nin.setVisibility(View.GONE);
					break;
				case 3:
					image_one.setVisibility(View.VISIBLE);
					image_two.setVisibility(View.VISIBLE);
					image_thr.setVisibility(View.VISIBLE);
					image_four.setVisibility(View.GONE);
					image_five.setVisibility(View.GONE);
					image_six.setVisibility(View.GONE);
					image_sev.setVisibility(View.GONE);
					image_egh.setVisibility(View.GONE);
					image_nin.setVisibility(View.GONE);
					break;
				case 4:
					image_one.setVisibility(View.VISIBLE);
					image_two.setVisibility(View.VISIBLE);
					image_thr.setVisibility(View.VISIBLE);
					image_four.setVisibility(View.VISIBLE);
					image_five.setVisibility(View.GONE);
					image_six.setVisibility(View.GONE);
					image_sev.setVisibility(View.GONE);
					image_egh.setVisibility(View.GONE);
					image_nin.setVisibility(View.GONE);
					break;
				case 5:
					image_one.setVisibility(View.VISIBLE);
					image_two.setVisibility(View.VISIBLE);
					image_thr.setVisibility(View.VISIBLE);
					image_four.setVisibility(View.VISIBLE);
					image_five.setVisibility(View.VISIBLE);
					image_six.setVisibility(View.GONE);
					image_sev.setVisibility(View.GONE);
					image_egh.setVisibility(View.GONE);
					image_nin.setVisibility(View.GONE);
					break;
				case 6:
					image_one.setVisibility(View.VISIBLE);
					image_two.setVisibility(View.VISIBLE);
					image_thr.setVisibility(View.VISIBLE);
					image_four.setVisibility(View.VISIBLE);
					image_five.setVisibility(View.VISIBLE);
					image_six.setVisibility(View.VISIBLE);
					image_sev.setVisibility(View.GONE);
					image_egh.setVisibility(View.GONE);
					image_nin.setVisibility(View.GONE);
					break;
				case 7:
					image_one.setVisibility(View.VISIBLE);
					image_two.setVisibility(View.VISIBLE);
					image_thr.setVisibility(View.VISIBLE);
					image_four.setVisibility(View.VISIBLE);
					image_five.setVisibility(View.VISIBLE);
					image_six.setVisibility(View.VISIBLE);
					image_sev.setVisibility(View.VISIBLE);
					image_egh.setVisibility(View.GONE);
					image_nin.setVisibility(View.GONE);
					break;
				case 8:
					image_one.setVisibility(View.VISIBLE);
					image_two.setVisibility(View.VISIBLE);
					image_thr.setVisibility(View.VISIBLE);
					image_four.setVisibility(View.VISIBLE);
					image_five.setVisibility(View.VISIBLE);
					image_six.setVisibility(View.VISIBLE);
					image_sev.setVisibility(View.VISIBLE);
					image_egh.setVisibility(View.VISIBLE);
					image_nin.setVisibility(View.GONE);
					break;
				case 9:
					image_one.setVisibility(View.VISIBLE);
					image_two.setVisibility(View.VISIBLE);
					image_thr.setVisibility(View.VISIBLE);
					image_four.setVisibility(View.VISIBLE);
					image_five.setVisibility(View.VISIBLE);
					image_six.setVisibility(View.VISIBLE);
					image_sev.setVisibility(View.VISIBLE);
					image_egh.setVisibility(View.VISIBLE);
					image_nin.setVisibility(View.VISIBLE);
					break;
				}
				for (int j = 0; j < arrayImgs.length(); j++) {
					switch (j) {
					case 0:
						imgUrl = arrayImgs.getString(j);
						showHttpImg(image_one, arrayImgs.getString(j));
						break;
					case 1:
						showHttpImg(image_two, arrayImgs.getString(j));
						break;
					case 2:
						showHttpImg(image_thr, arrayImgs.getString(j));
						break;
					case 3:
						showHttpImg(image_four, arrayImgs.getString(j));
						break;
					case 4:
						showHttpImg(image_five, arrayImgs.getString(j));
						break;
					case 5:
						showHttpImg(image_six, arrayImgs.getString(j));
						break;
					case 6:
						showHttpImg(image_sev, arrayImgs.getString(j));
						break;
					case 7:
						showHttpImg(image_egh, arrayImgs.getString(j));
						break;
					case 8:
						showHttpImg(image_nin, arrayImgs.getString(j));
						break;
					}
				}
			}
		}

		private void DataNull() {
			if (list.size()<=0) {
				PetInsideDetail insideDetail = new PetInsideDetail();
				insideDetail.userName="";
				insideDetail.avatar="";
				insideDetail.created="";
				insideDetail.content="";
				insideDetail.userId=-1;
				list.add(insideDetail);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			try {
				mPDialog.closeDialog();
				listView_petcircle_inside_detail.onRefreshComplete();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	};
	private void setView() {
		listView_petcircle_inside_detail.getRefreshableView().addHeaderView(header);
		detailAdapter = new PetInsideDetailAdapter<PetInsideDetail>(this, list);
		listView_petcircle_inside_detail.setMode(Mode.BOTH);
		listView_petcircle_inside_detail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
					// 下拉刷新
					page = 1;
					list.clear();
					listLast.clear();
					detailAdapter.notifyDataSetChanged();
					getData();
				} else {
					try {
						if (list.size()>0) {
							if (list.get(0).userId==-1) {
								list.clear();
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try {
						if (listLast.size()>0&&listLast.size()<pageSize) {
							page = page - 1;
							list.removeAll(listLast);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					getData();
				}
			}
		});
		listView_petcircle_inside_detail.setAdapter(detailAdapter);
		
//		getData();
	}
	private void initView() {
		listView_petcircle_inside_detail = (PullToRefreshListView) findViewById(R.id.listView_petcircle_inside_detail);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		tv_titlebar_other = (TextView) findViewById(R.id.tv_titlebar_other);
		bqmmSend = (BQMMSendButton) findViewById(R.id.button_petcircle_git_eva);
		bqmmEditView = (BQMMEditView) findViewById(R.id.editText_input_eva);
		bqmmKeyboard = (BQMMKeyboard) findViewById(R.id.chat_msg_input_box);
		bqmmKeyboardOpen = (CheckBox) findViewById(R.id.chatbox_open);
		inputbox = findViewById(R.id.layout_eva);
		mMainContainer = findViewById(R.id.main_container);
		
		header = LayoutInflater.from(mContext).inflate(R.layout.header_pet_circleinsidedetail, null);
		textview_delete = (TextView) header.findViewById(R.id.textview_delete);
		textview_from_eva = (TextView) header.findViewById(R.id.textview_from_eva);
		layout_delte = (LinearLayout) header.findViewById(R.id.layout_delte);
		show_no_data = (LinearLayout) header.findViewById(R.id.show_no_data);
		imageview_tag = (ImageView) header.findViewById(R.id.imageview_tag);
		imageview_show_ower_icon = (ImageView) header.findViewById(R.id.imageview_show_ower_icon);
		pet_circle_inside_content = (BQMMMessageText) header.findViewById(R.id.pet_circle_inside_content);
		textView_show_post_ower = (TextView) header.findViewById(R.id.textView_show_post_ower);
		show_post_time = (TextView) header.findViewById(R.id.show_post_time);
		layout_show_img = (LinearLayout) header.findViewById(R.id.layout_show_img);
		textview_petinside_count = (TextView) header.findViewById(R.id.textview_petinside_count);
		button_pet_inside_or = (Button) header.findViewById(R.id.button_pet_inside_or);
		image_one = (ImageView) header.findViewById(R.id.image_one);
		image_two = (ImageView) header.findViewById(R.id.image_two);
		image_thr = (ImageView) header.findViewById(R.id.image_thr);
		image_four = (ImageView) header.findViewById(R.id.image_four);
		image_five = (ImageView) header.findViewById(R.id.image_five);
		image_six = (ImageView) header.findViewById(R.id.image_six);
		image_sev = (ImageView) header.findViewById(R.id.image_sev);
		image_egh = (ImageView) header.findViewById(R.id.image_egh);
		image_nin = (ImageView) header.findViewById(R.id.image_nin);
		
		
		tv_titlebar_other.setVisibility(View.VISIBLE);
		tv_titlebar_title.setText("详情");
		
		
		ib_titlebar_back.setOnClickListener(this);
		bqmmSend.setOnClickListener(this);
		button_pet_inside_or.setOnClickListener(this);
		imageview_show_ower_icon.setOnClickListener(this);
		textview_delete.setOnClickListener(this);
//		ib_titlebar_other.setBackgroundResource(R.drawable.ad_share);
		tv_titlebar_other.setText("分享");
		tv_titlebar_other.setTextColor(Color.parseColor("#BB996C"));
		tv_titlebar_other.setTextSize(16);
		tv_titlebar_other.setOnClickListener(this);
		image_one.setOnClickListener(this);
		image_two.setOnClickListener(this);
		image_thr.setOnClickListener(this);
		image_four.setOnClickListener(this);
		image_five.setOnClickListener(this);
		image_six.setOnClickListener(this);
		image_sev.setOnClickListener(this);
		image_egh.setOnClickListener(this);
		image_nin.setOnClickListener(this);
		
//		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) ib_titlebar_other.getLayoutParams();
//		params.height=Utils.dip2px(mContext, 18);
//		params.width = Utils.dip2px(mContext, 17);
//		params.rightMargin =Utils.dip2px(mContext, 10);
//		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		ib_titlebar_other.setLayoutParams(params);
	}
	
	
	class MyTask extends AsyncTask<String, Void, byte[]> {
		private ImageView vi;
		private int j;
		private MyTask(ImageView vi,int j) {
			this.vi = vi;
			this.j=j;
			try {
				mPDialog.showDialog();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected byte[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			BufferedInputStream bis = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				URL url = new URL(params[0]);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.connect();
				if (connection.getResponseCode() == 200) {
					bis = new BufferedInputStream(connection.getInputStream());
					byte[] buffer = new byte[1024 * 8];
					int c = 0;
					while ((c = bis.read(buffer)) != -1) {
						baos.write(buffer, 0, c);
						baos.flush();
					}
					return baos.toByteArray();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(byte[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			setLayOutAddView(result);
		}

		private void setLayOutAddView(byte[] result) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0,
					result.length);
			double maxSize =100.00;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte [] b = baos.toByteArray();
			 //将字节换成KB
            double mid = b.length/1024;
            //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
            if (mid > maxSize) {
	            //获取bitmap大小 是允许最大大小的多少倍
	            double i = mid / maxSize;
	            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
	            bitmap = zoomImage(bitmap, bitmap.getWidth() / Math.sqrt(i),
	            		bitmap.getHeight() / Math.sqrt(i));
            }
//			Bitmap bitmap2 = BitmapFactory.decodeByteArray(b, 0, b.length);
			imgBitMap.add(bitmap);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			params.width = Utils.getDisplayMetrics((Activity)mContext)[0];
			params.topMargin = Utils.dip2px(mContext, 10);
			int imgWidth = 0;
			int imgHeight = 0;
			if (bitmap != null) {
				imgWidth = bitmap.getWidth();
				imgHeight = bitmap.getHeight();
				params.height = ((params.width * imgHeight) / imgWidth);
				params.topMargin = 20;
				params.bottomMargin = 20;
				vi.setPadding(10, 10, 10, 10);
				vi.setLayoutParams(params);
				vi.setScaleType(ScaleType.FIT_XY);
				vi.setImageBitmap(bitmap);
//				layout_show_img.mAddViewInlayout(vi);
//				if (j==images.size()-1) {
//					layout_show_img.requestLayout();
//					layout_show_img.invalidate();
//					try {
//						mPDialog.closeDialog();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
			}
		}
	}
	private AsyncHttpResponseHandler commentPostHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->提交评价："+new String(responseBody));
			
			mPDialog.closeDialog();
			isEva = true;
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					bqmmEditView.setText("");
					InputMethodManager imm = (InputMethodManager)getSystemService(mContext.INPUT_METHOD_SERVICE);  
					boolean isOpen=imm.isActive(); 
					if (isOpen) {
						imm.hideSoftInputFromWindow(PetCircleInsideDetailActivity.this.getCurrentFocus().getWindowToken(),  
						InputMethodManager.HIDE_NOT_ALWAYS);
					}
					try {
						PetCircleInsideActivity.EvaCountUp(removePosition);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (listLast.size()>0&&listLast.size()<pageSize) {
						page = page - 1;
						list.removeAll(listLast);
						Utils.mLogError("==-->arrayComm page 3 当前页没够十条 重新请求当前页" + page);
					}
//					detailAdapter.notifyDataSetChanged();
					getData();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				mPDialog.closeDialog();
			}
			
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			mPDialog.closeDialog();
			
		}
	};

	private AsyncHttpResponseHandler followUserHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->关注："+new String(responseBody));
			mPDialog.closeDialog();
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0 ) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("isFollow")&&!objectData.isNull("isFollow")) {
							int isFollow = objectData.getInt("isFollow");
							if (isFollow==1) { //关注
								ToastUtil.showToastShortCenter(mContext, getResources().getString(R.string.success_guanzhu));
								isJoinCircle = true;
								button_pet_inside_or.setText("已关注");
								button_pet_inside_or.setTextColor(getResources().getColor(R.color.aBBBBBB));
								button_pet_inside_or.setBackgroundResource(R.drawable.bg_button_huise_back);
							}else if (isFollow==2) { //取消关注
								ToastUtil.showToastShortCenter(mContext, getResources().getString(R.string.success_noguanzhu));
								isJoinCircle = false;
								button_pet_inside_or.setText("+ 关注");
								button_pet_inside_or.setTextColor(getResources().getColor(R.color.aD1494F));
								button_pet_inside_or.setBackgroundResource(R.drawable.bg_button_appoiment_ok);
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				mPDialog.closeDialog();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			mPDialog.closeDialog();
		}
	
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String[] arrs  = Imagelist.toArray(new String[Imagelist.size()]);
		Intent intent=null;
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finish();
			break;
//		case R.id.button_petcircle_git_eva:
//			//提交评价然后刷新数据
////			ToastUtil.showToastShortCenter(mContext, "提交评价然后刷新数据");
//			mPDialog.showDialog();
//			if (!TextUtils.isEmpty(bqmmEditView.getText())) {
//				CommUtil.commentPost(1,spUtil.getString("cellphone", ""), mContext,postId, bqmmEditView.getText().toString(), commentPostHandler);
//			}else {
//				ToastUtil.showToastShortCenter(mContext, "请填写评价~");
//			}
//			break;
		case R.id.button_pet_inside_or:
			if (Utils.checkLogin(mContext)) {
				followUser();//关注/取消关注 用户
			}else {
				intent = new Intent(mContext, LoginActivity.class);
				mContext.startActivity(intent);
				closebroad();
			}
			break;
		case R.id.tv_titlebar_other:
			mPDialog.showDialog();
			new Thread(networkTask).start();
			break;
		case R.id.imageview_show_ower_icon:
			if (Utils.checkLogin(mContext)) {
				goToNext(PostUserInfoActivity.class,userId,0, "");
			}else {
				intent = new Intent(mContext, LoginActivity.class);
				mContext.startActivity(intent);
				closebroad();
			}
			break;
		case R.id.textview_delete:
			setDialog(removePosition);
			break;
			
		case R.id.image_one:
			goImageDetail(0,arrs);
			break;
		case R.id.image_two:
			goImageDetail(1,arrs);
			break;
		case R.id.image_thr:
			goImageDetail(2,arrs);
			break;
		case R.id.image_four:
			goImageDetail(3,arrs);
			break;
		case R.id.image_five:
			goImageDetail(4,arrs);
			break;
		case R.id.image_six:
			goImageDetail(5,arrs);
			break;
		case R.id.image_sev:
			goImageDetail(6,arrs);
			break;
		case R.id.image_egh:
			goImageDetail(7,arrs);
			break;
		case R.id.image_nin:
			goImageDetail(8,arrs);
			break;
		}
	}
	private void goImageDetail(int position,String[] urls) {
//		Intent intent;
//		intent = new Intent(mContext, PetCirClePostImageActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putInt("indexTag", 1);
//		bundle.putInt("position", pos);
//		bundle.putStringArrayList("list",Imagelist);
//		intent.putExtras(bundle);
//		mContext.startActivityForResult(intent, Global.PETCIRCLELIST_TO_IMAGE);
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

	private void setDialog(final int position) {
		new AlertDialogNavAndPost(mContext).builder().setTitle("")
				.setMsg("确定删除此条动态吗")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						mPDialog.showDialog();
						CommUtil.deletePost(spUtil.getString("cellphone", "0"), mContext, id, deletePost);
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					}
				}).show();
	}
	private void followUser() {
		mPDialog.showDialog();
		CommUtil.followUser(spUtil.getString("cellphone", ""), mContext, userId, followUserHandler);
	}
	private void goToNext(Class clazz, int userId,int previous, String flag) {
		Intent intent = new Intent(mContext, clazz);
		intent.putExtra("userId", userId);
		intent.putExtra("postId", id);
		intent.putExtra("flag", flag);
		intent.putExtra("previous", previous);
		startActivity(intent);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			for (int i = 0; i < imgBitMap.size(); i++) {
				imgBitMap.get(i).recycle();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

/***
         * 图片的缩放方法
         * 
         * @param bgimage
         *            ：源图片资源
         * @param newWidth
         *            ：缩放后宽度
         * @param newHeight
         *            ：缩放后高度
         * @return
         */
        public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                        double newHeight) {
                // 获取这个图片的宽和高
                float width = bgimage.getWidth();
                float height = bgimage.getHeight();
                // 创建操作图片用的matrix对象
                Matrix matrix = new Matrix();
                // 计算宽高缩放率
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                // 缩放图片动作
                matrix.postScale(scaleWidth, scaleHeight);
                Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                                (int) height, matrix, true);
                return bitmap;
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
    		mPDialog.closeDialog();
    		pWin = null;
    		if (pWin == null) {
    			final View view = LayoutInflater.from(mContext).inflate(R.layout.sharedialog, null);
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
    			pWin.setWidth(DeviceUtils.getScreenWidth(mContext));
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
    		boolean weixinAvilible = Utils.isWeixinAvilible(mContext);
    		if (shareurl.contains("?")) {
    			shareurl = shareurl + "&postId=" + id;
    		} else {
    			shareurl = shareurl + "?postId=" + id;
    		}
    		if (bmp != null && sharetxt != null && !TextUtils.isEmpty(sharetxt)
    				&& sharetitle != null && !TextUtils.isEmpty(sharetitle)
    				&& shareurl != null && !TextUtils.isEmpty(shareurl)) {
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
    					ToastUtil.showToastShortBottom(mContext, "微信不可用");
    				}
    			} else if (type == 3) {// qq
    				umengShareUtils = new UmengShareUtils(this, sharetxt,shareurl, sharetitle, imgUrl);
    				umengShareUtils.mController.getConfig().closeToast();
    				umengShareUtils.mController.postShare(mContext, SHARE_MEDIA.QQ,
    						new SnsPostListener() {
    							@Override
    							public void onStart() {
    								
    								/* ToastUtil.showToastShortCenter(mContext
    								 , "开始分享.");*/
    								 
    							}

    							@Override
    							public void onComplete(SHARE_MEDIA arg0, int eCode,
    									SocializeEntity arg2) {
    								if (eCode == 200) {
    									
    									 ToastUtil.showToastShortCenter(mContext, getResources().getString(R.string.share_success));
    									 
    								} else {
    									String eMsg = "";
    									if (eCode == -101) {
    										eMsg = "没有授权";
    									}
    									
    									 /*ToastUtil.showToastShortCenter(
    											 mContext, "分享失败[" + eCode + "] " +
    									 eMsg);*/
    									 
    								}
    							}
    						});
    			} else if (type == 4) {// 新浪微博
    				umengShareUtils = new UmengShareUtils(this, sharetxt,shareurl, sharetitle, imgUrl);
    				umengShareUtils.mController.getConfig().closeToast();
    				boolean isSina = OauthHelper.isAuthenticated(mContext,
    						SHARE_MEDIA.SINA);
    				// 如果未授权则授权
    				if (!isSina) {
    					umengShareUtils.mController.doOauthVerify(mContext,
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
    											mContext, SHARE_MEDIA.SINA,
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
    				    									 ToastUtil.showToastShortCenter(mContext, getResources().getString(R.string.share_success));
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
    					umengShareUtils.mController.postShare(mContext,
    							SHARE_MEDIA.SINA, new SnsPostListener() {
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
       									 ToastUtil.showToastShortCenter(mContext, getResources().getString(R.string.share_success));
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
    			ToastUtil.showToastShortCenter(mContext, mContext.getResources()
    					.getString(R.string.no_bitmap));
    		}
    	}

    	private String buildTransaction(final String type) {
    		return (type == null) ? String.valueOf(System.currentTimeMillis())
    				: type + System.currentTimeMillis();
    	}
    	
    	
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
    	
    	
    	private void showHttpImg(ImageView img,String mPath) {
    		try {
				ImageLoaderUtil.instance.displayImage(mPath, img, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View view) {
						ImageView iv = (ImageView) view;
						iv.setImageResource(0);
					}
					
					@Override
					public void onLoadingFailed(String arg0, View view, FailReason arg2) {
						// TODO Auto-generated method stub
						ImageView iv = (ImageView) view;
						iv.setImageResource(R.drawable.icon_production_default);
					}
					
					@Override
					public void onLoadingComplete(String path, View view,
							Bitmap bitmap) {
						try {
							if (mPDialog.isShowing()) {
								mPDialog.closeDialog();
							}
							mPDialog.closeDialog();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (view != null && bitmap != null) {
//    					mBitmap = bitmap;
							ImageView iv = (ImageView) view;
							//去掉宽高设置，自适应
							int imgWidth = bitmap.getWidth();
//							if (imgWidth>Utils.getDisplayMetrics(mContext)[0]) {
								int imgHeight = bitmap.getHeight();
								LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
								params.width = Utils.getDisplayMetrics(mContext)[0];
								params.topMargin = Utils.dip2px(mContext, 10);
								params.height = ((params.width * imgHeight) / imgWidth);
								params.gravity=Gravity.LEFT;
								iv.setLayoutParams(params);
								iv.setScaleType(ScaleType.FIT_XY);
//							}
							iv.setImageBitmap(bitmap);
						}
					}

					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	private void tqDialog() {
    		Intent intent = new Intent(mContext, LanShowActivity.class);
    		startActivityForResult(intent, Global.PETCIRCLEINSIDEDETAIL_TO_DIALOG_SHOW);
    	}
    	
    	private AsyncHttpResponseHandler deletePost = new AsyncHttpResponseHandler() {
    		
    		@Override
    		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
    			// TODO Auto-generated method stub
    			Utils.mLogError("==-->删除帖子: = "+new String(responseBody));
    			mPDialog.closeDialog();
    			try {
    				JSONObject object = new JSONObject(new String(responseBody));
    				int code = object.getInt("code");
    				if (code == 0) {
//    					Intent intent = new Intent();
//    					intent.setAction("android.intent.action.PetCircleInsideActivity");
//    					intent.putExtra("position", removePosition);
//    					intent.putExtra("indexTag", 1);
//    					mContext.sendBroadcast(intent);
    					try {
							PetCircleInsideActivity.deltePost(removePosition);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					finish();
    				}else {
    					if (object.has("msg")&&!object.isNull("msg")) {
    						ToastUtil.showToastShortCenter(mContext, object.getString("msg"));
    					}
    				}
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    		
    		@Override
    		public void onFailure(int statusCode, Header[] headers,
    				byte[] responseBody, Throwable error) {
    			// TODO Auto-generated method stub
    			
    		}
    	};
}
