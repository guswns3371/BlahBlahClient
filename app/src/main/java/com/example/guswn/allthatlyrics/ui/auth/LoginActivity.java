package com.example.guswn.allthatlyrics.ui.auth;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guswn.allthatlyrics.extension.MyRetrofit;
import com.example.guswn.allthatlyrics.extension.MyPreference;
import com.example.guswn.allthatlyrics.ui.HomeActivity;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.api.RegisterAPI;
import com.example.guswn.allthatlyrics.response.UserResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_EMAIL_2;
import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_IDX;

import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_NAME;
import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_IMG;


public class LoginActivity extends AppCompatActivity {

    public static String MY_EMAIL;
    @BindView(R.id.input_email) EditText input_email;
    @BindView(R.id.input_password) EditText input_password;
    @BindView(R.id.btn_login) Button btn_login;
    @BindView(R.id.link_signup) TextView link_signup;
    RegisterAPI api;
    String email,password;

    @OnClick(R.id.btn_login)
    public void login(){
        email = input_email.getText().toString();
        password = input_password.getText().toString();

        Map<String,String> map = new HashMap<>();
        map.put("Email",email);
//        map.put("Idx",MY_IDX);
        map.put("Password",password);
        map.put("Token", FirebaseInstanceId.getInstance().getToken());
        Call<UserResponse> call = api.loginUser(map);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(LoginActivity.this,response.code()+"",Toast.LENGTH_SHORT).show();
                    return;
                }
                UserResponse val = response.body();
                String value = val.getValue();
                String message = val.getMessage();
                String username = val.getUsername();
                String email = val.getEmail();
                String img = val.getPhoto();
                String idx = val.getIdx();
                Log.e("Login_log_username_idx",username+"/"+idx);

                if(value.equals("1")){
                    //로그인 성공
                    MyPreference.setUserEmail(LoginActivity.this,email,email);
                    MyPreference.setMY_EMAIL(LoginActivity.this,email);
                    MyPreference.setUserIdx(LoginActivity.this,idx,email);
                    MyPreference.setUserName(LoginActivity.this, username,email);
                    MyPreference.setUserPhoto(LoginActivity.this,img,email);
                    MY_IDX = MyPreference.getUserIdx(LoginActivity.this,email);
                    MY_NAME = MyPreference.getUserName(LoginActivity.this,email);
                    MY_EMAIL = MyPreference.getUserEmail(LoginActivity.this,email);
                    MY_EMAIL_2 = MyPreference.getUserEmail(LoginActivity.this,email);
                    MY_IMG = MyPreference.getUserPhoto(LoginActivity.this,email);
                    Log.e("Login_log_MY_EMAIL/MY_EMAIL_2/MY_IDX/MY_NAME/MY_IMG",MY_EMAIL+"/"+MY_EMAIL_2+"/"+MY_IDX+"/"+MY_NAME+"/"+MY_IMG);
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    //Toast.makeText(Login.this,email,Toast.LENGTH_SHORT).show();
                }else {
                    //로그인 실패
                    if(value.equals("2")){
                        //이메일존재 +비밀번호 틀림
                        input_email.setTextColor(Color.BLACK);
                        input_password.setTextColor(Color.RED);
                        input_password.setError(message);
                    }else if(value.equals("3")){
                        //존재하지 않은 이메일
                        input_email.setTextColor(Color.RED);
                        input_password.setTextColor(Color.BLACK);
                        input_email.setError(message);
                    }

                   // Toast.makeText(Login.this,message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.link_signup)
    public void registerclick(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        api = new MyRetrofit().create(RegisterAPI.class);

    }
}
