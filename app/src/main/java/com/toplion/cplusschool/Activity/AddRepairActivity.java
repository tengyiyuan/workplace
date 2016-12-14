package com.toplion.cplusschool.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.Bean.RepairInfoBean;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustomDialogListview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 新建报修
 *
 * @author wang
 */
public class AddRepairActivity extends BaseActivity {
    private ImageView iv_add_repair_back;
    private TextView tv_repair_title;//
    private RadioGroup rg_equipment_group;//设备类型
    private RadioButton rbt_equipment_yidong;//移动设备
    private RadioButton rbt_equipment_diannao;//电脑
    private RadioGroup radio_internet_group;//上网方式
    private RadioButton rbt_internet_wuxian;//无线ddddddd
    private RadioButton rbt_internet_youxian;//有线
    private RadioGroup radio_internet_near_group;///周围朋友上网网速
    private RadioButton rbt_internet_near_normal;//正常
    private RadioButton rbt_internet_near_slow;//慢
    private EditText tv_fault_address_content;//房间号
    private EditText et_fault_description_content;//故障描述
    private TextView tv_reairing_next;//下一步
    private TextView tv_select_area;//选择区域
    private TextView tv_select_floornum;//选择楼号
    private TextView tv_question_type_content;//故障现象
    private List<CommonBean> areaList = null;//区域列表
    private List<CommonBean> faultList = null;//故障现象列表
    private List<CommonBean> floorList = null;//教学楼列表
    private AbHttpUtil abHttpUtil;//网络请求工具
    private int style = 0;//从哪里过来 1：我的报修详情跳转过来，做修改；2：从新建保修单过来做添加操作
    private RepairInfoBean repairInfoBean = new RepairInfoBean();//用来接收详情界面传递的值
    private SharePreferenceUtils share;
    private boolean isShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repairing);
        init();
        getData();
        setListener();
    }
    /**
     * 初始化布局
     */
    @Override
    public void init() {
        abHttpUtil = AbHttpUtil.getInstance(this);//初始化请求工具
        share = new SharePreferenceUtils(this);
        style = getIntent().getIntExtra("style", 0);//从哪跳转过来的
        iv_add_repair_back = (ImageView) findViewById(R.id.iv_add_repair_back);
        tv_repair_title = (TextView) findViewById(R.id.tv_repair_title);
        tv_question_type_content = (TextView) findViewById(R.id.tv_question_type_content);
        rg_equipment_group = (RadioGroup) findViewById(R.id.rg_equipment_group);
        rbt_equipment_yidong = (RadioButton) findViewById(R.id.rbt_equipment_yidong);
        rbt_equipment_diannao = (RadioButton) findViewById(R.id.rbt_equipment_diannao);
        radio_internet_group = (RadioGroup) findViewById(R.id.radio_internet_group);
        rbt_internet_wuxian = (RadioButton) findViewById(R.id.rbt_internet_wuxian);
        rbt_internet_youxian = (RadioButton) findViewById(R.id.rbt_internet_youxian);
        radio_internet_near_group = (RadioGroup) findViewById(R.id.radio_internet_near_group);
        rbt_internet_near_normal = (RadioButton) findViewById(R.id.rbt_internet_near_normal);
        rbt_internet_near_slow = (RadioButton) findViewById(R.id.rbt_internet_near_slow);
        tv_fault_address_content = (EditText) findViewById(R.id.tv_fault_address_content);
        et_fault_description_content = (EditText) findViewById(R.id.et_fault_description_content);
        tv_reairing_next = (TextView) findViewById(R.id.tv_reairing_next);
        tv_select_area = (TextView) findViewById(R.id.tv_select_area);
        tv_select_floornum = (TextView) findViewById(R.id.tv_select_floornum);
        tv_reairing_next.setEnabled(false);
    }

    @Override
    protected void getData() {
        super.getData();
        //网络请求数据
        getConstInfo();//获取报修单其他类型
    }

    /**
     * 获取其他问题类型
     */
    private void getConstInfo() {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getConstInfo") + Constants.BASEPARAMS+"&stuNo="+share.getString("ROLE_ID","");
        abHttpUtil.get(url, new CallBackParent(AddRepairActivity.this, getResources().getString(R.string.loading)) {
            @Override
            public void Get_Result(String result) {
                Log.e("questionList", result);
                try {
                    JSONObject json = new JSONObject(result);
                    JSONObject jsonObject = new JSONObject(json.getString("const"));
                    JSONObject phoneType = new JSONObject(jsonObject.getString("InternetTermina"));//设备类型
                    JSONObject isNormal = new JSONObject(jsonObject.getString("FriendsInternet"));//正常  不正常
                    JSONObject accessWay = new JSONObject(jsonObject.getString("AccessWay"));//有线无线
                    JSONArray areaArray = json.getJSONArray("area");//区域列表
                    JSONArray faultArray = json.getJSONArray("fault");//故障现象列表
                    int repairCount = json.getInt("repairCount");

                    areaList = new ArrayList<CommonBean>();
                    for (int i = 0; i < areaArray.length(); i++) {
                        JSONObject object = areaArray.getJSONObject(i);
                        areaList.add(new CommonBean(object.getInt("CA_ID") + "", object.getString("CA_NAME")));
                    }
                    faultList = new ArrayList<CommonBean>();
                    for (int i = 0; i < faultArray.length(); i++) {
                        JSONObject object = faultArray.getJSONObject(i);
                        faultList.add(new CommonBean(object.getInt("DX_ID") + "", object.getString("DX_TITLE")));
                    }
                    //设备类型
                    rbt_equipment_yidong.setText(phoneType.getString("100"));
                    rbt_equipment_diannao.setText(phoneType.getString("101"));
                    //上网方式
                    rbt_internet_wuxian.setText(accessWay.getString("302"));
                    rbt_internet_youxian.setText(accessWay.getString("301"));
                    //上网速度
                    rbt_internet_near_normal.setText(isNormal.getString("200"));
                    rbt_internet_near_slow.setText(isNormal.getString("201"));
                    if (style == 1) {//当style ==1时代表修改数据,做修改操作时，先把原数据set到控件上
                        repairInfoBean = (RepairInfoBean) getIntent().getSerializableExtra("repairInfoBean");
                        tv_repair_title.setText("编辑报修单");
                        isShow = true;
                        if (repairInfoBean != null) {
                            tv_question_type_content.setText(repairInfoBean.getDXTITLE()+ "");
                            if(!TextUtils.isEmpty(repairInfoBean.getNRIFACILITYTYPEID())){
                                //设置设备类型
                                if (repairInfoBean.getNRIFACILITYTYPEID().equals("100")) {
//                                    repairInfoBean.setNRIFACILITYTYPEID("100");
                                    rbt_equipment_yidong.setChecked(true);
                                } else {
                                    rbt_equipment_diannao.setChecked(true);
//                                    repairInfoBean.setNRIFACILITYTYPEID("101");
                                }
                            }
                            if(!TextUtils.isEmpty(repairInfoBean.getNRIINTERNETCASEID())){
                                //设置上网方式
                                if (repairInfoBean.getNRIOFACCESSID().equals("302")) {
                                    rbt_internet_wuxian.setChecked(true);
//                                    repairInfoBean.setNRIINTERNETCASEID("302");
                                } else {
                                    rbt_internet_youxian.setChecked(true);
//                                    repairInfoBean.setNRIINTERNETCASEID("301");
                                }
                            }
                            if(!TextUtils.isEmpty(repairInfoBean.getNRIOFACCESSID())){
                                //设置上网网速
                                if (repairInfoBean.getNRIINTERNETCASEID().equals("200")) {
                                    rbt_internet_near_normal.setChecked(true);
//                                    repairInfoBean.setNRIOFACCESSID("200");
                                } else {
                                    rbt_internet_near_slow.setChecked(true);
//                                    repairInfoBean.setNRIOFACCESSID("201");
                                }
                            }
                            tv_select_area.setText(repairInfoBean.getCANAME());
                            tv_select_floornum.setText(repairInfoBean.getSHDNAME());
                            Log.e("教学楼",repairInfoBean.getCAID()+"");
                            getFloorListById(repairInfoBean.getCAID()+"");
                            tv_fault_address_content.setText(repairInfoBean.getNRIADDRESS() + "");
                            et_fault_description_content.setText(repairInfoBean.getNRIFAULTDESCRIPTION() + "");
                        }
                    }else{
                        if(repairCount != 0){
                            final CommDialog dlog1 = new CommDialog(AddRepairActivity.this);
                            dlog1.CreateDialogOnlyOk("系统提示", "返回", "您有正在处理的工单，请在处理完成后提交新的工单!", new CommDialog.CallBack() {
                                @Override
                                public void isConfirm(boolean flag) {
                                    if (flag) {
                                        isShow = false;
                                        dlog1.cancelDialog();
                                        finish();
                                    }
                                }
                            });
                        }else{
                            isShow = true;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 根据区域id获取楼列表
     *
     * @param aId 区域id
     */
    private void getFloorListById(String aId) {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getFloorInfoByArea") + Constants.BASEPARAMS + "&areaId=" + aId;
        abHttpUtil.get(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                try {
                    JSONObject json = new JSONObject(content);
                    String code = json.getString("code");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        floorList = new ArrayList<CommonBean>();
                        JSONArray jsonArray = json.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            json = jsonArray.getJSONObject(i);
                            floorList.add(new CommonBean(json.getInt("SHDID") + "", json.getString("SHDNAME")));
                        }
                        tv_select_floornum.setText(floorList.get(0).getDes());
                        repairInfoBean.setSHDID(Integer.parseInt(floorList.get(0).getId()));
                    } else{
                        if (null != floorList && floorList.size() > 0) {
                            floorList.clear();
                        }
                        tv_select_floornum.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (null != floorList && faultList.size() > 0) {
                        floorList.clear();
                    }
                    tv_select_floornum.setText("");
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {

            }
        });
    }

    @Override
    public void setListener() {
        //返回键
        iv_add_repair_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AddRepairActivity.this.finish();
            }
        });
        //选择区域
        tv_select_area.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog("区域", areaList, tv_select_area);
            }
        });
        //选择楼号
        tv_select_floornum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog("楼号", floorList, tv_select_floornum);
            }
        });
        //故障现象列表
        tv_question_type_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog("故障现象", faultList, tv_question_type_content);
            }
        });
        //下一步
        tv_reairing_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String faultDes = et_fault_description_content.getText().toString();//故障描述
                repairInfoBean.setNRIFAULTDESCRIPTION(faultDes + "");
                Intent intent = new Intent(AddRepairActivity.this, RepairPersonInfoActivity.class);
                intent.putExtra("style", style);
                intent.putExtra("repairInfoBean", repairInfoBean);
                startActivity(intent);
            }
        });
        //设备类型
        rg_equipment_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbt_equipment_yidong) {//选择移动设备
                    repairInfoBean.setNRIFACILITYTYPEID("100");
                } else if (checkedId == R.id.rbt_equipment_diannao) {//选择电脑
                    repairInfoBean.setNRIFACILITYTYPEID("101");
                }
                getViewData();
            }
        });
        //上网方式
        radio_internet_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbt_internet_wuxian) {//选择无线
                    repairInfoBean.setNRIINTERNETCASEID("302");
                } else if (checkedId == R.id.rbt_internet_youxian) {//选择有线
                    repairInfoBean.setNRIINTERNETCASEID("301");
                }
                getViewData();
            }
        });
        //周围朋友上网网速
        radio_internet_near_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbt_internet_near_normal) {//正常
                    repairInfoBean.setNRIOFACCESSID("200");
                } else if (checkedId == R.id.rbt_internet_near_slow) {//不正常
                    repairInfoBean.setNRIOFACCESSID("201");
                }
                getViewData();
            }
        });
        //房间号输入框监听事件
        tv_fault_address_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                repairInfoBean.setNRIADDRESS(s.toString());
                getViewData();
            }
        });

    }

    //判断“下一步”按钮 是否可以点击
    private void getViewData() {
        if (repairInfoBean.getNRIQUESTIONTYPE()!= 0 && !TextUtils.isEmpty(repairInfoBean.getNRIFACILITYTYPEID())
                && !TextUtils.isEmpty(repairInfoBean.getNRIINTERNETCASEID()) && !TextUtils.isEmpty(repairInfoBean.getNRIOFACCESSID())
                && !TextUtils.isEmpty(repairInfoBean.getNRIADDRESS()) && repairInfoBean.getCAID()!=0
                && repairInfoBean.getSHDID()!=0&&isShow) {
            tv_reairing_next.setEnabled(true);
        } else {
            tv_reairing_next.setEnabled(false);
        }
    }

    /**
     * 设置弹框
     *
     * @param showMes  要显示的提示语
     * @param textView 当前控件
     */
    private void setDialog(String showMes, final List<CommonBean> data, final TextView textView) {
        if (data != null && data.size() > 0) {
            final CustomDialogListview dialog_sex = new CustomDialogListview(this, showMes, data, textView.getText().toString());
            dialog_sex.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    textView.setText(data.get(position).getDes());
                    dialog_sex.dismiss();
                    if (tv_select_area == textView) {//区域
                        repairInfoBean.setCAID(Integer.parseInt(data.get(position).getId()));
                        Log.e("教学楼",data.get(position).getId());
                        getFloorListById(data.get(position).getId());
                    } else if (tv_select_floornum == textView) {//楼号
                        repairInfoBean.setSHDID(Integer.parseInt(data.get(position).getId()));
                    } else if (tv_question_type_content == textView) {//故障现象id
                        repairInfoBean.setNRIQUESTIONTYPE(Integer.parseInt(data.get(position).getId()));
                    }
                    getViewData();
                }
            });
            dialog_sex.show();
        } else {
            ToastManager.getInstance().showToast(AddRepairActivity.this, "暂无数据");
        }

    }
}
