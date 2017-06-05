package com.haotang.pet.adapter;

import java.util.List;

import com.haotang.pet.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 取消原因adapter
 * @author Administrator
 *
 */
public class CancleAdapter extends BaseAdapter{

	private Context context;
	private String[] showReason;
	private List<Integer> pos;
	public CancleAdapter(Context context,String[] showReason,List<Integer> pos){
		this.context=context;
		this.showReason=showReason;
		this.pos=pos;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return showReason.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return showReason[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (convertView==null) {
			mHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.cancle_adapter_choose, null);
			mHolder.textView_item_cancle_title = (TextView) convertView.findViewById(R.id.textView_item_cancle_title);
			mHolder.imageView_item_cancle_state = (ImageView) convertView.findViewById(R.id.imageView_item_cancle_state);
			convertView.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		if (showReason[position].length()>15) {
			mHolder.textView_item_cancle_title.setText(showReason[position].substring(0, 15)+"...");
		}else {
			mHolder.textView_item_cancle_title.setText(showReason[position]);
		}
		for (int i = 0; i < pos.size(); i++) {
			if (position==pos.get(i)) {
				mHolder.imageView_item_cancle_state.setBackgroundResource(R.drawable.icon_pay_selected);
			}else {
				mHolder.imageView_item_cancle_state.setBackgroundResource(R.drawable.icon_pay_normal);	
			}
		}
		return convertView;
	}

	  class ViewHolder {
		TextView textView_item_cancle_title;
		ImageView imageView_item_cancle_state;
	}
}
