package com.toplion.cplusschool.Common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 输入验证
 * 验证手机号码的格式
 * 验证Email的格式
 * 验证密码的格式
 * @author liyb
 *
 */
public class InputValidate {
    // 判断是否为手机号
    public static  boolean isPhone(String inputText) {
        if(inputText.trim().length()<=0) return false;
        Pattern p = Pattern
                .compile("1[0-9]{1}[0-9]{9}");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

    // 判断格式是否为email
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    // 判断密码格式
    public static boolean isPassword(String pwd){
        if(pwd.trim().length()<=0) return false;
        String str = "(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,}";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(pwd);
        return m.matches();
    }
}
