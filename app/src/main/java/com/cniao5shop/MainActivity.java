package com.cniao5shop;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.cniao5shop.bean.Tab;
import com.cniao5shop.fragment.CartFragment;
import com.cniao5shop.fragment.CategoryFragment;
import com.cniao5shop.fragment.HomeFragment;
import com.cniao5shop.fragment.HotFragment;
import com.cniao5shop.fragment.MineFragment;
import com.cniao5shop.widget.CnToolbar;
import com.cniao5shop.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private FragmentTabHost mTabhost;
    private LayoutInflater mInflater;

    private CnToolbar mToolbar;

    CartFragment cartFragment;

    private List<Tab> mTabs = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();
    }


    private void initTab() {
        Tab tab_home = new Tab(R.string.home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab tab_hot = new Tab(R.string.hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab tab_category = new Tab(R.string.catagory, R.drawable.selector_icon_category, CategoryFragment.class);
        Tab tab_cart = new Tab(R.string.cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab tab_mine = new Tab(R.string.mine, R.drawable.selector_icon_mine, MineFragment.class);


        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mInflater = LayoutInflater.from(this);
        mTabhost = findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for(Tab tab : mTabs){
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec, tab.getFragment(), null);
        }

        /**
         * 监听Tab切换
         */
        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId == getString(R.string.cart)){
                    refData();
//                    cartFragment.changeToolbar();

                }else{
                    if(null == mToolbar)
                        mToolbar = findViewById(R.id.toolbar);
                    mToolbar.showSearchView();
                    mToolbar.hideTitleView();
                    mToolbar.getRightButton().setVisibility(View.GONE);
                }
            }
        });

        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);
    }

    private void refData(){
        if(cartFragment == null){
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if(null != fragment){
                cartFragment = (CartFragment) fragment;
                cartFragment.refData();
            }

        }else{
            cartFragment.refData();
        }
    }

    private View buildIndicator(Tab tab){
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = view.findViewById(R.id.icon_tab);
        TextView text = view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return view;
    }
}
