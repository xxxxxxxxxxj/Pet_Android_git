package com.haotang.pet.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Base64;

public class Global {
	public static final int CITYREQUESTCODE = 100;
	public static final int BOOKINGSERVICEREQUESTCODE_PET = 101;
	public static final int BOOKINGSERVICEREQUESTCODE_ADDR = 102;
	public static final int BOOKINGSERVICEREQUESTCODE_TIME = 103;
	public static final int ORDERDETAILREQUESTCODE_CONTACT = 104;
	public static final int ORDERDETAILREQUESTCODE_COUPON = 105;
	public static final int ORDERDETAILREQUESTCODE_NOTE = 106;
	public static final int SERVICEREQUESTCODE_PET = 107;
	public static final int SERVICEREQUESTCODE_ADDR = 108;
	public static final int SERVICEREQUESTCODE_TIME = 109;
	public static final int SERVICEREQUESTCODE_BEAUTICIAN = 110;
	public static final int APK_DOWNLOAD_OVER = 112;
	public static final int APK_DOWNLOAD_FAIL = 113;
	public static final int APK_DOWNLOADING = 111;
	public static final int RESULT_OK = 1000;
	public static final int SERVICEDETAIL_TO_APPOINTMENT = 1001;
	public static final int LOGIN_TO_AGREEMENT = 1002;
	public static final int FOSTERCARE_TO_AGREEMENT = 1003;
	public static final int PET_BIRTHDAY = 1004;
	public static final int MY_TO_ADDPET = 1005;
	public static final int ADDPET_TO_PETLIST = 1006;
	public static final int MY_TO_CHANGCUSTOMERINFORMATION = 1007;
	public static final int MY_TO_LOGIN = 1008;
	public static final int MY_TO_BALANCE = 1009;
	public static final int MY_TO_COUPON = 1010;
	public static final int MY_TO_PETINFO = 1011;
	public static final int PETINFO_TO_ADDPET = 1012;
	public static final int PETINFO_TO_EDITPET = 1013;
	public static final int SHOPLIST_TO_ROOMLIST = 1014;
	public static final int ALI_SDK_CHECK_FLAG = 1015;
	public static final int ALI_SDK_PAY_FLAG = 1016;
	public static final int APPOINTMENT_TO_ADDPET = 1017;
	public static final int APPOINTMENT_TO_ADDSERVICE = 1018;
	public static final int BATH_TO_ORDERPAY = 1019;
	public static final int PETINFO_ADDPET_POP = 1020;
	public static final int ORDERLIST_TO_ADD_PET = 1023;
	public static final int ORDERLIST_TO_ADD_PET_BACK_PETINFO_BACK_MY = 1021;
	public static final int PETADD_BACK_PETINFO_BACK_MY = 1022;
	public static final int FOSTERCARE_TO_ORDERPAY = 1023;
	public static final int DELETEPET_TO_UPDATEUSERINFO = 1024;
	public static final int H5_TO_LOGIN = 1025;
	public static final int TOPMSG_SCROLL = 1026;
	public static final int SERVICEFEATURE_TO_PETLIST = 1027;
	public static final int SERVICEFEATURE_TO_PETLIST_1 = 1028;
	public static final int BEAUTICIAN_TO_TIME = 1029;
	public static final int AD_TO_LOGIN = 1030;
	public static final int MAINBANNER_TO_AD = 1031;
	public static final int BEAUTICIAN_TO_APPOINTMENT = 1032;
	public static final int APPOINTMENT_TO_AREA = 1033;
	public static final int AREA_TO_HINT = 1034;
	public static final int NOAREA_TO_APPOINTMENT = 1035;
	public static final int TOPMSG_TO_AD = 1036;
	public static final int MAIN_TO_AREALIST = 1037;
	
	public static final int PRE_SERVICEACTIVITY_TO_ADDSERICEADDRRESSACTIVITY = 2000;
	public static final int PRE_MAINFRAGMENT_TO_SERVICEACTIVITY = 2001;
	public static final int PRE_MAINFRAGMENT_TO_BOOKINGSERVICEACTIVITY = 2002;
	public static final int PRE_MAINFRAGMENT = 2003;
	public static final int PRE_BOOKINGSERVICEACTIVITY_TO_LOGINACTIVITY = 2004;
	public static final int PRE_BOOKINGSERVICEACTIVITY_TO_ORDERDETAILACTIVITY = 2005;
	public static final int PRE_SERVICEACTIVITY_TO_LOGINACTIVITY = 2006;
	public static final int PRE_SERVICEACTIVITY_TO_ORDERDETAILACTIVITY = 2007;
	public static final int PRE_ORDERDETAILACTIVITY_TO_AVAILABLECOUPONACTIVITY = 2008;//OrderDetailActivity
	public static final int PRE_ORDERDETAILFROMORDERACTIVITY_TO_AVAILABLECOUPONACTIVITY= 2009;//OrderDetailFromOrderActivity
	public static final int PRE_ORDERDETAILFROMORDERTOCONFIRM_TO_AVAILABLECOUPONACTIVITY= 2010;//OrderDetailFromOrderToConfirmActivity
	public static final int PRE_PAYSUCCESSACTIVITY_TO_MAINACTIVITY= 2011;
	public static final int PRE_ORDDRDDTAILFROMORDRRACTIVITY_TO_MAINACTIVITY= 2012;
	public static final int PRE_ORDERDETAILFROMORDERTOCONFIRMACTIVITY_TO_MAINACTIVITY= 2013;
	public static final int PRE_ORDERDETAILACTIVITY_TO_MAINACTIVITY= 2014;
	public static final int PRE_MAINFRAGMENT_TO_KNOWLEDGEFRAGMENT= 2015;
	public static final int PRE_MALLFRAGMENT_TO_LOGIN_BACK_MALLFRAGMENT = 2016;
	public static final int PRE_MALLFRAGMENT_TO_LOGIN_BACK_MAINACTIVITY=2017;
	public static final int PRE_LOGINOUT_TO_BACK_MAINACTIVITY=2018;
	public static final int PRE_PUSH_TO_LOGIN=2019;
	public static final int PRE_PUSH_TO_ORDER=2020;
	public static final int PRE_PUSH_TO_NOSTARTAPP_ORDER=2021;
	public static final int PRE_PUSH_TO_NOSTARTAPP_LOGIN=2022;
	public static final int PRE_PUSH_TO_ORDER_ORDERDETAILHASOPEN=2023;
	public static final int PRE_PUSH_TO_NOSTARTAPP_COUPON=2024;
	public static final int PRE_EVALUATEOVER_BACK_MAIN=2025;
	public static final int PRE_LOGOUT_TO_MAINACTIVITY=2026;
	public static final int PRE_COMMONADDR_TO_ADDADDR=2027;
	public static final int PRE_BEAUTICIANPRODUCTIONLIST_TO_PRODUCTIONDETAIL=2028;
	public static final int PRE_BEAUTICIANDETAIL_TO_PRODUCTIONDETAIL=2029;
	public static final int PRE_RECHARGEPAGE_ZF=2030;//支付宝
	public static final int PRE_RECHARGEPAGE_WX=2031;//微信
	public static final int MYCOUPON_UNLOGIN=2032;
	public static final int PRE_MEMBERFRAGMENT_TO_LOGIN=2033;
	public static final int PRE_ORDERDETAIL_TO_UPGRADESERVICE=2034;
	public static final int PRE_PUSH_TO_EVALUATE=2035;
	public static final int PRE_ORDER_LIST_TO_MAINACTIVITY=2036;
	public static final int PRE_ORDERDETAIL_TO_RECHARGE=2037;
	public static final int PRE_SERVICEDETAIL_TO_SHOPLIST=2038;
	public static final int PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT=2039;
	public static final int PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT_PET=2040;
	public static final int FOSTERCARE_APPOINTMENT_CHANGEPET=2041;
	public static final int FOSTERCARE_APPOINTMENT_CHANGEADDR=2042;
	public static final int FOSTERCARE_APPOINTMENT_CHANGEADDR_LOGOUT=2043;
	public static final int FOSTERCARE_APPOINTMENT_SHOPLIST=2044;
	public static final int FOSTERCARE_TO_LOGIN=2045;
	public static final int FOSTERCAREORDER_TO_COUPON=2046;
	public static final int ORDERFOSTERDETAILACTIVITY_TO_EVA = 2047;//寄养到eva
	public static final int ORDER_EVAOVER_TO_ORDERDETAILUPDATE_AND_POPSHOW=2048;//评价跳转评价完成 评价成功后返回订单详情，并确认是否弹窗发送红包
	public static final int ORDER_TOPAYDETAIL_TOGIVE_MONEY=2049;//待付款到收银台支付
	public static final int ORDER_OTHER_STATUS_TO_SHOPDETAIL=2050;//订单详情到店铺/美容师详情
	public static final int ORDER_TOPAYDETAIL_TOGOUZHENG_MONEY=2051;//办理狗证到收银台支付
	public static final int MAIN_TO_BEAUTICIANLIST  = 3000;
	public static final int FOSTER_TO_CHOOSE_PET=3001;
	public static final int ORDER_CHANGE_CUSTOMEORPHONE=3002;
	public static final int ORDER_WAIT_TO_PAY_DETAIL_CHANGE_CUSTOMER=3003;
	public static final int ORDER_WAIT_TO_CHANGE_CUSTOME_REQUEST_CODE=3004;
	
	
	public static final int SWIM_APPOINMENT=4000;//pre
	public static final int SWIM_APPOINMENT_CHOOSEADDRESS=4001;
	public static final int SWIM_APPOINMENT_CHOOSEADDRESS_OTHER=4002;
	public static final int SWIM_APPOINMENT_CHOOSE_TIME=4003;
	public static final int SWIM_DETAIL_ADD_PET=4004;
	public static final int SWIM_DETAIL_TO_ORDERDETAIL=4005;
	public static final int SWIM_DETAIL_TO_LOCALACTIVITY=4006;
	public static final int SWIM_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET=4007;
	public static final int SWIM_MYPET_TO_SWIMAPPOINTMENT=4008;
	public static final int SWIM_APPOINMENT_TO_WEBVIEW=4009;
	public static final int SWIM_DETAIL_TO_WEBVIEW=4010;
	public static final int SWIM_DETAIL_CLICK_ONE_TO_CHANGE_PET=4011;
	public static final int MAIN_TO_SWIM_DETAIL=4012;
	
	public static final int URGENT_TO_ORDERDETAIL=5000;
	public static final int PET_CARD_TO_LOGIN = 5001;
	public static final int PRE_CUSTOMERPETINFO_TO_FOSTERCAREAPPOINTMENT_PET = 5002;
	public static final int AD_TO_LOGIN_TO_ORDER = 5003;
	
	public static final int PET_CIRCLE_TO_H5 = 5555;
	public static final int PET_CIRCLE_TO_POST = 5556;
	public static final int PETCIRCLEINSIDEDETAIL_TO_LOGIN_BACK=5557;
	public static final int PETCIRCLEINSIDEDETAIL_TO_DIALOG_SHOW=5558;
	public static final int PETCIRCLELIST_TO_IMAGE=5559;
	
	public static final int PAYSUCCESS_TO_MYFRAGMENT=6001;
	
	public static final int PUSH_TO_ORDER_ORDER_BEAU_ACCEPT = 7001;
	public static final int PUSH_TO_ORDER_ORDER_NO_BEAU_ACCEPT = 7002;
	
	
	public static final int TRAIN_TO_CHOOSE_PET_REQUESTCODE = 7500;
	public static final int TRAIN_TO_CHOOSE_PET_PRE = 7501;
	public static final int TRAIN_TO_CHOOSE_TIME=7502;
	public static final int MAIN_TO_TRAIN_DETAIL=7503;
	public static final int TRAIN_MAINFRAGMENT_UNLOGIN_TO_CHOOSEPET=7504;
	public static final int TRAIN_TO_ORDER_CONFIRM = 7505;
	
	public static final int CARDS_TO_MAINACTIVITY = 7600;
	
	public static final int CARDSDETAIL_TO_ORDERPAY=7700;
	public static final int CARDSORDERDETAIL_TO_CHOOSE_CARDS=7701;
	public static final int CARD_CHOOSE_PET=7702;
	public static final int CARD_NOTPET_CHOOSE_PET=7703;
	
	public static final int GROWTH_TO_USERMEMBERFRAGMENT = 8001;
	
	public static final int UPDATE_TO_ORDERPAY = 8200;
	public static boolean isPop = true;//控制训练进入弹窗
	public static final String APP_ID = "wx1668e9f200eb89b2";//微信appid
	public static final int LIVE_TO_SWIM_DETAIL = 7005;
	public static final int LIVEDELAYEDGONE = 7006;
	public static final int PETINFODELAYEDGONE = 7007;
	public static final int LOGIN_TO_POSTSELECTIONFRAGMENT = 7008;
	public static final int HOTMSG_SCROLL = 7009;
	public static final int LOGIN_TO_USERLISTACTIVITY = 7010;
	public static final int MIPCA_TO_ORDERPAY = 7011;
	public static final int MYFRAGMENT_INVITESHARE = 7012;
	public static final int USERMEMBERFRAGMENT_LOGIN = 7013;
	public static final String OfficialWebsite = "http://www.haotang365.com.cn/";
	public static final int RequestCode_UserMember = 7014;
	public static final int PRE_MAINFRAGMENT_TO_SERVICEACTIVITY_SHOPLIST = 7015;
	public static final int SERVICEFEATURE_TO_PETLIST_SHOPLIST = 7016;
	
	public static final int ORDERDETAIL_TO_BUY_CARD=7100;
	public static final int UPDATEORDERDETAIL_TO_BUY_CARD=7101;
	
	public static int ANIM_COVER_FROM_LEFT() { return 0; }
	public static int ANIM_COVER_FROM_RIGHT() { return 1; }
	public static int ANIM_NONE() { return -1; }
	public static String ANIM_DIRECTION() { return "anim_direction"; }
	/**
	 * 获取IMEI码
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context)
	{
		TelephonyManager tm = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	
	 /**
     * 拨打电话
     * @param context
     * @param phone
     */
    public static void cellPhone(Context context,String phone){
    	Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }
	
	/**
     * 获取当前版本号
     * @param context
     * @return
     */
    public static String getCurrentVersion(Context context){
        String curVersion="0";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);// getPackageName()是你当前类的包名，0代表是获取版本信息
            curVersion = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curVersion;
    }
    /**
    *
    * @param urlpath 下载链接URL
    * @param savepath 保存的路径
    * @param filename 保存的文件名
    * @return 保存的文件
    */
   public static File downloadApk(String urlpath, String savepath, String filename,
		   Handler handler){
       try {
           HttpClient client = new DefaultHttpClient();
           HttpGet httpGet = new HttpGet(urlpath);
           HttpResponse response = client.execute(httpGet);
           InputStream is = response.getEntity().getContent();
           File file = new File(savepath, filename);
           long total = response.getEntity().getContentLength();
           FileOutputStream fos = new FileOutputStream(file);
           byte[] buff = new byte[ 2 * 1024];
           int len = 0;
           int count = 0;
           long current=0;
           while ((len = is.read(buff)) != -1) {
               fos.write(buff, 0, len);
               count++;
               current+=len;
               if(count%13==0||current==total){
            	   Message msg = handler.obtainMessage();
            	   if(current==total){
            		   msg.what=APK_DOWNLOAD_OVER;
            	   }else{
            		   msg.what=APK_DOWNLOADING;
            	   }
            	   msg.obj = current*100.0/total;
            	   handler.sendMessage(msg);
               }
           }
           is.close();
           fos.close();
           return file;
       } catch (Exception e) {
           // TODO Auto-gener	ated catch block
           e.printStackTrace();
           Message msg = handler.obtainMessage();
           msg.what=APK_DOWNLOAD_FAIL;
           handler.sendMessage(msg);
           return null;
       }
   }
 
   public static String encodeWithBase64(Bitmap bitmap)
	{
		if (bitmap == null)
			return "";

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);//在保证图片尽量不失真的情况下，减少图片上传,下载所需要的流量
		byte[] byteArray = stream.toByteArray();
		return Base64.encodeToString(byteArray, Base64.NO_WRAP);
	}
	
	public static void savePushID(Context context,String cid){
		SharedPreferenceUtil.getInstance(context).saveString("cid", cid);
	}
	public static String getPushID(Context context){
		return SharedPreferenceUtil.getInstance(context).getString("cid", "");
	}
	/**
	 * 统计事件id
	 * @author Administrator
	 *
	 */
	public static final class UmengEventID {
//		public static final String click_BathSelect="click_BathSelect";//首页点击洗澡
//		public static final String click_BathOrder="click_BathOrder";//首页点击洗澡洗澡服务详情页点击预约
//		public static final String click_BathSubmit="click_BathSubmit";//首页点击洗澡洗澡服务详情页点击预约
//		public static final String click_BathToPay="click_BathToPay";//洗澡订单确认页点击去付款
//		public static final String result_BathPaySucuess="result_BathPaySucuess";//洗澡付款成功
//		public static final String click_BeautySelect="click_BeautySelect";//首页点击美容
//		public static final String click_BeautyOrder="click_BeautyOrder";//美容服务详情页点击预约
//		public static final String click_BeautySubmit="click_BeautySubmit";//美容预约页点击提交
//		public static final String click_BeautyToPay="click_BeautyToPay";//美容订单确认页点击去付款
//		public static final String result_BeautyPaySucuess="result_BeautyPaySucuess";//美容付款成功
//		public static final String click_ChargeInMainPage="click_ChargeInMainPage";//首页点击充值
//		public static final String click_ChargeInMyPage="click_ChargeInMyPage";//我的页面点击充值
//		public static final String click_ChargeNow="click_ChargeNow";//充值列表页点击立即充值
//		public static final String click_ChargeToPay="click_ChargeToPay";//充值详情页点击去付款
//		public static final String click_ChargePaySucess="click_ChargePaySucess";//充值付款成功
		
		
		
		
		public static final String click_HomePage_AreaSelect = "click_HomePage_AreaSelect";//进入区域选择页;//1
		public static final String click_HomePage_Scan = "click_HomePage_Scan";//扫一扫;//1
		public static final String click_HomePage_Complaints = "click_HomePage_Complaints" ;//首页投诉;//1
		public static final String click_HomePage_BathSelect="click_HomePage_BathSelect";//首页洗澡入口;//1
		public static final String click_HomePage_BeautySelect = "click_HomePage_BeautySelect";//首页美容入口;//1
		public static final String click_HomePage_FosterCareSelect = "click_HomePage_FosterCareSelect";//首页寄养入口;//1
		public static final String click_HomePage_CharacteristicSelect = "click_HomePage_CharacteristicSelect";//首页特色服务入口;//0
		public static final String click_HomePage_DogcardSelect = "click_HomePage_DogcardSelect";//首页犬证入口;//1
		public static final String click_HomePage_SwimmingSelect = "click_HomePage_SwimmingSelect";//首页游泳入口;//1
		public static final String click_HomePage_MedicalCareSelect = "click_HomePage_MedicalCareSelect";//首页医疗入口;//1
		public static final String click_HomePage_TrainingDogSelect = "click_HomePage_TrainingDogSelect";//首页训犬入口;//1
		public static final String click_HomePage_Headlines = "click_HomePage_Headlines";//首页宠物家头条;//1
		public static final String click_HomePage_PopularList="click_HomePage_PopularList";//热门美容师-进入列表页;//1
		public static final String click_HomePage_PopularSelect="click_HomePage_PopularSelect";//热门美容师-进入推荐美容师主页;//1
		public static final String click_HomePage_PetRingList = "click_HomePage_PetRingList";//什么值得看-进入宠圈;//1
		public static final String click_HomePage_PetRingDetails = "click_HomePage_PetRingDetails";//什么值得看-点击进入帖子详情;//1
		public static final String click_HomePage_CharacteristicDetails = "click_HomePage_CharacteristicDetails";//特色服务-进入服务预约页;//1
		public static final String click_HomePage_Hospital = "click_HomePage_Hospital";//推荐医院-进入医院详情;//1
		public static final String click_HomePage_Encyclopedia = "click_HomePage_Encyclopedia";//宠物百科-进入百科详情;//1
		public static final String click_Bath_SwitchPet = "click_Bath_SwitchPet";//切换宠物入口;//1
		public static final String click_Bath_ToRecharge = "click_Bath_ToRecharge";//充值活动入口;//1
		public static final String click_Bath_IntoEvaluate="click_Bath_IntoEvaluate";//评价入口;//1
		public static final String click_Bath_SelectDoor="click_Bath_SelectDoor";//洗澡上门服务;//1
		public static final String click_Bath_SelectShop="click_Bath_SelectShop";//洗澡到店服务;//1
		public static final String click_Beauty_ToRecharge = "click_Beauty_ToRecharge";//充值活动入口;//1
		public static final String click_Beauty_IntoEvaluate="click_Beauty_IntoEvaluate";//评价入口;//1
		public static final String click_Beauty_SelectDoor = "click_Beauty_SelectDoor";//美容上门服务;//1
		public static final String click_Beauty_SelectShop = "click_Beauty_SelectShop";//美容到店服务;//1
		public static final String click_SwitchShop="click_SwitchShop";//切换区域/门店;//1
		public static final String click_AddPet="click_AddPet";//添加宠物入口;//1
		public static final String click_SelectPetAddress="click_SelectPetAddress";//宠物地址选择入口;//1
		public static final String click_SelectTime="click_SelectTime";//预约时间入口;//1
		public static final String click_SwitchBeautician="click_SwitchBeautician";//切换美容师级别;//1
		public static final String click_Foster_CallStation="click_Foster_CallStation";//拨打寄养中转站电话;//1
		public static final String click_Foster_StationNavigation="click_Foster_StationNavigation";//寄养中转站导航;//1
		public static final String click_Foster_SwitchStation="click_Foster_SwitchStation";//切换寄养中转站;//1
		public static final String click_Foster_ToRecharge="click_Foster_ToRecharge";//寄养充值活动入口;//1
		public static final String click_Foster_SelectTime="click_Foster_SelectTime";//寄养时间选择入口;//1
		public static final String click_Foster_SelectRoom="click_Foster_SelectRoom";//寄养房型选择;//1
		public static final String click_Order_ModifyContacts="click_Order_ModifyContacts";//修改联系人;//1
		public static final String click_Order_AddRemarks="click_Order_AddRemarks";//添加备注;//1
		public static final String click_Order_SelectCoupon = "click_Order_SelectCoupon";//选择优惠券列表入口;//1
		public static final String click_Foster_LeaveBath="click_Foster_LeaveBath";//选择离店洗澡;//0
		public static final String click_Swimming_Navigation = "click_Swimming_Navigation";//游泳通州地址导航入口;//1
		public static final String click_Swimming_ToRecharge="click_Swimming_ToRecharge";//游泳充值活动入口;//1
		public static final String click_Swimming_Evaluation="click_Swimming_Evaluation";//游泳评价入口;//1
		public static final String click_Swimming_SwitchPet = "click_Swimming_SwitchPet";//游泳切换宠物入口;//1
		public static final String click_Swimming_AddPet = "click_Swimming_AddPet";//游泳添加宠物入口;//1
		public static final String click_Swimming_SelectTime = "click_Swimming_SelectTime";//选择预约时间;//1
//		public static final String click_Swimming_Call="click_Swimming_Call";//游泳拨打电话入口;//0
		public static final String click_TrainingDog_Navigation = "click_TrainingDog_Navigation";//训犬通州地址导航入口;//1
//		public static final String click_TrainingDog_ToRecharge = "click_TrainingDog_ToRecharge";//训犬游泳充值活动入口;//0
		public static final String click_TrainingDog_Evaluation = "click_TrainingDog_Evaluation";//训犬评价入口;//1
		public static final String click_TrainingDog_SwitchPet = "click_TrainingDog_SwitchPet";//训犬切换宠物入口;//1
		public static final String click_TrainingDog_SelectTime = "click_TrainingDog_SelectTime";//训犬预约时间入口;//1
		public static final String click_TrainingDog_StationNavigation = "click_TrainingDog_StationNavigation";//训犬选择中转站入口;//1
		public static final String click_TrainingDog_Package="click_TrainingDog_Package";//训犬套餐选择入口;//1
		public static final String click_TrainingDog_VideoList="click_TrainingDog_VideoList";//训犬视频介绍列表入口;//1
		public static final String click_My_MyBalance = "click_My_MyBalance";//我的余额;//1
		public static final String click_My_MemberCenter="click_My_MemberCenter";//会员中心;//1
		public static final String click_My_DogCard="click_My_DogCard";//犬证办理;//1
		public static final String click_My_FosterLive="click_My_FosterLive";//寄养直播;//1
		public static final String click_My_MyCoupon = "click_My_MyCoupon";//我的优惠券;//1
		public static final String click_My_InvitationCourtesy = "click_My_InvitationCourtesy";//邀请有礼;//1
		public static final String click_My_CommonAddress = "click_My_CommonAddress";//常用地址;//1
		public static final String click_My_MyEvaluation="click_My_MyEvaluation";//我的评价;//1
		public static final String click_My_More = "click_My_More";//更多;//0
		public static final String click_My_AddMyPet="click_My_AddMyPet";//添加宠物入口;//1
		public static final String click_My_PetHome ="click_My_PetHome";//宠物主页;//1
		public static final String click_PetHome_DogCard = "click_PetHome_DogCard";//宠物主页犬证办理;//1
		public static final String click_PetHome_EditPet="click_PetHome_EditPet";//编辑资料;//1
		public static final String click_PetHome_BathSelect = "click_PetHome_BathSelect";//宠物主页洗澡入口;//1
		public static final String click_PetHome_BeautySelect="click_PetHome_BeautySelect";//宠物主页美容入口;//1
		public static final String click_PetHome_FosterSelect="click_PetHome_FosterSelect";//宠物主页寄养入口;//1
		public static final String click_Select_Whole="click_Select_Whole";//宠圈精选全部标签;//1
		public static final String click_Select_Selected="click_Select_Selected";//宠圈精选关注标签;//1
		public static final String click_Select_Video="click_Select_Video";//宠圈精选视频标签;//1
		public static final String click_Select_Homepage = "click_Select_Homepage";//用户个人主页入口;//1
		public static final String click_Select_Details="click_Select_Details";//宠圈精选帖子详情;//1
		public static final String click_Select_Comment="click_Select_Comment";//宠圈精选评论;//1
		public static final String click_Select_Share="click_Select_Share";//宠圈精选分享;//1
		public static final String click_PetCircle="click_PetCircle";//宠圈圈子入口;//1
		public static final String click_PetCircle_Details="click_PetCircle_Details";//宠圈圈子帖子详情;//1
		public static final String click_PetCircle_Post="click_PetCircle_Post";//宠圈圈子发帖;//1
		public static final String click_PetCircle_Comment="click_PetCircle_Comment";//评论-圈子内;//1
		public static final String click_PetCircle_Share = "click_PetCircle_Share";//分享-圈子内;//1
		public static final String click_CancelOrder="click_CancelOrder";//取消订单;//1
		public static final String click_ModifyOrder="click_ModifyOrder";//修改订单;//1
	}

	public static final class SwimDayTime{
		public static final String AM="10:00:00";
		public static final String PM="14:00:00";
	}
}
