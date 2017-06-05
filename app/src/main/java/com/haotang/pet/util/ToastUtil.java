package com.haotang.pet.util;

import com.haotang.pet.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {
    private static Toast mToast = null;
	public static void showToast(Context context, String message, int showlocation, int duration){
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.mytoast, null);
		TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
		tvMessage.setText(message);

        if(mToast == null)
            mToast = new Toast(context);
//            mToast.cancel();
//		mToast = new Toast(context);
		mToast.setGravity(showlocation, 0, 0);
		mToast.setDuration(duration);
		mToast.setView(view);
		mToast.show();
	}
	public static void showToastLong(Context context, String message){
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.mytoast, null);
		TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
		tvMessage.setText(message);

        if(mToast == null)
            mToast = new Toast(context);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setView(view);
		mToast.show();
	}
	public static void showToastShort(Context context, String message){
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.mytoast, null);
		TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
		tvMessage.setText(message);
		
		if(mToast == null)
			mToast = new Toast(context);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.setView(view);
		mToast.show();
	}
	public static void showToastShortBottom(Context context, String message){
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.mytoast, null);
		TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
		tvMessage.setText(message);
		tvMessage.setTextSize(13f);
		if(mToast == null)
			mToast = new Toast(context);
		mToast.setGravity(Gravity.BOTTOM, 0, 0);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.setView(view);
		mToast.show();
	}
	public static void showToastShortCenter(Context context, String message){
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.mytoast, null);
		TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
		tvMessage.setText(message);
		if(mToast == null)
			mToast = new Toast(context);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.setView(view);
		mToast.show();
	}


}
