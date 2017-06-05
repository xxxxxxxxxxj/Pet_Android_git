package com.haotang.pet;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ColorfulRingProgressView;
import com.haotang.pet.view.LeftSlidingListener;
import com.haotang.pet.view.MChangeView;
import com.haotang.pet.view.RightSlidingListener;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

@SuppressLint("ResourceAsColor") public class PetInfoActivity extends SuperActivity {
	public static SuperActivity act;
	private ImageButton ibBack;
	private ImageButton ibAdd;
	private TextView tvBathNum;
	private TextView tvBeautyNum;
	private TextView tvFostercareNum;
	private SharedPreferenceUtil spUtil;
	private MProgressDialog pDialog;
	private int customerpetid;
	private int currentpetindex;
	private boolean isAddBack;//添加宠物成功后返回刷新
	private ArrayList<Pet> petlist = new ArrayList<Pet>();

//	---START 2016-3-14 17:30:30
	private LinearLayout layout_mypetinfo_go_wash;//始终支持去洗澡
	private ImageView imageview_go_wash;//去洗澡
	private TextView textview_go_wash;//去洗澡
	
	private LinearLayout layout_mypetinfo_go_beautiful;//不一定支持去美容
	private boolean isCanBeautiful = true;//是否支持去美容
	private ImageView imageview_mypet_info;
	private TextView textview_mypet_info;
	private PopupWindow pWin;
	private LayoutInflater mInflater;
	private ArrayList<MulPetService> mulpsList = null;
	private Iterator<MulPetService> itemulps;
	private int toNextId = -1;
	private int previous;
//	private int toNextId = 
//	---END 2016-3-14 17:32:25
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.petinfo);
		act = this;
		findView();
		setView();
	}

	private void findView() {
		mInflater = LayoutInflater.from(this);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		ibAdd = (ImageButton) findViewById(R.id.ib_titlebar_other);
		
		tvBathNum = (TextView) findViewById(R.id.tv_petinfo_bathnum);
		tvBeautyNum = (TextView) findViewById(R.id.tv_petinfo_beautynum);
		tvFostercareNum = (TextView) findViewById(R.id.tv_petinfo_fostercarenum);
		
		mcvLayout = (MChangeView) findViewById(R.id.mcv_petinfo_image);
		srivLeft = (SelectableRoundedImageView) findViewById(R.id.sriv_petinfo_image_left);
		srivCenter = (SelectableRoundedImageView) findViewById(R.id.sriv_petinfo_image_center);
		srivRight = (SelectableRoundedImageView) findViewById(R.id.sriv_petinfo_image_right);
		rlLeftImage = (RelativeLayout) findViewById(R.id.rl_petinfo_leftimage);
		rlRightImage = (RelativeLayout) findViewById(R.id.rl_petinfo_rightimage);
		tvPetName = (TextView) findViewById(R.id.tv_petinfo_petname);
		tvPetID = (TextView) findViewById(R.id.tv_petinfo_petid);
		ivPetSex = (ImageView) findViewById(R.id.iv_petinfo_petsex);
		
		crpvCleannum = (ColorfulRingProgressView) findViewById(R.id.cpv_petinfo_cleannum);
		tvCleannum = (TextView) findViewById(R.id.tv_petinfo_cleannum);
		tvCleannumUnit = (TextView) findViewById(R.id.tv_petinfo_cleannum_unit);
		crpvCharmnum = (ColorfulRingProgressView) findViewById(R.id.cpv_petinfo_charmnum);
		tvCharmnum = (TextView) findViewById(R.id.tv_petinfo_charmnum);
		tvCharmnumUnit = (TextView) findViewById(R.id.tv_petinfo_charmnum_unit);
		
		tvCleanHint = (TextView) findViewById(R.id.tv_petinfo_cleanhint);
		ivMood = (ImageView) findViewById(R.id.iv_petinfo_mood);
		
		svMain = (ScrollView) findViewById(R.id.sv_petinfo_main);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvMsg = (TextView) findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);
		layout_mypetinfo_go_wash = (LinearLayout) findViewById(R.id.layout_mypetinfo_go_wash);
		imageview_go_wash = (ImageView) findViewById(R.id.imageview_go_wash);
		textview_go_wash = (TextView) findViewById(R.id.textview_go_wash);
		
		layout_mypetinfo_go_beautiful = (LinearLayout) findViewById(R.id.layout_mypetinfo_go_beautiful);
		imageview_mypet_info = (ImageView) findViewById(R.id.imageview_mypet_info);
		textview_mypet_info = (TextView) findViewById(R.id.textview_mypet_info);
	}

	private void setView() {
		// TODO Auto-generated method stub
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		previous = getIntent().getIntExtra("previous", 0);
		customerpetid = getIntent().getIntExtra("customerpetid", 0);
		currentpetindex = getIntent().getIntExtra("currentpetindex", 0);
		mulpsList  =  getIntent().getParcelableArrayListExtra("mulpetservice");
		ibAdd.setVisibility(View.VISIBLE);
		btRefresh.setVisibility(View.VISIBLE);
		
		//从下单完成添加宠物界面进入要定位到最后一个宠物
		if(previous==Global.ORDERLIST_TO_ADD_PET_BACK_PETINFO_BACK_MY)
			isAddBack=true;
		
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goBack();
			}
		});
		ibAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(petlist.size()>=20){
					ToastUtil.showToastShortCenter(PetInfoActivity.this, "最多支持添加20只宝贝呦");
				}else{
					JumpToNextForData(PetAddActivity.class, Global.PETINFO_TO_ADDPET,0,0);
				}
			}
		});
		
		mcvLayout.setLeftSlidingListener(new LeftSlidingListener() {
			
			@Override
			public void changeImage() {
				// TODO Auto-generated method stub
				if(currentpetindex==petlist.size()-2){
					rlRightImage.setVisibility(View.GONE);
					srivRight.setImageResource(R.drawable.icon_production_default);
				}else if(currentpetindex<petlist.size()-2){
					rlRightImage.setVisibility(View.VISIBLE);
					setImage(petlist.get(currentpetindex+2).image, srivRight);
				}
				if(currentpetindex<petlist.size()-1){
					rlLeftImage.setVisibility(View.VISIBLE);
					setImage(petlist.get(currentpetindex).image, srivLeft);
					setImage(petlist.get(currentpetindex+1).image, srivCenter);
					currentpetindex = currentpetindex+1;
					setText();
				}
			}
		});
		mcvLayout.setRightSlidingListener(new RightSlidingListener() {
			
			@Override
			public void changeImage() {
				// TODO Auto-generated method stub
				if(currentpetindex==1){
					rlLeftImage.setVisibility(View.GONE);
					srivLeft.setImageResource(R.drawable.icon_production_default);
				}else if(currentpetindex>1){
					rlLeftImage.setVisibility(View.VISIBLE);
					setImage(petlist.get(currentpetindex-2).image, srivLeft);
				}
				if(currentpetindex>0){
					rlRightImage.setVisibility(View.VISIBLE);
					setImage(petlist.get(currentpetindex).image, srivRight);
					setImage(petlist.get(currentpetindex-1).image, srivCenter);
					currentpetindex = currentpetindex-1;
					setText();
				}
			}
		});
		
		srivLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentpetindex==1){
					rlLeftImage.setVisibility(View.GONE);
					srivLeft.setImageResource(R.drawable.icon_production_default);
				}else if(currentpetindex>1){
					rlLeftImage.setVisibility(View.VISIBLE);
					setImage(petlist.get(currentpetindex-2).image, srivLeft);
				}
				if(currentpetindex>0){
					rlRightImage.setVisibility(View.VISIBLE);
					setImage(petlist.get(currentpetindex).image, srivRight);
					setImage(petlist.get(currentpetindex-1).image, srivCenter);
					currentpetindex = currentpetindex-1;
					
					setText();
				}
			}
		});
		srivRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentpetindex==petlist.size()-2){
					rlRightImage.setVisibility(View.GONE);
					srivRight.setImageResource(R.drawable.icon_production_default);
				}else if(currentpetindex<petlist.size()-2){
					rlRightImage.setVisibility(View.VISIBLE);
					setImage(petlist.get(currentpetindex+2).image, srivRight);
				}
				if(currentpetindex<petlist.size()-1){
					rlLeftImage.setVisibility(View.VISIBLE);
					setImage(petlist.get(currentpetindex).image, srivLeft);
					setImage(petlist.get(currentpetindex+1).image, srivCenter);
					currentpetindex = currentpetindex+1;
					setText();
				}
				
				
			}
		});
		srivCenter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JumpToNextForData(PetAddActivity.class, 
						Global.PETINFO_TO_EDITPET,
						customerpetid,currentpetindex);
			}
		});
		//去洗澡
//		layout_mypetinfo_go_wash.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//带对象跳转至对应界面
//				Intent intent = new Intent(act, ServiceActivity.class);
//				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//				getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//				intent.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
//				intent.putExtra("servicetype", 1);
//				int petkind = petlist.get(currentpetindex).kindid;
//				intent.putExtra("petkind", petkind);
//				intent.putExtra("petid", petlist.get(currentpetindex).id);
//				if(petkind==1){
//					intent.putExtra("serviceid", 1);
//				}else if(petkind==2){
//					intent.putExtra("serviceid", 3);
//				}
//				intent.putExtra("customerpetid", customerpetid);
//				intent.putExtra("customerpetname", petlist.get(currentpetindex).nickName);
//				startActivity(intent);
//			}
//		});
//		//去美容
//		layout_mypetinfo_go_beautiful.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (isCanBeautiful) {
//					//带对象跳转至对应界面
//					Intent intent = new Intent(act, ServiceActivity.class);
//					intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//					getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//					intent.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
//					intent.putExtra("servicetype", 2);
//					int petkind = petlist.get(currentpetindex).kindid;
//					intent.putExtra("petkind", petkind);
//					intent.putExtra("petid", petlist.get(currentpetindex).id);
//					if(petkind==1){
//						intent.putExtra("serviceid", 2);
//					}else if(petkind==2){
//						intent.putExtra("serviceid", 4);
//					}
//					intent.putExtra("customerpetid", customerpetid);
//					intent.putExtra("customerpetname", petlist.get(currentpetindex).nickName);
//					startActivity(intent);
//				}
//			}
//		});
//		
		showMain(true);
		getPets();
	}
	
	private void showMain(boolean flag){
		if(flag){
			svMain.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		}else{
			svMain.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
			
	}
	
	private void goBack(){
		if (previous==Global.MY_TO_PETINFO) {
			setResult(Global.RESULT_OK);
		}else if (previous==Global.ORDERLIST_TO_ADD_PET_BACK_PETINFO_BACK_MY) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.mainactivity");
			intent.putExtra("previous", Global.PETADD_BACK_PETINFO_BACK_MY);
			sendBroadcast(intent);
		}
		finishWithAnimation();
	}
	private void JumpToNextForData(Class clazz,int requestcode,
			int customerpetid,int position) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", requestcode);
		intent.putExtra("customerpetid", customerpetid);
		intent.putExtra("position", position);
		intent.putExtra("nickname", petlist.get(currentpetindex).nickName);
		startActivityForResult(intent, requestcode);
	}
	private void JumpToNext(Class clazz,int requestcode,
			MulPetService mulPetService) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", requestcode);
		Bundle bundle = new Bundle();
		bundle.putParcelable("mulpsList", mulpsList.get(0));
		intent.putExtras(bundle);
		startActivityForResult(intent, requestcode);
	}
	
	private void setText(){
		Pet pet = petlist.get(currentpetindex);
		customerpetid = pet.customerpetid;
		if(pet.nickName!=null&&pet.nickName.length()>3)
			tvPetName.setText(pet.nickName.substring(0, 2)+"...");
		else if(pet.nickName!=null)
			tvPetName.setText(pet.nickName);
		if(pet.sex==1)
			ivPetSex.setImageResource(R.drawable.icon_sex_male);
		else
			ivPetSex.setImageResource(R.drawable.icon_sex_female);
		tvPetID.setText("ID:"+pet.visibleid);
		tvCleanHint.setText(pet.cleanhint);
		
		if(pet.mood==1){
			ivMood.setBackgroundResource(R.drawable.icon_mood_1);
		}else if(pet.mood==2){
			ivMood.setBackgroundResource(R.drawable.icon_mood_2);
		}else if(pet.mood==3){
			ivMood.setBackgroundResource(R.drawable.icon_mood_3);
		}else if(pet.mood==4){
			ivMood.setBackgroundResource(R.drawable.icon_mood_4);
		}else{
			ivMood.setBackgroundResource(R.drawable.icon_mood_1);
		}
		
		if(pet.bathnum>999)
			tvBathNum.setText("999+");
		else
			tvBathNum.setText(Integer.toString(pet.bathnum));
		if(pet.beautynum>999)
			tvBeautyNum.setText("999+");
		else
			tvBeautyNum.setText(Integer.toString(pet.beautynum));
		if(pet.fosternum>999)
			tvFostercareNum.setText("999+");
		else
			tvFostercareNum.setText(Integer.toString(pet.fosternum));
		
		tvCleannum.setText(Integer.toString(pet.cleannum));
		tvCleannumUnit.setVisibility(View.VISIBLE);
		
		crpvCleannum.setPercent(pet.cleannum);
		if(pet.cleannum<=0){
			tvCleannum.setText("-");
			tvCleannumUnit.setVisibility(View.GONE);
			crpvCleannum.setPercent(0);
		}else if(pet.cleannum<30){
			crpvCleannum.setFgColorStart(0xFFFA0606);
			crpvCleannum.setFgColorEnd(0xFFFA0606);
		}else if(pet.cleannum<60){
			crpvCleannum.setFgColorStart(0xFFF29EA2);
			crpvCleannum.setFgColorEnd(0xFFF29EA2);
		}else if(pet.cleannum<100){
			crpvCleannum.setFgColorStart(0xFF32B16C);
			crpvCleannum.setFgColorEnd(0xFF32B16C);
		}else if(pet.cleannum==100){
			crpvCleannum.setFgColorStart(0xFFEF9A4B);
			crpvCleannum.setFgColorEnd(0xFFEF9A4B);
		}else{
			crpvCleannum.setFgColorStart(0xffff4800);
			crpvCleannum.setFgColorEnd(0xffffe400);
		}
		
		
		tvCharmnum.setText(Integer.toString(pet.charmnum));
		tvCharmnumUnit.setVisibility(View.VISIBLE);
		crpvCharmnum.setPercent(pet.charmnum);
		if(pet.charmnum<=0){
			tvCharmnum.setText("-");
			tvCharmnumUnit.setVisibility(View.GONE);
			crpvCharmnum.setPercent(0);
		}else if(pet.charmnum<30){
			crpvCharmnum.setFgColorStart(0xFFFA0606);
			crpvCharmnum.setFgColorEnd(0xFFFA0606);
		}else if(pet.charmnum<60){
			crpvCharmnum.setFgColorStart(0xFFF29EA2);
			crpvCharmnum.setFgColorEnd(0xFFF29EA2);
		}else if(pet.charmnum<100){
			crpvCharmnum.setFgColorStart(0xFF32B16C);
			crpvCharmnum.setFgColorEnd(0xFF32B16C);
		}else if(pet.charmnum==100){
			crpvCharmnum.setFgColorStart(0xFFEF9A4B);
			crpvCharmnum.setFgColorEnd(0xFFEF9A4B);
		}else{
			crpvCharmnum.setFgColorStart(0xfffe4d3d);
			crpvCharmnum.setFgColorEnd(0xfffe4d3d);
		}
		
		//设置去美容背景颜色以及图标 根据数组长度判断 去洗澡始终支持，去美容有的不支持
		if (pet.availServiceType.contains("1")) {
			layout_mypetinfo_go_wash.setBackgroundResource(R.drawable.bg_search_orangeborder);
			imageview_go_wash.setBackgroundResource(R.drawable.icon_petinfo_wash_can);
			textview_go_wash.setTextColor(getResources().getColor(R.color.orange));
			layout_mypetinfo_go_wash.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 带对象跳转至对应界面
					Intent intent = new Intent(act, ServiceActivity.class);
					intent.putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_LEFT());
					intent.putExtra("previous",
							Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
					intent.putExtra("servicetype", 1);
					int petkind = petlist.get(currentpetindex).kindid;
					intent.putExtra("petkind", petkind);
					intent.putExtra("petid", petlist.get(currentpetindex).id);
					if (petkind == 1) {
						intent.putExtra("serviceid", 1);
					} else if (petkind == 2) {
						intent.putExtra("serviceid", 3);
					}
					intent.putExtra("customerpetid", customerpetid);
					intent.putExtra("customerpetname",
							petlist.get(currentpetindex).nickName);
					startActivity(intent);
					//保存选择的宠物的头像
					spUtil.saveString("petimg", petlist.get(currentpetindex).image);
				}
			});
		}else {
			layout_mypetinfo_go_wash.setOnClickListener(null);
			layout_mypetinfo_go_wash.setBackgroundResource(R.drawable.bg_cannot_to_beautiful);
			imageview_go_wash.setBackgroundResource(R.drawable.icon_petinfo_wash_cannot);
			textview_go_wash.setTextColor(getResources().getColor(R.color.white));
		}
		if (pet.availServiceType.contains("2")) {
			layout_mypetinfo_go_beautiful.setBackgroundResource(R.drawable.bg_search_orangeborder);
			imageview_mypet_info.setBackgroundResource(R.drawable.scissors_orange);
			textview_mypet_info.setTextColor(getResources().getColor(R.color.orange));
			//去美容
			layout_mypetinfo_go_beautiful.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (isCanBeautiful) {
						//带对象跳转至对应界面
						Intent intent = new Intent(act, ServiceActivity.class);
						intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
						getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
						intent.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
						intent.putExtra("servicetype", 2);
						int petkind = petlist.get(currentpetindex).kindid;
						intent.putExtra("petkind", petkind);
						intent.putExtra("petid", petlist.get(currentpetindex).id);
						if(petkind==1){
							intent.putExtra("serviceid", 2);
						}else if(petkind==2){
							intent.putExtra("serviceid", 4);
						}
						intent.putExtra("customerpetid", customerpetid);
						intent.putExtra("customerpetname", petlist.get(currentpetindex).nickName);
						startActivity(intent);
						//保存选择的宠物的头像
						spUtil.saveString("petimg", petlist.get(currentpetindex).image);
					}
				}
			});
			
		}else {
			layout_mypetinfo_go_beautiful.setOnClickListener(null);
			layout_mypetinfo_go_beautiful.setBackgroundResource(R.drawable.bg_cannot_to_beautiful);
			imageview_mypet_info.setBackgroundResource(R.drawable.scissors_grey);
			textview_mypet_info.setTextColor(getResources().getColor(R.color.white));
		}
	}
	private void getPets(){
		petlist.clear();
		pDialog.showDialog();
		CommUtil.queryCustomerPets(this,spUtil.getString("cellphone", ""), 
				Global.getCurrentVersion(this), 
				Global.getIMEI(this), petHandler);
	}
	private void queryPet(int id){
		CommUtil.queryCustomerPetById(this,spUtil.getString("cellphone", ""), 
				Global.getCurrentVersion(this), Global.getIMEI(this),  id, 
				queryHandler);
	}
	
	private AsyncHttpResponseHandler queryHandler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("查询："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode){
					if(jobj.has("data")&&!jobj.isNull("data")){
						JSONObject jdata = jobj.getJSONObject("data");
						if(jdata.has("pet")&&!jdata.isNull("pet")){
							JSONObject jpet = jdata.getJSONObject("pet");
							if(jpet.has("sumBath")&&!jpet.isNull("sumBath")){
								tvBathNum.setText(Integer.toString(jpet.getInt("sumBath")));
							}
							if(jpet.has("sumCos")&&!jpet.isNull("sumCos")){
								tvBeautyNum.setText(Integer.toString(jpet.getInt("sumCos")));
							}
							if(jpet.has("sumFos")&&!jpet.isNull("sumFos")){
								tvFostercareNum.setText(Integer.toString(jpet.getInt("sumFos")));
							}
							if(jpet.has("cleanGrade")&&!jpet.isNull("cleanGrade")){
							}
							if(jpet.has("beautyGrade")&&!jpet.isNull("beautyGrade")){
							}
							if(jpet.has("CustomerPetId")&&!jpet.isNull("CustomerPetId")){
								customerpetid = jpet.getInt("CustomerPetId");
							}
							if(jpet.has("nickName")&&!jpet.isNull("nickName")){
								String nickname = jpet.getString("nickName");
								if(nickname!=null&&nickname.length()>2)
									tvPetName.setText(nickname.substring(0, 2)+"...");
								else
									tvPetName.setText(nickname);
							}
							
						}
					}
				}else{
					ToastUtil.showToastShortCenter(getApplicationContext(), jobj.getString("msg"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastUtil.showToastShortCenter(getApplicationContext(), "获取宠物信息，请刷新");
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			ToastUtil.showToastShortCenter(getApplicationContext(), "获取宠物信息，请刷新");
		}
		
	};
	
	private AsyncHttpResponseHandler petHandler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			Utils.mLogError("获取宠物列表："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jdata = jobj.getJSONObject("data");
					if(jdata.has("pets")&&!jdata.isNull("pets")){
						JSONArray jpets = jdata.getJSONArray("pets");
						for(int i=0;i<jpets.length();i++){
							petlist.add(Pet.json2Entity(jpets.getJSONObject(i)));
						}
					}
//					if(petlist.size()>=20){
//						ibAdd.setVisibility(View.GONE);
//					}else{
//						ibAdd.setVisibility(View.VISIBLE);
//					}
					if(isAddBack){
						isAddBack = false;
						currentpetindex = petlist.size()-1;
					}
					if(petlist.size()>currentpetindex){
						setImage(petlist.get(currentpetindex).image, srivCenter);
						if(currentpetindex>0){
							rlLeftImage.setVisibility(View.VISIBLE);
							setImage(petlist.get(currentpetindex-1).image, srivLeft);
						}else{
							rlLeftImage.setVisibility(View.GONE);
						}
						if(currentpetindex<petlist.size()-1){
							rlRightImage.setVisibility(View.VISIBLE);
							setImage(petlist.get(currentpetindex+1).image, srivRight);
						}else{
							rlRightImage.setVisibility(View.GONE);
						}
						setText();
					}
				}else{
					showMain(false);
					if(jobj.has("msg")&&!jobj.isNull("msg"))
						ToastUtil.showToastShortCenter(PetInfoActivity.this, jobj.getString("msg"));
				}
				if (mulpsList!=null) {
					if (mulpsList.size()>0) {
						if (petlist.size()<20) {
							showPop();
						}
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastUtil.showToastShortCenter(PetInfoActivity.this, "获取宠物信息失败，请刷新");
				showMain(false);
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			ToastUtil.showToastShortCenter(PetInfoActivity.this, "获取宠物信息失败，请刷新");
			showMain(false);
		}
		
	};
	
	private void setImage(String path,SelectableRoundedImageView sriv){
		ImageLoaderUtil.loadImg(path, 
				sriv,0, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(String path, View view, Bitmap bitmap) {
						// TODO Auto-generated method stub
						if(path!=null&&bitmap!=null){
							SelectableRoundedImageView srivView = (SelectableRoundedImageView) view;
							srivView.setImageBitmap(bitmap);
						}
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
				});
	}
	private ScrollView svMain;
	private RelativeLayout rlNull;
	private TextView tvMsg;
	private Button btRefresh;
	private MChangeView mcvLayout;
	private ColorfulRingProgressView crpvCleannum;
	private TextView tvCleannum;
	private TextView tvCleannumUnit;
	private ColorfulRingProgressView crpvCharmnum;
	private TextView tvCharmnum;
	private TextView tvCharmnumUnit;
	private SelectableRoundedImageView srivLeft;
	private SelectableRoundedImageView srivCenter;
	private SelectableRoundedImageView srivRight;
	private TextView tvPetName;
	private TextView tvPetID;
	private ImageView ivPetSex;
	private TextView tvCleanHint;
	private ImageView ivMood;
	private RelativeLayout rlLeftImage;
	private RelativeLayout rlRightImage;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode==Global.RESULT_OK){
			switch (requestCode) {
			case Global.PETINFO_TO_ADDPET:
				//添加宠物
				isAddBack = true;
				getPets();
				break;
			case Global.PETINFO_ADDPET_POP:
				//添加宠物
				if (mulpsList!=null&&mulpsList.size()>0) {
					itemulps = mulpsList.iterator();
					while (itemulps.hasNext()) {
						int currentPetId  = itemulps.next().petId;
						if (currentPetId==toNextId) {
							itemulps.remove();
						}
					}
					
				}
				isAddBack = true;
				getPets();
				break;
			case Global.PETINFO_TO_EDITPET:
				//编辑宠物
				int editflag = data.getIntExtra("editflag", 0);
				if(editflag==1){
					//编辑返回
					int id = data.getIntExtra("customerpetid", 0);
					getPets();
				}else if(editflag==2){
					//删除返回
					int deleteposition = data.getIntExtra("position", -1);
					if(deleteposition>-1&&deleteposition<petlist.size()){
						petlist.remove(deleteposition);
						
						if(petlist.size()==0){
							goBack();
						}else{
							currentpetindex = deleteposition;
							if(deleteposition==petlist.size())
								currentpetindex = petlist.size()-1;
							setText();
							
							setImage(petlist.get(currentpetindex).image, srivCenter);
							if(currentpetindex>0){
								rlLeftImage.setVisibility(View.VISIBLE);
								setImage(petlist.get(currentpetindex-1).image, srivLeft);
							}else{
								rlLeftImage.setVisibility(View.GONE);
								srivLeft.setImageResource(R.drawable.icon_production_default);
							}
							if(currentpetindex<petlist.size()-1){
								rlRightImage.setVisibility(View.VISIBLE);
								setImage(petlist.get(currentpetindex+1).image, srivRight);
							}else{
								rlRightImage.setVisibility(View.GONE);
								srivRight.setImageResource(R.drawable.icon_production_default);
							}
							
						}
							
						
					}
				}
				
				break;
			
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void showPop(){
		pWin = null;
		if (pWin==null) {
			View view = mInflater.inflate(R.layout.dlg_order_all_style, null);
			ImageView textView_dlg_cancle = (ImageView) view.findViewById(R.id.imageView_dlg_cancle);
			TextView textView_dlg_pop_title = (TextView) view.findViewById(R.id.textView_dlg_pop_title);
			TextView textView_dlg_pop_content = (TextView) view.findViewById(R.id.textView_dlg_pop_content);
			TextView button_dlg_pop_press = (TextView) view.findViewById(R.id.button_dlg_pop_press);
			LinearLayout layout_dlg_all = (LinearLayout) view.findViewById(R.id.layout_dlg_all);
			LinearLayout llMain = (LinearLayout) view.findViewById(R.id.layout_low_all);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
			pWin.setBackgroundDrawable(new BitmapDrawable());
			pWin.setOutsideTouchable(true);
			pWin.setFocusable(true);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
//			pWin.setWidth(dm.widthPixels - 80);
//			pWin.setHeight(dm.heightPixels - dm.heightPixels/2);
			LayoutParams lp = new LayoutParams(dm.widthPixels - 80, 
					Utils.dip2px(PetInfoActivity.this, 220));
			llMain.setLayoutParams(lp);
					
			pWin.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
//			Utils.setAttribute(act, pWin);
			layout_dlg_all.setBackgroundColor(android.R.color.transparent);
			//关闭弹窗
			textView_dlg_cancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (pWin.isShowing()) {
						pWin.dismiss();
						pWin = null;
					}
					Utils.onDismiss(act);
				}
			});
			textView_dlg_pop_title.setText("添加成功了！~");
			textView_dlg_pop_content.setText("您的订单中还有"+mulpsList.size()+"只宠物可以添加至我的宠物呢，要继续添加吗？");
			button_dlg_pop_press.setText("继续添加");
			button_dlg_pop_press.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//这个是继续添加
//					JumpToNextForData(PetAddActivity.class, Global.PETINFO_ADDPET_POP,0,0);
					if (mulpsList.size()>0) {
						toNextId = mulpsList.get(0).petId;
						JumpToNext(PetAddActivity.class, Global.PETINFO_ADDPET_POP, mulpsList.get(0));
					}
					if (pWin.isShowing()) {
						pWin.dismiss();
						pWin = null;
					}
					Utils.onDismiss(act);
				}
			});
			pWin.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					Utils.onDismiss(act);
				}
			});
		}
	}
}
