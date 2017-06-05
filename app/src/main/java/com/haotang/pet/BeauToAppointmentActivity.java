package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haotang.pet.entity.Beautician;
import com.haotang.pet.fragment.BeauToAppointmentFragment;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

public class BeauToAppointmentActivity extends FragmentActivity{

	public static BeauToAppointmentActivity act;
	private ArrayList<Beautician> list = new ArrayList<Beautician>();
	private ViewPager vp_beauToAppoiment ;
	private int num = 0;
	private MyPagerAdapter adapter;
	private double sizeNum = 6.0;
	private LinearLayout point;
	private ArrayList<View> dots;
	private int oldPostion;
	
	private SharedPreferenceUtil spUtil;
	private int areaid;
	private int shopid;
	private boolean isshop;
	private boolean ishome;
	private int previous;
	private int beautician_id;
	private String beautician_name;
	private String beautician_iamge;
	private int beautician_ordernum;
	private int gender;
	private int sort;
	private String levelname;
	private int shopServiceSchedule = 0 ;
	private int homeServiceSchedule = 0 ;
	private ImageView beauappoiment_close;
	private String areaname="";
	private LinearLayout layout_beauappoinment;
	private String upgradeTip=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_beauappoinment);
		act = this;
		findView();
		getIntentData();
		setAdapter();
		getData();
		initListener();
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		beauappoiment_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		layout_beauappoinment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void getIntentData() {
		Intent intent = getIntent();
		isshop = intent.getBooleanExtra("isshop", true);
		ishome = intent.getBooleanExtra("ishome", true);
		areaid = intent.getIntExtra("areaid", 0);
		shopid = intent.getIntExtra("shopid", 0);
		previous = intent.getIntExtra("previous", 0);
		beautician_id = intent.getIntExtra("beautician_id", 0);
		beautician_name = intent.getStringExtra("beautician_name");
		areaname = intent.getStringExtra("areaname");
		beautician_iamge = intent.getStringExtra("beautician_image");
		beautician_ordernum = intent.getIntExtra("beautician_ordernum", 0);
		gender = intent.getIntExtra("beautician_sex", 0);
		sort = intent.getIntExtra("beautician_sort", 0);
		homeServiceSchedule = intent.getIntExtra("homeServiceSchedule", 0);
		shopServiceSchedule = intent.getIntExtra("shopServiceSchedule", 0);
		levelname = intent.getStringExtra("beautician_levelname");
		upgradeTip = intent.getStringExtra("upgradeTip");
	}

	private void getData() {
		CommUtil.queryWorkerMenuItems(this,homeServiceSchedule,shopServiceSchedule,beautician_id,queryWorkerMenuItems);
	}
	private AsyncHttpResponseHandler queryWorkerMenuItems = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("======-> "+new String(responseBody));
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONArray array = jsonObject.getJSONArray("data");
						if (array.length()>0) {
							for (int i = 0; i < array.length(); i++) {
								list.add(Beautician.json2Entity(array.getJSONObject(i)));
							}
						}
					}
					if (list.size()>0) {
						setView();
					}
					adapter.notifyDataSetChanged();
				}else {
					String msg = jsonObject.getString("msg");
					ToastUtil.showToastShortCenter(act, msg);
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
	private void setView() {
	    dots = new ArrayList<View>();
		//请求接口判断返回的数据 icon 有几个
		num = (int) Math.ceil(list.size()/sizeNum);
		dots.clear();
		point.removeAllViews();
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(15,15);
		params.leftMargin = 10;
		params.width = 20;
		params.height=20;
		if (num>1) {
			for (int i = 0; i < num; i++) {
	    		if (i==0) {
	    			View view = new View(act);
	    			view.setBackgroundResource(R.drawable.dot_focused_beau);
	    			view.setPadding(10, 0, 10, 0);
	    			view.setLayoutParams(params);
	    			point.addView(view);
	    			dots.add(view);
	    		}else {
	    			View view = new View(act);
	    			view.setBackgroundResource(R.drawable.dot_normal_beau);
	    			view.setPadding(10, 0, 10, 0);
	    			view.setLayoutParams(params);
	    			point.addView(view);
	    			dots.add(view);
	    		}
	    	}
		}
	}
	
	public void setAdapter(){
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		vp_beauToAppoiment.setAdapter(adapter);
		vp_beauToAppoiment.setOnPageChangeListener(new MPageChangeListener());
	}
	private void findView() {
		spUtil = SharedPreferenceUtil.getInstance(this);
		vp_beauToAppoiment = (ViewPager) findViewById(R.id.vp_beauToAppoiment);
		point = (LinearLayout) findViewById(R.id.point);
		beauappoiment_close = (ImageView) findViewById(R.id.beauappoiment_close);
		layout_beauappoinment = (LinearLayout) findViewById(R.id.layout_beauappoinment);
	}
	
	class MyPagerAdapter extends FragmentPagerAdapter{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			ArrayList<Beautician> mDatasLast = new ArrayList<Beautician>();
			num = position+1;
			try {
				for (int i = (num-1)*6; i < num*6; i++) {
					Utils.mLogError("==--> aaa "+i );
					mDatasLast.add(list.get(i));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.mLogError("==-->"+e.getMessage()+" "+e.getCause());
			}
			
			return new BeauToAppointmentFragment(mDatasLast,areaid,shopid,isshop,ishome,previous,beautician_id,areaname,beautician_name,beautician_iamge,beautician_ordernum,gender,sort,levelname,upgradeTip);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return num;
		}
		
	}
	
	private class MPageChangeListener implements OnPageChangeListener {
		/**
		 * 页面状态改变执行的方法
		 * */
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
		/**
		 * 页面选中以后执行的方法
		 * */
		@Override
		public void onPageSelected(int position) {
			// 圆点更新
			// 更新当前页面为白色的圆点
			dots.get(position % dots.size()).setBackgroundResource(
					R.drawable.dot_focused_beau);
			// 更新上一个页面为灰色的圆点
			dots.get(oldPostion % dots.size()).setBackgroundResource(
					R.drawable.dot_normal_beau);
			// 更新上一个页面的位置
			oldPostion = position;
		}

	}

}
