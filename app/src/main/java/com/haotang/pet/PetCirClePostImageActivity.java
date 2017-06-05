package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.haotang.pet.fragment.PetCirClePostImageFragment;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;

@SuppressLint("NewApi")
public class PetCirClePostImageActivity extends FragmentActivity{

	private ImageButton ib_titlebar_back;
	private ImageButton ib_titlebar_other;
	private TextView tv_titlebar_title;
	private ViewPager viewPager_post_image;
	
	private MPagerAdapter adapter;
	private int mPosition;
	private int indexTag;
	private int index;
	private ArrayList<String> pList;
	private LayoutInflater mInflater;
	private MProgressDialog mpDialog;
	private RelativeLayout rl_titlebar;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_pet_circle_post_image);
		mpDialog = new MProgressDialog(this);
		MApplication.listAppoint.add(this);
		getIntentFrom();
		initView();
		setView();
		initlistener();
	}

	private void initlistener() {
		// TODO Auto-generated method stub
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(Global.RESULT_OK);
				finish();
			}
		});
		ib_titlebar_other.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDelDialog();
			}
		});
	}
	private void showDelDialog(){
		MDialog mDialog = new MDialog.Builder(this)
		.setType(MDialog.DIALOGTYPE_CONFIRM)
		.setMessage("要删除这张照片吗?")
		.setCancelStr("否")
		.setOKStr("是")
		.positiveListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//这里通知PetCircleInsidePostActivity 刷新 gridview
				if(mPosition==pList.size()-1){
					if (pList.size()==1) {
						pList.remove(mPosition);
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putInt("index",0);
						bundle.putInt("position",mPosition);
						intent.setAction("android.intent.action.PetCircleInsidePostActivity");
						intent.putExtras(bundle);
						sendBroadcast(intent);
						finish();
					}else {
						pList.remove(mPosition);
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putInt("index",0);
						bundle.putInt("position",mPosition);
						intent.setAction("android.intent.action.PetCircleInsidePostActivity");
						intent.putExtras(bundle);
						sendBroadcast(intent);
						PetCirClePostImageFragment fragment = (PetCirClePostImageFragment) adapter.getItem(pList.size()-1);
						fragment.setData(pList.get(pList.size()-1),pList.size(),mPosition,indexTag);
						fragment.onAttach(PetCirClePostImageActivity.this);
						fragment.onCreateView(mInflater, null, null);
						adapter.notifyDataSetChanged();
						viewPager_post_image.invalidate();
						viewPager_post_image.setCurrentItem(pList.size()-1);
					}
				}else {
					pList.remove(mPosition);
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("index",0);
					bundle.putInt("position",mPosition);
					intent.setAction("android.intent.action.PetCircleInsidePostActivity");
					intent.putExtras(bundle);
					sendBroadcast(intent);
					PetCirClePostImageFragment fragment = (PetCirClePostImageFragment) adapter.getItem(mPosition);
					fragment.setData(pList.get(mPosition),pList.size(),mPosition,indexTag);
					fragment.onAttach(PetCirClePostImageActivity.this);
					fragment.onCreateView(mInflater, null, null);
					viewPager_post_image.invalidate();
					adapter.notifyDataSetChanged();
					viewPager_post_image.setCurrentItem(mPosition);
					
				}
			}
		}).build();
		mDialog.show();
	}

	private void getIntentFrom() {
		Bundle bundle = getIntent().getExtras();
		pList = bundle.getStringArrayList("list");
		mPosition = bundle.getInt("position");
		indexTag = bundle.getInt("indexTag");
		
	}

	private void setView() {
		adapter = new MPagerAdapter(getSupportFragmentManager());
		viewPager_post_image.setAdapter(adapter);
		viewPager_post_image.invalidate();
		adapter.notifyDataSetChanged();
		viewPager_post_image.setCurrentItem(mPosition);
	}

	private void initView() {
		mInflater = LayoutInflater.from(this);
		rl_titlebar = (RelativeLayout) findViewById(R.id.rl_titlebar);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		ib_titlebar_other = (ImageButton) findViewById(R.id.ib_titlebar_other);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		viewPager_post_image = (ViewPager) findViewById(R.id.viewPager_post_image);
		tv_titlebar_title.setVisibility(View.GONE);
		ib_titlebar_other.setVisibility(View.VISIBLE);
		ib_titlebar_other.setBackgroundResource(R.drawable.icon_del);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(70, 70);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,30);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		ib_titlebar_other.setLayoutParams(lp);
		
		for (int i = 0; i < pList.size(); i++) {
			if (pList.get(0).startsWith("http://") || pList.get(0).startsWith("https://")) {
				ib_titlebar_other.setVisibility(View.GONE);
			}
		}
		if (indexTag==1) {
			rl_titlebar.setVisibility(View.GONE);
		}
	}
	
	private class MPagerAdapter extends FragmentStatePagerAdapter implements
	ViewPager.OnPageChangeListener {

		public MPagerAdapter(FragmentManager fm) {
			super(fm);
			viewPager_post_image.setOnPageChangeListener(this);
		}
		
		@Override
		public Fragment getItem(int position) {
			PetCirClePostImageFragment fragment = (PetCirClePostImageFragment)Fragment.instantiate(PetCirClePostImageActivity.this, PetCirClePostImageFragment.class.getName());
			fragment.setData(pList.get(position),pList.size(),position,indexTag);
			return fragment;//这个到时候要改为返回的fragment
		}
		
		@Override
		public int getCount() {
			return pList.size();
		}
		
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
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
			mPosition = position;
//			tvTitle.setText(pList.get(position).title);
		}
	}

}
