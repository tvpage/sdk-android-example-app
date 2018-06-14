package com.tvpage.demo.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tvpage.demo.R;

/**
 * Created by MTPC-110 on 4/5/2017.
 */

public class SplashFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get Bundle Data
        init();
    }

    void init() {
        //lock drawer
        mainTvPageActivity.enableDisableDrawer(false);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mainTvPageActivity.pushFragments(new HomeFragment(), true, false, true, false, HomeFragment.class.getSimpleName(), null);
                //pushFragments(new ChannelListFragment(), true, false, true, false, ChannelListFragment.class.getSimpleName(),)
            }
        }, 3000);
    }
}
