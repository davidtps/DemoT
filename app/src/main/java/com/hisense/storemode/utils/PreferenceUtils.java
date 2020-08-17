package com.hisense.storemode.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hisense.storemode.StoreModeApplication;

public class PreferenceUtils {
    private static final String TAG = "PreferenceUtils";
    private static final String SHAREPREFERENCE_FILENAME = "store_mode";

    private SharedPreferences mPreference;

    private PreferenceUtils() {
        mPreference = StoreModeApplication.sContext.getSharedPreferences
                (SHAREPREFERENCE_FILENAME, Context.MODE_PRIVATE);
    }

    private volatile static PreferenceUtils sInstance;

    public static PreferenceUtils getInstance() {    //对获取实例的方法进行同步
        if (sInstance == null) {
            synchronized (PreferenceUtils.class) {
                if (sInstance == null)
                    sInstance = new PreferenceUtils();
            }
        }
        return sInstance;
    }

    //whether include key
    public boolean contains(String key) {
        return mPreference.contains(key);
    }

    protected void deleteDB(String name) throws Exception {
        mPreference.edit().remove(name);
    }

    // 根据键名提取键值
    public Object get(String key, Object defaultObj) {

        if (defaultObj instanceof String) {
            return mPreference.getString(key, (String) defaultObj);
        } else if (defaultObj instanceof Integer) {
            return mPreference.getInt(key, (Integer) defaultObj);
        } else if (defaultObj instanceof Boolean) {
            return mPreference.getBoolean(key, (Boolean) defaultObj);
        } else if (defaultObj instanceof Long) {
            return mPreference.getLong(key, (Long) defaultObj);
        } else if (defaultObj instanceof Float) {
            return mPreference.getFloat(key, (Float) defaultObj);
        } else {
            return mPreference.getString(key, defaultObj.toString());
        }

    }

    // 存储键对
    public void save(String key, Object defaultObj) {

        SharedPreferences.Editor edit = mPreference.edit();
        if (defaultObj instanceof String) {
            edit.putString(key, (String) defaultObj);
        } else if (defaultObj instanceof Integer) {
            edit.putInt(key, (Integer) defaultObj);
        } else if (defaultObj instanceof Boolean) {
            edit.putBoolean(key, (Boolean) defaultObj);
        } else if (defaultObj instanceof Long) {
            edit.putLong(key, (Long) defaultObj);
        } else if (defaultObj instanceof Float) {
            edit.putFloat(key, (Float) defaultObj);
        } else {
            edit.putString(key, defaultObj.toString());
        }

        edit.commit();


    }


    // 在原有基础上增加值
    public void saveAndApply(String key, String value) {
        try {
            SharedPreferences.Editor edit = mPreference.edit();
            edit.putString(key, mPreference.getString(key, "") + value);
            edit.commit();
        } catch (Exception ex) {
            LogUtils.e(TAG, ex.getMessage());
        }
    }

    // 删除存储值
    public void delete(String key) {
        try {
            SharedPreferences.Editor edit = mPreference.edit();
            edit.remove(key);
            edit.commit();
        } catch (Exception ex) {
            LogUtils.e(TAG, ex.getMessage());
        }
    }
}
