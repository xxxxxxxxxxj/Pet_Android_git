package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.KnowledgeAdapter;
import com.haotang.pet.entity.Knowledge;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.TopIndicator;
import com.haotang.pet.view.TopIndicator.OnTopIndicatorListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class KnowledgeActivity extends SuperActivity implements OnTouchListener,
OnTopIndicatorListener{
	private View view;
	private TextView tvTitle;
	private ImageButton ibBack;
	private TopIndicator tidTopTab;
	private PullToRefreshListView prlList;
	private MProgressDialog pDialog;
	private ArrayList<Knowledge> list = new ArrayList<Knowledge>();
	private int oldindex = 0;
	private SharedPreferenceUtil spUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.knowledgefragment);
		
		findView();
		setView();
	}

	private void findView() {
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tidTopTab = (TopIndicator) findViewById(R.id.tid_knowledgefragment_topindicator);
		tidTopTab.setOnTopIndicatorListener(this);
		prlList = (PullToRefreshListView) findViewById(R.id.prl_knowledgefragment_list);
		prlList.setMode(Mode.PULL_FROM_START);
		adapter = new KnowledgeAdapter(this, list);
		
		sendEvent();
	}

	private void setView() {
		tvTitle.setText("知识库");
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		prlList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				Mode mode = refreshView.getCurrentMode();
				if(mode == mode.PULL_FROM_START){
					//下拉刷新
					Utils.mLogError("下拉刷新");
					list.clear();
					adapter.notifyDataSetChanged();
					getData(oldindex);
				}else{
					//加载更多
				}
			}
		});
		prlList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Knowledge knowledge = (Knowledge) adapter.getItem(position-1);
				Intent intent = new Intent(KnowledgeActivity.this, KnowledgeDetailActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				KnowledgeActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				intent.putExtra("title", knowledge.title);
				intent.putExtra("path", knowledge.path);
				startActivity(intent);
			}
		});
		prlList.setAdapter(adapter);
		
		getData(0);
		
	}

	@Override
	public void onIndicatorSelected(int index) {
		// TODO Auto-generated method stub
		if(oldindex != index){
			oldindex = index;
			tidTopTab.setTabsDisplay(this, index);
			list.clear();
			adapter.notifyDataSetChanged();
			getData(index);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private void getData(int index){
		pDialog.setMessage("玩命加载中...");
		pDialog.showDialog();
		long time = System.currentTimeMillis();
		switch (index) {
		case 0:
			CommUtil.getWY(this,time,KnowledgeHanler);
			break;
		case 1:
			CommUtil.getHL(this,time,KnowledgeHanler);
			break;
		case 2:
			CommUtil.getJK(this,time,KnowledgeHanler);
			break;
		case 3:
			CommUtil.getYP(this,time,KnowledgeHanler);
			break;
		case 4:
			CommUtil.getXL(this,time,KnowledgeHanler);
			break;
		}
		
	}
	
	private void sendEvent(){
		CommUtil.recorder(this,spUtil.getString("cellphone", ""), Global.getIMEI(this),
				Global.getCurrentVersion(this), "4", 0, 
				spUtil.getInt("userid", 0), 0, 0, eventHanler);
	}
	
	private AsyncHttpResponseHandler KnowledgeHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
//			Utils.mLogError("知识库列表："+new String(responseBody));
			prlList.onRefreshComplete();
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if(0 == resultCode){
					if(jobj.has("data")&&!jobj.isNull("data")){
						JSONArray jDataArr = jobj.getJSONArray("data");
						for(int i=0;i<jDataArr.length();i++){
							Knowledge konwledge = Knowledge.json2Entity(jDataArr.getJSONObject(i));
							list.add(konwledge);
						}
						adapter.notifyDataSetChanged();
					}
				}else{
					ToastUtil.showToastShort(KnowledgeActivity.this, msg);
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
	private AsyncHttpResponseHandler eventHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
		
	};
	private KnowledgeAdapter adapter;
	
	
}
