package com.haotang.pet.adapter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.pet.PostUserInfoActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.UserListBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.UserNameAlertDialog;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:PostSelectionfragmentAdapter
 * </p>
 * <p>
 * Description:精选列表适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-12 上午10:23:28
 */
public class UserListAdapter<T> extends CommonAdapter<T> {
	private int position;
	public static final int NORMAL = 1;
	public static final int REFRESH = 2;

	public OnAdapterPlayListener onAdapterPlayListener = null;

	public interface OnAdapterPlayListener {
		public void OnAdapterPlay(int flag, int position);
	}

	public void setOnAdapterPlayListener(
			OnAdapterPlayListener onAdapterPlayListener) {
		this.onAdapterPlayListener = onAdapterPlayListener;
	}

	public UserListAdapter(Activity mContext, List<T> mDatas) {
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

	@SuppressWarnings("rawtypes")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_userlist, position);
		RelativeLayout rl_uerlist = viewHolder.getView(R.id.rl_uerlist);
		Button btn_item_uerlist_gz = viewHolder
				.getView(R.id.btn_item_uerlist_gz);
		SelectableRoundedImageView sriv_item_uerlist_userimg = viewHolder
				.getView(R.id.sriv_item_uerlist_userimg);
		ImageView iv_uerlist_level = viewHolder.getView(R.id.iv_uerlist_level);
		TextView tv_uerlist = viewHolder.getView(R.id.tv_uerlist);
		final UserListBean userListBean = (UserListBean) mDatas.get(position);
		String avatar = userListBean.avatar;
		int duty = userListBean.duty;
		int isFollow = userListBean.isFollow;
		int isMyself = userListBean.isMyself;
		String user_name = userListBean.user_name;
		String memberIcon = userListBean.memberIcon;
		if (avatar != null && !TextUtils.isEmpty(avatar)) {
			ImageLoaderUtil.loadImg(avatar, sriv_item_uerlist_userimg,
					R.drawable.icon_self, null);
		} else {
			sriv_item_uerlist_userimg.setImageResource(R.drawable.icon_self);
		}
		Utils.setStringTextGone(tv_uerlist, user_name);
		if (isMyself == 1) {
			btn_item_uerlist_gz.setVisibility(View.GONE);
		} else {
			btn_item_uerlist_gz.setVisibility(View.VISIBLE);
		}
		if (duty == 1 || duty == 2) {
			iv_uerlist_level
			.setBackgroundResource(R.drawable.iv_item_postselectionfragment_level);
			iv_uerlist_level.setVisibility(View.VISIBLE);
		} else {
			if (memberIcon != null && !TextUtils.isEmpty(memberIcon)) {
				ImageLoaderUtil.loadImg(memberIcon, iv_uerlist_level,
						R.drawable.icon_self, null);
				iv_uerlist_level.setVisibility(View.VISIBLE);
			} else {
				iv_uerlist_level.setVisibility(View.GONE);
			}
		}
		if (isFollow == 2) {// 未关注
			btn_item_uerlist_gz.setBackground(mContext.getResources()
					.getDrawable(
							R.drawable.btn_item_postselectionfragment_notgz));
		} else if (isFollow == 1) {// 已关注
			btn_item_uerlist_gz.setBackground(mContext.getResources()
					.getDrawable(R.drawable.btn_item_postselectionfragment_gz));
		}
		rl_uerlist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				operation(1, position, 0);
			}
		});
		btn_item_uerlist_gz.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				operation(2, position, 0);
			}
		});
		return viewHolder.getConvertView();
	}

	protected void operation(int i, int position, int location) {
		this.position = position;
		UserListBean userListBean = (UserListBean) mDatas.get(position);
		if (userListBean != null) {
			int id = userListBean.id;
			switch (i) {
			case 1:
				goToNext(PostUserInfoActivity.class, id, 0, "");
				break;
			case 2:
				if (spUtil.getString("cellphone", "") != null
						&& !TextUtils
								.isEmpty(spUtil.getString("cellphone", ""))) {
					String username = spUtil.getString("username", "");
					if (username != null && !TextUtils.isEmpty(username)) {
						CommUtil.followUser(spUtil.getString("cellphone", ""),
								mContext, id, followUserHandler);
					} else {
						tqDialog();
					}
				} else {
					onAdapterPlayListener.OnAdapterPlay(NORMAL, position);
				}
				break;
			default:
				break;
			}
		}
	}

	private void tqDialog() {
		new UserNameAlertDialog(mContext).builder().setTitle("没昵称我  \"蓝瘦\"")
				.setTextViewHint("请填写昵称").setCloseButton(new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).setComplaintsButton("保	 存", new OnClickListener() {
					@Override
					public void onClick(View v) {
						CommUtil.updateUser(spUtil.getString("cellphone", "0"),
								Global.getIMEI(mContext), mContext,
								spUtil.getInt("userid", 0),
								UserNameAlertDialog.getUserName(), null,
								updateUser);
					}
				}).show();
	}

	private AsyncHttpResponseHandler updateUser = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				Utils.mLogError("==-->updateUser" + new String(responseBody));
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					Toast.makeText(mContext, "创建成功", Toast.LENGTH_SHORT).show();
					spUtil.saveString("username",
							UserNameAlertDialog.getUserName());
					onAdapterPlayListener.OnAdapterPlay(REFRESH, position);
				} else {
					String msg = jsonObject.getString("msg");
					Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
	};

	private AsyncHttpResponseHandler followUserHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("关注：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						if (jData.has("isFollow") && !jData.isNull("isFollow")) {
							int isFollow = jData.getInt("isFollow");
							if (isFollow == 2) {// 未关注
								ToastUtil
										.showToastShortBottom(mContext, "取消关注");
							} else if (isFollow == 1) {// 已关注
								ToastUtil
										.showToastShortBottom(mContext, "关注成功");
							}
							refreshGZ(position, isFollow);
						}
					}
				}
			} catch (JSONException e) {
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
	};

	public void refreshGZ(int position, int isFollow) {
		if (mDatas != null && mDatas.size() > 0) {
			UserListBean userListBean = (UserListBean) mDatas.get(position);
			if (userListBean != null) {
				userListBean.isFollow = isFollow;
				notifyDataSetChanged();
			}
		}
	}

	private void goToNext(Class clazz, int id, int previous, String flag) {
		Intent intent = new Intent(mContext, clazz);
		intent.putExtra("userId", id);
		intent.putExtra("flag", flag);
		intent.putExtra("previous", previous);
		mContext.startActivity(intent);
	}
}
