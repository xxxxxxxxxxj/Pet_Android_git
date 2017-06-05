package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.CertiOrder;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class CustomerPetAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Pet> list;
	private LayoutInflater mInflater;
	private boolean addflag;
	private String statusName;

	public CustomerPetAdapter(Context context, ArrayList<Pet> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void setAddFlag(boolean addflag) {
		this.addflag = addflag;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater
					.inflate(R.layout.petlistitem, parent, false);
			holder.sriv = (SelectableRoundedImageView) convertView
					.findViewById(R.id.sriv_petlistitem_image);
			holder.tv = (TextView) convertView
					.findViewById(R.id.tv_petlistitem_name);
			holder.rl_petlistitem_state = (RelativeLayout) convertView
					.findViewById(R.id.rl_petlistitem_state);
			holder.tv_petlistitem_state = (TextView) convertView
					.findViewById(R.id.tv_petlistitem_state);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final Pet pet = list.get(position);
		if (addflag && position == list.size() - 1) {
			holder.rl_petlistitem_state.setVisibility(View.INVISIBLE);
			holder.tv.setVisibility(View.GONE);
			holder.sriv.setImageResource(R.drawable.icon_addimage);
		} else {
			if (pet != null) {
				Utils.setText(holder.tv, pet.nickName, "", View.VISIBLE,
						View.INVISIBLE);
				int petKind = pet.kindid;
				int certiDogSize = pet.certiDogSize;
				if (petKind == 2) {// 猫
					holder.rl_petlistitem_state.setVisibility(View.INVISIBLE);
				} else {// 狗
					if (certiDogSize == 1 || certiDogSize == 2) {
						// 小型犬
						holder.rl_petlistitem_state.setVisibility(View.VISIBLE);
					} else {
						// 大型犬
						holder.rl_petlistitem_state
								.setVisibility(View.INVISIBLE);
					}
				}
				String certiOrderStatus = pet.certiOrderStatus;
				CertiOrder certiOrder = pet.certiOrder;
				if (certiOrder != null) {
					String[] statusNames = certiOrder.statusName;
					int status = certiOrder.status;
					if (statusNames != null && statusNames.length > 0) {
						if (status < statusNames.length) {
							statusName = statusNames[status];
						}
					} else {
						statusName = certiOrderStatus;
					}
				} else {
					statusName = certiOrderStatus;
				}
				Utils.setStringText(holder.tv_petlistitem_state, statusName);
				holder.tv.setVisibility(View.VISIBLE);
				holder.sriv.setTag(pet.image);
				if (pet.image != null && !"".equals(pet.image.trim())) {
					ImageLoaderUtil.loadImg(pet.image, holder.sriv, R.drawable.icon_production_default,
							new ImageLoadingListener() {

								@Override
								public void onLoadingStarted(String arg0,
										View view) {
									// TODO Auto-generated method stub
								}

								@Override
								public void onLoadingFailed(String arg0,
										View view, FailReason arg2) {
									// TODO Auto-generated method stub
									/*
									 * SelectableRoundedImageView iv =
									 * (SelectableRoundedImageView) view;
									 * iv.setImageResource
									 * (R.drawable.icon_production_default);
									 */
								}

								@Override
								public void onLoadingComplete(String path,
										View view, Bitmap bitmap) {
									// TODO Auto-generated method stub
									if (view != null && bitmap != null) {
										SelectableRoundedImageView iv = (SelectableRoundedImageView) view;
										String imagetag = (String) iv.getTag();
										if (path != null
												&& path.equals(imagetag)) {
											iv.setImageBitmap(bitmap);
										}
									}
								}

								@Override
								public void onLoadingCancelled(String arg0,
										View arg1) {
									// TODO Auto-generated method stub

								}
							});
				}
			}
		}
		return convertView;
	}

	class Holder {
		RelativeLayout rl_petlistitem_state;
		TextView tv_petlistitem_state;
		SelectableRoundedImageView sriv;
		TextView tv;
	}
}
