package com.haotang.pet.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haotang.pet.R;
import com.haotang.pet.SelectTimeOrUrgentActivity;
import com.haotang.pet.adapter.AppointFragBeauAdapter;
import com.haotang.pet.adapter.ContentTimeAdapter;
import com.haotang.pet.adapter.TopTimeDateAdapter;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.entity.TimeListCodeBean;
import com.haotang.pet.entity.TimeListCodeBean.DataBean;
import com.haotang.pet.entity.TimeListCodeBean.DataBean.SelectionBean;
import com.haotang.pet.entity.TimeListCodeBean.DataBean.SelectionBean.TimesBean;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MyGridView;

/**
 * <p>
 * Title:AppointmentFragment
 * </p>
 * <p>
 * Description:预约时间界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-18 上午10:20:50
 */
@SuppressLint("NewApi")
public class AppointmentFragment extends Fragment implements OnClickListener {
	private Activity context;
	private MyGridView gv_appointfrg_content;
	private GridView gv_appointfrg_top;
	private TextView tv_appointfrg_ts;
	private TextView tvDayNum;
	private SharedPreferenceUtil spUtil;
	private Intent fIntent;
	private int shopid;
	private int areaid;
	private int dayposition = -1;
	private int beauticianId;
	private int serviceloc;
	private String strp;
	private MProgressDialog pDialog;
	private TopTimeDateAdapter topTimeDateAdapter;
	private ContentTimeAdapter contentTimeAdapter;
	protected String selectedDate = "";
	protected String selectedTime = "";
	private String date = "";
	private HorizontalScrollView hsv_appointfrg_date;
	private int itemwidth;
	private int vieEnabled;
	private ScrollView sv_appointfrg;
	private TextView button_appointfrg_jsyy;
	public boolean isBack;
	private double lat;
	private double lng;
	protected String strListWokerId = "";
	private List<SelectionBean> selection;
	private Timer timer;
	private TimerTask task;
	private ArrayMap<String, ArrayList<Beautician>> beauticianMap = new ArrayMap<String, ArrayList<Beautician>>();
	private TextView btBeauticianLevel1;
	private TextView btBeauticianLevel2;
	private TextView btBeauticianLevel3;
	private int clicksort;
	private RelativeLayout rl_appointment_switchbau;
	private View vw_appoint_beautician;
	private MyGridView mgv_appointfrg_contentbeau;
	private List<Beautician> localBeauList = new ArrayList<Beautician>();
	private AppointFragBeauAdapter<Beautician> appointFragBeauAdapter;
	private Button btn_appointfrag_submit;
	private Beautician localBautician;
	private PopupWindow pWin;

	public AppointmentFragment(Activity context, int vieEnabled, double lat,
			double lng) {
		this.lat = lat;
		this.lng = lng;
		this.context = context;
		this.vieEnabled = vieEnabled;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.appointmentfragment_layout,
				container, false);
		initData();
		initView(view);
		setView();
		initLinster();
		getData(beauticianId);
		return view;
	}

	private void setView() {
		localBeauList.clear();
		appointFragBeauAdapter = new AppointFragBeauAdapter<Beautician>(
				context, localBeauList);
		mgv_appointfrg_contentbeau.setAdapter(appointFragBeauAdapter);
	}

	private void initLinster() {
		tv_appointfrg_ts.setOnClickListener(this);
		button_appointfrg_jsyy.setOnClickListener(this);
		btBeauticianLevel1.setOnClickListener(this);
		btBeauticianLevel2.setOnClickListener(this);
		btBeauticianLevel3.setOnClickListener(this);
		btn_appointfrag_submit.setOnClickListener(this);
		gv_appointfrg_top.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dayposition = position;
				selectedDate = selection.get(position).getDate();
				date = selection.get(position).getYear() + "-" + selectedDate;
				if (topTimeDateAdapter != null) {
					topTimeDateAdapter.setClickItem(position);
					setTs(position);
					contentTimeAdapter = new ContentTimeAdapter(context,
							selection.get(position).getTimes(), selection.get(
									position).getIsFull());
					gv_appointfrg_content.setAdapter(contentTimeAdapter);
					contentTimeAdapter.notifyDataSetChanged();
				}
				ScollTo(dayposition);
				rl_appointment_switchbau.setVisibility(View.GONE);
				vw_appoint_beautician.setVisibility(View.GONE);
				mgv_appointfrg_contentbeau.setVisibility(View.GONE);
				btn_appointfrag_submit.setVisibility(View.GONE);
				localBeauList.clear();
			}
		});
		gv_appointfrg_content.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (selection != null && selection.size() > 0
						&& selection.size() > dayposition) {
					SelectionBean selectionBean = selection.get(dayposition);
					if (selectionBean != null) {
						List<TimesBean> times = selectionBean.getTimes();
						if (times != null && times.size() > 0
								&& times.size() > position) {
							TimesBean timesBean = times.get(position);
							if (timesBean != null) {
								selectedTime = timesBean.getTime();
								List<Integer> workers = timesBean.getWorkers();
								if (workers != null && workers.size() > 0) {
									String str = "";
									if (workers != null && workers.size() > 0) {
										for (int i = 0; i < workers.size(); i++) {
											str = str + "," + workers.get(i);
										}
									}
									strListWokerId = str.substring(1,
											str.length());
									Log.e("TAG", "appfrag strListWokerId = "+strListWokerId);
									rl_appointment_switchbau
											.setVisibility(View.GONE);
									vw_appoint_beautician
											.setVisibility(View.GONE);
									mgv_appointfrg_contentbeau
											.setVisibility(View.GONE);
									btn_appointfrag_submit
											.setVisibility(View.GONE);
									pDialog.showDialog();
									CommUtil.queryAvailableWorkers(context,
											strListWokerId,
											spUtil.getString("cellphone", ""),
											Global.getIMEI(context),
											Global.getCurrentVersion(context),
											areaid, date + " " + selectedTime
													+ ":00", serviceloc,
											shopid, lat, lng, strp,
											BeauticianHanler);
								}
							}
						}
					}
				}
				if (contentTimeAdapter != null) {
					contentTimeAdapter.setClickItem(position);
					// getTime();
				}
			}
		});
		mgv_appointfrg_contentbeau
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (localBeauList != null && localBeauList.size() > 0
								&& localBeauList.size() > position) {
							localBautician = localBeauList.get(position);
							if (localBautician != null) {
								appointFragBeauAdapter.setClick(position);
							}
						}
					}
				});
	}

	private void initView(View view) {
		btn_appointfrag_submit = (Button) view
				.findViewById(R.id.btn_appointfrag_submit);
		rl_appointment_switchbau = (RelativeLayout) view
				.findViewById(R.id.rl_appointment_switchbau);
		vw_appoint_beautician = (View) view
				.findViewById(R.id.vw_appoint_beautician);
		mgv_appointfrg_contentbeau = (MyGridView) view
				.findViewById(R.id.mgv_appointfrg_contentbeau);
		btBeauticianLevel1 = (TextView) view
				.findViewById(R.id.tv_servicedetail_beautician_level1);
		btBeauticianLevel2 = (TextView) view
				.findViewById(R.id.tv_servicedetail_beautician_level2);
		btBeauticianLevel3 = (TextView) view
				.findViewById(R.id.tv_servicedetail_beautician_level3);
		gv_appointfrg_content = (MyGridView) view
				.findViewById(R.id.gv_appointfrg_content);
		gv_appointfrg_top = (GridView) view
				.findViewById(R.id.gv_appointfrg_top);
		tv_appointfrg_ts = (TextView) view.findViewById(R.id.tv_appointfrg_ts);
		button_appointfrg_jsyy = (TextView) view
				.findViewById(R.id.button_appointfrg_jsyy);
		hsv_appointfrg_date = (HorizontalScrollView) view
				.findViewById(R.id.hsv_appointfrg_date);
		tvDayNum = (TextView) view.findViewById(R.id.tv_appointfrg_time);
		sv_appointfrg = (ScrollView) view.findViewById(R.id.sv_appointfrg);
	}

	private void initData() {
		pDialog = new MProgressDialog(context);
		spUtil = SharedPreferenceUtil.getInstance(context);
		fIntent = context.getIntent();
		serviceloc = fIntent.getIntExtra("serviceloc", 2);
		shopid = fIntent.getIntExtra("shopid", -1);
		clicksort = fIntent.getIntExtra("clicksort", 0);
		shopid = fIntent.getIntExtra("shopid", -1);
		areaid = fIntent.getIntExtra("areaid", -1);
		dayposition = fIntent.getIntExtra("position", -1);
		beauticianId = fIntent.getIntExtra("beautician_id", 0);
		strp = fIntent.getStringExtra("strp");
		localBautician = (Beautician) fIntent
				.getSerializableExtra("localBautician");
		Log.e("TAG", "fIntent localBautician = " + localBautician);
	}

	private AsyncHttpResponseHandler BeauticianHanler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				beauticianMap.remove("10");
				beauticianMap.remove("20");
				beauticianMap.remove("30");
				if (0 == resultCode) {
					if (jobj.has("data") && !jobj.isNull("data")) {
						JSONObject jData = jobj.getJSONObject("data");
						if (jData.has("workers") && !jData.isNull("workers")) {
							JSONObject jWorkers = jData
									.getJSONObject("workers");
							if (jWorkers.has("10") && !jWorkers.isNull("10")) {
								int bsort = 0;
								String blevelname = null;
								JSONObject jlevel = jWorkers
										.getJSONObject("10");
								if (jlevel.has("level")
										&& !jlevel.isNull("level")) {
									JSONObject jl = jlevel
											.getJSONObject("level");
									if (jl.has("name") && !jl.isNull("name")) {
										blevelname = jl.getString("name");
										btBeauticianLevel1.setText(blevelname);
									}
									if (jl.has("sort") && !jl.isNull("sort")) {
										bsort = jl.getInt("sort");
									}
								}
								if (jlevel.has("workers")
										&& !jlevel.isNull("workers")
										&& jlevel.getJSONArray("workers")
												.length() > 0) {
									JSONArray jlarr = jlevel
											.getJSONArray("workers");
									ArrayList<Beautician> blList = new ArrayList<Beautician>();
									for (int i = 0; i < jlarr.length(); i++) {
										Beautician btc = Beautician
												.json2Entity(jlarr
														.getJSONObject(i));
										btc.sort = bsort;
										btc.levelname = blevelname;
										blList.add(btc);
									}
									beauticianMap.put("10", blList);
								}
							}
							if (jWorkers.has("20") && !jWorkers.isNull("20")) {
								int bsort = 0;
								String blevelname = null;
								JSONObject jlevel = jWorkers
										.getJSONObject("20");
								if (jlevel.has("level")
										&& !jlevel.isNull("level")) {
									JSONObject jl = jlevel
											.getJSONObject("level");
									if (jl.has("name") && !jl.isNull("name")) {
										blevelname = jl.getString("name");
										btBeauticianLevel2.setText(blevelname);
									}
									if (jl.has("sort") && !jl.isNull("sort")) {
										bsort = jl.getInt("sort");
									}
								}
								if (jlevel.has("workers")
										&& !jlevel.isNull("workers")
										&& jlevel.getJSONArray("workers")
												.length() > 0) {
									JSONArray jlarr = jlevel
											.getJSONArray("workers");
									ArrayList<Beautician> blList = new ArrayList<Beautician>();
									for (int i = 0; i < jlarr.length(); i++) {
										Beautician btc = Beautician
												.json2Entity(jlarr
														.getJSONObject(i));
										btc.sort = bsort;
										btc.levelname = blevelname;

										blList.add(btc);
									}
									beauticianMap.put("20", blList);
								}
							}
							if (jWorkers.has("30") && !jWorkers.isNull("30")) {
								int bsort = 0;
								String blevelname = null;
								JSONObject jlevel = jWorkers
										.getJSONObject("30");
								if (jlevel.has("level")
										&& !jlevel.isNull("level")) {
									JSONObject jl = jlevel
											.getJSONObject("level");
									if (jl.has("name") && !jl.isNull("name")) {
										blevelname = jl.getString("name");
										btBeauticianLevel3.setText(blevelname);
									}
									if (jl.has("sort") && !jl.isNull("sort")) {
										bsort = jl.getInt("sort");
									}
								}
								if (jlevel.has("workers")
										&& !jlevel.isNull("workers")
										&& jlevel.getJSONArray("workers")
												.length() > 0) {
									JSONArray jlarr = jlevel
											.getJSONArray("workers");
									ArrayList<Beautician> blList = new ArrayList<Beautician>();
									for (int i = 0; i < jlarr.length(); i++) {
										Beautician btc = Beautician
												.json2Entity(jlarr
														.getJSONObject(i));
										btc.sort = bsort;
										btc.levelname = blevelname;
										blList.add(btc);
									}
									beauticianMap.put("30", blList);
								}
							}
							if (beauticianMap.size() > 0) {
								rl_appointment_switchbau
										.setVisibility(View.VISIBLE);
								vw_appoint_beautician
										.setVisibility(View.VISIBLE);
								// 从服务入口
								selectData();
							}
						}
					}
				} else {
					if (jobj.has("msg") && !jobj.isNull("msg")) {
						String msg = jobj.getString("msg");
						if (msg != null && !TextUtils.isEmpty(msg)) {
							ToastUtil.showToastShortBottom(context, msg);
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
			pDialog.closeDialog();
		}
	};

	private boolean isSelect(ArrayList<Beautician> beauticianList) {
		boolean bool = false;
		if (beauticianList != null && beauticianList.size() > 0) {
			bool = true;
		} else {
			bool = false;
		}
		return bool;
	}

	private void clickLevel3(ArrayList<Beautician> btc) {// 选择美容师级别
		clicksort = 30;
		btBeauticianLevel3.setTextColor(getResources()
				.getColor(R.color.aE33A4A));
		if (btBeauticianLevel1.isEnabled()) {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		if (btBeauticianLevel2.isEnabled()) {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		btn_appointfrag_submit.setVisibility(View.VISIBLE);
		mgv_appointfrg_contentbeau.setVisibility(View.VISIBLE);
		localBeauList.clear();
		localBeauList.addAll(btc);
		appointFragBeauAdapter.setData(localBeauList);
		if(localBautician != null){
			for (int i = 0; i < localBeauList.size(); i++) {
				Beautician beautician = localBeauList.get(i);
				if(beautician != null){
					if(beautician.id == localBautician.id){
						localBautician = beautician;
						appointFragBeauAdapter.setClick(i);
						break;
					}
				}
			}
		}
	}

	private void clickLevel2(ArrayList<Beautician> btc) {// 选择美容师级别
		clicksort = 20;
		btBeauticianLevel2.setTextColor(getResources()
				.getColor(R.color.aE33A4A));
		if (btBeauticianLevel1.isEnabled()) {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		if (btBeauticianLevel3.isEnabled()) {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		btn_appointfrag_submit.setVisibility(View.VISIBLE);
		mgv_appointfrg_contentbeau.setVisibility(View.VISIBLE);
		localBeauList.clear();
		localBeauList.addAll(btc);
		appointFragBeauAdapter.setData(localBeauList);
		if(localBautician != null){
			for (int i = 0; i < localBeauList.size(); i++) {
				Beautician beautician = localBeauList.get(i);
				if(beautician != null){
					if(beautician.id == localBautician.id){
						localBautician = beautician;
						appointFragBeauAdapter.setClick(i);
						break;
					}
				}
			}
		}
	}

	private void clickLevel1(ArrayList<Beautician> btc) {// 选择美容师级别
		clicksort = 10;
		btBeauticianLevel1.setTextColor(getResources()
				.getColor(R.color.aE33A4A));
		if (btBeauticianLevel2.isEnabled()) {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		if (btBeauticianLevel3.isEnabled()) {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.a333333));
		} else {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.acccccc));
		}
		btn_appointfrag_submit.setVisibility(View.VISIBLE);
		mgv_appointfrg_contentbeau.setVisibility(View.VISIBLE);
		localBeauList.clear();
		localBeauList.addAll(btc);
		appointFragBeauAdapter.setData(localBeauList);
		if(localBautician != null){
			for (int i = 0; i < localBeauList.size(); i++) {
				Beautician beautician = localBeauList.get(i);
				if(beautician != null){
					if(beautician.id == localBautician.id){
						localBautician = beautician;
						appointFragBeauAdapter.setClick(i);
						break;
					}
				}
			}
		}
	}

	private void selectData() {
		ArrayList<Beautician> btc = beauticianMap.get("10");
		ArrayList<Beautician> btc1 = beauticianMap.get("20");
		ArrayList<Beautician> btc2 = beauticianMap.get("30");
		if (isSelect(btc)) {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.a333333));
			btBeauticianLevel1.setEnabled(true);
		} else {
			btBeauticianLevel1.setTextColor(getResources().getColor(
					R.color.acccccc));
			btBeauticianLevel1.setEnabled(false);
		}
		if (isSelect(btc1)) {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.a333333));
			btBeauticianLevel2.setEnabled(true);
		} else {
			btBeauticianLevel2.setTextColor(getResources().getColor(
					R.color.acccccc));
			btBeauticianLevel2.setEnabled(false);
		}
		if (isSelect(btc2)) {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.a333333));
			btBeauticianLevel3.setEnabled(true);
		} else {
			btBeauticianLevel3.setTextColor(getResources().getColor(
					R.color.acccccc));
			btBeauticianLevel3.setEnabled(false);
		}
		if (clicksort > 0) {
			switch (clicksort) {
			case 10:
				if (isSelect(btc)) {// 中级可约
					clickLevel1(btc);
				} else {// 中级不可约
					if (isSelect(btc1)) {// 高级可约
						clickLevel2(btc1);
					} else {// 高级不可约
						if (isSelect(btc2)) {// 首席可约
							clickLevel3(btc2);
						}
					}
				}
				break;
			case 20:
				if (isSelect(btc1)) {// 高级可约
					clickLevel2(btc1);
				} else {// 高级不可约
					if (isSelect(btc)) {// 中级可约
						clickLevel1(btc);
					} else {// 中级不可约
						if (isSelect(btc2)) {// 首席可约
							clickLevel3(btc2);
						}
					}
				}
				break;
			case 30:
				if (isSelect(btc2)) {// 首席可约
					clickLevel3(btc2);
				} else {// 首席不可约
					if (isSelect(btc)) {// 中级可约
						clickLevel1(btc);
					} else {// 中级不可约
						if (isSelect(btc1)) {// 高级可约
							clickLevel2(btc1);
						}
					}
				}
				break;
			default:
				break;
			}
		}

		// 解决自动滚动问题
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				sv_appointfrg.fullScroll(ScrollView.FOCUS_DOWN);
			}
		}, 5);
	}

	private void setTs(int postion) {
		if (vieEnabled == 0) {
			tv_appointfrg_ts.setVisibility(View.GONE);
			button_appointfrg_jsyy.setVisibility(View.GONE);
		} else if (vieEnabled == 1) {
			if (postion == 0 || postion == 1) {
				tv_appointfrg_ts.setVisibility(View.VISIBLE);
				button_appointfrg_jsyy.setVisibility(View.VISIBLE);
			} else {
				tv_appointfrg_ts.setVisibility(View.GONE);
				button_appointfrg_jsyy.setVisibility(View.GONE);
			}
		}
		int integer = selection.get(postion).getIsFull();
		String str = "";
		if (integer == 1) {// 已满
			str = "提示: 时间已满,您可以选择加急通道!";
		} else if (integer == 0) {// 未满
			str = "提示:如果以上时间都不满意,您可以自己发布时间哦!";
		}
		SpannableString ss = new SpannableString(str);
		// 用下划线标记文本
		ss.setSpan(new UnderlineSpan(), 0, ss.length(),
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		tv_appointfrg_ts.setText(str);
	}

	private void getTime() {
		Intent data = new Intent();
		if(localBautician != null && localBautician.id > 0){
			Bundle bundle = new Bundle();
			bundle.putSerializable("localBautician", localBautician);
			data.putExtras(bundle);
		}
		data.putExtra("strListWokerId", strListWokerId);
		data.putExtra("position", dayposition);
		data.putExtra("time", selectedDate + " " + selectedTime);
		data.putExtra("date", date + " " + selectedTime + ":00");
		/*data.putExtra("beautician_name",
				fIntent.getStringExtra("beautician_name"));
		data.putExtra("beautician_sex",
				fIntent.getIntExtra("beautician_sex", 0));
		data.putExtra("beautician_level",
				fIntent.getIntExtra("beautician_level", 0));
		data.putExtra("beautician_stars",
				fIntent.getIntExtra("beautician_stars", 0));
		data.putExtra("beautician_titlelevel",
				fIntent.getStringExtra("beautician_titlelevel"));
		data.putExtra("beautician_image",
				fIntent.getStringExtra("beautician_image"));
		data.putExtra("beautician_ordernum",
				fIntent.getIntExtra("beautician_ordernum", 0));
		data.putExtra("beautician_storeid",
				fIntent.getIntExtra("beautician_storeid", 0));
		data.putExtra("beautician_id", fIntent.getIntExtra("beautician_id", 0));
		data.putExtra("beautician_sort",
				fIntent.getIntExtra("beautician_sort", 0));
		data.putExtra("beautician_levelname",
				fIntent.getStringExtra("beautician_levelname"));
		data.putExtra("beautician_bathmultiple",
				fIntent.getDoubleExtra("beautician_bathmultiple", 1));
		data.putExtra("beautician_beautymultiple",
				fIntent.getDoubleExtra("beautician_beautymultiple", 1));
		data.putExtra("beautician_factormultiple",
				fIntent.getDoubleExtra("beautician_factormultiple", 1));*/
		context.setResult(Global.RESULT_OK, data);
		context.finish();
	}

	private void getData(int beauticianid) {
		pDialog.showDialog();
		if (beauticianid == 0) {
			CommUtil.getBeauticianFreeTime(context, lat, lng,
					spUtil.getString("cellphone", ""), Global.getIMEI(context),
					Global.getCurrentVersion(context), areaid, serviceloc,
					shopid, strp, 0, null, 0, timeHanler);
		} else {
			CommUtil.getBeauticianTimeById(context, lat, lng,
					spUtil.getString("cellphone", ""), Global.getIMEI(context),
					Global.getCurrentVersion(context), areaid, serviceloc,
					shopid, beauticianid, strp, timeHanler);
		}
	}

	private AsyncHttpResponseHandler timeHanler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			String result = new String(responseBody);
			processData(result);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			pDialog.closeDialog();
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null)
			timer.cancel();
		if (task != null)
			task.cancel();
	}

	// 解析json数据
	private void processData(String result) {
		sv_appointfrg.setVisibility(View.VISIBLE);
		try {
			Gson gson = new Gson();
			TimeListCodeBean fromJson = gson.fromJson(result.toString(),
					TimeListCodeBean.class);
			if (fromJson != null) {
				int code = fromJson.getCode();
				DataBean data = fromJson.getData();
				String msg = fromJson.getMsg();
				if (code == 0) {
					if (data != null) {
						int position = 0;
						selection = data.getSelection();
						// Utils.setText(tvDayNum, data.getDesc(), "",
						// View.VISIBLE, View.GONE);
						if (selection != null && selection.size() > 0) {
							int size = selection.size();
							int length = 60;
							DisplayMetrics dm = new DisplayMetrics();
							context.getWindowManager().getDefaultDisplay()
									.getMetrics(dm);
							float density = dm.density;
							int gridviewWidth = (int) (size * (length + 5) * density);
							int itemWidth = (int) (length * density);
							itemwidth = itemWidth;
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									gridviewWidth,
									LinearLayout.LayoutParams.FILL_PARENT);
							gv_appointfrg_top.setLayoutParams(params); // 重点
							gv_appointfrg_top.setColumnWidth(itemWidth); // 重点
							gv_appointfrg_top.setHorizontalSpacing(5); // 间距
							gv_appointfrg_top
									.setStretchMode(GridView.NO_STRETCH);
							gv_appointfrg_top.setGravity(Gravity.CENTER);// 位置居中
							gv_appointfrg_top.setNumColumns(size); // 重点
							topTimeDateAdapter = new TopTimeDateAdapter(
									context, selection);
							gv_appointfrg_top.setAdapter(topTimeDateAdapter);
							for (int i = 0; i < selection.size(); i++) {
								if (selection.get(i).getIsFull() == 0) {
									position = i;
									break;
								}
							}
							if (dayposition >= 0) {
								selectedDate = selection.get(dayposition)
										.getDate();
								date = selection.get(dayposition).getYear()
										+ "-" + selectedDate;
								topTimeDateAdapter.setClickItem(dayposition);
								setTs(dayposition);
								ScollTo(dayposition);
							} else {
								dayposition = position;
								selectedDate = selection.get(position)
										.getDate();
								date = selection.get(position).getYear() + "-"
										+ selectedDate;
								topTimeDateAdapter.setClickItem(position);
								setTs(position);
								ScollTo(position);
							}
							// 默认显示可预约的第一天或者上次选中的那一天
							if (dayposition >= 0) {
								contentTimeAdapter = new ContentTimeAdapter(
										context, selection.get(dayposition)
												.getTimes(), selection.get(
												dayposition).getIsFull());
							} else {
								contentTimeAdapter = new ContentTimeAdapter(
										context, selection.get(dayposition)
												.getTimes(), selection.get(
												dayposition).getIsFull());
							}
							gv_appointfrg_content
									.setAdapter(contentTimeAdapter);
						}
						String isFullTip = data.getIsFullTip();
						if (isFullTip != null && !TextUtils.isEmpty(isFullTip)) {
							ToastUtil.showToastShortBottom(context, isFullTip);
							if (vieEnabled == 1) {
								isBack = true;
								goToVie();
							} else if (vieEnabled == 0) {
								task = new TimerTask() {
									@Override
									public void run() {
										context.finish();
									}
								};
								timer = new Timer();
								timer.schedule(task, 3000);
							}
						} else {
							// 显示新手引导
							showGuideView();
						}
					}
				} else {
					if (msg != null && !TextUtils.isEmpty(msg)) {
						ToastUtil.showToastShortBottom(context, msg);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showGuideView() {
		boolean TimeFragFlag = spUtil.getBoolean("TimeFragFlag", false);
		if (!TimeFragFlag) {
			spUtil.saveBoolean("TimeFragFlag", true);
			// 显示新手引导
			showGuide();
		}
	}

	private void showGuide() {
		if (pWin == null) {
			final View view = View.inflate(context, R.layout.timefrag_guide,
					null);
			RelativeLayout rl_timefrag_guide_root = (RelativeLayout) view
					.findViewById(R.id.rl_timefrag_guide_root);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);
			pWin.setFocusable(true);
			pWin.setWidth(Utils.getDisplayMetrics(context)[0]);
			pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
			rl_timefrag_guide_root.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					pWin.dismiss();
					pWin = null;
				}
			});
		}
	}

	private void ScollTo(final int dayposition2) {
		// 解决自动滚动问题
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				hsv_appointfrg_date.smoothScrollTo((dayposition2 - 2)
						* itemwidth, 0);
			}
		}, 5);
	}

	@Override
	public void onResume() {
		super.onResume();
		button_appointfrg_jsyy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		button_appointfrg_jsyy.getPaint().setAntiAlias(true);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_appointfrg_ts:
			goToVie();
			break;
		case R.id.button_appointfrg_jsyy:
			goToVie();
			break;
		case R.id.tv_servicedetail_beautician_level1:
			localBautician = null;
			clickLevel1(beauticianMap.get("10"));
			break;
		case R.id.tv_servicedetail_beautician_level2:
			localBautician = null;
			clickLevel2(beauticianMap.get("20"));
			break;
		case R.id.tv_servicedetail_beautician_level3:
			localBautician = null;
			clickLevel3(beauticianMap.get("30"));
			break;
		case R.id.btn_appointfrag_submit:
			Log.e("TAG", "localBautician = " + localBautician);
			getTime();
			break;
		default:
			break;
		}
	}

	private void goToVie() {
		SelectTimeOrUrgentActivity activity = (SelectTimeOrUrgentActivity) getActivity();
		activity.setTabSelection(1);
	}
}
