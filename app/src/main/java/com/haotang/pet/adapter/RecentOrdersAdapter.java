package com.haotang.pet.adapter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.haotang.pet.ComplaintActivity;
import com.haotang.pet.ComplaintOrderHistoryActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.OrdersBean;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:RecentOrdersAdapter
 * </p>
 * <p>
 * Description:最近订单数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-15 下午6:05:20
 */
@SuppressLint("NewApi")
public class RecentOrdersAdapter<T> extends CommonAdapter<T> {

	public RecentOrdersAdapter(Activity mContext, List<T> mDatas) {
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
				R.layout.item_recentorderadapter, position);
		TextView tv_item_recent_ordertype = viewHolder
				.getView(R.id.tv_item_recent_ordertype);
		TextView tv_item_recent_ordername = viewHolder
				.getView(R.id.tv_item_recent_ordername);
		SelectableRoundedImageView sriv_item_recent_petimg = viewHolder
				.getView(R.id.sriv_item_recent_petimg);
		TextView tv_item_recent_orderyysj = viewHolder
				.getView(R.id.tv_item_recent_orderyysj);
		TextView tv_item_recent_orderno = viewHolder
				.getView(R.id.tv_item_recent_orderno);
		Button btn_item_recent = viewHolder.getView(R.id.btn_item_recent);
		final OrdersBean ordersBean = (OrdersBean) mDatas.get(position);
		if (ordersBean != null) {
			ImageLoaderUtil.loadImg(ordersBean.getAvatar(),
					sriv_item_recent_petimg, R.drawable.user_icon_unnet, null);
			Utils.setText(tv_item_recent_ordertype, ordersBean.getTypeName(),
					"", View.VISIBLE, View.INVISIBLE);
			Utils.setText(tv_item_recent_ordername, ordersBean.getService(),
					"", View.VISIBLE, View.INVISIBLE);
			Utils.setText(tv_item_recent_orderyysj,
					"预约时间：" + ordersBean.getAppointment(), "", View.VISIBLE,
					View.INVISIBLE);
			Utils.setText(tv_item_recent_orderno,
					"订单编号：" + ordersBean.getOrderNum(), "", View.VISIBLE,
					View.INVISIBLE);
			if (ordersBean.isComplaint()) {
				Utils.setText(btn_item_recent, "我要投诉", "", View.VISIBLE,
						View.INVISIBLE);
			} else {
				Utils.setText(btn_item_recent,
						ordersBean.getManagerStatusName(), "", View.VISIBLE,
						View.INVISIBLE);
			}
			int type = ordersBean.getType();
			switch (type) {
			case 1:// 洗护
				tv_item_recent_ordertype.setBackgroundDrawable(Utils
						.getDW("FAA04A"));
				break;
			case 2:// 寄养
				tv_item_recent_ordertype.setBackgroundDrawable(Utils
						.getDW("E5728D"));
				break;
			case 3:// 游泳
				tv_item_recent_ordertype.setBackgroundDrawable(Utils
						.getDW("5BB0EC"));
				break;
			case 4:// 训练
				tv_item_recent_ordertype.setBackgroundDrawable(Utils
						.getDW("6C6CC2"));
				break;
			default:
				break;
			}
			btn_item_recent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (ordersBean.isComplaint()) {
						Intent intent = new Intent();
						intent.setClass(mContext, ComplaintActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("ordersBean", ordersBean);
						intent.putExtras(bundle);
						mContext.startActivity(intent);
					} else {
						mContext.startActivity(new Intent(mContext,
								ComplaintOrderHistoryActivity.class).putExtra(
								"orderid", ordersBean.getId()));
					}
				}
			});
		}
		return viewHolder.getConvertView();
	}
}
