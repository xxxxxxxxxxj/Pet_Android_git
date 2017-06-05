package com.haotang.pet.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.haotang.pet.R;

public class SelectPicPopupWindow extends PopupWindow {
	private View mMenuView;
	public GridView gv_dropmenu;
	public RelativeLayout rl_dropmenu_sq;
	public RelativeLayout rl_dropmenu_no;

	public SelectPicPopupWindow(Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.dropmenu, null);
		gv_dropmenu = (GridView) mMenuView.findViewById(R.id.gv_dropmenu);
		rl_dropmenu_sq = (RelativeLayout) mMenuView
				.findViewById(R.id.rl_dropmenu_sq);
		rl_dropmenu_no = (RelativeLayout) mMenuView
				.findViewById(R.id.rl_dropmenu_no);
		// 设置按钮监听
		rl_dropmenu_sq.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		this.setHeight(width * 25 / 75);
		// 设置SelectPicPopupWindow弹出窗体可点击
		//this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.MenudownAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		/*mMenuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.pop_layout)
						.getBottom();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y > height) {
						dismiss();
					}
				}
				return true;
			}
		});*/
	}
}
