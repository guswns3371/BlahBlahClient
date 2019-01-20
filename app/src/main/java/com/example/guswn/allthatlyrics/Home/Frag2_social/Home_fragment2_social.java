package com.example.guswn.allthatlyrics.Home.Frag2_social;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guswn.allthatlyrics.Home.Frag1_friends.FollowingResponse;
import com.example.guswn.allthatlyrics.Home.Frag3_account.Value_3;
import com.example.guswn.allthatlyrics.Home.Frag4_chat.ChatAPI;
import com.example.guswn.allthatlyrics.Home.Frag4_chat.ChatAddActivity;
import com.example.guswn.allthatlyrics.Home.Frag4_chat.ChatInfo;
import com.example.guswn.allthatlyrics.Home.Frag4_chat.MyAdapter_Chat;
import com.example.guswn.allthatlyrics.Home.Home;
import com.example.guswn.allthatlyrics.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.guswn.allthatlyrics.Main.Logo.MY_IDX;
import static com.example.guswn.allthatlyrics.MainActivity.URL;
import static com.example.guswn.allthatlyrics.MainActivity.URL_withoutslash;

public class Home_fragment2_social extends Fragment implements MyAdapter_Social.SocialReycyclerClickListner, SwipeRefreshLayout.OnRefreshListener {

    LinearLayoutManager mLayoutManager;
    MyAdapter_Social myAdapter;
    ArrayList<SocialInfoModel> socialInfos;
    @BindView(R.id.social_RV)
    RecyclerView mRecyclerView;
    @BindView(R.id.social_tb)
    Toolbar social_tb;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;

    Retrofit retrofit;
    SocialAPI api;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment2,container,false);
        ButterKnife.bind(this,view);

        //툴바
        Home activity = (Home) getActivity();
        activity.setSupportActionBar(social_tb);
        ActionBar actionBar = ((Home) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        setHasOptionsMenu(true);
        //툴바

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

        api = retrofit.create(SocialAPI.class);
        //레트로핏

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        //리스트 역순 배열
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        //리스트 역순 배열
        mRecyclerView.setLayoutManager(mLayoutManager);
        socialInfos = new ArrayList<>();

        loadSocialHistory();
        myAdapter = new MyAdapter_Social(socialInfos,getActivity());
        myAdapter.setOnClickListener_Social(this);
        mRecyclerView.setAdapter(myAdapter);


        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }
    @Override
    public void onRefresh() {
        // 프래그먼트 새로고침
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        //프래그먼트 새로고침

        swipeRefreshLayout.setRefreshing(false);
    }
    //툴바
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_social_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_social_add :
                Intent intent = new Intent(getActivity(), ShowGalleryActivity.class);
                getActivity().startActivityForResult(intent,2);
                // Toast.makeText(getActivity(),"setting",Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //툴바

    public void loadSocialHistory(){
        Call<SocialUploadResponse> call = api.getSocialHistoryList();

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait");
        progressDoalog.setTitle("Socia Information Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();
        call.enqueue(new Callback<SocialUploadResponse>() {
            @Override
            public void onResponse(Call<SocialUploadResponse> call, Response<SocialUploadResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("loadSocialHistory_code",""+response.code());
                    return;
                }
                progressDoalog.dismiss();

                SocialUploadResponse res2 = response.body();
                List<SocialUploadResponse> list = res2.getSocialHistoryList();

                for (SocialUploadResponse res : list){
                    ArrayList<SocialImageModel> socialImageModels = new ArrayList<>() ;

                    String content = res.getSocial_content();
                    String idx = res.getIdx();
                    String useridx = res.getSocial_useridx();
                    String username = res.getSocial_username();
                    String time = res.getSocial_time();
                    String location = res.getSocial_location();
                    String imgpath = res.getSocial_imagepath_list();
                    Log.e("loadSocialHistory ",content+"/"+useridx+"/"+
                            username+"/"+time+"/"+location+"/"+imgpath);

                    /**imgpath는 string이다*/
                    try {
                        JSONArray jsonArray = new JSONArray(imgpath);
                        /**스트링을 제이슨어레이로 [ {" 파일URL":" 필터종류"},{" 파일URL":" 필터종류"},{" 파일URL":" 필터종류"}...]*/
                        JSONObject explrObject;
                        for (int a=0;a<jsonArray.length();a++){
                            explrObject = jsonArray.getJSONObject(a);
                            //Log.e("1_explrObject key "+a, String.valueOf(explrObject));
                            for (Iterator<String> it = explrObject.keys(); it.hasNext(); ) {
                                String key = it.next(); /**제이슨 오브젝트의 키*/
                                String value2 = explrObject.getString(key); /** 제이슨오브젝트의 밸류*/
                                Log.e("2_explrObject key/value"+a,URL_withoutslash+key+" ____ "+value2); /** 성공*/
                                socialImageModels.add(new SocialImageModel(key,value2));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /**userinfo*/
                    List<Value_3> list2 = res.getSocialUserInfoList();
                    String photo = list2.get(0).getPhoto();
                    String name = list2.get(0).getUsername();

                    /**liked_list*/
                    List<SocialLikedMarkedResponse> likedlist = res.getSocial_Liked_List();
                    int likedcnt = 0;
                    boolean isLiked = false;
                    boolean isMarked = false;
                    for (SocialLikedMarkedResponse one : likedlist){

                        String myidx = one.getSocial_Liked_myidx();
                        if (!myidx.equals("null")){
                            likedcnt++;
                        }
                        if (myidx.equals(MY_IDX)){
                            isLiked=true;
                           // model.setLiked(true);
                        }
                    }

                    /**marked_list*/
                    List<SocialLikedMarkedResponse> markedlist = res.getSocial_Marked_List();
                    for (SocialLikedMarkedResponse one : markedlist){
                        String myidx = one.getSocial_Marked_myidx();
                        if (myidx.equals(MY_IDX)){
                            isMarked = true;
                           // model.setBookMarked(true);
                        }
                    }
                    SocialInfoModel model = new SocialInfoModel(idx,useridx,photo,name,location,likedcnt+"",content,time,socialImageModels);
                    if (isLiked){
                        model.setLiked(true);
                    }
                    if (isMarked){
                        model.setBookMarked(true);
                    }
                    socialInfos.add(model);
                }
                myAdapter.notifyDataSetChanged();
                }


            @Override
            public void onFailure(Call<SocialUploadResponse> call, Throwable t) {
                Log.e("loadSocialHistory_fail","Error : "+t.getMessage());
                progressDoalog.dismiss();
            }
        });
    }

    @Override
    public void onlike_btnClick(int position, View v, TextView tv) {
        SocialInfoModel ClickModel = socialInfos.get(position);
        ImageButton view = (ImageButton) v;
        int trueOrFalse;
        if (ClickModel.getLiked()){
            trueOrFalse = 0;
        }else {
            trueOrFalse=1;
        }
        switch (trueOrFalse){
            case 0://true
                ClickModel.setLiked(false);
                view.setImageResource(R.drawable.heart_blank_ios);
                int likedcnt = Integer.parseInt(ClickModel.getSocial_like_cnt())-1;
                ClickModel.setSocial_like_cnt(likedcnt+"");
                String s = "좋아요 "+likedcnt+"개";
                tv.setText(s);
                Log.e("unLiked ",ClickModel.getSocial_content_txt());
               // Toast.makeText(getActivity(),"좋아요 취소",Toast.LENGTH_SHORT).show();
                liked_or_unliked("no",MY_IDX,ClickModel.getSocial_idx());
                break;
            case 1://false
                ClickModel.setLiked(true);
                view.setImageResource(R.drawable.heart_filled_ios);
                int likedcnt2 = Integer.parseInt(ClickModel.getSocial_like_cnt())+1;
                ClickModel.setSocial_like_cnt(likedcnt2+"");
                String s2 = "좋아요 "+likedcnt2+"개";
                tv.setText(s2);
                Log.e("Liked ",ClickModel.getSocial_content_txt());
                //Toast.makeText(getActivity(),"좋아요",Toast.LENGTH_SHORT).show();
                liked_or_unliked("yes",MY_IDX,ClickModel.getSocial_idx());
                break;
        }
    }

    public void liked_or_unliked(String isLiked,String MY_IDX, String cliked_idx){
        Map<String,String > map = new HashMap<>();
        map.put("isLiked",isLiked);
        map.put("MY_IDX",MY_IDX);
        map.put("cliked_idx",cliked_idx);

        Call<SocialUploadResponse> call = api.make_liked_unliked(map);

        call.enqueue(new Callback<SocialUploadResponse>() {
            @Override
            public void onResponse(Call<SocialUploadResponse> call, Response<SocialUploadResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("liked_or_unliked_code",""+response.code());
                    return;
                }
                SocialUploadResponse res = response.body();
                String value = res.getValue();
                String message = res.getMessage();
                String isLiked = res.getSocial_isLiked();
                if (value.equals("1")){
                    if (isLiked.equals("yes")){
                        Toast.makeText(getActivity(),"좋아요",Toast.LENGTH_SHORT).show();
                    }else if (isLiked.equals("no")){
                        Toast.makeText(getActivity(),"좋아요 취소",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialUploadResponse> call, Throwable t) {
                Log.e("liked_or_unliked_fail","Error : "+t.getMessage());
            }
        });
    }
    @Override
    public void onbookmark_btnClick(int position, View v) {
        SocialInfoModel ClickModel = socialInfos.get(position);
        ImageButton view = (ImageButton) v;
        int trueOrFalse;
        if (ClickModel.getBookMarked()){
            trueOrFalse = 0;
        }else {
            trueOrFalse=1;
        }
        switch (trueOrFalse){
            case 0://true
                ClickModel.setBookMarked(false);
                view.setImageResource(R.drawable.bookmark_blank_ios);
               // Toast.makeText(getActivity(),"북마크 취소",Toast.LENGTH_SHORT).show();
                Log.e("unMarked ",ClickModel.getSocial_content_txt());
                marked_or_unmarked("no",MY_IDX,ClickModel.getSocial_idx());
                break;
            case 1://false
                ClickModel.setBookMarked(true);
                view.setImageResource(R.drawable.bookmark_filled_ios);
               // Toast.makeText(getActivity(),"북마크",Toast.LENGTH_SHORT).show();
                Log.e("Marked ",ClickModel.getSocial_content_txt());
                marked_or_unmarked("yes",MY_IDX,ClickModel.getSocial_idx());
                break;
        }
    }

    public void marked_or_unmarked(String isMarked,String MY_IDX, String cliked_idx){
        Map<String,String > map = new HashMap<>();
        map.put("isMarked",isMarked);
        map.put("MY_IDX",MY_IDX);
        map.put("cliked_idx",cliked_idx);

        Call<SocialUploadResponse> call = api.make_marked_unmarked(map);

        call.enqueue(new Callback<SocialUploadResponse>() {
            @Override
            public void onResponse(Call<SocialUploadResponse> call, Response<SocialUploadResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("marked_or_unmarked_code",""+response.code());
                    return;
                }
                SocialUploadResponse res = response.body();
                String value = res.getValue();
                String message = res.getMessage();
                String isMarked = res.getSocial_isBookMarked();
                if (value.equals("1")){
                    if (isMarked.equals("yes")){
                        Toast.makeText(getActivity(),"북마크",Toast.LENGTH_SHORT).show();
                    }else if (isMarked.equals("no")){
                        Toast.makeText(getActivity(),"북마크 취소",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialUploadResponse> call, Throwable t) {
                Log.e("marked_or_unmarked_fail","Error : "+t.getMessage());
            }
        });
    }

    @Override
    public void onreply_btnnClick(int position, View v) {

    }

    @Override
    public void onshare_btnClick(int position, View v) {

    }
    @Override
    public void onmore_btnClick(final int position, View v, final View itemview) {
        final SocialInfoModel ClickModel = socialInfos.get(position);
        ImageButton view = (ImageButton) v;
        final PopupMenu menu = new PopupMenu(getActivity(), view);
        if (ClickModel.getSocial_useridx().equals(MY_IDX)){
            menu.getMenu().add("삭제하기");
            menu.getMenu().add("수정하기");
        }
        menu.getMenu().add("JoonTalk으로 공유하기");
        menu.getMenu().add("링크 복사");
        menu.show();

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getTitle().toString()){
                    case "삭제하기" :
                       // Toast.makeText(getActivity(),"삭제하기",Toast.LENGTH_SHORT).show();
                        liked_or_unliked("no",MY_IDX,ClickModel.getSocial_idx());
                        marked_or_unmarked("no",MY_IDX,ClickModel.getSocial_idx());
                        delete_history(MY_IDX,ClickModel.getSocial_idx());
                        socialInfos.remove(position);
                        myAdapter.notifyItemRemoved(position);
                        myAdapter.notifyItemRangeChanged(position, socialInfos.size());
                        itemview.setVisibility(View.GONE);
                        /**test success*/
                        myAdapter.notifyDataSetChanged();
                        break;
                    case "JoonTalk으로 공유하기" :
                        Toast.makeText(getActivity(),"JoonTalk으로 공유하기",Toast.LENGTH_SHORT).show();
                        break;
                    case "수정하기" :
                        Toast.makeText(getActivity(),"수정하기",Toast.LENGTH_SHORT).show();
                        break;
                    case "링크 복사" :
                        Toast.makeText(getActivity(),"링크 복사",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }

    public void delete_history(String MY_IDX, String historyidx){
        Map<String ,String > map = new HashMap<>();
        map.put("MY_IDX",MY_IDX);
        map.put("historyidx",historyidx);

        Call<SocialUploadResponse> call = api.delete_socialhistory(map);
        call.enqueue(new Callback<SocialUploadResponse>() {
            @Override
            public void onResponse(Call<SocialUploadResponse> call, Response<SocialUploadResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("delete_history_code",""+response.code());
                    return;
                }
                SocialUploadResponse res = response.body();
                String value = res.getValue();
                String message = res.getMessage();
                if (value.equals("1")){
                    Toast.makeText(getActivity(),"게시물이 삭제되었습니다",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialUploadResponse> call, Throwable t) {
                Log.e("delete_history_fail","Error : "+t.getMessage());

            }
        });
    }

}