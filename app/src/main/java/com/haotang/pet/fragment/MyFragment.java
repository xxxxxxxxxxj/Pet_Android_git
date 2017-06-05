package com.haotang.pet.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.pet.ADActivity;
import com.haotang.pet.ChangeAccountActivity;
import com.haotang.pet.CommonAddressActivity;
import com.haotang.pet.FeedBackActivity;
import com.haotang.pet.LoginActivity;
import com.haotang.pet.MainActivity;
import com.haotang.pet.MoreAboutActivity;
import com.haotang.pet.MyCardsActivity;
import com.haotang.pet.MyCouponActivity;
import com.haotang.pet.MyEvaluateActivity;
import com.haotang.pet.MyLastMoney;
import com.haotang.pet.PetAddActivity;
import com.haotang.pet.PetInfoDeatilActivity;
import com.haotang.pet.PostUserInfoActivity;
import com.haotang.pet.R;
import com.haotang.pet.UserMemberActivity;
import com.haotang.pet.adapter.CustomerPetAdapter;
import com.haotang.pet.adapter.MemberPrivilegeAdapter;
import com.haotang.pet.entity.AllLevel;
import com.haotang.pet.entity.ExtraMenusCodeBean;
import com.haotang.pet.entity.ExtraMenusCodeBean.DataBean;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.Privileges;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.FastBlur;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.BanSeekBar;
import com.haotang.pet.view.BanSeekBar.OnBanSeekBarChangeListener;
import com.haotang.pet.view.HorizontalListView;
import com.haotang.pet.view.PullPushLayout;
import com.haotang.pet.view.PullPushLayout.OnTouchEventMoveListenre;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

/**
 * <p>
 * Title:MyFragment
 * </p>
 * <p>
 * Description:我的界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-1-16 下午3:48:03
 */
@SuppressLint("NewApi")
public class MyFragment extends Fragment implements OnClickListener,
		OnTouchListener {
	private MainActivity mainActivity;
	private SelectableRoundedImageView imageView_myfragment_icon;
	private TextView textView_login_user_name_by_self;
	private SharedPreferenceUtil spUtil;
	private View rootView;
	private TextView tv_myfragment_ljdl;
	private ArrayList<Pet> petlist = new ArrayList<Pet>();
	private MProgressDialog pDialog;
	private String liveTag;
	private String liveUrl;
	private String servicePhone = "";
	private ImageView iv_myfragment_level;
	private String memberIcon;
	private BanSeekBar bsb_myfragment_level;
	private LinearLayout ll_myfragment_czz;
	private TextView tv_myfragment_czz;
	private TextView tv_myfragment_rhsj;
	private GridView gv_myfragment_hytq;
	private LinearLayout ll_myfragment_yue;
	private LinearLayout ll_myfragment_hyzx;
	private LinearLayout ll_myfragment_blqz;
	private LinearLayout ll_myfragment_jyzb;
	private LinearLayout ll_myfragment_yhq;
	private LinearLayout ll_myfragment_yqyl;
	private LinearLayout ll_myfragment_cydz;
	private LinearLayout ll_myfragment_ts;
	private LinearLayout ll_myfragment_myevaluate;
	private LinearLayout ll_myfragment_more;
	private TextView tv_myfragment_yue;
	private TextView tv_myfragment_addpet;
	private HorizontalListView hlv_myfragment_pet;
	private RelativeLayout rl_myfragment_userljdl;
	private ImageView iv_myfragment_edituserinfo;
	private TextView tv_myfragment_jyzb;
	private TextView tv_myfragment_yhqnum;
	private String petCardUrl;
	private String inviteUrl;
	private LinearLayout ll_myfragment_userinfo;
	private ArrayList<Privileges> privilegesList = new ArrayList<Privileges>();
	private RelativeLayout rl_myfragment_hytq;
	private ImageView iv_myfragment_topbg;
	private PullPushLayout ppl_myfragment;
	private RelativeLayout rl_myfragment_title;
	private Drawable bgNavBarDrawable;
	private int alphaMax = 180;
	private ArrayList<AllLevel> allLevelList = new ArrayList<AllLevel>();
	private String memberLevelIntroUrl;
	private TextView tv_myfragment_blz;
	private int MemberLevelId;
	private int growthValue;
	private int max;
	private TextView tv_fragment_title;
	private ImageView iv_myfragment_top_bg;
	private LinearLayout ll_myfragment_mypackagecard;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mainActivity = (MainActivity) activity;
		spUtil = SharedPreferenceUtil.getInstance(mainActivity);
		pDialog = new MProgressDialog(mainActivity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null)
			rootView = inflater.inflate(R.layout.myfragment, null);
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		rootView.setOnTouchListener(this);
		intiView(rootView);
		initLinster();
		return rootView;
	}

	private void setView() {
	}

	private void initLinster() {
		ll_myfragment_mypackagecard.setOnClickListener(this);
		ll_myfragment_myevaluate.setOnClickListener(this);
		iv_myfragment_edituserinfo.setOnClickListener(this);
		tv_myfragment_rhsj.setOnClickListener(this);
		ll_myfragment_yue.setOnClickListener(this);
		ll_myfragment_hyzx.setOnClickListener(this);
		ll_myfragment_blqz.setOnClickListener(this);
		ll_myfragment_jyzb.setOnClickListener(this);
		ll_myfragment_yhq.setOnClickListener(this);
		ll_myfragment_yqyl.setOnClickListener(this);
		ll_myfragment_cydz.setOnClickListener(this);
		ll_myfragment_ts.setOnClickListener(this);
		ll_myfragment_more.setOnClickListener(this);
		tv_myfragment_addpet.setOnClickListener(this);
		rl_myfragment_hytq.setOnClickListener(this);
		rl_myfragment_userljdl.setOnClickListener(this);
		gv_myfragment_hytq.bringToFront();
		gv_myfragment_hytq.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(mainActivity, UserMemberActivity.class));
			}
		});
		hlv_myfragment_pet.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					UmengStatistics.UmengEventStatistics(mainActivity,
							Global.UmengEventID.click_My_PetHome);
					Pet pet = petlist.get(position);
					Intent intent = new Intent();
					intent.setClass(mainActivity, PetInfoDeatilActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("pet", pet);
					intent.putExtras(bundle);
					intent.putExtra("petCardUrl", petCardUrl);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bgNavBarDrawable = rl_myfragment_title.getBackground();
		bgNavBarDrawable.setAlpha(0);
		ppl_myfragment
				.setOnTouchEventMoveListenre(new OnTouchEventMoveListenre() {

					@Override
					public void onSlideUp(int mOriginalHeaderHeight,
							int mHeaderHeight) {
					}

					@Override
					public void onSlideDwon(int mOriginalHeaderHeight,
							int mHeaderHeight) {
					}

					@Override
					public void onSlide(int alpha) {
						int alphaReverse = alphaMax - alpha;
						if (alphaReverse < 0) {
							alphaReverse = 0;
						}
						bgNavBarDrawable.setAlpha(alpha);
					}
				});
	}

	private void intiView(View convertView) {
		imageView_myfragment_icon = (SelectableRoundedImageView) convertView
				.findViewById(R.id.imageView_myfragment_icon);
		textView_login_user_name_by_self = (TextView) convertView
				.findViewById(R.id.textView_login_user_name_by_self);
		tv_myfragment_ljdl = (TextView) convertView
				.findViewById(R.id.tv_myfragment_ljdl);
		iv_myfragment_edituserinfo = (ImageView) convertView
				.findViewById(R.id.iv_myfragment_edituserinfo);
		iv_myfragment_level = (ImageView) convertView
				.findViewById(R.id.iv_myfragment_level);
		bsb_myfragment_level = (BanSeekBar) convertView
				.findViewById(R.id.bsb_myfragment_level);
		ll_myfragment_czz = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_czz);
		tv_myfragment_czz = (TextView) convertView
				.findViewById(R.id.tv_myfragment_czz);
		tv_myfragment_rhsj = (TextView) convertView
				.findViewById(R.id.tv_myfragment_rhsj);
		gv_myfragment_hytq = (GridView) convertView
				.findViewById(R.id.gv_myfragment_hytq);
		ll_myfragment_yue = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_yue);
		ll_myfragment_hyzx = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_hyzx);
		ll_myfragment_blqz = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_blqz);
		ll_myfragment_jyzb = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_jyzb);
		ll_myfragment_yhq = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_yhq);
		ll_myfragment_yqyl = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_yqyl);
		ll_myfragment_cydz = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_cydz);
		ll_myfragment_ts = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_ts);
		ll_myfragment_myevaluate = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_myevaluate);
		ll_myfragment_more = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_more);
		tv_myfragment_yue = (TextView) convertView
				.findViewById(R.id.tv_myfragment_yue);
		tv_myfragment_addpet = (TextView) convertView
				.findViewById(R.id.tv_myfragment_addpet);
		hlv_myfragment_pet = (HorizontalListView) convertView
				.findViewById(R.id.hlv_myfragment_pet);
		rl_myfragment_userljdl = (RelativeLayout) convertView
				.findViewById(R.id.rl_myfragment_userljdl);
		tv_myfragment_jyzb = (TextView) convertView
				.findViewById(R.id.tv_myfragment_jyzb);
		tv_myfragment_yhqnum = (TextView) convertView
				.findViewById(R.id.tv_myfragment_yhqnum);
		ll_myfragment_userinfo = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_userinfo);
		rl_myfragment_hytq = (RelativeLayout) convertView
				.findViewById(R.id.rl_myfragment_hytq);
		iv_myfragment_topbg = (ImageView) convertView
				.findViewById(R.id.iv_myfragment_topbg);
		ppl_myfragment = (PullPushLayout) convertView
				.findViewById(R.id.ppl_myfragment);
		rl_myfragment_title = (RelativeLayout) convertView
				.findViewById(R.id.rl_myfragment_title);
		rl_myfragment_title.bringToFront();
		tv_myfragment_blz = (TextView) convertView
				.findViewById(R.id.tv_myfragment_blz);
		tv_fragment_title = (TextView) convertView
				.findViewById(R.id.tv_fragment_title);
		iv_myfragment_top_bg = (ImageView) convertView
				.findViewById(R.id.iv_myfragment_top_bg);
		ll_myfragment_mypackagecard = (LinearLayout) convertView
				.findViewById(R.id.ll_myfragment_mypackagecard);
		iv_myfragment_top_bg.bringToFront();
		rl_myfragment_userljdl.bringToFront();
		rl_myfragment_hytq.bringToFront();
		bsb_myfragment_level.banClick(true);
		bsb_myfragment_level.banDrag(true);
		bsb_myfragment_level
				.setOnBanSeekBarChangeListener(new OnBanSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
					}
				});
	}

	private void showUserInfo(String username, String imagepath) {
		if (memberIcon != null && !TextUtils.isEmpty(memberIcon)) {
			ImageLoaderUtil.loadImg(memberIcon, iv_myfragment_level,
					R.drawable.icon_self, null);
			iv_myfragment_level.bringToFront();
			iv_myfragment_level.setVisibility(View.VISIBLE);
		} else {
			iv_myfragment_level.setVisibility(View.GONE);
		}
		if (username != null && !"".equals(username.trim())) {
			textView_login_user_name_by_self.setText(username);
		} else {
			String str = spUtil.getString("cellphone", "").substring(3, 7);
			textView_login_user_name_by_self.setText(spUtil.getString(
					"cellphone", "").replace(str, "****"));
		}
		if (imagepath != null && !TextUtils.isEmpty(imagepath)) {
			ImageLoaderUtil.loadImg(imagepath, imageView_myfragment_icon,
					R.drawable.user_icon_unnet, null);
		} else {
			imageView_myfragment_icon
					.setImageResource(R.drawable.user_icon_unnet);
		}
		iv_myfragment_topbg
				.setBackgroundResource(R.drawable.bg_myfragment_topimg);
		ImageLoaderUtil.loadImg(imagepath, iv_myfragment_topbg,
				R.drawable.cus_petinfo_back, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
					}

					@Override
					public void onLoadingComplete(String path, View view,
							Bitmap bitmap) {
						if (path != null && bitmap != null) {
							ImageView iv = (ImageView) view;
							int scaleRatio = 3;
							int blurRadius = 8;
							Bitmap scaledBitmap = Bitmap.createScaledBitmap(
									bitmap, bitmap.getWidth() / scaleRatio,
									bitmap.getHeight() / scaleRatio, false);
							Bitmap bip = FastBlur.doBlur(scaledBitmap,
									blurRadius, true);
							iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
							iv.setImageBitmap(bip);
						}
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		setView();
		bgNavBarDrawable.setAlpha(0);
		bsb_myfragment_level.setProgress(0);
		tv_myfragment_czz.setText(String.valueOf(0));
		iv_myfragment_level.setVisibility(View.GONE);
		ImageLoaderUtil.loadImg("drawable://" + R.drawable.cus_petinfo_back,
				iv_myfragment_topbg, R.drawable.cus_petinfo_back, null);
		ImageLoaderUtil.loadImg("drawable://" + R.drawable.user_icon_unnet,
				imageView_myfragment_icon, R.drawable.user_icon_unnet, null);
		tv_myfragment_yue.setText("¥0.0");
		tv_myfragment_yhqnum.setText("0");
		hlv_myfragment_pet.setVisibility(View.GONE);
		MobclickAgent.onPageStart("MyFragment"); // 统计页面
		CommUtil.loadExtraMenus(mainActivity,
				spUtil.getString("cellphone", ""), loadExtraMenusHanler);
		CommUtil.loadMenuNames(mainActivity, spUtil.getString("cellphone", ""),
				loadMenuNamesHanler);
		CommUtil.getH5Url(mainActivity, h5Handler);
		if (Utils.checkLogin(mainActivity)) {
			tv_myfragment_yue.setVisibility(View.VISIBLE);
			tv_myfragment_yhqnum.setVisibility(View.VISIBLE);
			SpannableString ss = new SpannableString("如何升级>");
			// 用下划线标记文本
			ss.setSpan(new UnderlineSpan(), 0, ss.length(),
					Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			tv_myfragment_rhsj.setText(ss);
			ll_myfragment_userinfo.setVisibility(View.VISIBLE);
			// bsb_myfragment_level.setVisibility(View.VISIBLE);
			ll_myfragment_czz.setVisibility(View.VISIBLE);
			tv_myfragment_ljdl.setVisibility(View.GONE);
			// tv_myfragment_blz.setVisibility(View.VISIBLE);
			showDialog();
			// 自动登录
			CommUtil.loginAuto(mainActivity, spUtil.getString("cellphone", ""),
					Global.getIMEI(mainActivity),
					Global.getCurrentVersion(mainActivity),
					spUtil.getInt("userid", 0),
					Double.parseDouble(spUtil.getString("lat_home", "0")),
					Double.parseDouble(spUtil.getString("lng_home", "0")),
					autoLoginHandler);
		} else {
			tv_myfragment_yue.setVisibility(View.INVISIBLE);
			tv_myfragment_yhqnum.setVisibility(View.INVISIBLE);
			privilegesList.clear();
			privilegesList.add(new Privileges(1024, 0, "", "", "会员中心>", ""));
			gv_myfragment_hytq
					.setAdapter(new MemberPrivilegeAdapter<Privileges>(
							mainActivity, privilegesList, 1));
			ll_myfragment_userinfo.setVisibility(View.GONE);
			bsb_myfragment_level.setVisibility(View.GONE);
			ll_myfragment_czz.setVisibility(View.GONE);
			tv_myfragment_ljdl.setVisibility(View.VISIBLE);
			tv_myfragment_blz.setVisibility(View.GONE);
		}
		// 解决自动滚动问题
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ppl_myfragment.fullScroll(ScrollView.FOCUS_UP);
			}
		}, 5);
	}

	private void showDialog() {
		if (!pDialog.isShowing()) {
			pDialog.showDialog();
		}
	}

	private AsyncHttpResponseHandler h5Handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("获取h5url：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jdata = jobj.getJSONObject("data");
					if (jdata.has("invitationConfig")
							&& !jdata.isNull("invitationConfig")) {
						JSONObject ji = jdata.getJSONObject("invitationConfig");
						if (ji.has("url") && !ji.isNull("url")) {
							inviteUrl = ji.getString("url");
						}
					}
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

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MyFragment");
		bgNavBarDrawable.setAlpha(255);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_myfragment_mypackagecard:// 我的卡包
			if (Utils.checkLogin(mainActivity)) {
				startActivity(new Intent(mainActivity, MyCardsActivity.class));
			} else {
				startActivity(new Intent(mainActivity, LoginActivity.class));
			}
			break;
		case R.id.ll_myfragment_myevaluate:// 我的评价
			UmengStatistics.UmengEventStatistics(mainActivity,
					Global.UmengEventID.click_My_MyEvaluation);
			if (Utils.checkLogin(mainActivity)) {
				startActivity(new Intent(mainActivity, MyEvaluateActivity.class));
			} else {
				startActivity(new Intent(mainActivity, LoginActivity.class));
			}
			// String CurrentPetName = spUtil.getString("petname", "");
			//
			// if (!TextUtils.isEmpty(CurrentPetName)) {
			// startActivity(new Intent(mainActivity,
			// CardsDetailActivity.class));
			// }else {
			// Intent intent = new Intent(mainActivity,
			// ChoosePetActivityNew.class);
			// intent.putExtra("previous", Global.CARD_NOTPET_CHOOSE_PET);
			// startActivity(intent);
			// }
			break;
		case R.id.ll_myfragment_jyzb:
			UmengStatistics.UmengEventStatistics(mainActivity,
					Global.UmengEventID.click_My_FosterLive);
			startActivity(new Intent(mainActivity, ADActivity.class).putExtra(
					"url", liveUrl));
			break;
		case R.id.tv_myfragment_addpet:
			UmengStatistics.UmengEventStatistics(mainActivity,
					Global.UmengEventID.click_My_AddMyPet);
			if (Utils.checkLogin(mainActivity)) {
				if (petlist.size() > 20) {
					ToastUtil
							.showToastShortCenter(mainActivity, "最多支持添加20只宝贝呦");
				} else if (petlist.size() < 20) {
					startActivity(new Intent(mainActivity, PetAddActivity.class)
							.putExtra("previous", Global.MY_TO_ADDPET));
				}
			} else {
				startActivity(new Intent(mainActivity, LoginActivity.class));
			}
			break;
		case R.id.rl_myfragment_userljdl:
			if (Utils.checkLogin(mainActivity)) {
				startActivity(new Intent(mainActivity,
						PostUserInfoActivity.class).putExtra("userId",
						spUtil.getInt("userid", 0)));
			} else {
				startActivity(new Intent(mainActivity, LoginActivity.class));
			}
			break;
		case R.id.ll_myfragment_blqz:// 办理狗证
			UmengStatistics.UmengEventStatistics(mainActivity,
					Global.UmengEventID.click_My_DogCard);
			startActivity(new Intent(mainActivity, ADActivity.class).putExtra(
					"url", petCardUrl));
			break;
		case R.id.ll_myfragment_yue:
			startActivity(new Intent(mainActivity, MyLastMoney.class));
			UmengStatistics.UmengEventStatistics(mainActivity,
					Global.UmengEventID.click_My_MyBalance);
			break;
		case R.id.ll_myfragment_yhq:
			UmengStatistics.UmengEventStatistics(mainActivity,
					Global.UmengEventID.click_My_MyCoupon);
			startActivity(new Intent(mainActivity, MyCouponActivity.class));
			break;
		case R.id.ll_myfragment_cydz:// 常用地址
			UmengStatistics.UmengEventStatistics(mainActivity,
					Global.UmengEventID.click_My_CommonAddress);
			if (Utils.checkLogin(mainActivity)) {
				startActivity(new Intent(mainActivity,
						CommonAddressActivity.class).putExtra("index", 1));
			} else {
				startActivity(new Intent(mainActivity, LoginActivity.class));
			}
			break;
		case R.id.ll_myfragment_ts:
			startActivity(new Intent(mainActivity, FeedBackActivity.class));
			break;
		case R.id.ll_myfragment_more:
			UmengStatistics.UmengEventStatistics(mainActivity,
					Global.UmengEventID.click_My_More);
			startActivity(new Intent(mainActivity, MoreAboutActivity.class)
					.putExtra("servicePhone", servicePhone));
			break;
		case R.id.ll_myfragment_yqyl:
			UmengStatistics.UmengEventStatistics(mainActivity,
					Global.UmengEventID.click_My_InvitationCourtesy);
			startActivity(new Intent(mainActivity, ADActivity.class).putExtra(
					"url", inviteUrl).putExtra("previous",
					Global.MYFRAGMENT_INVITESHARE));
			break;
		case R.id.iv_myfragment_edituserinfo:
			startActivity(new Intent(mainActivity, ChangeAccountActivity.class));
			break;
		case R.id.ll_myfragment_hyzx:// 会员中心
			UmengStatistics.UmengEventStatistics(mainActivity,
					Global.UmengEventID.click_My_MemberCenter);
			startActivity(new Intent(mainActivity, UserMemberActivity.class));
			break;
		case R.id.tv_myfragment_rhsj:// 如何升级
			startActivity(new Intent(mainActivity, ADActivity.class).putExtra(
					"url", memberLevelIntroUrl));
			break;
		case R.id.rl_myfragment_hytq:// 会员中心
			startActivity(new Intent(mainActivity, UserMemberActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return true;
	}

	private AsyncHttpResponseHandler getMoney = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					if (jsonObject.has("data") && !jsonObject.isNull("data")) {
						JSONObject object = jsonObject.getJSONObject("data");
						if (object.has("balance") && !object.isNull("balance")) {
							tv_myfragment_yue.setText("¥"
									+ Utils.formatDouble(
											object.getDouble("balance"), 2)
									+ "");
						}
						if (object.has("totalCoupons")
								&& !object.isNull("totalCoupons")) {
							tv_myfragment_yhqnum.setText(object
									.getInt("totalCoupons") + "");
						}
					}
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

	private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			closeDialog();
			Utils.mLogError("获取头像：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				String msg = jobj.getString("msg");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						if (jData.has("user") && !jData.isNull("user")) {
							JSONObject jUser = jData.getJSONObject("user");
							if (jUser.has("areacode")
									&& !jUser.isNull("areacode")) {
								spUtil.saveInt("upRegionId",
										jUser.getInt("areacode"));
							} else {
								spUtil.removeData("upRegionId");
							}
							if (jUser.has("shopName")
									&& !jUser.isNull("shopName")) {
								spUtil.saveString("upShopName",
										jUser.getString("shopName"));
							} else {
								spUtil.removeData("upShopName");
							}
							if (jUser.has("shopId") && !jUser.isNull("shopId")) {
								spUtil.saveInt("shopid", jUser.getInt("shopId"));
							} else {
								spUtil.removeData("shopid");
							}
							if (jUser.has("areaId") && !jUser.isNull("areaId")) {
								spUtil.saveInt("areaid", jUser.getInt("areaId"));
							} else {
								spUtil.removeData("areaid");
							}
							if (jUser.has("areaName")
									&& !jUser.isNull("areaName")) {
								spUtil.saveString("areaname",
										jUser.getString("areaName"));
							} else {
								spUtil.removeData("areaname");
							}
							if (jUser.has("homeAddress")
									&& !jUser.isNull("homeAddress")) {
								JSONObject jAddr = jUser
										.getJSONObject("homeAddress");
								if (jAddr.has("Customer_AddressId")
										&& !jAddr.isNull("Customer_AddressId")) {
									spUtil.saveInt("addressid",
											jAddr.getInt("Customer_AddressId"));
								}
								if (jAddr.has("lat") && !jAddr.isNull("lat")) {
									spUtil.saveString("lat",
											jAddr.getDouble("lat") + "");
								}
								if (jAddr.has("lng") && !jAddr.isNull("lng")) {
									spUtil.saveString("lng",
											jAddr.getDouble("lng") + "");
								}
								if (jAddr.has("address")
										&& !jAddr.isNull("address")) {
									spUtil.saveString("address",
											jAddr.getString("address"));
								}
							} else {
								spUtil.removeData("addressid");
								spUtil.removeData("lat");
								spUtil.removeData("lng");
								spUtil.removeData("address");
							}

							if (jUser.has("pet") && !jUser.isNull("pet")) {
								JSONObject jPet = jUser.getJSONObject("pet");
								if (jPet.has("isCerti")
										&& !jPet.isNull("isCerti")) {
									spUtil.saveInt("isCerti",
											jPet.getInt("isCerti"));
								}
							} else {
								spUtil.removeData("isCerti");
							}
							if (jUser.has("allLevel")
									&& !jUser.isNull("allLevel")) {
								JSONArray allLevel = jUser
										.getJSONArray("allLevel");
								if (allLevel.length() > 0) {
									allLevelList.clear();
									for (int i = 0; i < allLevel.length(); i++) {
										allLevelList.add(AllLevel
												.json2Entity(allLevel
														.getJSONObject(i)));
									}
								}
							}
							if (jUser.has("memberLevelIntroUrl")
									&& !jUser.isNull("memberLevelIntroUrl")) {
								memberLevelIntroUrl = jUser
										.getString("memberLevelIntroUrl");
							}
							if (jUser.has("growthValue")
									&& !jUser.isNull("growthValue")) {
								growthValue = jUser.getInt("growthValue");
								tv_myfragment_czz.setText(String
										.valueOf(growthValue));
							}
							if (jUser.has("userMemberLevel")
									&& !jUser.isNull("userMemberLevel")) {
								JSONObject userMemberLevel = jUser
										.getJSONObject("userMemberLevel");
								if (userMemberLevel.has("memberIcon")
										&& !userMemberLevel
												.isNull("memberIcon")) {
									memberIcon = userMemberLevel
											.getString("memberIcon");
								}
								if (userMemberLevel.has("privileges")
										&& !userMemberLevel
												.isNull("privileges")) {
									JSONArray privilegesArray = userMemberLevel
											.getJSONArray("privileges");
									if (privilegesArray != null
											&& privilegesArray.length() > 0) {
										privilegesList.clear();
										for (int i = 0; i < privilegesArray
												.length(); i++) {
											privilegesList
													.add(Privileges
															.json2Entity(privilegesArray
																	.getJSONObject(i)));
										}
										if (privilegesList.size() > 3) {
											List<Privileges> subList = privilegesList
													.subList(0, 3);
											subList.add(new Privileges(1024, 0,
													"", "", "更多特权>", ""));
											gv_myfragment_hytq
													.setAdapter(new MemberPrivilegeAdapter<Privileges>(
															mainActivity,
															subList, 1));
										} else {
											privilegesList.add(new Privileges(
													1024, 0, "", "", "更多特权>",
													""));
											gv_myfragment_hytq
													.setAdapter(new MemberPrivilegeAdapter<Privileges>(
															mainActivity,
															privilegesList, 1));
										}
									}
								}
								if (userMemberLevel.has("MemberLevelId")
										&& !userMemberLevel
												.isNull("MemberLevelId")) {
									MemberLevelId = userMemberLevel
											.getInt("MemberLevelId");
								}
							}
							String imagepath = null;
							if (jUser.has("avatarPath")
									&& !jUser.isNull("avatarPath")) {
								imagepath = CommUtil.getServiceNobacklash()
										+ jUser.getString("avatarPath");
							}
							String username = null;
							if (jUser.has("userName")
									&& !jUser.isNull("userName")) {
								username = jUser.getString("userName");
							}
							showUserInfo(username, imagepath);
						}
						if (allLevelList.size() > 0) {
							if (MemberLevelId == 0) {
								max = Integer
										.parseInt(allLevelList.get(0).growthValue);
							} else {
								for (int j = 0; j < allLevelList.size(); j++) {
									AllLevel allLevel = allLevelList.get(j);
									if (allLevel != null) {
										if (MemberLevelId == allLevel.MemberLevelId) {
											if (j == allLevelList.size() - 1) {
												max = Integer
														.parseInt(allLevel.growthValue);
												break;
											} else {
												if (allLevelList.size() > j + 1) {
													AllLevel allLevel2 = allLevelList
															.get(j + 1);
													if (allLevel2 != null) {
														max = Integer
																.parseInt(allLevel2.growthValue);
														break;
													}
												}
											}
										}
									}
								}
							}
						}
						bsb_myfragment_level.setProgress(growthValue);
						bsb_myfragment_level.setMax(max);
						tv_myfragment_blz.setText(growthValue + "/" + max);
						if (privilegesList.size() <= 0) {
							privilegesList.clear();
							privilegesList.add(new Privileges(1024, 0, "", "",
									"会员中心>", ""));
							gv_myfragment_hytq
									.setAdapter(new MemberPrivilegeAdapter<Privileges>(
											mainActivity, privilegesList, 1));
						}
					}
					// 获取用户余额和优惠券信息
					CommUtil.getUserAccountBalance(
							spUtil.getString("cellphone", ""),
							Global.getIMEI(mainActivity), mainActivity,
							getMoney);
					// 获取我的宠物信息
					CommUtil.queryCustomerPets(mainActivity,
							spUtil.getString("cellphone", ""),
							Global.getCurrentVersion(mainActivity),
							Global.getIMEI(mainActivity), petHandler);
				} else if (999 == resultCode) {
					removeDataSp();
					bgNavBarDrawable.setAlpha(0);
					bsb_myfragment_level.setProgress(0);
					tv_myfragment_czz.setText(String.valueOf(0));
					iv_myfragment_level.setVisibility(View.GONE);
					ImageLoaderUtil.loadImg("drawable://"
							+ R.drawable.cus_petinfo_back, iv_myfragment_topbg,
							R.drawable.cus_petinfo_back, null);
					ImageLoaderUtil.loadImg("drawable://"
							+ R.drawable.user_icon_unnet,
							imageView_myfragment_icon,
							R.drawable.user_icon_unnet, null);
					tv_myfragment_yue.setText("¥0.0");
					tv_myfragment_yhqnum.setText("0");
					hlv_myfragment_pet.setVisibility(View.GONE);

					tv_myfragment_yue.setVisibility(View.INVISIBLE);
					tv_myfragment_yhqnum.setVisibility(View.INVISIBLE);
					privilegesList.clear();
					privilegesList.add(new Privileges(1024, 0, "", "", "会员中心>",
							""));
					gv_myfragment_hytq
							.setAdapter(new MemberPrivilegeAdapter<Privileges>(
									mainActivity, privilegesList, 1));
					ll_myfragment_userinfo.setVisibility(View.GONE);
					bsb_myfragment_level.setVisibility(View.GONE);
					ll_myfragment_czz.setVisibility(View.GONE);
					tv_myfragment_ljdl.setVisibility(View.VISIBLE);
					tv_myfragment_blz.setVisibility(View.GONE);

					ToastUtil.showToastShortBottom(mainActivity, msg);
				} else {
					ToastUtil.showToastShortBottom(mainActivity, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// 解决自动滚动问题
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					ppl_myfragment.fullScroll(ScrollView.FOCUS_UP);
				}
			}, 5);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			closeDialog();
		}
	};

	private void removeDataSp() {
		spUtil.removeData("upShopName");
		spUtil.removeData("upRegionId");
		spUtil.removeData("isCerti");
		spUtil.removeData("city");
		spUtil.removeData("cellphone");
		spUtil.removeData("userid");
		spUtil.removeData("username");
		spUtil.removeData("userimage");
		spUtil.removeData("payway");
		spUtil.removeData("petid");
		spUtil.removeData("petkind");
		spUtil.removeData("petname");
		spUtil.removeData("petimage");
		spUtil.removeData("addressid");
		spUtil.removeData("lat");
		spUtil.removeData("lng");
		spUtil.removeData("address");
		spUtil.removeData("serviceloc");
		spUtil.removeData("shopid");
		spUtil.removeData("newshopid");
		spUtil.removeData("newaddr");
		spUtil.removeData("newlat");
		spUtil.removeData("newlng");
		spUtil.removeData("invitecode");
	}

	private AsyncHttpResponseHandler petHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			closeDialog();
			Utils.mLogError("获取宠物：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						if (jdata.has("pets") && !jdata.isNull("pets")) {
							hlv_myfragment_pet.setVisibility(View.VISIBLE);
							petlist.clear();
							JSONArray jpets = jdata.getJSONArray("pets");
							for (int i = 0; i < jpets.length(); i++) {
								petlist.add(Pet.json2Entity(jpets
										.getJSONObject(i)));
							}
							hlv_myfragment_pet
									.setAdapter(new CustomerPetAdapter(
											mainActivity, petlist));
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			closeDialog();
		}
	};

	private void closeDialog() {
		if (pDialog.isShowing()) {
			pDialog.closeDialog();
		}
	}

	private AsyncHttpResponseHandler loadExtraMenusHanler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			String result = new String(responseBody);
			processData(result);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
	};

	private AsyncHttpResponseHandler loadMenuNamesHanler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						if (jData.has("servicePhone")
								&& !jData.isNull("servicePhone")) {
							servicePhone = jData.getString("servicePhone");
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

	// 解析json数据
	private void processData(String result) {
		try {
			Gson gson = new Gson();
			ExtraMenusCodeBean extraMenusCodeBean = gson.fromJson(result,
					ExtraMenusCodeBean.class);
			if (extraMenusCodeBean != null) {
				int code = extraMenusCodeBean.getCode();
				List<DataBean> data = extraMenusCodeBean.getData();
				String msg = extraMenusCodeBean.getMsg();
				if (code == 0) {
					if (data != null && data.size() > 0) {
						for (int i = 0; i < data.size(); i++) {
							DataBean dataBean = data.get(i);
							if (dataBean.getText().equals("办理狗证")) {
								petCardUrl = dataBean.getUrl();
							}
							if (dataBean.getText().equals("我的寄养直播")) {
								liveTag = dataBean.getTag();
								if (liveTag != null
										&& !TextUtils.isEmpty(liveTag)) {
									tv_myfragment_jyzb.setText(liveTag.trim());
								} else {
									tv_myfragment_jyzb.setText("寄养直播");
								}
								liveUrl = dataBean.getUrl();
							}
						}
					}
				} else {
					if (msg != null && !TextUtils.isEmpty(msg)) {
						ToastUtil.showToastShortCenter(mainActivity, msg);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
