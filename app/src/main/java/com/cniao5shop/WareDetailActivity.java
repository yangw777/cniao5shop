package com.cniao5shop;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cniao5shop.bean.Wares;
import com.cniao5shop.utils.CartProvider;
import com.cniao5shop.utils.ToastUtils;
import com.cniao5shop.widget.CnToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import dmax.dialog.SpotsDialog;

public class WareDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.webView)
    private WebView mWebView;

    @ViewInject(R.id.toolbar)
    private CnToolbar mToolbar;

    private Wares mWare;

    private WebAppInterface mAppInterface;

    private CartProvider cartProvider;

    private SpotsDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);

        ViewUtils.inject(this);

        Serializable serializable = getIntent().getSerializableExtra(Contants.WARE);
        if(null == serializable)
            this.finish();

        mDialog = new SpotsDialog(this, "loading......");
        mDialog.show();

        mWare = (Wares) serializable;
        cartProvider = new CartProvider(this);

        initToolBar();
        initWebView();
    }

    private void initWebView(){
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setAppCacheEnabled(true);

        mWebView.loadUrl(Contants.API.WARES_DETAIL);

        mAppInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(mAppInterface, "appInterface");

        mWebView.setWebViewClient(new WC());
    }

    private void initToolBar(){
        mToolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }

    class WC extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if(mDialog != null && mDialog.isShowing() )
                mDialog.dismiss();

            mAppInterface.showDetail();
        }
    }

    class WebAppInterface{
        private Context mContext;

        public WebAppInterface(Context context){
            this.mContext = context;
        }

        @JavascriptInterface
        public void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showDetail("+mWare.getId()+")");
                }
            });
        }

        @JavascriptInterface
        public void buy(long id){
            cartProvider.put(mWare);
            ToastUtils.show(mContext, "已添加到购物车");
        }

        @JavascriptInterface
        public void addFavorites(long id){}
    }
}
