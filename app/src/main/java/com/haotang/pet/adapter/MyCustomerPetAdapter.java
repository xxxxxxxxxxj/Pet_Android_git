package com.haotang.pet.adapter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.CertiOrder;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:MyCustomerPetAdapter
 * </p>
 * <p>
 * Description:我的宠物数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-1-16 下午4:50:52
 */
public class MyCustomerPetAdapter<T> extends CommonAdapter<T> {
	private Activity context;
	private ArrayList<Pet> petlist = new ArrayList<Pet>();

	public MyCustomerPetAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public void setData(List<T> mDatas) {
		super.setData(removeDuplicate(mDatas));
		notifyDataSetChanged();
	}

	private List<T> removeDuplicate(List<T> mDatas) {
		Set set = new LinkedHashSet<T>();
		set.addAll(mDatas);
		mDatas.clear();
		mDatas.addAll(set);
		return mDatas;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_pet_viewpager, position);
		SelectableRoundedImageView sriv_item_viewpager = viewHolder
				.getView(R.id.sriv_item_viewpager);
		TextView tv_item_viewpager = viewHolder.getView(R.id.tv_item_viewpager);
		ImageView iv_item_viewpager_member = viewHolder
				.getView(R.id.iv_item_viewpager_member);
		ImageView iv_item_viewpager_flag = viewHolder
				.getView(R.id.iv_item_viewpager_flag);
		iv_item_viewpager_flag.bringToFront();
		Pet pet = (Pet) mDatas.get(position);
		if (pet != null) {
			String avatarPath = pet.image;
			String nickName = pet.nickName;
			int petKind = pet.kindid;
			int certiDogSize = pet.certiDogSize;
			CertiOrder certiOrder = pet.certiOrder;
			int sex = pet.sex;
			if (sex == -1) {
				iv_item_viewpager_flag.setVisibility(View.VISIBLE);
			} else {
				iv_item_viewpager_flag.setVisibility(View.GONE);
			}
			Utils.setStringText(tv_item_viewpager, nickName);
			if (avatarPath != null && !TextUtils.isEmpty(avatarPath)) {
				ImageLoaderUtil.loadImg(avatarPath, sriv_item_viewpager,
						R.drawable.user_icon_unnet, null);
			}
			iv_item_viewpager_member.setVisibility(View.GONE);
			if (petKind != 2) {// 狗
				if (certiDogSize == 1 || certiDogSize == 2) {// 小型犬
					if (certiOrder != null) {
						int status = certiOrder.status;
						if (status == 2) {// 已认证
							iv_item_viewpager_member.bringToFront();
							iv_item_viewpager_member
									.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		}
		return viewHolder.getConvertView();
	}
}
