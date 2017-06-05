package com.haotang.pet.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.BeauticianDetailActivity;
import com.haotang.pet.BeauticianListActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.Beautician;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class BeauticianAdapter extends CommonAdapter<Beautician> {
	private int serviceid;
	private int serviceLoc;
	private int shopid;
	private int areaid;
	private String strp;
	private int index;
	private int sort = -1;
	private int from = -1;
	private DecimalFormat df = null;
	private String localDate;

	public BeauticianAdapter(Activity mContext, List<Beautician> mDatas, String localDate) {
		super(mContext, mDatas);
		df = new DecimalFormat("0.0");
		this.localDate = localDate;
	}

	public void setParams(int serviceid, int serviceloc, int shopid,
			int areaid, String strp) {
		this.serviceid = serviceid;
		this.serviceLoc = serviceloc;
		this.shopid = shopid;
		this.areaid = areaid;
		this.strp = strp;
	}

	@Override
	public void setData(List<Beautician> mDatas) {
		super.setData(mDatas);
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setSort(int srot, int from) {
		this.sort = srot;
		this.from = from;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.beauticianitem, position);
		TextView tv_beauticianitem_upgradetip = viewHolder
				.getView(R.id.tv_beauticianitem_upgradetip);
		final Beautician beautician = mDatas.get(position);
		if (sort != -1) {
			beautician.sort = sort;
		}
		if (from == Global.MAIN_TO_BEAUTICIANLIST) {
			viewHolder.getView(R.id.item_layout_beau_app).setVisibility(
					View.GONE);
		}
		int areaNameid = SharedPreferenceUtil.getInstance(mContext).getInt(
				"tareaid", 0);
		if (areaNameid == 0 || areaNameid == 100) {
			viewHolder.getView(R.id.tv_beautician_address).setVisibility(
					View.VISIBLE);
			viewHolder.getView(R.id.layout_beau_go_home).setVisibility(
					View.GONE);
			viewHolder.getView(R.id.layout_beau_go_shop).setVisibility(
					View.GONE);
			viewHolder.setText(R.id.tv_beautician_address, beautician.areaName);
		} else {
			viewHolder.getView(R.id.tv_beautician_address).setVisibility(
					View.GONE);
			if (beautician.homeServiceSchedule == 0) {
				viewHolder.getView(R.id.layout_beau_go_home).setVisibility(
						View.GONE);
			} else if (beautician.homeServiceSchedule == 1) {
				viewHolder.getView(R.id.layout_beau_go_home).setVisibility(
						View.VISIBLE);
			}
			if (beautician.shopServiceSchedule == 0) {
				viewHolder.getView(R.id.layout_beau_go_shop).setVisibility(
						View.GONE);
			} else if (beautician.shopServiceSchedule == 1) {
				viewHolder.getView(R.id.layout_beau_go_shop).setVisibility(
						View.VISIBLE);
			}
		}
		Utils.setText(tv_beauticianitem_upgradetip, beautician.upgradeTip, "",
				View.VISIBLE, View.GONE);
		viewHolder.setText(R.id.beau_detail_tag, beautician.titlelevel);
		viewHolder.setText(R.id.tv_beauticianitem_beauticianname,
				beautician.name);
		viewHolder.setText(R.id.textview_service_goodrate, "好评率："
				+ (int) (beautician.goodRate * 100) + "%");
		viewHolder.setText(R.id.textview_exchange_grade, "评分："
				+ beautician.grade + "");
		viewHolder.setText(R.id.tv_beauticianitem_ordernum, "接单："
				+ beautician.ordernum);
		if (beautician.isAvail == 1 || beautician.isAvail == 3) {
			viewHolder.setText(R.id.bt_beauticianitem_booking, "预约");
			viewHolder.setBackgroundResource(R.id.bt_beauticianitem_booking,
					R.drawable.bg_button_orager_oval);
		} else {
			viewHolder.setText(R.id.bt_beauticianitem_booking, "查看");
			viewHolder.setBackgroundResource(R.id.bt_beauticianitem_booking,
					R.drawable.bg_button_48c9e9_oval);
		}
		viewHolder.setBackgroundCircle(R.id.sriv_beauticianitem_beautician,
				beautician.image, R.drawable.icon_beautician_default);
		viewHolder.getView(R.id.bt_beauticianitem_booking).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (beautician.isAvail == 1) {
							Intent data = new Intent();
							data.putExtra("beautician_id", beautician.id);
							data.putExtra("beautician_sex", beautician.gender);
							data.putExtra("beautician_sort", beautician.sort);
							data.putExtra("clicksort", beautician.sort);
							data.putExtra("beautician_name", beautician.name);
							data.putExtra("beautician_levelname",
									beautician.levelname);
							data.putExtra("beautician_image", beautician.image);
							data.putExtra("beautician_level", beautician.levels);
							data.putExtra("beautician_stars", beautician.stars);
							data.putExtra("beautician_ordernum",
									beautician.ordernum);
							data.putExtra("beautician_storeid",
									beautician.storeid);
							data.putExtra("beautician_titlelevel",
									beautician.titlelevel);
							data.putExtra("upgradeTip",
									beautician.upgradeTip);
							BeauticianListActivity.act.setResult(
									Global.RESULT_OK, data);
							BeauticianListActivity.act.finishWithAnimation();

						} else if (beautician.isAvail == 3) {
							Intent data = new Intent();
							data.putExtra("beautician_id", beautician.id);
							data.putExtra("beautician_sex", beautician.gender);
							data.putExtra("beautician_sort", beautician.sort);
							data.putExtra("clicksort", beautician.sort);
							data.putExtra("beautician_name", beautician.name);
							data.putExtra("beautician_levelname",
									beautician.levelname);
							data.putExtra("beautician_image", beautician.image);
							data.putExtra("beautician_level", beautician.levels);
							data.putExtra("beautician_stars", beautician.stars);
							data.putExtra("beautician_ordernum",
									beautician.ordernum);
							data.putExtra("beautician_storeid",
									beautician.storeid);
							data.putExtra("beautician_titlelevel",
									beautician.titlelevel);
							data.putExtra("upgradeTip",
									beautician.upgradeTip);
							BeauticianListActivity.act.setResult(
									Global.RESULT_OK, data);
							BeauticianListActivity.act.finishWithAnimation();

						} else {
							Intent data = new Intent(
									BeauticianListActivity.act,
									BeauticianDetailActivity.class);
							data.putExtra(Global.ANIM_DIRECTION(),
									Global.ANIM_COVER_FROM_RIGHT());
							BeauticianListActivity.act.getIntent().putExtra(
									Global.ANIM_DIRECTION(),
									Global.ANIM_COVER_FROM_LEFT());
							data.putExtra("serviceid", serviceid);
							data.putExtra("shopid", shopid);
							data.putExtra("apptime", localDate);
							data.putExtra("areaid", areaid);
							data.putExtra("serviceloc", serviceLoc);
							data.putExtra("isavail", beautician.isAvail);
							data.putExtra("strp", strp);
							data.putExtra("beautician_id", beautician.id);
							data.putExtra("beautician_sex", beautician.gender);
							data.putExtra("beautician_sort", beautician.sort);
							data.putExtra("beautician_name", beautician.name);
							data.putExtra("beautician_levelname",
									beautician.levelname);
							data.putExtra("beautician_image", beautician.image);
							data.putExtra("beautician_level", beautician.levels);
							data.putExtra("beautician_stars", beautician.stars);
							data.putExtra("beautician_ordernum",
									beautician.ordernum);
							data.putExtra("beautician_storeid",
									beautician.storeid);
							data.putExtra("beautician_titlelevel",
									beautician.titlelevel);
							data.putExtra("upgradeTip",
									beautician.upgradeTip);
							BeauticianListActivity.act.startActivityForResult(
									data, Global.BEAUTICIAN_TO_TIME);
						}
					}
				});
		return viewHolder.getConvertView();
	}

}
