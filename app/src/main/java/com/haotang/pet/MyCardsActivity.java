package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
/**
 * 我的卡包
 * @author Administrator
 *
 */
public class MyCardsActivity extends SuperActivity implements OnClickListener{
	
	private ArrayList<Cards> arrayListCards = new ArrayList<Cards>();
	private PullToRefreshListView listView_cards;
	private MyCardsAdapter cardsAdapter;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private Button bt_titlebar_other;
	private int page=1;
	private int status = 1;
	private LinearLayout footerLiner;
	private TextView textview_notice_left;
	private TextView textview_notice_middle;
	private TextView textview_notice_right;
	private View footer;
	private ListView listView = null;
	private LinearLayout layout_find_time_out;
	private String explainUrl = "";
	private String buyCardUrl = "";
	
	private LinearLayout layout_null;
//	private LinearLayout layout_buy_card;
	private TextView bt_null_to_buy;
	private RelativeLayout no_has_data;
	private TextView tv_null_msg1;
	private TextView textview_to_buy;
	private ImageView iv_null_image;
	private int pageSize = 10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_cards);
		MApplication.listAppoint.add(mContext);
		initView();
		setMode();
		setView();
		getData(page);
	}

	private void getData(int page) {
		mPDialog.showDialog();
		CommUtil.myCardPackage(mContext, page, status, cardDetail);
	}
	private AsyncHttpResponseHandler cardDetail = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			mPDialog.closeDialog();
			listView_cards.onRefreshComplete();
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
//					if (code==0&&!object.has("data")&&MyCouponCanUseList.size()>0) {
//						footer.setVisibility(View.VISIBLE);//翻页无卷
//						footerLiner.setVisibility(View.VISIBLE);//翻页无卷
//						layout_find_time_out.setVisibility(View.GONE);
//					}
					if (code == 0&&object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						
						if (objectData.has("buyCardUrl")&&!objectData.isNull("buyCardUrl")) {
							buyCardUrl = objectData.getString("buyCardUrl");
						}
						if (objectData.has("explainUrl")&&!objectData.isNull("explainUrl")) {
							explainUrl = objectData.getString("explainUrl");
						}
						if (!objectData.has("myCards")&&arrayListCards.size()>0) {
							footer.setVisibility(View.VISIBLE);//翻页无卷
							footerLiner.setVisibility(View.VISIBLE);//翻页无卷
							layout_find_time_out.setVisibility(View.GONE);
						}
						if (objectData.has("limit")&&!objectData.isNull("limit")) {
							pageSize = objectData.getInt("limit");
						}
						if (!objectData.has("myCards")&&arrayListCards.size()<=0) {
//							footerLiner.setVisibility(View.GONE);//初次无卷
//							footer.setVisibility(View.GONE);
//							listView.removeFooterView(footer);
							footer.setVisibility(View.GONE);
							layout_find_time_out.setVisibility(View.VISIBLE);
						}
						if (objectData.has("myCards")&&!objectData.isNull("myCards")) {
							footerLiner.setVisibility(View.GONE);
							JSONArray arrayMyCards = objectData.getJSONArray("myCards");
							if (arrayMyCards.length()>0) {
								page++;
								for (int i = 0; i < arrayMyCards.length(); i++) {
									arrayListCards.add(Cards.json2Entity(arrayMyCards.getJSONObject(i)));
								}
							}
							
							if (arrayMyCards.length()<pageSize) {
								footer.setVisibility(View.VISIBLE);
								footerLiner.setVisibility(View.VISIBLE);
							}
							
						}
					}
					if(page==1){
						if (arrayListCards.size()<=0) {
							listView_cards.setVisibility(View.GONE);
							footer.setVisibility(View.GONE);
							layout_find_time_out.setVisibility(View.VISIBLE);
						}
					}
				}else {
					if(page==1){
						if (arrayListCards.size()<=0) {
							listView_cards.setVisibility(View.GONE);
							footer.setVisibility(View.GONE);
							layout_find_time_out.setVisibility(View.VISIBLE);
						}
					}
				}
				
				if (arrayListCards.size()>0) {
					layout_find_time_out.setVisibility(View.GONE);
					cardsAdapter.notifyDataSetChanged();
					showMain(true);
				}else {
					showMain(false);
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
			listView_cards.onRefreshComplete();
//			listView.removeFooterView(footer);
		}
	};
	private void setView() {
		cardsAdapter = new MyCardsAdapter<Cards>(mContext, arrayListCards);
		listView = listView_cards.getRefreshableView();
//		footer.setVisibility(View.GONE);
		listView.addFooterView(footer);
		
		listView_cards.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {

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
		listView_cards.setAdapter(cardsAdapter);
	}
	
	private void setMode() {
		listView_cards.setMode(Mode.BOTH);
	}
	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		listView_cards = (PullToRefreshListView) findViewById(R.id.listView_cards);
		layout_find_time_out = (LinearLayout) findViewById(R.id.layout_find_time_out);
		footer = LayoutInflater.from(this).inflate(R.layout.footer_my_coupon,null);
		footerLiner = (LinearLayout) footer.findViewById(R.id.layout_find_time_out);
		textview_notice_left = (TextView) footer.findViewById(R.id.textview_notice_left);
		textview_notice_middle = (TextView) footer.findViewById(R.id.textview_notice_middle);
		textview_notice_right = (TextView) footer.findViewById(R.id.textview_notice_right);
		
		layout_null = (LinearLayout) findViewById(R.id.layout_null);
		no_has_data = (RelativeLayout) findViewById(R.id.rl_null);
		tv_null_msg1 = (TextView) findViewById(R.id.tv_null_msg1);
		iv_null_image = (ImageView) findViewById(R.id.iv_null_image);
//		layout_buy_card = (LinearLayout) findViewById(R.id.layout_buy_card);
		bt_null_to_buy = (TextView) findViewById(R.id.bt_null_to_buy);
		textview_to_buy = (TextView) findViewById(R.id.textview_to_buy);
		
		textview_notice_left.setText("没有更多服务卡啦，到");
		textview_notice_middle.setText("历史卡包");
		textview_notice_right.setText("中查看>");
		ib_titlebar_back.setOnClickListener(this);
		bt_titlebar_other.setOnClickListener(this);
		layout_find_time_out.setOnClickListener(this);
		no_has_data.setOnClickListener(this);
		bt_null_to_buy.setOnClickListener(this);
		tv_titlebar_title.setText("我的卡包");
		bt_titlebar_other.setText("说明");
		
		footerLiner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentOutData = new Intent(mContext, MyCardsOutDataActivity.class);
				startActivity(intentOutData);
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finish();
			break;
		case R.id.bt_titlebar_other:
			Intent intent = new Intent(mContext, ADActivity.class);
			if (!TextUtils.isEmpty(explainUrl)) {
				intent.putExtra("url", explainUrl);
				startActivity(intent);
			}else {
				ToastUtil.showToastShortCenter(mContext, "没获取到链接");
			}
			break;
		case R.id.layout_find_time_out:
			Intent intentOutData = new Intent(mContext, MyCardsOutDataActivity.class);
			startActivity(intentOutData);
			break;
//		case R.id.rl_null:
		case R.id.bt_null_to_buy:
			if (buyCardUrl!=null&&!TextUtils.isEmpty(buyCardUrl)) {
				startActivity(new Intent(mContext, ADActivity.class).putExtra("url", buyCardUrl));
			}
			break;
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		textview_to_buy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}
	private void showMain(boolean ismain){
		if(ismain){
			listView_cards.setVisibility(View.VISIBLE);
			no_has_data.setVisibility(View.GONE);
			layout_null.setVisibility(View.GONE);
		}else{
			listView_cards.setVisibility(View.GONE);
			layout_null.setVisibility(View.VISIBLE);
			no_has_data.setVisibility(View.VISIBLE);
			tv_null_msg1.setText("卡包是空的，快去购买享优惠吧！");
			bt_null_to_buy.setVisibility(View.VISIBLE);
//			tv_null_msg1.setVisibility(View.GONE);
//			layout_buy_card.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Utils.dip2px(mContext, 89), Utils.dip2px(mContext, 66));
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			iv_null_image.setBackgroundResource(R.drawable.cards_is_null);
			iv_null_image.setLayoutParams(params);
		}
	}
}
