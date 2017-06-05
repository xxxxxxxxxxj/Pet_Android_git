package com.haotang.pet.util;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.StyleSpan;

/**
 * <p>
 * Title:MyStyleSpan
 * </p>
 * <p>
 * Description:设置textview部分文本加粗
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-3-1 下午4:57:05
 */
public class MyStyleSpan extends StyleSpan {
	public MyStyleSpan(int style) {
		super(style);
	}

	@Override
	public int describeContents() {
		return super.describeContents();
	}

	@Override
	public int getSpanTypeId() {
		return super.getSpanTypeId();
	}

	@Override
	public int getStyle() {
		return super.getStyle();
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setFakeBoldText(true);
		super.updateDrawState(ds);
	}

	@Override
	public void updateMeasureState(TextPaint paint) {
		paint.setFakeBoldText(true);
		super.updateMeasureState(paint);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
	}

}
