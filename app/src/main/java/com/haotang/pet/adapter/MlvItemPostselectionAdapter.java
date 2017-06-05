package com.haotang.pet.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.pet.PostSelectionDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.PostSelectionBean.PostsBean.CommentUsers;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ColorBQMMMessageText;
import com.haotang.pet.view.CommonAdapter;
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
public class MlvItemPostselectionAdapter<T> extends CommonAdapter<T> {
	private List<Object> emojis = new ArrayList<Object>();
	private int postId;

	public MlvItemPostselectionAdapter(Activity mContext, List<T> mDatas,
			int postId) {
		super(mContext, mDatas);
		this.postId = postId;
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
				R.layout.item_mlvitempostselectionadapter, position);
		ColorBQMMMessageText bqmt_item_mlvitempostselectionadapter_userdes = viewHolder
				.getView(R.id.bqmt_item_mlvitempostselectionadapter_userdes);
		TextView tv_item_mlvitempostselectionadapter_username = viewHolder
				.getView(R.id.tv_item_mlvitempostselectionadapter_username);
		LinearLayout ll_item_mlvitempostselectionadapter = viewHolder
				.getView(R.id.ll_item_mlvitempostselectionadapter);
		final CommentUsers commentUsers = (CommentUsers) mDatas.get(position);
		if (commentUsers != null) {
			try {
				int id = commentUsers.getId();
				int type = commentUsers.getContentType();
				String content = commentUsers.getContent();
				String userName = commentUsers.getUserName();
				if (type == 1) {// 小表情
					tv_item_mlvitempostselectionadapter_username
							.setVisibility(View.GONE);
				} else if (type == 2) {// 大表情
					if (userName != null && !TextUtils.isEmpty(userName)) {
						tv_item_mlvitempostselectionadapter_username
								.setVisibility(View.VISIBLE);
						Utils.setStringTextGone(
								tv_item_mlvitempostselectionadapter_username,
								userName + ": ");
					} else {
						tv_item_mlvitempostselectionadapter_username
								.setVisibility(View.GONE);
					}
				}
				if (content != null && !TextUtils.isEmpty(content)) {
					bqmt_item_mlvitempostselectionadapter_userdes
							.setVisibility(View.VISIBLE);
					JSONArray jsonArray = null;
					if (content.contains("［") && content.contains("］")
							&& content.contains("＂")) {
						content = content.replaceAll("［", "[")
								.replaceAll("］", "]").replaceAll("＂", "\"");
						if (type == 1 && userName != null
								&& !TextUtils.isEmpty(userName)) {
							content = Utils.insertStringInParticularPosition(
									content, userName + ": ", 3);
						}
						jsonArray = new JSONArray(content);
					} else if (content.contains("[") && content.contains("]")
							&& content.contains("\"")) {
						if (type == 1 && userName != null
								&& !TextUtils.isEmpty(userName)) {
							content = Utils.insertStringInParticularPosition(
									content, userName + ": ", 3);
						}
						jsonArray = new JSONArray(content);
					} else {
						emojis.clear();
						emojis.add(content);
						jsonArray = BQMMMessageHelper
								.getMixedMessageData(emojis);
					}
					if (type == 2) {// 大表情
						bqmt_item_mlvitempostselectionadapter_userdes
								.setColor(false);
						bqmt_item_mlvitempostselectionadapter_userdes
								.showMessage(id + "", BQMMMessageHelper
										.getMsgCodeString(jsonArray),
										BQMMMessageText.FACETYPE, jsonArray);
						bqmt_item_mlvitempostselectionadapter_userdes
								.getBackground().setAlpha(0);
					} else if (type == 1) {// 小表情或文字或图文混排
						if (userName != null && !TextUtils.isEmpty(userName)) {
							bqmt_item_mlvitempostselectionadapter_userdes
									.setColor(true);
							bqmt_item_mlvitempostselectionadapter_userdes
									.setNum(userName.length() + 1);
						} else {
							bqmt_item_mlvitempostselectionadapter_userdes
									.setColor(false);
						}
						bqmt_item_mlvitempostselectionadapter_userdes
								.showMessage(id + "", BQMMMessageHelper
										.getMsgCodeString(jsonArray),
										BQMMMessageText.EMOJITYPE, jsonArray);
						bqmt_item_mlvitempostselectionadapter_userdes
								.getBackground().setAlpha(255);
						bqmt_item_mlvitempostselectionadapter_userdes
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										mContext.startActivity(new Intent(
												mContext,
												PostSelectionDetailActivity.class)
												.putExtra("postId", postId));
									}
								});
					}
				} else {
					bqmt_item_mlvitempostselectionadapter_userdes
							.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		tv_item_mlvitempostselectionadapter_username
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mContext.startActivity(new Intent(mContext,
								PostSelectionDetailActivity.class).putExtra(
								"postId", postId));
					}
				});
		ll_item_mlvitempostselectionadapter
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mContext.startActivity(new Intent(mContext,
								PostSelectionDetailActivity.class).putExtra(
								"postId", postId));
					}
				});
		return viewHolder.getConvertView();
	}
}
