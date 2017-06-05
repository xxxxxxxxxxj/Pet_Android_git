package com.haotang.pet.adapter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.ComplaintHistory.DataBean.FeedbacksBean;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.MListview;
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
public class CustomerComplaintHistoryAdapter<T> extends CommonAdapter<T> {

	public CustomerComplaintHistoryAdapter(Activity mContext, List<T> mDatas) {
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
				R.layout.item_complainhistory_customer, position);
		TextView tv_item_complainthistorycustomer_tssj = viewHolder
				.getView(R.id.tv_item_complainthistorycustomer_tssj);
		MListview mlv_item_complainthistorycustomer_tswt = viewHolder
				.getView(R.id.mlv_item_complainthistorycustomer_tswt);
		TextView tv_item_complainthistorycustomer_title = viewHolder
				.getView(R.id.tv_item_complainthistorycustomer_title);
		TextView tv_item_complainthistorycustomer_cljg = viewHolder
				.getView(R.id.tv_item_complainthistorycustomer_cljg);
		final FeedbacksBean feedbacksBean = (FeedbacksBean) mDatas
				.get(position);
		if (feedbacksBean != null) {
			Utils.setText(tv_item_complainthistorycustomer_cljg,
					feedbacksBean.getManagerInfo(), "正在处理中,请您耐心等候...",
					View.VISIBLE, View.VISIBLE);
			if (feedbacksBean.getManagerInfo() != null
					&& !TextUtils.isEmpty(feedbacksBean.getManagerInfo())) {
				Utils.setText(tv_item_complainthistorycustomer_tssj,
						feedbacksBean.getManagerTime(), "", View.VISIBLE,
						View.INVISIBLE);
			} else {
				tv_item_complainthistorycustomer_tssj.setVisibility(View.GONE);
			}
			List<String> reasons = feedbacksBean.getReason();
			if (reasons != null && reasons.size() > 0) {
				mlv_item_complainthistorycustomer_tswt
						.setAdapter(new ItemComplaintReasonAdapter<String>(
								mContext, reasons));
			}
		}
		return viewHolder.getConvertView();
	}
}
