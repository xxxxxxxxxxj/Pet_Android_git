package com.haotang.pet.util;

import com.haotang.pet.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MProgressDialog extends Dialog {
	private Window window;
	private Context context;
	private int positon = Gravity.CENTER;
	private TextView text_status;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==100){
				try {
					if(isShowing()){
						dismiss();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			super.handleMessage(msg);
		}
		
	};
	public MProgressDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
		setDialog();
	}

	public MProgressDialog(Context context, int theme) {
		super(context, R.style.loadingdialog);
		this.context = context;
		setDialog();
	}

	public MProgressDialog(Context context) {
		super(context,R.style.loadingdialog);
		this.context = context;
		setDialog();
	}
	public void setDialog(){
		setContentView(R.layout.myprogressdialogcenter);
		text_status = (TextView) findViewById(R.id.text_status);
		LayoutParams lay = getWindow().getAttributes();  
		 setParams(lay); 
		 setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
//		window = getWindow();
//		
//		window.setWindowAnimations(R.style.dialogWindowAnim);
//		window.setBackgroundDrawable(new ColorDrawable(0));
//		window.setGravity(positon);
//		setCancelable(true);
//		setCanceledOnTouchOutside(true);
		
	}
	
	private void setParams(LayoutParams lay) {  
		  Rect rect = new Rect();  
		  View view = getWindow().getDecorView();  
		  view.getWindowVisibleDisplayFrame(rect);  
//		  lay.height =Utils.getDisplayMetrics((Activity)context)[1] - rect.top;  
//		  lay.width = Utils.getDisplayMetrics((Activity)context)[0];  
		  lay.height =Utils.dip2px(context, 80);  
		  lay.width = Utils.dip2px(context, 80);  
		 }  

	public void setMessage(String message){
//		tvMessage.setText(message);
	}
	public void showDialog(){
		if(!isShowing()){
			try {
				show();
				text_status.setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	public void showDialogAndText(){
		if(!isShowing()){
			try {
				show();
				text_status.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	public void closeDialog(){
		text_status.setVisibility(View.GONE);
		mHandler.sendEmptyMessageDelayed(100, 800);
			
	}
	
	
	
}
