package com.example.guswn.allthatlyrics.Home.Frag2_social;

import android.content.Intent;
import android.os.Parcelable;
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

import com.example.guswn.allthatlyrics.CircleTransform;
import com.example.guswn.allthatlyrics.Main.Logo;
import com.example.guswn.allthatlyrics.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
import static com.example.guswn.allthatlyrics.Main.Logo.MY_EMAIL_2;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_IDX;
import static com.example.guswn.allthatlyrics.MainActivity.URL;

public class InnerSocialActivity extends AppCompatActivity {

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

    Intent intent;
    SocialInfoModel object;
    ArrayList<SocialImageModel> imageModels;
     SocialAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_social);
        ButterKnife.bind(this);


        //레트로핏
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        api = retrofit.create(SocialAPI.class);
        //레트로핏

        intent = getIntent();
        object = intent.getParcelableExtra("Clikedmodel");
        imageModels = intent.getParcelableArrayListExtra("Clikedmodel_SocialImageModelList");

        //툴바
        setSupportActionBar(inner_social_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_black);// 뒤로가기 버튼, 내가 지정할수 있다
        getSupportActionBar().setTitle(object.getSocial_username());
        //툴바

        /**test success*/
        object.setSocialImageModelList(imageModels);
        /**test*/

        social_username.setText(object.getSocial_username());
        if (object.getSocial_location()!=null){
            social_location.setVisibility(View.VISIBLE);
            social_location.setText(object.getSocial_location());
        }else {
            social_location.setVisibility(View.GONE);
        }

        if (object.getSocial_content_txt()!=null){
            social_content_txt.setVisibility(View.VISIBLE);
            social_content_txt.setText(object.getSocial_content_txt());
        }else {
            social_content_txt.setVisibility(View.GONE);
        }

        if (object.getLiked()){
            social_like_btn.setImageResource(R.drawable.heart_filled_ios);
        }else {
            social_like_btn.setImageResource(R.drawable.heart_blank_ios);
        }

        if (object.getBookMarked()){
            social_bookmark_btn.setImageResource(R.drawable.bookmark_filled_ios);
        }else {
            social_bookmark_btn.setImageResource(R.drawable.bookmark_blank_ios);
        }

        String likedcnt = "좋아요 "+object.getSocial_like_cnt()+"개";
        social_like_ctn.setText(likedcnt);
        social_time.setText(object.getSocial_time());
        social_more_reply_txt_btn.setText("댓글 더보기");

        Picasso.with(this)
                .load(URL+object.getSocial_userimg())
                .transform(new CircleTransform())
                .placeholder(R.drawable.load)
                .into(social_user_img);

        if (object.getSocialImageModelList().size()>1){
            String count = 1+"/"+object.getSocialImageModelList().size();
            social_content_img_cnt.setText(count);
            social_content_img_cnt.setVisibility(View.VISIBLE);
        }else {
            social_content_img_cnt.setVisibility(View.INVISIBLE);
        }
        SlideAdapter slideAdapter = new SlideAdapter(this,object.getSocialImageModelList());
        social_content_img_viewpager.setAdapter(slideAdapter);
        social_content_img_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                String count = (i+1)+"/"+object.getSocialImageModelList().size();
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
    //툴바
}
