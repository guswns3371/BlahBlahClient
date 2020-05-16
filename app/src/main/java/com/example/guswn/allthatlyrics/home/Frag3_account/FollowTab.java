package com.example.guswn.allthatlyrics.home.Frag3_account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.guswn.allthatlyrics.R;

import java.util.ArrayList;

import static com.example.guswn.allthatlyrics.main.Logo.MY_NAME;

public class FollowTab extends AppCompatActivity {
    public static String isfollower_following;
    public static ArrayList<String> Follower_InfoList;
    public static ArrayList<String> Followed_InfoList;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_tab);

        intent = getIntent();
        String username = intent.getStringExtra("username");
        String toolbarTitle;
        if (username==null){
            toolbarTitle = MY_NAME;
        }else {
            toolbarTitle = username;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_black);// 뒤로가기 버튼, 내가 지정할수 있다
        getSupportActionBar().setTitle(toolbarTitle);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#000000"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        //
        isfollower_following = intent.getStringExtra("isfollower_following");
        Follower_InfoList = intent.getStringArrayListExtra("follow_er_List");
        Followed_InfoList = intent.getStringArrayListExtra("follow_ed_List");
        if (isfollower_following.equals("follower")){
            mViewPager.setCurrentItem(0);// 탭 포커스하는 코드
            Log.e("Follower_InfoList ",Follower_InfoList.toString());
        }else if (isfollower_following.equals("following")){
            mViewPager.setCurrentItem(1);
            Log.e("Following_InfoList ",Followed_InfoList.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timer_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {//toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.timer_setting1:
                return true;

            case R.id.timer_setting2:
                return true;

            case R.id.timer_setting3:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    FollowTab_Follower followTab_follower = new FollowTab_Follower();
                    return followTab_follower;
                case 1:
                    FollowTab_Following followTab_following = new FollowTab_Following();
                    return followTab_following;

                    default:

                        return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            //나는 2페이지 니깐 return 2; 로 해주자
            return 2;
        }

        private String tabTitles[] = new String[]{"Friends", "Suggested Friends"};
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }// 이게 안먹네? 뭐야 ㅅㅂ
    }
}
