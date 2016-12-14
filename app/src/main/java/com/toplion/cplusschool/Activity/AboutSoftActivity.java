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
 * 关于本软件界面
 * 使用WebView
 * 加载页面Constants.ABOUT_SOFT_URL
 * @author liyb
 *
 */
public class AboutSoftActivity extends BaseActivity{
	private WebView webview;                // WebView
	private ImageView about_iv_return;      // 返回
	private SharePreferenceUtils prefer;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_soft);
		prefer = new SharePreferenceUtils(this);
		// 隐藏ActtionBar
		// 返回上一级
		about_iv_return = (ImageView) this.findViewById(R.id.about_iv_return);
		about_iv_return.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 关闭当前页面
				finish();
			}
		});
		webview = (WebView) findViewById(R.id.about_webview);
		WebSettings webSettings = webview.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		// 加载需要显示的网页
		webview.loadUrl("http://" + prefer.getString("serverIp", "") + Constants.ABOUT_SOFT_URL);
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
	public void init() { }

	@Override
	public void setListener() { }
}
