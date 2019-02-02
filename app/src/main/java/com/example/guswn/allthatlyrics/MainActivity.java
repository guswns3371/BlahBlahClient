package com.example.guswn.allthatlyrics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.guswn.allthatlyrics.Home.Frag3_account.EditAPI;
import com.example.guswn.allthatlyrics.Home.Frag3_account.Value_2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.guswn.allthatlyrics.Main.Logo.MY_EMAIL_2;

public class MainActivity extends AppCompatActivity {

    //1
    //#3e2723 - 기본색
    //#6a4f4b - 밝은색
    //#1b0000 - 어두운 색

    //2
    //#efebe9 - 기본색
    //#ffffff - 밝은색
    //#bdb9b7 - 어두운 색
    public static String URL_withoutslash = "http://13.209.89.121";
    public static String URL = URL_withoutslash+"/";

    Retrofit retrofit;
    MainAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //레트로핏

//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//
//        api = retrofit.create(MainAPI.class);
        //레트로핏

//        Date today = new Date();
//        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
//        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
//        String DATE = date.format(today);
//        String TIME = time.format(today);
    }
    public static void showKeyboard(EditText mEtSearch, Context context) {
        mEtSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static void hideSoftKeyboard(EditText mEtSearch, Context context) {
        mEtSearch.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);


    }

    static public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    static public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    @NonNull
    static public RequestBody createPartFromString (String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM,descriptionString
        );
    }

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }
}
