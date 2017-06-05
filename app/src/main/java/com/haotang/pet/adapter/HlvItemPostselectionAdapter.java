package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haotang.pet.R;
import com.haotang.pet.entity.PostSelectionBean.PostsBean.GiftUsers;
import com.haotang.pet.entity.PostSelectionBean.PostsBean.GiftUsers.UserMemberLevel;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:HlvItemPostselectionAdapter
 * </p>
 * <p>
 * Description:精选列表界面点赞列表适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-12 上午10:23:53
 */
public class HlvItemPostselectionAdapter<T> extends CommonAdapter<T> {

	public HlvItemPostselectionAdapter(Activity mContext, List<T> mDatas) {
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
				R.layout.item_hlvitempostselectionadapter, position);
		SelectableRoundedImageView sriv_item_hlvitempostselectionadapter = viewHolder
				.getView(R.id.sriv_item_hlvitempostselectionadapter);
		ImageView iv_item_hlvitempostselectionadapter_level = viewHolder
				.getView(R.id.iv_item_hlvitempostselectionadapter_level);
		final GiftUsers giftUsers = (GiftUsers) mDatas.get(position);
		if (giftUsers != null) {
			String avatar = giftUsers.getAvatar();
			UserMemberLevel userMemberLevel = giftUsers.getUserMemberLevel();
			if (userMemberLevel != null) {
				String memberIcon = userMemberLevel.getMemberIcon();
				if (memberIcon != null && !TextUtils.isEmpty(memberIcon)) {
					ImageLoaderUtil.loadImg(memberIcon,
							iv_item_hlvitempostselectionadapter_level,
							R.drawable.icon_self, null);
					iv_item_hlvitempostselectionadapter_level
							.setVisibility(View.VISIBLE);
				} else {
					iv_item_hlvitempostselectionadapter_level
							.setVisibility(View.GONE);
				}
			} else {
				iv_item_hlvitempostselectionadapter_level
						.setVisibility(View.GONE);
			}
			if (avatar != null && !TextUtils.isEmpty(avatar)) {
				ImageLoaderUtil.loadImg(avatar,
						sriv_item_hlvitempostselectionadapter,
						R.drawable.jingxuan_flower_icon, null);
			} else {
				sriv_item_hlvitempostselectionadapter
						.setImageResource(R.drawable.jingxuan_flower_icon);
			}
		}
		return viewHolder.getConvertView();
	}
}
