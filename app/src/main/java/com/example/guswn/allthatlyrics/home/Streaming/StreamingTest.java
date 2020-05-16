package com.example.guswn.allthatlyrics.home.Streaming;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.guswn.allthatlyrics.R;

public class StreamingTest extends AppCompatActivity {

    public final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_test);

        Button startButton = (Button)findViewById(R.id.buttonStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StreamingTest.this, VideoActivity.class);
                EditText textRTSP = (EditText)findViewById(R.id.textRTSPUrl);
                intent.putExtra(VideoActivity.RTSP_URL, textRTSP.getText().toString());
                startActivity(intent);
            }
        });



    }

}
