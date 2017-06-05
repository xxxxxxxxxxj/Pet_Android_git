package com.haotang.pet;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.CertiOrder;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.luban.Luban;
import com.haotang.pet.luban.OnCompressListener;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PetAddActivity extends SuperActivity implements OnClickListener {
	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_CAMER = 2;
	private ImageButton ibBack;
	private String TEMPCERTIFICATIONNAME;
	private TextView tvTitle;
	private SelectableRoundedImageView srivImage;
	private EditText etPetName;
	private RelativeLayout rlPetkindName;
	private TextView tvPetkindName;
	private RadioButton rbPetSex1;
	private RadioButton rbPetSex2;
	private RelativeLayout rlPetBirthday;
	private TextView tvPetBirthday;
	private RadioButton rbPetHigh1;
	private RadioButton rbPetHigh2;
	private CheckBox cbNote1;
	private CheckBox cbNote2;
	private Button btSave;
	private ScrollView svMain;
	private RelativeLayout rlNull;
	private TextView tvMsg;
	private Button btRefresh;
	private Button btDelete;
	private PopupWindow pWin;
	private LayoutInflater mInflater;

	private File out;
	private Bitmap bmpImage;
	private String photoPath;
	private String bmpStr;
	private String birthday;
	private SharedPreferenceUtil spUtil;

	private int customerpetid;
	private int orderid;
	private int sex = -1;
	private int oldsex = -1;
	private int hight = -1;
	private int oldhight = -1;
	private String remark;
	private int petid;
	private int oldpetid;
	private String nickname;
	private MProgressDialog pDialog;
	private int previous;

	private int yearposition;
	private int monthposition;
	private int year;
	private int month;
	private int day;
	private int position = -1;

	// ---START 2016-3-15 13:25:06
	private LinearLayout layout_pet_add_show;
	private TextView textView_pet_add_showinfo;
	private ImageView imageView_pet_add_img;
	private MulPetService mulPetService;
	private ArrayList<MulPetService> mulpsList;
	private Iterator<MulPetService> itemulps;
	private LinearLayout ll_pet_add;
	private CertiOrder certiOrder;
	private EditText edit_pet_get_color;

	// ---END 2016-3-15 13:25:12
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pet_add);
		findView();
		setView();

	}

	private void findView() {
		mInflater = LayoutInflater.from(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		btDelete = (Button) findViewById(R.id.bt_titlebar_other);
		srivImage = (SelectableRoundedImageView) findViewById(R.id.sriv_petadd_photo);
		etPetName = (EditText) findViewById(R.id.et_petadd_petname);
		rlPetkindName = (RelativeLayout) findViewById(R.id.rl_petadd_petkindname);
		tvPetkindName = (TextView) findViewById(R.id.tv_petadd_petkindname);
		rbPetSex1 = (RadioButton) findViewById(R.id.rb_petadd_sex_1);
		rbPetSex2 = (RadioButton) findViewById(R.id.rb_petadd_sex_2);
		rlPetBirthday = (RelativeLayout) findViewById(R.id.rl_petadd_petbirthday);
		tvPetBirthday = (TextView) findViewById(R.id.tv_petadd_petbirthday);
		rbPetHigh1 = (RadioButton) findViewById(R.id.rb_petadd_high_1);
		rbPetHigh2 = (RadioButton) findViewById(R.id.rb_petadd_high_2);
		cbNote1 = (CheckBox) findViewById(R.id.cb_petadd_note_1);
		cbNote2 = (CheckBox) findViewById(R.id.cb_petadd_note_2);
		btSave = (Button) findViewById(R.id.bt_petadd_submit);

		svMain = (ScrollView) findViewById(R.id.sv_petadd_main);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvMsg = (TextView) findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);

		layout_pet_add_show = (LinearLayout) findViewById(R.id.layout_pet_add_show);
		textView_pet_add_showinfo = (TextView) findViewById(R.id.textView_pet_add_showinfo);
		imageView_pet_add_img = (ImageView) findViewById(R.id.imageView_pet_add_img);
		ll_pet_add = (LinearLayout) findViewById(R.id.ll_pet_add);
		edit_pet_get_color = (EditText) findViewById(R.id.edit_pet_get_color);
	}

	private void setView() {
		btDelete.setText("删除");
		btRefresh.setVisibility(View.VISIBLE);
		rbPetHigh1.setText(" <35cm");

		ibBack.setOnClickListener(this);
		btDelete.setOnClickListener(this);
		srivImage.setOnClickListener(this);
		rlPetkindName.setOnClickListener(this);
		rlPetBirthday.setOnClickListener(this);
		btSave.setOnClickListener(this);
		btRefresh.setOnClickListener(this);
		ll_pet_add.setOnClickListener(this);
		customerpetid = getIntent().getIntExtra("customerpetid", 0);
		position = getIntent().getIntExtra("position", -1);
		previous = getIntent().getIntExtra("previous", 0);
		certiOrder = (CertiOrder) getIntent()
				.getSerializableExtra("certiOrder");
		String nickname = getIntent().getStringExtra("nickname");
		if (previous == Global.MY_TO_ADDPET
				|| previous == Global.PETINFO_TO_ADDPET) {
			btDelete.setVisibility(View.GONE);
			tvTitle.setText("添加宠物");
		} else if (previous == Global.PETINFO_TO_EDITPET) {
			if (certiOrder != null) {
				int status = certiOrder.status;
				if (status == 0 || status == 6 || status == 7) {// 可删除，不可编辑
					btDelete.setVisibility(View.VISIBLE);
					setEn(false);
				} else {// 不可删除，也不可编辑
					btDelete.setVisibility(View.GONE);
					setEn(false);
				}
			} else {// 可删除，可编辑
				btDelete.setVisibility(View.VISIBLE);
				setEn(true);
			}
			tvTitle.setText(nickname);
			queryPet(customerpetid);
		} else if (previous == Global.PETINFO_ADDPET_POP) {
			mulPetService = getIntent().getParcelableExtra("mulpsList");// 从我的宠物跳转过来
			btDelete.setVisibility(View.GONE);
			tvTitle.setText("添加宠物");
			petid = mulPetService.petId;
			orderid = mulPetService.orderid;

			layout_pet_add_show.setVisibility(View.VISIBLE);
			textView_pet_add_showinfo.setText(mulPetService.petName
					+ mulPetService.serviceName);
			tvPetkindName.setText(mulPetService.petName);
			rlPetkindName.setOnClickListener(null);
			if (mulPetService.serviceName.contains("洗")) {
				imageView_pet_add_img
						.setBackgroundResource(R.drawable.icon_petinfo_wash);
			} else if (mulPetService.serviceName.contains("美")) {
				imageView_pet_add_img
						.setBackgroundResource(R.drawable.scissors_orange);
			}
		} else if (previous == Global.ORDERLIST_TO_ADD_PET) {
			btDelete.setVisibility(View.GONE);
			tvTitle.setText("添加宠物");
			mulpsList = getIntent().getParcelableArrayListExtra("mulpsList");// 从订单列表跳转过来
			if (mulpsList.size() > 0) {
				MulPetService mulPetServiceFromList = mulpsList.get(0);// 从列表过来取第一个宠物
				petid = mulPetServiceFromList.petId;
				orderid = mulPetServiceFromList.orderid;
				layout_pet_add_show.setVisibility(View.VISIBLE);
				textView_pet_add_showinfo.setText(mulPetServiceFromList.petName
						+ mulPetServiceFromList.serviceName);
				tvPetkindName.setText(mulPetServiceFromList.petName);
				rlPetkindName.setOnClickListener(null);
				if (mulPetServiceFromList.serviceType == 1) {
					imageView_pet_add_img
							.setBackgroundResource(R.drawable.icon_petinfo_wash);
				} else if (mulPetServiceFromList.serviceType == 2) {
					imageView_pet_add_img
							.setBackgroundResource(R.drawable.scissors_orange);
				} else if (mulPetServiceFromList.serviceType == 100) {
					imageView_pet_add_img
							.setBackgroundResource(R.drawable.foster);
				} else {
					imageView_pet_add_img
							.setBackgroundResource(R.drawable.icon_specialservice);
				}
			}
		}
		showMain(true);
	}

	private void setEn(boolean bool) {
		srivImage.setEnabled(bool);
		etPetName.setEnabled(bool);
		rlPetkindName.setEnabled(bool);
		rbPetSex1.setEnabled(bool);
		rbPetSex2.setEnabled(bool);
		rlPetBirthday.setEnabled(bool);
		rbPetHigh1.setEnabled(bool);
		rbPetHigh2.setEnabled(bool);
		cbNote1.setEnabled(bool);
		cbNote2.setEnabled(bool);
		btSave.setEnabled(bool);
	}

	private void showMain(boolean flag) {
		if (flag) {
			svMain.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			svMain.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_pet_add:
			goneImput(v);
			break;
		case R.id.ib_titlebar_back:
			// 返回
			goBack();
			break;
		case R.id.bt_titlebar_other:
			// 删除
			showBackDialog("主人确定不要我了么？", 2);
			break;
		case R.id.sriv_petadd_photo:
			goneImput(v);
			// 选择照片
			showSelectDialog();
			break;
		case R.id.rl_petadd_petkindname:
			// 选择宠物类型
			// goNextForData(ChoosePetActivity.class, Global.ADDPET_TO_PETLIST);
			goNextForData(ChoosePetActivityNew.class, Global.ADDPET_TO_PETLIST);

			break;
		case R.id.rl_petadd_petbirthday:
			goneImput(v);
			// 选择日期
			goNextForData(PetDateSelecterActivity.class, Global.PET_BIRTHDAY);
			break;
		case R.id.bt_petadd_submit:
			// 保存
			saveData(customerpetid, orderid);
			break;
		case R.id.bt_null_refresh:
			// 刷新
			queryPet(customerpetid);
			break;
		}

	}

	private void goneImput(View v) {
		// 隐藏键盘
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	private void goNextForData(Class clazz, int requestcode) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra("previous", requestcode);
		intent.putExtra("yearposition", yearposition);
		intent.putExtra("monthposition", monthposition);
		startActivityForResult(intent, requestcode);
	}

	private void sendBroadcastToMainUpdataUserinfo() {
		Utils.mLogError("去更新主界面");
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		intent.putExtra("previous", Global.DELETEPET_TO_UPDATEUSERINFO);
		sendBroadcast(intent);
	}

	private void saveData(int id, int orderid) {
		if (etPetName.getText().toString().trim().length() <= 0) {
			ToastUtil.showToastShortCenter(this, "还没有填写宝贝的名字呢");
			return;
		}
		if (tvPetkindName.getText().toString().trim().length() <= 0) {
			ToastUtil.showToastShortCenter(this, "还没有选择宝贝的品种呢");
			return;
		}
		if (!rbPetSex1.isChecked() && !rbPetSex2.isChecked()) {
			ToastUtil.showToastShortCenter(this, "还没有选择宝贝的性别呢");
			return;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());

		if (calendar.get(Calendar.YEAR) == year
				&& calendar.get(Calendar.MONTH) < month) {
			ToastUtil.showToastShortCenter(this, "请填写正确的宝贝生日哦");
			return;
		}
		if (rbPetSex1.isChecked())
			sex = 1;
		if (rbPetSex2.isChecked())
			sex = 0;
		if (rbPetHigh1.isChecked())
			hight = 0;
		if (rbPetHigh2.isChecked())
			hight = 1;
		StringBuffer sb = new StringBuffer();
		if (cbNote1.isChecked())
			sb.append("ym=1");
		if (cbNote2.isChecked()) {
			sb.append(";spayed=1");
		}
		pDialog.showDialog();
		CommUtil.newCustomerPet(this, spUtil.getString("cellphone", ""), Global
				.getCurrentVersion(this), Global.getIMEI(this), id, petid,
				tvPetkindName.getText().toString().trim(), etPetName.getText()
						.toString().trim(), sex, birthday, orderid, hight, sb
						.toString(), bmpStr, edit_pet_get_color.getText()
						.toString(), saveHandler);
	}

	private void deletePet(int id) {
		pDialog.showDialog();
		CommUtil.deleteCustomerPet(this, spUtil.getString("cellphone", ""),
				Global.getCurrentVersion(this), Global.getIMEI(this), id,
				deleteHandler);
	}

	private void queryPet(int id) {
		pDialog.showDialog();
		CommUtil.queryCustomerPetById(this, spUtil.getString("cellphone", ""),
				Global.getCurrentVersion(this), Global.getIMEI(this), id,
				queryHandler);
	}

	private void goBack() {
		if (rbPetSex1.isChecked())
			sex = 1;
		if (rbPetSex2.isChecked())
			sex = 0;
		if (rbPetHigh1.isChecked())
			hight = 0;
		if (rbPetHigh2.isChecked())
			hight = 1;
		if (isChanged())
			showBackDialog("离开页面后填写的内容会消失，确定离开？", 1);
		else
			finishWithAnimation();
	}

	private boolean isChanged() {
		if (bmpImage != null)
			return true;
		if ((nickname != null && !etPetName.getText().toString().trim()
				.equals(nickname))
				|| (nickname == null && etPetName.getText().toString().trim()
						.length() > 0))
			return true;
		if (petid != oldpetid)
			return true;
		if (oldsex != sex)
			return true;
		if (birthday != null && !"".equals(birthday))
			return true;
		if (hight != oldhight)
			return true;
		StringBuffer sb = new StringBuffer();
		if (cbNote1.isChecked())
			sb.append("ym=1");
		if (cbNote2.isChecked()) {
			sb.append(";spayed=1");
		}
		if ((remark != null && !sb.toString().trim().equals(remark))
				|| (remark == null && sb.toString().trim().length() > 0))
			return true;
		return false;
	}

	/**
	 * 
	 * @param hint
	 *            提示语
	 * @param flag
	 *            1为返回 2为删除
	 */
	private void showBackDialog(String hint, final int flag) {
		MDialog mDialog = new MDialog.Builder(this)
				.setType(MDialog.DIALOGTYPE_CONFIRM).setMessage(hint)
				.setCancelTextColor(getResources().getColor(R.color.orange))
				.setCancelStr("取消").setOKStr("确定")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (flag == 1) {
							PetAddActivity.this.finishWithAnimation();
							if (PetInfoDeatilActivity.act != null)
								PetInfoDeatilActivity.act.finishWithAnimation();
						} else if (flag == 2) {
							// 删除
							deletePet(customerpetid);
						}
					}
				}).build();
		mDialog.show();
	}

	private void showSelectDialog() {
		// TODO pop
		pWin = null;
		if (pWin == null) {
			View view = mInflater.inflate(R.layout.dlg_choose_icon, null);
			LinearLayout pop_getIcon_action = (LinearLayout) view
					.findViewById(R.id.pop_getIcon_action);
			LinearLayout pop_getIcon_local = (LinearLayout) view
					.findViewById(R.id.pop_getIcon_local);
			LinearLayout pop_getIcon_cancle = (LinearLayout) view
					.findViewById(R.id.pop_getIcon_cancle);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
			pWin.setFocusable(true);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			pWin.setWidth(dm.widthPixels/* - 40 */);
			pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);

			// 拍照
			pop_getIcon_action.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					TEMPCERTIFICATIONNAME = System.currentTimeMillis()
							+ "_pro.jpg";
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							.fromFile(new File(Utils
									.getPetPath(PetAddActivity.this),
									TEMPCERTIFICATIONNAME)));

					startActivityForResult(intent, SELECT_CAMER);
					pWin.dismiss();
					pWin = null;
				}
			});
			// 本地获取图片
			pop_getIcon_local.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					startActivityForResult(intent, SELECT_PICTURE);
					pWin.dismiss();
					pWin = null;
				}
			});
			pop_getIcon_cancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					pWin.dismiss();
					pWin = null;
				}
			});

		}
	}

	private AsyncHttpResponseHandler saveHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			Utils.mLogError("保存：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						if (jdata.has("petId") && !jdata.isNull("petId")) {
							if (PetInfoDeatilActivity.act != null)
								PetInfoDeatilActivity.act.finishWithAnimation();
							customerpetid = jdata.getInt("petId");
							if (previous == Global.MY_TO_ADDPET
									|| previous == Global.PETINFO_TO_ADDPET) {
								ToastUtil.showToastShortCenter(
										getApplicationContext(), "添加宠物成功");
							} else if (previous == Global.PETINFO_TO_EDITPET) {
								ToastUtil.showToastShortCenter(
										getApplicationContext(), "保存成功");
							}
							Intent data = new Intent();
							data.putExtra("editflag", 1);
							data.putExtra("customerpetid", customerpetid);
							if (orderid > 0)
								sendBroadcastToMainUpdataUserinfo();
							if (previous == Global.ORDERLIST_TO_ADD_PET) {
							} else {
								setResult(Global.RESULT_OK, data);
							}
							PetAddActivity.this.finishWithAnimation();
						}
					}
				} else {
					ToastUtil.showToastShortCenter(getApplicationContext(),
							jobj.getString("msg"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (previous == Global.MY_TO_ADDPET
						|| previous == Global.PETINFO_TO_ADDPET) {
					ToastUtil.showToastShortCenter(getApplicationContext(),
							"添加宠物失败，请重新保存");
				} else if (previous == Global.PETINFO_TO_EDITPET) {
					ToastUtil.showToastShortCenter(getApplicationContext(),
							"保存失败，请重新保存");
				}
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			if (previous == Global.MY_TO_ADDPET
					|| previous == Global.PETINFO_TO_ADDPET) {
				ToastUtil.showToastShortCenter(getApplicationContext(),
						"添加宠物失败，请重新保存");
			} else if (previous == Global.PETINFO_TO_EDITPET) {
				ToastUtil.showToastShortCenter(getApplicationContext(),
						"保存失败，请重新保存");
			}
		}

	};
	private AsyncHttpResponseHandler deleteHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			Utils.mLogError("删除：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("errno");
				if (0 == resultCode) {
					sendBroadcastToMainUpdataUserinfo();
					ToastUtil.showToastShortCenter(getApplicationContext(),
							"删除成功");
					if (CustomerPetInfoActivity.act != null) {
						CustomerPetInfoActivity.act.finishWithAnimation();
					}
					if (PetInfoDeatilActivity.act != null)
						PetInfoDeatilActivity.act.finishWithAnimation();
					finishWithAnimation();
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg"))
						ToastUtil.showToastShort(PetAddActivity.this,
								jobj.getString("msg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				ToastUtil.showToastShortCenter(getApplicationContext(),
						"删除失败，请重新操作");
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			ToastUtil.showToastShortCenter(getApplicationContext(),
					"删除失败，请重新操作");
		}

	};
	private AsyncHttpResponseHandler queryHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			Utils.mLogError("查询：" + new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jdata = jobj.getJSONObject("data");
						if (jdata.has("pet") && !jdata.isNull("pet")) {
							JSONObject jpet = jdata.getJSONObject("pet");
							if (jpet.has("nickName")
									&& !jpet.isNull("nickName")) {
								nickname = jpet.getString("nickName").trim();
								etPetName.setText(nickname.trim());
								// 把光标放在最后
								CharSequence text = etPetName.getText();
								if (text instanceof Spannable) {
									Spannable spantext = (Spannable) text;
									Selection.setSelection(spantext,
											text.length());
								}
							}
							if (jpet.has("petName") && !jpet.isNull("petName")) {
								tvPetkindName
										.setText(jpet.getString("petName"));
							}
							if (jpet.has("color") && !jpet.isNull("color")) {
								edit_pet_get_color.setText(jpet
										.getString("color"));
							}
							if (jpet.has("petId") && !jpet.isNull("petId")) {
								petid = jpet.getInt("petId");
								oldpetid = petid;
							}

							if (jpet.has("sex") && !jpet.isNull("sex")) {
								oldsex = jpet.getInt("sex");
								if (oldsex == 1)
									rbPetSex1.setChecked(true);
								else if (oldsex == 0)
									rbPetSex2.setChecked(true);
							}
							if (jpet.has("height") && !jpet.isNull("height")) {
								oldhight = jpet.getInt("height");
								if (oldhight == 1)
									rbPetHigh2.setChecked(true);
								else if (oldhight == 0)
									rbPetHigh1.setChecked(true);
							}
							if (jpet.has("remark") && !jpet.isNull("remark")) {
								remark = jpet.getString("remark");
								if (remark.contains("ym=1"))
									cbNote1.setChecked(true);
								if (remark.contains("spayed=1"))
									cbNote2.setChecked(true);
							}
							if (jpet.has("birthday")
									&& !jpet.isNull("birthday")) {
								// tvPetBirthday.setText(jpet.getString("birthday").substring(0,jpet.getString("birthday").lastIndexOf("-")));
								// tvPetBirthday.setText(jpet.getString("birthday"));
								tvPetBirthday.setText(jpet
										.getString("birthday").split(" ")[0]);
							}
							if (jpet.has("avatarPath")
									&& !jpet.isNull("avatarPath")) {
								ImageLoaderUtil.loadImg(
										jpet.getString("avatarPath"),
										srivImage, 0,
										new ImageLoadingListener() {

											@Override
											public void onLoadingStarted(
													String arg0, View arg1) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onLoadingFailed(
													String arg0, View arg1,
													FailReason arg2) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onLoadingComplete(
													String path, View view,
													Bitmap bitmap) {
												// TODO Auto-generated method
												// stub
												if (path != null
														&& !"".equals(path)
														&& bitmap != null) {
													SelectableRoundedImageView sv = (SelectableRoundedImageView) view;
													sv.setImageBitmap(bitmap);
												}
											}

											@Override
											public void onLoadingCancelled(
													String arg0, View arg1) {
												// TODO Auto-generated method
												// stub

											}
										});
							}
						}
					}
				} else {
					ToastUtil.showToastShortCenter(getApplicationContext(),
							jobj.getString("msg"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastUtil.showToastShortCenter(getApplicationContext(),
						"获取宠物信息，请刷新");
				showMain(false);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
			ToastUtil.showToastShortCenter(getApplicationContext(),
					"获取宠物信息，请刷新");
			showMain(false);
		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK || resultCode == Global.RESULT_OK) {
			switch (requestCode) {
			// 从相册选择
			case SELECT_PICTURE:
				// TODO 周一更新 目前从本地选择有问题
				// Uri uri = data.getData();
				// String photoPath =
				// RealPathUtil.getRealPathFromURI(PetAddActivity.this, uri);
				// bmpImage = Utils.getxtsldraw(PetAddActivity.this,photoPath);
				// if(bmpImage!=null)
				// srivImage.setImageBitmap(bmpImage);

				if (data == null) {
					ToastUtil.showToastShortCenter(PetAddActivity.this,
							"您选择的照片不存在，请重新选择");
					return;
				}
				try {
					Uri originalUri = data.getData(); // 获得图片的uri
					if (!TextUtils.isEmpty(originalUri.getAuthority())) {
						// 这里开始的第二部分，获取图片的路径：
						String[] proj = { MediaStore.Images.Media.DATA };
						// Cursor cursor = managedQuery(originalUri, proj, null,
						// null, null);
						Cursor cursor = getContentResolver().query(originalUri,
								proj, null, null, null);
						// 获得用户选择的图片的索引值
						int column_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						// 最后根据索引值获取图片路径
						photoPath = cursor.getString(column_index);

						cursor.close();
						// 华为及其他手机系统的图片Url获取
						compressWithLs(new File(photoPath));
					} else {
						// 小米系统走的方法
						compressWithLs(new File(originalUri.getPath()));
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				break;
			// 拍照添加图片
			case SELECT_CAMER:
				// bmpImage = (Bitmap) data.getExtras().get("data");
				// bmpImage = Utils.getxtsldraw(getApplicationContext(),
				// out.getAbsolutePath());
				// if(bmpImage!=null)
				// srivImage.setImageBitmap(bmpImage);

				File file = new File(Utils.getPetPath(PetAddActivity.this),
						TEMPCERTIFICATIONNAME);
				if (file != null && file.length() > 0) {
					photoPath = file.getAbsolutePath();
					compressWithLs(new File(photoPath));
				} else {
					Toast.makeText(PetAddActivity.this, "您选择的照片不存在，请重新选择",
							Toast.LENGTH_SHORT).show();
				}

				break;
			case Global.PET_BIRTHDAY:
				year = data.getIntExtra("year", 0);
				month = data.getIntExtra("month", 0);
				day = data.getIntExtra("day", 0);
				yearposition = data.getIntExtra("yearposition", 0);
				monthposition = data.getIntExtra("monthposition", 0);
				// Calendar calendar = Calendar.getInstance();
				// calendar.set(year, month, day);
				// calendar.set(year, month, day);
				Utils.mLogError("==-->pet aaaaa year  " + year + " month  "
						+ month + " day " + day);
				// birthday = Utils.formatDateAll(calendar.getTimeInMillis());
				getBirthday();
				tvPetBirthday.setText(birthday.split(" ")[0]/*
															 * birthday.subSequence
															 * (0,birthday.
															 * lastIndexOf("-"))
															 */);
				Utils.mLogError("==--> pet add " + birthday);
				break;
			case Global.ADDPET_TO_PETLIST:
				petid = data.getIntExtra("petid", 0);
				tvPetkindName.setText(data.getStringExtra("petname"));
				break;
			}
		}
	}

	private void getBirthday() {
		StringBuilder sb = new StringBuilder();
		String yearStr = year + "";
		sb.append(yearStr + "-");
		String monthStr = month + "";
		if (monthStr.length() == 1) {
			monthStr = "0" + monthStr;
			sb.append(monthStr + "-");
		} else {
			sb.append(monthStr + "-");
		}
		String dayStr = day + "";
		if (dayStr.length() == 1) {
			dayStr = "0" + dayStr;
			sb.append(dayStr + " ");
		} else {
			sb.append(dayStr + " ");
		}
		sb.append("00:00:00");
		birthday = sb.toString();
	}

	/**
	 * 压缩单张图片 Listener 方式
	 */
	private void compressWithLs(File file) {
		Luban.get(this).load(file).putGear(Luban.THIRD_GEAR)
				.setCompressListener(new OnCompressListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(File file) {
						Bitmap getxtsldraw = Utils.getxtsldraw(
								PetAddActivity.this, file.getAbsolutePath());
						srivImage.setImageBitmap(getxtsldraw);
						bmpStr = Global.encodeWithBase64(getxtsldraw);// 这句话将图片转码后发送给服务器
					}

					@Override
					public void onError(Throwable e) {

					}
				}).launch();
	}

	/**
	 * 压缩单张图片 RxJava 方式
	 */
	private void compressWithRx(File file) {
		Luban.get(this)
				.load(file)
				.putGear(Luban.THIRD_GEAR)
				.asObservable()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnError(new Action1<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						throwable.printStackTrace();
					}
				})
				.onErrorResumeNext(
						new Func1<Throwable, Observable<? extends File>>() {
							@Override
							public Observable<? extends File> call(
									Throwable throwable) {
								return Observable.empty();
							}
						}).subscribe(new Action1<File>() {
					@Override
					public void call(File file) {
					}
				});
	}
}
