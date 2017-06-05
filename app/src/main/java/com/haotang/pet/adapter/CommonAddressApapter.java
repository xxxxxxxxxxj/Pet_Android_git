package com.haotang.pet.adapter;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.pet.R;
import com.haotang.pet.entity.CommAddr;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

public class CommonAddressApapter extends BaseAdapter {

	private List<CommAddr> list;
	private Context context;
	public static int selectIndex = 0;//当前可作为默认选中貌似
	private int clickTemp = -1;
	private int positionNew;
	private Button bt_titlebar_other;
	private Button button_footer_add_address;
	private MProgressDialog pDialog;
	public CommonAddressApapter(Context context,List<CommAddr> list,Button bt_titlebar_other,MProgressDialog pDialog,Button button_footer_add_address){
		this.context=context;
		this.list=list;
		this.bt_titlebar_other = bt_titlebar_other; 
		this.pDialog = pDialog; 
		this.button_footer_add_address = button_footer_add_address; 
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		 ViewHolderComm viewHolderComm = null;
		if (view == null) {
			viewHolderComm = new ViewHolderComm();
			view = LayoutInflater.from(context).inflate(R.layout.comm_address_item, null);
			viewHolderComm.textView_common_address_item_name = (TextView) view.findViewById(R.id.textView_common_address_item_name);
			viewHolderComm.textView_show = (TextView) view.findViewById(R.id.textView_show);
			viewHolderComm.imageView_common_address_item_choose = (ImageView) view.findViewById(R.id.imageView_common_address_item_choose);
			viewHolderComm.layout_common_item_show = (RelativeLayout) view.findViewById(R.id.layout_common_item_show);
			view.setTag(viewHolderComm);
		}else {
			viewHolderComm = (ViewHolderComm) view.getTag();
		}
		viewHolderComm.textView_common_address_item_name.setText(list.get(position).address);
		if (clickTemp == position) {
			viewHolderComm.layout_common_item_show.setBackgroundColor(Color.parseColor("#FFFFFF"));
			viewHolderComm.textView_common_address_item_name.setTextColor(context.getResources().getColor(R.color.a333333));
			viewHolderComm.layout_common_item_show.setBackgroundColor(Color.parseColor("#FFFFFF"));
			viewHolderComm.imageView_common_address_item_choose.setVisibility(View.VISIBLE);
		}else {
			viewHolderComm.layout_common_item_show.setBackgroundColor(Color.parseColor("#FFFFFF"));
			viewHolderComm.textView_common_address_item_name.setTextColor(context.getResources().getColor(R.color.a333333));
			viewHolderComm.layout_common_item_show.setBackgroundColor(Color.parseColor("#FFFFFF"));
			viewHolderComm.imageView_common_address_item_choose.setVisibility(View.INVISIBLE);
		}
		if (list.get(position).isSelected) {
			viewHolderComm.textView_show.setVisibility(View.VISIBLE);
		}else {
			viewHolderComm.textView_show.setVisibility(View.GONE);
		}
		viewHolderComm.textView_show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MDialog mDialog = new MDialog.Builder(context)
				.setTitle("提示")
				.setType(MDialog.DIALOGTYPE_CONFIRM)
				.setMessage("是否确认删除?")
				.setCancelStr("否")
				.setOKStr("是")
				.positiveListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						pDialog.showDialog();
						positionNew = position;
						CommUtil.deleteServiceAddress(SharedPreferenceUtil.getInstance(context).getString("cellphone", ""), 
								Global.getIMEI(context),
								context,list.get(position).Customer_AddressId,deleteServiceAddress);
					}
				}).build();
				mDialog.show();
				
			}
		});
		return view;
	}

	
	public  class ViewHolderComm {
		  TextView textView_common_address_item_name;
		  ImageView imageView_common_address_item_choose;
		  RelativeLayout layout_common_item_show;
		  LinearLayout layout_common_address_item_choose;
		  LinearLayout layout_common_address_item_back_change;
		  TextView textView_show;
	}
	
	
	public void setSeclection(int position) {
		clickTemp = position;
	}
	
	
	private AsyncHttpResponseHandler deleteServiceAddress = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			Utils.mLogError("==-->delete "+new String(responseBody));
			try {
				pDialog.closeDialog();
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					String msg = jsonObject.getString("msg");
					Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
					list.remove(positionNew);
					notifyDataSetChanged();
					if (list.size()<=0) {
						bt_titlebar_other.setText("编辑");
						bt_titlebar_other.setVisibility(View.GONE);
						button_footer_add_address.setVisibility(View.VISIBLE);
					}
//					showWhereLayout();//再次加载，当前用法会出现闪屏bug，效果体验稍差
				}else {
					//服务器返回字段输出
					pDialog.closeDialog();
					String msg = jsonObject.getString("msg");
					ToastUtil.showToastShort(context, msg);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pDialog.closeDialog();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
		}
		
	};
}
