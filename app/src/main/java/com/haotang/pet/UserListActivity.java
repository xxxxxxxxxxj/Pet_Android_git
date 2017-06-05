package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.UserListAdapter;
import com.haotang.pet.adapter.UserListAdapter.OnAdapterPlayListener;
import com.haotang.pet.entity.UserListBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;

/**
 * <p>
 * Title:UserListActivity
 * </p>
 * <p>
 * Description:关注，送花，粉丝列表
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-11-9 上午10:30:41
 */
public class UserListActivity extends SuperActivity implements OnClickListener {
	private Button bt_titlebar_other;
	private TextView tv_titlebar_title;
	private ImageButton ib_titlebar_back;
	private PullToRefreshListView prl_userlist;
	private ImageView iv_postuser_default;
	private TextView tv_postuser_default;
	private int page = 1;
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;
	private String flag;
	private UserListAdapter<UserListBean> userListAdapter;
	private List<UserListBean> userList = new ArrayList<UserListBean>();
	private List<UserListBean> tempList = new ArrayList<UserListBean>();
	private List<UserListBean> localList = new ArrayList<UserListBean>();
	private int postId;
	private int localPage = 1;
	private int index;
	private int pageSize;
	protected int adapterFlag;
	protected int positionnum;
	private int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initDta();
		initListener();
	}

	private void initView() {
		setContentView(R.layout.activity_user_list);
		prl_userlist = (PullToRefreshListView) findViewById(R.id.prl_userlist);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		iv_postuser_default = (ImageView) findViewById(R.id.iv_postuser_default);
		tv_postuser_default = (TextView) findViewById(R.id.tv_postuser_default);
	}

	@Override
	protected void onResume() {
		super.onResume();
		page = localPage;
		getData();
	}

	private void initDta() {
		Intent intent = getIntent();
		postId = intent.getIntExtra("postId", 0);
		userId = intent.getIntExtra("userId", 0);
		flag = intent.getStringExtra("flag");
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		bt_titlebar_other.setVisibility(View.GONE);
		if (flag != null && !TextUtils.isEmpty(flag)) {
			if (flag.equals("fans")) {
				tv_titlebar_title.setText("粉丝列表");
			} else if (flag.equals("flower")) {
				tv_titlebar_title.setText("送花列表");
			} else if (flag.equals("follow")) {
				tv_titlebar_title.setText("关注列表");
			}
		}
	}

	private void initListener() {
		ib_titlebar_back.setOnClickListener(this);
		prl_userlist.setMode(Mode.BOTH);
		prl_userlist
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
		userListAdapter = new UserListAdapter<UserListBean>(this, userList);
		prl_userlist.setAdapter(userListAdapter);
		userListAdapter.setOnAdapterPlayListener(new OnAdapterPlayListener() {
			@Override
			public void OnAdapterPlay(int flag, int position) {
				adapterFlag = flag;
				positionnum = position;
				if (flag == userListAdapter.NORMAL) {
					startActivityForResult(new Intent(UserListActivity.this,
							LoginActivity.class).putExtra("previous",
							Global.LOGIN_TO_USERLISTACTIVITY),
							Global.LOGIN_TO_USERLISTACTIVITY);
				} else if (flag == userListAdapter.REFRESH) {
					page = localPage;
					getData();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Global.LOGIN_TO_USERLISTACTIVITY) {
				page = localPage;
				getData();
			}
		}
	}

	private void getData() {
		pDialog.showDialog();
		if (flag != null && !TextUtils.isEmpty(flag)) {
			localPage = page;
			if (flag.equals("fans")) {
				CommUtil.getUserFansOrfollow(spUtil.getString("cellphone", ""),
						this, page, 1, userId, getUserDataHandler);
			} else if (flag.equals("flower")) {
				CommUtil.followList(spUtil.getString("cellphone", ""), this,
						page, postId, getUserDataHandler);
			} else if (flag.equals("follow")) {
				CommUtil.getUserFansOrfollow(spUtil.getString("cellphone", ""),
						this, page, 2, userId, getUserDataHandler);
			}
		}
	}

	private AsyncHttpResponseHandler getUserDataHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			prl_userlist.onRefreshComplete();
			pDialog.closeDialog();
			processData(new String(responseBody));
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			prl_userlist.onRefreshComplete();
			pDialog.closeDialog();
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
						if (jdata.has("pageSize") && !jdata.isNull("pageSize")) {
							pageSize = jdata.getInt("pageSize");
						}
						if (jdata.has("users") && !jdata.isNull("users")) {
							JSONArray jusers = jdata.getJSONArray("users");
							if (jusers.length() > 0) {
								iv_postuser_default.setVisibility(View.GONE);
								tv_postuser_default.setVisibility(View.GONE);
								userList.clear();
								localList.clear();
								for (int i = 0; i < jusers.length(); i++) {
									localList.add(UserListBean
											.json2Entity(jusers
													.getJSONObject(i)));
								}
								if (page == 1) {
									tempList.clear();
									tempList.addAll(localList);
									userList.addAll(tempList);
								} else {
									tempList.addAll(localList);
									if (index == page) {
										List<UserListBean> subList = tempList
												.subList(0, (page * pageSize)
														- pageSize);// 前面几条正确的数据
										subList.addAll(localList);
										userList.addAll(subList);
										tempList.clear();
										tempList.addAll(userList);
									} else {
										userList.addAll(tempList);
									}
								}
								index = page;
								userListAdapter.setData(userList);
								if (adapterFlag == userListAdapter.NORMAL
										|| adapterFlag == userListAdapter.REFRESH) {
									adapterFlag = 0;
									prl_userlist.getRefreshableView()
											.setSelection(positionnum);
									userListAdapter.notifyDataSetChanged();
								}
								if (localList.size() >= pageSize) {
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
			iv_postuser_default.setVisibility(View.VISIBLE);
			tv_postuser_default.setVisibility(View.VISIBLE);
			if (flag != null && !TextUtils.isEmpty(flag)) {
				if (flag.equals("fans")) {
					if (userId == spUtil.getInt("userid", 0)) {
						tv_postuser_default.setText("快去晒萌宠,粉丝们都在来的路上~");
					} else {
						tv_postuser_default.setText("TA还没有粉丝哟~");
					}
				} else if (flag.equals("flower")) {

				} else if (flag.equals("follow")) {
					if (userId == spUtil.getInt("userid", 0)) {
						tv_postuser_default.setText("这里空空如也,宠友们都在等你的关注~");
					} else {
						tv_postuser_default.setText("TA还没有关注任何人哟~");
					}
				}
			}
			userList.clear();
		} else {
			ToastUtil.showToastShortBottom(this, "没有更多内容了");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:// 返回
			finish();
			break;

		default:
			break;
		}
	}
}
