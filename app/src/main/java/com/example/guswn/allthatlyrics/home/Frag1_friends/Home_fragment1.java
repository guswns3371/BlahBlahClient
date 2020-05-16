package com.example.guswn.allthatlyrics.home.Frag1_friends;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guswn.allthatlyrics.extension.MyRetrofit;
import com.example.guswn.allthatlyrics.response.UserResponse_3;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.adapter.MyAdapter_Friend;
import com.example.guswn.allthatlyrics.api.FriendAPI;
import com.example.guswn.allthatlyrics.model.FriendInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.guswn.allthatlyrics.main.Logo.MY_EMAIL_2;

public class Home_fragment1 extends Fragment{
    private boolean shouldRefreshOnResume = false;

    String NAME,BIRTHDAY,INTRODUCE,PHOTO;
    MyAdapter_Friend myAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    FriendAPI api;
    ArrayList<FriendInfo> friendInfos;
    @BindView(R.id.recyclerview_f1)
    RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment1,container,false);
        ButterKnife.bind(this,view);

        /**test success*/
        /**레트로핏*/
        MyRetrofit myRetrofit = new MyRetrofit();
        api = myRetrofit.create(FriendAPI.class);

        /**레트로핏*/

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
        Call<UserResponse_3> call = api.getOneInfo();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait");
        progressDoalog.setTitle("Friends Information Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<UserResponse_3>() {
            @Override
            public void onResponse(Call<UserResponse_3> call, Response<UserResponse_3> response) {
                if(!response.isSuccessful()){
                    Log.e("getoneinfo_B_code",""+response.code());
                    return;
                }
                progressDoalog.dismiss();

                UserResponse_3 val3 = response.body();
                List<UserResponse_3> value = val3.getUserinfolist();

                //A_TYPE
                for(UserResponse_3 val : value){
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
                for(UserResponse_3 val : value){
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
            public void onFailure(Call<UserResponse_3> call, Throwable t) {
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
