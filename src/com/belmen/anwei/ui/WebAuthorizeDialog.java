package com.belmen.anwei.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.belmen.anwei.R;

public class WebAuthorizeDialog extends Dialog {
	
	private String mUrl;
	private String callbackUrl;
	private AuthorizeDialogListener mListener;
	private WebView mWebView;
	private ProgressDialog mSpinner;
	private RelativeLayout mDialogContent;
	private RelativeLayout webViewContainer;

	public WebAuthorizeDialog(Context context, String url, String callbackUrl,
			AuthorizeDialogListener listener) {
		super(context, R.style.authorization_dialog);
		this.mUrl = url;
		this.callbackUrl = callbackUrl;
		this.mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("加载网页中，请稍候……");
		
		mDialogContent = new RelativeLayout(getContext());
		
		setupWebView();

        addContentView(mDialogContent, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mWebView.clearFocus();
	}

	private void setupWebView() {
		webViewContainer = new RelativeLayout(getContext());
		
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WeiboConfigDialogViewClient());
		mWebView.loadUrl(mUrl);
		mWebView.setVisibility(View.INVISIBLE);
		webViewContainer.addView(mWebView);
        
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        Resources resources = getContext().getResources();
        lp.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_left_margin);
        lp.topMargin = resources.getDimensionPixelSize(R.dimen.dialog_top_margin);
        lp.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_right_margin);
        lp.bottomMargin = resources.getDimensionPixelSize(R.dimen.dialog_bottom_margin);
        mDialogContent.addView(webViewContainer, lp);
	}
	
	private class WeiboConfigDialogViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if(url.startsWith(callbackUrl)) {
				view.stopLoading();
                mListener.onFinished(url);
                dismiss();
                mWebView.clearFocus();
                return;
			}
			super.onPageStarted(view, url, favicon);
			mSpinner.show();
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mSpinner.dismiss();
			mDialogContent.setBackgroundColor(Color.TRANSPARENT);
			webViewContainer.setBackgroundResource(android.R.drawable.dialog_frame);
			mWebView.setVisibility(View.VISIBLE);
			if(url.equals(mUrl))
				mWebView.requestFocusFromTouch();	// 解决输入框无响应问题
		}
		
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed();	// 忽略证书错误
		}
	}

}
