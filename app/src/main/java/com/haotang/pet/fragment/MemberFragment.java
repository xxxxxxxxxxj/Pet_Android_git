package com.haotang.pet.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.pet.LoginActivity;
import com.haotang.pet.MyCouponActivity;
import com.haotang.pet.MyLastMoney;
import com.haotang.pet.MyTimeOutCouponActivity;
import com.haotang.pet.R;
import com.haotang.pet.RechargePage;
import com.haotang.pet.adapter.CouponAdapter;
import com.haotang.pet.adapter.RechargeableCardAdapter;
import com.haotang.pet.entity.MyCouponCanUse;
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MemberFragment extends Fragment {

	private ImageButton ibBack;
	private TextView tvTitle;
	private LinearLayout llTopup;
	private LinearLayout llCoupon;
	private Button btTopup;
	private Button btCoupon;
	private boolean isTopup = true;
	private RelativeLayout llTopupNull;
	private PullToRefreshListView pflTopup;
	private TextView tvBalance;
	private EditText etCode;
	private Button btExchange;
	private PullToRefreshListView pflCoupon;
	private TextView tvOverdueCoupon;
	private Button btLogin;
	private LinearLayout llCouponLogin;
	private RelativeLayout rlCouponMain;
	private SharedPreferenceUtil spUtil;
	private Activity mActivity;
//	private MProgressDialog pDialog;
	private List<Rechar> Rechlist = new ArrayList<Rechar>();
	private List<MyCouponCanUse> couponList = new ArrayList<MyCouponCanUse>();
	private int page=1;
	private View footer;
	private LinearLayout footerLiner;
	private LayoutInflater mInflater;
	private ListView listView = null;
	
	private View left;
	private View right;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==1000){
//				llMain.setVisibility(View.VISIBLE);
				rlLoading.setVisibility(View.GONE);
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.mActivity = activity;
		spUtil = SharedPreferenceUtil.getInstance(activity);
//		pDialog = new MProgressDialog(activity);
		mInflater = LayoutInflater.from(mActivity);
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.memberfragment, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		isTopup = true;
		findView(view);
		setView();
	}
	
	private void findView(View view) {
		rlLoading = (RelativeLayout) view.findViewById(R.id.rl_memberfragmentloading);
		llMain = (LinearLayout) view.findViewById(R.id.ll_memberfragment);
		ibBack = (ImageButton) view.findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) view.findViewById(R.id.tv_titlebar_title);
		btTopup = (Button) view.findViewById(R.id.bt_member_topup);
		btCoupon = (Button) view.findViewById(R.id.bt_member_coupon);
		llTopup = (LinearLayout) view.findViewById(R.id.ll_member_topup);
		llCoupon = (LinearLayout) view.findViewById(R.id.ll_member_coupon);
		llTopupNull = (RelativeLayout) view.findViewById(R.id.rl_member_topup_null);
		pflTopup = (PullToRefreshListView) view.findViewById(R.id.pfl_member_topup);
		tvBalance = (TextView) view.findViewById(R.id.tv_member_balance);
		
		etCode = (EditText) view.findViewById(R.id.et_member_coupon_code);
		btExchange = (Button) view.findViewById(R.id.bt_member_coupon_exchange);
		pflCoupon = (PullToRefreshListView) view.findViewById(R.id.pfl_member_coupon);
		tvOverdueCoupon = (TextView) view.findViewById(R.id.tv_member_coupon_overdue);
		btLogin = (Button) view.findViewById(R.id.bt_member_coupon_login);
		llCouponLogin = (LinearLayout) view.findViewById(R.id.ll_member_coupon_login);
		rlCouponMain = (RelativeLayout) view.findViewById(R.id.rl_member_coupon_main);
		llCouponBottom = (LinearLayout) view.findViewById(R.id.ll_member_coupon_bottom);
		
		footer = mInflater.inflate(R.layout.footer_my_coupon,null);
		footerLiner = (LinearLayout) footer.findViewById(R.id.layout_find_time_out);
		left = (View) view.findViewById(R.id.left);
		right = (View) view.findViewById(R.id.right);
		
	}

	private void setView() {
		// TODO Auto-generated method stub
		ibBack.setVisibility(View.GONE);
		tvTitle.setText("会员");
		
		btTopup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isTopup){
					isTopup = true;
					left.setBackgroundColor(getResources().getColor(R.color.orange));
					right.setBackgroundColor(getResources().getColor(R.color.bggray));
					btTopup.setTextColor(getResources().getColor(R.color.orange));
					btCoupon.setTextColor(getResources().getColor(R.color.a888888));

//					btTopup.setBackgroundResource(R.drawable.order_choose_back);
//					btCoupon.setBackgroundResource(R.drawable.bg_member_right_normal);
//					btTopup.setTextColor(getResources().getColor(R.color.orange));
//					btCoupon.setTextColor(getResources().getColor(R.color.a888888));
					showTopup(true);
					getBalance();
					getTopupData();
				}
			}
		});
		btCoupon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isTopup){
					isTopup = false;
					left.setBackgroundColor(getResources().getColor(R.color.bggray));
					right.setBackgroundColor(getResources().getColor(R.color.orange));
					btTopup.setTextColor(getResources().getColor(R.color.a888888));
					btCoupon.setTextColor(getResources().getColor(R.color.orange));

//					btCoupon.setTextColor(getResources().getColor(R.color.orange));
//					btTopup.setTextColor(getResources().getColor(R.color.a888888));
//					btTopup.setBackgroundResource(R.drawable.order_choose_back);
//					btCoupon.setBackgroundResource(R.drawable.bg_member_right_passed);
					showTopup(false);
					couponList.clear();
					page = 1;
					if(spUtil.getInt("userid", -1) > 0&&
							!"".equals(spUtil.getString("cellphone", ""))){
						getAllCoupons(page);
					}
				}
			}
		});
		btLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNextForData(LoginActivity.class);
			}
		});
		btExchange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(etCode.getText().toString().trim().length()<=0){
					ToastUtil.showToastShortCenter(mActivity, "兑换码不能为空，请输入兑换码");
				}else{
					CommUtil.redeemCouponCode(spUtil.getString("cellphone", "0"), Global.getIMEI(mActivity), mActivity, 
							etCode.getText().toString(), exchangecouponHandler);
				}
			}
		});
		tvOverdueCoupon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(MyTimeOutCouponActivity.class);
			}
		});
		footerLiner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(MyTimeOutCouponActivity.class);
			}
		});
		
		
		cardAdapter = new RechargeableCardAdapter(mActivity, Rechlist);
		pflTopup.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		pflTopup.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					Rechlist.clear();
					cardAdapter.notifyDataSetChanged();
					getTopupData();
				}
			}
		});
		pflTopup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				JumpToNext(RechargePage.class, Rechlist.get(position-1));
//				UmengStatistics.UmengEventStatistics(mActivity,Global.UmengEventID.click_ChargeNow);
			}
		});
		pflTopup.setAdapter(cardAdapter);
		pflCoupon.setMode(PullToRefreshBase.Mode.BOTH);
		pflCoupon.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					couponList.clear();
					page=1;
					adapter.notifyDataSetChanged();
					getAllCoupons(page);
				}else{
					adapter.notifyDataSetChanged();
					getAllCoupons(page);
				}
			}
			
			
		});
		
		
		adapter = new CouponAdapter(mActivity,couponList);
		listView = pflCoupon.getRefreshableView();
		footer.setVisibility(View.GONE);
		listView.addFooterView(footer);
		pflCoupon.setAdapter(adapter);
		
	}
	
	private void showTopup(boolean flag){
		if(flag){
			llTopup.setVisibility(View.VISIBLE);
			llCoupon.setVisibility(View.GONE);
		}else{
			llTopup.setVisibility(View.GONE);
			llCoupon.setVisibility(View.VISIBLE);
			if(spUtil.getInt("userid", -1) > 0&&!"".equals(spUtil.getString("cellphone", ""))){
				showLogin(false);
			}else{
				showLogin(true);
			}
		}
	}
	private void showLogin(boolean flag){
		if(flag){
			llCouponLogin.setVisibility(View.VISIBLE);
			rlCouponMain.setVisibility(View.GONE);
		}else{
			llCouponLogin.setVisibility(View.GONE);
			rlCouponMain.setVisibility(View.VISIBLE);
		}
	}
	
	private void goNext(Class clazz) {
		Intent intent = new Intent(mActivity, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getActivity().getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		mActivity.startActivity(intent);
	}
	private void goNextForData(Class clazz) {
		Intent intent = new Intent(mActivity, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getActivity().getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", Global.PRE_MEMBERFRAGMENT_TO_LOGIN);
		mActivity.startActivity(intent);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if(isTopup){
			left.setBackgroundColor(getResources().getColor(R.color.orange));
			right.setBackgroundColor(getResources().getColor(R.color.bggray));
			btTopup.setTextColor(getResources().getColor(R.color.orange));
			btCoupon.setTextColor(getResources().getColor(R.color.a888888));
//			btTopup.setBackgroundResource(R.drawable.order_choose_back);
//			btCoupon.setBackgroundResource(R.drawable.bg_member_right_normal);
//			btTopup.setTextColor(getResources().getColor(R.color.orange));
//			btCoupon.setTextColor(getResources().getColor(R.color.a888888));
			showTopup(true);
			getBalance();
			getTopupData();
		}else{
			left.setBackgroundColor(getResources().getColor(R.color.bggray));
			right.setBackgroundColor(getResources().getColor(R.color.orange));
			btTopup.setTextColor(getResources().getColor(R.color.a888888));
			btCoupon.setTextColor(getResources().getColor(R.color.orange));
//			btCoupon.setTextColor(getResources().getColor(R.color.a888888));
//			btTopup.setTextColor(getResources().getColor(R.color.orange));
//			btTopup.setBackgroundResource(R.drawable.bg_member_left_normal);
//			btCoupon.setBackgroundResource(R.drawable.order_choose_back);
			showTopup(false);
			couponList.clear();
			page = 1;
			if(spUtil.getInt("userid", -1) > 0&&
					!"".equals(spUtil.getString("cellphone", ""))){
				getAllCoupons(page);
			}
		}
		super.onResume();
	}
	
	private void JumpToNext(Class clazz,Rechar rechar) {
		Intent intent = new Intent(mActivity, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		mActivity.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		Bundle bundle = new Bundle();
		bundle.putSerializable("rechar", rechar);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void getTopupData() {
		Rechlist.clear();
//		pDialog.showDialog();
//		llMain.setVisibility(View.GONE);
		rlLoading.setVisibility(View.VISIBLE);
		CommUtil.getRechargeTemplate(Global.getIMEI(mActivity), mActivity, RechargeTemplate);
	}
	private AsyncHttpResponseHandler RechargeTemplate = new AsyncHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			pflTopup.onRefreshComplete();
//			pDialog.closeDialog();
			Utils.mLogError("==-->充值卡列表  "+new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						llTopupNull.setVisibility(View.GONE);
						pflTopup.setVisibility(View.VISIBLE);
						JSONArray array = jsonObject.getJSONArray("data");
						if (array.length()>0) {
							for (int i = 0; i < array.length(); i++) {
								Rechlist.add(Rechar.json2Entity(array.getJSONObject(i)));
							}
						}else {
							llTopupNull.setVisibility(View.VISIBLE);
							pflTopup.setVisibility(View.GONE);
						}
					}else{
						llTopupNull.setVisibility(View.VISIBLE);
						pflTopup.setVisibility(View.GONE);
					}
					cardAdapter.notifyDataSetChanged();
				}else {
					if (jsonObject.has("msg")&&!jsonObject.isNull("msg")) {
						ToastUtil.showToastShortCenter(mActivity, jsonObject.getString("msg"));
					}
					llTopupNull.setVisibility(View.VISIBLE);
					pflTopup.setVisibility(View.GONE);
				}
				mHandler.sendEmptyMessageDelayed(1000, 800);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				ToastUtil.showToastShortCenter(mActivity, "网络连接失败,请检查您的网络");
				mHandler.sendEmptyMessageDelayed(1000, 800);
			}
			
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pflTopup.onRefreshComplete();
//			pDialog.closeDialog();
//			ToastUtil.showToastShortCenter(mActivity, "网络连接失败,请检查您的网络");
			llTopupNull.setVisibility(View.VISIBLE);
			pflTopup.setVisibility(View.GONE);
			mHandler.sendEmptyMessageDelayed(1000, 800);
		}
		
	};
	
	private void getBalance() {
		// TODO Auto-generated method stub
		CommUtil.getUserAccountBalance(spUtil.getString("cellphone", ""), 
				Global.getIMEI(mActivity), mActivity, getMoney);	
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
							tvBalance.setText("¥"+Utils.formatDouble(object.getDouble("balance"), 2));
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
	
	private void getAllCoupons(int page) {
//		pDialog.showDialog();
//		llMain.setVisibility(View.GONE);
		rlLoading.setVisibility(View.VISIBLE);
		CommUtil.myacouponsunused(spUtil.getString("cellphone", "0"), 
				Global.getIMEI(mActivity), mActivity,page,
				queryUserCoupons);
	}

	private AsyncHttpResponseHandler queryUserCoupons = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
//			pDialog.closeDialog();
			pflCoupon.onRefreshComplete();
			
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				
				if (code == 0&&jsonObject.has("data")&&!jsonObject.isNull("data")) {
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (jsonArray.length()>0) {
						page++;
						for (int i = 0; i < jsonArray.length(); i++) {
							couponList.add(MyCouponCanUse.json2Entity(jsonArray.getJSONObject(i)));
						}
					}
					
				}
				if (couponList.size()<=0) {
					llCouponBottom.setVisibility(View.VISIBLE);
					footer.setVisibility(View.GONE);
					footerLiner.setVisibility(View.GONE);
					pflCoupon.setVisibility(View.GONE);
					listView.removeFooterView(footer);
				}else{
					llCouponBottom.setVisibility(View.GONE);
					footer.setVisibility(View.VISIBLE);
					footerLiner.setVisibility(View.VISIBLE);
				}
				adapter.notifyDataSetChanged();
				mHandler.sendEmptyMessageDelayed(1000, 800);
			} catch (JSONException e) {
				e.printStackTrace();
//				ToastUtil.showToastShortCenter(mActivity, "网络连接失败，请检查您的网络");
				if (couponList.size()<=0) {
					llCouponBottom.setVisibility(View.VISIBLE);
				}else{
					llCouponBottom.setVisibility(View.GONE);
				}
				mHandler.sendEmptyMessageDelayed(1000, 800);
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
//			pDialog.closeDialog();
			pflCoupon.onRefreshComplete();
			if (couponList.size()<=0) {
				llCouponBottom.setVisibility(View.VISIBLE);
			}else{
				llCouponBottom.setVisibility(View.GONE);
			}
			mHandler.sendEmptyMessageDelayed(1000, 800);
		}
	};
	
	private AsyncHttpResponseHandler exchangecouponHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->redeem "+new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					couponList.clear();
					Utils.mLogError("==-->couponList code 1 := "+couponList.size());
					page = 1;
					getAllCoupons(page);
				}else {
					ToastUtil.showToastShortCenter(mActivity, jsonObject.getString("msg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			ToastUtil.showToastShortCenter(mActivity, "网络连接失败,请检查您的网络");
		}
		
	};
	
	private RechargeableCardAdapter cardAdapter;
	private LinearLayout llCouponBottom;
	private CouponAdapter adapter;
	private RelativeLayout rlLoading;
	private LinearLayout llMain;
	
	
}
