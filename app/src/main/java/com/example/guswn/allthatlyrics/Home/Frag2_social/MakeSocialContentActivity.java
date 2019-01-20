package com.example.guswn.allthatlyrics.Home.Frag2_social;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guswn.allthatlyrics.Home.Home;
import com.example.guswn.allthatlyrics.Main.Logo;
import com.example.guswn.allthatlyrics.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class MakeSocialContentActivity extends AppCompatActivity {

    @BindView(R.id.make_social_tb)
    Toolbar make_social_tb;
    @BindView(R.id.make_social_img)
    ImageView make_social_img;
    @BindView(R.id.make_social_ismulti_img)
    ImageView make_social_ismulti_img;
    @BindView(R.id.make_social_content_edit)
    EditText make_social_content_edit;
    @BindView(R.id.make_social_location)
    TextView make_social_location;

    Intent intent;
    ArrayList<AdvancedImgModel> edited_Images;
    Retrofit retrofit;
    SocialAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_social_content);
        ButterKnife.bind(this);

        //툴바
        setSupportActionBar(make_social_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_black);// 뒤로가기 버튼, 내가 지정할수 있다
        getSupportActionBar().setTitle("새 게시물");
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

        intent = getIntent();
        edited_Images = intent.getParcelableArrayListExtra("edited_Images");
        make_social_img.setImageURI(edited_Images.get(0).getImg());
        Log.e("edited_Images.size() ",edited_Images.size()+"");
        if (edited_Images.size()==1){
            make_social_ismulti_img.setVisibility(View.INVISIBLE);
        }else {
            make_social_ismulti_img.setVisibility(View.VISIBLE);
        }
            int a=0;
            for (AdvancedImgModel model : edited_Images){
                Log.e("MakeSocialContentActivity "+a,getResources().getString(model.getType()));
                a++;
            }

    }

    //툴바
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.make_social_toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.menu_toolbar_next3:
                uploadSocialImage();
//                Intent intent = new Intent(MakeSocialContentActivity.this, Home.class);
//                startActivityForResult(intent,2);
                Intent resultIntent = new Intent();
                setResult(RESULT_OK,resultIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Intent resultIntent;
        if(resultCode == RESULT_OK){
            Log.e("MakeSocial_onActivityResult_requestCode",""+requestCode);
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
    public void uploadSocialImage(){
        /**map_file*/
        List<MultipartBody.Part> map_file = new ArrayList<>();
        for (int i =0;i<edited_Images.size(); i++){
            Uri fileuri = edited_Images.get(i).getImg();
            String filename = "file"+i;
            map_file.add(prepareFilePart(filename,fileuri));
        }
        /***/

        /**map_filter*/
        JSONArray map_filter = new JSONArray();
        JSONObject obj= new JSONObject();
        for (int i =0;i<edited_Images.size(); i++){
            try {
                String  filter = getResources().getString(edited_Images.get(i).getType());
                obj.put("file"+i,filter);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        map_filter.put(obj);
        /***/

        String count_size = edited_Images.size()+"";
        Date TODAY = new Date();
        SimpleDateFormat TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String myidx = Logo.MY_IDX;
        String myname = Logo.MY_NAME;
        String now =TIME.format(TODAY);
        Log.e("myidx" ,myidx+"/"+myname+"/"+now);
        RequestBody idx = createPartFromString(myidx);
        RequestBody name = createPartFromString(myname);
        RequestBody time = createPartFromString(now);
        RequestBody count = createPartFromString(count_size);
        RequestBody content = createPartFromString(make_social_content_edit.getText().toString());

        Call<SocialUploadResponse> call = api.uploadMultiFiles(map_file,map_filter,count,idx,name,time,content);
        call.enqueue(new Callback<SocialUploadResponse>() {
            @Override
            public void onResponse(Call<SocialUploadResponse> call, Response<SocialUploadResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("uploadSocialImage_code",""+response.code());
                    return;
                }
                SocialUploadResponse res = response.body();
                String value = res.getValue();
                String message = res.getMessage();
                String content = res.getSocial_content();
                String useridx = res.getSocial_useridx();
                String username = res.getSocial_username();
                String time = res.getSocial_time();
                String location = res.getSocial_location();
                String imgpath = res.getSocial_imagepath_list();
                Log.e("uploadSocialImage ",value+"/"+message+"/"+content+"/"+useridx+"/"+
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
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SocialUploadResponse> call, Throwable t) {
                Log.e("uploadSocialImage_fail","Error : "+t.getMessage());
            }
        });
    }

    /**MultipartBody 관련 코드*/
    @NonNull
    public static RequestBody createPartFromString (String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM,
                descriptionString
        );
    }
    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri){
        Log.e("prepareFilePart_fileUri",""+fileUri);
        File file = FileUtils.getFile(this,fileUri);

        // Parsing any Media type file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("*/*"),
                        file
                );
        return MultipartBody.Part.createFormData(partName,file.getName(),requestFile);
    }
    /**MultipartBody 관련 코드*/
}
