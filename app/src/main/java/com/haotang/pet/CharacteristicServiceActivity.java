package com.haotang.pet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.MainCharacteristicServiceAdapter;
import com.haotang.pet.entity.MainCharacteristicService;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * <p>
 * Title:CharacteristicServiceActivity
 * </p>
 * <p>
 * Description:特色服务列表界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-14 下午4:59:01
 */
public class CharacteristicServiceActivity extends SuperActivity {
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private PullToRefreshListView listView_character;
	private MainCharacteristicServiceAdapter characterAdapter;
	private List<MainCharacteristicService> CharactList = new ArrayList<MainCharacteristicService>();
	private int page = 1;
	private MProgressDialog pDialog;
	private SharedPreferenceUtil spUtil;
	public static Activity act;
	private int previous;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character);
		act = this;
		MApplication.listAppoint.add(this);
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		findView();
		previous = getIntent().getIntExtra("previous", 0);
		spUtil.saveInt("charactservice", 1);
		getData();
		setData();
	}

	private void setData() {
		tv_titlebar_title.setText("特色服务");
		characterAdapter = new MainCharacteristicServiceAdapter<MainCharacteristicService>(
				this, CharactList);
		listView_character.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

		listView_character
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
					@Override
					public void onRefresh(PullToRefreshBase refreshView) {
						PullToRefreshBase.Mode mode = refreshView
								.getCurrentMode();
						if (mode == Mode.PULL_FROM_START) {
							CharactList.clear();
							characterAdapter.notifyDataSetChanged();
							getData();
						}
					}

				});
		//
		listView_character.setAdapter(characterAdapter);

		initListener();
	}

	private void initListener() {
		ib_titlebar_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goBack();
			}
		});

		listView_character.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (isLogin() && hasPet()) {
					if (spUtil.getInt("tareaid", 0) == 100) {
						goNext(ShopListActivity.class,
								String.valueOf(CharactList.get(position - 1).PetServiceTypeForListId),
								Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST,
								spUtil.getInt("petid", -1));
					} else {
						goNext(ServiceFeature.class, String.valueOf(CharactList
								.get(position - 1).PetServiceTypeForListId),
								Global.PRE_MAINFRAGMENT, 0);
					}
				} else {
					goNext(ChoosePetActivityNew.class,
							String.valueOf(CharactList.get(position - 1).PetServiceTypeForListId),
							Global.SERVICEFEATURE_TO_PETLIST, 0);
				}

			}
		});
	}

	private void findView() {
		listView_character = (PullToRefreshListView) findViewById(R.id.listView_character);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);

	}

	private boolean isLogin() {
		if (spUtil.getInt("userid", -1) > 0
				&& !"".equals(spUtil.getString("cellphone", "")))
			return true;
		return false;
	}

	private boolean hasPet() {
		if (spUtil.getInt("petid", -1) > 0)
			return true;
		return false;
	}

	// 上个订单的宠物是否有该服务
	private boolean hasService(int serviceid) {
		for (int i = 0; i < MainActivity.petServices.length; i++) {
			if (serviceid == MainActivity.petServices[i]) {
				return true;
			}
		}
		return false;
	}

	private void goNext(Class clazz, String servicename, int previous, int petid) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("servicename", servicename);
		intent.putExtra("previous", previous);
		intent.putExtra("petid", petid);
		intent.putExtra("typeId", Integer.parseInt(servicename));// 从特色服务跳转到门店列表专用，为了计算价格
		startActivity(intent);
	}

	private void goBack() {
		MApplication.listAppoint.remove(this);
		spUtil.removeData("charactservice");
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void getData() {
		pDialog.showDialog();
		CommUtil.getFe(this, getFe);
	}

	private AsyncHttpResponseHandler getFe = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			listView_character.onRefreshComplete();
			pDialog.closeDialog();

			try {
				Utils.mLogError("==-->str " + new String(responseBody));
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					if (jsonObject.has("data") && !jsonObject.isNull("data")) {
						JSONArray array = jsonObject.getJSONArray("data");
						if (array.length() > 0) {
							for (int i = 0; i < array.length(); i++) {
								CharactList.add(MainCharacteristicService
										.json2Entity(array.getJSONObject(i)));
							}
						}
					}
					characterAdapter.notifyDataSetChanged();
				} else {
					if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
						ToastUtil.showToastShortCenter(
								CharacteristicServiceActivity.this,
								jsonObject.getString("msg"));
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
			listView_character.onRefreshComplete();
			pDialog.closeDialog();
		}

	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("CharacteristicServiceActivity");// 统计页面(仅有Activity的应用中SDK自动调用，不需要
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("CharacteristicServiceActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）
		MobclickAgent.onPause(this);
	}

}
