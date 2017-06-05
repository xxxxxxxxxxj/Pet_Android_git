package com.haotang.pet.fragment;

import java.lang.reflect.Field;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;

public class MyOrderFragment extends Fragment implements OnTouchListener{
	
	private MainActivity mainActivity;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title,textView_my_order_all,textView_my_order_process,textView_my_order_evaluate,textView_if_has;
	private LinearLayout layout_my_order_all,layout_my_order_process,layout_my_order_evaluate;
	
	/**
	 * 广播接收器
	 */
	private MyReceiver receiver;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mainActivity = (MainActivity) activity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initReceiver();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_my_order, null);
		view.setOnTouchListener(this);
		initView(view);
		int code = SharedPreferenceUtil.getInstance(mainActivity).getInt("state", 0);
		if (code==0) {
			ProcessMyOrder();
		}else if (code!=4) {
			AllMyOrder();
		}else if (code ==4) {
			EvaluateMyOrder();
		}
		SharedPreferenceUtil.getInstance(mainActivity).removeData("state");
		initListener();
		
		CommUtil.queryEvaluateMyOrders(SharedPreferenceUtil.getInstance(getActivity()).getString("cellphone", "0"), 
				Global.getIMEI(getActivity()),
				getActivity(),1,
				queryEvaluateMyOrders);
		return view;
	}
	private AsyncHttpResponseHandler queryEvaluateMyOrders = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONObject objectData = jsonObject.getJSONObject("data");
						if (objectData.has("nToBeComment")&&!objectData.isNull("nToBeComment")) {
							int nToBeComment = objectData.getInt("nToBeComment");
							if (nToBeComment>0) {
								textView_if_has.setVisibility(View.VISIBLE);
								textView_if_has.setText(""+nToBeComment);
							}else {
								textView_if_has.setVisibility(View.GONE);
							}
						}else if (!objectData.has("nToBeComment")) {
							textView_if_has.setVisibility(View.GONE);
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
	private void initReceiver() {
		// 广播事件**********************************************************************
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MyOrderFragment");
		// 注册广播接收器
		getActivity().registerReceiver(receiver, filter);
	}
	private void initView(View view) {
		layout_my_order_all = (LinearLayout) view.findViewById(R.id.layout_my_order_all);
		layout_my_order_process = (LinearLayout) view.findViewById(R.id.layout_my_order_process);
		layout_my_order_evaluate = (LinearLayout) view.findViewById(R.id.layout_my_order_evaluate);
		
		ib_titlebar_back = (ImageButton) view.findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) view.findViewById(R.id.tv_titlebar_title);
		textView_my_order_all = (TextView) view.findViewById(R.id.textView_my_order_all);
		textView_my_order_process = (TextView) view.findViewById(R.id.textView_my_order_process);
		textView_my_order_evaluate = (TextView) view.findViewById(R.id.textView_my_order_evaluate);
		textView_if_has = (TextView) view.findViewById(R.id.textView_if_has);
		
		
		tv_titlebar_title.setText("我的订单");
		ib_titlebar_back.setVisibility(View.GONE);
	}
	
	
	private void initListener() {
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mainActivity.finish();
			}
		});
		//全部
		layout_my_order_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AllMyOrder();
			}
		});
		layout_my_order_process.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ProcessMyOrder();
			}

			
		});
		layout_my_order_evaluate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EvaluateMyOrder();
			}

			
		});
	}
	private void AllMyOrder() {
		changeFragment(new AllMyOrderFragment());
//		textView_my_order_all.setTextColor(Color.parseColor("#FF9942"));
		textView_my_order_all.setTextColor(mainActivity.getResources().getColor(R.color.orange));
		textView_my_order_all.setBackgroundResource(R.drawable.order_choose_back);
//		textView_my_order_process.setTextColor(Color.BLACK);
		textView_my_order_process.setTextColor(mainActivity.getResources().getColor(R.color.a666666));
		textView_my_order_process.setBackgroundColor(Color.parseColor("#FFFFFF"));
		textView_my_order_evaluate.setTextColor(mainActivity.getResources().getColor(R.color.a666666));
//		textView_my_order_evaluate.setTextColor(Color.BLACK);
		textView_my_order_evaluate.setBackgroundColor(Color.parseColor("#FFFFFF"));
	}
	private void ProcessMyOrder() {
		changeFragment(new ProcessMyOrderFragment());
//		textView_my_order_all.setTextColor(Color.BLACK);
		textView_my_order_all.setTextColor(mainActivity.getResources().getColor(R.color.a666666));
		textView_my_order_all.setBackgroundColor(Color.parseColor("#FFFFFF"));
//		textView_my_order_process.setTextColor(Color.parseColor("#FF9942"));
		textView_my_order_process.setTextColor(mainActivity.getResources().getColor(R.color.orange));
		textView_my_order_process.setBackgroundResource(R.drawable.order_choose_back);
//		textView_my_order_evaluate.setTextColor(Color.BLACK);
		textView_my_order_evaluate.setTextColor(mainActivity.getResources().getColor(R.color.a666666));
		textView_my_order_evaluate.setBackgroundColor(Color.parseColor("#FFFFFF"));
	}
	private void EvaluateMyOrder() {
		changeFragment(new EvaluateMyOrderFragment());
//		textView_my_order_all.setTextColor(Color.BLACK);
		textView_my_order_all.setTextColor(mainActivity.getResources().getColor(R.color.a666666));
		textView_my_order_all.setBackgroundColor(Color.parseColor("#FFFFFF"));
		textView_my_order_process.setTextColor(mainActivity.getResources().getColor(R.color.a666666));
//		textView_my_order_process.setTextColor(Color.BLACK);
		textView_my_order_process.setBackgroundColor(Color.parseColor("#FFFFFF"));
		textView_my_order_evaluate.setTextColor(mainActivity.getResources().getColor(R.color.orange));
//		textView_my_order_evaluate.setTextColor(Color.parseColor("#FF9942"));
		textView_my_order_evaluate.setBackgroundResource(R.drawable.order_choose_back);
	}
	private void changeFragment(Fragment fragment) {
		getChildFragmentManager().beginTransaction().replace(R.id.layout_my_order,fragment).commit();
	}
	
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		MobclickAgent.onPageStart("MyOrderFragment"); //统计页面
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickAgent.onPageEnd("MyOrderFragment"); 
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}
	//避免有遗漏每次刷新列表再单独处理下
	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			if (bundle!=null) {
				int nToBeComment = bundle.getInt("nToBeComment");
				textView_if_has.setVisibility(View.VISIBLE);
				textView_if_has.setText(""+nToBeComment);
			}else {
				textView_if_has.setVisibility(View.GONE);
			}
		}
		
	}
}
