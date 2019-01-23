package com.example.guswn.allthatlyrics.Home.Frag2_social.Reply;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import static com.example.guswn.allthatlyrics.MainActivity.showKeyboard;

public class SocialReplyActivity extends AppCompatActivity implements MyAdapter_Reply.SocialReplyClickListener{

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

        SocialReplyModel Firstmodel = new SocialReplyModel("null",history_useridx,replyroom_idx,history_username,URL+history_userimg,history_content,history_time,"");
        Firstmodel.setisLoaded(false);
        replytInfosList.add(Firstmodel);

        myAdapter = new MyAdapter_Reply(replytInfosList, SocialReplyActivity.this);
        myAdapter.setOnClickListener_Reply(this);
        mRecyclerView.setAdapter(myAdapter);

        /**로드*/
        loadReplyHistory(replyroom_idx);

    }

    /**댓글작성*/
    boolean isReReply = false;
    String clickedPosition,clickedIdx;
    @OnClick(R.id.social_reply_uploadtxt)
    public void replyit(){
        String reply_content = social_reply_edit.getText().toString();
        if (!reply_content.equals("")){
            Date TODAY = new Date();
            SimpleDateFormat TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String  chat_time = TIME.format(TODAY);
            /**emit*/
            JSONObject jsonObject = new JSONObject();
            String no = "no";
            String yes = "yes";
            try {
                if (isReReply){
                    jsonObject.put("is_ReReply" , yes);
                    jsonObject.put("reply_contentidx",clickedIdx);
                    jsonObject.put("reply_content_position",clickedPosition);
                }else {
                    jsonObject.put("is_ReReply" , no);
                }
                jsonObject.put("reply_useridx" , MY_IDX);
                jsonObject.put("reply_username" , MY_NAME);
                jsonObject.put("reply_roomidx",replyroom_idx);
                jsonObject.put("reply_time",chat_time);
                jsonObject.put("reply_content",reply_content);

                mSocket.emit("social reply message",jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            social_reply_uploadtxt.setText("게시");
            social_reply_edit.setText("");
            social_reply_edit.setHint("");
            clickedPosition = null;
            clickedIdx = null;
            isReReply = false;
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

                            /**댓글 유저 정보*/
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
                            SocialReplyModel loadedmodel = new SocialReplyModel(r_idx,useridx,r_roomdix,username,URL_withoutslash+userimg,r_content,r_time,"답글 달기");
                            loadedmodel.setisLoaded(true);
                            loadedmodel.setReReply(false);
                            replytInfosList.add(loadedmodel);
                            /**대댓글 정보*/
                            List<SocialReReplyReponse> ReReply_info =one.getReReply_info();
                            for (SocialReReplyReponse rere : ReReply_info){
                                String rere_idx = rere.getIdx();
                                String rere_roomidx = rere.getRere_roomidx();

                                if (!rere_idx.equals("null")){
                                    if (rere_roomidx.equals(replyroom_idx)){
                                        String rere_content = rere.getRere_content();
                                        String rere_time = rere.getRere_time();
                                        /**대댓글 유저 정보*/
                                        Value_3 rere_userinfo = rere.getRere_UserIdx_Info();
                                        // 리스트로 받으면 php 에서 array 로 제이슨 쏴주고
                                        // 그냥 객체로 받으면 php 에서 object 로 제이슨 쏴줘야 한다
                                        String rere_useridx = rere_userinfo.getIdx();
                                        String rere_username = rere_userinfo.getUsername();
                                        String rere_userimg = rere_userinfo.getPhoto();
                                        /**nodejs 로 디비에 저장할때 sql 문 오류때매 가공된 문자열 제대로 바꾸기*/
                                        rere_time =rere_time.replace("\'","");

                                        if ((rere_content.length() - 1)>0){
                                            rere_content = rere_content.substring( 0, rere_content.length() - 1);
                                        }
                                        rere_content =rere_content.replaceFirst("'","");
                                        rere_content =rere_content.replace("\\","");

                                        /**대댓글 정보 모델 입력*/
                                        SocialReplyModel rere_loadedmodel =
                                                new SocialReplyModel(rere_idx,rere_useridx,rere_roomidx,rere_username,
                                                        URL_withoutslash+rere_userimg,rere_content,rere_time,"답글 달기");
                                        rere_loadedmodel.setisLoaded(true);
                                        rere_loadedmodel.setReReply(true);
                                        replytInfosList.add(rere_loadedmodel);
                                    }
                                }


                            }

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
                    String is_ReReply;
                    String reply_re_contentidx = null;
                    String reply_content_position = null;
                    String idx,reply_useridx,reply_username,reply_roomidx,reply_time,reply_content,reply_userimg;
                    try{
                        is_ReReply = dataRecieved.getString("is_ReReply");
                        if (is_ReReply.equals("yes")){
                            /**대댓글일 경우*/
                            reply_re_contentidx = dataRecieved.getString("reply_re_contentidx");
                            reply_content_position = dataRecieved.getString("reply_content_position");
                        }
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

                            SocialReplyModel model = new SocialReplyModel(idx,reply_useridx,reply_roomidx,reply_username,URL+reply_userimg,reply_content,reply_time,"답글 달기");
                            model.setisLoaded(false);// 로드된 댓글이 아니다
                            Log.e("is_ReReply/reply_re_contentidx ",is_ReReply+"/"+reply_re_contentidx);

                            if (is_ReReply.equals("yes")){//대댓글일 경우
                                model.setReReply(true);

                                /**리사이클러뷰 아이템 위로 쌓기*/
                                /**대댓글 위치 바로 밑에 댓글이 달리도록*/
                                int reply_position = Integer.parseInt(reply_content_position);
                                /**test*/
                               // myAdapter.insert(reply_position+1,model);
                                replytInfosList.add(reply_position+1,model);
                                myAdapter.notifyItemInserted(reply_position+1);
                                myAdapter.notifyDataSetChanged();
                                /**리사이클러뷰 아이템 위로 쌓기*/
                                replytInfosList.get(reply_position).setReplyit("답글 달기");
                                mRecyclerView.smoothScrollToPosition(reply_position+1);
                            }else if (is_ReReply.equals("no")){//대댓글이 아닐경우
                                model.setReReply(false);

                                /**리사이클러뷰 아이템 위로 쌓기*/
                                /**postion 0 은 게시물 글이 있으므로 position 1부터 쌓이도록*/
                                /**test*/
                                //myAdapter.insert(1,model);
                                replytInfosList.add(1,model);
                                myAdapter.notifyItemInserted(1);
                                myAdapter.notifyDataSetChanged();
                                /**리사이클러뷰 아이템 위로 쌓기*/
                                mRecyclerView.smoothScrollToPosition(1);
                            }
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

    /**클릭 리스너 인터페이스*/
    @Override
    public void onre_reply_txtClick(int position, View v) {

        SocialReplyModel clickedModel = replytInfosList.get(position);
        TextView tv = (TextView) v;
        Log.e("tv",tv.getText().toString());
        if (tv.getText().toString().equals("답글 달기")){

            showKeyboard(social_reply_edit,SocialReplyActivity.this);

            clickedModel.setReplyit("[답글 취소]");
            tv.setText("[답글 취소]");
            isReReply = true;
            clickedPosition = position+"";
            clickedIdx = clickedModel.getIdx();
            social_reply_uploadtxt.setText("댓글");
            String hint = clickedModel.getUsername()+"님에게 댓글 남기는중";
            social_reply_edit.setHint(hint);
        }else if (tv.getText().toString().equals("[답글 취소]")){
            clickedModel.setReplyit("답글 달기");
            tv.setText("답글 달기");
            social_reply_uploadtxt.setText("게시");
            social_reply_edit.setText("");
            social_reply_edit.setHint("");
            clickedPosition = null;
            clickedIdx = null;
            isReReply = false;
        }
    }
    @Override
    public void ondelete_txtClick(int position, View v) {

        SocialReplyModel clickedModel = replytInfosList.get(position);
        boolean del_isReply = clickedModel.isReReply();
        JSONObject jsonObject = new JSONObject();
        try {
            if (del_isReply){
                String rere_idx = clickedModel.getIdx();
                jsonObject.put("delete_is_ReReply" , "yes");
                jsonObject.put("delete_reply_idx",rere_idx);
            }else {
                String re_idx = clickedModel.getIdx();
                jsonObject.put("delete_is_ReReply" , "no");
                jsonObject.put("delete_reply_idx",re_idx);
            }
            jsonObject.put("delete_reply_useridx" , MY_IDX);
            jsonObject.put("delete_reply_username" , MY_NAME);
            jsonObject.put("delete_reply_roomidx",replyroom_idx);

            mSocket.emit("social reply delete message",jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**리사틀러뷰 삭제*/
        myAdapter.remove(position);
    }


    //툴바
}
