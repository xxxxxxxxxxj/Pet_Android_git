package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.TimeListCodeBean.DataBean.SelectionBean.TimesBean;
import com.haotang.pet.util.Utils;

public class ContentTimeAdapter extends BaseAdapter {
	private Context context;
	private List<TimesBean> list;
	private int clickItemPosition = -1;
	private int isFull;

	private String timeOld;
	private int index;
	public ContentTimeAdapter(Activity context, List<TimesBean> list, int isFull) {
		this.context = context;
		this.list = list;
		this.isFull = isFull;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
					.inflate(context, R.layout.item_cotent_time, null);
			holder.rl_item_contenttime = (RelativeLayout) convertView
					.findViewById(R.id.rl_item_contenttime);
			holder.tv_item_contenttime = (TextView) convertView
					.findViewById(R.id.tv_item_contenttime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TimesBean timesBean = list.get(position);
		if (timesBean != null) {
			Utils.setText(holder.tv_item_contenttime, timesBean.getTime(), "",View.VISIBLE, View.INVISIBLE);
			if (isFull == 0) {// 当天可约
				List<Integer> workers = timesBean.getWorkers();
				if (workers != null && workers.size() > 0) {// 此时间格子可约
					holder.rl_item_contenttime.setEnabled(true);
					holder.rl_item_contenttime.setClickable(false);
					
					if (clickItemPosition == position) {// 选中
						holder.rl_item_contenttime.setBackgroundResource(R.drawable.time_select);
						holder.tv_item_contenttime.setTextColor(context.getResources().getColor(R.color.white));
					} else {// 未选中
						holder.rl_item_contenttime.setBackgroundResource(R.drawable.time_ky);
						holder.tv_item_contenttime.setTextColor(context.getResources().getColor(R.color.aE33A4A));
					}
					
				} else {// 此时间格子不可约
					holder.rl_item_contenttime.setEnabled(false);
					holder.rl_item_contenttime.setClickable(true);
					holder.rl_item_contenttime.setBackgroundResource(R.drawable.time_bky);
					holder.tv_item_contenttime.setTextColor(context.getResources().getColor(R.color.acccccc));
				}
			} else if (isFull == 1) {// 当天不可约
				holder.rl_item_contenttime.setEnabled(false);
				holder.rl_item_contenttime.setClickable(true);
				holder.rl_item_contenttime.setBackgroundResource(R.drawable.time_bky);
				holder.tv_item_contenttime.setTextColor(context.getResources().getColor(R.color.acccccc));
			}
		}
		
		try {
			if (!TextUtils.isEmpty(timeOld)) {//第一次进来选中
				if (timeOld.equals(timesBean.getTime())) {
					holder.rl_item_contenttime.setEnabled(true);
					holder.rl_item_contenttime.setClickable(false);
					holder.rl_item_contenttime.setBackgroundResource(R.drawable.time_ky);
					holder.tv_item_contenttime.setTextColor(context.getResources().getColor(R.color.aE33A4A));
					
					if (index==0) {
						holder.rl_item_contenttime.setBackgroundResource(R.drawable.time_select);
						holder.tv_item_contenttime.setTextColor(context.getResources().getColor(R.color.white));
					}
				}
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return convertView;
	}

	static class ViewHolder {
		public RelativeLayout rl_item_contenttime;
		public TextView tv_item_contenttime;
	}

	public void setClickItem(int position) {
		this.clickItemPosition = position;
		notifyDataSetChanged();
	}
	
	public void setTime(String timeOld,int index){
		this.timeOld=timeOld;
		this.index=index;
		notifyDataSetChanged();
	}
}
