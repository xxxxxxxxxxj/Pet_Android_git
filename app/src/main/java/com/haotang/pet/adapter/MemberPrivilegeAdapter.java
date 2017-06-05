package com.haotang.pet.adapter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.Privileges;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:MemberPrivilegeAdapter
 * </p>
 * <p>
 * Description:我的界面会员特权数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-1-16 下午6:30:08
 */
public class MemberPrivilegeAdapter<T> extends CommonAdapter<T> {
	public MemberPrivilegeAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
	}

	public MemberPrivilegeAdapter(Activity mContext, List<T> mDatas, int flag) {
		super(mContext, mDatas, flag);
	}

	@Override
	public void setData(List<T> mDatas) {
		super.setData(removeDuplicate(mDatas));
		notifyDataSetChanged();
	}

	private List<T> removeDuplicate(List<T> mDatas) {
		Set set = new LinkedHashSet<T>();
		set.addAll(mDatas);
		mDatas.clear();
		mDatas.addAll(set);
		return mDatas;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_memberprivilege, position);
		ImageView iv_item_memberprivilege = viewHolder
				.getView(R.id.iv_item_memberprivilege);
		TextView tv_item_memberprivilege = viewHolder
				.getView(R.id.tv_item_memberprivilege);
		Privileges privileges = (Privileges) mDatas.get(position);
		if (privileges != null) {
			int memberPrivilegeId = privileges.MemberPrivilegeId;
			if (memberPrivilegeId == 1024) {
				String privilegeName = privileges.privilegeName;
				if (privilegeName != null && !TextUtils.isEmpty(privilegeName)) {
					SpannableString ss = new SpannableString(privilegeName);
					// 用下划线标记文本
					ss.setSpan(new UnderlineSpan(), 0, ss.length(),
							Spanned.SPAN_INCLUSIVE_INCLUSIVE);
					ss.setSpan(new TextAppearanceSpan(mContext,
							R.style.foster_style_y), 0, ss.length(),
							Spanned.SPAN_INCLUSIVE_INCLUSIVE);
					tv_item_memberprivilege.setText(ss);
					tv_item_memberprivilege.setTextColor(mContext
							.getResources().getColor(R.color.aBB996C));
				}
				iv_item_memberprivilege.setVisibility(View.GONE);
			} else {
				String privilegeName = privileges.privilegeName;
				if (privilegeName != null && !TextUtils.isEmpty(privilegeName)) {
					SpannableString ss = new SpannableString(privilegeName);
					ss.setSpan(new TextAppearanceSpan(mContext,
							R.style.oneone_normal), 0, ss.length(),
							Spanned.SPAN_INCLUSIVE_INCLUSIVE);
					tv_item_memberprivilege.setText(ss);
					tv_item_memberprivilege.setTextColor(mContext
							.getResources().getColor(R.color.white));
				}
				iv_item_memberprivilege.setVisibility(View.VISIBLE);
				ImageLoaderUtil.loadImg(privileges.iconForMine,
						iv_item_memberprivilege, 0, null);
				tv_item_memberprivilege.setTextColor(mContext.getResources()
						.getColor(R.color.white));
			}
		}
		return viewHolder.getConvertView();
	}
}
