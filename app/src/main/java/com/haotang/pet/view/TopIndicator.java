package com.haotang.pet.view;

import java.util.ArrayList;
import java.util.List;

import com.haotang.pet.R;
import com.haotang.pet.entity.KnowledgeTopTab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 顶部indicator
 * 
 * @author dewyze
 * 
 */
@SuppressLint("NewApi")
public class TopIndicator extends LinearLayout {

	private static final String TAG = "TopIndicator";
	private int[] mDrawableIds = new int[] {R.drawable.bg_icon_feed,R.drawable.bg_icon_nurse,
			R.drawable.bg_icon_health,R.drawable.bg_icon_supplie,R.drawable.bg_icon_train};
	private List<KnowledgeTopTab> mCheckedList = new ArrayList<KnowledgeTopTab>();
	private List<View> mViewList = new ArrayList<View>();
	// 顶部菜单的文字数组
			private CharSequence[] mLabels = new CharSequence[] { "喂养", "护理", "健康",
			"用品","训练" };
	private int mScreenWidth;
	private int mUnderLineWidth;
	private View mUnderLine;
	// 底部线条移动初始位置
	private int mUnderLineFromX = 0;

	public TopIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TopIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TopIndicator(Context context) {
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
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		topLayout.setOrientation(LinearLayout.HORIZONTAL);

		LayoutInflater inflater = LayoutInflater.from(context);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		params.gravity = Gravity.CENTER;

		int size = mLabels.length;
		for (int i = 0; i < size; i++) {

			final int index = i;

			final View view = inflater.inflate(R.layout.top_indicator_item,
					null);

			final CheckedTextView ctvImage = (CheckedTextView) view
					.findViewById(R.id.ctv_knowledgetoptab_image);
			final TextView tvName = (TextView) view
					.findViewById(R.id.tv_knowledgetoptab_name);
			ctvImage.setCompoundDrawablesWithIntrinsicBounds(context
					.getResources().getDrawable(mDrawableIds[i]), null, null,
					null);
			ctvImage.setCompoundDrawablePadding(6);
			tvName.setText(mLabels[i]);

			topLayout.addView(view, params);
			KnowledgeTopTab kttab = new KnowledgeTopTab(context);
			kttab.mCheckedTextView = ctvImage;
			kttab.mTextView = tvName;
			kttab.setTag(index);
			

			mCheckedList.add(kttab);
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
				ctvImage.setChecked(true);
				tvName.setTextColor(context.getResources().getColor(R.color.aD1494F));
			} else {
				ctvImage.setChecked(false);
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
			KnowledgeTopTab kttab = mCheckedList.get(i);
			if ((Integer) (kttab.getTag()) == index) {
				kttab.mCheckedTextView.setChecked(true);
				
				if(index == 0){
					kttab.mTextView.setTextColor(context.getResources().getColor(R.color.aD1494F));
				}else if(index == 1){
					kttab.mTextView.setTextColor(Color.rgb(44, 202, 119));
				}else if(index == 2){
					kttab.mTextView.setTextColor(Color.rgb(157, 177, 251));
				}else if(index == 3){
					kttab.mTextView.setTextColor(Color.rgb(56, 194, 243));
				}else if(index == 4){
					kttab.mTextView.setTextColor(context.getResources().getColor(R.color.aD1494F));
				}else{
					kttab.mTextView.setTextColor(context.getResources().getColor(R.color.aD1494F));
				}
			} else {
				kttab.mCheckedTextView.setChecked(false);
				kttab.mTextView.setTextColor(Color.rgb(85, 85, 85));
			}
		}
		// 下划线动画
		doUnderLineAnimation(context,index);
	}

	private void doUnderLineAnimation(Context context, int index) {
		TranslateAnimation animation = new TranslateAnimation(mUnderLineFromX,
				index * mUnderLineWidth, 0, 0);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.setFillAfter(true);
		animation.setDuration(150);
		mUnderLine.startAnimation(animation);
		if(index == 0){
			mUnderLine.setBackgroundColor(context.getResources().getColor(R.color.aD1494F));
		}else if(index == 1){
			mUnderLine.setBackgroundColor(Color.rgb(44, 202, 119));
		}else if(index == 2){
			mUnderLine.setBackgroundColor(Color.rgb(157, 177, 251));
		}else if(index == 3){
			mUnderLine.setBackgroundColor(Color.rgb(56, 194, 243));
		}else if(index == 4){
			mUnderLine.setBackgroundColor(context.getResources().getColor(R.color.aD1494F));
		}else{
			mUnderLine.setBackgroundColor(context.getResources().getColor(R.color.aD1494F));
		}
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
