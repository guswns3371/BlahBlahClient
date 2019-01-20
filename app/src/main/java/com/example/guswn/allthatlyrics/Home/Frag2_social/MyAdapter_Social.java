package com.example.guswn.allthatlyrics.Home.Frag2_social;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guswn.allthatlyrics.CircleTransform;
import com.example.guswn.allthatlyrics.Home.Frag3_account.OtherFollowAccount;
import com.example.guswn.allthatlyrics.PhotoFilter;
import com.example.guswn.allthatlyrics.R;
import com.nostra13.universalimageloader.utils.L;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;
import static com.example.guswn.allthatlyrics.MainActivity.URL;
import static com.example.guswn.allthatlyrics.MainActivity.URL_withoutslash;
import static com.example.guswn.allthatlyrics.PhotoFilter.getTypeFromString;

public class MyAdapter_Social extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.social_user_img)
        ImageView social_user_img;
        @BindView(R.id.social_content_img_viewpager)
        ViewPager social_content_img_viewpager;
        @BindView(R.id.social_content_img_cnt)
        TextView social_content_img_cnt;

        @BindView(R.id.social_username)
        TextView social_username;
        @BindView(R.id.social_location)
        TextView social_location;
        @BindView(R.id.social_like_ctn)
        TextView social_like_ctn;
        @BindView(R.id.social_content_txt)
        TextView social_content_txt;
        @BindView(R.id.social_more_reply_txt_btn)
        TextView social_more_reply_txt_btn;
        @BindView(R.id.social_time)
        TextView social_time;

        @BindView(R.id.social_like_btn)
        ImageButton social_like_btn;
        @BindView(R.id.social_reply_btn)
        ImageButton social_reply_btn;
        @BindView(R.id.social_share_btn)
        ImageButton social_share_btn;
        @BindView(R.id.social_bookmark_btn)
        ImageButton social_bookmark_btn;
        @BindView(R.id.social_more_btn)
        ImageButton social_more_btn;

        @OnClick({R.id.social_user_img,R.id.social_username})
        public void userpage(){
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION){
                SocialInfoModel model = socialInfoModels.get(pos);
                Intent intent = new Intent(context, OtherFollowAccount.class);
                intent.putExtra("useridx",model.getSocial_useridx());
                intent.putExtra("username",model.getSocial_username());
                intent.putExtra("userimg",model.getSocial_userimg());
                context.startActivity(intent);
            }
        }
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public interface SocialReycyclerClickListner{
        void onlike_btnClick(int position,View v,TextView tv);
        void onmore_btnClick(int position,View v,View itemview);
        void onreply_btnnClick(int position,View v);
        void onshare_btnClick(int position,View v);
        void onbookmark_btnClick(int position,View v);
    }

    private SocialReycyclerClickListner SRlistner;
    public void setOnClickListener_Social(SocialReycyclerClickListner f){
        this.SRlistner = f;
    }

    private ArrayList<SocialInfoModel> socialInfoModels;
    private Context context;
    PhotoFilter photoFilter;
    MyAdapter_Social(ArrayList<SocialInfoModel> socialInfoModels, Context context){
        this.socialInfoModels = socialInfoModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.social_recycler_item,viewGroup,false);
        return new MyAdapter_Social.MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final SocialInfoModel object = socialInfoModels.get(i);
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        if(object!=null){
            myViewHolder.social_username.setText(object.getSocial_username());

            if (object.getSocial_location()!=null){
                myViewHolder.social_location.setVisibility(View.VISIBLE);
                myViewHolder.social_location.setText(object.getSocial_location());
            }else {
                myViewHolder.social_location.setVisibility(View.GONE);
            }

            if (object.getSocial_content_txt()!=null){
                myViewHolder.social_content_txt.setVisibility(View.VISIBLE);
                myViewHolder.social_content_txt.setText(object.getSocial_content_txt());
            }else {
                myViewHolder.social_content_txt.setVisibility(View.GONE);
            }

            if (object.getLiked()){
                myViewHolder.social_like_btn.setImageResource(R.drawable.heart_filled_ios);
            }else {
                myViewHolder.social_like_btn.setImageResource(R.drawable.heart_blank_ios);
            }

            if (object.getBookMarked()){
                myViewHolder.social_bookmark_btn.setImageResource(R.drawable.bookmark_filled_ios);
            }else {
                myViewHolder.social_bookmark_btn.setImageResource(R.drawable.bookmark_blank_ios);
            }

            String likedcnt = "좋아요 "+object.getSocial_like_cnt()+"개";
            myViewHolder.social_like_ctn.setText(likedcnt);
            myViewHolder.social_time.setText(object.getSocial_time());
            myViewHolder.social_more_reply_txt_btn.setText("댓글 더보기");

            Picasso.with(context)
                    .load(URL+object.getSocial_userimg())
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.load)
                    .into(myViewHolder.social_user_img);
//            Picasso.with(context)
//                    .load(URL+object.getSocialImageModelList().get(0).getUrl())
//                    .placeholder(R.drawable.load)
//                    .into(myViewHolder.social_content_img);

            /**test success*/
            if (object.getSocialImageModelList().size()>1){
                String count = 1+"/"+object.getSocialImageModelList().size();
                myViewHolder.social_content_img_cnt.setText(count);
                myViewHolder.social_content_img_cnt.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.social_content_img_cnt.setVisibility(View.INVISIBLE);
            }
            SlideAdapter slideAdapter = new SlideAdapter(context,object.getSocialImageModelList());
            myViewHolder.social_content_img_viewpager.setAdapter(slideAdapter);
            myViewHolder.social_content_img_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                }
                @Override
                public void onPageSelected(int i) {
                    String count = (i+1)+"/"+object.getSocialImageModelList().size();
                    myViewHolder.social_content_img_cnt.setText(count);
                }
                @Override
                public void onPageScrollStateChanged(int i) {
                    switch (i){
                        case SCROLL_STATE_DRAGGING:
                            break;
                        case SCROLL_STATE_IDLE:
                            break;
                        case SCROLL_STATE_SETTLING:
                            break;
                    }
                }
            });

            /**연습 end*/
            Integer type =  getTypeFromString(object.getSocialImageModelList().get(0).getFilter());
            String url = URL_withoutslash+object.getSocialImageModelList().get(0).getUrl();
            Log.e("onBindViewHolder "+i,context.getResources().getString(type)+"/"+url);
//            photoFilter = new PhotoFilter(true,type,url,null,context,myViewHolder.social_content_img);
//            photoFilter.photoFilterByType();

            /**클릭 리스너 인터페이스*/
            if(SRlistner!=null){
                final int pos =i;
                myViewHolder.social_like_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SRlistner.onlike_btnClick(pos,v,myViewHolder.social_like_ctn);
                    }
                });
                myViewHolder.social_reply_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SRlistner.onreply_btnnClick(pos,v);
                    }
                });
                myViewHolder.social_share_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SRlistner.onshare_btnClick(pos,v);
                    }
                });
                myViewHolder.social_bookmark_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SRlistner.onbookmark_btnClick(pos,v);
                    }
                });
                myViewHolder.social_more_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SRlistner.onmore_btnClick(pos,v,myViewHolder.itemView);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return socialInfoModels.size();
    }
}
