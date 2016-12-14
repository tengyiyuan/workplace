package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;

/**
 * 购买协议界面
 * 使用WebView
 * 加载页面Constants.AGREEMENT_URL
 * @author liyb
 *
 */
public class AgreementActivity extends BaseActivity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agreement);
		SharePreferenceUtils prefer = new SharePreferenceUtils(this);
		// 返回上一级
		ImageView agree_iv_return = (ImageView) this.findViewById(R.id.agree_iv_return);
		agree_iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		WebView webview = (WebView) findViewById(R.id.agree_webview);
		WebSettings webSettings = webview.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		// 加载需要显示的网页
		webview.loadUrl("http://"+ prefer.getString("serverIp", "")+Constants.AGREEMENT_URL);
		// 设置Web视图
		webview.setWebViewClient(new webViewClient());

	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public void init() {

	}

	@Override
	public void setListener() {

	}
}
