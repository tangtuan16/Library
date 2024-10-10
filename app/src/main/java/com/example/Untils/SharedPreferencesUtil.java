package com.example.Untils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static final String PREF_NAME = "USER_PREF";
    private static final String KEY_USER_ID = "USER_ID";

    public static void saveUserId(Context context, int userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public static int getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public static void clearUserId(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(KEY_USER_ID);
        editor.apply();
    }
}