package com.belmen.anwei;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends Activity {

	public static final String BUNDLE_URL = "url";
	public static final String BUNDLE_CALLBACK_URL = "callback";
	public static final String BUNDLE_RESULT_URL = "result";
	public static final String IDENT_OAUTH_TOKEN = "oauth_token";
	public static final String IDENT_ACCESS_TOKEN = "access_token";

	private String url;
	private String callbackUrl;
	private WebView wv;
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Bundle data = getIntent().getExtras();
		url = data.getString(BUNDLE_URL);
		callbackUrl = data.getString(BUNDLE_CALLBACK_URL);
		
		initViews();
		setupWebView();
		
	}

	private void initViews() {
		wv = (WebView) findViewById(R.id.webView1);
		
		pd = new ProgressDialog(this);
		pd.setMessage(getString(R.string.msg_loading_webpage));
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void setupWebView() {
		wv.setVerticalScrollBarEnabled(false);
        wv.setHorizontalScrollBarEnabled(false);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebViewClient(new LoginWebViewClient());
		wv.loadUrl(url);
	}
	
	private class LoginWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			if(url.startsWith(callbackUrl)
					&& (url.contains(IDENT_OAUTH_TOKEN) || url.contains(IDENT_ACCESS_TOKEN))) {
				Intent intent = new Intent();
				intent.putExtra(BUNDLE_RESULT_URL, url);
				
				setResult(RESULT_OK, intent);
				finish();
				return;
			} else {
				pd.show();
			}
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			pd.dismiss();
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed();	// 忽略证书错误
		}
		
	}
}