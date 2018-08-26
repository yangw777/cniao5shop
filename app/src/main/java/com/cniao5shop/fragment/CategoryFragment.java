package com.cniao5shop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.cniao5shop.Contants;
import com.cniao5shop.R;
import com.cniao5shop.adapter.BaseAdapter;
import com.cniao5shop.adapter.CategoryAdapter;
import com.cniao5shop.adapter.WaresAdapter;
import com.cniao5shop.adapter.decoration.DividerGridItemDecoration;
import com.cniao5shop.bean.Banner;
import com.cniao5shop.bean.Category;
import com.cniao5shop.bean.Page;
import com.cniao5shop.bean.Wares;
import com.cniao5shop.http.BaseCallback;
import com.cniao5shop.http.OkHttpHelper;
import com.cniao5shop.http.SpotsCallBack;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/7/20
 * @Description: ${todo}
 */

public class CategoryFragment extends Fragment {

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.recyclerview_category_wares)
    private RecyclerView mRecyclerViewWares;

    @ViewInject(R.id.refreshlayout_category)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.sliderlayout_category)
    private SliderLayout mSliderLayout;

    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdapter;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    private int currPage =1;
    private int pageSize =10;
    private int totalPage =1;
    private long category_id =0;

    private static final int STATE_NORMAL=0;
    private static final int STATE_REFREH=1;
    private static final int STATE_MORE=2;

    private int state = STATE_NORMAL;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ViewUtils.inject(this, view);

        requestCategoryData();
        requestBannerData();

        initRefreshLayout();

        return view;

    }

    private void initRefreshLayout(){
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(currPage <= totalPage){
                    loadMoreData();
                }else{
                    Toast.makeText(getActivity(), "已到达最后一页。", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }

            }
        });
    }

    private void refreshData(){
        currPage = 1;
        state = STATE_REFREH;
        requestWares(category_id);
    }

    private void loadMoreData(){
        currPage += 1;
        state = STATE_MORE;

        requestWares(category_id);
    }

    private void requestCategoryData(){
        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()){
            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);

                if(categories !=null && categories.size()>0){
                    category_id = categories.get(0).getId();
                    requestWares(category_id);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showCategoryData(List<Category> categories){
        mCategoryAdapter = new CategoryAdapter(getContext(), categories);

        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Category category = mCategoryAdapter.getItem(position);

                category_id = category.getId();
                currPage = 1;
                state = STATE_NORMAL;

                requestWares(category_id);
            }
        });

        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void requestBannerData() {

        String url = Contants.API.BANNER + "?type=1";

        mHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                showSliderView(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void showSliderView(List<Banner> banners){
        if(null != banners){
            for(Banner banner : banners){
                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);

                mSliderLayout.addSlider(sliderView);
            }
        }



        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        mSliderLayout.setDuration(3000);

    }

    private void requestWares(long categoryId){
        String url = Contants.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage=" + currPage +"&pageSize=" + pageSize;

        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {
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
            public void onSuccess(Response response, Page<Wares> waresPage) {
                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();

                showWareData(waresPage.getList());
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }


    private void showWareData(List<Wares> wares){
        switch (state){
            case STATE_NORMAL:
                if(null == mWaresAdapter) {
                    mWaresAdapter = new WaresAdapter(getContext(), wares);
                    mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                        }
                    });

                    mRecyclerViewWares.setAdapter(mWaresAdapter);
                    mRecyclerViewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerViewWares.setItemAnimator(new DefaultItemAnimator());
//                mRecyclerViewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }else{
                    mWaresAdapter.clearData();
                    mWaresAdapter.addData(wares);
                }


                break;

            case STATE_REFREH:
                mWaresAdapter.clearData();
                mWaresAdapter.addData(wares);

                mRecyclerViewWares.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;

            case STATE_MORE:
                mWaresAdapter.addData(mWaresAdapter.getDatas().size(), wares);
                mRecyclerViewWares.scrollToPosition(mWaresAdapter.getDatas().size());

                mRefreshLayout.finishRefreshLoadMore();
                break;

            default:
        }


    }
}
