package com.haotang.pet.adapter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CircleImageView;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:HotBeauticianAdapter
 * </p>
 * <p>
 * Description:首页热门美容师数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-10 下午4:19:34
 */
@SuppressLint("NewApi")
public class HotBeauticianAdapter<T> extends CommonAdapter<T> {

	public HotBeauticianAdapter(Activity mContext, List<T> mDatas) {
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
				R.layout.item_mainfrag_hotbeau, position);
		CircleImageView iv_mainbeauticianitem_photo = viewHolder
				.getView(R.id.iv_mainbeauticianitem_photo);
		TextView tv_mainbeauticianitem_name = viewHolder
				.getView(R.id.tv_mainbeauticianitem_name);
		TextView tv_mainbeauticianitem_sign = viewHolder
				.getView(R.id.tv_mainbeauticianitem_sign);
		LinearLayout ll_item_mainfrag_hotbeau = viewHolder
				.getView(R.id.ll_item_mainfrag_hotbeau);
		final Beautician beautician = (Beautician) mDatas.get(position);
		if (beautician != null) {
			if (beautician.colorId == 0) {
				ll_item_mainfrag_hotbeau.setBackgroundColor(mContext
						.getResources().getColor(R.color.aECF4F5));
			} else if (beautician.colorId == 1) {
				ll_item_mainfrag_hotbeau.setBackgroundColor(mContext
						.getResources().getColor(R.color.aEAE8F2));
			} else if (beautician.colorId == 2) {
				ll_item_mainfrag_hotbeau.setBackgroundColor(mContext
						.getResources().getColor(R.color.aF8F1E8));
			}
			ImageLoaderUtil.loadImg(beautician.image,
					iv_mainbeauticianitem_photo,
					R.drawable.icon_production_default, null);
			Utils.setText(tv_mainbeauticianitem_name, beautician.name, "",
					View.VISIBLE, View.GONE);
			Utils.setText(tv_mainbeauticianitem_sign, beautician.levelname
					+ "美容师", "", View.VISIBLE, View.GONE);
		}
		return viewHolder.getConvertView();
	}
}
