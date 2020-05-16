package com.example.guswn.allthatlyrics.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guswn.allthatlyrics.extension.CircleTransform;
import com.example.guswn.allthatlyrics.home.Frag3_account.OtherFollowAccount;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.model.FollowTabInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.guswn.allthatlyrics.main.Logo.MY_IDX;

public class MyAdapter_FollowTab extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.follow_userimg)
        ImageView follow_userimg;
        @BindView(R.id.follow_username)
        TextView follow_username;
        @BindView(R.id.follow_useremail)
        TextView follow_useremail;
        @BindView(R.id.follow_btn)
        TextView follow_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        FollowTabInfo clickedFollow = followTabInfos.get(pos);
                        Intent intent = new Intent(context, OtherFollowAccount.class);
                        intent.putExtra("useridx",clickedFollow.getIdx());
                        intent.putExtra("username",clickedFollow.getFollow_username());
                        intent.putExtra("userimg",clickedFollow.getFollow_img());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public interface FollowTabRycyclerClickListner{
        void onFollowBtnClick(int position,View v);
    }
    private FollowTabRycyclerClickListner FTlistner;
    public void setOnClickListener_Friend(FollowTabRycyclerClickListner f){
        this.FTlistner = f;
    }

    private ArrayList<FollowTabInfo> followTabInfos;
    private Context context;


    public MyAdapter_FollowTab(ArrayList<FollowTabInfo> followTabInfos, Context context){
        this.context = context;
        this.followTabInfos = followTabInfos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_follow_tab_item,viewGroup,false);
        return new MyAdapter_FollowTab.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final FollowTabInfo object = followTabInfos.get(i);
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        final int[] flag = {0};
//        final int position = i;
        if(object!=null){

            myViewHolder.follow_username.setText(object.getFollow_username());
            myViewHolder.follow_useremail.setText(object.getFollow_email());

            Picasso.with(context)
                    .load(context.getString(R.string.URL)+object.getFollow_img())
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.load)
                    .into(myViewHolder.follow_userimg);

            if (object.getAmIFollowHim().equals("no")) {
                myViewHolder.follow_btn.setTextColor(Color.WHITE);
                myViewHolder.follow_btn.setBackgroundResource(R.drawable.mybtn_2);
                myViewHolder.follow_btn.setText("팔로우");
                flag[0] = 1;
            }
            if (object.getIdx().equals(MY_IDX)){
                myViewHolder.follow_btn.setVisibility(View.INVISIBLE);
            }else {
                myViewHolder.follow_btn.setVisibility(View.VISIBLE);
            }

//            myViewHolder.follow_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("onBindViewHolder","onBindViewHolder");
//                    switch (flag[0]){
//                        case 1:
//                            Toast.makeText(context,object.getFollow_username()+"를 팔로우 하셨습니다",Toast.LENGTH_SHORT).show();
//                            myViewHolder.follow_btn.setText("팔로잉");
//                            myViewHolder.follow_btn.setTextColor(Color.BLACK);
//                            myViewHolder.follow_btn.setBackgroundResource(R.drawable.mybtn_1);
//                            object.setAmIFollowHim("yes");
//                            flag[0]=0;
//                            break;
//                        case 0:
//                            Toast.makeText(context,object.getFollow_username()+"를 언팔로우 하셨습니다",Toast.LENGTH_SHORT).show();
//                            myViewHolder.follow_btn.setText("팔로우");
//                            myViewHolder.follow_btn.setTextColor(Color.WHITE);
//                            myViewHolder.follow_btn.setBackgroundResource(R.drawable.mybtn_2);
//                            object.setAmIFollowHim("no");
//                            flag[0]++;
//                            break;
//                    }
//                }
//            });

            if(FTlistner!=null){
                final int pos =i;
                myViewHolder.follow_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FTlistner.onFollowBtnClick(pos,v);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return followTabInfos.size();
    }
}
