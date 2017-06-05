package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.haotang.pet.PetAddActivity;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;

/**
 * <p>
 * Title:ViewPagerPetAdapter
 * </p>
 * <p>
 * Description:我的界面宠物信息适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-31 下午8:36:13
 */
public class ViewPagerPetAdapter extends PagerAdapter {

	private ArrayList<View> viewList = new ArrayList<View>();
	private Activity context;
	private ArrayList<Pet> petlist = new ArrayList<Pet>();

	public ViewPagerPetAdapter(Activity context, ArrayList<View> viewList,
			ArrayList<Pet> petlist) {
		this.context = context;
		this.viewList.clear();
		this.viewList.addAll(viewList);
		this.petlist.clear();
		this.petlist.addAll(petlist);
	}

	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(viewList.get(position % viewList.size()));
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View view = viewList.get(position % viewList.size());
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (petlist.size() > 20 && position == petlist.size() - 1) {
					ToastUtil.showToastShortCenter(context, "最多支持添加20只宝贝呦");
				} else if (petlist.size() < 20
						&& position == petlist.size() - 1) {
					// 添加按钮
					JumpToNextForData(PetAddActivity.class,
							Global.MY_TO_ADDPET, 0);
				} else {
					JumpToNextForData(PetAddActivity.class,
							Global.PETINFO_TO_EDITPET, position - 1);
				}
			}
		});
		container.addView(view);
		return view;
	}

	private void JumpToNextForData(Class clazz, int requestcode, int position) {
		Pet pet = petlist.get(position);
		Intent intent = new Intent(context, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		context.getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", requestcode);
		intent.putExtra("position", position);
		if (pet != null) {
			intent.putExtra("customerpetid", pet.customerpetid);
			intent.putExtra("nickname", pet.nickName);
			if (pet.certiOrder != null) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("certiOrder", pet.certiOrder);
				intent.putExtras(bundle);
			}
		}
		context.startActivity(intent);
	}
}
