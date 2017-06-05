package com.haotang.pet.entity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <p>
 * Title:AutoScollViewPager
 * </p>
 * <p>
 * Description:控制滑动的viewpager
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-9-1 下午8:42:35
 */
public class AutoScollViewPager extends ViewPager {
	private boolean scrollble = true;

	public AutoScollViewPager(Context context) {
		super(context);
	}

	public AutoScollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!scrollble) {
			return true;
		}
		return super.onTouchEvent(ev);
	}

	public boolean isScrollble() {
		return scrollble;
	}

	public void setScrollble(boolean scrollble) {
		this.scrollble = scrollble;
	}
}
