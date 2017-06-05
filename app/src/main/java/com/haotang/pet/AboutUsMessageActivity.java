package com.haotang.pet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;

/**
 * <p>
 * Title:AboutUsMessageActivity
 * </p>
 * <p>
 * Description:关于界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-29 下午5:32:32
 */
public class AboutUsMessageActivity extends SuperActivity {
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private TextView tvVersion;
	private TextView textView_about_telephone;
	private String servicePhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us_message);
		initView();
	}

	private void initView() {
		Intent intent = getIntent();
		servicePhone = intent.getStringExtra("servicePhone");
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		tvVersion = (TextView) findViewById(R.id.textView_about_app_version);
		textView_about_telephone = (TextView) findViewById(R.id.textView_about_telephone);
		tv_titlebar_title.setText("关于");
		textView_about_telephone.setText("客服电话:" + servicePhone);
		tvVersion.setText("软件版本：V" + Global.getCurrentVersion(this));
		textView_about_telephone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPhoneDialog(servicePhone);
			}
		});
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
	}

	private void showPhoneDialog(final String phone) {
		MDialog mDialog = new MDialog.Builder(this)
				.setType(MDialog.DIALOGTYPE_CONFIRM).setMessage(phone)
				.setCancelStr("取消").setOKStr("呼叫")
				.positiveListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Global.cellPhone(AboutUsMessageActivity.this, phone);
					}
				}).build();
		mDialog.show();
	}

}
