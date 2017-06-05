package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.GoHomeStepAdapter;
import com.haotang.pet.entity.HomeStep;
import com.haotang.pet.entity.Procedure;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MyGridView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class GoHomeDetailActivity extends SuperActivity{

	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private MyGridView gridView_go_home_show_step;
	private LinearLayout layout_show_step_notice;
	private LinearLayout layout_show_step_gridview;
	private ImageView imageview_show_area;
	private GoHomeStepAdapter stepAdapter;
	private ScrollView scrollView_show_step;
	private RelativeLayout no_has_data;
	private TextView tv_null_msg1;
	private List<String> list = new ArrayList<String>();
	
//	String s="{'code': 0,'msg': '操作成功','data':{'procedure': [{'sn': 1,'txt': '第一步文字','img': '/static/content/homesvc/1/step1.png'},{'sn': 2,'txt': '第二步文字','img': '/static/content/homesvc/1/step2.png'},{'sn': 3,'txt': '第三步文字','img':'/static/content/homesvc/1/step3.png'},{'sn': 4,'txt': '第四步文字','img': '/static/content/homesvc/1/step4.png'},{'sn': 5,'txt': '第五步文字','img': '/static/content/homesvc/1/step5.png'},{'sn': 6,'txt': '第六步文字','img': '/static/content/homesvc/1/step6.png'}],'tip': ['1.的说法是到付件宽松的房价款收到; 2.的说法是都会发生的纠纷和健康;3.收到健身房和圣诞节;'],'area': {'desc': '','img': '/static/content/homesvc/1/area.png'}}}";
	String s="{'code': 0,'msg': '操作成功','data':{'procedure': [{'sn': 1,'txt': '第一步文字','img': '/static/banner/banner1.png?1434347541000'},{'sn': 2,'txt': '第二步文字','img': '/static/avatar/77c6a7efce1b.jpg?1328198400000'},{'sn': 3,'txt': '第三步文字','img':'/static/orderpic/1445942349157_3711_0.png'},{'sn': 4,'txt': '第四步文字','img': '/static/orderpic/1442389924353_2973_0.png'},{'sn': 5,'txt': '第五步文字','img': '/static/pavatar/zggmq.png?1445830230000'},{'sn': 6,'txt': '第六步文字','img': '/static/pavatar/mnxnr.png?1433963395000'}],'tip': ['1.的说法是到付件宽松的房价款收到;', '2.的说法是都会发生的纠纷和健康;', '3.收到健身房和圣诞节;'],'area': {'desc': '','img': '/static/pavatar/mndbq.png?1433963395000'}}}";
	private HomeStep homeStep;
	private TextView textview_step_show_notice;
	private int previous;
	private MProgressDialog pDialog;
	private int shopId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		shopId = intent.getIntExtra("shopid", 0);
		setContentView(R.layout.activity_go_home_detail);
		getIntenFromHead();
		initView();
		setView();
		getData();
		initListener();
	}
	private void getIntenFromHead() {
		previous = getIntent().getIntExtra("previous", 0);
	}
	private void getData() {
		pDialog.showDialog();
		CommUtil.getHomeDetail(this,shopId, System.currentTimeMillis()+"", getHomeDetail);
	}
	private AsyncHttpResponseHandler getHomeDetail = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			setData(responseBody);
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
		}
		
	};
	private void setData(byte[] responseBody) {
		try {
			Utils.mLogError("==-->go_home "+new String(responseBody));
			JSONObject jsonObject = new JSONObject(new String(responseBody));
//			JSONObject jsonObject = new JSONObject(s);
			int code = jsonObject.getInt("code");
			if (code==0) {
				if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
					JSONObject object = jsonObject.getJSONObject("data");
					if (object.has("procedure")&&!object.isNull("procedure")) {
						
						JSONArray array = object.getJSONArray("procedure");
						for (int i = 0; i < array.length(); i++) {
							Procedure procedure = new Procedure();
							JSONObject objectProcedure = array.getJSONObject(i);
							if (objectProcedure.has("txt")&&!objectProcedure.isNull("txt")) {
								procedure.txt= objectProcedure.getString("txt");
								Utils.mLogError("procedure.txt  "+procedure.txt);
							}
							if (objectProcedure.has("img")&&!objectProcedure.isNull("img")) {
								procedure.img=CommUtil.getServiceNobacklash()+objectProcedure.getString("img");
								Utils.mLogError("==-->procedure.img  "+procedure.img);
							}
							homeStep.list.add(procedure);
						}
						stepAdapter.notifyDataSetChanged();
						
					}
					if (object.has("tip")&&!object.isNull("tip")) {
						JSONArray array=object.getJSONArray("tip");
						TextView textViewOne = new TextView(GoHomeDetailActivity.this);
						textViewOne.setPadding(20, 0,20, 0);
						textViewOne.setText(" ");
						TextView textViewTwo = new TextView(GoHomeDetailActivity.this);
						textViewTwo.setText("温馨提示:");
						textViewTwo.setTextColor(Color.parseColor("#666666"));
						textViewTwo.setPadding(20, 0,20, 0);
						layout_show_step_notice.addView(textViewOne);
						layout_show_step_notice.addView(textViewTwo);
						for (int i = 0; i < array.length(); i++) {
							Utils.mLogError("==-->array:= "+array.getString(i));
							TextView textView = new TextView(GoHomeDetailActivity.this);
							textView.setText(array.getString(i).replace(" ", ""));
							textView.setPadding(20, 0,20, 0);
							textView.setTextColor(Color.parseColor("#666666"));
							layout_show_step_notice.addView(textView);
						}
						TextView textViewThr = new TextView(GoHomeDetailActivity.this);
						textViewThr.setPadding(20, 0,20, 0);
						textViewThr.setText(" ");
						layout_show_step_notice.addView(textViewThr);
					}
					if (object.has("area")&&!object.isNull("area")) {
						JSONObject objectArea = object.getJSONObject("area");
						if (objectArea.has("img")&&!objectArea.isNull("img")) {
							homeStep.areaImg = CommUtil.getServiceNobacklash()+objectArea.getString("img");
							Utils.mLogError("==-->homeStep.areaImg  "+homeStep.areaImg);
							ImageLoaderUtil.loadImg(homeStep.areaImg, imageview_show_area,0, new ImageLoadingListener() {
								
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onLoadingComplete(String path, View view, Bitmap bitmap) {
									// TODO Auto-generated method stub
									if(view!=null&&bitmap!=null){
										ImageView iv = (ImageView) view;
										iv.setImageBitmap(bitmap);
									}
								}
								
								@Override
								public void onLoadingCancelled(String arg0, View arg1) {
									// TODO Auto-generated method stub
									
								}
							});
						}
					}
					stepAdapter.notifyDataSetChanged();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initListener() {
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		gridView_go_home_show_step.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				String[] pics = new String[homeStep.list.size()];
				String[] txts = new String[homeStep.list.size()];
				for (int i = 0; i < pics.length; i++) {
					pics[i]=homeStep.list.get(i).img;
					txts[i]=homeStep.list.get(i).txt;
				}
				goNext(position, pics,txts);
			}
		});
	}
	private void initView() {
		homeStep = new HomeStep();
		pDialog = new MProgressDialog(this);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		gridView_go_home_show_step = (MyGridView) findViewById(R.id.gridView_go_home_show_step);
		layout_show_step_notice = (LinearLayout) findViewById(R.id.layout_show_step_notice);
		layout_show_step_gridview = (LinearLayout) findViewById(R.id.layout_show_step_gridview);
		imageview_show_area = (ImageView) findViewById(R.id.imageview_show_area);
		scrollView_show_step = (ScrollView) findViewById(R.id.scrollView_show_step);
		no_has_data = (RelativeLayout) findViewById(R.id.rl_null);
		tv_null_msg1 = (TextView) findViewById(R.id.tv_null_msg1);
//		textview_step_show_notice = (TextView) findViewById(R.id.textview_step_show_notice);

	}
	private void setView() {
		tv_titlebar_title.setText("上门详情");
		stepAdapter = new GoHomeStepAdapter(this,Utils.getDisplayMetrics(this),homeStep);
		gridView_go_home_show_step.setAdapter(stepAdapter);
	}
	private void goNext(int index,String[] pics,String[] txts){
		Intent intent = new Intent(this, BeauticianCommonPicActivity.class);
		intent.putExtra("index", index);
		intent.putExtra("pics", pics);
		intent.putExtra("txts", txts);
		startActivity(intent);
	}
}
