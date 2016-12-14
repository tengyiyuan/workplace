package com.toplion.cplusschool.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.toplion.cplusschool.Adapter.TypeListAdapter;
import com.toplion.cplusschool.Adapter.TypeSecondListAdapter;
import com.toplion.cplusschool.Bean.CustType;
import com.toplion.cplusschool.Bean.CustTypeSecond;
import com.toplion.cplusschool.R;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends Activity {
    private ListView typelist;
    private ListView listview;
    private TypeListAdapter typeadapter;
    private TypeSecondListAdapter typesecondadapter;
    private List<CustType> list = new ArrayList<CustType>();
    private List<CustTypeSecond> secondlist = new ArrayList<CustTypeSecond>();
    private TextView type_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_list);
        initview();
    }

    private void initview() {
        type_all = (TextView) findViewById(R.id.type_all);
        listview = (ListView) findViewById(R.id.listView);
        typelist = (ListView) findViewById(R.id.typelist);
        typeadapter = new TypeListAdapter(this, list);
        typesecondadapter = new TypeSecondListAdapter(this, secondlist);
        typelist.setAdapter(typeadapter);
        listview.setAdapter(typesecondadapter);
        typelist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                CustType custtype = list.get(pos);
                updatesecond(custtype.getTypeid());
                typeadapter.setSelectItem(pos);
                typeadapter.notifyDataSetChanged();
            }

        });

        type_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                typeadapter.setSelectItem(-1);
                typeadapter.notifyDataSetChanged();
            }
        });
        getDataSource();
    }

    private void getDataSource() {
//        for (int i = 0; i < 20; i++) {
//            CustType type = new CustType();
//            type.setTypeid(i + "");
//            type.setTypename("电脑办公");
//            list.add(type);
//        }
        CustType type1 = new CustType();
        type1.setTypeid(1 + "");
        type1.setTypename("2013级");
        list.add(type1);


        CustType type2 = new CustType();
        type2.setTypeid(2 + "");
        type2.setTypename("2014级");
        list.add(type2);

        CustType type3 = new CustType();
        type3.setTypeid(3 + "");
        type3.setTypename("2015级");
        list.add(type3);

        CustType type4 = new CustType();
        type4.setTypeid(4 + "");
        type4.setTypename("2016级");
        list.add(type4);
        typeadapter.notifyDataSetChanged();
//        for (int i = 0; i < 20; i++) {
//            CustTypeSecond second = new CustTypeSecond();
//            second.setTypeid((int) (Math.random() * 20) + "");
//            second.setTypename("类型名称");
//            secondlist.add(second);
//        }
        CustTypeSecond second1 = new CustTypeSecond();
        second1.setTypeid(1 + "");
        second1.setTypename("土木工程");
        secondlist.add(second1);


        CustTypeSecond second2 = new CustTypeSecond();
        second2.setTypeid(2 + "");
        second2.setTypename("地理信息科学");
        secondlist.add(second2);


        CustTypeSecond second3 = new CustTypeSecond();
        second3.setTypeid(2 + "");
        second3.setTypename("地理信息系统");
        secondlist.add(second3);

        CustTypeSecond second4 = new CustTypeSecond();
        second4.setTypeid(3 + "");
        second4.setTypename("测绘工程");
        secondlist.add(second4);

        typesecondadapter.notifyDataSetChanged();
    }

    private void updatesecond(String typeid) {
        List<CustTypeSecond> secondlist2 = new ArrayList<CustTypeSecond>();
        secondlist2.clear();
        for (int i = 0; i < secondlist.size(); i++) {
            CustTypeSecond second = secondlist.get(i);
            if (second.getTypeid().equals(typeid)) {
                secondlist2.add(second);
            }
        }
        typesecondadapter.setType(secondlist2);
        typesecondadapter.notifyDataSetChanged();
    }
}
