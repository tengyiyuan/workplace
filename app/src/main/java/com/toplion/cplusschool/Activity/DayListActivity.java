package com.toplion.cplusschool.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
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
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.dao.TimesDao;
import com.toplion.cplusschool.dao.UserInsideDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by toplion on 2016/6/14.
 */
public class DayListActivity extends BaseActivity {
    private String roomno = "";
    private ImageView leftweek;
    private ImageView rightweek;
    private UserInsideDao dao;
    private TextView textweek;
    private LinearLayout weekPanel_0;
    private LinearLayout weekcontent;
    private int itemHeight;
    private int marTop, marLeft;
    private List<Course> weeks;
    private Calendar cal;
    private AbHttpUtil mAbHttpUtil = null;
    private int count;
    private ImageView about_iv_return;
    private TextView daytoweek;
    List courseData[] = new ArrayList[7];
    private int days;
    private List<Course> listcourse;
    private int zhouji;
    private TimesDao timesDao;
    //某节课的背景图,用于随机获取
    private int[] bg = {R.drawable.kb1};
    private List<CourseDate> listtime;
    private String todayweek;
    private TextView about_iv_Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daylist);
        init();
    }

    @Override
    protected void init() {
        super.init();
        about_iv_Title=(TextView)findViewById(R.id.about_iv_Title);
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        dao = new UserInsideDao(this);
        timesDao = new TimesDao(this);
        weeks = (ArrayList) getIntent().getCharSequenceArrayListExtra("week");
        about_iv_Title.setText(getIntent().getStringExtra("title"));
        count = getIntent().getIntExtra("count", 0);
        roomno = getIntent().getStringExtra("roomno");
        textweek = (TextView) findViewById(R.id.textweek);
        itemHeight = 170;
        marTop = 1;
        marLeft = 0;
        daytoweek = (TextView) findViewById(R.id.daytoweek);
        about_iv_return = (ImageView) findViewById(R.id.about_iv_return);
        weekPanel_0 = (LinearLayout) findViewById(R.id.weekPanel_0);
        weekcontent = (LinearLayout) findViewById(R.id.weekcontent);
        rightweek = (ImageView) findViewById(R.id.rightweek);
        leftweek = (ImageView) findViewById(R.id.leftweek);
        cal = Calendar.getInstance();
        cal.setTime(new Date());
        textweek.setText(getDayintweek(Integer.parseInt(weeks.get(0).getDays())));
        todayweek = getDayintweek(cal.get(Calendar.DAY_OF_WEEK) - 1);
        if (textweek.getText().equals(todayweek)) {
            textweek.setTextColor(Color.rgb(238, 120, 12));
            textweek.setText(getDayintweek(Integer.parseInt(weeks.get(0).getDays())));
        } else {
            textweek.setTextColor(Color.BLACK);
            textweek.setText(getDayintweek(Integer.parseInt(weeks.get(0).getDays())));
        }

        if (textweek.getText().equals("星期一")) {
            leftweek.setVisibility(View.INVISIBLE);
        } else if (textweek.getText().equals("星期天")) {
            rightweek.setVisibility(View.INVISIBLE);
        }
        zhouji = Integer.parseInt(weeks.get(0).getDays());
        about_iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        daytoweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rightweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekcontent.removeAllViews();
                weekPanel_0.removeAllViews();
                if (zhouji >= 7) {
                    zhouji = 7;
                } else {
                    zhouji++;
                    leftweek.setVisibility(View.VISIBLE);
                }
                if (zhouji == 7) {
                    rightweek.setVisibility(View.INVISIBLE);
                }
                getWeekData(zhouji);
            }
        });
        leftweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekcontent.removeAllViews();
                weekPanel_0.removeAllViews();
                if (zhouji <= 1) {
                    zhouji = 1;
                } else {
                    zhouji--;
                    rightweek.setVisibility(View.VISIBLE);
                }
                if (zhouji == 1) {
                    leftweek.setVisibility(View.INVISIBLE);
                }
                getWeekData(zhouji);
            }
        });
        /**
         * 获取每节课的时间
         */
        timesDao.startReadableDatabase();
        listtime = timesDao.queryList();
        timesDao.closeDatabase();
        if (listtime.size() > 0) {
            getData();
        } else {
            getWeektime();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        daytoweek.setText("周/日");
    }

    @Override
    protected void getData() {
        super.getData();
        for (int i = 1; i < count; i++) {
            TextView tv = new TextView(DayListActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight);
            lp.weight = 0.5f;
            lp.setMargins(marLeft, marTop, 1, 0);
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(12);
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundResource(R.color.white);
            tv.setText(i + "");
            weekPanel_0.addView(tv);
        }

        for (int i = 1; i < count; i++) {
            TextView tv = new TextView(DayListActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight);
            lp.setMargins(0, marTop, 0, 0);
            tv.setLayoutParams(lp);
            tv.setTextSize(14);
            tv.setPadding(30, 30, 30, 30);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setTextColor(Color.rgb(174, 174, 174));
            for (int j = 0; j < weeks.size(); j++) {
                Course cour = weeks.get(j);
                if (cour.getStart() == i) {
                    String time = "";
                    for (int k = i; k < i + cour.getStep(); k++) {
                        if (k != i) {
                            time = time + "         " + listtime.get(k - 1).getBz() + ":" + listtime.get(k - 1).getKssj() + " - " + listtime.get(k - 1).getJssj() + "\n";
                        } else {
                            time = time + listtime.get(k - 1).getBz() + ":" + listtime.get(k - 1).getKssj() + " - " + listtime.get(k - 1).getJssj() + "\n";
                        }
                    }
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (marTop * cour.getStep()) + itemHeight * cour.getStep());
                    tv.setLayoutParams(lp2);
                    int bgRes = bg[0];
                    tv.setBackgroundResource(bgRes);
                    tv.setText("课程:" + cour.getName() + "      老师:" + cour.getTeach() + "\n时间:" + time + "上课周数:" + getWeeks(cour.getByone()) + " \n地点:(" + cour.getRoomname() + ")");
                    i = i + cour.getStep() - 1;
                }
            }
            weekcontent.addView(tv);
        }
    }

    /**
     * 加载数据库课程表
     */
    public void getWeekData(final int days) {
        AbDialogUtil.showProgressDialog(DayListActivity.this, 0, "正在查询...");
        AbTask task = AbTask.newInstance();
        //定义异步执行的对象
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {
            @Override
            public void update() {
                super.update();
                if (listcourse.size() > 0) {
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
                    weeks.clear();
                    if (courseData[days - 1].size() > 0) {
                        weeks.addAll(courseData[days - 1]);
                        getData();
                    }
                    if (getDayintweek(days).equals(todayweek)) {
                        textweek.setTextColor(Color.rgb(238, 120, 12));
                        textweek.setText(getDayintweek(days));
                    } else {
                        textweek.setTextColor(Color.BLACK);
                        textweek.setText(getDayintweek(days));
                    }
                    AbDialogUtil.removeDialog(DayListActivity.this);
                } else {
                    AbDialogUtil.removeDialog(DayListActivity.this);
                }
            }


            @Override
            public void get() {
                super.get();
                if (listcourse == null || listcourse.size() == 0) {
                    dao.startReadableDatabase();
                    if (null == roomno || roomno.equals("0")) {
                        String[] selectionArgs = {"0"};
                        listcourse = dao.queryList("floorno=?", selectionArgs);
                    } else {
                        String[] selectionArgs = {roomno};
                        listcourse = dao.queryList("floorno=?", selectionArgs);
                    }
                    dao.closeDatabase();
                }
            }
        });
        task.execute(item);
    }

    /**
     * 根据参数获取周几
     *
     * @param num
     * @return
     */
    private String getDayintweek(int num) {
        switch (num) {
            case 1:
                return "星期一";

            case 2:
                return "星期二";

            case 3:
                return "星期三";

            case 4:
                return "星期四";

            case 5:
                return "星期五";

            case 6:
                return "星期六";
            case 7:
                return "星期天";
        }
        return "星期天";
    }

    private void getWeektime() {
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String weekurl = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getSchoolCalendarByCode");
        mAbHttpUtil.post(weekurl, params, new CallBackParent(DayListActivity.this, "正在查询") {
            @Override
            public void Get_Result(String result) {
                try {
                    List<CourseDate> datas = new ArrayList<CourseDate>();
                    JSONObject json = new JSONObject(result);
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
                        timesDao.startWritableDatabase(true);
                        timesDao.deleteAll();
                        timesDao.insertList(datas);
                        timesDao.startReadableDatabase();
                        listtime = timesDao.queryList();
                        timesDao.closeDatabase();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                if (listtime.size() > 0) {
                    getData();
                }
            }
        });

    }

    /**
     * 返回上课周数
     */
    public static String getWeeks(String str) {
        str = str + "0";
        String weeks = "";
        int begin = 0;
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) != '1' && str.charAt(i - 1) == '1') {
                if (begin + 1 == i) {
                    weeks = weeks + i + " ";
                } else {
                    weeks = weeks + ((begin + 1) + "-" + (i)) + " ";
                }
            } else if (str.charAt(i) == '1' && str.charAt(i - 1) != '1') {
                begin = i;
            }
        }
        return weeks;
    }
}
