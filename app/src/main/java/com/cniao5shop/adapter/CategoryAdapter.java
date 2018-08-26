package com.cniao5shop.adapter;

import android.content.Context;

import com.cniao5shop.R;
import com.cniao5shop.bean.Category;

import java.util.List;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/8/3
 * @Description: ${todo}
 */

public class CategoryAdapter extends SimpleAdapter<Category>{
    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Category item) {
        viewHolder.getTextView(R.id.textView).setText(item.getName());
    }
}
