package com.haotang.pet.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.AllLevel;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.view.FancyCoverFlow;

public class MemberFancyCoverFlowAdapter extends FancyCoverFlowAdapter {
	private Activity context;
	private ArrayList<AllLevel> allLevelList = new ArrayList<AllLevel>();
	private int localmemberLevelId;
	private ArrayList<String> unSelectPic = new ArrayList<String>();
	private ArrayList<String> selectPic = new ArrayList<String>();
	private ArrayList<String> levelPic = new ArrayList<String>();

	public MemberFancyCoverFlowAdapter(Activity context) {
		this.context = context;
	}

	@Override
	public View getCoverFlowItem(final int position, View convertView,
			ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_usermember_level,
					null);
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			convertView.setLayoutParams(new FancyCoverFlow.LayoutParams(
					width / 3, FancyCoverFlow.LayoutParams.WRAP_CONTENT));
			holder = new ViewHolder();
			holder.tv_item_usermember_level = (TextView) convertView
					.findViewById(R.id.tv_item_usermember_level);
			holder.iv_item_usermember_level = (ImageView) convertView
					.findViewById(R.id.iv_item_usermember_level);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AllLevel allLevel = getItem(position);
		if (allLevel != null) {
			String growthValue = allLevel.growthValue;
			int MemberLevelId = allLevel.MemberLevelId;
			String string = unSelectPic.get(position);
			String string1 = selectPic.get(position);
			String string2 = levelPic.get(position);
			if (localmemberLevelId == MemberLevelId) {
				ImageLoaderUtil.loadImgLocal(string2,
						holder.iv_item_usermember_level, 0, null);
			} else if (localmemberLevelId > MemberLevelId) {
				ImageLoaderUtil.loadImgLocal(string1,
						holder.iv_item_usermember_level, 0, null);
			} else if (localmemberLevelId < MemberLevelId) {
				ImageLoaderUtil.loadImgLocal(string,
						holder.iv_item_usermember_level, 0, null);
			}
			if (growthValue != null && !TextUtils.isEmpty(growthValue)) {
				holder.tv_item_usermember_level.setText(growthValue);
			}
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return allLevelList.size();
	}

	static class ViewHolder {
		public TextView tv_item_usermember_level;
		public ImageView iv_item_usermember_level;
	}

	@Override
	public AllLevel getItem(int i) {
		return allLevelList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void addAll(ArrayList<AllLevel> allLevelList, int memberLevelId,
			List<String> unSelectPic, List<String> selectPic,
			List<String> levelPic) {
		this.localmemberLevelId = memberLevelId;
		this.allLevelList.clear();
		this.allLevelList.addAll(allLevelList);
		this.unSelectPic.clear();
		this.unSelectPic.addAll(unSelectPic);
		this.selectPic.clear();
		this.selectPic.addAll(selectPic);
		this.levelPic.clear();
		this.levelPic.addAll(levelPic);
		notifyDataSetChanged();
	}
}
