package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.toplion.cplusschool.Adapter.LocalSelectAdapter;
import com.toplion.cplusschool.Bean.CityBean;
import com.toplion.cplusschool.Bean.CityMapBean;
import com.toplion.cplusschool.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang
 * on 2016/11/18.
 * 加载城市列表
 */

public class LocalSelectActivity extends BaseActivity {
    private ImageView iv_select_city_return;//返回键
    private ListView lv_province;//省份
    private ListView lv_city;// 市
    private ListView lv_area;//区/县

    private List<CityBean> proList;//省
    private List<CityBean> clist;//市
    private List<CityBean> alist;//区

    private Map<String, List<CityBean>> cityMaps = new HashMap<String, List<CityBean>>();
    private Map<String, List<CityBean>> areaMaps = new HashMap<String, List<CityBean>>();


    private String proSelect = "";
    private String proCode = "";
    private String citySelect = "";
    private String cityCode = "";
    private String areaSelect = "";
    private String areaCode = "";

    private LocalSelectAdapter proAdapter;//省
    private LocalSelectAdapter cAdapter;//市
    private LocalSelectAdapter aAdapter;//区

    private int flag = 1;//标记点的层级


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citys_list);
        init();
    }

    @Override
    protected void init() {
        super.init();
        iv_select_city_return = (ImageView) findViewById(R.id.iv_select_city_return);
        lv_province = (ListView) findViewById(R.id.lv_province);
        lv_city = (ListView) findViewById(R.id.lv_city);
        lv_area = (ListView) findViewById(R.id.lv_area);
//        getData();
        CityMapBean cityMapBean = (CityMapBean) getIntent().getSerializableExtra("mapBean");
        proList = cityMapBean.getProList();
        cityMaps = cityMapBean.getCityMap();
        areaMaps = cityMapBean.getAreaMaps();
        if (proAdapter == null) {
            proAdapter = new LocalSelectAdapter(this, proList);
        } else {
            proAdapter.setMlist(proList);
        }
        setDataToListView(lv_province, proAdapter);
        setListener();
    }

    @Override
    protected void getData() {
        super.getData();
        JSONObject jsonObject = initJsonData();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");//获取整个json数据
            proList = new ArrayList<CityBean>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);
                CityBean pBean = new CityBean();
                pBean.setCode(jsonP.getString("code"));
                pBean.setName(jsonP.getString("name"));
                proList.add(pBean);
                JSONArray jsonCity = null;
                try {
                    jsonCity = jsonP.getJSONArray("data");//在所有的省中取出所有的市，转jsonArray
                } catch (Exception e) {
                    continue;
                }
                List<CityBean> cityList = new ArrayList<CityBean>();
                //遍历所有的市
                for (int j = 0; j < jsonCity.length(); j++) {
                    JSONObject jsonCy = jsonCity.getJSONObject(j);
                    CityBean cBean = new CityBean();
                    cBean.setCode(jsonCy.getString("code"));
                    cBean.setName(jsonCy.getString("name"));
                    cityList.add(cBean);
                    JSONArray jsonAra = null;
                    try {
                        jsonAra = jsonCy.getJSONArray("data");//在所有的省中取出所有的市，转jsonArray
                    } catch (Exception e) {
                        continue;
                    }
                    List<CityBean> areaList = new ArrayList<CityBean>();
                    for (int k = 0; k < jsonAra.length(); k++) {
                        JSONObject jsonA = jsonAra.getJSONObject(k);
                        CityBean aBean = new CityBean();
                        aBean.setCode(jsonA.getString("code"));
                        aBean.setName(jsonA.getString("name"));
                        areaList.add(aBean);
                    }
                    areaMaps.put(jsonCy.getString("code"), areaList);
                }
                cityMaps.put(jsonP.getString("code"), cityList);
            }
            if (proAdapter == null) {
                proAdapter = new LocalSelectAdapter(this, proList);
            } else {
                proAdapter.setMlist(proList);
            }
            setDataToListView(lv_province, proAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给listView赋值
     *
     * @param listView
     */
    private void setDataToListView(ListView listView, LocalSelectAdapter lAdapter) {
        listView.setAdapter(lAdapter);
        lAdapter.notifyDataSetChanged();
    }

    /**
     * 从assert文件夹中获取json数据
     */
    private JSONObject initJsonData() {
        JSONObject jsonObject = null;
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open("citys.json");//打开json数据
            byte[] by = new byte[is.available()];//转字节
            int len = -1;
            while ((len = is.read(by)) != -1) {
                sb.append(new String(by, 0, len, "utf-8"));//根据字节长度设置编码
            }
            is.close();//关闭流
            jsonObject = new JSONObject(sb.toString());//为json赋值
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void setListener() {
        super.setListener();
        lv_province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fatherCode = proList.get(position).getCode();
                String fatherName = proList.get(position).getName();
                proSelect = fatherName;
                proCode = fatherCode;
                clist = cityMaps.get(fatherCode);
                if (clist != null && clist.size() > 0) {
                    lv_province.setVisibility(View.GONE);
                    lv_city.setVisibility(View.VISIBLE);
                    lv_area.setVisibility(View.GONE);
                    if (cAdapter == null) {
                        cAdapter = new LocalSelectAdapter(LocalSelectActivity.this, clist);
                    } else {
                        cAdapter.setMlist(clist);
                    }
                    setDataToListView(lv_city, cAdapter);
                    flag++;
                } else {
                    goToResult();
                }
            }
        });
        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fatherCode = clist.get(position).getCode();
                String fatherName = clist.get(position).getName();
                citySelect = "-" + fatherName;
                cityCode = "-" + fatherCode;
                alist = areaMaps.get(fatherCode);
                if (alist != null && alist.size() > 0) {
                    lv_province.setVisibility(View.GONE);
                    lv_city.setVisibility(View.GONE);
                    lv_area.setVisibility(View.VISIBLE);
                    if (aAdapter == null) {
                        aAdapter = new LocalSelectAdapter(LocalSelectActivity.this, alist);
                    } else {
                        aAdapter.setMlist(alist);
                    }
                    setDataToListView(lv_area, aAdapter);
                    flag++;
                } else {
                    goToResult();
                }
            }
        });
        lv_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fatherCode = alist.get(position).getCode();
                String fatherName = alist.get(position).getName();
                areaSelect = "-" + fatherName;
                areaCode = "-" + fatherCode;
                goToResult();
            }
        });
        iv_select_city_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReturn();
            }
        });
    }

    private void goToResult() {
        Intent intent = new Intent();
        intent.putExtra("code", proCode + cityCode + areaCode);
        intent.putExtra("name", proSelect + citySelect + areaSelect);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setReturn();
    }

    private void setReturn() {
        if (flag == 1) {
            finish();
        } else if (flag == 2) {
            lv_province.setVisibility(View.VISIBLE);
            lv_city.setVisibility(View.GONE);
            cityCode = "";
            citySelect = "";
            flag--;
        } else if (flag == 3) {
            lv_city.setVisibility(View.VISIBLE);
            lv_area.setVisibility(View.GONE);
            areaSelect = "";
            areaCode = "";
            flag--;
        }
        Log.e("flag==========", flag + "");
    }
}
