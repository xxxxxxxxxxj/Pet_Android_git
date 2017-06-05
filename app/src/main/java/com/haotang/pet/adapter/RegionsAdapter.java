package com.haotang.pet.adapter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ServiceAreaCodeBean.AreasBean;
import com.haotang.pet.entity.ShopListBean.DataBean.RegionsBean;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:RegionsAdapter
 * </p>
 * <p>
 * Description:服务区域数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-13 下午4:14:53
 */
@SuppressLint("NewApi")
public class RegionsAdapter<T> extends CommonAdapter<T> {
	private int clickItemPosition;

	public RegionsAdapter(Activity mContext, List<T> mDatas, int flag) {
		super(mContext, mDatas);
		this.flag = flag;
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
				R.layout.item_regions_adapter, position);
		TextView tv_item_regions = viewHolder.getView(R.id.tv_item_regions);
		View vw_item_regions = viewHolder.getView(R.id.vw_item_regions);
		if(flag == 0){
			final AreasBean areasBean = (AreasBean) mDatas.get(position);
			if (areasBean != null) {
				if (clickItemPosition == position) {
					SpannableString ss = new SpannableString(areasBean.getRegion());
					ss.setSpan(new TextAppearanceSpan(mContext,
							R.style.styleorderprice), 0, ss.length(),
							Spanned.SPAN_INCLUSIVE_INCLUSIVE);
					tv_item_regions.setText(ss);
					vw_item_regions.setVisibility(View.VISIBLE);
					tv_item_regions.setTextColor(mContext.getResources().getColor(
							R.color.aD1494F));
				} else {
					SpannableString ss = new SpannableString(areasBean.getRegion());
					ss.setSpan(new TextAppearanceSpan(mContext, R.style.style4), 0,
							ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
					tv_item_regions.setText(ss);
					vw_item_regions.setVisibility(View.INVISIBLE);
					tv_item_regions.setTextColor(mContext.getResources().getColor(
							R.color.black));
				}
			}
		}else if(flag == 1){
			final RegionsBean regionsBean = (RegionsBean) mDatas.get(position);
			if (regionsBean != null) {
				if (clickItemPosition == position) {
					SpannableString ss = new SpannableString(regionsBean.getRegion());
					ss.setSpan(new TextAppearanceSpan(mContext,
							R.style.styleorderprice), 0, ss.length(),
							Spanned.SPAN_INCLUSIVE_INCLUSIVE);
					tv_item_regions.setText(ss);
					vw_item_regions.setVisibility(View.VISIBLE);
					tv_item_regions.setTextColor(mContext.getResources().getColor(
							R.color.aD1494F));
				} else {
					SpannableString ss = new SpannableString(regionsBean.getRegion());
					ss.setSpan(new TextAppearanceSpan(mContext, R.style.style4), 0,
							ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
					tv_item_regions.setText(ss);
					vw_item_regions.setVisibility(View.INVISIBLE);
					tv_item_regions.setTextColor(mContext.getResources().getColor(
							R.color.black));
				}
			}
		}
		return viewHolder.getConvertView();
	}

	public void setClickItem(int position) {
		this.clickItemPosition = position;
		notifyDataSetChanged();
	}
}
