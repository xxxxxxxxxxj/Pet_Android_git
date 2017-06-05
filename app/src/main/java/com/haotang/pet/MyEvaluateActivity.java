package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.MyEvaAdapter;
import com.haotang.pet.entity.CustomerOrderComment;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.util.CommUtil;

/**
 * <p>
 * Title:MyEvaluateActivity
 * </p>
 * <p>
 * Description:我的评价界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-10 上午11:14:21
 */
public class MyEvaluateActivity extends SuperActivity implements
		OnClickListener {
	private ImageButton ibBack;
	private TextView tvTitle;
	private PullToRefreshListView listView_show_my_self_eva;
	private MyEvaAdapter myEvaAdapter;
	private ArrayList<CustomerOrderComment> lists = new ArrayList<CustomerOrderComment>();
	private int page = 1;
	private LinearLayout layout_has_not_eva_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_only_my_evaluate);
		setContentView(R.layout.activity_my_evaluate);
		findView();
		setView();
		setLinster();
		
	}

	private void setView() {
		tvTitle.setText("我的评价");
		
//		for (int i = 0; i < 15; i++) {
//			lists.add("1=="+i);
//		}
		myEvaAdapter = new MyEvaAdapter<CustomerOrderComment>(mContext, lists);
		listView_show_my_self_eva.setMode(Mode.BOTH);
		listView_show_my_self_eva.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
					// 下拉刷新
					page = 1;
					lists.clear();
					myEvaAdapter.notifyDataSetChanged();
					getData(page);
				} else {
					getData(page);
				}
			}
		});
		listView_show_my_self_eva.setAdapter(myEvaAdapter);
		
		
		
		getData(page);
	}

	private void getData(int page) {
		mPDialog.showDialog();
		CommUtil.queryCustomerOrderComment(mContext, page, handler);
	}

	private void setLinster() {
		ibBack.setOnClickListener(this);
	}

	private void findView() {
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		listView_show_my_self_eva = (PullToRefreshListView) findViewById(R.id.listView_show_my_self_eva);
		layout_has_not_eva_list = (LinearLayout) findViewById(R.id.layout_has_not_eva_list);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finishWithAnimation();
			break;

		default:
			break;
		}
	}
	private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			try {
				listView_show_my_self_eva.onRefreshComplete();
				mPDialog.closeDialog();
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONArray objectData = object.getJSONArray("data");
						if (objectData.length()>0) {
							page++;
							for (int i = 0; i < objectData.length(); i++) {
								lists.add(CustomerOrderComment.json2Entity(objectData.getJSONObject(i)));
							}
						}
					}
				}
				if (lists.size()>0) {
					myEvaAdapter.notifyDataSetChanged();
					if (listView_show_my_self_eva.getVisibility()!=View.VISIBLE) {
						listView_show_my_self_eva.setVisibility(View.VISIBLE);
					}
					layout_has_not_eva_list.setVisibility(View.GONE);
				}else {
					layout_has_not_eva_list.setVisibility(View.VISIBLE);
					listView_show_my_self_eva.setVisibility(View.GONE);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mPDialog.closeDialog();
			}
			
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			mPDialog.closeDialog();
		}
	};
}
