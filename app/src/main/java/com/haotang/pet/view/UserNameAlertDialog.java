package com.haotang.pet.view;
 
 import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
import com.haotang.pet.R;
import com.haotang.pet.util.ToastUtil;
 
 public class UserNameAlertDialog {
 	private Context context;
 	private Dialog dialog;
 	private RelativeLayout lLayout_bg;
 	private TextView tv_alertdialog_title;
 	private Button btn_alertdialog_refuse;
 	private ImageView iv_alertdialog_close;
 	private static EditText et_alertdialog_name;
 	private Display display;
 
 	public static String getUserName() {
 		return et_alertdialog_name.getText().toString().trim();
 	}
 
 	public UserNameAlertDialog(Context context) {
 		this.context = context;
 		WindowManager windowManager = (WindowManager) context
 				.getSystemService(Context.WINDOW_SERVICE);
 		display = windowManager.getDefaultDisplay();
 	}
 
 	public UserNameAlertDialog builder() {
 		// 获取Dialog布局
 		View view = LayoutInflater.from(context).inflate(
 				R.layout.view_alertdialog_username, null);
 		// 获取自定义Dialog布局中的控件
 		lLayout_bg = (RelativeLayout) view.findViewById(R.id.lLayout_bg);
 		tv_alertdialog_title = (TextView) view
 				.findViewById(R.id.tv_alertdialog_title);
 		iv_alertdialog_close = (ImageView) view
 				.findViewById(R.id.iv_alertdialog_close);
 		et_alertdialog_name = (EditText) view
 				.findViewById(R.id.et_alertdialog_name);
 		et_alertdialog_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() >= 6) {
					ToastUtil.showToastShortCenter(context, "你最多只能输入六个字符");
				}
			}
		});
 		btn_alertdialog_refuse = (Button) view
 				.findViewById(R.id.btn_alertdialog_refuse);
 		// 定义Dialog布局和参数
 		dialog = new Dialog(context, R.style.AlertDialogStyle);
 		dialog.setContentView(view);
 		// 调整dialog背景大小
 		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
 				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
 		return this;
 	}
 
 	public UserNameAlertDialog setTitle(String title) {
 		if ("".equals(title)) {
 			tv_alertdialog_title.setText("您对宠物家还满意吗?");
 		} else {
 			tv_alertdialog_title.setText(title);
 		}
 		return this;
 	}
 
 	public UserNameAlertDialog setCancelable(boolean cancel) {
 		dialog.setCancelable(cancel);
 		return this;
 	}
 
 	public UserNameAlertDialog setComplaintsButton(String text,
 			final OnClickListener listener) {
 		if ("".equals(text)) {
 			btn_alertdialog_refuse.setText("我要吐槽");
 		} else {
 			btn_alertdialog_refuse.setText(text);
 		}
 		btn_alertdialog_refuse.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				listener.onClick(v);
 				dialog.dismiss();
 			}
 		});
 		return this;
 	}
 
 	public UserNameAlertDialog setCloseButton(final OnClickListener listener) {
 		iv_alertdialog_close.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				dialog.dismiss();
 			}
 		});
 		return this;
 	}
 
 	public void show() {
 		dialog.show();
 	}
 
 	public UserNameAlertDialog setTextViewHint(String string) {
 		et_alertdialog_name.setHint(string);
 		return this;
 	}
 }