package com.toplion.cplusschool.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.image.AbImageLoader;
import com.toplion.cplusschool.Adapter.ListViewAdapter;
import com.toplion.cplusschool.Bean.BanWidthBean;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.TimeUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wang
 * on 2016/8/23.
 *
 * @套餐选择界面
 */
public class MealsActivity extends BaseActivity {
    private LinearLayout ll_meal_center;
    private TextView errorstr;
    private LinearLayout lineout;
    private TextView tv_pay_explanation;
    private ImageView tanimg;
    private ImageButton imgBtn_dialog;
    private Dialog allMsg;
    //Dialog的布局View
    private View allMsgView;
    private ImageView iv_meal_return;//返回键
    private GridView gv_meals_type;//选择包月或者包学期
    private ListView lv_meals_bandwidth;//带宽选择
    private List<String> typeList;//包月包学期列表
    private Map<String, BanWidthBean> typeMap;
    private List<BanWidthBean> mealList;//套餐列表
    private SharePreferenceUtils share;
    private int height;              // 屏幕高度
    //    private String dataResult = "[包月有优惠|1|0|1000,包月无优惠|0|0|1000,包学期有优惠|1|1|1000,包学期无优惠|0|1|1000]";
    private String[] meals = null;
    private AbHttpUtil abHttpUtil;
    //图片下载类
    private AbImageLoader mAbImageLoader = null;

    private String baoyuetext = "";//包月优惠政策
    private String baoxueqitext = "";//包学期优惠政策
    private String startEnd;     // 开始结束时间
    private String nextBillingTime;    // 下次绑定时间
    private String online;             // 服务器时间


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_meals);
        init();
    }

    @Override
    protected void init() {
        super.init();
        ll_meal_center = (LinearLayout) findViewById(R.id.ll_meal_center);
        errorstr = (TextView) findViewById(R.id.error);
        lineout = (LinearLayout) findViewById(R.id.lineout);
        tv_pay_explanation = (TextView) findViewById(R.id.tv_pay_explanation);
        share = new SharePreferenceUtils(this);
        abHttpUtil = AbHttpUtil.getInstance(this);
        try {
            // 获取SAM用户信息
            JSONObject samUserInfo = new JSONObject(share.getString("samUserInfo", ""));
            // 获取下次绑定时间
            nextBillingTime = samUserInfo.getString("nextBillingTime");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MealsActivity.this, "账户出现错误......", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        height = share.getInt("height", 0);
        iv_meal_return = (ImageView) findViewById(R.id.iv_meal_return);
        gv_meals_type = (GridView) findViewById(R.id.gv_meals_type);
        lv_meals_bandwidth = (ListView) findViewById(R.id.lv_meals_bandwidth);
        mAbImageLoader = AbImageLoader.getInstance(this);
        // 通过LayoutInflater找到改布局
        allMsgView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tandialog, null);
        //创建Dialog
        allMsg = new AlertDialog.Builder(this).create();
        //设置点击外边缘不消失，2.x的应该是默认不消失的
        allMsg.setCanceledOnTouchOutside(false);
        //findView布局里的控件
        tanimg = (ImageView) allMsgView.findViewById(R.id.tanimg);
        imgBtn_dialog = (ImageButton) allMsgView.findViewById(R.id.dialog_pre_entry_close);
        imgBtn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allMsg.dismiss();
            }
        });
        updateImg();
        getImgUrl();
        setListener();
    }

//    private void getcenterImg() {
//        mAbImageLoader.display(tanimg, "http://pic14.nipic.com/20110613/7575067_130213594310_2.jpg");
//    }

    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getAllUserPkgInfo") + Constants.BASEPARAMS + "&token=" + ReturnUtils.encode(share.getString("token", "")) + "&username=" + ReturnUtils.encode(share.getString("username", ""));
        abHttpUtil.get(url, new CallBackParent(this, "加载套餐列表") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String msg = jsonObject.getString("msg");
                    jsonObject = new JSONObject(jsonObject.getString("data"));
                    String dataResult = null;
                    dataResult = jsonObject.getString("packages");
                    if (!TextUtils.isEmpty(dataResult.replace("[", "").replace("]", ""))) {
//                        dataResult = jsonObject.getString("packages");
                        online = TimeUtils.timeStamp2Date(jsonObject.getString("serverTime"), "yyyy.MM.dd");
                        dataResult = dataResult.replace("[", "").replace("]", "").replace("\"", "");
                        meals = dataResult.split(",");
                        Set<String> set = new HashSet<String>();
                        typeMap = new IdentityHashMap<String, BanWidthBean>();
                        for (int i = 0; i < meals.length; i++) {
                            String mealStr = meals[i];
                            String[] meal = mealStr.split("\\|");
                            if (meal.length >= 4) {
                                set.add(meal[2]);
                                BanWidthBean banWidthBean = new BanWidthBean();
                                banWidthBean.setBanWidth(meal[0]);
                                banWidthBean.setIsDiscount(meal[1]);
                                banWidthBean.setBanType(meal[2]);
                                banWidthBean.setPrice(meal[3]);
                                banWidthBean.setBanWidthWZ(mealStr);
                                typeMap.put(new String(meal[2]), banWidthBean);
                            }
                        }
                        List<String> newList = new ArrayList<String>(set);
                        newList.clear();
                        typeList = new ArrayList<String>(set);
                        for (int i = 0; i < typeList.size(); i++) {
                            if (typeList.get(i).equals("1")) {
                                newList.add(typeList.get(i));
                                typeList.remove(typeList.get(i));
                            }
                        }

                        for (int i = 0; i < typeList.size(); i++) {
                            newList.add(typeList.get(i));
                        }


                        gv_meals_type.setAdapter(new GridViewAdapter(newList));
                        ll_meal_center.setVisibility(View.VISIBLE);
                    } else {
                        String msgStr = msg;
                        if (TextUtils.isEmpty(msgStr)) {
                            msgStr = "服务器连接失败，请检查网络后重试!";
                        }
                        final CommDialog dialog = new CommDialog(MealsActivity.this);
                        dialog.CreateDialogOnlyOk("系统提示", "确定", msgStr, new CommDialog.CallBack() {
                            @Override
                            public void isConfirm(boolean flag) {
                                dialog.cancelDialog();
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(MealsActivity.this, "服务器异常，请检查网络后重试!");
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                super.onFailure(statusCode, content, error);
                errorstr.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    protected void setListener() {
        super.setListener();
        iv_meal_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_meals_bandwidth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MobclickAgent.onEvent(MealsActivity.this, "pay_detail");//统计

                Intent intent = new Intent(MealsActivity.this, PayOrderActivity.class);
                BanWidthBean banWidthBean = mealList.get(position);

                if (banWidthBean.getBanType().equals("0")) {
                    // 获取到期时间
                    String nextBilling = nextBillingTime.substring(0, nextBillingTime.indexOf("T")).replace("-", ".");
                    long next = Long.parseLong(TimeUtils.date2TimeStamp(nextBilling, "yyyy.MM.dd"));
                    long longOnline = Long.parseLong(TimeUtils.date2TimeStamp(online, "yyyy.MM.dd"));
                    long res = next - longOnline;
                    if (res < 0) {
                        // 服务器时间
                        startEnd = TimeUtils.getMealTLength(online, banWidthBean.getBanType());
                    } else {
                        // SAM时间
                        startEnd = TimeUtils.getMealTLength(nextBilling, banWidthBean.getBanType());
                    }
                } else if (banWidthBean.getBanType().equals("1")) {
                    startEnd = "包学期";
                }
                intent.putExtra("banWidthgBean", banWidthBean);
                intent.putExtra("tlength", startEnd);
//                intent.putExtra("banWidthgBean",mealList.get(position));
                startActivityForResult(intent, 100);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 100:
                MealsActivity.this.finish();
                break;

            default:
                break;
        }
    }

    /**
     * 包月选择
     */
    private class GridViewAdapter extends BaseAdapter {
        private boolean isFirst = true;
        private List<String> mlist;

        public GridViewAdapter(List<String> list) {
            this.mlist = list;
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final Button btnType;
            if (convertView == null) {
                // 生成Button
                btnType = new Button(MealsActivity.this);
                btnType.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 17));
                btnType.setPadding(4, 4, 4, 4);
                btnType.setTextSize(14f);
                btnType.setBackgroundResource(R.drawable.bg_ec);
            } else {
                btnType = (Button) convertView;
            }
            if (mlist.get(position).equals("0")) {
                btnType.setText("包月");
            } else if (mlist.get(position).equals("1")) {
                btnType.setText("包学期");
            }
            if (isFirst) {
                if (position == 0) {
                    btnType.setBackgroundResource(R.drawable.bg_orange);
                    btnType.setTextColor(Color.WHITE);
                    mealList = new ArrayList<BanWidthBean>();
                    for (Map.Entry<String, BanWidthBean> entry : typeMap.entrySet()) {
                        if (entry.getKey().equals(mlist.get(position))) {
                            mealList.add(entry.getValue());
                        }
                    }
                    lv_meals_bandwidth.setAdapter(new ListViewAdapter(MealsActivity.this, mealList));
                } else {
                    btnType.setBackgroundResource(R.drawable.bg_ec);
                    btnType.setTextColor(Color.BLACK);
                }
                isFirst = false;
            }
            btnType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        Button btn = (Button) parent.getChildAt(i);
                        if (position == i) { // 当前选中的Item改变背景颜色
                            btn.setBackgroundResource(R.drawable.bg_orange);
                            btn.setTextColor(Color.WHITE);
                        } else {
                            btn.setBackgroundResource(R.drawable.bg_ec);
                            btn.setTextColor(Color.BLACK);
                        }
                    }
                    if (mlist.get(position).equals("0")) {
                        if (!TextUtils.isEmpty(baoyuetext)) {
                            lineout.setVisibility(View.VISIBLE);
                            tv_pay_explanation.setText(baoyuetext);
                        } else {
                            lineout.setVisibility(View.GONE);
                        }
                    } else {
                        if (!TextUtils.isEmpty(baoxueqitext)) {
                            lineout.setVisibility(View.VISIBLE);
                            tv_pay_explanation.setText(baoxueqitext);
                        } else {
                            lineout.setVisibility(View.GONE);
                        }
                    }
                    mealList = new ArrayList<BanWidthBean>();
                    for (Map.Entry<String, BanWidthBean> entry : typeMap.entrySet()) {
                        if (entry.getKey().equals(mlist.get(position))) {
                            mealList.add(entry.getValue());
                        }
                    }
                    lv_meals_bandwidth.setAdapter(new ListViewAdapter(MealsActivity.this, mealList));
                }
            });
            return btnType;
        }
    }

    private void getImgUrl() {
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getMarketingBySchoolCode") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, false) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String obj = object.getString("marketInfo");
                    if (!TextUtils.isEmpty(obj)) {
                        JSONObject jsonobj = new JSONObject(obj);
                        mAbImageLoader.display(tanimg, jsonobj.getString("marketContent"));
                        allMsg.show();
                        allMsg.getWindow().setContentView((RelativeLayout) allMsgView);
                    }
                    String objby = object.getString("b1tripInfo");
                    if (!TextUtils.isEmpty(objby)) {
                        JSONObject byobj = new JSONObject(objby);
                        if (!TextUtils.isEmpty(byobj.getString("tripContent"))) {
                            baoyuetext = byobj.getString("tripContent");
                        }
                    }
                    String objbxue = object.getString("bttripInfo");
                    if (!TextUtils.isEmpty(objbxue)) {
                        JSONObject byxue = new JSONObject(objbxue);
                        if (!TextUtils.isEmpty(byxue.getString("tripContent"))) {
                            baoxueqitext = byxue.getString("tripContent");
                        }
                    }
                    if (!TextUtils.isEmpty(baoxueqitext)) {
                        lineout.setVisibility(View.VISIBLE);
                        tv_pay_explanation.setText(baoxueqitext);
                    } else {
                        lineout.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                getData();
            }
        });
    }

    private void updateImg() {
        int bwidth = 700;//图片宽度
        int bHeight = 900;//图片高度

        int width = BaseApplication.ScreenWidth;//这个是屏幕的宽度

        int height = width * bHeight / bwidth;
        ViewGroup.LayoutParams para = tanimg.getLayoutParams();
        para.height = height;
        tanimg.setLayoutParams(para);
    }
}
