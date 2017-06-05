package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.TimeListCodeBean.DataBean.SelectionBean;
import com.haotang.pet.util.Utils;

public class TopTimeDateAdapter extends BaseAdapter {
	private Activity context;
	private List<SelectionBean> calendar;
	private int clickItemPosition = -1;

	public TopTimeDateAdapter(Activity context, List<SelectionBean> calendar) {
		this.context = context;
		this.calendar = calendar;
	}

	@Override
	public int getCount() {
		return calendar.size();
	}

	@Override
	public Object getItem(int position) {
		return calendar.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_toptime_date,
					null);
			holder.ll_item_toptimedate = (LinearLayout) convertView
					.findViewById(R.id.ll_item_toptimedate);
			holder.tv_item_toptimedate_day = (TextView) convertView
					.findViewById(R.id.tv_item_toptimedate_day);
			holder.tv_item_toptimedate_date = (TextView) convertView
					.findViewById(R.id.tv_item_toptimedate_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SelectionBean selectionBean = calendar.get(position);
		if (selectionBean != null) {
			int isFull = selectionBean.getIsFull();
			Utils.setText(holder.tv_item_toptimedate_day,
					selectionBean.getDesc(), "", View.VISIBLE, View.INVISIBLE);
			Utils.setText(holder.tv_item_toptimedate_date,
					selectionBean.getDate(), "", View.VISIBLE, View.INVISIBLE);
			if (clickItemPosition == position) {// 选中
				holder.ll_item_toptimedate.setBackground(context.getResources()
						.getDrawable(R.drawable.date_appoint_select));
				holder.tv_item_toptimedate_day.setTextColor(context
						.getResources().getColor(R.color.white));
				holder.tv_item_toptimedate_date.setTextColor(context
						.getResources().getColor(R.color.white));
			} else {// 未选中
				holder.ll_item_toptimedate.setBackgroundResource(context
						.getResources().getColor(android.R.color.transparent));
				if (isFull == 0) {// 可约
					holder.tv_item_toptimedate_day.setTextColor(context
							.getResources().getColor(R.color.aE33A4A));
					holder.tv_item_toptimedate_date.setTextColor(context
							.getResources().getColor(R.color.aE33A4A));
				} else if (isFull == 1) {// 不可约
					holder.tv_item_toptimedate_day.setTextColor(context
							.getResources().getColor(R.color.acccccc));
					holder.tv_item_toptimedate_date.setTextColor(context
							.getResources().getColor(R.color.acccccc));
					holder.tv_item_toptimedate_date.setText("已约满");
				}
			}
		}
		return convertView;
	}

	static class ViewHolder {
		public LinearLayout ll_item_toptimedate;
		public TextView tv_item_toptimedate_day;
		public TextView tv_item_toptimedate_date;
	}

	public void setClickItem(int position) {
		this.clickItemPosition = position;
		notifyDataSetChanged();
	}
}
