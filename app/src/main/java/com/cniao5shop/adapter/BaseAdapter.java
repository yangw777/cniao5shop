package com.cniao5shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cniao5shop.bean.Wares;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/8/1
 * @Description: ${todo}
 */

public abstract class BaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected Context mContext;

    protected int mLayoutResId;

    protected OnItemClickListener listener;


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public BaseAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseAdapter(Context context, int layoutResId, List<T> datas){
        this.mContext = context;
        this.mDatas = datas == null ? new ArrayList<T>() : datas;
        this.mLayoutResId = layoutResId;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(mLayoutResId, parent, false);

        return new BaseViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T t = getItem(position);
        bindData((H)holder, t);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public T getItem(int position){
        if (position >= mDatas.size())
            return null;
        return mDatas.get(position);
    }

    public List<T> getDatas(){
        return mDatas;
    }

    public void clearData() {

        if (mDatas == null || mDatas.size() <= 0)
            return;

        for (Iterator it = mDatas.iterator(); it.hasNext(); ) {

            T t = (T) it.next();
            int position = mDatas.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }

    }

    public void addData(List<T> datas){
        addData(0, datas);
    }

    public void addData(int position, List<T> datas){
        if(datas != null && datas.size()>0 && position < datas.size()){
            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }

    }

    /**
     * 刷新数据
     * @param list
     */
    public void refreshData(List<T> list) {
        if (list != null && list.size() > 0) {
            clearData();

            int size = list.size();

            for (int i = 0; i < size; i++) {
                mDatas.add(i, list.get(i));
                notifyItemInserted(i);
            }
        }
    }

    /**
     * 加载更多
     * @param list
     */
    public void loadMoreData(List<T> list){

        if(list !=null && list.size()>0){

            int size = list.size();
            int begin = mDatas.size();
            for (int i=0;i<size;i++){
                mDatas.add(list.get(i));
                notifyItemInserted(i+begin);
            }

        }

    }

    public abstract void bindData(BaseViewHolder viewHolder, T t);
}
