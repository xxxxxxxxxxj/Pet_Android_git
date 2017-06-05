package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CouponAdapter;
import com.haotang.pet.entity.MyCouponCanUse;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyTimeOutCouponActivity extends SuperActivity{
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private PullToRefreshListView listView_show_mycoupon;
	private CouponAdapter adapter;
	private MProgressDialog pDialog;
	private ArrayList<MyCouponCanUse> MyCouponCanUseList = new ArrayList<MyCouponCanUse>();
	private int page=1;
	private RelativeLayout no_has_data;
	private TextView tv_null_msg1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mytimeout_coupon);
		MyCouponCanUseList.clear();
		findView();
		setView();
		setMode();
		initListener();
		getAllCoupons(page);
		setData();
	}
	private void setMode() {
		listView_show_mycoupon.setMode(Mode.PULL_FROM_END);
	}
	private void setData() {
		// TODO Auto-generated method stub
		adapter = new CouponAdapter(MyTimeOutCouponActivity.this,MyCouponCanUseList);
		listView_show_mycoupon.setAdapter(adapter);
	}
	private void getAllCoupons(int page) {
		pDialog.setMessage("玩命加载中...");
		pDialog.showDialog();
		CommUtil.myacouponsused(
				SharedPreferenceUtil.getInstance(MyTimeOutCouponActivity.this)
						.getString("cellphone", "0"), Global
						.getIMEI(MyTimeOutCouponActivity.this), MyTimeOutCouponActivity.this,page,
				queryUserCoupons);
	}
	private AsyncHttpResponseHandler queryUserCoupons = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			listView_show_mycoupon.onRefreshComplete();
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				
				if (code == 0&&jsonObject.has("data")&&!jsonObject.isNull("data")) {
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (jsonArray.length()>0) {
						page++;
						for (int i = 0; i < jsonArray.length(); i++) {
							MyCouponCanUseList.add(MyCouponCanUse.json2Entity(jsonArray.getJSONObject(i)));
						}
					}
					showMain(true);
					adapter.notifyDataSetChanged();
					if(page==1){
						showMain(false);
					}
				}else {
					if(page==1){
						showMain(false);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				if(page==1){
					showMain(false);
				}
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			listView_show_mycoupon.onRefreshComplete();
			if(page==1){
				showMain(false);
			}
		}
		
	};
	private void initListener() {
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		
		listView_show_mycoupon.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				
			}
			
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//				page++;
				adapter.notifyDataSetChanged();
				getAllCoupons(page);
			}
		});
	}

	private void findView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		
		listView_show_mycoupon = (PullToRefreshListView) findViewById(R.id.listView_show_mycoupon);
		tv_titlebar_title.setText("已过期");
		no_has_data = (RelativeLayout) findViewById(R.id.rl_null);
		tv_null_msg1 = (TextView) findViewById(R.id.tv_null_msg1);
		pDialog = new MProgressDialog(this);

	}

	private void setView() {
		// TODO Auto-generated method stub
		
	}
	
	private void showMain(boolean ismain){
		if(ismain){
			listView_show_mycoupon.setVisibility(View.VISIBLE);
			no_has_data.setVisibility(View.GONE);
		}else{
			listView_show_mycoupon.setVisibility(View.GONE);
			no_has_data.setVisibility(View.VISIBLE);
			tv_null_msg1.setText("暂时还没有过期优惠券");
		}
	}
}
