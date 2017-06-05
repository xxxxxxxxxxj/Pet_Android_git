package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.JoinWorkAdapter;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;

public class JointWorkShopDetail extends SuperActivity{
	
	private JointWorkShopDetail jDetail;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private PullToRefreshListView listView_show_main_to_list;
	private View header;
	private LinearLayout joinworkdetail_layout_address;
	private LinearLayout joinworkdetail_layout_phone;
	private TextView joinworkdetail_address;
	private TextView joinworkdetail_phone;
	private TextView joinworkdetail_business_time;
	private TextView joinworkdetail_introduce;
	private TextView joinworkdetail_title_name;
	private ArrayList<String> Imagelist = new ArrayList<String>(); 
	@SuppressWarnings("rawtypes")
	private JoinWorkAdapter joinWorkAdapter;
	
	private ImageView joinworkdetail_eva_one;
	private ImageView joinworkdetail_eva_two;
	private ImageView joinworkdetail_eva_thr;
	private ImageView joinworkdetail_eva_four;
	private ImageView joinworkdetail_eva_five;
	
	private SelectableRoundedImageView joinworkdetail_imageview;
	private String phoneNum = "";
	private String pic = "";
	private MProgressDialog dialog;
	private double lat = 0;
	private double lng = 0;
	private int id = 0 ;
	private int type = 0 ;
	private String name ="";
	private String addr ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_to_list);
		jDetail = this;
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
		joinworkdetail_layout_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (lat!=0||lng!=0) {
					Intent intent = new Intent(jDetail, ShopLocationActivity.class);
					intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
					intent.putExtra("shopname", name);
					intent.putExtra("shopaddr", addr);
					intent.putExtra("shoplat", lat);
					intent.putExtra("shoplng", lng);
					startActivity(intent);
				}
			}
		});
		
		joinworkdetail_layout_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MDialog mDialog = new MDialog.Builder(jDetail).setTitle("提示")
						.setType(MDialog.DIALOGTYPE_CONFIRM)
						.setMessage("是否拨打电话?").setCancelStr("否").setOKStr("是")
						.positiveListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// 确认拨打电话
								Utils.telePhoneBroadcast(jDetail, phoneNum);
							}
						}).build();
				mDialog.show();

			}
		});
	}
	private void setView() {
		id = getIntent().getIntExtra("id", 0);
		type = getIntent().getIntExtra("type", 0);
		tv_titlebar_title.setText("商家详情");
		listView_show_main_to_list.getRefreshableView().addHeaderView(header);
		listView_show_main_to_list.setMode(Mode.DISABLED);
		listView_show_main_to_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					//下拉刷新
					listView_show_main_to_list.onRefreshComplete();
				}else{
					listView_show_main_to_list.onRefreshComplete();
				}
			}
		});
		joinWorkAdapter = new JoinWorkAdapter<String>(this, Imagelist);
		listView_show_main_to_list.setAdapter(joinWorkAdapter);
		getData();
	}
	private void getData() {//数据没给目前按照医院的先搞
		switch (type) {
		case 1:
			CommUtil.getPhoto(this,id, getHospital);
			break;
		case 2:
			CommUtil.getEntertain(this,id, getHospital);
			break;
		case 3:
			CommUtil.getHospital(this,id, getHospital);
			break;
		case 4:
			CommUtil.getTrain(this,id, getHospital);
			break;
		}
	}
	private AsyncHttpResponseHandler getHospital = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				dialog.closeDialog();
				listView_show_main_to_list.onRefreshComplete();
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONObject object = jsonObject.getJSONObject("data");
						if (object.has("name")&&!object.isNull("name")) {
							name = object.getString("name");
							joinworkdetail_title_name.setText(name);
						}
						if (object.has("addr")&&!object.isNull("addr")) {
							addr = object.getString("addr");
							joinworkdetail_address.setText(addr);
						}
						if (object.has("phone")&&!object.isNull("phone")) {
							phoneNum =  object.getString("phone");
							joinworkdetail_phone.setText(phoneNum);
						}
						if (object.has("pic")&&!object.isNull("pic")) {
							pic =  CommUtil.getServiceNobacklash()+object.getString("pic");
							ImageLoaderUtil.loadImg(pic,joinworkdetail_imageview,  R.drawable.icon_production_default,null);
						}
						if (object.has("workTime")&&!object.isNull("workTime")) {
							joinworkdetail_business_time.setText(object.getString("workTime"));
						}
						if (object.has("intro")&&!object.isNull("intro")) {
							joinworkdetail_introduce.setText(object.getString("intro"));
						}
						if (object.has("star")&&!object.isNull("star")) {
							int star = object.getInt("star");
							setStar(star);
						}
						if (object.has("lat")&&!object.isNull("lat")) {
							lat = object.getDouble("lat");
						}
						if (object.has("lng")&&!object.isNull("lng")) {
							lng = object.getDouble("lng");
						}
						if (object.has("introPic")&&!object.isNull("introPic")) {
							JSONArray array = object.getJSONArray("introPic");
							if (array.length()>0) {
								for (int i = 0; i < array.length(); i++) {
									Imagelist.add(array.getString(i));
								}
							}
						}
						joinWorkAdapter.notifyDataSetChanged();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					dialog.closeDialog();
					listView_show_main_to_list.onRefreshComplete();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			try {
				dialog.closeDialog();
				listView_show_main_to_list.onRefreshComplete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		
	};
	private void initView() {
		dialog = new MProgressDialog(jDetail);
		dialog.showDialog();
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		listView_show_main_to_list = (PullToRefreshListView) findViewById(R.id.listView_show_main_to_list);
		header = LayoutInflater.from(this).inflate(R.layout.main_to_list_detail_header_view, null);
		joinworkdetail_layout_address = (LinearLayout) header.findViewById(R.id.joinworkdetail_layout_address);
		joinworkdetail_layout_phone = (LinearLayout) header.findViewById(R.id.joinworkdetail_layout_phone);
		joinworkdetail_address = (TextView) header.findViewById(R.id.joinworkdetail_address);
		joinworkdetail_phone = (TextView) header.findViewById(R.id.joinworkdetail_phone);
		joinworkdetail_business_time = (TextView) header.findViewById(R.id.joinworkdetail_business_time);
		joinworkdetail_introduce = (TextView) header.findViewById(R.id.joinworkdetail_introduce);
		joinworkdetail_title_name = (TextView) header.findViewById(R.id.joinworkdetail_title_name);
		joinworkdetail_eva_one = (ImageView) header.findViewById(R.id.joinworkdetail_eva_one);
		joinworkdetail_eva_two = (ImageView) header.findViewById(R.id.joinworkdetail_eva_two);
		joinworkdetail_eva_thr = (ImageView) header.findViewById(R.id.joinworkdetail_eva_thr);
		joinworkdetail_eva_four = (ImageView) header.findViewById(R.id.joinworkdetail_eva_four);
		joinworkdetail_eva_five = (ImageView) header.findViewById(R.id.joinworkdetail_eva_five);
		joinworkdetail_imageview = (SelectableRoundedImageView) header.findViewById(R.id.joinworkdetail_imageview);
	}
	private void setStar(int star) {
		switch (star) {
		case 1:
			joinworkdetail_eva_one.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_two.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_thr.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_four.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_five.setBackgroundResource(R.drawable.star_empty);
			
			break;
		case 2:
			joinworkdetail_eva_one.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_two.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_thr.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_four.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_five.setBackgroundResource(R.drawable.star_empty);
			break;
		case 3:
			joinworkdetail_eva_one.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_two.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_thr.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_four.setBackgroundResource(R.drawable.star_empty);
			joinworkdetail_eva_five.setBackgroundResource(R.drawable.star_empty);
			break;
		case 4:
			joinworkdetail_eva_one.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_two.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_thr.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_four.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_five.setBackgroundResource(R.drawable.star_empty);
			break;
		case 5:
			joinworkdetail_eva_one.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_two.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_thr.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_four.setBackgroundResource(R.drawable.star_full);
			joinworkdetail_eva_five.setBackgroundResource(R.drawable.star_full);
			break;
		}
	}
}
