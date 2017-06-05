package com.haotang.pet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * <p>
 * Title:ObservableScrollView
 * </p>
 * <p>
 * Description:监听ScrollView的滚动
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-3-21 下午4:32:31
 */
public class ObservableScrollView extends ScrollView {
	private ScrollViewListener scrollViewListener = null;

	public ObservableScrollView(Context context) {
		super(context);
	}

	public ObservableScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

}
