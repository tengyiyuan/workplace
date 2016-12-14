package com.toplion.cplusschool.Reimburse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbRequestParams;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Bean.StandardInfo;
import com.toplion.cplusschool.Bean.type;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toplion on 2016/7/30.
 */
public class BaotypeActivity extends BaseActivity {
    private ListView listView1;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private TextView text6;
    private ArrayList<type> list, list1, list2, list3, list4, list5, list6;
    private MyAdapter myAdapter;
    private int type = 1;
    private ImageView back;
    private int intCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typemain);
        init();
    }

    @Override
    protected void init() {
        super.init();
        intCode = getIntent().getIntExtra("code", 0);
        back = (ImageView) findViewById(R.id.back);
        listView1 = (ListView) findViewById(R.id.listView1);
        text1 = (TextView) findViewById(R.id.txt_1);
        text2 = (TextView) findViewById(R.id.txt_2);
        text3 = (TextView) findViewById(R.id.txt_3);
        text4 = (TextView) findViewById(R.id.txt_4);
        text5 = (TextView) findViewById(R.id.txt_5);
        text6 = (TextView) findViewById(R.id.txt_6);
        list = new ArrayList<type>();
        myAdapter = new MyAdapter();
        listView1.setAdapter(myAdapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type obj = list.get(position);
                switch (type) {
                    case 1:
                        if (obj.getRTTYPE() == 0) {
                            initSpinner2(obj);
                        } else {
                            Intent intent = new Intent(BaotypeActivity.this, ReimbursementDataActivity.class);
                            intent.putExtra("code", intCode);
                            intent.putExtra("TYPE", obj);
                            startActivityForResult(intent, intCode);
                        }
                        break;
                    case 2:
                        if (obj.getRTTYPE() == 0) {
                            initSpinner3(obj);
                        } else {
                            Intent intent = new Intent(BaotypeActivity.this, ReimbursementDataActivity.class);
                            intent.putExtra("code", intCode);
                            intent.putExtra("TYPE", obj);
                            startActivityForResult(intent, intCode);
                        }
                        break;
                    case 3:
                        if (obj.getRTTYPE() == 0) {
                            initSpinner4(obj);
                        } else {
                            Intent intent = new Intent(BaotypeActivity.this, ReimbursementDataActivity.class);
                            intent.putExtra("code", intCode);
                            intent.putExtra("TYPE", obj);
                            startActivityForResult(intent, intCode);
                        }
                        break;
                    case 4:
                        if (obj.getRTTYPE() == 0) {
                            initSpinner5(obj);
                        } else {
                            Intent intent = new Intent(BaotypeActivity.this, ReimbursementDataActivity.class);
                            intent.putExtra("code", intCode);
                            intent.putExtra("TYPE", obj);
                            startActivityForResult(intent, intCode);
                        }
                        break;
                    case 5:
                        if (obj.getRTTYPE() == 0) {
                            initSpinner6(obj);
                        } else {
                            Intent intent = new Intent(BaotypeActivity.this, ReimbursementDataActivity.class);
                            intent.putExtra("code", intCode);
                            intent.putExtra("TYPE", obj);
                            startActivityForResult(intent, intCode);
                        }
                        break;
                }


            }
        });
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = list1;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 1;
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                text3.setVisibility(View.GONE);
                text4.setVisibility(View.GONE);
                text5.setVisibility(View.GONE);
                text6.setVisibility(View.GONE);
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = list2;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 2;
                text2.setVisibility(View.GONE);
                text3.setVisibility(View.GONE);
                text4.setVisibility(View.GONE);
                text5.setVisibility(View.GONE);
                text6.setVisibility(View.GONE);

            }
        });
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = list3;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 3;
                text3.setVisibility(View.GONE);
                text4.setVisibility(View.GONE);
                text5.setVisibility(View.GONE);
                text6.setVisibility(View.GONE);
            }
        });
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = list4;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 4;
                text4.setVisibility(View.GONE);
                text5.setVisibility(View.GONE);
                text6.setVisibility(View.GONE);
            }
        });
        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = list5;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 5;
                text5.setVisibility(View.GONE);
                text6.setVisibility(View.GONE);
            }
        });
        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = list6;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 6;
                text6.setVisibility(View.GONE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    finish();

                } else if (type == 2) {
                    list = list1;
                    myAdapter.notifyDataSetChanged();
                    listGoTop();
                    type = 1;
                    text1.setVisibility(View.GONE);
                    text2.setVisibility(View.GONE);
                    text3.setVisibility(View.GONE);
                    text4.setVisibility(View.GONE);
                    text5.setVisibility(View.GONE);
                    text6.setVisibility(View.GONE);

                } else if ( type == 3) {
                    list = list2;
                    myAdapter.notifyDataSetChanged();
                    listGoTop();
                    type = 2;
                    text2.setVisibility(View.GONE);
                    text3.setVisibility(View.GONE);
                    text4.setVisibility(View.GONE);
                    text5.setVisibility(View.GONE);
                    text6.setVisibility(View.GONE);


                } else if ( type == 4) {
                    list = list3;
                    myAdapter.notifyDataSetChanged();
                    listGoTop();
                    type = 3;
                    text3.setVisibility(View.GONE);
                    text4.setVisibility(View.GONE);
                    text5.setVisibility(View.GONE);
                    text6.setVisibility(View.GONE);


                } else if (type == 5) {
                    list = list4;
                    myAdapter.notifyDataSetChanged();
                    listGoTop();
                    type = 4;
                    text4.setVisibility(View.GONE);
                    text5.setVisibility(View.GONE);
                    text6.setVisibility(View.GONE);


                } else if ( type == 6) {
                    list = list5;
                    myAdapter.notifyDataSetChanged();
                    listGoTop();
                    type = 5;
                    text5.setVisibility(View.GONE);
                    text6.setVisibility(View.GONE);

                }
            }
        });
        initSpinner1();
    }

    public void initSpinner1() {
        int style = 0;
        switch (style) {
            case 0:
                getTypeone();
                break;
        }

    }

    public void initSpinner2(type code) {
        list2 = new ArrayList<type>();
        try {
            JSONArray array = new JSONArray(code.getCHILDREN());
            if (array.length() != 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = (JSONObject) array.get(i);
                    type types = new type();
                    types.setRTTYPE(Function.getInstance().getInteger(obj, "RTTYPE"));
                    types.setRTID(Function.getInstance().getInteger(obj, "RTID"));
                    types.setRTPROCESSINGTIME(Function.getInstance().getInteger(obj, "RTPROCESSINGTIME"));
                    types.setRTNAME(Function.getInstance().getString(obj, "RTNAME"));
                    types.setCHILDREN(Function.getInstance().getString(obj, "CHILDREN"));
                    list2.add(types);
                }
                text1.setText(code.getRTNAME());
                text1.setVisibility(View.VISIBLE);
                list = list2;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 2;
            } else {
                ToastManager.getInstance().showToast(BaotypeActivity.this, "无法报销此类别");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initSpinner3(type code) {
        list3 = new ArrayList<type>();
        try {
            JSONArray array = new JSONArray(code.getCHILDREN());
            if (array.length() != 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = (JSONObject) array.get(i);
                    type types = new type();
                    types.setRTTYPE(Function.getInstance().getInteger(obj, "RTTYPE"));
                    types.setRTID(Function.getInstance().getInteger(obj, "RTID"));
                    types.setRTPROCESSINGTIME(Function.getInstance().getInteger(obj, "RTPROCESSINGTIME"));
                    types.setRTNAME(Function.getInstance().getString(obj, "RTNAME"));
                    types.setCHILDREN(Function.getInstance().getString(obj, "CHILDREN"));
                    list3.add(types);
                }
                text2.setText(code.getRTNAME());
                text2.setVisibility(View.VISIBLE);
                list = list3;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 3;
            } else {
                ToastManager.getInstance().showToast(BaotypeActivity.this, "无法报销此类别");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initSpinner4(type code) {
        list4 = new ArrayList<type>();
        try {
            JSONArray array = new JSONArray(code.getCHILDREN());
            if (array.length() != 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = (JSONObject) array.get(i);
                    type types = new type();
                    types.setRTTYPE(Function.getInstance().getInteger(obj, "RTTYPE"));
                    types.setRTID(Function.getInstance().getInteger(obj, "RTID"));
                    types.setRTPROCESSINGTIME(Function.getInstance().getInteger(obj, "RTPROCESSINGTIME"));
                    types.setRTNAME(Function.getInstance().getString(obj, "RTNAME"));
                    types.setCHILDREN(Function.getInstance().getString(obj, "CHILDREN"));
                    list4.add(types);
                }
                text3.setText(code.getRTNAME());
                text3.setVisibility(View.VISIBLE);
                list = list4;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 4;
            } else {
                ToastManager.getInstance().showToast(BaotypeActivity.this, "无法报销此类别");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initSpinner5(type code) {
        list5 = new ArrayList<type>();
        try {
            JSONArray array = new JSONArray(code.getCHILDREN());
            if (array.length() != 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = (JSONObject) array.get(i);
                    type types = new type();
                    types.setRTTYPE(Function.getInstance().getInteger(obj, "RTTYPE"));
                    types.setRTID(Function.getInstance().getInteger(obj, "RTID"));
                    types.setRTPROCESSINGTIME(Function.getInstance().getInteger(obj, "RTPROCESSINGTIME"));
                    types.setRTNAME(Function.getInstance().getString(obj, "RTNAME"));
                    types.setCHILDREN(Function.getInstance().getString(obj, "CHILDREN"));
                    list5.add(types);
                }
                text4.setText(code.getRTNAME());
                text4.setVisibility(View.VISIBLE);
                list = list5;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 5;
            } else {
                ToastManager.getInstance().showToast(BaotypeActivity.this, "无法报销此类别");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initSpinner6(type code) {
        list6 = new ArrayList<type>();
        try {
            JSONArray array = new JSONArray(code.getCHILDREN());
            if (array.length() != 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = (JSONObject) array.get(i);
                    type types = new type();
                    types.setRTTYPE(Function.getInstance().getInteger(obj, "RTTYPE"));
                    types.setRTID(Function.getInstance().getInteger(obj, "RTID"));
                    types.setRTPROCESSINGTIME(Function.getInstance().getInteger(obj, "RTPROCESSINGTIME"));
                    types.setRTNAME(Function.getInstance().getString(obj, "RTNAME"));
                    types.setCHILDREN(Function.getInstance().getString(obj, "CHILDREN"));
                    list6.add(types);
                }
                text5.setText(code.getRTNAME());
                text5.setVisibility(View.VISIBLE);
                list = list6;
                myAdapter.notifyDataSetChanged();
                listGoTop();
                type = 5;
            } else {
                ToastManager.getInstance().showToast(BaotypeActivity.this, "无法报销此类别");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getTypeone() {
        list1 = new ArrayList<type>();
        getData();
    }

    private void listGoTop() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listView1.setSelection(0);
            }
        }, 200);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case 0:
                    break;
                case 1:
                    StandardInfo info = (StandardInfo) data.getSerializableExtra("standardInfo");
                    data.putExtra("standardInfo", info);
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                default:
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void getData() {
        super.getData();
        AbRequestParams params = new AbRequestParams();
        params.put("schoolCode", Constants.SCHOOL_CODE);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getAccountType") + Constants.BASEPARAMS;
        abHttpUtil.post(url, params, new CallBackParent(this, "正在加载,请稍后...", "") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray data = object.getJSONArray("data");
                    if (data.length() != 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = (JSONObject) data.get(i);
                            type types = new type();
                            types.setRTTYPE(Function.getInstance().getInteger(obj, "RTTYPE"));
                            types.setRTID(Function.getInstance().getInteger(obj, "RTID"));
                            types.setRTPROCESSINGTIME(Function.getInstance().getInteger(obj, "RTPROCESSINGTIME"));
                            types.setRTNAME(Function.getInstance().getString(obj, "RTNAME"));
                            types.setCHILDREN(Function.getInstance().getString(obj, "CHILDREN"));
                            list1.add(types);
                        }
                        type = 1;
                        list = list1;
                        myAdapter.notifyDataSetChanged();
                        listGoTop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class MyAdapter extends BaseAdapter {
        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text_content = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(BaotypeActivity.this).inflate(
                        R.layout.single_select, null);
                text_content = (TextView) convertView.findViewById(R.id.txt_1);
                convertView.setTag(text_content);
            } else {
                text_content = (TextView) convertView.getTag();
            }
            type types = list.get(position);
            if (type == 1) {
                text_content.setText("  " + types.getRTNAME());
            } else if (type == 2) {
                text_content.setText("    " + types.getRTNAME());
            } else if (type == 3) {
                text_content.setText("      " + types.getRTNAME());
            } else if (type == 4) {
                text_content.setText("        " + types.getRTNAME());
            } else if (type == 5) {
                text_content.setText("          " + types.getRTNAME());
            } else if (type == 6) {
                text_content.setText("            " + types.getRTNAME());
            }
            return convertView;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && type == 1) {
            finish();
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_BACK) && type == 2) {
            list = list1;
            myAdapter.notifyDataSetChanged();
            listGoTop();
            type = 1;
            text1.setVisibility(View.GONE);
            text2.setVisibility(View.GONE);
            text3.setVisibility(View.GONE);
            text4.setVisibility(View.GONE);
            text5.setVisibility(View.GONE);
            text6.setVisibility(View.GONE);
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_BACK) && type == 3) {
            list = list2;
            myAdapter.notifyDataSetChanged();
            listGoTop();
            type = 2;
            text2.setVisibility(View.GONE);
            text3.setVisibility(View.GONE);
            text4.setVisibility(View.GONE);
            text5.setVisibility(View.GONE);
            text6.setVisibility(View.GONE);
            return true;

        } else if ((keyCode == KeyEvent.KEYCODE_BACK) && type == 4) {
            list = list3;
            myAdapter.notifyDataSetChanged();
            listGoTop();
            type = 3;
            text3.setVisibility(View.GONE);
            text4.setVisibility(View.GONE);
            text5.setVisibility(View.GONE);
            text6.setVisibility(View.GONE);
            return true;

        } else if ((keyCode == KeyEvent.KEYCODE_BACK) && type == 5) {
            list = list4;
            myAdapter.notifyDataSetChanged();
            listGoTop();
            type = 4;
            text4.setVisibility(View.GONE);
            text5.setVisibility(View.GONE);
            text6.setVisibility(View.GONE);
            return true;

        } else if ((keyCode == KeyEvent.KEYCODE_BACK) && type == 6) {
            list = list5;
            myAdapter.notifyDataSetChanged();
            listGoTop();
            type = 5;
            text5.setVisibility(View.GONE);
            text6.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
