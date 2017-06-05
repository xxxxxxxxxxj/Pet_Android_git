package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CityAdapter;
import com.haotang.pet.entity.City;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CitysActivity extends SuperActivity {
	private ImageButton ibBack;
	private TextView tvTitle;
	private TextView tvCitydefault;
	private PullToRefreshListView prlList;
	private CityAdapter adapter;
	private ArrayList<City> citysList = new ArrayList<City>();
	private MProgressDialog pDialog;
	private String citydefault;
	private String lastcity;
	private boolean isFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.citylist);
		
		findView();
		setView();
	}

	private void findView() {
		spUtil = SharedPreferenceUtil.getInstance(this);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		
		rlcitydefault = (RelativeLayout) findViewById(R.id.rl_citylist_defaultcity);
		ivcitydefault = (ImageView) findViewById(R.id.iv_city_selected);
		tvCitydefault = (TextView) findViewById(R.id.tv_citylist_defaultcity);
		prlList = (PullToRefreshListView) findViewById(R.id.prl_citylist_list);
		
		
	}

	private void setView() {
		tvTitle.setText("选择城市");
		lastcity = getIntent().getStringExtra("lastcity");
		citydefault = spUtil.getString("city", "");
		if(!"".equals(citydefault)){
			rlcitydefault.setEnabled(true);
			tvCitydefault.setText(citydefault);
//			tvCitydefault.setText("黄山");
			ivcitydefault.setBackgroundResource(R.drawable.icon_city_selected);
		}else{
			rlcitydefault.setEnabled(false);
			ivcitydefault.setBackgroundResource(R.drawable.bk_empty);
			tvCitydefault.setText("定位失败");
		}
		pDialog = new MProgressDialog(this);
		
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		rlcitydefault.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!serviceOpen(citysList, citydefault)){
					showNoServiceCityDialog();
				}else{
					
					Intent data = new Intent();
					data.putExtra("city", tvCitydefault.getText().toString());
					setResult(Global.RESULT_OK, data);
					finishWithAnimation();
				}
				
			}
		});
		
		prlList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				//下拉刷新
				citysList.clear();
				adapter.notifyDataSetChanged();
				getCity();
			}
		});
		prlList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				City citem = (City) adapter.getItem(position-1);
				Intent data = new Intent();
				data.putExtra("city", citem.name);
				setResult(Global.RESULT_OK, data);
				finishWithAnimation();
			}
		});
		
		adapter = new CityAdapter(this, citysList,lastcity);
		prlList.setAdapter(adapter);
		
		getCity();
	}
	
	private void getCity(){
		pDialog.setMessage("玩命加载中...");
		pDialog.showDialog();
		CommUtil.getCitys(this,citysHanler);
	}
	
	private AsyncHttpResponseHandler citysHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("城市列表："+new String(responseBody));
			prlList.onRefreshComplete();
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONArray jData = jobj.getJSONArray("data");
					for(int i = 0; i < jData.length(); i++){
						JSONObject jcity = jData.getJSONObject(i);
						citysList.add(City.json2City(jcity));
					}
					
					adapter.notifyDataSetChanged();
					
					if(isFirst){
						isFirst = false;
						if(!serviceOpen(citysList, citydefault))
							showNoServiceCityDialog();
					}
					
				}else{
					if(jobj.has("msg")&&!jobj.isNull("msg")){
						String msg = jobj.getString("msg");
						ToastUtil.showToastShort(CitysActivity.this, msg);
						
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
			prlList.onRefreshComplete();
			pDialog.closeDialog();
		}
		
	};
	private SharedPreferenceUtil spUtil;
	private ImageView ivcitydefault;
	private RelativeLayout rlcitydefault;
	//返回true为开通，false为未开通
	private boolean serviceOpen(ArrayList<City> list, String city){
		boolean flag = false;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).name.contains(city)||city.contains(list.get(i).name)){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	private void showNoServiceCityDialog(){
		MDialog mDialog = new MDialog.Builder(this)
		.setTitle("提示")
		.setType(MDialog.DIALOGTYPE_ALERT)
		.setMessage("您所在的城市尚未开通服务，敬请期待")
		.setOKStr("确定").positiveListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		}).build();
		mDialog.show();
	}
	
}
