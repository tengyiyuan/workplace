package com.toplion.cplusschool.dao;

import android.app.Activity;
import android.text.TextUtils;

import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbDialogUtil;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Utils.CommonUtil;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 封装抽象类网络请求返回判断（如果token失效自动跳入登录页面）
 */
public abstract class CallBackParent extends AbStringHttpResponseListener {
    public Activity context;
    private SharePreferenceUtils share;
    private String tostmsg="";
    private String tag = "";
    private boolean flag=true;

    public CallBackParent(Activity c, String tostmsg,String tag) {
        super();
        this.context = c;
        share = new SharePreferenceUtils(context);
        this.tostmsg = tostmsg;
        this.tag = tag;
        this.flag=true;
    }
    public CallBackParent(Activity c, String tostmsg) {
        super();
        this.context = c;
        share = new SharePreferenceUtils(context);
        this.tostmsg = tostmsg;
        this.flag=true;
    }
    public CallBackParent(Activity c, boolean flag) {
        super();
        this.context = c;
        share = new SharePreferenceUtils(context);
        this.flag=flag;
    }
    @Override
    public void onSuccess(int statusCode, String content) {
        try {
            JSONObject object = new JSONObject(content.toString());
            String code = object.getString("code");
            String msg = object.getString("msg");
            if (code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)) {
                Get_Result(content);
            } else if (code.equals(CacheConstants.TOKEN_FAIL)) {
                CommonUtil.intoLogin(context, share,msg);
            } else {
                Get_Result_faile(msg);
            }
            if(!TextUtils.isEmpty(tag)){
                AbDialogUtil.removeDialog(context,tag);
            }else{
                AbDialogUtil.removeDialog(context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if(!TextUtils.isEmpty(tag)){
                AbDialogUtil.removeDialog(context,tag);
            }else{
                AbDialogUtil.removeDialog(context);
            }
            ToastManager.getInstance().showToast(context, "服+务器异常!");
        }
    }

    public void Get_Result_faile(String errormsg) {
        ToastManager.getInstance().showToast(context, errormsg);
        if(!TextUtils.isEmpty(tag)){
            AbDialogUtil.removeDialog(context,tag);
        }else{
            AbDialogUtil.removeDialog(context);
        }
    }

    @Override
    public void onStart() {
        if(flag) {
            if (!TextUtils.isEmpty(tag)) {
                AbDialogUtil.showProgressDialog(context, 0, tostmsg, tag);
            } else {
                AbDialogUtil.showProgressDialog(context, 0, tostmsg);
            }
        }
    }

    public abstract void Get_Result(String result);

    @Override
    public void onFinish() {
        if(flag) {
            if (!TextUtils.isEmpty(tag)) {
                AbDialogUtil.removeDialog(context, tag);
            } else {
                AbDialogUtil.removeDialog(context);
            }
        }
    }

    @Override
    public void onFailure(int statusCode, String content, Throwable error) {
        if(flag) {
            if (!TextUtils.isEmpty(tag)) {
                AbDialogUtil.removeDialog(context, tag);
            } else {
                AbDialogUtil.removeDialog(context);
            }
        }
        ToastManager.getInstance().showToast(context, "服务器异常,请稍后重试!");
    }
}
