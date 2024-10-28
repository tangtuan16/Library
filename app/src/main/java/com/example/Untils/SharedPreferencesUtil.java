package com.example.Untils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SharedPreferencesUtil {
    private static final String PREF_NAME = "USER_PREF";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_IS_LOGGED_IN = "IS_LOGGED_IN";

    private static SharedPreferences getSharedPreferences(@NonNull Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveUserLoginState(Context context, int userId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public static int getUserId(Context context) {
        return getSharedPreferences(context).getInt(KEY_USER_ID, -1);
    }

    public static void clearUserLoginState(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_USER_ID);
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }
}