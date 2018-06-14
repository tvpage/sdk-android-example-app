package com.tvpage.demo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tvpage.demo.fragments.HomeFragment;
import com.tvpage.demo.fragments.SplashFragment;
import com.tvpage.demo.fragments.ChannelListFragment;
import com.tvpage.demo.fragments.VideoDetailFragment;
import com.tvpage.demo.utils.CommonUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by MTPC-110 on 4/5/2017.
 */

public class MainTvPageActivity extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout mDrawerLayout;
    ImageView imgCloseDrawer;
    ImageView imgLogoDrawer;

    TextView tvHome;
    TextView tvCoffee;
    TextView tvEquipment;
    TextView tvDrinkWare;
    TextView tvOurBlog;
    TextView tvVideoGallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tv_page);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        imgCloseDrawer = (ImageView) findViewById(R.id.imgCloseDrawer);
        imgLogoDrawer = (ImageView) findViewById(R.id.imgLogoDrawer);
        tvHome = (TextView) findViewById(R.id.tvHome);
        tvCoffee = (TextView) findViewById(R.id.tvCoffee);
        tvEquipment = (TextView) findViewById(R.id.tvEquipment);
        tvDrinkWare = (TextView) findViewById(R.id.tvDrinkWare);
        tvOurBlog = (TextView) findViewById(R.id.tvOurBlog);
        tvVideoGallery = (TextView) findViewById(R.id.tvVideoGallery);


        imgCloseDrawer.setOnClickListener(this);

        imgLogoDrawer.setOnClickListener(this);
        tvHome.setOnClickListener(this);
        tvCoffee.setOnClickListener(this);
        tvEquipment.setOnClickListener(this);
        tvDrinkWare.setOnClickListener(this);
        tvOurBlog.setOnClickListener(this);
        tvVideoGallery.setOnClickListener(this);


        //drawers
        //drawer other things
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        mDrawerLayout.setScrimColor(CommonUtils.getColor(MainTvPageActivity.this, R.color.app_background));
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View container = findViewById(R.id.container);
                container.setTranslationX(slideOffset * drawerView.getWidth());
                //relative.setTranslationX(slideOffset * drawerView.getWidth());
                if (slideOffset > 0f) {
                    //setBlurAlpha(slideOffset);
                } else {
                    //clearBlurImage();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        mDrawerLayout.setOnClickListener(this);


        //push splash screen
        pushFragments(new SplashFragment(), true, false, true, false, SplashFragment.class.getSimpleName(), null);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgCloseDrawer:
                closeDrawer();
                break;
            case R.id.imgLogoDrawer:
                closeDrawer();
                break;
            case R.id.tvHome:
                closeDrawer();
                clearBackstack();
                pushFragments(new HomeFragment(), true, false, true, false, HomeFragment.class.getSimpleName(), null);
                break;
            case R.id.tvCoffee:
                break;
            case R.id.tvEquipment:
                break;
            case R.id.tvDrinkWare:
                break;
            case R.id.tvOurBlog:
                break;
            case R.id.tvVideoGallery:
                closeDrawer();
                Bundle b = new Bundle();
                b.putString(CommonUtils.VIDEO_GALLERY_SOURCE_KEY, CommonUtils.VIDEO_GALLERY_SOURCE_VALUE);
                pushFragments(new ChannelListFragment(), true, true, false, true, ChannelListFragment.class.getSimpleName(), b);
                break;

            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {

        hideKeyboard();

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
            return;
        } else {
            super.onBackPressed();


        }

    }


    public void enableDisableDrawer(final boolean isDrawerEnable) {
        if (!isDrawerEnable) {
            //ib_drawer.setEnabled(false);
            mDrawerLayout
                    .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            //ib_drawer.setEnabled(true);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    public void closeDrawer() {
        hideKeyboard();
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    public void openDrawer() {
        hideKeyboard();
        //scrollDrawer.smoothScrollTo(0, 0);
        mDrawerLayout.openDrawer(Gravity.LEFT);


    }


    //fragments common methods
    public void pushFragments(Fragment fragment, boolean isAnimate, boolean isAdd, boolean isReplace,
                              boolean isAddToBackstack, String tag, Bundle bundle) {

        //set bundle data to fragments
        if (bundle != null) {
            fragment.setArguments(bundle);
        }


        FragmentManager fragmentManager = getFragmentManager();
        // Or: FragmentManager fragmentManager = getSupportFragmentManager() fro below 4.0 support
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (getCurrentFragment() != null) {
            if (getCurrentFragment().getTag().equalsIgnoreCase(tag)) {
                return;
            }
        }

        //determine animation
        if (isAnimate) {

          /*  fragmentTransaction.
                    setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right,R.animator.slide_in_right
                            ,R.animator.slide_out_left
                    );*/

        }


        if (isAdd) {
            //add fragments
            fragmentTransaction.add(R.id.container, fragment, tag);
        } else if (isReplace) {
            //replace fragments
            fragmentTransaction.replace(R.id.container, fragment, tag);
        } else {
        }

        //determine backstack
        if (isAddToBackstack) {
            fragmentTransaction.addToBackStack(tag);
        } else {
        }


        //hide keyboard
        hideKeyboard();

        if (getCurrentFragment() != null) {
            fragmentTransaction.hide(getCurrentFragment());
        }

        if (!isFinishing()) {
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void clearBackstack() {
        try {
            FragmentManager fm = getFragmentManager();
            if (fm != null) {
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearBackStackExceptHome() {
        try {
            FragmentManager fm = getFragmentManager();
            if (fm != null) {
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    if (!(getCurrentFragment() instanceof HomeFragment)) {
                        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Fragment getCurrentFragment() {
        FragmentManager fm = getFragmentManager();
        if (fm != null) {
            return fm.findFragmentById(R.id.container);
        } else {
            return null;
        }
    }

    public void setStatusAndNavigationBarColor(int color, boolean isStatusBar, boolean isNavigationBar) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isStatusBar) {
                window.setStatusBarColor(color);
            }

          /*  if(isNavigationBar){
                window.setNavigationBarColor(color);
            }*/
        }
    }

    public void hideKeyboard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (getCurrentFragment() != null) {
                if (getCurrentFragment() instanceof VideoDetailFragment) {
                    ((VideoDetailFragment) getCurrentFragment()).saveInstanceOfVideoView();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (getCurrentFragment() != null) {
                if (getCurrentFragment() instanceof VideoDetailFragment) {
                    ((VideoDetailFragment) getCurrentFragment()).restoreInsatnceOfVideoView();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


