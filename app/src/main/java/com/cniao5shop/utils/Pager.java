package com.cniao5shop.utils;

import android.content.Context;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.cniao5shop.bean.Page;
import com.cniao5shop.http.OkHttpHelper;
import com.cniao5shop.http.SpotsCallBack;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/8/10
 * @Description: ${todo}
 */

public class Pager {
    private static Builder builder;

    private OkHttpHelper httpHelper;

    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFRESH=1;
    private  static final int STATE_MORE=2;

    private int state=STATE_NORMAL;


    private Pager(){
        httpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    public static Builder newBuilder(){
        builder = new Builder();
        return builder;
    }

    public void request(){
        requestData();
    }

    public void  putParam(String key,Object value){
        builder.params.put(key,value);

    }


    private void initRefreshLayout(){

        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);

        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
                refresh();
            }


            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if(builder.pageIndex<builder.totalPage)
                    loadMore();
                else{
                    Toast.makeText(builder.mContext, "无更多数据", Toast.LENGTH_LONG).show();
                    materialRefreshLayout.finishRefreshLoadMore();
                    materialRefreshLayout.setLoadMore(false);
                }
            }
        });
    }

    /**
     * 刷新数据
     */
    private void refresh(){

        state=STATE_REFRESH;
        builder.pageIndex =1;
        requestData();
    }

    /**
     * 隐藏数据
     */
    private void loadMore(){

        state=STATE_MORE;
        builder.pageIndex =++builder.pageIndex;
        requestData();
    }

    /**
     * 构建URL
     * @return
     */
    private String buildUrl(){

        return builder.url +"?"+buildUrlParams();
    }

    private   String buildUrlParams() {

        HashMap<String, Object> map = builder.params;

        map.put("curPage",builder.pageIndex);
        map.put("pageSize",builder.pageSize);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0,s.length()-1);
        }
        return s;
    }

    /**
     * 请求数据
     */
    private void requestData(){


        String url = buildUrl();

        httpHelper.get(url, new RequestCallBack(builder.mContext));

    }

    /**
     * 显示数据
     * @param datas 数据
     * @param totalPage 总页数
     * @param totalCount 总数
     * @param <T> 数据类型集合
     */
    private <T> void showData(List<T> datas, int totalPage, int totalCount) {
        switch (state) {
            case STATE_NORMAL:

                if (builder.onPageListener != null) {
                    builder.onPageListener.load(datas, totalPage, totalCount);
                }
                break;
            case STATE_MORE://加载更多

                if (builder.onPageListener != null) {
                    builder.onPageListener.loadMore(datas, totalPage, totalCount);
                }
                builder.mRefreshLayout.finishRefreshLoadMore();
                break;
            case STATE_REFRESH://刷新

                if (builder.onPageListener != null) {
                    builder.onPageListener.refresh(datas, totalPage, totalCount);
                }
                builder.mRefreshLayout.finishRefresh();
                break;
            default:
                if (datas == null || datas.size() <=0){
                    ToastUtils.show(builder.mContext,"无法加载到数据");
                }
                break;
        }

    }




    public static class Builder{

        private String url;

        private  Type mType;

        private MaterialRefreshLayout mRefreshLayout;
        private boolean canLoadMore;

        private OnPageListener onPageListener;

        private int totalPage = 1;
        private int pageIndex = 1;
        private int pageSize = 10;

        private HashMap<String, Object> params = new HashMap<>(5);

        private Context mContext;

        public Builder setUrl(String url){
            this.url = url;
            return builder;
        }

        public Builder putParam(String key, Object value){
            params.put(key, value);
            return  builder;
        }

        public Builder setPageSize(int pageSize){
            this.pageSize = pageSize;
            return builder;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout){

            this.mRefreshLayout = refreshLayout;
            return builder;
        }

        public Builder setLoadMore(boolean loadMore){
            this.canLoadMore = loadMore;
            return builder;
        }

        public Builder setOnPageListener(OnPageListener onPageListener) {
            this.onPageListener = onPageListener;
            return builder;
        }

        public Pager build(Context context, Type type){
            this.mType = type;
            this.mContext = context;

            valid();

            return new Pager();
        }

        private void valid(){


            if(this.mContext==null)
                throw  new RuntimeException("content can't be null");

            if(this.url==null || "".equals(this.url))
                throw  new RuntimeException("url can't be  null");

            if(this.mRefreshLayout==null)
                throw  new RuntimeException("MaterialRefreshLayout can't be  null");
        }
    }

    class  RequestCallBack<T> extends SpotsCallBack<Page<T>> {

        public RequestCallBack(Context context) {
            super(context);

            super.mType = builder.mType;
        }

        @Override
        public void onFailure(Request request, Exception e) {

            dismissDialog();
            Toast.makeText(builder.mContext,"请求出错："+e.getMessage(),Toast.LENGTH_LONG).show();

            if(STATE_REFRESH==state)   {
                builder.mRefreshLayout.finishRefresh();
            }
            else  if(STATE_MORE == state){

                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }

        @Override
        public void onSuccess(Response response, Page<T> page) {


            builder.pageIndex = page.getCurrentPage();
            builder.pageSize = page.getPageSize();
            builder.totalPage = page.getTotalPage();

            showData(page.getList(),page.getTotalPage(),page.getTotalCount());
        }


        @Override
        public void onError(Response response, int code, Exception e) {

            Toast.makeText(builder.mContext,"加载数据失败",Toast.LENGTH_LONG).show();

            if(STATE_REFRESH==state)   {
                builder.mRefreshLayout.finishRefresh();
            }
            else  if(STATE_MORE == state){

                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }
    }

    public interface  OnPageListener<T>{

        void load(List<T> datas, int totalPage, int totalCount);

        void refresh(List<T> datas, int totalPage, int totalCount);

        void loadMore(List<T> datas, int totalPage, int totalCount);


    }
}
