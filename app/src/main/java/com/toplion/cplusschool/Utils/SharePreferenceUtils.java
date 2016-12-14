package com.toplion.cplusschool.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

/**
 * @author WangShengbo
 * @date       2016年2月25日
 * @TODO SharePreference 帮助类
 */
public class SharePreferenceUtils {

    private SharedPreferences mySp;
    private Editor mEditor;

    private static String spName = "canting_sp";

    /**
     * 构造方法。
     *
     * @paramm Ctx
     */

    public SharePreferenceUtils(Context mCtx) {
        mySp = mCtx.getSharedPreferences(spName, Context.MODE_PRIVATE);
        mEditor = mySp.edit();
    }


    /**
     * 获取保存着的boolean对象。
     *
     * @param key      键名
     * @param defValue 当不存在时返回的默认值。
     * @return 返回获取到的值，当不存在时返回默认值。
     */
    public boolean getBoolean(String key, boolean defValue) {
        return mySp.getBoolean(key, defValue);
    }

    /**
     * 获取保存着的int对象。
     *
     * @param key      键名
     * @param defValue 当不存在时返回的默认值。
     * @return 返回获取到的值，当不存在时返回默认值。
     */
    public int getInt(String key, int defValue) {
        return mySp.getInt(key, defValue);
    }

    /**
     * 获取保存着的long对象。
     *
     * @param key      键名
     * @param defValue 当不存在时返回的默认值。
     * @return 返回获取到的值，当不存在时返回默认值。
     */
    public long getLong(String key, long defValue) {
        return mySp.getLong(key, defValue);
    }

    /**
     * 获取保存着的float对象。
     *
     * @param key      键名
     * @param defValue 当不存在时返回的默认值。
     * @return 返回获取到的值，当不存在时返回默认值。
     */
    public float getFloat(String key, float defValue) {
        return mySp.getFloat(key, defValue);
    }

    /**
     * 获取保存着的String对象。
     *
     * @param key      键名
     * @param defValue 当不存在时返回的默认值。
     * @return 返回获取到的值，当不存在时返回默认值。
     */
    public String getString(String key, String defValue) {
        return mySp.getString(key, defValue);
    }

    /**
     * 获取所有键值对。
     *
     * @return 获取到的所胡键值对。
     */
    public Map<String, ?> getAll() {
        return mySp.getAll();
    }

    /**
     * 设置一个键值对，它将在{@linkplain #commit()}被调用时保存。<br/>
     * 注意：当保存的value不是boolean, byte(会被转换成int保存),int, long, float,
     * String等类型时将调用它的toString()方法进行值的保存。
     *
     * @param key   键名称。
     * @param value 值。
     * @return 引用的KV对象。
     */
    public SharePreferenceUtils put(String key, Object value) {
        if (value instanceof Boolean) {
            mEditor.putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer || value instanceof Byte) {
            mEditor.putInt(key, (Integer) value).commit();
        } else if (value instanceof Long) {
            mEditor.putLong(key, (Long) value).commit();
        } else if (value instanceof Float) {
            mEditor.putFloat(key, (Float) value).commit();
        } else if (value instanceof String) {
            mEditor.putString(key, (String) value).commit();
        }
        return this;
    }

    /**
     * 移除键值对。
     *
     * @param key 要移除的键名称。
     * @return 引用的KV对象。
     */
    public SharePreferenceUtils remove(String key) {
        mEditor.remove(key);
        mEditor.commit();
        return this;
    }

    /**
     * 清除所有键值对。
     *
     * @return 引用的KV对象。
     */
    public SharePreferenceUtils clear() {
        mEditor.clear();
        mEditor.commit();
        return this;
    }

    /**
     * 是否包含某个键。
     *
     * @param key 查询的键名称。
     * @return 当且仅当包含该键时返回true, 否则返回false.
     */
    public boolean contains(String key) {
        return mySp.contains(key);
    }

    /**
     * 返回是否提交成功。
     *
     * @return 当且仅当提交成功时返回true, 否则返回false.
     */
    public boolean commit() {
        return mEditor.commit();
    }

}
