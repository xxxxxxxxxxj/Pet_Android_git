package com.haotang.pet;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.entity.MyCouponCanUse;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

public class MyCouponGiveOtherActivity extends SuperActivity implements OnClickListener{

	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private TextView tv_couponitem_money;
	private TextView tv_couponitem_content;
	private TextView tv_couponitem_time;
	private TextView textview_isgohomeorgoshop;
	private EditText editText_write_num;
	private Button button_to_give;
	private MyCouponCanUse canUse = null;
	private int removePosition = -1;
	private ImageView imageview_tag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycoupon_give_other);
		getIntentData();
		initView();
		setView();
	}
	private void setView() {
		if (canUse!=null) {
			tv_couponitem_money.setText(canUse.amount+"");
			textview_isgohomeorgoshop.setText(canUse.description);
			tv_couponitem_content.setText(canUse.name.trim());
			tv_couponitem_time.setText(canUse.startTime.substring(0, canUse.startTime.indexOf(" ")).replace("-", ".")+
				" - "+canUse.endTime.substring(0, canUse.endTime.indexOf(" ")).replace("-", "."));
			if (canUse.isGive==1) {
				imageview_tag.setVisibility(View.VISIBLE);
				imageview_tag.setImageResource(R.drawable.good_friend);
			}
		}
	}
	private void getIntentData() {
		canUse = (MyCouponCanUse) getIntent().getSerializableExtra("canUse");
		removePosition = getIntent().getIntExtra("position", -1);
	}
	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		tv_couponitem_money = (TextView) findViewById(R.id.tv_couponitem_money);
		tv_couponitem_content = (TextView) findViewById(R.id.tv_couponitem_content);
		tv_couponitem_time = (TextView) findViewById(R.id.tv_couponitem_time);
		textview_isgohomeorgoshop = (TextView) findViewById(R.id.textview_isgohomeorgoshop);
		editText_write_num = (EditText) findViewById(R.id.editText_write_num);
		button_to_give = (Button) findViewById(R.id.button_to_give);
		imageview_tag = (ImageView) findViewById(R.id.imageview_tag);
		
		imageview_tag.setVisibility(View.GONE);
		tv_titlebar_title.setText("优惠券");
		button_to_give.setOnClickListener(this);
		ib_titlebar_back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finishWithAnimation();
			break;
		case R.id.button_to_give:
			boolean isPhone = Utils.checkPhone(mContext, editText_write_num);
			if (!isPhone) {
				ToastUtil.showToastShortCenter(mContext, "请输入正确手机号");
				return;
			}
			mPDialog.showDialog();
			CommUtil.giveCoupon(spUtil.getString("cellphone", ""), 
					mContext,canUse.CouponId,editText_write_num.getText().toString(), 
					giveCoupon);
			Utils.mLogError("==-->canUse.CouponId "+canUse.CouponId+" editText_write_num:= "+editText_write_num.getText().toString());
			break;

		}
	}
	
	private AsyncHttpResponseHandler giveCoupon  = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			Utils.mLogError("==-->赠送优惠券"+new String(responseBody));
			mPDialog.closeDialog();
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code == 0) {
					MyCouponActivity.delteCoupon(removePosition);
					Utils.mLogError("==-->removePosition 1 := "+removePosition);
					finish();
				}else {
					if (object.has("msg")&&!object.isNull("msg")) {
						ToastUtil.showToastShortCenter(mContext, object.getString("msg"));
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
	};
}
