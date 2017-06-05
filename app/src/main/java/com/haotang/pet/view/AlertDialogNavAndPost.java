package com.haotang.pet.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.haotang.pet.R;

public class AlertDialogNavAndPost {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
	private TextView txt_msg;
	private Button btn_neg;
	private Button btn_pos;
	private ImageView img_line;
	private Display display;

	public AlertDialogNavAndPost(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public AlertDialogNavAndPost builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_alertdialognavandpost, null);
		// 获取自定义Dialog布局中的控件
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
		btn_neg = (Button) view.findViewById(R.id.btn_neg);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);
		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
		return this;
	}

	public AlertDialogNavAndPost setTitle(String title) {
		if ("".equals(title)) {
			txt_title.setVisibility(View.GONE);
		} else {
			txt_title.setVisibility(View.VISIBLE);
			txt_title.setText(title);
		}
		return this;
	}

	public AlertDialogNavAndPost setMsg(String msg) {
		if ("".equals(msg)) {
			txt_msg.setVisibility(View.GONE);
		} else {
			txt_msg.setText(msg);
			txt_msg.setVisibility(View.VISIBLE);
		}
		return this;
	}

	public AlertDialogNavAndPost setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public AlertDialogNavAndPost setPositiveButton(String text,
			final OnClickListener listener) {
		if ("".equals(text)) {
			btn_pos.setText("确定");
		} else {
			btn_pos.setText(text);
		}
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public AlertDialogNavAndPost setNegativeButton(String text,
			final OnClickListener listener) {
		if ("".equals(text)) {
			btn_neg.setText("取消");
		} else {
			btn_neg.setText(text);
		}
		btn_neg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public void show() {
		dialog.show();
	}
}
