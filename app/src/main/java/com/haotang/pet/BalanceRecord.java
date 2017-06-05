package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import u.aly.bu;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BlanceRecordAdapter;
import com.haotang.pet.adapter.OtherGoodS;
import com.haotang.pet.entity.Blance;
import com.haotang.pet.entity.OtherGoods;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MListview;

/**
 * 余额记录
 * @author zhiqiang
 * 
 * iconIndex 
 * 1 支付宝  
 * 2 微信  
 * 3 余额 
 * 4 退款  
 * 5 优惠券 
 */
public class BalanceRecord extends SuperActivity{

	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private PullToRefreshListView listView_blancerecord;
	private BlanceRecordAdapter recordAdapter;
	private SharedPreferenceUtil spUtil;
	private int page = 1;
	private List<Blance> Blancelist = new ArrayList<Blance>();
	private BalanceRecord record;
	private MProgressDialog pDialog;
	private RelativeLayout no_has_data;
	private TextView tv_null_msg1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bakance_record);
		pDialog = new MProgressDialog(this);
		record = this;
		Blancelist.clear();
		findView();
		setView();
		getData();
		initListener();
	}
	private void getData() {
		pDialog.showDialog();
		CommUtil.queryTradeHistory(spUtil.getString("cellphone", ""), Global.getIMEI(this), this, page, queryTradeHistory);
	}
	private void initListener() {
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
//		listView_blancerecord.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Blance  blance = (Blance) parent.getItemAtPosition(position);
//				blance.isOpen=!blance.isOpen;
////				List<OtherGoods> liGoods = blance.list;
//				MListview mListview = (MListview) view.findViewById(R.id.other_things);
//				if (!blance.isOpen) {
//					OtherGoodS goodS = new OtherGoodS(mContext,blance.list);
//					mListview.setAdapter(goodS);
//					mListview.setVisibility(View.VISIBLE);
//				}else {
//					mListview.setVisibility(View.GONE);
//				}
//				ArrayList<>
//				Intent intent = new Intent(record, BalanceRecordDetail.class);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("blance",blance);
//				intent.putExtras(bundle);
//				startActivity(intent);
//				JumpToNext(BalanceRecordDetail.class, blance);
//			}
//		});
	}
	private void findView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		listView_blancerecord = (PullToRefreshListView) findViewById(R.id.listView_blancerecord);
		
		no_has_data = (RelativeLayout) findViewById(R.id.rl_null);
		tv_null_msg1 = (TextView) findViewById(R.id.tv_null_msg1);

		spUtil = SharedPreferenceUtil.getInstance(this);
	}
	private void setView() {
		tv_titlebar_title.setText("交易明细");
		
		recordAdapter = new BlanceRecordAdapter(this, Blancelist);
		listView_blancerecord.setMode(PullToRefreshBase.Mode.BOTH);
		listView_blancerecord.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					page = 1;
					Blancelist.clear();
					recordAdapter.notifyDataSetChanged();
					getData();
				}else{
					getData();
				}
			}
		});
		listView_blancerecord.setAdapter(recordAdapter);
	}
	
	private AsyncHttpResponseHandler queryTradeHistory = new AsyncHttpResponseHandler()
	{

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			listView_blancerecord.onRefreshComplete();
//			pDialog.closeDialog();
			Utils.mLogError("==-->余额记录 "+ new String(responseBody) );
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONArray array = jsonObject.getJSONArray("data");
						if (array.length()>0) {
							page++;
							for (int i = 0; i < array.length(); i++) {
								Blancelist.add(Blance.json2Entity(array.getJSONObject(i)));
							}
						}
						showMain(true);
					}
					recordAdapter.notifyDataSetChanged();
					if(page==1){
						showMain(false);
					}
				}else{
					if (code!=230&&jsonObject.has("msg")&&!jsonObject.isNull("msg")) {
						ToastUtil.showToastShortCenter(record, jsonObject.getString("msg"));
					}
					if(page==1){
						showMain(false);
					}
				}
				pDialog.closeDialog();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(page==1){
					showMain(false);
				}
				pDialog.closeDialog();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			listView_blancerecord.onRefreshComplete();
//			pDialog.closeDialog();
			if(page==1){
				showMain(false);
			}
			pDialog.closeDialog();
		}
		
	};

	private void showMain(boolean ismain){
		if(ismain){
			listView_blancerecord.setVisibility(View.VISIBLE);
			no_has_data.setVisibility(View.GONE);
		}else{
			listView_blancerecord.setVisibility(View.GONE);
			no_has_data.setVisibility(View.VISIBLE);
			tv_null_msg1.setText("您还没有余额消费记录呢");
		}
	}
	
	private void JumpToNext(Class clazz,Blance blance) {
		Intent intent = new Intent(record, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		Bundle bundle = new Bundle();
		bundle.putSerializable("blance",blance);
		intent.putExtras(bundle);
		startActivity(intent);
	}

}
