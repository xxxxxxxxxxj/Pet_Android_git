package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.RechargeableCardAdapter;
import com.haotang.pet.entity.Charact;
import com.haotang.pet.entity.Rechar;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;
/**
 * 余额
 * @author zhiqiang
 *
 */
public class MyLastMoney extends SuperActivity{
	
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private Button bt_titlebar_other;
	private TextView textView_MyLast_money_show;
	private PullToRefreshListView listView_my_last_money;
	private SharedPreferenceUtil spUtil;
	public static MyLastMoney lastMoney;
	private RechargeableCardAdapter cardAdapter;
	private List<Rechar> Rechlist = new ArrayList<Rechar>();
	private MProgressDialog pDialog;
	private RelativeLayout no_has_data;
	private TextView tv_null_msg1;
	private int previous;
	private MyReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_last_money);
		pDialog = new MProgressDialog(this);
		Rechlist.clear();
		lastMoney = this;
		findView();
		setView();
		initListener();
//		initReceiver();
	}
	private void initReceiver() {//暂时不用了，会员功能屏蔽
		// 广播事件**********************************************************************
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MyLastMoney");
		// 注册广播接收器
		registerReceiver(receiver, filter);
	}
	private void initListener() {
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goBack();
			}
		});
		bt_titlebar_other.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				JumpToNext(BalanceRecord.class);
			}
		});
	}
	private void findView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		textView_MyLast_money_show = (TextView) findViewById(R.id.textView_MyLast_money_show);
		listView_my_last_money = (PullToRefreshListView) findViewById(R.id.listView_my_last_money);
		
		no_has_data = (RelativeLayout) findViewById(R.id.rl_null);
		tv_null_msg1 = (TextView) findViewById(R.id.tv_null_msg1);

		spUtil = SharedPreferenceUtil.getInstance(this);
	}
	private void setView() {
		tv_titlebar_title.setText("我的余额");
		bt_titlebar_other.setText("交易明细");
		previous = getIntent().getIntExtra("previous", 0);
		bt_titlebar_other.setTextColor(getResources().getColor(R.color.orange));
		
		cardAdapter = new RechargeableCardAdapter(this, Rechlist);
		listView_my_last_money.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		listView_my_last_money.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					Rechlist.clear();
					cardAdapter.notifyDataSetChanged();
					getData();
				}
			}

			
		});
		listView_my_last_money.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				JumpToNextForData(RechargePage.class, Rechlist.get(position-1));
//				UmengStatistics.UmengEventStatistics(MyLastMoney.this,Global.UmengEventID.click_ChargeNow);
			}
		});
		listView_my_last_money.setAdapter(cardAdapter);
	}
	private void getData() {
		pDialog.showDialog();
		CommUtil.getRechargeTemplate(Global.getIMEI(this), this, RechargeTemplate);
	}
	private AsyncHttpResponseHandler RechargeTemplate = new AsyncHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			listView_my_last_money.onRefreshComplete();
			pDialog.closeDialog();
			Utils.mLogError("==-->充值卡列表  "+new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONArray array = jsonObject.getJSONArray("data");
						if (array.length()>0) {
							for (int i = 0; i < array.length(); i++) {
								Rechlist.add(Rechar.json2Entity(array.getJSONObject(i)));
							}
						}else {
							showMain(false);
						}
					}
					cardAdapter.notifyDataSetChanged();
				}else {
					if (jsonObject.has("msg")&&!jsonObject.isNull("msg")) {
						ToastUtil.showToastShortCenter(lastMoney, jsonObject.getString("msg"));
					}
					showMain(false);
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
			listView_my_last_money.onRefreshComplete();
			pDialog.closeDialog();
			showMain(false);
		}
		
	};
	
	private void getMoney() {
		// TODO Auto-generated method stub
		CommUtil.getUserAccountBalance(SharedPreferenceUtil.getInstance(this).getString("cellphone", ""), Global.getIMEI(this), this, getMoney);	
	}
	private AsyncHttpResponseHandler getMoney = new AsyncHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONObject  object = jsonObject.getJSONObject("data");
						if (object.has("balance")&&!object.isNull("balance")) {
							textView_MyLast_money_show.setText("¥"+Utils.formatDouble(object.getDouble("balance"), 2));
						}
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
	private void JumpToNext(Class clazz) {
		Intent intent = new Intent(lastMoney, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		Bundle bundle = new Bundle();
		bundle.putInt("index", 1);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	private void JumpToNextForData(Class clazz,Rechar rechar) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		Bundle bundle = new Bundle();
		bundle.putSerializable("rechar", rechar);
		intent.putExtras(bundle);
		startActivityForResult(intent, Global.PRE_ORDERDETAIL_TO_RECHARGE);
	}
	private void showMain(boolean ismain){
		if(ismain){
			listView_my_last_money.setVisibility(View.VISIBLE);
			no_has_data.setVisibility(View.GONE);
		}else{
			listView_my_last_money.setVisibility(View.GONE);
			no_has_data.setVisibility(View.VISIBLE);
			tv_null_msg1.setText("暂时没有充值卡");
		}
	}
	private void goBack(){
		setResult(Global.RESULT_OK);
		finishWithAnimation();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (spUtil.getString("cellphone", "").equals("")) {
			bt_titlebar_other.setVisibility(View.GONE);
		}else {
			bt_titlebar_other.setVisibility(View.VISIBLE);
		}
		MobclickAgent.onPageStart("MyLastMoneyActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	    MobclickAgent.onResume(this);          //统计时长
	    getMoney();
	    Rechlist.clear();
		cardAdapter.notifyDataSetChanged();
		getData();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	    MobclickAgent.onPageEnd("MyLastMoneyActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode==Global.RESULT_OK){
			if(previous==Global.PRE_ORDERDETAILACTIVITY_TO_AVAILABLECOUPONACTIVITY
					&&requestCode==Global.PRE_ORDERDETAIL_TO_RECHARGE){
				setResult(Global.RESULT_OK);
				finishWithAnimation();
			}else{
				getMoney();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private class MyReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String money = intent.getStringExtra("money");
			Intent intentGrowth = new Intent(mContext, GrowthActivity.class);
			intentGrowth.putExtra("money", money);
			Utils.mLogError("==-->rechargeAmount跳到活动界面 "+money);
			startActivity(intentGrowth);
			getMoney();//启动界面同时刷新金额;
		}
		
	}
}
