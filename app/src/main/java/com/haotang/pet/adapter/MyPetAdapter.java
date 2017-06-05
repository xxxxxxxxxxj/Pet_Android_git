package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class MyPetAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Pet> mypetList;
	private LayoutInflater mInflater;
	private int petkind;

	public MyPetAdapter(Context context, int petkind, ArrayList<Pet> mypetList) {
		this.context = context;
		this.mypetList = mypetList;
		this.petkind = petkind;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mypetList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mypetList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setPetKind(int petkind) {
		this.petkind = petkind;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_mypet, parent, false);
			holder.mypet_icon = (SelectableRoundedImageView) convertView
					.findViewById(R.id.sriv_mypetitem_image);
			holder.mypet_name = (TextView) convertView
					.findViewById(R.id.tv_mypetitem_name);
			holder.vMask = convertView.findViewById(R.id.view_mypet_item_mask);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (mypetList.get(position).sa == 0) {
			holder.vMask.setVisibility(View.VISIBLE);
		} else {
			holder.vMask.setVisibility(View.GONE);
		}
		holder.mypet_icon.setTag(mypetList.get(position).image);
		ImageLoaderUtil.loadImg(mypetList.get(position).image,
				holder.mypet_icon, 0, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String path, View view,
							FailReason arg2) {
						// TODO Auto-generated method stub
						ImageView iv = (ImageView) view;
						if (petkind == 1)
							iv.setImageResource(R.drawable.dog_icon_unnew);
						else if (petkind == 2)
							iv.setImageResource(R.drawable.cat_icon_unnet);
						else
							iv.setImageResource(R.drawable.icon_production_default);
					}

					@Override
					public void onLoadingComplete(String path, View view,
							Bitmap bitmap) {
						// TODO Auto-generated method stub
						if (view != null && bitmap != null) {
							ImageView iv = (ImageView) view;
							String imagetag = (String) iv.getTag();
							if (path != null && path.equals(imagetag)) {
								iv.setImageBitmap(bitmap);

							}
						}

					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}
				});
		holder.mypet_name.setText(mypetList.get(position).nickName);
		return convertView;
	}

	private class Holder {
		SelectableRoundedImageView mypet_icon;
		TextView mypet_name;
		View vMask;
	}

}
