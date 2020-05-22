package com.example.guswn.allthatlyrics.ui.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.guswn.allthatlyrics.extension.MyPreference;
import com.example.guswn.allthatlyrics.ui.HomeActivity;
import com.example.guswn.allthatlyrics.R;

public class LogoActivity extends AppCompatActivity {

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
                    if(MyPreference.getMY_EMAIL(LogoActivity.this).length() == 0) {
                        // call Login Activity
                        intent = new Intent(LogoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        LogoActivity.this.finish();
                    } else {
                        // Call Next Activity

                        Log.e("Logo_log_MY_EMAIL_2", MyPreference.getMY_EMAIL(LogoActivity.this));
                        MY_EMAIL_2 = MyPreference.getMY_EMAIL(LogoActivity.this);
                        MY_IDX = MyPreference.getUserIdx(LogoActivity.this,MY_EMAIL_2);
                        MY_NAME = MyPreference.getUserName(LogoActivity.this,MY_EMAIL_2);
                        MY_IMG = MyPreference.getUserPhoto(LogoActivity.this,MY_EMAIL_2);
                        Log.e("Logo_log_MY_IDX/MY_EMAIL_2/MY_NAME/MY_IMG",MY_IDX+"/"+MY_EMAIL_2+"/"+MY_NAME+"/"+MY_IMG);
                        intent = new Intent(LogoActivity.this, HomeActivity.class);
                        intent.putExtra("STD_NUM", MyPreference.getMY_EMAIL(LogoActivity.this).toString());
                        startActivity(intent);
                        LogoActivity.this.finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


}
