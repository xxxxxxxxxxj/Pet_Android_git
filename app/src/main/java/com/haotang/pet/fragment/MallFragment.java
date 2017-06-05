package com.haotang.pet.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.haotang.pet.LoginActivity;
import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.umeng.analytics.MobclickAgent;
/**
 * 显示所有主框架的Fragment
 * 订单
 * @author Administrator
 *	
 */
@SuppressLint("Recycle") 
public class MallFragment extends Fragment implements OnTouchListener{

	private View view;
	private MainActivity mainActivity;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mainActivity =(MainActivity) activity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getChildFragmentManager().beginTransaction().replace(R.id.layout_show_my_order, new MyOrderFragment()).commitAllowingStateLoss();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.mallfragment, null);
		convertView.setOnTouchListener(this);
		return convertView;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		view = getView();
		
		findView();
		setView();
	}


	private void findView() {
		
	}


	private void setView() {
		
	}
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getChildFragmentManager().beginTransaction().replace(R.id.layout_show_my_order, new MyOrderFragment()).commitAllowingStateLoss();
		if (SharedPreferenceUtil.getInstance(mainActivity).getString("cellphone", "0").equals("0")) {
			Intent intent = new Intent(mainActivity, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			mainActivity.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			intent.putExtra("previous", Global.PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT);
			startActivity(intent);
		}
		MobclickAgent.onPageStart("MallFragment"); //统计页面
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("MallFragment"); 
	}
	
}
