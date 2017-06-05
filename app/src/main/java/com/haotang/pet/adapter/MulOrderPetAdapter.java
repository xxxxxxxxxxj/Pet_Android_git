package com.haotang.pet.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.MulPetService;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

public class MulOrderPetAdapter<T> extends CommonAdapter<T> {

	private ArrayList<Integer> list;
	private int MWidth=0;
	private boolean isVie = false;
	public MulOrderPetAdapter(Activity mContext, List<T> mDatas) {
		super(mContext, mDatas);
		MWidth= Utils.getDisplayMetrics((Activity)mContext)[0];
	}
	
	public void setData(ArrayList<Integer> list){
		this.list=list;
	}
	public void setIsVie(boolean isVie){
		this.isVie = isVie;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MulPetService mps = (MulPetService) mDatas.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.mulorderpetitem, position);
		try {
			viewHolder.setBackgroundCircle(R.id.mul_petimg,mps.petimage, R.drawable.dog_icon_unnew);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(position==mDatas.size()-1){
			viewHolder.getView(R.id.vBottomLine).setVisibility(View.GONE);
		}else{
			viewHolder.getView(R.id.vBottomLine).setVisibility(View.VISIBLE);
		}	
		if (mps.petCustomerId>0) {
			viewHolder.setText(R.id.tv_mulorderpetitem_name, mps.petCustomerName.trim()+"专属"+mps.serviceName);
		}else {
			viewHolder.setText(R.id.tv_mulorderpetitem_name,mps.petName.trim()+mps.serviceName);
		}
		TextView textview_vie = viewHolder.getView(R.id.textview_vie);
		if (isVie) {
			textview_vie.setVisibility(View.VISIBLE);
			Drawable drawable = Utils.getDW("F9605C");
			textview_vie.setBackgroundDrawable(drawable);
		}else {
			textview_vie.setVisibility(View.GONE);
		}
		if (mps.serviceloc==1) {
			if (mps.serviceType==3) {
				viewHolder.setText(R.id.tv_mulorderpetitem_service_type, "服务方式：到店服务");
			}else if (mps.serviceType==1||mps.serviceType==2) {
				if (mps.basefee>0) {
					if (mps.addservicefee>0) {
						viewHolder.setText(R.id.tv_mulorderpetitem_service_type, "基础¥"+mps.basefee+" 单项¥"+mps.addservicefee);
					}else {
						viewHolder.setText(R.id.tv_mulorderpetitem_service_type, "基础¥"+mps.basefee);
					}
				}else {
					if (mps.addservicefee>0) {
						viewHolder.setText(R.id.tv_mulorderpetitem_service_type, "基础¥"+(mps.fee-mps.addservicefee)+" 单项¥"+mps.addservicefee);
					}else {
						viewHolder.setText(R.id.tv_mulorderpetitem_service_type, "基础¥"+(mps.fee-mps.addservicefee));
					}
				}
			}
		}else if (mps.serviceloc==2) {
			if (mps.serviceType==1||mps.serviceType==2) {
				if (mps.basefee>0) {
					if (mps.addservicefee>0) {
						viewHolder.setText(R.id.tv_mulorderpetitem_service_type, "基础¥"+mps.basefee+" 单项¥"+mps.addservicefee);
					}else {
						viewHolder.setText(R.id.tv_mulorderpetitem_service_type, "基础¥"+mps.basefee);
					}
				}else {
					if (mps.addservicefee>0) {
						viewHolder.setText(R.id.tv_mulorderpetitem_service_type, "基础¥"+(mps.fee-mps.addservicefee)+" 单项¥"+mps.addservicefee);
					}else {
						viewHolder.setText(R.id.tv_mulorderpetitem_service_type, "基础¥"+(mps.fee-mps.addservicefee));
					}
				}
			}else {
				viewHolder.setText(R.id.tv_mulorderpetitem_service_type, "服务方式：上门服务");
			}
		}
		viewHolder.setText(R.id.tv_mulorderpetitem_totalfee, Utils.formatDouble(mps.fee)+"");
		final TextView tv_mulorderpetitem_name = viewHolder.getView(R.id.tv_mulorderpetitem_name);
		int countLine = tv_mulorderpetitem_name.getLineCount();
		Utils.mLogError("==-->lineCount 0 "+countLine);//待优化界面不onresume无法准确计算line
		Paint paint= new Paint();
		float TextWidth = paint.measureText(tv_mulorderpetitem_name.getText().toString());
		Utils.mLogError("==-->TextWidth "+TextWidth+" position "+position);
		//目前以此种方式计算，不知道别的手机情况怎么样，等测试看看
		if (Utils.dip2px(mContext, TextWidth)+Utils.dip2px(mContext, 80)+Utils.dip2px(mContext, 5)+Utils.dip2px(mContext, 30)>(MWidth-250)) {
			countLine=2;
		}else {
			countLine=1;
		}
		if (countLine>1) {
			TextView textView = viewHolder.getView(R.id.tv_mulorderpetitem_name);
			RelativeLayout.LayoutParams tv = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			tv.rightMargin=Utils.dip2px(mContext, 30);
			tv.topMargin=Utils.dip2px(mContext, 10);
			tv.addRule(RelativeLayout.ALIGN_PARENT_TOP,R.id.mul_petimg);
			tv.addRule(RelativeLayout.RIGHT_OF, R.id.mul_petimg);
			textView.setLayoutParams(tv);
			
			LinearLayout layout = viewHolder.getView(R.id.linearLayout1);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lp.rightMargin=10;
			lp.addRule(RelativeLayout.BELOW, R.id.tv_mulorderpetitem_name);
			lp.addRule(RelativeLayout.ALIGN_LEFT,R.id.tv_mulorderpetitem_name);
			layout.setLayoutParams(lp);
		}else if(countLine==1){
			TextView textView = viewHolder.getView(R.id.tv_mulorderpetitem_name);
			RelativeLayout.LayoutParams tv = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			tv.rightMargin=Utils.dip2px(mContext, 30);
			tv.topMargin=Utils.dip2px(mContext, 15);
//			tv.addRule(RelativeLayout.RIGHT_OF,R.id.mul_petimg);
			tv.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			tv.addRule(RelativeLayout.RIGHT_OF, R.id.mul_petimg);
			textView.setLayoutParams(tv);
			
			
			LinearLayout layout = viewHolder.getView(R.id.linearLayout1);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lp.rightMargin=10;
			lp.topMargin=Utils.dip2px(mContext, 6);
			lp.addRule(RelativeLayout.BELOW, R.id.tv_mulorderpetitem_name);
			lp.addRule(RelativeLayout.ALIGN_LEFT,R.id.tv_mulorderpetitem_name);
			layout.setLayoutParams(lp);
		}
		return viewHolder.getConvertView();
	}
/*	private Context context;
	private ArrayList<MulPetService> list;
	private LayoutInflater mInflater;
	
	public MulOrderPetAdapter(Context context, ArrayList<MulPetService> list) {
		super();
		this.context = context;
		this.list = list;
		this.mInflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if(convertView==null){
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.mulorderpetitem, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_mulorderpetitem_name);
			holder.tvAddServiceFee = (TextView) convertView.findViewById(R.id.tv_mulorderpetitem_addservicefee);
			holder.tvAddServiceFeePre = (TextView) convertView.findViewById(R.id.tv_mulorderpetitem_addservicefeepre);
			holder.tvBaseFee = (TextView) convertView.findViewById(R.id.tv_mulorderpetitem_basefee);
			holder.tvBaseFeePre = (TextView) convertView.findViewById(R.id.tv_mulorderpetitem_basefeepre);
			holder.tvTotalFee = (TextView) convertView.findViewById(R.id.tv_mulorderpetitem_totalfee);
			holder.vBottomLine = convertView.findViewById(R.id.view_mulorderpetitem_bottomline);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		MulPetService mps = list.get(position);
		if(position==list.size()-1)
			holder.vBottomLine.setVisibility(View.GONE);
		else
			holder.vBottomLine.setVisibility(View.VISIBLE);
		if(mps.petCustomerId>0)
			holder.tvName.setText(mps.petCustomerName+"专属"+mps.serviceName);
		else
			holder.tvName.setText(mps.petName+mps.serviceName);
		holder.tvTotalFee.setText("¥"+Utils.formatDouble(mps.fee, 2));
		if(mps.addservicefee>0){
			holder.tvBaseFeePre.setVisibility(View.VISIBLE);
			holder.tvBaseFee.setVisibility(View.VISIBLE);
			holder.tvAddServiceFeePre.setVisibility(View.VISIBLE);
			holder.tvAddServiceFee.setVisibility(View.VISIBLE);
			holder.tvBaseFee.setText("¥"+Utils.formatDouble(mps.basefeewithbeautician, 2));
			holder.tvAddServiceFee.setText("¥"+Utils.formatDouble(mps.addservicefee, 2));
		}else{
			holder.tvAddServiceFee.setVisibility(View.GONE);
			holder.tvAddServiceFeePre.setVisibility(View.GONE);
			holder.tvBaseFee.setVisibility(View.GONE);
			holder.tvBaseFeePre.setVisibility(View.GONE);
			
		}
		
		return convertView;
	}
	
	class Holder{
		TextView tvName;
		TextView tvTotalFee;
		TextView tvBaseFee;
		TextView tvBaseFeePre;
		TextView tvAddServiceFee;
		TextView tvAddServiceFeePre;
		View vBottomLine;
	}
*/

	
}
