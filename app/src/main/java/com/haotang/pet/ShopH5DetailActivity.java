package com.haotang.pet;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled") public class ShopH5DetailActivity extends SuperActivity {
	
	private WebView webView_shop_show;
	private String orderDetailH5Url="";
	private Button btBack;
	private MProgressDialog mDialog;
	private int previous;
	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private LinearLayout layout_one;
	private String guarantee="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_h5shop_detail);
		initView();
		previous = getIntent().getIntExtra("previous", -1);
		guarantee = getIntent().getStringExtra("guarantee");
		setView();    
		initListener();
	}
	private void initView() {
		mDialog = new MProgressDialog(this);
		mDialog.showDialog();
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		layout_one = (LinearLayout) findViewById(R.id.layout_one);
		btBack = (Button) findViewById(R.id.bt_h5_back);
		webView_shop_show = (WebView) findViewById(R.id.webView_shop_show);
	}
	private void initListener() {
		btBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
		ib_titlebar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
//		ib_titlebar_back.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
	}
	
	private void setView() {
		if (previous==Global.SWIM_APPOINMENT_TO_WEBVIEW) {
			orderDetailH5Url = getIntent().getStringExtra("orderDetailH5Url");
			tv_titlebar_title.setText("宠物家游泳协议");
			btBack.setVisibility(View.GONE);
			layout_one.setVisibility(View.VISIBLE);
		}else if (previous==Global.SWIM_DETAIL_TO_WEBVIEW) {
			orderDetailH5Url = getIntent().getStringExtra("orderDetailH5Url");
			tv_titlebar_title.setText(guarantee);
			btBack.setVisibility(View.GONE);
			layout_one.setVisibility(View.VISIBLE);
		}else if(previous==20145){
			tv_titlebar_title.setText("取消订单规则");
			layout_one.setVisibility(View.VISIBLE);
			orderDetailH5Url = getIntent().getStringExtra("rulePage");
		}
		else if(previous==Global.PET_CIRCLE_TO_H5){
			tv_titlebar_title.setText("加载中...");
			layout_one.setVisibility(View.VISIBLE);
			orderDetailH5Url = getIntent().getStringExtra("orderDetailH5Url");
		}
		else {
			layout_one.setVisibility(View.GONE);
			orderDetailH5Url = getIntent().getStringExtra("orderDetailH5Url")+"?time="+System.currentTimeMillis();
		}
		
		WebSettings settings = webView_shop_show.getSettings();
		// User settings
		settings.setJavaScriptEnabled(true); // 设置webview支持javascript
		settings.setLoadsImagesAutomatically(true); // 支持自动加载图片
		settings.setUseWideViewPort(true); // 设置webview推荐使用的窗口，使html界面自适应屏幕
		settings.setSaveFormData(true); // 设置webview保存表单数据
		settings.setSavePassword(true); // 设置webview保存密码
		settings.setDefaultZoom(ZoomDensity.MEDIUM); // 设置中等像素密度，medium=160dpi
		settings.setSupportZoom(true); // 支持缩放
		CookieManager.getInstance().setAcceptCookie(true);
		if (Build.VERSION.SDK_INT > 8) {
			settings.setPluginState(PluginState.ON_DEMAND);
		}
		settings.setSupportMultipleWindows(true);
		webView_shop_show.setLongClickable(true);
		webView_shop_show.setScrollbarFadingEnabled(true);
		webView_shop_show.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView_shop_show.setDrawingCacheEnabled(true);
		settings.setAppCacheEnabled(true);
		settings.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		
		
		webView_shop_show.setWebViewClient(new WebViewClient());
		webView_shop_show.setWebChromeClient(new WebChromeClient());
		webView_shop_show.getSettings().setJavaScriptEnabled(true);
		WebChromeClient wvcc = new WebChromeClient() {  
            @Override  
            public void onReceivedTitle(WebView view, String title) {  
                super.onReceivedTitle(view, title);  
                tv_titlebar_title.setText(title);  
            }  
  
        };  
        // 设置setWebChromeClient对象  
        webView_shop_show.setWebChromeClient(wvcc);
		webView_shop_show.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("tel:")){
	                   Intent intent = new Intent(Intent.ACTION_VIEW,
	                           Uri.parse(url));
	                   startActivity(intent);
	                   } else if(url.startsWith("http:") || url.startsWith("https:")) {
	                       view.loadUrl(url);
	                   }
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mDialog.closeDialog();
			}
			
		});
        webView_shop_show.loadUrl(orderDetailH5Url);
	}
	
}
