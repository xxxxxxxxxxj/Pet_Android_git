package com.haotang.pet.view;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import com.haotang.pet.R;
import com.melink.bqmmsdk.widget.BQMMMessageText;

public class ColorBQMMMessageText extends BQMMMessageText {
	public int num;
	private boolean isColor;
	private Context context;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public boolean isColor() {
		return isColor;
	}

	public void setColor(boolean isColor) {
		this.isColor = isColor;
	}

	public ColorBQMMMessageText(Context context) {
		super(context);
		this.context = context;
	}

	public ColorBQMMMessageText(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.context = paramContext;
	}

	public ColorBQMMMessageText(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		this.context = paramContext;
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		Spannable spannable;
		if (text instanceof Spannable) {
			spannable = (Spannable) text;
		} else {
			spannable = new SpannableString(text);
		}
		if (isColor()) {
			spannable.setSpan(new ForegroundColorSpan(context.getResources()
					.getColor(R.color.orange)), 0, getNum(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		super.setText(spannable, type);
	}
}
