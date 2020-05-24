package com.example.guswn.allthatlyrics.ui.account.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.guswn.allthatlyrics.extension.MyRetrofit;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.api.EditAPI;
import com.example.guswn.allthatlyrics.response.userResponse3;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_IDX;

public class FollowInfoActivity extends AppCompatActivity {

    @BindView(R.id.follow_tb)
    Toolbar follow_tb;
    @BindView(R.id.follow_RV)
    RecyclerView follow_RV;

    EditAPI api;
    String isfollower_following;
    ArrayList<String> Follow_InfoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_info_activity);
        ButterKnife.bind(this);
        //레트로핏
        api = new MyRetrofit().create(EditAPI.class);
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
        Call<userResponse3> call = api.getallInfo(MY_IDX);

        call.enqueue(new Callback<userResponse3>() {
            @Override
            public void onResponse(Call<userResponse3> call, Response<userResponse3> response) {
                if(!response.isSuccessful()){
                    Log.e("getAlluserInfo_code",""+response.code());
                    return;
                }
                userResponse3 val3 = response.body();
                List<userResponse3> value = val3.getUserinfolist();

                for(userResponse3 val : value){
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
            public void onFailure(Call<userResponse3> call, Throwable t) {
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
