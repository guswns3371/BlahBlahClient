package com.example.guswn.allthatlyrics.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_USER_NAME = "username";
    static final String PREF_USER_EMAIL = "email";
    static final String PREF_USER_PHOTO = "photo";
    static final String PREF_USER_BIRTHDAY = "birthday";
    static final String PREF_USER_INTRODUCE = "introduce";
    static final String PREF_USER_BITMAPIMAGE = "bitmap";
    static final String PREF_USER_IDX = "idx";
    static final String PREF_FIREBASE_TOKEN = "token";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setMY_EMAIL(Context ctx, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, email);
        editor.apply();
    }
    public static void setUserName(Context ctx, String userName,String EMAIL) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(EMAIL+PREF_USER_NAME, userName);
        editor.apply();
    }
    public static void setUserEmail(Context ctx, String email,String EMAIL) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(EMAIL+PREF_USER_EMAIL, email);
        editor.apply();
    }
    public static void setUserPhoto(Context ctx, String photo,String EMAIL) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(EMAIL+PREF_USER_PHOTO, photo);
        editor.apply();
    }
    public static void setUserBirthday(Context ctx, String birthday,String EMAIL) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(EMAIL+PREF_USER_BIRTHDAY, birthday);
        editor.apply();
    }
    public static void setUserIntroduce(Context ctx, String introduce,String EMAIL) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(EMAIL+PREF_USER_INTRODUCE, introduce);
        editor.apply();
    }
    public static void setUserBitmapImage(Context ctx, String base64,String EMAIL) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(EMAIL+PREF_USER_BITMAPIMAGE, base64);
        editor.apply();
    }
    public static void setUserIdx(Context ctx, String useridx,String EMAIL) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(EMAIL+PREF_USER_IDX, useridx);
        editor.apply();
    }
    public static void setFirebaseToken(Context ctx, String firebase_token,String EMAIL) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(EMAIL+PREF_FIREBASE_TOKEN, firebase_token);
        editor.apply();
    }

    // 저장된 정보 가져오기
    public static String getMY_EMAIL(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }
    public static String getUserName(Context ctx,String EMAIL) {
        return getSharedPreferences(ctx).getString(EMAIL+PREF_USER_NAME, "");
    }
    public static String getUserEmail(Context ctx,String EMAIL) {
        return getSharedPreferences(ctx).getString(EMAIL+PREF_USER_EMAIL, "");
    }
    public static String getUserPhoto(Context ctx,String EMAIL) {
        return getSharedPreferences(ctx).getString(EMAIL+PREF_USER_PHOTO, "");
    }
    public static String getUserBirthday(Context ctx,String EMAIL) {
        return getSharedPreferences(ctx).getString(EMAIL+PREF_USER_BIRTHDAY, "");
    }
    public static String getUserIntroduce(Context ctx,String EMAIL) {
        return getSharedPreferences(ctx).getString(EMAIL+PREF_USER_INTRODUCE, "");
    }
    public static String getUserBitmapImage(Context ctx,String EMAIL) {
        return getSharedPreferences(ctx).getString(EMAIL+PREF_USER_BITMAPIMAGE, "");
    }
    public static String getUserIdx(Context ctx,String EMAIL) {
        return getSharedPreferences(ctx).getString(EMAIL+PREF_USER_IDX, "");
    }
    public static String getFirebaseToken(Context ctx,String EMAIL) {
        return getSharedPreferences(ctx).getString(EMAIL+PREF_FIREBASE_TOKEN, "");
    }
    // 로그아웃
    public static void clearUserInfo(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.apply();
    }
    public static void clearMY_EMAIL(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_USER_EMAIL);
        editor.apply();
    }

}
