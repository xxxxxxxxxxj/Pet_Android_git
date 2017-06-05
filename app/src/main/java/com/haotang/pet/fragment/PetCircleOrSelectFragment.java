package com.haotang.pet.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.BaseFragment;
import com.haotang.pet.R;
import com.haotang.pet.guideview.Guide;
import com.haotang.pet.guideview.GuideBuilder;
import com.haotang.pet.guideview.PetCircleComponent;
import com.haotang.pet.util.SharedPreferenceUtil;

@SuppressLint("NewApi")
public class PetCircleOrSelectFragment extends BaseFragment implements
		OnClickListener, OnTouchListener {
	public static PetCircleOrSelectFragment circleOrSelectFragment;
	private RelativeLayout rl_communitytab_selected;
	private RelativeLayout rl_communitytab_circle;
	private View vw_bottom_selected;
	private View vw_bottom_circle;
	private TextView tv_communitytab_selected;
	private TextView tv_communitytab_circle;
	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;
	/**
	 * 用于展示精选界面的Fragment
	 */
	private PostSelectionFragment postSelectionFragment;

	/**
	 * 用于展示宠圈界面的Fragment
	 */
	private PetCircleFragment petCircleFragment;
	private Activity mActivity;
	private SharedPreferenceUtil spUtil;
	private Guide guide;
	private ViewPager viewPager_circle;
	private List<Fragment> allFragment = new ArrayList<Fragment>();
	private View view;
	public static MyAdapter myAdapter;
	@Override
	public void onAttach(Activity activity) {
		this.mActivity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		spUtil = SharedPreferenceUtil.getInstance(mActivity);
		circleOrSelectFragment = this;
		if (view == null)
			view = inflater.inflate(R.layout.activity_community_tab, null);
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		view.setOnTouchListener(this);
		rl_communitytab_selected = (RelativeLayout) view
				.findViewById(R.id.rl_communitytab_selected);
		rl_communitytab_circle = (RelativeLayout) view
				.findViewById(R.id.rl_communitytab_circle);
		vw_bottom_selected = (View) view.findViewById(R.id.vw_bottom_selected);
		vw_bottom_circle = (View) view.findViewById(R.id.vw_bottom_circle);
		viewPager_circle = (ViewPager) view.findViewById(R.id.viewPager_circle);
		tv_communitytab_selected = (TextView) view
				.findViewById(R.id.tv_communitytab_selected);
		tv_communitytab_circle = (TextView) view
				.findViewById(R.id.tv_communitytab_circle);
		fragmentManager = getFragmentManager();
		rl_communitytab_selected.setOnClickListener(this);
		rl_communitytab_circle.setOnClickListener(this);
		allFragment.clear();
		postSelectionFragment = new PostSelectionFragment(mActivity);
		petCircleFragment = new PetCircleFragment(mActivity);
		allFragment.add(postSelectionFragment);
		allFragment.add(petCircleFragment);
		myAdapter = new MyAdapter(getChildFragmentManager(),allFragment);
		viewPager_circle.setAdapter(myAdapter);
		try {
			myAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewPager_circle.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					tv_communitytab_selected.setTextColor(getResources()
							.getColor(R.color.white));
					tv_communitytab_circle.setTextColor(getResources()
							.getColor(R.color.bbbbbb));
					vw_bottom_selected.setVisibility(View.VISIBLE);
					vw_bottom_circle.setVisibility(View.GONE);
					break;
				case 1:
					tv_communitytab_selected.setTextColor(getResources()
							.getColor(R.color.bbbbbb));
					tv_communitytab_circle.setTextColor(getResources()
							.getColor(R.color.white));
					vw_bottom_selected.setVisibility(View.GONE);
					vw_bottom_circle.setVisibility(View.VISIBLE);
					break;
				}
			}

			@Override
			public void onPageScrolled(int position, float offset,
					int offsetPixels) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}

		});
		viewPager_circle.setCurrentItem(0);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//initGuide();
	}

	public void initGuide() {
		boolean TAG_PETCIRCLECOMPONENT = spUtil.getBoolean(
				"TAG_PETCIRCLECOMPONENT", false);
		if (!TAG_PETCIRCLECOMPONENT) {
			getActivity()
					.getWindow()
					.getDecorView()
					.getViewTreeObserver()
					.addOnGlobalLayoutListener(
							new ViewTreeObserver.OnGlobalLayoutListener() {
								@Override
								public void onGlobalLayout() {
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
										getActivity()
												.getWindow()
												.getDecorView()
												.getViewTreeObserver()
												.removeOnGlobalLayoutListener(
														this);
									} else {
										getActivity()
												.getWindow()
												.getDecorView()
												.getViewTreeObserver()
												.removeGlobalOnLayoutListener(
														this);
									}
									showGuideView();
								}
							});
		}
	}

	public void showGuideView() {
		GuideBuilder builder = new GuideBuilder();
		builder.setTargetView(tv_communitytab_circle).setAlpha(150)
				.setHighTargetCorner(20).setHighTargetPaddingTop(15)
				.setHighTargetPaddingBottom(15).setHighTargetPaddingLeft(30)
				.setAutoDismiss(false).setHighTargetPaddingRight(30)
				.setOverlayTarget(false).setOutsideTouchable(false)
				.setExitAnimationId(android.R.anim.fade_out);
		builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
			@Override
			public void onShown() {
			}

			@Override
			public void onDismiss() {
				spUtil.saveBoolean("TAG_PETCIRCLECOMPONENT", true);
				if (postSelectionFragment == null) {
					postSelectionFragment = new PostSelectionFragment();
				}
				postSelectionFragment.initGuide();
			}
		});
		builder.addComponent(new PetCircleComponent(clickListener));
		guide = builder.createGuide();
		guide.setShouldCheckLocInWindow(true);
		guide.show(getActivity());
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView1:
				guide.dismiss();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_communitytab_selected:
			viewPager_circle.setCurrentItem(0);
			tv_communitytab_selected.setTextColor(getResources().getColor(
					R.color.white));
			tv_communitytab_circle.setTextColor(getResources().getColor(
					R.color.bbbbbb));
			vw_bottom_selected.setVisibility(View.VISIBLE);
			vw_bottom_circle.setVisibility(View.GONE);
			break;
		case R.id.rl_communitytab_circle:
			viewPager_circle.setCurrentItem(1);
			tv_communitytab_selected.setTextColor(getResources().getColor(
					R.color.bbbbbb));
			tv_communitytab_circle.setTextColor(getResources().getColor(
					R.color.white));
			vw_bottom_selected.setVisibility(View.GONE);
			vw_bottom_circle.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	class MyAdapter extends FragmentPagerAdapter {
		private List<Fragment> allFragment;
		public MyAdapter(FragmentManager fm,List<Fragment> allFragment) {
			super(fm);
			this.allFragment = allFragment;
		}

		@Override
		public Fragment getItem(int arg0) {
			return allFragment.get(arg0);
		}

		@Override
		public int getCount() {
			return allFragment.size();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			myAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
