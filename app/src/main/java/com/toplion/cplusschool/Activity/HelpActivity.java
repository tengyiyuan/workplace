package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;

/**
 * 帮助界面界面
 * 使用WebView
 * 加载页面Constants.ABOUT_SOFT_URL
 * @author WangShengbo
 *
 */
public class HelpActivity extends BaseActivity{
	private WebView webview;                // WebView
	private ImageView about_iv_return;      // 返回
	private SharePreferenceUtils share;
	private TextView about_iv_Title;        // 标题

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_soft);
		share = new SharePreferenceUtils(this);

		// 返回上一级
		about_iv_return = (ImageView) this.findViewById(R.id.about_iv_return);

		about_iv_Title = (TextView) findViewById(R.id.about_iv_Title);
		about_iv_Title.setText(this.getResources().getString(R.string.usehelp));
		webview = (WebView) findViewById(R.id.about_webview);
		WebSettings webSettings = webview.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		String url = getIntent().getExtras().getString("URL");
		// 加载需要显示的网页
		webview.loadUrl("http://" + share.getString("serverIp", "") + url);
		// 设置Web视图
		webview.setWebViewClient(new webViewClient());
		about_iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(webview.canGoBack()){
					webview.goBack();
				}else{
					// 关闭当前Activity
					finish();
				}
			}
		});
	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void init() { }

	@Override
	public void setListener() { }
}
