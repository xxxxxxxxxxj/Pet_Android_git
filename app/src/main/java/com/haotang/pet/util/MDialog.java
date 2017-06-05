package com.haotang.pet.util;

import com.haotang.pet.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 自定义的Dialog，有两种样式
 * @author Administrator
 *
 */
public class MDialog extends Dialog {
	public static int DIALOGTYPE_ALERT = 1;
	public static int DIALOGTYPE_CONFIRM = 2;

	private int nDialogType = DIALOGTYPE_ALERT;
	private Context mContext;
	private Button btOK;
	private Button btCancel;
	private TextView tvTitle;
	private TextView tvMessage;
	private RelativeLayout rlCancel;
	
	private String strTitle;
	private String strMessage;
	private String strOk;
	private String strCancel;
	private int okColorId;
	private int cancelColorId;
	
	private android.view.View.OnClickListener positive_listener;
	private android.view.View.OnClickListener negative_listener;
	private android.view.View.OnClickListener default_positive_listener = new android.view.View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			MDialog.this.dismiss();
			if(null != positive_listener)
				positive_listener.onClick(v);
		}
	};
	private android.view.View.OnClickListener default_negative_listener = new android.view.View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			MDialog.this.dismiss();
			if(null != negative_listener)
				negative_listener.onClick(v);
			
		}
	};
	public MDialog(Context context) {
		super(context);
		this.mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mdialog);
		
		initEnvironment();
		initControls();
	}

	private void initEnvironment() {
		// TODO Auto-generated method stub
		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	private void initControls() {
		tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
		tvMessage = (TextView) findViewById(R.id.tv_dialog_message);
		btOK = (Button) findViewById(R.id.bt_dialog_ok);
		btCancel = (Button) findViewById(R.id.bt_dialog_cancel);
		rlCancel = (RelativeLayout) findViewById(R.id.rl_dialog_cancel);
		if(null != strTitle)
			tvTitle.setText(strTitle);
		if(null != strMessage)
			tvMessage.setText(strMessage);
		if(0 != okColorId)
			btOK.setTextColor(okColorId);
		if(0 != cancelColorId)
			btCancel.setTextColor(cancelColorId);
		btOK.setOnClickListener(default_positive_listener);
		btCancel.setOnClickListener(default_negative_listener);
		if(null != strOk)
			btOK.setText(strOk);
		if(null != strCancel)
			btCancel.setText(strCancel);
		if(nDialogType == DIALOGTYPE_ALERT)
			rlCancel.setVisibility(View.GONE);
	}
	public void setDialogType(int nDialogType)
	{
		this.nDialogType = nDialogType;
	}
	public void setTitle(String strtitle){
		this.strTitle = strtitle;
	}
	public void setMessage(String strmessage){
		this.strMessage = strmessage;
	}
	public void setOkStr(String strok){
		this.strOk = strok;
	}
	public void setOkTextColor(int colorid){
		this.okColorId = colorid;
	}
	public void setCancelTextColor(int colorid){
		this.cancelColorId = colorid;
	}
	public void setCancelStr(String strcancel){
		this.strCancel = strcancel;
	}
	public void setPositiveListener(View.OnClickListener positive_listener)
	{
		this.positive_listener = positive_listener;
	}

	public void setNegativeListener(View.OnClickListener negative_listener)
	{
		this.negative_listener = negative_listener;
	}
	
	public static class Builder{
		private Context mContext;
		private String strTitle,strMessage,strOK,strCancel;
		private int okColorId,cancelColorId;
		private int nDialogType = DIALOGTYPE_ALERT;
		private boolean cancelable = true;
		private View.OnClickListener positive_listener, negative_listener;
		public Builder(Context context){
			this.mContext = context;
		}
		public Builder setTitle(String title){
			this.strTitle = title;
			return this;
		}
		public Builder setMessage(String msg){
			this.strMessage = msg;
			return this;
		}

		public Builder setType(int nType)
		{
			this.nDialogType = nType;
			return this;
		}
		public Builder setCancelable(boolean cancelable)
		{
			this.cancelable = cancelable;
			return this;
		}

		public Builder setOKStr(String strok)
		{
			this.strOK = strok;
			return this;
		}
		public Builder setOKTextColor(int colorid)
		{
			this.okColorId = colorid;
			return this;
		}
		public Builder setCancelTextColor(int colorid)
		{
			this.cancelColorId = colorid;
			return this;
		}

		public Builder setCancelStr(String strcancel)
		{
			this.strCancel = strcancel;
			return this;
		}

		public Builder positiveListener(View.OnClickListener positiveListener)
		{
			this.positive_listener = positiveListener;
			return this;
		}

		public Builder negativeListener(View.OnClickListener negativeListener)
		{
			this.negative_listener = negativeListener;
			return this;
		}
		
		public MDialog build(){
			if(null == mContext)
				return null;
			MDialog md = new MDialog(mContext);
			md.setDialogType(nDialogType);
			md.setTitle(strTitle);
			md.setMessage(strMessage);
			md.setOkStr(strOK);
			md.setCancelStr(strCancel);
			md.setOkTextColor(okColorId);
			md.setCancelTextColor(cancelColorId);
			md.setCancelable(cancelable);
			md.setPositiveListener(positive_listener);
			md.setNegativeListener(negative_listener);
			return md;
		}
		
	}

}
