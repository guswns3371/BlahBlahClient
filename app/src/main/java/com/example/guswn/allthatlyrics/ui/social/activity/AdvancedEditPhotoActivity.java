package com.example.guswn.allthatlyrics.ui.social.activity;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;


import com.example.guswn.allthatlyrics.adapter.AdvancedEditAdpater;
import com.example.guswn.allthatlyrics.adapter.AdvancedImageAdapter;
import com.example.guswn.allthatlyrics.extension.MyRetrofit;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.api.SocialAPI;
import com.example.guswn.allthatlyrics.model.AdvancedImgModel;
import com.example.guswn.allthatlyrics.model.ShowGalleryModel;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvancedEditPhotoActivity extends AppCompatActivity implements AdvancedImageAdapter.AdvanceImgClickListner{

    @BindView(R.id.ad_edit_tb)
    Toolbar ad_edit_tb;
    @BindView(R.id.show_img_RV)
    RecyclerView imgRecyclerView;
    @BindView(R.id.edit_img_RV)
    RecyclerView editRecyclerView;

    LinearLayoutManager mLayoutManager;
    LinearLayoutManager mLayoutManager2;
    public static AdvancedImageAdapter myAdapter;
    AdvancedEditAdpater myAdapter_edit;
    public  static ArrayList<AdvancedImgModel> EditedPreUploadFiles;
    ArrayList<AdvancedImgModel> editEffects;
    ArrayList<ShowGalleryModel>  get_multipleSelectedImages;
    ShowGalleryModel  get_selectedimage;
    Boolean isMulti;
    private Integer [] Effects =
            {R.string.Photo_Normal ,R.string.Photo_Blur,R.string.Photo_Sepia,R.string.Photo_Sketch, R.string.Photo_Invert,
            R.string.Photo_Pixel,R.string.Photo_Contrast, R.string.Photo_Brightness,
            R.string.Photo_Vignette, R.string.Photo_Gray,R.string.Photo_Cartoon };

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
        api = new MyRetrofit().create(SocialAPI.class);
        //레트로핏

        Intent intent = getIntent();
        isMulti = intent.getBooleanExtra("isMulti",false);
        if (isMulti){ // 사진 여러장
            get_multipleSelectedImages = intent.getParcelableArrayListExtra("multipleSelectedImages");
        }else {//  사진 한장
            get_selectedimage = intent.getParcelableExtra("selectedimage");
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

        EditedPreUploadFiles = new ArrayList<>();
        editEffects = new ArrayList<>();
        /**Effects load*/
        loadEditEffects();

        if (isMulti) { // 사진 여러장
            myAdapter_edit = new AdvancedEditAdpater(editEffects,AdvancedEditPhotoActivity.this,get_multipleSelectedImages.get(0).getFilepath());

            for (ShowGalleryModel imgs : get_multipleSelectedImages){

                File imgFile = new  File(imgs.getFilepath());
                if(imgFile.exists())
                {
                    Log.e("imgs.getMimtype()",imgs.getMimtype());
                    EditedPreUploadFiles.add(new AdvancedImgModel(Uri.fromFile(imgFile),imgFile,imgs.getFilepath(),R.string.Photo_Normal,imgs.getMimtype()));
                }
            }
        }else {//  사진 한장
            myAdapter_edit = new AdvancedEditAdpater(editEffects,AdvancedEditPhotoActivity.this,get_selectedimage.getFilepath());
            File imgFile = new  File(get_selectedimage.getFilepath());
            if(imgFile.exists())
            {
                Log.e("get_selectedimage.getMimtype()",get_selectedimage.getMimtype());
                EditedPreUploadFiles.add(new AdvancedImgModel(Uri.fromFile(imgFile),imgFile,get_selectedimage.getFilepath(),R.string.Photo_Normal,get_selectedimage.getMimtype()));
            }
        }
        editRecyclerView.setAdapter(myAdapter_edit);
        myAdapter = new AdvancedImageAdapter(EditedPreUploadFiles,AdvancedEditPhotoActivity.this);
        myAdapter.setOnClickListner_AdvancedImg(this);
        imgRecyclerView.setAdapter(myAdapter);


    }

    public void loadEditEffects(){
        for (int a=0; a< Effects.length; a++){
            AdvancedImgModel model = new  AdvancedImgModel(null,null,null,Effects[a],null);
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
        EditedPreUploadFiles = new ArrayList<>();
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
                EditedPreUploadFiles = new ArrayList<>();
                finish();
                return true;
            }
            case R.id.menu_toolbar_next2:
                /**test success*/
                if (!isSingleEdit){
                    Log.e("isSingleEdit ",myAdapter.isSingleEditNow+"");
                    Intent intent = new Intent(AdvancedEditPhotoActivity.this,MakeSocialContentActivity.class);
                    intent.putParcelableArrayListExtra("UploadFiles", EditedPreUploadFiles);
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
//        for (int i =0;i<EditedPreUploadFiles.size(); i++){
//            Uri fileuri = EditedPreUploadFiles.get(i).getImg();
//            String filename = "file"+i;
//            map_file.add(prepareFilePart(filename,fileuri));
//        }
//        /***/
//
//        /**map_filter*/
//        JSONArray map_filter = new JSONArray();
//        JSONObject obj= new JSONObject();
//        for (int i =0;i<EditedPreUploadFiles.size(); i++){
//            try {
//                    String  filter = getResources().getString(EditedPreUploadFiles.get(i).getType());
//                    obj.put("file"+i,filter);
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//        }
//        map_filter.put(obj);
//        /***/
//
//        String count_size = EditedPreUploadFiles.size()+"";
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
//        // Parsing any Media typeInfos file
//        RequestBody requestFile =
//                RequestBody.create(
//                        MediaType.parse("*/*"),
//                        file
//                );
//        return MultipartBody.Part.createFormData(partName,file.getName(),requestFile);
//    }
//    /**MultipartBody 관련 코드*/
}
