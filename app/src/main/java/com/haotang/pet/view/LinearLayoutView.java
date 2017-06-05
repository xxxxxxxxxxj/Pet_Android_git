package com.haotang.pet.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * <p>
 * Title:LinearLayoutView
 * </p>
 * <p>
 * Description:可拉伸线性布局
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-2 上午11:55:56
 */
public class LinearLayoutView extends LinearLayout {

	public static final int KEYBORAD_HIDE = 0;
	public static final int KEYBORAD_SHOW = 1;
	private static final int SOFTKEYPAD_MIN_HEIGHT = 50;

	private Handler uiHandler = new Handler();

	public LinearLayoutView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LinearLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onSizeChanged(int w, final int h, int oldw, final int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		uiHandler.post(new Runnable() {
			@Override
			public void run() {
				if (oldh - h > SOFTKEYPAD_MIN_HEIGHT) {
					Log.e("lp", "up");
					System.out.println("弹起");
					keyBordStateListener.stateChange(KEYBORAD_SHOW);
				} else {
					System.out.println("隐藏");
					Log.e("lp", "down");
					if (keyBordStateListener != null) {
						keyBordStateListener.stateChange(KEYBORAD_HIDE);
					}
				}
			}
		});
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private KeyBordStateListener keyBordStateListener;

	public void setKeyBordStateListener(
			KeyBordStateListener keyBordStateListener) {
		this.keyBordStateListener = keyBordStateListener;
	}

	public interface KeyBordStateListener {
		public void stateChange(int state);
	}
}
