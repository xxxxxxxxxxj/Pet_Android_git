package com.haotang.pet.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.sax.EndTextElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.LLSInterface;
import com.haotang.base.SuperActivity;
import com.haotang.pet.R;
import com.haotang.pet.entity.MDate;
import com.haotang.pet.entity.MMonth;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.SectionedBaseAdapter;

public class FostercareDateAdapter extends SectionedBaseAdapter {
	private SuperActivity context;
	private LayoutInflater mInflater;
	private ArrayList<MMonth> list;
	private TextView tvHint;
	private String startdate;
	private String enddate;
	private int selectedmonthposition;
	
	private boolean hasdefaultselected;
	private int defaultselected1month;
	private int defaultselected2month;
	private int defaultselected2day;
	private int defaultselected1day;
	
	private TranslateAnimation transtoLeftAnim;
	private TranslateAnimation transtoRightAnim;
	
	public FostercareDateAdapter(SuperActivity context,ArrayList<MMonth> list,
			final TextView tvHint,int startmonth,int startday,int endmonth,int endday) {
		super();
		this.context = context;
		this.list = list;
		this.tvHint = tvHint;
		this.defaultselected1day=startday;
		this.defaultselected1month = startmonth;
		this.defaultselected2day=endday;
		this.defaultselected2month=endday;
		mInflater = LayoutInflater.from(context);
		transtoLeftAnim = (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.transtoleft);
		transtoRightAnim = (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.transtoright);
		transtoLeftAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				tvHint.startAnimation(transtoRightAnim);
			}
		});
	}
	
	public void setStartMonth(int startmonth){
		this.defaultselected1month = startmonth;
	}
	public void setStartDay(int startday){
		this.defaultselected1day = startday;
	}
	public void setEndMonth(int endmonth){
		this.defaultselected2month = endmonth;
	}
	public void setEndDay(int endday){
		this.defaultselected2day = endday;
	}

	@Override
	public Object getItem(int section, int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getCountForSection(int section) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public View getItemView(final int section, int position, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		BodyHolder bHolder = null;
		if(convertView==null){
			bHolder = new BodyHolder();
			
			convertView = mInflater.inflate(R.layout.fostercare_datebody, null);
			bHolder.rlLevel1_1 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level1_1);
			bHolder.tvLevel1_1_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_1_top);
			bHolder.tvLevel1_1_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_1_bottom);
			bHolder.rlLevel1_2 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level1_2);
			bHolder.tvLevel1_2_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_2_top);
			bHolder.tvLevel1_2_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_2_bottom);
			bHolder.rlLevel1_3 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level1_3);
			bHolder.tvLevel1_3_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_3_top);
			bHolder.tvLevel1_3_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_3_bottom);
			bHolder.rlLevel1_4 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level1_4);
			bHolder.tvLevel1_4_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_4_top);
			bHolder.tvLevel1_4_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_4_bottom);
			bHolder.rlLevel1_5 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level1_5);
			bHolder.tvLevel1_5_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_5_top);
			bHolder.tvLevel1_5_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_5_bottom);
			bHolder.rlLevel1_6 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level1_6);
			bHolder.tvLevel1_6_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_6_top);
			bHolder.tvLevel1_6_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_6_bottom);
			bHolder.rlLevel1_7 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level1_7);
			bHolder.tvLevel1_7_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_7_top);
			bHolder.tvLevel1_7_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level1_7_bottom);
			
			
			bHolder.rlLevel2_1 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level2_1);
			bHolder.tvLevel2_1_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_1_top);
			bHolder.tvLevel2_1_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_1_bottom);
			bHolder.rlLevel2_2 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level2_2);
			bHolder.tvLevel2_2_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_2_top);
			bHolder.tvLevel2_2_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_2_bottom);
			bHolder.rlLevel2_3 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level2_3);
			bHolder.tvLevel2_3_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_3_top);
			bHolder.tvLevel2_3_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_3_bottom);
			bHolder.rlLevel2_4 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level2_4);
			bHolder.tvLevel2_4_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_4_top);
			bHolder.tvLevel2_4_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_4_bottom);
			bHolder.rlLevel2_5 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level2_5);
			bHolder.tvLevel2_5_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_5_top);
			bHolder.tvLevel2_5_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_5_bottom);
			bHolder.rlLevel2_6 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level2_6);
			bHolder.tvLevel2_6_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_6_top);
			bHolder.tvLevel2_6_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_6_bottom);
			bHolder.rlLevel2_7 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level2_7);
			bHolder.tvLevel2_7_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_7_top);
			bHolder.tvLevel2_7_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level2_7_bottom);
			
			
			bHolder.rlLevel3_1 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level3_1);
			bHolder.tvLevel3_1_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_1_top);
			bHolder.tvLevel3_1_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_1_bottom);
			bHolder.rlLevel3_2 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level3_2);
			bHolder.tvLevel3_2_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_2_top);
			bHolder.tvLevel3_2_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_2_bottom);
			bHolder.rlLevel3_3 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level3_3);
			bHolder.tvLevel3_3_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_3_top);
			bHolder.tvLevel3_3_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_3_bottom);
			bHolder.rlLevel3_4 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level3_4);
			bHolder.tvLevel3_4_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_4_top);
			bHolder.tvLevel3_4_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_4_bottom);
			bHolder.rlLevel3_5 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level3_5);
			bHolder.tvLevel3_5_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_5_top);
			bHolder.tvLevel3_5_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_5_bottom);
			bHolder.rlLevel3_6 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level3_6);
			bHolder.tvLevel3_6_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_6_top);
			bHolder.tvLevel3_6_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_6_bottom);
			bHolder.rlLevel3_7 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level3_7);
			bHolder.tvLevel3_7_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_7_top);
			bHolder.tvLevel3_7_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level3_7_bottom);
			
			
			bHolder.rlLevel4_1 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level4_1);
			bHolder.tvLevel4_1_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_1_top);
			bHolder.tvLevel4_1_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_1_bottom);
			bHolder.rlLevel4_2 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level4_2);
			bHolder.tvLevel4_2_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_2_top);
			bHolder.tvLevel4_2_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_2_bottom);
			bHolder.rlLevel4_3 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level4_3);
			bHolder.tvLevel4_3_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_3_top);
			bHolder.tvLevel4_3_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_3_bottom);
			bHolder.rlLevel4_4 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level4_4);
			bHolder.tvLevel4_4_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_4_top);
			bHolder.tvLevel4_4_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_4_bottom);
			bHolder.rlLevel4_5 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level4_5);
			bHolder.tvLevel4_5_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_5_top);
			bHolder.tvLevel4_5_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_5_bottom);
			bHolder.rlLevel4_6 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level4_6);
			bHolder.tvLevel4_6_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_6_top);
			bHolder.tvLevel4_6_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_6_bottom);
			bHolder.rlLevel4_7 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level4_7);
			bHolder.tvLevel4_7_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_7_top);
			bHolder.tvLevel4_7_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level4_7_bottom);
			
			
			bHolder.rlLevel5_1 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level5_1);
			bHolder.tvLevel5_1_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_1_top);
			bHolder.tvLevel5_1_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_1_bottom);
			bHolder.rlLevel5_2 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level5_2);
			bHolder.tvLevel5_2_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_2_top);
			bHolder.tvLevel5_2_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_2_bottom);
			bHolder.rlLevel5_3 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level5_3);
			bHolder.tvLevel5_3_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_3_top);
			bHolder.tvLevel5_3_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_3_bottom);
			bHolder.rlLevel5_4 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level5_4);
			bHolder.tvLevel5_4_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_4_top);
			bHolder.tvLevel5_4_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_4_bottom);
			bHolder.rlLevel5_5 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level5_5);
			bHolder.tvLevel5_5_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_5_top);
			bHolder.tvLevel5_5_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_5_bottom);
			bHolder.rlLevel5_6 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level5_6);
			bHolder.tvLevel5_6_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_6_top);
			bHolder.tvLevel5_6_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_6_bottom);
			bHolder.rlLevel5_7 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level5_7);
			bHolder.tvLevel5_7_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_7_top);
			bHolder.tvLevel5_7_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level5_7_bottom);
			
			
			bHolder.rlLevel6_1 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level6_1);
			bHolder.tvLevel6_1_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_1_top);
			bHolder.tvLevel6_1_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_1_bottom);
			bHolder.rlLevel6_2 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level6_2);
			bHolder.tvLevel6_2_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_2_top);
			bHolder.tvLevel6_2_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_2_bottom);
			bHolder.rlLevel6_3 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level6_3);
			bHolder.tvLevel6_3_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_3_top);
			bHolder.tvLevel6_3_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_3_bottom);
			bHolder.rlLevel6_4 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level6_4);
			bHolder.tvLevel6_4_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_4_top);
			bHolder.tvLevel6_4_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_4_bottom);
			bHolder.rlLevel6_5 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level6_5);
			bHolder.tvLevel6_5_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_5_top);
			bHolder.tvLevel6_5_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_5_bottom);
			bHolder.rlLevel6_6 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level6_6);
			bHolder.tvLevel6_6_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_6_top);
			bHolder.tvLevel6_6_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_6_bottom);
			bHolder.rlLevel6_7 = (RelativeLayout) convertView.findViewById(R.id.rl_fostercaredatebody_level6_7);
			bHolder.tvLevel6_7_top = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_7_top);
			bHolder.tvLevel6_7_bottom = (TextView) convertView.findViewById(R.id.tv_fostercaredatebody_level6_7_bottom);
			
			bHolder.llLastLevel = (LinearLayout) convertView.findViewById(R.id.ll_fostercaredatebody_last);
			bHolder.llLevel2 = (LinearLayout) convertView.findViewById(R.id.ll_fostercaredatebody_2);
			bHolder.llLevel3 = (LinearLayout) convertView.findViewById(R.id.ll_fostercaredatebody_3);
			bHolder.llLevel4 = (LinearLayout) convertView.findViewById(R.id.ll_fostercaredatebody_4);
			bHolder.llLevel5 = (LinearLayout) convertView.findViewById(R.id.ll_fostercaredatebody_5);
			
			convertView.setTag(bHolder);
		}else{
			bHolder = (BodyHolder) convertView.getTag();
		}
		
		final BodyHolder holder = bHolder;
		
		final ArrayList<MDate> mdlist = list.get(section).daylist;
		
		bHolder.llLastLevel.setVisibility(View.VISIBLE);
		bHolder.llLevel5.setVisibility(View.VISIBLE);
		bHolder.llLevel4.setVisibility(View.VISIBLE);
		bHolder.llLevel3.setVisibility(View.VISIBLE);
		bHolder.llLevel2.setVisibility(View.VISIBLE);
		
		if(mdlist.size()<=7){
			bHolder.llLastLevel.setVisibility(View.GONE);
			bHolder.llLevel5.setVisibility(View.GONE);
			bHolder.llLevel4.setVisibility(View.GONE);
			bHolder.llLevel3.setVisibility(View.GONE);
			bHolder.llLevel2.setVisibility(View.GONE);
		}else if(mdlist.size()<=14){
			bHolder.llLastLevel.setVisibility(View.GONE);
			bHolder.llLevel5.setVisibility(View.GONE);
			bHolder.llLevel4.setVisibility(View.GONE);
			bHolder.llLevel3.setVisibility(View.GONE);
		}else if(mdlist.size()<=21){
			bHolder.llLastLevel.setVisibility(View.GONE);
			bHolder.llLevel5.setVisibility(View.GONE);
			bHolder.llLevel4.setVisibility(View.GONE);
		}else if(mdlist.size()<=28){
			bHolder.llLastLevel.setVisibility(View.GONE);
			bHolder.llLevel5.setVisibility(View.GONE);
		}else if(mdlist.size()<=35){
			bHolder.llLastLevel.setVisibility(View.GONE);
		}
		
		
		for(int i=0;i<42;i++){
			String day=null;
			String holiday=null;
			boolean valid=false;
			boolean isselected=false;;
			boolean isselectedmiddle=false;;
			int week=0;
			int selectedindex=-1;
			if(i<mdlist.size()){
				day = mdlist.get(i).day;
				holiday = mdlist.get(i).holiday;
				valid = mdlist.get(i).valid;
				isselected = mdlist.get(i).isselected;
				isselectedmiddle = mdlist.get(i).isselectedmiddle;
				week = mdlist.get(i).week;
				selectedindex = mdlist.get(i).selectedindex;
			}
			switch (i) {
			case 0:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel1_1_top, holder.tvLevel1_1_bottom);
				break;
			case 1:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel1_2_top, holder.tvLevel1_2_bottom);
				break;
			case 2:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel1_3_top, holder.tvLevel1_3_bottom);
				break;
			case 3:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel1_4_top, holder.tvLevel1_4_bottom);
				break;
			case 4:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel1_5_top, holder.tvLevel1_5_bottom);
				break;
			case 5:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel1_6_top, holder.tvLevel1_6_bottom);
				break;
			case 6:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel1_7_top, holder.tvLevel1_7_bottom);
				break;
			case 7:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel2_1_top, holder.tvLevel2_1_bottom);
				break;
			case 8:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel2_2_top, holder.tvLevel2_2_bottom);
				break;
			case 9:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel2_3_top, holder.tvLevel2_3_bottom);
				break;
			case 10:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel2_4_top, holder.tvLevel2_4_bottom);
				break;
			case 11:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel2_5_top, holder.tvLevel2_5_bottom);
				break;
			case 12:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel2_6_top, holder.tvLevel2_6_bottom);
				break;
			case 13:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel2_7_top, holder.tvLevel2_7_bottom);
				break;
			case 14:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel3_1_top, holder.tvLevel3_1_bottom);
				break;
			case 15:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel3_2_top, holder.tvLevel3_2_bottom);
				break;
			case 16:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel3_3_top, holder.tvLevel3_3_bottom);
				break;
			case 17:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel3_4_top, holder.tvLevel3_4_bottom);
				break;
			case 18:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel3_5_top, holder.tvLevel3_5_bottom);
				break;
			case 19:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel3_6_top, holder.tvLevel3_6_bottom);
				break;
			case 20:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel3_7_top, holder.tvLevel3_7_bottom);
				break;
			case 21:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel4_1_top, holder.tvLevel4_1_bottom);
				break;
			case 22:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel4_2_top, holder.tvLevel4_2_bottom);
				break;
			case 23:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel4_3_top, holder.tvLevel4_3_bottom);
				break;
			case 24:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel4_4_top, holder.tvLevel4_4_bottom);
				break;
			case 25:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel4_5_top, holder.tvLevel4_5_bottom);
				break;
			case 26:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel4_6_top, holder.tvLevel4_6_bottom);
				break;
			case 27:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel4_7_top, holder.tvLevel4_7_bottom);
				break;
			case 28:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel5_1_top, holder.tvLevel5_1_bottom);
				break;
			case 29:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel5_2_top, holder.tvLevel5_2_bottom);
				break;
			case 30:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel5_3_top, holder.tvLevel5_3_bottom);
				break;
			case 31:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel5_4_top, holder.tvLevel5_4_bottom);
				break;
			case 32:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel5_5_top, holder.tvLevel5_5_bottom);
				break;
			case 33:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel5_6_top, holder.tvLevel5_6_bottom);
				break;
			case 34:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel5_7_top, holder.tvLevel5_7_bottom);
				break;
			case 35:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel6_1_top, holder.tvLevel6_1_bottom);
				break;
			case 36:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel6_2_top, holder.tvLevel6_2_bottom);
				break;
			case 37:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel6_3_top, holder.tvLevel6_3_bottom);
				break;
			case 38:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel6_4_top, holder.tvLevel6_4_bottom);
				break;
			case 39:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel6_5_top, holder.tvLevel6_5_bottom);
				break;
			case 40:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel6_6_top, holder.tvLevel6_6_bottom);
				break;
			case 41:
				setText(day, holiday,valid, isselected,week,isselectedmiddle,selectedindex, holder.tvLevel6_7_top, holder.tvLevel6_7_bottom);
				break;
			}
			
		}
		
		
		bHolder.rlLevel1_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				setclick(mdlist, 0,section);
			}
		});
		bHolder.rlLevel1_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 1,section);
			}
		});
		bHolder.rlLevel1_3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 2, section);
			}
		});
		bHolder.rlLevel1_4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 3,section);
			}
		});
		bHolder.rlLevel1_5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 4,section);
			}
		});
		bHolder.rlLevel1_6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 5,section);
			}
		});
		bHolder.rlLevel1_7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 6,section);
			}
		});
		
		bHolder.rlLevel2_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				setclick(mdlist, 7,section);
			}
		});
		bHolder.rlLevel2_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 8,section);
			}
		});
		bHolder.rlLevel2_3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 9, section);
			}
		});
		bHolder.rlLevel2_4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 10,section);
			}
		});
		bHolder.rlLevel2_5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 11,section);
			}
		});
		bHolder.rlLevel2_6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 12,section);
			}
		});
		bHolder.rlLevel2_7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 13,section);
			}
		});
		
		bHolder.rlLevel3_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				setclick(mdlist, 14,section);
			}
		});
		bHolder.rlLevel3_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 15,section);
			}
		});
		bHolder.rlLevel3_3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 16, section);
			}
		});
		bHolder.rlLevel3_4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 17,section);
			}
		});
		bHolder.rlLevel3_5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 18,section);
			}
		});
		bHolder.rlLevel3_6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 19,section);
			}
		});
		bHolder.rlLevel3_7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 20,section);
			}
		});
		
		
		bHolder.rlLevel4_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				setclick(mdlist, 21,section);
			}
		});
		bHolder.rlLevel4_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 22,section);
			}
		});
		bHolder.rlLevel4_3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 23, section);
			}
		});
		bHolder.rlLevel4_4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 24,section);
			}
		});
		bHolder.rlLevel4_5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 25,section);
			}
		});
		bHolder.rlLevel4_6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 26,section);
			}
		});
		bHolder.rlLevel4_7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 27,section);
			}
		});
		
		bHolder.rlLevel5_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				setclick(mdlist, 28,section);
			}
		});
		bHolder.rlLevel5_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 29,section);
			}
		});
		bHolder.rlLevel5_3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 30, section);
			}
		});
		bHolder.rlLevel5_4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 31,section);
			}
		});
		bHolder.rlLevel5_5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 32,section);
			}
		});
		bHolder.rlLevel5_6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 33,section);
			}
		});
		bHolder.rlLevel5_7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 34,section);
			}
		});
		
		bHolder.rlLevel6_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				setclick(mdlist, 35,section);
			}
		});
		bHolder.rlLevel6_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 36,section);
			}
		});
		bHolder.rlLevel6_3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 37, section);
			}
		});
		bHolder.rlLevel6_4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 38,section);
			}
		});
		bHolder.rlLevel6_5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 39,section);
			}
		});
		bHolder.rlLevel6_6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 40,section);
			}
		});
		bHolder.rlLevel6_7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setclick(mdlist, 41,section);
			}
		});
		
		
		return convertView;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		HeaderHolder hHolder = null;
		if(convertView==null){
			hHolder = new HeaderHolder();
			convertView = mInflater.inflate(R.layout.fostercare_dateheader, null);
			hHolder.tvHeader = (TextView) convertView.findViewById(R.id.tv_fostercaredate_header);
			
			convertView.setTag(hHolder);
		}else{
			hHolder = (HeaderHolder) convertView.getTag();
		}
		hHolder.tvHeader.setText(list.get(section).yearandmonth);
		return convertView;
	}
	
	private void setText(String str1,String str2,boolean valid,
			boolean isselected,int week,boolean isselectmiddle,
			int selectedflag,TextView tv,TextView tv2){
		if(str1!=null&&!"".equals(str1)){
			tv.setText(str1);
			tv.setVisibility(View.VISIBLE);
		}else{
			tv.setVisibility(View.INVISIBLE);
		}
		if(str2!=null&&!"".equals(str2))
			tv.setText(str2);
		
		
		if(!valid){
			tv.setTextColor(context.getResources().getColor(R.color.ac7cbcb));
		}else{
			if(week%7==0||week%7==6)
				tv.setTextColor(context.getResources().getColor(R.color.red));
			else
				tv.setTextColor(context.getResources().getColor(R.color.a222222));
		}
		if(isselectmiddle)
			tv.setTextColor(context.getResources().getColor(R.color.aD1494F));
		if(isselected){
			tv.setBackgroundResource(R.drawable.bg_orange_circle);
			tv.setTextColor(context.getResources().getColor(R.color.white));
			tv.setText(str1);
			tv2.setVisibility(View.VISIBLE);
			if(selectedflag==1){
				tv2.setText("入住");
			}else if(selectedflag==2){
				tv2.setText("离店");
			}

		}else{
			tv.setBackgroundResource(R.drawable.bg_white_circle);
			tv2.setVisibility(View.INVISIBLE);
		}
			
	}
	

	
	private void setclick(ArrayList<MDate> mdlist,int index,int section){
		if(index>=mdlist.size())
			return;
		if(mdlist.get(index).day!=null&&!"".equals(mdlist.get(index).day)
				&&mdlist.get(index).valid){
			if(startdate!=null&&!"".equals(startdate)&&
					(getTimeInMills(startdate)>=getTimeInMills(mdlist.get(index).date))){
				
				selectedmonthposition = section;
				startdate = mdlist.get(index).date;
				list.get(defaultselected1month).daylist.get(defaultselected1day).isselected=false;
				list.get(defaultselected1month).daylist.get(defaultselected1day).selectedindex=-1;
				list.get(defaultselected1month).daylist.get(defaultselected1day).isselectedmiddle=false;
				defaultselected1month = section;
				defaultselected1day=index;
				list.get(section).daylist.get(index).isselected=true;
				list.get(section).daylist.get(index).selectedindex=1;
				notifyDataSetChanged();
				return;
			}
			if(hasdefaultselected){
				enddate = mdlist.get(index).date;
				
				defaultselected2month = section;
				defaultselected2day=index;
				if(defaultselected1month==defaultselected2month){
					for(int i=defaultselected1day;i<=defaultselected2day;i++){
						list.get(defaultselected1month).daylist.get(i).isselectedmiddle=true;
					}
				}else if(defaultselected1month<defaultselected2month){
					for(int i=defaultselected1day;i<list.get(defaultselected1month).daylist.size();i++){
						list.get(defaultselected1month).daylist.get(i).isselectedmiddle=true;
					}
					for(int i=0;i<=defaultselected2day;i++){
						list.get(defaultselected2month).daylist.get(i).isselectedmiddle=true;
					}
					for(int i=defaultselected1month+1;i<defaultselected2month;i++){
						for(int j=0;j<list.get(i).daylist.size();j++){
							list.get(i).daylist.get(j).isselectedmiddle=true;
						}
					}
					
				}
				
				list.get(section).daylist.get(index).isselected=true;
				list.get(section).daylist.get(index).selectedindex=2;
				notifyDataSetChanged();
				
				Intent data = new Intent();
				data.putExtra("startdate", startdate);
				data.putExtra("enddate", enddate);
				data.putExtra("monthposition", selectedmonthposition);
				context.setResult(Global.RESULT_OK, data);
				context.finishWithAnimation();
			}else{
				selectedmonthposition = section;
				startdate = mdlist.get(index).date;
				tvHint.startAnimation(transtoLeftAnim);
				tvHint.setText("请选择离店时间");
				for(int i=defaultselected1month;i<=defaultselected2month;i++){
					for(int j=0;j<list.get(i).daylist.size();j++){
						list.get(i).daylist.get(j).isselectedmiddle=false;
						list.get(i).daylist.get(j).isselected=false;
						list.get(i).daylist.get(j).selectedindex=-1;
					}
				}
				
				
				defaultselected1month = section;
				defaultselected1day=index;
				list.get(section).daylist.get(index).isselected=true;
				list.get(section).daylist.get(index).selectedindex=1;
				notifyDataSetChanged();
				
			}
			hasdefaultselected = !hasdefaultselected;
		}
	}
	
	private long getTimeInMills(String date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			return calendar.getTimeInMillis();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	class HeaderHolder{
		TextView tvHeader;
	}
	class BodyHolder{
		RelativeLayout rlLevel1_1;
		TextView tvLevel1_1_top;
		TextView tvLevel1_1_bottom;
		RelativeLayout rlLevel1_2;
		TextView tvLevel1_2_top;
		TextView tvLevel1_2_bottom;
		RelativeLayout rlLevel1_3;
		TextView tvLevel1_3_top;
		TextView tvLevel1_3_bottom;
		RelativeLayout rlLevel1_4;
		TextView tvLevel1_4_top;
		TextView tvLevel1_4_bottom;
		RelativeLayout rlLevel1_5;
		TextView tvLevel1_5_top;
		TextView tvLevel1_5_bottom;
		RelativeLayout rlLevel1_6;
		TextView tvLevel1_6_top;
		TextView tvLevel1_6_bottom;
		RelativeLayout rlLevel1_7;
		TextView tvLevel1_7_top;
		TextView tvLevel1_7_bottom;
		
		RelativeLayout rlLevel2_1;
		TextView tvLevel2_1_top;
		TextView tvLevel2_1_bottom;
		RelativeLayout rlLevel2_2;
		TextView tvLevel2_2_top;
		TextView tvLevel2_2_bottom;
		RelativeLayout rlLevel2_3;
		TextView tvLevel2_3_top;
		TextView tvLevel2_3_bottom;
		RelativeLayout rlLevel2_4;
		TextView tvLevel2_4_top;
		TextView tvLevel2_4_bottom;
		RelativeLayout rlLevel2_5;
		TextView tvLevel2_5_top;
		TextView tvLevel2_5_bottom;
		RelativeLayout rlLevel2_6;
		TextView tvLevel2_6_top;
		TextView tvLevel2_6_bottom;
		RelativeLayout rlLevel2_7;
		TextView tvLevel2_7_top;
		TextView tvLevel2_7_bottom;
		
		RelativeLayout rlLevel3_1;
		TextView tvLevel3_1_top;
		TextView tvLevel3_1_bottom;
		RelativeLayout rlLevel3_2;
		TextView tvLevel3_2_top;
		TextView tvLevel3_2_bottom;
		RelativeLayout rlLevel3_3;
		TextView tvLevel3_3_top;
		TextView tvLevel3_3_bottom;
		RelativeLayout rlLevel3_4;
		TextView tvLevel3_4_top;
		TextView tvLevel3_4_bottom;
		RelativeLayout rlLevel3_5;
		TextView tvLevel3_5_top;
		TextView tvLevel3_5_bottom;
		RelativeLayout rlLevel3_6;
		TextView tvLevel3_6_top;
		TextView tvLevel3_6_bottom;
		RelativeLayout rlLevel3_7;
		TextView tvLevel3_7_top;
		TextView tvLevel3_7_bottom;
		
		RelativeLayout rlLevel4_1;
		TextView tvLevel4_1_top;
		TextView tvLevel4_1_bottom;
		RelativeLayout rlLevel4_2;
		TextView tvLevel4_2_top;
		TextView tvLevel4_2_bottom;
		RelativeLayout rlLevel4_3;
		TextView tvLevel4_3_top;
		TextView tvLevel4_3_bottom;
		RelativeLayout rlLevel4_4;
		TextView tvLevel4_4_top;
		TextView tvLevel4_4_bottom;
		RelativeLayout rlLevel4_5;
		TextView tvLevel4_5_top;
		TextView tvLevel4_5_bottom;
		RelativeLayout rlLevel4_6;
		TextView tvLevel4_6_top;
		TextView tvLevel4_6_bottom;
		RelativeLayout rlLevel4_7;
		TextView tvLevel4_7_top;
		TextView tvLevel4_7_bottom;
		
		RelativeLayout rlLevel5_1;
		TextView tvLevel5_1_top;
		TextView tvLevel5_1_bottom;
		RelativeLayout rlLevel5_2;
		TextView tvLevel5_2_top;
		TextView tvLevel5_2_bottom;
		RelativeLayout rlLevel5_3;
		TextView tvLevel5_3_top;
		TextView tvLevel5_3_bottom;
		RelativeLayout rlLevel5_4;
		TextView tvLevel5_4_top;
		TextView tvLevel5_4_bottom;
		RelativeLayout rlLevel5_5;
		TextView tvLevel5_5_top;
		TextView tvLevel5_5_bottom;
		RelativeLayout rlLevel5_6;
		TextView tvLevel5_6_top;
		TextView tvLevel5_6_bottom;
		RelativeLayout rlLevel5_7;
		TextView tvLevel5_7_top;
		TextView tvLevel5_7_bottom;
		
		RelativeLayout rlLevel6_1;
		TextView tvLevel6_1_top;
		TextView tvLevel6_1_bottom;
		RelativeLayout rlLevel6_2;
		TextView tvLevel6_2_top;
		TextView tvLevel6_2_bottom;
		RelativeLayout rlLevel6_3;
		TextView tvLevel6_3_top;
		TextView tvLevel6_3_bottom;
		RelativeLayout rlLevel6_4;
		TextView tvLevel6_4_top;
		TextView tvLevel6_4_bottom;
		RelativeLayout rlLevel6_5;
		TextView tvLevel6_5_top;
		TextView tvLevel6_5_bottom;
		RelativeLayout rlLevel6_6;
		TextView tvLevel6_6_top;
		TextView tvLevel6_6_bottom;
		RelativeLayout rlLevel6_7;
		TextView tvLevel6_7_top;
		TextView tvLevel6_7_bottom;
		
		LinearLayout llLastLevel;
		LinearLayout llLevel2;
		LinearLayout llLevel3;
		LinearLayout llLevel4;
		LinearLayout llLevel5;
		
	}
	

	
	

}
