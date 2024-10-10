package com.example.Untils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    public static int getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("USER_ID", -1);
    }
}
