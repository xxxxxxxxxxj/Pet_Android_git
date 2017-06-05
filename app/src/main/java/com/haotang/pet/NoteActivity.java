package com.haotang.pet;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteActivity extends SuperActivity implements OnClickListener{
	private ImageButton ibBack;
	private TextView tvTitle;
	private EditText etContent;
	private Button btSubmit;
	private TextView tvNote1;
	private TextView tvNote2;
	private TextView tvNote3;
	private TextView tvNote4;
	private LinearLayout llNote1;
	private LinearLayout llNote2;
	private LinearLayout llNote3;
	private LinearLayout llNote4;
	private String[] remarks;
	private int previous;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);
		findView();
		setView();
	}

	private void findView() {
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		
		etContent = (EditText) findViewById(R.id.et_note_content);
		btSubmit = (Button) findViewById(R.id.bt_note_submite);
		tvNote1 = (TextView) findViewById(R.id.tv_note_note1);
		tvNote2 = (TextView) findViewById(R.id.tv_note_note2);
		tvNote3 = (TextView) findViewById(R.id.tv_note_note3);
		tvNote4 = (TextView) findViewById(R.id.tv_note_note4);
		llNote1 = (LinearLayout) findViewById(R.id.ll_note_note1);
		llNote2 = (LinearLayout) findViewById(R.id.ll_note_note4);
		llNote3 = (LinearLayout) findViewById(R.id.ll_note_note3);
		llNote4 = (LinearLayout) findViewById(R.id.ll_note_note2);
		
		
	}

	private void setView() {
		tvTitle.setText("订单备注");
		remarks = getIntent().getStringArrayExtra("remarks");
		previous = getIntent().getIntExtra("previous", 0);
		String remark = getIntent().getStringExtra("remark");
		if(remark!=null&&!"".equals(remark.trim())){
			etContent.setText(remark);
			CharSequence text = etContent.getText();
			if(text instanceof Spannable){
				Spannable spantext = (Spannable) text;
				Selection.setSelection(spantext, text.length());
			}
			
		}
		ibBack.setOnClickListener(this);
		btSubmit.setOnClickListener(this);
		llNote1.setOnClickListener(this);
		llNote2.setOnClickListener(this);
		llNote3.setOnClickListener(this);
		llNote4.setOnClickListener(this);
//		if(remarks!=null&&remarks.length>0&&remarks[0]!=null&&!"".equals(remarks[0])){
//			tvNote1.setText(remarks[0]);
//		}
//		if(remarks!=null&&remarks.length>1&&remarks[1]!=null&&!"".equals(remarks[1])){
//			tvNote2.setText(remarks[1]);
//		}
//		if(remarks!=null&&remarks.length>2&&remarks[2]!=null&&!"".equals(remarks[2])){
//			tvNote3.setText(remarks[2]);
//		}
//		if(remarks!=null&&remarks.length>3&&remarks[3]!=null&&!"".equals(remarks[3])){
//			tvNote4.setText(remarks[3]);
//		}
		
		if(previous==Global.FOSTERCAREORDER_TO_COUPON){
			llNote1.setVisibility(View.GONE);
			llNote2.setVisibility(View.GONE);
			llNote3.setVisibility(View.GONE);
			llNote4.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			//返回
			finishWithAnimation();
			break;
		case R.id.ll_note_note1:
			//常用备注一
			addNoteToContent(tvNote1);
			break;
		case R.id.ll_note_note2:
			//常用备注二
			addNoteToContent(tvNote2);
			break;
		case R.id.ll_note_note3:
			//常用备注三
			addNoteToContent(tvNote3);
			break;
		case R.id.ll_note_note4:
			//常用备注四
			addNoteToContent(tvNote4);
			break;
		case R.id.bt_note_submite:
			//确定
			submiteNote();
			break;
		}
		
	}
	
	private void submiteNote(){
		String content = etContent.getText().toString().trim();
		if(content == null || "".equals(content)){
//			finishWithAnimation();
			ToastUtil.showToastShort(this, "请输入备注信息");
		}else{
			Intent data = new Intent();
			data.putExtra("note", content);
			setResult(Global.RESULT_OK, data);
			finishWithAnimation();
		}
	}
	
	private void addNoteToContent(TextView tv){
		String content = etContent.getText().toString().trim();
		String note = tv.getText().toString().trim();
		if(!content.contains(note)){
			if(content == null||"".equals(content)){
				etContent.setText(note);
			}else{
				etContent.setText(content+"、"+note);
			}
			//把光标放到最后
			CharSequence text = etContent.getText();
			if(text instanceof Spannable){
				Spannable spantext = (Spannable) text;
				Selection.setSelection(spantext, text.length());
			}
		}
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    MobclickAgent.onPageStart("NoteActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	    MobclickAgent.onResume(this);          //统计时长
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	    MobclickAgent.onPageEnd("NoteActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}
	
}
