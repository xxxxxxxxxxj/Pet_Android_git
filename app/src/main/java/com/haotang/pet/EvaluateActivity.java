package com.haotang.pet;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.MultiImage.PickOrTakeImageActivity;
import com.haotang.pet.adapter.CommentStarAdapter;
import com.haotang.pet.adapter.MyEvaluateAdapter;
import com.haotang.pet.entity.CommentStar;
import com.haotang.pet.entity.Share;
import com.haotang.pet.luban.Luban;
import com.haotang.pet.luban.OnCompressListener;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.MDialog;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.MyGridView;
import com.haotang.pet.view.SelectableRoundedImageView;
import com.haotang.pet.view.UserNameAlertDialog;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

public class EvaluateActivity extends SuperActivity implements OnClickListener{
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private SelectableRoundedImageView imageView_evaluate_dog_icon;
	private TextView textView_evaluate_dog_name,
//	textView_evaluate_service_grade,
	textView_evalute_price
//	,textView_evalute_old_price
	;
	private EditText editText_evalute_write_by_user;
	private GridView gridView;
	private RatingBar ratingBar_major_grade
//	,ratingBar_service_attitude,
//	ratingBar_punctuality_grade
	;
	private Button post_to_service_eva;
	private Bitmap bmp;
	ArrayList<Bitmap> aList;
	private MyEvaluateAdapter adapter;
	private Context context;
	private String picturePath;
	private static final String TEMPCERTIFICATIONNAM = "real_certification.jpg";
	private PopupWindow pWin;
	private LayoutInflater mInflater;
	
	
	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_CAMER = 2;
	Context mContext;
	String path = "";
	GridView gv;
	MyAdapter ImgAdapter;
	List<Bitmap> imgList = new ArrayList<Bitmap>();
	ImageView ivDelete;
	private boolean isShowDelete = false;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示
	ArrayList<String> list = new ArrayList<String>();
	int OrderId = 0;
	int one = 0;
	int two = 0;
	int thr = 0;
//	---start 2015年8月14日17:04:47
	private boolean isSina = false;
	private boolean isQQ = false;
//	private ImageView sinaShare;
//	private ImageView qqShare;
	
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	
	private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;
	private SHARE_MEDIA mPlatform2 = SHARE_MEDIA.TENCENT;
	
	public static  EvaluateActivity evaluateActivity;
	
	private SharedPreferenceUtil spUtil;
	private int qqShareStatus = 0;
	private int weiboShareStatus = 0;
	private MProgressDialog pDialog;
//	---end 2015年8月14日17:05:02
	/**
	 * 广播接收器
	 */
	private MyReceiver receiver;
	private List<Share> shareList;
	
//	----start 2016年1月19日11:11:56 评价
//	private LinearLayout linearLayout_grade_one;
//	private TextView textView_major_grade;
	
//	private LinearLayout linearLayout_grade_two;
//	private TextView textView_service_attitude;
	
//	private LinearLayout linearLayout_grade_thr;
//	private TextView textView_punctuality_grade;
//	private View view_evalute_old_price;
//	----end 2016年1月19日11:12:03
	
	
//	---START 2016年1月20日11:08:47
	private int type = 0;
//	---END 2016年1月20日11:08:52
	
	
//	---START 2016年1月26日17:47:12
	
	private double upgradefee=0;
	private double payfee=0;
	private  static final String IMAGE_UNSPECIFIED = "image/*";
	private static final int IMAGE_CERTIFICATION = 101;
	private String bmpStr="";
	private Bitmap bitmap;
	private int areaId;
	private List<File> listFile = new ArrayList<File>();
	private boolean isNickName = false;
	private String serviceName = "";
//	---END 2016年1月26日17:47:18
	private TextView textview_eva_length;
	private ImageView is_anonymous;
	private LinearLayout layout_is_anonymous;
	private boolean isAnonymous = false;
	private TextView textview_show_last_eva;
	private MyGridView gridView_last_show;
	private ArrayList<CommentStar> commentStars = new ArrayList<CommentStar>();
	private CommentStarAdapter starAdapter;
	private ArrayList<Integer> listChoose = new ArrayList<Integer>();
	private ImageView imageview_eva_one;
	private ImageView imageview_eva_two;
	private ImageView imageview_eva_thr;
	private ImageView imageview_eva_four;
	private ImageView imageview_eva_five;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_evaluate);
		setContentView(R.layout.activity_evaluate_new);
		evaluateActivity = this;
		listFile.clear();
		commentStars.clear();
		shareList = new ArrayList<Share>();	
		spUtil = SharedPreferenceUtil.getInstance(this);
		pDialog = new MProgressDialog(this);
		mContext = this;
		OrderId = getIntent().getIntExtra("orderid", 0);
		initView();
		initListener();
		
		getDate();
		initReceiver();
		initGridView();
		getOrderDetails();
		getRatingBar();

	}

//	private void getStatusFromNet() {
//		//新浪微博
//	    isSina = OauthHelper.isAuthenticated(evaluateActivity, SHARE_MEDIA.SINA);
//	    if (isSina&&weiboShareStatus==1) {
//	    	sinaShare.setBackgroundResource(R.drawable.sina_login);
//		}else if(isSina&&weiboShareStatus==0) {
//			sinaShare.setBackgroundResource(R.drawable.sina_no_login);
//		}else if (!isSina) {
//			sinaShare.setBackgroundResource(R.drawable.sina_no_login);
//		}
////	    qqShareStatus = 0;
//	    //腾讯微博
//	    isQQ =  OauthHelper.isAuthenticated(evaluateActivity, SHARE_MEDIA.TENCENT);
//	    if (isQQ&&qqShareStatus==1) {
//	    	qqShare.setBackgroundResource(R.drawable.tengxun_login);
//		}else if(isQQ&&qqShareStatus==0){
//			qqShare.setBackgroundResource(R.drawable.tengxun_no_login);
//		}else if (!isQQ) {
//			qqShare.setBackgroundResource(R.drawable.tengxun_no_login);
//		}
//	}

	
	//查询订单明细
	private void getOrderDetails() {
		pDialog.showDialog();
		CommUtil.queryOrderDetails(spUtil.getString("cellphone", ""),  
				Global.getIMEI(this), this,OrderId, getOrderDetails);
	}
	
	private void initListener() {
		
		gridView_last_show.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CommentStar star = (CommentStar) parent.getItemAtPosition(position);
				star.ifChoose=!star.ifChoose;
				commentStars.set(position, star);
				listChoose.clear();
				for (int i = 0; i < commentStars.size(); i++) {
					if (commentStars.get(i).ifChoose) {
						listChoose.add(commentStars.get(i).CommentTagId);
					}
				}
				starAdapter.notifyDataSetChanged();
				
			}
		});
		layout_is_anonymous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!isAnonymous) {
					isAnonymous = true;
					is_anonymous.setBackgroundResource(R.drawable.icon_pay_selected);
				}else {
					isAnonymous = false;
					is_anonymous.setBackgroundResource(R.drawable.icon_pay_normal);
				}
			}
		});
		editText_evalute_write_by_user.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				textview_eva_length.setText(s.length()+"/500");
			}
		});
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Utils.mLog("==-->position "+position +" imgList.size(): = " +imgList.size());
				if (position<=3) {
					if (position == imgList.size()&&position!=3/* &&position<=2 */) {
						showSelectDialog();
					} else if(position==3) {
						Toast.makeText(mContext, "当前最多支持三张图片", Toast.LENGTH_SHORT).show();
					}else {
						Intent intent = new Intent(mContext, ImgShow.class);
						Bundle bundle = new Bundle();
						bundle.putInt("position", position);
						Bitmap bitmap = imgList.get(position);
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
						byte [] bitmapByte =stream.toByteArray();  
						intent.putExtra("bitmap", bitmapByte);  
						bundle.putStringArrayList("list",list);
						intent.putExtras(bundle);
						startActivity(intent);
						
					}
				}
			}
		});
		// 长按事件
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view,
							int position, long id) {

						// 长按显示删除图标
						if (isShowDelete == false) {
							isShowDelete = true;
						}
						ImgAdapter.setIsShowDelete(isShowDelete);

						return true;
					}
				});
		
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goBack();
			}
		});
		//专业程度
//		ratingBar_major_grade.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
//			
//			@Override
//			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
////				Toast.makeText(mContext, "专业程度现在等级 "+rating, Toast.LENGTH_SHORT).show();
//				one = (int) rating;
//				if (rating<1) {
//					one = 1;
//					ratingBar_major_grade.setRating(1);
//				}else {
//					ratingBar_major_grade.setRating(one);
//				}
//				listChoose.clear();
//				getRatingBar();
//			}
//
//		});
		//服务态度
//		ratingBar_service_attitude.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
//			
//			@Override
//			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
////				Toast.makeText(mContext, "服务态度现在等级 "+rating, Toast.LENGTH_SHORT).show();
//				two = (int) rating;
//				if (rating<1) {
//					two = 1;
//					ratingBar_service_attitude.setRating(1);
//				}
//			}
//		});
		//守时程度
//		ratingBar_punctuality_grade.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
//			
//			@Override
//			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
////				Toast.makeText(mContext, "守时程度现在等级 "+rating, Toast.LENGTH_SHORT).show();
//				thr = (int) rating;
//				if (rating<1) {
//					thr = 1;
//					ratingBar_punctuality_grade.setRating(1);
//				}
//			}
//		});
		
		post_to_service_eva.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if("".equals(editText_evalute_write_by_user.getText().toString().trim())){
					ToastUtil.showToastShort(EvaluateActivity.this, "请填写你的评价");
					return;
				}
				if (one<=0) {
					ToastUtil.showToastShort(EvaluateActivity.this, "请给本次服务评分");
					return;
				}
				String userName = spUtil.getString("username", "");
				if (!isAnonymous) {
					if (userName.equals("")||TextUtils.isEmpty(userName)) {
						isNickName = false;
						tqDialog();
						return;
					}else {
						isNickName = true;
					}
				}
//				setShareContent(editText_evalute_write_by_user.getText().toString());
//				directShare();
				List<Integer> doubles = new ArrayList<Integer>();
				doubles.add(one);
				doubles.add(two);
				doubles.add(thr);
				List<String> imgStringList = new ArrayList<String>();
				if (imgList.size()>0) {
					for (int i = 0; i < imgList.size(); i++) {
						String bmpStr = Global.encodeWithBase64(imgList.get(i));
						imgStringList.add(bmpStr);
					}
				}
				Utils.mLogError("==-->评价  分享状态 qqShareStatus : "+qqShareStatus +" weiboShareStatus:=  "+weiboShareStatus);
				try {
					mPDialog.showDialog();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				CommUtil.commentOrder(SharedPreferenceUtil.getInstance(mContext).getString("cellphone", "0"), 
						Global.getIMEI(mContext), 
						mContext,
						OrderId,
						doubles,
						editText_evalute_write_by_user.getText().toString(),
						imgStringList, 
						qqShareStatus,
						weiboShareStatus,
						getCommentIds(),
						isAnonymous,
						commentOrder);
				
			}

		});
//		//新浪微博图标点击判断
//		sinaShare.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				isSina = OauthHelper.isAuthenticated(evaluateActivity, SHARE_MEDIA.SINA);
//				//如果未授权则授权
//				if (!isSina) {
//					mController.doOauthVerify(evaluateActivity, SHARE_MEDIA.SINA, new UMAuthListener() {
//						
//						@Override
//						public void onStart(SHARE_MEDIA arg0) {
//							// TODO Auto-generated method stub
//							
//						}
//						
//						@Override
//						public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
//							// TODO Auto-generated method stub
//							
//						}
//						
//						@Override
//						public void onComplete(Bundle value, SHARE_MEDIA platform) {
//							// TODO Auto-generated method stub
//							if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
////								Toast.makeText(evaluateActivity, "授权成功.",Toast.LENGTH_SHORT).show();
//								isSina=true;
//								weiboShareStatus=1;
//								sinaShare.setBackgroundResource(R.drawable.sina_login);
//							} else {
////								Toast.makeText(evaluateActivity, "授权失败",Toast.LENGTH_SHORT).show();
//								isSina=false;
//								weiboShareStatus=0;
//								sinaShare.setBackgroundResource(R.drawable.sina_no_login);
//							}
//						}
//						
//						@Override
//						public void onCancel(SHARE_MEDIA arg0) {
//							// TODO Auto-generated method stub
//							
//						}
//					});
//
//				}else if (isSina&&weiboShareStatus==1) {
//					isSina = false;
//					weiboShareStatus =0;
//					sinaShare.setBackgroundResource(R.drawable.sina_no_login);
//				}else if (isSina&&weiboShareStatus==0) {
//					isSina = true;
//					weiboShareStatus =1; 
//					sinaShare.setBackgroundResource(R.drawable.sina_login);
//				}
//				
//			}
//		});
//		
//		//腾讯图标处理
//		qqShare.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				 isQQ =  OauthHelper.isAuthenticated(evaluateActivity, SHARE_MEDIA.TENCENT);
//				//如果未授权则授权
//				 if (!isQQ) {
//						mController.doOauthVerify(evaluateActivity, SHARE_MEDIA.TENCENT, new UMAuthListener() {
//						
//						@Override
//						public void onStart(SHARE_MEDIA arg0) {
//							// TODO Auto-generated method stub
//							
//						}
//						
//						@Override
//						public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
//							// TODO Auto-generated method stub
//							
//						}
//						
//						@Override
//						public void onComplete(Bundle value, SHARE_MEDIA platform) {
//							// TODO Auto-generated method stub
//							if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
////								Toast.makeText(evaluateActivity, "授权成功.",Toast.LENGTH_SHORT).show();
//								isQQ=true;
//								qqShareStatus=1;
//								qqShare.setBackgroundResource(R.drawable.tengxun_login);
//							} else {
////								Toast.makeText(evaluateActivity, "授权失败",Toast.LENGTH_SHORT).show();
//								isQQ=false;
//								qqShareStatus=0;
//								qqShare.setBackgroundResource(R.drawable.tengxun_no_login);
//							}
//						}
//						
//						@Override
//						public void onCancel(SHARE_MEDIA arg0) {
//							// TODO Auto-generated method stub
//							
//						}
//					});
//
//				}else if (isQQ&&qqShareStatus==1) {
//					isQQ = false;
//					qqShareStatus =0;
//					qqShare.setBackgroundResource(R.drawable.tengxun_no_login);
//				}else if (isQQ&&qqShareStatus==0) {
//					isQQ = true;
//					qqShareStatus =1; 
//					qqShare.setBackgroundResource(R.drawable.tengxun_login);
//				}
//			}
//		});
		
	}
	private void getRatingBar() {
		CommUtil.commentStar(mContext, one, ratingHandler);
	}
	private void goBack(){
		if (one>0||two>0||thr>0||imgList.size()>0||editText_evalute_write_by_user.getText().length()>0) {
			showBackDialog();
		}else {
			setResult(RESULT_OK);
			finishWithAnimation();
		}
	}
	private AsyncHttpResponseHandler commentOrder = new AsyncHttpResponseHandler(){

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				mPDialog.closeDialog();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				JSONObject jsonObject = new JSONObject(new String(responseBody));
				int code = jsonObject.getInt("code");
				if (code==0) {
					ToastUtil.showToastShortCenter(mContext, "感谢您的评价，我们会继续努力~");
//					Intent intent = new Intent(mContext, EvaluateOverActivity.class);
//					intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//					getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//					startActivity(intent);
					if (!isAnonymous) {//不是匿名 发宠圈
						EvaOverPostData();//评价结束同时发帖
					}
//					setResult(RESULT_OK);//评价之后返回订单详情界面、、多宠物一期更改详情界面需弹窗发送红包 --暂时注释
//					finishWithAnimation();
					
					Intent intentStatus = new Intent();
					intentStatus.setAction("android.intent.action.OrderDetailFromOrderToConfirmActivity");
					intentStatus.putExtra("index", 1);
					sendBroadcast(intentStatus);
					
					setResult(Global.RESULT_OK);
					finishWithAnimation();
				}else {
					String msg = jsonObject.getString("msg");
					ToastUtil.showToastShort(mContext, msg);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		private void EvaOverPostData() {
			final File [] imgs = new File[list.size()];
			for (int i = 0; i < list.size(); i++) {
				imgs[i] = new File(list.get(i));
			}
			CommUtil.newPost(
					spUtil.getString("cellphone", ""),
					mContext,OrderId,"【"+serviceName+"】"+editText_evalute_write_by_user.getText().toString(),imgs,null,null,1,
					1,isAnonymous,
					newPost);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
		
	};
	private void initGridView() {
		ImgAdapter = new MyAdapter();
		ImgAdapter.setIsShowDelete(isShowDelete);
		gridView.setAdapter(ImgAdapter);
		
		
		starAdapter = new CommentStarAdapter<CommentStar>(evaluateActivity, commentStars);
		gridView_last_show.setAdapter(starAdapter);
	}
	private void initView() {
		mInflater = LayoutInflater.from(this);
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		imageView_evaluate_dog_icon = (SelectableRoundedImageView) findViewById(R.id.imageView_evaluate_dog_icon);
		textView_evaluate_dog_name = (TextView) findViewById(R.id.textView_evaluate_dog_name);
//		textView_evaluate_service_grade = (TextView) findViewById(R.id.textView_evaluate_service_grade);
		textView_evalute_price = (TextView) findViewById(R.id.textView_evalute_price);
//		textView_evalute_old_price = (TextView) findViewById(R.id.textView_evalute_old_price);
		editText_evalute_write_by_user = (EditText) findViewById(R.id.editText_evalute_write_by_user);
		gridView = (GridView) findViewById(R.id.gridView_get_dog_phone);
		ratingBar_major_grade = (RatingBar) findViewById(R.id.ratingBar_major_grade);
//		ratingBar_service_attitude = (RatingBar) findViewById(R.id.ratingBar_service_attitude);
//		ratingBar_punctuality_grade = (RatingBar) findViewById(R.id.ratingBar_punctuality_grade);
		post_to_service_eva = (Button) findViewById(R.id.post_to_service_eva);
//		sinaShare = (ImageView) findViewById(R.id.sinaShare);
//		qqShare = (ImageView) findViewById(R.id.qqShare);
		tv_titlebar_title.setText("评价");
		
		
//		linearLayout_grade_one = (LinearLayout) findViewById(R.id.linearLayout_grade_one);
//		textView_major_grade = (TextView) findViewById(R.id.textView_major_grade);
//		
//		linearLayout_grade_two = (LinearLayout) findViewById(R.id.linearLayout_grade_two);
//		textView_service_attitude = (TextView) findViewById(R.id.textView_service_attitude);
//		
//		linearLayout_grade_thr = (LinearLayout) findViewById(R.id.linearLayout_grade_thr);
//		textView_punctuality_grade = (TextView) findViewById(R.id.textView_punctuality_grade);
//		
//		view_evalute_old_price = findViewById(R.id.view_evalute_old_price);
		textview_eva_length = (TextView) findViewById(R.id.textview_eva_length);
		is_anonymous = (ImageView) findViewById(R.id.is_anonymous);
		layout_is_anonymous = (LinearLayout) findViewById(R.id.layout_is_anonymous);
		textview_show_last_eva = (TextView) findViewById(R.id.textview_show_last_eva);
		gridView_last_show = (MyGridView) findViewById(R.id.gridView_last_show);
		imageview_eva_one = (ImageView) findViewById(R.id.imageview_eva_one);
		imageview_eva_two = (ImageView) findViewById(R.id.imageview_eva_two);
		imageview_eva_thr = (ImageView) findViewById(R.id.imageview_eva_thr);
		imageview_eva_four = (ImageView) findViewById(R.id.imageview_eva_four);
		imageview_eva_five = (ImageView) findViewById(R.id.imageview_eva_five);
		
//		linearLayout_grade_one.setVisibility(View.GONE);
//		linearLayout_grade_two.setVisibility(View.GONE);
//		linearLayout_grade_thr.setVisibility(View.GONE);
		imageview_eva_one.setOnClickListener(this);
		imageview_eva_two.setOnClickListener(this);
		imageview_eva_thr.setOnClickListener(this);
		imageview_eva_four.setOnClickListener(this);
		imageview_eva_five.setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		if(!TextUtils.isEmpty(picturePath)){
//			Bitmap addbmp=BitmapFactory.decodeFile(picturePath);
//			aList.add(addbmp);
//			//先移除用来添加的图标，再添加以保证添加的图片始终在最后
//			aList.remove(bmp);
//			aList.add(bmp);
//			adapter.setDate(aList);
//			adapter.notifyDataSetChanged();
//			//刷新后释放，防止手机休眠后自动添加
//			picturePath=null;
//		}
	}
	File out;
	
	private void showSelectDialog() {
		try {
			goneJp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pWin = null;
		// TODO pop
		if (pWin == null) {
			View view = mInflater.inflate(R.layout.dlg_choose_icon, null);
			LinearLayout pop_getIcon_action =   (LinearLayout) view.findViewById(R.id.pop_getIcon_action);
			LinearLayout pop_getIcon_local =   (LinearLayout) view.findViewById(R.id.pop_getIcon_local);
			LinearLayout pop_getIcon_cancle =   (LinearLayout) view.findViewById(R.id.pop_getIcon_cancle);
			pWin = new PopupWindow(view,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
			pWin.setFocusable(true);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			pWin.setWidth(dm.widthPixels/* - 40*/);
			pWin.setOutsideTouchable(true);
			pWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
			
			//拍照
			pop_getIcon_action.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
					String photoname = getCurrentTime()+"a.jpg";
					out = new File(getSDPath(), photoname);
//					list.add(out.getAbsolutePath());
					Uri uri = Uri.fromFile(out);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(intent, SELECT_CAMER);
					pWin.dismiss();
					pWin = null;
				}
			});
			//本地获取图片
			pop_getIcon_local.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
//					Intent intent = new Intent();
//					intent.setType("image/*");
//					intent.setAction(Intent.ACTION_GET_CONTENT);
//					startActivityForResult(intent, SELECT_PICTURE);
//	    			pWin.dismiss();
//	    			pWin = null;
	    			
	    			
//					  Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//					  Intent intent = new Intent(Intent.ACTION_PICK, null);
//					  intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
//	    				startActivityForResult(intent, IMAGE_CERTIFICATION);
	    				
					  Intent intent = new Intent(evaluateActivity, PickOrTakeImageActivity.class);
					  intent.putExtra("extra_nums", 3-imgList.size());
	    			  startActivityForResult(intent, 100214);
	    				pWin.dismiss();
	    				pWin = null;
				}
			});
			pop_getIcon_cancle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					pWin.dismiss();
					pWin = null;
				}
			});
			
		}
	}

	
	/**
	 * 回调
	 */
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == RESULT_OK) {
				switch (requestCode) {
				case IMAGE_CERTIFICATION:
					if(data==null){
						ToastUtil.showToastShortCenter(EvaluateActivity.this, "您选择的照片不存在，请重新选择");
						return;
					}
					try {
						Uri originalUri = data.getData(); //获得图片的uri
						if (!TextUtils.isEmpty(originalUri.getAuthority())) {
							// 这里开始的第二部分，获取图片的路径： 
							String[] proj = {MediaStore.Images.Media.DATA}; 
							Cursor cursor = getContentResolver().query(originalUri, proj, null, null, null); 
							//获得用户选择的图片的索引值 
							int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
							cursor.moveToFirst(); 
							//最后根据索引值获取图片路径 
//							photoPath = cursor.getString(column_index); 
							path = cursor.getString(column_index); 
//							Bitmap bm = Utils.getxtsldraw(mContext,path);
//							path = Utils.creatfile(mContext, bm, "usermodify"+getCurrentTime());
							list.add(path);
							getLuban(path);
//							if (null != bm && !"".equals(bm)) {
//								imgList.add(bm);
//							}
							cursor.close();
							//华为及其他手机系统的图片Url获取
//							setPicToImageView(imageView_change_icon,photoPath);
						}else{
							//小米系统走的方法
//							setPicToImageView(imageView_change_icon,originalUri.getPath());
//							Bitmap bm = Utils.getxtsldraw(mContext,originalUri.getPath());
							list.add(originalUri.getPath());
							getLuban(originalUri.getPath());
//							if (null != bm && !"".equals(bm)) {
//								imgList.add(bm);
//							}
						}
						
						ImgAdapter.notifyDataSetChanged();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					break;
				//从相册选择
//				case SELECT_PICTURE:
//					// TODO 周一更新 目前从本地选择有问题
//					Uri uri = data.getData();
//					String photoPath = RealPathUtil.getRealPathFromURI(EvaluateActivity.this, uri);
//					Bitmap bm = Utils.getxtsldraw(mContext,photoPath);
//					path = Utils.creatfile(mContext, bm, "usermodify"+getCurrentTime());
//					list.add(path);
//					if (null != bm && !"".equals(bm)) {
//						imgList.add(bm);
//					}
//					ImgAdapter.notifyDataSetChanged();
//					break;
					//拍照添加图片
				case SELECT_CAMER:
					Bitmap bm1 = Utils.getxtsldraw(mContext, out.getAbsolutePath());
					path = Utils.creatfile(mContext, bm1, "usermodify"+getCurrentTime());
//					Utils.mLogError("==-->path 拍照添加图片 := "+path);
					list.add(path);
					getLuban(path);
					if (null != bm1 && !"".equals(bm1)) {
						imgList.add(bm1);
					}
					ImgAdapter.notifyDataSetChanged();
					break;
				case 100214:
					ArrayList<String> arrayList = data.getStringArrayListExtra("data");
					for (int i = 0; i < arrayList.size(); i++) {
						Bitmap bm = Utils.getxtsldraw(mContext,arrayList.get(i));
						list.add(arrayList.get(i));
						getLuban(arrayList.get(i));
						if (null != bm && !"".equals(bm)) {
							imgList.add(bm);
						}
					}
					ImgAdapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
			}
			
			/**使用SSO授权必须添加如下代码 */
	           UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	           if(ssoHandler != null){
	              ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	           }
		}
		
		
		
		
		/**
		 * 用于gridview显示多张照片
		 * 
		 * @author wlc
		 * @date 2015-4-16
		 */
		public class MyAdapter extends BaseAdapter {
			
			private boolean isDelete;  //用于删除图标的显隐
			private LayoutInflater inflater = LayoutInflater.from(mContext);

			@Override
			public int getCount() {
				//需要额外多出一个用于添加图片
				return imgList.size() >= 3 ? imgList.size() : imgList.size() + 1;
			}

			@Override
			public Object getItem(int arg0) {
				return imgList.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup arg2) {
				
				//初始化页面和相关控件
				convertView = inflater.inflate(R.layout.item_imgview, null);
				ImageView img_pic = (ImageView) convertView
						.findViewById(R.id.img_pic);
				LinearLayout ly = (LinearLayout) convertView
						.findViewById(R.id.layout);
				LinearLayout ll_picparent = (LinearLayout) convertView
						.findViewById(R.id.ll_picparent);
				ImageView delete = (ImageView) convertView
						.findViewById(R.id.img_delete);
				
				//默认的添加图片的那个item是不需要显示删除图片的
				if (imgList.size() >= 1) {
						if (position <= imgList.size() - 1) {
							ll_picparent.setVisibility(View.GONE);
							img_pic.setVisibility(View.VISIBLE);
							img_pic.setImageBitmap(imgList.get(position));
							// 设置删除按钮是否显示
							delete.setVisibility(isDelete ? View.VISIBLE : View.GONE);
					}
				}
				
				//当处于删除状态时，删除事件可用
				//注意：必须放到getView这个方法中，放到onitemClick中是不起作用的。
				if (isDelete) {
					delete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							
							imgList.remove(position);
							ImgAdapter.notifyDataSetChanged();

						}
					});
				}
				
				return convertView;
			}

			/**
			 * 设置是否显示删除图片
			 * 
			 * @param isShowDelete
			 */
			public void setIsShowDelete(boolean isShowDelete) {
				this.isDelete = isShowDelete;
				notifyDataSetChanged();
			}

		}
		/**
		 * 获取sd卡路径
		 * 
		 * @return
		 */
		private File getSDPath() {
			File sdDir = null;
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				// 这里可以修改为你的路径
				sdDir = new File(Environment.getExternalStorageDirectory()
						+ "/DCIM/Camera");

			}
			return sdDir;
		}
		
		
		private void initReceiver() {
			// 广播事件**********************************************************************
			receiver = new MyReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.intent.action.EvaluateActivity");
			// 注册广播接收器
			registerReceiver(receiver, filter);
		}

		private class MyReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getExtras();
				int index =  bundle.getInt("index");
				if (index==0) {
					Utils.mLogError("==-->login 接收广播");
					int position =  bundle.getInt("position");
					list.remove(position);
					imgList.remove(position);
					listFile.remove(position);
					Utils.mLogError("==-->listev  1  --> "+list.size());
					Utils.mLogError("==-->listev 2 --->  "+ imgList.size());
					ImgAdapter.notifyDataSetChanged();
					
				}else if (index==1) {
					
				}
			}
		}
		
		
		public String getCurrentTime(){//避免特殊字符产生无法调起拍照后无法保存返回
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
	        String currentTime = df.format(new Date());// new Date()为获取当前系统时间
			return currentTime;
		}

		public enum RealPathUtil {
			INSTANCE;

			public static String getRealPathFromURI(Context context, Uri uri)
			{
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
				{
					return getRealPathFromURI_BelowAPI11(context, uri);
				}
				else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
				{
					return getRealPathFromURI_API11to18(context, uri);
				}
				else
				{
					return getRealPathFromURI_API19(context, uri);
				}
			}

			@SuppressLint("NewApi")
			public static String getRealPathFromURI_API19(Context context, Uri uri) {
				String filePath = "";
				String wholeID = "";

				try {
					wholeID = DocumentsContract.getDocumentId(uri);
				} catch (Exception ex) {
					ex.printStackTrace();           // Android 4.4.2 can occur this exception.

					return getRealPathFromURI_API11to18(context, uri);
				}

				// Split at colon, use second item in the array
				String id = wholeID.split(":")[1];

				String[] column = { MediaColumns.DATA };

				// where id is equal to
				String sel = BaseColumns._ID + "=?";

				Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						column, sel, new String[]{ id }, null);

				int columnIndex = cursor.getColumnIndex(column[0]);

				if (cursor.moveToFirst()) {
					filePath = cursor.getString(columnIndex);
				}

				cursor.close();
				return filePath;
			}


			@SuppressLint("NewApi")
			public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
				String[] proj = { MediaColumns.DATA };
				String result = null;

				CursorLoader cursorLoader = new CursorLoader(
						context,
						contentUri, proj, null, null, null);
				Cursor cursor = cursorLoader.loadInBackground();

				if(cursor != null){
					int column_index =
							cursor.getColumnIndexOrThrow(MediaColumns.DATA);
					cursor.moveToFirst();
					result = cursor.getString(column_index);
				}
				return result;
			}

			public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
				String[] proj = { MediaColumns.DATA };
				Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
				int column_index
						= cursor.getColumnIndexOrThrow(MediaColumns.DATA);
				cursor.moveToFirst();
				return cursor.getString(column_index);
			}
		}

		
	       /**
	        * 根据不同的平台设置不同的分享内容</br>
	        */
	       private void setShareContent(String str) {

	           // 配置SSO
	           mController.getConfig().setSsoHandler(new SinaSsoHandler());
	           mController.getConfig().setSinaCallbackUrl("http://www.baidu.com");
	           mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
	           QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
	                   "100424468", "c7394704798a158208a74ab60104f0ba");
	           qZoneSsoHandler.addToSocialSDK();
	           if (shareList.size()>0) {
	        	   mController.setShareContent(str+" @宠物家365"+ " #"+shareList.get(0).title +" #"+shareList.get(0).url);
		           if (imgList.size()>0) {
		        	   mController.setShareImage(new UMImage(evaluateActivity, imgList.get(0)));
		           }
		           TencentWbShareContent tencent = new TencentWbShareContent();
		           tencent.setShareContent(str+" @宠物家365"+ " #"+shareList.get(0).title +" #"+shareList.get(0).url);
		           if (imgList.size()>0) {
		        	   tencent.setShareImage(new UMImage(evaluateActivity, imgList.get(0)));
		           }
		           mController.setShareMedia(tencent);
		           SinaShareContent sinaContent = new SinaShareContent();
		           sinaContent
		                   .setShareContent(str+" @宠物家365"+ " #"+shareList.get(0).title +" #"+shareList.get(0).url);
		           if (imgList.size()>0) {
		        	   sinaContent.setShareImage(new UMImage(evaluateActivity, imgList.get(0)));
		           }
		           mController.setShareMedia(sinaContent);
	           }
	           

	           
	           // 设置tencent分享内容


	          


	       }
		
		private void directShare() {
	    	   if (isSina&&weiboShareStatus==1) {
	    		   mController.directShare(this, mPlatform, new SnsPostListener() {
	    			   
	    			   @Override
	    			   public void onStart() {
	    				   
	    			   }
	    			   
	    			   @Override
	    			   public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//	    				   String showText = "新浪分享成功";
	    				   if (eCode != StatusCode.ST_CODE_SUCCESSED) {
//	    					   showText = "新浪分享失败 [" + eCode + "]";
	    				   }
//	    				   Toast.makeText(evaluateActivity, showText, Toast.LENGTH_SHORT).show();
	    			   }
	    		   });
	    	   }
	    	   if (isQQ&&qqShareStatus==1) {
	       		mController.directShare(this, mPlatform2, new SnsPostListener() {
	    			
	    			@Override
	    			public void onStart() {
	    			}
	    			
	    			@Override
	    			public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//	    				String showText = "腾讯微博分享成功";
	    				if (eCode != StatusCode.ST_CODE_SUCCESSED) {
//	    					showText = "腾讯微博分享失败 [" + eCode + "]";
	    				}
//	    				Toast.makeText(evaluateActivity, showText, Toast.LENGTH_SHORT).show();
	    			}
	    		});

	    	   }
	       }
	       
	       
	       /**
	        * 一键分享到多个已授权平台。</br>
	        */
	       private void shareMult(SHARE_MEDIA[] platforms) {
//	           SHARE_MEDIA[] platforms = new SHARE_MEDIA[] {
//	                   /*SHARE_MEDIA.SINA, */SHARE_MEDIA.TENCENT/*, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN*/
//	           };
	           mController.postShareMulti(evaluateActivity, new MulStatusListener() {

	               @Override
	               public void onStart() {
	               }

	               @Override
	               public void onComplete(MultiStatus multiStatus, int st, SocializeEntity entity) {
	                   String showText = "分享结果：" + multiStatus.toString();
//	                   Toast.makeText(evaluateActivity, showText, Toast.LENGTH_SHORT).show();
	               }
	           }, platforms);
	       }
	       
	       
	   	private void getDate() {//这里估计需要初始化请求分享平台状态
			String cellphone = spUtil.getString("cellphone", "");
			int userid = spUtil.getInt("userid", 0);
			if(!"".equals(cellphone)&&0!=userid){
				CommUtil.loginAuto(this,spUtil.getString("cellphone", ""), Global.getIMEI(this), 
						Global.getCurrentVersion(this), spUtil.getInt("userid", 0), 
						0,0,autoLoginHandler);
			}
			
			CommUtil.getShare(this,System.currentTimeMillis(),getShare);
		}
		private AsyncHttpResponseHandler autoLoginHandler = new AsyncHttpResponseHandler()
		{

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					JSONObject jobj = new JSONObject(new String(responseBody));
					int resultCode = jobj.getInt("code");
					if (0 == resultCode) {
						if(jobj.has("data") && !jobj.isNull("data")){
						JSONObject jData = jobj.getJSONObject("data");
						if(jData.has("user")&&!jData.isNull("user")){
							JSONObject jUser = jData.getJSONObject("user");
							if (jUser.has("areacode") && !jUser.isNull("areacode")) {
								spUtil.saveInt("upRegionId",
										jUser.getInt("areacode"));
							} else {
								spUtil.removeData("upRegionId");
							}
							if (jUser.has("shopName") && !jUser.isNull("shopName")) {
								spUtil.saveString("upShopName",
										jUser.getString("shopName"));
							} else {
								spUtil.removeData("upShopName");
							}
							if (jUser.has("shopId") && !jUser.isNull("shopId")) {
								spUtil.saveInt("shopid", jUser.getInt("shopId"));
							} else {
								spUtil.removeData("shopid");
							}
							if (jUser.has("areaId") && !jUser.isNull("areaId")) {
								spUtil.saveInt("areaid", jUser.getInt("areaId"));
							} else {
								spUtil.removeData("areaid");
							}
							if (jUser.has("areaName") && !jUser.isNull("areaName")) {
								spUtil.saveString("areaname",
										jUser.getString("areaName"));
							} else {
								spUtil.removeData("areaname");
							}
							if (jUser.has("homeAddress")
									&& !jUser.isNull("homeAddress")) {
								JSONObject jAddr = jUser
										.getJSONObject("homeAddress");
								if (jAddr.has("Customer_AddressId")
										&& !jAddr.isNull("Customer_AddressId")) {
									spUtil.saveInt("addressid",
											jAddr.getInt("Customer_AddressId"));
								}
								if (jAddr.has("lat") && !jAddr.isNull("lat")) {
									spUtil.saveString("lat", jAddr.getDouble("lat")
											+ "");
								}
								if (jAddr.has("lng") && !jAddr.isNull("lng")) {
									spUtil.saveString("lng", jAddr.getDouble("lng")
											+ "");
								}
								if (jAddr.has("address")
										&& !jAddr.isNull("address")) {
									spUtil.saveString("address",
											jAddr.getString("address"));
								}
							} else {
								spUtil.removeData("addressid");
								spUtil.removeData("lat");
								spUtil.removeData("lng");
								spUtil.removeData("address");
							}
							if (jUser.has("pet") && !jUser.isNull("pet")) {
								JSONObject jPet = jUser.getJSONObject("pet");
								if (jPet.has("isCerti") && !jPet.isNull("isCerti")) {
									spUtil.saveInt("isCerti",
											jPet.getInt("isCerti"));
								}
							} else {
								spUtil.removeData("isCerti");
							}
							//----START
							if (jUser.has("qqShareStatus")&&!jUser.isNull("qqShareStatus")) {
								qqShareStatus = jUser.getInt("qqShareStatus");
//								spUtil.saveInt("qqShareStatus", jUser.getInt("qqShareStatus"));
							}
							if (jUser.has("weiboShareStatus")&&!jUser.isNull("weiboShareStatus")) {
								weiboShareStatus = jUser.getInt("weiboShareStatus");
//								spUtil.saveInt("weiboShareStatus", jUser.getInt("weiboShareStatus"));
							}
//							getStatusFromNet();  
						}
					}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
			}
			
		};
		private AsyncHttpResponseHandler getShare = new AsyncHttpResponseHandler(){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				Utils.mLogError("==-->str "+new String(responseBody));
				try {
					JSONObject jsonObject = new JSONObject(new String(responseBody));
					int code = jsonObject.getInt("code");
					if (code==0) {
						if (jsonObject.has("data")&&!jsonObject.isNull("data")) {
							JSONObject object = jsonObject.getJSONObject("data");
							shareList.add(Share.json2Entity(object));
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
		
		//订单明细
		private AsyncHttpResponseHandler getOrderDetails = new AsyncHttpResponseHandler(){

			private int serviceloc;

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				pDialog.closeDialog();
				Utils.mLogError("订单详情："+new String(responseBody));
				try {
					JSONObject jsonObject = new JSONObject(new String(responseBody));
					int code = jsonObject.getInt("code");
					String msg = jsonObject.getString("msg");
					if (code==0&&jsonObject.has("data")&&!jsonObject.isNull("data")) {
						JSONObject object = jsonObject.getJSONObject("data");
						if (object.has("type")&&!object.isNull("type")) {
							type = object.getInt("type");
							if (type==1) {
//								linearLayout_grade_one.setVisibility(View.VISIBLE);
//								linearLayout_grade_two.setVisibility(View.VISIBLE);
//								linearLayout_grade_thr.setVisibility(View.VISIBLE);
//								textView_major_grade.setText("专业程度");
//								textView_service_attitude.setText("服务态度");
//								textView_punctuality_grade.setText("守时程度");
//								linearLayout_grade_thr.setVisibility(View.VISIBLE);
//								textView_evalute_old_price.setVisibility(View.GONE);
//								view_evalute_old_price.setVisibility(View.GONE);
							}else if (type==2) {
//								linearLayout_grade_one.setVisibility(View.VISIBLE);
//								linearLayout_grade_two.setVisibility(View.VISIBLE);
//								linearLayout_grade_thr.setVisibility(View.GONE);
//								textView_major_grade.setText("寄养环境");
//								textView_service_attitude.setText("服务态度");
//								textView_evalute_old_price.setVisibility(View.GONE);
//								view_evalute_old_price.setVisibility(View.GONE);
							}
						}
						if (object.has("areaId")&&!object.isNull("areaId")) {
							areaId = object.getInt("areaId");
						}
						if (object.has("pet")&&!object.isNull("pet")) {
							JSONObject  object2 = object.getJSONObject("pet");
							StringBuilder builder = new StringBuilder();
							if (object2.has("petName")&&!object2.isNull("petName")) {
								builder.append(object2.getString("petName"));
							}
							if (object.has("petServicePojo")&&!object.isNull("petServicePojo")) {
								JSONObject petServicePojo = object.getJSONObject("petServicePojo");
								if (petServicePojo.has("name")&&!petServicePojo.isNull("name")) {
									serviceName = petServicePojo.getString("name");
									builder.append(petServicePojo.getString("name"));
								}
							}
							textView_evaluate_dog_name.setText(builder.toString());
							if (object2.has("avatarPath")&&!object2.isNull("avatarPath")) {
								String avatarPath = object2.getString("avatarPath");
								imageView_evaluate_dog_icon.setTag(CommUtil.getServiceNobacklash()+avatarPath);
								ImageLoaderUtil.loadImg(CommUtil.getServiceNobacklash()+avatarPath,imageView_evaluate_dog_icon,0, new ImageLoadingListener() {
									
									@Override
									public void onLoadingStarted(String arg0, View view) {
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
						
						
						if (object.has("listPrice")&&!object.isNull("listPrice")) {
//							textView_evalute_old_price.setText("原价：¥ "+Utils.formatDouble(object.getDouble("listPrice"), 2));
						}
//						if (object.has("totalPrice")&&!object.isNull("totalPrice")) {
//							textView_evalute_price.setText("¥ "+
//							Utils.formatDouble(object.getDouble("totalPrice"), 2));
//						}
						if (object.has("payPrice")&&!object.isNull("payPrice")) {
							payfee = object.getDouble("payPrice");
//							payfee = Utils.formatDouble(object.getDouble("payPrice"), 2);
							Utils.mLogError("==--1112 "+payfee);
//							textView_evalute_price.setText("¥ "+Utils.formatDouble(object.getDouble("payPrice"), 2));
						}
//						if (object.has("updateOrder")&&!object.isNull("updateOrder")) {
//							JSONObject jupdateorder = object.getJSONObject("updateOrder");
//							if (jupdateorder.has("extraItemPrice")&&!jupdateorder.isNull("extraItemPrice")) {
//								upgradefee = Utils.formatDouble(object.getDouble("extraItemPrice"), 2);
//								Utils.mLogError("==--1113 "+upgradefee);
//							}
//						}
						if (object.has("updateOrder")&&!object.isNull("updateOrder")) {
							JSONObject jupdateorder = object.getJSONObject("updateOrder");
							if(jupdateorder.has("extraItemPrice")&&!jupdateorder.isNull("extraItemPrice")){
								upgradefee = jupdateorder.getDouble("extraItemPrice");
								Utils.mLogError("==--1113 "+upgradefee);
							}
						}
						textView_evalute_price.setText("¥ "+Utils.formatDouble(payfee+upgradefee, 2));
						/* 合并展示
						if (object.has("petServicePojo")&&!object.isNull("petServicePojo")) {
							JSONObject petServicePojo = object.getJSONObject("petServicePojo");
							if (petServicePojo.has("name")&&!petServicePojo.isNull("name")) {
								textView_evaluate_service_grade.setText(petServicePojo.getString("name"));
							}
						}*/
						
						
					}else{
						if(jsonObject.has("msg")&&!jsonObject.isNull("msg")){
							ToastUtil.showToastShort(EvaluateActivity.this,
									jsonObject.getString("msg"));
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
				pDialog.closeDialog();
			}
			
		};
		
		
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			unregisterReceiver(receiver);
		}
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
				goBack();
			}
			return super.onKeyDown(keyCode, event);
		}
		private void showBackDialog(){
			MDialog mDialog = new MDialog.Builder(evaluateActivity)
			.setTitle("提示")
			.setType(MDialog.DIALOGTYPE_CONFIRM)
			.setMessage("离开页面后填写的内容会消失，确定离开？")
			.setCancelStr("取消")
			.setOKStr("确定")
			.positiveListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setResult(RESULT_OK);
					finishWithAnimation();
				}
			}).build();
			mDialog.show();
		}
		
		private AsyncHttpResponseHandler newPost = new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				Utils.mLogError("==-->发帖:"+new String(responseBody));
				try {
					JSONObject object = new JSONObject(new String(responseBody));
					int code = object.getInt("code");
					if (code==0) {
//						ToastUtil.showToastShortCenter(mContext, "发帖成功~");
//						Intent data = new Intent();
						Intent intentStatus = new Intent();
						intentStatus.setAction("android.intent.action.OrderDetailFromOrderToConfirmActivity");
						intentStatus.putExtra("index", 1);
						sendBroadcast(intentStatus);
						
						setResult(Global.RESULT_OK);
						finishWithAnimation();
					}else {
						finishWithAnimation();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					try {
						mPDialog.closeDialog();
						finishWithAnimation();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				try {
					mPDialog.closeDialog();
					finishWithAnimation();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		
		};
		
		
		
		private void getLuban(String path) {
			Luban.get(this).load(new File(path)).putGear(Luban.THIRD_GEAR)
					.setCompressListener(new OnCompressListener() {

						@Override
						public void onSuccess(File file) {
							// TODO Auto-generated method stub
							mPDialog.closeDialog();
//							Bitmap bm = Utils.getxtsldraw(evaluateActivity, file.getAbsolutePath());
//							if (null != bm && !"".equals(bm)) {
//								imgList.add(bm);
//							}
							listFile.add(file);
//							ImgAdapter.notifyDataSetChanged();
						}

						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							mPDialog.showDialog();
						}

						@Override
						public void onError(Throwable e) {

						}
					}).launch();
		}
		
		private void tqDialog() {
			new UserNameAlertDialog(mContext).builder().setTitle("没昵称我  \"蓝瘦\"")
					.setTextViewHint("请填写昵称").setCloseButton(new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).setComplaintsButton("保	 存", new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								mPDialog.showDialog();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Utils.mLogError("==-->点击了保存 0 cellphone:= "+ spUtil.getString("cellphone", "0")+" userid:= "+spUtil.getInt("userid", 0)+" getUserName:= "+UserNameAlertDialog.getUserName());
							CommUtil.updateUser(spUtil.getString("cellphone", "0"),
									Global.getIMEI(mContext), mContext,
									spUtil.getInt("userid", 0),
									UserNameAlertDialog.getUserName(),null,
									updateUser);
						}
					}).show();
		}
		private AsyncHttpResponseHandler updateUser = new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				Utils.mLogError("==-->点击了保存 1");
				try {
					mPDialog.closeDialog();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					JSONObject jsonObject = new JSONObject(new String(responseBody));
					int code = jsonObject.getInt("code");
					if (code == 0) {
						isNickName = true;
						ToastUtil.showToastShortCenter(mContext, "创建成功");
						spUtil.saveString("username",UserNameAlertDialog.getUserName());
						try {
							goneJp();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						String msg = jsonObject.getString("msg");
						ToastUtil.showToastShortCenter(mContext, msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.mLogError("==-->点击了保存 2 "+e.getMessage());
					try {
						mPDialog.closeDialog();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				try {
					mPDialog.closeDialog();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		};
		
		private AsyncHttpResponseHandler ratingHandler = new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				try {
					JSONObject object = new JSONObject(new String(responseBody));
					int code = object.getInt("code");
					if (code==0) {
						commentStars.clear();
						if (object.has("data")&&!object.isNull("data")) {
							JSONObject objectData = object.getJSONObject("data");
							if (objectData.has("commentGradeCopy")&&!objectData.isNull("commentGradeCopy")) {
								textview_show_last_eva.setVisibility(View.VISIBLE);
								String commentGradeCopy = objectData.getString("commentGradeCopy");
								textview_show_last_eva.setText(commentGradeCopy);
							}
							if (objectData.has("commentTag")&&!objectData.isNull("commentTag")) {
								JSONArray arrayCommentTag = objectData.getJSONArray("commentTag");
								if (arrayCommentTag.length()>0) {
									for (int i = 0; i < arrayCommentTag.length(); i++) {
										commentStars.add(CommentStar.json2Entity(arrayCommentTag.getJSONObject(i)));
									}
								}
							}
						}
					}
					if (commentStars.size()>0) {
						starAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
			}
		};
		private String getCommentIds() {
			String commentIds = null;
			if (listChoose.size()>0) {
				StringBuilder commentTagids = new StringBuilder();
				for (int i = 0; i < listChoose.size(); i++) {
					commentTagids.append(listChoose.get(i)+",");
				}
				commentIds = commentTagids.substring(0, commentTagids.length()-1);
			}else {
				commentIds = null;
			}
			return commentIds;
		}
		private void goneJp() {
			// 强制收起键盘
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.imageview_eva_one:
				setBack(1);
				one=1;
				break;
			case R.id.imageview_eva_two:
				setBack(2);
				one=2;
				break;
			case R.id.imageview_eva_thr:
				setBack(3);
				one=3;
				break;
			case R.id.imageview_eva_four:
				setBack(4);
				one=4;
				break;
			case R.id.imageview_eva_five:
				setBack(5);
				one=5;
				break;
			}
			getRatingBar();
		}
		private void setBack(int num){
			switch (num) {
			case 1:
				imageview_eva_one.setImageResource(R.drawable.star_full);
				imageview_eva_two.setImageResource(R.drawable.star_empty);
				imageview_eva_thr.setImageResource(R.drawable.star_empty);
				imageview_eva_four.setImageResource(R.drawable.star_empty);
				imageview_eva_five.setImageResource(R.drawable.star_empty);
				break;
			case 2:
				imageview_eva_one.setImageResource(R.drawable.star_full);
				imageview_eva_two.setImageResource(R.drawable.star_full);
				imageview_eva_thr.setImageResource(R.drawable.star_empty);
				imageview_eva_four.setImageResource(R.drawable.star_empty);
				imageview_eva_five.setImageResource(R.drawable.star_empty);
				break;
			case 3:
				imageview_eva_one.setImageResource(R.drawable.star_full);
				imageview_eva_two.setImageResource(R.drawable.star_full);
				imageview_eva_thr.setImageResource(R.drawable.star_full);
				imageview_eva_four.setImageResource(R.drawable.star_empty);
				imageview_eva_five.setImageResource(R.drawable.star_empty);
				break;
			case 4:
				imageview_eva_one.setImageResource(R.drawable.star_full);
				imageview_eva_two.setImageResource(R.drawable.star_full);
				imageview_eva_thr.setImageResource(R.drawable.star_full);
				imageview_eva_four.setImageResource(R.drawable.star_full);
				imageview_eva_five.setImageResource(R.drawable.star_empty);
				break;
			case 5:
				imageview_eva_one.setImageResource(R.drawable.star_full);
				imageview_eva_two.setImageResource(R.drawable.star_full);
				imageview_eva_thr.setImageResource(R.drawable.star_full);
				imageview_eva_four.setImageResource(R.drawable.star_full);
				imageview_eva_five.setImageResource(R.drawable.star_full);
				break;
			}
		}
}
