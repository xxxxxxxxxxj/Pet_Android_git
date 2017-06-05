package com.haotang.pet.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class UpdateUtil {
	public static boolean compareVersion(String serviceVersion,
			String localVersion) {
		Utils.mLogError("serviceVersion:" + serviceVersion + " localVersion:"
				+ localVersion);
		boolean result = false;
		serviceVersion = serviceVersion.replace(".", "");
		localVersion = localVersion.replace(".", "");

		int flagLength = 0;
		int versionLength = serviceVersion.length() > localVersion.length() ? localVersion
				.length() : serviceVersion.length();
		for (int i = 0; i < versionLength; i++) {
			if (Integer.parseInt(serviceVersion.charAt(i) + "") > Integer
					.parseInt(localVersion.charAt(i) + "")) {
				result = true;
				break;
			} else if (Integer.parseInt(serviceVersion.charAt(i) + "") < Integer
					.parseInt(localVersion.charAt(i) + "")) {
				break;
			} else {
				flagLength = i;
			}
		}
		if (!result
				&& flagLength + 1 == versionLength
				&& serviceVersion.length() > localVersion.length()
				&& 0 < Integer.parseInt(serviceVersion.charAt(versionLength)
						+ "")) {
			result = true;
		}
		return result;
	}

	// 自动安装一个apk文件
	public static void installAPK(Context context, File file) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
		// mActivity.finish();
	}

}
