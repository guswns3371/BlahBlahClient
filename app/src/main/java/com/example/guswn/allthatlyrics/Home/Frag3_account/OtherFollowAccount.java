package com.example.guswn.allthatlyrics.Home.Frag3_account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guswn.allthatlyrics.CircleTransform;
import com.example.guswn.allthatlyrics.Home.Frag1_friends.FollowingResponse;
import com.example.guswn.allthatlyrics.Home.Frag4_chat.ChatAPI;
import com.example.guswn.allthatlyrics.Home.Frag4_chat.InnerChatActivity;
import com.example.guswn.allthatlyrics.Home.Home;
import com.example.guswn.allthatlyrics.Main.Logo;
import com.example.guswn.allthatlyrics.Main.SaveSharedPreference;
import com.example.guswn.allthatlyrics.MainActivity;
import com.example.guswn.allthatlyrics.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

import static com.example.guswn.allthatlyrics.Main.Login.MY_EMAIL;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_EMAIL_2;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_IDX;
import static com.example.guswn.allthatlyrics.MainActivity.URL;
import static com.example.guswn.allthatlyrics.MainActivity.URL_withoutslash;

public class OtherFollowAccount extends AppCompatActivity {
    @BindView(R.id.account_tb)
    Toolbar account_tb;
    @BindView(R.id.account_nametxt)
    TextView account_username;
    @BindView(R.id.account_emailtxt)
    TextView account_email;
    @BindView(R.id.account_introducetxt)
    TextView account_introducetxt;
    @BindView(R.id.account_img)
    ImageView account_img;
    @BindView(R.id.account_edit_btn)
    TextView account_edit_btn;

    @BindView(R.id.content_cnt_txt)
    TextView content_cnt_txt;
    @BindView(R.id.follower_cnt_txt)
    TextView follower_cnt_txt;
    @BindView(R.id.following_cnt_txt)
    TextView following_cnt_txt;

    @BindView(R.id.account_myhistory_imgbtn)
    ImageButton account_myhistory_imgbtn;
    @OnClick(R.id.account_myhistory_imgbtn)
    public void historybtn (){
        account_myhistory_imgbtn.setColorFilter(Color.BLUE);
        account_bookmark_imgbtn.setColorFilter(Color.BLACK);
        account_liked_imgbtn.setColorFilter(Color.BLACK);
        Home_frag3_history home_frag3_history = new Home_frag3_history().newInstance(useridx);
        getSupportFragmentManager().beginTransaction().replace(R.id.account_framelayout,home_frag3_history).commit();
    }

    @BindView(R.id.account_bookmark_imgbtn)
    ImageButton account_bookmark_imgbtn;
    @OnClick(R.id.account_bookmark_imgbtn)
    public void bookmark (){
        account_myhistory_imgbtn.setColorFilter(Color.BLACK);
        account_bookmark_imgbtn.setColorFilter(Color.BLUE);
        account_liked_imgbtn.setColorFilter(Color.BLACK);
        Home_frag3_bookmark home_frag3_bookmark = new Home_frag3_bookmark().newInstance(useridx);
        getSupportFragmentManager().beginTransaction().replace(R.id.account_framelayout,home_frag3_bookmark).commit();
    }

    @BindView(R.id.account_liked_imgbtn)
    ImageButton account_liked_imgbtn;
    @OnClick(R.id.account_liked_imgbtn)
    public void liked (){
        account_myhistory_imgbtn.setColorFilter(Color.BLACK);
        account_bookmark_imgbtn.setColorFilter(Color.BLACK);
        account_liked_imgbtn.setColorFilter(Color.BLUE);
        Home_frag3_like home_frag3_like = new Home_frag3_like().newInstance(useridx);
        getSupportFragmentManager().beginTransaction().replace(R.id.account_framelayout,home_frag3_like).commit();
    }

    @OnClick(R.id.account_edit_btn)
    public void edit(){
        if (account_edit_btn.getText().equals("Edit")){
            intent = new Intent(OtherFollowAccount.this, Userinfo_Edit.class);
            startActivityForResult(intent,3);
        }else if (account_edit_btn.getText().equals("채팅하기")){
            intent = new Intent(OtherFollowAccount.this, InnerChatActivity.class);
            startActivity(intent);
        }

    }

    @OnClick(R.id.follower_cnt_txt)
    public void follower_infos2(){
        Log.e("follow_er_infos ",follower_List.toString());
        Intent intent = new Intent(OtherFollowAccount.this,FollowTab.class);
        intent.putExtra("isfollower_following","follower");
        intent.putStringArrayListExtra("follow_er_List",follower_List);//어레이 리스트 인텐트로 넘기기
        intent.putStringArrayListExtra("follow_ed_List",followed_List);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    @OnClick(R.id.following_cnt_txt)
    public void following_infos2(){
        Log.e("follow_ing_infos ",followed_List.toString());
        Intent intent = new Intent(OtherFollowAccount.this,FollowTab.class);
        intent.putExtra("isfollower_following","following");
        intent.putStringArrayListExtra("follow_er_List",follower_List);//어레이 리스트 인텐트로 넘기기
        intent.putStringArrayListExtra("follow_ed_List",followed_List);
        intent.putExtra("username",username);
       startActivity(intent);
    }

    Intent intent;
    Retrofit retrofit;
    EditAPI api;
    ChatAPI api_chat;
    ArrayList<String> follower_List = new ArrayList<>();
    ArrayList<String> followed_List = new ArrayList<>();
    String username,useridx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_fragment3);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        useridx = intent.getStringExtra("useridx");
        username = intent.getStringExtra("username");
        String userimg = intent.getStringExtra("userimg");

        setSupportActionBar(account_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_black);// 뒤로가기 버튼, 내가 지정할수 있다
        getSupportActionBar().setTitle(username);

        Picasso.with(this)
                .load(URL_withoutslash+userimg)
                .transform(new CircleTransform())
                .placeholder(R.drawable.account)
                .into(account_img);
        if (!useridx.equals(MY_IDX)){
            account_edit_btn.setText("채팅하기");
        }else {
            account_edit_btn.setText("Edit");
        }

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
        api_chat = retrofit.create(ChatAPI.class);
        //레트로핏
        account_myhistory_imgbtn.setColorFilter(Color.BLUE);
        Home_frag3_history home_frag3_history = new Home_frag3_history().newInstance(useridx);
        getSupportFragmentManager().beginTransaction().replace(R.id.account_framelayout,home_frag3_history).commit();

        loadFollowinfo(useridx);
    }

    public void loadFollowinfo(String useridx){
        Call<Value_3> call = api_chat.getOneInfo3_idx(useridx);
        call.enqueue(new Callback<Value_3>() {
            @Override
            public void onResponse(Call<Value_3> call, Response<Value_3> response) {
                if(!response.isSuccessful()){
                    Log.e("loadFollowinfo_code",""+response.code());
                    return;
                }
                Value_3 val = response.body();
                String idx = val.getIdx();
                String email = val.getEmail();
                String username = val.getUsername();
                String photo = val.getPhoto();
                photo = MainActivity.URL+photo;
                String birthday = val.getBirthday();
                String introduce = val.getIntroduce();
                String socialHistorycnt = val.getSocialHistoryCount();
                List<FollowingResponse> UserFollower = val.getUserfollower();
                List<FollowingResponse> UserFollowing = val.getUserfollowing();
                int follower_cnt=0;
                int following_cnt = 0;

                for (FollowingResponse follower : UserFollower){
                    if (!follower.getFollower_idx().equals("null")){
                        String follower_idx = follower.getFollower_idx();
                        follower_List.add(follower_idx);
                        follower_cnt++;
                    }

                }
                for (FollowingResponse follower : UserFollowing){
                    if(!follower.getFollowed_idx().equals("null")){
                        String followed_idx = follower.getFollowed_idx();
                        followed_List.add(followed_idx);
                        following_cnt++;
                    }

                }
                follower_cnt_txt.setText(follower_cnt+"");
                following_cnt_txt.setText(following_cnt+"");
                content_cnt_txt.setText(socialHistorycnt+"");
//                Picasso.with(OtherFollowAccount.this)
//                        .load(photo)
//                        .transform(new CircleTransform())
//                        .placeholder(R.drawable.account)
//                        .into(account_img);
                account_username.setText(username);
                account_email.setText(email);
                account_introducetxt.setText(introduce);
            }

            @Override
            public void onFailure(Call<Value_3> call, Throwable t) {
                Log.e("loadFollowinfo_fail","Error : "+t.getMessage());
            }
        });
    }

    //툴바
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
