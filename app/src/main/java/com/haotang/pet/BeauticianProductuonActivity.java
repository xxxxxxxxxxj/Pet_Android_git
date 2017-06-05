package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BeauticianProductionAdapter;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.entity.Production;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import android.os.Bundle;
import android.provider.Settings.Global;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class BeauticianProductuonActivity extends SuperActivity {
	private ImageButton ibBack;
	private TextView tvTitle;
	private PullToRefreshListView prlListView;
	public static ArrayList<Production> productionList = new ArrayList<Production>();
	private BeauticianProductionAdapter adapter;
	private MProgressDialog pDialog;
	private int pagesize = 10;
	private int beauticianid;
	private int serviceid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beauticianproduction);
		
		findView();
		setView();
	}

	private void findView() {
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		
		prlListView = (PullToRefreshListView) findViewById(R.id.prl_beauticianproduction_list);
		pDialog = new MProgressDialog(this);
	}

	private void setView() {
		tvTitle.setText("作品列表");
		
		beauticianid = getIntent().getIntExtra("beautician_id", 0);
		serviceid = getIntent().getIntExtra("serviceid", -1);
		prlListView.setMode(Mode.BOTH);
		adapter = new BeauticianProductionAdapter(this,getDisplayWidth(), productionList);
		prlListView.setAdapter(adapter);
		prlListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					
					Utils.mLogError("下拉刷新");
					productionList.clear();
					adapter.notifyDataSetChanged();
					if (serviceid!=-1) {
						getData(serviceid, 0, 0);
					}else {
						getData(beauticianid, 0, 0);
					}
					
				}else{
					Utils.mLogError("加载更多");
					try {
						if (serviceid!=-1) {
							getData(serviceid, productionList.get(productionList.size()-1).id, 0);
						}else {
							getData(beauticianid, productionList.get(productionList.size()-1).id, 0);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 返回
				finishWithAnimation();
			}
		});
		productionList.clear();
		if (serviceid!=-1) {
			getData(serviceid, 0, 0);
		}else {
			getData(beauticianid, 0, 0);
		}
		
	}
	
	private int getDisplayWidth(){
		int[] display = Utils.getDisplayMetrics(this);
		int border = Utils.dip2px(this, 30);
		return display[0]/2 - border/2;
	}
	
	private void getData(int workerid, int beforeid,int afterid){
		pDialog.setMessage("玩命加载中...");
		pDialog.showDialog();
		if (serviceid!=-1) {
			CommUtil.queryWorksByService(this,serviceid, beforeid, afterid, productionHanler);
		}else {
			CommUtil.getProductionByBeauticianId(this,workerid, beforeid, afterid, pagesize, productionHanler);
		}
	}
	
	private AsyncHttpResponseHandler productionHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			prlListView.onRefreshComplete();
			pDialog.closeDialog();
			Utils.mLogError("美容师作品："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				
				if(0 == resultCode){
					if(jobj.has("data")&&!jobj.isNull("data")){
						JSONArray jData = jobj.getJSONArray("data");
						for(int i=0; i<jData.length();i++){
							Production pr = Production.json2Entity(jData.getJSONObject(i));
							productionList.add(pr);
						}
						adapter.notifyDataSetChanged();
						
					}
					
				}else{
					if(jobj.has("msg")&&!jobj.isNull("msg")){
						String msg = jobj.getString("msg");
						ToastUtil.showToastShort(BeauticianProductuonActivity.this, msg);
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
			prlListView.onRefreshComplete();
			pDialog.closeDialog();
			
		}
		
	};
	
}
