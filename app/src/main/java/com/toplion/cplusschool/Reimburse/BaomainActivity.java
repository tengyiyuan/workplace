package com.toplion.cplusschool.Reimburse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Adapter.BaomainAdapter;
import com.toplion.cplusschool.Bean.ParentBao;
import com.toplion.cplusschool.Bean.baoitem;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CalendarUtils;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialog;
import com.toplion.cplusschool.widget.MyListViewFill;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 教师费用报销主页
 * data：2016/8/1
 *
 * @author tengyy
 */
public class BaomainActivity extends BaseActivity implements BaomainAdapter.Callback {
    private MyListViewFill mylist;
    private ArrayList<baoitem> list;
    private BaomainAdapter baomainAdapter;
    private LinearLayout yuyuebaoxiao, myyuyue, yuqiyuyue;
    private ScrollView myscrow;
    private ImageView back;
    private TextView gonggao;
    private AbHttpUtil abHttpUtil;
    private LinearLayout noresult;
    private SharePreferenceUtils share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baomain);
        init();
        getData();
        getOtherData();
    }

    @Override
    protected void init() {
        super.init();
        share = new SharePreferenceUtils(this);
        noresult=(LinearLayout)findViewById(R.id.noresult);
        abHttpUtil = AbHttpUtil.getInstance(this);
        gonggao = (TextView) findViewById(R.id.gonggao);
        back = (ImageView) findViewById(R.id.back);
        myscrow = (ScrollView) findViewById(R.id.myscrow);
        yuyuebaoxiao = (LinearLayout) findViewById(R.id.yuyuebaoxiao);
        myyuyue = (LinearLayout) findViewById(R.id.myyuyue);
        yuqiyuyue = (LinearLayout) findViewById(R.id.yuqiyuyue);
        mylist = (MyListViewFill) findViewById(R.id.mylist);
        list = new ArrayList<baoitem>();
        baomainAdapter = new BaomainAdapter(this, list,this);
        mylist.setAdapter(baomainAdapter);
        yuyuebaoxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BaomainActivity.this, BaotypeActivity.class);
                startActivity(intent);
            }
        });
        myyuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BaomainActivity.this, MyBaoActivity.class);
                startActivity(intent);
            }
        });
        yuqiyuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("style", 1);
                intent.setClass(BaomainActivity.this, MyBaoActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void getData() {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("showNoticeInfo") +Constants.BASEPARAMS;
        abHttpUtil.get(url,params, new CallBackParent(this, false) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject obj = new JSONObject(Function.getInstance().getString(object, "data"));
                    String content = Function.getInstance().getString(obj, "NICONTENT");
                    gonggao.setText(content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getOtherData();
    }

    public void getOtherData() {
        AbRequestParams params = new AbRequestParams();
        params.put("userid", share.getString("username",""));
        params.put("state", "6");
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getAccountInfoByState")+Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, "正在加载,请稍后...") {
            @Override
            public void Get_Result(String result) {
                ParentBao listBean = AbJsonUtil.fromJson(result, ParentBao.class);
                list = (ArrayList<baoitem>) listBean.getData();
                if(list.size()==0){
                    noresult.setVisibility(View.VISIBLE);
                }else{
                    noresult.setVisibility(View.GONE);
                }
                baomainAdapter.setMlist(list);
                baomainAdapter.notifyDataSetChanged();
            }
        });
    }
    public void Channeltime(final String bookid) {
        AbRequestParams params = new AbRequestParams();
        params.put("bookid", bookid);
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("cancelAccountById")+Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, false) {
            @Override
            public void Get_Result(String result) {
                if(!TextUtils.isEmpty(share.getString(bookid, ""))){
                    CalendarUtils.deleteCalendar(BaomainActivity.this,share.getString(bookid, ""));
                    share.put(bookid, "");
                }
                getOtherData();
            }
        });
    }

    @Override
    public void click(final View v) {
        final CustomDialog dialog = new CustomDialog(
                BaomainActivity.this);
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
    }
}
