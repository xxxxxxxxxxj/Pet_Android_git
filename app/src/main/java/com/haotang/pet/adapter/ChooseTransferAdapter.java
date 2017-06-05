package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Transfers;
import com.haotang.pet.util.Utils;

/**
 * <p>
 * Title:ChooseTransferAdapter
 * </p>
 * <p>
 * Description:寄养选择房型界面中转站信息适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-9-1 下午3:24:52
 */
public class ChooseTransferAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Transfers> transfersList = new ArrayList<Transfers>();
	private int clickItem = -1;

	public ChooseTransferAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return transfersList.size();
	}

	@Override
	public Object getItem(int position) {
		return transfersList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Transfers transfers = transfersList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_choosetransfer,
					null);
			holder.tv_item_choosetransfer_name = (TextView) convertView
					.findViewById(R.id.tv_item_choosetransfer_name);
			holder.iv_item_choosetransfer_select = (ImageView) convertView
					.findViewById(R.id.iv_item_choosetransfer_select);
			holder.tv_item_choosetransfer_address = (TextView) convertView
					.findViewById(R.id.tv_item_choosetransfer_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (transfers != null) {
			String name = transfers.name;
			String address = transfers.address;
			Utils.setStringTextGone(holder.tv_item_choosetransfer_name, name);
			Utils.setStringTextGone(holder.tv_item_choosetransfer_address,
					address);
			if (clickItem == position) {
				holder.iv_item_choosetransfer_select
						.setVisibility(View.VISIBLE);
				holder.tv_item_choosetransfer_name.setTextColor(context
						.getResources().getColor(R.color.aBB996C));
			} else {
				holder.iv_item_choosetransfer_select.setVisibility(View.GONE);
				holder.tv_item_choosetransfer_name.setTextColor(context
						.getResources().getColor(R.color.a333333));
			}
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView tv_item_choosetransfer_name;
		public TextView tv_item_choosetransfer_address;
		public ImageView iv_item_choosetransfer_select;
	}

	public void setData(ArrayList<Transfers> transfersList) {
		this.transfersList.clear();
		this.transfersList.addAll(transfersList);
		notifyDataSetChanged();
	}

	public void setClickItem(int position) {
		this.clickItem = position;
		notifyDataSetChanged();
	}
}
