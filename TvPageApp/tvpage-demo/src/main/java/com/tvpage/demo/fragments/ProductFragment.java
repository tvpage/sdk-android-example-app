package com.tvpage.demo.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tvpage.demo.model.TvPageProductModel;
import com.tvpage.demo.model.TvPageVideoModel;
import com.tvpage.demo.utils.CommonUtils;
import com.tvpage.demo.R;
import com.tvpage.demo.utils.EndlessRecyclerOnScrollListener;
import com.tvpage.demo.utils.ItemDecorationAlbumColumns;
import com.tvpage.demo.utils.MyPreferencesForTvPageApp;
import com.tvpage.lib.api_listeners.OnTvPageResponseApiListener;

import com.tvpage.lib.model.TvPageResponseModel;

import com.tvpage.demo.utils.GridSpacingItemDecoration;
import com.tvpage.demo.utils.ItemClickListener;
import com.tvpage.lib.view.TvPagePlayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tvpage.demo.utils.CommonUtils.PARCABLE_VIDEO_MODEL_KEY;

/**
 * Created by MTPC-110 on 4/6/2017.
 */

public class ProductFragment extends BaseFragment implements View.OnClickListener, ItemClickListener {

    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    List<TvPageVideoModel> list;

    TvPagePlayer tvPagePlayer;
    ProgressDialog progressDialog;
    TextView tvVideoError;


    private RecyclerView recyclerViewEquipment;
    private EquipmentAdapter equipmentAdapter;
    ArrayList<TvPageProductModel> listEquipmentProduct;


    TextView tvEquipmentError;


    public ImageView ib_drawer;
    RelativeLayout ib_back;
    ImageView ib_drawers_temp;

    RelativeLayout relViewVideoGallery;
    int pageNumberLoadMore = 0;

    ImageView imgHeaderLogo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    void init() {

        tvVideoError = (TextView) rootView.findViewById(R.id.tvVideoError);
        tvEquipmentError = (TextView) rootView.findViewById(R.id.tvEquipmentError);
        ib_drawer = (ImageView) rootView.findViewById(R.id.ib_drawer);
        ib_back = (RelativeLayout) rootView.findViewById(R.id.ib_back);
        relViewVideoGallery = (RelativeLayout) rootView.findViewById(R.id.relViewVideoGallery);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerViewEquipment = (RecyclerView) rootView.findViewById(R.id.recyclerViewEquipment);
        ib_drawers_temp = (ImageView) rootView.findViewById(R.id.ib_drawers_temp);
        imgHeaderLogo = (ImageView) rootView.findViewById(R.id.imgHeaderLogo);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 20, true));
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        //equpment list
        recyclerViewEquipment.setHasFixedSize(true);
        recyclerViewEquipment.addItemDecoration(new ItemDecorationAlbumColumns(1,2));
        GridLayoutManager layoutManagerEqip = new GridLayoutManager(getActivity(), 2);
        recyclerViewEquipment.setLayoutManager(layoutManagerEqip);


        tvPagePlayer = new TvPagePlayer(getActivity());

        ib_back.setOnClickListener(this);
        relViewVideoGallery.setOnClickListener(this);
        imgHeaderLogo.setOnClickListener(this);
        //set tile
        setTitle();

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                pageNumberLoadMore++;
                callVideoListApi(true);
            }
        });

        callVideoListApi(false);


    }


    void setTitle() {

        //enabled coach options


        //unlock drawer
        mainTvPageActivity.enableDisableDrawer(false);

        ib_back.setVisibility(View.VISIBLE);
        ib_drawers_temp.setVisibility(View.VISIBLE);
        ib_drawer.setVisibility(View.GONE);


    }

    public void callVideoListApi(final boolean isLoadMore) {
        try {
            if (CommonUtils.isInternetConnected(getActivity())) {

                if (!isLoadMore) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }

                //call api of list of videos
                String channel_id = MyPreferencesForTvPageApp.getPref(getActivity(), MyPreferencesForTvPageApp.CHANNEL_ID_PREF_KEY);
                tvPagePlayer.tvPageChannelsVideosExtractor(channel_id, pageNumberLoadMore, CommonUtils.NUMBER_OF_RESULT_TO_RETURN, ""
                        , new OnTvPageResponseApiListener() {
                            @Override
                            public void onSuccess(TvPageResponseModel tvPageResponseModel) {
                                if (!isLoadMore) {
                                    callProductEquipmentListApi();
                                    list = new ArrayList<TvPageVideoModel>();
                                }
                                if (tvPageResponseModel != null && tvPageResponseModel.getJsonArray() != null) {

                                    try {
                                        JSONArray jsonArray = tvPageResponseModel.getJsonArray();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            TvPageVideoModel tvPageModel = new TvPageVideoModel();

                                            if (!jsonObject.isNull("title")) {
                                                String title = jsonObject.getString("title");
                                                //add title in tvpage model
                                                tvPageModel.setTitle(title);
                                            }
                                            if (!jsonObject.isNull("id")) {
                                                String id = jsonObject.getString("id");
                                                //add id in tvpage model
                                                tvPageModel.setId(id);
                                            }

                                            if (!jsonObject.isNull("description")) {
                                                String description = jsonObject.getString("description");
                                                //add id in tvpage model
                                                tvPageModel.setDescription(description);
                                            }

                                            if (!jsonObject.isNull("date_created")) {
                                                String date_created = jsonObject.getString("date_created");
                                                //add date in tvpage model
                                                tvPageModel.setDate_created(date_created);
                                            }

                                            if (!jsonObject.isNull("entityIdParent")) {
                                                String entityIdParent = jsonObject.getString("entityIdParent");
                                                //add date in tvpage model
                                                tvPageModel.setEntityIdParent(entityIdParent);
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
                                                tvPageModel.setAsset(assets);
                                            }


                                            list.add(tvPageModel);
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                                setDataToList(isLoadMore);

                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                if (!isLoadMore) {
                                    callProductEquipmentListApi();
                                }

                                throwable.printStackTrace();
                            }
                        });
            } else {
                /*if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                if (!isLoadMore) {
                    callProductEquipmentListApi();
                }
                CommonUtils.makeToast("No Internet Connection", getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callProductEquipmentListApi() {
        try {
            if (CommonUtils.isInternetConnected(getActivity())) {


                //call api of list of videos
                tvPagePlayer.tvPageProductExtractor(new OnTvPageResponseApiListener() {
                    @Override
                    public void onSuccess(TvPageResponseModel tvPageResponseModel) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        listEquipmentProduct = new ArrayList<TvPageProductModel>();

                        if (tvPageResponseModel != null && tvPageResponseModel.getJsonArray() != null) {

                            try {
                                JSONArray jsonArray = tvPageResponseModel.getJsonArray();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    TvPageProductModel tvPageProductModel = new TvPageProductModel();

                                    if (!jsonObject.isNull("title")) {
                                        String title = jsonObject.getString("title");
                                        //add title in tvpage model
                                        tvPageProductModel.setTitle(title);
                                    }
                                    if (!jsonObject.isNull("imageUrl")) {
                                        String imageUrl = jsonObject.getString("imageUrl");
                                        //add id in tvpage model
                                        tvPageProductModel.setImageUrl(imageUrl);
                                    }
                                    if (!jsonObject.isNull("price")) {
                                        String price = jsonObject.getString("price");
                                        //add id in tvpage model
                                        tvPageProductModel.setPrice(price);
                                    }

                                    if (!jsonObject.isNull("id")) {
                                        String id = jsonObject.getString("id");
                                        //add title in tvpage model
                                        tvPageProductModel.setId(id);
                                    }


                                    listEquipmentProduct.add(tvPageProductModel);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                        setEquipmentDataToList();

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
        }
    }

    void setDataToList(boolean isLoadMore) {
        if (list != null && list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tvVideoError.setVisibility(View.GONE);
            if (!isLoadMore) {
                adapter = new VideoAdapter(getActivity(), list);
                adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);
            } else {
                //just notify adapter
                adapter.notifyDataSetChanged();
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            tvVideoError.setVisibility(View.VISIBLE);

        }
    }

    void setEquipmentDataToList() {
        if (listEquipmentProduct != null && listEquipmentProduct.size() > 0) {
            recyclerViewEquipment.setVisibility(View.VISIBLE);
            tvEquipmentError.setVisibility(View.GONE);

            equipmentAdapter = new EquipmentAdapter(getActivity(), listEquipmentProduct);
            equipmentAdapter.setClickListener(this);

            recyclerViewEquipment.setAdapter(equipmentAdapter);
        } else {
            recyclerViewEquipment.setVisibility(View.GONE);
            tvEquipmentError.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        // Toast.makeText(this, list.get(position).name, Toast.LENGTH_SHORT).show();

        switch (view.getId()) {
            case R.id.linearParentFullWidth:
                if (list.size() > 0) {

                    String title = "";
                    String id = "";
                    String type = "";
                    String video_id = "";
                    String video_desc = "";
                    String video_duration = "";
                    String video_date = "";
                    String entity_id_parent = "";

                    /*ArrayList<HashMap<String, String>> qualityList = new ArrayList<HashMap<String, String>>();

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
                    }*/


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
                break;
            case R.id.linearParentEquip:
                //push Product detail screen
                if (listEquipmentProduct.size() > 0) {

                    TvPageProductModel tvPageProductModel = listEquipmentProduct.get(position);

                    Bundle b = new Bundle();
                    b.putSerializable("product_info", tvPageProductModel);

                    mainTvPageActivity.pushFragments(new ProductDetailFragament(), true, true,
                            false, true, ProductDetailFragament.class.getSimpleName(), b);

                }
                break;
            default:
                break;
        }


    }

    public static int getScreenWidth(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                mainTvPageActivity.onBackPressed();

                break;
            case R.id.relViewVideoGallery:
                mainTvPageActivity.pushFragments(new ChannelListFragment(), true, true,
                        false, true, ChannelListFragment.class.getSimpleName(), null);
                break;
            case R.id.imgHeaderLogo:
                mainTvPageActivity.clearBackStackExceptHome();
                break;
            default:
                break;
        }
    }


    //adapters


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
            View view = mInflater.inflate(R.layout.item_videos_full_width, parent, false);
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
            public LinearLayout linearParentFullWidth;

            public ViewHolder(View itemView) {
                super(itemView);
                txtVideo = (TextView) itemView.findViewById(R.id.txtVideo);
                imageVideo = (ImageView) itemView.findViewById(R.id.imageVideo);
                linearParentFullWidth = (LinearLayout) itemView.findViewById(R.id.linearParentFullWidth);
                linearParentFullWidth.setOnClickListener(this);
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

    public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

        private List<TvPageProductModel> mData;
        private LayoutInflater mInflater;
        private ItemClickListener mClickListener;

        // data is passed into the constructor
        public EquipmentAdapter(Context context, List<TvPageProductModel> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_equipments_full_width, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TvPageProductModel item = mData.get(position);
            if (item.getTitle() != null && item.getTitle().trim().length() > 0) {
                holder.txtEquipment.setText(item.getTitle());
            }

            if (item.getImageUrl() != null && item.getImageUrl().trim().length() > 0) {
                CommonUtils.setImageGlideProduct(getActivity(), item.getImageUrl(), holder.imageEquipment);
            }

            String prices = item.getPrice();

            if (prices != null && !prices.isEmpty()) {
                if (prices.equalsIgnoreCase("free")) {
                    holder.tvPrice.setText(prices);
                } else {
                    if (!prices.startsWith("$")) {
                        holder.tvPrice.setText("$" + prices);
                    } else {
                        holder.tvPrice.setText(prices);
                    }

                }
            } else {
                //rpices null
                holder.tvPrice.setText("");
            }


        }

        // total number of cells
        @Override
        public int getItemCount() {
            return mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView txtEquipment;
            public ImageView imageEquipment;
            public LinearLayout linearParentEquip;
            public TextView tvPrice;

            public ViewHolder(View itemView) {
                super(itemView);
                txtEquipment = (TextView) itemView.findViewById(R.id.txtEquipment);
                imageEquipment = (ImageView) itemView.findViewById(R.id.imageEquipment);
                linearParentEquip = (LinearLayout) itemView.findViewById(R.id.linearParentEquip);
                tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
                linearParentEquip.setOnClickListener(this);
            }


            @Override
            public void onClick(View v) {
                if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
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
