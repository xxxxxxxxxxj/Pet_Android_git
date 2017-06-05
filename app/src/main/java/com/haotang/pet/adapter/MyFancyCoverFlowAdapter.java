package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.CertiOrder;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.FancyCoverFlow;
import com.haotang.pet.view.SelectableRoundedImageView;

public class MyFancyCoverFlowAdapter extends FancyCoverFlowAdapter {
	private Activity context;
	private ArrayList<Pet> petlist = new ArrayList<Pet>();

	public MyFancyCoverFlowAdapter(Activity context) {
		this.context = context;
	}

	@Override
	public View getCoverFlowItem(final int position, View convertView,
			ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_pet_viewpager,
					null);
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			convertView.setLayoutParams(new FancyCoverFlow.LayoutParams(
					width / 5, FancyCoverFlow.LayoutParams.WRAP_CONTENT));
			holder = new ViewHolder();
			holder.sriv_item_viewpager = (SelectableRoundedImageView) convertView
					.findViewById(R.id.sriv_item_viewpager);
			holder.tv_item_viewpager = (TextView) convertView
					.findViewById(R.id.tv_item_viewpager);
			holder.iv_item_viewpager_member = (ImageView) convertView
					.findViewById(R.id.iv_item_viewpager_member);
			holder.iv_item_viewpager_flag = (ImageView) convertView
					.findViewById(R.id.iv_item_viewpager_flag);
			holder.iv_item_viewpager_flag.bringToFront();
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Pet pet = getItem(position);
		if (pet != null) {
			String avatarPath = pet.image;
			String nickName = pet.nickName;
			int petKind = pet.kindid;
			int certiDogSize = pet.certiDogSize;
			CertiOrder certiOrder = pet.certiOrder;
			int sex = pet.sex;
			if (sex == -1) {
				holder.iv_item_viewpager_flag.setVisibility(View.VISIBLE);
			} else {
				holder.iv_item_viewpager_flag.setVisibility(View.GONE);
			}
			Utils.setStringText(holder.tv_item_viewpager, nickName);
			if (avatarPath != null && !TextUtils.isEmpty(avatarPath)) {
				ImageLoaderUtil.loadImg(avatarPath, holder.sriv_item_viewpager,
						R.drawable.user_icon_unnet, null);
			}
			holder.iv_item_viewpager_member.setVisibility(View.GONE);
			if (petKind != 2) {// 狗
				if (certiDogSize == 1 || certiDogSize == 2) {// 小型犬
					if (certiOrder != null) {
						int status = certiOrder.status;
						if (status == 2) {// 已认证
							holder.iv_item_viewpager_member.bringToFront();
							holder.iv_item_viewpager_member
									.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return petlist.size();
	}

	static class ViewHolder {
		public SelectableRoundedImageView sriv_item_viewpager;
		public TextView tv_item_viewpager;
		public ImageView iv_item_viewpager_member;
		public ImageView iv_item_viewpager_flag;
	}

	@Override
	public Pet getItem(int i) {
		return petlist.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void addAll(ArrayList<Pet> petlist) {
		this.petlist.clear();
		this.petlist.addAll(petlist);
		notifyDataSetChanged();
	}
}
