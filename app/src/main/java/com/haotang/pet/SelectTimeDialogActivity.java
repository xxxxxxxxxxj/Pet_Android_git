package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.TimeAdapter;
import com.haotang.pet.entity.MDay;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

public class SelectTimeDialogActivity extends SuperActivity implements OnClickListener{
	private int[][] times = new int[31][24];
	private ArrayList<MDay> daylist = new ArrayList<MDay>();
	private ArrayList<MDay> availdaylist = new ArrayList<MDay>();
	private ArrayList<String> listAvailTime = new ArrayList<String>();
	private ArrayMap<String, int[]> mapTime = new ArrayMap<String, int[]>();
	private int[] daystatus = new int[31];
	private String[] strTimes;
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;
	private Button ibClose;
	private HorizontalScrollView mHorizontalScrollView;
	private LinearLayout llDaysContent;
	private ImageView ivPreDay;
	private ImageView ivNextDay;
	private TextView tvTitle;
	private GridView gvTime;
	private Intent fIntent;
	private int serviceid;
	private int shopid;
	private int areaid;
	private int serviceloc;
	private int beauticianId;
	private String strp;
	
	private int currentday;
	private String selectedDate="";
	private String date="";
	private int screenwidth;
	private int itemwidth;
	private TimeAdapter adapterTime;
	private String beauticianName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.selecttime_dialog);
		findView();
		setView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		ibClose = (Button) findViewById(R.id.iv_selecttime_dialog_close);
		tvTitle = (TextView) findViewById(R.id.tv_selecttime_dialog_name);
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_selecttime_dialog_scrollview);
		llDaysContent = (LinearLayout) findViewById(R.id.ll_selecttime_dialog_content);
		ivPreDay = (ImageView) findViewById(R.id.iv_selecttime_dialog_arrowleft);
		ivNextDay = (ImageView) findViewById(R.id.iv_selecttime_dialog_arrowright);
		gvTime = (GridView) findViewById(R.id.gv_selecttime_dialog);
		
	}

	private void setView() {
		// TODO Auto-generated method stub
		strTimes = new String[]{"9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00",
				"13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30",
				"18:00","18:30","19:00","19:30","20:00","20:30","21:00"};
		for(int i=0;i<daystatus.length;i++){
			daystatus[i]=1;
		}
		for(int i=0;i<times.length;i++){
			for(int j=0;j<times[i].length;j++){
				times[i][j]=1;
			}
		}
		ibClose.setOnClickListener(this);
		ivNextDay.setOnClickListener(this);
		ivPreDay.setOnClickListener(this);
		
		screenwidth = Utils.getDisplayMetrics(this)[0]-Utils.dip2px(this, 60);
		itemwidth = (int) ((screenwidth/ 5.0 + 0.5f));
		fIntent = getIntent();
		serviceid = fIntent.getIntExtra("serviceid", -1);
		serviceloc = fIntent.getIntExtra("serviceloc", 2);
		shopid = fIntent.getIntExtra("shopid", -1);
		areaid = fIntent.getIntExtra("areaid", -1);
		beauticianId = fIntent.getIntExtra("beautician_id", 0);
		strp = fIntent.getStringExtra("strp");
		beauticianName = fIntent.getStringExtra("beautician_name");
		if(beauticianName!=null&&!"".equals(beauticianName)){
			tvTitle.setText(beauticianName+"的可约时间");
		}
		adapterTime = new TimeAdapter(this, listAvailTime);
		android.view.ViewGroup.LayoutParams lp = gvTime.getLayoutParams();
		
		gvTime.setAdapter(adapterTime);
		int screenWidth = Utils.getDisplayMetrics(this)[0];
		int width = screenWidth - Utils.dip2px(this, 30);
		adapterTime.setWidth(width);
		lp.height = screenWidth/2;
		gvTime.setLayoutParams(lp);
		gvTime.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				String tmpTime = listAvailTime.get(position);
				
				Intent data = new Intent();
//				data.putExtra("position", currentday);
				if(tmpTime.split(":")[0].length()<2){
					data.putExtra("time", selectedDate+" 0"+tmpTime);
					data.putExtra("date", date+" 0"+tmpTime+":00");
				}else{
					data.putExtra("time", selectedDate+" "+tmpTime);
					data.putExtra("date", date+" "+tmpTime+":00");
				}
				
				setResult(Global.RESULT_OK, data);
				finishWithAnimation();
				
			}
		});
		
		getData(beauticianId);
	}
	private void getData(int beauticianid){
		pDialog.showDialog();
		
		CommUtil.getBeauticianTimeById(this,0,0,spUtil.getString("cellphone", ""),
				Global.getIMEI(this),Global.getCurrentVersion(this),
				areaid,serviceloc,shopid,beauticianid,strp, timeHanler);
			
	}
	
	private AsyncHttpResponseHandler timeHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("时间列表："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jData = jobj.getJSONObject("data");
					
					if(jData.has("calendar")&&!jData.isNull("calendar")){
						JSONArray jday = jData.getJSONArray("calendar");
						for(int i = 0; i < jday.length(); i++){
							MDay jmditem = MDay.json2Entity(jday.getJSONObject(i));
							daylist.add(jmditem);
						}
					}
					if(jData.has("fullList")&&!jData.isNull("fullList")){
						JSONArray statusarr = jData.getJSONArray("fullList");
						for(int i = 0; i < statusarr.length(); i++){
							daystatus[i] = statusarr.getInt(i);
						}
					}
					if(jData.has("selection")&&!jData.isNull("selection")){
						JSONArray jtime = jData.getJSONArray("selection");
						for(int i = 0; i < jtime.length(); i++){
							JSONArray item = jtime.getJSONArray(i);
							for(int j = 0; j < item.length(); j++){
								times[i][j] = item.getInt(j);
							}
						}
					}
					
					
					for(int i=0;i<daystatus.length;i++){
						if(daystatus[i]==0){
							availdaylist.add(daylist.get(i));
							mapTime.put(daylist.get(i).day, times[i]);
						}
					}
					
					
					initNav(availdaylist,0);
					setTime(0);
				}else{
					String msg = jobj.getString("msg");
					ToastUtil.showToastShort(SelectTimeDialogActivity.this, msg);
					finishWithAnimation();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastUtil.showToastShort(SelectTimeDialogActivity.this, "网络异常，请重试！");
				finishWithAnimation();
			}
			
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			ToastUtil.showToastShort(SelectTimeDialogActivity.this, "网络异常，请重试！");
			finishWithAnimation();
		}
		
	};
	
	private void initNav(ArrayList<MDay> list,int position) {
		for (int i = 0 ; i < list.size(); i++) {
			RelativeLayout layout = new RelativeLayout(this);
			layout.setGravity(Gravity.CENTER_VERTICAL);
			LinearLayout ll = new LinearLayout(this);
			LayoutParams llp = 
					new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setGravity(Gravity.CLIP_VERTICAL);
			TextView tvWeek = new TextView(this);
			TextView tvDay = new TextView(this);
			tvWeek.setText(list.get(i).daydes);
			tvWeek.setTextColor(getResources().getColor(R.color.orange));
			tvDay.setTextColor(getResources().getColor(R.color.orange));
			
			tvWeek.setTextSize(18);
			tvWeek.setLayoutParams(llp);
			tvWeek.setGravity(Gravity.CENTER);
			tvDay.setText(list.get(i).day);
			tvDay.setLayoutParams(llp);
			tvDay.setGravity(Gravity.CENTER);
//			tvDay.setTextSize(18);
			ll.addView(tvWeek);
			ll.addView(tvDay);
			layout.setOnClickListener(this);
			RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			params.topMargin=20;
			layout.addView(ll, params);
			layout.setId(i);
			layout.setOnClickListener(this);
			layout.setBackgroundResource(R.drawable.icon_selecttime_normal);
			if(i==position){
				layout.setBackgroundResource(R.drawable.bg_orange_round);
				tvDay.setTextColor(getResources().getColor(R.color.white));
				tvWeek.setTextColor(getResources().getColor(R.color.white));
				
			}
			llDaysContent.addView(layout, (int)(screenwidth/5 + 0.5f), LayoutParams.MATCH_PARENT);
			
		}
		currentday = position;
		selectedDate = availdaylist.get(currentday).day;
		StringBuilder sb = new StringBuilder();
		sb.append(availdaylist.get(currentday).year);
		sb.append("-");
		sb.append(selectedDate);
		date = sb.toString();
		
		setTime(position);
		
		//解决自动滚动问题
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHorizontalScrollView.smoothScrollTo((currentday-2) * itemwidth , 0);
			}
		}, 5);
		
		if(position==0){
			//左侧箭头不可用，置灰
			ivPreDay.setBackgroundResource(R.drawable.icon_selecttime_arrowleft);
		}else{
			ivPreDay.setBackgroundResource(R.drawable.icon_selecttime_arrowleft_orange);
		}
		if(position==list.size()-1){
			//右侧箭头不可用，置灰
			ivNextDay.setBackgroundResource(R.drawable.icon_selecttime_arrowright);
		}else {
			ivNextDay.setBackgroundResource(R.drawable.icon_selecttime_arrowright_orange);
		}
		
	}
	
	private void setTime(int index){
		listAvailTime.clear();
		int[] tmp = mapTime.get(availdaylist.get(index).day); 
		for(int i=0;i<tmp.length;i++){
			if(tmp[i]==0)
				listAvailTime.add(strTimes[i]);
		}
		adapterTime.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		for(int i=0;i<availdaylist.size();i++){
			if(i==v.getId()){
				llDaysContent.removeAllViews();
				initNav(availdaylist, i);
			}
		}
		switch (v.getId()) {
		case R.id.iv_selecttime_dialog_close:
			finish();
			break;
		case R.id.iv_selecttime_dialog_arrowleft:
			//左侧箭头
			if(currentday>0){
				llDaysContent.removeAllViews();
				initNav(availdaylist,currentday-1);
			}
			break;
		case R.id.iv_selecttime_dialog_arrowright:
			//右侧箭头
			if(currentday<availdaylist.size()-1){
				llDaysContent.removeAllViews();
				initNav(availdaylist,currentday+1);
			}
			break;
		}
	}
	
}
