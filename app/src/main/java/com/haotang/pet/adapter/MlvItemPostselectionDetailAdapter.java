package com.haotang.pet.adapter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.PostUserInfoActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.PostSelectionDetailBean.CommentsBean;
import com.haotang.pet.entity.PostSelectionDetailBean.CommentsBean.UserBean;
import com.haotang.pet.entity.PostSelectionDetailBean.CommentsBean.UserBean.UserMemberLevel;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.ViewHolder;
import com.melink.bqmmsdk.sdk.BQMMMessageHelper;
import com.melink.bqmmsdk.widget.BQMMMessageText;

/**
 * <p>
 * Title:MlvItemPostselectionAdapter
 * </p>
 * <p>
 * Description:精选列表界面评论列表适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-12 上午10:24:42
 */
public class MlvItemPostselectionDetailAdapter<T> extends CommonAdapter<T> {
	private List<Object> emojis = new ArrayList<Object>();
	private int flag;

	public MlvItemPostselectionDetailAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
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

	public void setFlag(int flag) {
		this.flag = flag;
		notifyDataSetChanged();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_mlvitempostselectiondetailadapter, position);
		BQMMMessageText bqmt_item_mlvitempostselectionadapter_userdes = viewHolder
				.getView(R.id.bqmt_item_mlvitempostselectionadapter_userdes);
		SelectableRoundedImageView sriv_item_mlvitempostselectionadapter = viewHolder
				.getView(R.id.sriv_item_mlvitempostselectionadapter);
		TextView tv_item_mlvitempostselectionadapter_username = viewHolder
				.getView(R.id.tv_item_mlvitempostselectionadapter_username);
		TextView tv_item_mlvitempostselectionadapter_createtime = viewHolder
				.getView(R.id.tv_item_mlvitempostselectionadapter_createtime);
		RelativeLayout rl_item_mlvitempostselectionadapter = viewHolder
				.getView(R.id.rl_item_mlvitempostselectionadapter);
		ImageView iv_item_mlvitempostselectionadapter_level = viewHolder
				.getView(R.id.iv_item_mlvitempostselectionadapter_level);
		if (flag == 1) {
			rl_item_mlvitempostselectionadapter.setVisibility(View.INVISIBLE);
		} else if (flag == 0) {
			rl_item_mlvitempostselectionadapter.setVisibility(View.VISIBLE);
			final CommentsBean commentsBean = (CommentsBean) mDatas
					.get(position);
			if (commentsBean != null) {
				int id = commentsBean.getPostCommentId();
				int type = commentsBean.getContentType();
				String content = commentsBean.getContent();
				if (content != null && !TextUtils.isEmpty(content)) {
					JSONArray jsonArray = null;
					if (content.contains("［") && content.contains("］")
							&& content.contains("＂")) {
						content = content.replaceAll("［", "[")
								.replaceAll("］", "]").replaceAll("＂", "\"");
						try {
							jsonArray = new JSONArray(content);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else if (content.contains("[") && content.contains("]")
							&& content.contains("\"")) {
						try {
							jsonArray = new JSONArray(content);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						emojis.clear();
						emojis.add(content);
						jsonArray = BQMMMessageHelper
								.getMixedMessageData(emojis);
					}
					bqmt_item_mlvitempostselectionadapter_userdes
							.setVisibility(View.VISIBLE);
					if (type == 2) {// 大表情
						bqmt_item_mlvitempostselectionadapter_userdes
								.showMessage(id + "", BQMMMessageHelper
										.getMsgCodeString(jsonArray),
										BQMMMessageText.FACETYPE, jsonArray);
						bqmt_item_mlvitempostselectionadapter_userdes
								.getBackground().setAlpha(0);
					} else if (type == 1) {// 小表情或文字或图文混排
						bqmt_item_mlvitempostselectionadapter_userdes
								.showMessage(id + "", BQMMMessageHelper
										.getMsgCodeString(jsonArray),
										BQMMMessageText.EMOJITYPE, jsonArray);
						bqmt_item_mlvitempostselectionadapter_userdes
								.getBackground().setAlpha(255);
					}
				} else {
					bqmt_item_mlvitempostselectionadapter_userdes
							.setVisibility(View.INVISIBLE);
				}
				Utils.setStringText(
						tv_item_mlvitempostselectionadapter_createtime,
						commentsBean.getCreated());
				UserBean user = commentsBean.getUser();
				if (user != null) {
					String userName = user.getUserName();
					String cellPhone = user.getCellPhone();
					UserMemberLevel userMemberLevel = user.getUserMemberLevel();
					if (userMemberLevel != null) {
						String memberIcon = userMemberLevel.getMemberIcon();
						if (memberIcon != null
								&& !TextUtils.isEmpty(memberIcon)) {
							ImageLoaderUtil.loadImg(memberIcon,
									iv_item_mlvitempostselectionadapter_level,
									R.drawable.icon_self, null);
							iv_item_mlvitempostselectionadapter_level
									.setVisibility(View.VISIBLE);
						} else {
							iv_item_mlvitempostselectionadapter_level
									.setVisibility(View.GONE);
						}
					} else {
						iv_item_mlvitempostselectionadapter_level
								.setVisibility(View.GONE);
					}
					if (userName != null && !TextUtils.isEmpty(userName)) {
						Utils.setStringText(
								tv_item_mlvitempostselectionadapter_username,
								userName);
					} else if (cellPhone != null
							&& !TextUtils.isEmpty(cellPhone)) {
						Utils.setStringText(
								tv_item_mlvitempostselectionadapter_username,
								cellPhone);
					} else {
						tv_item_mlvitempostselectionadapter_username
								.setVisibility(View.INVISIBLE);
					}
					String avatar = user.getAvatar();
					if (avatar != null && !TextUtils.isEmpty(avatar)) {
						ImageLoaderUtil.loadImg(avatar,
								sriv_item_mlvitempostselectionadapter,
								R.drawable.icon_self, null);
					} else {
						sriv_item_mlvitempostselectionadapter
								.setImageResource(R.drawable.icon_self);
					}
				}
			}
		}
		rl_item_mlvitempostselectionadapter
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CommentsBean commentsBean = (CommentsBean) mDatas
								.get(position);
						if (commentsBean != null) {
							UserBean user = commentsBean.getUser();
							if (user != null) {
								int userId = user.getUserId();
								mContext.startActivity(new Intent(mContext,
										PostUserInfoActivity.class).putExtra(
										"userId", userId));
							}
						}
					}
				});
		return viewHolder.getConvertView();
	}
}
