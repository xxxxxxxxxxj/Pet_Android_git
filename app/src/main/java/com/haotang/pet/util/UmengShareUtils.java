package com.haotang.pet.util;

import android.app.Activity;
import android.content.Intent;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * <p>
 * Title:UmengShareUtils
 * </p>
 * <p>
 * Description:友盟分享工具类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-18 下午5:08:22
 */
public class UmengShareUtils {
	private Activity mActivity;
	/** 友盟分享服务 */
	public final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	/** QQ app id */
	private static final String QQAppId = "1104724367";
	/** QQ app key */
	private static final String QQAppKey = "gASimi0oEHprSSxe";
	/** 要分享的内容 */
	private String content;
	/** 分享的url */
	private String url;
	/** 分享的标题 */
	private String title;
	private String imgUrl;

	public UmengShareUtils(Activity mActivity, String content, String url,
			String title, String imgUrl) {
		this.imgUrl = imgUrl;
		this.url = url;
		this.title = title;
		this.mActivity = mActivity;
		this.content = content;
		configPlatforms();
		setShareContent();
		// 设置分享面板显示的内容
		mController.getConfig().setPlatforms(SHARE_MEDIA.QQ,SHARE_MEDIA.SINA);
	}

	/** 调用分享面板 */
	public void share() {
		mController.openShare(mActivity, false);
	}

	/**
	 * 如需使用sso需要在onActivity中调用此方法
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void authSSO(int requestCode, int resultCode, Intent data) {
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private void configPlatforms() {
		// 添加QQ、QZone平台
		addQQQZonePlatform();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, QQAppId,
				QQAppKey);
		qqSsoHandler.setTitle(content);
		qqSsoHandler.addToSocialSDK();
	}

	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	private void setShareContent() {
		// 分享图片
		UMImage urlImage = new UMImage(mActivity, imgUrl);
		// 设置QQ分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setTitle(title);
		qqShareContent.setShareContent(content);
		qqShareContent.setTargetUrl(url);
		qqShareContent.setShareMedia(urlImage);
		boolean setShareMedia = mController.setShareMedia(qqShareContent);
		
		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setTitle(title);
		sinaContent.setShareContent(content);
		sinaContent.setShareImage(new UMImage(mActivity, imgUrl));
		sinaContent.setTargetUrl(url);
		mController.setShareMedia(sinaContent);
	}
}
