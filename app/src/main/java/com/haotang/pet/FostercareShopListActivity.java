package com.haotang.pet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.FostercareShopListAdapter;
import com.haotang.pet.entity.ShopsWithPrice;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

@SuppressLint("ResourceAsColor")
public class FostercareShopListActivity extends SuperActivity {
	private final static long DAYTIMEINMILLS = 86400000;
	public static SuperActivity act;
	private ImageButton ibBack;
	private TextView tvTitle;
	private PullToRefreshListView prlvList;
	private RelativeLayout rlNull;
	private TextView tvMsg;
	private Button btRefresh;
	private MProgressDialog pDialog;
	private Intent fIntent;
	private int petid;
	private int monthposition;
	private int daynum;
	private String starttime;
	private String endtime;
	private String startdatetxt;
	private String enddatetxt;
	private double lat;
	private double lng;
	private FostercareShopListAdapter adapter;
	private ArrayList<ShopsWithPrice> shopList = new ArrayList<ShopsWithPrice>();

	private PopupWindow pWin;
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fostercareshoplist);
		act = this;
		findView();
		setView();
	}

	private void findView() {
		mInflater = LayoutInflater.from(this);
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		prlvList = (PullToRefreshListView) findViewById(R.id.prl_fostercareshoplist_list);
		rlNull = (RelativeLayout) findViewById(R.id.rl_null);
		tvMsg = (TextView) findViewById(R.id.tv_null_msg1);
		btRefresh = (Button) findViewById(R.id.bt_null_refresh);
	}

	private void setView() {
		// TODO Auto-generated method stub
		tvTitle.setText("门店列表");
		tvMsg.setText("服务很繁忙吆，刷新试试");
		btRefresh.setVisibility(View.VISIBLE);
		pDialog = new MProgressDialog(this);
		fIntent = getIntent();

		petid = fIntent.getIntExtra("petid", 0);
		monthposition = fIntent.getIntExtra("monthposition", 0);
		starttime = fIntent.getStringExtra("startdate");
		endtime = fIntent.getStringExtra("enddate");
		enddatetxt = fIntent.getStringExtra("enddatetxt");
		startdatetxt = fIntent.getStringExtra("startdatetxt");
		lat = fIntent.getDoubleExtra("lat", 0);
		lng = fIntent.getDoubleExtra("lng", 0);
		daynum = fIntent.getIntExtra("daynum", 0);

		prlvList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		prlvList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				// 下拉刷新
				shopList.clear();
				adapter.notifyDataSetChanged();
				getData();
			}
		});
		prlvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				if (shopList.get(position - 1).status == 2) {
					showPop(shopList.get(position - 1).hotelImg);// 门店未开业
				} else {
					goNext(position - 1, shopList.get(position - 1).shopId);
				}
			}
		});
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goBack();
			}
		});
		btRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getData();
			}
		});

		adapter = new FostercareShopListAdapter(this, shopList);
		prlvList.setAdapter(adapter);
		getData();
	}

	private void showMain(boolean flag) {
		if (flag) {
			prlvList.setVisibility(View.VISIBLE);
			rlNull.setVisibility(View.GONE);
		} else {
			prlvList.setVisibility(View.GONE);
			rlNull.setVisibility(View.VISIBLE);
		}
	}

	private void goNext(int position, int shopid) {
		Intent intent = new Intent(this, FostercareChooseroomActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());

		intent.putExtra("petid", petid);
		intent.putExtra("shopid", shopid);
		intent.putExtra("petname", fIntent.getStringExtra("petname"));
		intent.putExtra("petkind", fIntent.getIntExtra("petkind", 0));
		intent.putExtra("customerpetid",
				fIntent.getIntExtra("customerpetid", 0));
		intent.putExtra("customerpetname",
				fIntent.getStringExtra("customerpetname"));
		intent.putExtra("addrid", fIntent.getIntExtra("addrid", 0));
		intent.putExtra("addr", fIntent.getStringExtra("addr"));
		intent.putExtra("lat", lat);
		intent.putExtra("lng", lng);
		intent.putExtra("startdate", starttime);
		intent.putExtra("enddate", endtime);
		intent.putExtra("startdatetxt", startdatetxt);
		intent.putExtra("enddatetxt", enddatetxt);
		intent.putExtra("daynum", daynum);
		intent.putExtra("monthposition", monthposition);
		startActivityForResult(intent, Global.SHOPLIST_TO_ROOMLIST);
	}

	private void goBack() {
		Intent data = new Intent();
		data.putExtra("startdate", starttime);
		data.putExtra("enddate", endtime);
		data.putExtra("monthposition", monthposition);
		data.putExtra("fromroom", true);
		setResult(Global.RESULT_OK, data);
		finishWithAnimation();
	}

	private void getData() {
		pDialog.showDialog();
		shopList.clear();
		adapter.notifyDataSetChanged();
		StringBuilder sbstart = new StringBuilder();
		StringBuilder sbend = new StringBuilder();
		sbstart.append(starttime);
		sbstart.append(" 00:00:00");
		sbend.append(endtime);
		sbend.append(" 00:00:00");
		/*CommUtil.getShopList(this, petid, sbstart.toString(), sbend.toString(),
				lat, lng, shoplistHandler);*/
	}

	private AsyncHttpResponseHandler shoplistHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("店铺列表： " + new String(responseBody));
			pDialog.closeDialog();
			prlvList.onRefreshComplete();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int code = jobj.getInt("code");
				if (code == 0 && jobj.has("data") && !jobj.isNull("data")) {
					showMain(true);
					JSONArray jarr = jobj.getJSONArray("data");
					int length = jarr.length();
					for (int i = 0; i < length; i++) {
						shopList.add(ShopsWithPrice.json2Entity(jarr
								.getJSONObject(i)));
					}
					if (length > 0) {
						if (length == 1) {
							if (shopList.get(0).status == 2) {
								showPop(shopList.get(0).hotelImg);// 门店未开业
							} else {
								goNext(0, shopList.get(0).shopId);
								finish();
							}
						} else {
							adapter.notifyDataSetChanged();
						}
					}
				} else {
					showMain(false);
					if (jobj.has("msg") && !jobj.isNull("msg"))
						ToastUtil.showToastShortCenter(
								FostercareShopListActivity.this,
								jobj.getString("msg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				showMain(false);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
			prlvList.onRefreshComplete();
			showMain(false);
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Global.RESULT_OK) {
			if (requestCode == Global.SHOPLIST_TO_ROOMLIST) {
				// 日期
				starttime = data.getStringExtra("startdate");
				endtime = data.getStringExtra("enddate");
				monthposition = data.getIntExtra("monthposition", 0);
				daynum = (int) ((getTimeInMills(endtime) - getTimeInMills(starttime)) / DAYTIMEINMILLS);
				String[] arr = starttime.split("-");
				StringBuilder sb = new StringBuilder();
				if (arr.length > 2) {
					sb.append(arr[1]);
					sb.append("月");
					sb.append(arr[2]);
					sb.append("日");
				}
				startdatetxt = sb.toString();
				arr = endtime.split("-");
				StringBuilder sb1 = new StringBuilder();
				if (arr.length > 2) {
					sb1.append(arr[1]);
					sb1.append("月");
					sb1.append(arr[2]);
					sb1.append("日");
				}
				enddatetxt = sb1.toString();
				getData();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	private long getTimeInMills(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			return calendar.getTimeInMillis();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	private void showPop(String imgUrl) {
		pWin = null;
		if (pWin == null) {
			View view = mInflater.inflate(R.layout.not_practice, null);
			ImageView imageView_not_practice = (ImageView) view
					.findViewById(R.id.imageView_not_practice);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
			pWin.setBackgroundDrawable(new BitmapDrawable());
			pWin.setOutsideTouchable(true);
			pWin.setFocusable(true);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			// pWin.setWidth(dm.widthPixels - 80);
			// pWin.setHeight(dm.heightPixels - dm.heightPixels/2);
			pWin.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
			Utils.setAttribute(FostercareShopListActivity.this, pWin);
			ImageLoaderUtil.loadImg(imgUrl, imageView_not_practice, 0,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingComplete(String path, View view,
								Bitmap bitmap) {
							// TODO Auto-generated method stub
							if (view != null && bitmap != null) {
								ImageView iv = (ImageView) view;
								iv.setImageBitmap(bitmap);
							}
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}
					});
			imageView_not_practice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (pWin.isShowing()) {
						pWin.dismiss();
						pWin = null;
					}
					Utils.onDismiss(FostercareShopListActivity.this);
				}
			});
			pWin.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					Utils.onDismiss(FostercareShopListActivity.this);
				}
			});
		}
	}

}
