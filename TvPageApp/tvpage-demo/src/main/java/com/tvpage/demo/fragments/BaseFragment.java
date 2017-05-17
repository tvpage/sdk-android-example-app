package com.tvpage.demo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.tvpage.demo.MainTvPageActivity;


/**
 * Created by MTPC-110 on 12/29/2016.
 */

public class BaseFragment extends Fragment {
    MainTvPageActivity mainTvPageActivity = null;
    View rootView = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mainTvPageActivity == null) {
            // Create the instance
            mainTvPageActivity = (MainTvPageActivity) getActivity();
        }


    }


}
