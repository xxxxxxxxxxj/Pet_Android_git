package com.haotang.pet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CustomerPetResonAdapter;
import com.haotang.pet.entity.CertiOrder;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MListview;
import com.haotang.pet.view.SelectableRoundedImageView;

public class CustomerPetInfoActivity extends SuperActivity implements
		OnClickListener {
	public static SuperActivity act;
	private SelectableRoundedImageView iv_cus_pet_petimg;
	private ImageView iv_cus_pet_member;
	private TextView tv_cus_pet_petname;
	private ImageView iv_cus_pet_petsex;
	private TextView tv_cus_pet_petno;
	private TextView tv_cus_pet_status;
	private RelativeLayout rl_cus_pet_gotobz;
	private LinearLayout ll_cus_pet_xz;
	private LinearLayout ll_cus_pet_mr;
	private LinearLayout ll_cus_pet_jy;
	private LinearLayout ll_cus_pet_yy;
	private TextView tv_cus_pet_xzcount;
	private TextView tv_cus_pet_mrcount;
	private TextView tv_cus_pet_jycount;
	private TextView tv_cus_pet_yycount;
	private TextView tv_cus_pet_mlz;
	private TextView tv_cus_pet_qjz;
	private ProgressBar pb_cus_pet_mlz;
	private ProgressBar pb_cus_pet_qjz;
	private RelativeLayout rl_cus_pet_reson;
	private MListview lv_cus_pet_reson;
	private int customerpetid;
	private SharedPreferenceUtil spUtil;
	private ImageView iv_cus_pet_mode;
	private TextView tv_cus_pet_mode;
	private TextView tv_cus_pet_petbh;
	private ImageView iv_cus_pet_dj;
	private int petKind;
	private int petId;
	private String nickName;
	private ArrayList<Pet> petlist = new ArrayList<Pet>();
	private MProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_pet_info);
		mDialog = new MProgressDialog(this);
		act = this;
		findView();
		init();
		getData();
	}

	@Override
	protected void onResume() {
		petlist.clear();
		super.onResume();
		mDialog.showDialog();
		CommUtil.queryCustomerPets(this,spUtil.getString("cellphone", ""),
				Global.getCurrentVersion(this), Global.getIMEI(this),
				petHandler);
	}

	private AsyncHttpResponseHandler petHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			mDialog.closeDialog();
			Utils.mLogError("获取宠物：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode && jobj.has("data") && !jobj.isNull("data")) {
					JSONObject jdata = jobj.getJSONObject("data");
					if (jdata.has("pets") && !jdata.isNull("pets")) {
						JSONArray jpets = jdata.getJSONArray("pets");
						petsize = jpets.length();
						for (int i = 0; i < jpets.length(); i++) {
							petlist.add(Pet.json2Entity(jpets.getJSONObject(i)));
						}
						Pet pet2 = petlist.get(currentpetindex);
						processData(pet2);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			mDialog.closeDialog();
		}
	};

	private void init() {
		spUtil = SharedPreferenceUtil.getInstance(this);
	}

	private String availServiceTypes;
	private CertiOrder certiOrder;
	private Pet pet;
	private String avatarPath;
	private ImageButton ib_cus_pet_back;
	private ImageButton ib_cus_pet_other;
	private int petsize;
	private String statusName;
	private String certiNo = "";
	private int currentpetindex;
	private String certiUrl;
	private int certiId;
	private int certiDogSize;

	// 解析json数据
	private void processData(Pet pet) {
		if (pet != null) {
			customerpetid = pet.customerpetid;
			nickName = pet.nickName;
			int sex = pet.sex;
			certiNo = pet.certiNo;
			avatarPath = pet.image;
			int sumBath = pet.bathnum;
			int sumCos = pet.beautynum;
			int sumFos = pet.fosternum;
			int swimCount = pet.sumSpecial;
			int cleanGrade = pet.cleannum;
			int beautyGrade = pet.charmnum;
			int emotion = pet.mood;
			String gradeTxt = pet.cleanhint;
			petKind = pet.kindid;
			petId = pet.id;
			certiOrder = pet.certiOrder;
			availServiceTypes = pet.availServiceType;
			certiUrl = pet.certiUrl;
			certiDogSize = pet.certiDogSize;
			Utils.setStringText(tv_cus_pet_petname, nickName);
			Utils.setStringText(tv_cus_pet_mode, gradeTxt);
			String certiOrderStatus = pet.certiOrderStatus;
			if (certiOrder != null) {
				certiId = certiOrder.certiId;
				int status = certiOrder.status;
				String[] statusNames = certiOrder.statusName;
				if (statusNames != null && statusNames.length > 0) {
					if (status < statusNames.length) {
						statusName = statusNames[status];
					}
				} else {
					statusName = certiOrderStatus;
				}
			} else {
				statusName = certiOrderStatus;
			}
			Utils.setStringText(tv_cus_pet_status, statusName);
			if (avatarPath != null && !TextUtils.isEmpty(avatarPath)) {
				ImageLoaderUtil.loadImg(avatarPath,iv_cus_pet_petimg, 
						R.drawable.user_icon_unnet,null);
			}
			if (beautyGrade > 0) {
				tv_cus_pet_mlz.setVisibility(View.VISIBLE);
				tv_cus_pet_mlz.setText(getStr(beautyGrade));
			} else {
				tv_cus_pet_mlz.setVisibility(View.INVISIBLE);
			}
			if (cleanGrade > 0) {
				tv_cus_pet_qjz.setVisibility(View.VISIBLE);
				tv_cus_pet_qjz.setText(getStr(cleanGrade));
			} else {
				tv_cus_pet_qjz.setVisibility(View.INVISIBLE);
			}
			pb_cus_pet_mlz.setProgress(beautyGrade);
			pb_cus_pet_qjz.setProgress(cleanGrade);
			tv_cus_pet_xzcount.setText(setStrColor(sumBath));
			tv_cus_pet_mrcount.setText(setStrColor(sumCos));
			tv_cus_pet_jycount.setText(setStrColor(sumFos));
			tv_cus_pet_yycount.setText(setStrColor(swimCount));
			if (sex == 1) {// 男
				iv_cus_pet_petsex
						.setBackgroundResource(R.drawable.cus_petinfo_men);
			} else {
				iv_cus_pet_petsex
						.setBackgroundResource(R.drawable.cus_petinfo_women);
			}
			setVisOrInVis();
			if (emotion == 1) {
				iv_cus_pet_mode.setBackgroundResource(R.drawable.icon_mood_1);
			} else if (emotion == 2) {
				iv_cus_pet_mode.setBackgroundResource(R.drawable.icon_mood_2);
			} else if (emotion == 3) {
				iv_cus_pet_mode.setBackgroundResource(R.drawable.icon_mood_3);
			} else if (emotion == 4) {
				iv_cus_pet_mode.setBackgroundResource(R.drawable.icon_mood_4);
			} else {
				iv_cus_pet_mode.setBackgroundResource(R.drawable.icon_mood_1);
			}
		}
	}

	private void setVisOrInVis() {
		if (petKind == 2) {// 猫
			tv_cus_pet_petbh.setVisibility(View.INVISIBLE);
			tv_cus_pet_petno.setVisibility(View.INVISIBLE);
			rl_cus_pet_gotobz.setVisibility(View.GONE);
			ll_cus_pet_yy.setVisibility(View.GONE);
			iv_cus_pet_member.setVisibility(View.INVISIBLE);
		} else {
			ll_cus_pet_yy.setVisibility(View.VISIBLE);
			if (certiDogSize == 1 || certiDogSize == 2) {// 小型犬
				tv_cus_pet_petbh.setVisibility(View.VISIBLE);
				tv_cus_pet_petno.setVisibility(View.INVISIBLE);
				rl_cus_pet_gotobz.setVisibility(View.VISIBLE);
				iv_cus_pet_member.setVisibility(View.INVISIBLE);
				Utils.setStringText(tv_cus_pet_petbh, statusName);
				iv_cus_pet_dj.setVisibility(View.VISIBLE);
				if (certiOrder != null) {
					switch (certiOrder.status) {
					case 2:// 已认证
						tv_cus_pet_petno.setVisibility(View.VISIBLE);
						iv_cus_pet_member.setVisibility(View.VISIBLE);
						tv_cus_pet_petno.setText(certiNo);
						break;
					case 5:// 已领证
						iv_cus_pet_dj.setVisibility(View.INVISIBLE);
						break;
					case 6:// 未通过
						setReson();
						break;
					default:
						break;
					}
				}
			} else {// 大型犬
				tv_cus_pet_petbh.setVisibility(View.INVISIBLE);
				tv_cus_pet_petno.setVisibility(View.INVISIBLE);
				rl_cus_pet_gotobz.setVisibility(View.GONE);
				iv_cus_pet_member.setVisibility(View.INVISIBLE);
			}
		}
	}

	private void setReson() {
		if (certiOrder != null) {
			String remark = certiOrder.remark;
			if (remark != null && !TextUtils.isEmpty(remark)) {
				List<String> resonList = new ArrayList<String>();
				resonList.clear();
				if (remark.contains("&")) {// 多条
					String[] reson = remark.split("&");
					List<String> asList = Arrays.asList(reson);
					resonList.addAll(asList);
				} else {// 一条
					resonList.add(remark);
				}
				rl_cus_pet_reson.setVisibility(View.VISIBLE);
				lv_cus_pet_reson.setAdapter(new CustomerPetResonAdapter(this,
						resonList));
			}
		}
	}

	private SpannableString setStrColor(int sumBath) {
		String str = "";
		if (sumBath > 999) {
			str = "999+次";
		} else {
			str = sumBath + "次";
		}
		SpannableString ss = new SpannableString(str);
		ss.setSpan(
				new ForegroundColorSpan(getResources()
						.getColor(R.color.a333333)), str.length() - 1, str
						.length(),
				// setSpan时需要指定的
				// flag,Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括).
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		return ss;
	}

	private SpannableString getStr(int beautyGrade) {
		String str = beautyGrade + "分";
		SpannableString ss = new SpannableString(str);
		ss.setSpan(
				new ForegroundColorSpan(getResources()
						.getColor(R.color.a333333)), str.length() - 1, str
						.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		ss.setSpan(new TextAppearanceSpan(this, R.style.style2),
				str.length() - 1, str.length(),
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		return ss;
	}

	private void findView() {
		ib_cus_pet_back = (ImageButton) findViewById(R.id.ib_cus_pet_back);
		ib_cus_pet_other = (ImageButton) findViewById(R.id.ib_cus_pet_other);
		iv_cus_pet_petimg = (SelectableRoundedImageView) findViewById(R.id.iv_cus_pet_petimg);
		iv_cus_pet_member = (ImageView) findViewById(R.id.iv_cus_pet_member);
		tv_cus_pet_petname = (TextView) findViewById(R.id.tv_cus_pet_petname);
		iv_cus_pet_petsex = (ImageView) findViewById(R.id.iv_cus_pet_petsex);
		tv_cus_pet_petno = (TextView) findViewById(R.id.tv_cus_pet_petno);
		tv_cus_pet_status = (TextView) findViewById(R.id.tv_cus_pet_status);
		rl_cus_pet_gotobz = (RelativeLayout) findViewById(R.id.rl_cus_pet_gotobz);
		ll_cus_pet_xz = (LinearLayout) findViewById(R.id.ll_cus_pet_xz);
		ll_cus_pet_mr = (LinearLayout) findViewById(R.id.ll_cus_pet_mr);
		ll_cus_pet_yy = (LinearLayout) findViewById(R.id.ll_cus_pet_yy);
		ll_cus_pet_jy = (LinearLayout) findViewById(R.id.ll_cus_pet_jy);
		tv_cus_pet_xzcount = (TextView) findViewById(R.id.tv_cus_pet_xzcount);
		tv_cus_pet_mrcount = (TextView) findViewById(R.id.tv_cus_pet_mrcount);
		tv_cus_pet_jycount = (TextView) findViewById(R.id.tv_cus_pet_jycount);
		tv_cus_pet_yycount = (TextView) findViewById(R.id.tv_cus_pet_yycount);
		tv_cus_pet_mlz = (TextView) findViewById(R.id.tv_cus_pet_mlz);
		tv_cus_pet_qjz = (TextView) findViewById(R.id.tv_cus_pet_qjz);
		pb_cus_pet_mlz = (ProgressBar) findViewById(R.id.pb_cus_pet_mlz);
		pb_cus_pet_qjz = (ProgressBar) findViewById(R.id.pb_cus_pet_qjz);
		rl_cus_pet_reson = (RelativeLayout) findViewById(R.id.rl_cus_pet_reson);
		lv_cus_pet_reson = (MListview) findViewById(R.id.lv_cus_pet_reson);
		iv_cus_pet_mode = (ImageView) findViewById(R.id.iv_cus_pet_mode);
		tv_cus_pet_mode = (TextView) findViewById(R.id.tv_cus_pet_mode);
		tv_cus_pet_petbh = (TextView) findViewById(R.id.tv_cus_pet_petbh);
		iv_cus_pet_dj = (ImageView) findViewById(R.id.iv_cus_pet_dj);
		ib_cus_pet_back.setOnClickListener(this);
		ib_cus_pet_other.setOnClickListener(this);
		rl_cus_pet_gotobz.setOnClickListener(this);
		ll_cus_pet_xz.setOnClickListener(this);
		ll_cus_pet_mr.setOnClickListener(this);
		ll_cus_pet_jy.setOnClickListener(this);
		ll_cus_pet_yy.setOnClickListener(this);
		iv_cus_pet_petimg.setOnClickListener(this);
	}

	private void getData() {
		Intent intent = getIntent();
		petsize = intent.getIntExtra("petsize", 0);
		currentpetindex = intent.getIntExtra("currentpetindex", 0);
		pet = (Pet) intent.getSerializableExtra("petinfo");
		processData(pet);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_cus_pet_petimg:// 修改或删除宠物信息
			JumpToNextForData(PetAddActivity.class, Global.PETINFO_TO_EDITPET,
					customerpetid, currentpetindex);
			break;
		case R.id.ib_cus_pet_back:// 返回
			finishWithAnimation();
			break;
		case R.id.ib_cus_pet_other:// 添加宠物
			if (petsize >= 20) {
				ToastUtil.showToastShortCenter(this, "最多支持添加20只宝贝呦");
			} else {
				JumpToNextForData(PetAddActivity.class,
						Global.PETINFO_TO_ADDPET, 0, 0);
			}
			break;
		case R.id.rl_cus_pet_gotobz:// 办理狗证
			if (certiOrder != null) {
				switch (certiOrder.status) {
				case 0:// 待支付
					goNextPetCard(OrderPayActivity.class,
							certiOrder.CertiOrderId, 0,
							Global.ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY);
					break;
				case 1:// 已支付-待审核
					goNextPetCard(ADActivity.class, 0, 1, 0);
					break;
				case 2:// 审核通过-已认证
					goNextPetCard(ADActivity.class, 0, 2, 0);
					break;
				case 3:// 邮寄
					goNextPetCard(ADActivity.class, 0, 3, 0);
					break;
				case 4:// 上门取件
					goNextPetCard(ADActivity.class, 0, 4, 0);
					break;
				case 5:// 完成
					break;
				case 6:// 审核不通过
					goNextPetCard(ADActivity.class, 0, 6, 0);
					break;
				case 7:// 未办证
					goNextPetCard(ADActivity.class, 0, 7, 0);
					break;
				default:
					break;
				}
			} else {
				goNextPetCard(ADActivity.class, 0, -1, 0);
			}
			break;
		case R.id.ll_cus_pet_xz:// 洗澡
			if (availServiceTypes.contains("1")) {
				// 带对象跳转至对应界面
				Intent intent = new Intent(this, ServiceActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				intent.putExtra("previous",
						Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
				intent.putExtra("servicetype", 1);
				intent.putExtra("petkind", petKind);
				intent.putExtra("petid", petId);
				if (petKind == 1) {
					intent.putExtra("serviceid", 1);
				} else if (petKind == 2) {
					intent.putExtra("serviceid", 3);
				}
				intent.putExtra("customerpetid", customerpetid);
				intent.putExtra("customerpetname", nickName);
				startActivity(intent);
				// 保存选择的宠物的头像
				spUtil.saveString("petimg", avatarPath);
			}
			break;
		case R.id.ll_cus_pet_mr:// 美容
			if (availServiceTypes.contains("2")) {
				// 带对象跳转至对应界面
				Intent mrintent = new Intent(this, ServiceActivity.class);
				mrintent.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				mrintent.putExtra("previous",
						Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
				mrintent.putExtra("servicetype", 2);
				mrintent.putExtra("petkind", petKind);
				mrintent.putExtra("petid", petId);
				if (petKind == 1) {
					mrintent.putExtra("serviceid", 2);
				} else if (petKind == 2) {
					mrintent.putExtra("serviceid", 4);
				}
				mrintent.putExtra("customerpetid", customerpetid);
				mrintent.putExtra("customerpetname", nickName);
				startActivity(mrintent);
				// 保存选择的宠物的头像
				spUtil.saveString("petimg", avatarPath);
			}
			break;
		case R.id.ll_cus_pet_jy:// 寄养
			// 寄养
			goNext(FostercareChooseroomActivity.class, 100, 100,
					Global.PRE_CUSTOMERPETINFO_TO_FOSTERCAREAPPOINTMENT_PET);
			break;
		case R.id.ll_cus_pet_yy:// 游泳
			goNext(SwimDetailActivity.class, 100, 100,
					Global.SWIM_MYPET_TO_SWIMAPPOINTMENT);
			break;
		default:
			break;
		}
	}

	private void goNextPetCard(Class clazz, int CertiOrderId, int status,
			int previous) {
		Intent adintent = new Intent();
		adintent.setClass(this, clazz);
		String url = "";
		if (status == 1 || status == 2 || status == 3 || status == 4) {
			url = CommUtil.getWebBaseUrl()
					+ "web/petcerti/auditresult?certi_id=" + certiId
					+ "&user_petid=" + customerpetid;
		} else if (status == 6) {
			url = CommUtil.getWebBaseUrl() + "web/petcerti/register?certi_id="
					+ certiId + "&user_petid=" + customerpetid;
		} else if (status == -1 || status == 7) {
			url = certiUrl + "?certi_id=" + certiId + "&user_petid=" + customerpetid;
		}
		adintent.putExtra("url", url);
		adintent.putExtra("previous", previous);
		adintent.putExtra("CertiOrderId", CertiOrderId);
		startActivity(adintent);
	}

	private void goNext(Class clazz, int serviceid, int servicetype,
			int previous) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("previous", previous);
		intent.putExtra("petid", pet.id);
		intent.putExtra("petkind", pet.kindid);
		intent.putExtra("petname", pet.name);
		intent.putExtra("customerpetid", pet.customerpetid);
		intent.putExtra("customerpetname", pet.nickName);
		intent.putExtra("petimage", pet.image);
		startActivity(intent);
	}

	private void JumpToNextForData(Class clazz, int requestcode,
			int customerpetid, int position) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", requestcode);
		intent.putExtra("customerpetid", customerpetid);
		intent.putExtra("position", position);
		intent.putExtra("nickname", pet.nickName);
		Bundle bundle = new Bundle();
		bundle.putSerializable("certiOrder", certiOrder);
		intent.putExtras(bundle);
		startActivityForResult(intent, requestcode);
	}

}
