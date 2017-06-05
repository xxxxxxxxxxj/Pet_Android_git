package com.haotang.pet.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.haotang.pet.ChoosePetActivity;
import com.haotang.pet.FostercareChooseroomActivity;
import com.haotang.pet.R;
import com.haotang.pet.ServiceActivity;
import com.haotang.pet.adapter.HeaderViewAdapter;
import com.haotang.pet.adapter.MyPetAdapter;
import com.haotang.pet.adapter.SortAdapter;
import com.haotang.pet.entity.Pet;
import com.haotang.pet.entity.SortModel;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CharacterParser;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.PinyinComparator;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.ClearEditText;
import com.haotang.pet.view.HeadViewListView;
import com.haotang.pet.view.HorizontalListView;
import com.haotang.pet.view.SideBar;
import com.haotang.pet.view.SideBar.OnTouchingLetterChangedListener;
import com.umeng.analytics.MobclickAgent;

public class ChooseCatFragment extends Fragment {
	
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private int previous;
	private int serviceid;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList=null;
	private List<String> HeaderSourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private View header;
	private LinearLayout top;
	private TextView my_pet_show;
	private ImageView imageView_title_icon;
	private HeadViewListView listView_headerview ;
	private ChoosePetActivity petactivity;
	private LinearLayout layout_choose_cat;
	private LinearLayout all_choose;
	private FrameLayout layout_choose_show;
	int SerachHeadheight;
	int SerachHeadwidth;
	private boolean first=true;
	private SharedPreferenceUtil spUtil;
	static ChooseCatFragment chooseCatFragment=null;
	
//	--START 2016年1月19日15:59:18
	
	private HorizontalListView hlv_showmypet_list;
	private MyPetAdapter myPetAdapter;
	private ArrayList<Pet> mypetList = new ArrayList<Pet>();
	private LinearLayout item_mypet_show;
	private MProgressDialog pDialog;
//	--END 2016年1月19日15:59:25
	
	public static ChooseCatFragment getChooseCatFragment(){
		if (chooseCatFragment==null) {
			chooseCatFragment = new ChooseCatFragment();
		}
		return chooseCatFragment;
		
	} 
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		petactivity = (ChoosePetActivity) activity;
		spUtil = SharedPreferenceUtil.getInstance(activity);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_choose_dog, null);
		initViewAndData(view);
		setMyPetAdapter();
		setSortListOther(view);
		initListener();
		setDataFromServ();

		//根据输入框输入值的改变来过滤搜索

		return view;
	}


	private void setDataFromServ() {
		if(serviceid==1){
			if("".equals(spUtil.getString("bathcatlist", ""))){
				getData(3);
			}else{
				getJson(spUtil.getString("bathcatlist", ""),false);
				CommUtil.getListVersion(petactivity,listversionHandler);
			}
		}else if(serviceid==2){
			if("".equals(spUtil.getString("beautycatlist", ""))){
				getData(4);
			}else{
				getJson(spUtil.getString("beautycatlist", ""),false);
				CommUtil.getListVersion(petactivity,listversionHandler);
			}
		}else{
			getData(serviceid);
		}
		getMyPetData();
		
	}
	private void getMyPetData() {
		// TODO Auto-generated method stub
		String cellphone  = spUtil.getString("cellphone", "");
		if (cellphone.equals("")) {
			item_mypet_show.setVisibility(View.GONE);
		}else if(previous==Global.ADDPET_TO_PETLIST){
			item_mypet_show.setVisibility(View.GONE);
		}else {
			CommUtil.queryCustomerPets(spUtil.getString("cellphone", ""), 
					Global.getIMEI(getActivity()), getActivity(),
					serviceid,2,0,0,-1,queryCustomerPets);
			Utils.mLogError("==-->serviceid cat:= "+serviceid);
		}
	}
	//获取我的宠物列表
	private AsyncHttpResponseHandler queryCustomerPets = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			String str = new String(responseBody);
			getMyPet(str);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
	
	};

	
	private void setSortListOther(View view) {
		sideBar.setTextView(dialog);
		sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
		sortListView.addHeaderView(header);
	}
	
	private void setMyPetAdapter() {
		myPetAdapter = new MyPetAdapter(petactivity,2,mypetList);
		hlv_showmypet_list.setAdapter(myPetAdapter);
	}

	private void initListener() {
		hlv_showmypet_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Pet pet = (Pet) parent.getItemAtPosition(position);
				if (pet.sa==0) {
					ToastUtil.showToastShortCenter(petactivity, "该宝贝还不能享受此项服务呦");
				}else {
					if(previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY){
						Intent data = new Intent(petactivity, ServiceActivity.class);
						data.putExtra("petid", pet.id);
						data.putExtra("serviceid", serviceid);
						data.putExtra("serviceid_new", serviceid);
						data.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
						data.putExtra("petkind", pet.kindid);
						data.putExtra("petname", pet.name);
						data.putExtra("petimage", pet.image);
						data.putExtra("customerpetid", pet.customerpetid);
						petactivity.startActivity(data);
						petactivity.finish();
					}else if(previous == Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT){
						Intent data = new Intent(petactivity, FostercareChooseroomActivity.class);
						data.putExtra("petid", pet.id);
						data.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT);
						data.putExtra("petkind", pet.kindid);
						data.putExtra("petname", pet.name);
						data.putExtra("serviceid", serviceid);
						data.putExtra("serviceid_new", serviceid);
						data.putExtra("petimage", pet.image);
						data.putExtra("customerpetid", pet.customerpetid);
						petactivity.startActivity(data);
						petactivity.finish();
					}else{
						Intent data = new Intent();
						data.putExtra("petid", pet.id);
						data.putExtra("petkind", pet.kindid);
						data.putExtra("petname", pet.name);
						data.putExtra("serviceid", serviceid);
						data.putExtra("serviceid_new", serviceid);
						data.putExtra("petimage", pet.image);
						data.putExtra("customerpetid", pet.customerpetid);
						petactivity.setResult(Global.RESULT_OK, data);
						petactivity.finish();
					}
				}
			}
		});
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置

				try {
					if (s.equals("热门")) {// 处理热门分类
						int position = 0;
						sortListView.setSelection(position);
					} else {
						int position = adapter.getPositionForSection(s.charAt(0));
						if (position != -1) {
							sortListView.setSelection(position);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
				SortModel sm = (SortModel) adapter.getItem(position);
				if(previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY){
					Intent data = new Intent(petactivity, ServiceActivity.class);
					data.putExtra("petid", sm.getPetId());
					data.putExtra("serviceid", serviceid);
					data.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
					data.putExtra("petkind", sm.getPetKind());
					if(serviceid==1){
						data.putExtra("serviceid_new", 3);
					}else if(serviceid == 2){
						data.putExtra("serviceid_new", 4);
					}else{
						data.putExtra("serviceid_new", serviceid);
					}
					data.putExtra("petname", sm.getName());
					data.putExtra("petimage", CommUtil.getServiceNobacklash()+sm.getAvatarPath());
					petactivity.startActivity(data);
					petactivity.finish();
				}else if(previous == Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT){
					Intent data = new Intent(petactivity, FostercareChooseroomActivity.class);
					data.putExtra("petid", sm.getPetId());
					data.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_BOOKINGSERVICEACTIVITY);
					data.putExtra("petkind", sm.getPetKind());
					data.putExtra("serviceid", serviceid);
					data.putExtra("petname", sm.getName());
					if(serviceid==1){
						data.putExtra("serviceid_new", 3);
					}else if(serviceid == 2){
						data.putExtra("serviceid_new", 4);
					}else{
						data.putExtra("serviceid_new", serviceid);
					}
					data.putExtra("petimage", CommUtil.getServiceNobacklash()+sm.getAvatarPath());
					petactivity.startActivity(data);
					petactivity.finish();
				}else{
					Intent data = new Intent();
					data.putExtra("petid", sm.getPetId());
					data.putExtra("petkind", sm.getPetKind());
					data.putExtra("petname", sm.getName());
					data.putExtra("serviceid", serviceid);
					data.putExtra("petimage", CommUtil.getServiceNobacklash()+sm.getAvatarPath());
					if(serviceid==1){
						data.putExtra("serviceid_new", 3);
					}else if(serviceid == 2){
						data.putExtra("serviceid_new", 4);
					}else{
						data.putExtra("serviceid_new", serviceid);
					}
					petactivity.setResult(Global.RESULT_OK, data);
					petactivity.finish();
				}
			}
		});
		
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				try {
					if (first&&header!=null&&header.getLayoutParams()!=null) {
						first = false;
						SerachHeadheight = header.getLayoutParams().height;
						SerachHeadwidth = header.getLayoutParams().width;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (SourceDateList==null) {
					return;
				}
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		
		listView_headerview.setOnItemClickListener(new OnItemClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long arg3) {
				ArrayMap<String,Object> map = (ArrayMap<String, Object>) parent.getItemAtPosition(position);
					if(previous == Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY){
						Intent data = new Intent(petactivity, ServiceActivity.class);
						data.putExtra("serviceid", serviceid);
						data.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_SERVICEACTIVITY);
						data.putExtra("petid", (Integer)map.get("PetId"));
						data.putExtra("petkind", (Integer)map.get("petKind"));
						data.putExtra("petname", (String)map.get("petName"));
						data.putExtra("petimage", CommUtil.getServiceNobacklash()+(String)map.get("avatarPath"));
						if(serviceid==1){
							data.putExtra("serviceid_new", 3);
						}else if(serviceid == 2){
							data.putExtra("serviceid_new", 4);
						}else{
							data.putExtra("serviceid_new", serviceid);
						}
						petactivity.startActivity(data);
						petactivity.finish();
					}else if(previous == Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT){
						Intent data = new Intent(petactivity, FostercareChooseroomActivity.class);
						data.putExtra("serviceid", serviceid);
						data.putExtra("previous", Global.PRE_MAINFRAGMENT_TO_BOOKINGSERVICEACTIVITY);
						data.putExtra("petid", (Integer)map.get("PetId"));
						data.putExtra("petkind", (Integer)map.get("petKind"));
						data.putExtra("petname", (String)map.get("petName"));
						data.putExtra("petimage", CommUtil.getServiceNobacklash()+(String)map.get("avatarPath"));
						if(serviceid==1){
							data.putExtra("serviceid_new", 3);
						}else if(serviceid == 2){
							data.putExtra("serviceid_new", 4);
						}else{
							data.putExtra("serviceid_new", serviceid);
						}
						petactivity.startActivity(data);
						petactivity.finish();
					}else{
						Intent data = new Intent();
						data.putExtra("petid", (Integer)map.get("PetId"));
						data.putExtra("petkind", (Integer)map.get("petKind"));
						data.putExtra("petname", (String)map.get("petName"));
						data.putExtra("serviceid", serviceid);
						data.putExtra("petimage", CommUtil.getServiceNobacklash()+(String)map.get("avatarPath"));
						if(serviceid==1){
							data.putExtra("serviceid_new", 3);
						}else if(serviceid == 2){
							data.putExtra("serviceid_new", 4);
						}else{
							data.putExtra("serviceid_new", serviceid);
						}
						petactivity.setResult(Global.RESULT_OK, data);
						petactivity.finish();
					}
			}
		});
	}


	private void initViewAndData(View view) {
		mypetList.clear();
		pDialog = new MProgressDialog(petactivity);
		pDialog.showDialog();
		previous = petactivity.getIntent().getIntExtra("previous", -1);
		serviceid = petactivity.getIntent().getIntExtra("serviceid", -1);
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		
		header = LayoutInflater.from(getActivity()).inflate(R.layout.headerview,null);
		top = (LinearLayout) header.findViewById(R.id.top);
		my_pet_show = (TextView) header.findViewById(R.id.my_pet_show);
//		imageView_title_icon = (ImageView) header.findViewById(R.id.imageView_title_icon);
		layout_choose_cat = (LinearLayout) view.findViewById(R.id.layout_choose_cat);
		all_choose = (LinearLayout) view.findViewById(R.id.all_choose);
		layout_choose_show = (FrameLayout) view.findViewById(R.id.layout_choose_show);
		listView_headerview = (HeadViewListView) header.findViewById(R.id.listView_headerview);
		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);
		
		hlv_showmypet_list = (HorizontalListView) header.findViewById(R.id.hlv_showmypet_list);
		item_mypet_show = (LinearLayout) header.findViewById(R.id.item_mypet_show);//没有我的宠物是否需要判断
	}
	
	private void getData(int serviceid){
		//all
		if(previous==Global.PRE_MAINFRAGMENT_TO_BOOKINGSERVICEACTIVITY||
				previous==Global.PRE_MAINFRAGMENT_TO_FOSTERCAREAPPOINTMENT){
			CommUtil.getPetList(petactivity,2,0,0,0,-1,dogGetList);
		}else{
			CommUtil.getPetList(petactivity,2,serviceid,0,0,-1,dogGetList);
			
		}
	}
	private AsyncHttpResponseHandler listversionHandler = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			String st = new String(responseBody);
			Utils.mLogError("是否有更新："+st);
			try {
				JSONObject jobj = new JSONObject(st);
				int code = jobj.getInt("code");
				List<ArrayMap<String,Object>> forNet = new ArrayList<ArrayMap<String,Object>>();
				List<ArrayMap<String,Object>> forNetHot = new ArrayList<ArrayMap<String,Object>>();
				if(code == 0&&jobj.has("data")&&!jobj.isNull("data")){
					long timestamp = jobj.getLong("data");
					if(serviceid==1){
						if(spUtil.getLong("bathtimestampcat", 0)<timestamp){
							spUtil.saveLong("bathtimestampcat", timestamp);
							getData(3);
						}
					}else if(serviceid==2){
						if(spUtil.getLong("beautytimestampcat", 0)<timestamp){
							spUtil.saveLong("beautytimestampcat", timestamp);
							getData(4);
						}
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
			pDialog.closeDialog();
		}
		
	};
	
	//获取狗
	private AsyncHttpResponseHandler dogGetList = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			pDialog.closeDialog();
			String st = new String(responseBody);
			Utils.mLogError("获取猫列表:= "+st);
			
			getJson(st,true);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			pDialog.closeDialog();
		}
		
	};
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		try {
			List<SortModel> filterDateList = new ArrayList<SortModel>();
			  
			if(TextUtils.isEmpty(filterStr)){
				header.setVisibility(View.VISIBLE);
				setProductionHeight(top, SerachHeadwidth, SerachHeadheight);
				filterDateList = SourceDateList;
			}else{
				filterDateList.clear();
				header.setVisibility(View.INVISIBLE);
				int height = header.getLayoutParams().height;
				int width = header.getLayoutParams().width;
				setProductionHeight(top, width, -SerachHeadheight);
				for(SortModel sortModel : SourceDateList){
					String name = sortModel.getName();
					if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
						filterDateList.add(sortModel);
					}
				}
			}
			
			// 根据a-z进行排序
			Collections.sort(filterDateList, pinyinComparator);
			adapter.updateListView(filterDateList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
//			sortModel.setImgUrl(date[i]);//这个设置显示的头像
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	/**
	 * 为ListView填充数据
	 * @param list
	 * @return
	 */
	private List<SortModel> filledDataList(List<ArrayMap<String,Object>> list){
		
		
		List<SortModel> mSortList = null;
		try {
			mSortList = new ArrayList<SortModel>();
			
			for(int i=0; i<list.size(); i++){
				SortModel sortModel = new SortModel();
				sortModel.setName(list.get(i).get("petName").toString());
				sortModel.setAvatarPath(list.get(i).get("avatarPath").toString());
				sortModel.setPetId(Integer.parseInt(list.get(i).get("PetId").toString()));
				sortModel.setHot(Boolean.parseBoolean(list.get(i).get("isHot").toString()));
				sortModel.setPetKind((Integer)list.get(i).get("petKind"));
				sortModel.setPyhead(list.get(i).get("pyhead").toString());
				//汉字转换成拼音
				String pinyin = characterParser.getSelling(list.get(i).get("petName").toString());
				String sortString = pinyin.substring(0, 1).toUpperCase();
				
				// 正则表达式，判断首字母是否是英文字母
				if(sortString.matches("[A-Z]")){
					sortModel.setSortLetters(sortString.toUpperCase());
				}else{
					sortModel.setSortLetters("#");
				}
				mSortList.add(sortModel);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mSortList;
		
	}
	private void getMyPet(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			int code = jsonObject.getInt("code");
			if (code==0) {
				if(jsonObject.has("data")&&!jsonObject.isNull("data")){
					item_mypet_show.setVisibility(View.VISIBLE);
					JSONObject object = jsonObject.getJSONObject("data");
					if (object.has("pets")&&!object.isNull("pets")) {
						
						JSONArray array = object.getJSONArray("pets");
						for (int i = 0; i < array.length(); i++) {
							mypetList.add(Pet.json2Entity(array.getJSONObject(i)));
						}
					}
				}
				myPetAdapter.notifyDataSetChanged();
				if (mypetList.size()<=0) {
					item_mypet_show.setVisibility(View.GONE);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void getJson(String json,boolean saveflag){
		if(json==null||"".equals(json)||"null".equals(json))
			return;
		try {
			JSONObject jobj = new JSONObject(json);
			int code = jobj.getInt("code");
			if(0==code&&jobj.has("data")&&!jobj.isNull("data")){
				
				JSONObject jsonObject = jobj.getJSONObject("data");
				List<ArrayMap<String,Object>> forNet = new ArrayList<ArrayMap<String,Object>>();
				if(jsonObject.has("all")&&!jsonObject.isNull("all")){
					
					JSONArray jsonArray = jsonObject.getJSONArray("all");
					if (jsonArray.length()<=0) {
						all_choose.setGravity(Gravity.CENTER);
						mClearEditText.setVisibility(View.GONE);
						layout_choose_show.setVisibility(View.GONE);
						sortListView.setVisibility(View.GONE);
						dialog.setVisibility(View.GONE);
						sideBar.setVisibility(View.GONE);
						layout_choose_cat.setVisibility(View.VISIBLE);
						int WHLiner  []  = Utils.getDisplayMetrics(getActivity());
						setProductionHeight(layout_choose_cat, WHLiner[0], WHLiner[1]-Utils.dip2px(petactivity, 50));
						layout_choose_cat.setGravity(Gravity.CENTER);
						layout_choose_cat.setBackgroundColor(Color.parseColor("#F3F3EC"));
					}else{
						if(saveflag){
							if(serviceid==1){
								spUtil.saveString("bathcatlist", json);
							}else if(serviceid==2){
								spUtil.saveString("beautycatlist", json);
							}
						}
					}
					for (int i = 0; i < jsonArray.length(); i++) {
						ArrayMap<String, Object> map = new ArrayMap<String, Object>();
						JSONObject object = jsonArray.getJSONObject(i);
						if (object.has("avatarPath")&&!object.isNull("avatarPath")) {
							String avatarPath = object.getString("avatarPath");
							map.put("avatarPath", avatarPath);
						}
						if (object.has("PetId")&&!object.isNull("PetId")) {
							int PetId = object.getInt("PetId");
							map.put("PetId", PetId);
						}
						if (object.has("isHot")&&!object.isNull("isHot")) {
							boolean isHot = object.getBoolean("isHot");
							map.put("isHot", isHot);
						}
						if (object.has("petKind")&&!object.isNull("petKind")) {
							map.put("petKind", object.getInt("petKind"));
						}
						if (object.has("petName")&&!object.isNull("petName")) {
							String petName = object.getString("petName").trim();
							map.put("petName", petName);
						}
						if (object.has("pyhead")&&!object.isNull("pyhead")) {
							String pyhead = object.getString("pyhead");
							map.put("pyhead", pyhead);
						}
						forNet.add(map);
					}
				}
				if(jsonObject.has("hot")&&!jsonObject.isNull("hot")){
				JSONArray jsonArrayAll = jsonObject.getJSONArray("hot");
				List<ArrayMap<String,Object>> forNetHot = new ArrayList<ArrayMap<String,Object>>();
				for (int k = 0; k < jsonArrayAll.length(); k++) {
					ArrayMap<String, Object> map = new ArrayMap<String, Object>();
					JSONObject object = jsonArrayAll.getJSONObject(k);
					if (object.has("avatarPath")&&!object.isNull("avatarPath")) {
						String avatarPath = object.getString("avatarPath");
						map.put("avatarPath", avatarPath);
					}
					if (object.has("PetId")&&!object.isNull("PetId")) {
						int PetId = object.getInt("PetId");
						map.put("PetId", PetId);
					}
					if (object.has("isHot")&&!object.isNull("isHot")) {
						boolean isHot = object.getBoolean("isHot");
						map.put("isHot", isHot);
						
					}
					if (object.has("petKind")&&!object.isNull("petKind")) {
						map.put("petKind", object.getInt("petKind"));
						
					}
					if (object.has("petName")&&!object.isNull("petName")) {
						String petName = object.getString("petName").trim();
						map.put("petName", petName);
						
					}
					if (object.has("pyhead")&&!object.isNull("pyhead")) {
						String pyhead = object.getString("pyhead");
						map.put("pyhead", pyhead);
					}
					forNetHot.add(map);
				}
				
				listView_headerview.setAdapter(new HeaderViewAdapter(petactivity,2, forNetHot));
				setListHeight(listView_headerview);
			}else {
				//处理当热门宠物没有时问题
				my_pet_show.setVisibility(View.GONE);
				listView_headerview.setVisibility(View.GONE);
			}
				SourceDateList = filledDataList(forNet);
				// 根据a-z进行排序源数据
				if(SourceDateList!=null&&SourceDateList.size()>0){
					Collections.sort(SourceDateList, pinyinComparator);
					adapter = new SortAdapter(getActivity(),2, SourceDateList);
					sortListView.setAdapter(adapter);
					
				}
				
				
			}else{
				if(serviceid==1){
					spUtil.removeData("bathtimestampcat");
				}else if(serviceid == 2){
					spUtil.removeData("beautytimestampcat");
				}
				if(jobj.has("msg")&&!jobj.isNull("msg")){
					String msg = jobj.getString("msg");
					ToastUtil.showToastShortCenter(petactivity, msg);
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setListHeight(ListView listView) {
		ListAdapter adapter = listView.getAdapter();

		if (adapter == null)
			return;

		int listHeight = 0;
		// Log.i(TAG, "====adapter=====" + adapter.getCount());
		for (int i = 0; i < adapter.getCount(); i++) {

			View listItem = adapter.getView(i, null, listView);
			// View view=adapter.getView(i, convertView, listView);
			listItem.measure(0, 0);

			listHeight += listItem.getMeasuredHeight();
			/*
			 * Log.i("==========", "=======listview每一个item高度======" +
			 * listItem.getMeasuredHeight());
			 */
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = listHeight
				+ (listView.getDividerHeight() * ((listView.getCount() - 1)));
		/*
		 * Log.i("==========", "=======listview每一个item相间的高度======" +
		 * listView.getDividerHeight());
		 */
		// Log.i("==========", "=======listview总高度======" + params.height);

		listView.setLayoutParams(params);
	}

	private void setProductionHeight(LinearLayout iv, int width,int height){
		if(iv==null||iv.getLayoutParams()==null)
			return;
		LayoutParams lParams = iv.getLayoutParams();
		lParams.width = width;
		lParams.height = height;
		iv.setLayoutParams(lParams);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("ChooseCatFragment"); //统计页面
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("ChooseCatFragment");
//		mClearEditText.setText("");
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mClearEditText.setText("");
	}
}
