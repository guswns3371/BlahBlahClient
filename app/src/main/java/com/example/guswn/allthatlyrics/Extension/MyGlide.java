package com.example.guswn.allthatlyrics.Extension;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.guswn.allthatlyrics.R;

import java.io.File;

public class MyGlide {
    ImageView imageView;
    Context context;

    public MyGlide( Context context, ImageView imageView){
       this. context = context;
       this. imageView = imageView;
    }

    public void glideURL(String url){
        Glide.with(context).load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load).circleCrop())
                .into(imageView);
    }
    public void glideURI(Uri uri){
        Glide.with(context).load(uri)
                .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load).circleCrop())
                .into(imageView);
    }
    public void glideFile(File file){
        Glide.with(context).load(Uri.fromFile(file))
                .apply(RequestOptions.placeholderOf(R.drawable.load).error(R.drawable.load).circleCrop())
                .into(imageView);
    }
}
