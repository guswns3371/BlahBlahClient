package com.example.guswn.allthatlyrics.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.guswn.allthatlyrics.extension.SaveSharedPreference;
import com.example.guswn.allthatlyrics.home.Home;
import com.example.guswn.allthatlyrics.R;

public class Logo extends AppCompatActivity {

    Intent intent;
    public static String MY_EMAIL_2;
    public static String MY_IDX;
    public static String MY_NAME;
    public static String MY_IMG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

//        FirebaseMessaging.getInstance().subscribeToTopic("news");
//        FirebaseInstanceId.getInstance().getToken();
//        Log.e("FirebaseIIDService_Logo_getToken()",FirebaseInstanceId.getInstance().getToken());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    if(SaveSharedPreference.getMY_EMAIL(Logo.this).length() == 0) {
                        // call Login Activity
                        intent = new Intent(Logo.this, Login.class);
                        startActivity(intent);
                        Logo.this.finish();
                    } else {
                        // Call Next Activity

                        Log.e("Logo_log_MY_EMAIL_2",SaveSharedPreference.getMY_EMAIL(Logo.this));
                        MY_EMAIL_2 = SaveSharedPreference.getMY_EMAIL(Logo.this);
                        MY_IDX = SaveSharedPreference.getUserIdx(Logo.this,MY_EMAIL_2);
                        MY_NAME = SaveSharedPreference.getUserName(Logo.this,MY_EMAIL_2);
                        MY_IMG = SaveSharedPreference.getUserPhoto(Logo.this,MY_EMAIL_2);
                        Log.e("Logo_log_MY_IDX/MY_EMAIL_2/MY_NAME/MY_IMG",MY_IDX+"/"+MY_EMAIL_2+"/"+MY_NAME+"/"+MY_IMG);
                        intent = new Intent(Logo.this, Home.class);
                        intent.putExtra("STD_NUM", SaveSharedPreference.getMY_EMAIL(Logo.this).toString());
                        startActivity(intent);
                        Logo.this.finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


}
