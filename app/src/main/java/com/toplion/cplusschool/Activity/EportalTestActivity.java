package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ConnectivityUtils;
import com.toplion.cplusschool.Utils.EportalUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EportalTestActivity extends BaseActivity implements
		OnClickListener {
	private Button tv_online;// 在线信息
	private Button tv_keeplive;// 保活
	private Button tv_eportal_inof;// eportal配置信息
	private JSONObject json;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eportal_test_activity);
		init();
		setListener();
	}

	@Override
	public void init() {
		tv_online = (Button) findViewById(R.id.tv_online);
		tv_keeplive = (Button) findViewById(R.id.tv_keeplive);
		tv_eportal_inof = (Button) findViewById(R.id.tv_eportal_inof);
	}

	@Override
	public void setListener() {
		tv_online.setOnClickListener(this);
		tv_keeplive.setOnClickListener(this);
		tv_eportal_inof.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_online://在线
				new Thread(){
					public void run() {
						String userId = ConnectivityUtils.getSysIp(EportalTestActivity.this);
						Map<String,String> map = new HashMap<String, String>();
						map.put("userip", userId+"");
						map.put("userIndex", Constants.userIndex);
						String baseUrl = Constants.baseUrl + "getOnlineUserInfo";
						json = EportalUtils.httpClientForPost(baseUrl,map);
						Log.d("json=====>", json.toString());
					}
				}.start();
				break;
			case R.id.tv_keeplive://保活
				new Thread(){
					public void run() {
						String userId = ConnectivityUtils.getSysIp(EportalTestActivity.this);
						Map<String,String> map = new HashMap<String, String>();
						map.put("userip", userId+"");
						map.put("userIndex", Constants.userIndex);

						String baseUrl = Constants.baseUrl + "keepalive";
						json = EportalUtils.httpClientForPost(baseUrl,map);
						Log.d("json=====>", json.toString());
					}
				}.start();

				break;
			case R.id.tv_eportal_inof://配置信息
				WebView webview;
				webview = new WebView(this);
				webview.layout(0, 0, 0, 0);
				final WebSettings settings = webview.getSettings();
				new Thread(){
					public void run() {
						String userId = ConnectivityUtils.getSysIp(EportalTestActivity.this);
						String ua = settings.getUserAgentString();//获取UserAgent
						String baseUrl = Constants.baseUrl + "pageInfo";
						Map<String,String> map = new HashMap<String, String>();
						map.put("userip", userId+"");
						map.put("userAgent",ua);
						map.put("queryString",Constants.queryString);
						json = EportalUtils.httpClientForPost(baseUrl,map);
						Log.d("json=====>", json.toString());
					}
				}.start();

				break;

			default:
				break;
		}
	}

}
