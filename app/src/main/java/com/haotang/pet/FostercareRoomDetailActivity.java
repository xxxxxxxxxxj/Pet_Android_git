package com.haotang.pet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.Fostercare;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class FostercareRoomDetailActivity extends SuperActivity {
	private View vBack;
	private RelativeLayout rlMain;
	private ImageButton ibClose;
	private TextView tvName;
	private TextView tvDes;
	private ImageView ivImage;
	private Button btSubmit;
	private Intent fIntent;
	private Fostercare fc;
	private SharedPreferenceUtil spUtil;
	private TextView tv_fostercareroomdetail_roomsize;
	private SelectableRoundedImageView sriv_fostercare_roomdetail_img;
	private String desc_img;
	private RelativeLayout rl_fostercareroomdetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fostercare_roomdetail);

		findView();
		setView();
	}

	private void findView() {
		vBack = findViewById(R.id.view_fostercareroomdetail_back);
		rlMain = (RelativeLayout) findViewById(R.id.rl_fostercareroomdetail_main);
		rl_fostercareroomdetail = (RelativeLayout) findViewById(R.id.rl_fostercareroomdetail);
		ibClose = (ImageButton) findViewById(R.id.ib_fostercareroomdetail_close);
		tvName = (TextView) findViewById(R.id.tv_fostercareroomdetail_name);
		tvDes = (TextView) findViewById(R.id.tv_fostercareroomdetail_des);
		ivImage = (ImageView) findViewById(R.id.iv_fostercareroomdetail_img);
		btSubmit = (Button) findViewById(R.id.bt_fostercareroomdetail_submit);
		tv_fostercareroomdetail_roomsize = (TextView) findViewById(R.id.tv_fostercareroomdetail_roomsize);
		sriv_fostercare_roomdetail_img = (SelectableRoundedImageView) findViewById(R.id.sriv_fostercare_roomdetail_img);
	}

	private void setView() {
		// TODO Auto-generated method stub
		spUtil = SharedPreferenceUtil.getInstance(this);
		fIntent = getIntent();
		fc = (Fostercare) fIntent.getSerializableExtra("fostercare");
		desc_img = fIntent.getStringExtra("desc_img");
		if (desc_img != null && !TextUtils.isEmpty(desc_img)) {
			sriv_fostercare_roomdetail_img.bringToFront();
			rl_fostercareroomdetail.setVisibility(View.INVISIBLE);
			sriv_fostercare_roomdetail_img.setVisibility(View.VISIBLE);
			ImageLoaderUtil.loadImg(desc_img, sriv_fostercare_roomdetail_img,
					R.drawable.icon_production_default, null);
		}
		if (fc != null) {
			rl_fostercareroomdetail.bringToFront();
			rl_fostercareroomdetail.setVisibility(View.VISIBLE);
			sriv_fostercare_roomdetail_img.setVisibility(View.INVISIBLE);
			Utils.setStringText(tvName, fc.roomname);
			Utils.setStringText(tvDes, fc.roomdes);
			Utils.setStringText(tv_fostercareroomdetail_roomsize, fc.roomSize);
			if (fc.roomvalid) {
				btSubmit.setEnabled(true);
				btSubmit.setBackgroundResource(R.drawable.bg_button_orange);
			} else {
				btSubmit.setEnabled(false);
				btSubmit.setBackgroundResource(R.drawable.bg_button_gray_normal);
			}
			if (fc.roomimg != null && !"".equals(fc.roomimg)) {
				ImageLoaderUtil.loadImg(fc.roomimg, ivImage, 0,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String arg0, View view) {
								// TODO Auto-generated method stub
								ImageView iv = (ImageView) view;
								iv.setImageResource(R.drawable.bg_room_default);
							}

							@Override
							public void onLoadingFailed(String arg0, View view,
									FailReason arg2) {
								// TODO Auto-generated method stub
								ImageView iv = (ImageView) view;
								iv.setImageResource(R.drawable.bg_room_default);
							}

							@Override
							public void onLoadingComplete(String path,
									View view, Bitmap bitmap) {
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
							public void onLoadingCancelled(String arg0,
									View arg1) {
								// TODO Auto-generated method stub

							}
						});
			}
			btSubmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					UmengStatistics.UmengEventStatistics(mContext,Global.UmengEventID.click_Foster_SelectRoom);
					if (isLogin())
						goNext(FostercareOrderConfirmActivity.class);
					else
						goNext(LoginActivity.class);

				}
			});
		}
		ibClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		vBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
	}

	private boolean isLogin() {
		if (spUtil.getInt("userid", -1) > 0
				&& !"".equals(spUtil.getString("cellphone", "")))
			return true;
		return false;
	}

	private void goNext(Class clazz) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		getIntent().putExtra(Global.ANIM_DIRECTION(),
				Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("previous", Global.FOSTERCARE_TO_LOGIN);
		Bundle bundle = new Bundle();
		bundle.putSerializable("fostercare", fc);
		intent.putExtras(bundle);
		startActivity(intent);
		finishWithAnimation();
	}

}
