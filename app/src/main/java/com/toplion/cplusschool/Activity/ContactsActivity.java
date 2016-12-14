package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Adapter.ContactsListAdapter;
import com.toplion.cplusschool.Bean.BanJiBean;
import com.toplion.cplusschool.Bean.DataBean;
import com.toplion.cplusschool.Bean.JobBean;
import com.toplion.cplusschool.Bean.MajorBean;
import com.toplion.cplusschool.Bean.NJBean;
import com.toplion.cplusschool.Bean.PersonBean;
import com.toplion.cplusschool.Bean.SeniorBean;
import com.toplion.cplusschool.Bean.SeniorListBean;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.TeacherContacts.ContactsTeaDetailActivity;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshengbo
 * on 2016/8/15.
 *
 * @des通讯录
 */
public class ContactsActivity extends BaseActivity {
    private ImageView iv_phone_books_return;//返回
    private TextView tv_phone_books_search;//搜索框
    private ListView lv_contants_grade;//年级
    private ListView lv_contacts_professional;//专业
    private ListView lv_contacts_class;//班级
    private ContactsListAdapter gAdapter;//适配器
    private ContactsListAdapter pAdapter;//适配器
    private ContactsListAdapter cAdapter;//适配器
    private List<NJBean> nlist;//年级
    private List<MajorBean> mlist;//专业
    private List<BanJiBean> blist;//班级


    private List<String> glist;//年级列表
    private List<String> plist;//专业列表
    private List<String> clist;//班级列表
    private AbHttpUtil abHttpUtil;
    private SharePreferenceUtils share;

    private TextView tv_one;//
    private TextView tv_two;
    private TextView tv_three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        share = new SharePreferenceUtils(this);
        int role = share.getInt("ROLE_TYPE", 2);
        if (role == 2) {
            Intent intent = new Intent(ContactsActivity.this, ContactsDetailListActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.contacts);
            init();
        }
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        gAdapter = new ContactsListAdapter(this);
        pAdapter = new ContactsListAdapter(this);
        cAdapter = new ContactsListAdapter(this);
        iv_phone_books_return = (ImageView) findViewById(R.id.iv_phone_books_return);
        tv_phone_books_search = (TextView) findViewById(R.id.tv_phone_books_search);
        lv_contants_grade = (ListView) findViewById(R.id.lv_contants_grade);
        lv_contacts_professional = (ListView) findViewById(R.id.lv_contacts_professional);
        lv_contacts_class = (ListView) findViewById(R.id.lv_contacts_class);
        tv_one = (TextView) findViewById(R.id.tv_one);
        tv_two = (TextView) findViewById(R.id.tv_two);
        tv_three = (TextView) findViewById(R.id.tv_three);
        tv_one.setText("年级");
        tv_two.setText("专业");
        tv_three.setText("班级");
        getData();
        setListener();
    }

    //获取年级信息
    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getClassInfoByTeachNo") + Constants.BASEPARAMS + "&teacherNo="
                + share.getString("ROLE_ID", "0");
        abHttpUtil.get(url, new CallBackParent(this, getResources().getString(R.string.loading), "getClassInfoByTeachNo") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    DataBean dataBean = AbJsonUtil.fromJson(jsonObject.getString("data"), DataBean.class);
                    if (dataBean != null) {
                        if (dataBean.getGrade() != null && dataBean.getGrade().size() > 0) {
                            nlist = new ArrayList<NJBean>();
                            nlist = dataBean.getGrade();
                            glist = new ArrayList<String>();
                            for (int i = 0; i < nlist.size(); i++) {
                                glist.add(nlist.get(i).getNj() + "级");
                            }
                            gAdapter.setMlist(glist);
                            lv_contants_grade.setAdapter(gAdapter);
                            gAdapter.notifyDataSetChanged();
                        } else {
                            ToastManager.getInstance().showToast(ContactsActivity.this, "暂无数据");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void setListener() {
        super.setListener();
        //搜索框点击
        tv_phone_books_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsActivity.this, ContactsSearcheActivity.class);
                startActivity(intent);
            }
        });
        //年级/部门列表点击
        lv_contants_grade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mlist = new ArrayList<MajorBean>();
                mlist = nlist.get(position).getMajor();
                plist = new ArrayList<String>();
                for (int i = 0; i < mlist.size(); i++) {
                    plist.add(mlist.get(i).getZymc());
                }
                if (clist != null && clist.size() > 0) {
                    clist.clear();
                    cAdapter.setMlist(clist);
                    cAdapter.notifyDataSetChanged();
                }
                pAdapter.setMlist(plist);
                lv_contacts_professional.setAdapter(pAdapter);
                pAdapter.notifyDataSetChanged();
                gAdapter.setSelectItem(position);
                gAdapter.notifyDataSetChanged();
                pAdapter.setSelectItem(-1);
                pAdapter.notifyDataSetChanged();
            }
        });
        //专业/职位列表点击
        lv_contacts_professional.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                blist = new ArrayList<BanJiBean>();
                blist = mlist.get(position).getClasses();
                clist = new ArrayList<String>();
                for (int i = 0; i < blist.size(); i++) {
                    clist.add(blist.get(i).getBjm());
                }
                cAdapter.setMlist(clist);
                lv_contacts_class.setAdapter(cAdapter);
                cAdapter.notifyDataSetChanged();
                pAdapter.setSelectItem(position);
                pAdapter.notifyDataSetChanged();
            }
        });
        //班级/姓名列表点击
        lv_contacts_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bId = blist.get(position).getBjdm();
                String bName = blist.get(position).getBjm();
                Intent intent = new Intent(ContactsActivity.this, ContactsDetailListActivity.class);
                intent.putExtra("bId", bId);
                intent.putExtra("bName", bName);
                startActivity(intent);

            }
        });
        //返回
        iv_phone_books_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
