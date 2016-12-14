package com.toplion.cplusschool.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.toplion.cplusschool.Bean.Course;
import com.toplion.cplusschool.Bean.CourseDate;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.dao.TimesDao;
import com.toplion.cplusschool.dao.UserInsideDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 课程表页面
 *
 * @author tengyiyuan
 * @version 1.1.9 2016-06-14
 */
public class WeekListActivity extends BaseActivity {
    // private AbPullToRefreshView mAbPullToRefreshView = null;
    private TextView tv_test_title;
    private TextView about_iv_Title;
    private RelativeLayout noweek;
    private String address = "";
    private TextView oneday, twoday, threeday, fourday, fiveday, sixday, sevenday;
    private ImageView about_iv_return;
    private List<Course> listcourse;
    private TextView weektoday;
    private ImageView leftbtn;
    private ImageView rightbtn;
    private TextView textweek;
    private LinearLayout weekPanel_0;
    private UserInsideDao dao;
    private TimesDao timesDao;
    private int count = 0;
    private int intweek;
    private int year;
    private int maxweek;
    //private String floorno;
    private String roomno = "0";
    private Calendar cal;
    private int bgRes;
    private int foncol;
    private SharePreferenceUtils share;
    LinearLayout weekPanels[] = new LinearLayout[7];
    List courseData[] = new ArrayList[7];
    int itemHeight;
    int marTop, marLeft;
    int style = 0;
    int juese=2;
    //某节课的背景图,用于随机获取
    private int[] bg = {R.mipmap.dashed_gray, R.mipmap.dashed_orange};
    private int[] fontbg = {R.color.black, R.color.white};
    private AbHttpUtil mAbHttpUtil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weeklist);
        init();
        getData();
    }

//    @Override
//    public void onHeaderRefresh(AbPullToRefreshView view) {
//        weekPanel_0.removeAllViews();
//        for (int i = 0; i < courseData.length; i++) {
//            courseData[i].clear();
//        }
//        for (int i = 0; i < weekPanels.length; i++) {
//            weekPanels[i] = (LinearLayout) findViewById(R.id.weekPanel_1 + i);
//            weekPanels[i].removeAllViews();
//        }
//        Loadweekdata();
//    }
//
//    @Override
//    public void onFooterLoad(AbPullToRefreshView view) {
//        mAbPullToRefreshView.onFooterLoadFinish();
//    }

    @Override
    protected void init() {
        super.init();
        RelativeLayout rl_nodata=(RelativeLayout)findViewById(R.id.rl_nodata);
        rl_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        share = new SharePreferenceUtils(this);
        tv_test_title = (TextView) findViewById(R.id.tv_test_title);
        about_iv_Title = (TextView) findViewById(R.id.about_iv_Title);
        style = getIntent().getIntExtra("style", 0);
        if (style == 1) {
            //floorno = getIntent().getStringExtra("foorno");
            roomno = getIntent().getStringExtra("roomno");
            address = getIntent().getStringExtra("address");
            about_iv_Title.setText(address + "的课程表");
        } else {
            about_iv_Title.setText(share.getString("userTrueName", "测试") + "的课程表");
            juese=share.getInt("ROLE_TYPE",2);
        }
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        dao = new UserInsideDao(this);
        timesDao = new TimesDao(this);
        itemHeight = 170;
        marTop = 2;
        marLeft = 2;
        count = share.getInt("weekcount", 0);
        noweek = (RelativeLayout) findViewById(R.id.noweek);
        oneday = (TextView) findViewById(R.id.top_monday);
        twoday = (TextView) findViewById(R.id.top_tuesday);
        threeday = (TextView) findViewById(R.id.top_wendesday);
        fourday = (TextView) findViewById(R.id.top_thursday);
        fiveday = (TextView) findViewById(R.id.top_firday);
        sixday = (TextView) findViewById(R.id.top_saturday);
        sevenday = (TextView) findViewById(R.id.top_sunday);
        weekPanel_0 = (LinearLayout) findViewById(R.id.weekPanel_0);
        textweek = (TextView) findViewById(R.id.textweek);
        leftbtn = (ImageView) findViewById(R.id.leftweek);
        rightbtn = (ImageView) findViewById(R.id.rightweek);
        weektoday = (TextView) findViewById(R.id.weektoday);
        about_iv_return = (ImageView) findViewById(R.id.about_iv_return);
        cal = Calendar.getInstance();
        cal.setTime(new Date());
        intweek = cal.get(Calendar.WEEK_OF_YEAR);
        year = cal.get(Calendar.YEAR);
        maxweek = getDayintweek(cal.get(Calendar.DAY_OF_WEEK));
        getDayintweekColor(cal.get(Calendar.DAY_OF_WEEK));
        textweek.setText(year + "年第" + intweek + "周(本周)" + getDayintweek(cal.get(Calendar.DAY_OF_WEEK)));
//        leftbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (intweek <= 1) {
//                    intweek = 1;
//                    getUpDate(intweek);
//                } else {
//                    intweek--;
//                    getUpDate(intweek);
//                }
//            }
//        });
//        rightbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (intweek >= maxweek) {
//                    intweek = maxweek;
//                    getUpDate(maxweek);
//                } else {
//                    intweek++;
//                    getUpDate(intweek);
//                }
//            }
//        });
        about_iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ToastManager.getInstance().getTvTitle(tv_test_title,"");
    }

    public void getData() {
        AbTask task = AbTask.newInstance();
        //定义异步执行的对象
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {
            @Override
            public void update() {
                super.update();
                if (null != listcourse && listcourse.size() > 0 && count > 0) {
                    List<Course> list1 = new ArrayList<Course>();
                    List<Course> list2 = new ArrayList<Course>();
                    List<Course> list3 = new ArrayList<Course>();
                    List<Course> list4 = new ArrayList<Course>();
                    List<Course> list5 = new ArrayList<Course>();
                    List<Course> list6 = new ArrayList<Course>();
                    List<Course> list7 = new ArrayList<Course>();
                    for (int i = 0; i < listcourse.size(); i++) {
                        Course cour = listcourse.get(i);
                        if (cour.getDays().equals("1")) {
                            list1.add(cour);
                        } else if (cour.getDays().equals("2")) {
                            list2.add(cour);
                        } else if (cour.getDays().equals("3")) {
                            list3.add(cour);
                        } else if (cour.getDays().equals("4")) {
                            list4.add(cour);
                        } else if (cour.getDays().equals("5")) {
                            list5.add(cour);
                        } else if (cour.getDays().equals("6")) {
                            list6.add(cour);
                        } else if (cour.getDays().equals("7")) {
                            list7.add(cour);
                        }
                    }
                    courseData[0] = list1;
                    courseData[1] = list2;
                    courseData[2] = list3;
                    courseData[3] = list4;
                    courseData[4] = list5;
                    courseData[5] = list6;
                    courseData[6] = list7;
                    initPage();
                    AbDialogUtil.removeDialog(WeekListActivity.this);
                } else {
                    AbDialogUtil.removeDialog(WeekListActivity.this);
                    getWeektime();
                }
            }


            @Override
            public void get() {
                super.get();
  //              AbDialogUtil.showProgressDialog(WeekListActivity.this, 0, "正在查询...");
//                dao.startReadableDatabase();
//                if (style == 0) {
//                    String[] selectionArgs = {"0"};
//                    listcourse = dao.queryList("floorno=?", selectionArgs);
//                } else {
//                    String[] selectionArgs = {roomno};
//                    listcourse = dao.queryList("floorno=?", selectionArgs);
//                    // String[] selectionArgs = {floorno, roomno};
//                    //  listcourse = dao.queryList("floorno=? and roomno=?", selectionArgs);
//                }
//                dao.closeDatabase();
            }
        });
        task.execute(item);
    }

    /**
     * 加载页面课程
     */
    private void initPage() {
        for (int i = 1; i < count; i++) {
            TextView tv = new TextView(WeekListActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight);
            lp.weight = 0.5f;
            lp.setMargins(marLeft, marTop, 0, 0);
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(12);
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundResource(R.color.white);
            tv.setText(i + "");
            weekPanel_0.addView(tv);
        }
        for (int i = 0; i < weekPanels.length; i++) {
            if (i == maxweek) {
                bgRes = bg[1];
                foncol = fontbg[1];
            } else {
                bgRes = bg[0];
                foncol = fontbg[0];
            }
            weekPanels[i] = (LinearLayout) findViewById(R.id.weekPanel_1 + i);
            initWeekPanel(weekPanels[i], courseData[i]);
            final int sum = i;
            weekPanels[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (courseData[sum] != null && courseData[sum].size() > 0) {
                        Intent intent = new Intent(WeekListActivity.this, DayListActivity.class);
                        intent.putCharSequenceArrayListExtra("week", (ArrayList) courseData[sum]);
                        intent.putExtra("count", count);
                        if (style == 1) {
                            intent.putExtra("roomno", roomno);
                        }
                        startActivity(intent);
                    }
                }
            });
            weektoday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (courseData[getDayintweek(cal.get(Calendar.DAY_OF_WEEK))].size() > 0) {
                        Intent intent = new Intent(WeekListActivity.this, DayListActivity.class);
                        intent.putCharSequenceArrayListExtra("week", (ArrayList) courseData[getDayintweek(cal.get(Calendar.DAY_OF_WEEK))]);
                        intent.putExtra("count", count);
                        if (style == 1) {
                            intent.putExtra("roomno", roomno);
                        }
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(WeekListActivity.this, "当天暂无课表！");
                    }
                }
            });
        }
    }

    /**
     * 网络加载课程数据
     */
    private void Loadweekdata() {
        AbRequestParams params = new AbRequestParams();
        params.put("stuNo",share.getString("ROLE_ID",""));
        params.put("term", "");
        params.put("schoolCode", Constants.SCHOOL_CODE);
        params.put("role",juese+"");
        String weekurl = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getCurriculumScheduleByTerm");
        //网络加载数据
        mAbHttpUtil.post(weekurl, params, new CallBackParent(WeekListActivity.this, "正在查询") {
            @Override
            public void Get_Result(String content) {
                List<Course> list1 = new ArrayList<Course>();
                List<Course> list2 = new ArrayList<Course>();
                List<Course> list3 = new ArrayList<Course>();
                List<Course> list4 = new ArrayList<Course>();
                List<Course> list5 = new ArrayList<Course>();
                List<Course> list6 = new ArrayList<Course>();
                List<Course> list7 = new ArrayList<Course>();
                try {
                    JSONObject object = new JSONObject(content);
                    JSONArray array = object.getJSONArray("data");
                    if (array.length() == 0) {
                        noweek.setVisibility(View.VISIBLE);
                        weekPanel_0.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = (JSONObject) array.get(i);
                        String days = obj.getInt("SKXQ") + "";
                        if (days.equals("1")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list1.add(cour);
                        } else if (days.equals("2")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list2.add(cour);
                        } else if (days.equals("3")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list3.add(cour);
                        } else if (days.equals("4")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list4.add(cour);
                        } else if (days.equals("5")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list5.add(cour);
                        } else if (days.equals("6")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list6.add(cour);
                        } else if (days.equals("7")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list7.add(cour);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//周一课程

                courseData[0] = list1;

//周二课程

                courseData[1] = list2;

//周三课程

                courseData[2] = list3;
                //周四课程
                courseData[3] = list4;
                //周五课程
                courseData[4] = list5;
                //周六课程
                courseData[5] = list6;
                //周天课程
                courseData[6] = list7;

                initPage();
                dao.startWritableDatabase(true);
                dao.deleteAll();
                dao.insertList(list1);
                dao.insertList(list2);
                dao.insertList(list3);
                dao.insertList(list4);
                dao.insertList(list5);
                dao.insertList(list6);
                dao.insertList(list7);
                dao.closeDatabase();
                AbDialogUtil.removeDialog(WeekListActivity.this);
            }

            @Override
            public void Get_Result_faile(String errormsg) {
                super.Get_Result_faile(errormsg);
                AbDialogUtil.removeDialog(WeekListActivity.this);
                ToastManager.getInstance().showToast(context, errormsg);
                noweek.setVisibility(View.VISIBLE);
                weekPanel_0.setVisibility(View.GONE);

            }
        });
    }

    /**
     * 网络加载房间课程表
     */
    private void Loadweektoroom() {
        AbRequestParams params = new AbRequestParams();
        params.put("classroomNo", roomno);
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String weekurl = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getTimetablesByBuildno");
        //网络加载数据
        mAbHttpUtil.post(weekurl, params, new CallBackParent(WeekListActivity.this, "正在查询") {
            @Override
            public void Get_Result(String content) {
                List<Course> list1 = new ArrayList<Course>();
                List<Course> list2 = new ArrayList<Course>();
                List<Course> list3 = new ArrayList<Course>();
                List<Course> list4 = new ArrayList<Course>();
                List<Course> list5 = new ArrayList<Course>();
                List<Course> list6 = new ArrayList<Course>();
                List<Course> list7 = new ArrayList<Course>();
                try {
                    JSONObject object = new JSONObject(content);
                    JSONArray array = object.getJSONArray("data");
                    if (array.length() == 0) {
                        noweek.setVisibility(View.VISIBLE);
                        weekPanel_0.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = (JSONObject) array.get(i);
                        String days = obj.getInt("SKXQ") + "";
                        if (days.equals("1")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list1.add(cour);
                        } else if (days.equals("2")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list2.add(cour);
                        } else if (days.equals("3")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list3.add(cour);
                        } else if (days.equals("4")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list4.add(cour);
                        } else if (days.equals("5")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list5.add(cour);
                        } else if (days.equals("6")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list6.add(cour);
                        } else if (days.equals("7")) {
                            Course cour = new Course();
                            initjson(cour, days, obj);
                            list7.add(cour);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//周一课程

                courseData[0] = list1;

//周二课程

                courseData[1] = list2;

//周三课程

                courseData[2] = list3;
                //周四课程
                courseData[3] = list4;
                //周五课程
                courseData[4] = list5;
                //周六课程
                courseData[5] = list6;
                //周天课程
                courseData[6] = list7;

                initPage();
                dao.startWritableDatabase(true);
                String[] selectionArgs = {roomno};
                dao.delete("floorno=?", selectionArgs);
                dao.insertList(list1);
                dao.insertList(list2);
                dao.insertList(list3);
                dao.insertList(list4);
                dao.insertList(list5);
                dao.insertList(list6);
                dao.insertList(list7);
                dao.closeDatabase();
                AbDialogUtil.removeDialog(WeekListActivity.this);
            }

            @Override
            public void Get_Result_faile(String errormsg) {
                super.Get_Result_faile(errormsg);
                AbDialogUtil.removeDialog(WeekListActivity.this);
                ToastManager.getInstance().showToast(context, errormsg);
                noweek.setVisibility(View.VISIBLE);
                weekPanel_0.setVisibility(View.GONE);

            }
        });

    }

    private void initjson(Course cour, String days, JSONObject obj) {
        try {
            cour.setDays(days);
            cour.setName(Function.getInstance().getString(obj, "KCM"));
            cour.setRoom(Function.getInstance().getString(obj, "JASH"));
            if (null != address && style == 1) {
                cour.setRoomname(address);
            } else {
                cour.setRoomname(Function.getInstance().getString(obj, "JSMC"));
            }
            cour.setTeach(Function.getInstance().getString(obj, "SKLS"));
            cour.setBid(Function.getInstance().getString(obj, "KBID"));
            cour.setStart(obj.getInt("KSJC"));
            cour.setStep(obj.getInt("JSJC") - cour.getStart() + 1);
            cour.setByone(obj.getString("SKZC"));
            if (null == roomno) {
                roomno = "0";
            }
            cour.setFloorno(roomno);
            cour.setRoomno(Function.getInstance().getString(obj, "JASH"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//    /**
//     * 获取上一周数据加载
//     *
//     * @version 1.1.9 2016-04-15
//     * @author tengyiyuan
//     */
//    private void getUpDate(final int upweek) {
//        mAbHttpUtil.post("http://www.baidu.com", new AbStringHttpResponseListener() {
//            @Override
//            public void onSuccess(int statusCode, String content) {
//                if (upweek == cal.get(Calendar.WEEK_OF_YEAR)) {
//                    textweek.setText(year + "年第" + upweek + "周(本周)" + getDayofweek(cal.get(Calendar.DAY_OF_WEEK)));
//                } else {
//                    textweek.setText(year + "年第" + upweek + "周");
//                }
//                AbDialogUtil.removeDialog(WeekListActivity.this);
//            }
//
//            @Override
//            public void onStart() {
//                AbDialogUtil.showProgressDialog(WeekListActivity.this, 0, "正在查询...");
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, String content, Throwable error) {
//                AbDialogUtil.removeDialog(WeekListActivity.this);
//                AbToastUtil.showToast(WeekListActivity.this, error.getMessage());
//            }
//        });
//    }

//    /**
//     * 获取下一周数据加载
//     *
//     * @version 1.1.9 2016-04-15
//     * @author tengyiyuan
//     */
//    private void getNextDate(final int nextweek) {
//        mAbHttpUtil.post("http://www.baidu.com", new AbStringHttpResponseListener() {
//            @Override
//            public void onSuccess(int statusCode, String content) {
//                if (nextweek == cal.get(Calendar.WEEK_OF_YEAR)) {
//                    textweek.setText(year + "年第" + nextweek + "周(本周)" + getDayofweek(cal.get(Calendar.DAY_OF_WEEK)));
//                } else {
//                    textweek.setText(year + "年第" + nextweek + "周");
//                }
//                AbDialogUtil.removeDialog(WeekListActivity.this);
//            }
//
//            @Override
//            public void onStart() {
//                AbDialogUtil.showProgressDialog(WeekListActivity.this, 0, "正在查询...");
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, String content, Throwable error) {
//                AbDialogUtil.removeDialog(WeekListActivity.this);
//                AbToastUtil.showToast(WeekListActivity.this, error.getMessage());
//            }
//        });
//    }

    /**
     * 绘制课程
     *
     * @param ll   布局
     * @param data 数据
     * @version 1.1.9 2016-04-15
     * @author tengyiyuan
     */
    private void initWeekPanel(LinearLayout ll, List<Course> data) {
        if (ll == null || data == null || data.size() < 1) return;
        Course pre = data.get(0);
        for (int i = 0; i < data.size(); i++) {
            final Course c = data.get(i);
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight * c.getStep() + marTop * (c.getStep() - 1));
            if (i > 0) {
                lp.setMargins(marLeft, (c.getStart() - (pre.getStart() + pre.getStep())) * (itemHeight + marTop) + marTop, 0, 0);
            } else {
                lp.setMargins(marLeft, (c.getStart() - 1) * (itemHeight + marTop) + marTop, 0, 0);
            }
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(12);
            tv.setTextColor(getResources().getColor(foncol));
            tv.setText(c.getName() + "\n" + c.getRoomname());
            tv.setBackgroundResource(bgRes);
            ll.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WeekListActivity.this, DayListActivity.class);
                    intent.putCharSequenceArrayListExtra("week", (ArrayList) courseData[Integer.parseInt(c.getDays()) - 1]);
                    intent.putExtra("count", count);
                    if (style == 1) {
                        intent.putExtra("roomno", roomno);
                    }
                    startActivity(intent);
                }
            });
            pre = c;
        }
    }

    // 获取当前时间所在年的最大周数
    private int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return getWeekOfYear(c.getTime());
    }

    // 获取当前时间所在年的周数
    private int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    // 获取当前周几
    private int getDayintweek(int num) {
        switch (num) {
            case 1:
                return 6;

            case 2:
                return 0;

            case 3:
                return 1;

            case 4:
                return 2;

            case 5:
                return 3;

            case 6:
                return 4;

            case 7:
                return 5;

        }
        return 0;
    }

    /**
     * 头部周几颜色变色
     */
    // 获取当前周几
    private int getDayintweekColor(int num) {
        switch (num) {
            case 1:
                sevenday.setTextColor(Color.rgb(238, 120, 12));
                return 6;
            case 2:
                oneday.setTextColor(Color.rgb(238, 120, 12));
                return 0;
            case 3:
                twoday.setTextColor(Color.rgb(238, 120, 12));
                return 1;
            case 4:
                threeday.setTextColor(Color.rgb(238, 120, 12));
                return 2;
            case 5:
                fourday.setTextColor(Color.rgb(238, 120, 12));
                return 3;
            case 6:
                fiveday.setTextColor(Color.rgb(238, 120, 12));
                return 4;
            case 7:
                sixday.setTextColor(Color.rgb(238, 120, 12));
                return 5;
        }
        return 0;
    }

    /**
     * 网络加载每节课的时间
     */
    private void getWeektime() {
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String weekurl = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getSchoolCalendarByCode");
        mAbHttpUtil.post(weekurl, params, new AbStringHttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

                if (style == 0) {
                    Loadweekdata();
                } else {
                    Loadweektoroom();
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {

            }

            @Override
            public void onSuccess(int statusCode, String content) {
                try {
                    List<CourseDate> datas = new ArrayList<CourseDate>();
                    JSONObject json = new JSONObject(content);
                    String code = json.getString("code");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        JSONArray array = json.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = (JSONObject) array.get(i);
                            CourseDate data = new CourseDate();
                            data.setJssj((Function.getInstance().getString(obj, "JSSJ")));
                            data.setWid((Function.getInstance().getString(obj, "WID")));
                            data.setJcdm((Function.getInstance().getInteger(obj, "JCDM") + ""));
                            data.setBz((Function.getInstance().getString(obj, "BZ")));
                            data.setKssj((Function.getInstance().getString(obj, "KSSJ")));
                            data.setJcmc((Function.getInstance().getString(obj, "JCMC")));
                            data.setJclb((Function.getInstance().getInteger(obj, "JCLB") + ""));
                            datas.add(data);
                        }
                        count = array.length() + 1;
                        share.put("weekcount", count);
                        timesDao.startWritableDatabase(true);
                        timesDao.deleteAll();
                        timesDao.insertList(datas);
                        timesDao.closeDatabase();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
