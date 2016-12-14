package com.toplion.cplusschool.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ab.http.AbHttpUtil;
import com.ab.image.AbImageLoader;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbImageUtil;
import com.toplion.cplusschool.Activity.AboutSoftActivity;
import com.toplion.cplusschool.Activity.FeekBackActivity;
import com.toplion.cplusschool.Activity.MainActivity;
import com.toplion.cplusschool.Activity.MyOrderActivity;
import com.toplion.cplusschool.Activity.MyRepairListActivity;
import com.toplion.cplusschool.Activity.PersonInfoActivity;
import com.toplion.cplusschool.Activity.UpdatePwdActivity;
import com.toplion.cplusschool.Activity.UseHelpActivity;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Update.UpdateVersion;
import com.toplion.cplusschool.Utils.ConnectivityUtils;
import com.toplion.cplusschool.Utils.EportalUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.widget.CustomDialog;
import com.toplion.cplusschool.widget.TimePickerView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * 设置界面
 * 显示用户基本信息.
 * 显示用户设置信息
 * 我的订单、我的报修、使用帮助
 * 关于本软件、软件升级、退出登录
 * * Created by wang
 * on 2016/10/19.
 */
public class MeFragmentNew extends Fragment implements OnClickListener {
    private View view;
    private TextView tv_my_edit;//编辑按钮
    private RelativeLayout rl_my_order;
    private ImageView iv_my_icon;//头像
    private TextView tv_my_nickname;//昵称
    private ImageView iv_my_sex;//性别
    private TextView tv_my_schoolname;//学校
    private TextView tv_my_flower_number;//鲜花数量
    private RelativeLayout rl_my_repair;//我的报修
    private RelativeLayout rl_my_reimburse;// 我的报销
    private RelativeLayout rl_my_updatepwd;//修改密码
    private RelativeLayout rl_my_feekback;//意见反馈
    private RelativeLayout rl_my_help;//使用帮助
    private RelativeLayout rl_my_soft_update;//软件升级
    private RelativeLayout rl_my_about;//关于本软件
    private TextView tv_my_logout;//退出登录
    private SharePreferenceUtils share;
    private AbHttpUtil abHttpUtil;
    private int PERSONINFOCODE = 333;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.me_fragment_new, container, false);
        share = new SharePreferenceUtils(view.getContext());
        abHttpUtil = AbHttpUtil.getInstance(view.getContext());
        iv_my_icon = (ImageView) view.findViewById(R.id.iv_my_icon);
        tv_my_nickname = (TextView) view.findViewById(R.id.tv_my_nickname);
        iv_my_sex = (ImageView) view.findViewById(R.id.iv_my_sex);
        tv_my_schoolname = (TextView) view.findViewById(R.id.tv_my_schoolname);
        tv_my_flower_number = (TextView) view.findViewById(R.id.tv_my_flower_number);
        tv_my_edit = (TextView) view.findViewById(R.id.tv_my_edit);
        rl_my_order = (RelativeLayout) view.findViewById(R.id.rl_my_order);
        rl_my_repair = (RelativeLayout) view.findViewById(R.id.rl_my_repair);
        rl_my_reimburse = (RelativeLayout) view.findViewById(R.id.rl_my_reimburse);
        rl_my_feekback = (RelativeLayout) view.findViewById(R.id.rl_my_feekback);
        rl_my_updatepwd = (RelativeLayout) view.findViewById(R.id.rl_my_updatepwd);
        rl_my_help = (RelativeLayout) view.findViewById(R.id.rl_my_help);
        rl_my_soft_update = (RelativeLayout) view.findViewById(R.id.rl_my_soft_update);
        rl_my_about = (RelativeLayout) view.findViewById(R.id.rl_my_about);
        tv_my_logout = (TextView) view.findViewById(R.id.tv_my_logout);
        setViewText();
        setListener();
        return view;
    }

    //设置数据
    private void setViewText() {
        loadHead(share.getString("HEADIMAGE", ""));
        tv_my_nickname.setText(share.getString("NICKNAME", "游客"));
        tv_my_schoolname.setText(share.getString("schoolName", ""));
        tv_my_flower_number.setText(share.getInt("SBIFLOWERSNUMBER", 0) + "");
        String sex = share.getString("XB", "0");
        int sexIcon = 0;
        if (sex.equals("1")) {
            sexIcon = R.mipmap.boy;
        } else if (sex.equals("2")) {
            sexIcon = R.mipmap.girl;
        }
        iv_my_sex.setImageResource(sexIcon);
    }

    /**
     * 加载头像
     *
     * @param url
     */
    private void loadHead(String url) {
        Bitmap bt = BitmapFactory.decodeResource(getResources(), R.mipmap.my_default_head);
        final int roundPx = bt.getWidth() / 2;
        AbImageLoader.getInstance(getActivity()).download(url, bt.getWidth(), bt.getWidth(), new AbImageLoader.OnImageListener2() {
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

    //点击事件添加
    private void setListener() {
        tv_my_edit.setOnClickListener(this);
        rl_my_order.setOnClickListener(this);
        rl_my_repair.setOnClickListener(this);
        rl_my_reimburse.setOnClickListener(this);
        rl_my_updatepwd.setOnClickListener(this);
        rl_my_feekback.setOnClickListener(this);
        rl_my_help.setOnClickListener(this);
        rl_my_soft_update.setOnClickListener(this);
        rl_my_about.setOnClickListener(this);
        tv_my_logout.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PERSONINFOCODE){
                if(data !=null){
                    int flag = data.getIntExtra("flag",0);
                    if(flag == 1){
                        tv_my_nickname.setText(share.getString("NICKNAME", "游客"));
                    }else if(flag == 2){
                        loadHead(share.getString("HEADIMAGE", ""));
                    }else if(flag == 3){
                        tv_my_nickname.setText(share.getString("NICKNAME", "游客"));
                        loadHead(share.getString("HEADIMAGE", ""));
                    }
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_my_edit:  //编辑按钮
                // 个人中心
                Intent iperson = new Intent(getActivity(), PersonInfoActivity.class);
                startActivityForResult(iperson, PERSONINFOCODE);
                break;
            case R.id.rl_my_order:  //我的订单
                MobclickAgent.onEvent(getActivity(), "pay_list");//友盟统计
                // 我的订单
                Intent iorder = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(iorder);
                break;
            case R.id.rl_my_repair: //我的报修
                // 我的报修
                Intent irepair = new Intent(getActivity(), MyRepairListActivity.class);
                startActivity(irepair);
                break;
            case R.id.rl_my_reimburse://我的报销
                break;
            case R.id.rl_my_updatepwd://修改密码
                Intent uppwd = new Intent(getActivity(), UpdatePwdActivity.class);
                startActivity(uppwd);
                break;
            case R.id.rl_my_feekback://修改密码
                // 意见反馈
                Intent ifeekback = new Intent(getActivity(), FeekBackActivity.class);
                startActivity(ifeekback);
                break;
            case R.id.rl_my_help://使用帮助
                MobclickAgent.onEvent(getActivity(), "helpList");//友盟统计
                // 使用帮助
                Intent ihelp = new Intent(getActivity(), UseHelpActivity.class);
                startActivity(ihelp);
                break;
            case R.id.rl_my_soft_update://软件升级
                String version = share.getString("version", "");
                String sysVersion = ConnectivityUtils.getAppVersionName(getActivity());
                if (!TextUtils.isEmpty(version)) {
                    if (Integer.parseInt(version.replace(".", "")) > Integer.parseInt(sysVersion.replace(".", ""))) {
                        UpdateVersion versionUp = new UpdateVersion(getActivity());
                        versionUp.checkUpdate();
                    } else {
                        ToastManager.getInstance().showToast(getActivity(), "已是最新版本!");
                    }
                } else {
                    ToastManager.getInstance().showToast(getActivity(), "已是最新版本!");
                }
                break;
            case R.id.rl_my_about://关于本软件
                MobclickAgent.onEvent(getActivity(), "aboutUs");//友盟统计
                // 关于本软件
                Intent intent = new Intent(getActivity(), AboutSoftActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_my_logout:
                final CustomDialog dialog = new CustomDialog(getActivity());
                dialog.setlinecolor();
                dialog.setTitle("提示");
                dialog.setContentboolean(true);
                dialog.setDetial("确认退出登录吗?");
                dialog.setLeftText("确定");
                dialog.setRightText("取消");
                dialog.setLeftOnClick(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        MobclickAgent.onEvent(getActivity(), "logout");//友盟统计
                        Constants.baseUrl = share.getString("baseUrl", "");
                        Constants.userIndex = share.getString("userIndex", "");
                        share.put("username", "");
                        share.put("pwd", "");
                        share.put("token", "");
                        share.put("tokenTim", "0");
                        share.put("baseUrl", "");
                        share.put("userIndex", "");
                        share.put("samUserInfo", "");
                        share.put("order", "");
                        share.put("serverIp", "");
                        share.clear();
                        if (!TextUtils.isEmpty(Constants.userIndex) && !TextUtils.isEmpty(Constants.baseUrl)) {
                            logoutEportal();
                        }
                        // 跳转到登录界面
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
                dialog.setRightOnClick(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }

    /**
     * 退出Eportal登录
     */
    private void logoutEportal() {
        AbTask abTask = AbTask.newInstance();
        AbTaskItem abTaskItem = new AbTaskItem();
        abTaskItem.setListener(new AbTaskListener() {
            @Override
            public void update() {
                super.update();
                String baseUrl = Constants.baseUrl + "?method=logout&userIndex=" + Constants.userIndex;
                JSONObject object = EportalUtils.httpClientPost(baseUrl, null);
                try {
                    if (object != null) {
                        String reString = object.getString("result");
                        if (reString != null && !"".equals(reString)) {
                            String nextUrl = reString.substring(reString.indexOf("<script type="), reString.indexOf("</script>"));
                            nextUrl = nextUrl.substring(nextUrl.indexOf("?"), nextUrl.indexOf("\");"));
                            object = EportalUtils.httpClientGet(Constants.baseUrl + nextUrl);
                            String title = object.getString("result").toString();
                            if (title.indexOf("您已经下线") > 0) {
                                Constants.ISRUN = false;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        abTask.execute(abTaskItem);
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_my_flower_number.setText(share.getInt("SBIFLOWERSNUMBER", 0) + "");
        MobclickAgent.onPageStart(getActivity().getClass().getSimpleName()); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getActivity().getClass().getSimpleName());
    }
}
