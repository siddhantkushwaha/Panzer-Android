package com.panzer.androidapp;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class LocalStorage {

    public static void setString(Context context, String bucketName, String key, String val) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(bucketName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static String getString(Context context, String bucketName, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(bucketName, MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
}
