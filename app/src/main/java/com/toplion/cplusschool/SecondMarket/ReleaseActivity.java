package com.toplion.cplusschool.SecondMarket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.toplion.cplusschool.Activity.BaseActivity;
import com.toplion.cplusschool.Activity.CommonWebViewActivity;
import com.toplion.cplusschool.Bean.MarketBean;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.PhotoBrowser.photo.ImagePagerActivity;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SelectPicUtil;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.StringUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.Utils.Tools;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CameraPopWindow;
import com.toplion.cplusschool.widget.ListPopWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang
 * on 2016/10/11.
 *
 * @des 发布界面
 */

public class ReleaseActivity extends BaseActivity {
    private ImageView iv_release_return;//返回键
    private TextView tv_release_title;//标题
    private EditText et_release_title;//商品标题
    private EditText et_release_describe;//描述
    private LinearLayout ll_release_imgleft;//显示上传的图片
    private ImageView iv_release_addimg;//添加图片按钮
    private EditText et_release_price;//价格
    private TextView tv_release_price_des;
    private LinearLayout ll_release_danwei;//结算方式
    private TextView tv_release_danwei;//单位
    private View v_release_dw_line;
    private LinearLayout rl_release_type;//选择分类
    private TextView tv_release_type;//类别
    private EditText et_release_phone;//手机号
    private EditText et_release_wx;//微信
    private EditText et_release_qq;//qq
    private Button btn_release_confirm;//确认发布
    private TextView tv_release_xieyi;//发布者协议
    private LinearLayout ll_parentview;
    private LinearLayout ll_release_price;
    private View v_release_line;
    private int module = 0;//1-失物招领  2-二手市场  3-兼职招聘
    private int reltype = 0;//发布类型（ 1：寻物、出售、工作 2：招领、求购、求职）

    private int maxImg = 3;//图片最大显示数量

    private int imgWidth = 0;//imgview宽
    private int imgHeight = 0;//imgview高
    private int outputX = 500;// 图片裁剪宽
    private int outputY = 500;//图片裁剪高
    private List<String> idList;
    private List<String> imgList;
    private String result;   //上传图片返回结果
    private AbHttpUtil abHttpUtil;
    private List<Map<String, String>> typeList;
    private String typeId = "";//类型id
    private String danweiId = "0";//工资单位id
    private SharePreferenceUtils share;
    private MarketBean markbean;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release);
        init();
    }

    @Override
    protected void init() {
        super.init();
        abHttpUtil = AbHttpUtil.getInstance(this);
        share = new SharePreferenceUtils(this);
        module = getIntent().getIntExtra("module", 0);
        reltype = getIntent().getIntExtra("reltype", 0);
        markbean = (MarketBean) getIntent().getSerializableExtra("markbean");
        ll_parentview = (LinearLayout) findViewById(R.id.ll_parentview);
        iv_release_return = (ImageView) findViewById(R.id.iv_release_return);
        tv_release_title = (TextView) findViewById(R.id.tv_release_title);
        et_release_title = (EditText) findViewById(R.id.et_release_title);
        et_release_describe = (EditText) findViewById(R.id.et_release_describe);
        ll_release_imgleft = (LinearLayout) findViewById(R.id.ll_release_imgleft);
        ll_release_price = (LinearLayout) findViewById(R.id.ll_release_price);
        tv_release_price_des = (TextView) findViewById(R.id.tv_release_price_des);
        v_release_line = findViewById(R.id.v_release_line);
        iv_release_addimg = (ImageView) findViewById(R.id.iv_release_addimg);
        et_release_price = (EditText) findViewById(R.id.et_release_price);
        ll_release_danwei = (LinearLayout) findViewById(R.id.ll_release_danwei);
        tv_release_danwei = (TextView) findViewById(R.id.tv_release_danwei);
        v_release_dw_line = findViewById(R.id.v_release_dw_line);
        rl_release_type = (LinearLayout) findViewById(R.id.rl_release_type);
        tv_release_type = (TextView) findViewById(R.id.tv_release_type);
        et_release_phone = (EditText) findViewById(R.id.et_release_phone);
        et_release_wx = (EditText) findViewById(R.id.et_release_wx);
        et_release_qq = (EditText) findViewById(R.id.et_release_qq);
        btn_release_confirm = (Button) findViewById(R.id.btn_release_confirm);
        tv_release_xieyi = (TextView) findViewById(R.id.tv_release_xieyi);
        setTitle();
        idList = new ArrayList<String>();
        imgList = new ArrayList<String>();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        outputX = BaseApplication.ScreenWidth;
        outputY = BaseApplication.ScreenWidth;
        imgWidth = screenWidth / 3 - 45;//计算屏幕的宽度   平分3等份再减去两端的间隔就是ImageView的宽高
        imgHeight = imgWidth;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgWidth, imgHeight);
        layoutParams.topMargin = 15;
        layoutParams.bottomMargin = 5;
        iv_release_addimg.setLayoutParams(layoutParams);
        if (markbean != null) {
            btn_release_confirm.setText("确认修改");
            if (markbean.getIRIURL() != null && markbean.getIRIURL().size() > 0
                    && markbean.getID() != null && markbean.getID().size() > 0) {
                for (int i = 0; i < markbean.getIRIURL().size(); i++) {
                    int imgId = Integer.parseInt(markbean.getID().get(i));
                    String url = markbean.getIRIURL().get(i);
                    addImagView(imgId, url);
                }
            }
            if (module == 2) {
                typeId = markbean.getCIID()+"";
            }else if(module == 3){
                danweiId = markbean.getUIID()+"";
            }
            et_release_title.setText(markbean.getAUITITLE());
            et_release_describe.setText(markbean.getAUICONTENT());
            et_release_price.setText(markbean.getAUIPRICE() + "");
            et_release_phone.setText(markbean.getAUIPHONE() + "");
            et_release_wx.setText(markbean.getAUIWEIXIN() + "");
            et_release_qq.setText(markbean.getAUIQQ() + "");
            if (markbean.getCINAME() != null) {
                tv_release_type.setText(markbean.getCINAME());
            }
            if (markbean.getUINAME() != null) {
                tv_release_danwei.setText(markbean.getUINAME());
            }
        } else {
            btn_release_confirm.setText("确认发布");
        }
        setListener();
        if (module == 2 || module == 3) {
            getData();
        }
    }

    private void setTitle() {
        String str = null;
        if (module == 1) {
            if (reltype == 1) {
                str = "失物发布";
            } else if (reltype == 2) {
                str = "招领发布";
            }
            typeId = "1";
            et_release_describe.setHint("描述一下你的物品吧");
            ll_release_price.setVisibility(View.GONE);
            v_release_line.setVisibility(View.GONE);
            rl_release_type.setVisibility(View.GONE);
            ll_release_danwei.setVisibility(View.GONE);

        } else if (module == 2) {
            if (reltype == 1) {
                str = "商品发布";
            } else if (reltype == 2) {
                str = "求购发布";
            }
            rl_release_type.setVisibility(View.VISIBLE);
            ll_release_danwei.setVisibility(View.GONE);
        } else if (module == 3) {
            if (reltype == 1) {
                str = "招聘发布";
            } else if (reltype == 2) {
                str = "求职发布";
            }
            typeId = "2";
            et_release_describe.setHint("描述一下你的需求吧");
            tv_release_price_des.setText("工资待遇");
            et_release_price.setHint("请输入具体金额");
            v_release_line.setVisibility(View.GONE);
            rl_release_type.setVisibility(View.GONE);
            ll_release_danwei.setVisibility(View.VISIBLE);
        }
        tv_release_title.setText(str);
    }

    @Override
    protected void getData() {
        super.getData();
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getTypeByModule") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("module", module);
        if (module == 2) {
            params.put("type", 1);
        } else if (module == 3) {
            params.put("type", 2);
        }
        abHttpUtil.post(url, params, new CallBackParent(this, getResources().getString(R.string.loading)) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    typeList = new ArrayList<Map<String, String>>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Map<String, String> map = new HashMap<String, String>();
                        JSONObject json = jsonArray.getJSONObject(i);
                        map.put("id", json.getInt("CIID") + "");
                        map.put("des", json.getString("CINAME"));
                        typeList.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //发布商品
    private void release() {
        String photourls = "";
        if (idList != null && idList.size() > 0) {
            StringBuffer sbf = new StringBuffer();
            for (String str : idList) {
                sbf.append(str);
                sbf.append(",");
            }
            sbf.deleteCharAt(sbf.length() - 1);
            photourls = sbf.toString();
        }
        String rid = "";
        if (markbean != null) {
            rid = "updReleaseInfo";
        } else {
            rid = "addReleaseInfo";
        }
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode(rid) + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        if (markbean != null) {
            params.put("relid", markbean.getAUIID() + "");
        }
        params.put("username", share.getString("ROLE_ID", ""));
        params.put("reltitle", et_release_title.getText().toString().trim());
        params.put("relcontent", et_release_describe.getText().toString());
        params.put("relprice", et_release_price.getText().toString().trim());
        params.put("relcontacts", share.getString("ROLE_USERNAME", ""));
        params.put("reladdress", share.getString("schoolName", ""));
        params.put("relstatus", 2);
        params.put("reltype", reltype);
        params.put("goodtype", typeId);
        params.put("relphone", et_release_phone.getText().toString().trim());
        params.put("relqq", et_release_qq.getText().toString().trim());
        params.put("relwechat", et_release_wx.getText().toString().trim());
        params.put("photourls", photourls);
        params.put("priceUnit", danweiId);
        params.put("module", module);
        abHttpUtil.post(url, params, new CallBackParent(this, getResources().getString(R.string.loading), "addReleaseInfo") {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String code = json.getString("code");
                    String message = json.getString("msg");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        final CommDialog dialog = new CommDialog(ReleaseActivity.this);
                        dialog.CreateDialogOnlyOk("提交成功", "返回", message + "", new CommDialog.CallBack() {
                            @Override
                            public void isConfirm(boolean flag) {
                                // 判断点击按钮
                                if (flag) {
                                    dialog.cancelDialog();
                                    Intent intent = new Intent();
                                    intent.putExtra("flag", "true");
                                    //设置返回数据
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }
                        });
                    } else {
                        ToastManager.getInstance().showToast(ReleaseActivity.this, "添加失败");
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
        //返回键
        iv_release_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("flag", "false");
                //设置返回数据
                setResult(RESULT_OK, intent);
                HideKeyboard(et_release_title);
                finish();
            }
        });
        //结算方式--兼职招聘
        ll_release_danwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard(et_release_title);
                if (typeList != null && typeList.size() > 0) {
                    final ListPopWindow listPopWindow = new ListPopWindow(ReleaseActivity.this, typeList, ll_parentview);
                    listPopWindow.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            listPopWindow.setSelect(position);
                            listPopWindow.tv_pop_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    danweiId = typeList.get(position).get("id");
                                    listPopWindow.dismiss();
                                    tv_release_danwei.setText(typeList.get(position).get("des"));
                                    if (typeList.get(position).get("des").equals("面议")) {
                                        ll_release_price.setVisibility(View.GONE);
                                        v_release_dw_line.setVisibility(View.GONE);
                                    } else {
                                        ll_release_price.setVisibility(View.VISIBLE);
                                        v_release_dw_line.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        //选择分类
        rl_release_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard(et_release_title);
                if (typeList != null && typeList.size() > 0) {
                    final ListPopWindow listPopWindow = new ListPopWindow(ReleaseActivity.this, typeList, ll_parentview);
                    listPopWindow.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            listPopWindow.setSelect(position);
                            listPopWindow.tv_pop_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    typeId = typeList.get(position).get("id");
                                    listPopWindow.dismiss();
                                    tv_release_type.setText(typeList.get(position).get("des"));
                                }
                            });
                        }
                    });
                }else{
                    ToastManager.getInstance().showToast(ReleaseActivity.this,"暂无分类信息");
                }
            }
        });
        //添加图片按钮
        iv_release_addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard(et_release_title);
                final CameraPopWindow cameraPopWindow = new CameraPopWindow(ReleaseActivity.this, ll_parentview);
                cameraPopWindow.setBtnCameraOnlickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectPicUtil.getByCamera(ReleaseActivity.this, true);
                        overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                        cameraPopWindow.dismiss();
                    }
                });
                cameraPopWindow.setBtnPictureOnlickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectPicUtil.getByAlbum(ReleaseActivity.this);
                        overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                        cameraPopWindow.dismiss();
                    }
                });
            }
        });
        btn_release_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_release_title.getText().toString().trim())) {
                    if (module == 2 || (module == 3 && !tv_release_danwei.getText().equals("面议"))) {
                        if (TextUtils.isEmpty(et_release_price.getText().toString().trim())) {
                            ToastManager.getInstance().showToast(ReleaseActivity.this, "请输入金额");
                            return;
                        }
                        if (et_release_price.getText().toString().trim().equals(".")) {
                            ToastManager.getInstance().showToast(ReleaseActivity.this, "请输入价格");
                            return;
                        }
                    }
                    if (module == 3 && TextUtils.isEmpty(tv_release_danwei.getText().toString())) {
                        ToastManager.getInstance().showToast(ReleaseActivity.this, "请选择结算方式");
                        return;
                    }
                    if (module == 2) {
                        if (TextUtils.isEmpty(tv_release_type.getText().toString().trim())) {
                            ToastManager.getInstance().showToast(ReleaseActivity.this, "请选择分类");
                            return;
                        }
                    }

                    if (TextUtils.isEmpty(et_release_phone.getText().toString().trim())) {
                        ToastManager.getInstance().showToast(ReleaseActivity.this, "手机号不能为空");
                        return;
                    } else {
                        if (!StringUtils.isMobile(et_release_phone.getText().toString().trim())) {
                            ToastManager.getInstance().showToast(ReleaseActivity.this, "请输入正确手机号");
                            return;
                        }
                        final CommDialog commDialog = new CommDialog(ReleaseActivity.this);
                        commDialog.CreateDialog("发布", "警告", getResources().getString(R.string.release_tishi), ReleaseActivity.this, new CommDialog.CallBack() {
                            @Override
                            public void isConfirm(boolean flag) {
                                commDialog.cancelDialog();
                                if (flag) {
                                    release();
                                }
                            }
                        });
                    }
                } else {
                    ToastManager.getInstance().showToast(ReleaseActivity.this, "标题不能为空");
                }
            }
        });
        //发布者协议
        tv_release_xieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReleaseActivity.this, CommonWebViewActivity.class);
                intent.putExtra("url", "http://123.233.121.17:12100/help/PublisherAgreement.html");
                intent.putExtra("title", "发布者相关协议");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Uri uri = SelectPicUtil.onActivityResultUri(this, requestCode, resultCode, data, outputX, outputY, 1, 1);
        if (uri != null && uri.getPath() != null) {
            try {
                double size = (double) new File(uri.getPath()).length() / 1024 / 1024;
                Log.e("fileSize", size + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            uploadImage(new File(uri.getPath()));
        }
    }

    //上传图片
    private void uploadImage(final File file) {
        AbDialogUtil.showProgressDialog(ReleaseActivity.this, 0, "图片正在上传...");
        AbTask task = AbTask.newInstance();
        AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
//                String url = share.getString("uploadUrl","")+"upload_image.php?schoolCode=sdjzu"+Constants.BASEPARAMS;
                String url = share.getString("uploadUrl", "") + "upload_image.php?ss=ss" + Constants.BASEPARAMS;
                result = HttpUtils.uploadFile(url, file);
                Log.e("result", result + ":" + file.getName());
            }

            @Override
            public void update() {
                super.update();
                try {
                    AbDialogUtil.removeDialog(ReleaseActivity.this);
                    JSONObject json = new JSONObject(result);
                    String code = json.getString("code");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        json = new JSONObject(json.getString("data"));
                        int imgId = json.getInt("IRIID");
                        String imgUrl = json.getString("IRIURL");
                        addImagView(imgId, imgUrl);
                    } else {
                        ToastManager.getInstance().showToast(ReleaseActivity.this, "上传失败,请重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(ReleaseActivity.this, "上传失败,请重试");
                }
            }
        });
        task.execute(item);
    }

    private void addImagView(int ivId, String url) {
        if (url != null) {
            FrameLayout relativeLayout = new FrameLayout(this);
            relativeLayout.setId(ivId);
            final ImageView imageView1 = new ImageView(this);
            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(imgWidth, imgHeight);
            params1.topMargin = 15;
            params1.rightMargin = 25;
            params1.bottomMargin = 5;
            params1.gravity = Gravity.CENTER_VERTICAL;
            imageView1.setLayoutParams(params1);
            imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView1.setId(ivId);
            imageView1.setContentDescription(url.replace("thumb/", ""));
            Log.e("url", url);
            AbImageLoader.getInstance(ReleaseActivity.this).display(imageView1, url);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < imgList.size(); i++) {
                        if (imgList.get(i).equals(v.getContentDescription())) {
                            Intent intent = new Intent(ReleaseActivity.this, ImagePagerActivity.class);
                            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) imgList);
                            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, i);
                            intent.putExtra("isShowSave", "0");
                            startActivity(intent);
                            break;
                        }
                    }
                }
            });
            relativeLayout.addView(imageView1);
            ImageView imageView2 = new ImageView(this);
            imageView2.setId(ivId);
            imageView2.setImageResource(R.mipmap.release_del);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.TOP | Gravity.RIGHT;
            imageView2.setLayoutParams(params);
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_release_imgleft.removeView(ll_release_imgleft.findViewById(v.getId()));
                    imgList.remove(imageView1.getContentDescription());
                    idList.remove(v.getId() + "");
                    if (ll_release_imgleft.getChildCount() < maxImg) {
                        iv_release_addimg.setVisibility(View.VISIBLE);
                    }
                }
            });
            relativeLayout.addView(imageView2);
            RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, imgHeight + 30);
            relativeLayout.setLayoutParams(rParams);
            ll_release_imgleft.addView(relativeLayout);
            imgList.add(url.replace("thumb/", ""));
            idList.add(ivId + "");
            if (ll_release_imgleft.getChildCount() >= maxImg) {
                iv_release_addimg.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("flag", "false");
            //设置返回数据
            setResult(RESULT_OK, intent);
            finish();
        }
        return false;
    }

    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }
}
