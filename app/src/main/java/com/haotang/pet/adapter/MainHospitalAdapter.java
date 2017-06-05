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
import com.haotang.pet.entity.MainHospital;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:MainCharacteristicServiceAdapter
 * </p>
 * <p>
 * Description:推荐医院数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-13 上午11:58:57
 */
@SuppressLint("NewApi")
public class MainHospitalAdapter<T> extends CommonAdapter<T> {

	public MainHospitalAdapter(Activity mContext, List<T> mDatas) {
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
				R.layout.item_mainfrag_hospital, position);
		ImageView iv_item_mainfraghospital = viewHolder
				.getView(R.id.iv_item_mainfraghospital);
		TextView tv_item_mainfraghospital_title = viewHolder
				.getView(R.id.tv_item_mainfraghospital_title);
		TextView tv_item_mainfraghospital_address = viewHolder
				.getView(R.id.tv_item_mainfraghospital_address);
		final MainHospital mainHospital = (MainHospital) mDatas
				.get(position);
		if (mainHospital != null) {
			ImageLoaderUtil.loadImg(mainHospital.avatar,
					iv_item_mainfraghospital,
					R.drawable.icon_production_default, null);
			Utils.setText(tv_item_mainfraghospital_title,
					mainHospital.name, "", View.VISIBLE,
					View.INVISIBLE);
			Utils.setText(tv_item_mainfraghospital_address,
					mainHospital.addr, "", View.VISIBLE,
					View.INVISIBLE);
		}
		return viewHolder.getConvertView();
	}
}
