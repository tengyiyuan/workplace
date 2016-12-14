package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.global.AbActivityManager;
import com.ab.http.AbHttpUtil;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.toplion.cplusschool.Bean.RepairInfoBean;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.CommDialog.CallBack;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.StringUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.View.ChangeIconEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 填写报修人信息
 *
 * @author wang
 */
public class RepairPersonInfoActivity extends BaseActivity {
    private ImageView iv_repair_person_back;// 返回
    private EditText et_repair_person_name;// 报修人姓名
    private EditText et_repair_person_phone;// 报修人电话
    private ChangeIconEditText et_repair_validate_input;// 验证码输入框
    private Button bt_repair_validate_image_see;// 看不清
    private DownTask downTask; // DOWNTASK
    private ImageView iv_repair_validate_image;// 验证码显示
    private AnimationDrawable animationDrawable; // 等待Loading
    private JSONObject obj;// 验证码返回数据
    private boolean isRight = false;// 验证码输入是否正确
    private TextView tv_repair_person_next;// 提交
    private AbHttpUtil abHttpUtil;//网络请求工具
    private SharePreferenceUtils share;//
    private AbTask abTask;//
    private AbTaskItem taskItem;//
    private RepairInfoBean repairInfoBean = new RepairInfoBean();//上传服务器的数据
    private int style = 0;//1：修改数据 2：新建数据
    private String url = null;//请求路径
    public static RepairPersonInfoActivity instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_person_info);
        instance = this;
        init();
        getData();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);//初始化
        share = new SharePreferenceUtils(this);
        repairInfoBean = (RepairInfoBean) getIntent().getSerializableExtra("repairInfoBean");
        style = getIntent().getIntExtra("style", 0);
        iv_repair_person_back = (ImageView) findViewById(R.id.iv_repair_person_back);
        et_repair_person_name = (EditText) findViewById(R.id.et_repair_person_name);
        et_repair_person_phone = (EditText) findViewById(R.id.et_repair_person_phone);
        tv_repair_person_next = (TextView) findViewById(R.id.tv_repair_person_next);
        tv_repair_person_next.setEnabled(false);
        et_repair_validate_input = (ChangeIconEditText) findViewById(R.id.et_repair_validate_input);
        iv_repair_validate_image = (ImageView) findViewById(R.id.iv_repair_validate_image);
        bt_repair_validate_image_see = (Button) findViewById(R.id.bt_repair_validate_image_see);
        // 图片loading.....
        bt_repair_validate_image_see.setText(Html.fromHtml(getResources().getString(R.string.see_no)));
        if (style == 1) {
            et_repair_person_name.setText(repairInfoBean.getOIRINAME() + "");
            et_repair_person_phone.setText(repairInfoBean.getOIRIPHONE() + "");
        }else{
            String rname = share.getString("rName" + share.getString("username", ""), "");//联系人姓名
            String rphone = share.getString("rPhone" + share.getString("username", ""), "");//联系人电话
            et_repair_person_name.setText(rname);
            et_repair_person_phone.setText(rphone);
            repairInfoBean.setOIRINAME(rname + "");
            repairInfoBean.setOIRIPHONE(rphone + "");
        }
        setListener();
    }

    //加载数据
    @Override
    protected void getData() {
        super.getData();
        initLoading();
    }

    // 加载验证码
    private void initLoading() {
        // 图片loading.....
        animationDrawable = (AnimationDrawable) iv_repair_validate_image.getDrawable();
        animationDrawable.start();
        bt_repair_validate_image_see.performClick(); // 请求验证码

    }

    /**
     * 验证验证码
     *
     * @param inputNum 输入的验证码
     */
    private void checkNum(final String inputNum) {
        abTask = AbTask.newInstance();
        taskItem = new AbTaskItem();
        taskItem.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
                String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("validateChkNum") + Constants.BASEPARAMS;
                Log.e("yanzhengmayanzheng", url);
                // 输入长度等于4时，验证验证码
                Map<String, String> params = new HashMap<String, String>();
                params.put("chkNum", ReturnUtils.encode(inputNum));
                obj = new JSONObject();
                obj = HttpUtils.httpClientPost(url, params);
            }

            @Override
            public void update() {
                super.update();
                // 验证验证码
                try {
                    obj = new JSONObject(obj.getString("result"));
                    String code = obj.getString("code");
                    String message = obj.getString("msg");
                    Log.e("进来了", obj + "");
                    // 验证码成功匹配
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        isRight = true;
                        et_repair_validate_input.setClearIconVisible(true, true);

                        validateInput();
                    } else {
                        // 验证码匹配错误
                        ToastManager.getInstance().showToast(RepairPersonInfoActivity.this, message);
                        // 刷新验证码
                        bt_repair_validate_image_see.performClick();
                        // 原输入验证码清空
                        et_repair_validate_input.setClearIconVisible(true, false);
                        et_repair_validate_input.setText("");
                        isRight = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        abTask.execute(taskItem);
    }

    /**
     * 报修信息提交服务器
     */
    private void saveData() {
        abTask = AbTask.newInstance();
        taskItem = new AbTaskItem();
        taskItem.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
                String url = null;
                Map<String, String> params = new HashMap<String, String>();
                if (style == 2) {
                    url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("saveRepair") + Constants.BASEPARAMS;
                } else if (style == 1) {
                    url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("editRepairInfo") + Constants.BASEPARAMS;
                    params.put("repairId", repairInfoBean.getOIID() + "");
                }
                params.put("deviceType", repairInfoBean.getNRIFACILITYTYPEID() + "");//设备类型
                params.put("accessFunction", repairInfoBean.getNRIINTERNETCASEID() + "");//上网方式
                params.put("netAround", repairInfoBean.getNRIOFACCESSID() + "");//上网速度
                params.put("faultArea", repairInfoBean.getCAID() + "");//区域
                params.put("faultFloor", repairInfoBean.getSHDID() + "");//教学楼
                params.put("faultRoom", repairInfoBean.getNRIADDRESS() + "");//房间号
                params.put("faultAppearance", repairInfoBean.getNRIQUESTIONTYPE() + "");//故障现象
                params.put("faultDes", repairInfoBean.getNRIFAULTDESCRIPTION() + "");//故障描述
                params.put("faultContact", repairInfoBean.getOIRINAME() + "");//报修人
                params.put("faultPhone", repairInfoBean.getOIRIPHONE() + "");//报修电话
                params.put("stuNo", share.getString("ROLE_ID", ""));//学号
                Log.e("save==========>", url);
                obj = new JSONObject();
                obj = HttpUtils.httpClientPost(url, params);
            }

            @Override
            public void update() {
                super.update();
                try {
                    obj = new JSONObject(obj.getString("result"));
                    String code = obj.getString("code");
                    String message = obj.getString("msg");
                    Log.e("result==========>", obj + "");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {//成功
                        if (style == 1) {
                            Constants.isRefresh = true;
                        }
                        share.put("rName" + share.getString("username", ""), repairInfoBean.getOIRINAME());//联系人姓名
                        share.put("rPhone" + share.getString("username", ""), repairInfoBean.getOIRIPHONE());//联系人电话
                        final CommDialog dialog = new CommDialog(RepairPersonInfoActivity.this);
                        dialog.CreateDialogOnlyOk("提交成功", "返回", message + "", new CallBack() {
                            @Override
                            public void isConfirm(boolean flag) {
                                // 判断点击按钮
                                if (flag) {
                                    dialog.cancelDialog();
                                    if (style == 2) {
                                        AbActivityManager.getInstance().finishActivity(RepairQuestionListActivity.class);
                                        AbActivityManager.getInstance().finishActivity(RepairQuestionDetailActivity.class);
                                        Intent intent = new Intent(RepairPersonInfoActivity.this, MyRepairListActivity.class);
                                        startActivity(intent);
                                    } else if (style == 1) {
                                        AbActivityManager.getInstance().finishActivity(RepairDetailActivity.class);
                                    }
                                    AbActivityManager.getInstance().finishActivity(AddRepairActivity.class);
                                    finish();
                                }
                            }
                        });
                    } else {
                        ToastManager.getInstance().showToast(RepairPersonInfoActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        abTask.execute(taskItem);
    }

    //设置点击事件
    @Override
    protected void setListener() {
        // 返回
        iv_repair_person_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 输入姓名
        et_repair_person_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                repairInfoBean.setOIRINAME(s.toString() + "");
                //输入结束后验证是否输入完整
                validateInput();
            }
        });
        et_repair_person_phone.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    String str = et_repair_person_phone.getText().toString();
                    if (str.length() != 11) {
                        ToastManager.getInstance().showToast(RepairPersonInfoActivity.this, "请正确输入手机号码");
                    }
                }
            }
        });

        // 输入电话验证
        et_repair_person_phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                repairInfoBean.setOIRIPHONE(s.toString() + "");
                validateInput();
            }
        });
        //验证码输入验证
        et_repair_validate_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 获取输入的验证码
                String input = et_repair_validate_input.getText().toString();
                // 验证码长度等于4的时候，开始验证输入的验证码
                if (input.length() == 4) {
                    checkNum(input);
                } else {
                    isRight = false;
                    et_repair_validate_input.setClearIconVisible(true, false);
                    validateInput(); //输入结束后验证是否输入完整
                }
            }
        });
        // 提交
        tv_repair_person_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(et_repair_person_name.getText().toString())){
                    if (StringUtils.isMobile(repairInfoBean.getOIRIPHONE().trim()) && StringUtils.isMobile(et_repair_person_phone.getText().toString().trim())) {
                        repairInfoBean.setOIRINAME(et_repair_person_name.getText().toString());
                        repairInfoBean.setOIRIPHONE(et_repair_person_phone.getText().toString());
                        saveData();
                    } else {
                        ToastManager.getInstance().showToast(RepairPersonInfoActivity.this, "手机号不正确,请重新输入");
                    }
                }else{
                    ToastManager.getInstance().showToast(RepairPersonInfoActivity.this, "请输入联系人姓名");
                }
            }
        });
    }


    // 验证码使用
    // 点击按钮时下载图片
    public void downImage(View v) {
        downTask = new DownTask();
        downTask.execute("");
    }

    // 定义一个异步任务，实现图片的下载
    // 第二个泛型是进度值的类型
    class DownTask extends AsyncTask<String, Integer, byte[]> {
        // 该方法由主线程执行，在doInBackground方法之前执行
        // doInBackground方法需要接收下载的图片的网址
        @Override
        protected byte[] doInBackground(String... params) {
            Log.e("yanzhengma", Constants.BASE_URL + "rid=" + ReturnUtils.encode("getChkNum"));
            return HttpUtils.httpClientImage(Constants.BASE_URL, "rid=" + ReturnUtils.encode("getChkNum") + Constants.BASEPARAMS);
        }

        @Override
        protected void onPostExecute(byte[] result) {
            super.onPostExecute(result);
            if (result != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
                iv_repair_validate_image.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 检测是否输入完整
     */

    private void validateInput() {
        if (!TextUtils.isEmpty(repairInfoBean.getOIRINAME().trim()) && !TextUtils.isEmpty(repairInfoBean.getOIRIPHONE().trim()) && isRight) {
            tv_repair_person_next.setEnabled(true);
        } else {
            tv_repair_person_next.setEnabled(false);
        }
    }
}
