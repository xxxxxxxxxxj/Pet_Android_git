package com.haotang.pet.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.haotang.pet.BeauToAppointmentActivity;
import com.haotang.pet.BeauticianDetailActivity;
import com.haotang.pet.ChoosePetActivityNew;
import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.ServiceActivity;
import com.haotang.pet.ServiceFeature;
import com.haotang.pet.adapter.BeauToAppointmentAdapter;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

@SuppressLint("ValidFragment")
public class BeauToAppointmentFragment extends Fragment {
	private SharedPreferenceUtil spUtil;
	private GridView gridView_beauappoiment;
	private ArrayList<Beautician> mDatasLast;
	private BeauToAppointmentAdapter<Beautician> adapter;

	private int areaid;
	private int shopid;

	private boolean isshop;
	private boolean ishome;
	private int previous;
	private int beautician_id;
	private String areaname;
	private String beautician_name;
	private String beautician_iamge;
	private int beautician_ordernum;
	private int gender;
	private int sort;
	private String levelname;
	private String upgradeTip;

	public BeauToAppointmentFragment(ArrayList<Beautician> mDatasLast,
			int areaid, int shopid, boolean isshop, boolean ishome,
			int previous, int beautician_id, String areaname,
			String beautician_name, String beautician_iamge,
			int beautician_ordernum, int gender, int sort, String levelname,String upgradeTip) {
		this.mDatasLast = mDatasLast;
		this.areaid = areaid;
		this.shopid = shopid;
		this.isshop = isshop;
		this.ishome = ishome;
		this.previous = previous;
		this.beautician_id = beautician_id;
		this.areaname = areaname;
		this.beautician_name = beautician_name;
		this.beautician_iamge = beautician_iamge;
		this.beautician_ordernum = beautician_ordernum;
		this.gender = gender;
		this.sort = sort;
		this.levelname = levelname;
		this.upgradeTip=upgradeTip;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		spUtil = SharedPreferenceUtil.getInstance(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragmen_beau_to_appointment, null);
		gridView_beauappoiment = (GridView) view
				.findViewById(R.id.gridView_beauappoiment);
		adapter = new BeauToAppointmentAdapter<Beautician>(getActivity(),
				mDatasLast);
		gridView_beauappoiment.setAdapter(adapter);
		gridView_beauappoiment
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Beautician beautician = (Beautician) parent
								.getItemAtPosition(position);
						if (isLogin() && hasPet()) {
							Utils.mLogError("==-->beautician "
									+ beautician.BeauDetailServiceType);
							// if (beautician.BeauDetailServiceType!=1 &&
							// beautician.BeauDetailServiceType!=2) {
							// goNext(ServiceFeature.class,beautician,0);
							// }
							if (beautician.BeauDetailServiceType == 1
									|| beautician.BeauDetailServiceType == 2) {
								if (beautician.BeauDetailName.contains("洗澡")) {
									if (hasService(1)) {
										goNext(ServiceActivity.class,
												beautician, 1);
									} else {
										choosePet(beautician);
									}
								} else if (beautician.BeauDetailName
										.contains("美容")) {
									if (hasService(2)) {
										goNext(ServiceActivity.class,
												beautician, 2);
									} else {
										choosePet(beautician);
									}
								}
							} else {
								goNext(ServiceFeature.class, beautician, 0);
							}
						} else {
							choosePet(beautician);
						}
					}

					private void choosePet(Beautician beautician) {
						Intent data = new Intent(getActivity(),ChoosePetActivityNew.class);
						data.putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_RIGHT());
						getActivity().getIntent().putExtra(Global.ANIM_DIRECTION(),Global.ANIM_COVER_FROM_LEFT());
						if (beautician.BeauDetailName.contains("洗澡")) {
							data.putExtra("serviceid", 1);
							data.putExtra("servicetype", 1);
						} else if (beautician.BeauDetailName.contains("美容")) {
							data.putExtra("serviceid", 2);
							data.putExtra("servicetype", 2);
						}
						data.putExtra("isshop", isshop);
						data.putExtra("ishome", ishome);
						data.putExtra("areaid", areaid);
						data.putExtra("shopid", shopid);
						// if(beautician.BeauDetailServiceType==3){
						// data.putExtra("tmpflag", 3);
						// data.putExtra("servicename",
						// beautician.BeauDetailName);
						// }
						if (beautician.BeauDetailServiceType == 1|| beautician.BeauDetailServiceType == 2) {
							// 马丹 空格xx
						} else {
							data.putExtra("tmpflag", 3);
//							data.putExtra("servicename",beautician.BeauDetailName);
							data.putExtra("servicename",beautician.BeauDetailTypeId);
						}
						data.putExtra("previous",Global.BEAUTICIAN_TO_APPOINTMENT);

						data.putExtra("beautician_id", beautician_id);
						data.putExtra("areaname", areaname);
						data.putExtra("beautician_name", beautician_name);
						data.putExtra("beautician_image", beautician_iamge);
						data.putExtra("beautician_ordernum",beautician_ordernum);
						data.putExtra("beautician_sex", gender);
						data.putExtra("beautician_sort", sort);
						data.putExtra("beautician_levelname", levelname);
						
						data.putExtra("servicetype", beautician.BeauDetailServiceType);
						data.putExtra("upgradeTip", upgradeTip);
						startActivity(data);
						if (BeauToAppointmentActivity.act != null)
							BeauToAppointmentActivity.act.finish();
					}

				});
		return view;
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

	private void goNext(Class cls, Beautician beautician, int serviceid) {
		Intent data = new Intent(getActivity(), cls);
		data.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getActivity().getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		data.putExtra("serviceid", serviceid);// id
		data.putExtra("servicetype", beautician.BeauDetailServiceType);
		data.putExtra("servicename", beautician.BeauDetailTypeId);
		data.putExtra("isshop", isshop);
		data.putExtra("ishome", ishome);
		data.putExtra("areaid", areaid);
		data.putExtra("shopid", shopid);
		data.putExtra("previous", Global.MAIN_TO_BEAUTICIANLIST);

		data.putExtra("beautician_id", beautician_id);
		data.putExtra("areaname", areaname);
		data.putExtra("beautician_name", beautician_name);
		data.putExtra("beautician_image", beautician_iamge);
		data.putExtra("beautician_ordernum", beautician_ordernum);
		data.putExtra("beautician_sex", gender);
		data.putExtra("beautician_sort", sort);
		data.putExtra("beautician_levelname", levelname);
		data.putExtra("upgradeTip", upgradeTip);
		startActivity(data);
		if (BeauToAppointmentActivity.act != null)
			BeauToAppointmentActivity.act.finish();
	}
}
