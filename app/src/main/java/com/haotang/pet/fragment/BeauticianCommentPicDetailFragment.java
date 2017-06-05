package com.haotang.pet.fragment;

import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MProgressDialog;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class BeauticianCommentPicDetailFragment extends Fragment {
	private Activity mActivity;
	private ImageView ivImage;
	private TextView iv_beauticianproduction_step;
	private String mPath;
	private MProgressDialog pDialog;
	private String text;
	public void setData(String path){
		this.mPath = path;
	}
	public void setData(String path,String text){
		this.mPath = path;
		this.text = text;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.mActivity = activity;
		pDialog = new MProgressDialog(mActivity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		findView(view);
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.beauticiancommentpicdetailfragment, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setViw();
	}
	
	private void findView(View view) {
		ivImage = (ImageView) view.findViewById(R.id.iv_beauticianproduction_detail);
		iv_beauticianproduction_step = (TextView) view.findViewById(R.id.iv_beauticianproduction_step);
	}

	private void setViw() {
		try {
			iv_beauticianproduction_step.setText(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ivImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mActivity.finish();
			}
		});
		if(mPath!=null&&!"".equals(mPath.trim())){
			ImageLoaderUtil.loadImg(mPath, ivImage,0,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					pDialog.showDialog();
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					pDialog.closeDialog();
				}
				
				@Override
				public void onLoadingComplete(String path, View view, Bitmap bitmap) {
					// TODO Auto-generated method stub
					pDialog.closeDialog();
					if(view!=null&&bitmap!=null){
						ImageView iv = (ImageView)view;
						iv.setImageBitmap(bitmap);
					}
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					pDialog.closeDialog();
					
				}
			});
		}
	}
}
