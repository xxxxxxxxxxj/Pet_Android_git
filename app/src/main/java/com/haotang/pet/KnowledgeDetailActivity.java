package com.haotang.pet;

import com.haotang.base.SuperActivity;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.pulltorefresh.PullToRefreshWebView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.Utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.ImageButton;
import android.widget.TextView;

public class KnowledgeDetailActivity extends SuperActivity {
	private ImageButton ibBack;
	private TextView tvTitle;
	private TextView tvKnowledgeTitle;
	private PullToRefreshWebView prwbWeb;
	private WebView mWebView;
	private Intent fIntent;
	private String knowledgeTitle;
	private String path;
	private MProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.knowledgedetail);
		mDialog = new MProgressDialog(this);
		mDialog.showDialog();
		findView();
		setView();
	}

	private void findView() {
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		tvKnowledgeTitle = (TextView) findViewById(R.id.tv_knowledgedetail_title);
		prwbWeb = (PullToRefreshWebView) findViewById(R.id.prwb_knowledgedetail_web);
		
	}
	
	protected void onReceivedTitle(WebView view, String title) {
		tvTitle.setText(title);
	}

	private void setView() {
		// TODO Auto-generated method stub
		tvTitle.setText("加载中...");
		fIntent = getIntent();
		knowledgeTitle = fIntent.getStringExtra("title");
		path = fIntent.getStringExtra("path");
		
		tvKnowledgeTitle.setText(knowledgeTitle);
		prwbWeb.setMode(Mode.PULL_FROM_START);
		mWebView = prwbWeb.getRefreshableView();
		WebSettings settings = mWebView.getSettings();
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
		mWebView.setLongClickable(true);
		mWebView.setScrollbarFadingEnabled(true);
		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		mWebView.setDrawingCacheEnabled(true);
		settings.setAppCacheEnabled(true);
		settings.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				KnowledgeDetailActivity.this.onReceivedTitle(view, title);
			}
		});
		
		mWebView.setWebViewClient(new WebViewClient(){

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
		mWebView.loadUrl(CommUtil.getServiceNobacklash()+path);
		prwbWeb.setOnRefreshListener(new OnRefreshListener<WebView>() {

			@Override
			public void onRefresh(PullToRefreshBase<WebView> refreshView) {
				// TODO Auto-generated method stub
				Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					mWebView.loadUrl(CommUtil.getServiceNobacklash()+path);
				}
			}
		});
		
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
	}
}
