package com.haotang.pet.adapter;

import java.util.ArrayList;

import com.haotang.pet.R;
import com.haotang.pet.entity.Knowledge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class KnowledgeAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Knowledge> list;
	private LayoutInflater mInflater;
	
	public KnowledgeAdapter(Context context, ArrayList<Knowledge> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		Holder holder = null;
		if(convertView == null){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.knowledge_item, null);
			holder.ivHost = (ImageView) convertView.findViewById(R.id.iv_knowledgeitem_hot);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_knowledgeitem_content);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_knowledgeitem_title);
			
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.tvTitle.setText(list.get(position).title);
		holder.tvContent.setText(list.get(position).content);
		
		return convertView;
	}
	
	private class Holder{
		ImageView ivHost;
		TextView tvTitle;
		TextView tvContent;
		
	}

}
