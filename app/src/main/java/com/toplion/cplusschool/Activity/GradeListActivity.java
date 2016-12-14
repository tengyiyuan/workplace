package com.toplion.cplusschool.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Adapter.GradeListAdapter;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Bean.GradeInfoBean;
import com.toplion.cplusschool.Bean.TermListBean;
import com.toplion.cplusschool.Bean.TotalGradeInfoBean;
import com.toplion.cplusschool.Common.CommonPopupWindow;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialogListview;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wang
 * @date 2016/5/25.
 * @des 成绩查询
 */
public class GradeListActivity extends BaseActivity {
    private LinearLayout tv_title;
    private ImageView iv_grade_return;//返回键
    private TextView tv_select_term;//选择学期
    private ListView lv_gradelist;//成绩列表
    private TextView tv_stu_number;//学号
    private RelativeLayout rl_pass_subject;//通过的科目
    private TextView tv_pass_subject_number;//通过的科目数
    private TextView tv_pass_grade;//获得的学分
    private RelativeLayout rl_nopass_subject;//未通过的科目
    private TextView tv_nopass_subject_number;//未通过的科目数
    private TextView tv_nopass_grade;//未获得的学分
    private List<CommonBean> termList;//学期
    private GradeListAdapter gradeListAdapter;//成绩列表适配器
    private List<GradeInfoBean> gradeList = null;//成绩列表数据
    private AbHttpUtil abHttpUtil;//网络请求工具
    private SharePreferenceUtils share;
    private String termSel = "";//选择的学期
    private String isPassSel = "";//是否通过
    private LinearLayout ll_term;//选择筛选条件
    private TextView tv_select_ispass;//是否通过
    private CommonPopupWindow cPopWindow;//选择框
    private List<CommonBean> plist;//是否通过
    private RelativeLayout rl_nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gradelist);
        init();
        getData();
        setListener();
    }

    //初始化布局
    @Override
    protected void init() {
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        cPopWindow = new CommonPopupWindow(this);
        tv_title = (LinearLayout) findViewById(R.id.tv_title);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tv_title.setVisibility(View.GONE);
            }
        };
        new Handler().postDelayed(runnable, 5000);
        iv_grade_return = (ImageView) findViewById(R.id.iv_grade_return);
        tv_select_term = (TextView) findViewById(R.id.tv_select_term);
        tv_stu_number = (TextView) findViewById(R.id.tv_stu_number);
        rl_pass_subject = (RelativeLayout) findViewById(R.id.rl_pass_subject);
        tv_pass_subject_number = (TextView) findViewById(R.id.tv_pass_subject_number);
        tv_pass_grade = (TextView) findViewById(R.id.tv_pass_grade);
        rl_nopass_subject = (RelativeLayout) findViewById(R.id.rl_nopass_subject);
        tv_nopass_subject_number = (TextView) findViewById(R.id.tv_nopass_subject_number);
        tv_nopass_grade = (TextView) findViewById(R.id.tv_nopass_grade);
        ll_term = (LinearLayout) findViewById(R.id.ll_term);
        tv_select_ispass = (TextView) findViewById(R.id.tv_select_ispass);
        lv_gradelist = (ListView) findViewById(R.id.lv_gradelist);
        gradeList = new ArrayList<GradeInfoBean>();
        gradeListAdapter = new GradeListAdapter(GradeListActivity.this, gradeList);
        lv_gradelist.setAdapter(gradeListAdapter);
        rl_nodata=(RelativeLayout)findViewById(R.id.rl_nodata);
        rl_nodata.setVisibility(View.GONE);

    }

    //加载数据
    @Override
    protected void getData() {
        getTermList();//获取学期
        getGradeList(termSel, isPassSel);//获取成绩
    }

    //获取学期列表
    private void getTermList() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getTermsAboutScore") + Constants.BASEPARAMS + "&stuNo="+share.getString("ROLE_ID","");
        abHttpUtil.get(url, new CallBackParent(GradeListActivity.this, getResources().getString(R.string.loading), "termlistTag") {
            @Override
            public void Get_Result(String result) {
                Log.e("termlist", result + "");
                if (!TextUtils.isEmpty(result)) {
                    TermListBean termListBean = AbJsonUtil.fromJson(result, TermListBean.class);
                    termList = new ArrayList<CommonBean>();
                    termList.add(new CommonBean("", "所有学期"));
                    if (termListBean.getData() != null && termListBean.getData().size() > 0) {

                        for (int i = 0; i < termListBean.getData().size(); i++) {
                            termList.add(new CommonBean(termListBean.getData().get(i).getXNXQDM(), termListBean.getData().get(i).getXNXQMC()));
                        }
                    }
                }
            }
        });
    }

    /**
     * 获取成绩列表
     * @param term   要查询的 学期
     * @param isPass 是否通过（为空获取所有，1 为通过的科目，0为未通过的科目）
     */
    private void getGradeList(String term, String isPass) {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getScoreByInput") + Constants.BASEPARAMS
                + "&stuNo="+share.getString("ROLE_ID","") + "&term=" + ReturnUtils.encode(term) + "&isPass=" + ReturnUtils.encode(isPass);
        abHttpUtil.get(url, new CallBackParent(GradeListActivity.this, getResources().getString(R.string.loading), "graderlistTag") {
            @Override
            public void Get_Result(String result) {
                AbDialogUtil.removeDialog(GradeListActivity.this, "graderlist");
                Log.e("Gradelist", result + "");
                if (!TextUtils.isEmpty(result)) {
                    try {
                        TotalGradeInfoBean tgBean = AbJsonUtil.fromJson(result, TotalGradeInfoBean.class);
                        tv_stu_number.setText(share.getString("username", ""));
                        tv_pass_subject_number.setText(tgBean.getPass_count() + "");
                        tv_pass_grade.setText(tgBean.getPass_score() + "");
                        tv_nopass_subject_number.setText(tgBean.getNopass_count() + "");
                        tv_nopass_grade.setText(tgBean.getNopass_score() + "");
                        if (tgBean.getData() != null && tgBean.getData().size() > 0) {
                            rl_nodata.setVisibility(View.GONE);
                            gradeList = tgBean.getData();
                            gradeListAdapter.setMlist(gradeList);
                            gradeListAdapter.notifyDataSetChanged();
                        }else{
                            rl_nodata.setVisibility(View.VISIBLE);
                            gradeList = new ArrayList<GradeInfoBean>();
                            gradeListAdapter.setMlist(gradeList);
                            gradeListAdapter.notifyDataSetChanged();
                            ToastManager.getInstance().showToast(GradeListActivity.this,"暂无数据");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        rl_nodata.setVisibility(View.VISIBLE);
                        ToastManager.getInstance().showToast(GradeListActivity.this,"数据异常");
                    }
                }
            }
        });
    }

    //设置事件
    @Override
    protected void setListener() {
        //返回键
        iv_grade_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //选择学期
        tv_select_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (termList != null && termList.size() > 0) {
                    final CustomDialogListview dialog_sex = new CustomDialogListview(GradeListActivity.this, "选择学期", termList, tv_select_term.getText().toString());
                    dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_select_term.setText(termList.get(position).getDes());
                            termSel = termList.get(position).getId();
                            getGradeList(termSel, isPassSel);//获取成绩
                            dialog_sex.dismiss();
                        }
                    });
                    dialog_sex.show();
                }
            }
        });
        rl_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        //查看通过的成绩信息
        tv_select_ispass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    plist = new ArrayList<CommonBean>();
                    plist.add(new CommonBean("", "全部"));
                    plist.add(new CommonBean("1", "通过"));
                    plist.add(new CommonBean("0", "未通过"));
                    final CustomDialogListview dialog_sex = new CustomDialogListview(GradeListActivity.this, "是否通过", plist, tv_select_ispass.getText().toString());
                    dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_select_ispass.setText(plist.get(position).getDes());
                            isPassSel = plist.get(position).getId();
                            if (isPassSel.equals("0")) {//未通过
                                rl_nopass_subject.setVisibility(View.VISIBLE);
                                rl_pass_subject.setVisibility(View.GONE);
                            } else if (isPassSel.equals("1")) {
                                rl_nopass_subject.setVisibility(View.GONE);
                                rl_pass_subject.setVisibility(View.VISIBLE);
                            } else {
                                rl_nopass_subject.setVisibility(View.VISIBLE);
                                rl_pass_subject.setVisibility(View.VISIBLE);
                                isPassSel = "";
                            }
                            Log.e("isPassSel", isPassSel + "");
                            getGradeList(termSel, isPassSel);//获取成绩
                            dialog_sex.dismiss();
                        }
                    });
                    dialog_sex.show();
            }
        });
    }
}
