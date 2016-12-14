package com.toplion.cplusschool.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.global.AbActivityManager;
import com.ab.http.AbHttpUtil;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Bean.SchoolBean;
import com.toplion.cplusschool.Bean.SchoolListBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.dao.SchoolsDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 学校选择界面，主要显示学校信息
 * 学生登录后首先选择学校信息
 * 然后跳转到登录界面
 *
 * @author liyb
 * @version 1.1.1
 */
public class MainActivity extends BaseActivity {

    private ListView login_school_list;                  // 学校ListView
    private EditText login_et_input;            // 输入框
    private Button login_btn_next;              // 下一步
    private RelativeLayout login_input;//
    private RelativeLayout no_data;
    private ImageView iv_dis;
    // 表中必须有一个名称是 _id的字段
    private String[] from = {"_id", "name", "serverIpAddress", "wifiName", "schoolCode"};  // 指定的是字段
    private int[] to = {R.id.list_school_tv_id, R.id.list_school_tv_content, R.id.list_school_tv_ip, R.id.list_school_tv_wifi, R.id.list_school_tv_schoolCode}; // 指定的id
    private SharePreferenceUtils share;
    private AbHttpUtil abHttpUtil;
    private List<SchoolBean> slist;
    private SchoolListAdapter schoolListAdapter;
    private SchoolsDao schoolsDao;
    private String serverIp;                    // Ip地址
    private String wifiName;                    // WIFI
    private String school_code = "";            // 学校编码
    private String sch_name;                    // 学校名称
    private String sch_id;                      // 学校Id
    private boolean isChange = true;            // 值是否是输入
    private String isp = null;                   //cm:移动  lt:联通

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void init() {
        super.init();
        Constants.BASE_URL = "http://111.14.210.46:12100/index.php";
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        schoolsDao = new SchoolsDao(this);
        // 获取屏幕高/宽
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        share.put("width", screenWidth);
        share.put("height", screenHeight);
        login_school_list = (ListView) findViewById(R.id.login_school_list);
        login_et_input = (EditText) findViewById(R.id.login_et_input);
        login_btn_next = (Button) findViewById(R.id.login_btn_next);
        login_input = (RelativeLayout) findViewById(R.id.login_input);
        iv_dis = (ImageView) findViewById(R.id.iv_dis);
        no_data = (RelativeLayout) findViewById(R.id.rl_nodata);
        no_data.setVisibility(View.GONE);
        login_btn_next.setEnabled(false);
        login_btn_next.setBackgroundResource(R.mipmap.btn_gray);
        slist = new ArrayList<SchoolBean>();
        schoolListAdapter = new SchoolListAdapter(MainActivity.this, slist);
        login_school_list.setAdapter(schoolListAdapter);
        setListener();
        getSchoolData();
    }

    private void getSchoolData() {
        super.getData();
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getSchoolInfo") + "&schVerNum=" + ReturnUtils.encode("0");
        abHttpUtil.post(url, new CallBackParent(this, getResources().getString(R.string.loading), "getSchoolInfo") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    SchoolListBean schoolListBean = AbJsonUtil.fromJson(data, SchoolListBean.class);
                    slist = schoolListBean.getSchoolInfo();
                    schoolListAdapter.setMlist(slist);
                    schoolListAdapter.notifyDataSetChanged();
                    schoolsDao.startWritableDatabase(true);
                    schoolsDao.deleteAll();
                    schoolsDao.insertList(slist);
                    schoolsDao.closeDatabase();
                    login_input.setVisibility(View.VISIBLE);
                    no_data.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(MainActivity.this, Constants.NETWORK_ERROR);
                    login_input.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                AbDialogUtil.removeDialog(context,"getSchoolInfo");
                getLTSchoolData();
            }
        });
    }

    //请求联通地址
    private void getLTSchoolData() {
        isp = "lt";
        String url = Constants.BASE_URLB + "?rid=" + ReturnUtils.encode("getSchoolInfo") + "&schVerNum=" + ReturnUtils.encode("0") + "&isp=" + isp;
        abHttpUtil.post(url, new CallBackParent(this, getResources().getString(R.string.loading), "getSchoolInfo") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    SchoolListBean schoolListBean = AbJsonUtil.fromJson(data, SchoolListBean.class);
                    slist = schoolListBean.getSchoolInfo();
                    schoolListAdapter.setMlist(slist);
                    schoolListAdapter.notifyDataSetChanged();
                    schoolsDao.startWritableDatabase(true);
                    schoolsDao.deleteAll();
                    schoolsDao.insertList(slist);
                    schoolsDao.closeDatabase();
                    login_input.setVisibility(View.VISIBLE);
                    no_data.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(MainActivity.this, Constants.NETWORK_ERROR);
                    login_input.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                AbDialogUtil.removeDialog(context,"getSchoolInfo");
                login_input.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
            }
        });
    }


    //关键字搜索

    private void searchr(final String searcheContet) {
        AbTask abTask = AbTask.newInstance();
        AbTaskItem abTaskItem = new AbTaskItem();
        abTaskItem.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
                schoolsDao.startReadableDatabase();
                //输入关键字 从缓存中查找
                String[] selectionArgs = {"%" + searcheContet + "%"};
                slist = schoolsDao.queryList("schoolName like ?", selectionArgs);
                schoolsDao.closeDatabase();
            }

            @Override
            public void update() {
                super.update();
                if (slist.size() > 0) {
                    schoolListAdapter.setMlist(slist);
                    schoolListAdapter.notifyDataSetChanged();
                }
            }
        });
        abTask.execute(abTaskItem);
    }

    @Override
    protected void setListener() {
        super.setListener();
        iv_dis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.BASE_URL = "http://111.14.210.46:12100/index.php";
                getSchoolData();
            }
        });
        login_school_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sch_id = slist.get(position).getSchoolId();
                sch_name = slist.get(position).getSchoolName();
                // 获取Ip信息
                if (!TextUtils.isEmpty(isp) && isp.equals("lt")) {
                    serverIp = slist.get(position).getLtServerIpAddress();
                } else {
                    serverIp = slist.get(position).getServerIpAddress();
                }
                if(TextUtils.isEmpty(serverIp)){
                    serverIp = slist.get(position).getServerIpAddress();
                }
                // 获取WIFI信息
                wifiName = slist.get(position).getWifiName();
                // 获取学校编码
                school_code = slist.get(position).getSchoolCode();

                if (!TextUtils.isEmpty(sch_name)) {
                    isChange = false;
                    login_btn_next.setEnabled(true);
                    login_btn_next.setBackgroundResource(R.mipmap.btn_orange);
                }
                login_et_input.setText(sch_name); // 为输入框赋值
            }
        });
        login_btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断选中的学校名称和输入的文本框内容是否一致
                if (sch_name.equals(login_et_input.getText().toString().trim())) {
                    share.put("wifiName", wifiName);
//					serverIp = Constants.URL;
                    share.put("serverIp", serverIp);
                    share.put("schoolCode", school_code);
                    share.put("schoolName",sch_name);
                    Constants.BASE_URL = "http://" + serverIp + "/index.php";  //默认请求路径
                    Constants.SCHOOL_CODE = school_code;
                    share.put("BASE_URL", Constants.BASE_URL);
                    // 跳转到登录界面
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("sch_id", sch_id);
                    intent.putExtra("sch_name", sch_name);
                    // 传值到登录ActivityForResult
                    startActivityForResult(intent, 1);

                } else {
                    ToastManager.getInstance().showToast(getBaseContext(), "请选择完整的学校名称");
                }
            }
        });
        login_et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isChange) {
                    searchr(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                isChange = true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                // 来自请求状态码1的返回值，作相应业务处理
                Intent intent = new Intent(MainActivity.this, MainTabActivity.class);
                intent.putExtra("islogo","true");
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    private class SchoolListAdapter extends BaseAdapter {
        private Context mcontext;
        private List<SchoolBean> mlist;

        public void setMlist(List<SchoolBean> mlist) {
            this.mlist = mlist;
        }

        public SchoolListAdapter(Context context, List<SchoolBean> list) {
            this.mcontext = context;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mcontext, R.layout.list_school_item, null);
                viewHolder.tvSchoolName = (TextView) convertView.findViewById(R.id.list_school_tv_content);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvSchoolName.setText(mlist.get(position).getSchoolName());
            return convertView;
        }

        class ViewHolder {
            private TextView tvSchoolName;
        }
    }

    long waitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {  //返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - waitTime > 2000) { //如果两次按键时间间隔大于2秒，则不退出
                ToastManager.getInstance().showToast(this, "再按一次退出程序");
                waitTime = secondTime;//更新firstTime
                return true;
            } else {  //两次按键小于2秒时，退出应用
                AbActivityManager.getInstance().finishAllActivity(null);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
