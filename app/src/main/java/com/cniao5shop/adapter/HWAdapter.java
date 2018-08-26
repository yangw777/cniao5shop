package com.cniao5shop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.cniao5shop.R;
import com.cniao5shop.bean.ShoppingCart;
import com.cniao5shop.bean.Wares;
import com.cniao5shop.utils.CartProvider;
import com.cniao5shop.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/8/1
 * @Description: ${todo}
 */

public class HWAdapter extends SimpleAdapter<Wares> {
    CartProvider provider;

    public HWAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);

        provider = new CartProvider(context);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, final Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥ "+wares.getPrice());

        Button button = viewHolder.getButton(R.id.btn_add);
        if(null != button) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                ShoppingCart cart = (ShoppingCart) wares;

                    provider.put(wares);

                    ToastUtils.show(mContext, "已添加到购物车");

                }
            });
        }
    }

    public ShoppingCart convertData(Wares item){
        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }

    /**
     * 设置布局
     * @param layoutId
     */
    public void reSetLayout(int layoutId){
        this.mLayoutResId = layoutId;

        notifyItemRangeChanged(0, getDatas().size());
    }
}
