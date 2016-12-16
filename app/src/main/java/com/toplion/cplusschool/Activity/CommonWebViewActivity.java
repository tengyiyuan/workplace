package com.toplion.cplusschool.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.MyWebChromeClient;
import com.toplion.cplusschool.Utils.ToastManager;

/**
 * 公用WebView
 * @date 2016-6-21
 * @author WangShengbo
 *
 */
public class CommonWebViewActivity extends BaseActivity{
	private WebView webview;                // WebView
	private ImageView about_iv_return;      // 返回
	private TextView tv_webview_title;        // 标题
	private ProgressBar pb_webview;//等待框
	private String js="<script type=\"text/javascript\">\n" +
			" window.onload=function(){\n" +
			"var myimg;\n" +
			"var w=window.innerWidth-10;\n" +
			"  var images=document.getElementsByTagName(\"img\");\n" +
			"  var imgLen=images.length;\n" +
			"  for(var i=0;i<imgLen;i++){\n" +
			"  if(images[i].width>w){\n" +
			"var shijiheight=w*images[i].height/images[i].width;\n" +
			"images[i].width=w;\n" +
			"images[i].height=shijiheight;\n" +
			"}\n" +
			"\n" +
			"  }\t\n" +
			" }\n" +
			"\n" +
			"</script>";
	private  String innetsp="    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
			"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">";

	private String css = "<style>"+
			"img{" +
			"max-width:100%;" +
			"height:auto}" +

			"video{"+
			"max-width:100%;"+
			"height:auto}"+
			"</style>" ;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_webview);
		String url = getIntent().getStringExtra("url");
		String data = getIntent().getStringExtra("data");
		String title = getIntent().getStringExtra("title");
		// 返回上一级
		about_iv_return = (ImageView) this.findViewById(R.id.about_iv_return);

		tv_webview_title = (TextView) findViewById(R.id.tv_webview_title);
		pb_webview = (ProgressBar) findViewById(R.id.pb_webview);
		if(!TextUtils.isEmpty(title)){
			tv_webview_title.setText(title);
		}
		webview = (WebView) findViewById(R.id.about_webview);
		WebSettings webSettings = webview.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
		//自适应屏幕
		webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		MyWebChromeClient myWebChromeClient = new MyWebChromeClient();
		webview.setWebChromeClient(myWebChromeClient);
		// 加载需要显示的网页
		if(!TextUtils.isEmpty(url)){
			webview.loadUrl(url);
		}else{
			webview.loadDataWithBaseURL(null, "<head>" + innetsp + css + "</head><body>" + data + "</body>", "text/html", "UTF-8", null);
		}
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
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			pb_webview.setVisibility(View.VISIBLE);
			webview.setEnabled(false);
			super.onPageStarted(view, url, favicon);
		}

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.e("url",url);
			view.loadUrl(url);
			return true;
		}
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			view.loadUrl("file:///android_asset/error.html");
			ToastManager.getInstance().showToast(CommonWebViewActivity.this,"网络异常");
			return;
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			//tv_webview_title.setText(view.getTitle());
			pb_webview.setVisibility(View.GONE);
			webview.setEnabled(true);
			super.onPageFinished(view, url);
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

	public static void clearCookies(Context context) {
// Edge case: an illegal state exception is thrown if an instance of
// CookieSyncManager has not be created. CookieSyncManager is normally
// created by a WebKit view, but this might happen if you start the
// app, restore saved state, and click logout before running a UI
// dialog in a WebView -- in which case the app crashes
		@SuppressWarnings("unused")
		CookieSyncManager cookieSyncMngr =
				CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

}
