package com.example.guswn.allthatlyrics.firebase;

import android.util.Log;

import com.example.guswn.allthatlyrics.extension.App;
import com.example.guswn.allthatlyrics.extension.MyPreference;
import com.example.guswn.allthatlyrics.R;
import com.google.firebase.iid.FirebaseInstanceId;

import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_EMAIL_2;
import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_IDX;
import static com.example.guswn.allthatlyrics.extension.Extensions.isNullOrEmpty;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token : " + refreshedToken);

        if (!isNullOrEmpty(refreshedToken)){
          if (!isNullOrEmpty(MyPreference.getFirebaseToken(FirebaseInstanceIDService.this,MY_EMAIL_2))){
              //토큰에 데이터가 없는 경우에만 저장
              MyPreference.setFirebaseToken(FirebaseInstanceIDService.this,refreshedToken,MY_EMAIL_2);
              Log.d(TAG, "Refreshed token _SaveSharedPreference : " + MyPreference.getFirebaseToken(FirebaseInstanceIDService.this,MY_EMAIL_2));
          }

          if (!isNullOrEmpty(MY_EMAIL_2)){
              //로그인 상태일 경우에는 서버로 보낸다.
              sendRegistrationToServer(refreshedToken);
              Log.d(TAG, "Refreshed token _Update: " + refreshedToken);
//              if (!refreshedToken.equals(SaveSharedPreference.getFirebaseToken(FirebaseInstanceIDService.this,MY_EMAIL_2))){
//                  //기존에 저장된 토큰과 비교하여 다를 경우에만 서버 업데이트
//                  SaveSharedPreference.setFirebaseToken(FirebaseInstanceIDService.this,refreshedToken,MY_EMAIL_2);
//                  Log.d(TAG, "Refreshed token _Update: " + refreshedToken);
//                  sendRegistrationToServer(refreshedToken);
//              }
          }
        }
       // sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //FCM 토큰 서버에 갱신
        // Add custom implementation, as needed.
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .add("Useridx",MY_IDX)
                .build();

        //request
        Request request = new Request.Builder()
                .url(App.getInstance().getString(R.string.URL)
                        +"Chatting/FCM/save_fcm_token.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

