package com.example.guswn.allthatlyrics.Home.Frag2_social;

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
import android.widget.TabWidget;
import android.widget.VideoView;

import com.example.guswn.allthatlyrics.PhotoFilter;
import com.example.guswn.allthatlyrics.R;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;

import static com.example.guswn.allthatlyrics.MainActivity.URL;
import static com.example.guswn.allthatlyrics.MainActivity.URL_withoutslash;
import static com.example.guswn.allthatlyrics.PhotoFilter.getTypeFromString;

public class SlideAdapter extends PagerAdapter {
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

    public SlideAdapter(Context context, ArrayList<SocialImageModel> infoModels){
        this.context = context;
        this.infoModels = infoModels;

        for (int a=0 ; a<infoModels.size(); a++){
            img_list.add( infoModels.get(a).getUrl()) ;
            typeInfos.add(getTypeFromString(infoModels.get(a).getFilter()));
            mimeInfos.add(infoModels.get(a).getMimetype());
        }
    }
    View mCurrentView;
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, final int position, @NonNull Object object) {
        mCurrentView = (View) object;
        final String url = URL+img_list.get(position);
        final Uri videourlToURI = Uri.parse(url);
//        mCurrentView = container.getChildAt(position);
        Log.e("1_mCurrentView ",position
                        +"/"+mCurrentView.findFocus()
        );
        if (mCurrentView.findFocus()!=null){// Imageview 일/때
            String what = mCurrentView.findFocus().toString();
                if (what.contains("VideoView")){
                    Log.e("2_mCurrentView ",position+"VideoView____"+url);
                    Log.e("2_mCurrentView ",""+position+mCurrentView.findFocus().getRootView());
                    final VideoView view2 = (VideoView) mCurrentView.findFocus();
//                    mCurrentView.findFocus().getRootView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                        @Override
//                        public void onFocusChange(View v, boolean hasFocus) {
//                            final VideoView view = (VideoView) v;
//                            if (hasFocus){
//                                Log.e("33_hasFocus_true"+position,"___"+url);
//                                view.setVideoURI(videourlToURI);
//                                view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                                    @Override
//                                    public void onPrepared(final MediaPlayer mp) {
//                                        Log.e("44_hasFocus_true"+position,"___"+url);
//                                        mp.start();
//                                        mp.setLooping(true);
//                                        mp.setVolume(1,1);
//                                        /** click interface*/
//                                        if (slidelistener!=null){
//                                            final  int pos = position;
//                                            view.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    slidelistener.onSlideClick(pos,v,mp);
//                                                }
//                                            });
//                                        }
//                                    }
//                                });
//                            }else {
//                                Log.e("3_hasFocus_false"+position,"___"+url);
//                                view.stopPlayback();
//                            }
//                        }
//                    });
                }
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

    PhotoFilter photoFilter;
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        Log.e("instantiateItem ",position+"");
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.slide,container,false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slidelinearlayout);

        ImageView imgslide = (ImageView) view.findViewById(R.id.slideimg);
        FrameLayout frameslide = (FrameLayout) view.findViewById(R.id.slideFrame);
        final VideoView videoslide = (VideoView) view.findViewById(R.id.slidevideo);

        final String url = URL+img_list.get(position);
        String mime = mimeInfos.get(position);
        Log.e("mime/url"+position,mime+"/"+url);
        if (mime.contains("image")){
            imgslide.setVisibility(View.VISIBLE);
            frameslide.setVisibility(View.GONE);
            videoslide.setVisibility(View.GONE);
            if (videoslide.isPlaying()){
                videoslide.stopPlayback();
            }
            photoFilter = new PhotoFilter(true, typeInfos.get(position),url,null,context,imgslide);
            photoFilter.photoFilterByType();
        }else if (mime.contains("video")){
            imgslide.setVisibility(View.GONE);
            frameslide.setVisibility(View.VISIBLE);
            videoslide.setVisibility(View.VISIBLE);
            final Uri videourlToURI = Uri.parse(url);
//            videoslide.setVideoURI(videourlToURI);
//            videoslide.requestFocus();

            /**비디오 한개만 있는 게시물일때*/
            if (img_list.size()==1){
                videoslide.setVideoURI(videourlToURI);
                videoslide.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final MediaPlayer mp) {
                        mp.start();
                        mp.setLooping(true);
                        mp.setVolume(1,1);
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
            }
            /**비디오가 여러개있을 때*/
            videoslide.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final VideoView view = (VideoView) v;
                    if (hasFocus){
                        Log.e("33_hasFocus_true"+position,"___"+url);
                        view.setVideoURI(videourlToURI);
                        view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(final MediaPlayer mp) {
                                Log.e("44_hasFocus_true"+position,"___"+url);
                                mp.start();
                                mp.setLooping(true);
                                mp.setVolume(1,1);
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
            /**비디오와 이미지가 섞인 게시물*/

//            videoslide.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(final MediaPlayer mp) {
//                    mp.start();
//                    mp.setLooping(true);
//                    mp.setVolume(1,1);
//                    /** click interface*/
//                    if (slidelistener!=null){
//                        final  int pos = position;
//                        videoslide.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                slidelistener.onSlideClick(pos,v,mp);
//                            }
//                        });
//                    }
//                }
//            });
        }
            container.addView(view);

            return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       // Log.e("destroyItem ",position+"");
       container.removeView((LinearLayout)object);
    }
}
