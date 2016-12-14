package com.toplion.cplusschool.SendMessage;

import com.ab.http.AbBinaryHttpResponseListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDialogUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Adapter.FujianListAdapter;
import com.toplion.cplusschool.Bean.FujianBean;
import com.toplion.cplusschool.Bean.NewBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.ListViewInScrollView;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MessageContent extends BaseActivity {
    private ArrayList<FujianBean> fujian;
    private FujianListAdapter fujianListAdapter;
    private ListViewInScrollView fujianlist;
    private TextView zuozhe, time, newtitle;
    private WebView mWebView;
    private NewBean newbean;
    private String data = "获取新闻失败";
    private TextView tv_webview_title;
    private int style = 0;
    private RelativeLayout topzuozhe;
    private String zuozhetext;
    private String formtext;
    private String type = "0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagecontent);
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
        style = getIntent().getIntExtra("style", 1);
        mWebView = (WebView) findViewById(R.id.webView1);
        newbean = (NewBean) getIntent().getSerializableExtra("bean");
        tv_webview_title = (TextView) findViewById(R.id.tv_webview_title);
        zuozhe = (TextView) findViewById(R.id.editzuozhe);
        fujianlist = (ListViewInScrollView) findViewById(R.id.fujianlist);
        time = (TextView) findViewById(R.id.time);
        newtitle = (TextView) findViewById(R.id.newtitle);
        topzuozhe = (RelativeLayout) findViewById(R.id.topzuozhe);
        if (style == 1) {
            tv_webview_title.setText("信息发布");
        } else {
            tv_webview_title.setText("公文发布");
        }
        fujian = new ArrayList<FujianBean>();
        fujianListAdapter = new FujianListAdapter(this, fujian);
        fujianlist.setAdapter(fujianListAdapter);
        fujianlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                download(fujian.get(position).getFujianurl(), fujian.get(position).getFujianname());

            }
        });
    }

    //<head><style>img{max-width:100% !important;}</style></head>
    private void loadUrl(String data) {
        // 设置对JS的支持
        WebSettings webSettings = mWebView.getSettings();
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存
        //自适应屏幕
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        //mWebView.requestFocus();// 如果不 设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。 取消滚动条
        //mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // mWebView.setVerticalScrollBarEnabled(true);
        //webSettings.setBuiltInZoomControls(false);
        // webSettings.setSupportZoom(false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            mWebView.getSettings().setDisplayZoomControls(false);//设定缩放控件隐藏
//        }
        // mWebView.getSettings().setLoadWithOverviewMode(true);
        // mWebView.getSettings().setUseWideViewPort(true);
        //mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
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
        params.put("type", style);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showInfomationOrDocumentInfoById");
        abHttpUtil.post(url, params, new CallBackParent(MessageContent.this, "正在加载") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    String datastr = Function.getInstance().getString(obj, "data");
                    JSONObject dataobj = new JSONObject(datastr);
                    String title = Function.getInstance().getString(dataobj, "title");
                    String oher = Function.getInstance().getString(dataobj, "releaseDepart");
                    String othtime = Function.getInstance().getString(dataobj, "releaseTime");
                    String html = Function.getInstance().getString(dataobj, "content");
                    String fujianstr = Function.getInstance().getString(obj, "attchment");
                    if (!fujianstr.equals("[]")) {
                        JSONArray array = new JSONArray(fujianstr);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject fuobj = (JSONObject) array.get(i);
                            FujianBean bean = new FujianBean();
                            bean.setFujianid(Function.getInstance().getString(fuobj, "attachId"));
                            bean.setFujianname(Function.getInstance().getString(fuobj, "attachName"));
                            bean.setFujianurl(Function.getInstance().getString(fuobj, "attachAddress"));
                            if (!bean.getFujianname().trim().equals("")) {
                                fujian.add(bean);
                            }
                        }
                        fujianListAdapter.setMlist(fujian);
                        fujianListAdapter.notifyDataSetChanged();
                    }
                    newtitle.setText(title);
                    zuozhe.setText(oher);
                    time.setText(othtime);
                    loadUrl(html);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width\"> " +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    private void download(String url, final String name) {
        AbDialogUtil.showProgressDialog(MessageContent.this, 0, "正在下载附件");
        abHttpUtil.get(url.trim(), new AbBinaryHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, byte[] content) {
                createFileWithByte(content, name);
                File currentPath = new File(Environment.getExternalStorageDirectory() + "/" + name);
                if (currentPath != null && currentPath.isFile()) {
                    String fileName = currentPath.toString();
                    Intent intent;
                    try{
                    if (checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingImage))) {
                        intent = OpenFiles.getImageFileIntent(currentPath);
                        startActivity(intent);
                    } else if (checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingWebText))) {
                        intent = OpenFiles.getHtmlFileIntent(currentPath);
                        startActivity(intent);
                    } else if (checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingPackage))) {
                        ToastManager.getInstance().showToast(MessageContent.this, "文件已经下载至手机根目录，无法打开！");
                        return;
                    } else if (checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingAudio))) {
                        intent = OpenFiles.getAudioFileIntent(currentPath);
                        startActivity(intent);
                    } else if (checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingVideo))) {
                        intent = OpenFiles.getVideoFileIntent(currentPath);
                        startActivity(intent);
                    } else if (checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingText))) {
                        intent = OpenFiles.getTextFileIntent(currentPath);
                        startActivity(intent);
                    } else if (checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingPdf))) {
                        intent = OpenFiles.getPdfFileIntent(currentPath);
                        startActivity(intent);
                    } else if (checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingWord))) {
                        intent = OpenFiles.getWordFileIntent(currentPath);
                        startActivity(intent);
                    } else if (checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingExcel))) {
                        intent = OpenFiles.getExcelFileIntent(currentPath);
                        startActivity(intent);
                    } else if (checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingPPT))) {
                        intent = OpenFiles.getPPTFileIntent(currentPath);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(MessageContent.this, "无法打开，请安装相应的软件！");
                    }
                }catch(Exception e){
                        ToastManager.getInstance().showToast(MessageContent.this, "文件已经下载至手机根目录,您手机没有相应的权限打开文件!");
                }
                    }else{
                        ToastManager.getInstance().showToast(MessageContent.this, "对不起，这不是文件！");
                    }

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                AbDialogUtil.removeDialog(MessageContent.this);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {

            }
        });
    }

    /**
     * 根据byte数组生成文件
     *
     * @param bytes 生成文件用到的byte数组
     */
    private void createFileWithByte(byte[] bytes, String fileName) {
        // TODO Auto-generated method stub
        /**
         * 创建File对象，其中包含文件所在的目录以及文件的命名
         */
        File file = new File(Environment.getExternalStorageDirectory(),
                fileName);
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private boolean checkEndsWithInStringArray(String checkItsEnd, String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.endsWith(aEnd))
                return true;
        }
        return false;
    }

    public static class OpenFiles {
        //android获取一个用于打开HTML文件的intent
        public static Intent getHtmlFileIntent(File file) {
            Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString()).build();
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(uri, "text/html");
            return intent;
        }

        //android获取一个用于打开图片文件的intent
        public static Intent getImageFileIntent(File file) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "image/*");
            return intent;
        }

        //android获取一个用于打开PDF文件的intent
        public static Intent getPdfFileIntent(File file) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            return intent;
        }

        //android获取一个用于打开文本文件的intent
        public static Intent getTextFileIntent(File file) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "text/plain");
            return intent;
        }

        //android获取一个用于打开音频文件的intent
        public static Intent getAudioFileIntent(File file) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "audio/*");
            return intent;
        }

        //android获取一个用于打开视频文件的intent
        public static Intent getVideoFileIntent(File file) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "video/*");
            return intent;
        }


        //android获取一个用于打开CHM文件的intent
        public static Intent getChmFileIntent(File file) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/x-chm");
            return intent;
        }


        //android获取一个用于打开Word文件的intent
        public static Intent getWordFileIntent(File file) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/msword");
            return intent;
        }

        //android获取一个用于打开Excel文件的intent
        public static Intent getExcelFileIntent(File file) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.ms-excel");
            return intent;
        }

        //android获取一个用于打开PPT文件的intent
        public static Intent getPPTFileIntent(File file) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            return intent;
        }

        //android获取一个用于打开apk文件的intent
        public static Intent getApkFileIntent(File file) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            return intent;
        }


    }
}
