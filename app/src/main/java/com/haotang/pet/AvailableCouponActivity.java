package com.haotang.pet;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.AvailableCouponAdapter;
import com.haotang.pet.entity.Coupon;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class AvailableCouponActivity extends SuperActivity {
	private ImageButton ibBack;
	private TextView tvTitle;
	private PullToRefreshListView prlCouponList;
	private ArrayList<Coupon> couponList;
	private AvailableCouponAdapter adapter;
	private Button btNoUsedCoupon;
	private int previous;
	private int couponId;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.availablecoupon);
		
		findView();
		setView();
	}

	private void findView() {
		previous = getIntent().getIntExtra("previous", 0);
		if(previous == Global.PRE_ORDERDETAILACTIVITY_TO_AVAILABLECOUPONACTIVITY){
			couponList = OrderDetailActivity.couponList;
		}else if(previous == Global.PRE_ORDERDETAILFROMORDERACTIVITY_TO_AVAILABLECOUPONACTIVITY){
			couponList = OrderDetailFromOrderActivity.couponList;
		}else if(previous == Global.FOSTERCAREORDER_TO_COUPON){
			couponList = FostercareOrderConfirmActivity.couponList;
		}
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		
		prlCouponList = (PullToRefreshListView) findViewById(R.id.prl_availablecoupon_list);
		
		btNoUsedCoupon = (Button) findViewById(R.id.bt_availablecoupon_nousedcoupon);
		couponId = getIntent().getIntExtra("couponId", 0);
	}

	private void setView() {
		tvTitle.setText("我的优惠券");
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		prlCouponList.setRefreshing(false);
		prlCouponList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				
			}
		});
		adapter = new AvailableCouponAdapter(this, couponList);
		prlCouponList.setAdapter(adapter);
		adapter.setCouponId(couponId);
		adapter.notifyDataSetChanged();
		prlCouponList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Utils.mLogError("position="+position);
				Coupon coupon = (Coupon) adapter.getItem(position-1);
				
//				Utils.mLogError("id="+c.id+"  content="+c.content);
				Intent data = new Intent();
				data.putExtra("content", "-"+coupon.amount+"("+coupon.name+")");
				data.putExtra("amount", coupon.amount);
				data.putExtra("couponid", coupon.id);
				setResult(Global.RESULT_OK, data);
				finishWithAnimation();
			}
		});
		btNoUsedCoupon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent data = new Intent();
				data.putExtra("content", "不使用优惠券");
				data.putExtra("amount", "0");
				data.putExtra("couponid", 0);
				setResult(Global.RESULT_OK, data);
				finishWithAnimation();
			}
		});
		
	}
	
	
}
