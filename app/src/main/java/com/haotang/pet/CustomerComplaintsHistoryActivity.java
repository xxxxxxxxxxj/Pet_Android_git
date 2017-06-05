package com.haotang.pet;

import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CustomerComplaintHistoryAdapter;
import com.haotang.pet.entity.ComplaintHistory;
import com.haotang.pet.entity.ComplaintHistory.DataBean;
import com.haotang.pet.entity.ComplaintHistory.DataBean.FeedbacksBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.view.MListview;

/**
 * <p>
 * Title:CustomerComplaintsHistoryActivity
 * </p>
 * <p>
 * Description:客服投诉历史
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-15 下午7:34:24
 */
public class CustomerComplaintsHistoryActivity extends SuperActivity implements
		OnClickListener {
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private ListView lv_customercomplaints_histort;
	private Button btn_customercomplaints_histort_lxkf;
	private MProgressDialog pDialog;
	private Calendar calendar;
	private ImageView iv_customercomplaints_histort_default;

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
		setContentView(R.layout.activity_customer_complaints_history);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		lv_customercomplaints_histort = (ListView) findViewById(R.id.lv_customercomplaints_histort);
		btn_customercomplaints_histort_lxkf = (Button) findViewById(R.id.btn_customercomplaints_histort_lxkf);
		iv_customercomplaints_histort_default = (ImageView) findViewById(R.id.iv_customercomplaints_histort_default);
	}

	private void getData() {
		pDialog.showDialog();
		CommUtil.history(this, 3, historyHandler);
	}

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
							lv_customercomplaints_histort
									.setVisibility(View.VISIBLE);
							iv_customercomplaints_histort_default
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
							lv_customercomplaints_histort
									.setAdapter(new CustomerComplaintHistoryAdapter<FeedbacksBean>(
											CustomerComplaintsHistoryActivity.this,
											feedbacks));
						} else {
							lv_customercomplaints_histort
									.setVisibility(View.GONE);
							iv_customercomplaints_histort_default
									.setVisibility(View.VISIBLE);
						}
					} else {
						lv_customercomplaints_histort.setVisibility(View.GONE);
						iv_customercomplaints_histort_default
								.setVisibility(View.VISIBLE);
					}
				} else {
					if (msg != null && !TextUtils.isEmpty(msg)) {
						ToastUtil.showToastShortCenter(
								CustomerComplaintsHistoryActivity.this, msg);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setView() {
		tv_titlebar_title.setText("历史投诉");
	}

	private void setLinster() {
		ib_titlebar_back.setOnClickListener(this);
		btn_customercomplaints_histort_lxkf.setOnClickListener(this);
		lv_customercomplaints_histort
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

					}
				});
	}

	private void initData() {
		pDialog = new MProgressDialog(this);
		calendar = Calendar.getInstance();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finishWithAnimation();
			break;
		case R.id.btn_customercomplaints_histort_lxkf:
			calendar.setTimeInMillis(System.currentTimeMillis());
			int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
			if (currentHour >= 9 && currentHour <= 22) {
				showPhoneDialog(spUtil.getString("customerPhone", ""));
			} else {
				ToastUtil.showToastShortCenter(
						CustomerComplaintsHistoryActivity.this,
						"人工客服的工作时间为9：00-22:00");
			}
			break;

		default:
			break;
		}
	}

	private void showPhoneDialog(final String phone) {
		MDialog mDialog = new MDialog.Builder(
				CustomerComplaintsHistoryActivity.this)
				.setType(MDialog.DIALOGTYPE_CONFIRM).setMessage(phone)
				.setCancelStr("取消").setOKStr("呼叫")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Global.cellPhone(
								CustomerComplaintsHistoryActivity.this, phone);
					}
				}).build();
		mDialog.show();
	}
}
