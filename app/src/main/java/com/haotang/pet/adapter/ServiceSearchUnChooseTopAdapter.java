package com.haotang.pet.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haotang.pet.R;

@SuppressLint("NewApi") public class ServiceSearchUnChooseTopAdapter extends BaseAdapter{

	private Context context;
	private List<ArrayMap<String,String>> list = null;
	public int selectIndex =0;
	private String str;
	private ListView listview;
	public ServiceSearchUnChooseTopAdapter(Context context,List<ArrayMap<String,String>> list/*,String str,ListView listview*/){
		this.context=context;
		this.list=list;
//		this.str=str;
//		this.listview=listview;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		selectIndex = arg0;
		if (view==null) {
			mHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.service_search_all_unchoose, null);
			mHolder.textView_service_search_item_unchoose_address=(TextView) view.findViewById(R.id.textView_service_search_item_unchoose_address);
			mHolder.textView_service_search_item_unchoose_name=(TextView) view.findViewById(R.id.textView_service_search_item_unchoose_name);
			mHolder.layout_service_search_item_unchoose=(LinearLayout) view.findViewById(R.id.layout_service_search_item_unchoose);
			view.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) view.getTag();
		}
			mHolder.textView_service_search_item_unchoose_name.setText(list.get(arg0).get("name"));
			mHolder.textView_service_search_item_unchoose_address.setText(list.get(arg0).get("address"));
		return view;
	}

	public class ViewHolder {
		public TextView textView_service_search_item_unchoose_name;
		public TextView textView_service_search_item_unchoose_address;
		public LinearLayout layout_service_search_item_unchoose;
	}
	
	
}
