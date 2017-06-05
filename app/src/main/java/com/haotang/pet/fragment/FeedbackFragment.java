package com.haotang.pet.fragment;

import java.util.Calendar;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

/**
 * <p>
 * Title:FeedbackFragment
 * </p>
 * <p>
 * Description:意见反馈界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-15 下午3:54:30
 */
@SuppressLint("NewApi")
public class FeedbackFragment extends Fragment implements OnClickListener {
	private Activity context;
	private EditText et_feedback_content;
	private TextView tv_feedback_text_num;
	private EditText et_feedback_phone;
	private TextView tv_feedback_qq;
	private TextView tv_feedback_wx;
	private TextView tv_feedback_tel;
	private Button btn_feedback_frag_submit;
	private Calendar calendar;
	private String phone = "";
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;
	private String str = "客服QQ：";
	private String str1 = "客服微信：";
	private String str2 = "客服电话：";

	public FeedbackFragment() {
		super();
	}

	public FeedbackFragment(Activity context) {
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

	private void setView() {
		String cellphone = spUtil.getString("cellphone", "");
		et_feedback_phone.setText(cellphone);
	}

	private void getData() {
		CommUtil.getCompanyContact(context, System.currentTimeMillis(),
				contactHanler);
	}

	private AsyncHttpResponseHandler contactHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("获取微信和qq：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jdata = jobj.getJSONObject("data");
					if (jdata.has("qq") && !jdata.isNull("qq")) {
						tv_feedback_qq.setVisibility(View.VISIBLE);
						SpannableString ss = new SpannableString(str
								+ jdata.getString("qq"));
						ss.setSpan(new ForegroundColorSpan(context
								.getResources().getColor(R.color.aBB996C)), str
								.length(), ss.length(),
								Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						tv_feedback_qq.setText(ss);
					}else{
						tv_feedback_qq.setVisibility(View.GONE);
					}
					if (jdata.has("weixin") && !jdata.isNull("weixin")) {
						tv_feedback_wx.setVisibility(View.VISIBLE);
						/**
						 * 增加客服微信可复制功能sdk11 以上支持复制
						 */
						SpannableString ss = new SpannableString(str1
								+ jdata.getString("weixin"));
						ss.setSpan(new ForegroundColorSpan(context
								.getResources().getColor(R.color.aBB996C)),
								str1.length(), ss.length(),
								Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						tv_feedback_wx.setText(ss);
					}else{
						tv_feedback_wx.setVisibility(View.GONE);
					}
					if (jdata.has("phone") && !jdata.isNull("phone")) {
						tv_feedback_tel.setVisibility(View.VISIBLE);
						phone = jdata.getString("phone");
						spUtil.saveString("customerPhone", phone);
						SpannableString ss = new SpannableString(str2 + phone);
						// 用下划线标记文本
						ss.setSpan(new UnderlineSpan(), str2.length(),
								ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						ss.setSpan(new ForegroundColorSpan(context
								.getResources().getColor(R.color.aBB996C)),
								str2.length(), ss.length(),
								Spanned.SPAN_INCLUSIVE_INCLUSIVE);
						tv_feedback_tel.setText(ss);
					}else{
						tv_feedback_tel.setVisibility(View.GONE);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
	};

	private void initData() {
		calendar = Calendar.getInstance();
		spUtil = SharedPreferenceUtil.getInstance(context);
		pDialog = new MProgressDialog(context);
	}

	private void setLinster() {
		tv_feedback_tel.setOnClickListener(this);
		btn_feedback_frag_submit.setOnClickListener(this);
		et_feedback_content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tv_feedback_text_num.setText("" + s.length());
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

	private View initView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.feedback_fragment, container,
				false);
		et_feedback_content = (EditText) view
				.findViewById(R.id.et_feedback_content);
		tv_feedback_text_num = (TextView) view
				.findViewById(R.id.tv_feedback_text_num);
		et_feedback_phone = (EditText) view
				.findViewById(R.id.et_feedback_phone);
		tv_feedback_qq = (TextView) view.findViewById(R.id.tv_feedback_qq);
		tv_feedback_wx = (TextView) view.findViewById(R.id.tv_feedback_wx);
		tv_feedback_tel = (TextView) view.findViewById(R.id.tv_feedback_tel);
		btn_feedback_frag_submit = (Button) view
				.findViewById(R.id.btn_feedback_frag_submit);
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
		case R.id.tv_feedback_tel:
			calendar.setTimeInMillis(System.currentTimeMillis());
			int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
			if (currentHour >= 9 && currentHour <= 22) {
				showPhoneDialog(phone);
			} else {
				ToastUtil.showToastShortCenter(context, "人工客服的工作时间为9：00-22:00");
			}
			break;
		case R.id.btn_feedback_frag_submit:
			if (et_feedback_content.getText().toString().trim().length() > 0) {
				if (et_feedback_phone.getText().toString().trim().length() > 0) {
					boolean checkPhone = Utils.checkPhone(context,
							et_feedback_phone);
					if (checkPhone) {
						sendContent(et_feedback_content.getText().toString()
								.trim());
					} else {
						ToastUtil.showToastShort(context, "请填写正确的联系电话");
					}
				} else {
					ToastUtil.showToastShort(context, "请填写您的联系电话");
				}
			} else {
				ToastUtil.showToastShort(context, "您的意见将是我们前进的最大动力");
			}
			break;
		default:
			break;
		}
	}

	private void sendContent(String content) {
		pDialog.showDialog();
		CommUtil.feedBack(context, spUtil.getInt("userid", 0), spUtil
				.getString("cellphone", ""), Global.getIMEI(context), Global
				.getCurrentVersion(context), et_feedback_phone.getText()
				.toString(), content, feedBackHanler);
	}

	private AsyncHttpResponseHandler feedBackHanler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			Utils.mLogError("意见反馈：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (0 == resultCode) {
					ToastUtil
							.showToastShort(context, "您的反馈意见阿宠已经收到，我们会尽快处理，谢谢");
				} else {
					ToastUtil.showToastShort(context, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	private void showPhoneDialog(final String phone) {
		MDialog mDialog = new MDialog.Builder(context)
				.setType(MDialog.DIALOGTYPE_CONFIRM).setMessage(phone)
				.setCancelStr("取消").setOKStr("呼叫")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Global.cellPhone(context, phone);
					}
				}).build();
		mDialog.show();
	}
}
