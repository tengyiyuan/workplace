package com.toplion.cplusschool.Wangyi;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.DownloadListener;

import com.toplion.cplusschool.Activity.BaseActivity;

/**
 * Created by toplion on 2016/11/4.
 */
public class MukeHTML extends BaseActivity {
    private HTML5WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new HTML5WebView(this);

        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        } else {
            mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        }
        setContentView(mWebView.getLayout());
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        //添加监听事件即可
        public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                    String mimetype, long contentLength) {
            Uri uri = Uri.parse(getIntent().getStringExtra("url"));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mWebView.stopLoading();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.inCustomView()) {
                mWebView.hideCustomView();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
