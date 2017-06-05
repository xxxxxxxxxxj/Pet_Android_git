package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.AllPrivilege;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:MgvMemberServiceAdapter
 * </p>
 * <p>
 * Description:会员主界面会员特权数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-12-29 下午1:43:54
 */
public class MgvMemberServiceAdapter<T> extends CommonAdapter<T> {
	private boolean isLight;

	public MgvMemberServiceAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	public void setDataLevel(boolean isLight, List<T> mDatas) {
		this.isLight = isLight;
		this.mDatas = mDatas;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_mgvmember_service, position);
		ImageView iv_item_mgvmember = viewHolder
				.getView(R.id.iv_item_mgvmember);
		TextView tv_item_mgvmember = viewHolder.getView(R.id.tv_item_mgvmember);
		AllPrivilege allPrivilege = (AllPrivilege) mDatas.get(position);
		if (allPrivilege != null) {
			String privilegeIcon = allPrivilege.privilegeIcon;
			String privilegeIconHui = allPrivilege.privilegeIconHui;
			String privilegeName = allPrivilege.privilegeName;
			Utils.setText(tv_item_mgvmember, privilegeName, "", View.VISIBLE,
					View.GONE);
			if (isLight) {
				ImageLoaderUtil.loadImgLocal(privilegeIcon, iv_item_mgvmember,
						R.drawable.icon_production_default, null);
				tv_item_mgvmember.setTextColor(mContext.getResources()
						.getColor(R.color.a333333));
			} else {
				ImageLoaderUtil.loadImgLocal(privilegeIconHui,
						iv_item_mgvmember, R.drawable.icon_production_default,
						null);
				tv_item_mgvmember.setTextColor(mContext.getResources()
						.getColor(R.color.found_description_color));
			}
		}
		return viewHolder.getConvertView();
	}
}
