package com.dpl.syluapp.utils;



import com.dpl.syluapp.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class WebBrowser1 extends Activity {
	private WebView mWebView;
	EditText urll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String url = getIntent().getStringExtra("url");
		Toast.makeText(WebBrowser1.this, url, 1).show();
		setContentView(R.layout.web);
		urll = (EditText) findViewById(R.id.urltext);
		mWebView = (WebView) findViewById(R.id.web);
		setWebStyle();
		mWebView.loadUrl(url);
	}

	private void setWebStyle() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.requestFocus();
		mWebView.loadUrl("http://blog.csdn.net/nuptboyzhb/");
		mWebView.setWebViewClient(new MyWebViewClient());
	}

	class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url_) {
			view.loadUrl(url_);
			urll.setText(url_);
			return true;
		}
	}
}