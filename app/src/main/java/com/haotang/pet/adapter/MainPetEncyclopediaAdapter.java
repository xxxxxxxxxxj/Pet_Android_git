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
import com.haotang.pet.entity.MainPetEncyclopedia;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:MainCharacteristicServiceAdapter
 * </p>
 * <p>
 * Description:宠物百科数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-13 上午11:58:57
 */
@SuppressLint("NewApi")
public class MainPetEncyclopediaAdapter<T> extends CommonAdapter<T> {

	public MainPetEncyclopediaAdapter(Activity mContext, List<T> mDatas) {
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
				R.layout.item_mainfrag_petencyclopedia, position);
		ImageView iv_item_mainfragpetencyclopedia = viewHolder
				.getView(R.id.iv_item_mainfragpetencyclopedia);
		TextView tv_item_mainfragpetencyclopedia_title = viewHolder
				.getView(R.id.tv_item_mainfragpetencyclopedia_title);
		final MainPetEncyclopedia mainPetEncyclopedia = (MainPetEncyclopedia) mDatas
				.get(position);
		if (mainPetEncyclopedia != null) {
			ImageLoaderUtil.loadImg(mainPetEncyclopedia.img,
					iv_item_mainfragpetencyclopedia,
					R.drawable.icon_production_default, null);
			Utils.setText(tv_item_mainfragpetencyclopedia_title,
					mainPetEncyclopedia.title, "", View.VISIBLE, View.INVISIBLE);
		}
		return viewHolder.getConvertView();
	}
}
