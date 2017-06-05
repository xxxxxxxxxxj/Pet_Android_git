package com.haotang.pet.adapter;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.FostercareOrderConfirmActivity;
import com.haotang.pet.FostercareRoomDetailActivity;
import com.haotang.pet.LoginActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.Fostercare;
import com.haotang.pet.entity.Room;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class RoomAdapter extends BaseAdapter {
	private SuperActivity context;
	private ArrayList<Room> list;
	private LayoutInflater mInflater;
	private Fostercare fc;
	private SharedPreferenceUtil spUtil;

	public RoomAdapter(SuperActivity context, ArrayList<Room> list,
			Fostercare fc) {
		super();
		this.context = context;
		this.list = list;
		this.fc = fc;
		mInflater = LayoutInflater.from(context);
		spUtil = SharedPreferenceUtil.getInstance(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setFC(Fostercare fc) {
		this.fc = fc;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(
					R.layout.fostercare_chooseroom_item, null);
			holder.srivImage = (SelectableRoundedImageView) convertView
					.findViewById(R.id.sriv_fostercarechooseroom_item_room);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_fostercarechooseroom_item_roomname);
			holder.tvDes = (TextView) convertView
					.findViewById(R.id.tv_fostercarechooseroom_item_roomdes);
			holder.tvPrice = (TextView) convertView
					.findViewById(R.id.tv_fostercarechooseroom_item_roomprice);
			holder.tvFlag = (TextView) convertView
					.findViewById(R.id.tv_fostercarechooseroom_item_roomflag);
			holder.tvInvalid = (TextView) convertView
					.findViewById(R.id.tv_fostercarechooseroom_item_roominvalid);
			holder.llRoom = (LinearLayout) convertView
					.findViewById(R.id.ll_fostercarechooseroom_item_room);
			holder.tv_fostercarechooseroom_item_other = (TextView) convertView
					.findViewById(R.id.tv_fostercarechooseroom_item_other);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final Room room = list.get(position);
		String other_desc = room.other_desc;
		Utils.setStringTextGone(holder.tv_fostercarechooseroom_item_other, other_desc);
		String imgtag = null;
		if (room.valid) {
			imgtag = room.img;
			// holder.srivImage.setBorderColor(context.getResources().getColor(R.color.aD1494F));
			holder.tvFlag.setTextColor(Color.RED);
			holder.tvFlag.setBackgroundResource(R.drawable.bg_redline_round);
			holder.tvName.setTextColor(context.getResources().getColor(
					R.color.a222222));
			holder.tvPrice.setTextColor(context.getResources().getColor(
					R.color.aD1494F));
			holder.tvInvalid.setVisibility(View.GONE);
		} else {
			imgtag = room.imggray;
			// holder.srivImage.setBorderColor(context.getResources().getColor(R.color.bordergray));
			holder.tvName.setTextColor(context.getResources().getColor(
					R.color.a888888));
			holder.tvPrice.setTextColor(context.getResources().getColor(
					R.color.a888888));
			holder.tvInvalid.setVisibility(View.VISIBLE);
			holder.tvFlag.setBackgroundResource(R.drawable.bg_grayline_round);
			holder.tvFlag.setTextColor(context.getResources().getColor(
					R.color.a888888));
		}
		holder.tvName.setText(room.kindname);
		holder.tvDes.setText(room.des);

		String text = "¥" + Utils.formatDouble(room.price);
		SpannableString ss = new SpannableString(text);
		ss.setSpan(
				new TextAppearanceSpan(context, R.style.swimdetailshowtext0),
				1, ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		holder.tvPrice.setText(ss);
		if (room.flag == null || "".equals(room.flag)) {
			holder.tvFlag.setVisibility(View.GONE);
		} else {
			holder.tvFlag.setVisibility(View.VISIBLE);
			holder.tvFlag.setText(room.flag);
		}
		holder.srivImage.setTag(imgtag);
		if (imgtag != null && !"".equals(imgtag)) {
			ImageLoaderUtil.loadImg(imgtag, holder.srivImage, 0,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View view) {
							// TODO Auto-generated method stub
							SelectableRoundedImageView iv = (SelectableRoundedImageView) view;
							iv.setImageResource(R.drawable.bg_room_default);
						}

						@Override
						public void onLoadingFailed(String arg0, View view,
								FailReason arg2) {
							// TODO Auto-generated method stub
							SelectableRoundedImageView iv = (SelectableRoundedImageView) view;
							iv.setImageResource(R.drawable.bg_room_default);
						}

						@Override
						public void onLoadingComplete(String path, View view,
								Bitmap bitmap) {
							// TODO Auto-generated method stub
							if (view != null && bitmap != null) {
								SelectableRoundedImageView iv = (SelectableRoundedImageView) view;
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
		}

		holder.llRoom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UmengStatistics.UmengEventStatistics(context,Global.UmengEventID.click_Foster_SelectRoom);
				if (room.valid) {
					if (isLogin())
						goNext(FostercareOrderConfirmActivity.class, room);
					else
						goNext(LoginActivity.class, room);
				}
			}
		});
		holder.srivImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goNext(FostercareRoomDetailActivity.class, room);
			}
		});

		return convertView;
	}

	private void goNext(Class clazz, Room r) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		context.getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		fc.roomid = r.id;
		fc.roomname = r.kindname;
		fc.roomprice = r.price;
		fc.roomlistprice = r.listprice;
		fc.roomdes = r.scopedes;
		fc.roomimg = r.img2;
		fc.roomvalid = r.valid;
		fc.roomSize = r.sizeDesc;
		Bundle bundle = new Bundle();
		bundle.putSerializable("fostercare", fc);
		intent.putExtras(bundle);
		intent.putExtra("previous", Global.FOSTERCARE_TO_LOGIN);
		context.startActivity(intent);
	}

	private boolean isLogin() {
		if (spUtil.getInt("userid", -1) > 0
				&& !"".equals(spUtil.getString("cellphone", "")))
			return true;
		return false;
	}

	class Holder {
		TextView tv_fostercarechooseroom_item_other;
		SelectableRoundedImageView srivImage;
		TextView tvName;
		TextView tvDes;
		TextView tvPrice;
		TextView tvFlag;
		TextView tvInvalid;
		LinearLayout llRoom;
	}

}
