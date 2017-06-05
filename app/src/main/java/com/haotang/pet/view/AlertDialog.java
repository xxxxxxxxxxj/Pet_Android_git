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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.haotang.pet.R;

public class AlertDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView tv_alertdialog_title;
	private Button btn_alertdialog_gotocomment;
	private Button btn_alertdialog_complaints;
	private Button btn_alertdialog_refuse;
	private Display display;

	public AlertDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public AlertDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_alertdialog, null);
		// 获取自定义Dialog布局中的控件
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		tv_alertdialog_title = (TextView) view
				.findViewById(R.id.tv_alertdialog_title);
		btn_alertdialog_gotocomment = (Button) view
				.findViewById(R.id.btn_alertdialog_gotocomment);
		btn_alertdialog_complaints = (Button) view
				.findViewById(R.id.btn_alertdialog_complaints);
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

	public AlertDialog setTitle(String title) {
		if ("".equals(title)) {
			tv_alertdialog_title.setText("您对宠物家还满意吗?");
		} else {
			tv_alertdialog_title.setText(title);
		}
		return this;
	}

	public AlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public AlertDialog setComplaintsButton(String text,
			final OnClickListener listener) {
		if ("".equals(text)) {
			btn_alertdialog_complaints.setText("我要吐槽");
		} else {
			btn_alertdialog_complaints.setText(text);
		}
		btn_alertdialog_complaints.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public AlertDialog setCommentButton(String text,
			final OnClickListener listener) {
		if ("".equals(text)) {
			btn_alertdialog_gotocomment.setText("我去评论");
		} else {
			btn_alertdialog_gotocomment.setText(text);
		}
		btn_alertdialog_gotocomment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public AlertDialog setRefuseButton(String text,
			final OnClickListener listener) {
		if ("".equals(text)) {
			btn_alertdialog_refuse.setText("残忍拒绝");
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

	public void show() {
		dialog.show();
	}
}
