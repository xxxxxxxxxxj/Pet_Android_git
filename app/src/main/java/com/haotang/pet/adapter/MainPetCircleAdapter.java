package com.haotang.pet.adapter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MainPetCircle;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CircleImageView;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:MainPetCircleAdapter
 * </p>
 * <p>
 * Description:首页宠圈精选数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-13 上午11:04:11
 */
@SuppressLint("NewApi")
public class MainPetCircleAdapter<T> extends CommonAdapter<T> {

	public MainPetCircleAdapter(Activity mContext, List<T> mDatas) {
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
				R.layout.item_mainfrag_petcircle, position);
		CircleImageView civ_item_mainfragpetcircleimg = viewHolder
				.getView(R.id.civ_item_mainfragpetcircleimg);
		ImageView iv_mainfrag_petcircle = viewHolder
				.getView(R.id.iv_mainfrag_petcircle);
		TextView tv_item_mainfragpetcircle = viewHolder
				.getView(R.id.tv_item_mainfragpetcircle);
		TextView tv_item_mainfragpetcircle_ydl = viewHolder
				.getView(R.id.tv_item_mainfragpetcircle_ydl);
		TextView tv_item_mainfragpetcircle_title = viewHolder
				.getView(R.id.tv_item_mainfragpetcircle_title);
		final MainPetCircle mainPetCircle = (MainPetCircle) mDatas
				.get(position);
		if (mainPetCircle != null) {
			ImageLoaderUtil.loadImg(mainPetCircle.pic,
					civ_item_mainfragpetcircleimg,
					R.drawable.icon_production_default, null);
			ImageLoaderUtil.loadImg(mainPetCircle.imgUrl,
					iv_mainfrag_petcircle, R.drawable.icon_production_default,
					null);
			Utils.setText(tv_item_mainfragpetcircle, mainPetCircle.groupName,
					"", View.VISIBLE, View.INVISIBLE);
			Utils.setText(tv_item_mainfragpetcircle_ydl,
					mainPetCircle.isReadNum + "", "", View.VISIBLE,
					View.INVISIBLE);
			Utils.setText(tv_item_mainfragpetcircle_title,
					mainPetCircle.summary, "", View.VISIBLE, View.INVISIBLE);
		}
		return viewHolder.getConvertView();
	}
}
