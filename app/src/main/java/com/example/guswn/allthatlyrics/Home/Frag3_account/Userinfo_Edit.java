package com.example.guswn.allthatlyrics.Home.Frag3_account;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.guswn.allthatlyrics.Extension.MyRetrofit;
import com.example.guswn.allthatlyrics.Main.SaveSharedPreference;
import com.example.guswn.allthatlyrics.MainActivity;
import com.example.guswn.allthatlyrics.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static com.example.guswn.allthatlyrics.Main.Login.MY_EMAIL;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_EMAIL_2;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_IMG;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_NAME;
import static com.example.guswn.allthatlyrics.MainActivity.URL;


public class Userinfo_Edit extends AppCompatActivity {

    private Uri imgUri, photoURI, albumURI,fileUri;
    private String mCurrentPhotoPath;
    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;

    @BindView(R.id.userinfoedit_tb)
    Toolbar userinfoedit_tb;
    @BindView(R.id.edit_img)
    ImageView edit_img;
    @BindView(R.id.edit_photo_txt)
    TextView edit_photo_txt;
    @BindView(R.id.edit_username)
    EditText edit_username;
    @BindView(R.id.edit_birthday)
    EditText edit_birthday;
    @BindView(R.id.edit_introduce)
    EditText edit_introduce;

    @OnClick({R.id.edit_img,R.id.edit_photo_txt})
    public void editPhoto(){

        CameraPermission();

        final List<String> ListItems = new ArrayList<>();
        ListItems.add("프로필 사진 찍기");
        ListItems.add("프로필 사진 바꾸기");
        ListItems.add("프로필 사진 삭제");
        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("프로필 사진 설정");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {

                switch (pos){
                    case 0: //프로필 사진 찍기
                        Log.v("알림", "다이얼로그 > 사진촬영 선택");
                        takePhoto();
                        break;
                    case 1: //프로필 사진 바꾸기
                        Log.v("알림", "다이얼로그 > 앨범선택 선택");
                        selectAlbum();
                        break;
                    case 2: //프로필 사진 삭제
                        edit_img.setImageResource(R.drawable.account);
                        mCurrentPhotoPath = "null";
                        break;
                }
            }
        });
        builder.show();
    }
    EditAPI api;
    String NAME,BIRTHDAY,INTRODUCE,PHOTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo__edit);
        ButterKnife.bind(this);

        //레트로핏
        api = new MyRetrofit().create(EditAPI.class);
        //레트로핏

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // edittext 입력시 키보드가 ui 가림현상 해결

        setSupportActionBar(userinfoedit_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_black);// 뒤로가기 버튼, 내가 지정할수 있다
        if(MY_EMAIL==null){
            MY_EMAIL = MY_EMAIL_2;
        }


        getoneinfo();
//        edit_username.setText(NAME);
//        edit_birthday.setText(BIRTHDAY);
//        edit_introduce.setText(INTRODUCE);
//        File imgFile = new  File(SaveSharedPreference.getUserPhoto(Userinfo_Edit.this,MY_EMAIL));
//        if(imgFile.exists()){
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            edit_img.setImageBitmap(myBitmap);
//        }


        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM,edit_username.getText().toString());

        if(fileUri!=null){
            File orignialFile = FileUtils.getFile(this,fileUri);
            RequestBody filePart = RequestBody.create(
                    MediaType.parse(getContentResolver().getType(fileUri)),
                    orignialFile
            );
            MultipartBody.Part file = MultipartBody.Part.createFormData("file",orignialFile.getName(),filePart);
        }
    }

    public Boolean validate(){
        boolean valid = true;

        String username = edit_username.getText().toString();
        String birthday = edit_birthday.getText().toString();
        String introduce = edit_introduce.getText().toString();

        if(username.isEmpty() || username.length()>10 || username.length()<2 ||
                (!Pattern.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*",username))){
            edit_username.setError("유저 네임은 2~10자 이여야 합니다 (특수문자 포함 불가)");
            valid = false;
        }else {
            edit_username.setError(null);
        }

        if(!birthday.isEmpty()){
            if (!Pattern.matches("^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$", birthday)){
                edit_birthday.setError("생년월일은 YYYYMMDD 형식이여야 합니다 ex)19990101");
                valid = false;
            }else {
                edit_birthday.setError(null);
            }
        }

        if(!introduce.isEmpty()){
            if(introduce.length()>100){
                edit_introduce.setError("소개글은 100자 이하이여야 합니다");
                valid = false;
            }else {
                edit_introduce.setError(null);
            }
        }

        return valid;
    }

    //툴바
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.userinfo_edit_tb, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.menu_toolbar_ok:
                if (validate()){

//                    if(mCurrentPhotoPath!=null){
//                        imgUpload();
//                    }
//                    infoUpload();


                    if(mCurrentPhotoPath==null){//사진 변경 안 할 경우
                        infoUpload(false);
                    }else {
                        if(fileUri== null){//사진 찍을 경우
                            infoUpload(true);
                            imgUpload();
                        }else { //사진 고를 경우
                            img_info_Upload();
                        }
                    }

                }
        }
        return super.onOptionsItemSelected(item);
    }
    //툴바
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
                String birthday = val.getBirthday();
                String introduce = val.getIntroduce();

                Log.e("getoneinfo",username+"/"+photo+ "/"+birthday+"/"+introduce);

                if(!username.isEmpty()){
                    NAME = username;
                    BIRTHDAY = birthday;
                    INTRODUCE = introduce;
                    PHOTO = photo;
                    Log.e("getoneinfo_PHOTO",URL+PHOTO);
                    //SaveSharedPreference.setUserBitmapImage(Userinfo_Edit.this,PHOTO,MY_EMAIL);
                    edit_username.setText(NAME);
                    edit_birthday.setText(BIRTHDAY);
                    edit_introduce.setText(INTRODUCE);
                    if(PHOTO!=null){
//                        Bitmap decodedByte = StringToBitMap(PHOTO);
//                        edit_img.setImageBitmap(decodedByte);
                        Glide.with(Userinfo_Edit.this)
                                .load(URL+PHOTO)
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.load))
                                .into(edit_img);
                    }
                }
            }

            @Override
            public void onFailure(Call<Value_3> call, Throwable t) {
                Log.e("Userinfo_Edit_getoneinfo_fail","Error : "+t.getMessage());
            }
        });
    }


//    public void getoneinfo(){
//        Log.e("MY_EMAIL","/"+MY_EMAIL);
//        Call<ResponseBody> call = api.getOneInfo(MY_EMAIL);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if(!response.isSuccessful()){
//                    Log.e("Userinfo_Edit_getoneinfo_code",""+response.code());
//                    return;
//                }
//
//
//                ResponseBody val = response.body();
//                Gson gson = new Gson();
//                String s = gson.toJson(response);
//                Log.e("reponse_s",s);
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("Userinfo_Edit_getoneinfo_fail","Error : "+t.getMessage());
//            }
//        });
//    }

    public void infoUpload(final Boolean isShot){
        String username2 = edit_username.getText().toString();
        String birthday2 = edit_birthday.getText().toString();
        String introduce2 = edit_introduce.getText().toString();

        Map<String,String> map = new HashMap<>();
        String email = SaveSharedPreference.getMY_EMAIL(Userinfo_Edit.this);
        map.put("Email",email);
        map.put("Username",username2);
        map.put("Birthday",birthday2);
        if(mCurrentPhotoPath==null){
            mCurrentPhotoPath="null";
        }
        map.put("Photo",email+"_"+imageFileName2);
        map.put("Introduce",introduce2);

        Call<Value_2> call = api.editUserInfo(map);

        call.enqueue(new Callback<Value_2>() {
            @Override
            public void onResponse(Call<Value_2> call, Response<Value_2> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Userinfo_Edit.this,response.code()+"",Toast.LENGTH_SHORT).show();
                    Log.e("Userinfo_Edit_log_code",response.code()+"");
                    return;
                }

                Value_2 val = response.body();
                String value = val.getValue();
                String message = val.getMessage();
                String username = val.getUsername();
                String photo = val.getPhoto();
                String birthday = val.getBirthday();
                String introduce = val.getIntroduce();

                Log.e("Userinfo_Edit_log",value+"/"+message+"/"+username+"/"+introduce);

                if(value.equals("1")){
                    SaveSharedPreference.setUserName(Userinfo_Edit.this,username,MY_EMAIL);
                    SaveSharedPreference.setUserPhoto(Userinfo_Edit.this,photo,MY_EMAIL);
                    SaveSharedPreference.setUserBirthday(Userinfo_Edit.this,birthday,MY_EMAIL);
                    SaveSharedPreference.setUserIntroduce(Userinfo_Edit.this,introduce,MY_EMAIL);
                    MY_NAME = username;
                    MY_IMG = photo;
                   // Userinfo_Edit.this.finish();// 인텐트로 넘기지 못하는구나.. ㅠㅠ
                    if (!isShot){
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK,resultIntent);
                        finish();
                    }

                }else {
                    Toast.makeText(Userinfo_Edit.this,message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value_2> call, Throwable t) {

                Toast.makeText(Userinfo_Edit.this,"Error : "+t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("Userinfo_Edit_log_fail","Error : "+t.getMessage());
            }
        });
    }

    public void imgUpload(){
        Map<String, RequestBody> map = new HashMap<>();
        File file = new File(mCurrentPhotoPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        map.put("file\"; filename=\"" + file.getName() + "\"", requestBody);
        map.put("Email",createPartFromString(MY_EMAIL));
        Call<ServerResponse> call = api.uploadPhoto(
                "token",
                map
        );

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(Userinfo_Edit.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait");
        progressDoalog.setTitle("Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        progressDoalog.dismiss();
                        //Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        // 서버에서 base64 코드를 받아온다
                        //그리고 base64 코드를 비트맵으로 변환 후
                        //변환된 비트맵을 string 으로 변환하여 저장
                        Boolean value = serverResponse.getSuccess();
                        String photo = serverResponse.getMessage();
                        SaveSharedPreference.setUserPhoto(Userinfo_Edit.this,photo,MY_EMAIL);
                        MY_IMG = photo;
//                        String base64Image = photo;
//                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        photo = BitMapToString(decodedByte);
                        if(value==true){
                            //Userinfo_Edit.this.finish();
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK,resultIntent);
                            finish();
                        }

                        //
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    Log.e("img_Retro", serverResponse.getMessage());
                } else {
                    Log.v("img_Response", serverResponse.toString());
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressDoalog.dismiss();
                Log.e("img_failure",t.getMessage());
            }
        });
    }

    public void img_info_Upload(){
        String username2 = edit_username.getText().toString();
        String birthday2 = edit_birthday.getText().toString();
        String introduce2 = edit_introduce.getText().toString();

        Call<Value_2> call = api.uploadPhoto_Info(
                createPartFromString(SaveSharedPreference.getMY_EMAIL(Userinfo_Edit.this)),
                createPartFromString(username2),
                createPartFromString(birthday2),
                createPartFromString(introduce2),
                prepareFilePart("file",fileUri)
        );

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(Userinfo_Edit.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait");
        progressDoalog.setTitle("Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();

        call.enqueue(new Callback<Value_2>() {
            @Override
            public void onResponse(Call<Value_2> call, Response<Value_2> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Userinfo_Edit.this,response.code()+"",Toast.LENGTH_SHORT).show();
                    Log.e("Userinfo_Edit_log_code",response.code()+"");
                    return;
                }
                progressDoalog.dismiss();

                Value_2 val = response.body();
                String value = val.getValue();
                String message = val.getMessage();
                String username = val.getUsername();
                // 서버에서 base64 코드를 받아온다
                //그리고 base64 코드를 비트맵으로 변환 후
                //변환된 비트맵을 string 으로 변환하여 저장
                String photo = val.getPhoto();
//                String base64Image = photo;
//                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                photo = BitMapToString(decodedByte);
                //

                String birthday = val.getBirthday();
                String introduce = val.getIntroduce();

                Log.e("img_info_Upload",value+"/"+message+"/"+username+"/"+photo+"/"+birthday+"/"+introduce);
                if(value.equals("1")){
                    SaveSharedPreference.setUserName(Userinfo_Edit.this,username,MY_EMAIL);
                    SaveSharedPreference.setUserPhoto(Userinfo_Edit.this,photo,MY_EMAIL);
                    SaveSharedPreference.setUserBirthday(Userinfo_Edit.this,birthday,MY_EMAIL);
                    SaveSharedPreference.setUserIntroduce(Userinfo_Edit.this,introduce,MY_EMAIL);
                    MY_NAME = username;
                    MY_IMG = photo;
                   // Userinfo_Edit.this.finish();// 인텐트로 넘기지 못하는구나.. ㅠㅠ
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }else {
                    Toast.makeText(Userinfo_Edit.this,message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value_2> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(Userinfo_Edit.this,"Error : "+t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("Userinfo_Edit_log_fail","Error : "+t.getMessage());
            }
        });
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @NonNull
    public static RequestBody createPartFromString (String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM,descriptionString
        );
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri){
        Log.e("prepareFilePart_fileUri",""+fileUri);
        File file = FileUtils.getFile(this,fileUri);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );
        return MultipartBody.Part.createFormData(partName,file.getName(),requestFile);
    }

    /** 카메라 관련 코드들*/
    public void CameraPermission(){
        //TedPermission 라이브러리 -> 카메라 권한 획득
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(Userinfo_Edit.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                Log.e("Userinfo_Edit_Permission","Permission Granted");
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
               // Toast.makeText(Userinfo_Edit.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Userinfo_Edit_Permission","Permission Denied"+ deniedPermissions.toString());
            }
        };
        TedPermission.with(Userinfo_Edit.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("퍼미션 거부시 ,서비스를 이용 할 수 없습니다\n\n설정에서 퍼미션을 승인하세요 ")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                //카메라 퍼미션
                .check();
        //TedPermission 라이브러리 -> 카메라 권한 획득
    }

    //앨범 선택 클릭

    public void selectAlbum(){
        //앨범 열기
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        intent.setType("image/*");
//        startActivityForResult(intent, FROM_ALBUM);
        //        Intent intent = new Intent(Intent.ACTION_PICK); // 이걸로 하면 앱을 끄고 다시 켰을때 이미지불러오기 시 퍼미션 디나니얼나옴
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intent,FROM_ALBUM);
    }
//사진 찍기 클릭

    public void takePhoto(){
        // 촬영 후 이미지 가져옴
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null){
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                Log.e("takePhoto_mCurrentPhotoPath",photoFile.getPath());
                if(photoFile!=null){
                    Uri providerURI = FileProvider.getUriForFile(this,getPackageName(),photoFile);
                    //메니페스트에서 프로바이더 추가 하지 않으면 nullpointererror
                    imgUri = providerURI;
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(intent, FROM_CAMERA);
                }
            }
        }else{
            Log.v("알림", "저장공간에 접근 불가능");
            return;
        }
    }

    String imageFileName2;
    //카메라로 촬영한 이미지 생성
    public File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File imageFile;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(!storageDir.exists()){
            Log.v("알림","storageDir 존재 x " + storageDir.toString());
            storageDir.mkdirs();
        }
        Log.v("알림","storageDir 존재함 " + storageDir.toString());
        imageFile = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        Log.e("createImageFile_mCurrentPhotoPath",mCurrentPhotoPath);
        imageFileName2 = imageFile.getName();
        Log.e("createImageFile_imageFileName2",imageFileName2);
        return imageFile;
    }

    //사진 돌아가는거 수정
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    //사진 돌아가는거 수정

    //이미지 파일 고를때 경로 적절하게 바꿔주기
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    //이미지 파일 고를때 경로 적절하게 바꿔주기

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case FROM_ALBUM : {
                //앨범에서 가져오기
                if(data.getData()!=null){
                    try{
                        File albumFile = null;
                        albumFile = createImageFile();
                        photoURI = data.getData();
                        fileUri = photoURI;
                        albumURI = Uri.fromFile(albumFile);
                        galleryAddPic();

                        //이미지 파일 경로를 변환 해줌
                        mCurrentPhotoPath = getPath(this,photoURI);
                        imageFileName2 = mCurrentPhotoPath;
                        //이미지뷰에 이미지 셋팅
                        edit_img.setImageURI(photoURI);
                         Log.e("getPath(photoURI)",mCurrentPhotoPath+"\n"+getPath(this,photoURI));
                        //cropImage();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.v("알림","앨범에서 가져오기 에러");
                    }
                }
                break;
            }
            case FROM_CAMERA : {
                //촬영
                try{
                    Log.v("알림", "FROM_CAMERA 처리");
                    galleryAddPic();
                    //사진 회전
                    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(mCurrentPhotoPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int exifOrientation;
                    int exifDegree;
                    if (exif != null) {
                        //exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        exifDegree = exifOrientationToDegrees( ExifInterface.ORIENTATION_ROTATE_90);
                    } else {
                        exifDegree = 0;
                    }
                    //사진 회전
                    //이미지뷰에 이미지셋팅
                    //edit_img.setImageURI(imgUri);
                    fileUri = null;
                    //fileUri = data.getData();
                    edit_img.setImageBitmap(rotate(bitmap,exifDegree));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    //촬영한 이미지 저장
    public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Log.e("galleryAddPic_mCurrentPhotoPath",mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"사진이 저장되었습니다",Toast.LENGTH_SHORT).show();
    }
}
