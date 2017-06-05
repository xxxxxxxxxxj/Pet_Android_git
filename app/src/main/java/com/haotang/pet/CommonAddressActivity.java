package com.haotang.pet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Join;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CommonAddressApapter;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.swipemenulistview.SwipeMenu;
import com.haotang.pet.swipemenulistview.SwipeMenuCreator;
import com.haotang.pet.swipemenulistview.SwipeMenuItem;
import com.haotang.pet.swipemenulistview.SwipeMenuListView;
import com.haotang.pet.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.umeng.analytics.MobclickAgent;

public class CommonAddressActivity extends SuperActivity implements
		OnClickListener {

	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private Button bt_titlebar_other;
	private LinearLayout layout_add_common_address;
	private RelativeLayout layout_show_no_common_address,
			layout_show_common_address;
	private SwipeMenuListView mListView;
	private View footer;
	private Button button_footer_add_address;
	private List<String> HeaderSourceDateList;
	private CommonAddressApapter addressApapter;
	// private List<Map<String,Object>> addressList = new
	// ArrayList<Map<String,Object>>();
	private List<CommAddr> totalList = new ArrayList<CommAddr>();
	private int index = 0;
	private PopupWindow pWin;
	private LayoutInflater mInflater;
	private int removePosition;
	private MProgressDialog pDialog;
	private int previous;
	private int addrid = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_address);
		initView();
		try {
			previous = getIntent().getIntExtra("previous", 0);
			index = getIntent().getExtras().getInt("index");
			addrid = getIntent().getIntExtra("addrid", 0);
			Utils.mLogError("==-->addrid := " + addrid);
			if (previous == Global.BOOKINGSERVICEREQUESTCODE_ADDR) {
				index = 0;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pDialog = new MProgressDialog(this);
		pDialog.showDialog();
		initListener();
		// 有地址想要新添加
		button_footer_add_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (previous == Global.BOOKINGSERVICEREQUESTCODE_ADDR) {
					JumpToNextForData(AddServiceAddressActivity.class);
				} else {
					JumpToNext(AddServiceAddressActivity.class);
				}
			}
		});
		if (index == 1) {
			initMenuListView();
		} else {
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					addressApapter.setSeclection(position);
					addressApapter.notifyDataSetChanged();
					// Map<String,Object> amap = addressList.get(position);
					CommAddr amap = totalList.get(position);
					if (previous == Global.SWIM_APPOINMENT_CHOOSEADDRESS) {
						Intent intentSwim = new Intent();
						Bundle bundleSwim = new Bundle();
						bundleSwim.putString("addr", amap.address);
						bundleSwim.putDouble("addr_lng", amap.lng);
						bundleSwim.putDouble("addr_lat", amap.lat);
						bundleSwim.putInt("addr_id", amap.Customer_AddressId);
						bundleSwim.putInt("index", 1);
						intentSwim
								.setAction("android.intent.action.SwimAppointmentActivity");
						intentSwim.putExtras(bundleSwim);
						sendBroadcast(intentSwim);
					} else {
						// 给加急
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putString("addr", amap.address);
						bundle.putDouble("lng", amap.lng);
						bundle.putDouble("lat", amap.lat);
						bundle.putInt("addr_id", amap.Customer_AddressId);
						bundle.putInt("index", 0);
						intent.setAction("android.intent.action.UrgentFragment");
						intent.putExtras(bundle);
						sendBroadcast(intent);
					}
					// 貌似预约界面
					Intent data = new Intent();
					data.putExtra("addr", amap.address);
					data.putExtra("addr_lng", amap.lng);
					data.putExtra("addr_lat", amap.lat);
					data.putExtra("addr_id", amap.Customer_AddressId);
					setResult(Global.RESULT_OK, data);
					// SharedPreferenceUtil.getInstance(CommonAddressActivity.this).removeData("previous");
					finishWithAnimation();
				}
			});
		}

	}

	private void initMenuListView() {
		// 创建一个SwipeMenuCreator供ListView使用
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// 创建一个侧滑菜单
				// SwipeMenuItem openItem = new
				// SwipeMenuItem(getApplicationContext());
				// //给该侧滑菜单设置背景
				// openItem.setBackground(new ColorDrawable(Color.rgb(0xC9,
				// 0xC9,0xCE)));
				// //设置宽度
				// openItem.setWidth(dp2px(80));
				// //设置名称
				// openItem.setTitle("打开");
				// //字体大小
				// openItem.setTitleSize(18);
				// //字体颜色
				// openItem.setTitleColor(Color.WHITE);
				// //加入到侧滑菜单中
				// menu.addMenuItem(openItem);

				// 创建一个侧滑菜单
				SwipeMenuItem delItem = new SwipeMenuItem(
						getApplicationContext());
				// 给该侧滑菜单设置背景
				// delItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F,
				// 0x25)));
				delItem.setBackground(new ColorDrawable(Color
						.parseColor("#F95970")));
				// 设置宽度
				delItem.setWidth(dp2px(50));
				// 设置图片
				// delItem.setIcon(R.drawable.icon_del);
				delItem.setTitle("删除");
				delItem.setTitleSize(16);
				delItem.setTitleColor(Color.WHITE);
				// 加入到侧滑菜单中
				menu.addMenuItem(delItem);
			}
		};

		mListView.setMenuCreator(creator);

		// 侧滑菜单的相应事件
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				switch (index) {
				// case 0://第一个添加的菜单的响应时间(打开)
				// open(mAppList.get(position));
				// break;
				case 0:// 第二个添加的菜单的响应时间(删除)
					showSelectDialog(position);
					removePosition = position;

					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	private AsyncHttpResponseHandler deleteServiceAddress = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->delete " + new String(responseBody));
			try {
				pDialog.closeDialog();
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code == 0) {
					String msg = jsonObject.getString("msg");
					Toast.makeText(CommonAddressActivity.this, msg,
							Toast.LENGTH_SHORT).show();
					totalList.remove(removePosition);
					addressApapter.notifyDataSetChanged();
					if (totalList.size() <= 0) {
						bt_titlebar_other.setVisibility(View.GONE);
						button_footer_add_address.setVisibility(View.VISIBLE);
					} else {
						bt_titlebar_other.setVisibility(View.VISIBLE);

					}
					// showWhereLayout();//再次加载，当前用法会出现闪屏bug，效果体验稍差
				} else {
					// 服务器返回字段输出
					pDialog.closeDialog();
					String msg = jsonObject.getString("msg");
					ToastUtil.showToastShort(CommonAddressActivity.this, msg);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pDialog.closeDialog();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
		}

	};

	private void showWhereLayout() {
		// TODO 请求服务器判断显示那个布局
		totalList.clear();

		CommUtil.queryServiceAddress(
				SharedPreferenceUtil.getInstance(CommonAddressActivity.this)
						.getString("cellphone", ""),
				SharedPreferenceUtil.getInstance(CommonAddressActivity.this)
						.getInt("userid", 0), Global
						.getIMEI(CommonAddressActivity.this),
				CommonAddressActivity.this, queryServiceAddress);
	}

	private AsyncHttpResponseHandler queryServiceAddress = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->query address " + new String(responseBody));
			pDialog.closeDialog();
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");

				if (code == 0) {
					if (jsonObject.has("data") && !jsonObject.isNull("data")) {
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						if (jsonArray.length() > 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								totalList.add(CommAddr.json2Entity(jsonArray
										.getJSONObject(i)));
							}
						}
					}
					// //修复锁屏显示后删除按钮消失的bug
					if (bt_titlebar_other.getText().toString().equals("完成")) {
						for (int i = 0; i < totalList.size(); i++) {
							if (totalList.get(i).isSelected == false) {
								totalList.get(i).isSelected = true;
							}
						}
					}
					showAddr();

				} else if (code == 130) {
					Intent intent = new Intent(CommonAddressActivity.this,
							LoginActivity.class);
					intent.putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_RIGHT());
					getIntent().putExtra(Global.ANIM_DIRECTION(),
							Global.ANIM_COVER_FROM_LEFT());
					startActivity(intent);
					finishWithAnimation();
				} else {
					if (jsonObject.has("msg") && !jsonObject.isNull("msg")) {
						String msg = jsonObject.getString("msg");
						ToastUtil.showToastShort(CommonAddressActivity.this,
								msg);
					}
					showAddr();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				showAddr();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			showAddr();
		}

	};

	private void showAddr() {
		if (totalList.size() > 0) {
			bt_titlebar_other.setVisibility(View.VISIBLE);
			layout_show_common_address.setVisibility(View.VISIBLE);
			layout_show_no_common_address.setVisibility(View.GONE);
			addressApapter = new CommonAddressApapter(
					CommonAddressActivity.this, totalList, bt_titlebar_other,
					pDialog, button_footer_add_address);
			if (mListView.getFooterViewsCount() <= 0) {
				mListView.addFooterView(footer);
			}
			mListView.setAdapter(addressApapter);
			mListView.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					for (int i = 0; i < totalList.size(); i++) {
						if (totalList.get(i).Customer_AddressId == addrid) {
							// ImageView imgView = (ImageView)
							// mListView.getChildAt(i-1).findViewById(R.id.imageView_common_address_item_choose);
							// imgView.setVisibility(View.VISIBLE);
							// mListView.setItemChecked(i, true);
							try {
								addressApapter.setSeclection(i);
								addressApapter.notifyDataSetChanged();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			});
		} else {
			layout_show_no_common_address.setVisibility(View.VISIBLE);
			layout_show_common_address.setVisibility(View.GONE);
			bt_titlebar_other.setVisibility(View.GONE);
		}
		// 增加下单选择时进入地址编辑按钮隐藏
		if (index == 0) {
			bt_titlebar_other.setVisibility(View.GONE);
		}
	}

	private void initListener() {
		ib_titlebar_back.setOnClickListener(this);
		layout_add_common_address.setOnClickListener(this);

		bt_titlebar_other.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < totalList.size(); i++) {
					if (totalList.get(i).isSelected) {
						bt_titlebar_other.setText("编辑");
						button_footer_add_address.setVisibility(View.VISIBLE);
						totalList.get(i).isSelected = !totalList.get(i).isSelected;
					} else if (!totalList.get(i).isSelected) {
						bt_titlebar_other.setText("完成");
						button_footer_add_address.setVisibility(View.INVISIBLE);
						totalList.get(i).isSelected = !totalList.get(i).isSelected;
					}
				}
				addressApapter.notifyDataSetChanged();
			}
		});
	}

	private void initView() {
		mInflater = LayoutInflater.from(this);

		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		bt_titlebar_other.setText("编辑");
		bt_titlebar_other.setVisibility(View.INVISIBLE);
		layout_add_common_address = (LinearLayout) findViewById(R.id.layout_add_common_address);
		layout_show_no_common_address = (RelativeLayout) findViewById(R.id.layout_show_no_common_address);
		layout_show_common_address = (RelativeLayout) findViewById(R.id.layout_show_common_address);
		mListView = (SwipeMenuListView) findViewById(R.id.listView_show_common_add_and_delete);
		footer = LayoutInflater.from(this).inflate(
				R.layout.footer_common_address, null);
		button_footer_add_address = (Button) footer
				.findViewById(R.id.button_footer_add_address);
		tv_titlebar_title.setText("宠物地址");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_titlebar_back:
			finish();
			break;
		case R.id.layout_add_common_address:
			JumpToNext(AddServiceAddressActivity.class);
			break;
		case R.id.layout_show_no_common_address:

			break;
		default:
			break;
		}
	}

	// 将dp转化为px
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	private void JumpToNext(Class clazz) {
		Intent intent = new Intent(CommonAddressActivity.this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	private void JumpToNextForData(Class clazz) {
		Intent intent = new Intent(CommonAddressActivity.this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", Global.PRE_COMMONADDR_TO_ADDADDR);
		Utils.mLogError("----------------qidong---");
		startActivityForResult(intent, Global.PRE_COMMONADDR_TO_ADDADDR);
	}

	private void showSelectDialog(final int position) {
		MDialog mDialog = new MDialog.Builder(this).setTitle("提示")
				.setType(MDialog.DIALOGTYPE_CONFIRM).setMessage("是否确认删除?")
				.setCancelStr("否").setOKStr("是")
				.positiveListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// //请求服务器取消订单
						pDialog.showDialog();
						CommUtil.deleteServiceAddress(SharedPreferenceUtil
								.getInstance(CommonAddressActivity.this)
								.getString("cellphone", ""), Global
								.getIMEI(CommonAddressActivity.this),
								CommonAddressActivity.this, totalList
										.get(position).Customer_AddressId,
								deleteServiceAddress);

					}
				}).build();
		mDialog.show();
	}

	public void onDismiss() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 1f;
		getWindow().setAttributes(lp);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showWhereLayout();
		// for (int i = 0; i < addressList.size(); i++) {
		// if
		// (Integer.parseInt(addressList.get(i).get("Customer_AddressId").toString())==addrid)
		// {
		// mListView.setSelection(i);
		// }
		// }
		MobclickAgent.onPageStart("CommonAddressActivity");// 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("CommonAddressActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null)
			return;
		if (resultCode == Global.RESULT_OK) {
			if (requestCode == Global.PRE_COMMONADDR_TO_ADDADDR) {
				Intent rdata = new Intent();
				rdata.putExtra("addr", data.getStringExtra("addr"));
				rdata.putExtra("addr_lng", data.getDoubleExtra("addr_lng", 0));
				rdata.putExtra("addr_lat", data.getDoubleExtra("addr_lat", 0));
				rdata.putExtra("addr_id", data.getIntExtra("addr_id", 0));
				setResult(Global.RESULT_OK, rdata);
				finishWithAnimation();
			}
		}
	}

}
