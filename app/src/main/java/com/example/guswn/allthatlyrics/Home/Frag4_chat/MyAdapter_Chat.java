package com.example.guswn.allthatlyrics.Home.Frag4_chat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guswn.allthatlyrics.CircleTransform;
import com.example.guswn.allthatlyrics.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.guswn.allthatlyrics.MainActivity.URL;
import static com.example.guswn.allthatlyrics.MainActivity.isNullOrEmpty;

public class MyAdapter_Chat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.chat_img)
        ImageView chat_img;
        @BindView(R.id.chat_title)
        TextView chat_title;
        @BindView(R.id.chat_innercontent)
        TextView chat_innercontent;
        @BindView(R.id.chat_time)
        TextView chat_time;
        @BindView(R.id.chat_unread_message)
        TextView chat_unread_message;
        @BindView(R.id.chat_unread_message_cv)
        CardView chat_unread_message_cv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        ChatInfo clickedChat = chatInfos.get(pos);
                        Intent intent = new Intent(context,InnerChatActivity.class);
                        intent.putExtra("chatroom_idx",clickedChat.getIdx());
                        intent.putExtra("chatroomname",clickedChat.getTitle());
                        intent.putExtra("chatpeople_num",clickedChat.getPeopleNum());
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    //
    public interface ChatRycyclerClickListner{
        void onItemClick(int position,View v);
    }
    private ChatRycyclerClickListner CHlistner;
    public void setOnClickListener_Chat(ChatRycyclerClickListner f){
        this.CHlistner = f;
    }
    private ArrayList<ChatInfo> chatInfos;
    private Context context;

    MyAdapter_Chat(ArrayList<ChatInfo> chatInfos1, Context context){
        this.chatInfos = chatInfos1;
        this.context = context;
    }
    //

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_recyclerview_item,viewGroup,false);
        return new MyAdapter_Chat.MyViewHolder(v);
    }

    public static String outidx;
    public static String outmessage;
    public static String outtime;
    public static String unReadMessage;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final ChatInfo object = chatInfos.get(i);
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        if(object!=null){

            //Log.e("1_Frag4_onBindViewHolder ",object.getIdx()+"/"+object.getTitle());
            if(object.getImg()==null){
                myViewHolder.chat_img.setImageResource(R.drawable.account);
            }else {
                Picasso.with(context)
                        .load(URL+object.getImg())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.load)
                        .into(myViewHolder.chat_img);
            }
            myViewHolder.chat_title.setText(object.getTitle());

            myViewHolder.chat_innercontent.setText(object.getInnercontent());
            myViewHolder.chat_time.setText(object.getTime());

            /***/
            if (object.getUnreadMessage()!=null){
                myViewHolder.chat_unread_message.setText(object.getUnreadMessage());
                if (object.getUnreadMessage().equals("0")){
                    myViewHolder.chat_unread_message.setVisibility(View.INVISIBLE);
                    myViewHolder.chat_unread_message_cv.setVisibility(View.INVISIBLE);
                }else {
                    myViewHolder.chat_unread_message.setVisibility(View.VISIBLE);
                    myViewHolder.chat_unread_message_cv.setVisibility(View.VISIBLE);
                }

            }else {
                myViewHolder.chat_unread_message.setVisibility(View.INVISIBLE);
                myViewHolder.chat_unread_message_cv.setVisibility(View.INVISIBLE);
            }

            //Log.e("0_object.getIdx/outidx/outtime/outmessage",object.getIsLoaded()+"/"+object.getIdx()+"/"+outidx+"/"+outtime+"/"+outmessage);
           // Log.e("A_object.getIdx/outidx/getTime/getInnercontent",object.getIsLoaded()+"/"+object.getIdx()+"/"+outidx+"/"+object.getTime()+"/"+object.getInnercontent());
            if (outidx!=null){
               // Log.e("1_object.getIdx/outidx/outtime/outmessage",object.getIsLoaded()+"/"+object.getIdx()+"/"+outidx+"/"+outtime+"/"+outmessage);
                //Log.e("B_object.getIdx/outidx/getTime/getInnercontent",object.getIsLoaded()+"/"+object.getIdx()+"/"+outidx+"/"+object.getTime()+"/"+object.getInnercontent());
               // Log.e("2_Frag4_onBindViewHolder object.getIdx()/outidx ",object.getIdx()+"/"+outidx);

                if (object.getIdx().equals(outidx)){
                   // Log.e("2_object.getIdx/outidx/outtime/outmessage",object.getIsLoaded()+"/"+object.getIdx()+"/"+outidx+"/"+outtime+"/"+outmessage);
                   // Log.e("C_object.getIdx/outidx/getTime/getInnercontent",object.getIsLoaded()+"/"+object.getIdx()+"/"+outidx+"/"+object.getTime()+"/"+object.getInnercontent());
                    //Log.e("3_Frag4_onBindViewHolder ",object.getIdx()+"/"+outidx+"/"+outmessage+"/"+unReadMessage);

                    if (outmessage!=null) {
                        // 이걸해줘야 채팅리스트를 안보고 있어도
                        //미리보기 메시지가 갱신된다
                        object.setInnercontent(outmessage);
                        object.setTime(outtime);
                    }
                    if (unReadMessage!=null){
                        object.setUnreadMessage(unReadMessage);
                    }

                    myViewHolder.chat_innercontent.setText(object.getInnercontent());
                    myViewHolder.chat_time.setText(object.getTime());

                    if (object.getUnreadMessage()==null){ // 즉 unReadMessage==null 이라면
                        myViewHolder.chat_unread_message.setVisibility(View.INVISIBLE);
                        myViewHolder.chat_unread_message_cv.setVisibility(View.INVISIBLE);
                    }else {
                        myViewHolder.chat_unread_message.setText(object.getUnreadMessage());
                        if (object.getUnreadMessage().equals("0")){
                            myViewHolder.chat_unread_message.setVisibility(View.INVISIBLE);
                            myViewHolder.chat_unread_message_cv.setVisibility(View.INVISIBLE);
                        }else {

                            myViewHolder.chat_unread_message.setVisibility(View.VISIBLE);
                            myViewHolder.chat_unread_message_cv.setVisibility(View.VISIBLE);
                        }

                    }

                }else if (!object.getIdx().equals(outidx)){
                    if (object.getUnreadMessage()!=null){
                        myViewHolder.chat_unread_message.setText(object.getUnreadMessage());
                        if (object.getUnreadMessage().equals("0")){
                            myViewHolder.chat_unread_message.setVisibility(View.INVISIBLE);
                            myViewHolder.chat_unread_message_cv.setVisibility(View.INVISIBLE);
                        }else {
                            myViewHolder.chat_unread_message.setVisibility(View.VISIBLE);
                            myViewHolder.chat_unread_message_cv.setVisibility(View.VISIBLE);
                        }

                    }else {
                        myViewHolder.chat_unread_message.setVisibility(View.INVISIBLE);
                        myViewHolder.chat_unread_message_cv.setVisibility(View.INVISIBLE);
                    }
                }
            }else if (outidx==null){
               /// Log.e("2_FUCK_Frag4_onBindViewHolder object.getIdx()/outidx ",object.getIdx()+"/"+outidx);
            }

//            if (object.getUnreadMessage()!=null){
//                myViewHolder.chat_unread_message_cv.setVisibility(View.VISIBLE);
//            }else {
//                myViewHolder.chat_unread_message_cv.setVisibility(View.GONE);
//            }

            if(CHlistner!=null){
                final int pos =i;
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CHlistner.onItemClick(pos,v);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatInfos.size();
    }
}
