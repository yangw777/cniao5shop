package com.cniao5shop.adapter;

import android.content.Context;
import android.net.Uri;

import com.cniao5shop.R;
import com.cniao5shop.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/8/3
 * @Description: ${todo}
 */

public class WaresAdapter extends SimpleAdapter<Wares> {
    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_grid_wares, datas);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Wares item) {
        viewHolder.getTextView(R.id.text_title).setText(item.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

    }
}
