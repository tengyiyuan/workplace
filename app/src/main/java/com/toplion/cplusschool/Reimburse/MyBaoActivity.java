package com.toplion.cplusschool.Reimburse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Adapter.BaomainAdapter;
import com.toplion.cplusschool.Bean.ParentBao;
import com.toplion.cplusschool.Bean.baoitem;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CalendarUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.ActionItem;
import com.toplion.cplusschool.widget.CustomDialog;
import com.toplion.cplusschool.widget.TitlePopup;

import java.util.ArrayList;

/**
 * 我的预约报修页面
 * au:tengyy
 * data:2016/8/2
 */
public class MyBaoActivity extends BaseActivity implements BaomainAdapter.Callback {
    private ListView listView1;
    private TextView newtitle;
    private ImageView back;
    private int style = 0;
    private ArrayList<baoitem> list;
    private BaomainAdapter baomainAdapter;
    private String state = "0";
    private SharePreferenceUtils share;
    private ImageView addbaoxiao;
    private LinearLayout noresult;
    private TextView all, daiban, quxiao, yuqi, allimg, daibanimg, quxiaoimg, yuqiimg;
    private LinearLayout topyuyue;
    private LinearLayout yuyueline;
    //定义标题栏弹窗按钮
    private TitlePopup titlePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybao);
        style = getIntent().getIntExtra("style", 0);
        init();
        getData();
    }

    /**
     * 网络请求接口数据解析
     * au:tengyy
     * data:2016/8/2
     */
    @Override
    protected void getData() {
        super.getData();
        share = new SharePreferenceUtils(this);
        AbRequestParams params = new AbRequestParams();
        params.put("userid", share.getString("username", ""));
        params.put("state", state);
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getAccountInfoByState") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, "正在加载,请稍后...") {
            @Override
            public void Get_Result(String result) {
                ParentBao listBean = AbJsonUtil.fromJson(result, ParentBao.class);
                list = (ArrayList<baoitem>) listBean.getData();
                if (list.size() == 0) {
                    noresult.setVisibility(View.VISIBLE);
                } else {
                    noresult.setVisibility(View.GONE);
                }
                baomainAdapter.setMlist(list);
                baomainAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 初始化数据
     * au:tengyy
     * data:2016/8/2
     */
    @Override
    protected void init() {
        super.init();
        yuyueline=(LinearLayout)findViewById(R.id.yuyueline);
        topyuyue=(LinearLayout)findViewById(R.id.topyuyue);
        noresult = (LinearLayout) findViewById(R.id.noresult);
        addbaoxiao = (ImageView) findViewById(R.id.addbaoxiao);
        newtitle = (TextView) findViewById(R.id.newtitle);
        all = (TextView) findViewById(R.id.all);
        daiban = (TextView) findViewById(R.id.daiban);
        quxiao = (TextView) findViewById(R.id.quxiao);
        yuqi = (TextView) findViewById(R.id.yuqi);
        allimg = (TextView) findViewById(R.id.allimg);
        daibanimg = (TextView) findViewById(R.id.daibanimg);
        quxiaoimg = (TextView) findViewById(R.id.quxiaoimg);
        yuqiimg = (TextView) findViewById(R.id.yuqiimg);
        back = (ImageView) findViewById(R.id.back);
        listView1 = (ListView) findViewById(R.id.mylist);
        if (style == 0) {
            newtitle.setText("我的预约");
            state = "0";
            topyuyue.setVisibility(View.VISIBLE);
            yuyueline.setVisibility(View.VISIBLE);
        } else {
            newtitle.setText("逾期预约");
            state = "5";
            topyuyue.setVisibility(View.GONE);
            yuyueline.setVisibility(View.GONE);
        }
        //实例化标题栏弹窗
        titlePopup = new TitlePopup(this, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list = new ArrayList<baoitem>();
        baomainAdapter = new BaomainAdapter(this, list, this);
        listView1.setAdapter(baomainAdapter);
        addbaoxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyBaoActivity.this, BaotypeActivity.class);
                startActivity(intent);
                // titlePopup.show(v);
            }
        });
        initMenu();
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "0";
                all.setTextColor(getResources().getColor(R.color.logo_color));
                allimg.setVisibility(View.VISIBLE);

                daiban.setTextColor(getResources().getColor(R.color.black));
                daibanimg.setVisibility(View.INVISIBLE);

                quxiao.setTextColor(getResources().getColor(R.color.black));
                quxiaoimg.setVisibility(View.INVISIBLE);


                yuqi.setTextColor(getResources().getColor(R.color.black));
                yuqiimg.setVisibility(View.INVISIBLE);
                getData();
            }
        });
        daiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "6";
                all.setTextColor(getResources().getColor(R.color.black));
                allimg.setVisibility(View.INVISIBLE);

                daiban.setTextColor(getResources().getColor(R.color.logo_color));
                daibanimg.setVisibility(View.VISIBLE);

                quxiao.setTextColor(getResources().getColor(R.color.black));
                quxiaoimg.setVisibility(View.INVISIBLE);


                yuqi.setTextColor(getResources().getColor(R.color.black));
                yuqiimg.setVisibility(View.INVISIBLE);
                getData();
            }
        });
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "4";
                all.setTextColor(getResources().getColor(R.color.black));
                allimg.setVisibility(View.INVISIBLE);

                daiban.setTextColor(getResources().getColor(R.color.black));
                daibanimg.setVisibility(View.INVISIBLE);

                quxiao.setTextColor(getResources().getColor(R.color.logo_color));
                quxiaoimg.setVisibility(View.VISIBLE);


                yuqi.setTextColor(getResources().getColor(R.color.black));
                yuqiimg.setVisibility(View.INVISIBLE);
                getData();

            }
        });
        yuqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "5";
                all.setTextColor(getResources().getColor(R.color.black));
                allimg.setVisibility(View.INVISIBLE);

                daiban.setTextColor(getResources().getColor(R.color.black));
                daibanimg.setVisibility(View.INVISIBLE);

                quxiao.setTextColor(getResources().getColor(R.color.black));
                quxiaoimg.setVisibility(View.INVISIBLE);


                yuqi.setTextColor(getResources().getColor(R.color.logo_color));
                yuqiimg.setVisibility(View.VISIBLE);
                getData();
            }
        });

//        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
//            @Override
//            public void onItemClick(ActionItem item, int position) {
//               if(position==0){
//                   state = "0";
//                   newtitle.setText("我的预约");
//               }else if(position==1){
//                   state = "6";
//                   newtitle.setText("待办预约");
//               }else if(position==2){
//                   state = "4";
//                   newtitle.setText("取消预约");
//               }else if(position==3){
//                   state = "5";
//                   newtitle.setText("逾期预约");
//               }else if(position==4){
//                   Intent intent = new Intent();
//                   intent.setClass(MyBaoActivity.this, BaotypeActivity.class);
//                   startActivity(intent);
//                   return;
//               }
//                getData();
//            }
//        });
    }


    @Override
    public void click(final View v) {
        TextView tv = (TextView) v;
        if (tv.getText().toString().equals("取消预约")) {
            final CustomDialog dialog = new CustomDialog(MyBaoActivity.this);
            dialog.setlinecolor();
            dialog.setTitle("提示");
            dialog.setContentboolean(true);
            dialog.setDetial("确定取消预约吗?");
            dialog.setLeftText("确定");
            dialog.setRightText("取消");
            dialog.setLeftOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Channeltime(list.get((Integer) v.getTag()).getRIID());
                    dialog.dismiss();
                }
            });
            dialog.setRightOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (tv.getText().toString().equals("重新预约")) {
            Intent intent = new Intent(this, TypeListActivity.class);
            intent.putExtra("style", 1);
            intent.putExtra("infolist", list.get((Integer) v.getTag()).getStandardInfo());
            startActivity(intent);
        }
    }

    public void Channeltime(final String bookid) {
        AbRequestParams params = new AbRequestParams();
        params.put("bookid", bookid);
        params.put("schoolCode", "sdjzu");
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("cancelAccountById") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, false) {
            @Override
            public void Get_Result(String result) {
                if(!TextUtils.isEmpty(share.getString(bookid, ""))){
                    CalendarUtils.deleteCalendar(MyBaoActivity.this,share.getString(bookid, ""));
                }
                getData();
            }
        });
    }
//    /**
//     * 初始化数据
//     */
//    private void initData(){
//        //给标题栏弹窗添加子类
//        titlePopup.addAction(new ActionItem(this, "全部预约", R.mipmap.xuanze_bumenx));
//        titlePopup.addAction(new ActionItem(this, "待办预约", R.mipmap.xuanze_bumenx));
//        titlePopup.addAction(new ActionItem(this, "取消预约", R.mipmap.xuanze_bumenx));
//        titlePopup.addAction(new ActionItem(this, "逾期预约",  R.mipmap.xuanze_bumenx));
//        titlePopup.addAction(new ActionItem(this, "新建预约",  R.mipmap.xuanze_bumenx));
//    }

    /**
     * 初始化点击菜单选项
     */
    private void initMenu() {
        if (state.equals("0")) {
            all.setTextColor(getResources().getColor(R.color.logo_color));
            allimg.setVisibility(View.VISIBLE);
        } else if (state.equals("5")) {
            yuqi.setTextColor(getResources().getColor(R.color.logo_color));
            yuqiimg.setVisibility(View.VISIBLE);
        }
    }
}
