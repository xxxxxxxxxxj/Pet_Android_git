package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CouponAdapter;
import com.haotang.pet.entity.MyCouponCanUse;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MCommonNotification;
import com.umeng.analytics.MobclickAgent;

public class MyCouponActivity extends SuperActivity {
	public static MyCouponActivity myCouponActivity;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	
	private EditText editText_write_num;
	private Button button_put_code;
	
	private PullToRefreshListView listView_show_mycoupon;
	private static CouponAdapter adapter;
//	private List<Map<String, Object>> couponList = new ArrayList<Map<String, Object>>();
	private MProgressDialog pDialog;
	private static int page=1;
	private static ArrayList<MyCouponCanUse> MyCouponCanUseList = new ArrayList<MyCouponCanUse>();
	private static ArrayList<MyCouponCanUse> LastCanUseList = new ArrayList<MyCouponCanUse>();
//	---
	private LinearLayout layout_coupon_show_my;
	private LinearLayout layout_find_time_out;
	private RelativeLayout no_has_data;
	private TextView tv_null_msg1;
	
	private LinearLayout layout_mycoupon_unlogin;
	private Button  button_mycoupon_login;
	private View footer;
	private LinearLayout footerLiner;
	private LinearLayout layout_mycoupon;
	private ListView listView = null;
	private static double pageSize = 10;
//	---
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_coupon);
//		Toast.makeText(this, SharedPreferenceUtil.getInstance(this).getInt("userid", 0)+"", Toast.LENGTH_SHORT).show();
		MyCouponCanUseList.clear();
		myCouponActivity = this;
		page = 1;
		initView();
		setMode();
		initListener();
		
		setData();
	}

	private void setData() {
		adapter = new CouponAdapter(MyCouponActivity.this,MyCouponCanUseList);
		listView = listView_show_mycoupon.getRefreshableView();
		footer.setVisibility(View.GONE);
		listView.addFooterView(footer);
		
		
		listView_show_mycoupon.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
					// 下拉刷新
					page = 1;
					MyCouponCanUseList.clear();
					adapter.notifyDataSetChanged();
					getAllCoupons(page);
				} else {
					
					try {
						if (MyCouponCanUseList.size()>0&&LastCanUseList.size()<pageSize) {
//							MyCouponCanUseList.removeAll(removeList);//这个为啥没效果。...静态化之后内存地址不一致移除不了应该是这样
							ArrayList<MyCouponCanUse> arrayList = getNewList(MyCouponCanUseList);
							MyCouponCanUseList.clear();
							MyCouponCanUseList = arrayList;
							page = page - 1;
							adapter.notifyDataSetChanged();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					getAllCoupons(page);
				}
			}
		});
		listView_show_mycoupon.setAdapter(adapter);
	}

	private void setMode() {
		listView_show_mycoupon.setMode(Mode.BOTH);
	}

	private void getAllCoupons(int page) {
		pDialog.setMessage("玩命加载中...");
		pDialog.showDialog();
		CommUtil.myacouponsunused(
				SharedPreferenceUtil.getInstance(MyCouponActivity.this)
						.getString("cellphone", "0"), Global
						.getIMEI(MyCouponActivity.this), MyCouponActivity.this,page,
				queryUserCoupons);
	}

	private AsyncHttpResponseHandler queryUserCoupons = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			listView_show_mycoupon.onRefreshComplete();
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0&&!jsonObject.has("data")&&MyCouponCanUseList.size()>0) {
					footer.setVisibility(View.VISIBLE);//翻页无卷
					footerLiner.setVisibility(View.VISIBLE);//翻页无卷
					layout_find_time_out.setVisibility(View.GONE);
				}
				if (code==0&&!jsonObject.has("data")&&MyCouponCanUseList.size()<=0) {
					footerLiner.setVisibility(View.GONE);//初次无卷
					footer.setVisibility(View.GONE);
					listView.removeFooterView(footer);
					layout_find_time_out.setVisibility(View.VISIBLE);
				}
				if (code == 0&&jsonObject.has("data")&&!jsonObject.isNull("data")) {
					footerLiner.setVisibility(View.GONE);
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (jsonArray.length()>0) {
						footer.setVisibility(View.VISIBLE);
						LastCanUseList.clear();
						page++;
						for (int i = 0; i < jsonArray.length(); i++) {
							MyCouponCanUseList.add(MyCouponCanUse.json2Entity(jsonArray.getJSONObject(i)));
							pageSize = MyCouponCanUseList.get(0).pageSize;
							LastCanUseList.add(MyCouponCanUse.json2Entity(jsonArray.getJSONObject(i)));
						}
					}
					Utils.mLogError("==-->jsonArray.length():= "+jsonArray.length());
					adapter.notifyDataSetChanged();
					if (jsonArray.length()<pageSize) {
						footerLiner.setVisibility(View.VISIBLE);//翻页少于10条显示（每页显示10条不够后续没有）
					}
					if(page==1){
						if (MyCouponCanUseList.size()<=0) {
							listView_show_mycoupon.setVisibility(View.GONE);
							listView.removeFooterView(footer);
							layout_find_time_out.setVisibility(View.VISIBLE);
						}
					}else {
						layout_find_time_out.setVisibility(View.GONE);
					}
				}else {
					if(page==1){
//						showMain(false);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				if(page==1){
//					showMain(false);
				}
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			listView_show_mycoupon.onRefreshComplete();
			listView.removeFooterView(footer);
		}
	};

	private void showToast(String code) {
		Toast.makeText(MyCouponActivity.this, code, Toast.LENGTH_SHORT).show();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initListener() {
		
		
		button_put_code.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				CommUtil.redeemCouponCode(SharedPreferenceUtil.getInstance(MyCouponActivity.this)
						.getString("cellphone", "0"), Global.getIMEI(MyCouponActivity.this), MyCouponActivity.this, editText_write_num.getText().toString(), redeemCouponCode);
			}
		});
		ib_titlebar_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				goBack();
			}
		});
		listView_show_mycoupon.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MyCouponCanUse canUse = (MyCouponCanUse) arg0.getItemAtPosition(arg2);
				if (canUse.isCanGive==0) {//0  可以赠送 1  不可赠送
					if (canUse.isGive==0) { // 0 没赠送过得 1 赠送过的
						Utils.mLogError("==-->canUse.CouponId:="+canUse.CouponId);
						Intent intent = new Intent(mContext, MyCouponGiveOtherActivity.class);
						intent.putExtra("canUse", canUse);
						intent.putExtra("position", arg2-1);
						Utils.mLogError("==-->removePosition 0 := "+(arg2-1));
						startActivity(intent);
					}else if (canUse.isGive==1) {
//						ToastUtil.showToastShortCenter(mContext, "该优惠券好友赠送不可再次赠送");
					}
				}else if (canUse.isCanGive==1) {
//					ToastUtil.showToastShortCenter(mContext, "该优惠券不可赠送");
				}
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
		
		footerLiner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goNext();
			}
		});
		
		button_mycoupon_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyCouponActivity.this, LoginActivity.class);
				intent.putExtra("previous", Global.MYCOUPON_UNLOGIN);
				startActivity(intent);
			}
		});
		
		layout_find_time_out.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goNext();
			}
		});
	}

	private AsyncHttpResponseHandler redeemCouponCode = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->redeem "+new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					ToastUtil.showToastShortCenter(MyCouponActivity.this, jsonObject.getString("msg"));
					MyCouponCanUseList.clear();
//					Utils.mLogError("==-->couponList code 1 := "+couponList.size());
					page = 1;
					getAllCoupons(page);
				}else {
					ToastUtil.showToastShortCenter(MyCouponActivity.this, jsonObject.getString("msg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
		
	};
	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		editText_write_num = (EditText) findViewById(R.id.editText_write_num);
		button_put_code = (Button) findViewById(R.id.button_put_code);
		listView_show_mycoupon = (PullToRefreshListView) findViewById(R.id.listView_show_mycoupon);
		layout_find_time_out = (LinearLayout) findViewById(R.id.layout_find_time_out);
		no_has_data = (RelativeLayout) findViewById(R.id.rl_null);
		tv_null_msg1 = (TextView) findViewById(R.id.tv_null_msg1);
		layout_mycoupon_unlogin = (LinearLayout) findViewById(R.id.layout_mycoupon_unlogin);
		button_mycoupon_login = (Button) findViewById(R.id.button_mycoupon_login);

		tv_titlebar_title.setText("我的优惠券");
		pDialog = new MProgressDialog(this);
		footer = LayoutInflater.from(this).inflate(R.layout.footer_my_coupon,null);
		footerLiner = (LinearLayout) footer.findViewById(R.id.layout_find_time_out);
		layout_mycoupon = (LinearLayout)findViewById(R.id.layout_mycoupon);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (checklogin()) {
			layout_mycoupon.setVisibility(View.VISIBLE);
			listView_show_mycoupon.setVisibility(View.VISIBLE);
			layout_mycoupon_unlogin.setVisibility(View.GONE);
			getAllCoupons(page);
		}else {
			layout_mycoupon_unlogin.setVisibility(View.VISIBLE);
			layout_mycoupon_unlogin.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return true;
				}
			});
		}
	    MobclickAgent.onPageStart("MyCouponActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	    MobclickAgent.onResume(this);          //统计时长
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	    MobclickAgent.onPageEnd("MyCouponActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}
	private void goNext(){
		Intent intent = new Intent(this, MyTimeOutCouponActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}
	/*
	 * check log in or not
	 */
	private boolean checklogin(){
		boolean ifLogin = false;
		String str = SharedPreferenceUtil.getInstance(this).getString("cellphone", "0");
		if (str.equals("0")) {
			ifLogin = false;
		}else {
			ifLogin = true;
		}
		return ifLogin;
	}
	private void goBack(){
		setResult(Global.RESULT_OK);
		finishWithAnimation();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}
	public static void delteCoupon(int removePosition){
		page = 1;
		LastCanUseList.clear();
		if (removePosition!=-1) {
			MyCouponCanUseList.remove(removePosition);
			ToastUtil.showToastShortCenter(myCouponActivity, "优惠券已赠送给您的好友");
		}
		double lastSize  = MyCouponCanUseList.size() % pageSize;
		double countPage = 0 ;
		if (lastSize==0) {
			countPage = Math.ceil(MyCouponCanUseList.size() / pageSize);
			page = (int) (countPage+1);
			page+=1;
		}else {
			countPage = Math.ceil(MyCouponCanUseList.size() / pageSize);
			page = (int) countPage;
			page+=1;
		}
		Utils.mLogError("==-->lastSize 0:= "+lastSize+" countPage:= "+countPage+"  list:= "+MyCouponCanUseList.size());
		for (int i = (int) (MyCouponCanUseList.size()-LastCanUseList.size()); i < MyCouponCanUseList.size(); i++) {
			LastCanUseList.add(MyCouponCanUseList.get(i));
		}
		Utils.mLogError("==-->lastSize 1:= "+LastCanUseList.size());
		Utils.mLogError("==-->lastSize 2:= "+page);
		adapter.setNotif();
	}
	public ArrayList<MyCouponCanUse> getNewList(ArrayList<MyCouponCanUse> li){
        ArrayList<MyCouponCanUse> list = new ArrayList<MyCouponCanUse>();
        for(int i=0; i<li.size(); i++){
            long couponid = li.get(i).CouponId;  //获取传入集合对象的每一个元素
            if(list.get(i).CouponId != couponid){   //查看新集合中是否有指定的元素，如果没有则加入
                list.add(list.get(i));
            }
        }
        return list;  //返回集合
    }
}
