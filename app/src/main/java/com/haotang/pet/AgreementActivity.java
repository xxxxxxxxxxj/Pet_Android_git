package com.haotang.pet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.haotang.base.SuperActivity;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.SharedPreferenceUtil;
import com.haotang.pet.view.AdvancedWebView;

public class AgreementActivity extends SuperActivity  implements AdvancedWebView.Listener{
	private ImageButton ibBack;
	private TextView tvTitle;
	private String url;
	private AdvancedWebView mWebView;
	private MProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adbanner);
		mDialog = new MProgressDialog(this);
		mDialog.showDialog();
		long time = System.currentTimeMillis();
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		mWebView = (AdvancedWebView) findViewById(R.id.wv_adbanner);
		tvTitle.setText("加载中...");
		if(getIntent().getIntExtra("previous", 0)==Global.FOSTERCARE_TO_AGREEMENT){
			url = CommUtil.getServiceBaseUrl()+"static/content/jy.html?system="+
					CommUtil.getSource() + "_" + Global.getCurrentVersion(this)+"&imei="+
					Global.getIMEI(this)+"&cellPhone="+
					SharedPreferenceUtil.getInstance(this).getString("cellphone", "")+"&time="+time;
		}else{
			url = CommUtil.getServiceBaseUrl()+"static/content/protocol.html?system="+
					CommUtil.getSource() + "_" + Global.getCurrentVersion(this)+"&imei="+
					Global.getIMEI(this)+"&cellPhone="+
					SharedPreferenceUtil.getInstance(this).getString("cellphone", "")+"&time="+time;
		}
		initWebView();
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				AgreementActivity.this.onReceivedTitle(view, title);
			}
		});

		mWebView.setWebViewClient(new WebViewClient() {

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
		mWebView.addHttpHeader("X-Requested-With", "");
		mWebView.loadUrl(url);

		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});

	}

	private void initWebView() {
	    mWebView.setListener(this, this);
		mWebView.setGeolocationEnabled(false);
		mWebView.setMixedContentAllowed(true);
		mWebView.setCookiesEnabled(true);
		mWebView.setThirdPartyCookiesEnabled(true);
		}

	protected void onReceivedTitle(WebView view, String title) {
		tvTitle.setText(title);
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		mWebView.onResume();
		// ...
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPause() {
		mWebView.onPause();
		// ...
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mWebView.onDestroy();
		// ...
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		mWebView.onActivityResult(requestCode, resultCode, intent);
		// ...
	}

	@Override
	public void onBackPressed() {
		if (!mWebView.onBackPressed()) { return; }
		// ...
		super.onBackPressed();
	}

	@Override
	public void onPageStarted(String url, Bitmap favicon) {
		mWebView.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onPageFinished(String url) {
		mWebView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPageError(int errorCode, String description, String failingUrl) {
		Toast.makeText(this, "onPageError(errorCode = "+errorCode+",  description = "+description+",  failingUrl = "+failingUrl+")", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
		Toast.makeText(this, "onDownloadRequested(url = "+url+",  userAgent = "+userAgent+",  contentDisposition = "+contentDisposition+",  mimetype = "+mimetype+",  contentLength = "+contentLength+")", Toast.LENGTH_LONG).show();

		/*final String filename = UUID.randomUUID().toString();

		if (AdvancedWebView.handleDownload(this, url, filename)) {
			// download successfully handled
		}
		else {
			// download couldn't be handled because user has disabled download manager app on the device
		}*/
	}

	@Override
	public void onExternalPageRequest(String url) {
		Toast.makeText(this, "onExternalPageRequest(url = "+url+")", Toast.LENGTH_SHORT).show();
	}
}
