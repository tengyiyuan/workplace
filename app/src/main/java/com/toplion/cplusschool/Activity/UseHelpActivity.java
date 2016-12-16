package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;

/**
 * 使用帮助页面
 * 使用WebView
 * 加载页面Constants.USE_HELP_URL
 * @author liyb
 *
 */
public class UseHelpActivity extends BaseActivity{
	private WebView webview;                // WebView
	private ImageView usehelp_iv_return;    // 返回
	private SharePreferenceUtils share;
	private boolean bln = false;//
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.use_help);
//		prefer = getSharedPreferences("config", Context.MODE_PRIVATE);
		share = new SharePreferenceUtils(this);
		// 返回上一级
		usehelp_iv_return = (ImageView) this.findViewById(R.id.usehelp_iv_return);


		webview = (WebView) findViewById(R.id.help_webview);
		WebSettings webSettings = webview.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		// 加载需要显示的网页
		webview.loadUrl("http://"+share.getString("serverIp", "")+Constants.USE_HELP_URL);
		// 设置Web视图
		webview.setWebViewClient(new webViewClient());
		usehelp_iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(bln){
					finish();
				}else{
					if(webview.canGoBack()){
						webview.goBack();
					}else{
						// 关闭当前Activity
						finish();
					}
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

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			view.loadUrl("file:///android_asset/error.html");
			ToastManager.getInstance().showToast(UseHelpActivity.this,"网络异常");
			bln = true;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			if(bln){
				finish();
			}else{
				webview.goBack(); // goBack()表示返回WebView的上一页面
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}
}
