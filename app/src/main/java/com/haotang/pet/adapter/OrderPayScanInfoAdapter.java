package com.haotang.pet.adapter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.OrderPayActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.OrderPayScanInfo;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:OrderPayScanInfoAdapter
 * </p>
 * <p>
 * Description:扫码支付商品信息数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-11-24 下午4:05:46
 */
public class OrderPayScanInfoAdapter<T> extends CommonAdapter<T> {
	public OrderPayScanInfoAdapter(Activity mContext, List<T> mDatas) {
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
				R.layout.item_scanshopinfo, position);
		TextView tv_item_scanshopinfo_price = viewHolder
				.getView(R.id.tv_item_scanshopinfo_price);
		TextView tv_item_scanshopinfo_name = viewHolder
				.getView(R.id.tv_item_scanshopinfo_name);
		final OrderPayScanInfo orderPayScanInfo = (OrderPayScanInfo) mDatas
				.get(position);
		if (orderPayScanInfo != null) {
			String item = orderPayScanInfo.item;
			int amount = orderPayScanInfo.amount;
			double price = orderPayScanInfo.price;
			Utils.setText(tv_item_scanshopinfo_name, item, "", View.VISIBLE,
					View.INVISIBLE);
			String text = "¥ " + Math.round(price) + " x " + amount;
			SpannableString ss = new SpannableString(text);
			ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), 0, 1,
					Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			tv_item_scanshopinfo_price.setText(ss);
		}
		return viewHolder.getConvertView();
	}
}
