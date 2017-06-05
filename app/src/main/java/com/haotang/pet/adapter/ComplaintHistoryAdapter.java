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
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.SelectableRoundedImageView;
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
public class ComplaintHistoryAdapter<T> extends CommonAdapter<T> {

	public ComplaintHistoryAdapter(Activity mContext, List<T> mDatas) {
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
				R.layout.item_complainhistory_order, position);
		TextView tv_item_complainthistory_tssj = viewHolder
				.getView(R.id.tv_item_complainthistory_tssj);
		TextView tv_item_complainthistory_ordertype = viewHolder
				.getView(R.id.tv_item_complainthistory_ordertype);
		TextView tv_item_complainthistory_ordername = viewHolder
				.getView(R.id.tv_item_complainthistory_ordername);
		TextView tv_item_complainthistory_state = viewHolder
				.getView(R.id.tv_item_complainthistory_state);
		SelectableRoundedImageView sriv_item_complainthistory_petimg = viewHolder
				.getView(R.id.sriv_item_complainthistory_petimg);
		TextView tv_item_complainthistory_orderfwsj = viewHolder
				.getView(R.id.tv_item_complainthistory_orderfwsj);
		TextView tv_item_complainthistory_orderprice = viewHolder
				.getView(R.id.tv_item_complainthistory_orderprice);
		TextView tv_item_complainthistory_orderno = viewHolder
				.getView(R.id.tv_item_complainthistory_orderno);
		MListview mlv_item_complainthistory_tswt = viewHolder
				.getView(R.id.mlv_item_complainthistory_tswt);
		TextView tv_item_complainthistory_cljg_title = viewHolder
				.getView(R.id.tv_item_complainthistory_cljg_title);
		TextView tv_item_complainthistory_cljg = viewHolder
				.getView(R.id.tv_item_complainthistory_cljg);
		final FeedbacksBean feedbacksBean = (FeedbacksBean) mDatas
				.get(position);
		if (feedbacksBean != null) {
			Utils.setText(tv_item_complainthistory_cljg,
					feedbacksBean.getManagerInfo(), "正在处理中,请您耐心等候...",
					View.VISIBLE, View.VISIBLE);
			Utils.setText(tv_item_complainthistory_state,
					feedbacksBean.getManagerStatusName(), "", View.VISIBLE,
					View.INVISIBLE);
			if (feedbacksBean.getManagerInfo() != null
					&& !TextUtils.isEmpty(feedbacksBean.getManagerInfo())) {
				Utils.setText(tv_item_complainthistory_tssj,
						feedbacksBean.getManagerTime(), "", View.VISIBLE,
						View.INVISIBLE);
			} else {
				tv_item_complainthistory_tssj.setVisibility(View.GONE);
			}
			Utils.setText(tv_item_complainthistory_ordertype,
					feedbacksBean.getTypeName(), "", View.VISIBLE,
					View.INVISIBLE);
			Utils.setText(tv_item_complainthistory_ordername,
					feedbacksBean.getService(), "", View.VISIBLE,
					View.INVISIBLE);
			Utils.setText(tv_item_complainthistory_orderfwsj,
					feedbacksBean.getAppointment(), "", View.VISIBLE,
					View.INVISIBLE);
			Utils.setText(tv_item_complainthistory_orderprice, "¥"
					+ feedbacksBean.getPay_price(), "", View.VISIBLE,
					View.INVISIBLE);
			Utils.setText(tv_item_complainthistory_orderno, "订单编号："
					+ feedbacksBean.getOrderNum(), "", View.VISIBLE,
					View.INVISIBLE);
			ImageLoaderUtil.loadImg(feedbacksBean.getAvatar(),
					sriv_item_complainthistory_petimg,
					R.drawable.user_icon_unnet, null);
			List<String> reasons = feedbacksBean.getReason();
			if (reasons != null && reasons.size() > 0) {
				mlv_item_complainthistory_tswt
						.setAdapter(new ItemComplaintReasonAdapter<String>(
								mContext, reasons));
			}
			int type = feedbacksBean.getType();
			switch (type) {
			case 1:// 洗护
				tv_item_complainthistory_ordertype.setBackgroundDrawable(Utils
						.getDW("FAA04A"));
				break;
			case 2:// 寄养
				tv_item_complainthistory_ordertype.setBackgroundDrawable(Utils
						.getDW("E5728D"));
				break;
			case 3:// 游泳
				tv_item_complainthistory_ordertype.setBackgroundDrawable(Utils
						.getDW("5BB0EC"));
				break;
			case 4:// 训练
				tv_item_complainthistory_ordertype.setBackgroundDrawable(Utils
						.getDW("6C6CC2"));
				break;
			default:
				break;
			}
		}
		return viewHolder.getConvertView();
	}
}
