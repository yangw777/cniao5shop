package com.cniao5shop.adapter;

import android.content.Context;

import java.util.List;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/8/1
 * @Description: ${todo}
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {

    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }


    public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }



}
