package com.toplion.cplusschool.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by wang
 * on 2016/9/12.
 * 发送邮件
 */
public class EmailUtil {

    /**
     * @param context
     * @param email 账号
     * @param title 标题
     * @param content 内容
     */
    public static void sendEmail(Context context,String email,String title,String content){
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:"+email));
        data.putExtra(Intent.EXTRA_SUBJECT, title);
        data.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(data);
    }
}
