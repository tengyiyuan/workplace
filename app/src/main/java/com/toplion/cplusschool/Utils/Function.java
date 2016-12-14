package com.toplion.cplusschool.Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.widget.TextView;


/**
 * @author tengyiyuan
 * @version 1.0.0 2016-5-24
 * @Description公共方法
 * @reviewer
 */
public class Function {
    public static Function myfunction;

    public static Function getInstance() {

        if (myfunction == null) {

            synchronized (Function.class) {

                if (myfunction == null) {

                    myfunction = new Function();
                }
            }
        }
        return myfunction;
    }

    public void setTextView(JSONObject jo, String key, TextView v) {
        if (jo.has(key))
            try {
                String ls_sex = jo.getString(key);
                if ("sexual".equals(key)) {
                    if ("0".equals(ls_sex))
                        v.setText("男");
                    else if ("1".equals(ls_sex))
                        v.setText("女");
                    else v.setText("");
                } else {
                    v.setText("null".equals(ls_sex) ? "" : ls_sex);
                }
            } catch (JSONException e) {

                // 自定义统计事件
            }
        else v.setText("");
    }

    public String getString(JSONObject jo, String key) {
        if (jo.has(key))
            try {
                if ("null".equals(jo.getString(key))) {
                    return "";
                }
                return jo.getString(key);
            } catch (JSONException e) {

            }
        else return "";
        return "";
    }

    public boolean getBoolean(JSONObject jo, String key) {
        if (jo.has(key))
            try {
                if ("null".equals(jo.getString(key))) {
                    return false;
                }
                return jo.getBoolean(key);
            } catch (JSONException e) {

            }
        else return false;
        return false;
    }

    public int getInteger(JSONObject jo, String key) {
        if (jo.has(key))
            try {
                if ("null".equals(jo.getString(key))) {
                    return 0;
                }
                return jo.getInt(key);
            } catch (JSONException e) {

            }
        else return 0;
        return 0;
    }
    public long getLong(JSONObject jo, String key) {
        if (jo.has(key))
            try {
                if ("null".equals(jo.getString(key))) {
                    return 0;
                }
                return jo.getLong(key);
            } catch (JSONException e) {

            }
        else return 0;
        return 0;
    }

    /**
     * xml解析(暂)
     *
     * @param is
     * @param city
     * @return
     * @throws Exception
     */
    public List<HashMap<String, String>> parse(InputStream is, String city)
            throws Exception {
        List<HashMap<String, String>> books = new ArrayList<HashMap<String, String>>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 取得DocumentBuilderFactory实例
        DocumentBuilder builder = factory.newDocumentBuilder(); // 从factory获取DocumentBuilder实例
        Document doc = builder.parse(is); // 解析输入流 得到Document实例
        Element rootElement = doc.getDocumentElement();
        NodeList items = rootElement.getElementsByTagName("point");
        for (int i = 0; i < items.getLength(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            Node item = items.item(i);
            NodeList properties = item.getChildNodes();
            for (int j = 0; j < properties.getLength(); j++) {
                Node property = properties.item(j);
                String nodeName = property.getNodeName();
                if (nodeName.equals("name")) {
                    map.put("name", property.getFirstChild().getNodeValue());
                } else if (nodeName.equals("lng")) {
                    map.put("lng", property.getFirstChild().getNodeValue());
                } else if (nodeName.equals("lat")) {
                    map.put("lat", property.getFirstChild().getNodeValue());
                } else if (nodeName.equals("district_text")) {
                    map.put("city", city);
                }
            }
            books.add(map);
        }
        return books;
    }


    public String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String macAdress = info.getMacAddress(); // 获取mac地址
        return "".equals(macAdress) ? "" + System.currentTimeMillis()
                : macAdress;
    }

    public int getVersionName(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),
                0);
        return packInfo.versionCode;
    }
}
