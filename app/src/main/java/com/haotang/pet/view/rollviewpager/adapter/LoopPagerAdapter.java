package com.haotang.pet.view.rollviewpager.adapter;

import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.haotang.pet.view.rollviewpager.HintView;
import com.haotang.pet.view.rollviewpager.RollPagerView;

/**
 * Created by Mr.Jude on 2016/1/9.
 */
public abstract class LoopPagerAdapter extends PagerAdapter{
    private RollPagerView mViewPager;

    private ArrayList<View> mViewList = new ArrayList<View>();

    private class LoopHintViewDelegate implements RollPagerView.HintViewDelegate{
        @Override
        public void setCurrentPosition(int position, HintView hintView) {
            if (hintView!=null && getRealCount() != 0)
                hintView.setCurrent(position%getRealCount());
        }

        @Override
        public void initView(int length, int gravity, HintView hintView) {
            if (hintView!=null)
                hintView.initView(getRealCount(),gravity);
        }
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mViewList.clear();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        if (getCount() == 0)return;
        int half = Integer.MAX_VALUE/2;
        int start = half - half%getRealCount();
        mViewPager.getViewPager().setCurrentItem(start,false);
    }

    public LoopPagerAdapter(RollPagerView viewPager){
        this.mViewPager = viewPager;
        viewPager.setHintViewDelegate(new LoopHintViewDelegate());
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = position%getRealCount();
        View itemView = findViewByPosition(container,realPosition);
        container.addView(itemView);
        return itemView;
    }


    private View findViewByPosition(ViewGroup container,int position){
        for (View view : mViewList) {
            if (((Integer)view.getTag()) == position&&view.getParent()==null){
                return view;
            }
        }
        View view = getView(container,position);
        view.setTag(position);
        mViewList.add(view);
        return view;
    }

    public abstract View getView(ViewGroup container, int position);

    @Deprecated
    @Override
    public final int getCount() {
    	int countReal = 0;
		try {
			countReal = getRealCount()<=1?getRealCount():Integer.MAX_VALUE;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return countReal;
    }

    protected abstract int getRealCount();
}
