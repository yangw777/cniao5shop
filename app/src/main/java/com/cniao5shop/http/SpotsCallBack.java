package com.cniao5shop.http;

import android.content.Context;
import android.content.Intent;

import com.cniao5shop.CniaoApplication;
import com.cniao5shop.LoginActivity;
import com.cniao5shop.R;
import com.cniao5shop.utils.ToastUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import dmax.dialog.SpotsDialog;


/**
 * @author yangwan
 * @version V1.0
 * @Date 2018/7/27
 * @Description: ${todo}
 */

public abstract class SpotsCallBack<T> extends SimpleCallback<T> {

    SpotsDialog dialog;

    public SpotsCallBack(Context context){
        super(context);

        initSpotsDialog();
    }

    private  void initSpotsDialog(){

        dialog = new SpotsDialog(mContext,"拼命加载中...");
    }

    public void showDialog(){
        dialog.show();
    }

    public void dismissDialog(){
        if(null != dialog)
            dialog.dismiss();
    }

    public void setMessage(String message){
        dialog.setMessage(message);
    }

    @Override
    public void onBeforeRequest(Request request) {

        showDialog();
    }

    @Override
    public void onFailure(Request rquest, Exception e) {
        dismissDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, R.string.token_error);

        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        CniaoApplication.getInstance().clearUser();
    }
}
