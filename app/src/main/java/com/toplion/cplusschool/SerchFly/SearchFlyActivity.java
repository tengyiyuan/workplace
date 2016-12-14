package com.toplion.cplusschool.SerchFly;

import java.util.ArrayList;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.ConnectivityUtils;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomEditTextDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 自定义FramLayout文字飞入飞出效果
 * 主页面
 *
 * @author tengyy
 */
public class SearchFlyActivity extends BaseActivity implements OnClickListener {
    private ArrayList<String> messagelist;
    public static ArrayList<String> staticlist;
    private String[] keywords;
    private KeywordsFlow keywordsFlow;
    private Button btnIn, btnOut;
    private TextView sendmessage;
    private SharePreferenceUtils share;
    private ImageView back;
    private boolean flag = true;
    private Handler myhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    getData();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_serch_fly_main);
        share = new SharePreferenceUtils(this);
        messagelist = new ArrayList<String>();
        staticlist = new ArrayList<String>();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        // 屏幕的高度和宽度保存到本地缓存
        BaseApplication.ScreenWidth = screenWidth;
        BaseApplication.ScreenHeight = screenHeight;
        initView();
    }

    @Override
    public void onClick(View v) {
        if (v == btnIn) {
            keywordsFlow.rubKeywords();
            // keywordsFlow.rubAllViews();
            feedKeywordsFlow(keywordsFlow, keywords);
            keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
        } else if (v == btnOut) {
            keywordsFlow.rubKeywords();
            // keywordsFlow.rubAllViews();
            feedKeywordsFlow(keywordsFlow, keywords);
            keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
        } else if (v instanceof TextView) {
            String keyword = ((TextView) v).getText().toString();
            // Intent intent = new Intent();
            // intent.setAction(Intent.ACTION_VIEW);
            // intent.addCategory(Intent.CATEGORY_DEFAULT);
            // intent.setData(Uri.parse("http://www.google.com.hk/#q=" +
            // keyword));
            // startActivity(intent);
            Log.e("Search", keyword);
        }
    }

    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        sendmessage = (TextView) findViewById(R.id.sendmessage);
        btnIn = (Button) findViewById(R.id.button1);
        btnOut = (Button) findViewById(R.id.button2);
        btnIn.setOnClickListener(this);
        btnOut.setOnClickListener(this);
        keywordsFlow = (KeywordsFlow) findViewById(R.id.frameLayout1);
        keywordsFlow.setDuration(800l);
        keywordsFlow.setOnItemClickListener(this);
//        // 添加
//        feedKeywordsFlow(keywordsFlow, keywords);
//        keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
        sendmessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustomEditTextDialog dialog = new CustomEditTextDialog(SearchFlyActivity.this);
                dialog.setTextNumberBoolean(false);
                dialog.setTitle("我要发言");
                dialog.setHintText("请输入您的发言内容(15字内)");
                dialog.setNumText(15);
                dialog.setEditRightText("发言");
                dialog.setEditTextType(InputType.TYPE_CLASS_TEXT);
                dialog.setLeftOnClick(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager inputMgr = (InputMethodManager) SearchFlyActivity.this
                                .getSystemService(SearchFlyActivity.INPUT_METHOD_SERVICE);
                        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                        dialog.dismiss();
                    }
                });
                dialog.setRightOnClick(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                                        InputMethodManager inputMgr = (InputMethodManager) SearchFlyActivity.this
                        .getSystemService(SearchFlyActivity.INPUT_METHOD_SERVICE);
                inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                        addmessage(dialog.getEditText().toString().trim());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(ConnectivityUtils.isWifi(SearchFlyActivity.this)) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (flag) {
                        Message mes = new Message();
                        mes.what = 0;
                        myhandler.sendMessage(mes);
                        try {
                            sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }else{
            ToastManager.getInstance().showToast(SearchFlyActivity.this,"当前流量状态下无法使用水花墙！");
        }
    }
    @Override
    public void setListener() {
        // TODO Auto-generated method stub

    }

    /**
     * 随机/不随机产生MAX个文字显示
     *
     * @param keywordsFlow
     * @param arr
     */
    private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
//        Random random = new Random();
//        for (int i = 0; i < KeywordsFlow.MAX; i++) {
//            int ran = random.nextInt(arr.length);
//            String tmp = arr[ran];
//            keywordsFlow.feedKeyword(tmp);
//        }
        for (int i = 0; i < arr.length; i++) {
            String tmp = arr[i];
            keywordsFlow.feedKeyword(tmp);
        }
    }

    @Override
    protected void getData() {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("scount", 15);
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getWaterWallInfo") + Constants.BASEPARAMS;
        abHttpUtil.post(url,params, new CallBackParent(this, false) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String datajson = object.getString("data");
                    if (!datajson.equals("[]")) {
                        staticlist.clear();
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = (JSONObject) array.get(i);
                            String content = Function.getInstance().getString(obj, "WWCONTENT");
                            staticlist.add(content);
                        }
                        messagelist.clear();
                        messagelist.addAll(staticlist);
                        keywords = new String[messagelist.size()];
                        for (int i = 0; i < messagelist.size(); i++) {
                            keywords[i] = messagelist.get(i);
                        }
                        feedKeywordsFlow(keywordsFlow, keywords);
                        keywordsFlow.rubKeywords();
                        // keywordsFlow.rubAllViews();
                        feedKeywordsFlow(keywordsFlow, keywords);
                        keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
    }

    private void addmessage(final String str) {
        AbRequestParams params = new AbRequestParams();
         params.put("userid", share.getString("username",""));
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("content",str);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("addWaterWallInfo") + Constants.BASEPARAMS;
        abHttpUtil.post(url,params, new CallBackParent(this, false) {
            @Override
            public void Get_Result(String result) {
                staticlist.add(str);
            }
        });
    }
//    private ArrayList<String> updatelist() {
//        staticlist.add("我是机电学院的");
//        staticlist.add("大家好，建筑大学");
//        staticlist.add("艺术学院：张大伟路过");
//        staticlist.add("食堂的饭不好吃！！！！！");
//        staticlist.add("今天下午没课，哈哈哈哈");
//        staticlist.add("据说明天学校放假休息???????");
//        staticlist.add("张一毛看见联系我");
//        staticlist.add("有人在操场打球吗？？？？？");
//        staticlist.add("有没有美女？？？？");
//        staticlist.add("仔细发一下");
//        staticlist.add("我是美女出来冒泡");
//        staticlist.add("今天早上建大有没有去跑步的？？？？");
//        staticlist.add("可以的");
//        staticlist.add("夜跑的+1.。。。。");
//        return staticlist;
//    }
}
