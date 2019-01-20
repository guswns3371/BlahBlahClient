package com.example.guswn.allthatlyrics.Home.Frag3_account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.guswn.allthatlyrics.Home.Frag1_friends.FriendAPI;
import com.example.guswn.allthatlyrics.Home.Frag1_friends.FriendInfo;
import com.example.guswn.allthatlyrics.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.guswn.allthatlyrics.Main.Logo.MY_EMAIL_2;
import static com.example.guswn.allthatlyrics.MainActivity.URL;

public class Follow_info_activity extends AppCompatActivity {

    @BindView(R.id.follow_tb)
    Toolbar follow_tb;
    @BindView(R.id.follow_RV)
    RecyclerView follow_RV;

    Retrofit retrofit;
    EditAPI api;
    String isfollower_following;
    ArrayList<String> Follow_InfoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_info_activity);
        ButterKnife.bind(this);
        //레트로핏
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        api = retrofit.create(EditAPI.class);
        //레트로핏

        //툴바
        setSupportActionBar(follow_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        //툴바


        Intent intent = getIntent();
        isfollower_following = intent.getStringExtra("isfollower_following");

        if (isfollower_following.equals("follower")){
            getSupportActionBar().setTitle("Follower");
            Follow_InfoList = intent.getStringArrayListExtra("info_list");
            Log.e("Follower_InfoList ",Follow_InfoList.toString());//어레이 리스트 인텐트로 받기
        }else if (isfollower_following.equals("following")){
            getSupportActionBar().setTitle("Following");
            Follow_InfoList = intent.getStringArrayListExtra("info_list");
            Log.e("Following_InfoList ",Follow_InfoList.toString());
        }

        getAlluserInfo();

    }

    public void getAlluserInfo(){
        Call<Value_3> call = api.getallInfo();

        call.enqueue(new Callback<Value_3>() {
            @Override
            public void onResponse(Call<Value_3> call, Response<Value_3> response) {
                if(!response.isSuccessful()){
                    Log.e("getAlluserInfo_code",""+response.code());
                    return;
                }
                Value_3 val3 = response.body();
                List<Value_3> value = val3.getUserinfolist();

                for(Value_3 val : value){
                    String idx = val.getIdx();
                    String email = val.getEmail();
                    String username = val.getUsername();
                    String token = val.getToken();
                    String photo = val.getPhoto();
                    String birthday = val.getBirthday();
                    String introduce = val.getIntroduce();
                    for (int i=0; i<Follow_InfoList.size();i++){
                        if (idx.equals(Follow_InfoList.get(i))){
                            Log.e("idx_Follow_InfoList.get(i)",idx+"/"+Follow_InfoList.get(i));


                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<Value_3> call, Throwable t) {
                Log.e("getAlluserInfo_fail","Error : "+t.getMessage());
            }
        });
    }

    //툴바
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.userinfo_edit_tb, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //툴바
}
