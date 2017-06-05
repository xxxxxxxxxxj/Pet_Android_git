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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.PetCircleInsideDetailActivity;
import com.haotang.pet.PostSelectionDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.VideoPlayerActivity;
import com.haotang.pet.entity.PostUserInfoBean.PostsBean;
import com.haotang.pet.util.Constant;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.MyGridView;
import com.haotang.pet.view.ViewHolder;
import com.loveplusplus.demo.image.ImagePagerActivity;
import com.melink.bqmmsdk.sdk.BQMMMessageHelper;
import com.melink.bqmmsdk.widget.BQMMMessageText;

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
public class PostUserInfoAdapter<T> extends CommonAdapter<T> {
	private static final int isVideo = 2;
	private static final int isPic = 1;
	private int picOrVideo = 0;
	private List<String> imgs;
	private String[] imgUrls = new String[1];
	private List<String> videos;
	private int id;
	private String img;
	private String videoPath;
	private List<Object> emojis = new ArrayList<Object>();

	public PostUserInfoAdapter(Activity mContext, List<T> mDatas) {
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
				R.layout.item_postuserinfo, position);
		RelativeLayout rl_item_postuserinfo_line = viewHolder
				.getView(R.id.rl_item_postuserinfo_line);
		RelativeLayout rl_item_postuserinfo_root = viewHolder
				.getView(R.id.rl_item_postuserinfo_root);
		View vw1 = viewHolder.getView(R.id.vw1);
		ImageView iv_item_postuserinfo_circle = viewHolder
				.getView(R.id.iv_item_postuserinfo_circle);
		View vw2 = viewHolder.getView(R.id.vw2);
		TextView tv_item_postuserinfo_time = viewHolder
				.getView(R.id.tv_item_postuserinfo_time);
		BQMMMessageText bqmt_item_postuserinfo_content = viewHolder
				.getView(R.id.bqmt_item_postuserinfo_content);
		MyGridView mgv_item_postuserinfo_pic = viewHolder
				.getView(R.id.mgv_item_postuserinfo_pic);
		RelativeLayout rl_item_postuserinfo_picorvideo = viewHolder
				.getView(R.id.rl_item_postuserinfo_picorvideo);
		ImageView iv_item_postuserinfo_coverimg = viewHolder
				.getView(R.id.iv_item_postuserinfo_coverimg);
		ImageView iv_item_postuserinfo_play_status = viewHolder
				.getView(R.id.iv_item_postuserinfo_play_status);
		picOrVideo = 0;
		final PostsBean postsBean = (PostsBean) mDatas.get(position);
		int id2 = postsBean.getId();
		int postType = postsBean.getPostType();
		String created = postsBean.getCreated();
		String content = postsBean.getContent();
		List<String> coverImgs = postsBean.getCoverImgs();
		imgs = postsBean.getImgs();
		videos = postsBean.getVideos();
		List<String> smallCoverImgs = postsBean.getSmallCoverImgs();
		List<String> smallImgs = postsBean.getSmallImgs();
		if (created != null && !TextUtils.isEmpty(created)) {
			tv_item_postuserinfo_time.setVisibility(View.VISIBLE);
			vw1.setVisibility(View.VISIBLE);
			iv_item_postuserinfo_circle.setVisibility(View.VISIBLE);
			vw2.setVisibility(View.VISIBLE);
			tv_item_postuserinfo_time.setText(created);
		} else {
			tv_item_postuserinfo_time.setVisibility(View.GONE);
			vw1.setVisibility(View.GONE);
			iv_item_postuserinfo_circle.setVisibility(View.GONE);
			vw2.setVisibility(View.GONE);
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
			bqmt_item_postuserinfo_content.setVisibility(View.VISIBLE);
			// 小表情或文字或图文混排
			bqmt_item_postuserinfo_content.showMessage(id2 + "",
					BQMMMessageHelper.getMsgCodeString(jsonArray),
					BQMMMessageText.EMOJITYPE, jsonArray);
			bqmt_item_postuserinfo_content.getBackground().setAlpha(255);
		} else {
			bqmt_item_postuserinfo_content.setVisibility(View.GONE);
		}
		if (postType == 1) {// 宠圈
			if (smallImgs != null && smallImgs.size() > 0) {
				mgv_item_postuserinfo_pic.bringToFront();
				mgv_item_postuserinfo_pic.setVisibility(View.VISIBLE);
				rl_item_postuserinfo_picorvideo.setVisibility(View.GONE);
				mgv_item_postuserinfo_pic
						.setAdapter(new PostUserInfoImageAdapter<String>(
								mContext, smallImgs));
			} else {
				mgv_item_postuserinfo_pic.setVisibility(View.GONE);
				rl_item_postuserinfo_picorvideo.setVisibility(View.GONE);
			}
		} else if (postType == 2) {// 精选
			if (smallCoverImgs != null && smallCoverImgs.size() > 0) {
				picOrVideo = isVideo;
				String smallCoverImg = smallCoverImgs.get(0);
				rl_item_postuserinfo_picorvideo.bringToFront();
				mgv_item_postuserinfo_pic.setVisibility(View.GONE);
				iv_item_postuserinfo_play_status.bringToFront();
				rl_item_postuserinfo_picorvideo.setVisibility(View.VISIBLE);
				iv_item_postuserinfo_coverimg.setVisibility(View.VISIBLE);
				iv_item_postuserinfo_play_status.setVisibility(View.VISIBLE);
				if (smallCoverImg != null && !TextUtils.isEmpty(smallCoverImg)) {
					ImageLoaderUtil.loadImg(smallCoverImg,
							iv_item_postuserinfo_coverimg,
							R.drawable.icon_production_default, null);
				} else {
					iv_item_postuserinfo_coverimg
							.setImageResource(R.drawable.icon_production_default);
				}
			} else if (smallImgs != null && smallImgs.size() > 0) {
				picOrVideo = isPic;
				String smallImg = smallImgs.get(0);
				iv_item_postuserinfo_coverimg.bringToFront();
				rl_item_postuserinfo_picorvideo.bringToFront();
				mgv_item_postuserinfo_pic.setVisibility(View.GONE);
				rl_item_postuserinfo_picorvideo.setVisibility(View.VISIBLE);
				iv_item_postuserinfo_coverimg.setVisibility(View.VISIBLE);
				iv_item_postuserinfo_play_status.setVisibility(View.GONE);
				if (smallImg != null && !TextUtils.isEmpty(smallImg)) {
					ImageLoaderUtil.loadImg(smallImg,
							iv_item_postuserinfo_coverimg,
							R.drawable.icon_production_default, null);
				} else {
					iv_item_postuserinfo_coverimg
							.setImageResource(R.drawable.icon_production_default);
				}
			} else {
				mgv_item_postuserinfo_pic.setVisibility(View.GONE);
				rl_item_postuserinfo_picorvideo.setVisibility(View.GONE);
			}
		}
		rl_item_postuserinfo_root
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(1, position, 0);
					}
				});
		iv_item_postuserinfo_coverimg
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						operation(2, position, 0);
					}
				});
		mgv_item_postuserinfo_pic
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int location, long id) {
						operation(3, position, location);
					}
				});
		return viewHolder.getConvertView();
	}

	protected void operation(int i, int position, int location) {
		PostsBean postsBean = (PostsBean) mDatas.get(position);
		if (postsBean != null) {
			List<String> videos = postsBean.getVideos();
			id = postsBean.getId();
			List<String> imgs = postsBean.getImgs();
			int postType = postsBean.getPostType();
			if (imgs != null && imgs.size() > 0) {
				img = imgs.get(0);
				picOrVideo = isPic;
			}
			if (videos != null && videos.size() > 0) {
				videoPath = videos.get(0);
				picOrVideo = isVideo;
			}
			switch (i) {
			case 1:
				if (postType == 1) {
					goToNext(PetCircleInsideDetailActivity.class, id, 0,
							"click");
				} else if (postType == 2) {
					goToNext(PostSelectionDetailActivity.class, id, 0, "click");
				}
				break;
			case 2:
				if (picOrVideo == isVideo) {
					if (videoPath != null && !TextUtils.isEmpty(videoPath)) {
						mContext.startActivity(new Intent(mContext,
								VideoPlayerActivity.class).putExtra(
								Constant.RECORD_VIDEO_PATH, videoPath));
					}
				} else if (picOrVideo == isPic) {
					if (img != null && !TextUtils.isEmpty(img)) {
						imgUrls[0] = img;
						imageBrower(0, imgUrls);
					}
				}
				break;
			case 3:
				String[] ytImgs = new String[imgs.size()];
				for (int j = 0; j < imgs.size(); j++) {
					ytImgs[j] = imgs.get(j);
				}
				imageBrower(location, ytImgs);
				break;
			default:
				break;
			}
		}
	}

	private void goToNext(Class clazz, int id, int previous, String flag) {
		Intent intent = new Intent(mContext, clazz);
		intent.putExtra("postId", id);
		intent.putExtra("flag", flag);
		intent.putExtra("previous", previous);
		mContext.startActivity(intent);
	}

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}
}
