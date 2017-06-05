package com.haotang.pet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class HeadViewListView extends ListView{

	public HeadViewListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HeadViewListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}
