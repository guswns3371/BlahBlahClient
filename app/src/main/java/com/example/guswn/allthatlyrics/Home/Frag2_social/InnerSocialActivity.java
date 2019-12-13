package com.example.guswn.allthatlyrics.Home.Frag2_social;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.guswn.allthatlyrics.Extension.CircleTransform;
import com.example.guswn.allthatlyrics.Extension.MyRetrofit;
import com.example.guswn.allthatlyrics.Home.Frag2_social.Reply.SocialReplyActivity;
import com.example.guswn.allthatlyrics.Home.Frag3_account.OtherFollowAccount;
import com.example.guswn.allthatlyrics.Home.Frag3_account.Value_3;
import com.example.guswn.allthatlyrics.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

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
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_IDX;
import static com.example.guswn.allthatlyrics.MainActivity.URL;
import static com.example.guswn.allthatlyrics.MainActivity.URL_withoutslash;

public class InnerSocialActivity extends AppCompatActivity implements SlideAdapter.SlideClickListener {

    @BindView(R.id.social_user_img)
    ImageView social_user_img;
    @BindView(R.id.social_content_img_viewpager)
    ViewPager social_content_img_viewpager;
    @BindView(R.id.social_content_img_cnt)
    TextView social_content_img_cnt;

    @BindView(R.id.social_username)
    TextView social_username;
    @BindView(R.id.social_location)
    TextView social_location;
    @BindView(R.id.social_like_ctn)
    TextView social_like_ctn;
    @BindView(R.id.social_content_txt)
    TextView social_content_txt;
    @BindView(R.id.social_more_reply_txt_btn)
    TextView social_more_reply_txt_btn;
    @BindView(R.id.social_time)
    TextView social_time;

    @BindView(R.id.social_like_btn)
    ImageButton social_like_btn;

    @BindView(R.id.social_bookmark_btn)
    ImageButton social_bookmark_btn;

    @BindView(R.id.social_more_btn)
    ImageButton social_more_btn;

    @BindView(R.id.social_reply_btn)
    ImageButton social_reply_btn;
    @BindView(R.id.social_share_btn)
    ImageButton social_share_btn;

    @BindView(R.id.inner_social_tb)
    Toolbar inner_social_tb;

    @OnClick({R.id.social_user_img,R.id.social_username})
    public void userpage(){
            SocialInfoModel model = object;
            Intent intent = new Intent(InnerSocialActivity.this, OtherFollowAccount.class);
            intent.putExtra("useridx",model.getSocial_useridx());
            intent.putExtra("username",model.getSocial_username());
            intent.putExtra("userimg",model.getSocial_userimg());
            startActivity(intent);

    }
    @OnClick(R.id.social_reply_btn)
    public void reply_act(){
        Intent intent = new Intent(InnerSocialActivity.this,SocialReplyActivity.class);
        intent.putExtra("replyroom_idx",object.getSocial_idx());
        intent.putExtra("history_userimg",object.getSocial_userimg());
        intent.putExtra("history_username",object.getSocial_username());
        intent.putExtra("history_content",object.getSocial_content_txt());
        intent.putExtra("history_useridx",object.getSocial_useridx());
        intent.putExtra("history_time",object.getSocial_time());
        startActivity(intent);
    }

    @OnClick(R.id.social_more_reply_txt_btn)
    public void reply_act_txt(){
        Intent intent = new Intent(InnerSocialActivity.this,SocialReplyActivity.class);
        intent.putExtra("replyroom_idx",object.getSocial_idx());
        intent.putExtra("history_userimg",object.getSocial_userimg());
        intent.putExtra("history_username",object.getSocial_username());
        intent.putExtra("history_content",object.getSocial_content_txt());
        intent.putExtra("history_useridx",object.getSocial_useridx());
        intent.putExtra("history_time",object.getSocial_time());
        startActivity(intent);
    }
//    @BindView(R.id.SwipeInner)
//    SwipeRefreshLayout swipeRefreshLayout;

    Intent intent;
    SocialInfoModel object;
    ArrayList<SocialImageModel> imageModels;
     SocialAPI api;
    private static String social_idx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_social);
        ButterKnife.bind(this);


        //레트로핏
        api = new MyRetrofit().create(SocialAPI.class);
        //레트로핏

        intent = getIntent();
        object = intent.getParcelableExtra("Clikedmodel"); // 사실상 필요 없는 코드 어차피 밑에서 새롭게 초기화 됨
        imageModels = intent.getParcelableArrayListExtra("Clikedmodel_SocialImageModelList");
        social_idx = object.getSocial_idx();

        //툴바
        setSupportActionBar(inner_social_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_black);// 뒤로가기 버튼, 내가 지정할수 있다
        getSupportActionBar().setTitle("게시물");
        //툴바

        /**test success*/
        object.setSocialImageModelList(imageModels);
        /**test success*/

        /**test success*/
        //loadSocialHistory(social_idx);
        /**test success*/

        //swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        /**test*/
        loadSocialHistory(social_idx);
        /**test*/
        super.onResume();
    }

    /**좋아요*/
    @OnClick(R.id.social_like_btn)
    public void like(){
        SocialInfoModel ClickModel = object;
        ImageButton view = (ImageButton) social_like_btn;
        TextView  tv = (TextView) social_like_ctn;
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
                        Toast.makeText(    InnerSocialActivity.this,"좋아요",Toast.LENGTH_SHORT).show();
                    }else if (isLiked.equals("no")){
                        Toast.makeText(    InnerSocialActivity.this,"좋아요 취소",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(    InnerSocialActivity.this,message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialUploadResponse> call, Throwable t) {
                Log.e("liked_or_unliked_fail","Error : "+t.getMessage());
            }
        });
    }
    /**좋아요*/

    /**북마크*/
    @OnClick(R.id.social_bookmark_btn)
    public void bookmark(){
        SocialInfoModel ClickModel = object;
        ImageButton view = (ImageButton) social_bookmark_btn;
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
                        Toast.makeText(InnerSocialActivity.this,"북마크",Toast.LENGTH_SHORT).show();
                    }else if (isMarked.equals("no")){
                        Toast.makeText(InnerSocialActivity.this,"북마크 취소",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(InnerSocialActivity.this,message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialUploadResponse> call, Throwable t) {
                Log.e("marked_or_unmarked_fail","Error : "+t.getMessage());
            }
        });
    }
    /**북마크*/

    /**삭제*/
    @OnClick(R.id.social_more_btn)
    public void more(){
        final SocialInfoModel ClickModel = object;
        ImageButton view = (ImageButton) social_more_btn;
        final PopupMenu menu = new PopupMenu(InnerSocialActivity.this, view);
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
                        finish();
                        break;
                    case "JoonTalk으로 공유하기" :
                        Toast.makeText(InnerSocialActivity.this,"JoonTalk으로 공유하기",Toast.LENGTH_SHORT).show();
                        break;
                    case "수정하기" :
                        Toast.makeText(InnerSocialActivity.this,"수정하기",Toast.LENGTH_SHORT).show();
                        break;
                    case "링크 복사" :
                        Toast.makeText(InnerSocialActivity.this,"링크 복사",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(InnerSocialActivity.this,"게시물이 삭제되었습니다",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(InnerSocialActivity.this,message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialUploadResponse> call, Throwable t) {
                Log.e("delete_history_fail","Error : "+t.getMessage());

            }
        });
    }
    /**삭제*/

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

    /**SlideAdapter clicklistener interface*/
    int flag = 0;
    @Override
    public void onSlideClick(int postion, View v,MediaPlayer mp) {
        Log.e("1_onSlideClick ",postion+"/"+flag);
        if (v instanceof ImageView) {
            ImageView view = (ImageView) v;
            Log.e("2_onSlideClick ","ImageView"+"/"+flag);
        }else if (v instanceof VideoView) {
            VideoView view = (VideoView) v;
            Log.e("2_onSlideClick ","VideoView"+"/"+flag);

            switch (flag){
                case 0:
                    mp.setVolume(0,0);
                    Log.e("3_onSlideClick ","0");
                    flag++;
                    break;
                case 1:
                    mp.setVolume(1,1);
                    // 0.0f = no sound , 1.0f =full sound
                    Log.e("3_onSlideClick ","100");
                    flag=0;
                    break;
            }
        }
    }

    /**load*/
    public void loadSocialHistory(String social_idx){
        Call<SocialUploadResponse> call = api.getSocialHistoryList_onesocialidx(social_idx);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(InnerSocialActivity.this);
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
                                String mimetype = null;
                                String filter = null;
                                if (value2.contains(",")){//array ["filter_type","mime_type"]
                                    JSONArray innerArray  = explrObject.getJSONArray(key);
                                    filter = innerArray.getString(0);
                                    mimetype = innerArray.getString(1);
                                }else {//string "Normal" etc...
                                    filter = value2;
                                    mimetype = "image";
                                }
                                Log.e("2_loadSocialHistory_oneidx_explrObject key/value"+a,URL_withoutslash+key+" ____ "+filter+"___"+mimetype); /** 성공*/
                                socialImageModels.add(new SocialImageModel(key,filter,mimetype));
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

                    /**setting*/
                    final SocialInfoModel object2 = new SocialInfoModel(idx,useridx,photo,name,location,likedcnt+"",content,time,socialImageModels);
                    if (isLiked){
                        object2.setLiked(true);
                    }
                    if (isMarked){
                        object2.setBookMarked(true);
                    }

                    object = object2;

                    social_username.setText(object2.getSocial_username());
                    if (object2.getSocial_location()!=null){
                        social_location.setVisibility(View.VISIBLE);
                        social_location.setText(object2.getSocial_location());
                    }else {
                        social_location.setVisibility(View.GONE);
                    }

                    if (object2.getSocial_content_txt()!=null){
                        social_content_txt.setVisibility(View.VISIBLE);
                        social_content_txt.setText(object2.getSocial_content_txt());
                    }else {
                        social_content_txt.setVisibility(View.GONE);
                    }

                    if (object2.getLiked()){
                        social_like_btn.setImageResource(R.drawable.heart_filled_ios);
                    }else {
                        social_like_btn.setImageResource(R.drawable.heart_blank_ios);
                    }

                    if (object2.getBookMarked()){
                        social_bookmark_btn.setImageResource(R.drawable.bookmark_filled_ios);
                    }else {
                        social_bookmark_btn.setImageResource(R.drawable.bookmark_blank_ios);
                    }

                    String likedcnt2 = "좋아요 "+object2.getSocial_like_cnt()+"개";
                    social_like_ctn.setText(likedcnt2);
                    social_time.setText(object2.getSocial_time());
                    social_more_reply_txt_btn.setText("댓글 더보기");

                    Picasso.with(InnerSocialActivity.this)
                            .load(URL+object2.getSocial_userimg())
                            .transform(new CircleTransform())
                            .placeholder(R.drawable.load)
                            .into(social_user_img);

                    if (object2.getSocialImageModelList().size()>1){
                        String count = 1+"/"+object2.getSocialImageModelList().size();
                        social_content_img_cnt.setText(count);
                        social_content_img_cnt.setVisibility(View.VISIBLE);
                    }else {
                        social_content_img_cnt.setVisibility(View.INVISIBLE);
                    }
                    SlideAdapter slideAdapter = new SlideAdapter(InnerSocialActivity.this,object2.getSocialImageModelList());
                    slideAdapter.SetSlideClickListener(InnerSocialActivity.this);
                    social_content_img_viewpager.setAdapter(slideAdapter);
                    social_content_img_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {
                        }
                        @Override
                        public void onPageSelected(int i) {
                            String count = (i+1)+"/"+object2.getSocialImageModelList().size();
                            social_content_img_cnt.setText(count);
                        }
                        @Override
                        public void onPageScrollStateChanged(int i) {
                            switch (i){
                                case SCROLL_STATE_DRAGGING:
                                    break;
                                case SCROLL_STATE_IDLE:
                                    break;
                                case SCROLL_STATE_SETTLING:
                                    break;
                            }
                        }
                    });

                }

            }


            @Override
            public void onFailure(Call<SocialUploadResponse> call, Throwable t) {
                Log.e("loadSocialHistory_fail","Error : "+t.getMessage());
                progressDoalog.dismiss();
            }
        });
    }
}
