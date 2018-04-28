package com.yxclient.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.HashMap;

public class SharedPreUtil {
    private static String filename = "cookie";

    /**
     * ȡ��SharedPreference�ж�������
     *
     * @param context
     * @param name
     * @param listKey
     * @return
     * @description
     */
    public static ArrayList<String> getValue(Context context, String name,
                                             ArrayList<String> listKey) {
        ArrayList<String> listresult = new ArrayList<String>();
        SharedPreferences share = context.getSharedPreferences(name,
                0);
        for (int i = 0; i < listKey.size(); i++) {
            listresult.add(share.getString(listKey.get(i), "value"));
        }
        return listresult;
    }

    /**
     * ������ֵ����Ӧ��SharedPreference
     *
     * @param context
     * @param name
     * @param map
     * @description
     */
    public static void putValue(Context context, String name,
                                HashMap<String, String> map) {

        SharedPreferences share = context.getSharedPreferences(name,
                0);
        Editor editor = share.edit();// ��ȡ�༭
        for (String str : map.keySet()) {
            editor.putString(str, map.get(str));
        }
        editor.commit();
        editor.clear();
    }

    public static void putValue(Context context, HashMap<String, Object> map) {

        SharedPreferences share = context.getSharedPreferences(filename,
                0);
        Editor editor = share.edit();// ��ȡ�༭
        for (String str : map.keySet()) {
            if (map.get(str) instanceof String)
                editor.putString(str, (String) map.get(str));
            else if (map.get(str) instanceof Integer)
                editor.putInt(str, (Integer) map.get(str));
            else if (map.get(str) instanceof Float)
                editor.putFloat(str, (Float) map.get(str));
            else if (map.get(str) instanceof Long)
                editor.putLong(str, (Long) map.get(str));
            else if (map.get(str) instanceof Boolean)
                editor.putBoolean(str, (Boolean) map.get(str));
        }
        editor.commit();
        editor.clear();
    }

    /**
     * ����ֵ����ʱ�ļ�
     *
     * @param context
     * @param name
     * @param key
     * @param value
     * @description
     */
    public static void putValue(Context context, String name, String key,
                                String value) {
        SharedPreferences share = context.getSharedPreferences(name,
                0);
        Editor editor = share.edit();// ��ȡ�༭
        if (key != null && value != null) {
            editor.putString(key, value);
        }
        editor.commit();
        editor.clear();
    }

    /**
     * ��һ�������
     *
     * @param context
     * @param name
     * @param key
     * @return
     * @description
     */
    public static String getValue(Context context, String name, String key,
                                  String temp) {
        String str = "";
        SharedPreferences share = context.getSharedPreferences(name,
                0);
        str = share.getString(key, temp);
        return str;
    }

    /**
     * �����ʱ�ļ��е�
     *
     * @param context
     * @param name
     * @description
     */
    public static void clearValue(Context context, String name) {
        SharedPreferences share = context.getSharedPreferences(name,
                0);
        Editor editor = share.edit();
        editor.clear();
        editor.commit();
    }

    public static void putValue(Context context, String key, String value) {
        SharedPreferences share = context.getSharedPreferences(filename,
                0);
        Editor editor = share.edit();// ��ȡ�༭
        if (key != null && value != null) {
            editor.putString(key, value);
        }
        editor.commit();
        editor.clear();
    }

    public static String getValue(Context context, String key, String temp) {
        String str = temp;
        try {
            SharedPreferences share = context.getSharedPreferences(filename,
                    0);
            str = share.getString(key, temp);
        } catch (Exception ignored) {

        }
        return str;
    }
}
