package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.MainToListAdapter;
import com.haotang.pet.entity.HostPitalAndOther;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

public class MainToList extends SuperActivity{

	private MainToList mainToList;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private PullToRefreshListView listView_show_main_to_list;
	private ArrayList<HostPitalAndOther> listDatas = new ArrayList<HostPitalAndOther>();
	private MainToListAdapter<HostPitalAndOther> adapter;
	private int type = 0 ;
	private MProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_to_list);
		mainToList = this;
		listDatas.clear();
		initView();
		setView();
		initListener();
	}
	private void initListener() {
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
		listView_show_main_to_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mainToList,JointWorkShopDetail.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				HostPitalAndOther pitalAndOther = (HostPitalAndOther) parent.getItemAtPosition(position);
				intent.putExtra("id", pitalAndOther.id);
				if (type==5) {
					type = 3;
				}
				intent.putExtra("type", type);
				startActivity(intent);
			}
		});
	}
	private void setView() {
		type = getIntent().getIntExtra("type", 1);
		listView_show_main_to_list.setMode(Mode.DISABLED);
		listView_show_main_to_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					
				}else{
					
				}
			}
		});
		adapter = new MainToListAdapter<HostPitalAndOther>(mainToList, listDatas);
		listView_show_main_to_list.setAdapter(adapter);
		
		
		getData();
	}
	private void getData() {
		// TODO Auto-generated method stub
		switch (type) {
		case 1:
			tv_titlebar_title.setText("摄影");
			CommUtil.getPhoto_List(this,getHospital_List);
			break;
		case 2:
			tv_titlebar_title.setText("娱乐");
			CommUtil.getEntertain_List(this,getHospital_List);
			break;
		case 3:
			tv_titlebar_title.setText("医疗");
			CommUtil.getHospital_List(this,getHospital_List);
			break;
		case 4:
			tv_titlebar_title.setText("训练");
			CommUtil.getTrain_List(this,getHospital_List);
			break;
		case 5:
			tv_titlebar_title.setText("推荐医院");
			CommUtil.getHospital_List(this,getHospital_List);
			break;
		}
	}
	private AsyncHttpResponseHandler getHospital_List = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				dialog.closeDialog();
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONObject objectData = jsonObject.getJSONObject("data");
						if (objectData.has("list")&&!objectData.isNull("list")) {
							JSONArray array = objectData.getJSONArray("list");
//							Utils.mLogError("array-----------"+array.length());
							if (array.length()>0) {
								for (int i = 0; i < array.length(); i++) {
									listDatas.add(HostPitalAndOther.json2Entity(array.getJSONObject(i)));
								}
								adapter.notifyDataSetChanged();
							}
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					dialog.closeDialog();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			try {
				dialog.closeDialog();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	};
	private void initView() {
		dialog = new MProgressDialog(mainToList);
		dialog.showDialog();
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		listView_show_main_to_list = (PullToRefreshListView) findViewById(R.id.listView_show_main_to_list);
	}
}
