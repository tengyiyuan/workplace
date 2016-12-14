package com.toplion.cplusschool.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbImageUtil;
import com.toplion.cplusschool.Adapter.LocalSelectAdapter;
import com.toplion.cplusschool.Bean.CityBean;
import com.toplion.cplusschool.Bean.CityMapBean;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.Function;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SelectPicUtil;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.TimeUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.Utils.Tools;
import com.toplion.cplusschool.dao.CallBackParent;
import com.toplion.cplusschool.widget.CameraPopWindow;
import com.toplion.cplusschool.widget.TimePickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by toplion
 * on 2016/10/18.
 *
 * @des 个人信息展示接界面
 */
public class PersonInfoActivity extends BaseActivity {
    private ImageView iv_pinfo_back;//返回键
    private LinearLayout ll_pinfo_parent;
    private ImageView iv_my_icon;//头像
    private RelativeLayout rl_pinfo_nickname;//昵称
    private RelativeLayout rl_pinfo_sex;//性别
    private RelativeLayout rl_pinfo_brithday;//生日
    private RelativeLayout rl_pinfo_phone;//电话
    private RelativeLayout rl_pinfo_address;//地址
    private RelativeLayout rl_pinfo_suozaidi;//所在地
    private RelativeLayout rl_pinfo_grsm;//个人说明
    private TextView tv_pinfo_nickname;//
    private TextView tv_pinfo_sex;//
    private ImageView iv_pinfo_sex;
    private TextView tv_pinfo_age;//年龄
    private TextView tv_pinfo_xz;//星座
    private TextView tv_pinfo_phone;//
    private TextView tv_pinfo_zhuanye;
    private TextView tv_pinfo_major;//
    private TextView tv_pinfo_brithday;//
    private ImageView iv_pinfo_brithday;
    private TextView tv_pinfo_address;//
    private TextView tv_pinfo_nowaddress;//所在地
    private TextView tv_pinfo_grsm;//个人说明
    private int outputX = 100;// 图片裁剪宽
    private int outputY = 100;//图片裁剪高
    private SharePreferenceUtils share;
    private String result = "";//上传照片返回结果
    private int flag = 0;
    private StringBuffer hometown = new StringBuffer();
    private StringBuffer nowAddress = new StringBuffer();
    private TimePickerView timePickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info);
        init();
    }

    @Override
    protected void init() {
        super.init();
        share = new SharePreferenceUtils(this);
        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setRange(1969, 2100);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);

        iv_pinfo_back = (ImageView) findViewById(R.id.iv_pinfo_back);
        ll_pinfo_parent = (LinearLayout) findViewById(R.id.ll_pinfo_parent);
        iv_my_icon = (ImageView) findViewById(R.id.iv_my_icon);
        rl_pinfo_brithday = (RelativeLayout) findViewById(R.id.rl_pinfo_brithday);
        rl_pinfo_nickname = (RelativeLayout) findViewById(R.id.rl_pinfo_nickname);
        rl_pinfo_sex = (RelativeLayout) findViewById(R.id.rl_pinfo_sex);
        iv_pinfo_sex = (ImageView) findViewById(R.id.iv_pinfo_sex);
        rl_pinfo_phone = (RelativeLayout) findViewById(R.id.rl_pinfo_phone);
        rl_pinfo_address = (RelativeLayout) findViewById(R.id.rl_pinfo_address);
        rl_pinfo_suozaidi = (RelativeLayout) findViewById(R.id.rl_pinfo_suozaidi);
        rl_pinfo_grsm = (RelativeLayout) findViewById(R.id.rl_pinfo_grsm);
        tv_pinfo_grsm = (TextView) findViewById(R.id.tv_pinfo_grsm);

        tv_pinfo_nickname = (TextView) findViewById(R.id.tv_pinfo_nickname);
        tv_pinfo_sex = (TextView) findViewById(R.id.tv_pinfo_sex);
        tv_pinfo_age = (TextView) findViewById(R.id.tv_pinfo_age);
        tv_pinfo_xz = (TextView) findViewById(R.id.tv_pinfo_xz);
        tv_pinfo_nowaddress = (TextView) findViewById(R.id.tv_pinfo_nowaddress);

        tv_pinfo_phone = (TextView) findViewById(R.id.tv_pinfo_phone);
        tv_pinfo_zhuanye = (TextView) findViewById(R.id.tv_pinfo_zhuanye);
        tv_pinfo_major = (TextView) findViewById(R.id.tv_pinfo_major);
        tv_pinfo_brithday = (TextView) findViewById(R.id.tv_pinfo_brithday);
        iv_pinfo_brithday = (ImageView) findViewById(R.id.iv_pinfo_brithday);
        tv_pinfo_address = (TextView) findViewById(R.id.tv_pinfo_address);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.my_default_head);
        outputX = bitmap.getWidth();
        outputY = bitmap.getHeight();
        geCitytData();
        getData();
        setListener();
    }

    @Override
    protected void getData() {
        super.getData();
        loadHead(share.getString("HEADIMAGE", ""));
        tv_pinfo_nickname.setText(share.getString("NICKNAME", ""));
        String sex = "";
        if (share.getString("XB", "0").equals("1")) {
            sex = "男";
        } else if (share.getString("XB", "0").equals("2")) {
            sex = "女";
        }
        tv_pinfo_sex.setText(sex);
        tv_pinfo_phone.setText(share.getString("SJH", ""));
        if (share.getInt("ROLE_TYPE", 0) == 1) {
            tv_pinfo_zhuanye.setText("职位");
            tv_pinfo_major.setText(share.getString("DEPARTNAME", ""));
        } else if (share.getInt("ROLE_TYPE", 0) == 2) {
            tv_pinfo_zhuanye.setText("专业");
            tv_pinfo_major.setText(share.getString("ZYMC", ""));
        }
        String birthday = share.getString("CSRQ", "");
        tv_pinfo_brithday.setText(birthday);
        if (!TextUtils.isEmpty(birthday)) {
            try {
                int age = TimeUtils.getAge(birthday);
                tv_pinfo_age.setText(age + "");
                String xingzuo = TimeUtils.getConstellation(birthday);
                tv_pinfo_xz.setText(xingzuo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tv_pinfo_address.setText(hometown.toString());
        tv_pinfo_nowaddress.setText(nowAddress.toString());
        tv_pinfo_grsm.setText(share.getString("GRSM", ""));
    }

    /**
     * 加载头像
     *
     * @param url
     */
    private void loadHead(String url) {
        Bitmap bt = BitmapFactory.decodeResource(getResources(), R.mipmap.my_default_head);
        final int roundPx = bt.getWidth() / 2;
        AbImageLoader.getInstance(this).download(url, bt.getWidth(), bt.getWidth(), new AbImageLoader.OnImageListener2() {
            @Override
            public void onEmpty() {

            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                bitmap = AbImageUtil.toRoundBitmap(bitmap, roundPx);
                iv_my_icon.setImageBitmap(bitmap);
            }
        });
    }

    //上传图片
    private void uploadImage(final File file) {
        AbDialogUtil.showProgressDialog(PersonInfoActivity.this, 0, "图片正在上传...");
        AbTask task = AbTask.newInstance();
        AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {
            @Override
            public void get() {
                super.get();
//                String url = share.getString("uploadUrl","")+"upload_image.php?schoolCode=sdjzu"+Constants.BASEPARAMS;
                String url = share.getString("personUrl", "") + "upload_image.php?ss=ss" + Constants.BASEPARAMS;
                result = HttpUtils.uploadFile(url, file);
                Log.e("result", result + ":" + file.getName());
            }

            @Override
            public void update() {
                super.update();
                try {
                    AbDialogUtil.removeDialog(PersonInfoActivity.this);
                    JSONObject json = new JSONObject(result);
                    String code = json.getString("code");
                    if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                        json = new JSONObject(json.getString("data"));
                        int imgId = json.getInt("IRIID");
                        String imgUrl = json.getString("IRIURL");
                        updateUserInfo("photo", imgUrl, "");

                    } else {
                        ToastManager.getInstance().showToast(PersonInfoActivity.this, "上传失败,请重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastManager.getInstance().showToast(PersonInfoActivity.this, "上传失败,请重试");
                }
            }
        });
        task.execute(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    tv_pinfo_nickname.setText(share.getString("NICKNAME", ""));
                    if (flag == 2) {
                        flag = 3;
                    } else {
                        flag = 1;
                    }
                    //设置返回数据
                    setResult(RESULT_OK);
                    break;
                case 2:
                    String sex = "";
                    if (share.getString("XB", "0").equals("1")) {
                        sex = "男";
                    } else if (share.getString("XB", "0").equals("2")) {
                        sex = "女";
                    }
                    tv_pinfo_sex.setText(sex);
                    iv_pinfo_sex.setVisibility(View.GONE);
                    rl_pinfo_sex.setEnabled(false);
                    break;
                case 3:
                    tv_pinfo_phone.setText(share.getString("SJH", ""));
                    break;
                case 4:
                    String cityCode = data.getStringExtra("code");
                    String cityName = data.getStringExtra("name");
                    updateUserInfo("hometown", cityCode, cityName);
                    break;
                case 5:
                    String xzzCode = data.getStringExtra("code");
                    String xzzName = data.getStringExtra("name");
                    updateUserInfo("xzz", xzzCode, xzzName);
//                    ToastManager.getInstance().showToast(PersonInfoActivity.this,xzzCode+":"+xzzName);
                    break;
                case 6:
                    tv_pinfo_grsm.setText(share.getString("GRSM", ""));
                    break;
                default:
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
                    break;
            }
        }

    }

    /**
     * 修改信息
     *
     * @param key   要修改的字段
     * @param value 修改的值
     */
    private void updateUserInfo(final String key, String value, final String cityName) {
        AbHttpUtil abHttpUtil = AbHttpUtil.getInstance(this);
        final SharePreferenceUtils share = new SharePreferenceUtils(this);
        String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("updateUserInfoByInput") + Constants.BASEPARAMS;
        AbRequestParams params = new AbRequestParams();
        params.put("username", share.getString("ROLE_ID", ""));
        params.put(key, value);
        abHttpUtil.post(url, params, new CallBackParent(this, getResources().getString(R.string.loading)) {
            @Override
            public void Get_Result(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    if (jsonArray != null && jsonArray.length() > 0) {
                        jsonObject = jsonArray.getJSONObject(0);
                        if (key.equals("hometown")) {
                            share.put("CSDM", Function.getInstance().getString(jsonObject, "CSDM"));
                            tv_pinfo_address.setText(cityName + "");
                        } else if (key.equals("xzz")) {
                            share.put("XZZ", Function.getInstance().getString(jsonObject, "XZZ"));
                            tv_pinfo_nowaddress.setText(cityName + "");
                        } else if (key.equals("birthday")) {
                            String birthday = Function.getInstance().getString(jsonObject, "CSRQ");
                            share.put("CSRQ", birthday);
                            tv_pinfo_brithday.setText(birthday);
                            if (!TextUtils.isEmpty(birthday)) {
                                try {
                                    int age = TimeUtils.getAge(birthday);
                                    tv_pinfo_age.setText(age + "");
                                    String xingzuo = TimeUtils.getConstellation(birthday);
                                    tv_pinfo_xz.setText(xingzuo);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            iv_pinfo_brithday.setVisibility(View.GONE);
                            rl_pinfo_brithday.setEnabled(false);
                        } else if (key.equals("photo")) {
                            share.put("HEADIMAGE", Function.getInstance().getString(jsonObject, "TXDZ").replace("thumb/", ""));
                            loadHead(share.getString("HEADIMAGE", ""));
                            if (flag == 1) {
                                flag = 3;
                            } else {
                                flag = 2;
                            }
                        }
                        ToastManager.getInstance().showToast(PersonInfoActivity.this, "修改成功");
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

        if (TextUtils.isEmpty(share.getString("CSRQ", ""))) {
            iv_pinfo_brithday.setVisibility(View.VISIBLE);
            rl_pinfo_brithday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePickerView.show();
                }
            });
            // 时间选择后回调
            timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = dateFormat.format(date);
                    Log.e("birthday", dateStr + "");
                    updateUserInfo("birthday", dateStr + "", "");
                }
            });
        } else {
            iv_pinfo_brithday.setVisibility(View.GONE);
        }
        iv_my_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CameraPopWindow cameraPopWindow = new CameraPopWindow(PersonInfoActivity.this, ll_pinfo_parent);
                cameraPopWindow.setBtnCameraOnlickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraPopWindow.dismiss();
                        SelectPicUtil.getByCamera(PersonInfoActivity.this, true);
                    }
                });
                cameraPopWindow.setBtnPictureOnlickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraPopWindow.dismiss();
                        SelectPicUtil.getByAlbum(PersonInfoActivity.this);
                    }
                });
            }
        });
        //昵称
        rl_pinfo_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonInfoActivity.this, ModifyUserInfoActivity.class);
                intent.putExtra("content", tv_pinfo_nickname.getText().toString());
                intent.putExtra("type", 1);
                startActivityForResult(intent, 1);
            }
        });
        if (TextUtils.isEmpty(share.getString("XB", ""))) {
            iv_pinfo_sex.setVisibility(View.VISIBLE);
            //性别
            rl_pinfo_sex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonInfoActivity.this, ModifyUserInfoActivity.class);
                    intent.putExtra("type", 2);
                    startActivityForResult(intent, 2);
                }
            });
        } else {
            iv_pinfo_sex.setVisibility(View.GONE);
        }

        //电话
        rl_pinfo_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonInfoActivity.this, ModifyUserInfoActivity.class);
                intent.putExtra("content", tv_pinfo_phone.getText().toString());
                intent.putExtra("type", 3);
                startActivityForResult(intent, 3);
            }
        });
        //故乡
        rl_pinfo_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonInfoActivity.this, LocalSelectActivity.class);
                CityMapBean mapBean = new CityMapBean();
                mapBean.setProList(proList);
                mapBean.setCityMap(cityMaps);
                mapBean.setAreaMaps(areaMaps);
                intent.putExtra("mapBean", mapBean);
                startActivityForResult(intent, 4);
            }
        });
        //所在地
        rl_pinfo_suozaidi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonInfoActivity.this, LocalSelectActivity.class);
                CityMapBean mapBean = new CityMapBean();
                mapBean.setProList(proList);
                mapBean.setCityMap(cityMaps);
                mapBean.setAreaMaps(areaMaps);
                intent.putExtra("mapBean", mapBean);
                startActivityForResult(intent, 5);
            }
        });
        //个人说明
        rl_pinfo_grsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonInfoActivity.this, ModifyUserInfoActivity.class);
                intent.putExtra("content", tv_pinfo_grsm.getText().toString());
                intent.putExtra("type", 6);
                startActivityForResult(intent, 6);
            }
        });
        iv_pinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag != 0) {
                    Intent intent = new Intent();
                    intent.putExtra("flag", flag);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
    }

    private Map<String, List<CityBean>> cityMaps = new HashMap<String, List<CityBean>>();
    private Map<String, List<CityBean>> areaMaps = new HashMap<String, List<CityBean>>();
    private List<CityBean> proList;

    private void geCitytData() {
        String addressCode = share.getString("CSDM", "");
        String addressNowCode = share.getString("XZZ", "");
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
                if (addressCode.contains(jsonP.getString("code"))) {
                    hometown.append(jsonP.getString("name"));
                }
                if (addressNowCode.contains(jsonP.getString("code"))) {
                    nowAddress.append(jsonP.getString("name"));
                }
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
                    if (addressCode.contains(jsonCy.getString("code"))) {
                        hometown.append("-" + jsonCy.getString("name"));
                    }
                    if (addressNowCode.contains(jsonCy.getString("code"))) {
                        nowAddress.append("-" + jsonCy.getString("name"));
                    }
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
                        if (addressCode.contains(jsonA.getString("code"))) {
                            hometown.append("-" + jsonA.getString("name"));
                        }
                        if (addressNowCode.contains(jsonA.getString("code"))) {
                            nowAddress.append("-" + jsonA.getString("name"));
                        }
                    }
                    areaMaps.put(jsonCy.getString("code"), areaList);
                }
                cityMaps.put(jsonP.getString("code"), cityList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public void onBackPressed() {
        if (flag != 0) {
            Intent intent = new Intent();
            intent.putExtra("flag", flag);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}
