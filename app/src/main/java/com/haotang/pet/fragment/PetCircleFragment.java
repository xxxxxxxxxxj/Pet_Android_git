package com.haotang.pet.fragment;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.BaseFragment;
import com.haotang.pet.PetCircleInsideActivity;
import com.haotang.pet.R;
import com.haotang.pet.adapter.BannerBathLoopAdapter;
import com.haotang.pet.adapter.PetCirCleAdapter;
import com.haotang.pet.entity.PetCircle;
import com.haotang.pet.entity.PetCircleBanner;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.rollviewpager.RollPagerView;
import com.haotang.pet.view.rollviewpager.hintview.ColorPointHintView;
import com.yixia.weibo.sdk.util.DeviceUtils;

/**
 * <p>
 * Title:PetCircleFragment
 * </p>
 * <p>
 * Description:宠圈列表界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-9-27 下午3:52:19
 */
@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PetCircleFragment extends BaseFragment implements OnTouchListener{
	public static PetCircleFragment petCircleFragment; 
	private Activity context;
	private RollPagerView rpvBanner;
	private BannerBathLoopAdapter adapterBanner;
	private ArrayList<String> listBanner = new ArrayList<String>();
	private ArrayList<PetCircleBanner> petCircleList =  new ArrayList<PetCircleBanner>();
	private PullToRefreshListView pullToRefreshPetCircle;
	private PetCirCleAdapter cirCleAdapter;
	private ArrayList<PetCircle> arrayList = new ArrayList<PetCircle>();
//	private TextView textview_next;
	private SharedPreferenceUtil spUtils;
	private MProgressDialog mDialog;
	private boolean isFirst = true;
	private View header;
	private View view = null;
	ArrayList<PetCircle> AddCirCle = new ArrayList<PetCircle>();
	ArrayList<PetCircle> UnCirCle = new ArrayList<PetCircle>();
	/**
	 * 广播接收器
	 */
	private MyReceiver receiver;
	public static PetCircleFragment getPetCircleFragment(){
		if (petCircleFragment==null) {
			petCircleFragment = new PetCircleFragment();
		}
		return petCircleFragment;
		
	} 
	public PetCircleFragment() {
		super();
	}

	public PetCircleFragment(Activity context) {
		this.context = context;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		listBanner.clear();
		arrayList.clear();
		petCircleList.clear();
		AddCirCle.clear();
		UnCirCle.clear();
		petCircleFragment = this;
		if (view==null) {
			view = inflater.inflate(R.layout.fragment_pet_circle,null);
			header = LayoutInflater.from(mContext).inflate(R.layout.header_petcircle_fragment, null);
			initView(view);
			initReceiver();
			setView();
			
			
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
            parent.removeView(view);
        } 
		view.setOnTouchListener(this);
		try {
			PetCircleOrSelectFragment.myAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			adapterBanner.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
		getData();
		return view;
	}


	private void setView() {
		adapterBanner = new BannerBathLoopAdapter(rpvBanner, listBanner);
		rpvBanner.setAdapter(adapterBanner);
		pullToRefreshPetCircle.setMode(Mode.BOTH);
		pullToRefreshPetCircle.getRefreshableView().addHeaderView(header);
		cirCleAdapter = new PetCirCleAdapter<PetCircle>(getActivity(), arrayList);
		
		pullToRefreshPetCircle.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
				if (mode == Mode.PULL_FROM_START) {
//					// 下拉刷新
					getData();
				}else {
					getData();
				}
			}
		});
		pullToRefreshPetCircle.setAdapter(cirCleAdapter);
	}

	public void getData() {
		listBanner.clear();
		arrayList.clear();
		petCircleList.clear();
		AddCirCle.clear();
		UnCirCle.clear();
//		mDialog.showDialog();
		CommUtil.queryGroups(spUtils.getString("cellphone", ""),getActivity(), queryGroups);
	}
	private AsyncHttpResponseHandler queryGroups = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			mDialog.closeDialog();
			pullToRefreshPetCircle.onRefreshComplete();
			try {
				JSONObject object  = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("groups")&&!objectData.isNull("groups")) {
							JSONArray arrayGroup = objectData.getJSONArray("groups");
							if (arrayGroup.length()>0) {
								for (int i = 0; i < arrayGroup.length(); i++) {
									PetCircle circle = new PetCircle();
									JSONObject objectEvery = arrayGroup.getJSONObject(i);
									if (objectEvery.has("groupName")&&!objectEvery.isNull("groupName")) {
										circle.groupName = objectEvery.getString("groupName");
									}
									if (objectEvery.has("areaId")&&!objectEvery.isNull("areaId")) {
										circle.areaId = objectEvery.getInt("areaId");
									}
									if (objectEvery.has("created")&&!objectEvery.isNull("created")) {
										circle.created = objectEvery.getString("created");
									}
									if (objectEvery.has("description")&&!objectEvery.isNull("description")) {
										circle.description = objectEvery.getString("description");
									}
									if (objectEvery.has("pic")&&!objectEvery.isNull("pic")) {
										circle.pic = objectEvery.getString("pic");
									}
									if (objectEvery.has("PostGroupId")&&!objectEvery.isNull("PostGroupId")) {
										circle.PostGroupId = objectEvery.getInt("PostGroupId");
									}
									if (objectEvery.has("isFollowed")&&!objectEvery.isNull("isFollowed")) {
										circle.isFollowed = objectEvery.getInt("isFollowed");
									}
									arrayList.add(circle);
								}
								
								AddCirCle.clear();
								UnCirCle.clear();
								if (arrayList.size()>0) {
									for (int j = 0; j < arrayList.size(); j++) {
										if (arrayList.get(j).isFollowed==0) {
											UnCirCle.add(arrayList.get(j));
										}else if (arrayList.get(j).isFollowed==1) {
											AddCirCle.add(arrayList.get(j));
										}
									}
								}
								arrayList.clear();
								arrayList.addAll(AddCirCle);
								arrayList.addAll(UnCirCle);
								AddCirCle.clear();
								UnCirCle.clear();
								if (arrayList.size()>0) {
									cirCleAdapter.notifyDataSetChanged();
//									PetCircleOrSelectFragment.myAdapter.notifyDataSetChanged();
								}
							}
						}
//						if (isFirst) {
//							isFirst = false;
							petCircleList.clear();
							if (objectData.has("banners")&&!objectData.isNull("banners")) {
								JSONArray arrayBanner = objectData.getJSONArray("banners");
								if (arrayBanner.length()>0) {
									for (int i = 0; i < arrayBanner.length(); i++) {
										PetCircleBanner circleBanner = new PetCircleBanner();
										JSONObject objectBanner = arrayBanner.getJSONObject(i);
										if (objectBanner.has("imgLink")&&!objectBanner.isNull("imgLink")) {
											circleBanner.imgLink = objectBanner.getString("imgLink");
//										listBanner.add(circleBanner.imgLink);
										}
										if (objectBanner.has("imgUrl")&&!objectBanner.isNull("imgUrl")) {
											circleBanner.imgUrl = objectBanner.getString("imgUrl");
											listBanner.add(circleBanner.imgUrl);
										}
										petCircleList.add(circleBanner);
									}
									if (listBanner.size() > 1) {
										rpvBanner.setHintView(new ColorPointHintView(getActivity(), Color.parseColor("#FE8A3F"), Color.parseColor("#FFE2D0")));
									}else {
										rpvBanner.setHintView(null);
									}
									adapterBanner.setPetCircleBanner(petCircleList);
									adapterBanner.notifyDataSetChanged();
								}
							}
//						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					mDialog.closeDialog();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			try {
				mDialog.closeDialog();
				pullToRefreshPetCircle.onRefreshComplete();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	};
	private void initView(View view) {
		mDialog = new MProgressDialog(getActivity());
		spUtils = SharedPreferenceUtil.getInstance(getActivity());
//		rpvBanner = (RollPagerView) view.findViewById(R.id.rpv_servicedetail_pet);
		rpvBanner = (RollPagerView) header.findViewById(R.id.rpv_servicedetail_pet);
		
//		textview_next = (TextView) view.findViewById(R.id.textview_next);
		pullToRefreshPetCircle = (PullToRefreshListView) view.findViewById(R.id.pullToRefreshPetCircle);
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			context.unregisterReceiver(receiver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			PetCircleOrSelectFragment.myAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			adapterBanner.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initReceiver() {
		// 广播事件**********************************************************************
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PetCircleFragment");
		// 注册广播接收器
		context.registerReceiver(receiver, filter);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_PetCircle);
		int imageHeight=0;
		int screenWidth=0;
		screenWidth = Utils.getDisplayMetrics((Activity)mContext)[0];
		imageHeight = screenWidth * 2 / 5;
		rpvBanner.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,imageHeight));
	}
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Utils.mLogError("==-->onec 0 ");
			int index = intent.getIntExtra("index", 0);
			if (index==1) {
				listBanner.clear();
				arrayList.clear();
				petCircleList.clear();
				AddCirCle.clear();
				UnCirCle.clear();
				getData();
			}
		}
		
	}
	
}
