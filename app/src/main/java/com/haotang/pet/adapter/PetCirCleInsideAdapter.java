package com.haotang.pet.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.haotang.pet.LoginActivity;
import com.haotang.pet.PetCirClePostImageActivity;
import com.haotang.pet.PetCircleInsideActivity;
import com.haotang.pet.PetCircleInsideDetailActivity;
import com.haotang.pet.PostUserInfoActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.PetCircleInside;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengShareUtils;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.util.wxpay.Util_WX;
import com.haotang.pet.view.AlertDialogNavAndPost;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.UserNameAlertDialog;
import com.haotang.pet.view.ViewHolder;
import com.loveplusplus.demo.image.ImagePagerActivity;
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

public class PetCirCleInsideAdapter<T> extends CommonAdapter<T>{
	
	
	private String imgUrl;
	protected Bitmap bmp;
	private UmengShareUtils umengShareUtils;
	private PopupWindow pWin;
	protected String sharetitle;
	protected String sharetxt;
	protected String shareurl;
	private IWXAPI api;
	private int id;
	MProgressDialog dialog = null;
	private int isShareUrl = -1;
	private int ClickPosition;
	public PetCirCleInsideAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		dialog = new MProgressDialog(mContext);
		api = WXAPIFactory.createWXAPI(mContext, Global.APP_ID);
		sharetitle = "宠物家";
		sharetxt = "宠物家";
		shareurl = "http://www.haotang365.com.cn/";
		imgUrl = "http://www.haotang365.com.cn/images/logo1.png";
	}

	public void setNotif(){
		notifyDataSetChanged();
	}
	@SuppressWarnings("rawtypes")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item_pet_circle_inside, position);
		final PetCircleInside inside = (PetCircleInside) mDatas.get(position);
		if (TextUtils.isEmpty(inside.userName)) {
			viewHolder.setText(R.id.circle_inisde_name,"");
		}else {
			viewHolder.setText(R.id.circle_inisde_name, inside.userName);
		}
		viewHolder.setText(R.id.circle_inside_context, inside.content);
		viewHolder.setText(R.id.txt_post_time, inside.created);
		SelectableRoundedImageView imageview_petcircle_icon = viewHolder.getView(R.id.imageview_petcircle_icon);
		if(inside.avatar != null && !TextUtils.isEmpty(inside.avatar)){
			ImageLoaderUtil.loadImg(inside.avatar, imageview_petcircle_icon, R.drawable.icon_self, null);
		}else{
			imageview_petcircle_icon.setImageResource(R.drawable.icon_self);
		}
		
		if (inside.duty==1||inside.duty==2) {
			viewHolder.getView(R.id.imageview_petcircle_tag).setVisibility(View.VISIBLE);
		}else {
			viewHolder.getView(R.id.imageview_petcircle_tag).setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(inside.memberIcon)) {
			if (inside.duty==1||inside.duty==2) {
				ImageView imageView = viewHolder.getView(R.id.imageview_petcircle_tag);
				imageView.setImageResource(R.drawable.dz_jl_icon);
			}else {
				viewHolder.getView(R.id.imageview_petcircle_tag).setVisibility(View.VISIBLE);
				ImageLoaderUtil.loadImg(inside.memberIcon, (ImageView)viewHolder.getView(R.id.imageview_petcircle_tag),0, null);
			}
		}else {
			viewHolder.getView(R.id.imageview_petcircle_tag).setVisibility(View.GONE);
		}
		LinearLayout layout_petcircle = viewHolder.getView(R.id.layout_petcircle);
		LinearLayout layoutflower = viewHolder.getView(R.id.circle_layout_give_flower);
		if (inside.smallImgsList.size()>0) {
			viewHolder.getView(R.id.circle_inisde_image_more).setVisibility(View.VISIBLE);
			if (inside.smallImgsList.size()>3) {
				viewHolder.getView(R.id.one_img).setVisibility(View.VISIBLE);
				viewHolder.getView(R.id.two_img).setVisibility(View.VISIBLE);
				viewHolder.getView(R.id.thr_img).setVisibility(View.VISIBLE);
				viewHolder.setBackgroundNormal(R.id.one_img, inside.smallImgsList.get(0), R.drawable.icon_production_default);
				viewHolder.setBackgroundNormal(R.id.two_img, inside.smallImgsList.get(1), R.drawable.icon_production_default);
				viewHolder.setBackgroundNormal(R.id.thr_img, inside.smallImgsList.get(2), R.drawable.icon_production_default);
			}else {
				switch (inside.smallImgsList.size()) {
				case 0:
					viewHolder.getView(R.id.one_img).setVisibility(View.GONE);
					viewHolder.getView(R.id.two_img).setVisibility(View.GONE);
					viewHolder.getView(R.id.thr_img).setVisibility(View.GONE);
					break;
				case 1:
					viewHolder.getView(R.id.one_img).setVisibility(View.VISIBLE);
					viewHolder.setBackgroundNormal(R.id.one_img, inside.smallImgsList.get(0), R.drawable.icon_production_default);
					viewHolder.getView(R.id.two_img).setVisibility(View.GONE);
					viewHolder.getView(R.id.thr_img).setVisibility(View.GONE);
					break;
				case 2:
					viewHolder.getView(R.id.one_img).setVisibility(View.VISIBLE);
					viewHolder.setBackgroundNormal(R.id.one_img, inside.smallImgsList.get(0), R.drawable.icon_production_default);
					viewHolder.getView(R.id.two_img).setVisibility(View.VISIBLE);
					viewHolder.setBackgroundNormal(R.id.two_img, inside.smallImgsList.get(1), R.drawable.icon_production_default);
					viewHolder.getView(R.id.thr_img).setVisibility(View.GONE);
					break;
				case 3:
					viewHolder.getView(R.id.one_img).setVisibility(View.VISIBLE);
					viewHolder.setBackgroundNormal(R.id.one_img, inside.smallImgsList.get(0), R.drawable.icon_production_default);
					viewHolder.getView(R.id.two_img).setVisibility(View.VISIBLE);
					viewHolder.setBackgroundNormal(R.id.two_img, inside.smallImgsList.get(1), R.drawable.icon_production_default);
					viewHolder.getView(R.id.thr_img).setVisibility(View.VISIBLE);
					viewHolder.setBackgroundNormal(R.id.thr_img, inside.smallImgsList.get(2), R.drawable.icon_production_default);
					break;
				}
			}
		}else {
			viewHolder.getView(R.id.circle_inisde_image_more).setVisibility(View.GONE);
		}
		
		if (inside.commentAmount>99) {
			viewHolder.setText(R.id.textview_eva_count, "评论(99+)");
		}else {
			viewHolder.setText(R.id.textview_eva_count, "评论("+inside.commentAmount+")");
		}
		if (!TextUtils.isEmpty(inside.title)) {
			viewHolder.getView(R.id.layout_form_eva).setVisibility(View.VISIBLE);
			viewHolder.setText(R.id.textview_form_eva, inside.title);
		}else {
			viewHolder.getView(R.id.layout_form_eva).setVisibility(View.GONE);
			viewHolder.setText(R.id.textview_form_eva, "");
		}
		ImgToDetail(viewHolder, inside);
		itemToDetail(position, viewHolder, layout_petcircle);
		GoPetUserInfo(position, viewHolder);
		GoEvaToDetail(position, layoutflower);
		
		GoShare(position, viewHolder);
		
		viewHolder.getView(R.id.txt_selfpost_can_delete).setVisibility(View.GONE);
		if (!Utils.checkLogin(mContext)) {
			viewHolder.getView(R.id.txt_selfpost_can_delete).setVisibility(View.GONE);
		}else {
			int userid = spUtil.getInt("userid", 0);
			if (userid!=0) {
				if (userid == inside.userId) {
					viewHolder.getView(R.id.txt_selfpost_can_delete).setVisibility(View.VISIBLE);
				}else {
					viewHolder.getView(R.id.txt_selfpost_can_delete).setVisibility(View.GONE);
				}
			}
		}
		
		viewHolder.getView(R.id.txt_selfpost_can_delete).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				ToastUtil.showToastShortCenter(mContext, "请求接口删除当前自己发的帖子");
//				Intent intent = new Intent();
//				intent.setAction("android.intent.action.PetCircleInsideActivity");
//				intent.putExtra("position", ClickPosition);
//				intent.putExtra("indexTag", 1);
//				mContext.sendBroadcast(intent);
//				Utils.mLogError("==-->indexTag 0");
				setDialog(position);
			}
		});
		
		return viewHolder.getConvertView();
	}
	private void setDialog(final int position) {
		new AlertDialogNavAndPost(mContext).builder().setTitle("")
				.setMsg("确定删除此条动态吗?")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.showDialog();
						ClickPosition = position;
						PetCircleInside  insideClick = (PetCircleInside) mDatas.get(position);
						CommUtil.deletePost(spUtil.getString("cellphone", "0"), mContext, insideClick.PostInfoId, deletePost);
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					}
				}).show();
	}
	private void GoShare(final int position, ViewHolder viewHolder) {
		viewHolder.getView(R.id.circle_inside_share).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_PetCircle_Share);
				// 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
				isShareUrl = -1;
				PetCircleInside  insideClick = (PetCircleInside) mDatas.get(position);
				id = insideClick.PostInfoId;
				if (!TextUtils.isEmpty(insideClick.shareurl)||!insideClick.shareurl.equals("")) {
					shareurl = insideClick.shareurl;
					isShareUrl = 1;
				}else {
					shareurl = "http://www.haotang365.com.cn/";
				}
				if (insideClick.smallImgsList.size()>0) {
					imgUrl = insideClick.smallImgsList.get(0);
				}else {
					imgUrl = "http://www.haotang365.com.cn/images/logo1.png";
				}
				if (!TextUtils.isEmpty(insideClick.userName)||!"".equals(insideClick.userName)) {
					sharetitle = insideClick.userName;
				}else {
					sharetitle = "宠物家";
				}
				if (!TextUtils.isEmpty(insideClick.content)||!"".equals(insideClick.content)) {
					sharetxt  = insideClick.content;
					Utils.mLogError("==-->sharetxt :=  "+sharetxt);
				}else {
					sharetxt = "宠物家";
				}
				try {
					dialog.showDialog();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new Thread(networkTask).start();
			}
		});
	}

	private void GoEvaToDetail(final int position, LinearLayout layoutflower) {
		layoutflower.setOnClickListener(new OnClickListener() {//评论
			
			@Override
			public void onClick(View v) {
				UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_PetCircle_Comment);
				if (!Utils.checkLogin(mContext)) {
					ClickPosition = position;
					PetCircleInside circleInside = (PetCircleInside) mDatas.get(position);
					Intent intent = new Intent(mContext, PetCircleInsideDetailActivity.class);
					intent.putExtra("postId", circleInside.PostInfoId);
					intent.putExtra("ClickPosition",ClickPosition);
					Utils.mLogError("==-->postId 0 : "+position);
					mContext.startActivity(intent);
					ClickPosition  = -1;
				}else {
					String userName = spUtil.getString("username", "");
					if (userName.equals("")||TextUtils.isEmpty(userName)) {
						tqDialog();
					}else {
						ClickPosition = position;
						PetCircleInside circleInside = (PetCircleInside) mDatas.get(position);
						Intent intent = new Intent(mContext, PetCircleInsideDetailActivity.class);
						intent.putExtra("postId", circleInside.PostInfoId);
						intent.putExtra("ClickPosition", ClickPosition);
						Utils.mLogError("==-->postId 0 : "+position);
						mContext.startActivity(intent);
						ClickPosition =-1;
					}
				}
			}
		});
	}

	private void GoPetUserInfo(final int position, ViewHolder viewHolder) {
		viewHolder.getView(R.id.imageview_petcircle_icon).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Utils.checkLogin(mContext)) {
					PetCircleInside circleInside = (PetCircleInside) mDatas.get(position);
					goToNext(PostUserInfoActivity.class,circleInside.userId,0, "");
				}else {
					goToNext(LoginActivity.class,0,0, "");
				}
			}
		});
	}

	private void itemToDetail(final int position, ViewHolder viewHolder,
			LinearLayout layout_petcircle) {
		viewHolder.getView(R.id.circle_inside_context).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ClickPosition = position;
				PetCircleInside circleInside = (PetCircleInside) mDatas.get(position);
				Intent intent = new Intent(mContext, PetCircleInsideDetailActivity.class);
				intent.putExtra("postId", circleInside.PostInfoId);
				intent.putExtra("ClickPosition",ClickPosition);
				Utils.mLogError("==-->postId 1 : "+position);
				mContext.startActivity(intent);
				ClickPosition = -1;
			}
		});
		viewHolder.getView(R.id.circle_inisde_image_more).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ClickPosition = position;
				PetCircleInside circleInside = (PetCircleInside) mDatas.get(position);
				Intent intent = new Intent(mContext, PetCircleInsideDetailActivity.class);
				intent.putExtra("postId", circleInside.PostInfoId);
				intent.putExtra("ClickPosition",ClickPosition);
				Utils.mLogError("==-->postId 1 : "+position);
				mContext.startActivity(intent);
				ClickPosition = -1;
			}
		});
		layout_petcircle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ClickPosition = position;
				PetCircleInside circleInside = (PetCircleInside) mDatas.get(position);
				Intent intent = new Intent(mContext, PetCircleInsideDetailActivity.class);
				intent.putExtra("postId", circleInside.PostInfoId);
				intent.putExtra("ClickPosition",ClickPosition);
				Utils.mLogError("==-->postId 1 : "+position);
				mContext.startActivity(intent);
				ClickPosition = -1;
			}
		});
		
	}

	private void ImgToDetail(ViewHolder viewHolder, final PetCircleInside inside) {
		viewHolder.getView(R.id.one_img).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				list.addAll(inside.list);
//				Intent intent = new Intent(mContext, PetCirClePostImageActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putInt("indexTag", 1);
//				bundle.putInt("position", 0);
//				bundle.putStringArrayList("list",list);
//				intent.putExtras(bundle);
////				mContext.startActivity(intent);
//				mContext.startActivityForResult(intent, Global.PETCIRCLELIST_TO_IMAGE);
				String[] arrs  = list.toArray(new String[list.size()]);
				imageBrower(0, arrs);
			}
		});
		viewHolder.getView(R.id.two_img).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				list.addAll(inside.list);
//				Intent intent = new Intent(mContext, PetCirClePostImageActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putInt("indexTag", 1);
//				bundle.putInt("position", 1);
//				bundle.putStringArrayList("list",list);
//				intent.putExtras(bundle);
////				mContext.startActivity(intent);
//				mContext.startActivityForResult(intent, Global.PETCIRCLELIST_TO_IMAGE);
				String[] arrs  = list.toArray(new String[list.size()]);
				imageBrower(1, arrs);
			}
		});
		viewHolder.getView(R.id.thr_img).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				list.addAll(inside.list);
//				Intent intent = new Intent(mContext, PetCirClePostImageActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putInt("indexTag", 1);
//				bundle.putInt("position", 2);
//				bundle.putStringArrayList("list",list);
//				intent.putExtras(bundle);
////				mContext.startActivity(intent);
//				mContext.startActivityForResult(intent, Global.PETCIRCLELIST_TO_IMAGE);
				String[] arrs  = list.toArray(new String[list.size()]);
				imageBrower(2, arrs);
			}
		});
	}

	private void goToNext(Class clazz, int userId,int previous, String flag) {
		Intent intent = new Intent(mContext, clazz);
		intent.putExtra("userId", userId);
		intent.putExtra("postId", id);
		intent.putExtra("flag", flag);
		intent.putExtra("previous", previous);
		mContext.startActivity(intent);
	}
	/**
	 * 网络操作相关的子线程
	 */
	Runnable networkTask = new Runnable() {

		@Override
		public void run() {
			// TODO
			// 在这里进行 http request.网络请求相关操作
			Bitmap returnBitmap = Utils.GetLocalOrNetBitmap(imgUrl);
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
		try {
			dialog.closeDialog();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		if (isShareUrl==1) {
			if (shareurl.contains("?")) {
				shareurl = shareurl + "&postId=" + id;
			} else {
				shareurl = shareurl + "?postId=" + id;
			}
		}
		Utils.mLogError("==-->shareurl：= "+shareurl);
		if (bmp != null && sharetxt != null && !TextUtils.isEmpty(sharetxt)
				&& sharetitle != null && !TextUtils.isEmpty(sharetitle)
				&& shareurl != null && !TextUtils.isEmpty(shareurl)) {
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
						shareurl, sharetitle, imgUrl);
				umengShareUtils.mController.getConfig().closeToast();
				umengShareUtils.mController.postShare(mContext, SHARE_MEDIA.QQ,
						new SnsPostListener() {
							@Override
							public void onStart() {
								
								/* ToastUtil.showToastShortCenter(mContext
								 , "开始分享.");*/
								 
							}

							@Override
							public void onComplete(SHARE_MEDIA arg0, int eCode,
									SocializeEntity arg2) {
								if (eCode == 200) {
									
									/* ToastUtil.showToastShortCenter(
											 mContext, "分享成功.");*/
									 
								} else {
									String eMsg = "";
									if (eCode == -101) {
										eMsg = "没有授权";
									}
									
									 /*ToastUtil.showToastShortCenter(
											 mContext, "分享失败[" + eCode + "] " +
									 eMsg);*/
									 
								}
							}
						});
			} else if (type == 4) {// 新浪微博
				umengShareUtils = new UmengShareUtils(mContext, sharetxt,
						shareurl, sharetitle, imgUrl);
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
	
	
	private void tqDialog() {
		new UserNameAlertDialog(mContext).builder().setTitle("没昵称我  \"蓝瘦\"")
				.setTextViewHint("请填写昵称").setCloseButton(new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).setComplaintsButton("保	 存", new OnClickListener() {
					@Override
					public void onClick(View v) {
						Utils.mLogError("==-->点击了保存 0 cellphone:= "+ spUtil.getString("cellphone", "0")+" userid:= "+spUtil.getInt("userid", 0)+" getUserName:= "+UserNameAlertDialog.getUserName());
						CommUtil.updateUser(spUtil.getString("cellphone", "0"),
								Global.getIMEI(mContext), mContext,
								spUtil.getInt("userid", 0),
								UserNameAlertDialog.getUserName(),null,
								updateUser);
					}
				}).show();
	}
	private AsyncHttpResponseHandler deletePost = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->删除帖子: = "+new String(responseBody));
			dialog.closeDialog();
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0) {
//					Intent intent = new Intent();
//					intent.setAction("android.intent.action.PetCircleInsideActivity");
//					intent.putExtra("position", ClickPosition);
//					intent.putExtra("indexTag", 1);
//					mContext.sendBroadcast(intent);
					PetCircleInsideActivity.deltePost(ClickPosition);
//					notifyDataSetChanged();
					ClickPosition = -1;
				}else {
					if (object.has("msg")&&!object.isNull("msg")) {
						ToastUtil.showToastShortCenter(mContext, object.getString("msg"));
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
	};
	private AsyncHttpResponseHandler updateUser = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->点击了保存 1");
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					ToastUtil.showToastShortCenter(mContext, "创建成功");
					spUtil.saveString("username",UserNameAlertDialog.getUserName());
				}else {
					String msg = jsonObject.getString("msg");
					ToastUtil.showToastShortCenter(mContext, msg);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.mLogError("==-->点击了保存 2 "+e.getMessage());
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
		
	};

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

}
