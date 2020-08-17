package com.hisense.storemode.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jason on 6/7/16.
 */
public class PropertyUtils {

    /**
     * Get the value for the given key.
     *
     * @return an empty string if the key isn't found
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static String get(String key) {
        init();
        String value = null;
        try {
            value = (String) mGetMethod.invoke(mClazz, key);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Get the value for the given key.
     *
     * @return if the key isn't found, return def if it isn't null, or an empty string otherwise
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static String get(String key, String def) {
        init();
        String value = def;
        try {
            value = (String) mGetDefMethod.invoke(mClazz, key, def);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Get the value for the given key, and return as an integer.
     *
     * @param key the key to lookup
     * @param def a default value to return
     * @return the key parsed as an integer, or def if the key isn't found or
     * cannot be parsed
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static int getInt(String key, int def) {
        init();
        int value = def;
        try {
            Integer v = (Integer) mGetIntMethod.invoke(mClazz, key, def);
            value = v.intValue();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Get the value for the given key, returned as a boolean.
     * Values 'n', 'no', '0', 'false' or 'off' are considered false.
     * Values 'y', 'yes', '1', 'true' or 'on' are considered true.
     * (case sensitive).
     * If the key does not exist, or has any other value, then the default
     * result is returned.
     *
     * @param key the key to lookup
     * @param def a default value to return
     * @return the key parsed as a boolean, or def if the key isn't found or is
     * not able to be parsed as a boolean.
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static boolean getBoolean(String key, boolean def) {
        init();
        boolean value = def;
        try {
            Boolean v = (Boolean) mGetBooleanMethod.invoke(mClazz, key, def);
            value = v.booleanValue();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Set the value for the given key.
     *
     * @throws IllegalArgumentException if the key exceeds 32 characters
     * @throws IllegalArgumentException if the value exceeds 92 characters
     */
    public static void set(String key, String val) {
        init();
        try {
            mSetMethod.invoke(mClazz, key, val);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //-------------------------------------------------------------------
    private static Class<?> mClazz = null;
    private static Method mGetMethod = null;
    private static Method mGetIntMethod = null;
    private static Method mGetDefMethod = null;
    private static Method mGetBooleanMethod = null;
    private static Method mSetMethod = null;

    private static void init() {
        try {
            if (mClazz == null) {
                mClazz = Class.forName("android.os.SystemProperties");

                mGetMethod = mClazz.getDeclaredMethod("get", String.class);
                mGetDefMethod = mClazz.getDeclaredMethod("get", String.class, String.class);
                mGetIntMethod = mClazz.getDeclaredMethod("getInt", String.class, int.class);
                mGetBooleanMethod = mClazz.getDeclaredMethod("getBoolean", String.class, boolean.class);

                mSetMethod = mClazz.getDeclaredMethod("set", String.class, String.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
