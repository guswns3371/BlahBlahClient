package com.example.guswn.allthatlyrics.ui.friends.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guswn.allthatlyrics.extension.CircleTransform;
import com.example.guswn.allthatlyrics.extension.MyRetrofit;
import com.example.guswn.allthatlyrics.ui.account.activity.UserinfoEditActivity;
import com.example.guswn.allthatlyrics.response.FollowingResponse;
import com.example.guswn.allthatlyrics.response.userResponse3;
import com.example.guswn.allthatlyrics.api.ChatAPI;
import com.example.guswn.allthatlyrics.response.ChatResponse;
import com.example.guswn.allthatlyrics.ui.videochat.AgoraVideoChatOneActivity;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.api.FriendAPI;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.guswn.allthatlyrics.adapter.MyAdapter_Friend.AddedChatPeopleList;
import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_EMAIL_2;
import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_IDX;

public class InnerFriendActivity extends AppCompatActivity {

    @BindView(R.id.innerf_backimg)
    ImageView innerf_backimg;
    @BindView(R.id.innerf_img)
    ImageView innerf_img;
    @BindView(R.id.innerf_name)
    TextView innerf_name;
    @BindView(R.id.innerf_email)
    TextView innerf_email;
    @BindView(R.id.innerf_intro)
    TextView innerf_intro;
    @BindView(R.id.innerf_chat_btn)
    ImageButton innerf_chat_btn;
    @BindView(R.id.innerf_edit_profile_btn)
    ImageButton innerf_edit_profile_btn;
    @BindView(R.id.innerf_call_btn)
    ImageButton innerf_call_btn;

    @BindView(R.id.innerf_follow_btn)
    ImageButton innerf_follow_btn;
    @BindView(R.id.edit_linear)
    LinearLayout edit_linear;
    @BindView(R.id.call_linear)
    LinearLayout call_linear;

    int flag=0;
    @OnClick(R.id.innerf_follow_btn)
     public void followbtn(){
        switch (flag){
            case 0:
                innerf_follow_btn.setImageResource(R.drawable.following);
                follow_or_unfollow("follow",MY_IDX,friend_idx);
                flag++;
                break;
            case 1:
                innerf_follow_btn.setImageResource(R.drawable.unfollow);
                follow_or_unfollow("unfollow",MY_IDX,friend_idx);
                flag=0;
                break;
        }
    }
    @OnClick(R.id.innerf_edit_profile_btn)
    public void editprofile(){
        intent = new Intent(InnerFriendActivity.this, UserinfoEditActivity.class);
        startActivityForResult(intent,1);
    }
    @OnClick(R.id.innerf_chat_btn)
    public void startchat(){
//        intent = new Intent(InnerFriendActivity.this, InnerChatActivity.class);
//        intent.putExtra("chatperson_idx",friend_idx);
//        startActivityForResult(intent,4);
    }
    @OnClick(R.id.innerf_call_btn)
    public void startcall(){
       Intent intent = new Intent(InnerFriendActivity.this, AgoraVideoChatOneActivity.class);
        startActivity(intent);
    }

    Intent intent;
    String friend_idx;

    FriendAPI api_friend;
    ChatAPI api_chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_friend);
        ButterKnife.bind(this);


        //레트로핏
        api_chat = new MyRetrofit().create(ChatAPI.class);
        api_friend = new MyRetrofit().create(FriendAPI.class);
        //레트로핏

        intent = getIntent();
        friend_idx = intent.getStringExtra("idx");

        if (friend_idx.equals(MY_IDX)){
            innerf_follow_btn.setVisibility(View.GONE);
        }

        loadInnerFriendinfo();
    }

    public void loadInnerFriendinfo(){
        Call<userResponse3> call = api_chat.getOneInfo3_idx(friend_idx);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(InnerFriendActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait");
        progressDoalog.setTitle("Friends Information Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<userResponse3>() {
            @Override
            public void onResponse(Call<userResponse3> call, Response<userResponse3> response) {
                if(!response.isSuccessful()){
                    Log.e("loadInnerFriendinfo_code",""+response.code());
                    return;
                }
                progressDoalog.dismiss();

                userResponse3 val = response.body();
                    String idx = val.getIdx();
                    String email = val.getEmail();
                    String username = val.getUsername();
                    String photo = val.getPhoto();
                    photo = getString(R.string.URL)+photo;
                    String birthday = val.getBirthday();
                    String introduce = val.getIntroduce();
                    List<FollowingResponse> UserFollow = val.getUserfollower();

                    boolean isfollowed = false;
                    for (FollowingResponse follow : UserFollow){
                       String  er = follow.getFollower_idx();
//                       String  ed = follow.getFollowed_idx();
                           if (er.equals(MY_IDX)){
                               isfollowed=true;
                           }
                    }

                    if (isfollowed){
                        flag++;
                        innerf_follow_btn.setImageResource(R.drawable.following);
                    }
                        if(!email.equals(MY_EMAIL_2)){
                            edit_linear.setVisibility(View.GONE);
                        }else {
                            call_linear.setVisibility(View.GONE);
                        }
                        Picasso.with(InnerFriendActivity.this)
                                .load(photo)
                                .transform(new CircleTransform())
                                .placeholder(R.drawable.account)
                                .into(innerf_img);
                        Picasso.with(InnerFriendActivity.this)
                                .load(photo)
                                .placeholder(R.drawable.load)
                                .into(innerf_backimg);
                        innerf_name.setText(username);
                        innerf_email.setText(email);
                        innerf_intro.setText(introduce);


            }

            @Override
            public void onFailure(Call<userResponse3> call, Throwable t) {
                progressDoalog.dismiss();
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
                if (value.equals("1")){
                    if (isfollow.equals("follow")){
                        Toast toast = Toast.makeText(InnerFriendActivity.this,"팔로우 하셨습니다",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,200);
                        toast.show();
                    }else if (isfollow.equals("unfollow")){
                        Toast toast = Toast.makeText(InnerFriendActivity.this,"언팔로우 하셨습니다",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,200);
                        toast.show();
                    }
                }else {
                    Toast toast = Toast.makeText(InnerFriendActivity.this,message,Toast.LENGTH_SHORT);
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

    /** 나중에 하자*/
    public void makechatroom(String ChatPeopleNum ,String ChatPeople_json){
        Map<String,String> map = new HashMap<>();
        map.put("ChatPeopleNum",ChatPeopleNum);
        map.put("ChatPeople",ChatPeople_json);
        map.put("MY_IDX",MY_IDX);

        Call<ChatResponse> call = api_chat.makeChatRoom(map);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait");
        progressDoalog.setTitle("Making Chat Room...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("makechatroom_innerfriend_code",""+response.code());
                    return;
                }progressDoalog.dismiss();


                ChatResponse res = response.body();
                String value= res.getValue();
                String message = res.getMessage();


                Log.e("makechatroom_innerfriend_ChatResponse",value+"/"+message);
                if (value.equals("1")){
                    //액티비티에서 프래그 먼트로 이동
                    //Home 액티비티에서 startActivityForResult 를 이용해
                    //프래그먼트를 이어줌
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result","연산 결과는 입니다.");
                    setResult(RESULT_OK,resultIntent);
                    finish();
                    //액티비티에서 프래그 먼트로 이동
                }else {
                    AddedChatPeopleList.remove(MY_IDX);
                    Toast.makeText(InnerFriendActivity.this,message,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                progressDoalog.dismiss();
                Log.e("makechatroom_innerfriend_fail","Error : "+t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Intent resultIntent;
        if(resultCode == RESULT_OK){
            Log.e("InnerFirend_onActivityResult_requestCode",""+requestCode);
            switch (requestCode){
                // testActivity 에서 요청할 때 보낸 요청 코드 ()
                case 1:
                     resultIntent = new Intent();
                    setResult(RESULT_OK,resultIntent);
                    finish();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                     resultIntent = new Intent();
                    setResult(RESULT_OK,resultIntent);
                    finish();
                    break;
            }

        }
    }

}
