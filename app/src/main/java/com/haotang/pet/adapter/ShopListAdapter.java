package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.GoShopDetailActivity;
import com.haotang.pet.R;
import com.haotang.pet.ServiceActivity;
import com.haotang.pet.ServiceFeature;
import com.haotang.pet.entity.ShopListBean.DataBean.RegionsBean.ShopsBean;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ShopListAdapter extends BaseAdapter {
	private SuperActivity act;
	private List<ShopsBean> list;
	private LayoutInflater mInflater;
	private int serviceid;
	private double addserviceprice;
	private int previous;
	private Intent fIntent;
	private PopupWindow pWin;
	private int clicksort;
	private String servicename;

	public ShopListAdapter(SuperActivity act, int serviceid,
			List<ShopsBean> list, double addserviceprice, int clicksort,
			String servicename) {
		this.clicksort = clicksort;
		this.act = act;
		this.list = list;
		this.serviceid = serviceid;
		this.addserviceprice = addserviceprice;
		this.servicename = servicename;
		mInflater = LayoutInflater.from(act);
	}

	public void setPrevious(int previous) {
		this.previous = previous;
	}

	public void setFIntent(Intent fIntent) {
		this.fIntent = fIntent;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_shop_list, null);
			mHolder.imageview_item_shop_list_icon = (ImageView) convertView
					.findViewById(R.id.imageview_item_shop_list_icon);
			mHolder.textView_item_shop_list_name = (TextView) convertView
					.findViewById(R.id.textView_item_shop_list_name);
			mHolder.textView_item_shop_list_price = (TextView) convertView
					.findViewById(R.id.textView_item_shop_list_price);
			mHolder.textView_item_shop_list_address_detail = (TextView) convertView
					.findViewById(R.id.textView_item_shop_list_address_detail);
			mHolder.textView_item_shop_list_address = (TextView) convertView
					.findViewById(R.id.textView_item_shop_list_address);
			mHolder.button_item_shop_list_go_appointment = (Button) convertView
					.findViewById(R.id.button_item_shop_list_go_appointment);
			mHolder.service_go_home_detail = (LinearLayout) convertView
					.findViewById(R.id.service_go_home_detail);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		final ShopsBean swp = list.get(position);
		if (swp != null) {
			ImageLoaderUtil.loadImg(swp.getImg(),
					mHolder.imageview_item_shop_list_icon,
					R.drawable.icon_production_default, null);
			Utils.setText(mHolder.textView_item_shop_list_name,
					swp.getShopName(), "", View.VISIBLE, View.INVISIBLE);
			Utils.setText(mHolder.textView_item_shop_list_address,
					swp.getAddress(), "", View.VISIBLE, View.INVISIBLE);
			Utils.setText(mHolder.textView_item_shop_list_address_detail,
					"距宠物地址 " + swp.getDist(), "", View.VISIBLE, View.INVISIBLE);
			if (swp.getStatus() == 2) {
				mHolder.textView_item_shop_list_price.setText(swp
						.getShopPrice());
				mHolder.button_item_shop_list_go_appointment
						.setBackgroundResource(R.drawable.bg_search_orangeborder);
				mHolder.button_item_shop_list_go_appointment.setTextColor(act
						.getResources().getColor(R.color.orange));
				String text = "即将开业";
				SpannableString ss = new SpannableString(text);
				ss.setSpan(new TextAppearanceSpan(act, R.style.style3), 0,
						ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				mHolder.button_item_shop_list_go_appointment.setText(ss);
			} else {
				if (swp.getShopPrice() != null
						&& !"".equals(swp.getShopPrice())) {
					String replace = "";
					if (swp.getShopPrice().contains(",")) {
						replace = swp.getShopPrice().replace(",", "");
					} else if (swp.getShopPrice().contains("，")) {
						replace = swp.getShopPrice().replace("，", "");
					} else {
						replace = swp.getShopPrice();
					}
					String text = "¥"
							+ Utils.formatDouble(Double.parseDouble(replace)
									+ addserviceprice, 2);
					SpannableString ss = new SpannableString(text);
					ss.setSpan(new TextAppearanceSpan(act, R.style.style3), 0,
							1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
					mHolder.textView_item_shop_list_price.setText(ss);
				}
				mHolder.button_item_shop_list_go_appointment
						.setBackgroundResource(R.drawable.bg_button_orager_oval);
				mHolder.button_item_shop_list_go_appointment.setTextColor(act
						.getResources().getColor(R.color.white));
				String text = "预约";
				SpannableString ss = new SpannableString(text);
				ss.setSpan(new TextAppearanceSpan(act, R.style.style4), 0,
						ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				mHolder.button_item_shop_list_go_appointment.setText(ss);
			}
			mHolder.button_item_shop_list_go_appointment
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (swp.getStatus() == 2) {
								if (swp.getHotelImg() != null
										&& swp.getHotelImg().size() > 0) {
									showPop(swp.getHotelImg().get(0));
								}
							} else {
								if (previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST
										|| previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY) {
									goAppointment(ServiceActivity.class,
											swp.getShopId(), swp.getAreaId());
								} else if (previous == Global.SERVICEFEATURE_TO_PETLIST_SHOPLIST
										|| previous == Global.SERVICEFEATURE_TO_PETLIST) {
									goAppointment(ServiceFeature.class,
											swp.getShopId(), swp.getAreaId());
								} else {
									Intent data = new Intent();
									data.putExtra("shopid", swp.getShopId());
									data.putExtra("areaid", swp.getAreaId());
									data.putExtra("shopname", swp.getShopName());
									data.putExtra("shopimg", swp.getImg());
									data.putExtra("shopaddr", swp.getAddress());
									data.putExtra("shoptel", swp.getPhone());
									act.setResult(Global.RESULT_OK, data);
									act.finishWithAnimation();
								}
							}
						}
					});
			mHolder.service_go_home_detail
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (swp.getStatus() == 2) {
								if (swp.getHotelImg() != null
										&& swp.getHotelImg().size() > 0) {
									showPop(swp.getHotelImg().get(0));
								}
							} else {
								goNext(GoShopDetailActivity.class,
										list.get(position).getShopId(),
										swp.getAreaId());
							}
						}
					});
		}
		return convertView;
	}

	class ViewHolder {
		ImageView imageview_item_shop_list_icon;
		TextView textView_item_shop_list_name;
		TextView textView_item_shop_list_price;
		TextView textView_item_shop_list_address_detail;
		TextView textView_item_shop_list_address;
		Button button_item_shop_list_go_appointment;
		LinearLayout service_go_home_detail;
	}

	private void goNext(Class clazz, int shopid, int areaid) {
		Intent intent = new Intent(act, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		((Activity) act).getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("servicename", servicename);
		intent.putExtra("shopid", shopid);
		intent.putExtra("clicksort", clicksort);
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("previous", previous);
		intent.putExtra("serviceid", fIntent.getIntExtra("serviceid", 0));
		intent.putExtra("servicetype", fIntent.getIntExtra("servicetype", 0));
		intent.putExtra("serviceloc", fIntent.getIntExtra("serviceloc", 0));
		intent.putExtra("petid", fIntent.getIntExtra("petid", 0));
		intent.putExtra("petkind", fIntent.getIntExtra("petkind", 0));
		intent.putExtra("petname", fIntent.getStringExtra("petname"));
		intent.putExtra("customerpetid",
				fIntent.getIntExtra("customerpetid", 0));
		intent.putExtra("customerpetname",
				fIntent.getStringExtra("customerpetname"));
		intent.putExtra("areaid", areaid);
		act.startActivity(intent);
	}

	private void goAppointment(Class clazz, int shopid, int areaid) {
		Intent intent = new Intent(act, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		((Activity) act).getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("servicename", servicename);
		intent.putExtra("previous", previous);
		intent.putExtra("serviceid", fIntent.getIntExtra("serviceid", 0));
		intent.putExtra("servicetype", fIntent.getIntExtra("servicetype", 0));
		intent.putExtra("serviceloc", fIntent.getIntExtra("serviceloc", 0));
		intent.putExtra("petid", fIntent.getIntExtra("petid", 0));
		intent.putExtra("petkind", fIntent.getIntExtra("petkind", 0));
		intent.putExtra("petname", fIntent.getStringExtra("petname"));
		intent.putExtra("customerpetid",
				fIntent.getIntExtra("customerpetid", 0));
		intent.putExtra("customerpetname",
				fIntent.getStringExtra("customerpetname"));
		intent.putExtra("shopid", shopid);
		intent.putExtra("areaid", areaid);
		intent.putExtra("clicksort", clicksort);
		act.startActivity(intent);
		act.finishWithAnimation();
	}

	private void showPop(String imgUrl) {
		pWin = null;
		if (pWin == null) {
			View view = mInflater.inflate(R.layout.not_practice, null);
			ImageView imageView_not_practice = (ImageView) view
					.findViewById(R.id.imageView_not_practice);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
			pWin.setBackgroundDrawable(new BitmapDrawable());
			pWin.setOutsideTouchable(true);
			pWin.setFocusable(true);
			DisplayMetrics dm = new DisplayMetrics();
			act.getWindowManager().getDefaultDisplay().getMetrics(dm);
			pWin.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
			Utils.setAttribute(act, pWin);
			ImageLoaderUtil.loadImg(imgUrl, imageView_not_practice, 0,
					new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
						}

						@Override
						public void onLoadingComplete(String path, View view,
								Bitmap bitmap) {
							if (view != null && bitmap != null) {
								ImageView iv = (ImageView) view;
								iv.setImageBitmap(bitmap);
							}
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {

						}
					});
			imageView_not_practice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
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
					Utils.onDismiss(act);
				}
			});
		}
	}
}
