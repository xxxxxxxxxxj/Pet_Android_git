package com.haotang.pet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class MRelativeLayout extends RelativeLayout {
	private RoundedImageClickListener listener;
	public MRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float start = 1.0f;
		float end = 0.95f;
		Animation scaleAnimation = new ScaleAnimation(start, end, start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		Animation endAnimation = new ScaleAnimation(end, start, end, start,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setDuration(200);
		scaleAnimation.setFillAfter(true);
		endAnimation.setDuration(200);
		endAnimation.setFillAfter(true);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.startAnimation(scaleAnimation);
//			state = 1;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			this.startAnimation(endAnimation);
//			state = 0;
			invalidate();
			if(listener!=null){
				listener.onclick();
			}
			break;
		// 滑动出去不会调用action_up,调用action_cancel
		case MotionEvent.ACTION_CANCEL:
			this.startAnimation(endAnimation);
//			state = 0;
			invalidate();
			break;
		}
		// 不返回true，Action_up就响应不了
		return true;
	}
	
	public void setRoundedImageOnClick(RoundedImageClickListener listener){
    	this.listener = listener;
    }
	
}
