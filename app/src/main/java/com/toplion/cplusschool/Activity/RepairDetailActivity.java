package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.util.AbDateUtil;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbJsonUtil;
import com.toplion.cplusschool.Bean.RepairDetailBean;
import com.toplion.cplusschool.Bean.RepairInfoBean;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CustMenuCancledialog;
import com.toplion.cplusschool.widget.CustMenudialog;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 报修信息详情
 * @author wang
 */
public class RepairDetailActivity extends BaseActivity {
    private AbHttpUtil mAbHttpUtil = null;
    private TextView tv_pj;
    private ImageView iv_repair_confirm_back;//返回
    private TextView tv_repair_number;//报修单号
    private TextView tv_edit;//编辑
    private TextView tv_cancle;//撤销
    private TextView tv_cd;//催单
    private TextView tv_repair_state;//目前状态
    private TextView tv_repair_date;//报修时间
    private TextView tv_repair_confirm_question;//问题类型
    private TextView tv_repair_confirm_equipment;//设备类型
    private TextView tv_repair_confirm_internet;//上网方式
    private TextView tv_repair_confirm_address;//故障地点
    private TextView tv_repair_confirm_internet_near;//周围上网网速
    private TextView tv_repair_confirm_description;//故障描述
    private TextView tv_repair_confirm_person_name;//报修人姓名
    private TextView tv_repair_confirm_person_phone;//报修人电话
    private int width, height;
    private int rId = 0;//报修id
    private RepairInfoBean repairInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_confirm);
        init();
    }

    @Override
    public void init() {
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        width = (int) (BaseApplication.ScreenWidth * 0.8);
        height = (int) (BaseApplication.ScreenWidth * 0.5);
        rId = getIntent().getIntExtra("rId", 0);
        tv_pj = (TextView) findViewById(R.id.tv_pj);
        iv_repair_confirm_back = (ImageView) findViewById(R.id.iv_repair_confirm_back);
        tv_repair_number = (TextView) findViewById(R.id.tv_repair_number);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_cd = (TextView) findViewById(R.id.tv_cd);
        tv_repair_state = (TextView) findViewById(R.id.tv_repair_state);
        tv_repair_date = (TextView) findViewById(R.id.tv_repair_date);
        tv_repair_confirm_question = (TextView) findViewById(R.id.tv_repair_confirm_question);
        tv_repair_confirm_equipment = (TextView) findViewById(R.id.tv_repair_confirm_equipment);
        tv_repair_confirm_address = (TextView) findViewById(R.id.tv_repair_confirm_address);
        tv_repair_confirm_internet_near = (TextView) findViewById(R.id.tv_repair_confirm_internet_near);
        tv_repair_confirm_internet = (TextView) findViewById(R.id.tv_repair_confirm_internet);
        tv_repair_confirm_description = (TextView) findViewById(R.id.tv_repair_confirm_description);
        tv_repair_confirm_person_name = (TextView) findViewById(R.id.tv_repair_confirm_person_name);
        tv_repair_confirm_person_phone = (TextView) findViewById(R.id.tv_repair_confirm_person_phone);
        getDetailById(rId);
        setListener();
    }

    //获取报修详情
    private void getDetailById(int rId) {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getRepairInfoById") + Constants.BASEPARAMS + "&repairId=" + rId;
        abHttpUtil.get(url, new CallBackParent(RepairDetailActivity.this, getResources().getString(R.string.loading), "getRepairInfoById") {
            @Override
            public void Get_Result(String result) {
                RepairDetailBean repairDetailBean = AbJsonUtil.fromJson(result, RepairDetailBean.class);
                if (repairDetailBean != null && repairDetailBean.getData() != null) {
                    repairInfoBean = repairDetailBean.getData();
                    tv_repair_number.setText(repairInfoBean.getOINUMBER() + "");
                    tv_repair_state.setText(getState(repairInfoBean.getOISTATUS()));
                    tv_repair_date.setText(AbDateUtil.getStringByFormat(repairInfoBean.getOICREATETIME(), "yyyy-MM-dd HH:mm:ss"));
                    tv_repair_confirm_question.setText(repairInfoBean.getDXTITLE());
                    if(!TextUtils.isEmpty(repairInfoBean.getNRIFACILITYTYPE())){
                        String shebeiStr [] =repairInfoBean.getNRIFACILITYTYPE().split(";");
                        tv_repair_confirm_equipment.setText(shebeiStr[1]);
                        repairInfoBean.setNRIFACILITYTYPEID(shebeiStr[0]);
                    }
                    if(!TextUtils.isEmpty(repairInfoBean.getNRIINTERNETCASE())){
                        String zwsdStr [] =repairInfoBean.getNRIINTERNETCASE().split(";");
                        tv_repair_confirm_internet_near.setText(zwsdStr[1]);
                        repairInfoBean.setNRIINTERNETCASEID(zwsdStr[0]);
                    }
                    if(!TextUtils.isEmpty(repairInfoBean.getNRIOFACCESS())){
                        String swfsStr [] =repairInfoBean.getNRIOFACCESS().split(";");
                        tv_repair_confirm_internet.setText(swfsStr[1]);
                        repairInfoBean.setNRIOFACCESSID(swfsStr[0]);
                    }
                    tv_repair_confirm_address.setText(repairInfoBean.getADDRESS());
                    tv_repair_confirm_description.setText(repairInfoBean.getNRIFAULTDESCRIPTION());
                    tv_repair_confirm_person_name.setText(repairInfoBean.getOIRINAME());
                    tv_repair_confirm_person_phone.setText(repairInfoBean.getOIRIPHONE());
                }
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        //评价
        tv_pj.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final CustMenudialog dialog = new CustMenudialog(RepairDetailActivity.this, width, height);
                dialog.button1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        float speed = dialog.pjsu_start.getRating();
                        float taidu = dialog.fwtd_start.getRating();
                        float jieguo = dialog.fwjg_start.getRating();
                        float nengli = dialog.ywnl_start.getRating();
                        String content = dialog.et_star_content.getText().toString();
                        UpLoadPj(speed, taidu, jieguo,nengli,content);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        //编辑
        tv_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepairDetailActivity.this, AddRepairActivity.class);
                intent.putExtra("style", 1);
                intent.putExtra("repairInfoBean",repairInfoBean);
                startActivity(intent);
            }
        });
        //撤销
        tv_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getresult();
            }
        });
        //催单
        tv_cd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UpLoadCd(repairInfoBean.getOIRTIME()+"");
            }
        });
        //返回
        iv_repair_confirm_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                RepairDetailActivity.this.finish();
            }
        });
    }
    //报修单状态
    private String getState(int state) {
        if (state == 1 || state == 9) {
            tv_cd.setVisibility(View.VISIBLE);
            tv_pj.setVisibility(View.GONE);
            tv_edit.setVisibility(View.VISIBLE);
            tv_cancle.setVisibility(View.VISIBLE);
            return "待接单";
        } else if (state == 2) {
            tv_cd.setVisibility(View.VISIBLE);
            tv_pj.setVisibility(View.GONE);
            tv_edit.setVisibility(View.VISIBLE);
            tv_cancle.setVisibility(View.VISIBLE);
            return "待预约";
        } else if (state == 3) {
            tv_cd.setVisibility(View.VISIBLE);
            tv_pj.setVisibility(View.GONE);
            tv_edit.setVisibility(View.VISIBLE);
            tv_cancle.setVisibility(View.VISIBLE);
            return "待维修";
        } else if (state == 4) {
            tv_cd.setVisibility(View.GONE);
            tv_pj.setVisibility(View.VISIBLE);
            tv_edit.setVisibility(View.GONE);
            tv_cancle.setVisibility(View.GONE);
            return "待评价";
        } else if (state == 5 || state == 6) {
            tv_cd.setVisibility(View.GONE);
            tv_pj.setVisibility(View.GONE);
            tv_edit.setVisibility(View.GONE);
            tv_cancle.setVisibility(View.GONE);
            return "已完结";
        } else if (state == 8) {
            tv_cd.setVisibility(View.GONE);
            tv_pj.setVisibility(View.GONE);
            tv_edit.setVisibility(View.GONE);
            tv_cancle.setVisibility(View.GONE);
            return "已撤销";
        }
        return "";
    }

    /**
     * 催单
     * @param rtime 催单时间（可为空）
     */
    private void UpLoadCd(String rtime){
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("reminderRepair") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("repairId",rId);
        params.put("remindertime",rtime);
        abHttpUtil.post(url,params,new CallBackParent(RepairDetailActivity.this,getResources().getString(R.string.loading),"reminderRepair") {
            @Override
            public void Get_Result(String result) {
                try{
                    JSONObject object = new JSONObject(result);
                    String msg = object.getString("msg");
                    setTiShi(msg);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 提交评价
     */
    private void UpLoadPj(float speed, float td, float jg,float nl,String str) {
        AbDialogUtil.showProgressDialog(RepairDetailActivity.this, 0, "正在提交...");
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("evaluateRepair") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        /**
         * repairId	string	工单Id
         serviceSpeed	Double	服务速度
         serviceAttitude	Double	服务态度
         serviceEffect	Double	服务效果
         operationalCapacity	Double	业务能力
         content	String	评价内容
         */
        params.put("repairId", rId);
        params.put("serviceSpeed", speed + "");
        params.put("serviceAttitude", td + "");
        params.put("serviceEffect", jg + "");
        params.put("operationalCapacity",nl+"");
        params.put("content",str);
        mAbHttpUtil.post(url, params, new CallBackParent(RepairDetailActivity.this,getResources().getString(R.string.loading),"saveEvaluateInfo") {
            @Override
            public void Get_Result(String result) {
                try{
                    JSONObject object = new JSONObject(result);
                    String msg = object.getString("msg");
                    setTiShi(msg);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 提交撤销
     */
    private void Uploaddelete(String cancelCause) {
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("cancelRepairById") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("repairId", rId);
        params.put("repealing", cancelCause);
        mAbHttpUtil.post(url, params, new CallBackParent(RepairDetailActivity.this, getResources().getString(R.string.loading), "cancelRepairById") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String msg = object.getString("msg");
                    setTiShi(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 撤销弹窗
     */
    private void getresult() {
        final CustMenuCancledialog cancledialog = new CustMenuCancledialog(RepairDetailActivity.this, width, height);
        cancledialog.bt_repair_cancle_commit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uploaddelete(cancledialog.radioButton.getText().toString());
                cancledialog.dismiss();
            }
        });
        cancledialog.show();
    }
    //成功后的提示框
    private void setTiShi(String msg){
        final CommDialog dialog = new CommDialog(RepairDetailActivity.this);
        dialog.CreateDialogOnlyOk("提示", "返回", msg + "", new CommDialog.CallBack() {
            @Override
            public void isConfirm(boolean flag) {
                // 判断点击按钮
                if (flag) {
                    setResult(RESULT_OK);
                    finish();
                    dialog.cancelDialog();
                }
            }
        });
    }
}
