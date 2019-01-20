package com.example.guswn.allthatlyrics.Home.Frag1_friends;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guswn.allthatlyrics.Home.Frag3_account.EditAPI;
import com.example.guswn.allthatlyrics.Home.Frag3_account.Userinfo_Edit;
import com.example.guswn.allthatlyrics.Home.Frag3_account.Value_3;
import com.example.guswn.allthatlyrics.Main.SaveSharedPreference;
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

import static com.example.guswn.allthatlyrics.Main.Login.MY_EMAIL;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_EMAIL_2;

import static com.example.guswn.allthatlyrics.MainActivity.StringToBitMap;
import static com.example.guswn.allthatlyrics.MainActivity.URL;

public class Home_fragment1 extends Fragment{
    private boolean shouldRefreshOnResume = false;

    String NAME,BIRTHDAY,INTRODUCE,PHOTO;
    MyAdapter_Friend myAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    Retrofit retrofit;
    FriendAPI api;
    ArrayList<FriendInfo> friendInfos;
    @BindView(R.id.recyclerview_f1)
    RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment1,container,false);
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

        api = retrofit.create(FriendAPI.class);
        //레트로핏


        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        friendInfos = new ArrayList<>();
        //friendInfos.add(new FriendInfo(FriendInfo.A_TYPE, SaveSharedPreference.getUserPhoto(getActivity(),MY_EMAIL_2),"?","??"));


        myAdapter = new MyAdapter_Friend(friendInfos,getActivity(),false);
        mRecyclerView.setAdapter(myAdapter);

        getoneinfo_B();
//        test();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Check should we need to refresh the fragment
        if(shouldRefreshOnResume){
            // refresh fragment
            Log.e("plzzzzzzzz","qqqqqq");
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    public void getoneinfo_B(){
        Log.e("MY_EMAIL","/"+MY_EMAIL_2);
        Call<Value_3> call = api.getOneInfo();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait");
        progressDoalog.setTitle("Friends Information Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<Value_3>() {
            @Override
            public void onResponse(Call<Value_3> call, Response<Value_3> response) {
                if(!response.isSuccessful()){
                    Log.e("getoneinfo_B_code",""+response.code());
                    return;
                }
                progressDoalog.dismiss();

                Value_3 val3 = response.body();
                List<Value_3> value = val3.getUserinfolist();

                //A_TYPE
                for(Value_3 val : value){
                    String idx = val.getIdx();
                    String email = val.getEmail();
                    String username = val.getUsername();

                    String photo = val.getPhoto();
                    String birthday = val.getBirthday();
                    String introduce = val.getIntroduce();
                    if(email.equals(MY_EMAIL_2)){
                        friendInfos.add(new FriendInfo(FriendInfo.A_TYPE,idx,photo,username,introduce,email,birthday));
                    }
                }

                //B_TYPE
                for(Value_3 val : value){
                    String idx = val.getIdx();
                    String email = val.getEmail();
                    String username = val.getUsername();
                    String photo = val.getPhoto();
                    String birthday = val.getBirthday();
                    String introduce = val.getIntroduce();
                    if(email.equals(MY_EMAIL_2)){
                        //friendInfos.add(new FriendInfo(FriendInfo.A_TYPE,photo,username,introduce));
                    }else {
                        friendInfos.add(new FriendInfo(FriendInfo.B_TYPE,idx,photo,username,introduce,email,birthday));
                    }
                }

                myAdapter.notifyDataSetChanged();

               // Log.e("getoneinfo_A",username+"/"+photo+ "/"+birthday+"/"+introduce);

//                if(!username.isEmpty()){
//                    NAME = username;
//                    BIRTHDAY = birthday;
//                    INTRODUCE = introduce;
//                    PHOTO = photo;
//                    if(PHOTO!=null){
//                        // Bitmap decodedByte = StringToBitMap(PHOTO);
//                        //edit_img.setImageBitmap(decodedByte);
//                        friendInfos.add(new FriendInfo(FriendInfo.A_TYPE,PHOTO,NAME,INTRODUCE));
//                        myAdapter = new MyAdapter_Friend(friendInfos);
//                        mRecyclerView.setAdapter(myAdapter);
//                    }
//
//                }

            }

            @Override
            public void onFailure(Call<Value_3> call, Throwable t) {
                progressDoalog.dismiss();
                Log.e("getoneinfo_B_fail","Error : "+t.getMessage());
            }
        });
    }

    public void test(){
        friendInfos.add(new FriendInfo(FriendInfo.A_TYPE,"1", "1","name","des","email","birthday"));
        for(int i=1;i<20;i++){
            friendInfos.add(new FriendInfo(FriendInfo.B_TYPE,i+"","1","name "+i,"des "+i,"email "+i,"birthday "+i));
        }
        myAdapter = new MyAdapter_Friend(friendInfos,getActivity(),false);
        mRecyclerView.setAdapter(myAdapter);
    }
}
