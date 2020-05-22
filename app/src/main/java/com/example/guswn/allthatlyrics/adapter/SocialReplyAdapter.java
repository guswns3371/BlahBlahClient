package com.example.guswn.allthatlyrics.adapter;

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

import com.example.guswn.allthatlyrics.ui.account.activity.OtherFollowAccountActivity;
import com.example.guswn.allthatlyrics.extension.MyGlide;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.model.SocialReplyModel;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.guswn.allthatlyrics.ui.auth.LogoActivity.MY_IDX;

public class SocialReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
                Intent intent = new Intent(context, OtherFollowAccountActivity.class);
                intent.putExtra("useridx",model.getUseridx());
                intent.putExtra("username",model.getUsername());
                String userimg = model.getUserimg();
                Log.e("1_userimg",userimg);
                boolean isContain = userimg.contains(context.getString(R.string.URL));
                if (isContain){
                    userimg = userimg.replace(context.getString(R.string.URL),"");
                }
                Log.e("2_userimg",userimg);
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
        notifyItemChanged(position);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        String ori_idx = socialReplyModels.get(position).getIdx();
        boolean isReReply = socialReplyModels.get(position).isReReply();
        Log.e("_isReReply",isReReply+"");
        if (isReReply){ // 대댓글일때
            socialReplyModels.remove(position);
            notifyItemChanged(position);
            notifyItemRangeRemoved(position, 1);
        }else { // 댓글일때
            /**먼저 댓글 에 달린 대댓글들 position 값*/
            ArrayList<Integer> deletethese = new ArrayList<>();
            deletethese.add(position);
            for (int i=0; i<socialReplyModels.size(); i++){
                if (socialReplyModels.get(i).isReReply()){
                    String rere_idx =socialReplyModels.get(i).getWhereReplyto();
                    String rere_con = socialReplyModels.get(i).getReplycontent();
                   // Log.e("1_remove_it__rere_idx/ori_idx ",rere_con+"_"+rere_idx+"/"+ori_idx);
                    if (rere_idx.equals(ori_idx)){
                        Log.e("2_remove_it__rere_idx/ori_idx ",rere_con+"_"+rere_idx+"/"+ori_idx);
                        deletethese.add(i);
                    }
                }
            }

            /**이제 대댓글 삭제*/
            Collections.sort(deletethese, Collections.reverseOrder());// position 역순으로 정렬
                for (int a=0; a<deletethese.size(); a++){
                    int deletePos = deletethese.get(a);
                    Log.e("deletethese.get(a) position",deletePos+"/"+position);
                    socialReplyModels.remove(deletePos);
                    notifyItemChanged(deletePos);
                   notifyItemRangeRemoved(deletePos, 1);
                }
        }
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

    public SocialReplyAdapter(ArrayList<SocialReplyModel> socialReplyModels, Context context){
        this.socialReplyModels = socialReplyModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.social_reply_item,viewGroup,false);
        return new SocialReplyAdapter.MyViewHolder(v);
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
            /**클릭 인터페이스*/
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
