package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BeauticianAdapter;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ClearEditText;

/**
 * <p>
 * Title:SerchBeauActivity
 * </p>
 * <p>
 * Description:搜索美容师洁面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-1 上午11:48:20
 */
@SuppressLint("NewApi")
public class SerchBeauActivity extends SuperActivity implements
		OnClickListener, OnLayoutChangeListener {
	private ClearEditText editText_serch_beau;
	private TextView tv_serch_beau_qx;
	private PullToRefreshListView prl_serch_beau_list;
	private int page = 1;
	private int pageSize = 10;
	private ArrayList<Beautician> beauList = new ArrayList<Beautician>();
	private BeauticianAdapter adapter;
	private SharedPreferenceUtil spUtil;
	private int areaNameid;
	private String realName;
	private RelativeLayout rlNull;
	private TextView tv_null_msg1;
	private int num;
	private RelativeLayout rl_serch_beau_vis;
	private View vw_serchbeau_mb;
	// Activity最外层的Layout视图
	private LinearLayout ll_serchbeau_root;
	// 屏幕高度
	private int screenHeight = 0;
	// 软件盘弹起后所占高度阀值
	private int keyHeight = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Utils.mLogError("==-->onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serch_beau);
		MApplication.listAppoint.add(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		areaNameid = spUtil.getInt("tareaid", 0);
		editText_serch_beau = (ClearEditText) findViewById(R.id.editText_serch_beau);
		tv_serch_beau_qx = (TextView) findViewById(R.id.tv_serch_beau_qx);
		prl_serch_beau_list = (PullToRefreshListView) findViewById(R.id.prl_serch_beau_list);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tv_null_msg1 = (TextView) findViewById(R.id.tv_null_msg1);
		ll_serchbeau_root = (LinearLayout) findViewById(R.id.ll_serchbeau_root);
		rl_serch_beau_vis = (RelativeLayout) findViewById(R.id.rl_serch_beau_vis);
		vw_serchbeau_mb = (View) findViewById(R.id.vw_serchbeau_mb);
		vw_serchbeau_mb.bringToFront();
		// 获取屏幕高度
		screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		// 阀值设置为屏幕高度的1/3
		keyHeight = screenHeight / 3;
		// 添加layout大小发生改变监听器
		ll_serchbeau_root.addOnLayoutChangeListener(this);
		tv_serch_beau_qx.setOnClickListener(this);
		prl_serch_beau_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Beautician beautician = (Beautician) parent
						.getItemAtPosition(position);
				goNextBeauDetail(BeauticianDetailActivity.class, beautician);
			}
		});

		prl_serch_beau_list.setMode(Mode.BOTH);
		prl_serch_beau_list
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
					@Override
					public void onRefresh(PullToRefreshBase refreshView) {
						PullToRefreshBase.Mode mode = refreshView
								.getCurrentMode();
						if (mode == Mode.PULL_FROM_START) {
							// 下拉刷新
							page = 1;
							beauList.clear();
							getData();
						} else {
							getData();
						}
					}
				});

		editText_serch_beau.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// 监听文本变化，请求网络
				beauList.clear();
				page = 1;
				getData();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		getData();
	}

	@Override
	protected void onResume() {
		num++;
		Utils.mLogError("==-->onResume");
		super.onResume();
		if (num != 1) {
			page = 1;
			beauList.clear();
			getData();
		}
	}

	private void getData() {
		realName = editText_serch_beau.getText().toString();
		CommUtil.getWorkerByName(this, areaNameid, realName != null ? realName
				: "", page, pageSize, handler);
	}

	private void showMain(boolean ismain) {
		if (ismain) {
			rl_serch_beau_vis.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			rl_serch_beau_vis.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
			tv_null_msg1.setText("未搜索到此美容师。。。");
		}
	}

	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			num++;
			prl_serch_beau_list.onRefreshComplete();
			Utils.mLogError("SerchBeauActivity result = "
					+ new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int code = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (code == 0) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONArray array = jobj.getJSONArray("data");
						if (array.length() > 0) {
							showMain(true);
							page++;
							for (int i = 0; i < array.length(); i++) {
								beauList.add(Beautician.json2Entity(array
										.getJSONObject(i)));
							}
							adapter = new BeauticianAdapter(
									SerchBeauActivity.this, beauList,"");
							adapter.setSort(10, Global.MAIN_TO_BEAUTICIANLIST);
							prl_serch_beau_list.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						} else {
							if (page == 1) {
								showMain(false);
							}
						}
					} else {
						if (page == 1) {
							showMain(false);
						}
					}
				} else {
					ToastUtil.showToastShortCenter(SerchBeauActivity.this, msg);
					if (page == 1) {
						showMain(false);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				try {
					ToastUtil.showToastShortCenter(SerchBeauActivity.this,
							"服务器请求失败");
					if (page == 1) {
						showMain(false);
					}
					prl_serch_beau_list.onRefreshComplete();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			num++;
			try {
				ToastUtil.showToastShortCenter(SerchBeauActivity.this,
						"网络连接失败，请检查您的网络");
				if (page == 1) {
					showMain(false);
				}
				prl_serch_beau_list.onRefreshComplete();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	};

	private void goNextBeauDetail(Class clazz, Beautician beautician) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("id", beautician.id);
		intent.putExtra("previous", Global.MAIN_TO_BEAUTICIANLIST);
		intent.putExtra("areaid", areaNameid);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_serch_beau_qx:
			goBack();
			break;
		case R.id.editText_serch_beau:
			editText_serch_beau.setFocusable(true);
			break;
		default:
			break;
		}
	}

	private void goBack() {
		MApplication.listAppoint.remove(this);
		finishWithAnimation();
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right,
			int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		// 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
		if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
			vw_serchbeau_mb.setVisibility(View.VISIBLE);
		} else if (oldBottom != 0 && bottom != 0
				&& (bottom - oldBottom > keyHeight)) {
			vw_serchbeau_mb.setVisibility(View.GONE);
		}
	}
}
