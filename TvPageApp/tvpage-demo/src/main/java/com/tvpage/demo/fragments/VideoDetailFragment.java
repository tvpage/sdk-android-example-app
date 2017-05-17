package com.tvpage.demo.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tvpage.demo.model.ProductModel;
import com.tvpage.demo.model.TvPageVideoModel;
import com.tvpage.demo.utils.CommonUtils;
import com.tvpage.demo.R;
import com.tvpage.lib.utils.TvPageInterfaces;

import com.tvpage.lib.api_listeners.OnTvPageResponseApiListener;
import com.tvpage.lib.model.TvPageResponseModel;
import com.tvpage.lib.view.TvPageBuilder;
import com.tvpage.lib.view.TvPagePlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tvpage.lib.utils.TvPageUtils.NORMAL_TVPAGE_VIDEO_TYPE;
import static com.tvpage.lib.utils.TvPageUtils.VIMEO_PRE_URLS;
import static com.tvpage.lib.utils.TvPageUtils.VIMEO_VIDEO_TYPE;
import static com.tvpage.lib.utils.TvPageUtils.YOUTUBE_PRE_URLS;
import static com.tvpage.lib.utils.TvPageUtils.YOUTUBE_VIDEO_TYPE;

/**
 * Created by MTPC-110 on 4/6/2017.
 */

public class VideoDetailFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView horizontalRecyclerView;
    private HorizontalAdapter adapter;
    private ArrayList<ProductModel> productList = new ArrayList<ProductModel>();
    private TextView txtVideoTitle;
    private TextView tvNoProduct;

    private TvPagePlayer tvPagePlayer;
    //intent data
    String titleIntent = "";
    String idIntent = "";
    String typeIntent = "";
    String video_idIntent = "";
    String video_descIntent = "";
    String video_durationIntent = "";
    String video_dateIntent = "";
    String video_entityIdAsChannelIdIntent = "";

    ArrayList<HashMap<String, String>> qualityListIntent = new ArrayList<HashMap<String, String>>();

    public ImageView ib_drawer;
    RelativeLayout ib_back;

    ImageView image_pin;

    TextView tvDescription;
    TextView tvDuration;
    TextView tvPublishedDate;

    ImageView ib_drawers_temp;

    ImageView imgHeaderLogo;

    TvPageVideoModel tvPageVideoModelFromIntent;

    String jsonObjectToPass = "";

    JSONObject jsonControlsToPass = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //get ineteht data
        Bundle b = getArguments();
        if (b != null) {

            if (b.containsKey(CommonUtils.PARCABLE_VIDEO_MODEL_KEY)) {
                tvPageVideoModelFromIntent = b.getParcelable(CommonUtils.PARCABLE_VIDEO_MODEL_KEY);
            }



         /*   if (b.containsKey("title")) {
                titleIntent = b.getString("title");
                CommonUtils.sout("t " + titleIntent);
            }
            if (b.containsKey("id")) {
                idIntent = b.getString("id");
                CommonUtils.sout("id " + idIntent);
            }
            if (b.containsKey("type")) {
                typeIntent = b.getString("type");
                CommonUtils.sout("typ " + typeIntent);
            }
            if (b.containsKey("video_id")) {
                video_idIntent = b.getString("video_id");
                CommonUtils.sout("vv " + video_idIntent);
            }
            if (b.containsKey("video_desc")) {
                video_descIntent = b.getString("video_desc");

            }
            if (b.containsKey("video_duration")) {
                video_durationIntent = b.getString("video_duration");

            }
            if (b.containsKey("video_date")) {
                video_dateIntent = b.getString("video_date");

            }
            if (b.containsKey("entity_id_parent")) {
                video_entityIdAsChannelIdIntent = b.getString("entity_id_parent");

            }
            if (b.containsKey("quality_urls")) {
                qualityListIntent = (ArrayList<HashMap<String, String>>) b.getSerializable("quality_urls");

            }

            if (b.containsKey("videoModel_paracalable")) {
                tvPageVideoModelFromIntent = b.getParcelable("videoModel_paracalable");
            }*/
        }
        init();
    }

    /**
     * Init.
     */
    void init(){

        //get data from parcable objects

        if (tvPageVideoModelFromIntent != null) {
            try {
                Gson gson = new Gson();
                jsonObjectToPass = gson.toJson(tvPageVideoModelFromIntent);

                if (tvPageVideoModelFromIntent.getTitle() != null) {
                    titleIntent = tvPageVideoModelFromIntent.getTitle();

                }

                if (tvPageVideoModelFromIntent.getId() != null) {
                    idIntent = tvPageVideoModelFromIntent.getId();
                }

                if (tvPageVideoModelFromIntent.getDate_created() != null) {
                    video_dateIntent = tvPageVideoModelFromIntent.getDate_created();
                }

                if (tvPageVideoModelFromIntent.getDescription() != null) {
                    video_descIntent = tvPageVideoModelFromIntent.getDescription();
                }

                if (tvPageVideoModelFromIntent.getEntityIdParent() != null) {
                    video_entityIdAsChannelIdIntent = tvPageVideoModelFromIntent.getEntityIdParent();
                }

                TvPageVideoModel.Asset asset = tvPageVideoModelFromIntent.getAsset();


                if (asset != null) {

                    if (asset.getPrettyDuration() != null) {
                        video_durationIntent = asset.getPrettyDuration();
                    }
                    if (asset.getType() != null) {
                        typeIntent = asset.getType();
                    }

                    if (asset.getVideoId() != null) {
                        video_idIntent = asset.getVideoId();
                    }


                    //Manage SOurces (File & Quality)

                    if (asset.getSources() != null && asset.getSources().size() > 0) {

                        for (int j = 0; j < asset.getSources().size(); j++) {
                            if (asset.getSources().get(j).getQuality() != null && asset.getSources().get(j).getFile() != null) {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put(asset.getSources().get(j).getQuality(), asset.getSources().get(j).getFile());
                                qualityListIntent.add(hashMap);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        horizontalRecyclerView = (RecyclerView) rootView.findViewById(R.id.horizontal_recyclerview);
        txtVideoTitle = (TextView) rootView.findViewById(R.id.txtVideoTitle);

        tvPagePlayer = (TvPagePlayer) rootView.findViewById(R.id.tvPagePlayer);
        tvNoProduct = (TextView) rootView.findViewById(R.id.tvNoProduct);
        tvDescription = (TextView) rootView.findViewById(R.id.tvDescription);
        tvDuration = (TextView) rootView.findViewById(R.id.tvDuration);
        tvPublishedDate = (TextView) rootView.findViewById(R.id.tvPublishedDate);

        ib_drawer = (ImageView) rootView.findViewById(R.id.ib_drawer);
        ib_back = (RelativeLayout) rootView.findViewById(R.id.ib_back);
        image_pin = (ImageView) rootView.findViewById(R.id.image_pin);
        ib_drawers_temp = (ImageView) rootView.findViewById(R.id.ib_drawers_temp);
        imgHeaderLogo = (ImageView) rootView.findViewById(R.id.imgHeaderLogo);

        ib_drawer.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        image_pin.setOnClickListener(this);
        imgHeaderLogo.setOnClickListener(this);

        setTitle();


        JSONObject jsonObjectTemp = null;
        jsonObjectTemp = getJsonForControls();
        if (jsonObjectTemp != null) {
            jsonControlsToPass = jsonObjectTemp;
        }




        //builder
        TvPageBuilder tvPageBuilder = new TvPageBuilder(tvPagePlayer).
                setOnVideoViewReady(new TvPageInterfaces.OnVideoViewReady() {
                    @Override
                    public void OnVideoViewReady(boolean isVideoLoaded) {
                        //TvPagePlayer Ready call back
                    }
                }).
                setOnVideoViewError(new TvPageInterfaces.OnVideoViewError() {
                    @Override
                    public void OnVideoViewError(String error) {
                        //TvPagePlayer Error call back
                    }
                }).
                setOnMediaError(new TvPageInterfaces.OnMediaError() {
                    @Override
                    public void OnMediaError(String error) {
                        //TvPagePlayer media Error call back
                    }
                })
                .setOnMediaReady(new TvPageInterfaces.OnMediaReady() {
                    @Override
                    public void OnMediaReady(boolean isMediaReady) {
                        //TvPagePlayer video ready call back
                    }
                }).setOnMediaComplete(new TvPageInterfaces.OnMediaComplete() {
                    @Override
                    public void OnMediaComplete(boolean isMediaCompleted) {
                        //TvPagePlayer video completed call back
                    }
                }).setOnVideoEnded(new TvPageInterfaces.OnVideoEnded() {
                    @Override
                    public void OnVideoEnded(boolean isVideoEnded) {
                        //TvPagePlayer video end call back
                    }
                }).setOnVideoPlaying(new TvPageInterfaces.OnVideoPlaying() {
                    @Override
                    public void OnVideoPlaying(boolean isVideoPlaying) {
                        //TvPagePlayer video playing call back
                    }
                }).setOnVideoPaused(new TvPageInterfaces.OnVideoPaused() {
                    @Override
                    public void OnVideoPaused(boolean isVideoPaused) {
                        //TvPagePlayer video paused call back
                    }
                }).setOnVideoBuffering(new TvPageInterfaces.OnVideoBuffering() {
                    @Override
                    public void OnVideoBuffering(boolean isVideoBuffering) {
                        //TvPagePlayer video buffering call back
                    }
                }).setOnMediaPlayBackQualityChanged(new TvPageInterfaces.OnMediaPlayBackQualityChanged() {
                    @Override
                    public void OnMediaPlayBackQualityChanged(String selectedQuality) {
                        //TvPagePlayer video quality changed call back
                    }
                }).setOnSeek(new TvPageInterfaces.OnSeek() {
                    @Override
                    public void OnSeek(String currentVideoTime) {
                        //TvPagePlayer seek call back
                    }
                }).setOnVideoLoad(new TvPageInterfaces.OnVideoLoad() {
                    @Override
                    public void OnVideoLoad(boolean isVideoLoaded) {
                        //TvPagePlayer Video loaded
                    }
                }).setOnVideoCued(new TvPageInterfaces.OnVideoCued() {
                    @Override
                    public void OnVideoCued(boolean isVideoLoaded) {
                        //TvPagePlayer Video cued
                    }
                }).setOnReady(new TvPageInterfaces.OnReady() {
                    @Override
                    public void OnPlayerReady() {
                        //TvPagePlayer Ready call back
                    }
                }).setOnStateChanged(new TvPageInterfaces.OnStateChanged() {
                    @Override
                    public void OnStateChanged() {
                        //TvPagePlayer state changed call back
                    }
                })
                .setOnError(new TvPageInterfaces.OnError() {
                    @Override
                    public void OnError() {
                        //TvPagePlayer error callback
                    }
                })
                .controls(jsonControlsToPass)
                .size(0, 0).initialise();


        horizontalRecyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManagaer);


        //call api for product
        //idIntent mean its video id
        if (CommonUtils.isInternetConnected(getActivity())) {
            tvPagePlayer.tvPageGetVideoProductExtractor(idIntent, new OnTvPageResponseApiListener() {
                @Override
                public void onSuccess(TvPageResponseModel tvPageResponseModel) {
                    productList.clear();

                    if (tvPageResponseModel != null && tvPageResponseModel.getJsonArray() != null) {

                        try {
                            JSONArray jsonArray = tvPageResponseModel.getJsonArray();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                ProductModel productModel = new ProductModel();

                                //reset variables for
                                String product_id = "";
                                String channel_id = "";
                                String video_id = "";

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //TvPageVideoProductModel tvPageModel = new TvPageVideoProductModel();
                                if (!jsonObject.isNull("title")) {
                                    String title = jsonObject.getString("title");
                                    productModel.setTitle_product(title);
                                }
                                if (!jsonObject.isNull("imageUrl")) {
                                    String imageUrl = jsonObject.getString("imageUrl");
                                    productModel.setImg_product(imageUrl);
                                }
                                if (!jsonObject.isNull("price")) {
                                    String price_sale = jsonObject.getString("price");
                                    productModel.setPrice_product(price_sale);
                                }
                                if (!jsonObject.isNull("linkUrl")) {
                                    String linkUrl = jsonObject.getString("linkUrl");
                                    productModel.setProduct_info_url(linkUrl);
                                }
                                if (!jsonObject.isNull("id")) {
                                    product_id = jsonObject.getString("id");
                                    productModel.setProduct_id(product_id);
                                }
                                if (!jsonObject.isNull("entityIdParent")) {
                                    channel_id = jsonObject.getString("entityIdParent");
                                    productModel.setProduct_channel_id(channel_id);
                                }
                                if (!jsonObject.isNull("entityIdChild")) {
                                    video_id = jsonObject.getString("entityIdChild");
                                    productModel.setProduct_video_id(video_id);
                                }


                                /*"id":"83102610",                      = Product ID
                                "entityIdParent":"83094490", = Channel Id
                                "entityIdChild":"83102610",   = Video Id*/


                                productList.add(productModel);


                                //call api for Product Impression
                                if (tvPagePlayer != null) {
                                    if (product_id != null && !TextUtils.isEmpty(product_id)
                                            && channel_id != null && !TextUtils.isEmpty(channel_id)
                                            && video_id != null && !TextUtils.isEmpty(video_id)
                                            ) {
                                        tvPagePlayer.analyticsProductImpressionApi(Integer.parseInt(channel_id),
                                                Integer.parseInt(video_id), Integer.parseInt(product_id));
                                    }
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    setDataForProduct();

                }

                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }


        //set title
        txtVideoTitle.setText(titleIntent);


        //set desc
        tvDescription.setText(video_descIntent);

        //set date
        if (video_dateIntent.trim().length() > 0) {
            String datefromTimestamp = CommonUtils.getDateFromTimestamp(Integer.parseInt(video_dateIntent) * 1000L, "MMMM dd, yyyy");
            if (datefromTimestamp != null) {
                tvPublishedDate.setText(datefromTimestamp);
            }
        } else {
            tvPublishedDate.setText(" - ");
        }


        //set duration
        tvDuration.setText(video_durationIntent);


        try {
            //set urls..load urls
            if (typeIntent.equalsIgnoreCase(YOUTUBE_VIDEO_TYPE)) {
                //load youtube urls
                tvPagePlayer.loadVideo(YOUTUBE_PRE_URLS + video_idIntent, jsonObjectToPass);
            } else if (typeIntent.equalsIgnoreCase(VIMEO_VIDEO_TYPE)) {
                //load vimeo urls
                tvPagePlayer.loadVideo(VIMEO_PRE_URLS + video_idIntent,  jsonObjectToPass);
            } else if (typeIntent.equalsIgnoreCase(NORMAL_TVPAGE_VIDEO_TYPE)) {
                //load normal urls
                tvPagePlayer.loadVideo("",  jsonObjectToPass);
            } else {
                //load normal urls
                tvPagePlayer.loadVideo("",  jsonObjectToPass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*
     {
          active: [true/false],
          seekbar :
               {       progressColor: 'HEX COLOR'
               },
          analytics:
              {
            tvpa: [true/false]
            }
     }

    */
    private JSONObject getJsonForControls() {
        JSONObject parent = new JSONObject();
        try {


            JSONObject seekbar = new JSONObject();
            seekbar.put("progressColor", "#FFFFFF");


            JSONObject analytics = new JSONObject();
            analytics.put("tvpa", "true");


            parent.put("active", "true");
            parent.put("seekbar", seekbar);
            parent.put("analytics", analytics);


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return parent;
    }

    void setTitle() {

        //enabled coach options


        //unlock drawer
        mainTvPageActivity.enableDisableDrawer(false);
        ib_back.setVisibility(View.VISIBLE);
        ib_drawers_temp.setVisibility(View.VISIBLE);
        ib_drawer.setVisibility(View.GONE);


    }


    void setDataForProduct() {
        if (productList != null && productList.size() > 0) {
            //hide error text and visible recyclerview

            //CommonUtils.sout("Productss > " + productList.toString());

            tvNoProduct.setVisibility(View.INVISIBLE);
            horizontalRecyclerView.setVisibility(View.VISIBLE);

            adapter = new HorizontalAdapter(productList);
            horizontalRecyclerView.setAdapter(adapter);
        } else {
            //display error text
            tvNoProduct.setVisibility(View.VISIBLE);
            horizontalRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    void manageErrorText(TextView textView, RecyclerView recyclerView, int size) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                mainTvPageActivity.onBackPressed();
                break;
            case R.id.image_pin:
                if (productList.size() > 0) {

                    if (tvPagePlayer != null) {
                        tvPagePlayer.pause();
                    }

                    Bundle b = new Bundle();
                    b.putSerializable("product_list", productList);
                    mainTvPageActivity.pushFragments(new VideoProductDetailsFragments(), true, true, false
                            , true, VideoProductDetailsFragments.class.getSimpleName(), b);
                } else {
                    CommonUtils.makeToast("No Product Found", getActivity());
                }
                break;
            case R.id.imgHeaderLogo:
                mainTvPageActivity.clearBackStackExceptHome();
                break;
            default:
                break;
        }
    }


    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private ArrayList<ProductModel> horizontalList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView image_related;

            public MyViewHolder(View view) {
                super(view);
                image_related = (ImageView) view.findViewById(R.id.image_related);

            }
        }

        public HorizontalAdapter(ArrayList<ProductModel> horizontalList) {
            this.horizontalList = horizontalList;
        }

        @Override
        public HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_horizonal, parent, false);

            return new HorizontalAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HorizontalAdapter.MyViewHolder holder, final int position) {

            //set glide images
            CommonUtils.setImageGlideProduct(getActivity(), horizontalList.get(position).getImg_product(), holder.image_related);

            holder.image_related.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (tvPagePlayer != null) {
                        tvPagePlayer.pause();
                    }
                    String product_id = "";
                    String channel_id = "";
                    String video_id = "";


                    product_id = horizontalList.get(position).getProduct_id();
                    channel_id = horizontalList.get(position).getProduct_channel_id();
                    video_id = horizontalList.get(position).getProduct_video_id();

                    //call api for Product Click Api
                    if (tvPagePlayer != null) {
                        if (product_id != null && !TextUtils.isEmpty(product_id)
                                && channel_id != null && !TextUtils.isEmpty(channel_id)
                                && video_id != null && !TextUtils.isEmpty(video_id)
                                ) {
                            tvPagePlayer.analyticsProductClickApi(Integer.parseInt(channel_id),
                                    Integer.parseInt(video_id), Integer.parseInt(product_id));
                        }
                    }

                    Bundle b = new Bundle();
                    b.putInt("position", position);
                    b.putSerializable("product_list", productList);
                    mainTvPageActivity.pushFragments(new VideoProductDetailsFragments(), true, true, false
                            , true, VideoProductDetailsFragments.class.getSimpleName(), b);

                }
            });
        }

        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }


    public void saveInstanceOfVideoView() {
        try {
            if (tvPagePlayer != null) {
                tvPagePlayer.onSaveInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreInsatnceOfVideoView() {
        try {
            if (tvPagePlayer != null) {
                tvPagePlayer.onRestoreInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
