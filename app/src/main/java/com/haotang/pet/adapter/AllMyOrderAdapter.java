package com.haotang.pet.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.f;
import com.haotang.pet.MainActivity;
import com.haotang.pet.R;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CircleImageView;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 我的订单  全部adapter
 * @author zhiqiang
 *
 */
public class AllMyOrderAdapter extends BaseAdapter{

	private Context context;
	private List<ArrayMap<String,Object>> list;
	public AllMyOrderAdapter(Context context,List<ArrayMap<String,Object>> list){
		this.context=context;
		this.list=list;
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
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (view==null) {
			mHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.my_order_item_show,null);
			mHolder.textView_myorder_name = (TextView) view.findViewById(R.id.textView_myorder_name);
			mHolder.textView_myorder_state = (TextView) view.findViewById(R.id.textView_myorder_state);
			mHolder.imageView_myorder_animal = (SelectableRoundedImageView) view.findViewById(R.id.imageView_myorder_animal);
			mHolder.textView_myorder_price = (TextView) view.findViewById(R.id.textView_myorder_price);
			mHolder.textView_myorder_time = (TextView) view.findViewById(R.id.textView_myorder_time);
			mHolder.textView_myorder_addrtype = (TextView) view.findViewById(R.id.textView_myorder_addrtype);
			mHolder.textView_myorder_addrtype_des = (TextView) view.findViewById(R.id.textView_myorder_addrtype_des);
			mHolder.textView_myorder_name_show = (TextView) view.findViewById(R.id.textView_myorder_name_show);
			mHolder.imageview_myorder_icon = (ImageView) view.findViewById(R.id.imageview_myorder_icon);
			view.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) view.getTag();
		}
		
		try {
				//item icon set
//			mHolder.imageview_myorder_icon.setBackgroundResource(R.drawable.wash);//洗澡
//			mHolder.imageview_myorder_icon.setBackgroundResource(R.drawable.cosmetology);//美容
//			mHolder.imageview_myorder_icon.setBackgroundResource(R.drawable.foster);//寄养
			if (list.get(arg0).get("type").equals(1)) {//洗澡和美容
				if (list.get(arg0).get("serviceType").equals(1)) {
					mHolder.imageview_myorder_icon.setBackgroundResource(R.drawable.wash);//洗澡
					mHolder.textView_myorder_name_show.setText("洗澡");
				}else if (list.get(arg0).get("serviceType").equals(2)) {
					mHolder.imageview_myorder_icon.setBackgroundResource(R.drawable.cosmetology);//美容
					mHolder.textView_myorder_name_show.setText("美容");
				}else if (list.get(arg0).get("serviceType").equals(3)) {
					mHolder.imageview_myorder_icon.setBackgroundResource(R.drawable.icon_specialservice);//美容
					mHolder.textView_myorder_name_show.setText("特色服务");
				}
			}else if (list.get(arg0).get("type").equals(2)) {
				mHolder.imageview_myorder_icon.setBackgroundResource(R.drawable.foster);//寄养
				mHolder.textView_myorder_name_show.setText("寄养");
			}
//			mHolder.textView_myorder_name_show.setText("洗澡美容寄养");
			if (list.get(arg0).get("nickName")!=null&&!"".equals(list.get(arg0).get("nickName"))) {
				mHolder.textView_myorder_name.setText(list.get(arg0).get("nickName").toString().trim()+list.get(arg0).get("name").toString());
			}else {
				mHolder.textView_myorder_name.setText(list.get(arg0).get("petName").toString().trim()+list.get(arg0).get("name").toString());
			}
			mHolder.textView_myorder_state.setText(list.get(arg0).get("statusDescription").toString());
			
			if (list.get(arg0).get("nickavatarPath")!=null&&!"".equals(list.get(arg0).get("nickavatarPath"))) {
				mHolder.imageView_myorder_animal.setTag(CommUtil.getServiceNobacklash()+list.get(arg0).get("nickavatarPath").toString());
//				if (list.get(arg0).get("avatarPath")!=null&&!"".equals(list.get(arg0).get("avatarPath"))) {
				ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+list.get(arg0).get("nickavatarPath").toString(), 
							mHolder.imageView_myorder_animal,0, new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingComplete(String path, View view, Bitmap bitmap) {
							// TODO Auto-generated method stub
							if(view!=null&&bitmap!=null){
								ImageView iv = (ImageView) view;
								String imagetag = (String) iv.getTag();
								if(path!=null&&path.equals(imagetag)){
									iv.setImageBitmap(bitmap);
								}
							}
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
							
						}
					});
//				}
			}else {
				if (list.get(arg0).get("avatarPath")!=null&&!"".equals(list.get(arg0).get("avatarPath"))) {
					mHolder.imageView_myorder_animal.setTag(CommUtil.getServiceNobacklash()+list.get(arg0).get("avatarPath").toString());
					ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+list.get(arg0).get("avatarPath").toString(), 
							mHolder.imageView_myorder_animal,0, new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onLoadingComplete(String path, View view, Bitmap bitmap) {
							// TODO Auto-generated method stub
							if(view!=null&&bitmap!=null){
								ImageView iv = (ImageView) view;
								String imagetag = (String) iv.getTag();
								if(path!=null&&path.equals(imagetag)){
									iv.setImageBitmap(bitmap);
								}
							}
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
				mHolder.textView_myorder_price.setText("¥ "+Utils.formatDouble(Double.parseDouble(list.get(arg0).get("totalPrice").toString()), 2));
				//从这里往下判断分为寄养、洗澡美容分开
				if (list.get(arg0).get("type").equals(1)) {//洗澡和美容
					mHolder.textView_myorder_time.setText("预约时间："+Utils.ChangeTime(list.get(arg0).get("appointment").toString()));
					if(1==(Integer)list.get(arg0).get("serviceLoc")){
						mHolder.textView_myorder_addrtype.setText("服务方式：门店服务");
						if(1==(Integer)list.get(arg0).get("pickUp")){
							mHolder.textView_myorder_addrtype_des.setVisibility(View.VISIBLE);
							mHolder.textView_myorder_addrtype_des.setText("(需要接送)");
						}else if(0==(Integer)list.get(arg0).get("pickUp")){
							mHolder.textView_myorder_addrtype_des.setVisibility(View.VISIBLE);
							mHolder.textView_myorder_addrtype_des.setText("(不需要接送)");
						}else if (2==(Integer)list.get(arg0).get("pickUp")) {
							mHolder.textView_myorder_addrtype_des.setVisibility(View.GONE);
						}
					}else{
						mHolder.textView_myorder_addrtype_des.setVisibility(View.GONE);
						mHolder.textView_myorder_addrtype.setText("服务方式：上门服务");
					}
				}else if (list.get(arg0).get("type").equals(2)) {//寄养
					mHolder.textView_myorder_time.setText("入住时间："+Utils.ChangeTime(list.get(arg0).get("startTime").toString()));
					mHolder.textView_myorder_addrtype.setText("离店时间："+Utils.ChangeTime(list.get(arg0).get("stopTime").toString()));
					mHolder.textView_myorder_addrtype_des.setVisibility(View.GONE);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
	
	class ViewHolder {
		TextView textView_myorder_name;
		TextView textView_myorder_state;
		SelectableRoundedImageView imageView_myorder_animal;
		TextView textView_myorder_price;
		TextView textView_myorder_time;
		TextView textView_myorder_addrtype;
		TextView textView_myorder_addrtype_des;
		
//		---start 2015年12月22日15:59:29
		ImageView imageview_myorder_icon;
		TextView textView_myorder_name_show;
//		---end 2015年12月22日15:59:35
		
	}
}
