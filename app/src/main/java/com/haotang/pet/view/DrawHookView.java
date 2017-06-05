package com.haotang.pet.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.haotang.pet.R;

public class DrawHookView extends View {
	// 线1的x轴
	private int line1_x = 0;
	// 线1的y轴
	private int line1_y = 0;
	// 线2的x轴
	private int line2_x = 0;
	// 线2的y轴
	private int line2_y = 0;

	public DrawHookView(Context context) {
		super(context);
	}

	public DrawHookView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DrawHookView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 绘制

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/**
		 * 绘制圆弧
		 */
		Paint paint = new Paint();
		// 设置圆弧的宽度
		paint.setStrokeWidth(7);
		// 设置圆弧为空心
		paint.setStyle(Paint.Style.STROKE);
		// 消除锯齿
		paint.setAntiAlias(true);

		// 获取圆心的x坐标
		int center = getWidth() / 2;
		int center1 = center - getWidth() / 5;
		// 圆弧半径
		int radius = getWidth() / 2 - 5;
		/**
		 * 绘制对勾
		 */
		paint.setColor(getResources().getColor(R.color.arc_blue));
		if (line1_x < radius / 3) {
			line1_x+=3;
			line1_y+=3;
		}
		// 画第一根线
		canvas.drawLine(center1, center, center1 + line1_x, center + line1_y,
				paint);

		if (line1_x == radius / 3) {
			line2_x = line1_x;
			line2_y = line1_y;
			line1_x++;
			line1_y++;
		}
		if (line1_x >= radius / 3 && line2_x <= radius) {
			line2_x+=1.5;
			line2_y--;
		}
		if (line1_x >= radius / 3) {
			// 画第二根线
			canvas.drawLine(center1 + line1_x - 2, center + line1_y - 2,
					center1 + line2_x, center + line2_y, paint);
		}
		// 每隔10毫秒界面刷新
		postInvalidateDelayed(0);
	}

	public synchronized void setOk() {
		this.line1_x = 0;
		this.line1_y = 0;
		this.line2_x = 0;
		this.line2_y = 0;
		invalidate();
	}
}
