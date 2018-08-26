package com.cniao5shop.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/8/1
 * @Description: ${todo}
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    protected BaseAdapter.OnItemClickListener listener;

    private SparseArray<View> views;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);

        views = new SparseArray<>();

        this.listener = listener;
        itemView.setOnClickListener(this);
    }



    public View getView(int id){
        return findView(id);
    }

    public TextView getTextView(int id){
        return findView(id);
    }

    public ImageView getImageView(int id){
        return findView(id);
    }

    public Button getButton(int id){
        return findView(id);
    }

    private <T extends View> T findView(int id){
        View view = views.get(id);
        if(null == view){
            view = itemView.findViewById(id);
            views.put(id, view);
        }

        return (T) view;
    }

    @Override
    public void onClick(View v) {
        if(null != listener){
            listener.onItemClick(v, getLayoutPosition());
        }
    }
}
