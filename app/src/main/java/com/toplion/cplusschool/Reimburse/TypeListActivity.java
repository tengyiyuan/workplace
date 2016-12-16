package com.toplion.cplusschool.Reimburse;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Bean.StandardInfo;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.widget.CustomDialog;

import java.util.ArrayList;

/**
 * 我的预约报修列表页面
 * au:tengyy
 * data:2016/8/3
 */
public class TypeListActivity extends BaseActivity {
    private ListView listView1;
    private ImageView back;
    private MyAdapter adapter;
    private TextView goon;
    private TextView gostart;
    private ArrayList<StandardInfo> standardInfo;
    private int style = 0;
    private StandardInfo info;
    private int maxdanju = 0;
    private int maxtime = 0;
    private TextView oldertime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typelist);
        init();
    }

    /**
     * 初始化数据
     * au:tengyy
     * data:2016/8/3
     */
    @Override
    protected void init() {
        super.init();
        standardInfo = new ArrayList<StandardInfo>();
        style = getIntent().getIntExtra("style", 0);
        if (style == 0) {
            info = (StandardInfo) getIntent().getSerializableExtra("standardInfo");
            standardInfo.add(info);
        } else {
            standardInfo = (ArrayList<StandardInfo>) getIntent().getSerializableExtra("infolist");
        }
        oldertime = (TextView) findViewById(R.id.oldertime);
        goon = (TextView) findViewById(R.id.goon);
        gostart = (TextView) findViewById(R.id.gostart);
        back = (ImageView) findViewById(R.id.back);
        listView1 = (ListView) findViewById(R.id.mylist);
        adapter = new MyAdapter();
        listView1.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        goon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TypeListActivity.this, BaotypeActivity.class);
                intent.putExtra("code", 1);
                startActivityForResult(intent, 1);
            }
        });
        gostart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxtime < 120) {
                    if (standardInfo != null && standardInfo.size() > 0) {
                        Intent intent = new Intent(TypeListActivity.this, ReimbursementOrderTimeActivity.class);
                        intent.putExtra("infolist", standardInfo);
                        intent.putExtra("maxtime", maxtime);
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance().showToast(TypeListActivity.this, "请选择要报销的单据");
                    }
                } else {
                    ToastManager.getInstance().showToast(TypeListActivity.this, "当前票据数量过多，请到窗口进行办理。");
                }
            }
        });
        oldertime.setText("共" + maxdanju + "张单据,耗时" + maxtime + "分钟");

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final CustomDialog dialog = new CustomDialog(
                        TypeListActivity.this);
                dialog.setlinecolor();
                dialog.setTitle("提示");
                dialog.setContentboolean(true);
                dialog.setDetial("确定删除此条单据?");
                dialog.setLeftText("确定");
                dialog.setRightText("取消");
                dialog.setLeftOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        standardInfo.remove(position);
                        dialog.dismiss();
                        maxdanju = 0;
                        maxtime = 0;
                        updateText(standardInfo);
                        adapter.notifyDataSetChanged();
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
        });
        updateText(standardInfo);
    }

    /**
     * 更新底部数据
     * au:tengyy
     * data:2016/10/8
     */
    private void updateText(ArrayList<StandardInfo> infolist) {
        if (infolist.size() != 0) {
            maxdanju = 0;
            maxtime = 0;
            for (int i = 0; i < infolist.size(); i++) {
                StandardInfo info = infolist.get(i);
                maxdanju = maxdanju + info.getRRNUMBER();
                maxtime = maxtime + info.getRTPROCESSINGTIME() * info.getRRNUMBER();
            }
            oldertime.setText("共" + maxdanju + "张单据,耗时" + maxtime + "分钟");
        } else {
            oldertime.setText("共0张单据,耗时0分钟");
        }
    }

    /**
     * 返回页面数据跳转
     * au:tengyy
     * data:2016/8/3
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case 0:
                    break;
                case 1:
                    maxdanju = 0;
                    maxtime = 0;
                    info = (StandardInfo) data.getSerializableExtra("standardInfo");
                    standardInfo.add(info);
                    updateText(standardInfo);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 网络请求接口数据解析
     * au:tengyy
     * data:2016/8/3
     */
    @Override
    protected void getData() {
        super.getData();
    }

    /**
     * 列表适配器
     * au:tengyy
     * data:2016/8/3
     */
    private class MyAdapter extends BaseAdapter {
        public int getCount() {
            return standardInfo.size();
        }

        public Object getItem(int position) {
            return standardInfo.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(TypeListActivity.this).inflate(
                        R.layout.typeitem, null);
                viewHolder.text_name = (TextView) convertView.findViewById(R.id.typename);
                viewHolder.text_num = (TextView) convertView.findViewById(R.id.typenum);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            StandardInfo info = standardInfo.get(position);
//            maxdanju = maxdanju + info.getRRNUMBER();
//            maxtime = maxtime + info.getRTPROCESSINGTIME() * info.getRRNUMBER();
            viewHolder.text_name.setText(info.getRTNAME());
            viewHolder.text_num.setText("票据" + info.getRRNUMBER() + "张");
//            oldertime.setText("共" + maxdanju + "张单据,耗时" + maxtime + "分钟");
            return convertView;
        }
    }

    class ViewHolder {
        private TextView text_name;//报销名称
        private TextView text_num;//单据数量

    }
}
