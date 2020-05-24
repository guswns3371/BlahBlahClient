package com.example.guswn.allthatlyrics.ui.chat.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.guswn.allthatlyrics.adapter.InnerChatAdapter;
import com.example.guswn.allthatlyrics.extension.MyRetrofit;
import com.example.guswn.allthatlyrics.response.userResponse3;
import com.example.guswn.allthatlyrics.ui.chat.ChatFragment;
import com.example.guswn.allthatlyrics.ui.auth.LogoActivity;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.api.ChatAPI;
import com.example.guswn.allthatlyrics.model.InnerChatModel;
import com.example.guswn.allthatlyrics.response.InnerChatResponse;
import com.example.guswn.allthatlyrics.response.UnReadCountResponse;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static com.example.guswn.allthatlyrics.adapter.ChatAdapter.outidx;
import static com.example.guswn.allthatlyrics.adapter.ChatAdapter.outmessage;
import static com.example.guswn.allthatlyrics.adapter.ChatAdapter.outtime;
import static com.example.guswn.allthatlyrics.adapter.ChatAdapter.unReadMessage;
import static com.example.guswn.allthatlyrics.adapter.InnerChatAdapter.AllPeople;
import static com.example.guswn.allthatlyrics.adapter.InnerChatAdapter.isRead;
import static com.example.guswn.allthatlyrics.adapter.InnerChatAdapter.isReadList;
import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_EMAIL_2;
import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_IDX;

import static com.ipaulpro.afilechooser.utils.FileUtils.getDataColumn;

public class InnerChatActivity extends AppCompatActivity {

    ArrayList<String> peopleList = new ArrayList<>();
    String peopleList_json;
    private static final int FROM_CAMERA = 0;
    private static final int FROM_ALBUM = 1;
    private Uri imgUri, photoURI, albumURI,fileUri;
    private String mCurrentPhotoPath;

    @BindView(R.id.innerchat_tb)
    Toolbar innerchat_tb;
    @BindView(R.id.innerchat_recyclerview)
    RecyclerView mRecyclerView;


    @BindView(R.id.innerchat_send_btn)
    ImageView innerchat_send_btn;
    @BindView(R.id.innerchat_plus_btn)
    ImageView innerchat_plus_btn;
    @BindView(R.id.innerchat_edittext)
    EditText innerchat_edittext;

    @OnClick(R.id.innerchat_plus_btn)
    public void selectfile(){
        CameraPermission();
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("사진 첨부하기");
        ListItems.add("파일 첨부하기");

        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("파일 올리기");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {

                switch (pos){
                    case 0: //사진 첨부하기
                        selectAlbum();
                        //sendfile();
                        break;
                    case 1: //파일 첨부하기

                        break;
                }
            }
        });
        builder.show();
    }


    public void savemessage_withfile(String chat_time,String chat_message,String chat_useridx,String chatroom_idx,String chat_readpeople,String chat_readpeople_list){
        Map<String, RequestBody> map = new HashMap<>();
        File file = new File(mCurrentPhotoPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        map.put("file\"; filename=\"" + file.getName() + "\"", requestBody);
        map.put("chat_time",createPartFromString(chat_time));
        map.put("chat_message",createPartFromString(chat_message));
        map.put("chat_useridx",createPartFromString(chat_useridx));
        map.put("chatroom_idx",createPartFromString(chatroom_idx));
        map.put("chat_readpeople",createPartFromString(chat_readpeople));
        map.put("chat_readpeople_list",createPartFromString(chat_readpeople_list));
        Call<InnerChatResponse> call = api_chat.makechatHistory_withfile(
                "token",
                map
        );

        call.enqueue(new Callback<InnerChatResponse>() {
            @Override
            public void onResponse(Call<InnerChatResponse> call, Response<InnerChatResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("savemessage_withfile_code",""+response.code());
                    return;
                }
                InnerChatResponse res = response.body();
                String value = res.getValue();
                String message =res.getMessage();
                String chat_isfile =res.getChat_isfile();
                String chat_message = res.getChat_message();
                String chat_time = res.getChat_time();
                String chat_useridx = res.getChat_useridx();
                String chatroom_idx = res.getChatroom_idx();
                String chat_readpeople = res.getChat_readpeople();
                String chat_readpeople_list = res.getChat_readpeople_list();

                Log.e("savemessage_withfile ",value+"/"+message+"/"+chat_isfile+"/"+chat_message+"/"+chat_time+"/"+chat_useridx+"/"+chatroom_idx+"/"+chat_readpeople+"/"+chat_readpeople_list+"/");

            }

            @Override
            public void onFailure(Call<InnerChatResponse> call, Throwable t) {
                Log.e("savemessage_withfile_fail","Error : "+t.getMessage());
            }
        });

    }

    /** 레트로핏으로 파일 올리때 관련된 코드*/
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
    /** 레트로핏으로 파일 올리때 관련된 코드*/

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
                        Log.e("getPath(photoURI)",mCurrentPhotoPath+"\n"+getPath(this,photoURI));

                        if (fileUri!=null){

                            Date TODAY = new Date();
                            SimpleDateFormat TIME = new SimpleDateFormat("hh:mm");
                            chat_time = TIME.format(TODAY);
                            chat_message = innerchat_edittext.getText().toString();
                            chat_useridx = LogoActivity.MY_IDX;
                            chatroom_idx = chatroom_idx_1;

                            String unchat_readpeople = (Integer.parseInt(chatpeople_num)-peopleList.size())+"";
                            AllPeople =chatpeople_num; // 전체 사람수
                            isRead = peopleList.size()+""; //읽은사람수
                            InnerChatModel info = new InnerChatModel(true, MY_IDX, "", "", fileUri.toString()+"", chat_time,peopleList.size()+"");
                            info.setIsReadPeopleList(peopleList);
                            info.setChat_readpeople(unchat_readpeople);
                            info.setIsFile("yes");
                            innerChatInfosList.add(info);

                            //사진을 채팅방에 올릴떄
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("chat_firebase_token",firebaseToken);
                                jsonObject.put("chat_firebase_topic",chatroom_idx);
                                jsonObject.put("chat_isfile" , "yes");
                                jsonObject.put("chat_message" , "사진");
                                jsonObject.put("chat_time" , chat_time);
                                jsonObject.put("chat_useridx" , MY_IDX);
                                jsonObject.put("chat_username" , LogoActivity.MY_NAME);
                                jsonObject.put("chatroom_idx",chatroom_idx);
                                jsonObject.put("chatroom_name",chatroomname);
                                jsonObject.put("chatpeople_num",chatpeople_num);

                                mSocket.emit("chat message", jsonObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            /**실험 성공~*/
                            outidx= chatroom_idx;
                            outmessage = "사진";
                            outtime = chat_time;
                            ChatFragment.myAdapter.notifyDataSetChanged();
                            /***/
                            innerchat_edittext.setText("");
                            if(myAdapter !=null){
                                myAdapter.notifyDataSetChanged();
                                mRecyclerView.smoothScrollToPosition(innerChatInfosList.size()); // 맨 아래로 이동
                            }
                            savemessage_withfile(chat_time, "사진", chat_useridx, chatroom_idx,peopleList.size()+"",peopleList_json);

                        }
                        //cropImage();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.v("알림","앨범에서 가져오기 에러");
                    }
                }
                break;
            }
        }
    }

    @OnClick(R.id.innerchat_send_btn)
    public void sendmessage(){
        Date TODAY = new Date();
        SimpleDateFormat TIME = new SimpleDateFormat("hh:mm");
        chat_time = TIME.format(TODAY);
        chat_message = innerchat_edittext.getText().toString();
        chat_useridx = LogoActivity.MY_IDX;
        chatroom_idx = chatroom_idx_1;

        if(!chat_message.equals("")) {
            String unchat_readpeople = (Integer.parseInt(chatpeople_num)-peopleList.size())+"";
            AllPeople =chatpeople_num; // 전체 사람수
            isRead = peopleList.size()+""; //읽은사람수
            InnerChatModel info = new InnerChatModel(true, MY_IDX, "", "", chat_message, chat_time,peopleList.size()+"");
            info.setIsReadPeopleList(peopleList);
            info.setIsFile("no");
            info.setChat_readpeople(unchat_readpeople);
            innerChatInfosList.add(info);

            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("chat_firebase_token",firebaseToken);
                jsonObject.put("chat_firebase_topic",chatroom_idx);
                jsonObject.put("chat_isfile" , "no");
                jsonObject.put("chat_message" , chat_message);
                jsonObject.put("chat_time" , chat_time);
                jsonObject.put("chat_useridx" , MY_IDX);
                jsonObject.put("chat_username" , LogoActivity.MY_NAME);
                jsonObject.put("chatroom_idx",chatroom_idx);
                jsonObject.put("chatroom_name",chatroomname);
                jsonObject.put("chatpeople_num",chatpeople_num);

                mSocket.emit("chat message", jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            /**실험 성공~*/
            outidx= chatroom_idx;
            outmessage = chat_message;
            outtime = chat_time;
            ChatFragment.myAdapter.notifyDataSetChanged();
            /***/
            innerchat_edittext.setText("");
//        mLayoutManager.setStackFromEnd(true);
            if(myAdapter !=null){
                myAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(innerChatInfosList.size()); // 맨 아래로 이동
            }
            savemessage(chat_time, chat_message, chat_useridx, chatroom_idx,peopleList.size()+"",peopleList_json);
        }
    }

    InnerChatAdapter myAdapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<InnerChatModel> innerChatInfosList;

    ChatAPI api_chat;
    Intent intent;
    String firebaseToken;
    String chatroomname;
    String chatroom_idx_1,chat_time,chat_message,chat_useridx,chatroom_idx,chatpeople_num;
    Socket mSocket;
    Timer t;
    boolean isConnected;
    boolean loadComplete; //updatechatHisory 가 끝나면 그 뒤에 로드 해준다

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_chat);
        ButterKnife.bind(this);

        try {
            mSocket = IO.socket(getString(R.string.NodeServer));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        intent = getIntent();
        chatroomname = intent.getStringExtra("chatroomname");
        chatroom_idx_1 = intent.getStringExtra("chatroom_idx");
        if(intent.getStringExtra("chatperson_idx")!=null){
            chatroom_idx_1 = intent.getStringExtra("chatperson_idx");
        }
        chatpeople_num = intent.getStringExtra("chatpeople_num");
        AllPeople = chatpeople_num;

        //파이어 베이스 토픽이름을 방 이름으로 한다
        if (chatroom_idx_1!=null){
            FirebaseMessaging.getInstance().subscribeToTopic(chatroom_idx_1);

        }
        firebaseToken = FirebaseInstanceId.getInstance().getToken();
        setSupportActionBar(innerchat_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_black);// 뒤로가기 버튼, 내가 지정할수 있다
        getSupportActionBar().setTitle(chatroomname);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // edittext 입력시 키보드가 ui 가림현상 해결
        //
        onSocketConnect();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("connect_chat_useremail" , MY_EMAIL_2);
            jsonObject.put("connect_chat_useridx" , LogoActivity.MY_IDX);
            jsonObject.put("connect_chatroom_idx",chatroom_idx_1);

            mSocket.emit("connect_user",jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        api_chat = new MyRetrofit().create(ChatAPI.class);
        //레트로핏

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        innerChatInfosList = new ArrayList<>();
        myAdapter = new InnerChatAdapter(innerChatInfosList, InnerChatActivity.this);
        mRecyclerView.setAdapter(myAdapter);
        mLayoutManager.setStackFromEnd(true);//목록의 마지막 요소로 recyclerview 포커스 이동
//        loadChatHistory();
        t = new java.util.Timer();
        t.schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        loadChatHistory();
                    }
                    }, 200);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**실험성공 이거 하면 */
        outidx= chatroom_idx_1;
        unReadMessage = "0";
        ChatFragment.myAdapter.notifyDataSetChanged();
        update_ireadit(); // 읽었으니 안읽은 메시지 개수를 0으로 디비 저장 해준다
        /***/
    }


    public void update_ireadit(){
        Map<String ,String > map = new HashMap<>();
        map.put("user_idx",MY_IDX);
        map.put("chatroom_idx",chatroom_idx_1);

        Call<UnReadCountResponse> call = api_chat.makeireadit_perRoomidx(map);

        call.enqueue(new Callback<UnReadCountResponse>() {
            @Override
            public void onResponse(Call<UnReadCountResponse> call, Response<UnReadCountResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("update_ireadit_code",""+response.code());
                    return;
                }
                UnReadCountResponse val = response.body();
                String value = val.getValue();
                String message = val.getMessage();
                String chatroom_idx = val.getChatroom_idx();
                String user_idx = val.getUser_idx();
                String unread_count = val.getUnread_count();
                Log.e("update_ireadit ",value+"/"+message+"/"+chatroom_idx+"/"+user_idx+"/"+unread_count);
            }

            @Override
            public void onFailure(Call<UnReadCountResponse> call, Throwable t) {
                Log.e("Hupdate_ireadit_fail","Error : "+t.getMessage());
            }
        });
    }

    private void onSocketConnect() {
        mSocket.on("connect_user",onConnectedUser);
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisConnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR,onConnectionError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT,onConnectionError);
        mSocket.on("chat message",onNewMessage);
        mSocket.on("disconnect_user",onDisconnectedUser);
        mSocket.connect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("onBackPressed_InnerChat","onBackPressed_InnerChat");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("disconnect_chat_useremail" , MY_EMAIL_2);
            jsonObject.put("disconnect_chat_useridx" , LogoActivity.MY_IDX);
            jsonObject.put("disconnect_chatroom_idx",chatroom_idx_1);

            mSocket.emit("disconnect_user",jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        Log.e("onStop_innerChat","onStop_innerChat");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 소켓을 off하는걸 onStop 에 놓는다면
        // 사진을 고를 때 (다른 액티비티로 넘어가기때문에) onStop이 실행되어서
        // 사진을 고르고 보낸 후에 소켓이 off되어서 더이상의 메시지를 받을수 없게 된다
        // 메시지를 보내는게 가능한 이유는 Home 액티비티에서 소켓을 연결 시켜 놓았기때문이다 (아직 disconnect 되지 않았으므로)
        Log.e("onDestroy_innerChat","onDestroy_innerChat");
        //mSocket.disconnect(); // 여기서 소케송신을 끊어버리면 home_frag4 에서 소켓이 disconnect 되버리므로 끊으면 안됨
        mSocket.off("disconnect_user",onDisconnectedUser);
        mSocket.off(Socket.EVENT_CONNECT,onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT,onDisConnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR,onConnectionError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT,onConnectionError);
        mSocket.off("chat message",onNewMessage);
        mSocket.off("connect_user",onConnectedUser);
        t.cancel();
        super.onDestroy();
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected){
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("connect_chat_useremail" , MY_EMAIL_2);
                            jsonObject.put("connect_chat_useridx" , LogoActivity.MY_IDX);
                            jsonObject.put("connect_chatroom_idx",chatroom_idx_1);

                            mSocket.emit("connect_user",jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                        isConnected=true;

                    }
                }
            });
        }
    };

    private Emitter.Listener onDisConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected=false;
                    Toast.makeText(getApplicationContext(),"DisConnected",Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    private Emitter.Listener onConnectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(),"ConnetionError",Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("1_dataRecieved_InnerChat ","onNewMessage_InnerChat");
                    JSONObject dataRecieved = (JSONObject) args[0];
                    String useridx,message,chatroomidx,isfile,unreadCOUNT;
                    JSONObject unreadcount_list;
                    try{
                        isfile = dataRecieved.getString("chat_isfile");
                        useridx = dataRecieved.getString("chat_useridx");
                        message = dataRecieved.getString("chat_message");
                        chatroomidx = dataRecieved.getString("chatroom_idx");
                        unreadcount_list = dataRecieved.getJSONObject("chat_unreadcount_list");
                        unreadCOUNT =unreadcount_list.get(MY_IDX).toString();
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.e("2_dataRecieved_InnerChat ","JSONException : "+e);
                        return;
                    }

                    if (message!=null){
                        Log.e("2_dataRecieved_InnerChat ", isfile + "/" + useridx + "/" + message + "/" + chatroomidx+"/"+unreadcount_list+"/"+unreadCOUNT);
                        Date TODAY = new Date();
                        SimpleDateFormat TIME = new SimpleDateFormat("hh:mm");
                        String chattime = TIME.format(TODAY);
                            /**실험 성공*/
                            outidx= chatroomidx;
                            if (isfile.equals("yes")){
                                outmessage = "사진";
                            }else if (isfile.equals("no")){
                                outmessage = message;
                            }
                        Log.e("3_dataRecieved_InnerChat_outmessage ",outmessage);
                            outtime = chattime;
                            unReadMessage = unreadCOUNT;
                            ChatFragment.myAdapter.notifyDataSetChanged();
                        Log.e("4_dataRecieved_InnerChat_outmessage ",outmessage);
                            /***/
                        if(chatroomidx.equals(chatroom_idx_1)){
                            if(!useridx.equals(MY_IDX)){
                                //Toast.makeText(getApplicationContext(),useridx+" Connected",Toast.LENGTH_SHORT).show();
                                String chat_readpeople = (Integer.parseInt(chatpeople_num)-peopleList.size())+"";
                                AllPeople =chatpeople_num; // 전체 사람수
                                isRead = peopleList.size()+""; //읽은사람수
                                isReadList = peopleList;
                                if (isfile.equals("no")) {// 그냥 텍스트 문자일경우
                                    getuserinfoByidx(useridx, message, chattime, chat_readpeople,false);// 저장은 하지 않고 아이템으로 뿌려주기만함
                                }else if (isfile.equals("yes")) { // 이미지 uri 문자일경우 대신 string 으로 형변환됨
                                    getuserinfoByidx(useridx, message, chattime, chat_readpeople,true);// 저장은 하지 않고 아이템으로 뿌려주기만함
                                }
//                               innerChatInfosList.add(new InnerChatInfo(false, MY_IDX, "", useridx, message, chattime));
                                //savemessage(chattime,message,useridx,chatroom_idx_1);
                                // myAdapter.notifyDataSetChanged();
                            }
                        }
                    }else {
                        Log.e("2_dataRecieved_InnerChat","null");
                    }





                }
            });
        }
    };

    int a=0;
    private Emitter.Listener onConnectedUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject dataRecieved = (JSONObject) args[0];
                    String connect_chat_useridx,connect_chat_useremail,connect_chatroom_idx,whos_on_now;
                    JSONObject whos_on_now_json;
                    try{

                        connect_chat_useridx = dataRecieved.getString("connect_chat_useridx");
                        connect_chat_useremail = dataRecieved.getString("connect_chat_useremail");
                        connect_chatroom_idx = dataRecieved.getString("connect_chatroom_idx");
                        whos_on_now_json = dataRecieved.getJSONObject("whos_on_now");
                        Log.e("whos_on_now_json.toString()_1 ",whos_on_now_json.toString());
                        Log.e("whos_on_now_json.toString()_2 ",whos_on_now_json.get(chatroom_idx_1).toString());
                        ArrayList<String> list = new ArrayList<String>();
                        peopleList = list;
                        peopleList_json = whos_on_now_json.get(chatroom_idx_1).toString();
                        JSONArray jsonArray = (JSONArray) whos_on_now_json.get(chatroom_idx_1);

                        if (jsonArray != null) {
                            int len = jsonArray.length();
                            for (int i=0;i<len;i++){
                                list.add(jsonArray.get(i).toString());
                            }
                        }

                        Log.e("whos_on_now_json.toString()_3 ",list.toString());
                        Log.e("chatpeople_num_4 ",chatpeople_num);
                        AllPeople =chatpeople_num; // 전체 사람 수
                        isRead = peopleList.size()+""; //지금 읽은 사람 수
                        isReadList = list;

                        updatechatHisory(chatroom_idx_1,peopleList_json,MY_IDX);//서버에 지금 읽은 사람들을 저장한다

                        myAdapter.notifyDataSetChanged();
                    }catch (JSONException e){
                        e.printStackTrace();
                        return;
                    }
                    Log.e("onConnectedUser",connect_chat_useridx+"/"+connect_chat_useremail+"/"+connect_chatroom_idx);
//                    if (connect_chat_useridx!=null){
//                        if(connect_chatroom_idx.equals(chatroom_idx)){
//                            connectedPeople.add(connect_chat_useridx);
//                            String connectePeopleJson = new Gson().toJson(connectedPeople);
//                            Log.e("connectePeopleJson",connectePeopleJson);
//                            Toast.makeText(getApplicationContext(),"지금 여기 있는 사람 "+connectedPeople.size()+"명 / "+connectePeopleJson,Toast.LENGTH_SHORT).show();
//                        }
//                    }else {
//                        Log.e("connectePeopleJson","null");
//                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnectedUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject dataRecieved = (JSONObject) args[0];
                    String disconnect_chat_useremail,disconnect_chat_useridx,disconnect_chatroom_idx;
                    JSONObject whos_on_now_json;
                    try{
                        disconnect_chat_useremail = dataRecieved.getString("disconnect_chat_useremail");
                        disconnect_chat_useridx = dataRecieved.getString("disconnect_chat_useridx");
                        disconnect_chatroom_idx = dataRecieved.getString("disconnect_chatroom_idx");
                        whos_on_now_json = dataRecieved.getJSONObject("whos_on_now");
                        Log.e("whos_on_now_json.toString()_1 ",whos_on_now_json.toString());
                        Log.e("whos_on_now_json.toString()_2 ",whos_on_now_json.get(chatroom_idx_1).toString());
                        ArrayList<String> list = new ArrayList<String>();
                        peopleList = list;
                        peopleList_json = whos_on_now_json.get(chatroom_idx_1).toString();
                        //updatechatHisory(chatroom_idx_1,peopleList_json,MY_IDX);
                        JSONArray jsonArray = (JSONArray) whos_on_now_json.get(chatroom_idx_1);
                        if (jsonArray != null) {
                            int len = jsonArray.length();
                            for (int i=0;i<len;i++){
                                list.add(jsonArray.get(i).toString());
                            }
                        }
                        Log.e("whos_on_now_json.toString()_3 ",list.toString());
                        AllPeople =chatpeople_num; // 전체 사람 수
                        isRead = peopleList.size()+""; //지금 읽은 사람 수
                        isReadList = list;

//                        myAdapter.notifyDataSetChanged();
                    }catch (JSONException e){
                        e.printStackTrace();
                        return;
                    }
                    Log.e("onConnectedUser",disconnect_chat_useridx+"/"+disconnect_chat_useremail+"/"+disconnect_chatroom_idx);
//                    if (disconnect_chat_useridx!=null){
//                        if (disconnect_chatroom_idx.equals(chatroom_idx)){
//                            connectedPeople.remove(disconnect_chat_useridx);
//                            String connectePeopleJson = new Gson().toJson(connectedPeople);
//                            Log.e("connectePeopleJson",connectePeopleJson);
//                            Toast.makeText(getApplicationContext(),"지금 여기 있는 사람 "+connectedPeople.size()+"명 / "+connectePeopleJson,Toast.LENGTH_SHORT).show();
//                        }
//                          }else {
//                        Log.e("connectePeopleJson","null");
//                    }
                }
            });
        }
    };


    public void  loadChatHistory(){
        Log.e("loadChatHistory_chatroom_idx",chatroom_idx_1);
        Call<InnerChatResponse> call = api_chat.getchatHistory_chatroomidx(chatroom_idx_1);

        call.enqueue(new Callback<InnerChatResponse>() {
            @Override
            public void onResponse(Call<InnerChatResponse> call, Response<InnerChatResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("loadChatHistory_code",""+response.code());
                    return;
                }
                InnerChatResponse res1 = response.body();

                List<InnerChatResponse> res2 = res1.getChathistorylist();
                if(res2 !=null){
                    int i = 0;
                    for(InnerChatResponse res : res2){
                        String value = res.getValue();
                        String message =res.getMessage();

                        if(value.equals("1")){
                            String ChatHistoryIdx = res.getChathistory_idx();
                            String ChatHistory_useridx = res.getChat_useridx();
                            String ChatHistory_content = res.getChat_message();
                            String ChatHistory_time = res.getChat_time();
                            String ChatHistory_readpeolenum =res.getChat_readpeople();
                            String ChatHistory_readpeople_list =res.getChat_readpeople_list();
                            String ChatHistory_isfile = res.getChat_isfile();

                            Log.e("Load_peopleList "+ChatHistoryIdx+"("+ChatHistory_content+")",ChatHistory_readpeople_list);
                            ArrayList<String> list = new ArrayList<>();
                            try {
                                JSONArray jsonArray = new JSONArray(ChatHistory_readpeople_list);
                                if (jsonArray != null) {
                                    int len = jsonArray.length();
                                    for (int k=0;k<len;k++){
                                        list.add(jsonArray.get(k).toString());
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            List<userResponse3> userinfolist = res.getInnerchatuserinfolist();
                            String Photo = userinfolist.get(0).getPhoto();
                            String Username = userinfolist.get(0).getUsername();
                            Boolean isMine;
                            if(ChatHistory_useridx.equals(MY_IDX)){
                                isMine=true;
                            }else {
                                isMine=false;
                            }

                            InnerChatModel info = new InnerChatModel(isMine, ChatHistory_useridx, Photo, Username, ChatHistory_content, ChatHistory_time, ChatHistory_readpeolenum);
                            info.setIsLoaded("loaded");
                            if (ChatHistory_isfile.equals("yes")){
                                info.setIsFile("yes");
                            }else if (ChatHistory_isfile.equals("no")){
                                info.setIsFile("no");
                            }
                            info.setIsReadPeopleList(list);
                            innerChatInfosList.add(info);
//                                int people_num = (Integer.parseInt(chatpeople_num)-1);
//                                String str_people_num = people_num+"";
//
//                                    if(Integer.parseInt(ChatHistory_readpeole) < people_num) {
//                                        // 맨 마지막 댓글 읽은 사람 < 채팅방에 있는 사람
//                                        InnerChatInfo info = new InnerChatInfo(isMine, ChatHistory_useridx, Photo, Username, ChatHistory_content, ChatHistory_time, ChatHistory_readpeole);
//                                        info.setLast("last"); //마지막 로드된 댓글이라는 걸 체크 해줌
//                                        innerChatInfosList.add(info);
//                                    }else {
//                                        // 맨 마지막 댓글 읽은 사람 >= 채팅방에 있는 사람
//                                        innerChatInfosList.add(new InnerChatInfo(isMine, ChatHistory_useridx, Photo, Username, ChatHistory_content, ChatHistory_time,str_people_num));
//                                    }
                            myAdapter.notifyDataSetChanged();
                        }else {
                            //Toast toast =  Toast.makeText(InnerChatActivity.this,message,Toast.LENGTH_SHORT);
                            //toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                            //toast.show();
                            //Log.e("loadChatHistory_history",message);
                        }
                    }
//                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<InnerChatResponse> call, Throwable t) {
                Log.e("loadChatHistory_fail","Error : "+t.getMessage());
            }
        });
    }


    public void savemessage(String chat_time,String chat_message,String chat_useridx,String chatroom_idx,String chat_readpeople,String chat_readpeople_list){
        Map<String,String> map = new HashMap<>();
        map.put("chat_time",chat_time);
        map.put("chat_message",chat_message);
        map.put("chat_useridx",chat_useridx);
        map.put("chatroom_idx",chatroom_idx);
        map.put("chat_readpeople",chat_readpeople);
        map.put("chat_readpeople_list",chat_readpeople_list);

        Call<InnerChatResponse> call = api_chat.makechatHistory(map);

        call.enqueue(new Callback<InnerChatResponse>() {
            @Override
            public void onResponse(Call<InnerChatResponse> call, Response<InnerChatResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("savemessage_code",""+response.code());
                    return;
                }

                InnerChatResponse res = response.body();
                String chat_time = res.getChat_time();
                String chat_message = res.getChat_message();
                String chat_useridx = res.getChat_useridx();
                String chatroom_idx = res.getChatroom_idx();
                String chat_readpeople = res.getChat_readpeople();

                Log.e("InnerChatActivity","savemessage_ "+
                        chat_time+"/"+chat_message+"/"+chat_useridx+"/"+chatroom_idx+"/"+chat_readpeople);

//                innerchat_edittext.setText("");

//                mRecyclerView.setHasFixedSize(true);
//                mLayoutManager = new LinearLayoutManager(InnerChatActivity.this);
//                mRecyclerView.setLayoutManager(mLayoutManager);
//                innerChatInfosList = new ArrayList<>();
//                myAdapter = new MyAdapter_InnerChat(innerChatInfosList, InnerChatActivity.this);
//                mRecyclerView.setAdapter(myAdapter);
//                loadChatHistory();
                //mLayoutManager.setStackFromEnd(true);
                mRecyclerView.smoothScrollToPosition(innerChatInfosList.size()); // 맨 아래로 이동
                updatechatHisory(chatroom_idx_1,peopleList_json,MY_IDX);

            }

            @Override
            public void onFailure(Call<InnerChatResponse> call, Throwable t) {
                Log.e("savemessage_fail","Error : "+t.getMessage());
            }
        });
    }


    public void updatechatHisory(String chatroom_idx,String chat_readpeople_list,String user_idx){
        Map<String,String> map = new HashMap<>();
        map.put("chatroom_idx",chatroom_idx);
        map.put("chat_readpeople_list",chat_readpeople_list);
        map.put("user_idx",MY_IDX);

        Call<InnerChatResponse> call = api_chat.updatechatHistory(map);
        call.enqueue(new Callback<InnerChatResponse>() {
            @Override
            public void onResponse(Call<InnerChatResponse> call, Response<InnerChatResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("updatechatHisory_code",""+response.code());
                    return;
                }
                InnerChatResponse res = response.body();
                String value = res.getValue();
                String message = res.getMessage();
                String room_idx = res.getChatroom_idx();
                String readpeople = res.getChat_readpeople();

                Log.e("updatechatHisory ",value+"/"+message+"/"+room_idx+"/"+readpeople);

                loadComplete =true;
            }

            @Override
            public void onFailure(Call<InnerChatResponse> call, Throwable t) {
                Log.e("updatechatHisory_fail","Error : "+t.getMessage());

            }
        });

    }

    public void getuserinfoByidx(final String useridx, final String message2, final String time2,final String readpeople2,final boolean isFile){
        Call<userResponse3> call = api_chat.getOneInfo3_idx(useridx);

        call.enqueue(new Callback<userResponse3>() {
            @Override
            public void onResponse(Call<userResponse3> call, Response<userResponse3> response) {
                if(!response.isSuccessful()){
                    Log.e("getuserinfoByidx_InnerChat_code",""+response.code());
                    return;
                }
                userResponse3 val = response.body();
                String idx = val.getIdx();
                String email = val.getEmail();
                String username = val.getUsername();
                String photo = val.getPhoto();
                String birthday = val.getBirthday();
                String introduce = val.getIntroduce();
                Log.e("getuserinfoByidx_InnerChat ","/"+isFile+"/"+useridx+"/"+photo+"/"+username+"/"+message2+"/"+time2+"/"+readpeople2);
                if (isFile){
                    InnerChatModel info = new InnerChatModel(false, useridx, photo, username, message2, time2,readpeople2);
                    info.setIsLoaded("default");
                    info.setIsFile("yes");
                    innerChatInfosList.add(info);
                }else {
                    InnerChatModel info = new InnerChatModel(false, useridx, photo, username, message2, time2,readpeople2);
                    info.setIsFile("no");
                    info.setIsLoaded("default");
                    innerChatInfosList.add(info);
                }
                myAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(innerChatInfosList.size()); // 맨 아래로 이동

            }

            @Override
            public void onFailure(Call<userResponse3> call, Throwable t) {
                Log.e("getuserinfoByidx_InnerChat_fail","Error : "+t.getMessage());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.inner_chat_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("disconnect_chat_useremail" , MY_EMAIL_2);
                    jsonObject.put("disconnect_chat_useridx" , LogoActivity.MY_IDX);
                    jsonObject.put("disconnect_chatroom_idx",chatroom_idx_1);

                    mSocket.emit("disconnect_user",jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
                return true;
            }
            case R.id.menu_toolbar_inner_chat_info:
                Toast.makeText(InnerChatActivity.this,"drawer",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //툴바



    /*******************************파일 고를때 관련된 코드******************************/

    /** 1카메라 관련 코드*/
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
        TedPermission.with(InnerChatActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("퍼미션 거부시 ,서비스를 이용 할 수 없습니다\n\n설정에서 퍼미션을 승인하세요 ")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                //카메라 퍼미션
                .check();
        //TedPermission 라이브러리 -> 카메라 권한 획득
    }
    /** 1카메라 관련 코드*/

    /**2 */
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
        return imageFile;
    }
    /**2 */

    /** 3*/
    public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Log.e("galleryAddPic_mCurrentPhotoPath",mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"사진이 저장되었습니다",Toast.LENGTH_SHORT).show();
    }
    /**4 */

    /** 5*/
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
    /** 5*/

    /** 6*/
    public void selectAlbum(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intent,FROM_ALBUM);
    }
    /** 6*/
}
