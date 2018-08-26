package com.cniao5shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.cniao5shop.Contants;
import com.cniao5shop.R;
import com.cniao5shop.WareDetailActivity;
import com.cniao5shop.adapter.BaseAdapter;
import com.cniao5shop.adapter.HWAdapter;
import com.cniao5shop.adapter.decoration.DividerItemDecoration;
import com.cniao5shop.bean.Page;
import com.cniao5shop.bean.Wares;
import com.cniao5shop.http.OkHttpHelper;
import com.cniao5shop.http.SpotsCallBack;
import com.cniao5shop.utils.Pager;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.List;


/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/7/20
 * @Description: ${todo}
 */

public class HotFragment extends Fragment {
/*

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
    private int currPage =1;
    private int pageSize =10;
    private int totalPage =1;

    private List<Wares> datas;
*/

    private HWAdapter mAdapter;


    @ViewInject(R.id.recyclerview_hot)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.refreshlayout_hot)
    private MaterialRefreshLayout mRefreshLayout;
/*
    private static final int STATE_NORMAL=0;
    private static final int STATE_REFREH=1;
    private static final int STATE_MORE=2;

    private int state = STATE_NORMAL;*/


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerview_hot);
        mRefreshLayout = view.findViewById(R.id.refreshlayout_hot);

        ViewUtils.inject(view);

/*
        initRefreshLayout();

        getData();
        */



        Pager pager = Pager.newBuilder()
                                    .setUrl(Contants.API.WARES_HOT)
                                    .setLoadMore(true)
                                    .setOnPageListener(new Pager.OnPageListener(){

                                        @Override
                                        public void load(List datas, int totalPage, int totalCount) {
                                            mAdapter = new HWAdapter(getContext(), datas);
                                            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View view, int position) {
                                                    Wares wares = mAdapter.getItem(position);
                                                    Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                                                    intent.putExtra(Contants.WARE, wares);
                                                    startActivity(intent);

                                                }
                                            });

                                            mRecyclerView.setAdapter(mAdapter);
                                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

                                        }

                                        @Override
                                        public void refresh(List datas, int totalPage, int totalCount) {
                                            mAdapter.clearData();
                                            mAdapter.addData(datas);

                                            mRecyclerView.scrollToPosition(0);
//                                            mRefreshLayout.finishRefresh();
                                        }

                                        @Override
                                        public void loadMore(List datas, int totalPage, int totalCount) {
                                            mAdapter.addData(mAdapter.getDatas().size(), datas);
                                            mRecyclerView.scrollToPosition(mAdapter.getDatas().size());

//                                            mRefreshLayout.finishRefreshLoadMore();

                                        }
                                    }).setPageSize(10)
                                    .setRefreshLayout(mRefreshLayout).build(getContext(), new TypeToken<Page<Wares>>(){}.getType());

        pager.request();


        return view;

    }
/*
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
    }*/
/*
    private void refreshData(){
        currPage = 1;
        state = STATE_REFREH;
        getData();
    }

    private void loadMoreData(){
        currPage += 1;
        state = STATE_MORE;

        getData();
    }

    private void getData(){
        String url = Contants.API.WARES_HOT + "?curPage=" + currPage + "&pageSize=" + pageSize;
        Log.d("getData-->", url);
        httpHelper.get(url, new SpotsCallBack<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                datas = waresPage.getList();
                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();

                showData();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }*/

/*
    private void showData(){
        switch (state){
            case STATE_NORMAL:
                mAdapter = new HWAdapter(getContext(), datas);
                mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });

                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));


                break;

            case STATE_REFREH:
                mAdapter.clearData();
                mAdapter.addData(datas);

                mRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();

                break;

            case STATE_MORE:
                mAdapter.addData(mAdapter.getDatas().size(), datas);
                mRecyclerView.scrollToPosition(mAdapter.getDatas().size());

                mRefreshLayout.finishRefreshLoadMore();

                break;

            default:
        }


    } */
}
