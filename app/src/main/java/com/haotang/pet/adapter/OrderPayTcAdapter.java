package com.haotang.pet.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.PetCardInfoCodeBean.CertiCouponsBean;
import com.haotang.pet.util.Utils;

/**
 * <p>
 * Title:OrderPayTcAdapter
 * </p>
 * <p>
 * Description:支付界面套餐信息适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-7-19 上午11:22:23
 */
public class OrderPayTcAdapter extends BaseAdapter {
	private Context context;
	private List<CertiCouponsBean> certiCoupons;
	private int checkItemPosition = -1;

	public OrderPayTcAdapter(Context context,
			List<CertiCouponsBean> certiCoupons) {
		this.context = context;
		this.certiCoupons = certiCoupons;
	}

	@Override
	public int getCount() {
		return certiCoupons.size();
	}

	@Override
	public Object getItem(int position) {
		return certiCoupons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View
					.inflate(context, R.layout.item_oederpay_tc, null);
			holder.tv_item_orderpay_tc = (TextView) convertView
					.findViewById(R.id.tv_item_orderpay_tc);
			holder.iv_item_orderpay_tc = (ImageView) convertView
					.findViewById(R.id.iv_item_orderpay_tc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CertiCouponsBean certiCouponsBean = certiCoupons.get(position);
		String description = certiCouponsBean.getDescription();
		Utils.setStringTextGone(holder.tv_item_orderpay_tc, description);
		if (checkItemPosition != -1) {
			if (checkItemPosition == position) {
				holder.iv_item_orderpay_tc
						.setBackgroundResource(R.drawable.icon_pay_selected);
			} else {
				holder.iv_item_orderpay_tc
						.setBackgroundResource(R.drawable.icon_pay_normal);
			}
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView tv_item_orderpay_tc;
		public ImageView iv_item_orderpay_tc;
	}

	public void setOnItemClick(int position) {
		checkItemPosition = position;
		notifyDataSetChanged();
	}
}
