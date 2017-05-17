package com.tvpage.demo.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tvpage.demo.R;
import com.tvpage.demo.model.TvPageProductModel;
import com.tvpage.demo.model.TvPageVideoModel;
import com.tvpage.demo.utils.CommonUtils;
import com.tvpage.demo.utils.ItemClickListener;
import com.tvpage.lib.api_listeners.OnTvPageResponseApiListener;
import com.tvpage.lib.model.TvPageResponseModel;

import com.tvpage.lib.view.TvPagePlayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tvpage.demo.utils.CommonUtils.PARCABLE_VIDEO_MODEL_KEY;

/**
 * Created by MTPC-110 on 4/12/2017.
 */

public class ProductDetailFragament extends BaseFragment implements View.OnClickListener, ItemClickListener {
    RecyclerView recyclerView;
    TextView tvVideoError;
    ImageView imageEquipment;
    TextView tvProductPrice;
    TextView tvProductTitle;

    TvPageProductModel productInfoIntent;
    String productIdIntent = "";

    TvPagePlayer tvPagePlayer;

    ProgressDialog progressDialog;
    List<TvPageVideoModel> list;

    VideoAdapter adapter;

    public ImageView ib_drawer;
    public ImageView ib_drawers_temp;
    RelativeLayout ib_back;

    ImageView imgHeaderLogo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
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
        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey("product_info")) {
                productInfoIntent = (TvPageProductModel) b.getSerializable("product_info");
            }
        }

        if (productInfoIntent != null && productInfoIntent.getId() != null) {
            productIdIntent = productInfoIntent.getId();
        }

        init();
    }

    void init() {

        tvPagePlayer = new TvPagePlayer(getActivity());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        tvVideoError = (TextView) rootView.findViewById(R.id.tvVideoError);
        imageEquipment = (ImageView) rootView.findViewById(R.id.imageEquipment);
        tvProductPrice = (TextView) rootView.findViewById(R.id.tvProductPrice);
        tvProductTitle = (TextView) rootView.findViewById(R.id.tvProductTitle);
        ib_drawer = (ImageView) rootView.findViewById(R.id.ib_drawer);
        ib_drawers_temp = (ImageView) rootView.findViewById(R.id.ib_drawers_temp);
        imgHeaderLogo = (ImageView) rootView.findViewById(R.id.imgHeaderLogo);
        ib_back = (RelativeLayout) rootView.findViewById(R.id.ib_back);

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.setNestedScrollingEnabled(false);

        setTitle();

        ib_back.setOnClickListener(this);
        imgHeaderLogo.setOnClickListener(this);


        //api to get product videos
        getProductVideosApi();

        //set product data
        if (productInfoIntent != null) {
            if (productInfoIntent.getImageUrl() != null) {
                if (productInfoIntent.getImageUrl().startsWith("http")) {
                    CommonUtils.setImageGlideProductDetail(getActivity(), productInfoIntent.getImageUrl(), imageEquipment);
                } else {
                    CommonUtils.setImageGlideProductDetail(getActivity(), "http:" + productInfoIntent.getImageUrl(), imageEquipment);
                }

            }

            //set product title
            if (productInfoIntent.getTitle() != null) {
                tvProductTitle.setText("" + productInfoIntent.getTitle());
            }
            //set product price
            String prices = productInfoIntent.getPrice();
            if (prices != null && !prices.isEmpty()) {
                if (prices.equalsIgnoreCase("free")) {
                    tvProductPrice.setText(prices);
                } else {
                    if (!prices.startsWith("$")) {
                        tvProductPrice.setText("$" + prices);
                    } else {
                        tvProductPrice.setText(prices);
                    }

                }
            } else {
                //rpices null
                tvProductPrice.setText("");
            }


        }


    }

    void setTitle() {

        //enabled coach options


        //unlock drawer
        mainTvPageActivity.enableDisableDrawer(false);
        ib_back.setVisibility(View.VISIBLE);
        ib_drawers_temp.setVisibility(View.VISIBLE);
        ib_drawer.setVisibility(View.GONE);


    }

    void getProductVideosApi() {
        try {
            if (CommonUtils.isInternetConnected(getActivity())) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                //call api of list of videos
                tvPagePlayer.tvPageProductVideosExtractor(productIdIntent, new OnTvPageResponseApiListener() {
                    @Override
                    public void onSuccess(TvPageResponseModel tvPageResponseModel) {
                        progressDialog.dismiss();
                        list = new ArrayList<TvPageVideoModel>();

                        if (tvPageResponseModel != null && tvPageResponseModel.getJsonArray() != null) {

                            try {
                                JSONArray jsonArray = tvPageResponseModel.getJsonArray();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    TvPageVideoModel tvPageProductVideoModel = new TvPageVideoModel();

                                    if (!jsonObject.isNull("title")) {
                                        String title = jsonObject.getString("title");
                                        //add title in tvpage model
                                        tvPageProductVideoModel.setTitle(title);
                                    }
                                    if (!jsonObject.isNull("id")) {
                                        String id = jsonObject.getString("id");
                                        //add id in tvpage model
                                        tvPageProductVideoModel.setId(id);
                                    }

                                    if (!jsonObject.isNull("description")) {
                                        String description = jsonObject.getString("description");
                                        //add id in tvpage model
                                        tvPageProductVideoModel.setDescription(description);
                                    }

                                    if (!jsonObject.isNull("date_created")) {
                                        String date_created = jsonObject.getString("date_created");
                                        //add date in tvpage model
                                        tvPageProductVideoModel.setDate_created(date_created);
                                    }

                                    if (!jsonObject.isNull("entityIdParent")) {
                                        String entityIdParent = jsonObject.getString("entityIdParent");
                                        //add date in tvpage model
                                        tvPageProductVideoModel.setEntityIdParent(entityIdParent);
                                    }


                                    if (!jsonObject.isNull("asset")) {
                                        JSONObject jsonObjectAsset = jsonObject.getJSONObject("asset");

                                        TvPageVideoModel.Asset assets = new TvPageVideoModel.Asset();
                                        //get dash url & hls urls
                                        if (!jsonObjectAsset.isNull("dashUrl")) {
                                            String dashUrl = jsonObjectAsset.getString("dashUrl");
                                            assets.setDashUrl(dashUrl);
                                        }

                                        if (!jsonObjectAsset.isNull("hlsUrl")) {
                                            String hlsUrl = jsonObjectAsset.getString("hlsUrl");
                                            assets.setHlsUrl(hlsUrl);
                                        }

                                        if (!jsonObjectAsset.isNull("sources")) {

                                            JSONArray jsonArray1 = jsonObjectAsset.getJSONArray("sources");
                                            List<TvPageVideoModel.Sources> sourceList = new ArrayList<TvPageVideoModel.Sources>();
                                            for (int j = 0; j < jsonArray1.length(); j++) {

                                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                                TvPageVideoModel.Sources sourceToInsert = new TvPageVideoModel.Sources();
                                                if (!jsonObject1.isNull("file")) {
                                                    String file = jsonObject1.getString("file");
                                                    sourceToInsert.setFile(file);
                                                }
                                                if (!jsonObject1.isNull("quality")) {
                                                    String quality = jsonObject1.getString("quality");
                                                    sourceToInsert.setQuality(quality);
                                                }


                                                sourceList.add(sourceToInsert);

                                            }
                                            //add source list
                                            assets.setSources(sourceList);
                                        }


                                        if (!jsonObjectAsset.isNull("type")) {
                                            String type = jsonObjectAsset.getString("type");
                                            //add type list
                                            assets.setType(type);
                                        }

                                        if (!jsonObjectAsset.isNull("thumbnailUrl")) {
                                            String thumbnailUrl = jsonObjectAsset.getString("thumbnailUrl");
                                            //add type list
                                            assets.setThumbnailUrl(thumbnailUrl);
                                        }
                                        if (!jsonObjectAsset.isNull("videoId")) {
                                            String videoId = jsonObjectAsset.getString("videoId");
                                            //add type list
                                            assets.setVideoId(videoId);
                                        }
                                        if (!jsonObjectAsset.isNull("prettyDuration")) {
                                            String prettyDuration = jsonObjectAsset.getString("prettyDuration");
                                            assets.setPrettyDuration(prettyDuration);
                                        }


                                        //add assets in tvpage model
                                        tvPageProductVideoModel.setAsset(assets);
                                    }


                                    list.add(tvPageProductVideoModel);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                        setDataToList();

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        throwable.printStackTrace();
                    }
                });
            } else {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                CommonUtils.makeToast("No Internet Connection", getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        }
    }

    void setDataToList() {
        if (list != null && list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tvVideoError.setVisibility(View.GONE);

            adapter = new VideoAdapter(getActivity(), list);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            tvVideoError.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                mainTvPageActivity.onBackPressed();
                break;
            case R.id.imgHeaderLogo:
                mainTvPageActivity.clearBackStackExceptHome();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {


        if (list.size() > 0) {

            String title = "";
            String id = "";
            String type = "";
            String video_id = "";
            String video_desc = "";
            String video_duration = "";
            String video_date = "";
            String entity_id_parent = "";

/*
            ArrayList<HashMap<String, String>> qualityList = new ArrayList<HashMap<String, String>>();

            TvPageVideoModel tvPageVideoModelPos = list.get(position);

            if (tvPageVideoModelPos.getTitle() != null) {
                title = tvPageVideoModelPos.getTitle();

            }

            if (tvPageVideoModelPos.getId() != null) {
                id = tvPageVideoModelPos.getId();
            }

            if (tvPageVideoModelPos.getDate_created() != null) {
                video_date = tvPageVideoModelPos.getDate_created();
            }

            if (tvPageVideoModelPos.getDescription() != null) {
                video_desc = tvPageVideoModelPos.getDescription();
            }

            if (tvPageVideoModelPos.getEntityIdParent() != null) {
                entity_id_parent = tvPageVideoModelPos.getEntityIdParent();
            }

            TvPageVideoModel.Asset asset = tvPageVideoModelPos.getAsset();


            if (asset != null) {

                if (asset.getPrettyDuration() != null) {
                    video_duration = asset.getPrettyDuration();
                }
                if (asset.getType() != null) {
                    type = asset.getType();
                }

                if (asset.getVideoId() != null) {
                    video_id = asset.getVideoId();
                }


                //Manage SOurces (File & Quality)

                if (asset.getSources() != null && asset.getSources().size() > 0) {

                    for (int j = 0; j < asset.getSources().size(); j++) {
                        if (asset.getSources().get(j).getQuality() != null && asset.getSources().get(j).getFile() != null) {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put(asset.getSources().get(j).getQuality(), asset.getSources().get(j).getFile());
                            qualityList.add(hashMap);
                        }
                    }
                }
            }
*/


            Bundle bundle = new Bundle();

            /*bundle.putString("title", title);
            bundle.putString("id", id);
            bundle.putString("type", type);
            bundle.putString("video_id", video_id);
            bundle.putString("video_duration", video_duration);
            bundle.putString("video_desc", video_desc);
            bundle.putString("video_date", video_date);
            bundle.putString("entity_id_parent", entity_id_parent);
            bundle.putSerializable("quality_urls", qualityList);*/

            bundle.putParcelable(PARCABLE_VIDEO_MODEL_KEY, list.get(position));

            //push video Detail acttivity
            mainTvPageActivity.pushFragments(new VideoDetailFragment(), true, true, false, true, VideoDetailFragment.class.getSimpleName(), bundle);
        }


    }

    public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

        private List<TvPageVideoModel> mData;
        private LayoutInflater mInflater;
        private ItemClickListener mClickListener;

        // data is passed into the constructor
        public VideoAdapter(Context context, List<TvPageVideoModel> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the cell layout from xml when needed
        @Override
        public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_videos, parent, false);
            VideoAdapter.ViewHolder viewHolder = new VideoAdapter.ViewHolder(view);
            return viewHolder;
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(VideoAdapter.ViewHolder holder, int position) {
            TvPageVideoModel item = mData.get(position);
            if (item.getTitle() != null && item.getTitle().trim().length() > 0) {
                holder.txtVideo.setText(item.getTitle());
            }

            TvPageVideoModel.Asset asset = item.getAsset();
            if (asset != null && asset.getThumbnailUrl() != null) {
                //TvPageUtils.sout(asset.getThumbnailUrl() == null ? "Null" : asset.getThumbnailUrl());
                CommonUtils.setImageGlide(getActivity(), asset.getThumbnailUrl(), holder.imageVideo);

            }
        }

        // total number of cells
        @Override
        public int getItemCount() {
            return mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView txtVideo;
            public ImageView imageVideo;

            public ViewHolder(View itemView) {
                super(itemView);
                txtVideo = (TextView) itemView.findViewById(R.id.txtVideo);
                imageVideo = (ImageView) itemView.findViewById(R.id.imageVideo);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        // convenience method for getting data at click position
        /*public VideoBean getItem(int id) {
            return mData.get(id);
        }*/

        // allows clicks events to be caught
        public void setClickListener(ItemClickListener itemClickListener) {
            this.mClickListener = itemClickListener;
        }


    }
}
