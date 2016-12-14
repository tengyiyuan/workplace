package com.toplion.cplusschool.Utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import com.toplion.cplusschool.widget.CustomDialog;

/**
 * Created by wangshegnob
 * on 2016/6/30.
 *
 * @des 打电话工具类
 */
public class CallUtil {

    public static void CallPhone(final Context mcontext, final String phoneStr) {
        final CustomDialog dialog = new CustomDialog(mcontext);
        dialog.setlinecolor();
        dialog.setTitle("确认拨打电话吗?");
        dialog.setContentboolean(true);
        dialog.setDetial(phoneStr);
        dialog.setLeftText("呼叫");
        dialog.setRightText("取消");

        dialog.setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneStr));
                mcontext.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //发短信
    public static void sendMessage(Context context,String number, String message) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.putExtra("sms_body", message);
        context.startActivity(sendIntent);
    }

    //复制
    public static void copyPhone(Context context, String str) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            cmb.setPrimaryClip(ClipData.newPlainText("text", str));
            ToastManager.getInstance().showToast(context, "已复制到剪贴板");
        }
    }
    //复制
    public static void copyStrNoToast(Context context, String str) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            cmb.setPrimaryClip(ClipData.newPlainText("text", str));
        }
    }
    //保存电话号码
    public static void toContacts(Context context, String nameStr,String jobStr, String phoneStr) {
        Intent it = new Intent(Intent.ACTION_INSERT, Uri.withAppendedPath(Uri.parse("content://com.android.contacts"), "contacts"));
        it.setType("vnd.android.cursor.dir/person");
         it.setType("vnd.android.cursor.dir/contact");
         it.setType("vnd.android.cursor.dir/raw_contact");
        // 联系人姓名
        it.putExtra(android.provider.ContactsContract.Intents.Insert.NAME,jobStr + nameStr);
        // 电话号码
        it.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, phoneStr);
        context.startActivity(it);
    }
}
