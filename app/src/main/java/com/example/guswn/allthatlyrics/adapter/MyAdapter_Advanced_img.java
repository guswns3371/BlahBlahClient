package com.example.guswn.allthatlyrics.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.guswn.allthatlyrics.extension.PhotoFilter;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.model.AdvancedImgModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapter_Advanced_img extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.show_img_btn)
        ImageButton show_img_btn;
        @BindView(R.id.show_img_img)
        ImageView show_img_img;
        @BindView(R.id.show_img_video)
        VideoView show_img_video;
        @BindView(R.id.show_img_frame)
        FrameLayout show_img_frame;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
//            show_img_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    if(pos != RecyclerView.NO_POSITION){
//                        AdvancedImgModel clickedImg = EditedPreUploadFiles.get(pos);
//                        Intent intent = new Intent(context,AdvancedEditPhotoActivity.class);
//                        intent.putExtra("clickedImg",clickedImg);
//                        intent.putExtra("position",pos);
//                    }
//                }
//            });
        }
    }

    private ArrayList<AdvancedImgModel> advancedImgModels;
    private Context context;
    PhotoFilter photoFilter;
    public static  boolean isEdit;
    public static  Integer editedType;
    public MyAdapter_Advanced_img(ArrayList<AdvancedImgModel> advancedImgModels, Context context){
        this.advancedImgModels = advancedImgModels;
        this.context = context;
    }

    public interface AdvanceImgClickListner{
        void onEyeImgClicked(int pos);
    }
    private  AdvanceImgClickListner advanceImgClickListner;
    public void setOnClickListner_AdvancedImg(AdvanceImgClickListner a){
        this.advanceImgClickListner = a;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ad_edit_recycler_item_1,viewGroup,false);
        return new MyAdapter_Advanced_img.MyViewHolder(v);
    }

    public static boolean isSingleEditNow;
    public static Integer clickedpos;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final AdvancedImgModel object = advancedImgModels.get(i);
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        if(object!=null){
            //myViewHolder.show_img_img.setImageURI(object.getImg());

//            if (isEdit){
//                object.setType(editedType);
//            }
            String mime = object.getMimetype();
            Log.e("mimeInfos ",mime);
            if (mime.contains("video")){
                myViewHolder.show_img_img.setVisibility(View.GONE);
                myViewHolder.show_img_frame.setVisibility(View.VISIBLE);
                myViewHolder.show_img_video.setVisibility(View.VISIBLE);
                myViewHolder.show_img_btn.setVisibility(View.GONE);

                myViewHolder.show_img_video.setVideoPath(object.getFilepath());
                myViewHolder.show_img_video.start();
                myViewHolder.show_img_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setVolume(0,0);
                        mp.setLooping(true);
                    }
                });
                if (!isSingleEditNow){
                    myViewHolder.itemView.setVisibility(View.VISIBLE);
                    if (isEdit) {
                        object.setType(editedType);
                    }
                }else {
                    if (clickedpos == i){
                        myViewHolder.itemView.setVisibility(View.VISIBLE);
                        myViewHolder.show_img_btn.setVisibility(View.GONE);
                        if (isEdit){
                            object.setType(editedType);
                        }
                    }else {
                        myViewHolder.itemView.setVisibility(View.GONE);
                    }
                }
            }else if (mime.contains("image")){
                myViewHolder.show_img_img.setVisibility(View.VISIBLE);
                myViewHolder.show_img_frame.setVisibility(View.GONE);
                myViewHolder.show_img_video.setVisibility(View.GONE);
                myViewHolder.show_img_btn.setVisibility(View.VISIBLE);

                if (myViewHolder.show_img_video.isPlaying()){
                    myViewHolder.show_img_video.stopPlayback();
                }
                /**test success*/
                if (!isSingleEditNow){
                    myViewHolder.itemView.setVisibility(View.VISIBLE);
                    myViewHolder.show_img_btn.setVisibility(View.VISIBLE);
                    if (isEdit) {
                        object.setType(editedType);
                    }
                }else {
                    if (clickedpos == i){
                        myViewHolder.itemView.setVisibility(View.VISIBLE);
                        myViewHolder.show_img_btn.setVisibility(View.GONE);
                        if (isEdit){
                            object.setType(editedType);
                        }
                    }else {
                        myViewHolder.itemView.setVisibility(View.GONE);

                    }
                }
            }

//
//            if (isEdit){
//                if (!isSingleEditNow){ // 여러개 편집 할 때
//                    if (editedType!=null)
//                    object.setType(editedType);
//                }else {// 한개만 편집할 때
//                    if (clickedpos!=null){
//                        if (clickedpos == i){
//                            object.setType(editedType);
//                        }
//                    }
//                }
//            }

            photoFilter = new PhotoFilter(false,object.getType(),object.getFilepath(),object.getFile(),context,myViewHolder.show_img_img);
            photoFilter.photoFilterByType();
//            Glide.with(context)
//                    .load(object.getImg())
//                    .apply(new RequestOptions().fitCenter())
//                    .into(myViewHolder.show_img_img);
            if (advanceImgClickListner!=null){
                final int pos = i;
                myViewHolder.show_img_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        advanceImgClickListner.onEyeImgClicked(pos);
                    }
                });
            }
        }

        }

    @Override
    public int getItemCount() {
        return advancedImgModels.size();
    }
}
