package com.example.guswn.allthatlyrics.Home.Frag3_account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guswn.allthatlyrics.CircleTransform;
import com.example.guswn.allthatlyrics.Home.Frag1_friends.FollowingResponse;
import com.example.guswn.allthatlyrics.Home.Frag4_chat.ChatAPI;
import com.example.guswn.allthatlyrics.Home.Home;
import com.example.guswn.allthatlyrics.Main.SaveSharedPreference;
import com.example.guswn.allthatlyrics.MainActivity;
import com.example.guswn.allthatlyrics.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

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

public class Home_fragment3 extends Fragment {

    @BindView(R.id.account_tb)
    Toolbar account_tb;
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
    @BindView(R.id.account_nametxt)
    TextView account_nametxt;
    @BindView(R.id.account_emailtxt)
    TextView account_emailtxt;
    @BindView(R.id.account_introducetxt)
    TextView account_introducetxt;

    @BindView(R.id.account_myhistory_imgbtn)
    ImageButton account_myhistory_imgbtn;
    @OnClick(R.id.account_myhistory_imgbtn)
    public void historybtn (){
        Home_frag3_history home_frag3_history = new Home_frag3_history().newInstance(MY_IDX);
        getFragmentManager().beginTransaction().replace(R.id.account_framelayout,home_frag3_history).commit();
    }

    @BindView(R.id.account_bookmark_imgbtn)
    ImageButton account_bookmark_imgbtn;
    @OnClick(R.id.account_bookmark_imgbtn)
    public void bookmark (){
        Home_frag3_bookmark home_frag3_bookmark = new Home_frag3_bookmark().newInstance(MY_IDX);
        getFragmentManager().beginTransaction().replace(R.id.account_framelayout,home_frag3_bookmark).commit();
    }

    @BindView(R.id.account_liked_imgbtn)
    ImageButton account_liked_imgbtn;
    @OnClick(R.id.account_liked_imgbtn)
    public void liked (){
        Home_frag3_like home_frag3_like = new Home_frag3_like().newInstance(MY_IDX);
        getFragmentManager().beginTransaction().replace(R.id.account_framelayout,home_frag3_like).commit();
    }



    @BindView(R.id.account_framelayout)
    FrameLayout account_framelayout;

//    @BindView(R.id.swipeRefreshLo)
//    SwipeRefreshLayout swipeRefreshLayout;

    @OnClick(R.id.follower_cnt_txt)
    public void follower_infos(){
        Log.e("follow_er_infos ",follower_List.toString());
        Intent intent = new Intent(getActivity(),FollowTab.class);
        intent.putExtra("isfollower_following","follower");
        intent.putStringArrayListExtra("follow_er_List",follower_List);//어레이 리스트 인텐트로 넘기기
        intent.putStringArrayListExtra("follow_ed_List",followed_List);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.following_cnt_txt)
    public void following_infos(){
        Log.e("follow_ing_infos ",followed_List.toString());
        Intent intent = new Intent(getActivity(),FollowTab.class);
        intent.putExtra("isfollower_following","following");
        intent.putStringArrayListExtra("follow_er_List",follower_List);//어레이 리스트 인텐트로 넘기기
        intent.putStringArrayListExtra("follow_ed_List",followed_List);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.account_edit_btn)
    public void edit(){
        intent = new Intent(getActivity(), Userinfo_Edit.class);
        getActivity().startActivityForResult(intent,3);
    }

    Intent intent;
    Retrofit retrofit;
    EditAPI api;
    ChatAPI api_chat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment3,container,false);
        ButterKnife.bind(this,view);

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


        Home activity = (Home) getActivity();
        activity.setSupportActionBar(account_tb);
        ActionBar actionBar = ((Home) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        if(MY_EMAIL==null){
            MY_EMAIL = MY_EMAIL_2;
        }
        actionBar.setTitle(SaveSharedPreference.getUserName(getActivity(),MY_EMAIL));

        setHasOptionsMenu(true);
        /**fragment test success*/
        Home_frag3_history home_frag3_history = new Home_frag3_history().newInstance(MY_IDX);
        getFragmentManager().beginTransaction().replace(R.id.account_framelayout,home_frag3_history).commit();

        loadMYinfo();
        /**실험*/
        //이미지뷰
//        File imgFile = new  File(SaveSharedPreference.getUserPhoto(getActivity(),MY_EMAIL));
//        if(imgFile.exists()){
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            account_img.setImageBitmap(myBitmap);
//        }
//        if(SaveSharedPreference.getUserPhoto(getActivity(),MY_EMAIL)!=null){
//            Bitmap decodedByte = StringToBitMap(SaveSharedPreference.getUserPhoto(getActivity(),MY_EMAIL));
//            account_img.setImageBitmap(decodedByte);
//        }

//        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_account_tollbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_setting :
                Intent intent = new Intent(getActivity(), Setting.class);
                startActivity(intent);
               // Toast.makeText(getActivity(),"setting",Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onRefresh() {
//        // 프래그먼트 새로고침
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(this).attach(this).commit();
//        //프래그먼트 새로고침
//
//        swipeRefreshLayout.setRefreshing(false);
//    }

    ArrayList<String> follower_List = new ArrayList<>();
    ArrayList<String> followed_List = new ArrayList<>();
    public void loadMYinfo(){
        Call<Value_3> call = api_chat.getOneInfo3_idx(MY_IDX);
        call.enqueue(new Callback<Value_3>() {
            @Override
            public void onResponse(Call<Value_3> call, Response<Value_3> response) {
                if(!response.isSuccessful()){
                    Log.e("loadMYinfo_code",""+response.code());
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
                Picasso.with(getActivity())
                        .load(photo)
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.account)
                        .into(account_img);
                account_nametxt.setText(username);
                account_emailtxt.setText(email);
                account_introducetxt.setText(introduce);


            }

            @Override
            public void onFailure(Call<Value_3> call, Throwable t) {
                Log.e("loadMYinfo_fail","Error : "+t.getMessage());
            }
        });
    }

    public void getoneinfo(){
        Log.e("MY_EMAIL","/"+MY_EMAIL);
        Call<Value_3> call = api.getOneInfo(MY_EMAIL);

        call.enqueue(new Callback<Value_3>() {
            @Override
            public void onResponse(Call<Value_3> call, Response<Value_3> response) {
                if(!response.isSuccessful()){
                    Log.e("Userinfo_Edit_getoneinfo_code",""+response.code());
                    return;
                }


                Value_3 val = response.body();
                String username = val.getUsername();
                String photo = val.getPhoto();
                String email = val.getEmail();
                String birthday = val.getBirthday();
                String introduce = val.getIntroduce();

                Log.e("getoneinfo",username+"/"+photo+ "/"+birthday+"/"+introduce);

                if(!username.isEmpty()){

                    account_nametxt.setText(username);
                    account_emailtxt.setText(email);
                    account_introducetxt.setText(introduce);
                    if(photo!=null){

                        Picasso.with(getActivity())
                                .load(URL+photo)
                                .transform(new CircleTransform())
                                .placeholder(R.drawable.load)
                                .into(account_img);
                    }
                }
            }

            @Override
            public void onFailure(Call<Value_3> call, Throwable t) {
                Log.e("Userinfo_Edit_getoneinfo_fail","Error : "+t.getMessage());
            }
        });
    }
}
