package com.haotang.pet.fragment;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

public class MTabContent implements TabContentFactory {
	private Context mContext;
	
	public MTabContent(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public View createTabContent(String tag) {
		View view = new View(mContext);
		return view;
	}

}
