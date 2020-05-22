package com.example.guswn.allthatlyrics.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.example.guswn.allthatlyrics.extension.MyPhotoFilter;
import com.example.guswn.allthatlyrics.R;
import com.example.guswn.allthatlyrics.model.SocialImageModel;

import java.util.ArrayList;

import static com.example.guswn.allthatlyrics.extension.MyPhotoFilter.getTypeFromString;

public class SlideImageAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<String>img_list = new ArrayList<>();
    ArrayList<Integer> typeInfos = new ArrayList<>();
    ArrayList<String> mimeInfos = new ArrayList<>();
    ArrayList<SocialImageModel> infoModels;

    public interface SlideClickListener{
        void onSlideClick(int postion, View v,MediaPlayer mp);
    }
    private SlideClickListener slidelistener;
    public void SetSlideClickListener (SlideClickListener s){
        this.slidelistener = s;
    }

    public SlideImageAdapter(Context context, ArrayList<SocialImageModel> infoModels){
        this.context = context;
        this.infoModels = infoModels;

        for (int a=0 ; a<infoModels.size(); a++){
            img_list.add( infoModels.get(a).getUrl()) ;
            typeInfos.add(getTypeFromString(infoModels.get(a).getFilter()));
            mimeInfos.add(infoModels.get(a).getMimetype());
        }
    }
    @Override
    public int getCount() {
        return img_list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (LinearLayout)o);
    }

    MyPhotoFilter myPhotoFilter;
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.slide,container,false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slidelinearlayout);

        ImageView imgslide = (ImageView) view.findViewById(R.id.slideimg);
        FrameLayout frameslide = (FrameLayout) view.findViewById(R.id.slideFrame);
        final  VideoView videoslide = (VideoView) view.findViewById(R.id.slidevideo);

        final String url = context.getString(R.string.URL)+img_list.get(position);
        String mime = mimeInfos.get(position);
        Log.e("mime/url"+position,mime+"/"+url);
        if (mime.contains("image")){
            imgslide.setVisibility(View.VISIBLE);
            frameslide.setVisibility(View.GONE);
            videoslide.setVisibility(View.GONE);
            if (videoslide.isPlaying()){
                videoslide.stopPlayback();
            }
            myPhotoFilter = new MyPhotoFilter(true, typeInfos.get(position),url,null,context,imgslide);
            myPhotoFilter.photoFilterByType();
        }else if (mime.contains("video")){
            imgslide.setVisibility(View.GONE);
            frameslide.setVisibility(View.VISIBLE);
            videoslide.setVisibility(View.VISIBLE);
            final Uri videourlToURI = Uri.parse(url);
           // videoslide.requestFocus();
            /**비디오 한개만 있는 게시물일때*/
            if (img_list.size()==1){
                videoslide.setVideoURI(videourlToURI);
                videoslide.requestFocus();
                videoslide.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final MediaPlayer mp) {
                        mp.start();
                        mp.setLooping(true);
                        mp.setVolume(0,0);
                        /** click interface*/
                        if (slidelistener!=null){
                            final  int pos = position;
                            videoslide.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    slidelistener.onSlideClick(pos,v,mp);
                                }
                            });
                        }
                    }
                });
            }else {
                /**비디오가 여러개있을 때*/
                videoslide.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        final VideoView view = (VideoView) v;
                        if (hasFocus){
                            Log.e("33_hasFocus_true"+position,"___"+url);
                            view.setVideoURI(videourlToURI);
                            view.requestFocus();
                            view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(final MediaPlayer mp) {
                                    Log.e("44_hasFocus_true"+position,"___"+url);
                                    mp.start();
                                    mp.setLooping(true);
                                    mp.setVolume(0,0);
                                    /** click interface*/
                                    if (slidelistener!=null){
                                        final  int pos = position;
                                        view.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                slidelistener.onSlideClick(pos,v,mp);
                                            }
                                        });
                                    }
                                }
                            });
                        }else {
                            Log.e("3_hasFocus_false"+position,"___"+url);
                            view.stopPlayback();
                        }
                    }
                });
            }

            /**비디오와 이미지가 섞인 게시물*/
            videoslide.setTag("view"+position);
        }
        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((LinearLayout)object);
    }
}
