package com.toplion.cplusschool.Utils;

import android.app.Dialog;
import android.content.Context;

import com.toplion.cplusschool.Common.CustomDialog;


/**
 * @Description等待框
 * @author tengyiyuan
 * @version 1.0.0 2016-3-28
 * @reviewer
 */
public class DialogManager {

    private Dialog dialog;                       // dialog
    public static DialogManager myDialog;

    private DialogManager() {
    }

    /**
     * 获取MyToast对象。。
     * 
     * @return MyToast对象
     */
    public static DialogManager getInstance() {

        if (myDialog == null) {

            synchronized (DialogManager.class) {

                if (myDialog == null) {

                    myDialog = new DialogManager();
                }
            }
        }
        return myDialog;
    }

    /**
     * 显示等待框
     * 
     * @param context
     * @param str
     *            string 提示文字
     */
    public void showDialog(Context context,String str) {
        dialog = new CustomDialog(context).initDialog(str);
    }



    /**
     * 取消Dialog
     */
    public void cancelDialog() {

        if (dialog != null) {

            dialog.dismiss();
        }
    }

}
