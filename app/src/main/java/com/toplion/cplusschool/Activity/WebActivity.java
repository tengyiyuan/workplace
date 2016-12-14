package com.toplion.cplusschool.Activity;

import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Bean.NewBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.dao.CallBackParent;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class WebActivity extends BaseActivity {
    private TextView zuozhe, laiyuan, time, newtitle;
    private WebView mWebView;
    private NewBean newbean;
    private String data = "获取新闻失败";
    private TextView tv_webview_title;
    private int style = 0;
    private RelativeLayout topzuozhe;
    private String zuozhetext;
    private String formtext;
    private String type="0";
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsinfo);
        initViews();
        getData();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void initViews() {
        ImageView about_iv_return = (ImageView) findViewById(R.id.about_iv_return);
        about_iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        style = getIntent().getIntExtra("style", 0);
        mWebView = (WebView) findViewById(R.id.webView1);
        newbean = (NewBean) getIntent().getSerializableExtra("bean");
        tv_webview_title = (TextView) findViewById(R.id.tv_webview_title);
        zuozhe = (TextView) findViewById(R.id.editzuozhe);
        laiyuan = (TextView) findViewById(R.id.messagelaiyuan);
        time = (TextView) findViewById(R.id.time);
        newtitle = (TextView) findViewById(R.id.newtitle);
        topzuozhe = (RelativeLayout) findViewById(R.id.topzuozhe);
        if (style == 0) {
            tv_webview_title.setText("新闻详情");
        } else {
            tv_webview_title.setText("讲座详情");
        }
        if (!newbean.getTime().isEmpty()) {
            time.setText("发布时间:" + newbean.getTime().substring(0, 11));
        } else {
            time.setText("发布时间:暂无");
        }
        newtitle.setText(newbean.getNews_title());
    }

    //<head><style>img{max-width:100% !important;}</style></head>
    private void loadUrl(String data) {
        // 设置对JS的支持
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        mWebView.requestFocus();// 如果不 设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。 取消滚动条
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        if(type.equals("0")) {
            mWebView.loadDataWithBaseURL("", "<head>" + innetsp + css + "</head><body>" + data + "</body>", "text/html", "UTF-8", "");
        }else{
            mWebView.loadUrl(data);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!zuozhetext.isEmpty()) {
                    zuozhe.setText(zuozhetext);
                } else {
                    zuozhe.setText("暂无");
                }
                if (!formtext.isEmpty()) {
                    laiyuan.setText(formtext);
                } else {
                    laiyuan.setText("暂无");
                }
                newtitle.setVisibility(View.VISIBLE);
                topzuozhe.setVisibility(View.VISIBLE);
                //AbDialogUtil.showProgressDialog(WebActivity.this, 0, "");
            }

            public void onPageFinished(WebView view, String url) {

                //AbDialogUtil.removeDialog(WebActivity.this);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {

            }

            public void onReceivedIcon(WebView view, Bitmap icon) {
            }

            public void onReceivedTitle(WebView view, String title) {

            }
        });

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void getData() {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("uid", newbean.getNewsID());
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showNewsById");
        abHttpUtil.post(url, params, new CallBackParent(WebActivity.this, "正在加载") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    String datastr = Function.getInstance().getString(obj, "data");
                    JSONObject dataobj = new JSONObject(datastr);
                    type=Function.getInstance().getString(dataobj, "ISSETURL");
                    if(type.equals("0")) {
                        data = Function.getInstance().getString(dataobj, "NEWS_INFO");
                    }else{
                        data = Function.getInstance().getString(dataobj, "REDIRECT_URL");
                    }
                    zuozhetext = Function.getInstance().getString(dataobj, "AUTHOR");
                    formtext = Function.getInstance().getString(dataobj, "TREE_NODE_NAME");
                    loadUrl(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
