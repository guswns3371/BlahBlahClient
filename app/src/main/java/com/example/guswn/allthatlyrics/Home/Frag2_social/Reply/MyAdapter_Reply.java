package com.example.guswn.allthatlyrics.Home.Frag2_social.Reply;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.guswn.allthatlyrics.Home.Frag2_social.InnerSocialActivity;
import com.example.guswn.allthatlyrics.Home.Frag2_social.MyAdapter_Social;
import com.example.guswn.allthatlyrics.Home.Frag2_social.SocialInfoModel;
import com.example.guswn.allthatlyrics.Home.Frag3_account.OtherFollowAccount;
import com.example.guswn.allthatlyrics.MyGlide;
import com.example.guswn.allthatlyrics.PhotoFilter;
import com.example.guswn.allthatlyrics.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.guswn.allthatlyrics.Main.Logo.MY_IDX;
import static com.example.guswn.allthatlyrics.MainActivity.URL;

public class MyAdapter_Reply extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.s_reply_img)
        ImageView s_reply_img;

        @BindView(R.id.s_reply_username)
        TextView s_reply_username;
        @BindView(R.id.s_reply_content)
        TextView s_reply_content;
        @BindView(R.id.s_reply_time)
        TextView s_reply_time;
        @BindView(R.id.s_reply_replybtn_txt)
        TextView s_reply_replybtn_txt;
        @BindView(R.id.s_reply_deletebtn_txt)
        TextView s_reply_deletebtn_txt;

        @BindView(R.id.space)
        Space space;

        @BindView(R.id.reply_line)
        View reply_line;

        @OnClick({R.id.s_reply_img,R.id.s_reply_username})
        public void userpage(){
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION){
                SocialReplyModel model = socialReplyModels.get(pos);
                Intent intent = new Intent(context, OtherFollowAccount.class);
                intent.putExtra("useridx",model.getUseridx());
                intent.putExtra("username",model.getUsername());
                String userimg = model.getUserimg();
                Log.e("userimg",userimg);
                boolean isContain = userimg.contains(URL);
                if (isContain){
                    userimg = userimg.replace(URL,"");
                }
                intent.putExtra("userimg",userimg);
                context.startActivity(intent);
            }

        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    void insert(int position,SocialReplyModel model){
        socialReplyModels.add(position,model);
//        socialReplyModels.get(position-1).setReplyit("답글 달기");
        notifyItemChanged(position);
        notifyItemInserted(position);
    }

    void remove(int position) {
        socialReplyModels.remove(position);
        notifyItemChanged(position);
        notifyItemRangeRemoved(position, 1);
        notifyDataSetChanged();
    }
    /**interface*/
    public interface SocialReplyClickListener{
        void ondelete_txtClick(int position,View v);
        void onre_reply_txtClick(int position,View v);
    }

    private SocialReplyClickListener SRlistner;
    public void setOnClickListener_Reply(SocialReplyClickListener f){
        this.SRlistner = f;
    }

    private ArrayList<SocialReplyModel> socialReplyModels;
    private Context context;

    MyAdapter_Reply(ArrayList<SocialReplyModel> socialReplyModels, Context context){
        this.socialReplyModels = socialReplyModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.social_reply_item,viewGroup,false);
        return new MyAdapter_Reply.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final SocialReplyModel object = socialReplyModels.get(i);
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        if(object!=null){
            myViewHolder.s_reply_content.setText(object.getReplycontent());

            MyGlide myGlide = new MyGlide(context,myViewHolder.s_reply_img);
            myGlide.glideURL(object.getUserimg());

            myViewHolder.s_reply_username.setText(object.getUsername());
            myViewHolder.s_reply_time.setText(object.getReplytime());
            myViewHolder.s_reply_replybtn_txt.setText(object.getReplyit());
            if (object.getIdx().equals("null")){
                myViewHolder.s_reply_replybtn_txt.setVisibility(View.GONE);
                myViewHolder.s_reply_deletebtn_txt.setVisibility(View.GONE);
                myViewHolder.reply_line.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.reply_line.setVisibility(View.GONE);
                myViewHolder.s_reply_replybtn_txt.setVisibility(View.VISIBLE);
                if (object.getUseridx().equals(MY_IDX)){
                    myViewHolder.s_reply_deletebtn_txt.setVisibility(View.VISIBLE);
                }else {
                    myViewHolder.s_reply_deletebtn_txt.setVisibility(View.GONE);
                }
            }
            /**대댓글일 경우*/
            if (object.isReReply){
                myViewHolder.space.setVisibility(View.VISIBLE);
                myViewHolder.s_reply_replybtn_txt.setVisibility(View.GONE);
            }else {
                myViewHolder.space.setVisibility(View.GONE);
                myViewHolder.s_reply_replybtn_txt.setVisibility(View.VISIBLE);
            }
            if (SRlistner!=null){
                final int pos = i;
                myViewHolder.s_reply_replybtn_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SRlistner.onre_reply_txtClick(pos,v);
                    }
                });
                myViewHolder.s_reply_deletebtn_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SRlistner.ondelete_txtClick(pos,v);
                    }
                });
            }
        }
    }



    @Override
    public int getItemCount() {
        return socialReplyModels.size();
    }
}
