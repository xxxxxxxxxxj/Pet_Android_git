package com.haotang.pet;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.crop.Crop;

/**
 * <p>
 * Title:MediaActivity
 * </p>
 * <p>
 * Description:选择媒体界面
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-17 下午6:27:35
 */
public class MediaActivity extends SuperActivity implements OnClickListener {
	private RelativeLayout rl_pop_selectphoto_parent;
	private LinearLayout ll_pop_selectphoto_camera;
	private LinearLayout ll_pop_selectphoto_photo;
	private LinearLayout ll_pop_selectphoto_video;
	/**
	 * 标识从相机的返回
	 */
	private static final int REQUEST_CODE_CAPTURE_CAMEIA = 3;
	private String logoPathStr;
	private File logoFile;
	/**
	 * logo的路径
	 */
	private File logoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media);
		rl_pop_selectphoto_parent = (RelativeLayout) findViewById(R.id.rl_pop_selectphoto_parent);
		ll_pop_selectphoto_camera = (LinearLayout) findViewById(R.id.ll_pop_selectphoto_camera);
		ll_pop_selectphoto_photo = (LinearLayout) findViewById(R.id.ll_pop_selectphoto_photo);
		ll_pop_selectphoto_video = (LinearLayout) findViewById(R.id.ll_pop_selectphoto_video);
		rl_pop_selectphoto_parent.setOnClickListener(this);
		ll_pop_selectphoto_camera.setOnClickListener(this);
		ll_pop_selectphoto_photo.setOnClickListener(this);
		ll_pop_selectphoto_video.setOnClickListener(this);
		logoPath = new File(Environment.getExternalStorageDirectory(),
				"logoPath");
		if (!logoPath.exists()) {
			logoPath.mkdirs();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_pop_selectphoto_parent:
			finish();
			break;
		case R.id.ll_pop_selectphoto_camera:
			getImageFromCamera();
			break;
		case R.id.ll_pop_selectphoto_photo:
			Crop.pickImage(this);
			break;
		case R.id.ll_pop_selectphoto_video:
			startActivity(new Intent(this,
					MediaRecorderActivity.class));
			break;
		default:
			break;
		}
	}
	
	/**
	 * 从相机中获取拍照图片
	 */
	private void getImageFromCamera() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String fileName = "Temp_camera"
					+ String.valueOf(System.currentTimeMillis());
			File cropFile = new File(logoPath, fileName);
			Uri fileUri = Uri.fromFile(cropFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			logoPathStr = fileUri.getPath();
			startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
		} else {
			Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Crop.REQUEST_PICK) {
				beginCrop(data.getData());
			} else if (requestCode == Crop.REQUEST_CROP) {
				handleCrop(resultCode, data);
			} else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
				if (logoPathStr != null) {
					beginCrop(Uri.fromFile(new File(logoPathStr)));
				}
			}
		}
	}

	private void handleCrop(int resultCode, Intent result) {
		if (resultCode == Activity.RESULT_OK) {
			Uri output = Crop.getOutput(result);
			Intent intent = new Intent(this, SendSelectPostActivity.class);
			intent.putExtra("flag", "pic");
			intent.putExtra("picOutput", output);
			startActivity(intent);
		} else if (resultCode == Crop.RESULT_ERROR) {
		}
	}

	private void beginCrop(Uri source) {
		Uri destination = Uri.fromFile(new File(getCacheDir(),
				"Pet_Crop"));
		Crop.of(source, destination).asSquare().start(this);
	}
}
