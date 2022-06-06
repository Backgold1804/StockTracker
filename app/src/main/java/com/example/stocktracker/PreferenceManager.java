package com.example.stocktracker;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    public static final String PREFERENCE_NAME = "autoLogin";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    //  autoLogin 설정하기
    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = getPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //  autoLogin 불러오기
    public static String getString(Context context, String key) {
        SharedPreferences sp = getPreferences(context);
        String value = sp.getString(key, "");
        return value;
    }

    //  autoLogin 초기화
    public static void clear(Context context) {
        SharedPreferences sp = getPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
