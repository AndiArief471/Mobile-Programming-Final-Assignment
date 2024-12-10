package com.example.mobileprogfinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    static final String UserName = "user";
    static final String UserEmail = "email@gmail.com";
    static final String Key_Status_Login = "status";

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserName(Context context, String userName) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(UserName, userName);
        editor.apply();
    }

    public static String getUserName(Context context) {
        return getSharedPreference(context).getString(UserName, "");
    }

    public static void setUserEmail(Context context, String userEmail) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(UserEmail, userEmail);
        editor.apply();
    }

    public static String getUserEmail(Context context) {
        return getSharedPreference(context).getString(UserEmail, "");
    }

    public static void setStatusLogin(Context context, boolean status) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(Key_Status_Login, status);
        editor.apply();
    }

    public static boolean getStatusLogin(Context context) {
        return getSharedPreference(context).getBoolean(Key_Status_Login, false);
    }

    public static void clearLoggedUser(Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(UserName);
        editor.remove(UserEmail);
        editor.remove(Key_Status_Login);
        editor.apply();
    }
}
