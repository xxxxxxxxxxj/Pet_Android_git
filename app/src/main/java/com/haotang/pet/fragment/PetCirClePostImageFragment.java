package com.haotang.pet.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.PetCirClePostImageActivity;
import com.haotang.pet.R;
import com.haotang.pet.MultiImage.PickBigImagesActivity;
import com.haotang.pet.MultiImage.ZoomImageView;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.Utils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PetCirClePostImageFragment extends Fragment{

	private TextView textView_show_current;
	private ImageView imageview_pet_circle_image;
	private String mPath;
	PetCirClePostImageActivity imageActivity;
	private MProgressDialog mPDialog;
	private boolean isVisible = false;
	private Bitmap mBitmap = null;
	private int size;
	private int current;
	private int indexTag;
	@Override
	@Deprecated
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		imageActivity = (PetCirClePostImageActivity) activity;
		mPDialog = new MProgressDialog(imageActivity);
		mPDialog.setCanceledOnTouchOutside(true);
	}
	public void setData(String path,int size,int current,int indexTag){
		this.mPath = path;
		this.size = size;
		this.current = current;
		this.indexTag = indexTag;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.mLogError("==-->mPDialog 0");
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			if (indexTag==1) {
				mPDialog.showDialog();
			}
			Utils.mLogError("==-->mPDialog 1");
			if (mBitmap!=null) {
				if (indexTag==1) {
					if (mPDialog.isShowing()) {
						mPDialog.closeDialog();
					}
				}
			}
		}else {
			isVisible = false;
			Utils.mLogError("==-->mPDialog 2");
			try {
				mBitmap.recycle();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				if (indexTag==1) {
					mPDialog.closeDialog();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view  = inflater.inflate(R.layout.fragment_pet_circle_post_image, null);
		imageview_pet_circle_image = (ImageView) view.findViewById(R.id.imageview_pet_circle_image);
		textView_show_current = (TextView) view.findViewById(R.id.textView_show_current);
		textView_show_current.setText((current+1)+"/"+size);
		if (!mPath.startsWith("http://") && !mPath.startsWith("https://")) {
			showHttpImg("file://"+mPath);
		}else {
			showHttpImg(mPath);
		}
		
		imageview_pet_circle_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().setResult(Global.RESULT_OK);
				imageActivity.finish();
			}
		});
		return view;
	}
	private void showHttpImg(String mPath) {
		ImageLoaderUtil.instance.displayImage(mPath, imageview_pet_circle_image, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View view) {
				ImageView iv = (ImageView) view;
				iv.setImageResource(0);
			}
			
			@Override
			public void onLoadingFailed(String arg0, View view, FailReason arg2) {
				// TODO Auto-generated method stub
				ImageView iv = (ImageView) view;
				iv.setImageResource(R.drawable.icon_production_default);
			}
			
			@Override
			public void onLoadingComplete(String path, View view,
					Bitmap bitmap) {
				try {
					if (indexTag==1) {
						if (mPDialog.isShowing()) {
							mPDialog.closeDialog();
						}
						mPDialog.closeDialog();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (view != null && bitmap != null) {
					mBitmap = bitmap;
					ImageView iv = (ImageView) view;
					int imgWidth = bitmap.getWidth();
					int imgHeight = bitmap.getHeight();
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT);
					params.width = Utils.getDisplayMetrics(imageActivity)[0];
					params.topMargin = Utils.dip2px(imageActivity, 10);
					params.height = ((params.width * imgHeight) / imgWidth);
					params.addRule(RelativeLayout.CENTER_IN_PARENT);
					iv.setLayoutParams(params);
					iv.setScaleType(ScaleType.FIT_XY);
					iv.setImageBitmap(bitmap);
//					iv.setSourceImageBitmap(bitmap, getActivity());
				}
			}

			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			mBitmap.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
