package com.haotang.pet.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.pet.CustomerComplaintsHistoryActivity;
import com.haotang.pet.R;
import com.haotang.pet.adapter.ComplaintReasonAdapter;
import com.haotang.pet.entity.ComplaintReason;
import com.haotang.pet.entity.ComplaintReasonStr;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.view.MListview;

/**
 * <p>
 * Title:FeedbackFragment
 * </p>
 * <p>
 * Description:投诉客服界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-15 下午3:54:30
 */
@SuppressLint("NewApi")
public class CustomerComplaintsFragment extends Fragment implements
		OnClickListener {
	private Activity context;
	private RelativeLayout rl_customercomplaints_lsts;
	private MListview mlv_customercomplaints_tswt;
	private EditText et_customercomplaints_content;
	private TextView tv_customercomplaints_text_num;
	private Button btn_customercomplaints_frag_submit;
	private MProgressDialog pDialog;
	private List<ComplaintReasonStr> listReasons = new ArrayList<ComplaintReasonStr>();
	private ComplaintReasonAdapter<ComplaintReasonStr> complaintReasonAdapter;
	private StringBuilder reasonSB = new StringBuilder();

	public CustomerComplaintsFragment() {
		super();
	}

	public CustomerComplaintsFragment(Activity context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initData();
		View view = initView(inflater, container);
		setLinster();
		setView();
		getData();
		return view;
	}

	private void getData() {
		pDialog.showDialog();
		CommUtil.reason(context, 3, 0, 0, reasonHandler);
	}

	private AsyncHttpResponseHandler reasonHandler = new AsyncHttpResponseHandler() {
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
			ComplaintReason complaintReason = gson.fromJson(result,
					ComplaintReason.class);
			if (complaintReason != null) {
				int code = complaintReason.getCode();
				com.haotang.pet.entity.ComplaintReason.DataBean data = complaintReason
						.getData();
				String msg = complaintReason.getMsg();
				if (code == 0) {
					if (data != null) {
						List<String> reasons = data.getReasons();
						if (reasons != null && reasons.size() > 0) {
							for (int i = 0; i < reasons.size(); i++) {
								String string = reasons.get(i);
								if (string != null
										&& !TextUtils.isEmpty(string)) {
									listReasons.add(new ComplaintReasonStr(
											string, false));
								}
							}
							complaintReasonAdapter.setData(listReasons);
						}
					}
				} else {
					if (msg != null && !TextUtils.isEmpty(msg)) {
						ToastUtil.showToastShortCenter(context, msg);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setView() {
		listReasons.clear();
		complaintReasonAdapter = new ComplaintReasonAdapter<ComplaintReasonStr>(
				context, listReasons);
		mlv_customercomplaints_tswt.setAdapter(complaintReasonAdapter);
	}

	private void setLinster() {
		rl_customercomplaints_lsts.setOnClickListener(this);
		btn_customercomplaints_frag_submit.setOnClickListener(this);
		et_customercomplaints_content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tv_customercomplaints_text_num.setText("" + s.length());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void initData() {
		pDialog = new MProgressDialog(context);
	}

	private View initView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.customercomplaints_frag,
				container, false);
		rl_customercomplaints_lsts = (RelativeLayout) view
				.findViewById(R.id.rl_customercomplaints_lsts);
		mlv_customercomplaints_tswt = (MListview) view
				.findViewById(R.id.mlv_customercomplaints_tswt);
		et_customercomplaints_content = (EditText) view
				.findViewById(R.id.et_customercomplaints_content);
		tv_customercomplaints_text_num = (TextView) view
				.findViewById(R.id.tv_customercomplaints_text_num);
		btn_customercomplaints_frag_submit = (Button) view
				.findViewById(R.id.btn_customercomplaints_frag_submit);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_customercomplaints_lsts:
			startActivity(new Intent(context,
					CustomerComplaintsHistoryActivity.class));
			break;
		case R.id.btn_customercomplaints_frag_submit:
			reasonSB.setLength(0);
			for (int i = 0; i < listReasons.size(); i++) {
				ComplaintReasonStr complaintReasonStr = listReasons.get(i);
				if (complaintReasonStr != null) {
					if (complaintReasonStr.isCheck()) {
						if (complaintReasonStr.getStr() != null
								&& !TextUtils.isEmpty(complaintReasonStr
										.getStr())) {
							reasonSB.append(complaintReasonStr.getStr() + ",");
						}
					}
				}
			}
			if (et_customercomplaints_content.getText().toString().trim()
					.length() <= 0
					&& reasonSB.length() <= 0) {
				ToastUtil.showToastShortCenter(context, "请选择或填写您遇到的问题");
			} else {
				pDialog.showDialog();
				CommUtil.save(context, 3, reasonSB.toString(),
						et_customercomplaints_content.getText().toString(), 0,
						saveHandler);
			}
			break;
		default:
			break;
		}
	}

	private AsyncHttpResponseHandler saveHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (0 == resultCode) {
					et_customercomplaints_content.setText("");
					reasonSB.setLength(0);
					for (int i = 0; i < listReasons.size(); i++) {
						ComplaintReasonStr complaintReasonStr = listReasons
								.get(i);
						if (complaintReasonStr != null) {
							if (complaintReasonStr.isCheck()) {
								complaintReasonStr.setCheck(false);
							}
						}
					}
					complaintReasonAdapter.setData(listReasons);
					ToastUtil.showToastShortCenter(context, "提交成功");
				} else {
					if (msg != null && !TextUtils.isEmpty(msg)) {
						ToastUtil.showToastShortCenter(context, msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};
}
