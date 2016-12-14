package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.ab.http.AbHttpUtil;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Bean.ClassRoomBean;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Bean.CourseDate;
import com.toplion.cplusschool.Bean.EmptyClassroomListBean;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.CommonPopupWindow;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.dao.TimesDao;
import com.toplion.cplusschool.widget.CustomDialogListview;
import com.toplion.cplusschool.widget.MyHorizontalScrollView;
import com.toplion.cplusschool.widget.MyScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wangshegnbo
 * @version 1.1.3
 * @des 空教室查询
 * @date 2016-4-20 14:50:20
 * @updatetime 2016-6-13 调试空教室接口
 */
public class EmptyClassRoomActivity extends BaseActivity implements MyScrollView.ScrollViewListener {
    private LinearLayout ll_row;//行
    private LinearLayout ll_column;//列

    private MyHorizontalScrollView mhsv_table;
    private MyHorizontalScrollView hsl_row;

    private MyScrollView sl_column;
    private MyScrollView msv_table;
    private TableLayout tl_table;
    private int itemWidth;//宽度i
    private int itemHeight;//高度
    private int row = 13;//行
    private int column = 12;//列数

    private ImageView iv_classroom_return;//返回键
    private TextView tv_classroom_title;//标题
    private ImageView leftdate;//减日期
    private ImageView rightdate;//加日期
    private TextView textdate;//日期显示
    private LinearLayout ll_floor_number;//选择楼层
    private TextView tv_floor_number;//选择楼层
    private FrameLayout fl_floor_tishi;
    private Calendar cld;//日期
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
    private Date nowDate;//当前时间
    private AbHttpUtil abHttpUtil;//网络请求工具
    private List<CommonBean> mlist;//楼层
    private String fId = null;//选择的教学楼id
    private String fName = null;//教学楼名称
    private String fTotalNumber = null;//楼层总数
    private String fNumber = "1";//当前选择的楼层
    private RelativeLayout rl_nodata;//没有数据显示的界面
    private RelativeLayout ll_havedata;//有数据显示的界面
    private CommonPopupWindow cPWindow;
    private String requesttime = "";//选择当前的时间
    private List<CourseDate> listtime;//节次列表
    private TimesDao timesDao;
    private List<ClassRoomBean> allClassRoomByfloor;//当前楼层所有房间列表
    private List<ClassRoomBean> emptyClassRoomList;//当前楼层被占用的教室列表
    private Map<String, TextView> tvmaps = new HashMap<String, TextView>();//保存绘制的表格

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emptyclassroom);
        init();
    }


    /**
     * 加载布局
     */
    @Override
    protected void init() {
        timesDao = new TimesDao(this);
        fId = getIntent().getExtras().getString("fid");
        fName = getIntent().getExtras().getString("fname");
        fTotalNumber = getIntent().getStringExtra("fnumber");
        requesttime = getIntent().getStringExtra("requesttime");
        itemWidth = this.getResources().getDimensionPixelSize(R.dimen.weekItemWidth);
        itemHeight = this.getResources().getDimensionPixelSize(R.dimen.weekItemHeight);
        iv_classroom_return = (ImageView) findViewById(R.id.iv_classroom_return);
        tv_classroom_title = (TextView) findViewById(R.id.tv_classroom_title);
        fl_floor_tishi = (FrameLayout) findViewById(R.id.fl_floor_tishi);
        tv_classroom_title.setText(fName + getResources().getString(R.string.empty_class_room));
        leftdate = (ImageView) findViewById(R.id.leftdate);
        rightdate = (ImageView) findViewById(R.id.rightdate);
        textdate = (TextView) findViewById(R.id.textdate);
        abHttpUtil = AbHttpUtil.getInstance(this);
        cPWindow = new CommonPopupWindow(this);
        cld = Calendar.getInstance();
        cld.setTime(new Date());
        nowDate = cld.getTime();
        try {
            Log.e("time", new Date(Long.parseLong(requesttime)) + "");
            cld.setTime(new Date(Long.parseLong(requesttime)));
        } catch (Exception e) {
            e.printStackTrace();
            cld.setTime(new Date());
        }
        if (sdf.format(cld.getTime()).equals(sdf.format(nowDate))) {
            leftdate.setVisibility(View.INVISIBLE);
        } else {
            leftdate.setVisibility(View.VISIBLE);
        }
        requesttime = cld.getTimeInMillis() + "";
        textdate.setText(sdf.format(cld.getTime()));
        tv_floor_number = (TextView) findViewById(R.id.tv_floor_number);
        ll_floor_number = (LinearLayout) findViewById(R.id.ll_floor_number);

        mhsv_table = (MyHorizontalScrollView) findViewById(R.id.mhsv_table);
        hsl_row = (MyHorizontalScrollView) findViewById(R.id.hsl_row);
        mhsv_table.setHorScrollView(hsl_row);
        hsl_row.setHorScrollView(mhsv_table);

        msv_table = (MyScrollView) findViewById(R.id.msv_table);
        sl_column = (MyScrollView) findViewById(R.id.sl_column);
        msv_table.setScrollViewListener(this);
        sl_column.setScrollViewListener(this);
        tl_table = (TableLayout) findViewById(R.id.tl_table);
        tl_table.setStretchAllColumns(true);
        ll_row = (LinearLayout) findViewById(R.id.ll_row);
        ll_column = (LinearLayout) findViewById(R.id.ll_column);

        rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);
        rl_nodata.setVisibility(View.GONE);
        ll_havedata = (RelativeLayout) findViewById(R.id.ll_havedata);
        setListener();
        /**
         * 获取每节课的时间
         */
        timesDao.startReadableDatabase();
        listtime = timesDao.queryList();
        timesDao.closeDatabase();
        if (listtime.size() > 0) {
            getData();
        } else {
            getJieCi();
        }
        RelativeLayout rl_nodata=(RelativeLayout)findViewById(R.id.rl_nodata);
        rl_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listtime.size() > 0) {
                    getData();
                } else {
                    getJieCi();
                }
            }
        });
    }

    //点击事件
    @Override
    protected void setListener() {
        super.setListener();
        //左减时间
        leftdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tl_table.removeAllViews();
                cld.add(Calendar.DAY_OF_MONTH, -1);
                textdate.setText(sdf.format(cld.getTime()));
                Date leftDate = cld.getTime();
                requesttime = cld.getTimeInMillis() + "";
                if (sdf.format(leftDate).equals(sdf.format(nowDate))) {
                    leftdate.setVisibility(View.INVISIBLE);
                } else {
                    leftdate.setVisibility(View.VISIBLE);
                }
                getData();
            }
        });
        //右加时间
        rightdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tl_table.removeAllViews();
                cld.add(Calendar.DAY_OF_MONTH, 1);
                textdate.setText(sdf.format(cld.getTime()));
                Date rightDate = cld.getTime();
                requesttime = cld.getTimeInMillis() + "";
                if (rightDate.after(nowDate)) {
                    leftdate.setVisibility(View.VISIBLE);
                }
                getData();
            }
        });
        //返回键
        iv_classroom_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //选择楼层
        ll_floor_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFloor();
            }
        });
    }

    /**
     * 加载数据
     */
    @Override
    protected void getData() {
        AbTask task = AbTask.newInstance();
        AbTaskItem taskItem = new AbTaskItem();
        taskItem.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
                //楼层数据
                mlist = new ArrayList<CommonBean>();
                if (!TextUtils.isEmpty(fTotalNumber)) {
                    for (int i = 1; i <= Integer.parseInt(fTotalNumber); i++) {
                        CommonBean commonBean = new CommonBean();
                        commonBean.setId(i + "");
                        commonBean.setDes(i + "层");
                        mlist.add(commonBean);
                    }
                    CommonBean commonBeanOther = new CommonBean();
                    commonBeanOther.setId("-99");
                    commonBeanOther.setDes("其他");
                    mlist.add(commonBeanOther);
                    ll_floor_number.setVisibility(View.VISIBLE);
                } else {
                    ll_floor_number.setVisibility(View.GONE);
                }
            }

            @Override
            public void update() {
                super.update();
                column = listtime.size();
                getClassRooms(fId, requesttime, fNumber);
            }
        });
        task.execute(taskItem);
    }

    //获取该学校一天总共几节课
    private void getJieCi() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getSchoolCalendarByCode") + Constants.BASEPARAMS;
        abHttpUtil.get(url, new CallBackParent(EmptyClassRoomActivity.this, getResources().getString(R.string.loading), "EmptyClassRoomActivity") {
            @Override
            public void Get_Result(String result) {
                Log.e("jcresult==", result);
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
                super.onFinish();
                if (listtime.size() > 0) {
                    getData();
                }
            }
        });
    }


    /**
     * 得到房间信息
     *
     * @param floorId 教学楼号
     * @param timeStr 时间戳
     */
    private void getClassRooms(String floorId, String timeStr, String fnum) {
        if (!TextUtils.isEmpty(timeStr)) {
            timeStr = timeStr.substring(0, 10);
        }
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getEmployClassroom")
                + Constants.BASEPARAMS + "&buildNo=" + floorId + "&requesttime=" + timeStr + "&floor=" + fnum;
        Log.e("url", url + "");
        abHttpUtil.get(url, new CallBackParent(EmptyClassRoomActivity.this, getResources().getString(R.string.loading), "EmptyClassRoomActivity") {
            @Override
            public void Get_Result(String result) {
                EmptyClassroomListBean listBean = AbJsonUtil.fromJson(result, EmptyClassroomListBean.class);
                if (listBean.getAllClassroom() != null && listBean.getAllClassroom().size() > 0) {
                    allClassRoomByfloor = listBean.getAllClassroom();
                } else {
                    allClassRoomByfloor = null;
                }
                if (listBean.getEmptyClassroom() != null && listBean.getAllClassroom().size() > 0) {
                    emptyClassRoomList = listBean.getEmptyClassroom();
                } else {
                    emptyClassRoomList = null;
                }
                if (listtime != null && listtime.size() > 0 && allClassRoomByfloor != null && allClassRoomByfloor.size() > 0) {
                    ll_havedata.setVisibility(View.VISIBLE);
                    fl_floor_tishi.setVisibility(View.VISIBLE);
                    rl_nodata.setVisibility(View.GONE);
                    if (ll_column.getChildCount() <= 0) {
                        setColumn(ll_column);
                    }
                    setRow(ll_row);
                } else {
                    ll_havedata.setVisibility(View.GONE);
                    rl_nodata.setVisibility(View.VISIBLE);
                    fl_floor_tishi.setVisibility(View.GONE);
                }
            }
            @Override
            public void Get_Result_faile(String errormsg) {
                ll_havedata.setVisibility(View.GONE);
                rl_nodata.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 绘制行
     *
     * @param llRow
     */
    private void setRow(LinearLayout llRow) {
        row = allClassRoomByfloor.size();
        llRow.removeAllViews();
        for (int i = 0; i < row; i++) {
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemHeight, itemHeight);
            lp.weight = 1;
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(13);
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setText(allClassRoomByfloor.get(i).getJASH());
            tv.setBackgroundResource(R.drawable.bg_table_yes);
            llRow.addView(tv);
        }
        //动态添加表格
        setTable(tl_table);
    }

    /**
     * 绘制列
     *
     * @param llColumn
     */
    private void setColumn(LinearLayout llColumn) {
        for (int i = 0; i < column; i++) {
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemWidth, itemHeight);
            lp.weight = 1;
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(13);
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setText(listtime.get(i).getJcdm());
            tv.setBackgroundResource(R.drawable.bg_table_yes);
            llColumn.addView(tv);
        }
    }


    /**
     * 绘制表格
     *
     * @param table 副布局
     */
    private void setTable(TableLayout table) {
        table.removeAllViews();
        //先绘制出表格
        for (int i = 0; i < column; i++) {
            TableRow tablerow = new TableRow(this);
            for (int j = 0; j < row; j++) {
                TextView textView = new TextView(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(itemHeight, itemHeight);
                lp.weight = 1;
                textView.setLayoutParams(lp);
                textView.setText("空闲");
                textView.setTextColor(getResources().getColor(R.color.logo_color));
                textView.setBackgroundResource(R.drawable.bg_table_yes);
                textView.setTextSize(13);
                textView.setGravity(Gravity.CENTER);
                final String roomNum = allClassRoomByfloor.get(j).getJASH();
                final String roomAddress = allClassRoomByfloor.get(j).getJSMC();
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(EmptyClassRoomActivity.this, WeekListActivity.class);
                        intent.putExtra("style", 1);
                        intent.putExtra("roomno", roomNum + "");
                        intent.putExtra("address", roomAddress + "");
                        startActivity(intent);
                    }
                });
                tablerow.addView(textView);
                tvmaps.put(listtime.get(i).getJcdm() + allClassRoomByfloor.get(j).getJASH(), textView);
            }
            table.addView(tablerow, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        }
        if (emptyClassRoomList != null && emptyClassRoomList.size() > 0) {
            //循环遍历所有有课教室，重新设置界面显示状态
            for (int k = 0; k < emptyClassRoomList.size(); k++) {
                TextView tv = tvmaps.get(emptyClassRoomList.get(k).getJC() + emptyClassRoomList.get(k).getJASH());
                tv.setText("占用");
                tv.setTextColor(getResources().getColor(R.color.gray666));
            }
        }
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (scrollView == msv_table) {
            sl_column.scrollTo(x, y);
        } else if (scrollView == sl_column) {
            msv_table.scrollTo(x, y);
        }
    }

    //设置楼层
    private void setFloor() {
        final CustomDialogListview dialog_sex = new CustomDialogListview(this, "楼层", mlist, tv_floor_number.getText().toString());
        dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_floor_number.setText(mlist.get(position).getDes());
                fNumber = mlist.get(position).getId();
                getClassRooms(fId, requesttime, fNumber);
                dialog_sex.dismiss();
            }
        });
        dialog_sex.show();
    }
}
