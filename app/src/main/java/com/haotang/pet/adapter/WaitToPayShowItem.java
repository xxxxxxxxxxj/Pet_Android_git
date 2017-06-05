package com.haotang.pet.adapter;

import java.util.List;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 后期扩展需要-用gridview（具体条目产品无法确定）
 * @author Administrator
 *
 */
public class WaitToPayShowItem extends BaseAdapter{

	private Context context;
	private List<String> list;
	private int code = -1;
	
	public WaitToPayShowItem(Context context,List<String> list){
		this.context=context;
		this.list=list;
	}
	public WaitToPayShowItem(Context context,List<String> list,int code){
		this.context=context;
		this.list=list;
		this.code=code;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Utils.mLogError("==-->==-->fosterList1 "+list.size());
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.to_pay_show_item, null);
			mHolder.textView_item_show_to_pay = (TextView) convertView.findViewById(R.id.textView_item_show_to_pay);
			convertView.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		String [] strBaseShow = list.get(position).split(",");
		String NameBase = strBaseShow[0];
		String PriceBase =  strBaseShow[1];
		if (code!=-1) {
			mHolder.textView_item_show_to_pay.setText(Html.fromHtml("<font size=\"3\" color=\"#757575\">"+NameBase+"</font><font size=\"3\" color=\"#757575\">"+PriceBase+"</font>"));
		}else {
			mHolder.textView_item_show_to_pay.setText(Html.fromHtml("<font size=\"3\" color=\"#757575\">"+NameBase+"</font><font size=\"3\" color=\"#FF8C00\">"+PriceBase+"</font>"));
		}
		return convertView;
	}

	class ViewHolder{
		TextView textView_item_show_to_pay;
	}
}
