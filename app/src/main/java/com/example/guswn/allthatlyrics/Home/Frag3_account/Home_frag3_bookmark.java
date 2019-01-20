package com.example.guswn.allthatlyrics.Home.Frag3_account;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guswn.allthatlyrics.R;

import butterknife.ButterKnife;

public class Home_frag3_bookmark extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_home_frag3_bookmark, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
