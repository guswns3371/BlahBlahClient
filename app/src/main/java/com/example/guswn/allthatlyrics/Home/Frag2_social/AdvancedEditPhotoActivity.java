package com.example.guswn.allthatlyrics.Home.Frag2_social;

import android.content.Intent;
import android.media.effect.Effect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.guswn.allthatlyrics.Home.Frag4_chat.ChatAPI;
import com.example.guswn.allthatlyrics.Main.Logo;
import com.example.guswn.allthatlyrics.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.guswn.allthatlyrics.MainActivity.URL;
import static com.example.guswn.allthatlyrics.MainActivity.URL_withoutslash;

public class AdvancedEditPhotoActivity extends AppCompatActivity implements MyAdapter_Advanced_img.AdvanceImgClickListner{

    @BindView(R.id.ad_edit_tb)
    Toolbar ad_edit_tb;
    @BindView(R.id.show_img_RV)
    RecyclerView imgRecyclerView;
    @BindView(R.id.edit_img_RV)
    RecyclerView editRecyclerView;

    LinearLayoutManager mLayoutManager;
    LinearLayoutManager mLayoutManager2;
    public static MyAdapter_Advanced_img myAdapter;
    MyAdapter_Advanced_edit myAdapter_edit;
    public  static ArrayList<AdvancedImgModel> advancedImgModels;
    ArrayList<AdvancedImgModel> editEffects;
    ArrayList<String>  get_multipleSelectedImages;
    String  get_selectedimage;
    Boolean isMulti;
    private Integer [] Effects =
            {R.string.Photo_Normal ,R.string.Photo_Blur,R.string.Photo_Sepia,R.string.Photo_Sketch, R.string.Photo_Invert,
            R.string.Photo_Pixel,R.string.Photo_Contrast, R.string.Photo_Brightness,
            R.string.Photo_Vignette, R.string.Photo_Gray,R.string.Photo_Cartoon };

    Retrofit retrofit;
    SocialAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_edit_photo);
        ButterKnife.bind(this);

        //툴바
        setSupportActionBar(ad_edit_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_black);// 뒤로가기 버튼, 내가 지정할수 있다
        getSupportActionBar().setTitle("");
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

        Intent intent = getIntent();
        isMulti = intent.getBooleanExtra("isMulti",false);
        if (isMulti){ // 사진 여러장
            get_multipleSelectedImages = intent.getStringArrayListExtra("multipleSelectedImages");
        }else {//  사진 한장
            get_selectedimage = intent.getStringExtra("selectedimage");
        }
        Log.e("AdvancedEditPhotoActivity ",get_multipleSelectedImages+"/"+get_selectedimage);

        /**imgRecyclerView*/
        imgRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imgRecyclerView.setLayoutManager(mLayoutManager);
        /**imgRecyclerView*/

        /**editRecyclerView*/
        editRecyclerView.setHasFixedSize(true);
        mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        editRecyclerView.setLayoutManager(mLayoutManager2);
        /**editRecyclerView*/

        advancedImgModels = new ArrayList<>();
        editEffects = new ArrayList<>();
        /**Effects load*/
        loadEditEffects();

        if (isMulti) { // 사진 여러장
            myAdapter_edit = new MyAdapter_Advanced_edit(editEffects,AdvancedEditPhotoActivity.this,get_multipleSelectedImages.get(0));

            for (String imgs : get_multipleSelectedImages){

                File imgFile = new  File(imgs);
                File compressedImageFile = null;
                try {
                     compressedImageFile = new Compressor(this).compressToFile(imgFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(imgFile.exists())
                {
                    advancedImgModels.add(new AdvancedImgModel(Uri.fromFile(imgFile),imgFile,imgs,R.string.Photo_Normal));
                }
            }
        }else {//  사진 한장
            myAdapter_edit = new MyAdapter_Advanced_edit(editEffects,AdvancedEditPhotoActivity.this,get_selectedimage);
            File imgFile = new  File(get_selectedimage);
            File compressedImageFile = null;
            try {
                compressedImageFile = new Compressor(this).compressToFile(imgFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(imgFile.exists())
            {
                advancedImgModels.add(new AdvancedImgModel(Uri.fromFile(imgFile),imgFile,get_selectedimage,R.string.Photo_Normal));
            }
        }
        editRecyclerView.setAdapter(myAdapter_edit);
        myAdapter = new MyAdapter_Advanced_img(advancedImgModels,AdvancedEditPhotoActivity.this);
        myAdapter.setOnClickListner_AdvancedImg(this);
        imgRecyclerView.setAdapter(myAdapter);


    }

    public void loadEditEffects(){
        for (int a=0; a< Effects.length; a++){
            AdvancedImgModel model = new  AdvancedImgModel(null,null,null,Effects[a]);
            editEffects.add(model);
        }
    }
    /**한장만 효과줄 때 test*/
    boolean isSingleEdit;
    @Override
    public void onEyeImgClicked(int pos) {
        isSingleEdit=true;
        myAdapter.isSingleEditNow = true;
        myAdapter.clickedpos = pos;
        myAdapter.notifyDataSetChanged();
        Toast toast = Toast.makeText(AdvancedEditPhotoActivity.this,"선택 사진 편집",Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        advancedImgModels = new ArrayList<>();
    }

    //툴바
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ad_edit_toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                advancedImgModels = new ArrayList<>();
                finish();
                return true;
            }
            case R.id.menu_toolbar_next2:
                /**test success*/
                if (!isSingleEdit){
                    Log.e("isSingleEdit ",myAdapter.isSingleEditNow+"");
                    Intent intent = new Intent(AdvancedEditPhotoActivity.this,MakeSocialContentActivity.class);
                    intent.putParcelableArrayListExtra("edited_Images",advancedImgModels);
                    startActivityForResult(intent,2);
                }else {
                    isSingleEdit=false;
                    myAdapter.isSingleEditNow = false;
                    myAdapter.clickedpos = null;
                    myAdapter.isEdit = false;
                    myAdapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(AdvancedEditPhotoActivity.this,"선택 사진 편집 완료",Toast.LENGTH_SHORT);
                    toast.show();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Intent resultIntent;
        if(resultCode == RESULT_OK){
            Log.e("AdvancedEdit_onActivityResult_requestCode",""+requestCode);
            switch (requestCode){
                // testActivity 에서 요청할 때 보낸 요청 코드 ()
                case 1:
                    resultIntent = new Intent();
                    setResult(RESULT_OK,resultIntent);
                    finish();
                    break;
                case 2:
                    resultIntent = new Intent();
                    setResult(RESULT_OK,resultIntent);
                    finish();
                    break;
                case 3:
                    resultIntent = new Intent();
                    setResult(RESULT_OK,resultIntent);
                    finish();
                    break;
                case 4:
                    resultIntent = new Intent();
                    setResult(RESULT_OK,resultIntent);
                    finish();
                    break;
            }

        }
    }


    //툴바

//    public void uploadSocialImage(){
//        /**map_file*/
//        List<MultipartBody.Part> map_file = new ArrayList<>();
//        for (int i =0;i<advancedImgModels.size(); i++){
//            Uri fileuri = advancedImgModels.get(i).getImg();
//            String filename = "file"+i;
//            map_file.add(prepareFilePart(filename,fileuri));
//        }
//        /***/
//
//        /**map_filter*/
//        JSONArray map_filter = new JSONArray();
//        JSONObject obj= new JSONObject();
//        for (int i =0;i<advancedImgModels.size(); i++){
//            try {
//                    String  filter = getResources().getString(advancedImgModels.get(i).getType());
//                    obj.put("file"+i,filter);
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//        }
//        map_filter.put(obj);
//        /***/
//
//        String count_size = advancedImgModels.size()+"";
//        Date TODAY = new Date();
//        SimpleDateFormat TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String myidx = Logo.MY_IDX;
//        String myname = Logo.MY_NAME;
//        String now =TIME.format(TODAY);
//        Log.e("myidx" ,myidx+"/"+myname+"/"+now);
//        RequestBody idx = createPartFromString(myidx);
//        RequestBody name = createPartFromString(myname);
//        RequestBody time = createPartFromString(now);
//        RequestBody count = createPartFromString(count_size);
//
//        Call<SocialUploadResponse> call = api.uploadMultiFiles(map_file,map_filter,count,idx,name,time);
//        call.enqueue(new Callback<SocialUploadResponse>() {
//            @Override
//            public void onResponse(Call<SocialUploadResponse> call, Response<SocialUploadResponse> response) {
//                if(!response.isSuccessful()){
//                    Log.e("uploadSocialImage_code",""+response.code());
//                    return;
//                }
//                SocialUploadResponse res = response.body();
//                String value = res.getValue();
//                String message = res.getMessage();
//                String content = res.getSocial_content();
//                String useridx = res.getSocial_useridx();
//                String username = res.getSocial_username();
//                String time = res.getSocial_time();
//                String location = res.getSocial_location();
//                String imgpath = res.getSocial_imagepath_list();
//                Log.e("uploadSocialImage ",value+"/"+message+"/"+content+"/"+useridx+"/"+
//                        username+"/"+time+"/"+location+"/"+imgpath);
//
//                /**imgpath는 string이다*/
//                try {
//                    JSONArray jsonArray = new JSONArray(imgpath);
//                    /**스트링을 제이슨어레이로 [ {" 파일URL":" 필터종류"},{" 파일URL":" 필터종류"},{" 파일URL":" 필터종류"}...]*/
//                   JSONObject explrObject;
//                    for (int a=0;a<jsonArray.length();a++){
//                        explrObject = jsonArray.getJSONObject(a);
//                        //Log.e("1_explrObject key "+a, String.valueOf(explrObject));
//                        for (Iterator<String> it = explrObject.keys(); it.hasNext(); ) {
//                            String key = it.next(); /**제이슨 오브젝트의 키*/
//                            String value2 = explrObject.getString(key); /** 제이슨오브젝트의 밸류*/
//                            Log.e("2_explrObject key/value"+a,URL_withoutslash+key+" ____ "+value2); /** 성공*/
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SocialUploadResponse> call, Throwable t) {
//                Log.e("uploadSocialImage_fail","Error : "+t.getMessage());
//            }
//        });
//    }
//
//    /**MultipartBody 관련 코드*/
//    @NonNull
//    public static RequestBody createPartFromString (String descriptionString) {
//        return RequestBody.create(
//                MultipartBody.FORM,
//                descriptionString
//        );
//    }
//    @NonNull
//    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri){
//        Log.e("prepareFilePart_fileUri",""+fileUri);
//        File file = FileUtils.getFile(AdvancedEditPhotoActivity.this,fileUri);
//
//        // Parsing any Media type file
//        RequestBody requestFile =
//                RequestBody.create(
//                        MediaType.parse("*/*"),
//                        file
//                );
//        return MultipartBody.Part.createFormData(partName,file.getName(),requestFile);
//    }
//    /**MultipartBody 관련 코드*/
}
