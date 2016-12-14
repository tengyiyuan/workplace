package com.toplion.cplusschool.Reimburse;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Bean.StandardInfo;
import com.toplion.cplusschool.Bean.type;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomEditTextDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wang
 * on 2016/8/1.
 *
 * @des报销资料准备
 */
public class ReimbursementDataActivity extends BaseActivity {
    private RelativeLayout rl_nodata;
    private ImageView iv_reimbursement_back;//返回键
    private WebView wv_reimbursement_data;//加载报销流程所需步骤
    private TextView tv_reimbursement_cancle;//取消
    private TextView tv_reimbursement_finish;//完成
    private RelativeLayout rl_reimbursement_nodata;//没有数
    private TextView tv_reimbursement_disa;//无数据提示
    private AbHttpUtil abHttpUtil;
    private int intCode = 0;
    private int index = 0;
    private int style = 0;
    private type TYPE;
    private StandardInfo standardInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reimbursement_data);
        init();
    }

    @Override
    protected void init() {
        super.init();
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        // 屏幕的高度和宽度保存到本地缓存
        BaseApplication.ScreenWidth = screenWidth;
        BaseApplication.ScreenHeight = screenHeight;
        intCode = getIntent().getIntExtra("code", 0);
        style = getIntent().getIntExtra("style", 0);
        TYPE = (type) getIntent().getSerializableExtra("TYPE");
        standardInfo = new StandardInfo();
        standardInfo.setRTID(TYPE.getRTID());
        standardInfo.setRTNAME(TYPE.getRTNAME());
        standardInfo.setRTPROCESSINGTIME(TYPE.getRTPROCESSINGTIME());
        abHttpUtil = AbHttpUtil.getInstance(this);
        rl_reimbursement_nodata = (RelativeLayout) findViewById(R.id.rl_reimbursement_nodata);
        iv_reimbursement_back = (ImageView) findViewById(R.id.iv_reimbursement_back);
        wv_reimbursement_data = (WebView) findViewById(R.id.wv_reimbursement_data);
        tv_reimbursement_cancle = (TextView) findViewById(R.id.tv_reimbursement_cancle);
        tv_reimbursement_finish = (TextView) findViewById(R.id.tv_reimbursement_finish);
        tv_reimbursement_disa = (TextView) findViewById(R.id.tv_reimbursement_disa);
        setListener();
        getData();
        rl_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private String innetsp = "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">";
    private String css = "<style>" +
            "img{" +
            "max-width:100%;" +
            "height:auto}" +

            "video{" +
            "max-width:100%;" +
            "height:auto}" +
            "</style>";

    //加载数据
    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getStandardBytypeid") + Constants.BASEPARAMS + "&typeid=" + TYPE.getRTID();
        Log.e("url", url);
        abHttpUtil.get(url, new CallBackParent(this, getResources().getString(R.string.loading), "getStandardBytypeid") {
            @Override
            public void Get_Result(String result) {
                Log.e("resultssssss", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String msg = jsonObject.getString("msg");
                    final JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    if (jsonArray.length() > 0) {
                        rl_reimbursement_nodata.setVisibility(View.GONE);
                        wv_reimbursement_data.setVisibility(View.VISIBLE);
                        loadUrl(jsonArray.getJSONObject(0).getString("RSCONTENT"));
                        if (jsonArray.length() == 1) {
                            setButtonText();
                        } else {
                            tv_reimbursement_cancle.setVisibility(View.VISIBLE);
                            tv_reimbursement_finish.setText("下一步");
                            tv_reimbursement_finish.setVisibility(View.VISIBLE);
                            tv_reimbursement_finish.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        index++;
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(index);
                                        loadUrl(jsonObject1.getString("RSCONTENT"));
                                        if (index == jsonArray.length() - 1) {
                                            setButtonText();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } else {
                        setButtonText();
                        tv_reimbursement_disa.setText(msg);
                        rl_reimbursement_nodata.setVisibility(View.VISIBLE);
                        wv_reimbursement_data.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setButtonText() {
        tv_reimbursement_cancle.setVisibility(View.VISIBLE);
        if (style == 0) {
            tv_reimbursement_finish.setText("材料齐全,添加");
        }else{
            tv_reimbursement_finish.setText("完  成");
        }
        tv_reimbursement_finish.setVisibility(View.VISIBLE);
        tv_reimbursement_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(style==0) {
                    showDialog();
                }else{
                    finish();
                }
            }
        });

    }



    @Override
    protected void setListener() {
        super.setListener();
        //取消
        tv_reimbursement_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //返回
        iv_reimbursement_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //显示填写报销单数量弹框
    private void showDialog() {
        final CustomEditTextDialog dialog = new CustomEditTextDialog(ReimbursementDataActivity.this);
        dialog.setTitle("需要报销单据数量:");
        dialog.setHintText("请填写你要报销的单据数量");
        dialog.setEditRightText("张");
        dialog.setTextNumberBoolean(true);
        dialog.setEditTextType(InputType.TYPE_CLASS_NUMBER);
        dialog.setEditTextLength(6);
        dialog.setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = dialog.getEditText();
                HideKeyboard(dialog.getEdit());
                if (!TextUtils.isEmpty(content)) {
                    standardInfo.setRRNUMBER(Integer.parseInt(content));
                    if (intCode == 0) {
                        Intent intent = new Intent(ReimbursementDataActivity.this, TypeListActivity.class);
                        intent.putExtra("style", 0);
                        intent.putExtra("standardInfo", standardInfo);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("standardInfo", standardInfo);
                        setResult(RESULT_OK, intent);
                    }
                    dialog.dismiss();
                    finish();
                } else {
                    ToastManager.getInstance().showToast(ReimbursementDataActivity.this, "请输入单据数量");
                }
            }
        });
        dialog.setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //加载html代码片段
    private void loadUrl(String data) {
        // 设置对JS的支持
        WebSettings webSettings = wv_reimbursement_data.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
//        //html的图片就会以单列显示就不会变形占了别的位置
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//       //让缩放显示的最小值为起始
//        wv_reimbursement_data.setInitialScale(5);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        wv_reimbursement_data.requestFocus();// 如果不 设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。 取消滚动条
        wv_reimbursement_data.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv_reimbursement_data.setVerticalScrollBarEnabled(true);

        wv_reimbursement_data.getSettings().setDefaultTextEncodingName("UTF-8");
        wv_reimbursement_data.loadDataWithBaseURL("", "<head>" + innetsp + css + "</head><body>" + data + "</body>", "text/html", "UTF-8", "");
        wv_reimbursement_data.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            public void onPageFinished(WebView view, String url) {

            }
        });
        wv_reimbursement_data.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {

            }

            public void onReceivedIcon(WebView view, Bitmap icon) {
            }

            public void onReceivedTitle(WebView view, String title) {

            }
        });
    }

    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }
}
