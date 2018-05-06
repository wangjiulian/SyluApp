package com.dpl.syluapp.utils;

import com.dpl.syluapp.AppActivity;
import com.dpl.syluapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/* 
 *@author: ZhengHaibo   
 *web:     blog.csdn.net/nuptboyzhb 
 *mail:    zhb931706659@126.com 
 *2012-8-31  Nanjing njupt 
 */
public class WebBrowser extends AppActivity implements OnClickListener {
	WebView mWebView;
	Button goButton;
	Button backButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_browser);
		String url = getIntent().getStringExtra("url");
		setTitle("WebBrowser Made by Zhenghaibo");
		setControl();
		setWebStyle();
		mWebView.loadUrl(url);
	}

	private void setControl() {
		mWebView = (WebView) findViewById(R.id.webshow);
		goButton = (Button) findViewById(R.id.GoBtn);
		backButton = (Button) findViewById(R.id.BackBtn);
		goButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
	}

	private void setWebStyle() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.requestFocus();
		mWebView.loadUrl("http://blog.csdn.net/nuptboyzhb/");
		mWebView.setWebViewClient(new MyWebViewClient());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.GoBtn:
			mWebView.goForward();

			break;
		case R.id.BackBtn:
			mWebView.goBack();
			break;
		}
	}

	class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url_) {
			view.loadUrl(url_);
			return true;
		}
	}
}
