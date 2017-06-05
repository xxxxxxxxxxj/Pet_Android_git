package com.haotang.pet.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haotang.pet.R;

@SuppressLint({ "UseSparseArrays", "NewApi" }) public class ServiceSearchUnChooseAdapter extends BaseAdapter{

	private Context context;
	private List<ArrayMap<String,String>> list = null;
	public int selectIndex =0;
	private String str;
	private ListView listview;
	private ArrayMap<Integer, Object> map;
	public ServiceSearchUnChooseAdapter(Context context,List<ArrayMap<String,String>> list,String str/*ListView listview*/){
		this.context=context;
		this.list=list;
		this.str=str;
		map = new ArrayMap<Integer, Object>();
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
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO 当前复用会出现bug，因数据不多，暂时采用非优化方式解决，后期调试
//		ViewHolder mHolder = null;
//		selectIndex = position;
//		if (view==null) {
//			mHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.service_search_all_unchoose, null);
			TextView textView_service_search_item_unchoose_name=(TextView) view.findViewById(R.id.textView_service_search_item_unchoose_name);
			TextView textView_service_search_item_unchoose_address=(TextView) view.findViewById(R.id.textView_service_search_item_unchoose_address);
			LinearLayout layout_service_search_item_unchoose=(LinearLayout) view.findViewById(R.id.layout_service_search_item_unchoose);
			ImageView imageView_add_service_show_item=(ImageView)view.findViewById(R.id.imageView_add_service_show_item);
			textView_service_search_item_unchoose_name.setText(list.get(position).get("name"));
			textView_service_search_item_unchoose_address.setText(list.get(position).get("address"));
			if (position==0) {
				textView_service_search_item_unchoose_name.setText("[当前]"+list.get(position).get("name"));
				textView_service_search_item_unchoose_name.setTextColor(Color.parseColor("#FF9942"));
				imageView_add_service_show_item.setBackgroundResource(R.drawable.cet_selectarea_add);
			}
//			view.setTag(mHolder);
//		}else {
//			mHolder = (ViewHolder) view.getTag();
//		}	
		
//		mHolder.textView_service_search_item_unchoose_name.setText(list.get(position).get("name"));
//		mHolder.textView_service_search_item_unchoose_address.setText(list.get(position).get("address"));
//		if (position==0&&str.equals(list.get(0).get("name"))) {
//			mHolder.textView_service_search_item_unchoose_name.setText("[当前]"+list.get(0).get("name"));
//			mHolder.textView_service_search_item_unchoose_name.setTextColor(Color.parseColor("#FF9942"));
//			mHolder.imageView_add_service_show_item.setBackgroundResource(R.drawable.add_serviceaddress_give_service);
//		}
		return view;
	}

	
	public class ViewHolder {
		public TextView textView_service_search_item_unchoose_name;
		public TextView textView_service_search_item_unchoose_address;
		public LinearLayout layout_service_search_item_unchoose;
		ImageView imageView_add_service_show_item;
	}
	
	
}
