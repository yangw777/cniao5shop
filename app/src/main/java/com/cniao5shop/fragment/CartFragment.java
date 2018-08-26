package com.cniao5shop.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.cniao5shop.MainActivity;
import com.cniao5shop.NewOrderActivity;
import com.cniao5shop.R;
import com.cniao5shop.adapter.CartAdapter;
import com.cniao5shop.adapter.decoration.DividerItemDecoration;
import com.cniao5shop.bean.ShoppingCart;
import com.cniao5shop.utils.CartProvider;
import com.cniao5shop.utils.ToastUtils;
import com.cniao5shop.widget.CnToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/7/20
 * @Description: ${todo}
 */

public class CartFragment extends BaseFragment{
    private static final String TAG = "CartFragment";

    public static final int ACTION_EDIT=1;
    public static final int ACTION_CAMPLATE=2;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    @ViewInject(R.id.toolbar)
    private CnToolbar mToolbar;

    private Button rightBtn;

    private CartAdapter mAdapter;
    private CartProvider cartProvider;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        ViewUtils.inject(this, view);

        cartProvider = new CartProvider(getContext());

        showData();


        return view;
    }

    @Override
    public void init() {
        cartProvider = new CartProvider(getActivity());

        changeToolbar();
        showData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
        mToolbar.setRightButtonOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int action = (int) v.getTag();
                if(ACTION_EDIT == action){
                    showDelControl();

                }else if(ACTION_CAMPLATE == action){
                    hideDelControl();
                }
            }
        });
    }

    @OnClick(R.id.btn_del)
    public void delCart(View view){
        mAdapter.delCart();
    }

    @OnClick(R.id.btn_order)
    public void toOrder(View view){

        Intent intent = new Intent(getActivity(), NewOrderActivity.class);

        startActivity(intent,true);
    }

    private void showData(){
        List<ShoppingCart> carts = cartProvider.getAll();

        mAdapter = new CartAdapter(getActivity(),carts,mCheckBox,mTextTotal);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    public void refData(){
        mAdapter.clearData();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
    }

   @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;

            mToolbar = activity.findViewById(R.id.toolbar);

            changeToolbar();

        }
    }

    public void changeToolbar(){
        mToolbar.hideSearchView();
        mToolbar.setTitle(R.string.cart);

        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");

        rightBtn = mToolbar.getRightButton();

 //       rightBtn.setOnClickListener(this);

        mToolbar.getRightButton().setTag(ACTION_EDIT);



    }

    private void showDelControl(){
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);
    }

    private void hideDelControl(){
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);


        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }

/*
    @Override
    public void onClick(View v) {
        Log.d("cartFragment", "---->onclick111111111");
        ToastUtils.show(getActivity(), "点击123456---->");
        int action = (int) v.getTag();
        if(ACTION_EDIT == action){
            showDelControl();

        }else if(ACTION_CAMPLATE == action){
            hideDelControl();
        }
    }*/
}
