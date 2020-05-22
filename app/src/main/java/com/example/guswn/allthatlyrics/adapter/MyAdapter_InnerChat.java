package com.example.guswn.allthatlyrics.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.guswn.allthatlyrics.extension.CircleTransform;
import com.example.guswn.allthatlyrics.ui.friends.activity.InnerFriendActivity;
import com.example.guswn.allthatlyrics.ui.chat.activity.ShowImageActivity;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.model.InnerChatModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapter_InnerChat extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    public class MyViewHolder_InnerChat extends RecyclerView.ViewHolder{

        @BindView(R.id.not_mine_linear)
        LinearLayout not_mine_linear;
        @BindView(R.id.not_mine_img)
        ImageView not_mine_img;
        @BindView(R.id.not_mine_imgfile)
        ImageView not_mine_imgfile;
        @BindView(R.id.not_mine_name)
        TextView not_mine_name;
        @BindView(R.id.not_mine_content)
        TextView not_mine_content;
        @BindView(R.id.not_mine_isread)
        TextView not_mine_isread;
        @BindView(R.id.not_mine_time)
        TextView not_mine_time;

        @BindView(R.id.mine_linear)
        LinearLayout mine_linear;
        @BindView(R.id.mine_content)
        TextView mine_content;
        @BindView(R.id.mine_isread)
        TextView mine_isread;
        @BindView(R.id.mine_time)
        TextView mine_time;
        @BindView(R.id.mine_imgfile)
        ImageView mine_imgfile;



        public MyViewHolder_InnerChat(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            not_mine_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        InnerChatModel clickedInnerChat = innerChatModels.get(pos);
                        Intent intent = new Intent(context, InnerFriendActivity.class);
                        intent.putExtra("idx",clickedInnerChat.getChat_useridx());
                        intent.putExtra("innerchat",true);
                        context.startActivity(intent);
                    }
                }
            });

            mine_imgfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        InnerChatModel clickedInnerChat = innerChatModels.get(pos);
                        Intent intent = new Intent(context, ShowImageActivity.class);
                        intent.putExtra("isLoaded",clickedInnerChat.getIsLoaded());
                        intent.putExtra("isMine","mine");
                        intent.putExtra("image",clickedInnerChat.getChat_content());
                        context.startActivity(intent);
                    }
                }
            });
            not_mine_imgfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        InnerChatModel clickedInnerChat = innerChatModels.get(pos);
                        Intent intent = new Intent(context, ShowImageActivity.class);
                        intent.putExtra("isLoaded",clickedInnerChat.getIsLoaded());
                        intent.putExtra("isMine","not_mine");
                        intent.putExtra("image",clickedInnerChat.getChat_content());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }


    //
    public interface ChatRecyclerClickListner{
        void onItemClick(int position,View v);
        void onCheckBoxClick(int position,View v);
    }
    private ChatRecyclerClickListner CClistner;
    public void setOnClickListener_InnerChat(ChatRecyclerClickListner f){
        this.CClistner = f;
    }
    private ArrayList<InnerChatModel> innerChatModels;
    private Context context;
    private Boolean isMine;
    public MyAdapter_InnerChat(ArrayList<InnerChatModel> innerChatInfos2, Context context){
        this.innerChatModels = innerChatInfos2;
        this.context = context;
    }
    //

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View  view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_inner_chat_item,viewGroup,false);
        return new MyAdapter_InnerChat.MyViewHolder_InnerChat(view);
    }

    public static String AllPeople; // 전체 사람수
    public static String isRead; // 지금 읽은 사람수
    public static ArrayList<String> isReadList; // 지금 읽은 사람 리스트
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final MyViewHolder_InnerChat myViewHolder = (MyViewHolder_InnerChat) viewHolder;
        final InnerChatModel object = innerChatModels.get(i);
        final int pos = i;
        if(object!=null){

            if(object.getChat_img()==null){
                myViewHolder.not_mine_img.setImageResource(R.drawable.account);
            }else {
                Picasso.with(context)
                        .load(context.getString(R.string.URL)+object.getChat_img())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.load)
                        .into(myViewHolder.not_mine_img);
            }


            myViewHolder.not_mine_content.setText(object.getChat_content());
            myViewHolder.not_mine_name.setText(object.getChat_username());
            myViewHolder.not_mine_content.setText(object.getChat_content());
            myViewHolder.not_mine_time.setText(object.getChat_time());

            myViewHolder.mine_content.setText(object.getChat_content());
            myViewHolder.mine_content.setText(object.getChat_content());
            myViewHolder.mine_time.setText(object.getChat_time());
            //Log.e("object.getMine()",object.getisMine()+"");



//            if(isReadList!=null){
//
//                if (object.getLast().equals("default")){//새롭게 추가된 댓글
//
//                    if (object.getIsReadPeopleList()!=null){ // 사람 리스트가 저장이 된 댓글
//                        Log.e("object.getIsReadPeopleList() "+i,object.getIsReadPeopleList().toString());
//
//                        ArrayList<String> oldReadList = object.getIsReadPeopleList();
//                        boolean isMe =true;
//                        for (String old : oldReadList){
//                            if (old.equals(MY_IDX)){
//                                isMe = false;
//                            }
//                        }
//                        Log.e("isMe "+i,isMe+"");
//                        if (isMe){// 댓글을 읽은 적이 없을경우
//                            object.setIsReadPeopleList(isReadList); // 사람 리스트
//                            object.setChat_readpeople(isReadList.size()+""); // 사람 수
//                        }
//
//                        myViewHolder.not_mine_isread.setText(object.getChat_readpeople());
//                        myViewHolder.mine_isread.setText(object.getChat_readpeople());
//
//                        if(AllPeople.equals(object.getChat_readpeople())){
//                            // 모두 읽었을 때 숫자 사라짐
//                            myViewHolder.not_mine_isread.setVisibility(View.GONE);
//                            myViewHolder.mine_isread.setVisibility(View.GONE);
//                        }else {
//                            myViewHolder.not_mine_isread.setVisibility(View.VISIBLE);
//                            myViewHolder.mine_isread.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }else if (object.getLast().equals("loaded")){// 로드된 댓글
//
//                }
//
//            }

//            Log.e("AllPeople",AllPeople);
            if (isRead!=null && isReadList!=null){

                    if (object.getIsReadPeopleList()==null){// 새롭게 생긴 톡은 아직 읽은 사람 정보가 세팅 안되어있음
                        object.setChat_readpeople(isRead);
                        object.setIsReadPeopleList(isReadList);
                        Log.e("1_object.getIsReadPeopleList() "+object.getIsLoaded()+i, object.getIsReadPeopleList().toString()+"/");
                    }

                    Log.e("2_object.getIsReadPeopleList() "+object.getIsLoaded()+i, object.getIsReadPeopleList().toString()+"/");
                    ArrayList<String> newReadList = isReadList;
                    ArrayList<String> oldReadList = object.getIsReadPeopleList();
                    Log.e("3_oldReadList",oldReadList.toString());
                    Log.e("4_newReadList",newReadList.toString());

                    for (int a=0;a<newReadList.size();a++){
                        boolean isMe = true;
                        for (int b=0;b<oldReadList.size();b++){
                            if (newReadList.get(a).equals(oldReadList.get(b))){
                                isMe = false;
                            }
                        }// old에 new 자식이 들어있나 검사

                        if (isMe){ // 없으면 새로 끼어 넣는다
                            oldReadList.add(newReadList.get(a));
                        }
                    }// old 개종 완료

                    object.setIsReadPeopleList(oldReadList);
                    object.setChat_readpeople(oldReadList.size()+"");
                    Log.e("5_final_oldReadList",oldReadList.toString());
                    Log.e("6_Allpeople_Readpeople "+object.getIsLoaded()+i,AllPeople+"_"+object.getChat_readpeople());

                    int a = Integer.parseInt(AllPeople)-Integer.parseInt(object.getChat_readpeople());
                    String s =a+"";// 보여줄때는 안읽은 사람 수를 보여줘야 하므로
                    myViewHolder.not_mine_isread.setText(s);
                    myViewHolder.mine_isread.setText(s);
                    // 지금 읽은 사람 > 마지막 댓글에 저장된 읽은 사람 수

                    if(s.equals("0")){
                        // 모두 읽었을 때 숫자 사라짐
                        myViewHolder.not_mine_isread.setVisibility(View.GONE);
                        myViewHolder.mine_isread.setVisibility(View.GONE);
                    }else {
                        myViewHolder.not_mine_isread.setVisibility(View.VISIBLE);
                        myViewHolder.mine_isread.setVisibility(View.VISIBLE);
                    }


//                if (object.getIsLoaded().equals("loaded")){
//                    Log.e("loaded:Allpeople_Readpeople "+i,AllPeople+"_"+object.getChat_readpeople());
//                    int a = Integer.parseInt(AllPeople)-Integer.parseInt(object.getChat_readpeople());
//                    String s =a+"";// 보여줄때는 안읽은 사람 수를 보여줘야 하므로
//                    myViewHolder.not_mine_isread.setText(s);
//                    myViewHolder.mine_isread.setText(s);
//                    // 지금 읽은 사람 > 마지막 댓글에 저장된 읽은 사람 수
//
//                    if(s.equals("0")){
//                        // 모두 읽었을 때 숫자 사라짐
//                        myViewHolder.not_mine_isread.setVisibility(View.GONE);
//                        myViewHolder.mine_isread.setVisibility(View.GONE);
//                    }else {
//                        myViewHolder.not_mine_isread.setVisibility(View.VISIBLE);
//                        myViewHolder.mine_isread.setVisibility(View.VISIBLE);
//                    }
//                }



            }

//            if(isRead!=null){
//
//                if (object.getChat_readpeople().equals("")){// 새롭게 생긴 톡은 아직 읽은 사람 정보가 세팅 안되어있음
//                    object.setChat_readpeople(isRead);
//                    object.setIsReadPeopleList(isReadList);
//                }
//
//
//                if (Integer.parseInt(isRead)>Integer.parseInt(object.getChat_readpeople())){
//                    /** 지금 읽은 사람수 > 읽었던 사람 수*/
//
//                    object.setChat_readpeople(isRead);// 새로운 읽은 사람 정보 갱신
//                    object.setIsReadPeopleList(isReadList);
//
//                    int a = Integer.parseInt(AllPeople)-Integer.parseInt(isRead);
//                    String s =a+"";// 보여줄때는 안읽은 사람 수를 보여줘야 하므로
//                    myViewHolder.not_mine_isread.setText(s);
//                    myViewHolder.mine_isread.setText(s);
//                    // 지금 읽은 사람 > 마지막 댓글에 저장된 읽은 사람 수
//
//                    if(isRead.equals("0")){
//                        // 모두 읽었을 때 숫자 사라짐
//                        myViewHolder.not_mine_isread.setVisibility(View.GONE);
//                        myViewHolder.mine_isread.setVisibility(View.GONE);
//                    }else {
//                        myViewHolder.not_mine_isread.setVisibility(View.VISIBLE);
//                        myViewHolder.mine_isread.setVisibility(View.VISIBLE);
//                    }
//
//                }else if (Integer.parseInt(isRead)==Integer.parseInt(object.getChat_readpeople())){
//                    /** 지금 읽은 사람수 = 읽었던 사람 수*/
//                    ArrayList<String> newReadList = isReadList;
//                    ArrayList<String> oldReadList = object.getIsReadPeopleList();
//
//
//
//                }else if (Integer.parseInt(isRead)<Integer.parseInt(object.getChat_readpeople())){
//                    /** 지금 읽은 사람수 < 읽었던 사람 수*/
//
//                    int a = Integer.parseInt(AllPeople)-Integer.parseInt(object.getChat_readpeople());
//                    String s =a+"";
//                    myViewHolder.not_mine_isread.setText(s);// 전체사람수 - 애초에 저장된 읽은 사람 수를 보여준다
//                    myViewHolder.mine_isread.setText(s);
//
//                    if (s.equals("0")){
//                        // 모두 읽었을 때 숫자 사라짐
//                        myViewHolder.not_mine_isread.setVisibility(View.GONE);
//                        myViewHolder.mine_isread.setVisibility(View.GONE);
//                    }else {
//                        myViewHolder.not_mine_isread.setVisibility(View.VISIBLE);
//                        myViewHolder.mine_isread.setVisibility(View.VISIBLE);
//                    }
//                }
//
////                String s="";
////                if (object.getLast().equals("last")){// 마지막 로드된 댓글일때
////                    if(Integer.parseInt(isRead)<Integer.parseInt(object.getChat_readpeople())){
////                        myViewHolder.not_mine_isread.setText(isRead);
////                        myViewHolder.mine_isread.setText(isRead);
////                        // 지금 읽은 사람 > 마지막 댓글에 저장된 읽은 사람 수
////
////                        s=isRead;
////                        if(isRead.equals("0")){
////                            // 모두 읽었을 때 숫자 사라짐
////                            myViewHolder.not_mine_isread.setVisibility(View.GONE);
////                            myViewHolder.mine_isread.setVisibility(View.GONE);
////                        }else {
////                            myViewHolder.not_mine_isread.setVisibility(View.VISIBLE);
////                            myViewHolder.mine_isread.setVisibility(View.VISIBLE);
////                        }
////
////                    }else {
////                        // 지금 읽은 사람 =< 마지막 댓글에 저장된 읽은 사람 수
////                        myViewHolder.not_mine_isread.setText(object.getChat_readpeople());
////                        myViewHolder.mine_isread.setText(object.getChat_readpeople());
////
////                        s=object.getChat_readpeople();
////                        if (object.getChat_readpeople().equals("0")){
////                            // 모두 읽었을 때 숫자 사라짐
////                            myViewHolder.not_mine_isread.setVisibility(View.GONE);
////                            myViewHolder.mine_isread.setVisibility(View.GONE);
////                        }else {
////                            myViewHolder.not_mine_isread.setVisibility(View.VISIBLE);
////                            myViewHolder.mine_isread.setVisibility(View.VISIBLE);
////                        }
////                    }
////                }else if(object.getLast().equals("not_last")){//마지막 로드된 댓글 전 댓글일때
////                    myViewHolder.not_mine_isread.setText(s);
////                    myViewHolder.mine_isread.setText(s);
////
////                    if (s.equals("0")){
////                        // 모두 읽었을 때 숫자 사라짐
////                        myViewHolder.not_mine_isread.setVisibility(View.GONE);
////                        myViewHolder.mine_isread.setVisibility(View.GONE);
////                    }else {
////                        myViewHolder.not_mine_isread.setVisibility(View.VISIBLE);
////                        myViewHolder.mine_isread.setVisibility(View.VISIBLE);
////                    }
////                }else if (object.getLast().equals("default")){//새롭게 추가된 댓글일 때
////                    myViewHolder.not_mine_isread.setText(isRead);
////                    myViewHolder.mine_isread.setText(isRead);
////                    // 지금 읽은 사람 > 마지막 댓글에 저장된 읽은 사람 수
////
////                    if(isRead.equals("0")){
////                        // 모두 읽었을 때 숫자 사라짐
////                        myViewHolder.not_mine_isread.setVisibility(View.GONE);
////                        myViewHolder.mine_isread.setVisibility(View.GONE);
////                    }else {
////                        myViewHolder.not_mine_isread.setVisibility(View.VISIBLE);
////                        myViewHolder.mine_isread.setVisibility(View.VISIBLE);
////                    }
////                }
//            }


            if (object.getIsFile().equals("yes")){
                myViewHolder.not_mine_content.setVisibility(View.GONE);
                myViewHolder.mine_content.setVisibility(View.GONE);
                myViewHolder.not_mine_imgfile.setVisibility(View.VISIBLE);
                myViewHolder.mine_imgfile.setVisibility(View.VISIBLE);


                if (object.getIsLoaded().equals("default")){//content에 uri가 있다
                    if (object.getisMine()){//내가 보내면 content 에  uri
                        myViewHolder.not_mine_imgfile.setImageURI(Uri.parse(object.getChat_content()));
                        myViewHolder.mine_imgfile.setImageURI(Uri.parse(object.getChat_content()));
                    }else {// 남이 받은것에는 content 에 이미지경로
                        Picasso.with(context)
                                .load(context.getString(R.string.URL_withoutslash)+object.getChat_content())
                                .placeholder(R.drawable.load)
                                .into(myViewHolder.not_mine_imgfile);
                        Picasso.with(context)
                                .load(context.getString(R.string.URL_withoutslash)+object.getChat_content())
                                .placeholder(R.drawable.load)
                                .into(myViewHolder.mine_imgfile);
                    }

                }else if (object.getIsLoaded().equals("loaded")){//content에 이미지 경로가 있다
                    Glide.with(context)
                            .load(context.getString(R.string.URL)+object.getChat_content())
                            .apply(new RequestOptions().placeholder(R.drawable.load))
                            .into(myViewHolder.not_mine_imgfile);
                    Glide.with(context)
                            .load(context.getString(R.string.URL)+object.getChat_content())
                            .apply(new RequestOptions().placeholder(R.drawable.load))
                            .into(myViewHolder.mine_imgfile);
                }

            }else if (object.getIsFile().equals("no")){
                myViewHolder.not_mine_imgfile.setVisibility(View.GONE);
                myViewHolder.mine_imgfile.setVisibility(View.GONE);
                myViewHolder.not_mine_content.setVisibility(View.VISIBLE);
                myViewHolder.mine_content.setVisibility(View.VISIBLE);
            }

            if (object.getisMine()){
                myViewHolder.not_mine_linear.setVisibility(View.GONE);
                myViewHolder.mine_linear.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.not_mine_linear.setVisibility(View.VISIBLE);
                myViewHolder.mine_linear.setVisibility(View.GONE);
            }


        }
    }

    @Override
    public int getItemCount() {
        return innerChatModels.size();
    }
}
