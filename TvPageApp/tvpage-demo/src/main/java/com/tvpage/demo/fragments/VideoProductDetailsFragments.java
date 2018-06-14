package com.tvpage.demo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tvpage.demo.R;
import com.tvpage.demo.model.ProductModel;
import com.tvpage.demo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MTPC-110 on 4/10/2017.
 */

public class VideoProductDetailsFragments extends BaseFragment implements View.OnClickListener {


    ArrayList<ProductModel> productFromIntentlist = new ArrayList<ProductModel>();
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    ImageView imgClose;
    TextView tvTitle;
    int positionOfimageIntent = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_product_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //get bundle data
        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey("product_list")) {
                productFromIntentlist = (ArrayList<ProductModel>) b.getSerializable("product_list");
            }
            if (b.containsKey("position")) {
                positionOfimageIntent = b.getInt("position");
            }
        }
        init();
    }

    void init() {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        imgClose = (ImageView) rootView.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);

        setImageToPager();

    }

    void setImageToPager() {
        if (productFromIntentlist.size() > 0) {
            viewPagerAdapter = new ViewPagerAdapter(getActivity(), productFromIntentlist);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setCurrentItem(positionOfimageIntent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgClose:
                mainTvPageActivity.onBackPressed();
                break;
            default:
                break;
        }
    }


    public class ViewPagerAdapter extends PagerAdapter {
        // Declare Variables
        Context context;
        List<ProductModel> list;
        LayoutInflater inflater;

        public ViewPagerAdapter(Context context, List<ProductModel> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            // Declare Variables
            ImageView flag;
            TextView tvTitle;
            TextView tvPrice;
            TextView tvDetails;


            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.viewpager_item, container,
                    false);
            // Locate the ImageView in viewpager_item.xml
            flag = (ImageView) itemView.findViewById(R.id.flag);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvDetails = (TextView) itemView.findViewById(R.id.tvDetails);

            CommonUtils.setImageGlideProductDetails(context, list.get(position).getImg_product(), flag);

            tvTitle.setText(list.get(position).getTitle_product());


            String prices = list.get(position).getPrice_product();
            if (prices != null && !prices.isEmpty()) {
                if (prices.equalsIgnoreCase("free")) {
                    tvPrice.setText(prices);
                } else {
                    if (!prices.startsWith("$")) {
                        tvPrice.setText("$" + prices);
                    } else {
                        tvPrice.setText(prices);
                    }

                }
            } else {
                //rpices null
                tvPrice.setText("");
            }

            tvDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open Browser with product info link url...
                    try {
                        String url = "";
                        url = list.get(position).getProduct_info_url();
                        if (url != null && !TextUtils.isEmpty(url)) {

                            if (!url.startsWith("http:")) {
                                url = "http:" + url;
                            }

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // Add viewpager_item.xml to ViewPager
            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            ((ViewPager) container).removeView((RelativeLayout) object);

        }
    }
}
