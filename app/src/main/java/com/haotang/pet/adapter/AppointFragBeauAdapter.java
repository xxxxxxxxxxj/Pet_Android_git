package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CircleImageView;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:AppointFragBeauAdapter
 * </p>
 * <p>
 * Description:选择时间界面底部美容师数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-4-10 上午9:46:19
 */
public class AppointFragBeauAdapter<T> extends CommonAdapter<T> {

	public AppointFragBeauAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	@Override
	public void setData(List<T> mDatas) {
		this.flag = -1;
		super.setData(mDatas);
		notifyDataSetChanged();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.appointfrag_beauticianlist_item, position);
		CircleImageView civ_appointment_beauticianlist_item_img = viewHolder
				.getView(R.id.civ_appointment_beauticianlist_item_img);
		ImageView iv_appointment_beauticianlist_item = viewHolder
				.getView(R.id.iv_appointment_beauticianlist_item);
		TextView tv_appointment_beauticianlist_item_name = viewHolder
				.getView(R.id.tv_appointment_beauticianlist_item_name);
		Beautician beautician = (Beautician) mDatas.get(position);
		if (beautician != null) {
			ImageLoaderUtil.loadImg(beautician.image,
					civ_appointment_beauticianlist_item_img,
					R.drawable.icon_production_default, null);
			Utils.setText(tv_appointment_beauticianlist_item_name,
					beautician.name, "", View.VISIBLE, View.INVISIBLE);
			if (position == flag) {
				tv_appointment_beauticianlist_item_name.setTextColor(mContext
						.getResources().getColor(R.color.aE33A4A));
				iv_appointment_beauticianlist_item
						.setImageResource(R.drawable.beautician_select);
				civ_appointment_beauticianlist_item_img.mBorderColor = Color
						.parseColor("#E33A4A");
				civ_appointment_beauticianlist_item_img.invalidate();
			} else {
				iv_appointment_beauticianlist_item
						.setImageResource(R.drawable.beautician_disable);
				tv_appointment_beauticianlist_item_name.setTextColor(mContext
						.getResources().getColor(R.color.a333333));
				civ_appointment_beauticianlist_item_img.mBorderColor = Color
						.parseColor("#bbbbbb");
				civ_appointment_beauticianlist_item_img.invalidate();
			}
		}
		return viewHolder.getConvertView();
	}

	public void setClick(int position) {
		this.flag = position;
		notifyDataSetChanged();
	}
}
