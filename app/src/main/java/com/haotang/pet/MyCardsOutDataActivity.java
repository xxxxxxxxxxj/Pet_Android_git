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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.MyCardsAdapter;
import com.haotang.pet.entity.Cards;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Utils;

public class MyCardsOutDataActivity extends SuperActivity implements OnClickListener{

	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private Button bt_titlebar_other;
	private PullToRefreshListView listView_out_data;
	private MyCardsAdapter cardsAdapter;
	private ArrayList<Cards> arrayListCards = new ArrayList<Cards>();
	private int page=1;
	private int status = 2;
	private String explainUrl = "";
	
	private RelativeLayout no_has_data;
	private TextView tv_null_msg1;
	private ImageView iv_null_image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycards_out_data);
		initView();
		setMode();
		setView();
	}
	
	private void getData(int page) {
		// TODO Auto-generated method stub
		mPDialog.showDialog();
		CommUtil.myCardPackage(mContext, page, status, cardDetail);
	}
	
	private AsyncHttpResponseHandler cardDetail = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			mPDialog.closeDialog();
			listView_out_data.onRefreshComplete();
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (code == 0&&object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("explainUrl")&&!objectData.isNull("explainUrl")) {
							explainUrl = objectData.getString("explainUrl");
						}
						if (objectData.has("myCards")&&!objectData.isNull("myCards")) {
							JSONArray arrayMyCards = objectData.getJSONArray("myCards");
							if (arrayMyCards.length()>0) {
								page++;
								for (int i = 0; i < arrayMyCards.length(); i++) {
									arrayListCards.add(Cards.json2Entity(arrayMyCards.getJSONObject(i)));
//									Cards cards = new Cards();
//									JSONObject objectCards = arrayMyCards.getJSONObject(i);
//									if (objectCards.has("cardName")&&!objectCards.isNull("cardName")) {
//										cards.cardName = objectCards.getString("cardName");
//									}
//									if (objectCards.has("cardTip")&&!objectCards.isNull("cardTip")) {
//										cards.cardTip = objectCards.getString("cardTip");
//									}
//									if (objectCards.has("cardImg")&&!objectCards.isNull("cardImg")) {
//										cards.cardImg = objectCards.getString("cardImg");
//									}
//									if (objectCards.has("packageDetails")&&!objectCards.isNull("packageDetails")) {
//										JSONObject objectPackageDetail = objectCards.getJSONObject("packageDetails");
//										if (objectPackageDetail.has("serviceDetails")&&!objectPackageDetail.isNull("serviceDetails")) {
//											JSONObject objectServiceDetail = objectPackageDetail.getJSONObject("serviceDetails");
//											if (objectServiceDetail.has("bath_count_text")&&!objectServiceDetail.isNull("bath_count_text")) {
//												cards.bath_count_text = objectServiceDetail.getString("bath_count_text");
//											}
//											if (objectServiceDetail.has("beauty_count_text")&&!objectServiceDetail.isNull("beauty_count_text")) {
//												cards.beauty_count_text = objectServiceDetail.getString("beauty_count_text");
//											}
//											if (objectServiceDetail.has("beauty_level_text")&&!objectServiceDetail.isNull("beauty_level_text")) {
//												cards.beauty_level_text = objectServiceDetail.getString("beauty_level_text");
//											}
//											if (objectServiceDetail.has("bath_level_text")&&!objectServiceDetail.isNull("bath_level_text")) {
//												cards.bath_level_text = objectServiceDetail.getString("bath_level_text");
//											}
//										}
//										if (objectPackageDetail.has("usedDetails")&&!objectPackageDetail.isNull("usedDetails")) {
//											JSONArray arrayUserDetail = objectPackageDetail.getJSONArray("usedDetails");
//											if (arrayUserDetail.length()>0) {
//												StringBuilder builder = new StringBuilder();
//												for (int j = 0; j < arrayUserDetail.length(); j++) {
//													JSONObject objectUserDetail = arrayUserDetail.getJSONObject(j);
//													if (objectUserDetail.has("appointment")&&!objectUserDetail.isNull("appointment")) {
//														if (objectUserDetail.has("usedText")&&!objectUserDetail.isNull("usedText")) {
//															if (j==arrayUserDetail.length()-1) {
//																builder.append(objectUserDetail.getString("appointment")+"		"+objectUserDetail.getString("usedText"));
//															}else {
//																builder.append(objectUserDetail.getString("appointment")+"		"+objectUserDetail.getString("usedText")+"\n\n");
//															}
//														}
//													}
//												}
//												cards.timeAndNums = builder.toString();
//											}
//										}
//									}
//									arrayListCards.add(cards);
								}
							}
							if (arrayListCards.size()>0) {
								cardsAdapter.setIsHistory(true);
								cardsAdapter.notifyDataSetChanged();
							}
							
							if(page==1){
								if (arrayListCards.size()<=0) {
									listView_out_data.setVisibility(View.GONE);
								}
							}
						}
					}
					if (arrayListCards.size()>0) {
						showMain(true);
					}else {
						showMain(false);
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
			mPDialog.closeDialog();
			listView_out_data.onRefreshComplete();
//			listView.removeFooterView(footer);
		}
	};
	private void setView() {
		cardsAdapter = new MyCardsAdapter<Cards>(mContext, arrayListCards);
//		listView = listView_cards.getRefreshableView();
		
		listView_out_data.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
					// 下拉刷新
					page = 1;
					arrayListCards.clear();
					cardsAdapter.notifyDataSetChanged();
					getData(page);
				}else {
					getData(page);
				}
			}

			
			
		});
		listView_out_data.setAdapter(cardsAdapter);
		getData(page);
	}
	private void setMode() {
		listView_out_data.setMode(Mode.BOTH);
	}
	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		listView_out_data = (PullToRefreshListView) findViewById(R.id.listView_out_data);
		
		no_has_data = (RelativeLayout) findViewById(R.id.rl_null);
		tv_null_msg1 = (TextView) findViewById(R.id.tv_null_msg1);
		iv_null_image = (ImageView) findViewById(R.id.iv_null_image);
		
		tv_titlebar_title.setText("过期卡包");
		bt_titlebar_other.setText("说明");
		
		ib_titlebar_back.setOnClickListener(this);
		bt_titlebar_other.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finish();
			break;
		case R.id.bt_titlebar_other:
			Intent intent = new Intent(mContext, ADActivity.class);
			intent.putExtra("url", explainUrl);
			startActivity(intent);
			break;
		}
	}
	
	private void showMain(boolean ismain){
		if(ismain){
			listView_out_data.setVisibility(View.VISIBLE);
			no_has_data.setVisibility(View.GONE);
		}else{
			listView_out_data.setVisibility(View.GONE);
			no_has_data.setVisibility(View.VISIBLE);
			tv_null_msg1.setText("暂无历史服务卡");
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Utils.dip2px(mContext, 89), Utils.dip2px(mContext, 66));
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			iv_null_image.setBackgroundResource(R.drawable.cards_is_null);
			iv_null_image.setLayoutParams(params);
		}
	}
}
