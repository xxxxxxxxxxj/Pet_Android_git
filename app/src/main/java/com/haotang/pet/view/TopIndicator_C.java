package com.haotang.pet.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.TopIndicator_B.OnTopIndicatorListener;

@SuppressLint("NewApi")
public class TopIndicator_C extends LinearLayout {

	private static final String TAG = "TopIndicator";
	private List<TextView> mCheckedList = new ArrayList<TextView>();
	private List<TextView> mFlagList = new ArrayList<TextView>();
	private List<View> mViewList = new ArrayList<View>();
	// 顶部菜单的文字数组
	private CharSequence[] mLabels = new CharSequence[] { "中级", "高级", "首席"};
	private int mScreenWidth;
	private int mUnderLineWidth;
	private View mUnderLine;
	// 底部线条移动初始位置
	private int mUnderLineFromX = 0;
	public TopIndicator_C(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TopIndicator_C(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TopIndicator_C(Context context) {
		super(context);
		init(context);
	}
	private void init(final Context context) {
		setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(250, 250, 250));

		mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
		mUnderLineWidth = mScreenWidth / mLabels.length;

		mUnderLine = new View(context);
		mUnderLine.setBackgroundColor(context.getResources().getColor(R.color.aD1494F));
		LinearLayout.LayoutParams underLineParams = new LinearLayout.LayoutParams(
				mUnderLineWidth, 4);

		// 标题layout
		LinearLayout topLayout = new LinearLayout(context);
		LinearLayout.LayoutParams topLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		topLayout.setOrientation(LinearLayout.HORIZONTAL);

		LayoutInflater inflater = LayoutInflater.from(context);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		params.gravity = Gravity.CENTER;

		int size = mLabels.length;
		for (int i = 0; i < size; i++) {

			final int index = i;

//			final View view = inflater.inflate(R.layout.top_indicator_item_t,null);
			final View view = inflater.inflate(R.layout.top_indicator_item_b,null);

			final TextView tvName = (TextView) view.findViewById(R.id.tv_order_name);
			tvName.setTextSize(18);
//			final TextView tvFlag = (TextView) view.findViewById(R.id.tv_order_flag);
			final TextView left  = (TextView) view.findViewById(R.id.left);
			tvName.setText(mLabels[i]);

			topLayout.addView(view, params);
			tvName.setTag(index);
			left.setTag(index);
			Utils.mLogError("==-->left "+index);
//			tvFlag.setTag(index);
			

			mCheckedList.add(tvName);
//			mFlagList.add(tvFlag);
			mFlagList.add(left);
			mViewList.add(view);

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != mTabListener) {
						mTabListener.onIndicatorSelected(index);
					}
				}
			});

			// 初始化 底部菜单选中状态,默认第一个选中
			if (i == 0) {
				tvName.setTextColor(context.getResources().getColor(R.color.aD1494F));
			} else {
//				tvName.setTextColor(Color.rgb(19, 12, 14));
				tvName.setTextColor(Color.rgb(85, 85, 85));
			}

		}
		this.addView(topLayout, topLayoutParams);
		this.addView(mUnderLine, underLineParams);
	}
	public  void init(final Context context,CharSequence[] mLabels) {
		setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(250, 250, 250));

		mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
		mUnderLineWidth = mScreenWidth / mLabels.length;

		mUnderLine = new View(context);
		mUnderLine.setBackgroundColor(context.getResources().getColor(R.color.aD1494F));
		LinearLayout.LayoutParams underLineParams = new LinearLayout.LayoutParams(
				mUnderLineWidth, 4);

		// 标题layout
		LinearLayout topLayout = new LinearLayout(context);
		LinearLayout.LayoutParams topLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		topLayout.setOrientation(LinearLayout.HORIZONTAL);

		LayoutInflater inflater = LayoutInflater.from(context);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		params.gravity = Gravity.CENTER;

		int size = mLabels.length;
		for (int i = 0; i < size; i++) {

			final int index = i;

//			final View view = inflater.inflate(R.layout.top_indicator_item_t,null);
			final View view = inflater.inflate(R.layout.top_indicator_item_b,null);

			final TextView tvName = (TextView) view.findViewById(R.id.tv_order_name);
			tvName.setTextSize(18);
//			final TextView tvFlag = (TextView) view.findViewById(R.id.tv_order_flag);
			final TextView left  = (TextView) view.findViewById(R.id.left);
			tvName.setText(mLabels[i]);

			topLayout.addView(view, params);
			tvName.setTag(index);
			left.setTag(index);
			Utils.mLogError("==-->left "+index);
//			tvFlag.setTag(index);
			

			mCheckedList.add(tvName);
//			mFlagList.add(tvFlag);
			mFlagList.add(left);
			mViewList.add(view);

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != mTabListener) {
						mTabListener.onIndicatorSelected(index);
					}
				}
			});

			// 初始化 底部菜单选中状态,默认第一个选中
			if (i == 0) {
				tvName.setTextColor(context.getResources().getColor(R.color.aD1494F));
			} else {
//				tvName.setTextColor(Color.rgb(19, 12, 14));
				tvName.setTextColor(Color.rgb(85, 85, 85));
			}

		}
		this.addView(topLayout, topLayoutParams);
		this.addView(mUnderLine, underLineParams);
	}

	/**
	 * 设置底部导航中图片显示状态和字体颜色
	 */
	public void setTabsDisplay(Context context, int index) {
		int size = mCheckedList.size();
		for (int i = 0; i < size; i++) {
			TextView kttab = mCheckedList.get(i);
			if ((Integer) (kttab.getTag()) == index) {
				kttab.setTextColor(context.getResources().getColor(R.color.aD1494F));
			} else {
				kttab.setTextColor(Color.rgb(85, 85, 85));
			}
		}
		// 下划线动画
		doUnderLineAnimation(context,index);
	}
	/**
	 * 设置未评价订单的数量
	 * @param num
	 */
	public void setFlagDisplay(int num){
		if(mFlagList.size()>1&&num>0){
			TextView ktflag = mFlagList.get(1);
			ktflag.setVisibility(View.GONE);
			if ((Integer) (ktflag.getTag()) == 1) {
				ktflag.setText(""+num);
				ktflag.setVisibility(View.VISIBLE);
			}
		}else if(num<=0){
			for(int i=0;i<mFlagList.size();i++){
				TextView ktflag = mFlagList.get(i);
				ktflag.setVisibility(View.GONE);
			}
		}
	}

	public void setLeftOneisShow(){
		if (mFlagList.size()>0) {
			TextView left = mFlagList.get(0);
			left.setVisibility(View.GONE);
			if ((Integer)left.getTag()==0) {
				left.setVisibility(View.GONE);
			}
		}
	}
	private void doUnderLineAnimation(Context context,int index) {
		TranslateAnimation animation = new TranslateAnimation(mUnderLineFromX,
				index * mUnderLineWidth, 0, 0);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.setFillAfter(true);
		animation.setDuration(150);
		mUnderLine.startAnimation(animation);
		mUnderLine.setBackgroundColor(context.getResources().getColor(R.color.aD1494F));
		
		mUnderLineFromX = index * mUnderLineWidth;
	}

	// 回调接口
	private OnTopIndicatorListener mTabListener;

	public interface OnTopIndicatorListener {
		void onIndicatorSelected(int index);
	}

	public void setOnTopIndicatorListener(OnTopIndicatorListener listener) {
		this.mTabListener = listener;
	}

}
