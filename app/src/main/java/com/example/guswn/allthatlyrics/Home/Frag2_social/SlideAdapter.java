package com.example.guswn.allthatlyrics.Home.Frag2_social;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.guswn.allthatlyrics.PhotoFilter;
import com.example.guswn.allthatlyrics.R;

import java.util.ArrayList;

import static com.example.guswn.allthatlyrics.MainActivity.URL_withoutslash;
import static com.example.guswn.allthatlyrics.PhotoFilter.getTypeFromString;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<String>img_list = new ArrayList<>();
    ArrayList<Integer>type = new ArrayList<>();
    ArrayList<SocialImageModel> infoModels;


    public SlideAdapter(Context context, ArrayList<SocialImageModel> infoModels){
        this.context = context;
        this.infoModels = infoModels;

        for (int a=0 ; a<infoModels.size(); a++){
            img_list.add( infoModels.get(a).getUrl()) ;
            type.add(getTypeFromString(infoModels.get(a).getFilter()));
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
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.e("instantiateItem ",position+"");
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.slide,container,false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slidelinearlayout);
        ImageView imgslide = (ImageView) view.findViewById(R.id.slideimg);

            photoFilter = new PhotoFilter(true,type.get(position),URL_withoutslash+img_list.get(position),null,context,imgslide);
            photoFilter.photoFilterByType();
            container.addView(view);
            return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       // Log.e("destroyItem ",position+"");
       container.removeView((LinearLayout)object);
    }
}
