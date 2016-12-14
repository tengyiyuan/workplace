package com.toplion.cplusschool.Activity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import java.util.List;

/**
 * Created by toplion on 2016/4/15.
 */
public class WifiUpload extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread() {
            @Override
            public void run() {
                super.run();
                getCurrentChannel(WifiUpload.this);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public static int getCurrentChannel(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        // System.out.println("我的信道是：" + scanResults.size());
        for (ScanResult result : scanResults) {
            System.out.println("我的信道是：" + result.SSID);
//            if (result.BSSID.equalsIgnoreCase(wifiInfo.getBSSID())
//                    && result.SSID.equalsIgnoreCase(wifiInfo.getSSID()
//                    .substring(1, wifiInfo.getSSID().length() - 1))) {
//               // System.out.println("我的信道是t.frequency);：" + getChannelByFrequency(result.frequency));
//                return getChannelByFrequency(resul
//            }
        }
        return -1;
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
}
