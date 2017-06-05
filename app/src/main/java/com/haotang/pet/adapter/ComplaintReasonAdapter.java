package com.haotang.pet.adapter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ComplaintReasonStr;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:MainCharacteristicServiceAdapter
 * </p>
 * <p>
 * Description:投诉原因数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-13 上午11:58:57
 */
@SuppressLint("NewApi")
public class ComplaintReasonAdapter<T> extends CommonAdapter<T> {

	public ComplaintReasonAdapter(Activity mContext, List<T> mDatas) {
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
				R.layout.item_complaint_reason, position);
		CheckBox cb_item_complaint_reason = viewHolder
				.getView(R.id.cb_item_complaint_reason);
		TextView tv_item_complaint_reason = viewHolder
				.getView(R.id.tv_item_complaint_reason);
		RelativeLayout rl_item_complaint_reason = viewHolder
				.getView(R.id.rl_item_complaint_reason);
		final ComplaintReasonStr complaintReasonStr = (ComplaintReasonStr) mDatas
				.get(position);
		if (complaintReasonStr != null) {
			Utils.setText(tv_item_complaint_reason, (position + 1) + "."
					+ complaintReasonStr.getStr(), "", View.VISIBLE,
					View.INVISIBLE);
			cb_item_complaint_reason.setChecked(complaintReasonStr.isCheck());
			cb_item_complaint_reason.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ComplaintReasonStr complaintReasonStr = (ComplaintReasonStr) mDatas
							.get(position);
					complaintReasonStr.setCheck(!complaintReasonStr.isCheck());
					notifyDataSetChanged();
				}
			});
			rl_item_complaint_reason.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ComplaintReasonStr complaintReasonStr = (ComplaintReasonStr) mDatas
							.get(position);
					complaintReasonStr.setCheck(!complaintReasonStr.isCheck());
					notifyDataSetChanged();
				}
			});
		}
		return viewHolder.getConvertView();
	}
}
