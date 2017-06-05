package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.TopIndicator_C;
import com.haotang.pet.view.TopIndicator_C.OnTopIndicatorListener;

public class MainToBeauList extends SuperActivity implements OnTouchListener,
		OnTopIndicatorListener, OnClickListener {
	private ImageButton ibBack;
	private TextView tvTitle;
	private PullToRefreshListView prlList;
	private RelativeLayout rlNull;
	private TextView tv_null_msg1;
	private TextView tv_null_msg2;
	private ImageView ivNull;
	private TopIndicator_C tidTopTab;
	private int oldindex = 0;
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;
	public static SuperActivity act;
	private boolean isgoing = true;
	private int page = 1;
	private int pageSize = 10;
	private ArrayList<Beautician> beauList = new ArrayList<Beautician>();
	private BeauticianAdapter adapter;
	private ImageButton ib_titlebar_other;
	
	private LinearLayout show;
	CharSequence[] mLabels = new CharSequence[] { "中级", "高级", "首席"};
	private ArrayList<String> showList = new ArrayList<String>();
	private boolean isFrist =false;
	private int workerLevel = -1;
	private int currSort;
	private int currentIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_to_beaulist);
		act = this;
		MApplication.listAppoint.add(this);
		findView();
		setView();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goBack();
			}
		});
		prlList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Beautician beautician = (Beautician) parent.getItemAtPosition(position);
				goNextBeauDetail(BeauticianDetailActivity.class, beautician);
			}
		});
	}

	private void setView() {
		Intent intent = getIntent();
		workerLevel  = intent.getIntExtra("workerLevel", -1);
		if(workerLevel >= 0){
			switch (workerLevel) {
			case 1:
				oldindex = 0;
				break;
			case 2:
				oldindex = 1;
				break;
			case 3:
				oldindex = 2;
				break;
			default:
				break;
			}
		}
		pDialog = new MProgressDialog(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		String topCurrentAreaName = spUtil.getString("tareaname", "美容师");
		int areaNameid = spUtil.getInt("tareaid", 0);
		if (areaNameid == 0 || areaNameid == 100) {
			tvTitle.setText("美容师");
		} else {
			tvTitle.setText(topCurrentAreaName + "美容师");
		}
		ivNull.setBackgroundResource(R.drawable.icon_null);
	}

	private void findView() {
		show = (LinearLayout) findViewById(R.id.show);
		isgoing = true;
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		ib_titlebar_other = (ImageButton) findViewById(R.id.ib_titlebar_other);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		LayoutParams layoutParams = ib_titlebar_other.getLayoutParams();
		layoutParams.width = Math.round(36*2/3*density);
		layoutParams.height = Math.round(38*2/3*density);
		ib_titlebar_other.setLayoutParams(layoutParams);
		
		ib_titlebar_other.setBackgroundResource(R.drawable.serch_beau);
		ib_titlebar_other.setVisibility(View.VISIBLE);
		ib_titlebar_other.setOnClickListener(this);

		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		ivNull = (ImageView) findViewById(R.id.iv_null_image);
		tv_null_msg1 = (TextView) findViewById(R.id.tv_null_msg1);
		tv_null_msg2 = (TextView) findViewById(R.id.tv_null_msg2);
		prlList = (PullToRefreshListView) findViewById(R.id.prl_beauticianlist_list);
//		tidTopTab = (TopIndicator_B) findViewById(R.id.tid_beauti_topindicator);
//		tidTopTab.setOnTopIndicatorListener(this);
		tidTopTab = new TopIndicator_C(act);
//		tidTopTab.setOnTouchListener(this);
		LinearLayout.LayoutParams topLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		tidTopTab.setLayoutParams(topLayoutParams);
		show.addView(tidTopTab);
		tidTopTab.setOnTopIndicatorListener(this);
		adapter = new BeauticianAdapter(act, beauList,"");
		prlList.setMode(Mode.BOTH);
		prlList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
					// 下拉刷新
					page = 1;
					beauList.clear();
					showList.clear();
					adapter.notifyDataSetChanged();
					getData(oldindex);
				} else {
					getData(oldindex);
				}
			}
		});
		prlList.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		page = 1;
		beauList.clear();
		showList.clear();
		getData(oldindex);
		/*if (isgoing) {
			isgoing = false;
			oldindex = 0;
			adapter.setIndex(oldindex);
			adapter.setSort(10, Global.MAIN_TO_BEAUTICIANLIST);
			adapter.notifyDataSetChanged();
			getData(oldindex);
		} else {
			adapter.setIndex(oldindex);
			adapter.notifyDataSetChanged();
			getData(oldindex);
		}*/
	}

	@Override
	public void onIndicatorSelected(int index) {
		// TODO Auto-generated method stub
		Utils.mLogError("index===" + index);
		Utils.mLogError("oldindex===" + oldindex);
		if (oldindex != index) {
			oldindex = index;
			currentIndex = index;
			tidTopTab.setTabsDisplay(act, index);
			if (oldindex == 0) {
				adapter.setSort(10, Global.MAIN_TO_BEAUTICIANLIST);
			} else if (oldindex == 1) {
				adapter.setSort(20, Global.MAIN_TO_BEAUTICIANLIST);
			} else if (oldindex == 2) {
				adapter.setSort(30, Global.MAIN_TO_BEAUTICIANLIST);
			}
			page = 1;
			beauList.clear();
			showList.clear();
			adapter.notifyDataSetChanged();
			getData(index);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	private void getData(int index) {
		pDialog.showDialog();
		if (index == 0) {
			CommUtil.getAllWorkers(spUtil.getString("cellphone", ""),Global.getIMEI(act), act, 10, spUtil.getInt("tareaid", 0),page, pageSize, getAllWorkers);
		} else if (index == 1) {
			CommUtil.getAllWorkers(spUtil.getString("cellphone", ""),Global.getIMEI(act), act, 20, spUtil.getInt("tareaid", 0),page, pageSize, getAllWorkers);
		} else if (index == 2) {
			CommUtil.getAllWorkers(spUtil.getString("cellphone", ""),Global.getIMEI(act), act, 30, spUtil.getInt("tareaid", 0),page, pageSize, getAllWorkers);
		}
	}

	private AsyncHttpResponseHandler getAllWorkers = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			prlList.onRefreshComplete();
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int code = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (code == 0) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject object = jobj.getJSONObject("data");
						if (currentIndex == 0) {
							if (object.has("10")&&!object.isNull("10")) {
								JSONObject object2 = object.getJSONObject("10");
								String levelName = "";
								if (object2.has("level")&&!object2.isNull("level")) {
									JSONObject objectLevel = object2.getJSONObject("level");
									if (objectLevel.has("name")&&!objectLevel.isNull("name")) {
										levelName = objectLevel.getString("name");
										showList.add(levelName);
									}
								}
								if (object2.has("workers")&&!object2.isNull("workers")) {
									JSONArray array = object2.getJSONArray("workers");
									if (array.length()>0) {
										page++;
										for (int i = 0; i < array.length(); i++) {
											Beautician btc = Beautician.json2Entity(array.getJSONObject(i));
											btc.levelname = levelName;
											btc.titlelevel = levelName;
											beauList.add(btc);
//											beauList.add(Beautician.json2Entity(array.getJSONObject(i)));
										}
									}
								}
							}
						}else if(currentIndex == 1){
							if (object.has("20")&&!object.isNull("20")) {
								JSONObject object2 = object.getJSONObject("20");
								String levelName = "";
								if (object2.has("level")&&!object2.isNull("level")) {
									JSONObject objectLevel = object2.getJSONObject("level");
									if (objectLevel.has("name")&&!objectLevel.isNull("name")) {
										levelName = objectLevel.getString("name");
										showList.add(levelName);
									}
								}							
								if (object2.has("workers")&&!object2.isNull("workers")) {
									JSONArray array = object2.getJSONArray("workers");
									if (array.length()>0) {
										page++;
										for (int i = 0; i < array.length(); i++) {
											Beautician btc = Beautician.json2Entity(array.getJSONObject(i));
											btc.levelname = levelName;
											btc.titlelevel = levelName;
											beauList.add(btc);
//											beauList.add(Beautician.json2Entity(array.getJSONObject(i)));
										}
									}
								}
							}
						}else if (currentIndex == 2) {
							if (object.has("30")&&!object.isNull("30")) {
								JSONObject object2 = object.getJSONObject("30");
								String levelName = "";
								if (object2.has("level")&&!object2.isNull("level")) {
									JSONObject objectLevel = object2.getJSONObject("level");
									if (objectLevel.has("name")&&!objectLevel.isNull("name")) {
										levelName = objectLevel.getString("name");
										showList.add(levelName);
									}
								}
								if (object2.has("workers")&&!object2.isNull("workers")) {
									JSONArray array = object2.getJSONArray("workers");
									if (array.length()>0) {
										page++;
										for (int i = 0; i < array.length(); i++) {
											Beautician btc = Beautician.json2Entity(array.getJSONObject(i));
											btc.levelname = levelName;
											btc.titlelevel = levelName;
											beauList.add(btc);
//											beauList.add(Beautician.json2Entity(array.getJSONObject(i)));
										}
									}
								}
							}
						}
						try {
						if (showList.size()>0) {
							for (int i = 0; i < showList.size(); i++) {
								mLabels[i]=showList.get(i);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
						setSort();
						showMain(true);
					}else{
						setSort();
					}
					adapter.notifyDataSetChanged();
					if (page == 1) {
						showMain(false);
					}
				} else {
					setSort();
					ToastUtil.showToastShortCenter(act, msg);
					if (page == 1) {
						showMain(false);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					if (page == 1) {
						showMain(false);
					}
					prlList.onRefreshComplete();
					pDialog.closeDialog();
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
				if (page == 1) {
					showMain(false);
				}
				prlList.onRefreshComplete();
				pDialog.closeDialog();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	};
	
	private void setSort(){
		if (!isFrist) {
			isFrist=true;
			show.removeAllViews();
			tidTopTab.removeAllViews();
			tidTopTab.init(act, mLabels);
			if(workerLevel >= 0){
				switch (workerLevel) {
				case 1:
					tidTopTab.setTabsDisplay(act, 0);
					currSort = 10;
					break;
				case 2:
					tidTopTab.setTabsDisplay(act, 1);
					currSort = 20;
					break;
				case 3:
					tidTopTab.setTabsDisplay(act, 2);
					currSort = 30;
					break;
				default:
					break;
				}
					adapter.setSort(currSort, Global.MAIN_TO_BEAUTICIANLIST);
					adapter.notifyDataSetChanged();
			}else{
				adapter.setSort(10, Global.MAIN_TO_BEAUTICIANLIST);
				adapter.notifyDataSetChanged();
			}
			show.addView(tidTopTab);
		}
	}

	private void showMain(boolean ismain) {
		if (ismain) {
			prlList.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			prlList.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
			tv_null_msg1.setText("没有美容师了。。。");
		}
	}

	private void goNextBeauDetail(Class clazz, Beautician beautician) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("id", beautician.id);
		intent.putExtra("areaName", beautician.areaName);
		intent.putExtra("areaid", beautician.areaId);
		intent.putExtra("previous", Global.MAIN_TO_BEAUTICIANLIST);
		startActivity(intent);
	}

	private void goBack() {
		MApplication.listAppoint.remove(this);
		finishWithAnimation();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_other:
			startActivity(new Intent(this, SerchBeauActivity.class));
			break;

		default:
			break;
		}
	}

}
