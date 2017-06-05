package com.haotang.pet;

import java.util.ArrayList;

import com.haotang.pet.entity.Production;
import com.haotang.pet.fragment.BeauticianProductionDetailFragment;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class BeauticianProductionDetailActivity extends FragmentActivity {
	private ImageButton ibBack;
	private TextView tvTitle;
	private ViewPager vpProduction;
	private MPagerAdapter adapter;
	private ArrayList<Production> pList;
	private int index;
	private int previous;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beauticianproduction_detail);
		
		index = getIntent().getIntExtra("index", 0);
		previous = getIntent().getIntExtra("previous", 0);
		if(previous==Global.PRE_BEAUTICIANPRODUCTIONLIST_TO_PRODUCTIONDETAIL){
			pList = BeauticianProductuonActivity.productionList;
			
		}else if(previous==Global.PRE_BEAUTICIANDETAIL_TO_PRODUCTIONDETAIL){
			pList = BeauticianDetailActivity.productionList;
		}
		
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		vpProduction = (ViewPager) findViewById(R.id.vp_beauticianproduction_pager);
		adapter = new MPagerAdapter(getSupportFragmentManager());
		
		vpProduction.setAdapter(adapter);
		vpProduction.invalidate();
		adapter.notifyDataSetChanged();
		vpProduction.setCurrentItem(index);
		tvTitle.setText(pList.get(index).title);
		
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	private class MPagerAdapter extends FragmentStatePagerAdapter implements
	ViewPager.OnPageChangeListener {

		public MPagerAdapter(FragmentManager fm) {
			super(fm);
			vpProduction.setOnPageChangeListener(this);
		}
		
		@Override
		public Fragment getItem(int position) {
			BeauticianProductionDetailFragment fragment = (BeauticianProductionDetailFragment) Fragment
					.instantiate(BeauticianProductionDetailActivity.this,
							BeauticianProductionDetailFragment.class.getName());
			fragment.setData(pList.get(position).image, pList.get(position).des,pList.get(position).smallimage);
			return fragment;
		}
		
		@Override
		public int getCount() {
			return pList.size();
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			
		}
		
		@Override
		public void onPageSelected(int position) {
			tvTitle.setText(pList.get(position).title);
		}
	}
	
	
}
