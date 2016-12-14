package com.toplion.cplusschool.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.toplion.cplusschool.Bean.PhoneInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author liyb
 * 手机基本信息
 */
public class TelephoneUtils {

    @SuppressWarnings("unused")
    private static Context context;
    private static ConnectivityManager mConnectivity;
    private static TelephonyManager mTelephony;
    private static WifiManager wifiManager;
    @SuppressWarnings("static-access")
    public TelephoneUtils(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    //获取手机MAC地址
    public String getMacAddress(){
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

    //获取用户ID
    public static String getSubscriberNum(){
        String sub = mTelephony.getSubscriberId();
        if("".equals(sub)||sub==null) return "0";
        else return sub;
    }
    //获取Sim卡序列号，如无Sim卡则置0
    public static String getSimNum(){
        String sim = mTelephony.getSimSerialNumber();
        if("".equals(sim) || sim==null) return "0";
        else return sim;
    }
    //获取设备唯一编号
    public static String getDeviceNum() {
        String deviceId = mTelephony.getDeviceId();
        if("".equals(deviceId) || deviceId==null) return "0";
        else return deviceId;
    }
    //获取设备制式
    public static String getDeviceMode(){
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        String NetType = "";

        if (info.getType() == ConnectivityManager.TYPE_WIFI){
            NetType = "WIFI";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            NetType = getNetworkTypeName(info.getSubtype());
        }
        Log.e("ssssssssssss",NetType+"");
        return NetType;
    }
    //检测手机上网状态
    private static String getNetworkTypeName(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "CDMA - EvDo rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "CDMA - EvDo rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "CDMA - EvDo rev. B";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "CDMA - 1xRTT";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "CDMA - eHRPD";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDEN";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPA+";
            default:
                return "UNKNOWN";
        }
    }
    /**
     * 获取手机User-Agent
     * @return
     */
    public static String getUserAgent(Context cxt){
        WebView webview = new WebView(cxt);

        webview.layout(0, 0, 0, 0);

        WebSettings settings = webview.getSettings();
        String ua = settings.getUserAgentString();
        Log.e("HJJ", "User Agent:" + ua);
        return ua;
    }

    /**
     *
     * 获取手机号
     * @return
     */
    public static String getPhoneNumber(){
        String phone = mTelephony.getLine1Number();
        if(!TextUtils.isEmpty(phone)){
            return phone;
        }else{
            return "0";
        }
    }


    /**
     * 获取手机联系人
     * @return
     */
    public static List<PhoneInfo> getContacts(){
        List<PhoneInfo> list = new ArrayList<PhoneInfo>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        //moveToNext方法返回的是一个boolean类型的数据
        if(cursor!=null){
            while (cursor.moveToNext()) {
                //读取通讯录的姓名
                String name = cursor.getString(cursor  .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //读取通讯录的号码
                String number = cursor.getString(cursor .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                PhoneInfo phoneInfo = new PhoneInfo(name, number);
                list.add(phoneInfo);
            }
        }
        return list;
    }
}
