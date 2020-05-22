package com.example.guswn.allthatlyrics.ui.account.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.guswn.allthatlyrics.ui.auth.LoginActivity;
import com.example.guswn.allthatlyrics.extension.MyPreference;
import com.example.guswn.allthatlyrics.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.setting_tb)
    Toolbar setting_tb;
    @BindView(R.id.logout_btn)
    Button logout_btn;
    @BindView(R.id.streamingtest)
    TextView streamingtest;

    @OnClick(R.id.streamingtest)
    public void streamingtest(){
//        Intent intent = new Intent(Setting.this, AgoraVideoChatOne.class);
//        startActivity(intent);
    }
    @OnClick(R.id.logout_btn)
    public void logout(){
        MyPreference.clearMY_EMAIL(SettingActivity.this);
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setSupportActionBar(setting_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_black);// 뒤로가기 버튼, 내가 지정할수 있다
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
