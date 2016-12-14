package com.toplion.cplusschool.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.ab.http.AbHttpUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Bean.RepairQuestionBean;
import com.toplion.cplusschool.Bean.RepairQuestionListBean;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;

import java.util.List;

/**
 * 常见问题解决方案详情
 *@updatetime 2016-6-30
 * @author wang
 */
public class RepairQuestionDetailActivity extends BaseActivity {
    private ImageView iv_repair_question_detail_back;//返回
    private TextView tv_fault_name;//标题
    private TextView tv_fault_content;//问题解决方法
    private WebView wv_fault_content;//
    private TextView tv_resolved;//已解决
    private TextView tv_repair_go;//去报修
    private String qId = null;//问题id
    private AbHttpUtil abHttpUtil;//网络请求框架


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_question_detail);
        init();
        setListener();
    }
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
    public void init() {
        qId = this.getIntent().getExtras().getString("qId");
        iv_repair_question_detail_back = (ImageView) findViewById(R.id.iv_repair_question_detail_back);
        tv_fault_name = (TextView) findViewById(R.id.tv_fault_name);
        tv_fault_content = (TextView) findViewById(R.id.tv_fault_content);
        wv_fault_content = (WebView) findViewById(R.id.wv_fault_content);

        tv_resolved = (TextView) findViewById(R.id.tv_resolved);
        tv_repair_go = (TextView) findViewById(R.id.tv_repair_go);
        abHttpUtil = AbHttpUtil.getInstance(RepairQuestionDetailActivity.this);
        if (!TextUtils.isEmpty(qId)) {
            getData(qId);
        }
    }
    //加载html代码片段
    private void loadUrl(String data) {
        // 设置对JS的支持
        WebSettings webSettings = wv_fault_content.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        wv_fault_content.requestFocus();// 如果不 设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。 取消滚动条
        wv_fault_content.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv_fault_content.setVerticalScrollBarEnabled(true);

        wv_fault_content.getSettings().setDefaultTextEncodingName("UTF-8");
        wv_fault_content.loadDataWithBaseURL("",  "<head>" + innetsp + css + "</head><body>" + data + "</body>", "text/html", "UTF-8", "");
        wv_fault_content.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(!url.contains("tel:")){
                    view.loadUrl(url);
                }
                return true;
            }

            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            public void onPageFinished(WebView view, String url) {

                //AbDialogUtil.removeDialog(WebActivity.this);
            }
        });

        wv_fault_content.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {

            }

            public void onReceivedIcon(WebView view, Bitmap icon) {
            }

            public void onReceivedTitle(WebView view, String title) {

            }
        });

    }

    /**
     * 获取数据
     */
    private void getData(final String qid) {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getCommonFaultById") + Constants.BASEPARAMS + "&commonFaultId=" + ReturnUtils.encode(qid);
        abHttpUtil.get(url, new CallBackParent(RepairQuestionDetailActivity.this, getResources().getString(R.string.loading), "RepairQuestionDetailActivity") {
            @Override
            public void Get_Result(String result) {
                try {
                    //解析服务器返回数据
                    RepairQuestionListBean rqBean = AbJsonUtil.fromJson(result, RepairQuestionListBean.class);
                    if (rqBean.getCode().equals(CacheConstants.LOCAL_SUCCESS) || rqBean.getCode().equals(CacheConstants.SAM_SUCCESS)) {
                        if (rqBean.getData() != null) {
                           List<RepairQuestionBean> qlist = rqBean.getData();
                            if (qlist.size() > 0) {
                                tv_fault_name.setText(qlist.get(0).getCF_TITLE() + "");
//                                tv_fault_content.setText("\u3000\u3000"+ qlist.get(0).getCF_DESCRIPTION());
                                loadUrl(qlist.get(0).getCF_DESCRIPTION());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(RepairQuestionDetailActivity.this, "数据异常!");
                }
            }
        });
    }

    @Override
    public void setListener() {
        //返回
        iv_repair_question_detail_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //已解决
        tv_resolved.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //报修
        tv_repair_go.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepairQuestionDetailActivity.this, AddRepairActivity.class);
                intent.putExtra("style", 2);
                startActivity(intent);
            }
        });
    }

}
