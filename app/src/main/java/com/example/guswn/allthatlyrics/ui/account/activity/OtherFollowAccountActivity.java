package com.example.guswn.allthatlyrics.ui.account.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guswn.allthatlyrics.api.FriendAPI;
import com.example.guswn.allthatlyrics.extension.CircleTransform;
import com.example.guswn.allthatlyrics.extension.MyRetrofit;
import com.example.guswn.allthatlyrics.model.FollowTabModel;
import com.example.guswn.allthatlyrics.response.FollowingResponse;
import com.example.guswn.allthatlyrics.api.ChatAPI;
import com.example.guswn.allthatlyrics.ui.account.followtab.FollowTab;
import com.example.guswn.allthatlyrics.ui.account.inner.BookmarkFragment;
import com.example.guswn.allthatlyrics.ui.account.inner.HistoryFragment;
import com.example.guswn.allthatlyrics.ui.account.inner.LikeFragment;
import com.example.guswn.allthatlyrics.ui.chat.activity.InnerChatActivity;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.api.EditAPI;
import com.example.guswn.allthatlyrics.response.userResponse3;
import com.example.guswn.allthatlyrics.ui.friends.activity.InnerFriendActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_EMAIL_2;
import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_IDX;

public class OtherFollowAccountActivity extends AppCompatActivity {
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
        HistoryFragment historyFragment = new HistoryFragment().newInstance(useridx);
        getSupportFragmentManager().beginTransaction().replace(R.id.account_framelayout, historyFragment).commit();
    }

    @BindView(R.id.account_bookmark_imgbtn)
    ImageButton account_bookmark_imgbtn;
    @OnClick(R.id.account_bookmark_imgbtn)
    public void bookmark (){
        account_myhistory_imgbtn.setColorFilter(Color.BLACK);
        account_bookmark_imgbtn.setColorFilter(Color.BLUE);
        account_liked_imgbtn.setColorFilter(Color.BLACK);
        BookmarkFragment bookmarkFragment = new BookmarkFragment().newInstance(useridx);
        getSupportFragmentManager().beginTransaction().replace(R.id.account_framelayout, bookmarkFragment).commit();
    }

    @BindView(R.id.account_liked_imgbtn)
    ImageButton account_liked_imgbtn;
    @OnClick(R.id.account_liked_imgbtn)
    public void liked (){
        account_myhistory_imgbtn.setColorFilter(Color.BLACK);
        account_bookmark_imgbtn.setColorFilter(Color.BLACK);
        account_liked_imgbtn.setColorFilter(Color.BLUE);
        LikeFragment likeFragment = new LikeFragment().newInstance(useridx);
        getSupportFragmentManager().beginTransaction().replace(R.id.account_framelayout, likeFragment).commit();
    }

    @OnClick(R.id.account_edit_btn)
    public void edit(){
        if (account_edit_btn.getText().equals("Edit")){
            intent = new Intent(OtherFollowAccountActivity.this, UserinfoEditActivity.class);
            startActivityForResult(intent,3);
            }else {
            if (!AmIFollowHim){
                setButtonFollow();
                follow_or_unfollow("follow",MY_IDX,useridx);
            }else {
                setButtonUnfollow();
                follow_or_unfollow("unfollow",MY_IDX,useridx);
            }
        }

    }

    @OnClick(R.id.follower_cnt_txt)
    public void follower_infos2(){
        Log.e("follow_er_infos ",follower_List.toString());
        Intent intent = new Intent(OtherFollowAccountActivity.this, FollowTab.class);
        intent.putExtra("isfollower_following","follower");
        intent.putStringArrayListExtra("follow_er_List",follower_List);//어레이 리스트 인텐트로 넘기기
        intent.putStringArrayListExtra("follow_ed_List",followed_List);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    @OnClick(R.id.following_cnt_txt)
    public void following_infos2(){
        Log.e("follow_ing_infos ",followed_List.toString());
        Intent intent = new Intent(OtherFollowAccountActivity.this,FollowTab.class);
        intent.putExtra("isfollower_following","following");
        intent.putStringArrayListExtra("follow_er_List",follower_List);//어레이 리스트 인텐트로 넘기기
        intent.putStringArrayListExtra("follow_ed_List",followed_List);
        intent.putExtra("username",username);
       startActivity(intent);
    }

    Intent intent;
    EditAPI api;
    ChatAPI api_chat;
    FriendAPI api_friend;

    ArrayList<String> follower_List = new ArrayList<>();
    ArrayList<String> followed_List = new ArrayList<>();
    ArrayList<String> infoList;
    boolean AmIFollowHim = false;
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
                .load(getString(R.string.URL)+userimg)
                .transform(new CircleTransform())
                .placeholder(R.drawable.account)
                .into(account_img);


        //레트로핏
        api = new MyRetrofit().create(EditAPI.class);
        api_chat = new MyRetrofit().create(ChatAPI.class);
        api_friend = new MyRetrofit().create(FriendAPI.class);
        //레트로핏
        account_myhistory_imgbtn.setColorFilter(Color.BLUE);
        HistoryFragment historyFragment = new HistoryFragment().newInstance(useridx);
        getSupportFragmentManager().beginTransaction().replace(R.id.account_framelayout, historyFragment).commit();

        loadFollowInfoByIdx(useridx);
        loadInnerFriendinfo();

    }

    public void loadFollowInfoByIdx(String useridx){
        Call<userResponse3> call = api_chat.getOneInfo3_idx(useridx);
        call.enqueue(new Callback<userResponse3>() {
            @Override
            public void onResponse(Call<userResponse3> call, Response<userResponse3> response) {
                if(!response.isSuccessful()){
                    Log.e("loadFollowinfo_code",""+response.code());
                    return;
                }
                userResponse3 val = response.body();
                String idx = val.getIdx();
                String email = val.getEmail();
                String username = val.getUsername();
                String photo = val.getPhoto();
                photo = getString(R.string.URL)+photo;
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
                account_username.setText(username);
                account_email.setText(email);
                account_introducetxt.setText(introduce);
            }

            @Override
            public void onFailure(Call<userResponse3> call, Throwable t) {
                Log.e("loadFollowinfo_fail","Error : "+t.getMessage());
            }
        });
    }

    public void loadInnerFriendinfo(){
        Call<userResponse3> call = api_chat.getOneInfo3_idx(useridx);
        call.enqueue(new Callback<userResponse3>() {
            @Override
            public void onResponse(Call<userResponse3> call, Response<userResponse3> response) {
                if(!response.isSuccessful()){
                    Log.e("loadInnerFriendinfo_code",""+response.code());
                    return;
                }
                userResponse3 val = response.body();
                List<FollowingResponse> UserFollow = val.getUserfollower();

                boolean isfollowed = false;
                for (FollowingResponse follow : UserFollow){
                    String  er = follow.getFollower_idx();
                    if (er.equals(MY_IDX)){
                        isfollowed=true;
                    }
                }

                if (isfollowed){
                    setButtonFollow();
                }else {
                    setButtonUnfollow();
                }

                if (useridx.equals(MY_IDX)){
                    account_edit_btn.setTextColor(Color.BLACK);
                    account_edit_btn.setBackgroundResource(R.drawable.mybtn_1);
                    account_edit_btn.setText("Edit");
                }
            }

            @Override
            public void onFailure(Call<userResponse3> call, Throwable t) {
                Log.e("loadInnerFriendinfo_fail","Error : "+t.getMessage());
            }
        });
    }

    public void follow_or_unfollow(String isfollow,String follow_er_idx,String follow_ed_idx){
        Map<String,String> map = new HashMap<>();
        map.put("isfollow",isfollow);
        map.put("follow_er_idx",follow_er_idx);
        map.put("follow_ed_idx",follow_ed_idx);

        Call<FollowingResponse> call = api_friend.make_follow_unfollow(map);

        call.enqueue(new Callback<FollowingResponse>() {
            @Override
            public void onResponse(Call<FollowingResponse> call, Response<FollowingResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("follow_or_unfollow_code",""+response.code());
                    return;
                }

                FollowingResponse  res = response.body();
                String value = res.getValue();
                String message = res.getMessage();
                String isfollow = res.getIsfollow();
                String er = res.getFollower_idx();
                String ed = res.getFollowed_idx();
                Log.e("follow_or_unfollow ",value+"/"+message+"/"+isfollow+"/"+er+"/"+ed);
                if (!value.equals("1")) {
                    Toast toast = Toast.makeText(OtherFollowAccountActivity.this,message,Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,200);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<FollowingResponse> call, Throwable t) {
                Log.e("follow_or_unfollow_fail","Error : "+t.getMessage());
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

    private void setButtonFollow(){
        account_edit_btn.setText("팔로잉");
        account_edit_btn.setTextColor(Color.BLACK);
        account_edit_btn.setBackgroundResource(R.drawable.mybtn_1);
        AmIFollowHim = true;
    }
    private void setButtonUnfollow(){
        account_edit_btn.setText("팔로우");
        account_edit_btn.setTextColor(Color.WHITE);
        account_edit_btn.setBackgroundResource(R.drawable.mybtn_2);
        AmIFollowHim = false;
    }
}
