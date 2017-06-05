package com.haotang.pet.adapter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.pet.PostSelectionDetailActivity;
import com.haotang.pet.PostUserInfoActivity;
import com.haotang.pet.R;
import com.haotang.pet.UserListActivity;
import com.haotang.pet.entity.PostSelectionBean.PostsBean;
import com.haotang.pet.entity.PostSelectionBean.PostsBean.CommentUsers;
import com.haotang.pet.entity.PostSelectionBean.PostsBean.GiftUsers;
import com.haotang.pet.entity.PostSelectionBean.PostsBean.UserMemberLevel;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengShareUtils;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.wxpay.Util_WX;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.HorizontalListView;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.PeriscopeLayout;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.SurfaceVideoView;
import com.haotang.pet.view.UserNameAlertDialog;
import com.haotang.pet.view.ViewHolder;
import com.melink.bqmmsdk.sdk.BQMMMessageHelper;
import com.melink.bqmmsdk.widget.BQMMMessageText;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.OauthHelper;
import com.yixia.weibo.sdk.util.DeviceUtils;

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
@SuppressLint("NewApi")
public class PostSelectionfragmentAdapter<T> extends CommonAdapter<T> {
	private SharedPreferenceUtil spUtil;
	private Button btn_item_postselectionfragment_gz;
	private int FLAG;
	private int FOLLOW = 1;
	private int FLOWER = 2;
	private int SHIT = 3;
	private int DELETEPOST = 4;
	private RelativeLayout rl_item_postselectionfragment_num;
	private Button btn_item_postselectionfragment_num;
	private PeriscopeLayout circle_image_flower_other;
	private PeriscopeLayout circle_image_feces_other;
	private ImageButton ib_item_postselectionfragment_sh;
	private ImageButton ib_item_postselectionfragment_bb;
	private PopupWindow pWin;
	protected String shareurl;
	private IWXAPI api;
	private UmengShareUtils umengShareUtils;
	protected Bitmap bmp;
	private String sharetitle = "宠物家";
	private String sharetxt = "宠物家";
	private String img;
	private int id;
	private String cellphone;
	private int video = 2;
	private int picOrVideo;
	private int pic = 1;
	private int userId;
	public static final int NORMAL = 1;
	public static final int PLAYVIDEO = 2;
	public static final int REFRESH = 3;
	public static final int REFRESHDELETEPOST = 4;
	public OnGoToLoginListener onGoToLoginListener = null;
	private List<Object> emojis = new ArrayList<Object>();
	private SurfaceVideoView sfvv_item_postselectionfragment;
	private ImageView iv_item_postselectionfragment_play_status;
	private ImageView iv_item_postselectionfragment_videoloading;
	private ImageView iv_item_postselectionfragment_img;
	private ListView listView;
	private CheckBox cb_item_postselectionfragment_voice;
	private int position;
	private HorizontalListView hlv_item_postselectionfragment;
	private AudioManager mAudioManager;
	private TextView tv_item_postselectionfragment_delete;

	public interface OnGoToLoginListener {
		public void OnGoToLogin(int flag, int position, SurfaceVideoView view1,
				ImageView view2, ImageView view3, ImageView view4,
				CheckBox cb_item_postselectionfragment_voice);
	}

	public void setOnGoToLoginListener(OnGoToLoginListener onGoToLoginListener) {
		this.onGoToLoginListener = onGoToLoginListener;
	}

	public PostSelectionfragmentAdapter(Activity mContext, List<T> mDatas,
			ListView listView, AudioManager mAudioManager) {
		super(mContext, mDatas);
		this.listView = listView;
		this.mAudioManager = mAudioManager;
		spUtil = SharedPreferenceUtil.getInstance(mContext);
		api = WXAPIFactory.createWXAPI(mContext, Global.APP_ID);
		cellphone = spUtil.getString("cellphone", "");
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
				R.layout.item_postselectionfragment, position);
		TextView tv_item_postselectionfragment_username = viewHolder
				.getView(R.id.tv_item_postselectionfragment_username);
		TextView tv_item_postselectionfragment_fbtime = viewHolder
				.getView(R.id.tv_item_postselectionfragment_fbtime);
		TextView tv_item_postselectionfragment_ckpl = viewHolder
				.getView(R.id.tv_item_postselectionfragment_ckpl);
		LinearLayout ll_item_postselectionfragment = viewHolder
				.getView(R.id.ll_item_postselectionfragment);
		SelectableRoundedImageView sriv_item_postselectionfragment_userimg = viewHolder
				.getView(R.id.sriv_item_postselectionfragment_userimg);
		ImageView iv_item_postselectionfragment_level = viewHolder
				.getView(R.id.iv_item_postselectionfragment_level);
		btn_item_postselectionfragment_gz = viewHolder
				.getView(R.id.btn_item_postselectionfragment_gz);
		iv_item_postselectionfragment_img = viewHolder
				.getView(R.id.iv_item_postselectionfragment_img);
		hlv_item_postselectionfragment = viewHolder
				.getView(R.id.hlv_item_postselectionfragment);
		btn_item_postselectionfragment_num = viewHolder
				.getView(R.id.btn_item_postselectionfragment_num);
		ib_item_postselectionfragment_sh = viewHolder
				.getView(R.id.ib_item_postselectionfragment_sh);
		ib_item_postselectionfragment_bb = viewHolder
				.getView(R.id.ib_item_postselectionfragment_bb);
		ImageButton ib_item_postselectionfragment_pl = viewHolder
				.getView(R.id.ib_item_postselectionfragment_pl);
		ImageButton ib_item_postselectionfragment_fx = viewHolder
				.getView(R.id.ib_item_postselectionfragment_fx);
		MListview mlv_item_postselectionfragment_pl = viewHolder
				.getView(R.id.mlv_item_postselectionfragment_pl);
		BQMMMessageText bqmt_item_postselectionfragment_title = viewHolder
				.getView(R.id.bqmt_item_postselectionfragment_title);
		View vw_item_postselectionfragment = viewHolder
				.getView(R.id.vw_item_postselectionfragment);
		TextView tv_item_postselectionfragment_qbpl = viewHolder
				.getView(R.id.tv_item_postselectionfragment_qbpl);
		RelativeLayout rl_item_postselectionfragment_picorvideo = viewHolder
				.getView(R.id.rl_item_postselectionfragment_picorvideo);
		sfvv_item_postselectionfragment = viewHolder
				.getView(R.id.sfvv_item_postselectionfragment);
		sfvv_item_postselectionfragment.setTag(position);
		iv_item_postselectionfragment_play_status = viewHolder
				.getView(R.id.iv_item_postselectionfragment_play_status);
		iv_item_postselectionfragment_videoloading = viewHolder
				.getView(R.id.iv_item_postselectionfragment_videoloading);
		RelativeLayout rl_item_postselectionfragment_pl = viewHolder
				.getView(R.id.rl_item_postselectionfragment_pl);
		rl_item_postselectionfragment_num = viewHolder
				.getView(R.id.rl_item_postselectionfragment_num);
		circle_image_flower_other = viewHolder
				.getView(R.id.circle_image_flower_other);
		circle_image_feces_other = viewHolder
				.getView(R.id.circle_image_feces_other);
		cb_item_postselectionfragment_voice = viewHolder
				.getView(R.id.cb_item_postselectionfragment_voice);
		TextView tv_item_postselectionfragment_delete = viewHolder
				.getView(R.id.tv_item_postselectionfragment_delete);
		iv_item_postselectionfragment_play_status.bringToFront();
		vw_item_postselectionfragment.setVisibility(View.GONE);
		tv_item_postselectionfragment_qbpl.setVisibility(View.GONE);
		RelativeLayout.LayoutParams mPreviewParams = (RelativeLayout.LayoutParams) rl_item_postselectionfragment_picorvideo
				.getLayoutParams();
		mPreviewParams.width = DeviceUtils.getScreenWidth(mContext);
		mPreviewParams.height = DeviceUtils.getScreenWidth(mContext) * 69 / 75;
		final PostsBean postsBean = (PostsBean) mDatas.get(position);
		if (postsBean != null) {
			int id2 = postsBean.getId();
			String avatar = postsBean.getAvatar();
			List<String> coverImgs = postsBean.getCoverImgs();
			List<String> imgs = postsBean.getImgs();
			List<String> videos = postsBean.getVideos();
			int isBianBian = postsBean.getIsBianBian();
			int isFlower = postsBean.getIsFlower();
			String content = postsBean.getContent();
			int flowerAmount = postsBean.getFlowerAmount();
			int duty = postsBean.getDuty();
			int isFollowed = postsBean.getIsFollow();
			List<GiftUsers> giftUsers = postsBean.getGiftUsers();
			List<CommentUsers> commentUsers = postsBean.getCommentUsers();
			String userName = postsBean.getUserName();
			String created = postsBean.getCreated();
			int commentAmount = postsBean.getCommentAmount();
			int isMyself = postsBean.getIsMyself();
			boolean voice = postsBean.isVoice();
			UserMemberLevel userMemberLevel = postsBean.getUserMemberLevel();
			Utils.setStringText(tv_item_postselectionfragment_username,
					userName);
			Utils.setStringText(tv_item_postselectionfragment_fbtime, created);
			if (commentAmount <= 3) {
				tv_item_postselectionfragment_ckpl.setVisibility(View.GONE);
			} else {
				tv_item_postselectionfragment_ckpl.setVisibility(View.VISIBLE);
				Utils.setStringTextGone(tv_item_postselectionfragment_ckpl,
						"查看所有" + commentAmount + "条评论");
			}
			if (isMyself == 1) {
				tv_item_postselectionfragment_delete
						.setVisibility(View.VISIBLE);
				btn_item_postselectionfragment_gz.setVisibility(View.GONE);
			} else {
				tv_item_postselectionfragment_delete.setVisibility(View.GONE);
				btn_item_postselectionfragment_gz.setVisibility(View.VISIBLE);
			}
			if (content != null && !TextUtils.isEmpty(content)) {
				JSONArray jsonArray = null;
				if (content.contains("［") && content.contains("］")
						&& content.contains("＂")) {
					content = content.replaceAll("［", "[").replaceAll("］", "]")
							.replaceAll("＂", "\"");
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
					jsonArray = BQMMMessageHelper.getMixedMessageData(emojis);
				}
				bqmt_item_postselectionfragment_title
						.setVisibility(View.VISIBLE);
				// 小表情或文字或图文混排
				bqmt_item_postselectionfragment_title.showMessage(id2 + "",
						BQMMMessageHelper.getMsgCodeString(jsonArray),
						BQMMMessageText.EMOJITYPE, jsonArray);
				bqmt_item_postselectionfragment_title.getBackground().setAlpha(
						255);
			} else {
				bqmt_item_postselectionfragment_title.setVisibility(View.GONE);
			}
			if (avatar != null && !TextUtils.isEmpty(avatar)) {
				ImageLoaderUtil.loadImg(avatar,
						sriv_item_postselectionfragment_userimg,
						R.drawable.icon_self, null);
			} else {
				sriv_item_postselectionfragment_userimg
						.setImageResource(R.drawable.icon_self);
			}
			if (imgs != null && imgs.size() > 0) {
				cb_item_postselectionfragment_voice.setVisibility(View.GONE);
				iv_item_postselectionfragment_videoloading
						.setVisibility(View.GONE);
				rl_item_postselectionfragment_picorvideo
						.setVisibility(View.VISIBLE);
				String imgUrl = imgs.get(0);
				if (imgUrl != null && !TextUtils.isEmpty(imgUrl)) {
					ImageLoaderUtil.loadImgLocal(imgUrl,
							iv_item_postselectionfragment_img,
							R.drawable.icon_production_default, null);
				} else {
					iv_item_postselectionfragment_img
							.setImageResource(R.drawable.icon_production_default);
				}
				sfvv_item_postselectionfragment.setVisibility(View.GONE);
				iv_item_postselectionfragment_play_status
						.setVisibility(View.GONE);
				iv_item_postselectionfragment_img.setVisibility(View.VISIBLE);
			} else if (videos != null && videos.size() > 0) {
				cb_item_postselectionfragment_voice.setVisibility(View.VISIBLE);
				cb_item_postselectionfragment_voice.setChecked(voice);
				iv_item_postselectionfragment_videoloading
						.setVisibility(View.GONE);
				rl_item_postselectionfragment_picorvideo
						.setVisibility(View.VISIBLE);
				initPlayer(iv_item_postselectionfragment_img,
						sfvv_item_postselectionfragment,
						iv_item_postselectionfragment_play_status, coverImgs);
			} else {
				rl_item_postselectionfragment_picorvideo
						.setVisibility(View.INVISIBLE);
			}
			if (flowerAmount > 99) {
				btn_item_postselectionfragment_num.setVisibility(View.VISIBLE);
				btn_item_postselectionfragment_num.setText("99+");
			} else {
				if (flowerAmount > 10) {
					btn_item_postselectionfragment_num
							.setVisibility(View.VISIBLE);
					btn_item_postselectionfragment_num.setText(flowerAmount
							+ "");
				} else {
					btn_item_postselectionfragment_num.setVisibility(View.GONE);
				}
			}
			if (duty == 1 || duty == 2) {
				iv_item_postselectionfragment_level
						.setBackgroundResource(R.drawable.iv_item_postselectionfragment_level);
				iv_item_postselectionfragment_level.setVisibility(View.VISIBLE);
			} else {
				if (userMemberLevel != null) {
					String memberIcon = userMemberLevel.getMemberIcon();
					if (memberIcon != null && !TextUtils.isEmpty(memberIcon)) {
						ImageLoaderUtil.loadImg(memberIcon,
								iv_item_postselectionfragment_level,
								R.drawable.icon_self, null);
						iv_item_postselectionfragment_level
								.setVisibility(View.VISIBLE);
					} else {
						iv_item_postselectionfragment_level
								.setVisibility(View.GONE);
					}
				} else {
					iv_item_postselectionfragment_level
							.setVisibility(View.GONE);
				}
			}
			if (isFollowed == 2) {// 未关注
				btn_item_postselectionfragment_gz
						.setBackground(mContext
								.getResources()
						  		.getDrawable(
										R.drawable.btn_item_postselectionfragment_notgz));
			} else if (isFollowed == 1) {// 已关注
				btn_item_postselectionfragment_gz.setBackground(mContext
						.getResources().getDrawable(
								R.drawable.btn_item_postselectionfragment_gz));
			}
			if (giftUsers != null && giftUsers.size() > 0) {
				rl_item_postselectionfragment_num.setVisibility(View.VISIBLE);
				hlv_item_postselectionfragment
						.setAdapter(new HlvItemPostselectionAdapter<GiftUsers>(
								mContext, giftUsers));
			} else {
				rl_item_postselectionfragment_num.setVisibility(View.GONE);
			}
			if (commentUsers != null && commentUsers.size() > 0) {
				rl_item_postselectionfragment_pl.setVisibility(View.VISIBLE);
				mlv_item_postselectionfragment_pl
						.setAdapter(new MlvItemPostselectionAdapter<CommentUsers>(
								mContext, commentUsers, id2));
			} else {
				rl_item_postselectionfragment_pl.setVisibility(View.GONE);
			}
			if (isBianBian == 1) {
				ib_item_postselectionfragment_bb.setEnabled(false);
				ib_item_postselectionfragment_bb
						.setBackground(mContext
								.getResources()
								.getDrawable(
										R.drawable.ib_item_postselectionfragment_bb_select));
			} else {
				ib_item_postselectionfragment_bb.setEnabled(true);
				ib_item_postselectionfragment_bb.setBackground(mContext
						.getResources().getDrawable(
								R.drawable.ib_item_postselectionfragment_bb));
			}
			if (isFlower == 1) {
				ib_item_postselectionfragment_sh.setEnabled(false);
				ib_item_postselectionfragment_sh
						.setBackground(mContext
								.getResources()
								.getDrawable(
										R.drawable.ib_item_postselectionfragment_sh_select));
			} else {
				ib_item_postselectionfragment_sh.setEnabled(true);
				ib_item_postselectionfragment_sh.setBackground(mContext
						.getResources().getDrawable(
								R.drawable.ib_item_postselectionfragment_sh));
			}
		}
		tv_item_postselectionfragment_username
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(1, position, 0);
					}
				});
		sriv_item_postselectionfragment_userimg
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(1, position, 0);
					}
				});
		ll_item_postselectionfragment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				operation(2, position, 0);
			}
		});
		btn_item_postselectionfragment_gz
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(3, position, 0);
					}
				});
		rl_item_postselectionfragment_picorvideo
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(4, position, 0);
					}
				});
		ib_item_postselectionfragment_sh
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(5, position, 0);
					}
				});
		ib_item_postselectionfragment_bb
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(6, position, 0);
					}
				});
		ib_item_postselectionfragment_pl
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(7, position, 0);
					}
				});
		ib_item_postselectionfragment_fx
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(8, position, 0);
					}
				});
		tv_item_postselectionfragment_ckpl
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(9, position, 0);
					}
				});
		cb_item_postselectionfragment_voice
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(11, position, 0);
					}
				});
		hlv_item_postselectionfragment
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int location, long id) {
						operation(12, position, location);
					}
				});
		tv_item_postselectionfragment_delete
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(13, position, 0);
					}
				});
		btn_item_postselectionfragment_num
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(14, position, 0);
					}
				});
		return viewHolder.getConvertView();
	}

	private void initPlayer(ImageView iv_item_postselectionfragment_img,
			SurfaceVideoView sfvv_item_postselectionfragment,
			ImageView iv_item_postselectionfragment_play_status,
			List<String> coverImgs) {
		sfvv_item_postselectionfragment.setVisibility(View.GONE);
		iv_item_postselectionfragment_play_status.setVisibility(View.VISIBLE);
		iv_item_postselectionfragment_img.setVisibility(View.VISIBLE);
		if (coverImgs != null && coverImgs.size() > 0) {
			String coverImg = coverImgs.get(0);
			if (coverImg != null && !TextUtils.isEmpty(coverImg)) {
				ImageLoaderUtil.loadImgLocal(coverImg,
						iv_item_postselectionfragment_img,
						R.drawable.icon_production_default, null);
			} else {
				iv_item_postselectionfragment_img
						.setImageResource(R.drawable.icon_production_default);
			}
		}
	}

	protected void operation(int i, int position, int location) {
		PostsBean postsBean = (PostsBean) mDatas.get(position);
		this.position = position;
		if (postsBean != null) {
			int isBianBian = postsBean.getIsBianBian();
			int isFlower = postsBean.getIsFlower();
			cellphone = spUtil.getString("cellphone", "");
			List<String> videos = postsBean.getVideos();
			id = postsBean.getId();
			userId = postsBean.getUserId();
			if (postsBean.getUserName() != null
					&& !TextUtils.isEmpty(postsBean.getUserName())) {
				sharetitle = postsBean.getUserName();
			} else {
				sharetitle = "宠物家";
			}
			if (postsBean.getContent() != null
					&& !TextUtils.isEmpty(postsBean.getContent())) {
				sharetxt = postsBean.getContent();
			} else {
				sharetxt = "宠物家";
			}
			List<String> imgs = postsBean.getImgs();
			List<String> coverImgs = postsBean.getCoverImgs();
			List<GiftUsers> giftUsers = postsBean.getGiftUsers();
			if (imgs != null && imgs.size() > 0) {
				img = imgs.get(0);
				picOrVideo = pic;
			}
			if (coverImgs != null && coverImgs.size() > 0) {
				img = coverImgs.get(0);
			}
			if (videos != null && videos.size() > 0) {
				picOrVideo = video;
			}
			switch (i) {
			case 1:
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Select_Homepage);
				goToNext(PostUserInfoActivity.class, 0, "");
				break;
			case 2:
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Select_Details);
				goToNext(PostSelectionDetailActivity.class, 0, "click");
				break;
			case 3:
				if (cellphone != null && !TextUtils.isEmpty(cellphone)) {
					FLAG = FOLLOW;
					String username = spUtil.getString("username", "");
					if (username != null && !TextUtils.isEmpty(username)) {
						CommUtil.followUser(cellphone, mContext, userId,
								followUserHandler);
					} else {
						tqDialog();
					}
				} else {
					onGoToLoginListener.OnGoToLogin(NORMAL, position, null,
							null, null, null, null);
				}
				break;
			case 4:
				if (picOrVideo == pic) {
					goToNext(PostSelectionDetailActivity.class, 0, "click");
				} else if (picOrVideo == video) {
					// 下面是你对应的页面效果处理，比如显示、隐藏
					int visiblePosition = listView.getFirstVisiblePosition();
					View currentView = listView.getChildAt(position
							- visiblePosition + 2);
					sfvv_item_postselectionfragment = (SurfaceVideoView) currentView
							.findViewById(R.id.sfvv_item_postselectionfragment);
					iv_item_postselectionfragment_play_status = (ImageView) currentView
							.findViewById(R.id.iv_item_postselectionfragment_play_status);
					iv_item_postselectionfragment_videoloading = (ImageView) currentView
							.findViewById(R.id.iv_item_postselectionfragment_videoloading);
					iv_item_postselectionfragment_img = (ImageView) currentView
							.findViewById(R.id.iv_item_postselectionfragment_img);
					cb_item_postselectionfragment_voice = (CheckBox) currentView
							.findViewById(R.id.cb_item_postselectionfragment_voice);
					onGoToLoginListener.OnGoToLogin(PLAYVIDEO, position,
							sfvv_item_postselectionfragment,
							iv_item_postselectionfragment_play_status,
							iv_item_postselectionfragment_videoloading,
							iv_item_postselectionfragment_img,
							cb_item_postselectionfragment_voice);
				}
				break;
			case 5:
				if (cellphone != null && !TextUtils.isEmpty(cellphone)) {
					if (isFlower != 1) {
						String username = spUtil.getString("username", "");
						if (username != null && !TextUtils.isEmpty(username)) {
							// 下面是你对应的页面效果处理，比如显示、隐藏
							int visiblePosition = listView
									.getFirstVisiblePosition();
							View currentView = listView.getChildAt(position
									- visiblePosition + 2);
							circle_image_flower_other = (PeriscopeLayout) currentView
									.findViewById(R.id.circle_image_flower_other);
							circle_image_flower_other.addHeart(0);
							circle_image_flower_other.addHeart(0);
							circle_image_flower_other.addHeart(0);
							FLAG = FLOWER;
							CommUtil.giftPost(cellphone, mContext, id, 1,
									followUserHandler);
						} else {
							tqDialog();
						}
					}
				} else {
					onGoToLoginListener.OnGoToLogin(NORMAL, position, null,
							null, null, null, null);
				}
				break;
			case 6:
				if (cellphone != null && !TextUtils.isEmpty(cellphone)) {
					if (isBianBian != 1) {
						String username = spUtil.getString("username", "");
						if (username != null && !TextUtils.isEmpty(username)) {
							// 下面是你对应的页面效果处理，比如显示、隐藏
							int visiblePosition = listView
									.getFirstVisiblePosition();
							View currentView = listView.getChildAt(position
									- visiblePosition + 2);
							circle_image_feces_other = (PeriscopeLayout) currentView
									.findViewById(R.id.circle_image_feces_other);
							circle_image_feces_other.addHeart(1);
							circle_image_feces_other.addHeart(1);
							circle_image_feces_other.addHeart(1);
							FLAG = SHIT;
							CommUtil.giftPost(cellphone, mContext, id, 2,
									followUserHandler);
						} else {
							tqDialog();
						}
					}
				} else {
					onGoToLoginListener.OnGoToLogin(NORMAL, position, null,
							null, null, null, null);
				}
				break;
			case 7:
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Select_Comment);
				if (cellphone != null && !TextUtils.isEmpty(cellphone)) {
					String username = spUtil.getString("username", "");
					if (username != null && !TextUtils.isEmpty(username)) {
						goToNext(PostSelectionDetailActivity.class, 0,
								"comment");
					} else {
						tqDialog();
					}
				} else {
					onGoToLoginListener.OnGoToLogin(NORMAL, position, null,
							null, null, null, null);
				}
				break;
			case 8:
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Select_Share);
				new Thread(networkTask).start();
				break;
			case 9:
				goToNext(PostSelectionDetailActivity.class, 0, "bottom");
				break;
			case 11:
				// 下面是你对应的页面效果处理，比如显示、隐藏
				int visiblePosition1 = listView.getFirstVisiblePosition();
				View currentView1 = listView.getChildAt(position
						- visiblePosition1 + 2);
				cb_item_postselectionfragment_voice = (CheckBox) currentView1
						.findViewById(R.id.cb_item_postselectionfragment_voice);
				sfvv_item_postselectionfragment = (SurfaceVideoView) currentView1
						.findViewById(R.id.sfvv_item_postselectionfragment);
				PostsBean postsBean1 = (PostsBean) mDatas.get(position);
				if (postsBean1 != null) {
					postsBean1.setVoice(!postsBean1.isVoice());
					cb_item_postselectionfragment_voice.setChecked(postsBean1
							.isVoice());
					String str = cb_item_postselectionfragment_voice
							.isChecked() ? "有声音" : "无声音";
					int vol = (int) (cb_item_postselectionfragment_voice
							.isChecked() ? mAudioManager
							.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) : 0);
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							vol, 0); // 设置音量
				}
				break;
			case 12:
				if (giftUsers != null && giftUsers.size() > 0) {
					GiftUsers giftUsers2 = giftUsers.get(location);
					if (giftUsers2 != null) {
						int id = giftUsers2.getId();
						mContext.startActivity(new Intent(mContext,
								PostUserInfoActivity.class)
								.putExtra("userId", id)
								.putExtra("position", position)
								.putExtra("selectlist", "selectlist"));
					}
				}
				break;
			case 13:
				if (cellphone != null && !TextUtils.isEmpty(cellphone)) {
					setDialog();
				} else {
					onGoToLoginListener.OnGoToLogin(NORMAL, position, null,
							null, null, null, null);
				}
				break;
			case 14:
				goToNext(UserListActivity.class, 0, "flower");
				break;
			default:
				break;
			}
		}
	}

	private void setDialog() {
		new AlertDialogNavAndPost(mContext).builder().setTitle("")
				.setMsg("确定删除此条动态吗")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						FLAG = DELETEPOST;
						CommUtil.deletePost(cellphone, mContext, id,
								followUserHandler);
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
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
					onGoToLoginListener.OnGoToLogin(REFRESH, position, null,
							null, null, null, null);
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

	private void goToNext(Class clazz, int previous, String flag) {
		Intent intent = new Intent(mContext, clazz);
		intent.putExtra("userId", userId);
		intent.putExtra("postId", id);
		intent.putExtra("flag", flag);
		intent.putExtra("selectlist", "selectlist");
		intent.putExtra("position", position);
		intent.putExtra("previous", previous);
		mContext.startActivity(intent);
	}

	/**
	 * 网络操作相关的子线程
	 */
	Runnable networkTask = new Runnable() {

		@Override
		public void run() {
			// 在这里进行 http request.网络请求相关操作
			Bitmap returnBitmap = Utils.GetLocalOrNetBitmap(img);
			Message msg = new Message();
			msg.obj = returnBitmap;
			handler.sendMessage(msg);
		}
	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			bmp = (Bitmap) msg.obj;
			showShare();
		}
	};

	private void showShare() {
		final View view = mInflater.inflate(R.layout.sharedialog, null);
		RelativeLayout rlParent = (RelativeLayout) view
				.findViewById(R.id.rl_sharedialog_parent);
		LinearLayout ll_sharedialog_wxfriend = (LinearLayout) view
				.findViewById(R.id.ll_sharedialog_wxfriend);
		LinearLayout ll_sharedialog_wxpyq = (LinearLayout) view
				.findViewById(R.id.ll_sharedialog_wxpyq);
		LinearLayout ll_sharedialog_qqfriend = (LinearLayout) view
				.findViewById(R.id.ll_sharedialog_qqfriend);
		LinearLayout ll_sharedialog_sina = (LinearLayout) view
				.findViewById(R.id.ll_sharedialog_sina);
		ll_sharedialog_sina.setVisibility(View.VISIBLE);
		Button btn_sharedialog_cancel = (Button) view
				.findViewById(R.id.btn_sharedialog_cancel);
		if (pWin == null) {
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
		}
		pWin.setFocusable(true);
		pWin.setWidth(DeviceUtils.getScreenWidth(mContext));
		pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		rlParent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pWin.dismiss();
				pWin = null;
			}
		});
		ll_sharedialog_wxfriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {// 微信好友
				pWin.dismiss();
				pWin = null;
				setWXShareContent(1);
			}
		});
		ll_sharedialog_wxpyq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {// 微信朋友圈
				pWin.dismiss();
				pWin = null;
				setWXShareContent(2);
			}
		});
		ll_sharedialog_qqfriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {// QQ好友
				pWin.dismiss();
				pWin = null;
				setWXShareContent(3);
			}
		});
		ll_sharedialog_sina.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {// 新浪微博
				pWin.dismiss();
				pWin = null;
				setWXShareContent(4);
			}
		});
		btn_sharedialog_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {// 取消
				pWin.dismiss();
				pWin = null;
			}
		});
	}

	private void setWXShareContent(int type) {
		boolean weixinAvilible = Utils.isWeixinAvilible(mContext);
		if (shareurl.contains("?")) {
			shareurl = shareurl + "&postId=" + id;
		} else {
			shareurl = shareurl + "?postId=" + id;
		}
		if (bmp != null && shareurl != null && !TextUtils.isEmpty(shareurl)) {
			if (type == 1 || type == 2) {// 微信
				if (weixinAvilible) {
					WXWebpageObject wxwebpage = new WXWebpageObject();
					wxwebpage.webpageUrl = shareurl;
					WXMediaMessage wxmedia = new WXMediaMessage(wxwebpage);
					wxmedia.title = sharetitle;
					wxmedia.description = sharetxt;
					Bitmap createBitmapThumbnail = Utils
							.createBitmapThumbnail(bmp);
					wxmedia.setThumbImage(createBitmapThumbnail);
					wxmedia.thumbData = Util_WX.bmpToByteArray(
							createBitmapThumbnail, true);
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("webpage");
					req.message = wxmedia;
					if (type == 1) {
						req.scene = SendMessageToWX.Req.WXSceneSession;
					} else {
						req.scene = SendMessageToWX.Req.WXSceneTimeline;
					}
					api.sendReq(req);
				} else {
					ToastUtil.showToastShortBottom(mContext, "微信不可用");
				}
			} else if (type == 3) {// qq
				umengShareUtils = new UmengShareUtils(mContext, sharetxt,
						shareurl, sharetitle, img);
				umengShareUtils.mController.getConfig().closeToast();
				umengShareUtils.mController.postShare(mContext, SHARE_MEDIA.QQ,
						new SnsPostListener() {
							@Override
							public void onStart() {
								/*
								 * ToastUtil.showToastShortCenter(ADActivity.this
								 * , "开始分享.");
								 */
							}

							@Override
							public void onComplete(SHARE_MEDIA arg0, int eCode,
									SocializeEntity arg2) {
								if (eCode == 200) {
									/*
									 * ToastUtil.showToastShortCenter(
									 * ADActivity.this, "分享成功.");
									 */
								} else {
									String eMsg = "";
									if (eCode == -101) {
										eMsg = "没有授权";
									}
									/*
									 * ToastUtil.showToastShortCenter(
									 * ADActivity.this, "分享失败[" + eCode + "] " +
									 * eMsg);
									 */
								}
							}
						});
			} else if (type == 4) {// 新浪微博
				umengShareUtils = new UmengShareUtils(mContext, sharetxt,
						shareurl, sharetitle, img);
				umengShareUtils.mController.getConfig().closeToast();
				boolean isSina = OauthHelper.isAuthenticated(mContext,
						SHARE_MEDIA.SINA);
				// 如果未授权则授权
				if (!isSina) {
					umengShareUtils.mController.doOauthVerify(mContext,
							SHARE_MEDIA.SINA, new UMAuthListener() {
								@Override
								public void onStart(SHARE_MEDIA arg0) {

								}

								@Override
								public void onError(SocializeException arg0,
										SHARE_MEDIA arg1) {

								}

								@Override
								public void onComplete(Bundle value,
										SHARE_MEDIA platform) {
									umengShareUtils.mController.postShare(
											mContext, SHARE_MEDIA.SINA,
											new SnsPostListener() {
												@Override
												public void onStart() {
													/*
													 * ToastUtil.
													 * showToastShortCenter
													 * (ADActivity.this ,
													 * "开始分享.");
													 */
												}

												@Override
												public void onComplete(
														SHARE_MEDIA arg0,
														int eCode,
														SocializeEntity arg2) {
													if (eCode == 200) {
														/*
														 * ToastUtil.
														 * showToastShortCenter(
														 * ADActivity.this,
														 * "分享成功.");
														 */
													} else {
														String eMsg = "";
														if (eCode == -101) {
															eMsg = "没有授权";
														}
														/*
														 * ToastUtil.
														 * showToastShortCenter(
														 * ADActivity.this,
														 * "分享失败[" + eCode +
														 * "] " + eMsg);
														 */
													}
												}
											});
								}

								@Override
								public void onCancel(SHARE_MEDIA arg0) {
								}
							});
				} else {
					umengShareUtils.mController.postShare(mContext,
							SHARE_MEDIA.SINA, new SnsPostListener() {
								@Override
								public void onStart() {
									/*
									 * ToastUtil.showToastShortCenter(ADActivity.
									 * this , "开始分享.");
									 */
								}

								@Override
								public void onComplete(SHARE_MEDIA arg0,
										int eCode, SocializeEntity arg2) {
									if (eCode == 200) {
										/*
										 * ToastUtil.showToastShortCenter(
										 * ADActivity.this, "分享成功.");
										 */
									} else {
										String eMsg = "";
										if (eCode == -101) {
											eMsg = "没有授权";
										}
										/*
										 * ToastUtil.showToastShortCenter(
										 * ADActivity.this, "分享失败[" + eCode +
										 * "] " + eMsg);
										 */
									}
								}
							});
				}
			}
		} else {
			ToastUtil.showToastShortCenter(mContext, mContext.getResources()
					.getString(R.string.no_bitmap));
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	private AsyncHttpResponseHandler followUserHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("关注：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (FLAG == FOLLOW) {
						if (jobj.has("data") && !jobj.isNull("data")) {
							JSONObject jData = jobj.getJSONObject("data");
							if (jData.has("isFollow")
									&& !jData.isNull("isFollow")) {
								int isFollow = jData.getInt("isFollow");
								if (isFollow == 2) {// 未关注
									ToastUtil.showToastShortBottom(mContext,
											"取消关注");
								} else if (isFollow == 1) {// 已关注
									ToastUtil.showToastShortBottom(mContext,
											"关注成功");
								}
								refreshGZ(position, isFollow);
							}
						}
					} else if (FLAG == FLOWER) {
						refreshFlower(position);
						ToastUtil.showToastShortBottom(mContext, "小花+1");
					} else if (FLAG == SHIT) {
						refreshShit(position);
						ToastUtil.showToastShortBottom(mContext, "biu~");
					} else if (FLAG == DELETEPOST) {
						onGoToLoginListener.OnGoToLogin(REFRESHDELETEPOST,
								position, null, null, null, null, null);
						ToastUtil.showToastShortBottom(mContext, "删除成功");
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

	public void setShareUrl(String shareUrl) {
		this.shareurl = shareUrl;
	}

	public void refreshFlower(int position) {
		if (mDatas != null && mDatas.size() > 0) {
			PostsBean postsBean = (PostsBean) mDatas.get(position);
			postsBean.setIsFlower(1);
			List<GiftUsers> giftUsers = postsBean.getGiftUsers();
			if (giftUsers != null && giftUsers.size() > 0) {
				giftUsers.add(
						0,
						new GiftUsers(spUtil.getString("userimage", ""), spUtil
								.getInt("userid", 0)));
				postsBean.setGiftUsers(giftUsers);
			} else {
				List<GiftUsers> giftUsersTemp = new ArrayList<GiftUsers>();
				giftUsersTemp.add(
						0,
						new GiftUsers(spUtil.getString("userimage", ""), spUtil
								.getInt("userid", 0)));
				postsBean.setGiftUsers(giftUsersTemp);
			}
			int flowerAmount = postsBean.getFlowerAmount();
			++flowerAmount;
			postsBean.setFlowerAmount(flowerAmount);
			notifyDataSetChanged();
		}
	}

	public void refreshShit(int position) {
		if (mDatas != null && mDatas.size() > 0) {
			PostsBean postsBean = (PostsBean) mDatas.get(position);
			if (postsBean != null) {
				postsBean.setIsBianBian(1);
			}
			notifyDataSetChanged();
		}
	}

	public void refreshGZ(int position, int isFollow) {
		if (mDatas != null && mDatas.size() > 0) {
			PostsBean postsBean2 = (PostsBean) mDatas.get(position);
			if (postsBean2 != null) {
				int userId2 = -1;
				if (postsBean2 != null) {
					userId2 = postsBean2.getUserId();
				}
				for (int i = 0; i < mDatas.size(); i++) {
					PostsBean postsBean = (PostsBean) mDatas.get(i);
					if (postsBean != null) {
						int userId = postsBean.getUserId();
						if (userId == userId2) {
							postsBean.setIsFollow(isFollow);
						}
					}
				}
				notifyDataSetChanged();
			}
		}
	}

	public void refreshComments(int postSelectionfragmentAdapter_position,
			List<CommentUsers> commentUsers) {
		if (mDatas != null && mDatas.size() > 0) {
			PostsBean postsBean = (PostsBean) mDatas
					.get(postSelectionfragmentAdapter_position);
			if (postsBean != null) {
				postsBean.setCommentAmount(postsBean.getCommentAmount()
						+ commentUsers.size());
				List<CommentUsers> commentUsers2 = postsBean.getCommentUsers();
				if (commentUsers2 != null && commentUsers2.size() > 0) {
					commentUsers2.addAll(commentUsers);
					postsBean.setCommentUsers(commentUsers2.subList(0, 3));
				} else {
					if (commentUsers.size() > 3) {
						postsBean.setCommentUsers(commentUsers.subList(0, 3));
					} else {
						postsBean.setCommentUsers(commentUsers);
					}
				}
			}
			notifyDataSetChanged();
		}
	}
}
