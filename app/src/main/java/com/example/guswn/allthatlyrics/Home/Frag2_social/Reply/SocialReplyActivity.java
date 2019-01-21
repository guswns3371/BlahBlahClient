package com.example.guswn.allthatlyrics.Home.Frag2_social.Reply;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guswn.allthatlyrics.Home.Frag1_friends.FriendAPI;
import com.example.guswn.allthatlyrics.Home.Frag2_social.SocialAPI;
import com.example.guswn.allthatlyrics.Home.Frag2_social.SocialUploadResponse;
import com.example.guswn.allthatlyrics.Home.Frag3_account.Value_3;
import com.example.guswn.allthatlyrics.Home.Frag4_chat.Home_fragment4;
import com.example.guswn.allthatlyrics.Home.Frag4_chat.InnerChatActivity;
import com.example.guswn.allthatlyrics.Home.Frag4_chat.MyAdapter_InnerChat;
import com.example.guswn.allthatlyrics.Main.Logo;
import com.example.guswn.allthatlyrics.MainActivity;
import com.example.guswn.allthatlyrics.MyGlide;
import com.example.guswn.allthatlyrics.MyRetrofit;
import com.example.guswn.allthatlyrics.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.guswn.allthatlyrics.Main.Logo.MY_EMAIL_2;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_IDX;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_IMG;
import static com.example.guswn.allthatlyrics.Main.Logo.MY_NAME;
import static com.example.guswn.allthatlyrics.MainActivity.URL;
import static com.example.guswn.allthatlyrics.MainActivity.URL_withoutslash;

public class SocialReplyActivity extends AppCompatActivity {

    @BindView(R.id.social_reply_tb)
    Toolbar social_reply_tb;
    @BindView(R.id.social_reply_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.social_reply_myimg)
    ImageView social_reply_myimg;
    @BindView(R.id.social_reply_edit)
    EditText social_reply_edit;
    @BindView(R.id.social_reply_uploadtxt)
    TextView social_reply_uploadtxt;

    LinearLayoutManager mLayoutManager;
    MyAdapter_Reply myAdapter;
    ArrayList<SocialReplyModel> replytInfosList;

    Retrofit retrofit;
    SocialAPI api;
    Intent intent;
    String replyroom_idx,history_userimg,history_username,history_content,history_useridx,history_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_reply);
        ButterKnife.bind(this);
         retrofit = new MyRetrofit(retrofit).getRetrofit();
         api = retrofit.create(SocialAPI.class);

        MyGlide myGlide = new MyGlide(SocialReplyActivity.this,social_reply_myimg);
        myGlide.glideURL(URL+MY_IMG);

        setSupportActionBar(social_reply_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_black);// 뒤로가기 버튼, 내가 지정할수 있다
        getSupportActionBar().setTitle("댓글");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // edittext 입력시 키보드가 ui 가림현상 해결
        //


        intent = getIntent();
        replyroom_idx = intent.getStringExtra("replyroom_idx");
        history_userimg = intent.getStringExtra("history_userimg");
        history_username = intent.getStringExtra("history_username");
        history_content = intent.getStringExtra("history_content");
        history_useridx = intent.getStringExtra("history_useridx");
        history_time = intent.getStringExtra("history_time");

        /**소켓 에밋하기*/
        onSocketConnect();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("connect_reply_useremail" , MY_EMAIL_2);
            jsonObject.put("connect_reply_useridx" , MY_IDX);
            jsonObject.put("connect_replyroom_idx",replyroom_idx);

            mSocket.emit("connect_reply_user",jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**소켓 에밋하기*/

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        replytInfosList = new ArrayList<>();

        SocialReplyModel Firstmodel = new SocialReplyModel("null",history_useridx,replyroom_idx,history_username,URL+history_userimg,history_content,history_time);
        Firstmodel.setisLoaded(false);
        replytInfosList.add(Firstmodel);

        myAdapter = new MyAdapter_Reply(replytInfosList, SocialReplyActivity.this);
        mRecyclerView.setAdapter(myAdapter);

        /**로드*/
        loadReplyHistory(replyroom_idx);

    }

    /**댓글작성*/
    @OnClick(R.id.social_reply_uploadtxt)
    public void replyit(){
        String reply_content = social_reply_edit.getText().toString();
        if (!reply_content.equals("")){
            Date TODAY = new Date();
            SimpleDateFormat TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String  chat_time = TIME.format(TODAY);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("reply_useridx" , MY_IDX);
                jsonObject.put("reply_username" , MY_NAME);
                jsonObject.put("reply_roomidx",replyroom_idx);
                jsonObject.put("reply_time",chat_time);
                jsonObject.put("reply_content",reply_content);

                mSocket.emit("social reply message",jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            social_reply_edit.setText("");
        }

    }
    public void loadReplyHistory(String RoomIdx){
        Call<SocialReplyResponse> call = api.getSocial_ReplyHistoryList(RoomIdx);

        call.enqueue(new Callback<SocialReplyResponse>() {
            @Override
            public void onResponse(Call<SocialReplyResponse> call, Response<SocialReplyResponse> response) {
                if(!response.isSuccessful()){
                    Log.e("loadReplyHistory_code",""+response.code());
                    return;
                }
                SocialReplyResponse res = response.body();
                List<SocialReplyResponse> list = res.getSocial_replylist();

                for (SocialReplyResponse one : list){
                    /**댓글 정보*/
                    String  r_value = one.getValue();
                    String  r_message = one.getMessage();
                    String r_idx = one.getIdx();
                    String r_useridx = one.getSocial_Reply_useridx();
                    String  r_roomdix = one.getSocial_Reply_roomidx();
                    String r_content = one.getSocial_Reply_content();
                    String  r_time = one.getSocial_Reply_time();

                    if (r_value.equals("1")){
                        if (r_roomdix.equals(replyroom_idx)){ // 들어온 댓글 방에 해당하는 정보로 추리기

                            /**유저 정보*/
                            List<Value_3> userinfoarray = one.getUserIdx_Info();
                            Value_3 userinfo = userinfoarray.get(0);
                            String useridx = userinfo.getIdx();
                            String username = userinfo.getUsername();
                            String userimg =userinfo.getPhoto();
                            /**모델 세팅*/
                            /**nodejs 로 디비에 저장할때 sql 문 오류때매 가공된 문자열 제대로 바꾸기*/
                            r_time =r_time.replace("\'","");

                            if ((r_content.length() - 1)>0){
                                r_content = r_content.substring( 0, r_content.length() - 1);
                            }
                            r_content =r_content.replaceFirst("'","");
                            r_content =r_content.replace("\\","");
                            SocialReplyModel loadedmodel = new SocialReplyModel(r_idx,useridx,r_roomdix,username,URL_withoutslash+userimg,r_content,r_time);
                            loadedmodel.setisLoaded(true);
                            replytInfosList.add(loadedmodel);
                        }
                    }else if (r_value.equals("0")){
                        Toast.makeText(SocialReplyActivity.this,r_message,Toast.LENGTH_LONG).show();
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SocialReplyResponse> call, Throwable t) {
                Log.e("loadReplyHistory_fail","Error : "+t.getMessage());
            }
        });
    }
    /**socket*/
    Socket mSocket;
    boolean isConnected;
    {
        try {
            mSocket = IO.socket(URL_withoutslash+":5000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void onSocketConnect() {
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisConnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR,onConnectionError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT,onConnectionError);
        mSocket.on("social reply",onNewReply);
        mSocket.connect();
    }
    @Override
    protected void onDestroy() {
        //mSocket.disconnect(); // 여기서 소케송신을 끊어버리면 home_frag4 에서 소켓이 disconnect 되버리므로 끊으면 안됨
        mSocket.off(Socket.EVENT_CONNECT,onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT,onDisConnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR,onConnectionError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT,onConnectionError);
        mSocket.off("social reply",onNewReply);
        super.onDestroy();
    }
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected){
                        Toast.makeText(getApplicationContext(),"Connected reply",Toast.LENGTH_SHORT).show();
                        isConnected=true;

                    }
                }
            });
        }
    };
    private Emitter.Listener onNewReply = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject dataRecieved = (JSONObject) args[0];
                    String idx,reply_useridx,reply_username,reply_roomidx,reply_time,reply_content,reply_userimg;
                    try{
                        idx = dataRecieved.getString("reply_idx");
                        reply_useridx = dataRecieved.getString("reply_useridx");
                        reply_username = dataRecieved.getString("reply_username");
                        reply_userimg = dataRecieved.getString("reply_userimg");
                        reply_roomidx = dataRecieved.getString("reply_roomidx");
                        reply_time = dataRecieved.getString("reply_time");
                        reply_content = dataRecieved.getString("reply_content");

                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.e("2_dataRecieved_onNewReply ","JSONException : "+e);
                        return;
                    }

                    /**노드서버에서 디비에 저장하고 꺼내온 값으로 모델 세팅해준다*/
                    if (reply_roomidx.equals(replyroom_idx)){
                        if (reply_content!=null){

                            /**nodejs 로 디비에 저장할때 sql 문 오류때매 가공된 문자열 제대로 바꾸기*/
                            reply_time =reply_time.replace("\'","");
                            reply_content = reply_content.substring( 0, reply_content.length() - 1);
                            reply_content =reply_content.replaceFirst("'","");
                            reply_content =reply_content.replace("\\","");

                            SocialReplyModel model = new SocialReplyModel(idx,reply_useridx,reply_roomidx,reply_username,URL+reply_userimg,reply_content,reply_time);
                            model.setisLoaded(false);
                            /**리사이클러뷰 아이템 위로 쌓기*/
                            /**postion 0 은 게시물 글이 있으므로 position 1부터 쌓이도록*/
                            replytInfosList.add(1,model);
                            myAdapter.notifyItemInserted(1);
                            /**리사이클러뷰 아이템 위로 쌓기*/
//                            myAdapter.notifyDataSetChanged();
                            mRecyclerView.smoothScrollToPosition(1);
                        }
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
                    Toast.makeText(getApplicationContext(),"DisConnected reply",Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(getApplicationContext(),"ConnetionError reply",Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    //툴바
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
