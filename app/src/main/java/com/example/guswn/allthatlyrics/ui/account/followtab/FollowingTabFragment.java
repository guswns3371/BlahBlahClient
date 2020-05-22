package com.example.guswn.allthatlyrics.ui.account.followtab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guswn.allthatlyrics.adapter.FollowTabAdapter;
import com.example.guswn.allthatlyrics.extension.MyRetrofit;
import com.example.guswn.allthatlyrics.response.FollowingResponse;
import com.example.guswn.allthatlyrics.api.FriendAPI;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.api.EditAPI;
import com.example.guswn.allthatlyrics.model.FollowTabModel;
import com.example.guswn.allthatlyrics.response.userResponse3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.example.guswn.allthatlyrics.ui.account.followtab.FollowTab.Followed_InfoList;
import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_IDX;

public class FollowingTabFragment extends Fragment implements FollowTabAdapter.FollowTabRycyclerClickListner{

    LinearLayoutManager mLayoutManager;
    FollowTabAdapter myAdapter;
    ArrayList<FollowTabModel> followTabModels;
    @BindView(R.id.following_RV)
    RecyclerView mRecyclerView;

    ArrayList<String> infoList;
    FriendAPI api_friend;
    EditAPI api;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.following_tab_frag2, container, false);
        ButterKnife.bind(this,rootView);

        infoList = Followed_InfoList;
        //레트로핏
        api = new MyRetrofit().create(EditAPI.class);
        api_friend = new MyRetrofit().create(FriendAPI.class);
        //레트로핏

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        //리스트 역순 배열
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        //리스트 역순 배열
        mRecyclerView.setLayoutManager(mLayoutManager);
        followTabModels = new ArrayList<>();
        myAdapter = new FollowTabAdapter(followTabModels,getActivity());
        myAdapter.setOnClickListener_Friend(this);
        mRecyclerView.setAdapter(myAdapter);  //이부분을 onResponse 에 놯야 보인다
        loadFollowinfo();
        return rootView;
    }

    public void loadFollowinfo(){
        Call<userResponse3> call = api.getallInfo();
        call.enqueue(new Callback<userResponse3>() {
            @Override
            public void onResponse(Call<userResponse3> call, Response<userResponse3> response) {
                if(!response.isSuccessful()){
                    Log.e("loadFollowinfo_code",""+response.code());
                    return;
                }
                userResponse3 val3 = response.body();
                List<userResponse3> userinfolist = val3.getUserinfolist();
                for (userResponse3 val : userinfolist){

                    boolean AmIFollowHim = false;
                    String idx = val.getIdx();
                    String email = val.getEmail();
                    String username = val.getUsername();
                    String photo = val.getPhoto();
                    String birthday = val.getBirthday();
                    String introduce = val.getIntroduce();
//                    if (infoList!=null){
//                        for (int i=0;i<infoList.size(); i++){
//                            if (idx.equals(infoList.get(i))){
//                                FollowTabInfo info = new FollowTabInfo(idx,photo,username,email);
//                                info.setAmIFollowHim("yes");
//                                followTabInfos.add(info);
//                            }
//                        }
//                    }

                    /**실험 */
                    if (infoList!=null){
                        for (int i=0;i<infoList.size(); i++){
                            if (idx.equals(infoList.get(i))){
                                List<FollowingResponse> userfollower = val.getUserfollower(); //이 유저를 팔로하는 사람들
                                for (FollowingResponse follower : userfollower){
                                    if (follower.getFollower_idx().equals(MY_IDX)){// 즉 나를 팔로우 하는 사람을 내가 팔로 하는지
                                        AmIFollowHim = true;
                                    }
                                    // Log.e("AmIFollowHim_follower.getFollowed_idx()",AmIFollowHim+"/"+username+follower.getFollowed_idx()+"/"+MY_IDX);
                                }
                                Log.e("username_AmIFollowHim",username+"/"+AmIFollowHim);
                                FollowTabModel info = new FollowTabModel(idx,photo,username,email);
                                if (AmIFollowHim){
                                    info.setAmIFollowHim("yes");
                                }else {
                                    info.setAmIFollowHim("no");
                                }
                                followTabModels.add(info);
                            }
                        }
                    }
                    /**실험 */

                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<userResponse3> call, Throwable t) {
                Log.e("loadFollowinfo_fail","Error : "+t.getMessage());
            }
        });
    }

    public void follow_or_unfollow(String isfollow, String follow_er_idx, String follow_ed_idx, final String username){
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
                        Toast toast = Toast.makeText(getActivity(),username+"님을 팔로우 하셨습니다",Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,200);
                        toast.show();
                    }else if (isfollow.equals("unfollow")){
                        Toast toast = Toast.makeText(getActivity(),username+"님을 언팔로우 하셨습니다",Toast.LENGTH_SHORT);
                        //toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,200);
                        toast.show();
                    }
                }else {
                    Toast toast = Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,200);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<FollowingResponse> call, Throwable t) {
                Log.e("follow_or_unfollow_fail","Error : "+t.getMessage());
            }
        });
    }

    @Override
    public void onFollowBtnClick(int position, View v) {
        FollowTabModel clikedBtn = followTabModels.get(position);
        TextView view = (TextView) v;
        Log.e("onFollowBtnClick","onFollowBtnClick");
        switch (clikedBtn.getAmIFollowHim()){
            case "no":
                //Toast.makeText(getActivity(),clikedBtn.getFollow_username()+"를 팔로우 하셨습니다",Toast.LENGTH_SHORT).show();
                view.setText("팔로잉");
                view.setTextColor(Color.BLACK);
                view.setBackgroundResource(R.drawable.mybtn_1);
                clikedBtn.setAmIFollowHim("yes");
                follow_or_unfollow("follow",MY_IDX,clikedBtn.getIdx(),clikedBtn.getFollow_username());
                break;
            case "yes":
                //Toast.makeText(getActivity(),clikedBtn.getFollow_username()+"를 언팔로우 하셨습니다",Toast.LENGTH_SHORT).show();
                view.setText("팔로우");
                view.setTextColor(Color.WHITE);
                view.setBackgroundResource(R.drawable.mybtn_2);
                clikedBtn.setAmIFollowHim("no");
                follow_or_unfollow("unfollow",MY_IDX,clikedBtn.getIdx(),clikedBtn.getFollow_username());
                break;
        }
    }
}

