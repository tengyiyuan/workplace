
package com.toplion.cplusschool.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.toplion.cplusschool.Activity.MainActivity;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;

import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 */
public class CommonUtil {
    public static int getRandom(int max) {
        return (int) (Math.random() * max);
    }

    public static ProgressDialog getProcessDialog(Context context, String tips) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(tips);
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * 获取wif信道信息转换成json传给后台
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static JSONObject getCurrentChannel(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        JSONObject object = new JSONObject();
        JSONArray orray = new JSONArray();
        try {
            object.put("mBSSID", wifiInfo.getBSSID());
            object.put("mWifiSsiD", wifiInfo.getSSID());
            object.put("mIpAddress", intToIp(wifiInfo.getIpAddress()));
            object.put("mMacAddress", wifiInfo.getMacAddress());
            object.put("Gpslocation", getLocationGps(context));
            object.put("Netlocation", getLocationNet(context));
            List<ScanResult> scanResults = wifiManager.getScanResults();
            for (ScanResult result : scanResults) {
                final JSONObject obj = new JSONObject();
                obj.put("BSSID", result.BSSID);
                obj.put("SSID", result.SSID);
                obj.put("timestamp", result.timestamp);
                obj.put("frequency", result.frequency);
                obj.put("frequencytochnnel", getChannelByFrequency(result.frequency));
                obj.put("level", result.level);
                orray.put(obj);
            }
            object.put("ScanResults", orray);
            return object;
        } catch (JSONException e) {
            return object;
        }
    }

    /**
     * 根据频率获得信息
     *
     * @param frequency
     * @return
     */
    public static int getChannelByFrequency(int frequency) {
        int channel = -1;
        switch (frequency) {
            case 2412:
                channel = 1;
                break;
            case 2417:
                channel = 2;
                break;
            case 2422:
                channel = 3;
                break;
            case 2427:
                channel = 4;
                break;
            case 2432:
                channel = 5;
                break;
            case 2437:
                channel = 6;
                break;
            case 2442:
                channel = 7;
                break;
            case 2447:
                channel = 8;
                break;
            case 2452:
                channel = 9;
                break;
            case 2457:
                channel = 10;
                break;
            case 2462:
                channel = 11;
                break;
            case 2467:
                channel = 12;
                break;
            case 2472:
                channel = 13;
                break;
            case 2484:
                channel = 14;
                break;
            case 5745:
                channel = 149;
                break;
            case 5765:
                channel = 153;
                break;
            case 5785:
                channel = 157;
                break;
            case 5805:
                channel = 161;
                break;
            case 5825:
                channel = 165;
                break;
        }
        return channel;
    }

    /**
     * 获取设备所在的经纬度
     */


    public static Location getLocation(Context con) {
        double latitude = 0.0;
        double longitude = 0.0;
        LocationManager locationManager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                System.out.println("WifiInfo GPS:" + latitude);
            }
            return location;
        } else {
            LocationListener locationListener = new LocationListener() {
                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        Log.e("Map", "Location changed : Lat: "
                                + location.getLatitude() + " Lng: "
                                + location.getLongitude());

                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude(); //经度
                longitude = location.getLongitude(); //纬度
                System.out.println("WifiInfo" + latitude);
            }
            return location;
        }

    }

    /**
     * 通过gps获取设备定位信息
     *
     * @param con
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static JSONObject getLocationGps(Context con) throws JSONException {
        JSONObject obj = new JSONObject();
        LocationManager locationManager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            obj.put("mIsFromMockProvider", location.getProvider());
            obj.put("mTime", location.getTime());
            obj.put("mElapsedRealtimeNanos", location.getElapsedRealtimeNanos());
            obj.put("mLatitude", location.getLatitude());
            obj.put("mLongitude", location.getLongitude());
            obj.put("mAltitude", location.getAltitude());
            obj.put("mSpeed", location.getSpeed());
            obj.put("mBearing", location.getBearing());
            obj.put("mAccuracy", location.getAccuracy());
            return obj;
        }
        obj.put("mIsFromMockProvider", "");
        obj.put("mTime", "");
        obj.put("mElapsedRealtimeNanos", "");
        obj.put("mLatitude", "");
        obj.put("mLongitude", "");
        obj.put("mAltitude", "");
        obj.put("mSpeed", "");
        obj.put("mBearing", "");
        obj.put("mAccuracy", "");
        return obj;
    }

    /**
     * 通过net获取设备定位信息
     *
     * @param con
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static JSONObject getLocationNet(Context con) throws JSONException {
        JSONObject obj = new JSONObject();
        LocationManager locationManager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            obj.put("mIsFromMockProvider", location.getProvider());
            obj.put("mTime", location.getTime());
            obj.put("mElapsedRealtimeNanos", location.getElapsedRealtimeNanos());
            obj.put("mLatitude", location.getLatitude());
            obj.put("mLongitude", location.getLongitude());
            obj.put("mAltitude", location.getAltitude());
            obj.put("mSpeed", location.getSpeed());
            obj.put("mBearing", location.getBearing());
            obj.put("mAccuracy", location.getAccuracy());
            return obj;
        }
        obj.put("mIsFromMockProvider", "");
        obj.put("mTime", "");
        obj.put("mElapsedRealtimeNanos", "");
        obj.put("mLatitude", "");
        obj.put("mLongitude", "");
        obj.put("mAltitude", "");
        obj.put("mSpeed", "");
        obj.put("mBearing", "");
        obj.put("mAccuracy", "");
        return obj;
    }

    public static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
    }

    /**
     * 封装判断token失效后，退出到登录界面
     */
    public static void intoLogin(final Activity activity, SharePreferenceUtils share, String message) {
        Constants.baseUrl = share.getString("baseUrl", "");
        Constants.userIndex = share.getString("userIndex", "");
        /**
         * 退出到登录页面修改为给share赋值的模式，解决退出后（及修改密码后）再次登录广告页面
         * 显示不出来的BUG
         */
        share.put("username", "");
        share.put("pwd", "");
        share.put("token", "");
        share.put("tokenTim", "0");
        share.put("baseUrl", "");
        share.put("userIndex", "");
        share.put("samUserInfo", "");
        share.put("order", "");
        share.put("serverIp", "");
        new Thread() {
            public void run() {
                if (Constants.userIndex == null || Constants.baseUrl == null
                        || "".equals(Constants.baseUrl) || "".equals(Constants.userIndex)) {
                    //Intent intent = new Intent(getActivity(), MainActivity.class);
                    //startActivity(intent);
                } else {
                    /**
                     * 一键上网改版前
                     */
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
                    /**
                     * 一键上网改版后
                     */
//												String baseUrl = Constants.baseUrl + "logout";
//												Map<String,String> map = new HashMap<String,String>();
//												map.put("userip",ConnectivityUtils.getSysIp(getActivity()));
//												map.put("userIndex",Constants.userIndex);
//												try {
//													JSONObject object = EportalUtils.httpClientForPost(baseUrl, map);
//													if(object!=null){
//														if(object.getString("result").equals("success")){
//															System.out.println("成功下线");
//															Constants.ISRUN = false;
//														}
//													}
//												} catch (Exception e) {
//													e.printStackTrace();
//												}

                }
            }
        }.start();
        if (TextUtils.isEmpty(message)) {
            message = activity.getResources().getString(R.string.login_timeout);
        }
//        ToastManager.getInstance().showToast(activity, R.string.login_timeout);
        final CommDialog dialog = new CommDialog(activity);
        dialog.CreateDialogOnlyOkForUp("系统提示", "确定", message, new CommDialog.CallBack() {
            @Override
            public void isConfirm(boolean flag) {
                if (flag) {
//					 // 跳转到登录界面
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
                dialog.cancelDialog();
            }
        });

    }
}
