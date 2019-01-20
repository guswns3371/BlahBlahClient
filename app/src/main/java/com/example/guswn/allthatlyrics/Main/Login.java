package com.example.guswn.allthatlyrics.Main;

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

import com.example.guswn.allthatlyrics.Home.Home;
import com.example.guswn.allthatlyrics.R;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.guswn.allthatlyrics.Main.Logo.MY_EMAIL_2;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_IDX;

import static com.example.guswn.allthatlyrics.Main.Logo.MY_NAME;
import static com.example.guswn.allthatlyrics.MainActivity.URL;


public class Login extends AppCompatActivity {

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
        Call<Value> call = api.loginUser(map);

        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Login.this,response.code()+"",Toast.LENGTH_SHORT).show();
                    return;
                }
                Value val = response.body();
                String value = val.getValue();
                String message = val.getMessage();
                String username = val.getUsername();
                String email = val.getEmail();
                String idx = val.getIdx();
                Log.e("Login_log_username_idx",username+"/"+idx);

                if(value.equals("1")){
                    //로그인 성공
                    SaveSharedPreference.setUserEmail(Login.this,email,email);
                    SaveSharedPreference.setMY_EMAIL(Login.this,email);
                    SaveSharedPreference.setUserIdx(Login.this,idx,email);
                    SaveSharedPreference.setUserName(Login.this, username,email);
                    MY_IDX = SaveSharedPreference.getUserIdx(Login.this,email);
                    MY_NAME = SaveSharedPreference.getUserName(Login.this,email);
                    MY_EMAIL = SaveSharedPreference.getUserEmail(Login.this,email);
                    MY_EMAIL_2 = SaveSharedPreference.getUserEmail(Login.this,email);
                    Log.e("Login_log_MY_EMAIL/MY_EMAIL_2/MY_IDX/MY_NAME",MY_EMAIL+"/"+MY_EMAIL_2+"/"+MY_IDX+"/"+MY_NAME);
                    Intent intent = new Intent(Login.this,Home.class);
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
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(Login.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    @OnClick(R.id.link_signup)
    public void registerclick(){
        Intent intent = new Intent(Login.this,Register.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        api = retrofit.create(RegisterAPI.class);

    }
}
