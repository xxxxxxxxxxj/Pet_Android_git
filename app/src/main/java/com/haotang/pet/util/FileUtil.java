package com.haotang.pet.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

/**
 * <p>
 * Title:FileUtil
 * </p>
 * <p>
 * Description:下载文件工具类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-1-11 下午4:16:04
 */
public class FileUtil {

	public static File updateDir = null;
	public static File updateFile = null;
	/*********** 保存升级APK的目录 ***********/
	public static final String PetApplication = "petUpdateApplication";

	public static boolean isCreateFileSucess;

	// 保存视频
	public static File videoDir = null;
	public static File videoFile = null;
	/*********** 保存升级APK的目录 ***********/
	public static final String videoDirApplication = "videoDirApplication";
	public static boolean isCreateVideoFileSucess;

	/**
	 * 方法描述：createFile方法
	 * 
	 * @param String
	 *            app_name
	 * @return
	 * @see FileUtil
	 */
	public static void createFile(String app_name) {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			isCreateFileSucess = true;
			updateDir = new File(Environment.getExternalStorageDirectory()
					+ "/" + PetApplication + "/");
			updateFile = new File(updateDir + "/" + app_name + ".apk");
			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					isCreateFileSucess = false;
					e.printStackTrace();
				}
			}
		} else {
			isCreateFileSucess = false;
		}
	}

	/**
	 * 保存视频
	 */
	public static void createVideoFile(String video_name) {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			isCreateVideoFileSucess = true;
			videoDir = new File(Environment.getExternalStorageDirectory() + "/"
					+ videoDirApplication + "/");
			videoFile = new File(videoDir + "/" + video_name);
			if (!videoDir.exists()) {
				videoDir.mkdirs();
			}
			if (!videoFile.exists()) {
				try {
					videoFile.createNewFile();
				} catch (IOException e) {
					isCreateVideoFileSucess = false;
					e.printStackTrace();
				}
			}
		} else {
			isCreateVideoFileSucess = false;
		}
	}
}