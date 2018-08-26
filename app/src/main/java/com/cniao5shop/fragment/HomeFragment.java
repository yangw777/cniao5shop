package com.cniao5shop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cniao5shop.Contants;
import com.cniao5shop.R;
import com.cniao5shop.WareListActivity;
import com.cniao5shop.adapter.decoration.DividerItemDecoration;
import com.cniao5shop.adapter.HomeCatgoryAdapter;
import com.cniao5shop.bean.Banner;
import com.cniao5shop.bean.Campaign;
import com.cniao5shop.bean.HomeCampaign;
import com.cniao5shop.http.BaseCallback;
import com.cniao5shop.http.OkHttpHelper;
import com.cniao5shop.http.SpotsCallBack;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;

import java.util.List;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/7/20
 * @Description: ${todo}
 */

public class HomeFragment extends Fragment {

    private SliderLayout mSliderLayout;
    private PagerIndicator indicator;

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    private HomeCatgoryAdapter mAdapter;

    private static final String TAG = "HomeFragment";
    private Gson mGson = new Gson();
    private List<Banner> mBanner;

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        indicator = view.findViewById(R.id.custom_indicator);

        requestImages();

        initRecyclerView(view);

        return view;
    }

    private void requestImages(){
        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";

        httpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });



    }

    private void initRecyclerView(View view) {

        mRecyclerView = view.findViewById(R.id.recyclerview);
/*
        List<HomeCategory> datas = new ArrayList<>(15);

        HomeCategory category = new HomeCategory("热门活动", R.drawable.img_big_1, R.drawable.img_1_small1, R.drawable.img_1_small2);
        datas.add(category);

        category = new HomeCategory("有利可图", R.drawable.img_big_4, R.drawable.img_4_small1, R.drawable.img_4_small2);
        datas.add(category);
        category = new HomeCategory("品牌街", R.drawable.img_big_2, R.drawable.img_2_small1, R.drawable.img_2_small2);
        datas.add(category);

        category = new HomeCategory("金融街 包赚翻", R.drawable.img_big_1, R.drawable.img_3_small1, R.drawable.imag_3_small2);
        datas.add(category);

        category = new HomeCategory("超值购", R.drawable.img_big_0, R.drawable.img_0_small1, R.drawable.img_0_small2);
        datas.add(category);

        mAdapter = new HomeCatgoryAdapter(datas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecortion());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
*/
        httpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }


            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }

    private void initData(List<HomeCampaign> homeCampaigns){
        mAdapter = new HomeCatgoryAdapter(homeCampaigns, getActivity());

        mAdapter.setOnCampaignClickListener(new HomeCatgoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID, campaign.getId());

                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    private void initSlider(){
        if(null != mBanner){
            for(Banner banner : mBanner){
                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);

                mSliderLayout.addSlider(textSliderView);
            }
        }



        mSliderLayout.setCustomIndicator(indicator);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);

        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d(TAG, "onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
//                Log.d(TAG, "onPageSelected");

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.d(TAG, "onPageScrollStateChanged");
            }
        });
    }
}
