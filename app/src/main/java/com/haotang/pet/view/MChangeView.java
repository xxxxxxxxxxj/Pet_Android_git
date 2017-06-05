package com.haotang.pet.view;

import com.haotang.pet.util.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MChangeView extends RelativeLayout {
	private LeftSlidingListener leftListener;
	private RightSlidingListener rightListener;
	private int downX;
	private int distanceX;
	public MChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MChangeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MChangeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			distanceX = (int) event.getRawX() - downX;
			if(distanceX>10){
				rightListener.changeImage();
			}else if(distanceX < -10){
				leftListener.changeImage();
			}
			break;
		}
		return true;
	}
	
	public void setLeftSlidingListener(LeftSlidingListener lListener){
		this.leftListener = lListener;
	}
	public void setRightSlidingListener(RightSlidingListener rListener){
		this.rightListener = rListener;
	}
	
}
