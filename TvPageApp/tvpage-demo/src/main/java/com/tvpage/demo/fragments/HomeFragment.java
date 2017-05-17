package com.tvpage.demo.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tvpage.demo.R;
import com.tvpage.demo.model.TvPageVideoModel;
import com.tvpage.demo.utils.CommonUtils;


import com.tvpage.demo.utils.EndlessRecyclerOnScrollListener;
import com.tvpage.demo.utils.MyPreferencesForTvPageApp;
import com.tvpage.demo.utils.SnappyRecyclerView;
import com.tvpage.lib.api_listeners.OnTvPageResponseApiListener;
import com.tvpage.lib.model.TvPageResponseModel;

import com.tvpage.demo.utils.ItemClickListener;

import com.tvpage.lib.utils.TvPageInstance;
import com.tvpage.lib.view.TvPagePlayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.tvpage.demo.utils.CommonUtils.PARCABLE_VIDEO_MODEL_KEY;

/**
 * Created by MTPC-110 on 4/5/2017.
 */

public class HomeFragment extends BaseFragment implements ItemClickListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    List<TvPageVideoModel> list;

    TvPagePlayer tvPagePlayer;
    ProgressDialog progressDialog;
    TextView tvVideoError;


    public ImageView ib_drawer;
    RelativeLayout ib_back;
    RelativeLayout relCoffee;
    RelativeLayout relEquipment;
    RelativeLayout relDrink;

    ImageView imgHeaderLogo;

    RelativeLayout relViewVideoGallery;
    int pageNumberLoadMore = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
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
        tvVideoError = (TextView) rootView.findViewById(R.id.tvVideoError);

        ib_drawer = (ImageView) rootView.findViewById(R.id.ib_drawer);
        imgHeaderLogo = (ImageView) rootView.findViewById(R.id.imgHeaderLogo);
        ib_back = (RelativeLayout) rootView.findViewById(R.id.ib_back);
        relCoffee = (RelativeLayout) rootView.findViewById(R.id.relCoffee);
        relEquipment = (RelativeLayout) rootView.findViewById(R.id.relEquipment);
        relDrink = (RelativeLayout) rootView.findViewById(R.id.relDrink);
        relViewVideoGallery = (RelativeLayout) rootView.findViewById(R.id.relViewVideoGallery);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        /*int spanCount = 2; // 3 columns
        int spacing = 20; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);*/

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);

        recyclerView.setNestedScrollingEnabled(false);

        tvPagePlayer = new TvPagePlayer(getActivity());

        ib_drawer.setOnClickListener(this);
        relCoffee.setOnClickListener(this);
        relEquipment.setOnClickListener(this);
        relDrink.setOnClickListener(this);
        imgHeaderLogo.setOnClickListener(this);
        relViewVideoGallery.setOnClickListener(this);

        //set tile
        setTitle();


        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(horizontalLayoutManagaer) {
            @Override
            public void onLoadMore(int current_page) {
                pageNumberLoadMore++;
                callVideoListApi(true);
            }
        });
        callVideoListApi(false);
    }

    private void dialogUserID() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_userid);

        Button btnOK = (Button) dialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        final EditText edtUserId = (EditText) dialog.findViewById(R.id.edtAccountId);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainTvPageActivity.hideKeyboard();
                dialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable editValue = edtUserId.getText();
                if (!TextUtils.isEmpty(editValue) && editValue.toString().trim().length() > 0) {


                    TvPageInstance.getInstance(getActivity()).setApiKey(editValue.toString());
                    //here need to clear pagenumber as its refrseh whole data
                    pageNumberLoadMore = 0;
                    callVideoListApi(false);

                } else {
                    //show alert
                    CommonUtils.makeToast("Please enter your id", getActivity());
                }

                mainTvPageActivity.hideKeyboard();
                dialog.dismiss();


            }
        });

        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }


    void setTitle() {

        //enabled coach options


        //unlock drawer
        mainTvPageActivity.enableDisableDrawer(true);

        ib_back.setVisibility(View.GONE);
        ib_drawer.setVisibility(View.VISIBLE);


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
                                    progressDialog.dismiss();
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

    void setDataToList(boolean isLoadmore) {
        if (list != null && list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tvVideoError.setVisibility(View.GONE);

            if (!isLoadmore) {
                adapter = new VideoAdapter(getActivity(), list);
                adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            tvVideoError.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        // Toast.makeText(this, list.get(position).name, Toast.LENGTH_SHORT).show();


        if (list.size() > 0) {

            String title = "";
            String id = "";
            String type = "";
            String video_id = "";
            String video_desc = "";
            String video_duration = "";
            String video_date = "";
            String entity_id_parent = "";

        /*    ArrayList<HashMap<String, String>> qualityList = new ArrayList<HashMap<String, String>>();

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


    }

    public static int getScreenWidth(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_drawer:
                mainTvPageActivity.enableDisableDrawer(true);
                mainTvPageActivity.openDrawer();
                break;
            case R.id.relCoffee:
                pushProductScreen();
                break;
            case R.id.relEquipment:
                pushProductScreen();
                break;
            case R.id.relDrink:
                pushProductScreen();
                break;
            case R.id.imgHeaderLogo:
                //ssdialogUserIDSDFDFDF();
                break;
            case R.id.relViewVideoGallery:
                mainTvPageActivity.pushFragments(new ChannelListFragment(), true, true, false, true, ChannelListFragment.class.getSimpleName(), null);
                break;
            default:
                break;
        }
    }

    void pushProductScreen() {
        mainTvPageActivity.pushFragments(new ProductFragment(), true, true, false, true, ProductFragment.class.getSimpleName(), null);
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
            View view = mInflater.inflate(R.layout.item_videos_home, parent, false);
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
