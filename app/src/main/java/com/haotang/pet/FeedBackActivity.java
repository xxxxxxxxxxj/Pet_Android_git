package com.haotang.pet;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.fragment.ComplaintOrderFragment;
import com.haotang.pet.fragment.CustomerComplaintsFragment;
import com.haotang.pet.fragment.FeedbackFragment;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * <p>
 * Title:FeedBackActivity
 * </p>
 * <p>
 * Description:投诉主界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-15 下午2:55:35
 */
public class FeedBackActivity extends SuperActivity implements OnClickListener {
	private ImageButton ibBack;
	private TextView tvTitle;
	private RelativeLayout rl_feedback_yjfk;
	private TextView tv_feedback_yjfk;
	private View vw_feedback_yjfk;
	private RelativeLayout rl_feedback_tsdd;
	private TextView tv_feedback_tsdd;
	private View vw_feedback_tsdd;
	private RelativeLayout rl_feedback_tskf;
	private TextView tv_feedback_tskf;
	private View vw_feedback_tskf;
	private FeedbackFragment feedbackFragment;
	private ComplaintOrderFragment complaintOrderFragment;
	private CustomerComplaintsFragment customerComplaintsFragment;
	private FragmentManager fragmentManager;
	private SpannableString ss_yjfk = new SpannableString("意见反馈");
	private SpannableString ss_tsdd = new SpannableString("投诉订单");
	private SpannableString ss_tskf = new SpannableString("投诉客服");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findView();
		setLinster();
		setView();
	}

	private void findView() {
		setContentView(R.layout.feedback);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		rl_feedback_yjfk = (RelativeLayout) findViewById(R.id.rl_feedback_yjfk);
		tv_feedback_yjfk = (TextView) findViewById(R.id.tv_feedback_yjfk);
		vw_feedback_yjfk = (View) findViewById(R.id.vw_feedback_yjfk);
		rl_feedback_tsdd = (RelativeLayout) findViewById(R.id.rl_feedback_tsdd);
		tv_feedback_tsdd = (TextView) findViewById(R.id.tv_feedback_tsdd);
		vw_feedback_tsdd = (View) findViewById(R.id.vw_feedback_tsdd);
		rl_feedback_tskf = (RelativeLayout) findViewById(R.id.rl_feedback_tskf);
		tv_feedback_tskf = (TextView) findViewById(R.id.tv_feedback_tskf);
		vw_feedback_tskf = (View) findViewById(R.id.vw_feedback_tskf);
	}

	private void setLinster() {
		ibBack.setOnClickListener(this);
		rl_feedback_yjfk.setOnClickListener(this);
		rl_feedback_tsdd.setOnClickListener(this);
		rl_feedback_tskf.setOnClickListener(this);
	}

	private void setView() {
		tvTitle.setText("投诉");
		fragmentManager = getFragmentManager();
		setTabSelection(0);
	}

	private void setTabSelection(int index) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragments(transaction);
		switch (index) {
		case 0:
			feedbackFragment = new FeedbackFragment(this);
			transaction.add(R.id.fl_feedback_content, feedbackFragment);
			break;
		case 1:
			complaintOrderFragment = new ComplaintOrderFragment(this);
			transaction.add(R.id.fl_feedback_content,
					complaintOrderFragment);
			break;
		case 2:
			customerComplaintsFragment = new CustomerComplaintsFragment(
					this);
			transaction.add(R.id.fl_feedback_content,
					customerComplaintsFragment);
			break;
		}
		transaction.commit();
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (feedbackFragment != null) {
			transaction.hide(feedbackFragment);
		}
		if (complaintOrderFragment != null) {
			transaction.hide(complaintOrderFragment);
		}
		if (customerComplaintsFragment != null) {
			transaction.hide(customerComplaintsFragment);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FeedBackActivity");
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("FeedBackActivity");
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finishWithAnimation();
			break;
		case R.id.rl_feedback_yjfk:
			tv_feedback_yjfk.setTextColor(getResources().getColor(
					R.color.aD1494F));
			tv_feedback_tsdd.setTextColor(getResources().getColor(
					R.color.a333333));
			tv_feedback_tskf.setTextColor(getResources().getColor(
					R.color.a333333));
			vw_feedback_yjfk.setVisibility(View.VISIBLE);
			vw_feedback_tsdd.setVisibility(View.GONE);
			vw_feedback_tskf.setVisibility(View.GONE);
			ss_yjfk.setSpan(new TextAppearanceSpan(mContext, R.style.style2),
					0, ss_yjfk.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			tv_feedback_yjfk.setText(new SpannableString(ss_yjfk));
			ss_tsdd.setSpan(new TextAppearanceSpan(mContext, R.style.style4),
					0, ss_tsdd.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			tv_feedback_tsdd.setText(new SpannableString(ss_tsdd));
			ss_tskf.setSpan(new TextAppearanceSpan(mContext, R.style.style4),
					0, ss_tskf.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			tv_feedback_tskf.setText(new SpannableString(ss_tskf));
			setTabSelection(0);
			break;
		case R.id.rl_feedback_tsdd:
			if (Utils.checkLogin(this)) {
				tv_feedback_tsdd.setTextColor(getResources().getColor(
						R.color.aD1494F));
				tv_feedback_yjfk.setTextColor(getResources().getColor(
						R.color.a333333));
				tv_feedback_tskf.setTextColor(getResources().getColor(
						R.color.a333333));
				vw_feedback_tsdd.setVisibility(View.VISIBLE);
				vw_feedback_yjfk.setVisibility(View.GONE);
				vw_feedback_tskf.setVisibility(View.GONE);
				ss_yjfk.setSpan(
						new TextAppearanceSpan(mContext, R.style.style4), 0,
						ss_yjfk.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tv_feedback_yjfk.setText(new SpannableString(ss_yjfk));
				ss_tsdd.setSpan(
						new TextAppearanceSpan(mContext, R.style.style2), 0,
						ss_tsdd.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tv_feedback_tsdd.setText(new SpannableString(ss_tsdd));
				ss_tskf.setSpan(
						new TextAppearanceSpan(mContext, R.style.style4), 0,
						ss_tskf.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tv_feedback_tskf.setText(new SpannableString(ss_tskf));
				setTabSelection(1);
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		case R.id.rl_feedback_tskf:
			if (Utils.checkLogin(this)) {
				tv_feedback_tskf.setTextColor(getResources().getColor(
						R.color.aD1494F));
				tv_feedback_tsdd.setTextColor(getResources().getColor(
						R.color.a333333));
				tv_feedback_yjfk.setTextColor(getResources().getColor(
						R.color.a333333));
				vw_feedback_tskf.setVisibility(View.VISIBLE);
				vw_feedback_tsdd.setVisibility(View.GONE);
				vw_feedback_yjfk.setVisibility(View.GONE);
				ss_yjfk.setSpan(
						new TextAppearanceSpan(mContext, R.style.style4), 0,
						ss_yjfk.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tv_feedback_yjfk.setText(new SpannableString(ss_yjfk));
				ss_tsdd.setSpan(
						new TextAppearanceSpan(mContext, R.style.style4), 0,
						ss_tsdd.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tv_feedback_tsdd.setText(new SpannableString(ss_tsdd));
				ss_tskf.setSpan(
						new TextAppearanceSpan(mContext, R.style.style2), 0,
						ss_tskf.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tv_feedback_tskf.setText(new SpannableString(ss_tskf));
				setTabSelection(2);
			} else {
				startActivity(new Intent(this, LoginActivity.class));
			}
			break;
		default:
			break;
		}
	}
}
