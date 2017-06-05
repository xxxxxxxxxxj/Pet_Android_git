package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ComplaintHistoryAdapter;
import com.haotang.pet.entity.ComplaintHistory;
import com.haotang.pet.entity.ComplaintHistory.DataBean;
import com.haotang.pet.entity.ComplaintHistory.DataBean.FeedbacksBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;

/**
 * <p>
 * Title:ComplaintOrderHistoryActivity
 * </p>
 * <p>
 * Description:订单投诉历史
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-15 下午7:34:24
 */
public class ComplaintOrderHistoryActivity extends SuperActivity implements
		OnClickListener {
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private ListView lv_complaintorder_histort;
	private MProgressDialog pDialog;
	private int orderId;
	private ImageView iv_complaints_histort_default;
	private List<FeedbacksBean> listFeedbacks = new ArrayList<ComplaintHistory.DataBean.FeedbacksBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initView();
		setLinster();
		setView();
		getData();
	}

	private void initView() {
		setContentView(R.layout.activity_complaint_order_history);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		lv_complaintorder_histort = (ListView) findViewById(R.id.lv_complaintorder_histort);
		iv_complaints_histort_default = (ImageView) findViewById(R.id.iv_complaints_histort_default);
	}

	private void getData() {
		pDialog.showDialog();
		if (orderId > 0) {
			CommUtil.detail(this, orderId, detailHandler);
		} else {
			CommUtil.history(this, 2, historyHandler);
		}
	}

	private AsyncHttpResponseHandler detailHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						if (jData.has("feedback") && !jData.isNull("feedback")) {
							JSONObject jfeedback = jData
									.getJSONObject("feedback");
							Gson gson = new Gson();
							FeedbacksBean FeedbacksBean = gson.fromJson(
									jfeedback.toString(), FeedbacksBean.class);
							if (FeedbacksBean != null) {
								lv_complaintorder_histort
										.setVisibility(View.VISIBLE);
								iv_complaints_histort_default
										.setVisibility(View.GONE);
								listFeedbacks.clear();
								listFeedbacks.add(FeedbacksBean);
								lv_complaintorder_histort
										.setAdapter(new ComplaintHistoryAdapter<FeedbacksBean>(
												ComplaintOrderHistoryActivity.this,
												listFeedbacks));
							} else {
								lv_complaintorder_histort
										.setVisibility(View.GONE);
								iv_complaints_histort_default
										.setVisibility(View.VISIBLE);
							}
						} else {
							lv_complaintorder_histort.setVisibility(View.GONE);
							iv_complaints_histort_default
									.setVisibility(View.VISIBLE);
						}
					} else {
						lv_complaintorder_histort.setVisibility(View.GONE);
						iv_complaints_histort_default
								.setVisibility(View.VISIBLE);
					}
				} else {
					lv_complaintorder_histort.setVisibility(View.GONE);
					iv_complaints_histort_default.setVisibility(View.VISIBLE);
				}
			} catch (JSONException e) { 
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	private AsyncHttpResponseHandler historyHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			processData(new String(responseBody));
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	// 解析json数据
	private void processData(String result) {
		pDialog.closeDialog();
		try {
			Gson gson = new Gson();
			ComplaintHistory complaintHistory = gson.fromJson(result,
					ComplaintHistory.class);
			if (complaintHistory != null) {
				int code = complaintHistory.getCode();
				DataBean data = complaintHistory.getData();
				String msg = complaintHistory.getMsg();
				if (code == 0) {
					if (data != null) {
						List<FeedbacksBean> feedbacks = data.getFeedbacks();
						if (feedbacks != null && feedbacks.size() > 0) {
							lv_complaintorder_histort
									.setVisibility(View.VISIBLE);
							iv_complaints_histort_default
									.setVisibility(View.GONE);
							for (int i = 0; i < feedbacks.size(); i++) {
								FeedbacksBean feedbacksBean = feedbacks.get(i);
								if (feedbacksBean != null) {
									String content = feedbacksBean.getContent();
									List<String> reason = feedbacksBean
											.getReason();
									if (reason != null && content != null
											&& !TextUtils.isEmpty(content)) {
										reason.add(content);
									}
								}
							}
							lv_complaintorder_histort
									.setAdapter(new ComplaintHistoryAdapter<FeedbacksBean>(
											ComplaintOrderHistoryActivity.this,
											feedbacks));
						} else {
							lv_complaintorder_histort.setVisibility(View.GONE);
							iv_complaints_histort_default
									.setVisibility(View.VISIBLE);
						}
					} else {
						lv_complaintorder_histort.setVisibility(View.GONE);
						iv_complaints_histort_default
								.setVisibility(View.VISIBLE);
					}
				} else {
					if (msg != null && !TextUtils.isEmpty(msg)) {
						ToastUtil.showToastShortCenter(
								ComplaintOrderHistoryActivity.this, msg);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setView() {
		if (orderId > 0) {
			tv_titlebar_title.setText("投诉详情");
		} else {
			tv_titlebar_title.setText("历史投诉");
		}
	}

	private void setLinster() {
		ib_titlebar_back.setOnClickListener(this);
		lv_complaintorder_histort
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

					}
				});
	}

	private void initData() {
		pDialog = new MProgressDialog(this);
		Intent intent = getIntent();
		orderId = intent.getIntExtra("orderid", 0);
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
}
