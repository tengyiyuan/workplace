package com.toplion.cplusschool.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.toplion.cplusschool.Activity.BanshiActivity;
import com.toplion.cplusschool.Activity.CommonWebViewActivity;
import com.toplion.cplusschool.Activity.ContactsActivity;
import com.toplion.cplusschool.Activity.FloorListActivity;
import com.toplion.cplusschool.Activity.GradeListActivity;
import com.toplion.cplusschool.Activity.HelpActivity;
import com.toplion.cplusschool.Activity.MainActivity;
import com.toplion.cplusschool.Activity.MainTabActivity;
import com.toplion.cplusschool.Activity.MealsActivity;
import com.toplion.cplusschool.Activity.NewsListActivity;
import com.toplion.cplusschool.Activity.OneCardPassActivity;
import com.toplion.cplusschool.Activity.PhoneBooksActivity;
import com.toplion.cplusschool.Activity.SchoolBusActivity;
import com.toplion.cplusschool.Bean.Menus;
import com.toplion.cplusschool.JianKong.KongMainActivity;
import com.toplion.cplusschool.Map.LocationSourceActivity;
import com.toplion.cplusschool.PhotoWall.PhotoMainActivity;
import com.toplion.cplusschool.QianDao.QianDaoActivity;
import com.toplion.cplusschool.Reimburse.BaomainActivity;
import com.toplion.cplusschool.Activity.RepairQuestionListActivity;
import com.toplion.cplusschool.Activity.TestListActivity;
import com.toplion.cplusschool.Activity.WeekListActivity;
import com.toplion.cplusschool.Adapter.FunctionListAdapter;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.CommDialog.CallBack;
import com.toplion.cplusschool.Common.CommonPopupWindow;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.SecondMarket.MainMarket;
import com.toplion.cplusschool.SendMessage.MessageActivity;
import com.toplion.cplusschool.SerchFly.SearchFlyActivity;
import com.toplion.cplusschool.TeacherContacts.TeaContactsListActivity;
import com.toplion.cplusschool.TeacherContacts.TeacherContactsListActivity;
import com.toplion.cplusschool.Utils.AESUtils;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.CommonUtil;
import com.toplion.cplusschool.Utils.ConnectivityUtils;
import com.toplion.cplusschool.Utils.EportalUtils;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.StringUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.View.BannerImg;

import com.toplion.cplusschool.Wangyi.KaoLaActivity;

import com.toplion.cplusschool.Wage.SelectTypeActivity;

import com.toplion.cplusschool.Wangyi.MukeActivity;
import com.toplion.cplusschool.WeekMeeting.WeekMeetingListActivity;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.BannerViewPager;
import com.toplion.cplusschool.widget.MyListViewFill;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页界面，
 * 显示首页数据 图片轮播 显示用户套餐基本信息
 * 显示续费、报修、一键上网、退出网络 显示校园广播
 *
 * @author tengyy
 * @version 1.1.1
 */
public class PlayGroundFragment extends Fragment {
    private TextView qiandao;
    private LinearLayout layout_dot;
    private BannerViewPager layout_image;
    private ScrollView myscrow;
    // 图片轮播
    private BannerImg slidshowView;
    private List<String> imgs;
    List<Map<String, String>> data;
    private RelativeLayout ground_rl_pay;//缴费
    private RelativeLayout ground_rl_repair;//报修
    private RelativeLayout coustlist;//课程表查询
    private RelativeLayout rl_empty_classroom;//空教室
    private RelativeLayout rl_test;//考试安排
    private RelativeLayout rl_grade;//成绩
    private RelativeLayout rl_school_bus;//校车时刻表
    private RelativeLayout ground_rl_net;//一键上网
    private ImageView ground_iv_buy; // 一键上网
    private TextView ground_tv_buy_des; // 一键上网描述

    private MyListViewFill listView; // 校园广播

    private JSONObject json; // 返回值
    private SharePreferenceUtils share;

    private TextView ground_account; // 账户
    private TextView ground_tv_meal; // 套餐
    private TextView ground_tv_time; // 时长
    private TextView ground_tv_open; // 开通

    private LinearLayout ll_meal; // 套餐
    private LinearLayout ll_time; // 时长
    private LinearLayout ll_open; // 开通
    private ArrayList<Menus> menulist;

    private TextView ground_tv_pay; // 续费
    private TextView ground_tv_balance; // 余额

    private JSONObject samUserInfo; // SAM用户信息
    private Context context;
    private JSONObject bannerJson; // banner json数据
    private JSONArray bannerArray; // banner 图片列表
    private String messageStr = null;//提示
    private AbHttpUtil abHttpUtil;//网络请求工具
//    private boolean isShow = false; // 是否显示 "是不是规定wifi"或者是不是从本页面登录的提示
    private JSONObject breakJson = null;//断开连接返回数据

    private GridView gv_pgf_function;//首页功能 2016-6-28
    private List<Map<String, Object>> functionData;//功能列表
    private FunctionListAdapter flistAdapter;
    private int netstate = -1;//一键上网的位置记录
    private boolean  netFlag = false;//是否连接网络

    // 定义公共方法
    private static PlayGroundFragment play;

    public static PlayGroundFragment getPlay() {
        return play;
    }

    //<<<<<<< .mine
//    private int[] icons = new int[]{R.mipmap.btn_network_pay, R.mipmap.btn_repair, R.mipmap.btn_dis_network, R.mipmap.btn_week, R.mipmap.btn_grade, R.mipmap.btn_test, R.mipmap.btn_classroom, R.mipmap.school_bus, R.mipmap.xiaoli, R.mipmap.news, R.mipmap.jiangzuoyuyue, R.mipmap.postcard, R.mipmap.calssphone, R.mipmap.tongxun, R.mipmap.map, R.mipmap.baoxiao, R.mipmap.baoxiao, R.mipmap.shuihua, R.mipmap.kaolaimg, R.mipmap.yunyuedu, R.mipmap.manhuaimg};
//||||||| .r984
//    private int[] icons = new int[]{R.mipmap.btn_network_pay, R.mipmap.btn_repair, R.mipmap.btn_dis_network, R.mipmap.btn_week, R.mipmap.btn_grade, R.mipmap.btn_test, R.mipmap.btn_classroom, R.mipmap.school_bus, R.mipmap.xiaoli, R.mipmap.news, R.mipmap.jiangzuoyuyue, R.mipmap.postcard, R.mipmap.calssphone, R.mipmap.tongxun, R.mipmap.map, R.mipmap.baoxiao, R.mipmap.baoxiao, R.mipmap.shuihua, R.mipmap.kaolaimg, R.mipmap.yuedu, R.mipmap.manhuaimg};
//=======
    private int[] icons = new int[]{R.mipmap.btn_network_pay, R.mipmap.btn_repair, R.mipmap.btn_dis_network, R.mipmap.btn_week, R.mipmap.btn_grade, R.mipmap.btn_test, R.mipmap.btn_classroom, R.mipmap.school_bus, R.mipmap.xiaoli, R.mipmap.news, R.mipmap.jiangzuoyuyue, R.mipmap.postcard, R.mipmap.calssphone, R.mipmap.tongxun, R.mipmap.map, R.mipmap.baoxiao, R.mipmap.gongzichaxun, R.mipmap.shuihua, R.mipmap.kaolaimg, R.mipmap.yunyuedu, R.mipmap.manhuaimg, R.mipmap.jstxl,
            R.mipmap.banshi, R.mipmap.xinxi, R.mipmap.gongwen, R.mipmap.weekmeeting, R.mipmap.baogaoting, R.mipmap.zhongdian, R.mipmap.market, R.mipmap.lost_and_found, R.mipmap.parttime, R.mipmap.muke};
    private List<Map<String, String>> dataListAd = new ArrayList<Map<String, String>>();

    /**
     * -
     * 获取View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playground_fragment, container, false);
        //AbDialogUtil.showProgressDialog(getActivity(), 0, "正在加载菜单");
        menulist = new ArrayList<Menus>();
        play = this;
        context = getActivity();
        abHttpUtil = AbHttpUtil.getInstance(context);
        layout_image = (BannerViewPager) view.findViewById(R.id.layout_image);
        layout_dot = (LinearLayout) view.findViewById(R.id.layout_dot);
        gv_pgf_function = (GridView) view.findViewById(R.id.gv_pgf_function);
        myscrow = (ScrollView) view.findViewById(R.id.myscrow);
        functionData = new ArrayList<Map<String, Object>>();
        flistAdapter = new FunctionListAdapter(getActivity(), functionData);
        gv_pgf_function.setAdapter(flistAdapter);
        ground_tv_pay = (TextView) view.findViewById(R.id.ground_tv_pay);
        String exchange = getResources().getString(R.string.pay);
        ground_tv_pay.setText(Html.fromHtml(exchange));
        // 续费点击事件, 跳转到套餐选择界面
        ground_tv_pay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 跳转到套餐选择界面
                Intent intent = new Intent(getActivity(), MealsActivity.class);
                startActivity(intent);
            }
        });
        qiandao = (TextView) view.findViewById(R.id.qiandao);
        coustlist = (RelativeLayout) view.findViewById(R.id.coustlist);
        rl_empty_classroom = (RelativeLayout) view.findViewById(R.id.rl_empty_classroom);
        rl_test = (RelativeLayout) view.findViewById(R.id.rl_test);
        rl_grade = (RelativeLayout) view.findViewById(R.id.rl_grade);
        rl_school_bus = (RelativeLayout) view.findViewById(R.id.rl_school_bus);
        ground_tv_balance = (TextView) view.findViewById(R.id.ground_tv_balance);
        ground_rl_net = (RelativeLayout) view.findViewById(R.id.ground_rl_net);
        ground_iv_buy = (ImageView) view.findViewById(R.id.ground_iv_buy);
        ground_tv_buy_des = (TextView) view.findViewById(R.id.ground_tv_buy_des); // 一键上网描述

        share = new SharePreferenceUtils(getActivity());
        ground_account = (TextView) view.findViewById(R.id.ground_tv_account);
        ground_tv_meal = (TextView) view.findViewById(R.id.ground_tv_meal);
        ground_tv_time = (TextView) view.findViewById(R.id.ground_tv_time);
        ground_tv_open = (TextView) view.findViewById(R.id.ground_tv_open);
        ground_tv_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 开通点击事件, 跳转到套餐选择界面
                Intent intent = new Intent(getActivity(), MealsActivity.class);
                startActivity(intent);
            }
        });
        //课程表
        coustlist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), "timeTable");//统计
                Intent intent = new Intent(getActivity(), WeekListActivity.class);
                startActivity(intent);
            }
        });
        //空教室
        rl_empty_classroom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(getActivity(), "emptyRoom");//统计
                Intent intent = new Intent(getActivity(), FloorListActivity.class);
                startActivity(intent);
            }
        });
        //考试安排
        rl_test.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), "examPlan");//统计
                Intent intent = new Intent(getActivity(), TestListActivity.class);
                startActivity(intent);
            }
        });
        //成绩查询
        rl_grade.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), "queryScore");//统计
                Intent intent = new Intent(getActivity(), GradeListActivity.class);
                startActivity(intent);
            }
        });
        //校车时刻表
        rl_school_bus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), "schoolBus");//统计
                Intent intent = new Intent(getActivity(), CommonWebViewActivity.class);
                intent.putExtra("url", "http://123.233.121.17:12100/help/SchoolBus.html");
                intent.putExtra("title", "");
                startActivity(intent);
            }
        });
        qiandao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), "qiandao");//统计
                Intent intent = new Intent(getActivity(), QianDaoActivity.class);
                startActivity(intent);
            }
        });
        ll_meal = (LinearLayout) view.findViewById(R.id.ground_ll_meal);
        ll_time = (LinearLayout) view.findViewById(R.id.ground_ll_time);
        ll_open = (LinearLayout) view.findViewById(R.id.ground_ll_open);
        ll_open.setVisibility(View.GONE);
        //myAdapterAd = new ImageAdapter(getActivity(), dataListAd);
        //layout_image.setAdapter(myAdapterAd);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (int) (BaseApplication.ScreenWidth * 0.66));
        layout_image.setLayoutParams(rp);
        //layout_image.setOnPageChangeListener(new ImageAdPageChange(layout_dot));
        initPhoto(view);// 图片滚动
        initInfomenu();   // 初始化用户信息
        pay(view);    // 网费缴纳
        repair(view); // 报修
        //showConn();// 显示一键上网/断开网络
        // 设置首页图片Adapter
        listView = (MyListViewFill) view.findViewById(R.id.ground_lv_schgb);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), getData(), R.layout.list_item,
                new String[]{"title", ""},
                new int[]{R.id.list_tv_content, R.id.list_tv_time});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
                Intent intent = new Intent(context, HelpActivity.class);
                intent.putExtra("URL", data.get((int) id).get("time").toString());
                startActivity(intent);
            }
        });
        setGridViewSetListener(gv_pgf_function);
        // 实时注册侦听是否连接网络 10S
        new Thread(mRunnable).start();
        if (share.getInt("ROLE_TYPE", 2) == 1) {
            ground_tv_pay.setVisibility(View.GONE);
        }
        return view;
    }


    //首页功能列表添加
    private void addFunction() {
        if (!functionData.isEmpty()) {
            functionData.clear();
        }
        if (share.getString("username", "").equals("wzw")) {
            Map<String, Object> functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.btn_network_pay);
            functionMap.put("mid", Constants.JIAOFEI);
            functionMap.put("funDes", "缴费");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //添加报修
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.btn_repair);
            functionMap.put("mid", Constants.BAOXIU);
            functionMap.put("funDes", "报修");
            functionMap.put("flag", true);
            functionData.add(functionMap);


            //一键上网
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.btn_dis_network);
            functionMap.put("mid", Constants.YIJIANNET);
            functionMap.put("funDes", "一键上网");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //课程表
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.btn_week);
            functionMap.put("mid", Constants.KECHENGBIAO);
            functionMap.put("funDes", "课程表");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //查成绩
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.btn_grade);
            functionMap.put("mid", Constants.CHACHENGJI);
            functionMap.put("funDes", "查成绩");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //考试安排
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.btn_test);
            functionMap.put("mid", Constants.KAOSHIANPAI);
            functionMap.put("funDes", "考试安排");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //空教室
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.btn_classroom);
            functionMap.put("mid", Constants.KONGJIAOSHI);
            functionMap.put("funDes", "空教室");
            functionMap.put("flag", true);
            functionData.add(functionMap);


            //校车时刻表
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.school_bus);
            functionMap.put("mid", Constants.SHIKEBIAO);
            functionMap.put("funDes", "校车时刻表");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //添加校历功能
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.xiaoli);
            functionMap.put("mid", Constants.XIAOLI);
            functionMap.put("funDes", "校历");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //添加校园地图
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.map);
            functionMap.put("mid", Constants.DITU);
            functionMap.put("funDes", "校园地图");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //添加新闻功能
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.news);
            functionMap.put("mid", Constants.XINWEN);
            functionMap.put("funDes", "新闻");
            functionMap.put("flag", true);
            functionData.add(functionMap);
            //添加讲座预告功能
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.jiangzuoyuyue);
            functionMap.put("mid", Constants.JIANGZUO);
            functionMap.put("funDes", "讲座预告");
            functionMap.put("flag", true);
            functionData.add(functionMap);
            //添加一卡通功能
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.postcard);
            functionMap.put("mid", Constants.YIKATONG);
            functionMap.put("funDes", "一卡通");
            functionMap.put("flag", true);
            functionData.add(functionMap);
            //添加办公电话功能
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.calssphone);
            functionMap.put("mid", Constants.BANGONGDIANHUA);
            functionMap.put("funDes", "办公电话");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //添加班班通功能
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.tongxun);
            functionMap.put("mid", Constants.TONGXUNLU);
            functionMap.put("funDes", "班班通");
            functionMap.put("flag", true);
            functionData.add(functionMap);
            //办事指南
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.banshi);
            functionMap.put("mid", Constants.BANSHI);
            functionMap.put("funDes", "办事指南");
            functionMap.put("flag", true);
            functionData.add(functionMap);
            //规章制度
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.rule);
            functionMap.put("mid", Constants.GUIZHANGZHIDU);
            functionMap.put("funDes", "规章制度");
            functionMap.put("flag", true);
            functionData.add(functionMap);
            //添加教工通讯录功能
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.jstxl);
            functionMap.put("mid", Constants.JSTXL);
            functionMap.put("funDes", "教工通讯录");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //添加报销功能
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.baoxiao);
            functionMap.put("mid", Constants.BAOXIAO);
            functionMap.put("funDes", "报销");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //添加工资查询
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.gongzichaxun);
            functionMap.put("mid", Constants.GONGZICHAXUN);
            functionMap.put("funDes", "工资查询");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //信息发布
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.xinxi);
            functionMap.put("mid", Constants.XINXIFABU);
            functionMap.put("funDes", "信息发布");
            functionMap.put("flag", true);
            functionData.add(functionMap);
            //公文发布
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.gongwen);
            functionMap.put("mid", Constants.GONGWEN);
            functionMap.put("funDes", "公文发布");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //周会议
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.weekmeeting);
            functionMap.put("mid", Constants.ZHOUHUIYI);
            functionMap.put("funDes", "周会议");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //报告厅
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.baogaoting);
            functionMap.put("mid", Constants.BAOGAOTING);
            functionMap.put("funDes", "报告厅");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //重点工作监控
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.zhongdian);
            functionMap.put("mid", Constants.ZHONGDIAN);
            functionMap.put("funDes", "重点工作");
            functionMap.put("flag", true);
            functionData.add(functionMap);
            //二手市场
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.market);
            functionMap.put("mid", Constants.ERSHOUSHICHANG);
            functionMap.put("funDes", "二手市场");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //失物招领
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.lost_and_found);
            functionMap.put("mid", Constants.SHIWUZHAOLING);
            functionMap.put("funDes", "失物招领");
            functionMap.put("flag", true);
            functionData.add(functionMap);
            //兼职
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.parttime);
            functionMap.put("mid", Constants.JIANZHI);
            functionMap.put("funDes", "兼职招聘");
            functionMap.put("flag", true);
            functionData.add(functionMap);
            //水花墙
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.shuihua);
            functionMap.put("mid", Constants.SHUIHUA);
            functionMap.put("funDes", "水花墙");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //网易考拉
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.kaolaimg);
            functionMap.put("mid", Constants.KAOLA);
            functionMap.put("funDes", "考拉海购");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //网易云阅读
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.yunyuedu);
            functionMap.put("mid", Constants.YUEDU);
            functionMap.put("funDes", "云阅读");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //网易漫画
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.manhuaimg);
            functionMap.put("mid", Constants.MANHUA);
            functionMap.put("funDes", "漫画");
            functionMap.put("flag", true);
            functionData.add(functionMap);

            //网易慕课
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.muke);
            functionMap.put("mid", Constants.MUKE);
            functionMap.put("funDes", "网易慕课");
            functionMap.put("flag", true);
            functionData.add(functionMap);
            //照片墙
            functionMap = new HashMap<String, Object>();
            functionMap.put("funImage", R.mipmap.photo_wall);
            functionMap.put("mid", Constants.ZHAOPIANQIANG);
            functionMap.put("funDes", "照片墙");
            functionMap.put("flag", true);
            functionData.add(functionMap);

        } else {
            for (int i = 0; i < menulist.size(); i++) {
                Map<String, Object> functionMap = new HashMap<String, Object>();
                Menus menu = menulist.get(i);
                if (menu.getAI_ID() <= icons.length) {
                    functionMap.put("funImage", icons[menu.getAI_ID() - 1]);
                    functionMap.put("mid", menu.getAI_ID() + "");
                    functionMap.put("funDes", menu.getAI_NAME());
                    functionMap.put("flag", menu.isRSA_ENABLE());
                    functionData.add(functionMap);
                    if (menu.getAI_ID()==3) {
                        netstate = i;
                    }
                }
            }
        }
        flistAdapter.notifyDataSetChanged();
        myscrow.smoothScrollTo(0, 0);
        // AbDialogUtil.removeDialog(context);
        checkUserInfo(share.getString("username", ""));
    }

    //设置GridView首页功能的点击事件
    private void setGridViewSetListener(GridView gridView) {
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView img = (ImageView) view.findViewById(R.id.ground_iv_pay);
                TextView text = (TextView) view.findViewById(R.id.tv_title_des);
                String functionType = functionData.get(position).get("mid").toString();
                boolean flag = Boolean.parseBoolean(functionData.get(position).get("flag").toString());
                if (functionType.equals(Constants.XIAOLI)) {
                    if (flag) {
                        MobclickAgent.onEvent(getActivity(), "schoolBus");//统计
                        Intent intent = new Intent(getActivity(), SchoolBusActivity.class);
                        intent.putExtra("style", 1);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.GONGZICHAXUN)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), SelectTypeActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.XINWEN)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), NewsListActivity.class);
                        intent.putExtra("style", 0);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.JIANGZUO)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), NewsListActivity.class);
                        intent.putExtra("style", 1);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.YIKATONG)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), OneCardPassActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.BANGONGDIANHUA)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), PhoneBooksActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.TONGXUNLU)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), ContactsActivity.class);
                        intent.putExtra("type", 1);//班班通
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.JSTXL)) {
                    if (flag) {
//                        Intent intent = new Intent(getActivity(), TeaContactsListActivity.class);
                        Intent intent = new Intent(getActivity(), TeacherContactsListActivity.class);
                        intent.putExtra("type", 2);//教工通讯录
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.BAOXIAO)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), BaomainActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.GENGDUO)) {
                    ToastManager.getInstance().showToast(getActivity(), "更多");
                } else if (functionType.equals(Constants.DITU)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), LocationSourceActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.JIAOFEI)) {
                    if (flag) {
                        MobclickAgent.onEvent(context, "pay_select");//统计
                        // 跳转到套餐选择页面
                        Intent intent = new Intent(getActivity(), MealsActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.BAOXIU)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), RepairQuestionListActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.YIJIANNET)) {
                    if (flag) {
                        if(netFlag){//已经连接网络
                            breakNet();
                        }else{//未连接网络
                            EportalValidate();
                        }
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.KECHENGBIAO)) {
                    if (flag) {
                        MobclickAgent.onEvent(getActivity(), "timeTable");//统计
                        Intent intent = new Intent(getActivity(), WeekListActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.CHACHENGJI)) {
                    if (flag) {
                        MobclickAgent.onEvent(getActivity(), "queryScore");//统计
                        Intent intent = new Intent(getActivity(), GradeListActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.KAOSHIANPAI)) {
                    if (flag) {
                        MobclickAgent.onEvent(getActivity(), "examPlan");//统计
                        Intent intent = new Intent(getActivity(), TestListActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.KONGJIAOSHI)) {
                    if (flag) {
                        MobclickAgent.onEvent(getActivity(), "emptyRoom");//统计
                        Intent intent = new Intent(getActivity(), FloorListActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.SHIKEBIAO)) {
                    if (flag) {
                        MobclickAgent.onEvent(getActivity(), "schoolBus");//统计
                        Intent intent = new Intent(getActivity(), SchoolBusActivity.class);
                        intent.putExtra("style", 2);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.KAOLA)) {
                    if (flag) {
                        MobclickAgent.onEvent(context, "kaola");//云阅读
                        Intent intent = new Intent(getActivity(), KaoLaActivity.class);
                        intent.putExtra("url", "http://m.kaola.com/");
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.YUEDU)) {
                    if (flag) {
                        MobclickAgent.onEvent(context, "yunyuedu");//云阅读
                        Intent intent = new Intent(getActivity(), KaoLaActivity.class);
                        intent.putExtra("url", "http://m.yuedu.163.com/?_tdchannel=X8Joccry5&_tdcid=j3Oocekjr");
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.MANHUA)) {
                    if (flag) {
                        MobclickAgent.onEvent(context, "manhua");//漫画
                        Intent intent = new Intent(getActivity(), KaoLaActivity.class);
                        intent.putExtra("url", "http://manhua.163.com?utm_source=qingcai");
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.SHUIHUA)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), SearchFlyActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.BANSHI)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), BanshiActivity.class);
                        intent.putExtra("type",1);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.GUIZHANGZHIDU)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), BanshiActivity.class);
                        intent.putExtra("type",2);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.XINXIFABU)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), MessageActivity.class);
                        intent.putExtra("style", 1);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.GONGWEN)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), MessageActivity.class);
                        intent.putExtra("style", 2);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.ZHOUHUIYI)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), WeekMeetingListActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.BAOGAOTING)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), WeekMeetingListActivity.class);
                        intent.putExtra("type", 2);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.ZHONGDIAN)) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), KongMainActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.ERSHOUSHICHANG)) {//二手市场
                    if (flag) {
                        Intent intent = new Intent(getActivity(), MainMarket.class);
                        intent.putExtra("module", 2);//1-失物招领  2-二手市场  3-兼职招聘
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.SHIWUZHAOLING)) {//失物招领
                    if (flag) {
                        Intent intent = new Intent(getActivity(), MainMarket.class);
                        intent.putExtra("module", 1);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.JIANZHI)) {//兼职
                    if (flag) {
                        Intent intent = new Intent(getActivity(), MainMarket.class);
                        intent.putExtra("module", 3);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                } else if (functionType.equals(Constants.MUKE)) {//慕课
                    if (flag) {
                        Intent intent = new Intent(getActivity(), MukeActivity.class);
                        startActivity(intent);
                        MobclickAgent.onEvent(context, "muke");
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                }else if (functionType.equals(Constants.ZHAOPIANQIANG)) {//照片墙
                    if (flag) {
                        Intent intent = new Intent(getActivity(), PhotoMainActivity.class);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "敬请期待!");
                    }
                }
            }
        });
    }

    /**
     * 初始化轮播图片
     *
     * @param view
     */
    private void initPhoto(View view) {
        slidshowView = (BannerImg) view.findViewById(R.id.ssv_slid_show_view2);
//		// 首先加载缓存
//        Message message = new Message();
//        message.what = 4;
//        handler.sendMessage(message);
        // 获取banner数据
        new Thread() {
            public void run() {
                bannerJson = HttpUtils.httpClientPost(Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getBannerInfo") + Constants.BASEPARAMS, null);
                Log.i("bannerData===>", bannerJson + "");
                // 加载数据
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
        }.start();
    }

    // 缓存用户信息
    public void loadUsers() {
        String samUser = share.getString("samUserInfo", "").toString();
        try {
            if (samUser != null && !"".equals(samUser)) {
                JSONObject samUserInfo = new JSONObject(samUser);
                // 账户
                share.put("userTrueName", samUserInfo.getString("userName") + "");//2016-6-16保存用户真实姓名
                // 账户
                ground_account.setText(samUserInfo.getString("userName"));
                ground_tv_balance.setText(samUserInfo.getString("accountFee"));
                // 套餐
                String meal = samUserInfo.getString("packageName");
                String group = samUserInfo.getString("userGroupName");
                if (Constants.FEE.equals(meal) || Constants.FEE.contains(meal)
                        || "网络运营".equals(group) || meal.contains("试用套餐")) {
                    ll_meal.setVisibility(View.GONE);
                    ll_time.setVisibility(View.GONE);
                    ll_open.setVisibility(View.VISIBLE);
                    String exchange = getActivity().getResources().getString(R.string.meal_open);
                    System.out.println(exchange + "'----");
                    ground_tv_open.setText(Html.fromHtml(exchange));
                    ground_tv_open.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent(getActivity(), MealsActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    ll_meal.setVisibility(View.GONE);
                    ll_time.setVisibility(View.GONE);
                    ll_open.setVisibility(View.GONE);
                    if (meal.indexOf("|") > 0) {
                        meal = meal.substring(0, meal.indexOf("|"));
                    }
                    ground_tv_meal.setText(meal);
                    String next = samUserInfo.getString("nextBillingTime");

                    if (meal.contains("包学期")) {
                        ground_tv_time.setText("本学期末");
                    } else {
                        ground_tv_time.setText(next.substring(0, next.indexOf("T")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 初始化用户信息
    public void initInfomenu() {
        loadUsers();    // 首先加载缓存信息
        new Thread() {
            public void run() {
                String username = share.getString("username", "").toString();
                String token = share.getString("token", "").toString();
                if (!"".equals(username) && !"".equals(token)) {
                    Map<String, String> postParams = new HashMap<String, String>();
                    postParams.put("username", username);
                    postParams.put("token", token);
                    try {
                        json = new JSONObject(share.getString("menujson", ""));
//                        System.out.println("获取的json"+json);
//                        json = new JSONObject(json.getString("result"));
                        String code = json.getString("code");
                        messageStr = json.getString("msg");
                        if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                            String menus = json.getString("appInfo");
                            if (!menus.equals("[]")) {
                                JSONArray menuarray = new JSONArray(menus);
                                menulist.clear();
                                for (int i = 0; i < menuarray.length(); i++) {
                                    JSONObject obj = (JSONObject) menuarray.get(i);
                                    Menus menu = new Menus();
                                    menu.setAI_NAME(Function.getInstance().getString(obj, "AI_NAME"));
                                    menu.setNEWICON(Function.getInstance().getString(obj, "NEWICON"));
                                    menu.setAI_DESCRIBE(Function.getInstance().getString(obj, "AI_DESCRIBE"));
                                    menu.setSDS_CODE(Function.getInstance().getString(obj, "SDS_CODE"));
                                    menu.setNEWNAME(Function.getInstance().getString(obj, "NEWNAME"));
                                    menu.setAG_NAME(Function.getInstance().getString(obj, "AG_NAME"));
                                    menu.setAI_DEFAULTICON(Function.getInstance().getString(obj, "AI_DEFAULTICON"));
                                    menu.setAG_ID(Function.getInstance().getInteger(obj, "AG_ID"));
                                    menu.setAI_ID(Function.getInstance().getInteger(obj, "AI_ID"));
                                    menu.setRSA_ENABLE(Function.getInstance().getBoolean(obj, "RSA_ENABLE"));
                                    menulist.add(menu);
                                }
                            }
                            Log.e("schoolInfo", json.getString("schoolInfo"));
                            share.put("schoolInfo", json.getString("schoolInfo"));
                            json = new JSONObject(json.getString("data"));
                            share.put("canModifyPassword", json.getBoolean("canModifyPassword"));
                            share.put("canPayNetFee", json.getBoolean("canPayNetFee"));
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        } else if (code.equals(CacheConstants.TOKEN_FAIL)) {
                            Message msg = new Message();
                            msg.what = 9001;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = 9001;
                            handler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }
            }
        }.start();
    }

    // 初始化用户信息
    public void initInfo() {
        loadUsers();    // 首先加载缓存信息
        new Thread() {
            public void run() {
                String username = share.getString("username", "").toString();
                String token = share.getString("token", "").toString();
                if (!"".equals(username) && !"".equals(token)) {
                    Map<String, String> postParams = new HashMap<String, String>();
                    postParams.put("username", username);
                    postParams.put("token", token);
                    try {
                        json = HttpUtils.httpClientPost(Constants.BASE_URL + "?rid=" + ReturnUtils.encode("queryUserInfo") + Constants.BASEPARAMS, postParams);
                        System.out.println("获取的json" + json);
                        json = new JSONObject(json.getString("result"));
                        String code = json.getString("code");
                        messageStr = json.getString("msg");
                        if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                            Log.e("schoolInfo", json.getString("schoolInfo"));
                            share.put("schoolInfo", json.getString("schoolInfo"));
                            json = new JSONObject(json.getString("data"));
                            share.put("canModifyPassword", json.getBoolean("canModifyPassword"));
                            share.put("canPayNetFee", json.getBoolean("canPayNetFee"));
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        } else if (code.equals(CacheConstants.TOKEN_FAIL)) {
                            Message msg = new Message();
                            msg.what = 9001;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = 9001;
                            handler.sendMessage(msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }
            }
        }.start();
    }

    //判断连接的是不是规定wifi
    public boolean isWifi() {
        // 判断WIFINAME
        String wifiName = share.getString("wifiName", "").toString();
        // 获取当前系统wifi
        String ssid = ConnectivityUtils.getConnectWifiSsid(BaseApplication.getInstance());
        ssid = ssid.replaceAll("\"", "").trim();
        return wifiName.contains(ssid);
    }

    Handler nethandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    final CommDialog dlog = new CommDialog(getActivity());
                    dlog.CreateDialogOnlyOk("系统提示", "确定", "网络已连接", new CallBack() {
                        @Override
                        public void isConfirm(boolean flag) {
                            if (netstate != -1) {
                                functionData.get(netstate).put("funImage", R.mipmap.btn_dis_network);
                                functionData.get(netstate).put("funDes", "断开网络");
                            }
                            netFlag = true;
//                            ground_iv_buy.setImageResource(R.mipmap.btn_dis_network);
//                            ground_tv_buy_des.setText("断开网络");
//                            ground_rl_net.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    breakNet();//断开网络
//                                }
//                            });
                            dlog.cancelDialog();
                            flistAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2:
                    check();//网络异常提示重试或保修
                    break;
                case 3:
                    if (netstate != -1) {
                        functionData.get(netstate).put("funImage", R.mipmap.btn_dis_network);
                        functionData.get(netstate).put("funDes", "断开网络");
                    }
                    netFlag = true;
//                    ground_iv_buy.setImageResource(R.mipmap.btn_dis_network);
//                    ground_tv_buy_des.setText("断开网络");
                    final CommDialog dlog3 = new CommDialog(getActivity());
                    dlog3.CreateDialogOnlyOk("系统提示", "确定", "网络连接成功", new CallBack() {
                        @Override
                        public void isConfirm(boolean flag) {
                            if (flag) {
//                                ground_rl_net.setOnClickListener(new OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        breakNet();
//                                    }
//                                });
                                dlog3.cancelDialog();
                            }
                        }
                    });
                    flistAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    String error = (String) msg.obj;
                    final CommDialog dlog1 = new CommDialog(getActivity());
                    dlog1.CreateDialogOnlyOk("系统提示", "确定", error, new CallBack() {
                        @Override
                        public void isConfirm(boolean flag) {
                            if (flag) {
                                if (netstate != -1) {
                                    functionData.get(netstate).put("funImage", R.mipmap.btn_dis_network);
                                    functionData.get(netstate).put("funDes", "断开网络");
                                }
                                netFlag = true;
//                                ground_iv_buy.setImageResource(R.mipmap.btn_dis_network);
//                                ground_tv_buy_des.setText("断开网络");
//                                ground_rl_net.setOnClickListener(new OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        breakNet();
//                                    }
//                                });
                                dlog1.cancelDialog();
                                flistAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;
                case 5:
                    final CommDialog dlog2 = new CommDialog(getActivity());
                    dlog2.CreateDialogOnlyOk("系统提示", "确定", "网络已断开", new CallBack() {
                        @Override
                        public void isConfirm(boolean flag) {
                            if (netstate != -1) {
                                functionData.get(netstate).put("funImage", R.mipmap.btn_key_intenet);
                                functionData.get(netstate).put("funDes", "一键上网");
                            }
                            netFlag = false;
//                            ground_iv_buy.setImageResource(R.mipmap.btn_key_intenet);
//                            ground_tv_buy_des.setText("一键上网");
//                            isShow = true;
//                            ground_rl_net.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    showConn();//重新设置点击事件
//                                }
//                            });
                            dlog2.cancelDialog();
                            flistAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 6:
                    String errorStr = (String) msg.obj;
                    final CommDialog dlog4 = new CommDialog(getActivity());
                    dlog4.CreateDialogOnlyOk("系统提示", "确定", errorStr, new CallBack() {
                        @Override
                        public void isConfirm(boolean flag) {
                            dlog4.cancelDialog();
                        }
                    });
                    break;
            }
        }
    };

    /**
     * @author wang
     * @date 2016-5-23
     * @version 2.0
     * 显示一键上网/断开网络
     */
    private void showConn() {
        abHttpUtil.get("http://www.baidu.com/", new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                if (!TextUtils.isEmpty(content)) {
                    if (content.indexOf("百度一下") > 0) {//已连接规定wifi并且能够上网
                        Constants.ISRUN = true;
                        if (netstate != -1) {
                            functionData.get(netstate).put("funImage", R.mipmap.btn_dis_network);
                            functionData.get(netstate).put("funDes", "断开网络");
                        }
//                        ground_iv_buy.setImageResource(R.mipmap.btn_dis_network);
//                        ground_tv_buy_des.setText("断开网络");
                        netFlag = true;
//                        if (isShow) {
//                            final CommDialog dia = new CommDialog(getActivity());
//                            dia.CreateDialogOnlyOk("系统提示", "确定", "不是从本系统上网，请从上网验证的页面关闭网络", new CallBack() {
//                                @Override
//                                public void isConfirm(boolean flag) {
////                                    ground_rl_net.setOnClickListener(new OnClickListener() {
////                                        @Override
////                                        public void onClick(View arg0) {//请求断开连接
//////                                 `            NewBreakNet();//断开网络
////                                            breakNet();
////                                        }
////                                    });
//                                    dia.cancelDialog();
//                                }
//                            });
//                        } else {
//                            ground_rl_net.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View arg0) {//请求断开连接
////                                 `    NewBreakNet();//断开网络
//                                    breakNet();
//                                }
//                            });
//                        }
                    } else {//如果连接了wifi,并且访问百度重定向到其他界面，设置点击按钮显示一键上网
                        Constants.ISRUN = false;
                        netFlag = false;
//                        if (isShow) {
//                            EportalValidate();
//                        } else {
                            if (netstate != -1) {
                                functionData.get(netstate).put("funImage", R.mipmap.btn_key_intenet);
                                functionData.get(netstate).put("funDes", "一键上网");
                            }
//                            ground_iv_buy.setImageResource(R.mipmap.btn_key_intenet);
//                            ground_tv_buy_des.setText("一键上网");
//                            ground_rl_net.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View arg0) {
////                                  NewEportalValidate();//一键上网
//                                    EportalValidate();
//                                }
//                            });
//                        }
                    }
                }
                flistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {

            }
        });
    }

    /**
     * 旧版
     *
     * @des 一键上网认证
     */
    private void EportalValidate() {
        if (isWifi()) {// 是规定wifi
            AbDialogUtil.showProgressDialog(getActivity(), 0, getActivity().getResources().getString(R.string.loading));
            MobclickAgent.onEvent(getActivity(), "oneKeyNet");//友盟统计
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Log.d("", "第一步.........................");
                    try {
                        json = EportalUtils.httpClientGet("http://www.baidu.com/");
                        if (json == null) {
                            Message netMessage = new Message();
                            netMessage.what = 2;
                            nethandler.sendMessage(netMessage);
                        } else {
                            String url = json.getString("result");
                            if (url.contains("<title>百度一下，你就知道</title>") || url.indexOf("<script>top.self.location.href") > 0) {
                                // 说明连接了网络
                                Constants.ISRUN = true;
                                Message netMessage = new Message();
                                netMessage.what = 1;
                                nethandler.sendMessage(netMessage);
                            } else {
                                // 说明无网络连接，连接eportal
                                url = url.substring(url.indexOf("<script>"), url.indexOf("</script>"));
                                url = StringUtils.subStringWord(url, "<script>", "</script>");
                                url = StringUtils.subStringWord(url, "top.self.location.href='", "'");
                                // 执行第二步 跳转到Eportal认证界面
                                Log.d("", "第二步认证.............................................");
                                json = EportalUtils.httpClientGet(url);
                                Log.d("从这开始。。。。。。。。。。", "再次截取");
                                if (json == null) {
//                                check();//网络异常提示重试或保修
                                    Message netMessage = new Message();
                                    netMessage.what = 2;
                                    nethandler.sendMessage(netMessage);
                                } else {
                                    String second = json.getString("result");
                                    // 第三步认证
                                    Log.d("", "第三步认证.........................................");
                                    json = EportalUtils.httpClientGet("http://1.1.1.1/?rand=0.619542766188214");
                                    if (json == null) {
//                                    check();//网络异常提示重试或保修
                                        Message netMessage = new Message();
                                        netMessage.what = 2;
                                        nethandler.sendMessage(netMessage);
                                    } else {
                                        String nextUrl = StringUtils.getStringBetween(json.getString("result"), "<NextURL>", "</NextURL>");
                                        // 第四步认证
                                        Log.d("第四步认证........" + nextUrl, "");
                                        String baseUrl = StringUtils.getStringBetween(second, "<form", "</form>");
                                        baseUrl = StringUtils.getStringBetween(baseUrl, "action='", "'><div");
                                        baseUrl = url.substring(0, url.indexOf("index.jsp")) + baseUrl;
                                        Constants.baseUrl = baseUrl.substring(0, baseUrl.indexOf("?"));
                                        share.put("baseUrl", Constants.baseUrl);
                                        // 获取账号和密码，密码需要解密
                                        String account = share.getString("username", "");
                                        String pwd = share.getString("pwd", "");
                                        pwd = AESUtils.decode(pwd);
                                        Map<String, String> postparams = new HashMap<String, String>();
                                        String param = url.substring(url.indexOf("?") + 1);
                                        param += "&param=true&username=" + account + "&pwd=" + pwd + "";
                                        baseUrl += "&" + param;
                                        Log.d("baseUrl===", baseUrl);
                                        json = EportalUtils.httpClientPost(baseUrl, postparams);
                                        Log.d("json", json.toString());
                                        if (json == null) {
//                                        check();//网络异常提示重试或保修
                                            Message netMessage = new Message();
                                            netMessage.what = 2;
                                            nethandler.sendMessage(netMessage);
                                        } else {
                                            String res = json.getString("result");
                                            if (res.indexOf("d.contentDive.userIndex=") > 0) {
                                                String index = "d.contentDive.userIndex='";
                                                String userIndex = res.substring(res.indexOf(index) + index.length());
                                                userIndex = userIndex.substring(0, userIndex.indexOf("';"));
                                                Constants.userIndex = userIndex;
                                                share.put("userIndex", Constants.userIndex);
                                                System.out.println(Constants.baseUrl + " eportal - " + Constants.userIndex);
                                                Constants.TRY_COUNT = 0;
                                                Constants.ISRUN = true;

                                                //登录成功后将点击事件设置断开网络
                                                Message netMessage = new Message();
                                                netMessage.what = 3;
                                                nethandler.sendMessage(netMessage);

                                            } else if (res.indexOf("errorInfo") > 0) {
                                                Log.d("", "错误");
                                                String error = res.substring(res.indexOf("<body id="));
                                                error = error.substring(error.indexOf("errorInfo_center"));
                                                error = error.substring(error.indexOf("val=") + 5, error.indexOf("</div>") - 2);
                                                Log.d("", error);
                                                Constants.ISRUN = false;
//                                            check();//提示重试或保修
                                                Message netMessage = new Message();
                                                netMessage.what = 6;
                                                netMessage.obj = error;
                                                nethandler.sendMessage(netMessage);
                                            } else {
                                                Log.d("", res);
                                                if (res.indexOf("<script>") >= 0) {
                                                    String error = res.substring(res.indexOf("alert('"), res.indexOf("'); "));
                                                    String userIndex = res.substring(res.indexOf("userIndex="));
                                                    Constants.userIndex = userIndex.substring(0, userIndex.indexOf("&"));
                                                    Constants.ISRUN = false;
                                                    //登录成功提示断开网络
                                                    Message netMessage = new Message();
                                                    netMessage.what = 4;
                                                    netMessage.obj = error;
                                                    nethandler.sendMessage(netMessage);

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        check();//出现异常提示保修
                        Message netMessage = new Message();
                        netMessage.what = 2;
                        nethandler.sendMessage(netMessage);
                    } finally {
                        AbDialogUtil.removeDialog(getActivity());
                    }
                }
            }.start();
        } else {
            // 连接的不是规定wifi,显示一键上网
            if (netstate != -1) {
                functionData.get(netstate).put("funImage", R.mipmap.btn_key_intenet);
                functionData.get(netstate).put("funDes", "一键上网");
            }
            netFlag = false;
            final CommDialog dog = new CommDialog(getActivity());
            dog.CreateDialogOnlyOk("系统提示", "确定", Constants.NOT_WIFI, new CallBack() {
                @Override
                public void isConfirm(boolean flag) {
//                    ground_rl_net.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            isShow = true;
//                            showConn();
//                        }
//                    });
                    dog.cancelDialog();
                }
            });
            flistAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 旧版断开网络
     */
    private void breakNet() {
        MobclickAgent.onEvent(getActivity(), "breakNet");//友盟统计 -- 断开网络
        if (TextUtils.isEmpty(Constants.baseUrl)) {
            Constants.baseUrl = share.getString("baseUrl", "");
        }
        if (TextUtils.isEmpty(Constants.userIndex)) {
            Constants.userIndex = share.getString("userIndex", "");
        }
        if (!TextUtils.isEmpty(Constants.baseUrl) && !TextUtils.isEmpty(Constants.userIndex) && isWifi()) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    AbDialogUtil.showProgressDialog(getActivity(), 0, getActivity().getResources().getString(R.string.loading), "breakTag");
                    String baseUrl = Constants.baseUrl + "?method=logout&userIndex=" + Constants.userIndex;
                    Log.d("baseUrl", baseUrl);
                    breakJson = EportalUtils.httpClientPost(baseUrl, null);
                    try {
                        Log.d("breakJson", breakJson + "");
                        String reString = null;
                        if (breakJson != null) {
                            if (breakJson.getString("result") != null) {
                                reString = breakJson.getString("result").toString();
                            }
                            if (reString != null && !"".equals(reString)) {
                                String nextUrl = reString.substring(reString.indexOf("<script type="), reString.indexOf("</script>"));
                                nextUrl = nextUrl.substring(nextUrl.indexOf("?"), nextUrl.indexOf("\");"));
                                breakJson = EportalUtils.httpClientGet(Constants.baseUrl + nextUrl);
                                if (breakJson != null) {
                                    Log.d("breakJson", breakJson + "");
                                    String title = breakJson.getString("result").toString();
                                    if (title.indexOf("您已经下线") > 0 || title.indexOf("您还未登录或者Session超时,请重新登录后再操作") > 0) {
                                        Log.d("", "成功下线");
                                        Constants.ISRUN = false;
                                        Message breakMessage = new Message();
                                        breakMessage.what = 5;
                                        nethandler.sendMessage(breakMessage);
                                    }
                                } else {
                                    ToastManager.getInstance().showToast(BaseApplication.getInstance(), "断开网络失败，请重试");
                                }
                            }
                        } else {
                            ToastManager.getInstance().showToast(BaseApplication.getInstance(), "断开网络失败，请重试");
                        }
                        AbDialogUtil.removeDialog(getActivity(), "breakTag");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("breakJson" + breakJson, "");
                        ToastManager.getInstance().showToast(BaseApplication.getInstance(), "断开网络失败，请重试");
                        AbDialogUtil.removeDialog(getActivity(), "breakTag");
                    }
                }
            }.start();
        } else {
            final CommDialog dia = new CommDialog(getActivity());
            dia.CreateDialogOnlyOk("系统提示", "确定", "不是从本系统上网，请从上网验证的页面关闭网络", new CallBack() {
                @Override
                public void isConfirm(boolean flag) {
                    dia.cancelDialog();
//                    ground_rl_net.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            isShow = true;
//                            showConn();
//                        }
//                    });
                }
            });
        }

    }

    /**
     * 新版
     *
     * @date 2016-5-23
     * @version 2.0
     * @author wang
     * @des 断开网络连接
     */
    private void NewBreakNet() {
        MobclickAgent.onEvent(getActivity(), "breakNet");//友盟统计--断开网络
        if (TextUtils.isEmpty(Constants.baseUrl)) {
            Constants.baseUrl = share.getString("baseUrl", "");
        }
        if (TextUtils.isEmpty(Constants.userIndex)) {
            Constants.userIndex = share.getString("userIndex", "");
        }
        if (TextUtils.isEmpty(Constants.userIndex) || TextUtils.isEmpty(Constants.baseUrl)) {//说明不是从本页面登录的
            final CommDialog dia = new CommDialog(getActivity());
            dia.CreateDialogOnlyOk("系统提示", "确定", "不是从本系统上网，请从上网验证的页面关闭网络", new CallBack() {
                @Override
                public void isConfirm(boolean flag) {
                    dia.cancelDialog();
//                    ground_rl_net.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            isShow = true;
//                            showConn();
//                        }
//                    });
                }
            });
        } else {
            String baseUrl = Constants.baseUrl + "logout";
            AbRequestParams requestParams = new AbRequestParams();
            requestParams.put("userip", ConnectivityUtils.getSysIp(context));
            requestParams.put("userIndex", Constants.userIndex);
            abHttpUtil.post(baseUrl, requestParams, new AbStringHttpResponseListener() {
                @Override
                public void onSuccess(int statusCode, String content) {
                    if (!TextUtils.isEmpty(content)) {
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            if (jsonObject.getString("result").equals("success")) {
                                Constants.ISRUN = true;
                                if (netstate != -1) {
                                    functionData.get(netstate).put("funImage", R.mipmap.btn_key_intenet);
                                    functionData.get(netstate).put("funDes", "一键上网");
                                }
//                                ground_iv_buy.setImageResource(R.mipmap.btn_key_intenet);
//                                ground_tv_buy_des.setText("一键上网");
                                ToastManager.getInstance().showToast(BaseApplication.getInstance(), "已断开连接");
                                flistAdapter.notifyDataSetChanged();
                            } else {
                                ToastManager.getInstance().showToast(BaseApplication.getInstance(), "网络断开异常,请重试!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastManager.getInstance().showToast(BaseApplication.getInstance(), "网络断开异常,请重试!");
                    }
                }

                @Override
                public void onStart() {
                    AbDialogUtil.showProgressDialog(getActivity(), 0, getActivity().getResources().getString(R.string.loading));
                }

                @Override
                public void onFinish() {
                    AbDialogUtil.removeDialog(getActivity());
                }

                @Override
                public void onFailure(int statusCode, String content, Throwable error) {
                    AbDialogUtil.removeDialog(getActivity());
                    ToastManager.getInstance().showToast(BaseApplication.getInstance(), content);
                }
            });
        }
    }

    /**
     * 新版
     *
     * @Date 2016-5-23
     * @version 2.0
     * @author Wang
     * @Des 最新一键上网接口认证
     */
    private void NewEportalValidate() {
        AbDialogUtil.showProgressDialog(getActivity(), 0, getActivity().getResources().getString(R.string.loading));
        MobclickAgent.onEvent(getActivity(), "oneKeyNet");//友盟统计
        WebView webview;
        webview = new WebView(context);
        webview.layout(0, 0, 0, 0);
        final WebSettings settings = webview.getSettings();
        AbTask abTask = AbTask.newInstance();
        AbTaskItem abTaskItem = new AbTaskItem();
        abTaskItem.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
            }

            @Override
            public void update() {
                super.update();
                try {
                    Log.e(".", "第一步...................................................");
                    json = EportalUtils.httpClientGet("http://www.baidu.com/");
                    String url = json.getString("result");
                    if (url.contains("<title>百度一下，你就知道</title>") || url.indexOf("<script>top.self.location.href") != -1) {
                        // 说明连接了网络
                        Constants.ISRUN = true;
                        AbDialogUtil.removeDialog(getActivity());
                        final CommDialog dlog = new CommDialog(getActivity());
                        dlog.CreateDialogOnlyOk("系统提示", "确定", "网络已连接", new CallBack() {

                            @Override
                            public void isConfirm(boolean flag) {
                                dlog.cancelDialog();
                                if (flag) {
                                    if (netstate != -1) {
                                        functionData.get(netstate).put("funImage", R.mipmap.btn_dis_network);
                                        functionData.get(netstate).put("funDes", "断开网络");
                                    }
                                    ground_iv_buy.setImageResource(R.mipmap.btn_dis_network);
                                    ground_tv_buy_des.setText("断开网络");
                                    ground_rl_net.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            NewBreakNet();//断开网络
                                        }
                                    });
                                    flistAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    } else {
                        // 说明无网络连接，连接eportal
                        url = url.substring(url.indexOf("<script>"), url.indexOf("</script>"));
                        // 执行第二步 跳转到Eportal认证界面
                        Log.e("", "第二步认证..................");
                        String queryString = url.substring(url.indexOf("wlanuserip"), url.length() - 1);
                        Constants.queryString = queryString;
                        url = url.substring(url.indexOf("http://"), url.indexOf("/eportal/"));
                        String userId = ConnectivityUtils.getSysIp(context);
                        String username = share.getString("username", "-1");
                        String pwd = AESUtils.decode(share.getString("pwd", "-1"));
                        Log.e("queryString=====", queryString);
                        String ua = settings.getUserAgentString();//获取UserAgent
                        Log.d("User Agent:", ua);
                        final String baseUrl = url + "/eportal/inferface/authAPI/login";
                        Constants.baseUrl = url + "/eportal/inferface/authAPI/";
                        final AbRequestParams requestParams = new AbRequestParams();
                        requestParams.put("userip", userId + "");
                        requestParams.put("userId", username);
                        requestParams.put("password", pwd);
                        requestParams.put("queryString", queryString);
                        requestParams.put("userAgent", ua);
                        CommonPopupWindow.OnKeyNetPopupWindow(getActivity(), ground_rl_net, new CommonPopupWindow.OneKeyNetCallBack() {
                            @Override
                            public void confirmText(String text) {
                                if (text.equals("0")) {//0:校内网
                                    requestParams.put("service", "");
                                    OneKeyNet(baseUrl, requestParams);
                                } else if (text.equals("1")) {//互联网
                                    requestParams.put("service", "");
                                    OneKeyNet(baseUrl, requestParams);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    check();//出现异常提示重试或保修
                }
            }
        });
        abTask.execute(abTaskItem);//执行

    }

    /**
     * 新版
     *
     * @author wang
     * @date 2016-5-23
     * @version 2.0
     * @des 一键上网认证
     */
    private void OneKeyNet(String baseUrl, AbRequestParams requestParams) {
        abHttpUtil.post(baseUrl, requestParams, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    Log.d("json=====>", jsonObject.toString());
                    String msg = jsonObject.getString("success");
                    ToastManager.getInstance().showToast(context, msg);
                    if (msg.equals("success")) {
                        Constants.userIndex = jsonObject.getString("userIndex");
                        System.out.println(Constants.baseUrl + " eportal - " + Constants.userIndex);
                        //登录成功后保存 baseUrl 和 userIndex，断开连接时需要用到
                        share.put("baseUrl", Constants.baseUrl);
                        share.put("userIndex", Constants.userIndex);
                        Constants.TRY_COUNT = 0;
                        Constants.ISRUN = true;
                        if (netstate != -1) {
                            functionData.get(netstate).put("funImage", R.mipmap.btn_dis_network);
                            functionData.get(netstate).put("funDes", "断开网络");
                        }
//                        ground_iv_buy.setImageResource(R.mipmap.btn_dis_network);
//                        ground_tv_buy_des.setText("断开网络");
                        ground_rl_net.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NewBreakNet();//断开网络
                            }
                        });
                        flistAdapter.notifyDataSetChanged();
                    } else {//登录失败
                        check();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    check();
                } finally {
                    AbDialogUtil.removeDialog(getActivity());
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                AbDialogUtil.removeDialog(getActivity());
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                AbDialogUtil.removeDialog(getActivity());
                check();
            }
        });

    }

    /**
     * 新版
     *
     * @author wang
     * @date 2016-5-23
     * @version 2.0
     * @des 重试3次，提示保修
     */
    private void check() {
        Constants.TRY_COUNT++;
        if (Constants.TRY_COUNT <= 3) {
            final CommDialog dia = new CommDialog(context);
            dia.CreateDialog("重试", "系统提示", "网络连接异常", context, new CallBack() {

                @Override
                public void isConfirm(boolean flag) {
                    if (flag) {
                        Log.d("验证登录.......", "");
//                      NewEportalValidate();
                        EportalValidate();
                    } else {
                        Constants.ISRUN = false;
                    }
                    dia.cancelDialog();
                }
            });
        } else {
            Constants.TRY_COUNT = 0;
            final CommDialog dia = new CommDialog(getActivity());
            dia.CreateDialog("报修", "网络连接异常", "点击报修查看常用解决方案或报修", getActivity(), new CallBack() {
                @Override
                public void isConfirm(boolean flag) {
                    if (flag) {
                        Intent intent = new Intent(getActivity(), RepairQuestionListActivity.class);
                        startActivity(intent);
                        // 报修
//                        final CommDialog dlog = new CommDialog(getActivity());
//                        dlog.CreateDialog("拨打", "确认拨打维修电话?", Constants.HELP_TRIP, getActivity(), new CallBack() {
//
//                            @Override
//                            public void isConfirm(boolean flag) {
//                                if (flag) {
//                                    Intent intent = new Intent();
//                                    intent.setAction(Intent.ACTION_CALL);
//                                    intent.setData(Uri.parse(Constants.HELP_PHONE));
//                                    startActivity(intent);
//                                }
//                                Constants.ISRUN = false;
//                                dlog.cancelDialog();
//                            }
//                        });
                    }
                }
            });
        }
    }

    // 初始化校园广播数据
    private List<Map<String, String>> getData() {
        data = new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("title", "登录操作流程说明");
        map1.put("time", Constants.HELP_LOGIN_URL);
        data.add(map1);
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("title", "套餐购买操作流程说明");
        map2.put("time", Constants.HELP_BUY_URL);
        data.add(map2);
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("title", "一键上网操作流程说明");
        map3.put("time", Constants.HELP_ONEKEY_URL);
        data.add(map3);
        return data;
    }

    // 网费缴纳
    private void pay(View view) {
        ground_rl_pay = (RelativeLayout) view.findViewById(R.id.ground_rl_pay);
//		ground_iv_pay = (ImageView) view.findViewById(R.id.ground_iv_pay);
        // 点击事件跳转套餐选择界面
        ground_rl_pay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MobclickAgent.onEvent(context, "pay_select");//统计
                // 跳转到套餐选择页面
                Intent intent = new Intent(getActivity(), MealsActivity.class);
                startActivity(intent);
            }
        });
    }

    // 报修
    private void repair(View view) {
        ground_rl_repair = (RelativeLayout) view.findViewById(R.id.ground_rl_repair);
        // 点击事件跳转常见问题及解决方案
        ground_rl_repair.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                // 跳转到常见问题及解决方案页面
                Intent intent = new Intent(getActivity(), RepairQuestionListActivity.class);
                startActivity(intent);
//                final CommDialog dlog = new CommDialog(getActivity());
//                dlog.CreateDialog("拨打", "确认拨打维修电话?", Constants.HELP_TRIP, getActivity(), new CallBack() {
//
//                    @Override
//                    public void isConfirm(boolean flag) {
//                        if (flag) {
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_CALL);
//                            intent.setData(Uri.parse(Constants.HELP_PHONE));
//                            startActivity(intent);
//                        }
//                        dlog.cancelDialog();
//                    }
//                });

            }
        });
    }

    /**
     * 实时侦听
     */
    public void refresh() {
        abHttpUtil.get("http://61.135.169.121/", new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                if (!TextUtils.isEmpty(content)) {
                    if (content.contains("百度一下")) {
                        //防止从WiFi切换到流量状态，还一直处于监听状态
                        if (!ConnectivityUtils.isWifi(getActivity())) {
                            Constants.ISRUN = false;
                        }
                        // 网络正常
                        Log.e("", "网络正常.......");
                        if (netstate != -1) {
                            functionData.get(netstate).put("funImage", R.mipmap.btn_dis_network);
                            functionData.get(netstate).put("funDes", "断开网络");
                            netFlag = true;
                        }
                    } else {
                        Constants.ISRUN = false;
                        // 已经断网了
                        if (netstate != -1) {
                            functionData.get(netstate).put("funImage", R.mipmap.btn_key_intenet);
                            functionData.get(netstate).put("funDes", "一键上网");
                        }
                        netFlag = false;
//                        ground_iv_buy.setImageResource(R.mipmap.btn_key_intenet);
//                        ground_tv_buy_des.setText("一键上网");
//                        ground_rl_net.setOnClickListener(new OnClickListener() {
//                            @Override
//                            public void onClick(View arg0) {
//                                isShow = true;
//                                showConn();
//                            }
//                        });
                    }
                    flistAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
//                AbToastUtil.showToast(BaseApplication.getInstance(), "出错了....");
                Constants.ISRUN = false;
                // 已经断网了
                if (netstate != -1) {
                    functionData.get(netstate).put("funImage", R.mipmap.btn_key_intenet);
                    functionData.get(netstate).put("funDes", "一键上网");
                }
                netFlag = false;
//                ground_iv_buy.setImageResource(R.mipmap.btn_key_intenet);
//                ground_tv_buy_des.setText("一键上网");
//                ground_rl_net.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View arg0) {
//                        isShow = true;
//                        showConn();
//                    }
//                });
                flistAdapter.notifyDataSetChanged();
            }
        });
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10 * 1000);
                    // 发送消息
                    handler.sendMessage(handler.obtainMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ToastManager.getInstance().showToast(context, Constants.NETWORK_ERROR);
            } else if (msg.what == 2) {
                addFunction();//配置菜单
                showConn();// 显示一键上网/断开网络
                try {
                    System.out.println(json);
                    if (json != null) {
                        samUserInfo = json.getString("samUserInfo").equals("[]") ? null : new JSONObject(json.getString("samUserInfo"));
                        JSONObject localUserInfo = json.getString("localUserInfo").equals("[]") ? null : new JSONObject(json.getString("localUserInfo"));
                        System.out.println(localUserInfo);
                    } else
                        samUserInfo = null;
                    System.out.println("samUserInfo:" + samUserInfo);
                    if (samUserInfo != null) {
                        share.put("samUserInfo", json.getString("samUserInfo"));
                    } else {
                        samUserInfo = new JSONObject(share.getString("samUserInfo", ""));
                    }
                    share.put("userTrueName", samUserInfo.getString("userName") + "");//2016-6-16保存用户真实姓名
                    // 账户
                    ground_account.setText(samUserInfo.getString("userName"));
                    ground_tv_balance.setText(samUserInfo.getString("accountFee"));
                    // 套餐
                    String meal = samUserInfo.getString("packageName");
                    String group = samUserInfo.getString("userGroupName");
                    if (Constants.FEE.equals(meal) || Constants.FEE.contains(meal)
                            || "网络运营".equals(group) || meal.contains("试用套餐")) {
                        ll_meal.setVisibility(View.GONE);
                        ll_time.setVisibility(View.GONE);
                        ll_open.setVisibility(View.VISIBLE);
                        String exchange = getActivity().getResources().getString(R.string.meal_open);
                        System.out.println(exchange + "'----");
                        ground_tv_open.setText(Html.fromHtml(exchange));
                        ground_tv_open.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                Intent intent = new Intent(getActivity(), MealsActivity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        ll_meal.setVisibility(View.GONE);
                        ll_time.setVisibility(View.GONE);
                        ll_open.setVisibility(View.GONE);
                        if (meal.indexOf("|") > 0) {
                            meal = meal.substring(0, meal.indexOf("|"));
                        }
                        ground_tv_meal.setText(meal);
                        String next = samUserInfo.getString("nextBillingTime");

                        if (meal.contains("包学期")) {
                            ground_tv_time.setText("本学期末");
                        } else {
                            ground_tv_time.setText(next.substring(0, next.indexOf("T")));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 3) {
                try {
                    share.put("bannerJson", bannerJson.toString());
                    bannerJson = new JSONObject(bannerJson.getString("result"));
                    Log.e("bannerJson", bannerJson + "");
                    String code = bannerJson.getString("code");
                    String msgStr = bannerJson.getString("msg");
                    // 验证码成功匹配
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        bannerArray = new JSONArray(bannerJson.getString("data"));
                        imgs = new ArrayList<String>();
                        // 实例化集合，循环添加要显示的图片
                        for (int i = 0; i < bannerArray.length(); i++) {
                            Map<String, String> map = new HashMap<String, String>();
                            JSONObject banner = bannerArray.getJSONObject(i);
                            String picUrl = banner.getString("picUrl");
                            String serverIp = share.getString("serverIp", "");
                            Log.e("serverIp", serverIp + "");
                            if (picUrl != "" || picUrl != null) {
                                imgs.add(i, "http://" + serverIp + "/" + picUrl);
                                map.put("imageAddr", "http://" + serverIp + "/" + picUrl);
                            }
                        }
                        layout_image.start(getActivity(), imgs, imgs, layout_dot);
                    } else if (code.equals(CacheConstants.TOKEN_FAIL)) {
                        CommonUtil.intoLogin(getActivity(), share, msgStr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 4) {
                try {
                    String banners = share.getString("bannerJson", "").toString();
                    if (banners != null && !banners.equals("")) {
                        bannerJson = new JSONObject(banners);
                        bannerArray = new JSONArray(new JSONObject(bannerJson.getString("result")).getString("data"));
                        imgs = new ArrayList<String>();
                        dataListAd.clear();
                        // 实例化集合，循环添加要显示的图片
                        for (int i = 0; i < bannerArray.length(); i++) {
                            Map<String, String> map = new HashMap<String, String>();
                            JSONObject banner = bannerArray.getJSONObject(i);
                            String picUrl = banner.getString("picUrl");
                            String serverIp = share.getString("serverIp", "");
                            if (picUrl != "" || picUrl != null) {
                                imgs.add(i, "http://" + serverIp + "/" + picUrl);
                                map.put("imageAddr", "http://" + serverIp + "/" + picUrl);
                            }
                            dataListAd.add(map);
                        }
                        layout_image.start(getActivity(), imgs, imgs, layout_dot);
                    }
                } catch (Exception w) {
                    w.printStackTrace();
                }
            } else if (msg.what == 9001) {
                CommonUtil.intoLogin(getActivity(), share, messageStr);
            } else {
                //连接网络后并且是连接wifi,开始网络监测进程
                if (ConnectivityUtils.isWifi(getActivity())) {
                    if (Constants.ISRUN) {
                        refresh();
                    }
                }
//                else {
//                    if (netstate != -1) {
//                        functionData.get(netstate).put("funImage", R.mipmap.btn_key_intenet);
//                        functionData.get(netstate).put("funDes", "一键上网");
//                        flistAdapter.notifyDataSetChanged();
//                    }
//                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Constants.ISRUN = true;
        MobclickAgent.onPageStart(getActivity().getClass().getSimpleName()); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        Constants.ISRUN = false;
        LocationManager ss = null;
        MobclickAgent.onPageEnd(getActivity().getClass().getSimpleName());
    }

    private void createDot(Activity thisActivity, int position,
                           LinearLayout layout_dot) {
        View v = new View(thisActivity);
        int radio = (int) (BaseApplication.ScreenWidth * 0.022);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(radio,
                radio);
        lp.leftMargin = 15;
        if (position == 0)
            v.setBackgroundResource(R.drawable.dot_focused);
        else v.setBackgroundResource(R.drawable.dot_normal);
        v.setLayoutParams(lp);
        layout_dot.addView(v, position);
    }

    //获取用户信息
    private void checkUserInfo(String userName) {
//        if (ConnectivityUtils.isNetworkAvailable(getActivity())) {//有网络，进行自动登录
        MobclickAgent.onEvent(getActivity(), "login");//友盟统计
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("queryUserInfo") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        //信息需要Base64进行加密
        params.put("username", ReturnUtils.encode(userName));  //用户名
        params.put("token", ReturnUtils.encode(share.getString("token", ""))); //Token加密
        abHttpUtil.post(url, params, new CallBackParent(getActivity(), false) {
            @Override
            public void Get_Result(String content) {
                try {
                    JSONObject json = new JSONObject(content);
                    Log.e("content", content + "");
                    String code = json.getString("code");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        json = new JSONObject(json.getString("data"));
                        share.put("samUserInfo", json.getString("samUserInfo") + "");
                        share.put("canModifyPassword", json.getBoolean("canModifyPassword"));
                        share.put("canPayNetFee", json.getBoolean("canPayNetFee"));
                        share.put("menujson", content);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // 重新登录
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    startActivity(intent);
                    CommonUtil.intoLogin(getActivity(), share, "");
                    Log.e("gogogogogogogogo","Playground");

                }
            }

            @Override
            public void Get_Result_faile(String errormsg) {
                super.Get_Result_faile(errormsg);
                // 重新登录
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//                getActivity().finish();
                CommonUtil.intoLogin(getActivity(), share, "");
                Log.e("gogogogogogogogo","Playground");
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                super.onFailure(statusCode, content, error);
                // 重新登录
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//                getActivity().finish();

                CommonUtil.intoLogin(getActivity(), share, "");
                Log.e("gogogogogogogogo","Playground");
            }
        });
//        } else {
//            // 重新登录
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            startActivity(intent);
//            getActivity().finish();
//        }
    }
}
