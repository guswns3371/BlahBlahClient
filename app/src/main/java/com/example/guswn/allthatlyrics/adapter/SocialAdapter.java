package com.example.guswn.allthatlyrics.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.guswn.allthatlyrics.extension.CircleTransform;
import com.example.guswn.allthatlyrics.ui.account.activity.OtherFollowAccountActivity;
import com.example.guswn.allthatlyrics.extension.MyPhotoFilter;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.model.SocialInfoModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;
import static com.example.guswn.allthatlyrics.extension.MyPhotoFilter.getTypeFromString;

public class SocialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SlideImageAdapter.SlideClickListener {

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

        @BindView(R.id.social_content_showmore)
        TextView social_content_showmore;

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
                Intent intent = new Intent(context, OtherFollowAccountActivity.class);
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
    public void remove(int position) {
        socialInfoModels.remove(position);
        notifyItemChanged(position);
        notifyItemRangeRemoved(position, 1);
    }

    public interface SocialReycyclerClickListner{
        void onlike_btnClick(int position,View v,TextView tv);
        void onmore_btnClick(int position,View v,View itemview);
        void onreply_btnnClick(int position,View v);
        void onreply_txtClick(int position,View v);
        void onshare_btnClick(int position,View v);
        void onbookmark_btnClick(int position,View v);
    }

    private SocialReycyclerClickListner SRlistner;
    public void setOnClickListener_Social(SocialReycyclerClickListner f){
        this.SRlistner = f;
    }

    private ArrayList<SocialInfoModel> socialInfoModels;
    private Context context;
    MyPhotoFilter myPhotoFilter;
    public SocialAdapter(ArrayList<SocialInfoModel> socialInfoModels, Context context){
        this.socialInfoModels = socialInfoModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.social_recycler_item,viewGroup,false);
        return new SocialAdapter.MyViewHolder(v);
    }
    /**SlideAdapter clicklistener interface*/
    int flag = 0;
    @Override
    public void onSlideClick(int postion, View v,MediaPlayer mp) {
        Log.e("1_onSlideClick ",postion+"/"+flag);
        if (v instanceof ImageView) {
            ImageView view = (ImageView) v;
            Log.e("2_onSlideClick ","ImageView"+"/"+flag);
        }else if (v instanceof VideoView) {
            VideoView view = (VideoView) v;
            Log.e("2_onSlideClick ","VideoView"+"/"+flag);

            switch (flag){
                case 1:
                    mp.setVolume(0,0);
                    Log.e("3_onSlideClick ","sound off");
                    Toast.makeText(context,"sound off",Toast.LENGTH_SHORT).show();
                    flag=0;
                    break;
                case 0:
                    mp.setVolume(1,1);
                    // 0.0f = no sound , 1.0f =full sound
                    Log.e("3_onSlideClick ","sound on");
                    Toast.makeText(context,"sound on",Toast.LENGTH_SHORT).show();
                    flag++;
                    break;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        flag = 0;
        final SocialInfoModel object = socialInfoModels.get(i);
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        if(object!=null){
            myViewHolder.social_username.setText(object.getSocial_username());

            myViewHolder.social_location.setText(object.getSocial_location());
            if (object.getSocial_location()!=null){
                myViewHolder.social_location.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.social_location.setVisibility(View.GONE);
            }

            myViewHolder.social_content_txt.setText(object.getSocial_content_txt());
            if (object.getSocial_content_txt()!=null){
                myViewHolder.social_content_txt.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.social_content_txt.setVisibility(View.GONE);
            }

           // Log.e("myViewHolder.social_content_txt.getLineHeight() ",""+myViewHolder.social_content_txt.getLineCount()+"/"+ object.getSocial_content_txt().length());
            /**내용 더보기 버튼*/
            if (object.getSocial_content_txt().length()>=181){
                final boolean[] expanded = {false};
                myViewHolder.social_content_showmore.setVisibility(View.VISIBLE);
                myViewHolder.social_content_showmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (expanded[0]) {
                            myViewHolder.social_content_txt.setMaxLines(5);
                            myViewHolder.social_content_showmore.setText("펼치기");
                        } else {
                            myViewHolder.social_content_txt.setMaxLines(100);
                            myViewHolder.social_content_showmore.setText("접기");
                        }
                        expanded[0] = !expanded[0];
                    }
                });
            }else {
                myViewHolder.social_content_showmore.setVisibility(View.GONE);
            }

            /**좋아요 버튼*/
            if (object.getLiked()){
                myViewHolder.social_like_btn.setImageResource(R.drawable.heart_filled_ios);
            }else {
                myViewHolder.social_like_btn.setImageResource(R.drawable.heart_blank_ios);
            }
            /**북마크 버튼*/
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
                    .load(context.getString(R.string.URL)+object.getSocial_userimg())
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
            /**viewpager*/
            final SlideImageAdapter slideImageAdapter = new SlideImageAdapter(context,object.getSocialImageModelList());

            slideImageAdapter.SetSlideClickListener(this);
            myViewHolder.social_content_img_viewpager.setAdapter(slideImageAdapter);
            myViewHolder.social_content_img_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }
                @Override
                public void onPageSelected(final int i) {
                    /**몇번째 사지인지 나타는 txtview*/
                    String count = (i+1)+"/"+object.getSocialImageModelList().size();
                    myViewHolder.social_content_img_cnt.setText(count);
                    flag = 0;
                    /**test*/
//                    View viewTag = myViewHolder.social_content_img_viewpager.findViewWithTag("view" + i);
//                    Log.e("viewTag"+i,"/ "+viewTag);
//                    if (viewTag instanceof VideoView){
//                        final VideoView videoViewTag = (VideoView)viewTag.findViewById(R.id.slidevideo);
//                        String url = URL+object.getSocialImageModelList().get(i).getUrl();
//                        Log.e("url ",url);
//                        Uri uri = Uri.parse(url);
//                        videoViewTag.setVideoURI(uri);
//                        videoViewTag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                            @Override
//                            public void onFocusChange(final View v, boolean hasFocus) {
//                                Log.e("0_hasFocus_???","hasFocus"+i);
//                                if (hasFocus){
//                                    Log.e("1_hasFocus_true","hasFocus"+i);
//                                    videoViewTag.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                                        @Override
//                                        public void onPrepared(final MediaPlayer mp) {
//                                            mp.start();
//                                            mp.setLooping(true);
//                                            mp.setVolume(1,1);
//                                            final int[] flag = {0};
//                                            v.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    switch (flag[0]){
//                                                        case 0:
//                                                            Toast.makeText(context,"0",Toast.LENGTH_SHORT).show();
//                                                            mp.setVolume(0,0);
//                                                            flag[0]++;
//                                                            break;
//                                                        case 1:
//                                                            Toast.makeText(context,"1",Toast.LENGTH_SHORT).show();
//                                                            mp.setVolume(1,1);
//                                                            flag[0]=0;
//                                                            break;
//                                                    }
//                                                }
//                                            });
//                                        }
//                                    });
//                                }else {
//                                    Log.e("1_hasFocus_false","hasFocus"+i);
//                                    videoViewTag.stopPlayback();
//                                }
//                            }
//                        });
//                    }
                    /**test*/
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
            String url = context.getString(R.string.URL_withoutslash)+object.getSocialImageModelList().get(0).getUrl();
            Log.e("onBindViewHolder "+i,context.getResources().getString(type)+"/"+url);
//            photoFilter = new PhotoFilter(true,typeInfos,url,null,context,myViewHolder.social_content_img);
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
                myViewHolder.social_more_reply_txt_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SRlistner.onreply_txtClick(pos,v);
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
