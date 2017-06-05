package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MemberLevelDes;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:MemberLevelDesAdapter
 * </p>
 * <p>
 * Description:会员主界面会员介绍数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-1-4 下午3:05:43
 */
public class MemberLevelDesAdapter<T> extends CommonAdapter<T> {

	public MemberLevelDesAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public void setData(List<T> mDatas) {
		super.setData(mDatas);
		notifyDataSetChanged();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_memberleveldesadapter, position);
		TextView tv_item_memberleveldes_no = viewHolder
				.getView(R.id.tv_item_memberleveldes_no);
		TextView tv_item_memberleveldes_title = viewHolder
				.getView(R.id.tv_item_memberleveldes_title);
		TextView tv_item_memberleveldes_des = viewHolder
				.getView(R.id.tv_item_memberleveldes_des);
		MemberLevelDes memberLevelDes = (MemberLevelDes) mDatas.get(position);
		if (memberLevelDes != null) {
			String no = memberLevelDes.getNo();
			String title = memberLevelDes.getTitle();
			String des = memberLevelDes.getDes();
			Utils.setText(tv_item_memberleveldes_no, no, "", View.VISIBLE,
					View.GONE);
			Utils.setText(tv_item_memberleveldes_title, title, "",
					View.VISIBLE, View.GONE);
			Utils.setText(tv_item_memberleveldes_des, des, "", View.VISIBLE,
					View.GONE);
		}
		return viewHolder.getConvertView();
	}
}
