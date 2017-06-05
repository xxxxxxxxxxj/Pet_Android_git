package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.ComplaintReasonAdapter;
import com.haotang.pet.entity.ComplaintReason;
import com.haotang.pet.entity.ComplaintReasonStr;
import com.haotang.pet.entity.OrdersBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.SelectableRoundedImageView;

/**
 * <p>
 * Title:ComplaintActivity
 * </p>
 * <p>
 * Description:我要投诉界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-15 下午8:16:27
 */
public class ComplaintActivity extends SuperActivity implements OnClickListener {
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private TextView tv_complaint_ordertype;
	private TextView tv_complaint_ordername;
	private SelectableRoundedImageView sriv_complaint_petimg;
	private TextView tv_complaint_orderfwsj;
	private TextView tv_complaint_orderno;
	private TextView tv_complaint_orderprice;
	private MListview mlv_complaint_tswt;
	private EditText et_complaints_content;
	private TextView tv_complaints_text_num;
	private Button btn_complaint_submit;
	private OrdersBean ordersBean;
	private MProgressDialog pDialog;
	private List<ComplaintReasonStr> listReasons = new ArrayList<ComplaintReasonStr>();
	private ComplaintReasonAdapter<ComplaintReasonStr> complaintReasonAdapter;
	private StringBuilder reasonSB = new StringBuilder();

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
		setContentView(R.layout.activity_complaint);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		tv_complaint_ordertype = (TextView) findViewById(R.id.tv_complaint_ordertype);
		tv_complaint_ordername = (TextView) findViewById(R.id.tv_complaint_ordername);
		sriv_complaint_petimg = (SelectableRoundedImageView) findViewById(R.id.sriv_complaint_petimg);
		tv_complaint_orderfwsj = (TextView) findViewById(R.id.tv_complaint_orderfwsj);
		tv_complaint_orderno = (TextView) findViewById(R.id.tv_complaint_orderno);
		tv_complaint_orderprice = (TextView) findViewById(R.id.tv_complaint_orderprice);
		mlv_complaint_tswt = (MListview) findViewById(R.id.mlv_complaint_tswt);
		et_complaints_content = (EditText) findViewById(R.id.et_complaints_content);
		tv_complaints_text_num = (TextView) findViewById(R.id.tv_complaints_text_num);
		btn_complaint_submit = (Button) findViewById(R.id.btn_complaint_submit);
	}

	private void getData() {
		pDialog.showDialog();
		CommUtil.reason(this, 2, ordersBean.getType(),
				ordersBean.getServiceLoc(), reasonHandler);
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
				if (code == 0 && data != null) {
					List<String> reasons = data.getReasons();
					if (reasons != null && reasons.size() > 0) {
						for (int i = 0; i < reasons.size(); i++) {
							String string = reasons.get(i);
							if (string != null && !TextUtils.isEmpty(string)) {
								listReasons.add(new ComplaintReasonStr(string,
										false));
							}
						}
						complaintReasonAdapter.setData(listReasons);
					}
				} else {
					if (msg != null && !TextUtils.isEmpty(msg)) {
						ToastUtil.showToastShortCenter(ComplaintActivity.this,
								msg);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setView() {
		tv_titlebar_title.setText("我要投诉");
		if (ordersBean != null) {
			ImageLoaderUtil.loadImg(ordersBean.getAvatar(),
					sriv_complaint_petimg, R.drawable.user_icon_unnet, null);
			Utils.setText(tv_complaint_ordertype, ordersBean.getTypeName(), "",
					View.VISIBLE, View.INVISIBLE);
			Utils.setText(tv_complaint_ordername, ordersBean.getService(), "",
					View.VISIBLE, View.INVISIBLE);
			Utils.setText(tv_complaint_orderfwsj,
					"预约时间：" + ordersBean.getAppointment(), "", View.VISIBLE,
					View.INVISIBLE);
			Utils.setText(tv_complaint_orderno,
					"订单编号：" + ordersBean.getOrderNum(), "", View.VISIBLE,
					View.INVISIBLE);
			Utils.setText(tv_complaint_orderprice,
					"¥" + ordersBean.getPay_price(), "", View.VISIBLE,
					View.INVISIBLE);
			int type = ordersBean.getType();
			switch (type) {
			case 1:// 洗护
				tv_complaint_ordertype.setBackgroundDrawable(Utils
						.getDW("FAA04A"));
				break;
			case 2:// 寄养
				tv_complaint_ordertype.setBackgroundDrawable(Utils
						.getDW("E5728D"));
				break;
			case 3:// 游泳
				tv_complaint_ordertype.setBackgroundDrawable(Utils
						.getDW("5BB0EC"));
				break;
			case 4:// 训练
				tv_complaint_ordertype.setBackgroundDrawable(Utils
						.getDW("6C6CC2"));
				break;
			default:
				break;
			}
		}
		listReasons.clear();
		complaintReasonAdapter = new ComplaintReasonAdapter<ComplaintReasonStr>(
				ComplaintActivity.this, listReasons);
		mlv_complaint_tswt.setAdapter(complaintReasonAdapter);
	}

	private void setLinster() {
		ib_titlebar_back.setOnClickListener(this);
		btn_complaint_submit.setOnClickListener(this);
		et_complaints_content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tv_complaints_text_num.setText("" + s.length());
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
		pDialog = new MProgressDialog(this);
		Intent intent = getIntent();
		ordersBean = (OrdersBean) intent.getSerializableExtra("ordersBean");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finishWithAnimation();
			break;
		case R.id.btn_complaint_submit:
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
			if (et_complaints_content.getText().toString().trim().length() <= 0
					&& reasonSB.length() <= 0) {
				ToastUtil.showToastShortCenter(ComplaintActivity.this,
						"请选择或填写您遇到的问题");
			} else {
				pDialog.showDialog();
				CommUtil.save(ComplaintActivity.this, 2, reasonSB.toString(),
						et_complaints_content.getText().toString(),
						ordersBean.getId(), saveHandler);
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
					ToastUtil.showToastShortCenter(ComplaintActivity.this,
							"提交成功");
					setResult(RESULT_OK);
					finish();
				} else {
					if (msg != null && !TextUtils.isEmpty(msg)) {
						ToastUtil.showToastShortCenter(ComplaintActivity.this,
								msg);
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
