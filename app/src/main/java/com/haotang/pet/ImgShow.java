package com.haotang.pet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.Utils;

public class ImgShow extends Activity{
	private ImageView iv;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private Button bt_titlebar_other;
	private PopupWindow pWin;
	private LayoutInflater mInflater;
	private int position = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.img_show_detail);
		initView();
		getIntentFromEvaluate();
		initListener();
	}
	private void initListener() {
		bt_titlebar_other.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showSelectDialog();
			}
		});
		
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	private void getIntentFromEvaluate() {
		Bundle bundle = getIntent().getExtras();
		position = bundle.getInt("position");
		ArrayList<String> list = bundle.getStringArrayList("list");
		Bitmap bm = Utils.getxtsldraw(this, list.get(position));
		if (bm!=null) {
			iv.setImageBitmap(bm);
		}else {
			//处理部分手机不显示图片
			byte [] bis = bundle.getByteArray("bitmap");
			Bitmap bitmap=BitmapFactory.decodeByteArray(bis, 0, bis.length);
			iv.setImageBitmap(bitmap);  
		}
	}
	private void initView() {
		mInflater = LayoutInflater.from(this);
		iv = (ImageView) findViewById(R.id.iv);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		
		tv_titlebar_title.setText("图片详情");
	}
	
	
	private void showSelectDialog() {
		MDialog mDialog = new MDialog.Builder(ImgShow.this)
		.setTitle("提示")
		.setType(MDialog.DIALOGTYPE_CONFIRM)
		.setMessage("确定要删除照片?")
		.setCancelStr("否")
		.setOKStr("是")
		.positiveListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				//请求服务器取消订单
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("index",0);
				bundle.putInt("position",position);
				
				intent.setAction("android.intent.action.EvaluateActivity");
				intent.putExtras(bundle);
				sendBroadcast(intent);
				finish();

			}
		}).build();
		mDialog.show();

	}

	public void onDismiss(){
	    WindowManager.LayoutParams lp=getWindow().getAttributes();
	    lp.alpha = 1f;
	    getWindow().setAttributes(lp);	
	}

}
