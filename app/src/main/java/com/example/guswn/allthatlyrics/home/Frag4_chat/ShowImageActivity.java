package com.example.guswn.allthatlyrics.home.Frag4_chat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.guswn.allthatlyrics.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowImageActivity extends AppCompatActivity {

    @BindView(R.id.imageView2)
    ImageView imageView;

    Intent intent;
    PhotoViewAttacher pAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);

        intent = getIntent();
        String isLoaded = intent.getStringExtra("isLoaded");
        String image = intent.getStringExtra("image");
        String isMine = intent.getStringExtra("isMine");

        if (isLoaded.equals("loaded")){
            Glide.with(ShowImageActivity.this)
                    .load(getString(R.string.URL)+image)
                    .apply(new RequestOptions().placeholder(R.drawable.load).fitCenter())
                    .into(imageView);
        }else if (isLoaded.equals("default")){
            if (isMine.equals("mine")){
                imageView.setImageURI(Uri.parse(image));
            }else if (isMine.equals("not_mine")){
                Glide.with(ShowImageActivity.this)
                        .load(getString(R.string.URL_withoutslash)+image)
                        .apply(new RequestOptions().placeholder(R.drawable.load).fitCenter())
                        .into(imageView);
            }

        }
        pAttacher = new PhotoViewAttacher(imageView);

//        pAttacher.update();
    }
}


