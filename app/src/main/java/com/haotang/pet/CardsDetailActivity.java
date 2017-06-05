package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.CardMakeDogAdapter;
import com.haotang.pet.adapter.JoinWorkAdapter;
import com.haotang.pet.adapter.MyCardsEveryItemAdapter;
import com.haotang.pet.entity.CardMakeDog;
import com.haotang.pet.entity.CardsBuy;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MListview;
import com.yixia.weibo.sdk.util.DeviceUtils;
/**
 * 次卡详情
 * @author Administrator
 *
 */
public class CardsDetailActivity extends SuperActivity implements OnClickListener{

	public static CardsDetailActivity detailActivity;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private RelativeLayout layout_top_set_back;
	private ImageView imageView_left;
	private TextView textView_cards_name;
	private LinearLayout layout_wash;
	private TextView textview_wash_num;
	private LinearLayout layout_beau;
	private TextView textview_beau_num;
	private TextView textView_cards_detail;
	private TextView textview_cards_dog_name;
	private TextView textview_cards_changepet;
	private TextView textView_cards_activity_notice;
	private TextView textView_cards_cannot_support;
	private TextView textView_cards_cannot_support_make;
	private TextView textview_first_make_dog;
	private TextView textview_first_make_dog_price;
	private ImageView imageview_first_make_dog_card;
	private TextView textview_everyyear_test_dogcard;
	private TextView textview_everyyear_test_dog_price;
	private ImageView imageview_everyyear_test_dog_card;
	private MListview listView_choose_cards_every;
	private MListview choose_make_dog_cards;
	private JoinWorkAdapter joinWorkAdapter;
	
	private ArrayList<CardMakeDog> cardMakeDogs = new ArrayList<CardMakeDog>();
	private CardMakeDogAdapter<CardMakeDog> cardMakeDogAdapter;
	
	private ArrayList<CardsBuy> cardsBuys = new ArrayList<CardsBuy>();
	private MyCardsEveryItemAdapter<CardsBuy> cardsEveryItemAdapter;
	private int id;
	private CardMakeDog cardMakeDog = null;
	private String titleName=null;
	private String petName;
	private int petid;
	private int serviceid;
	private int customerpetid;
	private int servicetype;
	private String customerpetname;
//	private String petname;
	private int petkind;
	
	private ImageView imageview_show_back_img;
	private MListview cards_scaling_image;
	private ArrayList<String> Imagelist = new ArrayList<String>();
	private String notSupportCertiTip = null;
	private String notSupportCardTip = null;
	private int isCerti=-1;
	private String CardName="";
	private int previous = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cards_detail);
		detailActivity = this;
		MApplication.listAppoint.add(detailActivity);
		initView();
		setView();
		initListener();
	}
	private void initListener() {
		choose_make_dog_cards.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cardMakeDog = (CardMakeDog) parent.getItemAtPosition(position);
				if (cardMakeDog.isCerti==0) {
//					ToastUtil.showToastShortCenter(mContext, "不支持办狗证");
				}else {
					if (cardMakeDog.isChoose) {
						cardMakeDog.isChoose = !cardMakeDog.isChoose;
						cardMakeDogs.set(position,cardMakeDog);
					}else {
						for (int i = 0; i < cardMakeDogs.size(); i++) {
							CardMakeDog cardMakeDogReset =  (CardMakeDog) cardMakeDogs.get(i);
							cardMakeDogReset.isChoose = false;
							cardMakeDogs.set(i,cardMakeDogReset);
						}
						cardMakeDog.isChoose = !cardMakeDog.isChoose;
						cardMakeDogs.set(position,cardMakeDog);
					}
					cardMakeDogAdapter.setChooseState(position);
					cardsEveryItemAdapter.setCutDownPrice(cardMakeDog);
					cardMakeDogAdapter.notifyDataSetChanged();
				}
			}
		});
		listView_choose_cards_every.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CardsBuy cardsBuyEvery = (CardsBuy) parent.getItemAtPosition(position);
				if (Utils.checkLogin(detailActivity)) {
					goNext(cardsBuyEvery, OrderPayActivity.class,Global.CARDSDETAIL_TO_ORDERPAY);
				}else {
					goNext(cardsBuyEvery,LoginActivity.class, Global.CARDSDETAIL_TO_ORDERPAY);
				}
			}
			
		});
	}
	private void setView() {
		
		previous = getIntent().getIntExtra("previous", 0);
		
		tv_titlebar_title.setText("服务卡");
		cardsEveryItemAdapter = new MyCardsEveryItemAdapter<CardsBuy>(mContext, cardsBuys);
		listView_choose_cards_every.setAdapter(cardsEveryItemAdapter);
		
		cardMakeDogAdapter = new CardMakeDogAdapter<CardMakeDog>(mContext, cardMakeDogs);
		choose_make_dog_cards.setAdapter(cardMakeDogAdapter);
		
		if (!TextUtils.isEmpty(spUtil.getString("petname", ""))) {
			id = getIntent().getIntExtra("id", 3);//弄个默认的。//这个没宠物时候需要带到宠物列表 再带到这里
			petid = spUtil.getInt("petid", 0);
			petName = spUtil.getString("petname", "");
			isCerti = spUtil.getInt("isCerti", -1);
			if (isCerti==0) {
				textView_cards_cannot_support.setVisibility(View.VISIBLE);
			}else if (isCerti==1) {
				textView_cards_cannot_support.setVisibility(View.GONE);
			}
		}else {
			id = getIntent().getIntExtra("id", 3);//弄个默认的。//这个没宠物时候需要带到宠物列表 再带到这里
			petid = getIntent().getIntExtra("petid", 0);//弄个默认值
			petName = getIntent().getStringExtra("petname");//弄个默认值
			isCerti = getIntent().getIntExtra("isCerti", -1);
			if (isCerti==0) {
				textView_cards_cannot_support.setVisibility(View.VISIBLE);
			}else if (isCerti==1) {
				textView_cards_cannot_support.setVisibility(View.GONE);
			}
		}
		textview_cards_dog_name.setText(petName);
		
		
		joinWorkAdapter = new JoinWorkAdapter<String>(this,Imagelist);
		cards_scaling_image.setAdapter(joinWorkAdapter);
		cards_scaling_image.setFocusable(false);
		getData(id,petid);
	}
	private void getData(int id,int petid) {
		CommUtil.cardDetail(mContext,id,petid, cardDetail);
	}
	private AsyncHttpResponseHandler cardDetail = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			// TODO Auto-generated method stub
			try {
				JSONObject object = new JSONObject(new String(responseBody));
				int code = object.getInt("code");
				if (code==0) {
					if (object.has("data")&&!object.isNull("data")) {
						JSONObject objectData = object.getJSONObject("data");
						if (objectData.has("card")&&!objectData.isNull("card")) {
							JSONObject objectCard = objectData.getJSONObject("card");
							if (objectCard.has("petCardTip")&&!objectCard.isNull("petCardTip")) {
								String petCardTip = objectCard.getString("petCardTip");
								textView_cards_activity_notice.setText(petCardTip.trim());
							}else {
								textView_cards_activity_notice.setVisibility(View.GONE);
							}
							if (objectCard.has("name")&&!objectCard.isNull("name")) {
								CardName = objectCard.getString("name");
//								textView_cards_cannot_support.setText(notSupportCertiTip);
								textview_cards_dog_name.setText(petName+CardName);
							}
							if (objectCard.has("notSupportCertiTip")&&!objectCard.isNull("notSupportCertiTip")) {
								notSupportCertiTip = objectCard.getString("notSupportCertiTip");
								textView_cards_cannot_support.setText(notSupportCertiTip);
							}
							if (objectCard.has("notSupportCardTip")&&!objectCard.isNull("notSupportCardTip")) {
								notSupportCardTip = objectCard.getString("notSupportCardTip");
								textView_cards_cannot_support_make.setText(notSupportCardTip);
							}
							double petCardDiscount = 0;
							String discount="";
							if (objectCard.has("petCardDiscount")&&!objectCard.isNull("petCardDiscount")) {
								petCardDiscount = objectCard.getDouble("petCardDiscount");
							}
							if (objectCard.has("discount")&&!objectCard.isNull("discount")) {
								discount = objectCard.getString("discount");
							}
							if (objectCard.has("cardImg")&&!objectCard.isNull("cardImg")) {
								String cardImg = CommUtil.getServiceBaseUrl()+objectCard.getString("cardImg");
								ImageLoaderUtil.setImage(imageview_show_back_img, cardImg, 0);
							}
							cardsBuys.clear();
							cardMakeDogs.clear();
							if (objectCard.has("petCards")&&!objectCard.isNull("petCards")) {
								JSONArray arrayPetCards = objectCard.getJSONArray("petCards");
								if (arrayPetCards.length()>0) {
									cardMakeDogs.clear();
									for (int i = 0; i < arrayPetCards.length(); i++) {
										CardMakeDog makeDog = new CardMakeDog();
										JSONObject objectEveryMakeDog = arrayPetCards.getJSONObject(i);
										if (objectEveryMakeDog.has("price")&&!objectEveryMakeDog.isNull("price")) {
											makeDog.price = objectEveryMakeDog.getInt("price");	
										}
										if (objectEveryMakeDog.has("name")&&!objectEveryMakeDog.isNull("name")) {
											makeDog.name = objectEveryMakeDog.getString("name");	
										}
										if (objectEveryMakeDog.has("petCard")&&!objectEveryMakeDog.isNull("petCard")) {
											makeDog.petCard = objectEveryMakeDog.getInt("petCard");	
										}
//										if (isCerti==-1) {
//											isCerti = 0;
//										}
										makeDog.isCerti=isCerti;
										cardMakeDogs.add(makeDog);
									}
								}
								if (cardMakeDogs.size()>0) {
									cardMakeDogAdapter.setChooseState(-1);
									cardMakeDogAdapter.notifyDataSetChanged();
								}
							}else {
								textView_cards_activity_notice.setVisibility(View.GONE);
								textView_cards_cannot_support.setVisibility(View.GONE);
								choose_make_dog_cards.setVisibility(View.GONE);
							}
							if (objectCard.has("packages")&&!objectCard.isNull("packages")) {
								JSONArray arrayPackages = objectCard.getJSONArray("packages");
								if (arrayPackages.length()>0) {
									cardsBuys.clear();
									for (int i = 0; i < arrayPackages.length(); i++) {
										CardsBuy cardsBuy = new CardsBuy();
										JSONObject objectEvery = arrayPackages.getJSONObject(i);
										if (objectEvery.has("package")&&!objectEvery.isNull("package")) {
											cardsBuy.CardPackageId = objectEvery.getInt("package");
										}
										if (objectEvery.has("price")&&!objectEvery.isNull("price")) {
											cardsBuy.price = objectEvery.getInt("price");
										}
										cardsBuy.discount = discount;
										cardsBuy.petCardDiscount = petCardDiscount;
//										if (objectEvery.has("discount")&&!objectEvery.isNull("discount")) {
//											cardsBuy.discount = objectEvery.getString("discount");
//										}
										if (objectEvery.has("title")&&!objectEvery.isNull("title")) {
											cardsBuy.title = objectEvery.getString("title");
										}
										if (objectEvery.has("content")&&!objectEvery.isNull("content")) {
//											cardsBuy.content = objectEvery.getString("content");
											JSONArray objectContent = objectEvery.getJSONArray("content");
											if (objectContent.length()>0) {
												for (int j = 0; j < objectContent.length(); j++) {
													cardsBuy.listStr.add(objectContent.getString(j));
												}
											}
										}
										if (objectEvery.has("listPrice")&&!objectEvery.isNull("listPrice")) {
											cardsBuy.listPrice = objectEvery.getInt("listPrice");
										}
										if (objectEvery.has("price1")&&!objectEvery.isNull("price1")) {
											cardsBuy.price1 = objectEvery.getInt("price1");
										}
										cardsBuys.add(cardsBuy);
									}
								}
								if (cardsBuys.size()>0) {
									listView_choose_cards_every.setVisibility(View.VISIBLE);
									textView_cards_cannot_support_make.setVisibility(View.GONE);
									cardsEveryItemAdapter.setCutDownPrice(null);
									cardsEveryItemAdapter.notifyDataSetChanged();
								}else {
									listView_choose_cards_every.setVisibility(View.GONE);
									textView_cards_cannot_support_make.setVisibility(View.VISIBLE);
								}
							}
							
							if (objectCard.has("tip")&&!objectCard.isNull("tip")) {
								textView_cards_detail.setText(objectCard.getString("tip"));
							}
							if (objectCard.has("name")&&!objectCard.isNull("name")) {
								titleName = objectCard.getString("name");
								textView_cards_name.setText(objectCard.getString("name"));
							}
							if (objectCard.has("bathCount")&&!objectCard.isNull("bathCount")) {
								int bathCount = objectCard.getInt("bathCount");
								if (bathCount>0) {
									layout_wash.setVisibility(View.VISIBLE);
									textview_wash_num.setText(bathCount+"");
								}else {
									layout_wash.setVisibility(View.GONE);
								}
							}else {
								layout_wash.setVisibility(View.GONE);
							}
							if (objectCard.has("beautyCount")&&!objectCard.isNull("beautyCount")) {
								int beautyCount = objectCard.getInt("beautyCount");
								if (beautyCount>0) {
									layout_beau.setVisibility(View.VISIBLE);
									textview_beau_num.setText(beautyCount+"");
								}else {
									layout_beau.setVisibility(View.GONE);
								}
							}else {
								layout_beau.setVisibility(View.GONE);
							}
							
							if (objectCard.has("id")&&!objectCard.isNull("id")) {
								id = objectCard.getInt("id");
							}
							if (objectCard.has("cardDetailImg")&&!objectCard.isNull("cardDetailImg")) {
								Imagelist.clear();
								JSONArray arrayCardImgs = objectCard.getJSONArray("cardDetailImg");
								if (arrayCardImgs.length()>0) {
									for (int i = 0; i < arrayCardImgs.length(); i++) {
										if (arrayCardImgs.getString(i).startsWith("http")) {
											Imagelist.add(arrayCardImgs.getString(i));
										}else {
											Imagelist.add(CommUtil.getServiceNobacklash()+arrayCardImgs.getString(i));
										}
									}
								}
							}
							if (Imagelist.size()>0) {
								joinWorkAdapter.notifyDataSetChanged();
							}
						}
						
						
					}
				}else {
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Utils.mLogError("==-->error "+e.getMessage());
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			
		}
	};
	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		
		layout_top_set_back = (RelativeLayout) findViewById(R.id.layout_top_set_back);
		imageView_left = (ImageView) findViewById(R.id.imageView_left);
		textView_cards_name = (TextView) findViewById(R.id.textView_cards_name);
		layout_wash = (LinearLayout) findViewById(R.id.layout_wash);
		textview_wash_num = (TextView) findViewById(R.id.textview_wash_num);
		layout_beau = (LinearLayout) findViewById(R.id.layout_beau);
		textview_beau_num = (TextView) findViewById(R.id.textview_beau_num);
		textView_cards_detail = (TextView) findViewById(R.id.textView_cards_detail);
		textview_cards_dog_name = (TextView) findViewById(R.id.textview_cards_dog_name);
		textview_cards_changepet = (TextView) findViewById(R.id.textview_cards_changepet);
		textView_cards_activity_notice = (TextView) findViewById(R.id.textView_cards_activity_notice);
		textView_cards_cannot_support = (TextView) findViewById(R.id.textView_cards_cannot_support);
		textView_cards_cannot_support_make = (TextView) findViewById(R.id.textView_cards_cannot_support_make);
		choose_make_dog_cards = (MListview) findViewById(R.id.choose_make_dog_cards);
		listView_choose_cards_every = (MListview) findViewById(R.id.listView_choose_cards_every);
		cards_scaling_image = (MListview) findViewById(R.id.cards_scaling_image);
		imageview_show_back_img = (ImageView) findViewById(R.id.imageview_show_back_img);
		cards_scaling_image = (MListview) findViewById(R.id.cards_scaling_image);
		
		ib_titlebar_back.setOnClickListener(this);
		textview_cards_changepet.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		textview_cards_changepet.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		textview_cards_changepet.getPaint().setAntiAlias(true);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.getDisplayMetrics((Activity)mContext)[0]*255/716);
		params.leftMargin=10;
		params.rightMargin=10;
		params.topMargin=10;
		layout_top_set_back.setLayoutParams(params);
	}
	
	private void goNext(CardsBuy cardsBuyEvery,Class cls,int pre) {
		Intent intent = new Intent(mContext,cls);
		intent.putExtra("CardsBuy", cardsBuyEvery);
		intent.putExtra("previous", pre);
		intent.putExtra("previous_liucheng", previous);
		intent.putExtra("CardMakeDog",cardMakeDog);
		intent.putExtra("titleName",titleName);
		intent.putExtra("id",id);
		intent.putExtra("petid",petid);
		intent.putExtra("petName",petName);
		Utils.mLogError("==-->流程购买次卡  "+getClass().getName() +" --- 000 --- "+previous);
		startActivity(intent);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finish();
			break;
		case R.id.textview_cards_changepet:
			Intent intent = new Intent(detailActivity, ChoosePetActivityNew.class);
			intent.putExtra("previous", Global.CARD_CHOOSE_PET);
			intent.putExtra("id",id);
			startActivityForResult(intent, Global.CARD_CHOOSE_PET);
			break;

		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Global.RESULT_OK) {
			if (requestCode == Global.CARD_CHOOSE_PET) {
				cardMakeDog=null;
				isCerti = 0;
				// 选择宠物类型返回
				petid = data.getIntExtra("petid", 0);
//				customerpetname = data.getStringExtra("customerpetname");
//				serviceid = data.getIntExtra("serviceid", 0);
				servicetype = data.getIntExtra("servicetype", 0);
//				customerpetid = data.getIntExtra("customerpetid", 0);
				petName = data.getStringExtra("petname");
				petkind = data.getIntExtra("petkind", 0);
				isCerti = data.getIntExtra("isCerti", -1);
				
				if (isCerti==0) {
					textView_cards_cannot_support.setVisibility(View.VISIBLE);
				}else if (isCerti==1) {
					textView_cards_cannot_support.setVisibility(View.GONE);
				}
				textview_cards_dog_name.setText(petName+CardName);
				Utils.mLogError("==-->次卡详情 petid "+petid);
				getData(id,petid);
			}
		}
	}
}
