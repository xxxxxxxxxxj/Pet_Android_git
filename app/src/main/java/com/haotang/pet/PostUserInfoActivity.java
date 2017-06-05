package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.PostUserInfoAdapter;
import com.haotang.pet.entity.PostUserInfoBean;
import com.haotang.pet.entity.PostUserInfoBean.PostsBean;
import com.haotang.pet.entity.PostUserInfoBean.UserMemberLevel;
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
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.UserNameAlertDialog;
import com.loveplusplus.demo.image.ImagePagerActivity;

/**
 * <p>
 * Title:PostUserInfoActivity
 * </p>
 * <p>
 * Description:个人主页
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-18 下午5:34:52
 */
@SuppressLint("NewApi")
public class PostUserInfoActivity extends SuperActivity implements
		OnClickListener {
	private ImageView ib_postuser_info_back;
	private TextView tv_postuser_info_username;
	private SelectableRoundedImageView sriv_postuser_info_userimg;
	private ImageView iv_postuser_info_level;
	private Button btn_postuser_info_gz;
	private TextView tv_postuser_info_fs;
	private TextView tv_postuser_info_gz;
	private TextView tv_postuser_info_tz;
	private PullToRefreshListView prl_postuser_info;
	private int page = 1;
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;
	private PostUserInfoAdapter postUserInfoAdapter;
	private List<PostsBean> postList = new ArrayList<PostsBean>();
	private int userId;
	private RelativeLayout relativeLayout3;
	private int isMyself;
	private String[] imgUrls = new String[1];
	private String avatar;
	private ImageView iv_postuser_default;
	private TextView tv_postuser_default;
	private int position;
	private List<PostsBean> tempList = new ArrayList<PostsBean>();
	private int index;
	private LinearLayout ll_postuser_info_fans;
	private LinearLayout ll_postuser_info_follow;
	private int localPage = 1;
	private int fansAmount;
	private int followAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDta();
		initView();
		initListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		page = localPage;
		getData();
	}

	private void initDta() {
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		userId = intent.getIntExtra("userId", 0);
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
	}

	private void initListener() {
		ll_postuser_info_fans.setOnClickListener(this);
		ll_postuser_info_follow.setOnClickListener(this);
		ib_postuser_info_back.setOnClickListener(this);
		btn_postuser_info_gz.setOnClickListener(this);
		relativeLayout3.setOnClickListener(this);
		prl_postuser_info.setMode(Mode.BOTH);
		prl_postuser_info
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
		postUserInfoAdapter = new PostUserInfoAdapter<PostsBean>(this, postList);
		prl_postuser_info.setAdapter(postUserInfoAdapter);
	}

	private void getData() {
		pDialog.showDialog();
		localPage = page;
		CommUtil.getUserData(spUtil.getString("cellphone", ""), this, page,
				userId, getUserDataHandler);
	}

	private AsyncHttpResponseHandler getUserDataHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			prl_postuser_info.onRefreshComplete();
			pDialog.closeDialog();
			processData(new String(responseBody));
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			prl_postuser_info.onRefreshComplete();
			pDialog.closeDialog();
			ToastUtil.showToastShort(PostUserInfoActivity.this, "请求失败");
		}
	};

	private void processData(String result) {
		try {
			JSONObject jobj = new JSONObject(result);
			if (jobj.has("code") && !jobj.isNull("code")) {
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						Gson gson = new Gson();
						PostUserInfoBean data = gson.fromJson(jdata.toString(),
								PostUserInfoBean.class);
						if (data != null) {
							String userName = data.getUserName();
							avatar = data.getAvatar();
							fansAmount = data.getFansAmount();
							followAmount = data.getFollowAmount();
							int postAmount = data.getPostAmount();
							isMyself = data.getIsMyself();
							int isFollowed = data.getIsFollow();
							List<com.haotang.pet.entity.PostUserInfoBean.PostsBean> posts = data
									.getPosts();
							int duty = data.getDuty();
							int pageSize = data.getPageSize();
							UserMemberLevel userMemberLevel = data
									.getUserMemberLevel();
							if (duty == 1 || duty == 2) {
								iv_postuser_info_level
										.setBackgroundResource(R.drawable.iv_item_postselectionfragment_level);
								iv_postuser_info_level
										.setVisibility(View.VISIBLE);
							} else {
								if (userMemberLevel != null) {
									String memberIcon = userMemberLevel
											.getMemberIcon();
									if (memberIcon != null
											&& !TextUtils.isEmpty(memberIcon)) {
										ImageLoaderUtil.loadImg(memberIcon,
												iv_postuser_info_level,
												R.drawable.icon_self, null);
										iv_postuser_info_level
												.setVisibility(View.VISIBLE);
									} else {
										iv_postuser_info_level
												.setVisibility(View.GONE);
									}
								} else {
									iv_postuser_info_level
											.setVisibility(View.GONE);
								}
							}
							if (isMyself == 1) {
								btn_postuser_info_gz
										.setBackground(getResources()
												.getDrawable(
														R.drawable.img_postuser_info_bjzl));
							} else {
								if (isFollowed == 2) {// 未关注
									btn_postuser_info_gz
											.setBackground(getResources()
													.getDrawable(
															R.drawable.img_postuser_info_wgz));
								} else if (isFollowed == 1) {// 已关注
									btn_postuser_info_gz
											.setBackground(getResources()
													.getDrawable(
															R.drawable.img_postuser_info_ygz));
								}
							}
							Utils.setStringText(tv_postuser_info_username,
									userName);
							Utils.setStringText(tv_postuser_info_fs, fansAmount
									+ "");
							Utils.setStringText(tv_postuser_info_gz,
									followAmount + "");
							Utils.setStringText(tv_postuser_info_tz, postAmount
									+ "");
							ImageLoaderUtil.loadImg(avatar,
									sriv_postuser_info_userimg,
									R.drawable.icon_self, null);
							// 帖子列表
							if (posts != null && posts.size() > 0) {
								postList.clear();
								prl_postuser_info.setVisibility(View.VISIBLE);
								iv_postuser_default.setVisibility(View.GONE);
								tv_postuser_default.setVisibility(View.GONE);
								if (page == 1) {
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
								postUserInfoAdapter.setData(postList);
								if (posts.size() >= pageSize) {
									page = ++page;
								}
							} else {
								if (page == 1) {
									postList.clear();
									prl_postuser_info.setVisibility(View.GONE);
									iv_postuser_default
											.setVisibility(View.VISIBLE);
									tv_postuser_default
											.setVisibility(View.VISIBLE);
									if (isMyself == 1) {
										tv_postuser_default
												.setText("别让这里空着哟,晒晒萌宠小花到手");
									} else {
										tv_postuser_default
												.setText("TA还没有发布动态哦~");
									}
								} else {
									ToastUtil.showToastShortBottom(this,
											"没有更多帖子了");
								}
								postUserInfoAdapter.setData(postList);
							}
						}
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		setContentView(R.layout.activity_post_user_info);
		ib_postuser_info_back = (ImageView) findViewById(R.id.ib_postuser_info_back);
		tv_postuser_info_username = (TextView) findViewById(R.id.tv_postuser_info_username);
		sriv_postuser_info_userimg = (SelectableRoundedImageView) findViewById(R.id.sriv_postuser_info_userimg);
		iv_postuser_info_level = (ImageView) findViewById(R.id.iv_postuser_info_level);
		btn_postuser_info_gz = (Button) findViewById(R.id.btn_postuser_info_gz);
		tv_postuser_info_fs = (TextView) findViewById(R.id.tv_postuser_info_fs);
		tv_postuser_info_gz = (TextView) findViewById(R.id.tv_postuser_info_gz);
		tv_postuser_info_tz = (TextView) findViewById(R.id.tv_postuser_info_tz);
		prl_postuser_info = (PullToRefreshListView) findViewById(R.id.prl_postuser_info);
		relativeLayout3 = (RelativeLayout) findViewById(R.id.relativeLayout3);
		iv_postuser_default = (ImageView) findViewById(R.id.iv_postuser_default);
		tv_postuser_default = (TextView) findViewById(R.id.tv_postuser_default);
		ll_postuser_info_fans = (LinearLayout) findViewById(R.id.ll_postuser_info_fans);
		ll_postuser_info_follow = (LinearLayout) findViewById(R.id.ll_postuser_info_follow);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_postuser_info_fans:
			if (spUtil.getString("cellphone", "") != null
					&& !TextUtils.isEmpty(spUtil.getString("cellphone", ""))) {
				startActivity(new Intent(this, UserListActivity.class)
						.putExtra("flag", "fans").putExtra("userId", userId));
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.ll_postuser_info_follow:
			if (spUtil.getString("cellphone", "") != null
					&& !TextUtils.isEmpty(spUtil.getString("cellphone", ""))) {
				startActivity(new Intent(this, UserListActivity.class)
						.putExtra("flag", "follow").putExtra("userId", userId));
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.relativeLayout3:
			if (avatar != null && !TextUtils.isEmpty(avatar)) {
				imgUrls[0] = avatar;
				imageBrower(0, imgUrls);
			}
			break;
		case R.id.ib_postuser_info_back:
			finish();
			break;
		case R.id.btn_postuser_info_gz:
			if (spUtil.getString("cellphone", "") != null
					&& !TextUtils.isEmpty(spUtil.getString("cellphone", ""))) {
				if (isMyself == 1) {
					startActivity(new Intent(this, ChangeAccountActivity.class));
				} else {
					String username = spUtil.getString("username", "");
					if (username != null && !TextUtils.isEmpty(username)) {
						CommUtil.followUser(spUtil.getString("cellphone", ""),
								this, userId, followUserHandler);
					} else {
						tqDialog();
					}
				}
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		default:
			break;
		}
	}

	private void tqDialog() {
		new UserNameAlertDialog(mContext).builder().setTitle("没昵称我  \"蓝瘦\"")
				.setTextViewHint("请填写昵称").setCloseButton(new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).setComplaintsButton("保	 存", new OnClickListener() {
					@Override
					public void onClick(View v) {
						CommUtil.updateUser(spUtil.getString("cellphone", "0"),
								Global.getIMEI(mContext), mContext,
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
					Toast.makeText(mContext, "创建成功", Toast.LENGTH_SHORT).show();
					spUtil.saveString("username",
							UserNameAlertDialog.getUserName());
				} else {
					String msg = jsonObject.getString("msg");
					Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
		}
	};

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	private AsyncHttpResponseHandler followUserHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("关注：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						if (jData.has("isFollow") && !jData.isNull("isFollow")) {
							int isFollow = jData.getInt("isFollow");
							if (isFollow == 2) {// 未关注
								ToastUtil.showToastShortBottom(
										PostUserInfoActivity.this, "取消关注");
								btn_postuser_info_gz
										.setBackground(getResources()
												.getDrawable(
														R.drawable.img_postuser_info_wgz));
							} else if (isFollow == 1) {// 已关注
								ToastUtil.showToastShortBottom(
										PostUserInfoActivity.this, "关注成功");
								btn_postuser_info_gz
										.setBackground(getResources()
												.getDrawable(
														R.drawable.img_postuser_info_ygz));
							}
							spUtil.saveInt(
									"PostSelectionfragmentAdapter_isFollow",
									isFollow);
							spUtil.saveInt(
									"PostSelectionfragmentAdapter_position",
									position);
						}
					}
				}
			} catch (JSONException e) {

			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
	};
}
