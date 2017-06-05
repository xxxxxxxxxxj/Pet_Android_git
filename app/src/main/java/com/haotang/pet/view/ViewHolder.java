package com.haotang.pet.view;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ImageLoaderUtil;
import com.melink.bqmmsdk.sdk.BQMMMessageHelper;
import com.melink.bqmmsdk.widget.BQMMMessageText;

public class ViewHolder {

	private final SparseArray<View> mViews;
	private View mConvertView;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		// setTag
		mConvertView.setTag(this);

	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {

		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {

		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		if (text != null && !TextUtils.isEmpty(text)) {
			view.setText(text);
		}
		return this;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setBQMMMessageText(int viewId, String text, int id,
			int type) {
		BQMMMessageText view = getView(viewId);
		if (text != null && !TextUtils.isEmpty(text)) {
			if (text.contains("［")) {
				text = text.replaceAll("［", "[");
			}
			if (text.contains("］")) {
				text = text.replaceAll("］", "]");
			}
			if (text.contains("＂")) {
				text = text.replaceAll("＂", "\"");
			}
			try {
				view.setVisibility(View.VISIBLE);
				if (type == 2) {// 大表情
					view.showMessage(id + "", BQMMMessageHelper
							.getMsgCodeString(new JSONArray(text)),
							BQMMMessageText.FACETYPE, new JSONArray(text));
					view.getBackground().setAlpha(0);
				} else if (type == 1) {// 小表情或文字或图文混排
					view.showMessage(id + "", BQMMMessageHelper
							.getMsgCodeString(new JSONArray(text)),
							BQMMMessageText.EMOJITYPE, new JSONArray(text));
					view.getBackground().setAlpha(255);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				view.setText(text);
			}
		} else {
			view.setVisibility(View.INVISIBLE);
		}
		return this;
	}

	/**
	 * 为TextView设置颜色
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setTextColor(int viewId, String color) {
		TextView view = getView(viewId);
		view.setTextColor(Color.parseColor(color));
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		return this;
	}

	/**
	 * 为ImageView设置背景图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setBackgroundResource(int viewId, int drawableId) {
		View view = getView(viewId);
		view.setBackgroundResource(drawableId);
		return this;
	}

	/**
	 * 为圆形ImageView设置背景图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setBackgroundCircle(int viewId, String imageurl,
			int drawableId) {
		try {
			try {
				if (imageurl.startsWith("drawable://")) {
				} else if (imageurl.startsWith("file://")) {
				} else if (!imageurl.startsWith("http://")&& !imageurl.startsWith("https://")) {
					imageurl = CommUtil.getServiceNobacklash() + imageurl;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SelectableRoundedImageView view = getView(viewId);
			view.setTag(imageurl);
			ImageLoaderUtil.setImageWithTag(view, imageurl, drawableId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 为正常ImageView设置背景图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setBackgroundNormal(int viewId, String imageurl,
			int drawableId) {
		try {
			ImageView view = getView(viewId);
			try {
				if (imageurl.startsWith("drawable://")) {
				} else if (imageurl.startsWith("file://")) {
				} else if (!imageurl.startsWith("http://")
						&& !imageurl.startsWith("https://")) {
					imageurl = CommUtil.getServiceNobacklash() + imageurl;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.e("TAG", "imageurl = "+imageurl);
			view.setTag(imageurl);
			ImageLoaderUtil.setImageWithTag(view, imageurl, drawableId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 为圆形ImageView设置背景图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setBackgroundSex(int viewId, int drawableId) {
		SelectableRoundedImageView view = getView(viewId);
		view.setBackgroundResource(drawableId);
		return this;
	}

	/**
	 * 为圆形ImageView设置背景图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setBackgroundCircleImage(int viewId, String imageurl,
			int drawableId) {
		CircleImageView view = getView(viewId);
		view.setTag(imageurl);
		ImageLoaderUtil.setImageWithTag(view, imageurl, drawableId);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 设置空间是否隐藏
	 * 
	 * @return
	 */
	public ViewHolder setViewVisible(int viewId, int visible) {
		TextView view = getView(viewId);
		view.setVisibility(visible);
		return this;
	}

	public View getConvertView() {
		return mConvertView;
	}
}
