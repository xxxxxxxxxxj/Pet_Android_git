package com.haotang.pet.wxapi;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.haotang.pet.CardsDetailActivity;
import com.haotang.pet.OrderDetailActivity;
import com.haotang.pet.RechargePage;
import com.haotang.pet.UpgradeServiceActivity;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.UmengStatistics;
import com.haotang.pet.util.Utils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
    private IWXAPI api;
	private OrderDetailActivity act;
	private SharedPreferenceUtil spUtil;
	private int servicetype;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spUtil = SharedPreferenceUtil.getInstance(this);
    	api = WXAPIFactory.createWXAPI(this, Global.APP_ID);

        api.handleIntent(getIntent(), this);
        
        
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			Utils.mLogError("微信支付返回");
			Utils.mLogError("errCode="+resp.errCode);
			Utils.mLogError("errStr="+resp.errStr);
			Utils.mLogError("-----------RechargePage.rechargePage==null??"+(RechargePage.rechargePage==null));
			if(resp.errCode==0){
				//支付成功
				Utils.mLogError("----------errcode==0");
				try {
					if(RechargePage.rechargePage!=null){
						RechargePage.rechargePage.finish();;
						//充值成功
//						UmengStatistics.UmengEventStatistics(this,Global.UmengEventID.click_ChargePaySucess);//充值成功
						sendToMyLastMoney();
					}else if (CardsDetailActivity.detailActivity!=null) {
						CardsDetailActivity.detailActivity.finish();
						sendToOrderDetail();
					}else{
						if(spUtil.getInt("upgradeservice", 0)==Global.PRE_ORDERDETAIL_TO_UPGRADESERVICE){
							if(UpgradeServiceActivity.act!=null)
								UpgradeServiceActivity.act.setResultForOrderDetail();
						}else{
							String orderno = spUtil.getString("orderno", "");
							sendToOrderDetail();
						}
						//洗澡美容付款成功需判断
						servicetype = spUtil.getInt("servicetype", 0);
						if (servicetype!=0) {
							if (servicetype==1) {
//								UmengStatistics.UmengEventStatistics(this,Global.UmengEventID.result_BathPaySucuess);//洗澡支付成功
							}else if (servicetype==2) {
//								UmengStatistics.UmengEventStatistics(this,Global.UmengEventID.result_BeautyPaySucuess);//美容支付成功
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		spUtil.removeData("orderno");
		spUtil.removeData("upgradeservice");
		finish();
	}
	private void sendToMian(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.mainactivity");
		intent.putExtra("previous", Global.PRE_PAYSUCCESSACTIVITY_TO_MAINACTIVITY);
		sendBroadcast(intent);
		Utils.mLogError("开始发送广播");
	}
	private void sendToOrderDetail(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.orderpay");
		sendBroadcast(intent);
		Utils.mLogError("111开始发送广播");
	}
	private void sendToMyLastMoney(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MyLastMoney");
		intent.putExtra("money", RechargePage.rechargeAmount);
		Utils.mLogError("==-->微信发送通知："+RechargePage.rechargeAmount);
		sendBroadcast(intent);
		Utils.mLogError("==-->微信充值完成发广播了");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		spUtil.removeData("servicetype");
		
	}
	
}