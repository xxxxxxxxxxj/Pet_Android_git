package com.haotang.pet;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.Blance;
import com.haotang.pet.util.Utils;
/**
 * 余额记录详情界面 界面废用
 * @author zhiqiang
 *
 */
public class BalanceRecordDetail extends SuperActivity{

	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private TextView textView_balance_detail_price;
	private TextView textView_title_detail;
	
	//one
	private LinearLayout layout_one;
	private TextView textView_show_one,textView_show_one_detail;
	//two
	private LinearLayout layout_two;
	private TextView textView_show_two,textView_show_two_detail;
	//thr
	private LinearLayout layout_thr;
	private TextView textView_show_thr,textView_show_thr_detail;
	//FOUR
	private LinearLayout layout_four;
	private TextView textView_show_four,textView_show_four_detail;
	//Five
	private LinearLayout layout_five;
	private TextView textView_show_five,textView_show_five_detail;
	private Blance  blance ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_balance_record_detail);
		getIntentFrom();
		findView();
		setView();
		intiListener();
	}
	private void intiListener() {
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
	}
	private void getIntentFrom() {
	    Bundle bundle = getIntent().getExtras();
	    blance = (Blance) bundle.getSerializable("blance");
	}
	private void findView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		textView_balance_detail_price = (TextView) findViewById(R.id.textView_balance_detail_price);
		textView_title_detail = (TextView) findViewById(R.id.textView_title_detail);
		//one
		layout_one = (LinearLayout) findViewById(R.id.layout_one);
		textView_show_one = (TextView) findViewById(R.id.textView_show_one);
		textView_show_one_detail = (TextView) findViewById(R.id.textView_show_one_detail);
		//TOW
		layout_two = (LinearLayout) findViewById(R.id.layout_two);
		textView_show_two = (TextView) findViewById(R.id.textView_show_two);
		textView_show_two_detail = (TextView) findViewById(R.id.textView_show_two_detail);
		//THR
		layout_thr = (LinearLayout) findViewById(R.id.layout_thr);
		textView_show_thr = (TextView) findViewById(R.id.textView_show_thr);
		textView_show_thr_detail = (TextView) findViewById(R.id.textView_show_thr_detail);
		//FOUR
		layout_four = (LinearLayout) findViewById(R.id.layout_four);
		textView_show_four = (TextView) findViewById(R.id.textView_show_four);
		textView_show_four_detail = (TextView) findViewById(R.id.textView_show_four_detail);
		//FIVE
		layout_five = (LinearLayout) findViewById(R.id.layout_five);
		textView_show_five = (TextView) findViewById(R.id.textView_show_five);
		textView_show_five_detail = (TextView) findViewById(R.id.textView_show_five_detail);
	}
	private void setView() {
//		if (blance.amount>=0) {
//			tv_titlebar_title.setText("收入详情");
//		}else {
//			tv_titlebar_title.setText("支出详情");
//		}
//		if (blance.tradeType==1) {
//			tv_titlebar_title.setText("充值详情");
//			textView_show_one.setText("充值时间");
//			textView_show_one_detail.setText(blance.tradeDate);
//			textView_show_four.setText("支付方式");
//			textView_show_four_detail.setText(blance.payWay);
//			textView_show_five.setText("流水号");
//			textView_show_five_detail.setText(blance.tradeNo);
//			textView_balance_detail_price.setTextColor(Color.parseColor("#FF9531"));
//		}else if (blance.tradeType==2) {
//			tv_titlebar_title.setText("赠送详情");
//			textView_show_one.setText("赠送时间");
//			textView_show_one_detail.setText(blance.tradeDate);
//			textView_show_four.setText("充值流水号");
//			textView_show_four_detail.setText(blance.tradeNo);
//			layout_five.setVisibility(View.GONE);
//			textView_balance_detail_price.setTextColor(Color.parseColor("#FF9531"));
//		}else if (blance.tradeType==3||blance.tradeType==6) {
//			tv_titlebar_title.setText("支出详情");
//			textView_show_one.setText("支出详情");
//			textView_show_one_detail.setText(blance.tradeDate);
//			textView_show_four.setText("服务项目");
//			textView_show_four_detail.setText(blance.item);
//			textView_show_five.setText("订单编号");
//			textView_show_five_detail.setText(blance.tradeNo);
//		}else if (blance.tradeType==4) {
//			tv_titlebar_title.setText("提现详情");
//			textView_show_one.setText("提现时间");
//			textView_show_one_detail.setText(blance.tradeDate);
//			textView_show_four.setText("流水号");
//			textView_show_four_detail.setText(blance.tradeNo);
//			layout_five.setVisibility(View.GONE);
//		}else if (blance.tradeType==5) {
//			tv_titlebar_title.setText("退款详情");
//			layout_two.setVisibility(View.VISIBLE);
//			layout_thr.setVisibility(View.VISIBLE);
//			textView_show_one.setText("退款金额");
//			textView_show_one_detail.setText(blance.refundAmount+"");
//			textView_show_two.setText("退款时间");
//			textView_show_two_detail.setText(blance.refundDate);
//			textView_show_thr.setText("支出时间");
//			textView_show_thr_detail.setText(blance.tradeDate);
//			textView_show_four.setText("服务项目");
//			textView_show_four_detail.setText(blance.item);
//			textView_show_five.setText("订单编号");
//			textView_show_five_detail.setText(blance.tradeNo);
//		}else if (blance.tradeType==7) {
//			tv_titlebar_title.setText("赠送详情");
//			textView_show_one.setText("赠送时间");
//			textView_show_one_detail.setText(blance.tradeDate);
//			textView_show_four.setText("新用户邀请奖励");
//			textView_show_four.setTextColor(getResources().getColor(R.color.a333333));
//			textView_show_four_detail.setVisibility(View.GONE);
//			textView_show_five.setText("充值流水号");
//			textView_show_five_detail.setText(blance.tradeNo);
//			textView_balance_detail_price.setTextColor(Color.parseColor("#FF9531"));
//		}
//		textView_title_detail.setText(blance.status);
////		if (blance.tradeType!=5) {
////			textView_balance_detail_price.setText(blance.amount+"");
////		}else if (blance.tradeType==5) {
//			textView_balance_detail_price.setText(blance.amount+"");
////		}
	}
}
