package com.haotang.pet.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.entity.SortModel;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CircleImageView;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class SortAdapter extends BaseAdapter implements SectionIndexer{
	private static final String TAG="SortAdapter";
	private List<SortModel> list = null;
	private Context mContext;
	private int kind;
	
	public SortAdapter(Context mContext,int kind, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
		this.kind = kind;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<SortModel> list){
		this.list = list;
		notifyDataSetChanged();
	}
	
	public void setKind(int kind){
		this.kind=kind;
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position-1);
	}

	public long getItemId(int position) {
		return position-1;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.sort_adapter_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.imageView_animal_icon = (SelectableRoundedImageView) view.findViewById(R.id.imageView_animal_icon);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			if("@".equals(mContent.getSortLetters())){
				viewHolder.tvLetter.setText("热门宠物");
			}else{
				viewHolder.tvLetter.setText(mContent.getSortLetters());
			}
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(this.list.get(position).getName().replace("zang", "藏"));
		viewHolder.imageView_animal_icon.setTag(CommUtil.getServiceNobacklash()+list.get(position).getAvatarPath());
		if(kind == 1){
			viewHolder.imageView_animal_icon.setImageResource(R.drawable.dog_icon_unnew);
		}else{
			viewHolder.imageView_animal_icon.setImageResource(R.drawable.cat_icon_unnet);
		}
		if(list.get(position).getAvatarPath()!=null&&!"".equals(list.get(position).getAvatarPath())){
			ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+list.get(position).getAvatarPath(),viewHolder.imageView_animal_icon,0, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View view) {
					// TODO Auto-generated method stub
//					((ImageView)view).setImageResource(R.drawable.dog_icon_unnew);
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String path, View view, Bitmap bitmap) {
					// TODO Auto-generated method stub
					if(view!=null&&bitmap!=null){
						ImageView iv = (ImageView) view;
						String imagetag = (String) iv.getTag();
						if(path!=null&&path.equals(imagetag)){
							iv.setImageBitmap(bitmap);
						}
					}
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		return view;

	}
	


	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		SelectableRoundedImageView imageView_animal_icon;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}