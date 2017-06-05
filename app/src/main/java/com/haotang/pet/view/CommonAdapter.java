package com.haotang.pet.view;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.haotang.pet.util.SharedPreferenceUtil;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected LayoutInflater mInflater;
	protected Activity mContext;
	protected List<T> mDatas;
	protected SharedPreferenceUtil spUtil;
	protected int flag = -1;
	public CommonAdapter(Activity mContext,List<T> mDatas){
		this.mContext = mContext;
		this.mDatas=mDatas;
		mInflater = LayoutInflater.from(mContext);
		spUtil = SharedPreferenceUtil.getInstance(mContext);
	}
	
	public CommonAdapter(Activity mContext,List<T> mDatas,int flag){
		this.mContext = mContext;
		this.mDatas=mDatas;
		this.flag=flag;
		mInflater = LayoutInflater.from(mContext);
		spUtil = SharedPreferenceUtil.getInstance(mContext);
	}
	public void setData(List<T> mDatas){
		this.mDatas=mDatas;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
