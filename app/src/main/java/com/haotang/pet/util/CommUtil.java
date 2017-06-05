package com.haotang.pet.util;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.haotang.pet.net.AsyncHttpClient;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.net.RequestParams;

public class CommUtil {

	/**
	 * 获取带端口的IP地址
	 * 
	 * @return
	 */
	public static String getServiceBaseUrl() {
		// return "http://192.168.0.15/";
		// return "http://192.168.0.29/";
		// return "http://cw6.cwjia.cn/";//新的域名
		// return "http://cw7.cwjia.cn/";//新的域名
		// return "http://192.168.0.88/";//新测试环境域名
		// return "http://cwjia.wicp.net/";//88测试环境域名
		// return "http://haotang.51vip.biz/";//251测试环境域名
//		return "https://test.chongwuhome.cn/";// 252测试环境域名
		 return "http://192.168.0.252/";
		// return "http://192.168.0.142:8080/";
//		return "http://demo.cwjia.cn/";
		//return "https://api.cwjia.cn/";// 新的域名
	}

	/**
	 * 获取不带反斜杠的IP地址
	 * 
	 * @return
	 */
	public static String getServiceNobacklash() {
		// return "http://192.168.0.15";
		// return "http://192.168.0.29";
		// return "http://cw6.cwjia.cn";//新的域名
		// return "http://cw7.cwjia.cn";//新的域名
		// return "http://192.168.0.88";//新测试环境域名
		// return "http://cwjia.wicp.net";//88测试环境域名
		// return "http://haotang.51vip.biz";//251测试环境域名
//		return "https://test.chongwuhome.cn";// 252测试环境域名
		 return "http://192.168.0.252";
		// return "http://192.168.0.142:8080";
//		return "http://demo.cwjia.cn";
		//return "https://api.cwjia.cn";// 新的域名
	}

	public static String getServicePayUrl() {
		// return "http://192.168.0.15/";
		// return "http://192.168.0.29/";
		// return "http://cw6.cwjia.cn/";//新的域名
		// return "http://cw7.cwjia.cn/";//新的域名
		// return "http://192.168.0.88/";//新测试环境域名
		// return "http://cwjia.wicp.net/";//88测试环境域名
		/// / return "http://haotang.51vip.biz/";//251测试环境域名
//		return "https://test.chongwuhome.cn/";// 252测试环境域名
		 return "http://192.168.0.252/";
		// return "http://192.168.0.142:8080/";
//		return "http://demo.cwjia.cn/";
		//return "https://api.cwjia.cn/";// 新的域名
	}

	public static String getWebBaseUrl() {
		//return "http://m.cwjia.cn/";// 新的域名
		return "http://192.168.0.247/";// 新的域名
	}

	public static String getSource() {
		return "android";
	}

	/**
	 * 设置超时时间
	 * 
	 * @return
	 */
	public static int getTimeOut() {
		return 5 * 1000;
	}

	/**
	 * 设置voi超时时间
	 * 
	 * @return
	 */
	public static int getTimeOutVoi() {
		return Integer.MAX_VALUE;
	}

	public static RequestParams LocalParm(Context context) {
		RequestParams params = new RequestParams();
		String cellPhone = SharedPreferenceUtil.getInstance(context).getString(
				"cellphone", "");
		String imei = Global.getIMEI(context);
		String system = getSource() + "_" + Global.getCurrentVersion(context);
		if (cellPhone != null && !TextUtils.isEmpty(cellPhone)) {
			params.put("cellPhone", cellPhone);
		}
		if (imei != null && !TextUtils.isEmpty(imei)) {
			params.put("imei", imei);
		}
		if (system != null && !TextUtils.isEmpty(system)) {
			//params.put("system", system);
		}
		params.put("phoneModel", android.os.Build.BRAND + " "
				+ android.os.Build.MODEL);
		params.put("phoneSystemVersion", "Android "
				+ android.os.Build.VERSION.RELEASE);
		return params;
	}

	/**
	 * 自动登录
	 * 
	 * @param phone
	 * @param imei
	 * 
	 * @param handler
	 */
	public static void loginAuto(Context context, String phone, String imei,
			String version, int userid, double lat, double lng,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/autoLogin?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", userid);
			if (lat != 0 || lng != 0) {
				params.put("lat", lat);
				params.put("lng", lng);
			}
			params.put("channelId", Market.getMarketId(context));
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("自动登录参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取最新的app，是否强制升级
	 * 
	 * @param handler
	 */
	public static void getLatestVersion(Context context, String service,
			long time, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/content/update_android_"
				+ service + ".html?time=" + time + "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(3000);
			Utils.mLogError("升级："
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取h5的url
	 * 
	 * @param handler
	 */
	public static void getH5Url(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/getInviteConfig?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			Utils.mLogError("获取邀请有礼h5：" + url);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取公司qq和微信
	 * 
	 * @param time
	 *            时间戳
	 * @param handler
	 */
	public static void getCompanyContact(Context context, long time,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/getFeedbackPhoneInfo?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
			Utils.mLogError("获取公司qq和微信："
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取banner的图片地址
	 * 
	 * @param handler
	 */
	public static void getBanner(Context context, long time,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/content/banner.html?time="
				+ time+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 寄养首页
	 * 
	 * @param handler
	 */
	public static void getReadyReserve(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/hotel/ReadyReserve?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("寄养：" + url);
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取开通城市列表
	 * 
	 * @param handler
	 */
	public static void getCitys(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/querycitylist?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取是否显示红包及红包兑换码
	 * 
	 * @param handler
	 */
	public static void getGiftConfig(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/coupon/couponConfig?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取首页数据
	 * 
	 * @param handler
	 */
	public static void getHomeIndex(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/content/index/index.html?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("首页：" + url);
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void getHomeData(Context context, int areaid, long time,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/homePage?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			//params.put("name", "index");
			params.put("time", time);
			params.put("page", 1);
			params.put("pageSize", 4);
			if (areaid > 0 && areaid != 100)
				params.put("areaId", areaid);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("首页："
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取可用日期
	 * 
	 * @param handler
	 */
	public static void reserveCalendar(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/hotel/ReserveCalendar?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->获取寄养下单可用日期 -->  "
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 寄养支付成功获取注意事项
	 * 
	 * @param handler
	 */
	public static void getMatters(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/hotel/FosterageNotice?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取寄养接送提示
	 * 
	 * @param handler
	 */
	public static void getHotelPickUpRemindTxt(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl()
				+ "pet/order/orderHotelPickUpRemindTxt?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取洗澡美容接送提示
	 * 
	 * @param handler
	 */
	public static void getCarePickUpRemindTxt(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/orderCarePickUpRemindTxt?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, LocalParm(context), handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取可用的优惠券
	 * 
	 * @param phone
	 * @param imei
	 * @param version
	 * @param time
	 *            订单预约时间
	 * @param serviceid
	 *            服务id 1:洗澡，2：美容
	 * @param type
	 *            服务类型 1:洗澡美容，2：寄养 4 训练
	 * @param petid
	 *            宠物id
	 * @param serviceloc
	 *            服务类型id 1：到店，2:上门
	 * @param workerid
	 *            美容师id
	 * @param pickup
	 *            是否接送 0：不接送 1：接送
	 * @param customerName
	 *            客户姓名
	 * @param customerMobile
	 *            客户电话
	 * @param addressid
	 *            订单地址id
	 * @param address
	 *            订单地址
	 * @param lat
	 *            订单地址坐标
	 * @param lng
	 * @param strp
	 *            多宠物的petid_serviceid_我的宠物ID_单项ID 如 6_1_3_1,2,5
	 * @param handler
	 * 
	 */
	public static void getAvailableCoupon(Context context, String phone,
			String imei, String version, String time, int type, int serviceloc,
			int workerid, int pickup, String customerName,
			String customerMobile, int addressid, String address, double lat,
			double lng, String strp, int areaId, double payPrice, int shopId,
			String endTime, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/coupon/mycoupons?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("appointment", time);
			if (addressid > 0)
				params.put("addressId", addressid);
			if (workerid > 0)
				params.put("workerId", workerid);
			if (type > 0)
				params.put("type", type);
			params.put("customerName", customerName);
			params.put("customerMobile", customerMobile);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			params.put("address", address);
			params.put("serviceLoc", serviceloc);
			if (pickup > 0)
				params.put("pickUp", pickup);
			params.put("strp", strp);
			params.put("areaId", areaId);
			params.put("payPrice", payPrice);
			params.put("shopId", shopId);
			if (!TextUtils.isEmpty(endTime)) {
				params.put("endTime", endTime);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->可用优惠券："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
			Utils.mLogError("==-->可用优惠券挂了");
		}
	}

	/**
	 * 地址是否在上门范围
	 * 
	 * @param areaid
	 * @param lat
	 * @param lng
	 * @param handler
	 */
	public static void getServiceInArea(Context context, int areaid,
			double lat, double lng, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/queryServiceArea?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			params.put("areaId", areaid);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("地址是否在上门范围："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 根据经纬度查询商圈
	 * 
	 * @param lat
	 * @param lng
	 * @param handler
	 */
	public static void queryTradeAreaByLoc(Context context, double lat,
			double lng, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/shop/queryTradeAreaByLocNew?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("坐标查商圈："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取账户余额
	 * 
	 * @param phone
	 * @param imei
	 * @param version
	 * @param handler
	 */
	public static void getAccountBalance(Context context, String phone,
			String imei, String version, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/getUserAccountBalance?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("账户余额参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 寄养下单界面获取数据
	 * 
	 * @param phone
	 * @param imei
	 * @param version
	 * @param petid
	 * @param shopid
	 * @param starttime
	 * @param endtime
	 * @param lat
	 * @param lng
	 * @param handler
	 */
	public static void prepareConfirmOrder(Context context, int roomType,
			String phone, String imei, String version, int petid, int shopid,
			String starttime, String endtime, double lat, double lng,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/hotel/prepareConfirmOrder?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("roomType", roomType);
			params.put("petId", petid);
			params.put("shopId", shopid);
			params.put("startTime", starttime);
			params.put("endTime", endtime);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("寄养下单界面参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取店铺房间
	 * 
	 * @param shopid
	 * @param petid
	 * @param starttime
	 * @param endtime
	 * @param handler
	 */
	public static void getShopRoom(Context context, int areaId, int petid,
			String starttime, String endtime, double lat, double lng,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/hotel/queryShopRoom?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("petId", petid);
			params.put("startTime", starttime);
			params.put("endTime", endtime);
			params.put("lat", lat);
			params.put("lng", lng);
			params.put("areaId", areaId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("获取宠物房间参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取指定宠物特定服务详情
	 * 
	 * @param serviceid
	 * @param petid
	 * @param handler
	 */
	public static void getPetServicedetail(Context context, int serviceid,
			int petid, int servLoc, double lat, double lng, int workerId,
			int areaId, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/owner/petservicedetail?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("serviceId", serviceid);
			params.put("petId", petid);
			if (servLoc > 0) {
				params.put("servLoc", servLoc);
			}
			if (lat > 0) {
				params.put("lat", lat);
			}
			if (lng > 0) {
				params.put("lng", lng);
			}
			if (workerId > 0) {
				params.put("workerId", workerId);
			}
			if (areaId > 0 && areaId != 100) {
				params.put("areaId", areaId);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->服务下单："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取指定宠物特定服务详情
	 * 
	 * @param serviceid
	 * @param servicetype
	 * @param petid
	 * @param handler
	 */
	public static void getPetServicedetailV2(Context context, int servicetype,
			int serviceid, int petid, int shopid, int servLoc, String time,
			double lat, double lng, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/owner/queryAppointDetail?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (servicetype > 0)
				params.put("serviceType", servicetype);
			if (serviceid > 0)
				params.put("serviceId", serviceid);
			params.put("petId", petid);
			params.put("appTime", time);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			if (servLoc > 0) {
				params.put("servLoc", servLoc);
			}
			if (shopid > 0) {
				params.put("shopId", shopid);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->服务下单V2："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 多只宠物查询
	 * 
	 * @param shopid
	 * @param areaid
	 * @param time
	 * @param lat
	 * @param lng
	 * @param strp
	 *            分为6部分 petid_serviceid_mypetid_servicetype_petkind_单项ID
	 *            如10_1_12_1_1_2,3,5如果没有单项或者mypetid 10_1_0_1_1_0
	 * @param handler
	 */
	public static void getPetServicedetailV3(Context context, int shopid,
			int areaid, int servLoc, String time, double lat, double lng,
			String strp, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/owner/queryAppointDetails?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("appTime", time);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			params.put("strp", strp);
			if (servLoc > 0) {
				params.put("servLoc", servLoc);

				if (shopid > 0 && servLoc == 1) {
					params.put("shopId", shopid);
				}
				if (areaid > 0 && servLoc == 2) {
					params.put("areaId", areaid);
				}
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->服务下单V3："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 添加宠物获取服务
	 * 
	 * @param petid
	 * @param shopid
	 * @param servLoc
	 * @param time
	 * @param lat
	 * @param lng
	 * @param handler
	 */
	public static void getPetServiceByAddPet(Context context, int petid,
			int shopid, int servLoc, String time, double lat, double lng,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/owner/addPet?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("petId", petid);
			if (time != null && !"".equals(time))
				params.put("appTime", time);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			if (servLoc > 0) {
				params.put("servLoc", servLoc);
			}
			if (shopid > 0) {
				params.put("shopId", shopid);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("添加宠物获取服务："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取指定宠物的服务种类
	 * 
	 * @param petid
	 * @param handler
	 */
	public static void getPetService(Context context, int petid,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/owner/petservice?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("petId", petid);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取可以预约的时间
	 * 
	 * @param phone
	 * @param imei
	 * @param version
	 * @param areaid
	 * @param serviceloc
	 *            服务方式 1：店面 2：上门
	 * @param shopid
	 * @param strp
	 *            有三部分组成 petid_serviceid_单项id 如：10_2_1,5,7,没有单项时传0 如10_2_0
	 * @param tid
	 *            改单 时候需要传递 1 中级 2 高级 3 首席
	 * @param handler
	 */
	public static void getBeauticianFreeTime(Context context, double lat,
			double lng, String phone, String imei, String version, int areaid,
			int serviceloc, int shopid, String strp, int tid, String app_time,
			int orderid, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/getTaskCalender1?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = LocalParm(context);
			params.put("lat", lat);
			params.put("lng", lng);
			if (app_time != null && !TextUtils.isEmpty(app_time)) {
				params.put("app_time", app_time);
			}
			params.put("strp", strp);
			if (serviceloc > 0) {
				params.put("servLoc", serviceloc);
			}
			if (shopid > 0) {
				params.put("shopId", shopid);
			}
			if (areaid > 0) {
				params.put("areaId", areaid);
			}
			if (tid > 0) {
				params.put("tid", tid);
			}
			if (orderid > 0) {
				params.put("orderId", orderid);
			}
			client.setTimeout(getTimeOut());
			Utils.mLogError("获取时间："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取单个美容师的时间
	 * 
	 * @param phone
	 * @param imei
	 * @param version
	 * @param areaid
	 * @param serviceloc
	 * @param shopid
	 * @param beauticianid
	 * @param strp
	 * @param handler
	 */
	public static void getBeauticianTimeById(Context context, double lat,
			double lng, String phone, String imei, String version, int areaid,
			int serviceloc, int shopid, int beauticianid, String strp,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/getTaskCalenderOfWorker?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = LocalParm(context);
			params.put("lat", lat);
			params.put("lng", lng);
			params.put("strp", strp);
			if (serviceloc > 0) {
				params.put("servLoc", serviceloc);
			}
			if (shopid > 0) {
				params.put("shopId", shopid);
			}
			if (beauticianid > 0) {
				params.put("workerId", beauticianid);
			}
			if (areaid > 0) {
				params.put("areaId", areaid);
			}
			client.setTimeout(getTimeOut());
			Utils.mLogError("获取单个美容师时间："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取可以预约的美容师列表
	 * 
	 * @param phone
	 * @param imei
	 * @param version
	 * @param areaid
	 * @param time
	 * @param serviceloc
	 * @param shopid
	 * @param lat
	 * @param lng
	 * @param strp
	 *            有三部分组成 petid_serviceid_单项id 如10_2_1,3
	 * @param handler
	 */
	public static void getBeauticianList(Context context, String phone,
			String imei, String version, int areaid, String time,
			int serviceloc, int shopid, double lat, double lng, String strp,
			int workid, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/getAvailableWorkers1?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = LocalParm(context);
			if (workid > 0)
				params.put("workerId", workid);
			params.put("app_time", time);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			params.put("serviceLoc", serviceloc);
			params.put("strp", strp);
			if (shopid > 0) {
				params.put("shopId", shopid);
			}
			if (areaid > 0) {
				params.put("areaId", areaid);
			}
			if (strp != null && !"".equals(strp)) {
				params.put("strp", strp);
			}
			client.setTimeout(getTimeOut());
			Utils.mLogError("getAvailableWorkers1获取美容师列表："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 按区域查询美容师
	 * 
	 * @param phone
	 * @param imei
	 * @param version
	 * @param areaid
	 * @param serviceloc
	 * @param shopid
	 * @param lat
	 * @param lng
	 * @param strp
	 * @param handler
	 */
	public static void getBeauticianListNoTimeLimit(Context context,
			String phone, String imei, String version, int areaid,
			int serviceloc, int shopid, double lat, double lng, String strp,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl()
				+ "pet/worker/worker/getWorkersNoTimeLimit?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = LocalParm(context);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			params.put("serviceLoc", serviceloc);
			params.put("strp", strp);
			if (shopid > 0) {
				params.put("shopId", shopid);
			}
			if (areaid > 0) {
				params.put("areaId", areaid);
			}
			if (strp != null && !"".equals(strp)) {
				params.put("strp", strp);
			}
			client.setTimeout(getTimeOut());
			Utils.mLogError("getWorkersNoTime获取美容师列表："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 生成新订单
	 * 
	 * @param phone用户注册电话
	 * @param imei
	 *            用户手机IMEI码
	 * @param version
	 *            版本号
	 * @param shopid
	 *            店铺id
	 * @param areaid
	 *            服务范围id
	 * @param couponid
	 *            优惠券id
	 * @param workerid
	 *            美容师id
	 * @param addressid
	 *            地址id
	 * @param customerName
	 *            联系人姓名
	 * @param customerMobile
	 *            联系人电话
	 * @param address
	 *            服务地址（有id的空为空）
	 * @param lat
	 *            坐标
	 * @param lng
	 *            坐标
	 * @param time
	 *            服务时间
	 * @param totalPrice
	 *            订单金额
	 * @param payPrice
	 *            需付款金额
	 * @param payway
	 *            支付方式3：优惠券1：微信2：支付宝 4:余额
	 * @param remark
	 *            备注
	 * @param serviceloc
	 *            服务方式 1：到店2：到家
	 * @param pickup
	 *            是否接送 0：不接送 1：接送
	 * @param isDefaultWorker
	 *            是否为系统推荐美容师 0：否 1：是
	 * @param strp
	 *            多宠物的petid_serviceid_单项ID 如 6_1_1,2,5
	 * @param debitAmount
	 *            混合支付时余额应支付的金额，非混合支付时为-1
	 * @param uuid
	 * @param handler
	 */
	public static void newOrder(Context context, String phone, String imei,
			String version, int shopid, int areaid, int couponid, int workerid,
			int addressid, String customerName, String customerMobile,
			String address, double lat, double lng, String time,
			double totalPrice, double payPrice, int payway, String remark,
			int serviceloc, int pickup, int isDefaultWorker, String strp,
			double debitAmount, String uuid, String cardIds,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/newOrderm?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (shopid > 0)
				params.put("shopId", shopid);
			if (areaid > 0)
				params.put("areaId", areaid);
			params.put("couponId", couponid);
			params.put("payWay", payway);
			params.put("addressId", addressid);
			params.put("workerId", workerid);
			params.put("customerName", customerName);
			params.put("customerMobile", customerMobile);
			params.put("token", uuid);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			params.put("address", address);
			params.put("appointment", time);
			params.put("remark", remark);
			params.put("type", 1);
			params.put("totalPrice", totalPrice);
			params.put("payPrice", payPrice);
			params.put("debitAmount", debitAmount);
			if (strp != null && !"".equals(strp)) {
				params.put("strp", strp);
			}
			params.put("serviceLoc", serviceloc);
			params.put("isDefaultWorker", isDefaultWorker);
			params.put("pickUp", pickup);
			if (!TextUtils.isEmpty(cardIds)) {
				params.put("cardIds", cardIds);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("获取新订单参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 寄养下单
	 * 
	 * @param phone
	 * @param imei
	 * @param version
	 * @param serviceid房间类型
	 * @param petid
	 * @param shopid
	 * @param customerpetid
	 * @param couponid
	 * @param addressid
	 * @param customerName
	 * @param customerMobile
	 * @param address
	 * @param lat
	 * @param lng
	 * @param startdate
	 *            开始时间
	 * @param enddate
	 *            结束时间
	 * @param totalPrice
	 * @param listPrice
	 * @param payPrice
	 * @param payway
	 * @param remark
	 * @param pickup
	 * @param bathRequired
	 *            //是否洗澡
	 * @param foodfee
	 *            宠粮费用
	 * @param debitAmount
	 *            混合支付时余额应支付的金额，非混合支付时为-1
	 * @param uuid
	 * @param handler
	 */
	public static void newOrderFostercare(Context context, String promoterCode,
			String phone, String imei, String version, int serviceid,
			int petid, int shopid, int customerpetid, int couponid,
			int addressid, String customerName, String customerMobile,
			String address, double lat, double lng, String startdate,
			String enddate, double totalPrice, double listPrice,
			double payPrice, int payway, String remark, int pickup,
			int bathRequired, double foodfee, double debitAmount, String uuid,
			int transferId, String bathPetIds, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/newOrderV2?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (promoterCode != null) {
				params.put("promoterCode", promoterCode);
			}
			params.put("petService", serviceid);
			params.put("petId", petid);
			params.put("couponId", couponid);
			params.put("shopId", shopid);
			params.put("payWay", payway);
			params.put("addressId", addressid);
			params.put("customerName", customerName);
			params.put("customerMobile", customerMobile);
			params.put("token", uuid);
			if (bathRequired > -1)
				params.put("bathRequired", bathRequired);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			params.put("address", address);
			params.put("appointment", startdate);
			params.put("stopTime", enddate);
			params.put("remark", remark);
			params.put("totalPrice", totalPrice);
			params.put("listPrice", listPrice);
			params.put("payPrice", payPrice);
			params.put("serviceLoc", 1);
			params.put("type", 2);
			if (foodfee > -1)
				params.put("extraItemPrice", foodfee);
			if (customerpetid > 0)
				params.put("mypetId", customerpetid);
			params.put("pickUp", pickup);
			params.put("debitAmount", debitAmount);
			params.put("transferId", transferId);
			params.put("bathPetIds", bathPetIds);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("获取新订单参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取美容师的详情
	 * 
	 * @param phone
	 * @param imei
	 * @param version
	 * @param workerid
	 * @param handler
	 */
	public static void getBeauticianDetail(Context context, int workerid,int areaId,String app_time,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/queryWorkerById?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("workerId", workerid);
			if (areaId > 0 && areaId != 100) {
				params.put("areaId", areaId);
			}
			if (app_time != null && !TextUtils.isEmpty(app_time)) {
				params.put("app_time", app_time);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->查询美容师详情："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取美容师作品列表
	 * 
	 * @param workerid
	 *            美容师id
	 * @param beforeid
	 *            此id之前的美容师
	 * @param afterid
	 *            此id之后的美容师
	 * @param pagesize
	 *            每页个数
	 * @param handler
	 */
	public static void getProductionByBeauticianId(Context context,
			int workerid, int beforeid, int afterid, int pagesize,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/queryWorksByWorkerId?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("workerId", workerid);
			params.put("before", beforeid);
			params.put("after", afterid);
			params.put("pageSize", pagesize);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取美容师评论列表
	 * 
	 * @param workerid
	 * @param beforeid
	 * @param afterid
	 * @param pagesize
	 * @param handler
	 */
	public static void getCommentByBeauticianId(Context context, int workerid,
			int page, int pagesize, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/queryCommentsByWorkerId?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);

			params.put("workerId", workerid);
			params.put("page", page);
			params.put("pageSize", pagesize);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->获取评论列表参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 根据Id查询店铺信息
	 * 
	 * @param shopid
	 * @param handler
	 */
	public static void getShopById(Context context, int shopid,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/queryShopById?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", shopid);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("查询店铺参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 意见反馈
	 * 
	 * @param phone
	 *            注册手机号
	 * @param imei
	 *            手机IMEI码
	 * @param version
	 *            应用版本号
	 * @param contact
	 *            联系方式
	 * @param content
	 *            反馈内容
	 * @param handler
	 */
	public static void feedBack(Context context, int userId, String phone,
			String imei, String version, String contact, String content,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/feedback?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("content", content);
			params.put("contact", contact);
			params.put("userId", userId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("意见反馈参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取护理数据
	 * 
	 * @param handler
	 */
	public static void getHL(Context context, long time,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/kl/hl/catalog.html?time="
				+ time+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取健康数据
	 * 
	 * @param handler
	 */
	public static void getJK(Context context, long time,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/kl/jk/catalog.html?time="
				+ time+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取喂养数据
	 * 
	 * @param handler
	 */
	public static void getWY(Context context, long time,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/kl/wy/catalog.html?time="
				+ time+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取训练数据
	 * 
	 * @param handler
	 */
	public static void getXL(Context context, long time,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/kl/xl/catalog.html?time="
				+ time+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取用品数据
	 * 
	 * @param handler
	 */
	public static void getYP(Context context, long time,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/kl/yp/catalog.html?time="
				+ time+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 去后台注册个推
	 * 
	 * @param phone
	 *            可选
	 * @param version
	 * @param userId
	 *            可选
	 * @param devToken
	 *            个推返回的cid
	 * @param handler
	 */
	public static void registGeTuitoService(Context context, String phone,
			String version, String imei, int userId, String devToken,
			double lat, double lng, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/registUserDevice?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("devToken", devToken);
			params.put("userId", userId);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			params.put("channelId", Market.getMarketId(context));
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("注册个推参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取首页数据，当前只有订单是否有未评价
	 * 
	 * @param phone
	 * @param version
	 * @param imei
	 * @param handler
	 */
	public static void getIndex(Context context, String phone, String version,
			String imei, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/index?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("首页参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param phone
	 * @param version
	 * @param imei
	 * @param id
	 *            该用户宠物ID
	 * @param petid
	 *            宠物品种ID
	 * @param petname
	 *            宠物品种名称
	 * @param nickname
	 *            宠物昵称
	 * @param sex
	 *            性别
	 * @param birthday
	 *            生日
	 * @param height
	 *            肩高
	 * @param remark
	 *            备注
	 * @param pic
	 *            照片
	 * @param handler
	 */
	public static void newCustomerPet(Context context, String phone,
			String version, String imei, int id, int petid, String petname,
			String nickname, int sex, String birthday, int orderid, int height,
			String remark, String pic, String color,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/newCustomerPet?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("petId", petid);
			params.put("petName", petname);
			if (id > 0)
				params.put("id", id);
			if (orderid > 0)
				params.put("orderId", orderid);
			params.put("nickName", nickname);
			params.put("sex", sex);
			params.put("birthday", birthday);
			if (height >= 0)
				params.put("height", height);
			params.put("remark", remark);
			if (pic != null && !"".equals(pic))
				params.put("pic", pic);
			params.put("color", color);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->添加宠物参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 删除宠物接口
	 * 
	 * @param phone
	 * @param version
	 * @param imei
	 * @param id
	 *            该用户下的宠物id
	 * @param handler
	 */
	public static void deleteCustomerPet(Context context, String phone,
			String version, String imei, int id,
			AsyncHttpResponseHandler handler) {
		String url = getWebBaseUrl() + "web/petcerti/api/deluserpet?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (id > 0)
				params.put("id", id);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("删除宠物参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 查询该用户下某个宠物的信息
	 * 
	 * @param phone
	 * @param version
	 * @param imei
	 * @param id
	 *            该用户下宠物id
	 * @param handler
	 */
	public static void queryCustomerPetById(Context context, String phone,
			String version, String imei, int id,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/queryCustomerPetById?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (id > 0)
				params.put("id", id);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("查询单个宠物参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 查询用户的全部宠物
	 * 
	 * @param phone
	 * @param version
	 * @param imei
	 * @param handler
	 */
	public static void queryCustomerPets(Context context, String phone,
			String version, String imei, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/queryCustomerPets?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("查询我的宠物参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param phone
	 * @param version
	 * @param imei
	 * @param parms
	 *            需要加密的字符
	 * @param type
	 *            1:为包加密2:为app加密
	 * @param handler
	 */
	public static void getWxMD5(Context context, String phone, String version,
			String imei, String parms, String type,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/tenpay/md5?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("str", parms);
			params.put("type", type);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLog("获取微信签名："
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 记录点击事件
	 * 
	 * @param phone
	 * @param imei
	 * @param version
	 * @param event
	 * @param role
	 * @param userid
	 * @param lat
	 * @param lng
	 * @param handler
	 */
	public static void recorder(Context context, String phone, String imei,
			String version, String event, int role, int userid, double lat,
			double lng, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/recorder?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("event", event);
			params.put("role", role);
			params.put("user_id", userid);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLog("记录点击事件："
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取宠物列表更新
	 * 
	 * @param handler
	 */
	public static void getListVersion(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/owner/queryPetListVersion?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("获取宠物列表是否有更新：" + url);
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 支付成功后通知后台改变订单状态
	 * 
	 * @param phone
	 * @param version
	 * @param imei
	 * @param orderid
	 * @param handler
	 */
	public static void confirmOrderStatus(Context context, String phone,
			String version, String imei, String orderid,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/pay/confirmOrderStatus?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("outOrderNo", orderid);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLog("支付完成后改变订单状态："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 登录
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param code
	 * @param handler
	 */
	public static void loginAu(Context context, String phone, String imei,
			String code, double lat, double lng,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/login?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("cellPhone", phone);
			params.put("code", code);
			params.put("channelId", Market.getMarketId(context));
			if (lat != 0 || lng != 0) {
				params.put("lat", lat);
				params.put("lng", lng);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==--> 登录："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取验证码
	 * 
	 * @param phone
	 * @param imei
	 * @param city
	 * @param handler
	 */
	public static void genVerifyCode(Context context, String phone,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/genVerifyCode?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("phone", phone);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("获取验证码："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取宠物列表
	 * 
	 * @param kind
	 *            宠物类型
	 * @param serviceId
	 *            服务类型
	 * @param handler
	 *            serviceType //再增加一个serviceType 训练传400 serviceId 等同于serviceType
	 *            保持原样不动 serviceType 字段不加了
	 */
	public static void getPetList(Context context, int kind, int serviceId,
			int workerid, int ServiceType, int templateId,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/owner/petlist?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (kind > 0)
				params.put("kind", kind);
			if (workerid > 0)
				params.put("workerId", workerid);
			if (ServiceType > 0) {
				params.put("serviceType", ServiceType);
			} else {
				if (serviceId > 0 && serviceId != 100) {
					params.put("serviceId", serviceId);
				}
			}
			if (templateId > 0) {
				params.put("templateId", templateId);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->获取宠物列表："
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 添加服务地址
	 * 
	 * @param phone
	 * @param userId
	 * @param imei
	 * @param context
	 * @param address
	 * @param detail
	 * @param lat
	 * @param lng
	 * @param handler
	 */
	public static void addServiceAddress(String phone, int userId, String imei,
			Context context, String address, double lat, double lng,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/addServiceAddress?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("userId", userId);
			params.put("address", address);
			params.put("lat", lat);
			params.put("lng", lng);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("添加服务地址："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 查询服务地址/user/queryServiceAddress
	 * 
	 * @param phone
	 * @param userId
	 * @param imei
	 * @param context
	 * @param handler
	 */
	public static void queryServiceAddress(String phone, int userId,
			String imei, Context context, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/queryServiceAddress?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("userId", userId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->查询服务地址："
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 删除地址/user/queryServiceAddress
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param addressId
	 * @param handler
	 */
	public static void deleteServiceAddress(String phone, String imei,
			Context context, int addressId, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/deleteServiceAddress?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("addressId", addressId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 查询用户所有订单列表/order/myOrders
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param handler
	 */
	public static void queryMyOrders(String phone, String imei,
			Context context, int page, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/allOrders?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("page", page);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->查询用户所有订单列表："
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 查询用户已取消订单列表/order/canceledOrders
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param handler
	 */
	public static void queryCanceledOrders(String phone, String imei,
			Context context, int page, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/canceledOrders?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("page", page);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->查询用户已取消列表："
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 查询用户进行中订单列表/order/myOrders （当前状态status值不确定问产品）
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param statuses
	 * @param handler
	 */
	public static void queryProcessMyOrders(String phone, String imei,
			Context context, int page, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/ingOrders?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("page", page);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->status进行中："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 查询用户待评价订单列表/order/myOrders 待评价状态为4
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param handler
	 */
	public static void queryEvaluateMyOrders(String phone, String imei,
			Context context, int page, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/toCommentOrders?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("page", page);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->statuses =4 ："
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 更新用户信息/user/updateUser
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param userId
	 * @param userName
	 * @param content
	 * @param handler
	 */
	public static void updateUser(String phone, String imei, Context context,
			long userId, String userName, String content,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/updateUser?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("userId", userId);
			if (!userName.equals("")) {
				params.put("userName", userName);
			} else {
			}
			if (content != null && !content.equals("")) {
				params.put("content", content);
			} else {
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->更新test" + "  "
					+ client.getUrlWithQueryString(true, url, params));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 查询用户所有优惠券/user/coupon/mycoupons
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param handler
	 */
	public static void queryUserCoupons(String phone, String imei,
			Context context, int page, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/coupon/myacoupons?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("page", page);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->优惠券："
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 评价 /user/coupon/mycoupons
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param id
	 * @param grade
	 * @param assessment
	 * @param ImgStringlist
	 * @param qqShareStatus
	 * @param weiboShareStatus
	 * @param commentTag
	 *            评价tag用逗号隔开 1,2,3,4
	 * @param isAnonymous
	 *            是否匿名
	 * @param handler
	 */
	public static void commentOrder(String phone, String imei, Context context,
			int id, List<Integer> grade, String assessment,
			List<String> ImgStringlist, int qqShareStatus,
			int weiboShareStatus, String commentTag, boolean isAnonymous,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/commentOrder?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			params.put("grade1", grade.get(0));
			params.put("grade2", grade.get(1));
			params.put("grade3", grade.get(2));
			if (assessment.equals(null) || assessment.equals("")) {
			} else {
				params.put("assessment", assessment);
			}
			if (ImgStringlist.size() > 0 && ImgStringlist != null) {
				if (ImgStringlist.size() == 1) {
					params.put("pic", ImgStringlist.get(0));
				} else if (ImgStringlist.size() == 2) {
					params.put("pic", ImgStringlist.get(0) + ","
							+ ImgStringlist.get(1));
				} else if (ImgStringlist.size() == 3) {
					params.put("pic", ImgStringlist.get(0) + ","
							+ ImgStringlist.get(1) + "," + ImgStringlist.get(2));
				}
			}
			params.put("qqShareStatus", qqShareStatus);
			params.put("weiboShareStatus", weiboShareStatus);

			params.put("grade", grade.get(0));
			if (!TextUtils.isEmpty(commentTag)) {
				params.put("commentTag", commentTag);
			}
			if (isAnonymous) {
				params.put("isAnonymous", 1);
			} else {
				params.put("isAnonymous", 0);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->评价 "
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 查询订单明细
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param id
	 * @param handler
	 */
	public static void queryOrderDetails(String phone, String imei,
			Context context, int id, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/queryOrderDetail?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->查询"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询订单明细
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param orderid
	 * @param id
	 * @param handler
	 */
	public static void queryRoomMenuItems(String phone, String imei,
			Context context, int roomType, int orderid,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/queryRoomMenuItems?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("roomType", roomType);
			params.put("orderId", orderid);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->直播详情页底部服务菜单"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * /order/cancelOrder
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param id
	 * @param handler
	 */
	public static void cancelOrder(String phone, String imei, Context context,
			int id, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/cancelOrder?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->取消"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 新增取消原因接口，取消接口调用地方有几处独立原因取消接口 /order/cancelOrder
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param id
	 * @param handler
	 */
	public static void ReasonCancelOrder(String phone, String imei,
			Context context, int id, String reason,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/cancelOrder?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			params.put("reason", reason);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->取消附带原因"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新增游泳取消原因接口，取消接口调用地方有几处独立原因取消接口 /order/orderSwimCancelRemindTxt
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param id
	 * @param handler
	 */
	public static void orderSwimCancelRemindTxt(String phone, String imei,
			Context context, int id, int confirm,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/orderSwimCancelRemindTxt?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			params.put("confirm", confirm);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->游泳取消订单"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// /order/confirmOrder
	public static void confirmOrder(String phone, String imei, Context context,
			int id, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/confirmOrder?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->确认订单"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// /order/changeOrderPayMethod
	public static void changeOrderPayMethod(String phone, String imei,
			Context context, long id, long customerId, int payWay,
			long couponId, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/changeOrderPayMethod?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("orderId", id);
			params.put("customerId", customerId);
			params.put("payWay", payWay);
			params.put("couponId", couponId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->订单列表确认订单"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 订单二次支付
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param id
	 * @param customerId
	 * @param customerName
	 * @param customerMobile
	 * @param remark
	 * @param pickup
	 * @param bathRequired
	 * @param extraItemPrice
	 * @param payPrice
	 * @param payWay
	 * @param couponId
	 * @param debitAmount
	 *            混合支付时余额应支付金额，非混合支付时为-1
	 * @param isVie
	 *            标记是否加急订单----（升级订单不考虑一律用原来changeOrderPayMethodV2）
	 * @param handler
	 * 
	 */
	public static void changeOrderPayMethodV2(String promoterCode,
			String phone, String imei, Context context, long id,
			long customerId, String customerName, String customerMobile,
			String remark, int pickup, int bathRequired, double extraItemPrice,
			double payPrice, int payWay, long couponId, double debitAmount,
			String bathPetIds, boolean isVie, String cardIds,AsyncHttpResponseHandler handler) {
		String url = "";
		if (isVie) {
			url = getServiceBaseUrl() + "pet/vie/changePayWay?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		} else {
			url = getServiceBaseUrl() + "pet/order/changeOrderPayMethodV2?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		}
		try {
			RequestParams params = LocalParm(context);
			if (promoterCode != null) {
				params.put("promoterCode", promoterCode);
			}
			params.put("orderId", id);
			params.put("customerId", customerId);
			params.put("payWay", payWay);
			if (bathRequired > -1)
				params.put("bathRequired", bathRequired);
			params.put("couponId", couponId);
			if (customerName != null && !"".equals(customerName.trim()))
				params.put("customerName", customerName);
			if (customerMobile != null && !"".equals(customerMobile.trim()))
				params.put("customerMobile", customerMobile);
			if (remark != null && !"".equals(remark.trim()))
				params.put("remark", remark);
			if (pickup > -1)
				params.put("pickUp", pickup);
			if (extraItemPrice > -1)
				params.put("extraItemPrice", extraItemPrice);
			params.put("payPrice", payPrice);
			params.put("debitAmount", debitAmount);
			if (!TextUtils.isEmpty(bathPetIds)) {
				params.put("bathPetIds", bathPetIds);
			}
			if (!TextUtils.isEmpty(cardIds)) {
				params.put("cardIds", cardIds);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->订单二次支付"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// /user/updateUserPushNotify
	public static void updateUserPushNotify(String imei, Context context,
			Short pushNotify, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/updateUserPushNotify?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("pushNotify", pushNotify);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->推送开关"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * /pet/coupon/redeemCouponCode
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param code
	 * @param handler
	 */
	public static void redeemCouponCode(String phone, String imei,
			Context context, String code, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/coupon/redeemCouponCode?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("code", code);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->优惠券兑换"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getFe(Context context, AsyncHttpResponseHandler handler) {
		String url = CommUtil.getServiceBaseUrl()
				+ "pet/owner/getFeaturedService?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			Utils.mLogError("特色服务：" + url);
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void getShare(Context context, long time,
			AsyncHttpResponseHandler handler) {
		String url = CommUtil.getServiceBaseUrl()
				+ "static/content/share_text.html?time=" + time+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * /user/getRechargeTemplate
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param handler
	 */
	public static void getRechargeTemplate(String imei, Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/getRechargeTemplate?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->充值卡列表"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * user/getUserAccountBalance
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param handler
	 */
	public static void getUserAccountBalance(String phone, String imei,
			Context context, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/getUserAccountBalance?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->账户余额 "
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * /user/queryTradeHistory
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param pageNum
	 * @param handler
	 */
	public static void queryTradeHistory(String phone, String imei,
			Context context, int pageNum, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/queryTradeHistory?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("pageSize", 10);
			params.put("pageNum", pageNum);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->账户余额记录 "
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * /user/recharge
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param handler
	 */
	public static void recharge(String phone, String imei, Context context,
			int templateId, int payWay, String inviteCode,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/recharge?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("templateId", templateId);
			params.put("payWay", payWay);
			params.put("inviteCode", inviteCode);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->充值id "
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /worker/queryWorksByService
	public static void queryWorksByService(Context context, long servieId,
			long before, long after, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/queryWorksByService?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("serviceId", servieId);
			params.put("before", before);
			params.put("after", after);
			params.put("pageSize", 10);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->查询美容师作品"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /worker/queryCommentsByService
	public static void queryCommentsByService(Context context, String servieId,
			long before, long after, int type, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/queryCommentsByService?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("before", before);
			params.put("after", after);
			params.put("pageSize", 10);
			if (type > 0) {
				params.put("type", type);
			} else {
				params.put("serviceId", servieId);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->查询美容师评价"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// pet/order/GetShopInfo
	public static void GetShopInfo(Context context, int shopId, int serviceId,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/GetShopInfo?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("shopId", shopId);
			params.put("serviceId", serviceId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->到店详情"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getHomeDetail(Context context, int shopId, String time,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/content/homesvc/" + shopId
				+ "/desc?time=" + time+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
			Utils.mLogError("==-->上门详情"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// /coupon/ myacouponsunused
	public static void myacouponsunused(String phone, String imei,
			Context context, int page, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/coupon/myacouponsunused?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("page", page);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->查询可用优惠券"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /coupon/ myacouponsused
	public static void myacouponsused(String phone, String imei,
			Context context, int page, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/coupon/myacouponsused?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("page", page);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->查询不可用优惠券"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /owner/queryShopsWithPrice
	public static void queryShopsWithPrice(Context context, String strp,
			double lat, double lng, int typeId, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/owner/queryShopsWithPrice?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("strp", strp);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			if (typeId > 0)
				params.put("typeId", typeId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->店铺列表"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /hotel/searchTradeArea
	public static void searchTradeArea(Context context, double lat, double lng,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/hotel/searchTradeArea?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("lat", lat);
			params.put("lng", lng);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->判断未登陆状态下是否允许添加地址： "
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /hotel/prepCancelOrder
	public static void prepCancelOrder(String phone, String imei,
			Context context, int id, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/hotel/prepCancelOrder?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->查询退款"
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /user/queryCustomerPets
	public static void queryCustomerPets(String phone, String imei,
			Context context, int serviceid, int kind, int workerid,
			int ServiceType, int templateId, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/queryCustomerPets?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (ServiceType > 0) {
				params.put("serviceType", ServiceType);
			} else {
				if (serviceid != -1 && serviceid != 100) {// 特色服务需要传serviceID
					params.put("serviceId", serviceid);
				} else {
					if (serviceid > 0)
						params.put("serviceType", serviceid);
				}
			}
			if (kind > 0)
				params.put("petKind", kind);
			if (workerid > 0)
				params.put("workerId", workerid);
			if (templateId > 0) {
				params.put("templateId", templateId);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->我的宠物 "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 洗澡美容取消订单原因获取 /order/orderCareCancelRemindTxt
	 * 
	 * @param handler
	 */
	public static void orderCareCancelRemindTxt(String phone, String imei,
			Context context, int id, int confirm,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/orderCareCancelRemindTxt?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			params.put("confirm", confirm);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->洗澡美容取消订单"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 寄养取消订单原因获取 /order/orderHotelCancelRemindTxt
	 * 
	 * @param handler
	 */
	public static void orderHotelCancelRemindTxt(String phone, String imei,
			Context context, int id, int confirm,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl()
				+ "pet/order/orderHotelCancelRemindTxt?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			params.put("confirm", confirm);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->寄养取消订单"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取下单成功弹窗提示
	 * 
	 * @param handler
	 */
	public static void getAddPetTipAfterOrder(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/getAddPetTipAfterOrder?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 发红包 /order/getTipTxtAfterComment
	 * 
	 * @param handler
	 */
	public static void getTipTxtAfterComment(Context context, int id,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl()
				+ "pet/order/getTipTxtAfterComment?id=" + id+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取医院列表
	 * 
	 * @param handler
	 */
	public static void getHospital_List(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl()
				+ "static/content/index/hospital_list.html?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取摄影列表
	 * 
	 * @param handler
	 */
	public static void getPhoto_List(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl()
				+ "static/content/index/photo_list.html?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取训练列表
	 * 
	 * @param handler
	 */
	public static void getTrain_List(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl()
				+ "static/content/index/train_list.html?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取娱乐列表
	 * 
	 * @param handler
	 */
	public static void getEntertain_List(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl()
				+ "static/content/index/entertain_list.html?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取医院详情
	 * 
	 * @param handler
	 */
	public static void getHospital(Context context, int id,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/content/index/hospital_"
				+ id + ".html?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取摄影详情
	 * 
	 * @param handler
	 */
	public static void getPhoto(Context context, int id,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/content/index/photo_" + id
				+ ".html?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取训练详情
	 * 
	 * @param handler
	 */
	public static void getTrain(Context context, int id,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/content/index/train_" + id
				+ ".html?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取娱乐详情
	 * 
	 * @param handler
	 */
	public static void getEntertain(Context context, int id,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "static/content/index/entertain_"
				+ id + ".html?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取特色服务信息
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param petId
	 * @param serviceName
	 * @param level
	 *            美容师级别
	 * @param handler
	 */
	public static void getFeaturedService1(String phone, String imei,
			Context context, int petId, /* String serviceName */int typeId,
			int level, int workerId, int areaId,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/owner/getFeaturedService1?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("petId", petId);
			// params.put("serviceName", serviceName);
			params.put("typeId", typeId);
			params.put("expire", 10);
			if (level > 0) {
				params.put("level", level);
			}
			if (workerId > 0) {
				params.put("workerId", workerId);
			}
			if (areaId > 0 && areaId != 100) {
				params.put("areaId", areaId);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->特色服务 -->  "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 按美容师姓名模糊搜索
	 * 
	 * @param areaid
	 * @param realName
	 * @param page
	 * @param pageSize
	 * @param handler
	 */
	public static void getWorkerByName(Context context, int areaid,
			String realName, int page, int pageSize,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/queryWorkersByName?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (areaid > 0 && areaid != 100)
				params.put("areaId", areaid);
			params.put("realName", realName);
			params.put("page", page);
			params.put("pageSize", pageSize);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("===>getWorkerByName====--->"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 美容师首页进入详情
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param sort
	 * @param areaid
	 * @param page
	 * @param pageSize
	 * @param handler
	 */
	public static void getAllWorkers(String phone, String imei,
			Context context, int sort, int areaid, int page, int pageSize,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/allWorkers?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("sort", sort);
			if (areaid > 0 && areaid != 100)
				params.put("areaId", areaid);
			params.put("page", page);
			params.put("pageSize", pageSize);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->首页进去美容师列表 -->  "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取服务item
	 * 
	 * @param handler
	 */
	public static void queryWorkerMenuItems(Context context, int canWorkAtHome,
			int canWorkAtShop, int workerid, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl()
				+ "pet/worker/queryWorkerMenuItems?canWorkAtHome="
				+ canWorkAtHome + "&canWorkAtShop=" + canWorkAtShop
				+ "&workerId=" + workerid+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->获取美容师服务item -->  " + url);
			client.get(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 获取商圈信息
	public static void getServiceArea(Context context,
			AsyncHttpResponseHandler homeIndexHandler) {
		String url = getServiceBaseUrl() + "pet/shop/queryTradeAreaWithShop?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->获取商圈信息 -->  "
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, homeIndexHandler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void getServiceAreaInfo(Context context,
			AsyncHttpResponseHandler homeIndexHandler, double lat, double lng) {
		String url = getServiceBaseUrl() + "pet/shop/queryTradeAreaByLoc?lat="
				+ lat + "&lng=" + lng+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("获取商圈信息：" + url);
			client.get(url, LocalParm(context), homeIndexHandler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * /pet/order/insertReminderlog
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param orderId
	 * @param handler
	 */
	public static void getInsertReminderlog(String phone, String imei,
			Context context, int orderId, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/insertReminderlog?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", orderId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->催单 -->  "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param handler
	 */
	public static void getLoadAppointments(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/vie/loadAppointments?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->获取加急时间列表：" + url);
			client.get(url, LocalParm(context), handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 仿newOrder新接口
	 * 
	 * @param context
	 * @param cellPhone
	 * @param userId
	 * @param workerLevel
	 * @param serviceLoc
	 * @param couponId
	 * @param strp
	 * @param addressId
	 * @param lng
	 * @param appointDate
	 * @param appointTime
	 * @param lat
	 * @param areaId
	 * @param payWay
	 * @param payPrice
	 * @param debitAmount
	 * @param pickup
	 * @param customer
	 * @param telephone
	 * @param remark
	 * @param handler
	 */
	public static void saveOrder(Context context, String cellPhone, int userId,
			int workerLevel, int serviceLoc, int couponId, String strp,
			String address, int addressId, double lng, String appointDate,
			String appointTime, double lat, int areaId, int shopId, int payWay,
			double payPrice, double debitAmount, int pickup, String customer,
			String telephone, String remark,String cardIds,AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/vie/saveOrder?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("userId", userId);
			params.put("workerLevel", workerLevel);
			params.put("serviceLoc", serviceLoc);
			params.put("couponId", couponId);
			params.put("strp", strp);
			params.put("address", address);
			if (addressId > 0) {
				params.put("addressId", addressId);
			}
			if (lng > 0) {
				params.put("lng", lng);
			}
			params.put("appointDate", appointDate);
			params.put("appointTime", appointTime);
			if (lat > 0) {
				params.put("lat", lat);
			}
			if (areaId > 0 && areaId != 100) {
				params.put("areaId", areaId);
			}
			params.put("shopId", shopId);
			params.put("payWay", payWay);
			params.put("payPrice", payPrice);
			params.put("debitAmount", debitAmount);
			params.put("pickup", pickup);
			params.put("customer", customer);
			params.put("telephone", telephone);
			params.put("remark", remark);
			if (!TextUtils.isEmpty(cardIds)) {
				params.put("cardIds", cardIds);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->加急生成新订单-->  "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	/**
	 * 游泳time
	 * 
	 * @param context
	 * @param phone
	 * @param shopId
	 * @param handler
	 */
	public static void getSwimTime(Context context, String phone, int shopId,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/getSwimTime?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("shopId", shopId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->游泳订单获取可预约时间等数据 -->  "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// pet/order/getSwimTimeNew
	public static void getSwimTimeNew(Context context, String phone,
			int shopId, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/getSwimTimeNew?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("shopId", shopId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->游泳订单获取可预约时间等数据 -->  "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 游泳orderDetail
	 * 
	 * @param context
	 * @param phone
	 * @param shopId
	 * @param handler
	 */
	public static void getSwimDetail(Context context, String phone, int shopId,
			int petId, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/getSwimDetail?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("shopId", shopId);
			params.put("petId", petId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->游泳详情 -->  "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 仿newOrder：swimOrder
	 * 
	 * @param context
	 * @param phone
	 * @param shopid
	 * @param areaid
	 * @param couponid
	 * @param workerid
	 * @param addressid
	 * @param customerName
	 * @param customerMobile
	 * @param address
	 * @param lat
	 * @param lng
	 * @param time
	 * @param totalPrice
	 * @param payPrice
	 * @param payway
	 * @param remark
	 * @param serviceloc
	 * @param pickup
	 * @param isDefaultWorker
	 * @param strp
	 * @param debitAmount
	 * @param uuid
	 * @param handler
	 */

	public static void getSwimOrder(Context context, String phone, int shopid,
			int areaid, int couponid, int workerid, int addressid,
			String customerName, String customerMobile, String address,
			double lat, double lng, String time, double totalPrice,
			double payPrice, int payway, String remark, int serviceloc,
			int pickup, int isDefaultWorker, String strp, double debitAmount,
			String uuid, String promoterCode, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/swimOrder?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (shopid > 0)
				params.put("shopId", shopid);
			if (areaid > 0)
				params.put("areaId", areaid);
			params.put("couponId", couponid);
			params.put("payWay", payway);
			params.put("addressId", addressid);
			params.put("workerId", workerid);
			params.put("customerName", customerName);
			params.put("customerMobile", customerMobile);
			params.put("token", uuid);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			params.put("address", address);
			params.put("appointment", time);
			params.put("remark", remark);
			params.put("type", 3);// 游泳传3订单列表数据出不来。
			params.put("totalPrice", totalPrice);
			params.put("payPrice", payPrice);
			params.put("debitAmount", debitAmount);
			if (strp != null && !"".equals(strp)) {
				params.put("strp", strp);
			}
			params.put("serviceLoc", serviceloc);
			params.put("isDefaultWorker", isDefaultWorker);
			params.put("pickUp", pickup);
			if (promoterCode != null && !promoterCode.equals(null)
					&& !promoterCode.equals("")) {
				params.put("promoterCode", promoterCode);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->游泳新订单参数："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * /order/getResidualTimeOfPay
	 * 
	 * @param context
	 * @param phone
	 * @param customerId
	 * @param id
	 * @param type
	 * @param handler
	 */
	public static void getResidualTimeOfPay(Context context, String phone,
			long customerId, long id, int type, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/getResidualTimeOfPay?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("customerId", customerId);
			params.put("id", id);
			params.put("type", type);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->订单收银台计时："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * /vie/canBePickup 目前提示必要参数未填写
	 * 
	 * @param context
	 * @param phone
	 * @param areaId
	 *            -- shopid
	 * @param addressId
	 * @param appointTime
	 * @param serviceLoc
	 * @param handler
	 */
	public static void canBePickup(Context context, String phone, int areaId,
			int shopId, int addressId, String appointDate, String appointTime,
			int serviceLoc, double lat, double lng,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/vie/canBePickup?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("areaId", areaId);
			params.put("shopId", shopId);
			params.put("addressId", addressId);
			params.put("appointDate", appointDate);
			params.put("appointTime", appointTime);
			params.put("serviceLoc", serviceLoc);
			params.put("lat", lat);
			params.put("lng", lng);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->isCanPickUp ："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 办理狗证订单信息
	public static void getOrderInfo(Context context, String cellphone,
			String imei, String currentVersion, int certiOrderId,
			AsyncHttpResponseHandler orderInfoHanler) {
		String url = getServiceBaseUrl() + "pet/certiOrder/queryCertiOrderById?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", certiOrderId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->办理狗证订单信息："
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, orderInfoHanler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 狗证订单二次支付
	public static void changePayWay(String promoterCode, boolean isHybirdPay,
			double balance, double needpayfee, String cellphone, String imei,
			Context context, int orderNo, int UserId, int paytype, int tcid,
			AsyncHttpResponseHandler changePayWayHanler) {
		String url = getServiceBaseUrl() + "pet/certiOrder/changePayWay?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", orderNo);
			params.put("userId", UserId);
			params.put("payWay", paytype);
			params.put("certiCoupon", tcid);
			params.put("promoterCode", promoterCode);
			if (isHybirdPay) {
				params.put("debitAmount", balance);
			} else {
				params.put("debitAmount", -1);
			}
			params.put("payPrice", needpayfee);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->狗证订单二次支付 ："
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, changePayWayHanler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadExtraMenus(Context context, String cellphone,
			AsyncHttpResponseHandler loadExtraMenusHanler) {
		String url = getServiceBaseUrl() + "pet/user/loadExtraMenus?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->个人中心附加菜单 ："
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, loadExtraMenusHanler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void mycouponsForSwim(Context context, String phone,
			String imei, String version, String time, int type, int serviceloc,
			int workerid, int pickup, String customerName,
			String customerMobile, int addressid, String address, double lat,
			double lng, String strp, int areaId, double payPrice, int shopId,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/coupon/mycouponsForSwim?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("appointment", time);
			if (addressid > 0)
				params.put("addressId", addressid);
			if (workerid > 0)
				params.put("workerId", workerid);
			if (type > 0)
				params.put("type", type);
			params.put("customerName", customerName);
			params.put("customerMobile", customerMobile);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			params.put("address", address);
			params.put("serviceLoc", serviceloc);
			if (pickup > 0)
				params.put("pickUp", pickup);
			params.put("strp", strp);
			params.put("areaId", areaId);
			params.put("payPrice", payPrice);
			params.put("shopId", shopId);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->可用游泳优惠券："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void checkRedeemCode(String redeemCode, int couponId,
			int fee, String serviceType, int shopId, int roomType,
			String petId, String cellphone, Context context, int serviceId,
			AsyncHttpResponseHandler checkRedeemCodeHanler) {
		String url = getServiceBaseUrl() + "pet/certiOrder/checkRedeemCode?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("redeemCode", redeemCode);
			params.put("couponId", couponId);
			params.put("fee", fee);
			params.put("serviceType", serviceType);
			params.put("shopId", shopId);
			params.put("roomType", roomType);
			params.put("petId", petId);
			params.put("serviceId", serviceId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->swim 校验邀请码 ："
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, checkRedeemCodeHanler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取用户常用地址
	public static void queryMyHotAddress(String cellphone, String imei,
			Context context, AsyncHttpResponseHandler queryServiceAddress) {
		String url = getServiceBaseUrl() + "pet/user/queryMyHotAddress?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->获取用户常用地址 ："
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, queryServiceAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 坐标转换
	public static void ConverBaiduToGaoDe(Context context, String lng,
			String lat, AsyncHttpResponseHandler queryServiceAddress) {
		String url = "http://restapi.amap.com/v3/assistant/coordinate/convert?locations="
				+ lng
				+ ","
				+ lat
				+ "+&coordsys=baidu&output=json&key=e26a65e8ab9015b4953f8042edffe386"+ "&system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->坐标转换 ："
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, queryServiceAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 下单界面选取时间回来调此接口，刷新美容师
	public static void queryAvailableWorkers(Context context,
			String strListWokerId, String phone, String imei, String version,
			int areaid, String time, int serviceloc, int shopid, double lat,
			double lng, String strp, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/queryAvailableWorkers?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = LocalParm(context);
			params.put("workerIds", strListWokerId);
			params.put("app_time", time);
			if (lat > 0)
				params.put("lat", lat);
			if (lng > 0)
				params.put("lng", lng);
			params.put("serviceLoc", serviceloc);
			params.put("strp", strp);
			if (shopid > 0) {
				params.put("shopId", shopid);
			}
			if (areaid > 0) {
				params.put("areaId", areaid);
			}
			if (strp != null && !"".equals(strp)) {
				params.put("strp", strp);
			}
			client.setTimeout(getTimeOut());
			Utils.mLogError("下单界面选取时间获取美容师列表："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// /order/saveCancelReason

	public static void saveCancelReason(String cellphone, String imei,
			Context context, int id, String reason,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/saveCancelReason?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			params.put("reason", reason);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->保存取消原因 ："
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void checkDhmCode(String cellphone, String serialNum,
			Context context, AsyncHttpResponseHandler checkDhmCodeHanler) {
		String url = getServiceBaseUrl() + "pet/pay/checkRechargeCard?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("serialNum", serialNum);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->校验兑换码 ："
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, checkDhmCodeHanler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void rechargeWithCard(String cellphone, String serialNum,
			Context context, AsyncHttpResponseHandler rechargeWithCardHanler) {
		String url = getServiceBaseUrl() + "pet/pay/rechargeWithCard?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("serialNum", serialNum);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->礼品卡充值 ："
					+ client.getUrlWithQueryString(true, url, params));
			client.get(url, params, rechargeWithCardHanler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 当中的帖子列表/pet/postInfo/queryPostInfoList
	 * 
	 * @param phone
	 * @param imei
	 * @param context
	 * @param groupId
	 * @param page
	 * @param handler
	 */
	public static void queryPostInfoList(String phone, String imei,
			Context context, int groupId, int page, long timestamp,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/postInfo/queryPostInfoList?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("groupId", groupId);
			params.put("page", page);
			if (page != 1) {
				params.put("timestamp", timestamp);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->帖子列表："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// /postInfo/newPost
	public static void newPost(String phone, Context context, int groupId,
			String content, File[] imgs, File videos, File coverImgs,
			int postType, int source, boolean isAnonymous,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/postInfo/newPost?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (postType == 1) {
				params.put("groupId", groupId);
			}
			if (content != null && !TextUtils.isEmpty(content)) {
				params.put("content", content);
			}
			if ((imgs == null || imgs.length <= 0) && videos != null) {
				params.put("videos", videos);
				params.put("coverImgs", coverImgs);
			} else if ((imgs != null || imgs.length > 0) && videos == null) {
				params.put("imgs", imgs);
			}
			params.put("postType", postType);
			if (source != 0) {
				params.put("source", source);
				if (isAnonymous) {
					params.put("isAnonymous", 1);
				} else {
					params.put("isAnonymous", 0);
				}

			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOutVoi());
			Utils.mLogError("==-->发帖："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
			Utils.mLogError("==-->这里挂了");
		}
	}

	/**
	 * 查询帖子详情 pet/postInfo/queryPostInfo
	 * 
	 * @param phone
	 * @param context
	 * @param postId
	 * @param page
	 * @param handler
	 */
	public static void queryPostInfo(String phone, Context context, int postId,
			int page, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/postInfo/queryPostInfo?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("postId", postId);
			params.put("page", page);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->查询帖子详情："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * /postInfo/queryGroups?cellPhone=18888888888&imei=869186023039594&system=
	 * android_2.6.0 查询圈子
	 * 
	 * @param phone
	 * @param context
	 * @param groupId
	 * @param handler
	 */
	public static void queryGroups(String phone, Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/postInfo/queryGroups?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->查询圈子："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加入圈子 /pet/postInfo/followGroup
	 * 
	 * @param phone
	 * @param context
	 * @param postId
	 * @param page
	 * @param handler
	 */
	public static void followGroup(String phone, Context context, int groupId,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/postInfo/followGroup?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("groupId", groupId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->加入圈子："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 精选帖子列表
	 * 
	 * @param string
	 * @param context
	 * @param page
	 * @param isFollowed
	 * @param isExistsVideo
	 * @param isExistsImg
	 * @param state
	 * @param postType
	 * @param queryGoodPostsHandler
	 */
	public static void queryGoodPosts(long timestamp, String phone,
			Context context, int page, int isFollowed, int isExistsVideo,
			int isExistsImg, AsyncHttpResponseHandler queryGoodPostsHandler) {
		String url = getServiceBaseUrl() + "pet/postInfo/queryGoodPosts?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (page < 1) {
				params.put("page", 1);
			} else {
				params.put("page", page);
			}
			params.put("isFollowed", isFollowed);
			params.put("isExistsVideo", isExistsVideo);
			params.put("isExistsImg", isExistsImg);
			if (timestamp > 0 && page > 1) {
				params.put("timestamp", timestamp);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->精选帖子列表："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, queryGoodPostsHandler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 关注用户
	public static void followUser(String phone, Context context, int userId,
			AsyncHttpResponseHandler followUserHandler) {
		String url = getServiceBaseUrl() + "pet/postInfo/followUser?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("userId", userId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->关注："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, followUserHandler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 赠送鲜花，便便
	 * 
	 * @param phone
	 * @param context
	 * @param postId
	 * @param giftType
	 * @param followUserHandler
	 */
	public static void giftPost(String phone, Context context, int postId,
			int giftType, AsyncHttpResponseHandler followUserHandler) {
		String url = getServiceBaseUrl() + "pet/postInfo/giftPost?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("postId", postId);
			params.put("giftType", giftType);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->赠送鲜花，便便："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, followUserHandler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * /pet/postInfo/commentPost 提交评价
	 * 
	 * @param phone
	 * @param context
	 * @param postId
	 * @param content
	 * @param commentPostHandler
	 */
	public static void commentPost(int contentType, String phone,
			Context context, int postId, String content,
			AsyncHttpResponseHandler commentPostHandler) {
		String url = getServiceBaseUrl() + "pet/postInfo/commentPost?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("postId", postId);
			params.put("contentType", contentType);
			params.put("content", content);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->提交评价："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, commentPostHandler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 启动页
	 * 
	 * @param cellphone
	 * @param context
	 * @param startPageHandler
	 */
	public static void startPage(String cellphone, Context context,
			AsyncHttpResponseHandler startPageHandler) {
		String url = getServiceBaseUrl() + "pet/user/startPage?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->启动页："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, startPageHandler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 个人中心
	 * 
	 * @param cellphone
	 * @param context
	 * @param page
	 * @param userId
	 * @param getUserDataHandler
	 */
	public static void getUserData(String cellphone, Context context, int page,
			int userId, AsyncHttpResponseHandler getUserDataHandler) {
		String url = getServiceBaseUrl() + "pet/postInfo/getUserData?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("userId", userId);
			params.put("page", page);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->个人中心："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, getUserDataHandler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 送花列表
	 * 
	 * @param cellphone
	 * @param context
	 * @param page
	 * @param userId
	 * @param getUserDataHandler
	 */
	public static void followList(String cellphone, Context context, int page,
			int postId, AsyncHttpResponseHandler getUserDataHandler) {
		String url = getServiceBaseUrl() + "pet/postInfo/followList?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("postId", postId);
			params.put("page", page);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->送花列表："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, getUserDataHandler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * /pet/postInfo/deletePost
	 * 
	 * @param cellphone
	 * @param context
	 * @param postId
	 * @param deletePost
	 */
	public static void deletePost(String cellphone, Context context,
			int postId, AsyncHttpResponseHandler deletePost) {
		String url = getServiceBaseUrl() + "pet/postInfo/deletePost?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("postId", postId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->删除帖子："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, deletePost);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * pet/coupon/giveCoupon
	 * 
	 * @param cellphone
	 * @param context
	 * @param couponId
	 * @param toUserPhone
	 * @param giveCoupon
	 */
	public static void giveCoupon(String cellphone, Context context,
			int couponId, String toUserPhone,
			AsyncHttpResponseHandler giveCoupon) {
		String url = getServiceBaseUrl() + "pet/coupon/giveCoupon?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("couponId", couponId);
			params.put("toUserPhone", toUserPhone);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->赠送优惠券："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, giveCoupon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * pet/order/confirmOrderPrompt
	 * 
	 * @param cellphone
	 * @param context
	 * @param orderType
	 * @param confirmOrderPrompt
	 */
	public static void confirmOrderPrompt(String cellphone, Context context,
			int orderType, int workLoc, String strp, int workerId,int tid,
			String appointment,String endDate,
			AsyncHttpResponseHandler confirmOrderPrompt) {
		String url = getServiceBaseUrl() + "pet/order/confirmOrderPrompt?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("orderType", orderType);
			if (workLoc != 0) {
				params.put("workLoc", workLoc);
			}
			if (!TextUtils.isEmpty(strp)) {
				params.put("strp", strp);
			}
			if (workerId > 0) {
				params.put("workerId", workerId);
			}
			if (tid>0) {
				if (tid==10) {
					tid=1;
				}else if (tid==20) {
					tid=2;
				}else if (tid==30) {
					tid=3;
				}
				params.put("tid", tid);
			}
			if (!TextUtils.isEmpty(appointment)) {
				params.put("appointment", appointment);
			}
			if (!TextUtils.isEmpty(endDate)) {
				params.put("endDate", endDate);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->新增免责声明："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, confirmOrderPrompt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 粉丝，关注列表
	 * 
	 * @param cellphone
	 * @param context
	 * @param page
	 * @param type
	 * @param userId
	 * @param getUserDataHandler
	 */
	public static void getUserFansOrfollow(String cellphone, Context context,
			int page, int type, int userId,
			AsyncHttpResponseHandler getUserDataHandler) {
		String url = getServiceBaseUrl() + "pet/postInfo/getUserFansOrfollow?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("type", type);
			params.put("page", page);
			params.put("userId", userId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->粉丝，关注列表："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, getUserDataHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * /pet/user/checkRechargeInviteCode
	 * 
	 * @param cellphone
	 * @param context
	 * @param inviteCode
	 * @param getUserDataHandler
	 */
	public static void checkRechargeInviteCode(String cellphone,
			Context context, String inviteCode, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/user/checkRechargeInviteCode?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("inviteCode", inviteCode);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->充值前校验邀请码"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 我的界面获取菜单列表
	 * 
	 * @param context
	 * @param cellphone
	 * @param loadExtraMenusHanler
	 */
	public static void loadMenuNames(Context context, String cellphone,
			AsyncHttpResponseHandler loadExtraMenusHanler) {
		String url = getServiceBaseUrl() + "pet/user/loadMenuNames?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->我的界面获取菜单列表："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, loadExtraMenusHanler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 扫码获取商品信息
	 * 
	 * @param codeResult
	 * @param context
	 * @param string
	 * @param handler
	 */
	public static void ScanCodePayment(String code, Context context,
			String cellphone, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/goods/prepay?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("code", code);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->扫码获取商品信息："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 扫码支付
	 * 
	 * @param scanRemark
	 * @param scanOrderId
	 * @param isHybirdPay
	 * @param paytype
	 * @param balance
	 * @param totalPayPrice
	 * @param localGoods
	 * @param localShopId
	 * @param context
	 * @param string
	 * @param scanCodePaymentHandler
	 */
	public static void ScanCodePay(String scanRemark, long scanOrderId,
			boolean isHybirdPay, double totalPayPrice, double balance,
			int paytype, String localGoods, int localShopId, Context context,
			String cellphone, AsyncHttpResponseHandler scanCodePaymentHandler) {
		String url = getServiceBaseUrl() + "pet/goods/pay?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("goods", localGoods);
			params.put("shopId", localShopId);
			params.put("payPrice", totalPayPrice);
			if (isHybirdPay) {
				params.put("debitAmount", balance);
			}
			if (scanOrderId > 0) {
				params.put("orderId", scanOrderId);
			} else {
				if (scanRemark != null && !TextUtils.isEmpty(scanRemark)) {
					params.put("remark", scanRemark);
				}
			}
			params.put("payWay", paytype);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->扫码支付："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, scanCodePaymentHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void Train(Context context, String cellphone, int areaId,
			int petId, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/train/items?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (areaId > 0 && areaId != 100) {
				params.put("areaId", areaId);
			}
			params.put("petId", petId);// 先不传petid 传了没服务
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->训练："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * pet/train/getTrainVideos
	 * 
	 * @param context
	 * @param cellphone
	 * @param handler
	 */
	public static void getTrainVideos(Context context, String cellphone,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/train/getTrainVideos?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->训练视频列表："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 训练首次支付接口 /pet/train/pay
	 * 
	 * @param context
	 * @param cellphone
	 * @param appointment
	 *            预约时间
	 * @param areaId
	 *            区域id
	 * @param couponId
	 *            优惠券id
	 * @param customerMobile
	 *            联系人手机号
	 * @param customerName
	 *            联系人姓名
	 * @param debitAmount
	 *            混合支付时余额应支付的金额，非混合支付时为-1
	 * @param remark
	 *            提交的备注
	 * @param lat
	 * @param lng
	 *            经纬度
	 * @param payPrice
	 *            需付款金额
	 * @param payWay
	 *            支付方式3：优惠券1：微信2：支付宝 4:余额
	 * @param shopId
	 *            门店id
	 * @param strp
	 *            多宠物的petid_serviceid_我的宠物ID_单项ID 如 6_1_3_1,2,5
	 * @param uuid
	 * @param type
	 *            类型 训练type = 4
	 * @param handler
	 */
	public static void trainPay(Context context, String cellphone,
			String appointment, int areaId, int couponId,
			String customerMobile, String customerName, double debitAmount,
			String remark, double lat, double lng, double payPrice, int payWay,
			int shopId, String strp, String uuid, int type,
			String promoterCode, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/train/pay?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("appointment", appointment);
			params.put("areaId", areaId);
			params.put("couponId", couponId);
			params.put("customerMobile", customerMobile);
			params.put("customerName", customerName);
			params.put("debitAmount", debitAmount);
			params.put("remark", remark);
			params.put("lat", lat);
			params.put("lng", lng);
			params.put("payPrice", payPrice);
			params.put("payWay", payWay);
			params.put("shopId", shopId);
			params.put("strp", strp);
			params.put("token", uuid);
			params.put("type", 4);
			if (!TextUtils.isEmpty(promoterCode)) {
				if (promoterCode.length() == 4) {
					params.put("promoterCode", promoterCode);
				}
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->训练首次支付接口："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 训练订单二次支付 pet/train/pay
	 * 
	 * @param context
	 * @param cellphone
	 * @param couponId
	 * @param customerId
	 * @param customerMobile
	 * @param customerName
	 * @param debitAmount
	 * @param orderId
	 * @param payPrice
	 * @param payWay
	 * @param handler
	 */
	public static void TwoTrainPay(Context context, String cellphone,
			int couponId, int customerId, String customerMobile,
			String customerName, double debitAmount, int orderId,
			double payPrice, int payWay, String promoterCode,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/train/pay?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("couponId", couponId);
			params.put("customerId", customerId);
			params.put("customerMobile", customerMobile);
			params.put("customerName", customerName);
			params.put("debitAmount", debitAmount);
			params.put("orderId", orderId);
			params.put("payPrice", payPrice);
			params.put("payWay", payWay);
			params.put("type", 4);
			if (!TextUtils.isEmpty(promoterCode)) {
				if (promoterCode.length() == 4) {
					params.put("promoterCode", promoterCode);
				}
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->训练二次支付接口："
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 训练取消订单原因获取 orderTrainCancelRemindTxt confirm = 0 取消订单 1 取消原因
	 * 
	 * @param handler
	 */
	public static void orderTrainCancelRemindTxt(String phone, String imei,
			Context context, int id, int confirm,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl()
				+ "pet/order/orderTrainCancelRemindTxt?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			params.put("confirm", confirm);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->训练取消订单"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 首页活动可配置
	 * 
	 * @param context
	 * @param handler
	 * @param http
	 *            ://192.168.0.252/static/content/design/version/
	 *            homeActivityPage.html
	 */
	public static void getHomeActivityPage(Context context,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "/pet/user/getHomeActivityPage?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
			Utils.mLogError("==-->首页活动：= " + url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 会员中心
	 * 
	 * @param context
	 * @param memberHanler
	 */
	public static void loadMemberInfo(Context context,
			AsyncHttpResponseHandler memberHanler) {
		String url = getServiceBaseUrl() + "/pet/member/loadMemberInfo?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, params, memberHanler);
			Utils.mLogError("==-->会员中心：= "
					+ client.getUrlWithQueryString(true, url, params));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量查询美容师
	 * 
	 * @param context
	 * @param workerIds
	 * @param handler
	 */
	public static void queryWorkersByIds(Context context, String workerIds,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/worker/queryWorkersByIds?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("workerIds", workerIds);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->批量查询美容师"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// /order/modifyOrder?workerId=1&appointment=2017-01-20 14:30:00&id=11
	public static void modifyOrder(Context context, int workerId,
			String appointment, int id, String customerName,
			String customerMobile, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/modifyOrder?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (workerId > 0) {
				params.put("workerId", workerId);
			}
			if (!TextUtils.isEmpty(appointment)) {
				params.put("appointment", appointment);
			}
			params.put("id", id);
			if (!TextUtils.isEmpty(customerName)) {
				params.put("customerName", customerName);
			}
			if (!TextUtils.isEmpty(customerMobile)) {
				params.put("customerMobile", customerMobile);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->改单 或 修改联系人"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.mLogError("==-->改单 或 修改联系人 请求接口挂了");
		}
	}
	/**改单是否有接送提示文案
	 * modifyOrderCheck
	 * @param context
	 * @param workerId
	 * @param appointment
	 * @param orderId
	 * @param shopId
	 * @param handler
	 */
	public static void modifyOrderCheck(Context context,int workerId,String appointment,int orderId,int shopId,AsyncHttpResponseHandler handler){
		String url = getServiceBaseUrl() + "pet/order/modifyOrderCheck?system=" + getSource()+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			if (workerId > 0) {
				params.put("workerId", workerId);
			}
			if (!TextUtils.isEmpty(appointment)) {
				params.put("appointment", appointment);
			}
			if (orderId>0) {
				params.put("orderId", orderId);
			}
			if (shopId>0) {
				params.put("shopId", shopId);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->改单接送提示"+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.mLogError("==-->改单接送提示");
		}
	}

	// 最近订单
	public static void choose(Context context, AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/feedback/choose?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("最近订单"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void commentStar(Context context, int grade,
			AsyncHttpResponseHandler handler) {
		String url = getServiceBaseUrl() + "pet/order/commentStar?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);

		try {
			RequestParams params = LocalParm(context);
			params.put("grade", grade);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->评价星级选择"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, handler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 投诉原因
	public static void reason(Context context, int feedType, int type,
			int workLoc, AsyncHttpResponseHandler reasonHandler) {
		String url = getServiceBaseUrl() + "pet/feedback/reason?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("feedType", feedType);
			params.put("type", type);
			params.put("workLoc", workLoc);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("投诉原因"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, reasonHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 提交投诉
	public static void save(Context context, int feedType, String reason,
			String content, int orderId, AsyncHttpResponseHandler saveHandler) {
		String url = getServiceBaseUrl() + "pet/feedback/save?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("feedType", feedType);
			params.put("reason", reason);
			params.put("content", content);
			params.put("orderId", orderId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("提交投诉"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, saveHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 投诉历史
	public static void history(Context context, int feedType,
			AsyncHttpResponseHandler historyHandler) {
		String url = getServiceBaseUrl() + "pet/feedback/history?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("feedType", feedType);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("投诉历史"
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, historyHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * pet/user/queryCustomerOrderComment
	 * 
	 * @param context
	 * @param page
	 * @param Handler
	 */
	public static void queryCustomerOrderComment(Context context, int page,
			AsyncHttpResponseHandler Handler) {
		String url = getServiceBaseUrl() + "pet/user/queryCustomerOrderComment?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("page", page);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->查询我的评价 "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, Handler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 下发支付方式
	public static void payWays(Context context, String type, int orderId,
			AsyncHttpResponseHandler payWaysHandler) {
		String url = getServiceBaseUrl() + "pet/pay/payWays?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("type", type);
			if (orderId > 0) {
				params.put("orderId", orderId);
			}
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->下发支付方式 "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, payWaysHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 订单投诉详情
	public static void detail(Context context, int orderId,
			AsyncHttpResponseHandler detailHandler) {
		String url = getServiceBaseUrl() + "pet/feedback/detail?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("orderId", orderId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->订单投诉详情 "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, detailHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void cardDetail(Context context, int id, int petId,
			AsyncHttpResponseHandler cardDetail) {
		String url = getServiceBaseUrl() + "pet/card/cardDetail?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("id", id);
			params.put("petId", petId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->次卡服务详情 "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, cardDetail);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 次卡购买
	 * 
	 * @param context
	 * @param templateId
	 * @param petId
	 * @param petCard
	 * @param payPrice
	 * @param debitAmount
	 * @param payWay
	 * @param packageId
	 * @param BuycardDetail
	 */
	public static void BuyCard(Context context, int templateId, int petId,
			int petCard, double payPrice, double debitAmount, int payWay,
			int packageId, AsyncHttpResponseHandler BuycardDetail) {
		String url = getServiceBaseUrl() + "pet/card/buy?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("templateId", templateId);
			params.put("petId", petId);
			if (petCard > 0) {
				params.put("petCard", petCard);
			}
			params.put("payPrice", payPrice);
			params.put("debitAmount", debitAmount);
			params.put("payWay", payWay);
			params.put("packageId", packageId);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			Utils.mLogError("==-->次卡购买 "
					+ client.getUrlWithQueryString(true, url, params));
			client.post(url, params, BuycardDetail);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 卡包可用不可用 pet/card/myCardPackage
	 * 
	 * @param context
	 * @param page
	 *            start 1
	 * @param status
	 *            1 可用 2 不可用
	 * @param cardDetail
	 */
	public static void myCardPackage(Context context, int page, int status,
			AsyncHttpResponseHandler cardDetail) {
		String url = getServiceBaseUrl() + "pet/card/myCardPackage?system=" + getSource()
				+ "_" + Global.getCurrentVersion(context);
		try {
			RequestParams params = LocalParm(context);
			params.put("page", page);
			params.put("status", status);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			if (status == 1) {
				Utils.mLogError("==-->卡包可用  "
						+ client.getUrlWithQueryString(true, url, params));
			} else if (status == 2) {
				Utils.mLogError("==-->卡包不可用  "
						+ client.getUrlWithQueryString(true, url, params));
			}
			client.post(url, params, cardDetail);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
