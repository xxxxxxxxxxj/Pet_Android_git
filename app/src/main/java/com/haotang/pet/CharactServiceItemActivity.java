package com.haotang.pet;

import com.haotang.pet.entity.Charact;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class CharactServiceItemActivity extends Activity implements OnClickListener{
	private RelativeLayout rlMain;
	private ImageButton ibClose;
	private RelativeLayout rlItem1;
	private ImageView ivItem1;
	private TextView tvItem1;
	private RelativeLayout rlItem2;
	private ImageView ivItem2;
	private TextView tvItem2;
	private RelativeLayout rlItem3;
	private ImageView ivItem3;
	private TextView tvItem3;
	private Charact ct;
	private SharedPreferenceUtil spUtil;
	private LayoutParams lp;
	private LinearLayout llTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.charactserviceitem);
		findView();
		setView();
	}

	private void findView() {
		spUtil = SharedPreferenceUtil.getInstance(this);
		rlMain = (RelativeLayout) findViewById(R.id.rl_charactserviceitem_main);
		ibClose = (ImageButton) findViewById(R.id.ib_charactserviceitem_close);
		rlItem1 = (RelativeLayout) findViewById(R.id.rl_charactserviceitem_item1);
		ivItem1 = (ImageView) findViewById(R.id.iv_charactserviceitem_item1);
		tvItem1 = (TextView) findViewById(R.id.tv_charactserviceitem_item1);
		rlItem2 = (RelativeLayout) findViewById(R.id.rl_charactserviceitem_item2);
		ivItem2 = (ImageView) findViewById(R.id.iv_charactserviceitem_item2);
		tvItem2 = (TextView) findViewById(R.id.tv_charactserviceitem_item2);
		rlItem3 = (RelativeLayout) findViewById(R.id.rl_charactserviceitem_item3);
		ivItem3 = (ImageView) findViewById(R.id.iv_charactserviceitem_item3);
		tvItem3 = (TextView) findViewById(R.id.tv_charactserviceitem_item3);
		llTag = (LinearLayout) findViewById(R.id.ll_charactserviceitem_tag);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	private void setView() {
		ct = (Charact) getIntent().getSerializableExtra("charactitem");
		
		int[] wh = Utils.getDisplayMetrics(this);
		int imagewidth = Utils.dip2px(this, 70);
		
		if(ct.serviceitems.size()==2){
			int marginwidth = (wh[0]-imagewidth*2)/6;
			lp.setMargins(marginwidth, wh[1]/2-imagewidth, marginwidth, 0);
			llTag.setLayoutParams(lp);
			rlItem3.setVisibility(View.GONE);
			tvItem1.setText(ct.serviceitems.get(0).name);
			tvItem2.setText(ct.serviceitems.get(1).name);
			setImageTag(ivItem1, ct.serviceitems.get(0).name);
			setImageTag(ivItem2, ct.serviceitems.get(1).name);
		}else{
			int marginwidth = (wh[0]-imagewidth*3)/8;
			lp.setMargins(marginwidth, wh[1]/2-imagewidth, marginwidth, 0);
			llTag.setLayoutParams(lp);
			rlItem3.setVisibility(View.VISIBLE);
			
			tvItem1.setText(ct.serviceitems.get(0).name);
			tvItem2.setText(ct.serviceitems.get(1).name);
			tvItem3.setText(ct.serviceitems.get(2).name);
			setImageTag(ivItem1, ct.serviceitems.get(0).name);
			setImageTag(ivItem2, ct.serviceitems.get(1).name);
			setImageTag(ivItem3, ct.serviceitems.get(2).name);
		}
		
		rlMain.setOnClickListener(this);
		ibClose.setOnClickListener(this);
		rlItem1.setOnClickListener(this);
		rlItem2.setOnClickListener(this);
		rlItem3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_charactserviceitem_main:
			overridePendingTransition(R.anim.zoomout, R.anim.zoomin);
			finish();
			break;
		case R.id.ib_charactserviceitem_close:
			finish();
			break;
		case R.id.rl_charactserviceitem_item1:
			if(ct!=null&&ct.serviceitems.size()>0)
				setclick(ct.serviceitems.get(0).serviceid,ct.serviceitems.get(0).servicetype);
			break;
		case R.id.rl_charactserviceitem_item2:
			if(ct!=null&&ct.serviceitems.size()>1)
				setclick(ct.serviceitems.get(1).serviceid,ct.serviceitems.get(1).servicetype);
			
			break;
		case R.id.rl_charactserviceitem_item3:
			if(ct!=null&&ct.serviceitems.size()>2)
				setclick(ct.serviceitems.get(2).serviceid,ct.serviceitems.get(2).servicetype);
			
			break;
		}
	}
	private void setImageTag(ImageView iv,String name){
		if(name.contains("洗澡"))
			iv.setBackgroundResource(R.drawable.icon_spa_bath);
		else if(name.contains("美容"))
			iv.setBackgroundResource(R.drawable.icon_spa_beauty);
		else
			iv.setBackgroundResource(R.drawable.icon_spa);
	}
	private void setclick(int serviceid,int servicetype){
		if(isLogin()&&hasPet()&&hasService(serviceid)){
			goNext(ServiceActivity.class, serviceid,servicetype,Global.PRE_MAINFRAGMENT);
		}else{
//			goNext(ChoosePetActivity.class, serviceid, Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
			goNext(ChoosePetActivityNew.class, serviceid,servicetype, Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
		}
	}
	
	private boolean isLogin(){
		if(spUtil.getInt("userid", -1) > 0&&!"".equals(spUtil.getString("cellphone", "")))
			return true;
		return false;
	}
	private boolean hasPet(){
		if(spUtil.getInt("petid", -1) > 0)
			return true;
		return false;
	}
	//上个订单的宠物是否有该服务
	private boolean hasService(int serviceid){
		for(int i=0;i<MainActivity.petServices.length;i++){
			if(serviceid == MainActivity.petServices[i]){
				return true;
			}
		}
		return false;
	}
	private void goNext(Class clazz, int serviceid,int servicetype, int previous){
		Intent intent = new Intent(this, clazz);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		intent.putExtra("serviceid", serviceid);
		intent.putExtra("servicetype", servicetype);
		intent.putExtra("previous", previous);
		startActivity(intent);
		finish();
	}
	
	
}
