package com.haotang.pet;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.MyPetAdapter;
import com.haotang.pet.adapter.SortAdapter;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.SortModel;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CharacterParser;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.PinyinComparator;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ClearEditText;
import com.haotang.pet.view.LinearListView;
import com.haotang.pet.view.SideBar;
import com.haotang.pet.view.SideBar.OnTouchingLetterChangedListener;
import com.umeng.analytics.MobclickAgent;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class ChoosePetActivityNew extends SuperActivity {
	private ImageButton ibBack;
	private Button btDog;
	private Button btCat;
	private ClearEditText cetSearch;
	private SideBar sbBar;
	private TextView tvBarHint;
	private LinearListView llvMyPets;
	private ListView lvPets;
	private LinearLayout llMyPets;
	private FrameLayout flMain;
	private RelativeLayout rlNull;
	private TextView tvMsg1;
	private TextView tvMsg2;
	private boolean isCat;
	private int previous;
	private int serviceid;
	private int servicetype;
	private int serviceloc;
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private ArrayList<Pet> mypets = new ArrayList<Pet>();
	private MyPetAdapter myPetAdapter;
	private View vHeader;
	private LayoutInflater mInflater;
	private ArrayList<SortModel> petlist = new ArrayList<SortModel>();
	private SortAdapter petAdapter;
	private int petkind = 1;
	private Intent fIntent;
	private int typeId;
	public static SuperActivity act;
	private int templateId = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosepetnew);
		act = this;
		findView();
		setView();
	}

	private void findView() {
		mInflater = LayoutInflater.from(this);
		ibBack = (ImageButton) findViewById(R.id.ib_choosepet_back);
		btDog = (Button) findViewById(R.id.bt_choosepet_dog);
		btCat = (Button) findViewById(R.id.bt_choosepet_cat);

		cetSearch = (ClearEditText) findViewById(R.id.cet_choosepet_search);
		sbBar = (SideBar) findViewById(R.id.sb_choosepet_sidebar);
		tvBarHint = (TextView) findViewById(R.id.tv_choosepet_hint);

		lvPets = (ListView) findViewById(R.id.lv_choosepet_pets);

		flMain = (FrameLayout) findViewById(R.id.fl_choosepet_main);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvMsg1 = (TextView) findViewById(R.id.tv_null_msg1);
		tvMsg2 = (TextView) findViewById(R.id.tv_null_msg2);

		vHeader = mInflater.inflate(R.layout.choosepetheader, null);
		llMyPets = (LinearLayout) vHeader
				.findViewById(R.id.ll_choosepet_mypets);
		llvMyPets = (LinearListView) vHeader
				.findViewById(R.id.llv_choosepet_mypets);

	}

	private void setView() {
		// TODO Auto-generated method stub
		pDialog = new MProgressDialog(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		fIntent = getIntent();
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		previous = fIntent.getIntExtra("previous", -1);
		serviceid = fIntent.getIntExtra("serviceid", -1);
		servicetype = fIntent.getIntExtra("servicetype", -1);
		serviceloc = fIntent.getIntExtra("servloc", -1);
		typeId = fIntent.getIntExtra("typeId", 0);
		templateId = fIntent.getIntExtra("id", -1);
		if (previous == Global.BEAUTICIAN_TO_APPOINTMENT
				&& !fIntent.getBooleanExtra("isshop", false)
				&& fIntent.getBooleanExtra("ishome", false)) {
			serviceloc = 2;
		}

		isShowOverBurden();

		if (previous == Global.SERVICEFEATURE_TO_PETLIST
				|| fIntent.getIntExtra("tmpflag", 0) == 3
				|| previous == Global.SWIM_DETAIL_CLICK_ONE_TO_CHANGE_PET
				|| previous == Global.MAIN_TO_SWIM_DETAIL
				|| previous == Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET
				|| previous == Global.TRAIN_TO_CHOOSE_PET_PRE
				|| previous == Global.MAIN_TO_TRAIN_DETAIL
				|| previous == Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET
				|| previous == Global.SERVICEFEATURE_TO_PETLIST_1
				||previous ==Global.CARD_CHOOSE_PET||previous ==Global.CARD_NOTPET_CHOOSE_PET) {
			// 从特色服务进入，屏蔽猫的点击
			btCat.setEnabled(true);
			btCat.setTextColor(getResources().getColor(R.color.white));
			btCat.setBackgroundResource(R.drawable.bg_member_right_gray);
		} else if (previous == Global.SWIM_APPOINMENT
				|| previous == Global.SWIM_DETAIL_ADD_PET
				|| previous == Global.SWIM_DETAIL_CLICK_ONE_TO_CHANGE_PET
				|| previous == Global.MAIN_TO_SWIM_DETAIL
				|| previous == Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET
				|| previous == Global.TRAIN_TO_CHOOSE_PET_PRE
				|| previous == Global.MAIN_TO_TRAIN_DETAIL
				|| previous == Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET
				||previous==Global.CARD_CHOOSE_PET||previous ==Global.CARD_NOTPET_CHOOSE_PET) {
			// 游泳进来跟特色服务一样
			btCat.setEnabled(true);
			btCat.setTextColor(getResources().getColor(R.color.white));
			btCat.setBackgroundResource(R.drawable.bg_member_right_gray);
		} else {
			btCat.setEnabled(true);
			btCat.setTextColor(getResources().getColor(R.color.aBB996C));
			btCat.setBackgroundResource(R.drawable.choosepet_right_unselect);
		}
		tvMsg2.setVisibility(View.VISIBLE);
		tvMsg1.setText("汪星人的服务暂未开通哦");
		tvMsg2.setText("敬请期待吧");
		sbBar.setTextView(tvBarHint);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		btDog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isCat) {
					isCat = false;
					petkind = 1;
					btDog.setTextColor(getResources().getColor(R.color.black));
					btCat.setTextColor(getResources().getColor(R.color.aBB996C));
					btDog.setBackgroundResource(R.drawable.choosepet_left_select);
					btCat.setBackgroundResource(R.drawable.choosepet_right_unselect);
					tvMsg1.setText("汪星人的服务暂未开通哦");
					tvMsg2.setText("敬请期待吧");

					if (serviceid == 3)
						serviceid = 1;
					else if (serviceid == 4)
						serviceid = 2;
					petAdapter.setKind(petkind);
					myPetAdapter.setPetKind(petkind);
					getMyPets(petkind);
					if (fIntent.getIntExtra("beautician_id", 0) > 0) {
						getPets(petkind);
					} else {
						getPets(petkind);
					}
				}
			}
		});
		btCat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (previous == Global.SERVICEFEATURE_TO_PETLIST
						|| fIntent.getIntExtra("tmpflag", 0) == 3
						|| previous == Global.SWIM_APPOINMENT
						|| previous == Global.SWIM_DETAIL_ADD_PET
						|| previous == Global.SWIM_DETAIL_CLICK_ONE_TO_CHANGE_PET
						|| previous == Global.MAIN_TO_SWIM_DETAIL
						|| previous == Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET
						|| previous == Global.TRAIN_TO_CHOOSE_PET_PRE
						|| previous == Global.MAIN_TO_TRAIN_DETAIL
						|| previous == Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET
						|| previous == Global.SERVICEFEATURE_TO_PETLIST_1
						||previous == Global.CARD_CHOOSE_PET||previous ==Global.CARD_NOTPET_CHOOSE_PET) {
					ToastUtil.showToastShortCenter(ChoosePetActivityNew.this,
							"该服务暂不支持喵星人哦");
					return;
				}
				if (!isCat) {
					isCat = true;
					petkind = 2;
					btDog.setTextColor(getResources().getColor(R.color.aBB996C));
					btCat.setTextColor(getResources().getColor(R.color.black));
					btDog.setBackgroundResource(R.drawable.choosepet_left_unselect);
					btCat.setBackgroundResource(R.drawable.choosepet_right_select);
					tvMsg1.setText("喵星人的服务暂未开通哦");
					tvMsg2.setText("敬请期待吧");

					if (serviceid == 1)
						serviceid = 3;
					else if (serviceid == 2)
						serviceid = 4;
					petAdapter.setKind(petkind);
					myPetAdapter.setPetKind(petkind);
					getMyPets(petkind);
					if (fIntent.getIntExtra("beautician_id", 0) > 0) {
						getPets(petkind);
					} else {
						getPets(petkind);
					}
				}
			}
		});

		cetSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表

				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		sbBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置

				try {
					int position = petAdapter.getPositionForSection(s.charAt(0));
					if (position != -1) {
						lvPets.setSelection(position);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		myPetAdapter = new MyPetAdapter(this, petkind, mypets);

		llvMyPets.setAdapter(myPetAdapter);
		llvMyPets
				.setOnItemClickListener(new LinearListView.OnItemClickListener() {

					@Override
					public void onItemClick(LinearListView parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Pet pet = (Pet) myPetAdapter.getItem(position);
						// 后台那块给的方案是读取宠物列表里的然后判断-----（具体如果需要立马生效的，需要后台处理）0
						if (petlist.size() > 0) {
							for (int i = 0; i < petlist.size(); i++) {
								SortModel slist = petlist.get(i);
								if (pet.id == slist.getPetId()) {
									pet.serviceHome = slist.getServiceHome();
								}
							}
						}
						if (serviceloc == 2) {// 上门
							if (pet.serviceHome != null) {
								if (pet.serviceHome.length > 0) {
									BitSet bitSet = BitSet
											.valueOf(pet.serviceHome);
									if (!isCat) {// 狗
										Boolean isDogWash = bitSet.get(1);
										Boolean isDogBeau = bitSet.get(2);
										if (isDogWash || isDogBeau) {// 有一个支持就ok
										} else {

											ToastUtil.showToastShortCenter(act,
													"该宠物仅支持到店服务哦~");
											return;
										}
									} else if (isCat) {// 猫
										Boolean isCatWash = bitSet.get(3);
										Boolean isCatBeau = bitSet.get(4);
										if (isCatWash || isCatBeau) {
										} else {
											ToastUtil.showToastShortCenter(act,
													"该宠物仅支持到店服务哦~");
											return;
										}
									}
								} else {
									ToastUtil.showToastShortCenter(act,
											"该宠物仅支持到店服务哦~");
									return;
								}
							}

						}
						goNextMyPet(pet);
					}
				});

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		vHeader.measure(w, h);
		lvPets.addHeaderView(vHeader);
		petAdapter = new SortAdapter(this, petkind, petlist);
		lvPets.setAdapter(petAdapter);

		lvPets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				goNext(position);
			}
		});

		// lvPets.addHeaderView(vHeader, null, true);
		// lvPets.setFooterDividersEnabled(false);

		if (serviceid == 3)
			serviceid = 1;
		else if (serviceid == 4)
			serviceid = 2;

		getMyPets(petkind);
		if (fIntent.getIntExtra("beautician_id", 0) > 0) {
			getPets(petkind);
		} else {
			getPets(petkind);
		}

	}

	private void goNext(int position) {
		SortModel sm = (SortModel) petAdapter.getItem(position);
		if (serviceloc == 2) {// 上门
			if (sm.getServiceHome() != null) {
				if (sm.getServiceHome().length > 0) {
					BitSet bitSet = BitSet.valueOf(sm.getServiceHome());
					if (!isCat) {// 狗
						if (!bitSet.get(1) && !bitSet.get(2)) {
							ToastUtil.showToastShortCenter(act, "该宠物仅支持到店服务哦~");
							return;
						}
					} else if (isCat) {// 猫
						if (!bitSet.get(3) && !bitSet.get(4)) {
							ToastUtil.showToastShortCenter(act, "该宠物仅支持到店服务哦~");
							return;
						}
					}
				} else {
					ToastUtil.showToastShortCenter(act, "该宠物仅支持到店服务哦~");
					return;
				}
			}
		}
		if (previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY) {
			Intent data = new Intent();
			if (spUtil.getInt("tareaid", 0) == 100) {
				data.setClass(this, ShopListActivity.class);
				data.putExtra("previous",
						Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
			} else {
				data.setClass(this, ServiceActivity.class);
				data.putExtra("previous",
						Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
			}
			data.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_LEFT());
			data.putExtra("petid", sm.getPetId());
			data.putExtra("serviceid", serviceid);
			data.putExtra("servicetype", servicetype);
			data.putExtra("petkind", sm.getPetKind());
			data.putExtra("petname", sm.getName());

			data.putExtra("petimage",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
			startActivity(data);
			finishWithAnimation();
			// 保存选择的宠物的头像
			spUtil.saveString("petimg",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
		} else if (previous == Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT) {
			Intent data = new Intent(this, FostercareChooseroomActivity.class);
			data.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_LEFT());
			data.putExtra("petid", sm.getPetId());
			data.putExtra("previous",
					Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT);
			data.putExtra("petkind", sm.getPetKind());
			data.putExtra("petname", sm.getName());
			data.putExtra("serviceid", serviceid);
			data.putExtra("servicetype", servicetype);
			data.putExtra("petimage",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
			startActivity(data);
			finishWithAnimation();
		} else if (previous == Global.APPOINTMENT_TO_ADDPET) {
			Intent data = new Intent(this, MulPetAddServiceActivity.class);
			data.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_LEFT());
			data.putExtra("petid", sm.getPetId());
			data.putExtra("previous", Global.APPOINTMENT_TO_ADDPET);
			data.putExtra("petkind", sm.getPetKind());
			data.putExtra("petname", sm.getName());
			data.putExtra("serviceid", serviceid);
			data.putExtra("servicetype", servicetype);

			data.putExtra("lat", fIntent.getDoubleExtra("lat", 0));
			data.putExtra("lng", fIntent.getDoubleExtra("lng", 0));
			data.putExtra("shopid", fIntent.getIntExtra("shopid", 0));
			data.putExtra("servloc", fIntent.getIntExtra("servloc", 0));
			data.putExtra("beautician_sort",
					fIntent.getIntExtra("beautician_sort", 0));
			data.putExtra("time", fIntent.getStringExtra("time"));
			data.putExtra("petimage",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
			startActivityForResult(data, Global.APPOINTMENT_TO_ADDPET);
		} else if (previous == Global.SERVICEFEATURE_TO_PETLIST) {
			// 从特色服务获取
			Intent data = new Intent();
			if (spUtil.getInt("tareaid", 0) == 100) {
				data.setClass(this, ShopListActivity.class);
				data.putExtra("previous",
						Global.SERVICEFEATURE_TO_PETLIST);
				data.putExtra("typeId", typeId);// 从特色服务跳转到门店列表专用，为了计算价格
			} else {
				data.setClass(this, ServiceFeature.class);
				data.putExtra("previous", Global.SERVICEFEATURE_TO_PETLIST);
			}
			data.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_LEFT());
			data.putExtra("petid", sm.getPetId());
			data.putExtra("petkind", sm.getPetKind());
			data.putExtra("petname", sm.getName());
			data.putExtra("servicename", fIntent.getStringExtra("servicename"));
			data.putExtra("petimage",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
			startActivity(data);
			finishWithAnimation();
			// 保存选择的宠物的头像
			spUtil.saveString("petimg",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
		} else if (previous == Global.BEAUTICIAN_TO_APPOINTMENT) {
			// 从美容师预约
			Intent data = null;
			if (fIntent.getIntExtra("tmpflag", 0) == 3) {
				data = new Intent(this, ServiceFeature.class);
				data.putExtra("servicename",
						fIntent.getIntExtra("servicename", 0));
				data.putExtra("servicetype", fIntent.getIntExtra("servicetype", 0));
			} else {
				data = new Intent(this, ServiceActivity.class);
			}
			data.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_LEFT());
			data.putExtra("petid", sm.getPetId());
			data.putExtra("previous", Global.BEAUTICIAN_TO_APPOINTMENT);
			data.putExtra("petkind", sm.getPetKind());
			data.putExtra("petname", sm.getName());
			data.putExtra("petimage",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
			data.putExtra("isshop", fIntent.getBooleanExtra("isshop", true));
			data.putExtra("ishome", fIntent.getBooleanExtra("ishome", true));
			data.putExtra("areaid", fIntent.getIntExtra("areaid", 0));
			data.putExtra("shopid", fIntent.getIntExtra("shopid", 0));
			data.putExtra("serviceid", fIntent.getIntExtra("serviceid", 0));
			data.putExtra("servicetype", fIntent.getIntExtra("servicetype", 0));
			data.putExtra("servicename", fIntent.getStringExtra("servicename"));

			data.putExtra("areaname", fIntent.getStringExtra("areaname"));
			data.putExtra("beautician_levelname",
					fIntent.getStringExtra("beautician_levelname"));
			data.putExtra("beautician_image",
					fIntent.getStringExtra("beautician_image"));
			data.putExtra("beautician_name",
					fIntent.getStringExtra("beautician_name"));
			data.putExtra("beautician_id",
					fIntent.getIntExtra("beautician_id", 0));
			data.putExtra("beautician_sex",
					fIntent.getIntExtra("beautician_sex", 0));
			data.putExtra("beautician_sort",
					fIntent.getIntExtra("beautician_sort", 10));
			data.putExtra("beautician_ordernum",
					fIntent.getIntExtra("beautician_ordernum", 0));
			data.putExtra("petimage",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
			data.putExtra("upgradeTip", fIntent.getStringExtra("upgradeTip"));
			startActivity(data);
			finishWithAnimation();
			// 保存选择的宠物的头像
			spUtil.saveString("petimg",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
		} else if (previous == Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET) {
			// Intent data = new Intent(this, SwimAppointmentActivity.class);
			Intent data = new Intent(this, SwimDetailActivity.class);
			data.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_LEFT());
			data.putExtra("petid", sm.getPetId());
			data.putExtra("previous",
					Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET);
			data.putExtra("petkind", sm.getPetKind());
			data.putExtra("petname", sm.getName());
			data.putExtra("servicename", fIntent.getStringExtra("servicename"));
			data.putExtra("petimage",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
			startActivity(data);
			finishWithAnimation();
		}else if (previous == Global.CARD_NOTPET_CHOOSE_PET) {
			Intent data = new Intent(this, CardsDetailActivity.class);
			data.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_LEFT());
			data.putExtra("id",fIntent.getIntExtra("id", -1));//这个需要从h5带过来
			data.putExtra("isCerti", sm.getIsCerti());
			data.putExtra("petid", sm.getPetId());
			data.putExtra("previous",Global.CARD_NOTPET_CHOOSE_PET);
			data.putExtra("petkind", sm.getPetKind());
			data.putExtra("petname", sm.getName());
			data.putExtra("servicename", fIntent.getStringExtra("servicename"));
			data.putExtra("petimage",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
			startActivity(data);
			finishWithAnimation();
		}else if (previous == Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET) {
			Intent data = new Intent(this, TrainAppointMentActivity.class);
			data.putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(),
					Global.ANIM_COVER_FROM_LEFT());
			data.putExtra("petid", sm.getPetId());
			data.putExtra("previous",
					Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET);
			data.putExtra("petkind", sm.getPetKind());
			data.putExtra("petname", sm.getName());
			data.putExtra("servicename", fIntent.getStringExtra("servicename"));
			data.putExtra("petimage",
					CommUtil.getServiceNobacklash() + sm.getAvatarPath());
			startActivity(data);
			finishWithAnimation();
		} else {
			if (previous == Global.SWIM_APPOINMENT) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.SwimAppointmentActivity");
				intent.putExtra("petid", sm.getPetId());
				intent.putExtra("petkind", sm.getPetKind());
				intent.putExtra("petname", sm.getName());
				intent.putExtra("serviceid", serviceid);
				intent.putExtra("servicetype", servicetype);
				intent.putExtra("petimage", CommUtil.getServiceNobacklash()
						+ sm.getAvatarPath());
				intent.putExtra("index", 0);
				sendBroadcast(intent);
			} else if (previous == Global.SWIM_DETAIL_ADD_PET) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.SwimDetailActivity");
				intent.putExtra("petid", sm.getPetId());
				intent.putExtra("petkind", sm.getPetKind());
				intent.putExtra("petname", sm.getName());
				intent.putExtra("serviceid", serviceid);
				intent.putExtra("servicetype", servicetype);
				intent.putExtra("petimage", CommUtil.getServiceNobacklash()
						+ sm.getAvatarPath());
				intent.putExtra("index", 0);
				sendBroadcast(intent);
			} else if (previous == Global.SWIM_DETAIL_CLICK_ONE_TO_CHANGE_PET) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.SwimDetailActivity");
				intent.putExtra("petid", sm.getPetId());
				intent.putExtra("petkind", sm.getPetKind());
				intent.putExtra("petname", sm.getName());
				intent.putExtra("serviceid", serviceid);
				intent.putExtra("servicetype", servicetype);
				intent.putExtra("petimage", CommUtil.getServiceNobacklash()
						+ sm.getAvatarPath());
				intent.putExtra("index", 1);
				sendBroadcast(intent);
			} else {
				Intent data = new Intent();
				data.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				data.putExtra("isCerti", sm.getIsCerti());
				data.putExtra("petid", sm.getPetId());
				data.putExtra("petkind", sm.getPetKind());
				data.putExtra("petname", sm.getName());
				data.putExtra("serviceid", serviceid);
				data.putExtra("servicetype", servicetype);
				data.putExtra("petimage",
						CommUtil.getServiceNobacklash() + sm.getAvatarPath());
				setResult(Global.RESULT_OK, data);
				// 保存选择的宠物的头像
				spUtil.saveString("petimg", CommUtil.getServiceNobacklash()
						+ sm.getAvatarPath());
			}
			finishWithAnimation();

		}
	}

	private void goNextMyPet(Pet pet) {
		if (pet.sa == 0) {
			ToastUtil.showToastShortCenter(this, "该宝贝还不能享受此项服务呦");
		} else {
			if (previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY) {
				Intent data = new Intent();
				if (spUtil.getInt("tareaid", 0) == 100) {
					data.setClass(this, ShopListActivity.class);
					data.putExtra("previous",
							Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
				} else {
					data.setClass(this, ServiceActivity.class);
					data.putExtra("previous",
							Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
				}
				data.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				data.putExtra("petid", pet.id);
				data.putExtra("serviceid", serviceid);
				data.putExtra("servicetype", servicetype);
				data.putExtra("petkind", pet.kindid);
				data.putExtra("petname", pet.name);
				data.putExtra("petimage", pet.image);
				data.putExtra("customerpetname", pet.nickName);
				data.putExtra("customerpetid", pet.customerpetid);
				startActivity(data);
				finishWithAnimation();
				// 保存选择的宠物的头像
				spUtil.saveString("petimg", pet.image);
			} else if (previous == Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT) {
				Intent data = new Intent(this,
						FostercareChooseroomActivity.class);
				data.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				data.putExtra("petid", pet.id);
				data.putExtra("previous",
						Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT);
				data.putExtra("petkind", pet.kindid);
				data.putExtra("petname", pet.name);
				data.putExtra("serviceid", serviceid);
				data.putExtra("servicetype", servicetype);
				data.putExtra("petimage", pet.image);
				data.putExtra("customerpetname", pet.nickName);
				data.putExtra("customerpetid", pet.customerpetid);
				startActivity(data);
				finishWithAnimation();
			} else if (previous == Global.APPOINTMENT_TO_ADDPET) {
				ArrayList<MulPetService> listPets = AppointmentActivity.mulPetServiceList;
				for (int i = 0; i < AppointmentActivity.mulPetServiceList
						.size(); i++) {
					if (listPets.get(i).petCustomerId == pet.customerpetid) {
						ToastUtil.showToastShortCenter(mContext, "该宝贝已经添加了");
						return;
					}
				}
				Intent data = new Intent(this, MulPetAddServiceActivity.class);
				data.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				data.putExtra("petid", pet.id);
				data.putExtra("previous", Global.APPOINTMENT_TO_ADDPET);
				data.putExtra("petkind", pet.kindid);
				data.putExtra("petname", pet.name);
				data.putExtra("serviceid", serviceid);
				data.putExtra("servicetype", servicetype);
				data.putExtra("petimage", pet.image);
				data.putExtra("customerpetid", pet.customerpetid);
				data.putExtra("customerpetname", pet.nickName);
				data.putExtra("lat", fIntent.getDoubleExtra("lat", 0));
				data.putExtra("lng", fIntent.getDoubleExtra("lng", 0));
				data.putExtra("shopid", fIntent.getIntExtra("shopid", 0));
				data.putExtra("beautician_sort",fIntent.getIntExtra("beautician_sort", 0));
				data.putExtra("servloc", fIntent.getIntExtra("servloc", 0));
				data.putExtra("time", fIntent.getStringExtra("time"));
				startActivityForResult(data, Global.APPOINTMENT_TO_ADDPET);
			} else if (previous == Global.SERVICEFEATURE_TO_PETLIST) {
				// 从特色服务获取
				Intent data = new Intent();
				if (spUtil.getInt("tareaid", 0) == 100) {
					data.setClass(this, ShopListActivity.class);
					data.putExtra("previous",Global.SERVICEFEATURE_TO_PETLIST);
					data.putExtra("typeId", typeId);// 从特色服务跳转到门店列表专用，为了计算价格
				} else {
					data.setClass(this, ServiceFeature.class);
					data.putExtra("previous", Global.SERVICEFEATURE_TO_PETLIST);
				}
				data.putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
				data.putExtra("petid", pet.id);
				data.putExtra("petkind", pet.kindid);
				data.putExtra("petname", pet.name);
				data.putExtra("customerpetid", pet.customerpetid);
				data.putExtra("customerpetname", pet.nickName);
				data.putExtra("servicename",fIntent.getStringExtra("servicename"));
				data.putExtra("petimage", pet.image);
				startActivity(data);
				finishWithAnimation();
			} else if (previous == Global.BEAUTICIAN_TO_APPOINTMENT) {
				// 从美容师预约
				Intent data = null;
				if (fIntent.getIntExtra("tmpflag", 0) == 3) {
					data = new Intent(this, ServiceFeature.class);
					data.putExtra("servicename",
							fIntent.getIntExtra("servicename", 0));
				} else {
					data = new Intent(this, ServiceActivity.class);
				}
				data.putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
				data.putExtra("petid", pet.id);
				data.putExtra("petkind", pet.kindid);
				data.putExtra("petname", pet.name);
				data.putExtra("previous", Global.BEAUTICIAN_TO_APPOINTMENT);
				data.putExtra("petimage", pet.image);
				data.putExtra("isshop", fIntent.getBooleanExtra("isshop", true));
				data.putExtra("ishome", fIntent.getBooleanExtra("ishome", true));
				data.putExtra("areaid", fIntent.getIntExtra("areaid", 0));
				data.putExtra("shopid", fIntent.getIntExtra("shopid", 0));
				data.putExtra("serviceid", fIntent.getIntExtra("serviceid", 0));
				data.putExtra("servicetype",fIntent.getIntExtra("servicetype", 0));
				data.putExtra("servicename",fIntent.getStringExtra("servicename"));

				data.putExtra("beautician_levelname",fIntent.getStringExtra("beautician_levelname"));
				data.putExtra("beautician_image",fIntent.getStringExtra("beautician_image"));
				data.putExtra("beautician_name",fIntent.getStringExtra("beautician_name"));
				data.putExtra("beautician_id",fIntent.getIntExtra("beautician_id", 0));
				data.putExtra("beautician_sex",fIntent.getIntExtra("beautician_sex", 0));
				data.putExtra("beautician_sort",fIntent.getIntExtra("beautician_sort", 10));
				data.putExtra("beautician_ordernum",fIntent.getIntExtra("beautician_ordernum", 0));
				data.putExtra("upgradeTip", fIntent.getStringExtra("upgradeTip"));
				startActivity(data);
				finishWithAnimation();
			} else if (previous == Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET) {
				Intent data = new Intent(this, SwimDetailActivity.class);
				data.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				data.putExtra("petid", pet.id);
				data.putExtra("previous",
						Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET);
				data.putExtra("petkind", pet.kindid);
				data.putExtra("petname", pet.name);
				data.putExtra("petimage", pet.image);
				data.putExtra("customerpetname", pet.nickName);
				data.putExtra("customerpetid", pet.customerpetid);
				data.putExtra("servicetype", servicetype);
				startActivity(data);
				finishWithAnimation();
			}else if (previous==Global.CARD_NOTPET_CHOOSE_PET) {
				Intent data = new Intent(this, CardsDetailActivity.class);
				data.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				data.putExtra("id",fIntent.getIntExtra("id", -1));//这个需要从h5带过来
				data.putExtra("isCerti", pet.isCerti);
				data.putExtra("petid", pet.id);
				data.putExtra("previous",
						Global.SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET);
				data.putExtra("petkind", pet.kindid);
				data.putExtra("petname", pet.name);
				data.putExtra("petimage", pet.image);
				data.putExtra("customerpetname", pet.nickName);
				data.putExtra("customerpetid", pet.customerpetid);
				data.putExtra("servicetype", servicetype);
				startActivity(data);
				finishWithAnimation();
			}else if (previous == Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET) {
				Intent data = new Intent(this, TrainAppointMentActivity.class);
				data.putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_RIGHT());
				getIntent().putExtra(Global.ANIM_DIRECTION(),
						Global.ANIM_COVER_FROM_LEFT());
				data.putExtra("petid", pet.id);
				data.putExtra("previous",
						Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET);
				data.putExtra("petkind", pet.kindid);
				data.putExtra("petname", pet.name);
				data.putExtra("petimage", pet.image);
				data.putExtra("customerpetname", pet.nickName);
				data.putExtra("customerpetid", pet.customerpetid);
				data.putExtra("servicetype", servicetype);
				startActivity(data);
				finishWithAnimation();
			} else {
				if (previous == Global.SWIM_APPOINMENT) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.SwimAppointmentActivity");
					intent.putExtra("petid", pet.id);
					intent.putExtra("petkind", pet.kindid);
					intent.putExtra("petname", pet.name);
					intent.putExtra("serviceid", serviceid);
					intent.putExtra("servicetype", servicetype);
					intent.putExtra("petimage", pet.image);
					intent.putExtra("customerpetname", pet.nickName);
					intent.putExtra("customerpetid", pet.customerpetid);
					intent.putExtra("index", 0);
					sendBroadcast(intent);
				} else if (previous == Global.SWIM_DETAIL_ADD_PET) {
					ArrayList<Pet> swimPet = SwimDetailActivity.listPets;
					for (int i = 0; i < swimPet.size(); i++) {
						if (swimPet.get(i).customerpetid == pet.customerpetid) {
							ToastUtil
									.showToastShortCenter(mContext, "该宝贝已经添加了");
							return;
						}
					}
					Intent intent = new Intent();
					intent.setAction("android.intent.action.SwimDetailActivity");
					intent.putExtra("petid", pet.id);
					intent.putExtra("petkind", pet.kindid);
					intent.putExtra("petname", pet.name);
					intent.putExtra("serviceid", serviceid);
					intent.putExtra("servicetype", servicetype);
					intent.putExtra("petimage", pet.image);
					intent.putExtra("customerpetname", pet.nickName);
					intent.putExtra("customerpetid", pet.customerpetid);
					intent.putExtra("index", 0);
					sendBroadcast(intent);
				} else if (previous == Global.SWIM_DETAIL_CLICK_ONE_TO_CHANGE_PET) {
					ArrayList<Pet> swimPet = SwimDetailActivity.listPets;
					for (int i = 0; i < swimPet.size(); i++) {
						if (swimPet.get(i).customerpetid == pet.customerpetid) {
							ToastUtil
									.showToastShortCenter(mContext, "该宝贝已经添加了");
							return;
						}
					}
					Intent intent = new Intent();
					intent.setAction("android.intent.action.SwimDetailActivity");
					intent.putExtra("petid", pet.id);
					intent.putExtra("petkind", pet.kindid);
					intent.putExtra("petname", pet.name);
					intent.putExtra("serviceid", serviceid);
					intent.putExtra("servicetype", servicetype);
					intent.putExtra("petimage", pet.image);
					intent.putExtra("customerpetname", pet.nickName);
					intent.putExtra("customerpetid", pet.customerpetid);
					intent.putExtra("index", 1);
					sendBroadcast(intent);
				} else {
					Intent data = new Intent();
					data.putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_LEFT());
					data.putExtra("isCerti", pet.isCerti);
					data.putExtra("petid", pet.id);
					data.putExtra("petkind", pet.kindid);
					data.putExtra("petname", pet.name);
					data.putExtra("serviceid", serviceid);
					data.putExtra("servicetype", servicetype);
					data.putExtra("petimage", pet.image);
					data.putExtra("customerpetname", pet.nickName);
					data.putExtra("customerpetid", pet.customerpetid);
					setResult(Global.RESULT_OK, data);
				}
				finishWithAnimation();
			}
		}
	}

	private void isShowOverBurden() {
		if (spUtil.getBoolean("isShowOverBurden", true)) {
			Intent intent = new Intent(this, OverburdenActivity.class);
			startActivity(intent);
		}
	}

	private boolean isLogin() {
		return spUtil.getInt("userid", 0) > 0
				&& !"".equals(spUtil.getString("cellphone", ""));
	}

	private void showMain(boolean flag) {
		if (flag) {
			flMain.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			flMain.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
	}

	private void getMyPets(int petkind) {
		mypets.clear();
		vHeader.setVisibility(View.GONE);
		if (vHeader.getMeasuredHeight() > 0)
			vHeader.setPadding(0, -vHeader.getMeasuredHeight(), 0, 0);
		if (vHeader.getHeight() > 0)
			vHeader.setPadding(0, -vHeader.getHeight(), 0, 0);
		if (!isLogin() || previous == Global.ADDPET_TO_PETLIST) {
			llMyPets.setVisibility(View.GONE);
		} else {

			if (previous == Global.APPOINTMENT_TO_ADDPET
					|| previous == Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT
					|| previous == Global.FOSTERCARE_APPOINTMENT_CHANGEPET
					|| previous == Global.SERVICEFEATURE_TO_PETLIST) {
				CommUtil.queryCustomerPets(spUtil.getString("cellphone", ""),
						Global.getIMEI(this), this, -1, petkind,
						fIntent.getIntExtra("beautician_id", 0), 0,templateId,
						customerPetsHandler);
			} else if (previous == Global.MAIN_TO_TRAIN_DETAIL
					|| previous == Global.TRAIN_TO_CHOOSE_PET_PRE
					|| previous == Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET) {// 训练serviceType
																					// 传400
				CommUtil.queryCustomerPets(spUtil.getString("cellphone", ""),
						Global.getIMEI(this), this, serviceid, petkind,
						fIntent.getIntExtra("beautician_id", 0), 400,templateId,
						customerPetsHandler);
			} else {
				CommUtil.queryCustomerPets(spUtil.getString("cellphone", ""),
						Global.getIMEI(this), this, serviceid, petkind,
						fIntent.getIntExtra("beautician_id", 0), 0,templateId,
						customerPetsHandler);
			}

		}

	}

	private void getPets(int petkind) {
		pDialog.showDialog();
		// petlist.clear();
		showMain(true);
		if (previous == Global.PRE_MAINFRAGMENT_TO_BOOKINGSERVICEACTIVITY
				|| previous == Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT
				|| previous == Global.APPOINTMENT_TO_ADDPET
				|| previous == Global.FOSTERCARE_APPOINTMENT_CHANGEPET
				|| previous == Global.SERVICEFEATURE_TO_PETLIST) {
			CommUtil.getPetList(this, petkind, 0,
					fIntent.getIntExtra("beautician_id", 0), 0, templateId,petHandler);
		} else if (previous == Global.MAIN_TO_TRAIN_DETAIL
				|| previous == Global.TRAIN_TO_CHOOSE_PET_PRE
				|| previous == Global.TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET) {// 训练serviceType
																				// 传400
			CommUtil.getPetList(this, petkind, 0,
					fIntent.getIntExtra("beautician_id", 0), 400,templateId,petHandler);
		} else {
			CommUtil.getPetList(this, petkind, serviceid,
					fIntent.getIntExtra("beautician_id", 0), 0,templateId,petHandler);
		}
	}

	private void isUpdate(int petkind) {
		if (previous == Global.PRE_MAINFRAGMENT_TO_BOOKINGSERVICEACTIVITY
				|| previous == Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT
				|| previous == Global.APPOINTMENT_TO_ADDPET
				|| previous == Global.FOSTERCARE_APPOINTMENT_CHANGEPET
				|| previous == Global.SERVICEFEATURE_TO_PETLIST
				|| previous == Global.BEAUTICIAN_TO_APPOINTMENT) {
			getPets(petkind);
		} else {
			if (serviceid == 1) {
				if ("".equals(spUtil.getString("bathdoglist", ""))) {
					getPets(petkind);
				} else {
					getJson(spUtil.getString("bathdoglist", ""), false);
					CommUtil.getListVersion(this, listversionHandler);
				}
			} else if (serviceid == 2) {
				if ("".equals(spUtil.getString("beautydoglist", ""))) {
					getPets(petkind);
				} else {
					getJson(spUtil.getString("beautydoglist", ""), false);
					CommUtil.getListVersion(this, listversionHandler);
				}
			} else if (serviceid == 3) {
				if ("".equals(spUtil.getString("bathcatlist", ""))) {
					getPets(petkind);
				} else {
					getJson(spUtil.getString("bathcatlist", ""), false);
					CommUtil.getListVersion(this, listversionHandler);
				}
			} else if (serviceid == 4) {
				if ("".equals(spUtil.getString("beautycatlist", ""))) {
					getPets(petkind);
				} else {
					getJson(spUtil.getString("beautycatlist", ""), false);
					CommUtil.getListVersion(this, listversionHandler);
				}
			} else {
				getPets(petkind);
			}

		}
	}

	private AsyncHttpResponseHandler petHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			String st = new String(responseBody);
			Utils.mLogError("获取狗列表：" + st);

			getJson(st, true);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
		}

	};
	private AsyncHttpResponseHandler customerPetsHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			String str = new String(responseBody);
			Utils.mLogError("我的宠物" + str);
			try {
				JSONObject jobj = new JSONObject(str);
				int code = jobj.getInt("code");
				if (code == 0) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						if (jdata.has("pets") && !jdata.isNull("pets")) {
							JSONArray array = jdata.getJSONArray("pets");
							for (int i = 0; i < array.length(); i++) {
								mypets.add(Pet.json2Entity(array
										.getJSONObject(i)));
							}
						}
					}
					if (mypets.size() > 0) {

						vHeader.setVisibility(View.VISIBLE);
						vHeader.setPadding(0, 0, 0, 0);
						llMyPets.setVisibility(View.VISIBLE);
						// setGridView(gvMyPets, mypets.size());
					}
					myPetAdapter.notifyDataSetChanged();
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
	private AsyncHttpResponseHandler listversionHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			String st = new String(responseBody);
			Utils.mLogError("是否有更新：" + st);
			try {
				JSONObject jobj = new JSONObject(st);
				int code = jobj.getInt("code");
				List<ArrayMap<String, Object>> forNet = new ArrayList<ArrayMap<String, Object>>();
				List<ArrayMap<String, Object>> forNetHot = new ArrayList<ArrayMap<String, Object>>();
				if (code == 0 && jobj.has("data") && !jobj.isNull("data")) {
					long timestamp = jobj.getLong("data");
					if (serviceid == 1) {
						if (spUtil.getLong("bathtimestamp", 0) < timestamp) {
							spUtil.saveLong("bathtimestamp", timestamp);
							getPets(petkind);
						}
					} else if (serviceid == 2) {
						if (spUtil.getLong("beautytimestamp", 0) < timestamp) {
							spUtil.saveLong("beautytimestamp", timestamp);
							getPets(petkind);
						}
					} else if (serviceid == 3) {
						if (spUtil.getLong("bathtimestampcat", 0) < timestamp) {
							spUtil.saveLong("bathtimestampcat", timestamp);
							getPets(petkind);
						}
					} else if (serviceid == 4) {
						if (spUtil.getLong("beautytimestampcat", 0) < timestamp) {
							spUtil.saveLong("beautytimestampcat", timestamp);
							getPets(petkind);
						}
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
			pDialog.closeDialog();
		}

	};

	private void getJson(String json, boolean saveflag) {
		if (json == null || "".equals(json) || "null".equals(json))
			return;
		try {
			petlist.clear();
			// petAdapter.notifyDataSetChanged();
			JSONObject jobj = new JSONObject(json);
			int code = jobj.getInt("code");
			if (code == 0 && jobj.has("data") && !jobj.isNull("data")) {
				JSONObject jsonObject = jobj.getJSONObject("data");

				if (jsonObject.has("all") && !jsonObject.isNull("all")
						&& jsonObject.getJSONArray("all").length() > 0) {
					if (saveflag) {
						if (serviceid == 1) {
							spUtil.saveString("bathdoglist", json);
						} else if (serviceid == 2) {
							spUtil.saveString("beautydoglist", json);
						} else if (serviceid == 3) {
							spUtil.saveString("bathcatlist", json);
						} else if (serviceid == 4) {
							spUtil.saveString("beautycatlist", json);
						}
					}
					JSONArray jsonArray = jsonObject.getJSONArray("all");
					for (int i = 0; i < jsonArray.length(); i++) {
						SortModel sm = new SortModel();
						JSONObject object = jsonArray.getJSONObject(i);
						if (object.has("avatarPath")
								&& !object.isNull("avatarPath")) {
							sm.setAvatarPath(object.getString("avatarPath"));
						}
						if (object.has("isCerti") && !object.isNull("isCerti")) {
							sm.setIsCerti(object.getInt("isCerti"));
						}
						if (object.has("PetId") && !object.isNull("PetId")) {
							sm.setPetId(object.getInt("PetId"));
						}
						sm.setHot(false);
						if (object.has("petKind") && !object.isNull("petKind")) {
							sm.setPetKind(object.getInt("petKind"));
						}
						if (object.has("petName") && !object.isNull("petName")) {
							sm.setName(object.getString("petName").trim());
						}
						if (object.has("pyhead") && !object.isNull("pyhead")) {
							String pinyin = object.getString("pyhead");
							sm.setPyhead(pinyin);

							String sortStr = pinyin.substring(0, 1)
									.toUpperCase();
							if (sortStr.matches("[A-Z]")) {
								sm.setSortLetters(sortStr.toUpperCase());
							} else {
								sm.setSortLetters("#");
							}
						}
						if (object.has("serviceHome")
								&& !object.isNull("serviceHome")) {
							JSONArray arrayService = object
									.getJSONArray("serviceHome");
							long ServiceHome[] = new long[arrayService.length()];
							for (int j = 0; j < arrayService.length(); j++) {
								ServiceHome[j] = arrayService.getLong(j);
								// BitSet bitSet = BitSet.valueOf(ServiceHome);
								// boolean isCan = bitSet.get(serviceid);
							}
							sm.setServiceHome(ServiceHome);
						}
						if (object.has("serviceShop")
								&& !object.isNull("serviceShop")) {
							JSONArray arrayService = object
									.getJSONArray("serviceShop");
							long serviceShop[] = new long[arrayService.length()];
							for (int j = 0; j < arrayService.length(); j++) {
								serviceShop[j] = arrayService.getLong(j);
							}
							sm.setServiceShop(serviceShop);
						}
						petlist.add(sm);
					}
				}
				if (jsonObject.has("hot") && !jsonObject.isNull("hot")) {
					JSONArray jsonArrayHot = jsonObject.getJSONArray("hot");
					for (int k = jsonArrayHot.length() - 1; k >= 0; k--) {
						SortModel sm = new SortModel();
						JSONObject object = jsonArrayHot.getJSONObject(k);
						if (object.has("avatarPath")
								&& !object.isNull("avatarPath")) {
							sm.setAvatarPath(object.getString("avatarPath"));
						}
						if (object.has("isCerti") && !object.isNull("isCerti")) {
							sm.setIsCerti(object.getInt("isCerti"));
						}
						if (object.has("PetId") && !object.isNull("PetId")) {
							sm.setPetId(object.getInt("PetId"));
						}

						sm.setHot(true);
						if (object.has("petKind") && !object.isNull("petKind")) {
							sm.setPetKind(object.getInt("petKind"));

						}
						if (object.has("petName") && !object.isNull("petName")) {
							sm.setName(object.getString("petName").trim());

						}
						if (object.has("pyhead") && !object.isNull("pyhead")) {
							sm.setPyhead(object.getString("pyhead"));
							sm.setSortLetters("@");
						}
						if (object.has("serviceHome")
								&& !object.isNull("serviceHome")) {
							JSONArray arrayService = object
									.getJSONArray("serviceHome");
							long ServiceHome[] = new long[arrayService.length()];
							for (int j = 0; j < arrayService.length(); j++) {
								ServiceHome[j] = arrayService.getLong(j);
							}
							sm.setServiceHome(ServiceHome);
						}
						if (object.has("serviceShop")
								&& !object.isNull("serviceShop")) {
							JSONArray arrayService = object
									.getJSONArray("serviceShop");
							long serviceShop[] = new long[arrayService.length()];
							for (int j = 0; j < arrayService.length(); j++) {
								serviceShop[j] = arrayService.getLong(j);
								// Utils.mLogError("==-->serviceShopHot isCan1 "+serviceShop[j]);
							}
							sm.setServiceShop(serviceShop);
						}
						petlist.add(sm);
					}
				}

				// 根据a-z进行排序源数据
				Collections.sort(petlist, pinyinComparator);

				petAdapter.notifyDataSetChanged();
				lvPets.setSelection(0);
			} else {
				showMain(false);
				if (serviceid == 1) {
					spUtil.removeData("bathtimestamp");
				} else if (serviceid == 2) {
					spUtil.removeData("beautytimestamp");
				}
				if (jobj.has("msg") && !jobj.isNull("msg"))
					ToastUtil.showToastShortCenter(getApplicationContext(),
							jobj.getString("msg"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showMain(false);
		}
	}

	private void filterData(String filterstr) {
		List<SortModel> filterDataList = new ArrayList<SortModel>();
		if (TextUtils.isEmpty(filterstr)) {
			filterDataList = petlist;
		} else {
			filterDataList.clear();
			for (SortModel model : petlist) {
				String name = model.getName();
				if (!model.isHot()
						&& (name.indexOf(filterstr.toString()) != -1 || characterParser
								.getSelling(name).startsWith(
										filterstr.toString()))) {
					filterDataList.add(model);
				}
			}
		}
		Collections.sort(filterDataList, pinyinComparator);
		petAdapter.updateListView(filterDataList);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Global.RESULT_OK && data != null) {
			if (requestCode == Global.APPOINTMENT_TO_ADDPET) {
				Intent data1 = new Intent();
				data1.putExtra("petid", data.getIntExtra("petid", 0));
				data1.putExtra("petkind", data.getIntExtra("petkind", 0));
				data1.putExtra("petname", data.getStringExtra("petname"));
				data1.putExtra("serviceid", data.getIntExtra("serviceid", 0));
				data1.putExtra("servicetype",
						data.getIntExtra("servicetype", 0));
				data1.putExtra("petimage", data.getStringExtra("petimage"));
				data1.putExtra("customerpetid",
						data.getIntExtra("customerpetid", 0));
				data1.putExtra("customerpetname",
						data.getStringExtra("customerpetname"));
				data1.putExtra("servicename",
						data.getStringExtra("servicename"));
				data1.putExtra("addserviceids",
						data.getStringExtra("addserviceids"));
				data1.putExtra("totalfee", data.getDoubleExtra("totalfee", 0));
				data1.putExtra("basefee", data.getDoubleExtra("basefee", 0));
				data1.putExtra("pricelevel1",
						data.getDoubleExtra("pricelevel1", 0));
				data1.putExtra("pricelevel2",
						data.getDoubleExtra("pricelevel2", 0));
				data1.putExtra("pricelevel3",
						data.getDoubleExtra("pricelevel3", 0));
				data1.putExtra("addservicefee",
						data.getDoubleExtra("addservicefee", 0));
				setResult(Global.RESULT_OK, data1);
				finishWithAnimation();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
